package cn.com.zlz.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.zlz.model.*;
import cn.com.dj.model.*;
import cn.com.zlz.dto.EmployeeFaultBean;
import cn.com.zlz.dto.OrderQueryBean;

public abstract interface EmployeeDAO {
	
	public abstract List<EmployeeFaultBean> getEmployeeByLocation(ObjectId oId,
			String location);
	
	public abstract void updateEmployee(ObjectId oId, Employee employee);
	
	public abstract void createEmployee(ObjectId oId, Employee employee);
	
	public abstract void deleteEmployeeByCertId(ObjectId oId, String certId);
	
	public abstract void deleteEmployeeByUserName(ObjectId oId, String userName);
	
	public abstract Employee getEmployeeByCertId(ObjectId oId, String certId);
	
	public abstract Employee getEmployeeByUserName(ObjectId oId, String userName);
	
	public abstract boolean isEmployeeCertIdExists(ObjectId oId, String certId);

	public abstract void updateEmployeeByUserName(ObjectId oId, Employee employee);

	public abstract boolean isEmployeeUserNameExists(ObjectId oId, String userName);

	public abstract List<EmployeeFaultBean> getEmployeeByFault(ObjectId oId, Fault fault);

//	Employee getEmployeeByCertId(ObjectId oId, String certId);
	
	
}
