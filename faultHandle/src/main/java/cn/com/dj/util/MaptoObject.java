package cn.com.dj.util;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class MaptoObject {

	/**
	 * convert a map to a obj
	 * 	 * @param map
	 * @param object
	 */
	public static void convert(Map<String,Object> map,Object object)
	{
		Set<String> keySet=map.keySet();
		for(String key:keySet)
		{
			Object value=map.get(key);
			setMethod(key, value,object);
		}
	}
	private static void setMethod(String method,Object value,Object object)
	{
		Class c;  
	    try  
	    {  
	      c = Class.forName(object.getClass().getName());  
	      String met = (String) method;  
	      met = met.trim();  
	      if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase()))  
	      {  
	        met = met.substring(0, 1).toUpperCase() + met.substring(1);  
	      }  
	      if (!String.valueOf(method).startsWith("set"))  
	      {  
	        met = "set" + met;
	      }  
	      Class types[] = new Class[1];
	      types[0] = Class.forName("java.lang.String");  
	      Method m = c.getMethod(met, types);  
	      m.invoke(object, value);  
	    }  
	    catch (Exception e)  
	    {  
	      // TODO: handle exception  
	      e.printStackTrace();  
	    }  
	
	}
}
