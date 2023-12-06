package vn.edu.hcmuaf.fit.etl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.util.SendMail;

public class Extract {
        public void extract() {
            try {
                LocalDate currentDate = LocalDate.now();
                DatabaseConn connection = new DatabaseConn();
                DatabaseConn connection1 = new DatabaseConn();
                connection.connectToControl();
                // kết nối db control
                Connection control  = connection.getControlConn();
                // Không kết nối được thì gửi mail
                if(control == null) {
                    SendMail.sendEmail("20130266@st.hcmuaf.edu.vn","Error" ,"Can not connect ControlDB");
                }
                // lấy ra config theo status crawled
                String getConfig = connection.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_CONFIG");
                getConfig = getConfig.replace("?", "'CRAWLED'");

                List<Map<String, Object>> listConfig = connection.query(getConfig);
                for (Map<String, Object> config : listConfig) {
                    String id = config.get("id").toString();
                    System.out.println(id);
                    // Cập nhật status
                    connection.updateStatusConfig("EXTRACT",id);

                    // Kết nối db staging
                    connection1.connectToStaging();
                    Connection staging  = connection1.getStagingConn();

                    if(staging == null) {
                        // Gửi mail và báo lỗi vào Log
                        connection.updateStatusConfig("EXTRACT_ERR",id);
                        SendMail.sendEmail("20130266@st.hcmuaf.edu.vn","Error" ,"Can not connect Staging DB");
                        connection.log(id, "weather", "EXTRACT_ERROR", "Cannot connect Staging db", "extracter");
//
                    }

                    try (FileInputStream excelFile = new FileInputStream(config.get("source_path").toString() + config.get("location").toString() + currentDate + config.get("format").toString());
                         Workbook workbook = new XSSFWorkbook(excelFile);) {

                        Sheet sheet = workbook.getSheetAt(0);
                        Iterator<Row> iterator = sheet.iterator();
                        iterator.next();
                        // Truncate table staging
                        connection.Truncate_staging();

                        while (iterator.hasNext()) {
                            Row currentRow = iterator.next();
                            String date = currentRow.getCell(0).getStringCellValue();
                            String location = currentRow.getCell(1).getStringCellValue();
                            String status = currentRow.getCell(2).getStringCellValue();
                            String high = currentRow.getCell(3).getStringCellValue();
                            String low = currentRow.getCell(4).getStringCellValue();
                            String humidity = currentRow.getCell(5).getStringCellValue();
                            String precipitation = currentRow.getCell(6).getStringCellValue();
                            String average_temp = currentRow.getCell(7).getStringCellValue();
                            String day = currentRow.getCell(8).getStringCellValue();
                            String night = currentRow.getCell(9).getStringCellValue();
                            String morning = currentRow.getCell(10).getStringCellValue();
                            String evening = currentRow.getCell(11).getStringCellValue();
                            String pressure = currentRow.getCell(12).getStringCellValue();
                            String wind = currentRow.getCell(13).getStringCellValue();
                            String sunrise = currentRow.getCell(14).getStringCellValue();
                            String sunset = currentRow.getCell(15).getStringCellValue();


                            System.out.println(date);
                // Load dữ liệu từ excel vào staging.db
                    connection1.LoadStaging(date, location, status, high, low, humidity, precipitation, average_temp, day, night, morning, evening, pressure, wind, sunrise, sunset);
                        }
                    } catch (Exception e) {
                        // Gặp lỗi cập nhật status
                        connection.updateStatusConfig("CRAWL_ERROR",id);
                    connection.log(id, "weather", "EXTRACT_ERROR", "Cannot extract data","extracter");
                        return;
                    }
                    connection.updateStatusConfig("EXTRACTED",id);
                    connection.log(id, "weather", "EXTRACTED", "EXTRACT COMPLETE","extracter");

                    connection1.closeStaging();

                }
                connection.closeControl();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void start() {
            extract();
        }

    public static void main(String[] args) {
        new Extract().start();
    }

}
