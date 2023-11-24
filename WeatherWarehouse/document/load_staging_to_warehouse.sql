-- #QUERY_UPDATE_WEATHER_DATA
UPDATE warehouse.weather_data wd
    JOIN (
        SELECT
            st.id AS staging_id,
            ld.id AS location_id,
            dd.id AS date_id,
            ws.id AS status_id,
            st.low AS low,
            st.high AS high,
            st.humidity AS humidity,
            st.precipitation AS precipitation,
            st.average_temp AS average_temp,
            st.`day` AS `day`,
            st.night AS night,
            st.morning AS morning,
            st.evening AS evening,
            st.pressure AS pressure,
            st.wind AS wind,
            st.sunrise AS sunrise,
            st.sunset AS sunset
        FROM staging.staging st
                 JOIN warehouse.location_dim ld ON st.location = ld.location_name
                 JOIN warehouse.date_dim dd ON st.date = dd.full_date
                 JOIN warehouse.weather_dim ws ON st.status = ws.status_name
    ) AS st_data ON wd.location_id = st_data.location_id
        AND wd.date_id = st_data.date_id
        AND wd.status_id = st_data.status_id
    JOIN warehouse.date_dim dd ON wd.effective_date = dd.id
SET
    wd.low = st_data.low,
    wd.high = st_data.high,
    wd.humidity = st_data.humidity,
    wd.precipitation = st_data.precipitation,
    wd.average_temp = st_data.average_temp,
    wd.`day` = st_data.`day`,
    wd.night = st_data.night,
    wd.morning = st_data.morning,
    wd.evening = st_data.evening,
    wd.pressure = st_data.pressure,
    wd.wind = st_data.wind,
    wd.sunrise = st_data.sunrise,
    wd.sunset = st_data.sunset
WHERE
    wd.id IS NOT NULL;

-- #QUERY_INSERT_WAREHOUSE_DATA
INSERT INTO  warehouse.weather_data (location_id, date_id, status_id, low, high, humidity, precipitation, average_temp, day, night, morning, evening, pressure,
                                     wind, sunrise, sunset, effective_date)
SELECT
    ld.id AS location_id,
    dd.id AS date_id,
    ws.id AS status_id,
    st.low,
    st.high,
    st.humidity,
    st.precipitation,
    st.average_temp,
    st.`day`,
    st.night,
    st.morning,
    st.evening,
    st.pressure,
    st.wind,
    st.sunrise,
    st.sunset,
    dd.id AS effective_date
FROM
    staging.staging st
        JOIN
    warehouse.location_dim ld ON st.location = ld.location_name
        JOIN
    warehouse.date_dim dd ON st.date = dd.full_date
        JOIN
    warehouse.weather_dim ws ON st.status = ws.status_name
        LEFT JOIN
    warehouse.weather_data wd ON ld.id = wd.location_id
        AND dd.id = wd.date_id
        AND ws.id = wd.status_id
WHERE
    wd.id IS NULL;