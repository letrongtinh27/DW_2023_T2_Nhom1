package vn.edu.hcmuaf.fit.etl;

import org.apache.ibatis.jdbc.ScriptRunner;
import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.util.SendMail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class Transform extends Thread {
    private void transform() {
        try {
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();

            final String cnError = "Cannot connected!";
            final String executeError = "Cannot Execute Query!";

            if(dbc.getControlConn()==null) {
                SendMail.sendEmail("", cnError, "Cannot connected to Control");
            }
            // Lấy dữ liệu bảng configs có flag = 1 && status = CRAWL_COMPLETED
            String selectConfig = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'CRAWL_COMPLETED'");
            System.out.println(selectConfig);
            List<Map<String, Object>> configs = dbc.query(selectConfig);
            for (Map<String, Object> config: configs) { // Lặp qua từng cấu hình
                String id = config.get("id").toString();
                // Lấy log của từng cấu hình
                String getLog = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_LOGS");
                getLog = getLog.replace("?", config.get("id").toString());
                Map<String, Object> log = dbc.query(getLog).get(0);
                // Kết nối tới staging.db
                dbc.connectToStaging();
                Connection staging = dbc.getStagingConn();
                if(staging==null) {
                    dbc.updateLog(log.get("id").toString(), cnError, "Cannot connect to staging", "transform_script");
                    SendMail.sendEmail("", cnError, "Cannot connected to Staging");
                }
                // Câp nhật status trong config=TRANSFORM_START
                dbc.updateStatusConfig("TRANSFORM_START", id);
                // Chạy các file SQL chuẩn hóa dữ liệu (transform.sql)
                ScriptRunner rs = new ScriptRunner(staging);
                Reader reader;
                try {
                    reader = new BufferedReader(new FileReader("document/transform.sql"));
                    rs.setAutoCommit(true);
                    rs.setStopOnError(true);
                    rs.runScript(reader);
                } catch (IOException e) {
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    dbc.updateLog(log.get("id").toString(), "File transform.sql error", "Cannot execute transform.sql " + e.getMessage(), "transform_script");
                    SendMail.sendEmail("", executeError, "Cannot execute file transform.sql in config " + config.toString());
                    return;
                }
                // Chạy SQL thêm dữ liệu weather_dim
                String updateWeather_dim = dbc.readQueryFromFile("document/update_query.sql", "-- #QUERY_UPDATE_WEATHER_DIM");
                try {
                    assert staging != null;
                    PreparedStatement ps = staging.prepareStatement(updateWeather_dim);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    dbc.updateLog(log.get("id").toString(), "File update_query error", "Cannot execute sql insert weather_dim " + e.getMessage(), "transform_script");
                    SendMail.sendEmail("", executeError, "Cannot execute insert weather_dim query in config " + config.toString());
                    return;
                }
                // Câp nhật status trong config=TRANSFORM_COMPLETED
                dbc.updateStatusConfig("TRANSFORM_COMPLETED", id);
                dbc.updateLog(log.get("id").toString(), "Transform completed", "Transform done! ", "transform_script");
                // Đóng kết nối staging.db
                dbc.closeStaging();
            }
            // Đóng kết nối control.db
            dbc.closeControl();
        } catch (Exception e ){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        transform();
    }
}
