package com.example.mockup.dbc;

import com.example.mockup.model.Location;
import com.example.mockup.model.Weather;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dbconnect {
    @Getter
    private Connection connection;

    public Dbconnect() {

    }

    public void connectToDatamart() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = Properties.getDatamartUrl();
        String user = Properties.getDatamartUsername();
        String pass = Properties.getDatamartPassword();
        connection = DriverManager.getConnection(url, user, pass);
    }


    public List<Map<String, Object>> query(String sql) throws SQLException {
        ResultSet rs = null;
        List<Map<String, Object>> results = new ArrayList<>();
        Connection conn = this.connection;
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        results = rsToList(rs);
        rs.close();
        return results;
    }
    public static List<Map<String, Object>> rsToList(ResultSet rs)
            throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                row.put(md.getColumnLabel(i), rs.getObject(i));
            }
            results.add(row);
        }
        return results;
    }
    public String readQueryFromFile(String filePath, String query) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean isInsideDesiredQuery = false;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().startsWith(query)) {
                    isInsideDesiredQuery = true;
                }
                if(isInsideDesiredQuery) {
                    queryBuilder.append(line).append("\n");
                    if(line.trim().endsWith(";")) {
                        isInsideDesiredQuery = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return queryBuilder.toString();
    }

    public List<Location> getLocation() throws SQLException {
//        String sql = readQueryFromFile("/sql/query.sql","-- #QUERY_SELECT_ALL_LOCATION");
        String sql = "SELECT * FROM location_mart;";
        List<Map<String, Object>> listLocation = query(sql);
        List<Location> locations = new ArrayList<>();
        for (Map<String, Object> map : listLocation) {
            Location location = new Location();
            location.setLocation_name(map.get("location_name").toString());
            location.setDim_name(map.get("dim_name").toString());
            location.setArea(map.get("area").toString());
            locations.add(location);
        }
       return locations;
    }

    public List<Weather> getWeatherDetail(String name) throws SQLException {
        String sql = "SELECT * \n" +
                "FROM location_weather_mart lwm\n" +
                "JOIN location_mart lm ON lm.location_name = lwm.location\n" +
                "WHERE lwm.`date` >= DATE(NOW()) AND lm.dim_name = '?';";
        sql = sql.replace("?", name);
        List<Map<String, Object>> list = query(sql);
        List<Weather> weathers = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Weather weather = new Weather();
            weather.setDate(map.get("date").toString());
            weather.setLocation(map.get("location").toString());
            weather.setStatus(map.get("status").toString());
            weather.setLow(map.get("low").toString());
            weather.setHigh(map.get("high").toString());
            weather.setHumidity(map.get("humidity").toString());
            weather.setPrecipitation(map.get("precipitation").toString());
            weather.setAverage_temp(map.get("average_temp").toString());
            weather.setDay(map.get("day").toString());
            weather.setNight(map.get("night").toString());
            weather.setMorning(map.get("morning").toString());
            weather.setEvening(map.get("evening").toString());
            weather.setPressure(map.get("pressure").toString());
            weather.setWind(map.get("wind").toString());
            weather.setSunrise(map.get("sunrise").toString());
            weather.setSunset(map.get("sunset").toString());
            weathers.add(weather);
        }
        return weathers;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Dbconnect db = new Dbconnect();
        db.connectToDatamart();
        System.out.println(db.getLocation());
    }
}
