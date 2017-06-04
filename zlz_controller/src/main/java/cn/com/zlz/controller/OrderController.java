package cn.com.zlz.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dj.model.Fault;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.HandleExceptionController;
import cn.com.inhand.common.log.BusinessLogger;
import cn.com.inhand.common.resource.ResourceMessageSender;
import cn.com.inhand.dn4.utils.DateUtils;
import cn.com.zlz.dao.EmployeeDAO;
import cn.com.zlz.dao.FaultDAO;
import cn.com.zlz.dao.OrderDAO;
import cn.com.zlz.dto.OrderUpdateBean;
import cn.com.zlz.model.Employee;
import cn.com.zlz.model.Order;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping({ "order" })
public class OrderController extends HandleExceptionController {

	@Autowired
	OrderDAO orderService;

	@Autowired
	EmployeeDAO employeeService;
	
	@Autowired
	FaultDAO faultService;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	BusinessLogger businessLogger;

	@Autowired
	ResourceMessageSender resourceMessageSender;
	
//	final static String serverOid = "54BCA345DA08A0075C000001";

//	@Autowired
//	TokenClient tokenClient;	
	@RequestMapping(value = { "/create/fault" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Object createOrderByFault(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestParam(value = "serial_number", required = false) String serialNumber,
			@RequestParam(value = "remark", required = true) String remark,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestParam(value = "certId", required = true) String certId,
			@RequestParam(value = "faultId", required=true) ObjectId faultId) throws UnsupportedEncodingException {
		
		System.out.println("enter create function....");
		//remark = new String(remark.getBytes("ISO8859_1"),"utf8");
//		remark = new String("抓紧完成！".getBytes(), "utf8");
//		remark = new String(remark.getBytes("gb2312"), "utf8");
		Fault fault = this.faultService.getFaultInfoById(faultId, oId);
		Employee emp = this.employeeService.getEmployeeByCertId(oId, certId);
		
		Order order = new Order(); 
		long time = DateUtils.getUTC();
		order.setCreateTime(time);
		order.setEmployeeCertId(emp.getCertId());
		order.setEmployeeName(emp.getName());
		order.setFaultId(faultId);
		order.setFaultTime(fault.getCreateTime());
		order.setMachineId(fault.getMachineId());
		order.setMachineName(fault.getMachineName());
		order.setOid(oId);
		order.setPhenomenon(fault.getFaultPhenomenon());
		order.setReason(fault.getFaultReason());
		order.setSiteName(fault.getSiteName());
		order.setUpdateTime(time);
		order.setRemark(remark);
		order.setPumpId(fault.getPumpId());
		order.setPumpName(fault.getPumpName());

		System.out.println("my order: " + order);
		
		this.orderService.createOrder(oId, order);
		
		fault.setStatus(1);
		this.faultService.modifyFaultStatus(fault, oId);
		
		return order;
	}


	@RequestMapping(value = { "/get/employeename/{employeeName}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersByEmployeeName(
			
			@PathVariable String employeeName,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId
			) {
		// logger.debug("Get device info of {} by user {}", id, xUsername);

		List<Order> orders = this.orderService.getOrdersByEmployeeName(oId, employeeName);
		
		long total = orders.size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/employeeid" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersByEmployeeId(
//			@PathVariable String employeeId,
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "verbose", required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestParam(value = "start_time", required = false) long start_time,
			@RequestParam(value = "end_time", required = false) long end_time,
//			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam (value = "employeeId", required = true) String employeeId,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		
		List<Order> orders = this.orderService.getOrdersByEmployeeId(oId, employeeId, start_time, end_time);
		long total = orders.size();		
		
		return new BasicResultDTO(total, cursor, limit, orders);
	}
	
	@RequestMapping(value = { "/get/id" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrderById(
//			@PathVariable String employeeId,
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "verbose", required = false, defaultValue = "1") int verbose,
//			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam (value = "_id", required = true) ObjectId id,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		Order order = this.orderService.getOrderById(oId, id);
		
		return new OnlyResultDTO(order);
	}
	
	@RequestMapping(value = { "/get/finish/username" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersFinishByUserName(
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "verbose", required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestParam(value = "start_time", required = false) long start_time,
			@RequestParam(value = "end_time", required = false) long end_time,
//			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam (value = "username", required = true) String userName,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		List<Order> orders = this.orderService.getOrdersHistory(oId, userName, 
				start_time, end_time, cursor, limit, true);
		long total = this.orderService.getOrdersHistory(oId, userName, 
				start_time, end_time, cursor, limit, false).size();		
		
		return new BasicResultDTO(total, cursor, limit, orders);
	}
	
	@RequestMapping(value = { "/get/arrange/username" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersArrangeByUserName(
			@RequestParam("access_token") String accessToken,
			@RequestParam(value = "verbose", required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestParam(value = "start_time", required = false) long start_time,
			@RequestParam(value = "end_time", required = false) long end_time,
			@RequestParam (value = "username", required = true) String userName,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		System.out.println("get order arrange...");
		List<Order> orders = this.orderService.getOrdersArranged(oId, userName,
				start_time, end_time, cursor, limit, true);
		long total = this.orderService.getOrdersArranged(oId, userName, 
				start_time, end_time, cursor, limit, false).size();		
		
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/received" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersIsReceived(
			@RequestParam(value = "received", required = true) boolean received,
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "start_time", required = false) long startTime,
			@RequestParam(value = "end_time", required = false) long endTime,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		List<Order> orders = this.orderService.getOrdersIsReceived(oId, received, 
				startTime, endTime, cursor, limit, true);
		
		long total =  this.orderService.getOrdersIsReceived(oId, received, 
				startTime, endTime, cursor, limit, false).size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/finished" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersIsFinished(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestParam(required = false, value = "group_id") List<ObjectId> groupId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "start_time", required = false) long startTime,
			@RequestParam(value = "end_time", required = false) long endTime,
			@RequestParam(value = "finished", required = true) boolean isFinished,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		List<Order> orders = this.orderService.getOrdersIsFinished(oId, isFinished, 
				startTime, endTime, cursor, limit, true);
		
		long total = this.orderService.getOrdersIsFinished(oId, isFinished, 
				startTime, endTime, cursor, limit, false).size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}
	
	@RequestMapping(value = { "/get/refused" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrdersIsRefused(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
			@RequestParam(value = "cursor", required = false, defaultValue = "0") int cursor,
			@RequestParam(required = false, value = "group_id") List<ObjectId> groupId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "start_time", required = false) long startTime,
			@RequestParam(value = "end_time", required = false) long endTime,
			@RequestParam(value = "refused", required = true) boolean refused,
			@RequestParam(value = "oid", required = true) ObjectId oId){		

		List<Order> orders = this.orderService.getOrdersIsRefused(oId, refused, 
				startTime, endTime, cursor, limit, true);
		
		long total = this.orderService.getOrdersIsRefused(oId, refused, 
				startTime, endTime, cursor, limit, false).size();
		System.out.println("refuse order cursor: " + cursor);
		
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/location/{location}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrderByLocation(
			@PathVariable String location,
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestParam(required = false, value = "group_id") List<ObjectId> groupId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		List<Order> orders = this.orderService.getOrdersByLocation(oId, location);
		
		long total = orders.size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/machinename/{machineName}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrderByMachineName(
			@PathVariable String machineName,
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestParam(required = false, value = "group_id") List<ObjectId> groupId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId) {
		// logger.debug("Get device info of {} by user {}", id, xUsername);

		List<Order> orders = this.orderService.getOrdersByMachineName(oId, machineName);
		
		long total = orders.size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}

	@RequestMapping(value = { "/get/machineid/{machineId}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getOrderByMachineId(
			@PathVariable ObjectId machineId,
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestParam(required = false, value = "group_id") List<ObjectId> groupId,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = true) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId){

		List<Order> orders = this.orderService.getOrdersByMachineId(oId, machineId);
		
		long total = orders.size();
		return new BasicResultDTO(total, cursor, limit, orders);
	}
	
	@RequestMapping(value = { "/update/report/refuse" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object updateRefuseReasonByOrderId(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "orderId", required = true) ObjectId orderId,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestParam(value = "refuseReason", required = true) String refuseReason) {
		
		Order order = this.orderService.getOrderById(oId, orderId);
		
		try {
			refuseReason = new String(refuseReason.getBytes("ISO8859_1"),"utf-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		order.setRefuseReason(refuseReason);
		long time = DateUtils.getUTC();
		order.setUpdateTime(time);
		order.setRefuseTime(time);

		this.orderService.updateOrder(oId, order);

		return new OnlyResultDTO(this.orderService.getOrderById(oId, orderId));
	}
	
	@RequestMapping(value = { "/update/report/orderid" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object updateReportByOrderId(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "orderId", required = true) ObjectId orderId,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestParam(value = "report", required = true) String report,
			@RequestBody OrderUpdateBean orderUpdateBean) {
		
		try {
			//report = new String(report.getBytes("ISO8859_1"),"utf-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("order_update: " + orderUpdateBean);
		
		Order order = orderService.getOrderById(oId, orderId);
		order.setId(orderId);
		order.setReportContent(report);
		order.setFaultReason(orderUpdateBean.getFaultReason());
		order.setFaultType(orderUpdateBean.getFaultType());
		
		long time = new Date().getTime()/1000L;
		order.setReportUpdateTime(time);
		order.setUpdateTime(time);

		this.orderService.updateOrder(oId, order);

		return new OnlyResultDTO(this.orderService.getOrderById(oId, orderId));
	}
	
	@RequestMapping(value = { "/update" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object updateOrderById(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "orderId", required = true) ObjectId orderId,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestBody OrderUpdateBean orderUpdateBean) {
		
		Order order = (Order) this.mapper.convertValue(orderUpdateBean,
				Order.class);
		Order orderOld = this.orderService.getOrderById(oId, orderId);
		Fault fault = this.faultService.getFaultInfoById(orderOld.getFaultId(), oId);
		order.setId(orderId);
		
		long time = new Date().getTime()/1000L;
		if(order.isFinished()) 
			order.setFinishTime(time);
		if(order.isReceived()) {
			order.setReceiveTime(time);
			fault.setStatus(2);
		}
		if(order.isRefused()) {
			System.out.println("refused");
			order.setRefuseTime(time);
			fault.setStatus(0);
		}
		order.setUpdateTime(time);
		
		this.orderService.updateOrder(oId, order);
		System.out.println("fault id: " + fault.getId());
		this.faultService.modifyFaultStatus(fault, oId);

		return new OnlyResultDTO(this.orderService.getOrderById(oId, orderId));
	}

}
