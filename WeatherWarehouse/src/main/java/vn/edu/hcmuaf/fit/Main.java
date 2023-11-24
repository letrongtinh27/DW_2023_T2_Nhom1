package vn.edu.hcmuaf.fit;

import vn.edu.hcmuaf.fit.etl.Load;
import vn.edu.hcmuaf.fit.etl.Transform;

public class Main {
    public static void main(String[] args) {
        try {
//            DatabaseConn cnn = new DatabaseConn();
//            cnn.connectToControl();
//            cnn.connectToStaging();
//            cnn.connectToWarehouse();
//
//            System.out.println(cnn.query("select * from configs"));

//            new Transform().start();
//            new Load().loadStagingToWarehouse();
            new Load().loadWarehouseToAggregate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}