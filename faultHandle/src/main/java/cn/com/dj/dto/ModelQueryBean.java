/*    */ package cn.com.dj.dto;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bson.types.ObjectId;
/*    */ 
/*    */ public class ModelQueryBean
/*    */ {
/*  8 */   private Integer verbose = Integer.valueOf(0);
/*  9 */   private Integer cursor = Integer.valueOf(0);
/* 10 */   private Integer limit = Integer.valueOf(10);
/*    */   private String name;
/*    */   private Boolean gateway;
/*    */   private Long startTime;
/*    */   private Long endTime;
/*    */   private List<ObjectId> resourceIds;
/*    */ 
/*    */   public Integer getVerbose()
/*    */   {
/* 18 */     return this.verbose;
/*    */   }
/*    */ 
/*    */   public void setVerbose(Integer verbose) {
/* 22 */     this.verbose = verbose;
/*    */   }
/*    */ 
/*    */   public Integer getCursor() {
/* 26 */     return this.cursor;
/*    */   }
/*    */ 
/*    */   public void setCursor(Integer cursor) {
/* 30 */     this.cursor = cursor;
/*    */   }
/*    */ 
/*    */   public Integer getLimit() {
/* 34 */     return this.limit;
/*    */   }
/*    */ 
/*    */   public void setLimit(Integer limit) {
/* 38 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 46 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public Boolean getGateway() {
/* 50 */     return this.gateway;
/*    */   }
/*    */ 
/*    */   public void setGateway(Boolean gateway) {
/* 54 */     this.gateway = gateway;
/*    */   }
/*    */ 
/*    */   public List<ObjectId> getResourceIds() {
/* 58 */     return this.resourceIds;
/*    */   }
/*    */ 
/*    */   public void setResourceIds(List<ObjectId> resourceIds) {
/* 62 */     this.resourceIds = resourceIds;
/*    */   }
/*    */ 
/*    */   public Long getStartTime() {
/* 66 */     return this.startTime;
/*    */   }
/*    */ 
/*    */   public void setStartTime(Long startTime) {
/* 70 */     this.startTime = startTime;
/*    */   }
/*    */ 
/*    */   public Long getEndTime() {
/* 74 */     return this.endTime;
/*    */   }
/*    */ 
/*    */   public void setEndTime(Long endTime) {
/* 78 */     this.endTime = endTime;
/*    */   }
/*    */ }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.dto.ModelQueryBean
 * JD-Core Version:    0.6.1
 */