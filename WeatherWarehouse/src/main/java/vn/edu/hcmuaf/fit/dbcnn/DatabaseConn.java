package vn.edu.hcmuaf.fit.dbcnn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConn {
    private Connection controlConn;
    private Connection stagingConn;
    private Connection warehouseConn;

    public DatabaseConn() {
    }

    public void connectToControl() {
        try {
            String jdbcUrl = DBProperties.getControlURL();
            String user = DBProperties.getControlUsername();
            String pass = DBProperties.getControlPassword();
            controlConn = DriverManager.getConnection(jdbcUrl, user, pass);
            System.out.println("Connected to Control");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToStaging() {
        try {
            String jdbcUrl = DBProperties.getStagingURL();
            String user = DBProperties.getStagingUsername();
            String pass = DBProperties.getStagingPassword();
            stagingConn = DriverManager.getConnection(jdbcUrl, user, pass);
            System.out.println("Connected to Staging");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToWarehouse() {
        try {
            String jdbcUrl = DBProperties.getWarehouseURL();
            String user = DBProperties.getWarehouseUsername();
            String pass = DBProperties.getWarehousePassword();
            warehouseConn = DriverManager.getConnection(jdbcUrl, user, pass);
            System.out.println("Connected to Warehouse");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getControlConn() {
        return controlConn;
    }

    public Connection getStagingConn() {
        return stagingConn;
    }

    public Connection getWarehouseConn() {
        return warehouseConn;
    }

    public void closeControl() {
        try {
            if(controlConn != null) {
                controlConn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeStaging() {
        try {
            if(stagingConn != null) {
                stagingConn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeWarehouse() {
        try {
            if(warehouseConn != null){
                warehouseConn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Map<String, Object>> query(String sql) throws SQLException {
        ResultSet rs = null;
        List<Map<String, Object>> results = new ArrayList<>();
        Connection conn = this.controlConn;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            results = rsToList(rs);
            rs.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
    public void updateStatusConfig(String status, String id) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_UPDATE_CONFIG_STATUS");
        PreparedStatement ps = this.getControlConn().prepareStatement(sql);
        ps.setString(1, status);
        ps.setString(2, id);
        ps.executeUpdate();
    }
    public void updateLog(String id, String status, String note, String updated_by) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_UPDATE_LOGS");
        Connection control = this.getControlConn();
        PreparedStatement ps = control.prepareStatement(sql);
        ps.setString(1, status);
        ps.setString(2, note);
        ps.setString(3, updated_by);
        ps.setString(4, id);
        ps.executeUpdate();
    }

    public void log(String config_id, String name, String status, String note, String created_by) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_INSERT_LOG");
        Connection control = this.getControlConn();
        PreparedStatement ps = control.prepareStatement(sql);
        ps.setString(1, config_id);
        ps.setString(2, name);
        ps.setString(3, status);
        ps.setString(4, note);
        ps.setString(5, created_by);
        ps.executeUpdate();
    }

    public void Truncate_staging() throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql","-- #QUERY_TRUNCATE_STAGING");
        Connection staging = this.getStagingConn();
        PreparedStatement ps = staging.prepareStatement(sql);
        ps.executeUpdate();
    }

    public void LoadStaging(String date, String location, String status, String high, String low,
                            String humidity, String precipitation,  String average_temp, String day,
                            String night, String morning,  String evening, String pressure, String wind,
                            String sunrise, String sunset ) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql","-- #QUERY_UPDATE_LOGS");
        Connection staging = this.getStagingConn();
        PreparedStatement ps = staging.prepareStatement(sql);
                ps.setString(1, date);
                ps.setString(2, location);
                ps.setString(3, status);
                ps.setString(4, high);
                ps.setString(5, low);
                ps.setString(6, humidity);
                ps.setString(7, precipitation);
                ps.setString(8, average_temp);
                ps.setString(9, day);
                ps.setString(10, night);
                ps.setString(11, morning);
                ps.setString(12, evening);
                ps.setString(13, pressure);
                ps.setString(14, wind);
                ps.setString(15, sunrise);
                ps.setString(16, sunset);
        ps.executeUpdate();
    }

    public String getEmail(String config_id) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_SELECT_EMAIL");
        try (PreparedStatement ps = this.getControlConn().prepareStatement(sql)) {
            ps.setString(1, config_id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Map<String, Object>> resultList = rsToList(rs);
                if (!resultList.isEmpty()) {
                    Object emailObj = resultList.get(0).get("email");
                    if (emailObj != null) {
                        return emailObj.toString();
                    }
                }
            }
        }
        // Return null if no email is found
        return null;
    }

    public static void main(String[] args) throws SQLException {
//        DatabaseConn connection = new DatabaseConn();
//        connection.connectToControl();
//        connection.updateLog("1", "CRAWLED" , "crawl complete", "Hoang");

    }
}
