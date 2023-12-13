package vn.edu.hcmuaf.fit;

import vn.edu.hcmuaf.fit.dbcnn.DatabaseConn;
import vn.edu.hcmuaf.fit.etl.Crawl;
import vn.edu.hcmuaf.fit.etl.Extract;
import vn.edu.hcmuaf.fit.etl.Load;
import vn.edu.hcmuaf.fit.etl.Transform;
import vn.edu.hcmuaf.fit.util.SendMail;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        try {
            boolean run = true;
            DatabaseConn cnn = new DatabaseConn();
            cnn.connectToWarehouse();
            showStatusFrame(run);
            checkSizeTable(cnn);

            new Crawl().start();
            new Extract().start();
            new Transform().start();
            new Load().start();
            new Load().loadFactToAggregate();
            new Load().loadAggregateToDatamart();
            run = false;
            showStatusFrame(run);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkSizeTable(DatabaseConn databaseConn) throws SQLException {
        double sizeTable = databaseConn.getTableSize();
        double thresholdSize = 0.2;
        if (sizeTable > thresholdSize) {
            LocalDate fiveMonthAgo = LocalDate.now();
            String currentDate = LocalDate.now().toString();
            String projectPath = System.getProperty("user.dir");
            String exportFolderPath = projectPath + File.separator + "document\\";
            String fileName = "exportdataold" + currentDate + ".csv";
            databaseConn.exportAndStoreOldData(fiveMonthAgo.toString(), exportFolderPath+fileName);
//            databaseConn.deleteDataOld(date);
            SendMail.sendEmail("tinhle2772002@gmail.com", "Export data", "Warehouse bị đầy", exportFolderPath+fileName);
        }
    }

    public static void showStatusFrame(boolean status) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Status Frame");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel statusLabel = new JLabel("Warehouse: đang chạy");
            statusLabel.setHorizontalAlignment(JLabel.CENTER);
            frame.getContentPane().add(statusLabel);

            frame.setVisible(true);
            if (!status) {
                statusLabel.setText("Warehouse: hoàn thành");
             }
        });
    }
}