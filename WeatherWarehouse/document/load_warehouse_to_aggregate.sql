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
WHERE
    wa.id IS NULL;

-- #QUERY_UPDATE_AGGREGATE
UPDATE warehouse.`aggregate` wa
    JOIN (
        SELECT
            wd.id AS aggregate_id,
            ld.location_name AS location,
            dd.full_date AS date,
            ws.status_name AS status,
            wd.low AS low,
            wd.high AS high,
            wd.humidity AS humidity,
            wd.precipitation AS precipitation,
            wd.average_temp AS average_temp,
            wd.`day` AS `day`,
            wd.night AS night,
            wd.morning AS morning,
            wd.evening AS evening,
            wd.pressure AS pressure,
            wd.wind AS wind,
            wd.sunrise AS sunrise,
            wd.sunset AS sunset
        FROM warehouse.weather_data wd
                 JOIN warehouse.location_dim ld ON wd.location_id = ld.id
                 JOIN warehouse.date_dim dd ON wd.date_id = dd.id
                 JOIN warehouse.weather_dim ws ON wd.status_id = ws.id
    ) AS wd_data ON wa.location = wd_data.location
        AND wa.date = wd_data.date
SET
    wa.low = wd_data.low,
    wa.high = wd_data.high,
    wa.humidity = wd_data.humidity,
    wa.precipitation = wd_data.precipitation,
    wa.average_temp = wd_data.average_temp,
    wa.`day` = wd_data.`day`,
    wa.night = wd_data.night,
    wa.morning = wd_data.morning,
    wa.evening = wd_data.evening,
    wa.pressure = wd_data.pressure,
    wa.wind = wd_data.wind,
    wa.sunrise = wd_data.sunrise,
    wa.sunset = wd_data.sunset,
    wa.`status` = wd_data.status
WHERE
    wa.id IS NOT NULL;
