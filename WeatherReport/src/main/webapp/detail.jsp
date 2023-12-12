<%@ page import="com.example.mockup.model.Weather" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"></head>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<style>
    .back {
        font-size: 40px;
        margin-left: 10px;
    }
    h1 {
        margin-left: 25px;
    }
</style>
<body>
<a href="/" class="back">
    <i class="fa-solid fa-circle-chevron-left"></i>
</a>
<%
    List<Weather> weathers = (List<Weather>) request.getAttribute("weather");
    LocalDate currentDate = LocalDate.now();
    if(weathers != null) {
%>
<h1>Thời tiết <%=weathers.get(0).getLocation()%></h1>

<%
    for (Weather weather : weathers) {
%>
<div class="weather-date shadow-sm">
    <h2 class="font-size-18 weather-date-title pt-2" style="text-align: center;">
        <span><%=weather.getDate().equals(currentDate.toString()) ? "Hiện tại" : weather.getDate()%></span></h2>
    <div class="row">
        <div class="col-12 col-md-6">
            <div class="rounded p-3 weather-date-left">
                <div class="d-flex flex-wrap">
                    <div class="overview-current">
                        <%String status = weather.getStatus();
                        switch (status) {
                            case "Mưa nhẹ":
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/10d@2x.png" alt="Mưa nhẹ">
                        <% break;
                            case "Mây cụm":
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/04d@2x.png" alt="Mây cụm">
                        <%break;
                            case "Bầu trời quang đãng":
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/01n@2x.png" alt="Nắng">
                        <%break;
                            case "Mây thưa":
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/02n@2x.png" alt="Mây thưa">
                        <% break;
                            case "Mây đen u ám":
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/04d@2x.png" alt="Mây đen u ám">
                        <%break;
                            default:
                        %>
                        <img src="https://file.thoitiet.edu.vn/thoitietedu/icons/02d@2x.png" alt="Mây">
                        <%}%>
                    </div>
                    <div class="overview-caption mx-3">
                        <p class="overview-caption-item overview-caption-item-detail">
                            <%=status%>
                        </p>
                        <span class="current-temperature">
                                <%=weather.getAverage_temp()%>°
                            </span>
                    </div>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="d-flex ms-2 me-auto">
                            <div class="avatar">
                                <div class="avatar-img rounded-circle">
                                    <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="temp" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Nhiệt độ</title><path d="M10.333 15.48v.321c.971.357 1.667 1.322 1.667 2.456 0 1.438-1.12 2.604-2.5 2.604S7 19.695 7 18.257c0-1.134.696-2.099 1.667-2.456v-.322a2.084 2.084 0 0 1-1.25-1.91V5.583a2.083 2.083 0 1 1 4.166 0v7.986c0 .855-.514 1.589-1.25 1.91zM15.8 8.1a2.8 2.8 0 1 1 0-5.6 2.8 2.8 0 0 1 0 5.6zm0-1.85a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"></path></svg>
                                </div>
                            </div>
                            <span class="fw-bold">
                                    Thấp/Cao
                                </span>
                        </div>
                        <span class=""><%=weather.getLow()%>°/<%=weather.getHigh()%>°</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="d-flex ms-2 me-auto">
                            <div class="avatar">
                                <div class="weather-icon">
                                    <i class="bi bi-droplet"></i>
                                </div>
                            </div>
                            <span class="fw-bold">
                                    Độ ẩm
                                </span>
                        </div>
                        <span class=""><%=weather.getHumidity()%>%</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="d-flex ms-2 me-auto">
                            <div class="avatar">
                                <div class="weather-icon">
                                    <i class="bi bi-cloud-rain"></i>
                                </div>
                            </div>
                            <span class="fw-bold">
                                    Lượng mưa
                                </span>
                        </div>
                        <span class=""><%=weather.getPrecipitation()%> mm</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-12 col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                            <div class="avatar-img rounded-circle">
                                <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="temp" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Nhiệt độ</title><path d="M10.333 15.48v.321c.971.357 1.667 1.322 1.667 2.456 0 1.438-1.12 2.604-2.5 2.604S7 19.695 7 18.257c0-1.134.696-2.099 1.667-2.456v-.322a2.084 2.084 0 0 1-1.25-1.91V5.583a2.083 2.083 0 1 1 4.166 0v7.986c0 .855-.514 1.589-1.25 1.91zM15.8 8.1a2.8 2.8 0 1 1 0-5.6 2.8 2.8 0 0 1 0 5.6zm0-1.85a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"></path></svg>
                            </div>
                        </div>
                        <span class="fw-bold">

                            </span>
                    </div>

                </li>
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                            <div class="avatar-img rounded-circle">
                                <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="temp" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Nhiệt độ</title><path d="M10.333 15.48v.321c.971.357 1.667 1.322 1.667 2.456 0 1.438-1.12 2.604-2.5 2.604S7 19.695 7 18.257c0-1.134.696-2.099 1.667-2.456v-.322a2.084 2.084 0 0 1-1.25-1.91V5.583a2.083 2.083 0 1 1 4.166 0v7.986c0 .855-.514 1.589-1.25 1.91zM15.8 8.1a2.8 2.8 0 1 1 0-5.6 2.8 2.8 0 0 1 0 5.6zm0-1.85a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"></path></svg>
                            </div>
                        </div>
                        <span class="fw-bold">
                                Ngày/Đêm
                            </span>
                    </div>
                    <span class=""><%=weather.getDay()%>°/<%=weather.getNight()%>°</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                            <div class="avatar-img rounded-circle">
                                <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="temp" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Nhiệt độ</title><path d="M10.333 15.48v.321c.971.357 1.667 1.322 1.667 2.456 0 1.438-1.12 2.604-2.5 2.604S7 19.695 7 18.257c0-1.134.696-2.099 1.667-2.456v-.322a2.084 2.084 0 0 1-1.25-1.91V5.583a2.083 2.083 0 1 1 4.166 0v7.986c0 .855-.514 1.589-1.25 1.91zM15.8 8.1a2.8 2.8 0 1 1 0-5.6 2.8 2.8 0 0 1 0 5.6zm0-1.85a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"></path></svg>
                            </div>
                        </div>
                        <span class="fw-bold">
                                Sáng/Tối
                            </span>
                    </div>
                    <span class=""><%=weather.getMorning()%>°/<%=weather.getEvening()%>°</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                            <div class="avatar-img rounded-circle">
                                <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="pressure" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Áp suất</title><path d="M8.462 18.293l-.29-.002c-.6-.004-1.043-.007-1.259-.007-1.119 0-1.182-1.015-.34-1.734l.196-.164.508-.425 1.543-1.292c1.014-.846 1.74-1.45 2.073-1.723.735-.601 1.305-.596 2.033.022.387.329.959.805 2.207 1.841a377.936 377.936 0 0 1 2.18 1.816c.796.67.742 1.66-.295 1.66h-2.382v1.77c0 .83-.393 1.223-1.258 1.223h-2.994c-.809 0-1.258-.42-1.258-1.207v-1.773l-.664-.005zm0-12.807l-.29.002c-.6.004-1.043.006-1.259.006-1.119 0-1.182 1.016-.34 1.734l.196.164.508.426 1.543 1.29a348.68 348.68 0 0 0 2.073 1.724c.735.601 1.305.596 2.033-.022.387-.328.959-.805 2.207-1.84a377.937 377.937 0 0 0 2.18-1.817c.796-.67.742-1.659-.295-1.659h-2.382v-1.77c0-.832-.393-1.224-1.258-1.224h-2.994c-.809 0-1.258.42-1.258 1.207V5.48l-.664.005z"></path></svg>
                            </div>
                        </div>
                        <span class="fw-bold">
                                Áp suất
                            </span>
                    </div>
                    <span class=""><%=weather.getPressure()%> mmhg</span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                            <div class="avatar-img rounded-circle">
                                <svg class="WeatherDetailsListItem--icon--NgMGn Icon--icon--2AbGu Icon--darkTheme--2U1o8" set="current-conditions" name="wind" theme="dark" data-testid="Icon" aria-hidden="true" role="img" viewBox="0 0 24 24"><title>Wind</title><path d="M6 8.67h5.354c1.457 0 2.234-1.158 2.234-2.222S12.687 4.4 11.354 4.4c-.564 0-1.023.208-1.366.488M3 11.67h15.54c1.457 0 2.235-1.158 2.235-2.222S19.873 7.4 18.54 7.4c-.747 0-1.311.365-1.663.78M6 15.4h9.389c1.457 0 2.234 1.159 2.234 2.223 0 1.064-.901 2.048-2.234 2.048a2.153 2.153 0 0 1-1.63-.742" stroke-width="2" stroke="currentColor" stroke-linecap="round" fill="none"></path></svg>
                            </div>
                        </div>
                        <span class="fw-bold">
                                Gió
                            </span>
                    </div>
                    <span class="">
                            <%=weather.getWind()%> km/h
                        </span>
                </li>
                <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="d-flex ms-2 me-auto">
                        <div class="avatar">
                                <span class="weather-icon">
                                    <i class="bi bi-sun"></i>
                                </span>
                        </div>
                        <span class="fw-bold">
                                Bình minh/Hoàng hôn
                            </span>
                    </div>
                    <div class="d-flex ml-auto align-items-center">
                        <div class="weather-sun">
                            <span><i class="bi bi-sunrise"></i><%=weather.getSunrise()%> AM</span>
                            <span>
                                    <i class="bi bi-sunset"></i>
                                    <%=weather.getSunset()%> PM
                                </span>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
<%}}%>

</body>
</html>