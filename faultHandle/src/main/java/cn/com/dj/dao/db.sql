CREATE TABLE `heat_beat` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `ip` VARCHAR(45) NULL COMMENT '机器的IP地址',
 `date` DATETIME NULL COMMENT '上报时间',
 `cpu_rate` DOUBLE NULL COMMENT 'cpu load',
 PRIMARY KEY (`id`),
 UNIQUE INDEX `ip_UNIQUE` (`ip` ASC
 ));