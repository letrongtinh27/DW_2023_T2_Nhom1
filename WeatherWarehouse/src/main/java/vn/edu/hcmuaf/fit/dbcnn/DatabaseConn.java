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
            // crate statement
            Statement stmt = conn.createStatement();
            // get data from table 'student'
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
            throw new RuntimeException();
        }
        return queryBuilder.toString();
    }
    public void updateStatusConfig(String status, String id) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_UPDATE_CONFIG_STATUS");
        PreparedStatement ps = this.getStagingConn().prepareStatement(sql);
        ps.setString(1, status);
        ps.setString(2, id);
        ps.executeUpdate();
    }

    public void updateLog(String id, String status, String note, String updated_by) throws SQLException {
        String sql = this.readQueryFromFile("document/update_query.sql", "-- #QUERY_UPDATE_LOGS");
        Connection staging = this.getStagingConn();
        PreparedStatement ps = staging.prepareStatement(sql);
        ps.setString(1, status);
        ps.setString(2, note);
        ps.setString(3, updated_by);
        ps.setString(4, id);
        ps.executeUpdate();
    }
}
