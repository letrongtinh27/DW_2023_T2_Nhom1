package com.example.mockup.dbc;

import java.io.IOException;

public class Properties {
    private static final java.util.Properties properties = new java.util.Properties();
    static {
        try{
            properties.load(Properties.class.getClassLoader().getResourceAsStream("db.properties"));
        }catch (IOException ioException){
            throw new RuntimeException(ioException);
        }
    }
    public static String getDatamartUrl() {return properties.get("datamart.url").toString();}
    public static String getDatamartUsername() {
        return properties.get("datamart.username").toString();
    }
    public static String getDatamartPassword() {
        return properties.get("datamart.password").toString();
    }
}
