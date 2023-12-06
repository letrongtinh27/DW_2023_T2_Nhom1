package vn.edu.hcmuaf.fit;

import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.etl.Load;
import vn.edu.hcmuaf.fit.etl.Transform;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConn cnn = new DatabaseConn();
            cnn.connectToWarehouse();

            checkSizeTable(cnn);





//            new Transform().start();
//            new Load().loadStagingToWarehouse();
//            new Load().loadWarehouseToAggregate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkSizeTable(DatabaseConn databaseConn) throws SQLException {
        double sizeTable = databaseConn.getTableSize();
        double thresholdSize = 0.1;
        if (sizeTable > thresholdSize) {
            // Lấy dữ liệu cũ ra và lưu trữ
//            exportAndStoreOldData(databaseConn.getWarehouseConn(), "/path/to/exported_data.csv");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fiveMonthAgo = LocalDate.now().minusMonths(5);
            String date = "'" + fiveMonthAgo.format(formatter).toString() +"'";
            databaseConn.exportAndStoreOldData(date, "C:\\Users\\tinh\\Desktop\\exportdataold.csv");
            // Xóa dữ liệu cũ từ bảng
//            deleteOldData(connection, "your_table");
            databaseConn.deleteDataOld(date);
        }
    }
}