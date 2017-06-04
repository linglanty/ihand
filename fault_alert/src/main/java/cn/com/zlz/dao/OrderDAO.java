package cn.com.zlz.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.zlz.model.Order;
import cn.com.zlz.dto.OrderQueryBean;

public abstract interface OrderDAO {

	public abstract Order getOrderById(ObjectId paramObjectId1,
			ObjectId paramObjectId2);

	public abstract void deleteOrderById(ObjectId paramObjectId1,
			ObjectId paramObjectId2);

	public abstract void updateOrder(ObjectId paramObjectId, Order paramOrder);

	public abstract void createOrder(ObjectId paramObjectId, Order paramOrder);

	public abstract List<Order> getOrders(ObjectId paramObjectId,
			List<ObjectId> paramOrder);
	
	public abstract void createOrders(ObjectId paramObjectId,
			List<Order> paramList);

	public abstract List<Order> getOrdersByEmployeeId(ObjectId paramObjectId1,
			String paramObjectId2);
	
	public abstract List<Order> getOrdersByEmployeeId(ObjectId paramObjectId1,
			String paramObjectId2, long st_time, long en_time);
	
	public abstract List<Order> getOrdersByEmployeeName(ObjectId paramObjectId1,
			String userName);

	public abstract List<Order> getOrdersByMachineId(ObjectId paramObjectId1, ObjectId paramObjectId2);
	
	public abstract List<Order> getOrdersByMachineName(ObjectId paramObjectId1, String paramObjectId2);
		
	public abstract List<Order> getOrders(ObjectId oId, OrderQueryBean queryBean, 
			int verbose, int skip, int limit);
	
	public abstract List<Order> getOrdersByLocation(ObjectId paramObjectId, String location);
	
	public abstract List<Order> getOrdersIsReceived(ObjectId paramObjectId, boolean isReceived, 
			long start_time, long end_time, int skip, int limit, boolean useCursor);
	
	public abstract List<Order> getOrdersIsFinished(ObjectId paramObjectId, boolean isFinished,  
			long st_time, long en_time, int skip, int limit, boolean useCursor);

	public abstract List<Order> getOrdersArranged(ObjectId oId, String userName, 
			long st_time, long en_time, int skip, int limit, boolean useCursor);

	public abstract List<Order> getOrdersHistory(ObjectId oId, String userName, 
			long st_time, long en_time, int skip, int limit, boolean useCursor);

	public abstract List<Order> getOrdersIsRefused(ObjectId oId, boolean isRefused, 
			long st_time, long en_time, int skip, int limit, boolean useCursor);


	
	
	
}
