/*     */ package cn.com.dj.service;
/*     */ 
/*     */ import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.com.dj.dao.ModelDao;
import cn.com.dj.dto.ModelQueryBean;
import cn.com.dj.util.QueryGenrator;
import cn.com.inhand.common.model.Model;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.service.Verbose;
import cn.com.inhand.common.util.UpdateUtils;
import cn.com.inhand.dn4.utils.DateUtils;
/*     */ 
/*     */ @Service
/*     */ public class ModelService extends MongoService
/*     */   implements ModelDao
/*     */ {
/*  31 */   private static final Logger logger = LoggerFactory.getLogger(ModelService.class);
/*     */ 
/*     */   @Autowired
/*     */   @Qualifier("modelVerbose")
/*     */   private Verbose verbose;
/*     */ 
/*     */   @Autowired
/*     */   private QueryGenrator queryGenrator;
/*     */ 
/*  41 */   public void createModel(ObjectId oId, Model model) { 
			  MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/*  42 */     model.setCreateTime(Long.valueOf(DateUtils.getUTC()));
/*  43 */     model.setDelete(Integer.valueOf(0));
/*  44 */     template.save(model, "model");
/*  45 */     logger.info("add model info to mongo id = " + model.getId());
/*     */   }
/*     */ 
/*     */   public Model getModelById(ObjectId oId, ObjectId modelId)
/*     */   {
/*  50 */     Assert.notNull(modelId);
/*  51 */     return getModel(oId, "_id", modelId);
/*     */   }
/*     */ 
/*     */   public Model getModelById(ObjectId oId, ObjectId id, int verbose)
/*     */   {
/*  56 */     Assert.notNull(id);
/*  57 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/*  58 */     Query query = this.verbose.getQuery(verbose);
/*  59 */     query.addCriteria(Criteria.where("_id").is(id));
/*  60 */     query.addCriteria(Criteria.where("delete").is(Integer.valueOf(0)));
/*  61 */     return (Model)template.findOne(query, Model.class, "model");
/*     */   }
/*     */ 
/*     */   public Model getModel(ObjectId oId, String field, Object value)
/*     */   {
/*  66 */     Assert.notNull(field);
/*  67 */     Assert.notNull(value);
/*  68 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/*  69 */     Query query = new Query();
/*  70 */     query.addCriteria(Criteria.where(field).is(value));
/*  71 */     query.addCriteria(Criteria.where("delete").is(Integer.valueOf(0)));
/*  72 */     return (Model)template.findOne(query, Model.class, "model");
/*     */   }
/*     */ 
/*     */   public void deleteModelById(ObjectId oId, ObjectId modelId)
/*     */   {
/*  77 */     Assert.notNull(modelId);
/*  78 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/*  79 */     Model model = (Model)template.findById(modelId, Model.class, "model");
/*  80 */     model.setDelete(Integer.valueOf(1));
/*  81 */     template.save(model, "model");
/*  82 */     logger.info("mark the delete model where model id = " + modelId.toString());
/*     */   }
/*     */ 
/*     */   public void updateModel(Model model)
/*     */   {
/*  87 */     Assert.notNull(model.getOid());
/*  88 */     updateModel(model.getOid(), model);
/*     */   }
/*     */ 
/*     */   public void updateModel(ObjectId oId, Model model)
/*     */   {
/*  93 */     model.setUpdateTime(Long.valueOf(DateUtils.getUTC()));
/*  94 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/*  95 */     Query query = BasicQuery.query(Criteria.where("_id").is(model.getId()));
/*  96 */     template.updateFirst(query, UpdateUtils.convertBeanToUpdate(model, new String[] { "_id" }), "model");
/*  97 */     logger.info("update model Info from mongo where id = " + model.getId());
/*     */   }
/*     */ 
/*     */   public List<Model> getModels(ObjectId oId, List<ObjectId> modelds)
/*     */   {
/* 102 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/* 103 */     return template.find(Query.query(Criteria.where("_id").in(modelds)), Model.class, "model");
/*     */   }
/*     */ 
/*     */   public List<Model> getModels(ObjectId oId, ModelQueryBean mqb, int verbose, int skip, int limit)
/*     */   {
/* 108 */     Assert.notNull(mqb);
/* 109 */     Query query = this.verbose.getQuery(verbose);
/* 110 */     this.queryGenrator.getQuery(query, mqb);
/* 111 */     this.queryGenrator.withSortDESC(query, "createTime");
/* 112 */     query.limit(limit);
/* 113 */     query.skip(skip);
/* 114 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/* 115 */     return template.find(query, Model.class, "model");
/*     */   }
/*     */ 
/*     */   public List<ObjectId> getModels(ObjectId oId, String model)
/*     */   {
/* 120 */     if (model == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/* 124 */     Pattern pattern = Pattern.compile(model);
/* 125 */     Query query = Query.query(Criteria.where("name").regex(pattern));
/*     */ 
/* 127 */     List<Model> models = template.find(query, Model.class, "model");
/* 128 */     List ids = new ArrayList();
/* 129 */     logger.debug("pattern {}", model);
/* 130 */     for (Model model1 : models) {
/* 131 */       logger.debug("model {}", model1.getName());
/* 132 */       ids.add(model1.getId());
/*     */     }
/* 134 */     return ids;
/*     */   }
/*     */ 
/*     */   public long getCount(ObjectId oId, ModelQueryBean mqb)
/*     */   {
/* 139 */     Assert.notNull(mqb);
/* 140 */     Query query = new Query();
/* 141 */     this.queryGenrator.getQuery(query, mqb);
/* 142 */     MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
/* 143 */     return template.count(query, "model");
/*     */   }
/*     */ 
/*     */   public boolean isModelNameExists(ObjectId oId, String name)
/*     */   {
/* 148 */     Query query = Query.query(Criteria.where("name").is(name));
/* 149 */     query.addCriteria(Criteria.where("delete").is(Integer.valueOf(0)));
/* 150 */     return exist(oId, query, "model");
/*     */   }
/*     */ 
/*     */   public boolean isFieldExists(ObjectId oId, String field, Object value)
/*     */   {
/* 155 */     return exist(oId, Query.query(Criteria.where(field).is(value)), "model");
/*     */   }
/*     */ 
/*     */   public int getNextPlcTypeId(ObjectId oId)
/*     */   {
/* 160 */     Query query = new Query();
/* 161 */     query.with(new Sort(new Sort.Order[] { new Sort.Order(Sort.Direction.DESC, "plcTypeId") }));
/* 162 */     query.fields().include("plcTypeId");
/* 163 */     Model model = (Model)this.factory.getMongoTemplateByOId(oId).findOne(query, Model.class, "model");
/* 164 */     if (model.getPlcTypeId().intValue() < 2000) {
/* 165 */       return 2000;
/*     */     }
/* 167 */     return model.getPlcTypeId().intValue() + 1;
/*     */   }
/*     */ }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.service.ModelService
 * JD-Core Version:    0.6.1
 */