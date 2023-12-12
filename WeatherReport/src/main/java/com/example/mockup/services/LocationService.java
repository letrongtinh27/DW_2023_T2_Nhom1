package com.example.mockup.services;

import com.example.mockup.dbc.Dbconnect;
import com.example.mockup.model.Location;

import java.sql.SQLException;
import java.util.List;

public class LocationService {
    private final Dbconnect dbconnect = new Dbconnect();

    public List<Location> getAllLocation() throws SQLException, ClassNotFoundException {
        dbconnect.connectToDatamart();
        List<Location> result = dbconnect.getLocation();
        dbconnect.getConnection().close();
        return result;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println(new LocationService().getAllLocation());
    }
}
