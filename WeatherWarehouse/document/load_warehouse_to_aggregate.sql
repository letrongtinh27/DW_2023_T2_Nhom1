-- #QUERY_TRUNCATE_AGGREGATE
TRUNCATE TABLE warehouse.`aggregate`;

-- #QUERY_INSERT_AGGREGATE
INSERT INTO warehouse.`aggregate` (location, date, `status`, low, high, humidity, precipitation, average_temp, day, night, morning, evening, pressure, wind, sunrise, sunset)
SELECT
    ld.location_name AS location,
    dd.full_date AS date,
    ws.status_name AS `status`,
    wd.low,
    wd.high,
    wd.humidity,
    wd.precipitation,
    wd.average_temp,
    wd.day,
    wd.night,
    wd.morning,
    wd.evening,
    wd.pressure,
    wd.wind,
    wd.sunrise,
    wd.sunset
FROM
    warehouse.weather_data wd
        JOIN
    warehouse.location_dim ld ON wd.location_id = ld.id
        JOIN
    warehouse.date_dim dd ON wd.date_id = dd.id
        JOIN
    warehouse.weather_dim ws ON wd.status_id = ws.id
        LEFT JOIN
    warehouse.`aggregate` wa ON ld.location_name = wa.location
        AND dd.full_date = wa.date
        AND ws.status_name = wa.`status`
WHERE dd.full_date >= DATE(NOW());;
