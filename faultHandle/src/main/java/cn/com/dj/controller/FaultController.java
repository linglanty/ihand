/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.controller;

import cn.com.dj.dao.FaultDao;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultCreateBean;
import cn.com.dj.dto.FaultQueryBean;
import cn.com.dj.log.LogCode;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *  链接：http://pan.baidu.com/s/1mizERqg 密码：6241 项目文档地址
 * @author Jiang Du
 */
@Controller
@RequestMapping({"api/fault"})
public class FaultController {
    private static Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
	ObjectMapper mapper;

    @Resource(name = "faultService")
	FaultDao faultService;

    /**
	 * Request URL:http://120.209.233.188:8989/faultHandle/api/fault/list
	 * ?handled=--0--&level&oid=54BCA345DA08A0075C000001&start_time=0&end_time=1500729407914&limit=20&verbose=100&access_token=8a06ccb5f84d8b67c99249723596632e
	 */
	
	/**
	 * 获取所有的故障信息 包括已处理的 按时间顺序来 
	 * @param accessToken
	 * @param handled 0: 未派工  1:已派工 2：已处理 999:所有
	 * @param start_time: 0:all 
	 * @param end_time: 0:all
	 * @param level: 级别：1：预警，2：故障
	 * @param oId
	 * @param limit per page index
	 * @param cursor the current index
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody 
    public Object getAllFaultsInfo(@RequestParam("access_token") String accessToken,
    		@RequestParam(required=false, defaultValue="1,2") String level,
    		@RequestParam("handled") String handled,@RequestParam("start_time") long start_time,@RequestParam("end_time") long end_time,
            @RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,@RequestParam(required=false, defaultValue="10") int limit,
            @RequestParam(required=false) String site_name,
            @RequestParam(required=false, defaultValue="0") int cursor) throws UnsupportedEncodingException {
        FaultQueryBean queryBean=new FaultQueryBean();
        queryBean.setEndTime(end_time);
        queryBean.setStartTime(start_time);
        
        //处理级别
        List<Integer> handList=new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
			if(handled.indexOf(String.valueOf(i))>-1){
				handList.add(i);
			}
		}
        queryBean.setStatus(handList);
        
        //告警级别
        List<Integer> levelList=new ArrayList<Integer>();
        for (int i = 1; i < 3; i++){
			if(level.indexOf(String.valueOf(i))>-1){
				levelList.add(i);
			}
		}
        queryBean.setLevels(levelList);
        
        //编码问题
        if(StringUtils.isNotEmpty(site_name)) {
            site_name = new String(site_name.getBytes("ISO8859_1"),"utf-8");
        }
		queryBean.setSiteName(site_name);
		List<Fault> list = this.faultService.getFualtsBypage(oId,queryBean, cursor, limit);
        return new BasicResultDTO((long)list.size(), cursor, limit, list);
    }
	
	//通过id获取fault 的详细信息
	@RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object getFaultById(@PathVariable ObjectId id, @RequestParam("access_token") String accessToken, 
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId)
    {
    	Fault fault = this.faultService.getFaultInfoById(id, oId);
    	OnlyResultDTO result = new OnlyResultDTO();
    	result.setResult(fault);
    	return result;
    }
	
	//识别出fault后添加fault信息
	@RequestMapping(value={""}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Object addFault(@RequestParam("access_token") String accessToken,
    		@Valid @RequestBody FaultCreateBean faultCreateBean, @RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
    		@RequestHeader(value="X-API-USERNAME", required=false) String xUsername, @RequestHeader(value="X-API-IP", required=false) String xIp,
    		@RequestHeader(value="X-API-UID", required=false) ObjectId xUId, @RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType) {
		 Fault fault = (Fault)this.mapper.convertValue(faultCreateBean, Fault.class);
    	this.faultService.createFault(fault, oId);
    	this.logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, faultId:{} ", oId, LogCode.CREATE_FAULT_OK, xUId, xUsername, xIp, new String[] { fault.getId().toString() });
    	return new OnlyResultDTO(fault);
    }
	
	//处理fault
	@RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.DELETE})
    @ResponseBody
    public Object handleFault(@PathVariable ObjectId id,@RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp, @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType){
		Fault fault = this.faultService.getFaultInfoById(id, oId);
		if (fault==null) {
			throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST, new Object[] { id });
		}
		this.faultService.modifyFaultStatus(fault, oId);
		logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, faultId:{} ", oId, LogCode.UPDATE_RULE_OK, xUId, xUsername, xIp, id.toString());
    	return new OnlyResultDTO(fault);
    }
	
}
