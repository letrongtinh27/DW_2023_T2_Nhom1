package com.example.mockup.servlet;

import com.example.mockup.model.Location;
import com.example.mockup.services.LocationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet(value = "")
public class HomeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LocationService locationService = new LocationService();
        List<Location> locations = null;
        try {
            locations = locationService.getAllLocation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("location", locations);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
