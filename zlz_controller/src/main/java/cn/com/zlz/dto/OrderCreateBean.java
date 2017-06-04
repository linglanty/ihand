package cn.com.zlz.dto;

import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;

public class OrderCreateBean {
	
	 	@NotNull
	    private String name;
	    private int timeZone = 8;
	    private String serialNumber;
	  
	    @NotNull
	    private ObjectId orderId;
	    private String order;
	  
	    public String getName()
	    {
	      return this.name;
	    }
	  
	    public void setName(String name) {
	      this.name = name;
	    }
	  
	    public int getTimeZone() {
	      return this.timeZone;
	    }
	  
	    public void setTimeZone(int timeZone) {
	      this.timeZone = timeZone;
	    }
	  
	    public String getSerialNumber() {
	      return this.serialNumber;
	    }
	  
	    public void setSerialNumber(String serialNumber) {
	      this.serialNumber = serialNumber;
	    }

		public ObjectId getOrderId() {
			return orderId;
		}

		public void setOrderId(ObjectId orderId) {
			this.orderId = orderId;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}
	    
	  
	
}
