/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.test.service;

import cn.com.inhand.common.service.MongoService;
import cn.com.test.dao.SiteDao;
import cn.com.test.dto.SiteBean;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhangyf
 */
@Service
public class SiteService extends MongoService implements SiteDao{
    private String collectionName = "site";

    @Override
    public List<SiteBean> getData(String siteName, ObjectId oid) {
       
        MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
        Query query = new Query();
        /**
         * 查询条件
         */
        query.addCriteria(Criteria.where("name").regex(".*?" + siteName.trim() + ".*"));
        
        List<SiteBean> list = mt.find(query, SiteBean.class, this.collectionName);// 获取查询数据记录信息
 
        return list;
    }
}
