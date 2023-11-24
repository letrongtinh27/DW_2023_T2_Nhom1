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
        try{
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();

            final String cnError = "Cannot connected!";
            final String executeError = "Cannot Execute Query!";

            if(dbc.getControlConn() == null) {
                SendMail.sendEmail("", cnError, "Cannot connected to Control");
            }
            String selectConfig = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'TRANSFORM_COMPLETED'");
            List<Map<String, Object>> configs = dbc.query(selectConfig);

            for ( Map<String, Object> config : configs) {
                String id = config.get("id").toString();
                // Lấy log của từng cấu hình
                String getLog = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_LOGS");
                getLog = getLog.replace("?", config.get("id").toString());
                Map<String, Object> log = dbc.query(getLog).get(0);
                // Kết nối tới staging.db
                dbc.connectToStaging();
                Connection staging = dbc.getStagingConn();
                if(staging==null) {
                    dbc.updateLog(log.get("id").toString(), cnError, "Cannot connect to staging", "load_script");
                    SendMail.sendEmail("", cnError, "Cannot connected to Staging");
                }
                // Kết nối tới warehouse.db
                dbc.connectToWarehouse();
                Connection warehouse = dbc.getWarehouseConn();
                if(warehouse==null) {
                    dbc.updateLog(log.get("id").toString(), cnError, "Cannot connect to staging", "load_script");
                    SendMail.sendEmail("", cnError, "Cannot connected to Staging");
                }
                // Câp nhật status trong config=LOAD_START
                dbc.updateStatusConfig("LOAD_START", id);
                // Chạy file SQL load dữ liệu từ bảng staging vào bảng weather_data (load_staging_to_warehouse.sql)
                ScriptRunner rs = new ScriptRunner(staging);
                Reader reader;
                try {
                    reader = new BufferedReader(new FileReader("document/load_staging_to_warehouse.sql"));
                    rs.setAutoCommit(true);
                    rs.setStopOnError(true);
                    rs.runScript(reader);
                } catch (IOException e) {
                    dbc.updateStatusConfig("LOAD_ERROR", id);
                    dbc.updateLog(log.get("id").toString(), "File load_staging_to_warehouse.sql error", "Cannot execute load_staging_to_warehouse.sql " + e.getMessage(), "load_script");
                    SendMail.sendEmail("", executeError, "Cannot execute file load_staging_to_warehouse.sql in config " + config.toString());
                    return;
                }
                // Câp nhật status trong config=LOAD_COMPLETED
                dbc.updateStatusConfig("LOAD_COMPLETED", id);
            }
            dbc.closeControl();
            dbc.closeStaging();
            dbc.closeWarehouse();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
