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

-- Dumping data for table datamart.location_mart: ~63 rows (approximately)
INSERT INTO `location_mart` (`id`, `location_name`, `dim_name`, `area`) VALUES
                                                                            (1, 'Hà Giang', 'ha-giang', 'Đông Bắc Bộ'),
                                                                            (2, 'Cao Bằng', 'cao-bang', 'Đông Bắc Bộ'),
                                                                            (3, 'Bắc Kạn', 'bac-kan', 'Đông Bắc Bộ'),
                                                                            (4, 'Tuyên Quang', 'tuyen-quang', 'Đông Bắc Bộ'),
                                                                            (5, 'Thái Nguyên', 'thai-nguyen', 'Đông Bắc Bộ'),
                                                                            (6, 'Lạng Sơn', 'lang-son', 'Đông Bắc Bộ'),
                                                                            (7, 'Quảng Ninh', 'quang-ninh', 'Đông Bắc Bộ'),
                                                                            (8, 'Bắc Giang', 'bac-giang', 'Đông Bắc Bộ'),
                                                                            (9, 'Phú Thọ', 'phu-tho', 'Đông Bắc Bộ'),
                                                                            (10, 'Lào Cai', 'lao-cai', 'Tây Bắc Bộ'),
                                                                            (11, 'Điện Biên', 'dien-bien', 'Tây Bắc Bộ'),
                                                                            (12, 'Lai Châu', 'lai-chau', 'Tây Bắc Bộ'),
                                                                            (13, 'Sơn La', 'son-la', 'Tây Bắc Bộ'),
                                                                            (14, 'Yên Bái', 'yen-bai', 'Tây Bắc Bộ'),
                                                                            (15, 'Hòa Bình', 'hoa-binh', 'Tây Bắc Bộ'),
                                                                            (16, 'Hà Nội', 'ha-noi', 'Đồng Bằng Sông Hồng'),
                                                                            (17, 'Vĩnh Phúc', 'vinh-phuc', 'Đồng Bằng Sông Hồng'),
                                                                            (18, 'Bắc Ninh', 'bac-ninh', 'Đồng Bằng Sông Hồng'),
                                                                            (19, 'Hải Dương', 'hai-duong', 'Đồng Bằng Sông Hồng'),
                                                                            (20, 'Hải Phòng', 'hai-phong', 'Đồng Bằng Sông Hồng'),
                                                                            (21, 'Hưng Yên', 'hung-yen', 'Đồng Bằng Sông Hồng'),
                                                                            (22, 'Thái Bình', 'thai-binh', 'Đồng Bằng Sông Hồng'),
                                                                            (23, 'Hà Nam', 'ha-nam', 'Đồng Bằng Sông Hồng'),
                                                                            (24, 'Nam Định', 'nam-dinh', 'Đồng Bằng Sông Hồng'),
                                                                            (25, 'Ninh Bình', 'ninh-binh', 'Đồng Bằng Sông Hồng'),
                                                                            (26, 'Thanh Hóa', 'thanh-hoa', 'Bắc Trung Bộ'),
                                                                            (27, 'Nghệ An', 'nghe-an', 'Bắc Trung Bộ'),
                                                                            (28, 'Hà Tĩnh', 'ha-tinh', 'Bắc Trung Bộ'),
                                                                            (29, 'Quảng Bình', 'quang-binh', 'Bắc Trung Bộ'),
                                                                            (30, 'Quảng Trị', 'quang-tri', 'Bắc Trung Bộ'),
                                                                            (31, 'Thừa Thiên Huế', 'thua-thien-hue', 'Bắc Trung Bộ'),
                                                                            (32, 'Đà Nẵng', 'da-nang', 'Nam Trung Bộ'),
                                                                            (33, 'Quảng Nam', 'quang-nam', 'Nam Trung Bộ'),
                                                                            (34, 'Quảng Ngãi', 'quang-ngai', 'Nam Trung Bộ'),
                                                                            (35, 'Bình Định', 'binh-dinh', 'Nam Trung Bộ'),
                                                                            (36, 'Phú Yên', 'phu-yen', 'Nam Trung Bộ'),
                                                                            (37, 'Khánh Hòa', 'khanh-hoa', 'Nam Trung Bộ'),
                                                                            (38, 'Ninh Thuận', 'ninh-thuan', 'Nam Trung Bộ'),
                                                                            (39, 'Bình Thuận', 'binh-thuan', 'Nam Trung Bộ'),
                                                                            (40, 'Kon Tum', 'kon-tum', 'Tây Nguyên'),
                                                                            (41, 'Gia Lai', 'gia-lai', 'Tây Nguyên'),
                                                                            (42, 'Đắk Lắk', 'dak-lak', 'Tây Nguyên'),
                                                                            (43, 'Đắk Nông', 'dak-nong', 'Tây Nguyên'),
                                                                            (44, 'Lâm Đồng', 'lam-dong', 'Tây Nguyên'),
                                                                            (45, 'Bình Phước', 'binh-phuoc', 'Đông Nam Bộ'),
                                                                            (46, 'Tây Ninh', 'tay-ninh', 'Đông Nam Bộ'),
                                                                            (47, 'Bình Dương', 'binh-duong', 'Đông Nam Bộ'),
                                                                            (48, 'Đồng Nai', 'dong-nai', 'Đông Nam Bộ'),
                                                                            (49, 'Bà Rịa-Vũng Tàu', 'ba-ria-vung-tau', 'Đông Nam Bộ'),
                                                                            (50, 'Hồ Chí Minh', 'ho-chi-minh', 'Đông Nam Bộ'),
                                                                            (51, 'Long An', 'long-an', 'Đồng Bằng Sông Cửu Long'),
                                                                            (52, 'Tiền Giang', 'tien-giang', 'Đồng Bằng Sông Cửu Long'),
                                                                            (53, 'Bến Tre', 'ben-tre', 'Đồng Bằng Sông Cửu Long'),
                                                                            (54, 'Trà Vinh', 'tra-vinh', 'Đồng Bằng Sông Cửu Long'),
                                                                            (55, 'Vĩnh Long', 'vinh-long', 'Đồng Bằng Sông Cửu Long'),
                                                                            (56, 'Đồng Tháp', 'dong-thap', 'Đồng Bằng Sông Cửu Long'),
                                                                            (57, 'An Giang', 'an-giang', 'Đồng Bằng Sông Cửu Long'),
                                                                            (58, 'Kiên Giang', 'kien-giang', 'Đồng Bằng Sông Cửu Long'),
                                                                            (59, 'Cần Thơ', 'can-tho', 'Đồng Bằng Sông Cửu Long'),
                                                                            (60, 'Hậu Giang', 'hau-giang', 'Đồng Bằng Sông Cửu Long'),
                                                                            (61, 'Sóc Trăng', 'soc-trang', 'Đồng Bằng Sông Cửu Long'),
                                                                            (62, 'Bạc Liêu', 'bac-lieu', 'Đồng Bằng Sông Cửu Long'),
                                                                            (63, 'Cà Mau', 'ca-mau', 'Đồng Bằng Sông Cửu Long');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
