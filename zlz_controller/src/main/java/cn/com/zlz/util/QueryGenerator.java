 package cn.com.zlz.util;
 
 import cn.com.inhand.common.role.RoleType;
import cn.com.zlz.dto.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
 
 @Component
 public class QueryGenerator
 {
   public Query getQuery(Query query, OrderQueryBean qb)
   {
     addResourceIds(query, qb.getResourceIds());
     addName(query, qb.getName());

     addSN(query, qb.getSn());
     addBussinessState(query, qb.getState());
     addTime(query, qb.getStartTime(), qb.getEndTime());
     addGroupIds(query, qb.getRoleType(), qb.getGroupIds());
     return query;
   }
// 
//   public Query getQuery(Query query, EmployeeQueryBean qb) {
//	   
//     addResourceIds(query, qb.getResourceIds());
//     addName(query, qb.getName());
//     addAlias(query, qb.getAlias());
//     addSiteId(query, qb.getSiteId());
//     addAddress(query, qb.getAddress());
//     addSN(query, qb.getSn());
//     addModel(query, qb.getModel());
//     addBussinessState(query, qb.getState());
//     addTime(query, qb.getStartTime(), qb.getEndTime());
// 
//     return query;
//   }
// 
 
   
   public String regexFilter(String regex) {
     if (regex.equals("*")) {
       return "\\" + regex;
     }
     return regex;
   }
 
   public void addId(Query query, ObjectId id)
   {
     if (id != null)
       query.addCriteria(Criteria.where("_id").is(id));
   }
 
   public void addDeviceId(Query query, ObjectId deviceId)
   {
     if (deviceId != null)
       query.addCriteria(Criteria.where("deviceId").is(deviceId));
   }
 
   public void addGatway(Query query, Boolean gateway)
   {
     if (gateway != null)
       query.addCriteria(Criteria.where("gateway").is(gateway));
   }
 
   public void addExist(Query query)
   {
     query.addCriteria(Criteria.where("delete").is(Integer.valueOf(0)));
   }
 
   public void addResourceIds(Query query, List<ObjectId> resourceIds) {
     if (resourceIds != null)
       query.addCriteria(Criteria.where("_id").in(resourceIds));
   }
 
   public void addName(Query query, String name)
   {
     if (name != null)
       query.addCriteria(Criteria.where("name").regex(".*" + regexFilter(name) + ".*"));
   }
 
   public void addAlias(Query query, String alias)
   {
     if (alias != null)
       query.addCriteria(Criteria.where("alias").is(alias));
   }
 
   public void addSiteId(Query query, ObjectId siteId)
   {
     if (siteId != null)
       query.addCriteria(Criteria.where("siteId").is(siteId));
   }
 
   public void addHasGateway(Query query, Boolean hasGateway)
   {
     if (hasGateway != null)
       query.addCriteria(Criteria.where("gatewayId").exists(hasGateway.booleanValue()));
   }
 
   public void addHasMachine(Query query, Boolean hasMachine)
   {
     if (hasMachine != null) {
       List in = new ArrayList();
       in.add(null);
       in.add(new ArrayList());
       if (!hasMachine.booleanValue())
         query.addCriteria(Criteria.where("machineIdList").in(in));
       else
         query.addCriteria(Criteria.where("machineIdList").nin(in));
     }
   }
 
   public void addAddress(Query query, String address)
   {
     if (address != null)
       query.addCriteria(Criteria.where("address").regex(".*" + regexFilter(address) + ".*"));
   }
 
   public void addSN(Query query, String serialNumber)
   {
     if (serialNumber != null)
       query.addCriteria(Criteria.where("serialNumber").regex(".*" + regexFilter(serialNumber) + ".*"));
   }
 
   public void addModel(Query query, String model)
   {
     if (model != null)
       query.addCriteria(Criteria.where("model").regex(".*" + regexFilter(model) + ".*"));
   }
 
   public void addModelId(Query query, List<ObjectId> models)
   {
     if (models != null)
       query.addCriteria(Criteria.where("modelId").in(models));
   }
 
   public void addOnline(Query query, Integer online)
   {
     if ((online != null) && (
       (online.intValue() == 1) || (online.intValue() == 0)))
       query.addCriteria(Criteria.where("online").is(online));
   }
 
   public void addGroupIds(Query query, Integer roleType, List<ObjectId> groupIds)
   {
     if (roleType.intValue() > RoleType.ORGANIZATION_ADMINISTRATOR.getType())
       if (groupIds != null)
         query.addCriteria(new Criteria().orOperator(new Criteria[] { Criteria.where("groupId").is(null), Criteria.where("groupId").in(groupIds) }));
       else
         query.addCriteria(Criteria.where("groupId").is(null));
   }
 
   public void addGroupId(Query query, Integer roleType, ObjectId groupId)
   {
     if (roleType.intValue() > RoleType.ORGANIZATION_ADMINISTRATOR.getType())
       if (groupId != null)
         query.addCriteria(new Criteria().orOperator(new Criteria[] { Criteria.where("groupId").is(null), Criteria.where("groupId").is(groupId) }));
       else
         query.addCriteria(Criteria.where("groupId").is(null));
   }
 
   public void addBussinessState(Query query, Integer[] state)
   {
     if (state != null)
       query.addCriteria(Criteria.where("businessState").in(state));
   }
 
   public void withSortDESC(Query query, String field)
   {
     query.with(new Sort(Sort.Direction.DESC, new String[] { field }));
   }
 
   public void withSortASC(Query query, String field) {
     query.with(new Sort(Sort.Direction.ASC, new String[] { field }));
   }
 
   public void addTime(Query query, Long startTime, Long endTime) {
     if ((startTime != null) && (endTime != null))
       query.addCriteria(Criteria.where("createTime").gte(startTime).lte(endTime));
     else if ((startTime != null) && (endTime == null))
       query.addCriteria(Criteria.where("createTime").gte(startTime));
     else if ((startTime == null) && (endTime != null))
       query.addCriteria(Criteria.where("createTime").lte(endTime));
   }
 }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.util.QueryGenrator
 * JD-Core Version:    0.6.1
 */