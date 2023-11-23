package vn.edu.hcmuaf.fit;

import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConn cnn = new DatabaseConn();
            cnn.connectToControl();
            cnn.connectToStaging();
            cnn.connectToWarehouse();

            System.out.println(cnn.query("select * from configs"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}