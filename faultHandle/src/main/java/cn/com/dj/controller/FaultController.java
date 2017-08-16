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
	 * ?handles={0}&level&oid=54BCA345DA08A0075C000001&start_time=0&end_time=1500729407914&limit=20&verbose=100&access_token=8a06ccb5f84d8b67c99249723596632e
	 */
	/**
	 * 获取所有的故障信息 包括已处理的 按时间顺序来 
	 * @param accessToken
	 * @param handles 0: 未派工  1:已派工 2：已处理 999:所有
	 * @param startTime: 0:all
	 * @param endTime: 0:all
	 * @param levels: 级别：1：预警，2：故障
	 * @param oId
	 * @param limit per page index
	 * @param cursor the current index
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody 
    public Object getAllFaultsInfo(
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value = "levels", required=false, defaultValue="1,2") List<Integer> levels,
    		@RequestParam("handled") List<Integer> handles,
            @RequestParam("start_time") long startTime,
            @RequestParam("end_time") long endTime,
            @RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
            @RequestParam(required=false, defaultValue="10") int limit,
            @RequestParam(required=false) String site_name,
            @RequestParam(required=false, defaultValue="0") int cursor) throws UnsupportedEncodingException {
        FaultQueryBean queryBean=new FaultQueryBean();
        queryBean.setEndTime(endTime);
        queryBean.setStartTime(startTime);
        //处理级别
        queryBean.setStatus(handles);
        //告警级别
        queryBean.setLevels(levels);
        //编码问题
        if(StringUtils.isNotEmpty(site_name)) {
            site_name = new String(site_name.getBytes("ISO8859_1"),"utf-8");
        }
		queryBean.setSiteName(site_name);
		List<Fault> list = this.faultService.getFualtsBypage(oId,queryBean, cursor, limit);
        return new BasicResultDTO((long)list.size(), cursor, limit, list);
    }

	/**
	 * 通过id获取fault 的详细信息
	 * @param id
	 * @param accessToken
	 * @param oId
     * @return
     */
	@RequestMapping(value={"/{id}"}, method={RequestMethod.GET})
    @ResponseBody
    public Object getFaultById(
            @PathVariable ObjectId id,
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId) {
    	Fault fault = this.faultService.getFaultInfoById(id, oId);
    	OnlyResultDTO result = new OnlyResultDTO();
    	result.setResult(fault);
    	return result;
    }

    /**
     * 识别出fault后添加fault信息
     * @param accessToken
     * @param faultCreateBean
     * @param oId
     * @param xUsername
     * @param xIp
     * @param xUId
     * @param roleType
     * @return
     */
	@RequestMapping(value={""}, method={RequestMethod.POST})
    @ResponseBody
    public Object addFault(
            @RequestParam("access_token") String accessToken,
    		@Valid @RequestBody FaultCreateBean faultCreateBean,
            @RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
    		@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
            @RequestHeader(value="X-API-IP", required=false) String xIp,
    		@RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
            @RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType) {
		 Fault fault = this.mapper.convertValue(faultCreateBean, Fault.class);
    	this.faultService.createFault(fault, oId);
    	logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, faultId:{} ", oId, LogCode.CREATE_FAULT_OK, xUId, xUsername, xIp, new String[] { fault.getId().toString() });
    	return new OnlyResultDTO(fault);
    }

    /**
     * 处理故障
     * @param id
     * @param accessToken
     * @param oId
     * @param xUsername
     * @param xIp
     * @param xUId
     * @param roleType
     * @return
     */
	@RequestMapping(value={"/{id}"}, method={RequestMethod.DELETE})
    @ResponseBody
    public Object handleFault(@PathVariable ObjectId id,
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
            @RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp,
            @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
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
