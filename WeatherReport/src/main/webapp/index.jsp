<%@ page import="com.example.mockup.model.Location" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"></head>
<body>
<div class="card mt-3">
    <div class="card-header">
        <h2 class="font-h2">Thời tiết 63 Tỉnh thành</h2>
    </div>
    <div class="card-body">
        <div class="cover-content cover-content-bottom active" id="child-item-childrens">

            <div class="row">
                <%
                    List<Location> locations = (List<Location>) request.getAttribute("location");
                    for (Location location : locations) {
                %>
                <div class="col-12 col-md-4">
                    <div class="item-link my-2 p-1">
                        <h4 class="font-h3">
                            <a href="weather-detail?name=<%=location.getDim_name()%>" title=" <%=location.getLocation_name()%>" target="_blank">
                                <i class="bi bi-arrow-right-short"></i>
                                <%=location.getLocation_name()%>
                            </a>
                        </h4>
                    </div>
                </div>
                <%}%>
            </div>

        </div>
    </div>
</div>
</body>
</html>