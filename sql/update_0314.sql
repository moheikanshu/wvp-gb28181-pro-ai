
DROP TABLE IF EXISTS `device_algorithm`;
CREATE TABLE `device_algorithm` (
    `id` int NOT NULL AUTO_INCREMENT,
    `deviceId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `algorithmId` int NOT NULL,
    PRIMARY KEY (`id`),
    KEY `deviceId` (`deviceId`),
    KEY `algorithmId` (`algorithmId`),
    CONSTRAINT `device_algorithm_ibfk_1` FOREIGN KEY (`deviceId`) REFERENCES `device` (`deviceId`) ON DELETE CASCADE,
    CONSTRAINT `device_algorithm_ibfk_2` FOREIGN KEY (`algorithmId`) REFERENCES `ai_device` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

alter table device add `algorithm` int DEFAULT '0';