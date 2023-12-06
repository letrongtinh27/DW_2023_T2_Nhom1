package vn.edu.hcmuaf.fit.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.model.weatherForecast;
import vn.edu.hcmuaf.fit.util.SendMail;

public class Crawl {
    static String url = "";
    static String location = "";
    static String format ="";
    static String[] fieldNames = null;

    static String[] provinces = { "an-giang", "ba-ria-vung-tau", "bac-lieu", "bac-kan", "bac-giang", "bac-ninh", "ben-tre", "binh-duong", "binh-dinh", "binh-phuoc", "binh-thuan", "ca-mau", "cao-bang", "can-tho", "da-nang", "dak-lak", "dak-nong", "dien-bien", "dong-nai", "dong-thap", "gia-lai", "ha-giang", "ha-nam", "ha-noi", "ha-tinh", "hai-duong", "hai-phong", "hoa-binh", "ho-chi-minh", "hau-giang", "hung-yen", "khanh-hoa", "kien-giang", "kon-tum", "lai-chau", "lao-cai", "lang-son", "lam-dong", "long-an", "nam-dinh", "nghe-an", "ninh-binh", "ninh-thuan", "phu-tho", "phu-yen", "quang-binh", "quang-nam", "quang-ngai", "quang-ninh", "quang-tri", "soc-trang", "son-la", "tay-ninh", "thai-binh", "thai-nguyen", "thanh-hoa", "thua-thien-hue", "tien-giang", "tra-vinh", "tuyen-quang", "vinh-long", "vinh-phuc", "yen-bai"};
    static String fday = "/5-ngay-toi";

    public void crawlData() throws IOException, SQLException {
        LocalDate currentDate = LocalDate.now();

        DatabaseConn connection = new DatabaseConn();
        // kết nối db control
        connection.connectToControl();

        Connection control = connection.getControlConn();

        // K kết nối được thì gửi mail
        if(control == null) {
            SendMail.sendEmail("20130266@st.hcmuaf.edu.vn","Error" ,"Can not connect ControlDB");
        }

        String getConfig = connection.readQueryFromFile("document/query.sql", "-- #QUERY_SELECT_CONFIG");
        // Lấy dữ liệu config có flag = 1 và status = repair
        getConfig = getConfig.replace("?", "'REPAIRED'");


        List<Map<String, Object>> listConfig = connection.query(getConfig);
        // Chạy từng dòng config
        for (Map<String, Object> config : listConfig) {
            String id = config.get("id").toString();
            url = config.get("source_path").toString();
            location = config.get("location").toString();
            format = config.get("format").toString();

            File file = new File(location + currentDate +  format);
            // Nếu file đã tồn tại thì xóa đi để crawl dữ liệu mới
            if (file.exists()) {
                file.delete();
            }

            String field_name = config.get("colomn_name").toString();
            String separator = config.get("separator").toString();

            fieldNames = field_name.split(separator);

            for (String p : provinces) {
                try {
                    // Cập nhật status
                    connection.updateStatusConfig("CRAWLING",id);
                    GetData(url,p);
                } catch (Exception e) {
//                    String id = config.get("id").toString();
                    // Gặp lỗi cập nhật status
                    connection.updateStatusConfig("CRAWL_ERROR",id);
                    connection.updateLog(id, "CRAWL_ERROR" , "Can not crawl data by source_path", "Hoang");
                    return;
                }
            }
            connection.updateStatusConfig("CRAWLED",id);
            connection.log(id, "Log of Crawler" , "CRAWLER COMPLETE", "Crawler done!", "crawler_script");

        }

        // Đóng kết nối control
        connection.closeControl();
    }

