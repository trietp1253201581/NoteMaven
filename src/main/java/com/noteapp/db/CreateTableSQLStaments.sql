/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  admin
 * Created: Nov 14, 2024
 */
use notelite db;
CREATE TABLE `users` (
  `name` varchar(45) DEFAULT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `birthday` date DEFAULT NULL,
  `school` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notes` (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `author` varchar(45) NOT NULL,
  `header` varchar(100) NOT NULL,
  `last_modified_date` date DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2060 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `note_filters` (
  `note_id` int NOT NULL,
  `filter` varchar(100) NOT NULL,
  PRIMARY KEY (`note_id`,`filter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `note_blocks` (
  `block_id` varchar(45) NOT NULL,
  `note_id` int DEFAULT NULL,
  `header` varchar(45) DEFAULT NULL,
  `block_type` varchar(45) DEFAULT NULL,
  `block_order` int DEFAULT NULL,
  PRIMARY KEY (`block_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `text_blocks` (
  `block_id` varchar(45) NOT NULL,
  `editor` varchar(45) NOT NULL,
  `content` longtext,
  PRIMARY KEY (`block_id`,`editor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `survey_blocks` (
  `block_id` varchar(45) NOT NULL,
  `editor` varchar(45) NOT NULL,
  `survey_map` json DEFAULT NULL,
  PRIMARY KEY (`block_id`,`editor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `sharenotes` (
  `note_id` int NOT NULL,
  `receiver` varchar(45) NOT NULL,
  `share_type` varchar(15) NOT NULL,
  PRIMARY KEY (`receiver`,`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

