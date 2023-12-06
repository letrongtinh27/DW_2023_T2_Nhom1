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


public class Transform{
    private void transform() {
        try {
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();

            final String cnError = "Cannot connected!";
            final String executeError = "Cannot Execute Query!";

            String currentEmail = "tinhle2772002@gmail.com";

            if(dbc.getControlConn()==null) {
                SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Control");
                return;
            }
            // Lấy dữ liệu bảng configs có flag = 1 && status = CRAWL_COMPLETED
            String selectConfig = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'CRAWL_COMPLETED'");
            List<Map<String, Object>> configs = dbc.query(selectConfig);
            for (Map<String, Object> config: configs) { // Lặp qua từng cấu hình
                String id = config.get("id").toString();
                currentEmail = dbc.getEmail(id);
                // Kết nối tới staging.db
                dbc.connectToStaging();
                Connection staging = dbc.getStagingConn();
                if(staging==null) {
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot connect to staging", "transform_script" );
                    SendMail.sendEmail(currentEmail, cnError, "Cannot connected to Staging");
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    return;
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
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot execute transform.sql " + e.getMessage(), "transform_script" );
                    SendMail.sendEmail(currentEmail, executeError, "Cannot execute file transform.sql in config " + e.getMessage());
                    return;
                }
                // Chạy SQL thêm dữ liệu weather_dim
                String updateWeather_dim = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_UPDATE_WEATHER_DIM");
                try {
                    PreparedStatement ps = staging.prepareStatement(updateWeather_dim);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot execute sql insert weather_dim " + e.getMessage(), "transform_script" );
                    SendMail.sendEmail(currentEmail, executeError, "Cannot execute insert weather_dim query in config " + e.getMessage());
                    return;
                }
                // Câp nhật status trong config=TRANSFORM_COMPLETED
                dbc.updateStatusConfig("TRANSFORM_COMPLETED", id);
                dbc.log(id, "Log of transform", "TRANSFORM COMPLETED", "Transform done!", "transform_script");
                SendMail.sendEmail(currentEmail, "Warehouse TRANSFORM COMPLETED!", "Transform done in config_id: " + config.get("id").toString());
                // Đóng kết nối staging.db
                dbc.closeStaging();
            }
            // Đóng kết nối control.db
            dbc.closeControl();
        } catch (Exception e ){
            throw new RuntimeException(e);
        }
    }

    public void start() {
        transform();
    }

    public static void main(String[] args) {
        new Transform().start();
    }
}
