package vn.edu.hcmuaf.fit.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;
import java.io.File;
import java.util.Properties;

public class SendMail {
    public static final String HOST_NAME = "smtp.gmail.com";
    public static final int SSL_PORT = 465; // Port for SSL
    public static final int TSL_PORT = 587; // Port for TLS/STARTTLS
    public static final String APP_EMAIL = "group1warehouse@gmail.com"; // your email
    public static final String APP_PASSWORD = "txsfqfglvwgvdasb"; // your password
    public static boolean sendEmail(String to, String title, String content, String filePath) {
        Properties pros = new Properties();
        pros.put("mail.smtp.host" , HOST_NAME);
        pros.put("mail.smtp.port", TSL_PORT);
        pros.put("mail.smtp.auth", "true");
        pros.put("mail.smtp.starttls.enable", "true");
        pros.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        pros.put("mail.smtp.debug", "true");
        pros.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Authenticator auth = new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        };

        Session session = Session.getInstance(pros,auth);
        MimeMessage msg = new MimeMessage(session);

//        try {
//            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//            msg.setFrom(new InternetAddress(APP_EMAIL));
//            // Người nhận
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
//            // Tiêu đề email
//            msg.setSubject(title);
//            msg.setContent(content, "text/HTML; charset=UTF-8");
//            Transport.send(msg);
//            System.out.println("Gửi email thành công");
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(APP_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(title);

            // Nội dung email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/HTML; charset=UTF-8");

            // Tạo multipart để kết hợp nội dung và file đính kèm
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            File file = null;
            // File đính kèm
            if(filePath!=null) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                file = new File(filePath);
                String namefile = file.getName();
                attachmentBodyPart.setFileName(namefile); // Tên file đính kèm
                multipart.addBodyPart(attachmentBodyPart);
            }
            msg.setContent(multipart);

            // Gửi email
            Transport.send(msg);
            if(file!=null) file.deleteOnExit();
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
