package cn.com.zlz.controller;

import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dj.model.Fault;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;
import cn.com.inhand.common.log.BusinessLogger;
import cn.com.inhand.common.resource.ResourceMessageSender;
import cn.com.zlz.dao.EmployeeDAO;
import cn.com.zlz.dao.FaultDAO;
import cn.com.zlz.dto.EmployeeCreateBean;
import cn.com.zlz.dto.EmployeeFaultBean;
import cn.com.zlz.dto.EmployeeUpdateBean;
import cn.com.zlz.model.Employee;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping({ "employee" })
public class EmployeeController {

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
	
	@RequestMapping(value = { "/get/username" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getEmployeeByUserName (
			@RequestParam(value = "user_name", required = true) String userName,
//			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId) {
		
		Employee emp = this.employeeService.getEmployeeByUserName(oId, userName);
		
		return new OnlyResultDTO(emp);
	}

	@RequestMapping(value = { "/get/fault" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Object getEmployeesByFaultId(
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam(required = false, defaultValue = "0") int cursor,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "faultId", required = true) ObjectId faultId,
			@RequestParam(value = "oid", required = true) ObjectId oId) {

		System.out.println("my faultId: " + faultId);
		Fault fault = this.faultService.getFaultInfoById(faultId, oId);
		
		System.out.println("my fault: " + fault);
				
		List<EmployeeFaultBean> emps = this.employeeService.
				getEmployeeByFault(oId, fault);
		
		long total = emps.size();
		return new BasicResultDTO(total, cursor, limit, emps);
	}

	@RequestMapping(value = { "/delete/username" }, method = { org.springframework.web.bind.annotation.RequestMethod.DELETE })
	@ResponseBody
	public Object deleteEmployeeByUserName (
			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "user_name", required = true) String userName,
			@RequestParam(value = "oid", required = true) ObjectId oId) {
		
		
		Employee emp = this.employeeService.getEmployeeByUserName(oId, userName);
		System.out.println("email: " + userName);
		System.out.println(emp);
		this.employeeService.deleteEmployeeByUserName(oId, userName);
		
		return emp;
	}
	
	@RequestMapping(value = { "/create" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Object createEmployee(
//			@RequestParam("access_token") String accessToken,
			@RequestParam(required = false, defaultValue = "0") int verbose,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@Valid @RequestBody EmployeeCreateBean employeeCreateBean) {
		
		System.out.println(employeeCreateBean.getCertId());
		
		if (this.employeeService.isEmployeeCertIdExists(oId,
				employeeCreateBean.getCertId())) {
			throw new ErrorCodeException(
					ErrorCode.RESOURCE_NAME_ALREADY_EXISTS,
					new Object[] { employeeCreateBean.getCertId() });
		}
		
		Employee emp = (Employee) this.mapper.convertValue(employeeCreateBean,
				Employee.class);
		
		System.out.println(emp);
		
		emp.setoId(oId);

		this.employeeService.createEmployee(oId, emp);

		return new OnlyResultDTO(emp);
	}
	
	@RequestMapping(value = { "/update/username" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object updateEmployeeByCertId(
			@RequestParam(value="access_token", required = false) String accessToken,
			@RequestParam(required = true, value="username") String userName,
			@RequestParam(value = "cancel_site", required = false, defaultValue = "0") int cancelSite,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-OID", required = false) ObjectId xOId,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestHeader(value = "X-API-ACLS", required = false) List<ObjectId> xAcls,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestBody EmployeeUpdateBean employeeUpdateBean) {
		
		System.out.println(employeeUpdateBean);
		
		Employee emp = (Employee) this.mapper.convertValue(employeeUpdateBean,
				Employee.class);
		emp.setUserName(userName);
		
		System.out.println(emp);

		if(!this.employeeService.isEmployeeUserNameExists(oId, userName)) {
			
			System.out.println("create employee...");
			
			this.employeeService.createEmployee(oId, emp);
		}
		else {
			
			Employee oldEmp = this.employeeService.getEmployeeByUserName(oId, userName);
			
			if(!oldEmp.getCertId().equals(emp.getCertId())) {//更新为新的用户，则删除原用户
			
				System.out.println("new employee");
				
				this.employeeService.deleteEmployeeByCertId(oId, oldEmp.getCertId());
				
				emp.setoId(oId);
				this.employeeService.createEmployee(oId, emp);
			}
			else
				this.employeeService.updateEmployee(oId, emp);
		}

//		this.businessLogger.info(oId, LogCode.UPDATE_MACHINE_OK, xUId,
//				xUsername, xIp, new String[] { emp.getName() });

		return new OnlyResultDTO(this.employeeService.getEmployeeByUserName(oId, userName));
	}
	
	@RequestMapping(value = { "/update/location" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
	@ResponseBody
	public Object updateEmployeeByLocation(
			@RequestParam(value="access_token", required = false) String accessToken,
			@RequestParam(required = true, value="username") String userName,
			@RequestParam(value = "cancel_site", required = false, defaultValue = "0") int cancelSite,
			@RequestParam(required = false, defaultValue = "1") int verbose,
			@RequestHeader(value = "X-API-OID", required = false) ObjectId xOId,
			@RequestHeader(value = "X-API-USERNAME", required = false) String xUsername,
			@RequestHeader(value = "X-API-IP", required = false) String xIp,
			@RequestHeader(value = "X-API-UID", required = false) ObjectId xUId,
			@RequestHeader(value = "X-API-ACLS", required = false) List<ObjectId> xAcls,
			@RequestHeader(value = "X-API-ROLE-TYPE", required = false) Integer roleType,
			@RequestParam(value = "oid", required = true) ObjectId oId,
			@RequestBody EmployeeUpdateBean employeeUpdateBean) {
		
		System.out.println(employeeUpdateBean);
		
		Employee emp = (Employee) this.mapper.convertValue(employeeUpdateBean,
				Employee.class);
		emp.setUserName(userName);
		
		System.out.println(emp);
		
		this.employeeService.updateEmployeeByUserName(oId, emp);

//		this.businessLogger.info(oId, LogCode.UPDATE_MACHINE_OK, xUId,
//				xUsername, xIp, new String[] { emp.getName() });

		return new OnlyResultDTO(this.employeeService.getEmployeeByUserName(oId, userName));
	}

}