    // Tạo file
    public void saveToFile(weatherForecast WeatherForecast) throws IOException {
        try {
            LocalDate currentDate = LocalDate.now();
            String excelFilePath = location + currentDate +  format;

            Workbook workbook = getWorkbook(excelFilePath);
            Sheet sheet = workbook.getSheetAt(0);

            int lastRowIndex = sheet.getLastRowNum();

            Row rowToInsert = sheet.getRow(lastRowIndex + 1);
            if (rowToInsert == null) rowToInsert = sheet.createRow(lastRowIndex + 1);
            Field[] fields = WeatherForecast.getClass().getDeclaredFields();

            for (int i = 0; i < 16; i++) {
                Cell cell1 = rowToInsert.createCell(i);
                Field field = fields[i];
                field.setAccessible(true);
                cell1.setCellValue(field.get(WeatherForecast).toString());
            }

            for (int i = 0; i < 16; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo file
    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        File file = new File(excelFilePath);
        boolean fileExists = file.exists();

        if (!fileExists) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data sheet");

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < fieldNames.length; i++)
                headerRow.createCell(i).setCellValue(fieldNames[i]);
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        return new XSSFWorkbook(inputStream);
    }

    // Crawl dữ liệu từ link
    public void GetData(String url, String province) throws IOException {
        try {
            Document document;
            String url_pro = url + province + fday;
            System.out.println(url_pro);
            document = Jsoup.connect(url_pro).userAgent("Mozila/5.0").get();

            Elements weatherDateElements = document.select("div.weather-date.shadow-sm");

            for (int i =0; i <= 4 ; i++) {
                Element Elements = weatherDateElements.get(i);
                String location = province;
                LocalDate date = LocalDate.now();
                String get_date = Elements.select("span").first().text();
                if ( get_date.equals("Hiện tại")) {
                    get_date = date.getYear() + "-" + (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()) + "-" + date.getDayOfMonth();
                } else {
                    get_date = get_date.substring(3,get_date.length());
                    String[] parts = get_date.split("/");
                    get_date = date.getYear() + "-" + parts[1] + "-" + parts[0];
                }
                String average_temp = Elements.select(".current-temperature").first().text();
                if (average_temp.length() >=2) { average_temp = average_temp.substring(0, average_temp.length()-1); }
                String status = Elements.select("p.overview-caption-item.overview-caption-item-detail").first().text();
                String high_low = Elements.select("li.list-group-item span").get(1).text();
                String[] split1 = high_low.split("/");
                String low = split1[0].replace("°","");
                String high = split1[1].replace("°","");
                String humidity = Elements.select("li:has(span.fw-bold:contains(Độ ẩm)) span").get(1).text();
                if (humidity.length() >=2) { humidity = humidity.substring(0,humidity.length()-2); }
                String precipitation = Elements.select("li:has(span.fw-bold:contains(Lượng mưa)) span").get(1).text();
                if (precipitation.length() >=3) { precipitation = precipitation.substring(0,precipitation.length()-3);}
                String day_night = Elements.select("li:has(span.fw-bold:contains(Ngày/Đêm)) span").get(1).text();
                String[] split2 = day_night.split("/");
                String day = split2[0].replace("°","");
                String night = split2[1].replace("°","");
                String morning_evening = Elements.select("li:has(span.fw-bold:contains(Sáng/Tối)) span").get(1).text();
                String[] split3 = morning_evening.split("/");
                String morning = split2[0].replace("°","");
                String evening = split2[1].replace("°","");
                String pressure = Elements.select("li:has(span.fw-bold:contains(Áp suất)) span").get(1).text();
                if (pressure.length() >= 5) { pressure = pressure.substring(0, pressure.length()- 5); }
                String wind = Elements.select("li:has(span.fw-bold:contains(Gió)) span").get(1).text();
                if (wind.length() >= 5) { wind = wind.substring(0, wind.length()- 5); }
                String sunrise = Elements.select("li:has(span.fw-bold:contains(Bình minh/Hoàng hôn)) span").get(2).text();
                if (sunrise.length() >= 6) { sunrise = sunrise.substring(0,sunrise.length()-3); }
                String sunset = Elements.select("li:has(span.fw-bold:contains(Bình minh/Hoàng hôn)) span").get(3).text();
                if (sunset.length() >= 6) { sunset = sunset.substring(0,sunset.length()-3); }

                weatherForecast WF = new weatherForecast(get_date,location,status,high,low,humidity,precipitation, average_temp,  day, night, morning, evening,pressure, wind, sunrise, sunset);
                saveToFile(WF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            crawlData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        new Crawl().start();
    }
}

