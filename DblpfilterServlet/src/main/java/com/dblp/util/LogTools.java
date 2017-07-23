package com.dblp.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogTools {

	 static {
		 PropertyConfigurator.configure("G:\\Dblp filter2.0\\src\\log4j.properties");
		 }
	 
		public static Logger log = Logger.getLogger(com.dblp.util.LogTools.class);

}
