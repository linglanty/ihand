/*     */ package cn.com.dj.dto;
/*     */ 
/*     */ import cn.com.inhand.common.model.VarInfo;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.bson.types.ObjectId;
/*     */ 
/*     */ public class ModelCreateBean
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private String name;
/*     */ 
/*     */   @NotNull
/*     */   private Boolean gateway;
/*     */   private boolean system;
/*     */ 
/*     */   @NotNull
/*     */   private Integer sensorType;
/*     */   private String configFile;
/*     */   private int plcTypeId;
/*     */   private int ioType;
/*     */   private List<VarInfo> varInfo;
/*     */   private ObjectId icon;
/*     */   private Map<String, String> descriptions;
/*     */   private List<ObjectId> pics;
/*     */   private List<ObjectId> views;
/*     */ 
/*     */   public boolean isSystem()
/*     */   {
/*  28 */     return this.system;
/*     */   }
/*     */ 
/*     */   public void setSystem(boolean system) {
/*  32 */     this.system = system;
/*     */   }
/*     */ 
/*     */   public String getConfigFile() {
/*  36 */     return this.configFile;
/*     */   }
/*     */ 
/*     */   public void setConfigFile(String configFile) {
/*  40 */     this.configFile = configFile;
/*     */   }
/*     */ 
/*     */   public int getPlcTypeId() {
/*  44 */     return this.plcTypeId;
/*     */   }
/*     */ 
/*     */   public void setPlcTypeId(int plcTypeId) {
/*  48 */     this.plcTypeId = plcTypeId;
/*     */   }
/*     */ 
/*     */   public int getIoType() {
/*  52 */     return this.ioType;
/*     */   }
/*     */ 
/*     */   public void setIoType(int ioType) {
/*  56 */     this.ioType = ioType;
/*     */   }
/*     */ 
/*     */   public List<VarInfo> getVarInfo() {
/*  60 */     return this.varInfo;
/*     */   }
/*     */ 
/*     */   public void setVarInfo(List<VarInfo> varInfo) {
/*  64 */     this.varInfo = varInfo;
/*     */   }
/*     */ 
/*     */   public ObjectId getIcon() {
/*  68 */     return this.icon;
/*     */   }
/*     */ 
/*     */   public void setIcon(ObjectId icon) {
/*  72 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getDescriptions() {
/*  76 */     return this.descriptions;
/*     */   }
/*     */ 
/*     */   public void setDescriptions(Map<String, String> descriptions) {
/*  80 */     this.descriptions = descriptions;
/*     */   }
/*     */ 
/*     */   public List<ObjectId> getPics() {
/*  84 */     return this.pics;
/*     */   }
/*     */ 
/*     */   public void setPics(List<ObjectId> pics) {
/*  88 */     this.pics = pics;
/*     */   }
/*     */ 
/*     */   public List<ObjectId> getViews() {
/*  92 */     return this.views;
/*     */   }
/*     */ 
/*     */   public void setViews(List<ObjectId> views) {
/*  96 */     this.views = views;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 100 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/* 104 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public Boolean isGateway() {
/* 108 */     return this.gateway;
/*     */   }
/*     */ 
/*     */   public void setGateway(Boolean gateway) {
/* 112 */     this.gateway = gateway;
/*     */   }
/*     */ 
/*     */   public Integer getSensorType() {
/* 116 */     return this.sensorType;
/*     */   }
/*     */ 
/*     */   public void setSensorType(Integer sensorType) {
/* 120 */     this.sensorType = sensorType;
/*     */   }
/*     */ }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.dto.ModelCreateBean
 * JD-Core Version:    0.6.1
 */