package vn.edu.hcmuaf.fit.etl;

import org.apache.ibatis.jdbc.ScriptRunner;
import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.util.SendMail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
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
            String selectConfig = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_CONFIG");
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
                    System.out.println("LOAD_WAREHOUSE_ERROR");
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Staging");
                    return;
                }
                // Kết nối tới warehouse.db
                dbc.connectToWarehouse();
                Connection warehouse = dbc.getWarehouseConn();
                if(warehouse==null) {
                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot connect to warehouse", "load_script");
                    dbc.updateStatusConfig("LOAD_WAREHOUSE_ERROR", id);
                    System.out.println("LOAD_WAREHOUSE_ERROR");

                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Warehouse");
                    return;
                }
                // Câp nhật status trong config=LOAD_START
                dbc.updateStatusConfig("LOAD_WAREHOUSE_START", id);
                System.out.println("LOAD_WAREHOUSE_START");

                // Chạy file SQL load dữ liệu từ bảng staging vào bảng weather_data (load_staging_to_warehouse.sql)
                ScriptRunner rs = new ScriptRunner(staging);
                Reader reader;
                try {
                    reader = new BufferedReader(new FileReader("document/load_staging_to_warehouse.sql"));
                    rs.setAutoCommit(true);
                    rs.setStopOnError(true);
                    rs.runScript(reader);
                } catch (IOException e) {
                    dbc.updateStatusConfig("LOAD_WAREHOUSE_ERROR", id);
                    System.out.println("LOAD_WAREHOUSE_ERROR");

                    dbc.log(id, "Log of load", "LOAD ERROR", "File load_staging_to_warehouse.sql error " + e.getMessage(), "load_script");
                    SendMail.sendEmail(currentEmail, executeError, "Cannot execute file load_staging_to_warehouse.sql in config " + e.getMessage());
                    return;
                }
                // Câp nhật status trong config=LOAD_COMPLETED
                dbc.updateStatusConfig("LOAD_WAREHOUSE_COMPLETED", id);
                System.out.println("LOAD_WAREHOUSE_COMPLETED");

                dbc.log(id, "Log of load", "LOAD WAREHOUSE COMPLETE", "Load staging to warehouse done!", "Load_script");
                SendMail.sendEmail(currentEmail, "LOAD STAGING TO WAREHOUSE COMPLETED!", "Load done in config_id: " + config.get("id").toString());
            }
            dbc.closeControl();
            dbc.closeStaging();
            dbc.closeWarehouse();
        } catch (Exception e) {
            SendMail.sendEmail(currentEmail, executeError, new RuntimeException(e).toString());
        }
    }

    public void loadWarehouseToAggregate() {
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
            String selectConfig = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_CONFIG");
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
                    System.out.println("LOAD_AGGREGATE_ERROR");

                    dbc.log(id, "Log of load", "LOAD ERROR", "Cannot connect to warehouse", "load_script");
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Warehouse");
                    return;
                }
                // Câp nhật status trong config=LOAD_START
                dbc.updateStatusConfig("LOAD_AGGREGATE_START", id);
                System.out.println("LOAD_AGGREGATE_START");

                // Chạy file sql load weather_data sang aggregate (load_warehouse_to_aggregate.sql)
                ScriptRunner rs = new ScriptRunner(warehouse);
                Reader reader;
                try {
                    reader = new BufferedReader(new FileReader("document/load_warehouse_to_aggregate.sql"));
                    rs.setAutoCommit(true);
                    rs.setStopOnError(true);
                    rs.runScript(reader);
                } catch (IOException e) {
                    dbc.updateStatusConfig("LOAD_AGGREGATE_ERROR", id);
                    System.out.println("LOAD_AGGREGATE_ERROR");

                    dbc.log(id, "Log of load", "LOAD ERROR", "File load_warehouse_to_aggregate.sql error", "load_script");
                    SendMail.sendEmail(currentEmail, executeError, "Cannot execute file load_warehouse_to_aggregate.sql in config " + config.toString() + "\n Exception: " + e.getMessage());
                    return;
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
        loadWarehouseToAggregate();
    }

    public static void main(String[] args) {
        new Load().start();

    }
}
