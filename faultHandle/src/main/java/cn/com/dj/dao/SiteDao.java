/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.dao;
 
import cn.com.dj.dto.SiteBean;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author zhangyf
 */
public interface SiteDao {
    public List<SiteBean> getData(String siteName, ObjectId oid) ;
    
    public SiteBean getSiteById(ObjectId sid,ObjectId oid);

}
