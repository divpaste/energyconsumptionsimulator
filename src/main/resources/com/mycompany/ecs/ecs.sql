-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: escdb
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user_appliances`
--

DROP TABLE IF EXISTS `user_appliances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_appliances` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `Fan` tinyint DEFAULT '0',
  `Air_Conditioner` tinyint DEFAULT '0',
  `Heater` tinyint DEFAULT '0',
  `Led` tinyint DEFAULT '0',
  `Fridge` tinyint DEFAULT '0',
  `Microwave` tinyint DEFAULT '0',
  `Kettle` tinyint DEFAULT '0',
  `Laptop` tinyint DEFAULT '0',
  `Television` tinyint DEFAULT '0',
  `Washing_Machine` tinyint DEFAULT '0',
  `Iron` tinyint DEFAULT '0',
  `Vaccum_Cleaner` tinyint DEFAULT '0',
  `Blender` tinyint DEFAULT '0',
  `Toaster` tinyint DEFAULT '0',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_appliances`
--

LOCK TABLES `user_appliances` WRITE;
/*!40000 ALTER TABLE `user_appliances` DISABLE KEYS */;
INSERT INTO `user_appliances` VALUES ('amey','123',0,1,1,0,0,0,1,0,0,1,0,1,0,1),('arnav','123',0,0,0,0,0,0,0,0,1,1,0,0,0,0),('div','3108',0,0,1,0,0,0,0,0,0,0,0,0,0,0),('parv','parv',0,0,0,0,0,0,0,1,0,0,1,0,1,1);
/*!40000 ALTER TABLE `user_appliances` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-04  1:07:09
