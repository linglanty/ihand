/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.test.controller;

import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.test.dao.SiteDao;
import cn.com.test.dto.SiteBean; 
import java.util.List; 
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zhangyf
 */
@Controller
@RequestMapping({"api/test"})
public class SiteController {
    @Autowired
    private SiteDao alarmService;
 
   @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllDeviceOverviewInfo(@RequestParam("access_token") String accessToken, 
            @RequestParam(value="site_name", required=false) String siteName,   
            @RequestHeader(value="X-API-OID", required=false) ObjectId xOId, 
            @RequestHeader(value="X-API-USERNAME", required=false) String xUsername, 
            @RequestHeader(value="X-API-IP", required=false) String xIp, 
            @RequestHeader(value="X-API-UID", required=false) ObjectId xUId, 
            @RequestHeader(value="X-API-ACLS", required=false) List<ObjectId> xAcls, 
            @RequestHeader(value="X-API-ROLE-TYPE", required=true) Integer roleType, 
            @RequestParam(value="oid", required=true) ObjectId oId) { 
        
        List<SiteBean> list = alarmService.getData(siteName, oId);   
        return new BasicResultDTO((long)list.size(), 0, list.size(), list);
    }

}
