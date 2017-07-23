package cn.com.dj.dao;

import cn.com.dj.dto.heartbeat.HeartBeatInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 CREATE TABLE `movie_text`.`heat_beat` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `ip` VARCHAR(45) NULL COMMENT '机器的IP地址',
 `date` DATETIME NULL COMMENT '上报时间',
 `cpu_rate` DOUBLE NULL COMMENT 'cpu load',
 PRIMARY KEY (`id`),
 UNIQUE INDEX `ip_UNIQUE` (`ip` ASC));

 * Created by dujiang02 on 17/7/23.
 */
public interface HeartBeatDao {

    String INSERT_KEYS = "ip,date,cpu_rate";
    String SELECT_KEYS = "ip,date,cpu_rate AS cpuRate";

    /**
     * 上传心跳信息到数据库
     * @param beatInfo
     * @return
     */
    @Insert("REPLACE INTO heat_beat("+ INSERT_KEYS +") values(#{ip},#{date},#{cpuRate})")
    public int submitHeatBeatInfo(HeartBeatInfo beatInfo);

    @Select("SELECT " + SELECT_KEYS + " FROM heat_beat where date > #{date}")
    public List<HeartBeatInfo> getValidMachine(Date date);

}
