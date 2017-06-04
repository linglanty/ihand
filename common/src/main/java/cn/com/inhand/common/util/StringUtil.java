package cn.com.inhand.common.util;

public class StringUtil {
	
	
	/**
	 * @desc check char whether in the string
	 * @param str
	 * @param cr
	 * @return 
	 */
	public static boolean checkChar(String str,char cr){
		boolean has_=false;
		for(int i=0;i<str.length();i++){
			char c=str.charAt(i);
			if(c==cr){
				has_=true;
				continue;
			}
		}
		return has_;
	}
	
}
