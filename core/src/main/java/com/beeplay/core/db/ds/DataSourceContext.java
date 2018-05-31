package com.beeplay.core.db.ds;
/**
 * 动态数据源  切换用
 * @author chenlf
 */
public class DataSourceContext {
	public final static String DATA_SOURCE_READ = "dataSourceRead";

	public final static String DATA_SOURCE_WRITE = "dataSourceWrite";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerType(String customerType) {
	        contextHolder.set(customerType);  
	    }

	public static String getCustomerType() {
	        return contextHolder.get();  
	    }  
	      
	public static void clearCustomerType() {
	        contextHolder.remove();  
	    }
}
