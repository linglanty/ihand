/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.controller;

import cn.com.dj.dao.PumpDao;
import cn.com.dj.dto.Pump;
import cn.com.dj.dto.PumpCreateBean;
import cn.com.dj.log.LogCode;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jiang Du
 */
@Controller
@RequestMapping({ "api/pump" })
public class PumpController {
	private static Logger logger = LoggerFactory.getLogger(RuleController.class);

	@Autowired
	PumpDao pumpService;

	@Autowired
	ObjectMapper mapper;

	// list all of the pumps of a model
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Object getAllPumpOverviewInfo(
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "modelId", required = true) ObjectId modelId,
			@RequestParam(value = "oid", defaultValue = "54BCA345DA08A0075C000001") ObjectId oId) {
		// System.out.println("pump in...");
		List<Pump> list = pumpService.getPumpListByModelId(modelId, oId);
		// System.out.println("pump out...");

		return new BasicResultDTO((long) list.size(), 0, list.size(), list);
	}

	// get the pump info by the pump id
	@RequestMapping(value = { "/{id}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getPumpById(
			@PathVariable ObjectId id,
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "oid", defaultValue = "54BCA345DA08A0075C000001") ObjectId oId) {
		Pump pump = this.pumpService.getPumpById(id, oId);
		OnlyResultDTO result = new OnlyResultDTO();
		result.setResult(pump);
		return result;
	}

	@RequestMapping(value = { "" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Object add(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "0") int verbose,
			@RequestHeader(value = "X-API-OID", required = false) ObjectId xOId,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "oid", defaultValue = "54BCA345DA08A0075C000001") ObjectId oId,
			@Valid @RequestBody PumpCreateBean pumpCb) {
		if (this.pumpService.isPumpExists(oId, pumpCb.getPumpName())) {
			throw new ErrorCodeException(
					ErrorCode.RESOURCE_NAME_ALREADY_EXISTS,
					new Object[] { pumpCb.getPumpName() });
		}
		Pump pump = (Pump) this.mapper.convertValue(pumpCb, Pump.class);
		this.pumpService.createPump(pump, oId);
		logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, ruleId:{} ", oId, LogCode.CREATE_PUMP_OK, xUId, xUsername,
				xIp, pump.getId().toString());
		return new OnlyResultDTO(pump);
	}

	// update the pump info
	@RequestMapping(value = { "/{id}" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object update(
			@PathVariable ObjectId id,
			@RequestParam("access_token") String accessToken,
			@RequestBody Pump pump,
			@RequestParam(value = "oid", defaultValue = "54BCA345DA08A0075C000001") ObjectId oId,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType) {
		pump.setId(id);
		Pump oldPump = this.pumpService.getPumpById(id, oId);
		if (oldPump == null) {
			throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST, id);
		}
		if (pump.getPumpName() != null
				&& !pump.getPumpName().equals(oldPump.getPumpName())
				&& this.pumpService.isPumpExists(oId, pump.getPumpName())) {
			throw new ErrorCodeException(
					ErrorCode.RESOURCE_NAME_ALREADY_EXISTS,
					new Object[] { pump.getPumpName() });
		}
		this.pumpService.modifyPump(pump, oId);
		this.logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, ruleId:{} ", oId, LogCode.UPDATE_PUMP_OK, xUId, xUsername,
				xIp, pump.getId().toString());
		return new OnlyResultDTO(pump);
	}

	// delete a pump by the id
	@RequestMapping(value = { "/{id}" }, method = { org.springframework.web.bind.annotation.RequestMethod.DELETE })
	@ResponseBody
	public Object deleteById(
			@PathVariable ObjectId id,
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "oid", defaultValue = "54BCA345DA08A0075C000001") ObjectId oId,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType) {
		Pump pump = this.pumpService.getPumpById(id, oId);
		if (pump == null) {
			throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST,
					new Object[] { id });
		}
		this.pumpService.deletePump(id, oId);
		this.logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, pumpId:{} ", oId, LogCode.DELETE_PUMP_OK, xUId, xUsername,
				xIp, pump.getId().toString());
		OnlyResultDTO result = new OnlyResultDTO();
		Map<String, ObjectId> resultMap = new HashMap<String, ObjectId>();
		resultMap.put("id", id);
		result.setResult(resultMap);
		return result;
	}

}
