package cn.com.zlz.service;

import cn.com.dj.model.Fault;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;
import cn.com.zlz.dao.EmployeeDAO;
import cn.com.zlz.dto.EmployeeFaultBean;
import cn.com.zlz.model.Employee;
import cn.com.zlz.util.QueryGenerator;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService extends MongoService implements EmployeeDAO {
	
private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private QueryGenerator queryGenrator;
	
	private String collectionName = "employee";
	
	@Override
	public void deleteEmployeeByCertId(ObjectId oId, String certId) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		template.remove(Query.query(Criteria.where("certId").is(certId)), this.collectionName);
	}
	
	@Override
	public void deleteEmployeeByUserName(ObjectId oId, String userName) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		template.remove(Query.query(Criteria.where("userName").is(userName)), this.collectionName);
	}

	@Override
	public void updateEmployee(ObjectId oId, Employee employee) {
		// TODO Auto-generated method stub
//		Assert.notNull(employee.getCertId());
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		Query query = BasicQuery.query(Criteria.where("certId").is(employee.getCertId()));
		template.updateFirst(query, UpdateUtils.convertBeanToUpdate(employee, new String[] { "certId" }), this.collectionName);
//		logger.info("update order Info from mongo where id = " + employee.getCertId());
	}
	
	@Override
	public void updateEmployeeByUserName(ObjectId oId, Employee employee) {
		// TODO Auto-generated method stub
//		Assert.notNull(employee.getCertId());
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		Query query = BasicQuery.query(Criteria.where("userName").is(employee.getUserName()));
		template.updateFirst(query, UpdateUtils.convertBeanToUpdate(employee, new String[] { "userName" }), this.collectionName);
//		logger.info("update order Info from mongo where id = " + employee.getCertId());
	}
	
	@Override
	public void createEmployee(ObjectId oId, Employee employee) {
		// TODO Auto-generated method stub
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		 
		 template.save(employee, this.collectionName);
	}
	
	@Override
	public List<EmployeeFaultBean> getEmployeeByLocation(ObjectId oId, String location) {
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		List<Employee> emps = template.find(Query.query(Criteria.where("location").is(location)), Employee.class, this.collectionName);
		
		List<EmployeeFaultBean> result = new ArrayList<EmployeeFaultBean>();
		for(Employee emp : emps) {
			EmployeeFaultBean empBean = new EmployeeFaultBean(emp);
			empBean.setDistance(0.0f);
			result.add(empBean);
		}
		
		return result;
	}
	
	@Override
	public List<EmployeeFaultBean> getEmployeeByFault(ObjectId oId, Fault fault) {
		
		double longitude = fault.getLocation().getLongitude();
		double latitude = fault.getLocation().getLatitude();
		String major = fault.getRepairmanMajor();
		
		System.out.println("longitude: " + longitude);
		System.out.println("latitude: " + latitude);
		System.out.println(major);
		List<String> majorNeed = new ArrayList<String>();
		//输入以“，号隔开 “电气,机械”
		if(!major.contains(",")) 
			majorNeed.add(major);
		else {
			String[] sp = major.split(",");
			for(String m : sp)
				majorNeed.add(m);
		}
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		List<Employee> employeeList = template.find(new Query(), Employee.class);

		Map<Employee, Float> emp2Dis = new HashMap<Employee, Float>();
		for(Employee emp : employeeList) {
			float dis = (float) Math.sqrt((longitude-emp.getLongitude())*(longitude-emp.getLongitude())
					+ (latitude-emp.getLatitude())*(latitude-emp.getLatitude()));
			emp2Dis.put(emp, dis);
		}
		
		System.out.println("dis map: " + emp2Dis);
		
		List<Map.Entry<Employee, Float>> entryList = new ArrayList<Map.Entry<Employee, Float>>(emp2Dis.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<Employee, Float>>() {
			public int compare(Map.Entry<Employee, Float> entry1, Map.Entry<Employee, Float> entry2) {
				return entry1.getValue()>entry2.getValue() ? 1 : (entry1.getValue()==entry2.getValue() ? 0 : -1);
			}
		});
		
		List<EmployeeFaultBean> result = new ArrayList<EmployeeFaultBean>();
		for(int i = 0;i<10 && i<entryList.size();i++) {
			Employee emp = entryList.get(i).getKey();
			EmployeeFaultBean empBean = new EmployeeFaultBean(emp);
			empBean.set_id(empBean.getCertId());
			empBean.setDistance(entryList.get(i).getValue()*111);
			result.add(empBean);
		}
		
		return result;
	}
	
	@Override
	public Employee getEmployeeByCertId(ObjectId oId, String certId) {
		
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
	     
	     return (Employee)template.findOne(Query.query(Criteria.where("certId").is(certId)), Employee.class, this.collectionName);
	}
	
	@Override
	public Employee getEmployeeByUserName(ObjectId oId, String userName) {
		
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
	     
	     return (Employee)template.findOne(Query.query(Criteria.where("userName").is(userName)), Employee.class, this.collectionName);
	}
	
	@Override
	public boolean isEmployeeCertIdExists(ObjectId oId, String certId) {
		
		Query query = Query.query(Criteria.where("certId").is(certId));
	     return exist(oId, query, this.collectionName);
	}
	
	@Override
	public boolean isEmployeeUserNameExists(ObjectId oId, String userName) {
		
		Query query = Query.query(Criteria.where("userName").is(userName));
	     return exist(oId, query, this.collectionName);
	}

	public static void main(String[] args) {
		
		try {
			
			 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}
