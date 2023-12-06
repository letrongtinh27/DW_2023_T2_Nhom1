-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.25-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for control
DROP DATABASE IF EXISTS `control`;
CREATE DATABASE IF NOT EXISTS `control` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `control`;

-- Dumping structure for table control.configs
DROP TABLE IF EXISTS `configs`;
CREATE TABLE IF NOT EXISTS `configs` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
                                         `name` varchar(255) DEFAULT NULL,
                                         `description` text DEFAULT NULL,
                                         `source_path` varchar(255) DEFAULT NULL,
                                         `location` varchar(255) DEFAULT NULL,
                                         `format` varchar(255) DEFAULT NULL,
                                         `separator` char(5) DEFAULT NULL,
                                         `colomn_name` varchar(255) DEFAULT NULL,
                                         `email` varchar(255) DEFAULT NULL,
                                         `created_at` date NOT NULL DEFAULT current_timestamp(),
                                         `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
                                         `deleted_at` timestamp NULL DEFAULT NULL,
                                         `created_by` varchar(255) DEFAULT NULL,
                                         `updated_by` varchar(255) DEFAULT NULL,
                                         `deleted_by` varchar(255) DEFAULT NULL,
                                         `flag` tinyint(4) DEFAULT NULL,
                                         `status` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table control.logs
DROP TABLE IF EXISTS `logs`;
CREATE TABLE IF NOT EXISTS `logs` (
                                      `id` int(11) NOT NULL AUTO_INCREMENT,
                                      `config_id` int(11) DEFAULT NULL,
                                      `name` varchar(255) DEFAULT NULL,
                                      `status` varchar(255) DEFAULT NULL,
                                      `file_timestamp` date NOT NULL DEFAULT current_timestamp(),
                                      `note` text DEFAULT NULL,
                                      `created_at` date DEFAULT NULL,
                                      `created_by` varchar(50) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `config_id` (`config_id`),
                                      CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`config_id`) REFERENCES `configs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.


-- Dumping database structure for staging
DROP DATABASE IF EXISTS `staging`;
CREATE DATABASE IF NOT EXISTS `staging` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `staging`;

