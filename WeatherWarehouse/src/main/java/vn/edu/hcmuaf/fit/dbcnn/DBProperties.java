package vn.edu.hcmuaf.fit.dbcnn;

import java.io.IOException;
import java.util.Properties;

public class DBProperties {
    private static Properties properties = new Properties();

    static {
        try{
            properties.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static String getControlURL(){return properties.get("control.url").toString();};
    public static String getControlUsername(){return properties.get("control.username").toString();};
    public static String getControlPassword(){return properties.get("control.password").toString();};
    public static String getStagingURL(){return properties.get("staging.url").toString();};
    public static String getStagingUsername(){return properties.get("staging.username").toString();};
    public static String getStagingPassword(){return properties.get("staging.password").toString();};
    public static String getWarehouseURL(){return properties.get("warehouse.url").toString();};
    public static String getWarehouseUsername(){return properties.get("warehouse.username").toString();};
    public static String getWarehousePassword(){return properties.get("warehouse.password").toString();};



}
