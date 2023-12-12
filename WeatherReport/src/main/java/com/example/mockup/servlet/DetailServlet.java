package com.example.mockup.servlet;

import com.example.mockup.model.Weather;
import com.example.mockup.services.WeatherService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@WebServlet(value = "/weather-detail")
public class DetailServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        WeatherService weatherService = new WeatherService();
        List<Weather> weathers;
        try {
            weathers = weatherService.getWeatherDetail(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        weathers.sort(Comparator.comparing(Weather::getDate));
        request.setAttribute("weather", weathers);
        request.getRequestDispatcher("detail.jsp").forward(request, response);

    }
}
