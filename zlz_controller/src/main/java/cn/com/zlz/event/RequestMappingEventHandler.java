/*    */ package cn.com.zlz.event;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.web.context.support.ServletRequestHandledEvent;
/*    */ 
/*    */ @Component
/*    */ public class RequestMappingEventHandler
/*    */   implements ApplicationListener<ServletRequestHandledEvent>
/*    */ {
/* 12 */   private static Logger logger = LoggerFactory.getLogger(RequestMappingEventHandler.class);
/*    */ 
/*    */   public void onApplicationEvent(ServletRequestHandledEvent event)
/*    */   {
/* 16 */     logger.debug("uri: {}, method: {}, time: {}", new Object[] { event.getRequestUrl(), event.getMethod(), Long.valueOf(event.getProcessingTimeMillis()) });
/*    */   }
/*    */ }

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.event.RequestMappingEventHandler
 * JD-Core Version:    0.6.1
 */