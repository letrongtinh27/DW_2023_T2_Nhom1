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
CREATE DATABASE IF NOT EXISTS `control` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `control`;

-- Dumping structure for table control.configs
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
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
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
CREATE TABLE IF NOT EXISTS `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `row_count` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `file_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `note` text DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `config_id` (`config_id`),
  CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`config_id`) REFERENCES `configs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.


-- Dumping database structure for staging
CREATE DATABASE IF NOT EXISTS `staging` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `staging`;

-- Dumping structure for table staging.staging
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.


-- Dumping database structure for warehouse
CREATE DATABASE IF NOT EXISTS `warehouse` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `warehouse`;

-- Dumping structure for table warehouse.date_dim
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

-- Dumping structure for table warehouse.location_dim
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

-- Dumping structure for table warehouse.weather_data
CREATE TABLE IF NOT EXISTS `weather_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) DEFAULT NULL,
  `date_id` int(11) DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `average_temp` float DEFAULT NULL,
  `day` int(11) DEFAULT NULL,
  `night` int(11) DEFAULT NULL,
  `morning` int(11) DEFAULT NULL,
  `evening` int(11) DEFAULT NULL,
  `pressure` float DEFAULT NULL,
  `wind` float DEFAULT NULL,
  `sunrise` time DEFAULT NULL,
  `sunset` time DEFAULT NULL,
  `effective_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `location_id` (`location_id`),
  KEY `date_id` (`date_id`),
  KEY `status_id` (`status_id`),
  CONSTRAINT `weather_data_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `location_dim` (`id`),
  CONSTRAINT `weather_data_ibfk_2` FOREIGN KEY (`date_id`) REFERENCES `date_dim` (`id`),
  CONSTRAINT `weather_data_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `weather_dim` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table warehouse.weather_dim
CREATE TABLE IF NOT EXISTS `weather_dim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `is_current` tinyint(1) DEFAULT NULL,
  `previous_status_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
