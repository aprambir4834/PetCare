CREATE DATABASE  IF NOT EXISTS `petcare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `petcare`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: petcare
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shopname` varchar(45) NOT NULL,
  `shoplocation` varchar(45) NOT NULL,
  `shopphoto` varchar(450) NOT NULL,
  `gstnumber` varchar(45) NOT NULL,
  `shopemail` varchar(45) NOT NULL,
  `shopcontact` varchar(45) NOT NULL,
  `ownername` varchar(45) NOT NULL,
  `ownerphoto` varchar(450) NOT NULL,
  `ownercontact` varchar(45) NOT NULL,
  `owneremail` varchar(45) NOT NULL,
  `ownerdob` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `pass` varchar(415) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'JC Pets','Amritsar','Logo.png','1234567890','jackie@gmail.com','123456789','Bir','merii.jpg','8872553040','bir@gmail.com','2003-09-08','pending','123'),(2,'Jackie Pet Shop','Amritsar','managecompany.jpg','1234567890','jaks@gmail.com','1234567890','Bir','mngflat.png','1234567890','bir@gmail.com','2005-07-02','pending','123'),(3,'Charlie Pet Shop','Ludhiana','mngguard.png','1234567890','charlie@gmail.com','1234567890','Charlie Singh','mngflat.png','1234567890','bir@gmail.com','2015-10-20','pending','123'),(4,'Charlie Pet Shop','Amritsar,Punjab','Logo.png','1234567890','aprambir.bakshi4487@gmail.com','8872553040','Aprambir ','pp.jpeg','1234567890','aprambir.bakshi1234@gmail.com','2008-07-17','pending','123'),(5,'Jackie Pet Shop','Amritsar,Punjab','shop.png','1234567890','aprambir.bakshi1234@gmail.com','8872553040','Bir','notifacation.png','1234567890','bir@gmail.com','2025-04-26','pending','123');
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-02 11:36:16
