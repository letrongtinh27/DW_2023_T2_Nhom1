package com.example.mockup.services;

import com.example.mockup.dbc.Dbconnect;
import com.example.mockup.model.Location;
import com.example.mockup.model.Weather;

import java.sql.SQLException;
import java.util.List;

public class WeatherService {
    private final Dbconnect dbconnect = new Dbconnect();

    public List<Weather> getWeatherDetail(String name) throws SQLException, ClassNotFoundException {
        dbconnect.connectToDatamart();
        List<Weather> result = dbconnect.getWeatherDetail(name);
        dbconnect.getConnection().close();
        return result;
    }
}
