/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.inhand.alarm.dao;

import cn.com.inhand.alarm.dto.AlarmBean;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author zhangyf
 */
public interface AlarmDao {
    public List<AlarmBean> getAlarmData(String params, ObjectId xOId) ;
}
