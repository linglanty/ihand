package cn.com.zlz.service;

import java.util.*;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.com.zlz.model.*;
import cn.com.inhand.common.model.Machine;
import cn.com.inhand.common.service.MongoFactory;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;
import cn.com.inhand.dn4.utils.DateUtils;
import cn.com.zlz.dao.OrderDAO;
import cn.com.zlz.dto.OrderQueryBean;
import cn.com.zlz.util.QueryGenerator;

import com.mongodb.Mongo;

@Service
public class OrderService extends MongoService

implements OrderDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private QueryGenerator queryGenrator;
	
	private String collectionName = "order";

	@Override
	public Order getOrderById(ObjectId oId, ObjectId id) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		System.out.println("template: " + template==null);
		
		return (Order)template.findById(id, Order.class, this.collectionName);
	}

	@Override
	public void deleteOrderById(ObjectId oId, ObjectId id) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		template.remove(Query.query(Criteria.where("_id").is(id)), this.collectionName);
	}

	@Override
	public void updateOrder(ObjectId oId, Order order) {
		// TODO Auto-generated method stub
		
		order.setUpdateTime(Long.valueOf(DateUtils.getUTC()));
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		Query query = BasicQuery.query(Criteria.where("_id").is(order.getId()));
		
		template.updateFirst(query, UpdateUtils.convertBeanToUpdate(order, new String[] { "_id" }), this.collectionName);
		
		logger.info("update order Info from mongo where id = " + order.getId());
	}

	@Override
	public void createOrder(ObjectId oId, Order order) {
		// TODO Auto-generated method stub
//		System.out.println(oId);
		System.out.println("oid: " + oId);
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		 
		 order.setCreateTime(Long.valueOf(DateUtils.getUTC()));
		 
		 template.save(order, this.collectionName);
	}

	@Override
	public List<Order> getOrders(ObjectId oId, List<ObjectId> orderIds) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		return template.find(Query.query(Criteria.where("id").in(orderIds)), Order.class, this.collectionName);
	}

	@Override
	public void createOrders(ObjectId oId, List<Order> orders) {
		// TODO Auto-generated method stub
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		template.insert(orders, this.collectionName);
	}

	@Override
	public List<Order> getOrdersByEmployeeId(ObjectId oId, String employeeCertId) {
		// TODO Auto-generated method stub
		Assert.notNull(employeeCertId);
		Query query = Query.query(Criteria.where("employeeCertId").is(employeeCertId));
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		
		return tempList;
	}
	
	@Override
	public List<Order> getOrdersArranged(ObjectId oId, String userName, 
			long st_time, long en_time, int skip, int limit, boolean useCursor) {
		// TODO Auto-generated method stub
//		Assert.notNull(userName);
		
		List<Order> tempList = new ArrayList<Order>();
		
		if(userName == null) {
			Query query = Query.query(Criteria.where("isReceived").is(true));
			query.addCriteria(Criteria.where("isFinished").is(false));
			query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
			
			tempList = this.factory.getMongoTemplateByOId(oId).
					find(query, Order.class, this.collectionName);

		}
		else {
			
			Query query1 = Query.query(Criteria.where("userName").is(userName));
			
			String certId = this.factory.getMongoTemplateByOId(oId)
					.findOne(query1, Employee.class, "employee")
					.getCertId();
			System.out.println("certId: " + certId);
			System.out.println(st_time + " " + en_time);
			
			Query query2 = Query.query(Criteria.where("finished").is(false));
			query2.addCriteria(Criteria.where("refused").is(false));
			query2.addCriteria(Criteria.where("employeeCertId").is(certId));
			query2.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
			
			this.queryGenrator.withSortDESC(query2, "createTime");
			if(useCursor) {
				query2.limit(limit);
				query2.skip(skip);
			}
			
			tempList = this.factory.getMongoTemplateByOId(oId)
					.find(query2, Order.class, this.collectionName);
			System.out.println(tempList.size());
		}
		
//		System.out.println);
		return tempList;
	}
	
	@Override
	public List<Order> getOrdersHistory(ObjectId oId, String userName, 
			long st_time, long en_time, int skip, int limit, boolean useCursor) {
		
		List<Order> tempList = new ArrayList<Order>();
		
		if(userName == null) {
			Query query = Query.query(Criteria.where("isFinished").is(true));
			query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
			
			tempList = this.factory.getMongoTemplateByOId(oId).
					find(query, Order.class, this.collectionName);

		}
		else {
			
			Query query1 = Query.query(Criteria.where("userName").is(userName));
			
			String certId = this.factory.getMongoTemplateByOId(oId)
					.findOne(query1, Employee.class, "employee")
					.getCertId();
			System.out.println("certId: " + certId);
			System.out.println(st_time + " " + en_time);
			
			Query query2 = Query.query(new Criteria().
					orOperator(Criteria.where("finished").is(true), Criteria.where("refused").is(true)));
			query2.addCriteria(Criteria.where("employeeCertId").is(certId));
			query2.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
			
			this.queryGenrator.withSortDESC(query2, "createTime");
			if(useCursor) {
				query2.limit(limit);
				query2.skip(skip);
			}
			
			tempList = this.factory.getMongoTemplateByOId(oId)
					.find(query2, Order.class, this.collectionName);
			System.out.println(tempList.size());
		}
		
//		System.out.println);
		return tempList;
	}
	
	
	@Override
	public List<Order> getOrdersByEmployeeId(ObjectId oId, String employeeCertId, long st_time, long en_time) {
		// TODO Auto-generated method stub
		Assert.notNull(employeeCertId);
		Query query = Query.query(Criteria.where("employeeCertId").is(employeeCertId));
		query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		
		return tempList;
	}

	@Override
	public List<Order> getOrdersByEmployeeName(ObjectId oId, String employeeName) {
		// TODO Auto-generated method stub
		Assert.notNull(employeeName);
		
		Query query = Query.query(Criteria.where("employeeName").is(employeeName));
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}

	@Override
	public List<Order> getOrdersByMachineId(ObjectId oId, ObjectId machineId) {
		// TODO Auto-generated method stub
		Assert.notNull(machineId);
		Query query = Query.query(Criteria.where("machineId").is(machineId));
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}

	@Override
	public List<Order> getOrdersByMachineName(ObjectId oId, String machineName) {
		// TODO Auto-generated method stub
		Assert.notNull(machineName);
		Query query = Query.query(Criteria.where("machineName").is(machineName));
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}

	@Override
	public List<Order> getOrders(ObjectId oId, OrderQueryBean queryBean, 
			int verbose, int skip, int limit) {
		// TODO Auto-generated method stub
		
		Query query = new Query();
	     this.queryGenrator.getQuery(query, queryBean);
	     this.queryGenrator.withSortDESC(query, "createTime");
	     query.limit(limit);
	     query.skip(skip);
	     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		return template.find(query, Order.class, this.collectionName);
	}

	@Override
	public List<Order> getOrdersByLocation(ObjectId oId, String location) {
		// TODO Auto-generated method stub
		
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		
		Query query = Query.query(Criteria.where("location").is(location));
		
		return template.find(query, Order.class);
	}

	@Override
	public List<Order> getOrdersIsReceived(ObjectId oId, boolean isReceived, long st_time, long en_time
			, int skip, int limit, boolean useCursor) {
		// TODO Auto-generated method stub
		Query query = Query.query(Criteria.where("received").is(isReceived));
		query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
		
		this.queryGenrator.withSortDESC(query, "createTime");
		if(useCursor) {
			query.limit(limit);
			query.skip(skip);
		}
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}

	@Override
	public List<Order> getOrdersIsFinished(ObjectId oId, boolean isFinished, 
			long st_time, long en_time, int skip, int limit, boolean useCursor) {
		// TODO Auto-generated method stub
		Query query = Query.query(Criteria.where("finished").is(isFinished));
		query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
		
		this.queryGenrator.withSortDESC(query, "createTime");
		if(useCursor) {
			query.limit(limit);
			query.skip(skip);
		}
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}
	
	@Override
	public List<Order> getOrdersIsRefused(ObjectId oId, boolean isRefused, 
			long st_time, long en_time, int skip, int limit, boolean useCursor) {
		// TODO Auto-generated method stub
		Query query = Query.query(Criteria.where("refused").is(isRefused));
		query.addCriteria(Criteria.where("createTime").gte(st_time).lte(en_time));
		
		this.queryGenrator.withSortDESC(query, "createTime");
		if(useCursor) {
			query.limit(limit);
			query.skip(skip);
		}
		
		System.out.println("refused oid: " + oId);
		System.out.println(this.collectionName);
		System.out.println(query.toString());
		
		List<Order> tempList = this.factory.getMongoTemplateByOId(oId).find(query, Order.class, this.collectionName);
		return tempList;
	}

	
	public static void main(String[] args) {
		
		try {
			
			/*
			 * {
    "_id" : ObjectId("55bc64ce78082ef5576fe6cd"),
    "machineId" : ObjectId("551b37812cdca5d4748beaa5"),
    "ruleId" : ObjectId("55bc638578082ef5576fe6cc"),
    "machineName" : "1PLC",
    "siteName" : "1号_韩咀煤矿",
    "machineAddress" : "西坡镇韩咀煤矿",
    "faultPhenomenon" : "合闸后电泵不起动",
    "faultReason" : "电压偏低,低于60%额定电压",
    "handleMethod" : "将电压调整到额定值",
    "repairmanMajor" : "电气",
    "createTime" : NumberLong(1429686137),
    "status" : 0,
    "vars" : [ 
        {
            "name" : "Uab_1",
            "_id" : "400021",
            "value" : 30.0000000000000000
        }, 
        {
            "name" : "Uab_2",
            "_id" : "400029",
            "value" : 30.0000000000000000
        }, 
        {
            "name" : "Uca_1",
            "_id" : "400023",
            "value" : 30.0000000000000000
        }, 
        {
            "name" : "Uca_2",
            "_id" : "400036",
            "value" : 30.0000000000000000
        }, 
        {
            "name" : "Ubc_1",
            "_id" : "400022",
            "value" : 30.0000000000000000
        }, 
        {
            "name" : "Ubc_2",
            "_id" : "400035",
            "value" : 30.0000000000000000
        }
    ]
}

			 */
			
			ObjectId oId = new ObjectId("0000000000000000000ABCDE");
			String certId = "450103198803071035";
			
			Order order = new Order();
			order
			.setId(ObjectId.get())
			.setMachineName("1PLC")
			.setSiteName("1号_韩咀煤矿")
			.setEmployeeCertId(certId)
			.setEmployeeName("郑立洲")
			.setReason("电压偏低,低于90%额定电压")
			.setPhenomenon("便秘")
			.setCreateTime(new Date().getTime())
			.setFaultTime(new Date().getTime())
			.setReceived(false)
			.setFinished(false)
			.setRefused(false);

			OrderService os = new OrderService();
			os.queryGenrator = new QueryGenerator();
			Mongo mongo=new Mongo("localhost",27017);
			
			
			Date st = new Date();
//			st.setYear(2014);
			st.setMonth(5);
			st.setDate(15);

		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
