package com.guangde.home.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过ip地址得到具体地址
 */
public class IPAddressUtil {
	
	Logger logger = LoggerFactory.getLogger(IPAddressUtil.class);
	/**
	 * 获取地址的实现方法
	 */
	public static String getAddress(String content,String encodingString) throws UnsupportedEncodingException{
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		String returnStr  = getResult(urlStr, content, encodingString);
		if (returnStr != null) {  
			   // 处理返回的省市区信息  
			   System.out.println(returnStr);  
			   String[] temp = returnStr.split(",");  
			   if(temp.length<3){  
			    return "0";//无效IP，局域网测试  
			   }  
			            String country = "";  //国家
			            String area = "";  //地区
			            String region = "";  //省份
			            String city = "";  //市区
			            String county = "";  //地区
			            String isp = "";  //isp公司
			            for (int i = 0; i < temp.length; i++) {  
			                switch (i) {  
			                case 1:  
			                    country = (temp[i].split(":"))[2].replaceAll("\"", "");  
			                    country = decodeUnicode(country);// 国家  
			                    break;  
			                    case 3:  
			                        area = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                        area = decodeUnicode(area);// 地区   
			                    break;  
			                    case 5:  
			                        region = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                        region = decodeUnicode(region);// 省份   
			                    break;   
			                    case 7:  
			                        city = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                        city = decodeUnicode(city);// 市区  
			                    break;   
			                    case 9:  
			                            county = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                            county = decodeUnicode(county);// 地区   
			                    break;  
			                    case 11:  
			                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                        isp = decodeUnicode(isp); // ISP公司  
			                    break;  
			                }  
			            }  
			     
			 
			   return region;  
			  }  
			  return null; 
	}
	
	 private static String getResult(String urlStr, String content, String encoding) {  
		  URL url = null;  
		  HttpURLConnection connection = null;  
		  try {  
		   url = new URL(urlStr);  
		   connection = (HttpURLConnection) url.openConnection();// 新建连接实例  
		   connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒  
		   connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒  
		   connection.setDoOutput(true);// 是否打开输出流 true|false  
		   connection.setDoInput(true);// 是否打开输入流true|false  
		   connection.setRequestMethod("POST");// 提交方法POST|GET  
		   connection.setUseCaches(false);// 是否缓存true|false  
		   connection.connect();// 打开连接端口  
		   DataOutputStream out = new DataOutputStream(connection  
		     .getOutputStream());// 打开输出流往对端服务器写数据  
		   out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx  
		   out.flush();// 刷新  
		   out.close();// 关闭输出流  
		   BufferedReader reader = new BufferedReader(new InputStreamReader(  
		     connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据  
		   // ,以BufferedReader流来读取  
		   StringBuffer buffer = new StringBuffer();  
		   String line = "";  
		   while ((line = reader.readLine()) != null) {  
		    buffer.append(line);  
		   }  
		   reader.close();  
		   return buffer.toString();  
		  } catch (IOException e) {  
			  System.out.println("IPAddressUtil >> IOException ");
		  } finally {  
		   if (connection != null) {  
		    connection.disconnect();// 关闭连接  
		   }  
		  }  
		  return null;  
		 }
	 
	 /**
	  * 地址的编码转换
	  */
	 public static String decodeUnicode(String theString) {  
		  char aChar;  
		  int len = theString.length();  
		  StringBuffer outBuffer = new StringBuffer(len);  
		  for (int x = 0; x < len;) {  
		   aChar = theString.charAt(x++);  
		   if (aChar == '\\') {  
		    aChar = theString.charAt(x++);  
		    if (aChar == 'u') {  
		     int value = 0;  
		     for (int i = 0; i < 4; i++) {  
		      aChar = theString.charAt(x++);  
		      switch (aChar) {  
		      case '0':  
		      case '1':  
		      case '2':  
		      case '3':  
		      case '4':  
		      case '5':  
		      case '6':  
		      case '7':  
		      case '8':  
		      case '9':  
		       value = (value << 4) + aChar - '0';  
		       break;  
		      case 'a':  
		      case 'b':  
		      case 'c':  
		      case 'd':  
		      case 'e':  
		      case 'f':  
		       value = (value << 4) + 10 + aChar - 'a';  
		       break;  
		      case 'A':  
		      case 'B':  
		      case 'C':  
		      case 'D':  
		      case 'E':  
		      case 'F':  
		       value = (value << 4) + 10 + aChar - 'A';  
		       break;  
		      default:  
		       throw new IllegalArgumentException(  
		         "Malformed      encoding.");  
		      }  
		     }  
		     outBuffer.append((char) value);  
		    } else {  
		     if (aChar == 't') {  
		      aChar = '\t';  
		     } else if (aChar == 'r') {  
		      aChar = '\r';  
		     } else if (aChar == 'n') {  
		      aChar = '\n';  
		     } else if (aChar == 'f') {  
		      aChar = '\f';  
		     }  
		     outBuffer.append(aChar);  
		    }  
		   } else {  
		    outBuffer.append(aChar);  
		   }  
		  }  
		  return outBuffer.toString();  
		 }  
	 
	 /**
	  * ip转num
	  * @param ipAddress
	  * @return
	  */
	 public static long ipToLong(String ipAddress) {  

		 long result = 0;  

		 String[] ipAddressInArray = ipAddress.split("\\.");  

		 for (int i = 3; i >= 0; i--) {  

			 long ip = Long.parseLong(ipAddressInArray[3 - i]); 
			 result |= ip << (i * 8);  
		 }  
		 return result;  
	 }  
	 

}
