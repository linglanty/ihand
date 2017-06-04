package cn.com.inhand.alarm.service;

import cn.com.inhand.alarm.dao.AlarmDao;
import cn.com.inhand.alarm.dto.AlarmBean;
import cn.com.inhand.common.service.MongoService;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class AlarmService extends MongoService implements AlarmDao {

    private String collectionName = "alarms";

    public List<AlarmBean> getAlarmData(String params, ObjectId xOId) {
        List<AlarmBean> alarmList = new ArrayList<AlarmBean>();
        try {
            if (params != null && !params.equals("")) {
                JSONObject jsonObject = new JSONObject(params);
                int startTime = 0;
                int endTime = 0;
                String siteName = "";
                List<Integer> levelList = new ArrayList<Integer>();
                List<Integer> stateList = new ArrayList<Integer>();
                if (jsonObject.has("levels")) {
                    JSONArray levels = jsonObject.getJSONArray("levels");
                    if (levels != null && levels.length() > 0) {
                        for (int i = 0; i < levels.length(); i++) {
                            levelList.add(levels.getInt(i));
                        }
                    }
                }
                if (jsonObject.has("status")) {
                    JSONArray state = jsonObject.getJSONArray("status");
                    if (state != null && state.length() > 0) {
                        for (int i = 0; i < state.length(); i++) {
                            stateList.add(state.getInt(i));
                        }
                    }
                }
                if (jsonObject.has("start_time")) {
                    try {
                        startTime = jsonObject.getInt("start_time");
                    } catch (Exception e) {
                        startTime = 0;
                    }
                }
                if (jsonObject.has("end_time")) {
                    try {
                        endTime = jsonObject.getInt("end_time");
                    } catch (Exception e) {
                        endTime = 0;
                    }
                }
                if (jsonObject.has("site_name")) {
                    siteName = jsonObject.getString("site_name").replace("null", "");
                }
                MongoTemplate mt = factory.getMongoTemplateByOId(xOId);
                Query query = new Query();
                if (levelList != null && levelList.size() > 0) {
                    query.addCriteria(Criteria.where("level").in(levelList));
                }
                if (stateList != null && stateList.size() > 0) {
                    query.addCriteria(Criteria.where("state").in(stateList));
                }
                if (siteName != null && !"".equals(siteName.trim())) {
                    query.addCriteria(Criteria.where("siteName").regex(".*?" + siteName.trim() + ".*"));
                }
                if (startTime != 0 && endTime != 0) { // 查询制定时间之后的记录
                    query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
                } else if (endTime != 0) { // 查询指定时间之前的记录
                    query.addCriteria(Criteria.where("timestamp").lte(endTime));
                } else if (startTime != 0) {
                    query.addCriteria(Criteria.where("timestamp").gte(startTime));
                }
                alarmList = mt.find(query, AlarmBean.class, this.collectionName);// 获取导出信息
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alarmList;
    }

}
