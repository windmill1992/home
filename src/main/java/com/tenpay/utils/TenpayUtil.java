package com.tenpay.utils;





import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.guangde.entry.ApiPayConfig;
import com.tenpay.demo.H5Demo;


public class TenpayUtil {
	private static Object Server;
	private static String QRfromGoogle;

	/**
	 * 把对象转换成字符串
	 * @param obj
	 * @return String 转换成字符串,若对象为null,则返回空字符串.
	 */
	public static String toString(Object obj) {
		if(obj == null)
			return "";
		
		return obj.toString();
	}
	
	/**
	 * 把对象转换为int数值.
	 * 
	 * @param obj
	 *            包含数字的对象.
	 * @return int 转换后的数值,对不能转换的对象返回0。
	 */
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 获取当前日期 yyyyMMdd
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}
	
	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	/**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */

	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(null == request || null == response) {
			return "gbk";
		}
		
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		
		if(null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		
		return enc;
	}
	
	public  static String URLencode(String content){
		
		String URLencode;
		
		URLencode= replace(Server.equals(content), "+", "%20");
		
		return URLencode;
	}
	private static String replace(boolean equals, String string, String string2) {
		
		return null;
	}

	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if( null == date ) {
			return 0;
		}
		
		return date.getTime()/1000;
	}
	
	 public static String QRfromGoogle(String chl)
	    {
	        int widhtHeight = 300;
	        String EC_level = "L";
	        int margin = 0;
	        String QRfromGoogle;
	        chl = URLencode(chl);
	        
	        QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;
	       
	        return QRfromGoogle;
	    }

	/**
	 * 时间转换成字符串
	 * @param date 时间
	 * @param formatType 格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}
	
	
	/**
	 * 获取access_token。
	 * access_token是公众号的全局唯一票据，
	 * 公众号调用各接口时都需使用access_token。
	 * access_token的有效期目前为2个小时，需定时刷新，
	 * 重复获取将导致上次获取的access_token失效。
	 * 为了保密appsecrect，第三方需要一个access_token获取和刷新的中控服务器。
	 */
	public static String queryAccessToken() {
		String result = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&"
        		+ "appid="+H5Demo.appid+"&secret="+H5Demo.appsecret+"";
        // 调用接口返回json字符串
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			result = demoJson.getString("access_token");
			System.out.println(new Date()+"调用了access_token：：：：："+result);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取access_token。
	 * access_token是公众号的全局唯一票据，
	 * 公众号调用各接口时都需使用access_token。
	 * access_token的有效期目前为2个小时，需定时刷新，
	 * 重复获取将导致上次获取的access_token失效。
	 * 为了保密appsecrect，第三方需要一个access_token获取和刷新的中控服务器。
	 */
	public static String queryAccessToken(ApiPayConfig apiPayConfig) {
		String result = "";
		String appid,appsecret;
		if(apiPayConfig!=null){
			appid=apiPayConfig.getWeixinAppId();
			appsecret=apiPayConfig.getWeixinAppSecret();
		}
		else{
			appid=H5Demo.appid;
			appsecret=H5Demo.appsecret;
		}
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&"
        		+ "appid="+appid+"&secret="+appsecret+"";
        // 调用接口返回json字符串
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			result = demoJson.getString("access_token");
			System.out.println(new Date()+"调用了access_token：：：：："+result);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * jsapi_ticket是公众号用于调用微信JS接口的临时票据。
	 * jsapi_ticket的有效期为7200秒，通过access_token来获取
	 * 是access_token的有效时间来决定jsapi_ticket的有效期
	 */
	public static String queryJsapiTicket(String result) {
		String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+result+"&type=jsapi";
        // 调用接口返回json字符串
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			result = demoJson.getString("ticket");
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject httpRequest(String url, String requestMethod,
			String outputStr) {
		JSONObject demoJson = null;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setUseCaches(false);
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = http.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			demoJson = JSONObject.parseObject(message);
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return demoJson;
	}
}
	
	










