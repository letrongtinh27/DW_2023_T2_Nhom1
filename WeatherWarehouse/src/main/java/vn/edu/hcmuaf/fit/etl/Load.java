package vn.edu.hcmuaf.fit.etl;

import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.util.SendMail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Load {
    public void loadStagingToWarehouse() {
        final String cnError = "Cannot connected!";
        final String executeError = "Cannot Execute Query!";
        String currentEmail = "tinhle2772002@gmail.com";
        try{
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();
            if(dbc.getControlConn() == null) {
                SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Control");
                return;
            }
            String selectConfig = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'TRANSFORM_COMPLETED'");
            List<Map<String, Object>> configs = dbc.query(selectConfig);

            for ( Map<String, Object> config : configs) {
                String id = config.get("id").toString();
                currentEmail = dbc.getEmail(id);
                // Kết nối tới staging.db
                dbc.connectToStaging();
                Connection staging = dbc.getStagingConn();
                if(staging==null) {
                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot connect to staging", "load_script");
                    dbc.updateStatusConfig("LOAD_WAREHOUSE_ERROR", id);
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Staging");
                    continue;
                }
                // Kết nối tới warehouse.db
                dbc.connectToWarehouse();
                Connection warehouse = dbc.getWarehouseConn();
                if(warehouse==null) {
                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot connect to warehouse", "load_script");
                    dbc.updateStatusConfig("LOAD_WAREHOUSE_ERROR", id);
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Warehouse");
                    continue;
                }
                // Câp nhật status trong config=LOAD_START
                dbc.updateStatusConfig("LOAD_WAREHOUSE_START", id);

                // Chạy Procedure LoadStagingToWarehouse
                try {
                    dbc.callLoadStagingToWarehouse();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("LOAD_WAREHOUSE_ERROR", id);
                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot call PROCEDURE LoadStagingToWarehouse in staging " + e.getMessage(), "load_script");
                    SendMail.sendEmail(currentEmail, executeError, "Cannot call PROCEDURE LoadStagingToWarehouse in staging " + e.getMessage());
                    continue;
                }
                // Câp nhật status trong config=LOAD_COMPLETED
                dbc.updateStatusConfig("LOAD_WAREHOUSE_COMPLETED", id);
                dbc.log(id, "Log of load", "LOAD WAREHOUSE COMPLETED", "Load staging to warehouse done!", "Load_script");
                SendMail.sendEmail(currentEmail, "LOAD STAGING TO WAREHOUSE COMPLETED!", "Load done in config_id: " + config.get("id").toString());
            }
            dbc.closeControl();
            dbc.closeStaging();
            dbc.closeWarehouse();
        } catch (Exception e) {
            SendMail.sendEmail(currentEmail, executeError, new RuntimeException(e).toString());
        }
    }

    public void loadFactToAggregate() {
        final String cnError = "Cannot connected!";
        final String executeError = "Cannot Execute Query!";
        String currentEmail = "tinhle2772002@gmail.com";
        try {
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();

            if(dbc.getControlConn() == null) {
                SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Control");
                return;
            }
            String selectConfig = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'LOAD_WAREHOUSE_COMPLETED'");
            List<Map<String, Object>> configs = dbc.query(selectConfig);

            for ( Map<String, Object> config : configs) {
                String id = config.get("id").toString();
                currentEmail = dbc.getEmail(id);
                // Kết nối tới warehouse.db
                dbc.connectToWarehouse();
                Connection warehouse = dbc.getWarehouseConn();
                if(warehouse==null) {
                    dbc.updateStatusConfig("LOAD_AGGREGATE_ERROR", id);
                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot connect to warehouse", "load_script");
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Warehouse");
                    continue;
                }
                // Câp nhật status trong config=LOAD_START
                dbc.updateStatusConfig("LOAD_AGGREGATE_START", id);
                // Chạy Procedure LoadFactToAggregate
                try {
                    dbc.callLoadFactToAggregate();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("LOAD_AGGREGATE_ERROR", id);
                    System.out.println("LOAD_AGGREGATE_ERROR");

                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot call PROCEDURE LoadFactToAggregate in warehouse", "load_script");
                    SendMail.sendEmail(currentEmail, executeError, "Cannot call PROCEDURE LoadFactToAggregate in warehouse" + "\n Exception: " + e.getMessage());
                    continue;
                }
                // Câp nhật status trong config=LOAD_COMPLETED
                dbc.updateStatusConfig("LOAD_AGGREGATE_COMPLETED", id);
                System.out.println("LOAD_AGGREGATE_COMPLETED");

                dbc.log(id, "Log of load", "LOAD AGGREGATE COMPLETE", "Load warehouse to aggregate done!", "Load_script");
                SendMail.sendEmail(currentEmail, "LOAD WAREHOUSE TO AGGREGATE COMPLETED!", "Load done in config_id: " + config.get("id").toString());
            }
            // Đóng kết nối
            dbc.closeControl();
            dbc.closeWarehouse();
        } catch (Exception e) {
            SendMail.sendEmail(currentEmail, executeError, new RuntimeException(e).toString());
        }
    }

    public void start() {
        loadStagingToWarehouse();
    }

    public static void main(String[] args) {
        new Load().start();
    }
}
