-- #QUERY_UPDATE_CONFIG_STATUS
UPDATE control.configs SET status = ? WHERE id = ?;

-- #QUERY_SELECT_CONFIG
SELECT * FROM control.configs WHERE flag = '1' AND status = ?;

-- #QUERY_UPDATE_WEATHERSTATUS_DIM
INSERT IGNORE INTO warehouse.weatherstatus_dim (status_name, description, start_date, end_date, is_current, previous_status_id)
SELECT
    status,
    status AS description,
    CURDATE() AS start_date,
    (SELECT MAX(full_date) FROM warehouse.date_dim) AS end_date,
    1 AS is_current,
    0 AS previous_status_id
FROM staging.staging s LEFT JOIN warehouse.weatherstatus_dim wd ON s.status = wd.status_name
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
INSERT IGNORE INTO warehouse.weatherstatus_dim (status_name, description, start_date, end_date,
                                                is_current, previous_status_id)
SELECT
    status,
    status AS description,
    CURDATE() AS start_date,
    (SELECT MAX(full_date) FROM warehouse.date_dim) AS end_date,
    1 AS is_current,
    0 AS previous_status_id
FROM staging.staging s
         LEFT JOIN warehouse.weatherstatus_dim wd ON s.status = wd.status_name
WHERE s.status IS NOT NULL AND wd.status_name IS NULL;

-- #QUERY_LOAD_TO_STAGING
INSERT INTO  staging ( date, location,status,high,low, humidity,precipitation,average_temp, day, night, morning, evening, pressure, wind, sunrise, sunset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);

-- #QUERY_TRUNCATE_STAGING
TRUNCATE TABLE staging;

-- #QUERY_INSERT_LOG
INSERT INTO control.logs(config_id, `name`, `status`, file_timestamp, note, created_at, created_by) VALUES (?, ?, ?, CURRENT_DATE, ?, NOW(), ?);

-- #QUERY_SELECT_EMAIL
SELECT email FROM control.configs WHERE id = ?;

-- #QUERY_SELECT_SIZE_WEATHERDATA
SELECT table_name,
       ROUND((DATA_LENGTH + INDEX_LENGTH) / (1024 * 1024), 2) AS TABLE_SIZE_MB
FROM information_schema.tables
WHERE table_schema = 'warehouse' AND table_name = 'weather_fact';

-- #QUERY_EXPORT_DATA_OLD
SELECT *
INTO OUTFILE ?
    FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"'
    LINES TERMINATED BY '\n'
FROM warehouse.weather_fact AS wd
         INNER JOIN warehouse.date_dim AS dd ON wd.date_id = dd.id
WHERE dd.full_date < ?;

-- #QUERY_DELETE_DATA_OLD
DELETE wd
FROM warehouse.weather_fact wd
         INNER JOIN warehouse.date_dim dd ON dd.id = wd.date_id
WHERE dd.full_date < ?;

-- #CALL_TRANSFORM
CALL staging.Transform();

-- #CALL_LOADSTAGINGTOWAREHOUSE
CALL staging.LoadStagingToWarehouse();

-- #CALL_LOADFACTTOAGGREGATE
CALL warehouse.LoadFactToAggregate();
