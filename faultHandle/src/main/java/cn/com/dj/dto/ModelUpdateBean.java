/*    */ package cn.com.dj.dto;
/*    */ 
/*    */ import cn.com.inhand.common.model.VarInfo;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bson.types.ObjectId;
/*    */ import org.springframework.data.annotation.Id;
/*    */ 
/*    */ public class ModelUpdateBean
/*    */ {
/*    */ 
/*    */   @JsonProperty("_id")
/*    */   @Id
/*    */   private ObjectId id;
/*    */   private String name;
/*    */   private List<VarInfo> varInfo;
/*    */   private ObjectId icon;
/*    */   private Map<String, String> descriptions;
/*    */   private List<ObjectId> pics;
/*    */   private List<ObjectId> views;
/*    */   private ObjectId owner;
/*    */ 
/*    */   public ObjectId getId()
/*    */   {
/* 24 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setId(ObjectId id) {
/* 28 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public List<VarInfo> getVarInfo() {
/* 40 */     return this.varInfo;
/*    */   }
/*    */ 
/*    */   public void setVarInfo(List<VarInfo> varInfo) {
/* 44 */     this.varInfo = varInfo;
/*    */   }
/*    */ 
/*    */   public ObjectId getIcon() {
/* 48 */     return this.icon;
/*    */   }
/*    */ 
/*    */   public void setIcon(ObjectId icon) {
/* 52 */     this.icon = icon;
/*    */   }
/*    */ 
/*    */   public Map<String, String> getDescriptions() {
/* 56 */     return this.descriptions;
/*    */   }
/*    */ 
/*    */   public void setDescriptions(Map<String, String> descriptions) {
/* 60 */     this.descriptions = descriptions;
/*    */   }
/*    */ 
/*    */   public List<ObjectId> getPics() {
/* 64 */     return this.pics;
/*    */   }
/*    */ 
/*    */   public void setPics(List<ObjectId> pics) {
/* 68 */     this.pics = pics;
/*    */   }
/*    */ 
/*    */   public List<ObjectId> getViews() {
/* 72 */     return this.views;
/*    */   }
/*    */ 
/*    */   public void setViews(List<ObjectId> views) {
/* 76 */     this.views = views;
/*    */   }
/*    */ 
/*    */   public ObjectId getOwner() {
/* 80 */     return this.owner;
/*    */   }
/*    */ 
/*    */   public void setOwner(ObjectId owner) {
/* 84 */     this.owner = owner;
/*    */   }
/*    */ }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.dto.ModelUpdateBean
 * JD-Core Version:    0.6.1
 */