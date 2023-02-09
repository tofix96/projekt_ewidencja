-- MariaDB dump 10.19  Distrib 10.5.18-MariaDB, for debian-linux-gnu (aarch64)
--
-- Host: localhost    Database: ewidencja
-- ------------------------------------------------------
-- Server version	10.5.18-MariaDB-0+deb11u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `klienci`
--

DROP TABLE IF EXISTS `klienci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `klienci` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imie` varchar(60) NOT NULL DEFAULT 'Joe',
  `nazwisko` varchar(60) NOT NULL DEFAULT 'Doe',
  `numer_tel` int(12) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `klienci`
--

LOCK TABLES `klienci` WRITE;
/*!40000 ALTER TABLE `klienci` DISABLE KEYS */;
INSERT INTO `klienci` VALUES (1,'adam','malysz',123456789);
/*!40000 ALTER TABLE `klienci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sprzety`
--

DROP TABLE IF EXISTS `sprzety`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sprzety` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kategoria` varchar(255) NOT NULL DEFAULT 'brak',
  `model` varchar(255) NOT NULL DEFAULT 'brak',
  `opis` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sprzety`
--

LOCK TABLES `sprzety` WRITE;
/*!40000 ALTER TABLE `sprzety` DISABLE KEYS */;
INSERT INTO `sprzety` VALUES (3,'f','s',''),(4,'asd','asd',''),(5,'dsa','dsa',''),(6,'asd','asd',''),(7,'dsads','s','');
/*!40000 ALTER TABLE `sprzety` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wydania`
--

DROP TABLE IF EXISTS `wydania`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wydania` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_sprzetu` int(11) NOT NULL,
  `id_klienta` int(11) NOT NULL,
  `data_wydania` date NOT NULL,
  `data_zwrotu` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_sprzetu` (`id_sprzetu`),
  KEY `id_klienta` (`id_klienta`),
  CONSTRAINT `wydania_ibfk_1` FOREIGN KEY (`id_sprzetu`) REFERENCES `sprzety` (`id`),
  CONSTRAINT `wydania_ibfk_2` FOREIGN KEY (`id_klienta`) REFERENCES `klienci` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wydania`
--

LOCK TABLES `wydania` WRITE;
/*!40000 ALTER TABLE `wydania` DISABLE KEYS */;
INSERT INTO `wydania` VALUES (5,3,1,'2023-02-08',NULL),(6,5,1,'2023-02-08',NULL);
/*!40000 ALTER TABLE `wydania` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-07 23:02:20
