-- #QUERY_UPDATE_CONFIG_STATUS
UPDATE control.configs SET status = ? WHERE id = ?;

-- #QUERY_SELECT_CONFIG
SELECT * FROM control.configs WHERE flag = '1' AND status = ?;

-- #QUERY_UPDATE_WEATHER_DIM
INSERT IGNORE INTO warehouse.weather_dim (status_name, description, start_date, end_date, is_current, previous_status_id)
SELECT
    status,
    status AS description,
    CURDATE() AS start_date,
    (SELECT MAX(full_date) FROM warehouse.date_dim) AS end_date,
    1 AS is_current,
    0 AS previous_status_id
FROM staging.staging s LEFT JOIN warehouse.weather_dim wd ON s.status = wd.status_name
WHERE s.status IS NOT NULL AND wd.status_name IS NULL;

-- #QUERY_SELECT_LOGS
SELECT * FROM control.logs WHERE config_id = ? AND created_at = CURRENT_DATE;

-- #QUERY_UPDATE_LOGS
UPDATE control.logs
SET
    status = ?,
    note = ?,
    updated_at = CURRENT_DATE,
    updated_by = ?
WHERE id = ?;

-- #QUERY_LOAD_STAGING_TO_WAREHOUSE
INSERT IGNORE INTO warehouse.weather_dim (status_name, description, start_date, end_date,
                                          is_current, previous_status_id)
SELECT
    status,
    status AS description,
    CURDATE() AS start_date,
    (SELECT MAX(full_date) FROM warehouse.date_dim) AS end_date,
    1 AS is_current,
    0 AS previous_status_id
FROM staging.staging s
         LEFT JOIN warehouse.weather_dim wd ON s.status = wd.status_name
WHERE s.status IS NOT NULL AND wd.status_name IS NULL;


-- #QUERY_LOAD_TO_STAGING
INSERT INTO  staging ( date, location,status,high,low, humidity,precipitation,average_temp, day, night, morning, evening, pressure, wind, sunrise, sunset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);

-- #QUERY_TRUNCATE_STAGING
TRUNCATE TABLE staging;

