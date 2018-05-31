package com.beeplay.core.utils;
/**
 * @author chenlongfei
 */
public class StringUtils {
	public static Boolean isNotBlank(String str)
	{
		if(str==null){
			return false;
		}
		else{
			if("".equals(str)){
				return false;
				
			}
		}
		return true;
		
	}
}
