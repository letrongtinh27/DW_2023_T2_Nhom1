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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Transform{
    private void transform() {
        final String cnError = "Cannot connected! ";
        final String executeError = "Cannot Execute Query! ";
        String currentEmail = "tinhle2772002@gmail.com";
        LocalDate currentDate = LocalDate.now();
        try {
            // Kết nối control.db
            DatabaseConn dbc = new DatabaseConn();
            dbc.connectToControl();

            if(dbc.getControlConn()==null) {
                SendMail.sendEmail(currentEmail, cnError + currentDate, "Cannot connected to Control");
                return;
            }
            // Lấy dữ liệu bảng configs có flag = 1 && status = CRAWL_COMPLETED
            String selectConfig = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_SELECT_CONFIG");
            selectConfig = selectConfig.replace("?" , "'EXTRACT_COMPLETED'");
            List<Map<String, Object>> configs = dbc.query(selectConfig);
            for (Map<String, Object> config: configs) { // Lặp qua từng cấu hình
                String id = config.get("id").toString();
                currentEmail = dbc.getEmail(id);
                // Kết nối tới staging.db
                dbc.connectToStaging();
                Connection staging = dbc.getStagingConn();
                if(staging==null) {
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot connect to staging", "transform_script" );
                    SendMail.sendEmail(currentEmail, cnError + currentDate, "Cannot connected to Staging");
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    continue;
                }
                // Câp nhật status trong config=TRANSFORM_START
                dbc.updateStatusConfig("TRANSFORM_START", id);
                // Chạy Procedure Transform
                try {
                    dbc.callTransform();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot execute PROCEDURE Transform " + e.getMessage(), "transform_script" );
                    SendMail.sendEmail(currentEmail, executeError + currentDate, "Cannot execute PROCEDURE Transform in staging " + e.getMessage());
                    continue;
                }
                // Chạy SQL thêm dữ liệu weather_dim
                String updateWeatherStatus_dim = dbc.readQueryFromFile("document/query.sql", "-- #QUERY_UPDATE_WEATHERSTATUS_DIM");
                try {
                    PreparedStatement ps = staging.prepareStatement(updateWeatherStatus_dim);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    dbc.updateStatusConfig("TRANSFORM_ERROR", id);
                    dbc.log(id, "Log of transform", "TRANSFORM ERROR", "Cannot execute sql insert weatherstatus_dim " + e.getMessage(), "transform_script" );
                    SendMail.sendEmail(currentEmail, executeError + currentDate, "Cannot execute insert weatherstatus_dim query in warehouse " + e.getMessage());
                    continue;
                }
                // Câp nhật status trong config=TRANSFORM_COMPLETED
                dbc.updateStatusConfig("TRANSFORM_COMPLETED", id);
                dbc.log(id, "Log of transform", "TRANSFORM COMPLETED", "Transform done!", "transform_script");
                SendMail.sendEmail(currentEmail, "Warehouse TRANSFORM COMPLETED! " + currentDate, "Transform done in config_id: " + config.get("id").toString());
                // Đóng kết nối staging.db
                dbc.closeStaging();
            }
            // Đóng kết nối control.db
            dbc.closeControl();
        } catch (Exception e ){
            SendMail.sendEmail(currentEmail, cnError + currentDate, "Transform error: " + e.getMessage());
        }
    }

    public void start() {
        transform();
    }
}
