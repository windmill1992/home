package com.guangde.home.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 系统参数
 * todo,修改为每5分钟，更新一次系统变量
 *
 */
public class BGConfig {
	private static final Map<String,String> configuration = new HashMap<String, String>();
	static{
		    Properties param = new Properties();
	        ClassLoader loader = Thread.currentThread().getContextClassLoader();
	        InputStream stream = loader.getResourceAsStream("META-INF/SystemParameter.properties");
	        try {
	            param.load(stream);
	            configuration.putAll(new HashMap<String, String>((Map)param));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	        	if(stream!=null){
	        		try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	}
	        }
	 }
	public static String get(String key){
		return configuration.get(key);
	}
}
