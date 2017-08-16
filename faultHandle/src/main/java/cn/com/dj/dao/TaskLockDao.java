package cn.com.dj.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/*
##定时任务锁机制
CREATE TABLE `task_lock` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lock_time` DATETIME NOT NULL DEFAULT now(),
  `ip` VARCHAR(128) NULL,
  PRIMARY KEY (`id`));

* */
public interface TaskLockDao {
    /**
     *
     * @param date
     * @return
     */
    @Update("update task_lock set lock_time = now(),ip = #{ip} where lock_time > #{date}")
    public int lock(@Param("date") Date date, @Param("ip") String ip);
}