-- Dumping structure for procedure staging.LoadStagingToWarehouse
DROP PROCEDURE IF EXISTS `LoadStagingToWarehouse`;
DELIMITER //
CREATE PROCEDURE `LoadStagingToWarehouse`()
BEGIN
    -- #QUERY_UPDATE_weather_fact
    UPDATE warehouse.weather_fact wd
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
                     JOIN warehouse.weatherstatus_dim ws ON st.status = ws.status_name
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
        wd.sunset = st_data.sunset,
        wd.status_id = st_data.status_id  -- Thêm cập nhật status_id
    WHERE
        wd.id IS NOT NULL;

    -- #QUERY_INSERT_WAREHOUSE_DATA
    INSERT INTO warehouse.weather_fact (location_id, date_id, status_id, low, high, humidity, precipitation, average_temp, day, night, morning, evening, pressure,
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
            JOIN warehouse.location_dim ld ON st.location = ld.location_name
            JOIN warehouse.date_dim dd ON st.date = dd.full_date
            JOIN warehouse.weatherstatus_dim ws ON st.status = ws.status_name
            LEFT JOIN warehouse.weather_fact wd ON ld.id = wd.location_id
            AND dd.id = wd.date_id
            AND ws.id = wd.status_id
    WHERE
        wd.id IS NULL;
END//
DELIMITER ;

-- Dumping structure for table staging.staging
DROP TABLE IF EXISTS `staging`;
CREATE TABLE IF NOT EXISTS `staging` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
                                         `date` date DEFAULT NULL,
                                         `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `high` int(11) DEFAULT NULL,
                                         `low` int(11) DEFAULT NULL,
                                         `humidity` int(11) DEFAULT NULL,
                                         `precipitation` double DEFAULT NULL,
                                         `average_temp` int(11) DEFAULT NULL,
                                         `day` int(11) DEFAULT NULL,
                                         `night` int(11) DEFAULT NULL,
                                         `morning` int(11) DEFAULT NULL,
                                         `evening` int(11) DEFAULT NULL,
                                         `pressure` double DEFAULT NULL,
                                         `wind` double DEFAULT NULL,
                                         `sunrise` time DEFAULT NULL,
                                         `sunset` time DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for procedure staging.Transform
DROP PROCEDURE IF EXISTS `Transform`;
DELIMITER //
CREATE PROCEDURE `Transform`()
BEGIN
    -- Delete rows with empty or NULL location
    DELETE FROM staging.staging WHERE location = '' OR location IS NULL;

    -- Delete rows with empty or 0 DATE
    DELETE FROM staging.staging WHERE COALESCE(DATE, '') = '' OR DATE = 0;

    -- Update status to 'Bầu trời quang đãng' where status is empty or NULL
    UPDATE staging.staging SET status = 'Bầu trời quang đãng' WHERE status = '' OR status IS NULL;

    -- Update low with the minimum non-zero value
    UPDATE staging.staging SET low = (
        SELECT MIN(low) FROM staging.staging WHERE low <> 0
    ) WHERE low IS NULL OR low = 0;

    -- Update high with the maximum non-zero value
    UPDATE staging.staging SET high = (
        SELECT MAX(high) FROM staging.staging WHERE high <> 0
    ) WHERE high IS NULL OR high = 0;

    -- Update humidity to 70 where it is empty or 0
    UPDATE staging.staging SET humidity = 70 WHERE COALESCE(humidity, '') = '' OR humidity = 0;

    -- Update precipitation based on conditions
    UPDATE staging.staging SET precipitation =
                                   CASE
                                       WHEN status = 'Mưa vừa' THEN 8
                                       WHEN status = 'Mưa to' THEN 25
                                       WHEN status = 'Mưa rất to' THEN 50
                                       WHEN status IN ('Mưa cụm', 'Mưa nhẹ', 'Mây rải rác') THEN 3
                                       ELSE precipitation
                                       END
    WHERE precipitation IS NULL;

    -- Update average_temp where it is 0 and low and high are non-zero
    UPDATE staging.staging SET average_temp = (CAST(low AS FLOAT) + CAST(high AS FLOAT)) / 2
    WHERE average_temp = 0 AND low <> 0 AND high <> 0;

    -- Update day with high where day is NULL or 0 and high is non-zero
    UPDATE staging.staging SET `day` = high WHERE (`day` IS NULL OR `day` = 0) AND high <> 0;

    -- Update night with low where night is NULL or 0 and low is non-zero
    UPDATE staging.staging SET night = low WHERE (night IS NULL OR night = 0) AND low <> 0;

    -- Update morning with high where morning is NULL or 0 and high is non-zero
    UPDATE staging.staging SET morning = high WHERE (morning IS NULL OR morning = 0) AND high <> 0;

    -- Update evening with low where evening is NULL or 0 and low is non-zero
    UPDATE staging.staging SET evening = low WHERE (evening IS NULL OR evening = 0) AND low <> 0;

    -- Update pressure to 760 where it is empty or 0
    UPDATE staging.staging SET pressure = 760 WHERE COALESCE(pressure, '') = '' OR pressure = 0;

    -- Update wind to 5.5 where it is empty or 0
    UPDATE staging.staging SET wind = 5.5 WHERE COALESCE(wind, '') = '' OR wind = 0;

    -- Update sunrise with '05:30:00' where it is NULL
    UPDATE staging.staging SET sunrise = COALESCE(sunrise, '05:30:00') WHERE sunrise IS NULL;

    -- Update sunset with '05:30:00' where it is NULL
    UPDATE staging.staging SET sunset = COALESCE(sunset, '05:30:00') WHERE sunset IS NULL;
END//
DELIMITER ;


-- Dumping database structure for warehouse
DROP DATABASE IF EXISTS `warehouse`;
CREATE DATABASE IF NOT EXISTS `warehouse` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `warehouse`;

-- Dumping structure for table warehouse.aggregate
DROP TABLE IF EXISTS `aggregate`;
CREATE TABLE IF NOT EXISTS `aggregate` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                           `date` date DEFAULT NULL,
                                           `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                           `high` int(11) DEFAULT NULL,
                                           `low` int(11) DEFAULT NULL,
                                           `humidity` int(11) DEFAULT NULL,
                                           `precipitation` float DEFAULT NULL,
                                           `average_temp` int(11) DEFAULT NULL,
                                           `day` int(11) DEFAULT NULL,
                                           `night` int(11) DEFAULT NULL,
                                           `morning` int(11) DEFAULT NULL,
                                           `evening` int(11) DEFAULT NULL,
                                           `pressure` float DEFAULT NULL,
                                           `wind` float DEFAULT NULL,
                                           `sunrise` time DEFAULT NULL,
                                           `sunset` time DEFAULT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=512 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table warehouse.date_dim
DROP TABLE IF EXISTS `date_dim`;
CREATE TABLE IF NOT EXISTS `date_dim` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `full_date` date DEFAULT NULL,
                                          `day_since_2020` int(10) unsigned DEFAULT NULL,
                                          `month_since_2020` int(10) unsigned DEFAULT NULL,
                                          `day_of_week` varchar(50) DEFAULT NULL,
                                          `calendar_month` varchar(50) DEFAULT NULL,
                                          `calendar_year` smallint(5) unsigned DEFAULT NULL,
                                          `calendar_year_month` varchar(50) DEFAULT NULL,
                                          `day_of_month` tinyint(3) unsigned DEFAULT NULL,
                                          `day_of_year` smallint(5) unsigned DEFAULT NULL,
                                          `week_of_year_sunday` tinyint(3) unsigned DEFAULT NULL,
                                          `year_week_sunday` varchar(50) DEFAULT NULL,
                                          `week_sunday_start` date DEFAULT NULL,
                                          `week_of_year_monday` tinyint(4) DEFAULT NULL,
                                          `year_week_monday` varchar(50) DEFAULT NULL,
                                          `week_monday_start` date DEFAULT NULL,
                                          `holiday` varchar(50) DEFAULT NULL,
                                          `day_type` varchar(50) DEFAULT NULL,
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=184082 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for procedure warehouse.LoadFactToAggregate
DROP PROCEDURE IF EXISTS `LoadFactToAggregate`;
DELIMITER //
CREATE PROCEDURE `LoadFactToAggregate`()
BEGIN
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
        warehouse.weather_fact wd
            JOIN
        warehouse.location_dim ld ON wd.location_id = ld.id
            JOIN
        warehouse.date_dim dd ON wd.date_id = dd.id
            JOIN
        warehouse.weatherstatus_dim ws ON wd.status_id = ws.id
            LEFT JOIN
        warehouse.`aggregate` wa ON ld.location_name = wa.location
            AND dd.full_date = wa.date
            AND ws.status_name = wa.`status`
    WHERE dd.full_date >= DATE(NOW());
END//
DELIMITER ;

-- Dumping structure for table warehouse.location_dim
DROP TABLE IF EXISTS `location_dim`;
CREATE TABLE IF NOT EXISTS `location_dim` (
                                              `id` int(11) NOT NULL AUTO_INCREMENT,
                                              `location_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                              `start_date` date DEFAULT NULL,
                                              `end_date` date DEFAULT NULL,
                                              `is_current` tinyint(1) DEFAULT NULL,
                                              `previous_location_id` int(11) DEFAULT NULL,
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table warehouse.weatherstatus_dim
DROP TABLE IF EXISTS `weatherstatus_dim`;
CREATE TABLE IF NOT EXISTS `weatherstatus_dim` (
                                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                                   `status_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                                                   `description` text COLLATE utf8_unicode_ci DEFAULT NULL,
                                                   `start_date` date DEFAULT NULL,
                                                   `end_date` date DEFAULT NULL,
                                                   `is_current` tinyint(1) DEFAULT NULL,
                                                   `previous_status_id` int(11) DEFAULT NULL,
                                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table warehouse.weather_fact
DROP TABLE IF EXISTS `weather_fact`;
CREATE TABLE IF NOT EXISTS `weather_fact` (
                                              `id` int(11) NOT NULL AUTO_INCREMENT,
                                              `location_id` int(11) DEFAULT NULL,
                                              `date_id` int(11) DEFAULT NULL,
                                              `status_id` int(11) DEFAULT NULL,
                                              `low` int(11) DEFAULT NULL,
                                              `high` int(11) DEFAULT NULL,
                                              `humidity` int(11) DEFAULT NULL,
                                              `precipitation` float DEFAULT NULL,
                                              `average_temp` float DEFAULT NULL,
                                              `day` int(11) DEFAULT NULL,
                                              `night` int(11) DEFAULT NULL,
                                              `morning` int(11) DEFAULT NULL,
                                              `evening` int(11) DEFAULT NULL,
                                              `pressure` float DEFAULT NULL,
                                              `wind` float DEFAULT NULL,
                                              `sunrise` time DEFAULT NULL,
                                              `sunset` time DEFAULT NULL,
                                              `effective_date` int(11) DEFAULT NULL,
                                              PRIMARY KEY (`id`),
                                              KEY `location_id` (`location_id`),
                                              KEY `date_id` (`date_id`),
                                              KEY `status_id` (`status_id`),
                                              CONSTRAINT `weather_fact_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `location_dim` (`id`),
                                              CONSTRAINT `weather_fact_ibfk_2` FOREIGN KEY (`date_id`) REFERENCES `date_dim` (`id`),
                                              CONSTRAINT `weather_fact_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `weatherstatus_dim` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=512 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;


