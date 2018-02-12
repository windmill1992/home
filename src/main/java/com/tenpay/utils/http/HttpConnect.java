package com.tenpay.utils.http;


import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.net.URL;  
import java.net.URLConnection;  

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpConnect { 
	 private static final  Logger log=LoggerFactory.getLogger(HttpConnect.class);
	 private static HttpConnect httpConnect = new HttpConnect();
	    /**
	     * 工厂方法
	     * 
	     * @return
	     */
	    public static HttpConnect getInstance() {
	        return httpConnect;
	    }
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
      
        // 预定接口的返回处理，对特殊字符进行过滤
        public HttpResponse doGetStr(String url) {
    		String CONTENT_CHARSET = "GBK";
    		long time1 = System.currentTimeMillis();
    		HttpClient client = new HttpClient(connectionManager);  
            client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);  
            client.getHttpConnectionManager().getParams().setSoTimeout(55000);
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET); 
            HttpMethod method = new GetMethod(url);
            HttpResponse response = new HttpResponse();
            try {
				client.executeMethod(method);
				System.out.println("调接口返回的时间:"+(System.currentTimeMillis()-time1));
				response.setStringResult(method.getResponseBodyAsString());
			} catch (HttpException e) {
				method.releaseConnection();
				return null;
			} catch (IOException e) {
				method.releaseConnection();
				return null;
			}finally{
				method.releaseConnection();
			}
			return response;
    }
        public HttpResponse doPostStr(String url) {
    		String CONTENT_CHARSET = "UTF-8";
    		long time1 = System.currentTimeMillis();
    		HttpClient client = new HttpClient(connectionManager);  
            client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);  
            client.getHttpConnectionManager().getParams().setSoTimeout(55000);
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET); 
            HttpMethod method = new PostMethod(url);
            HttpResponse response = new HttpResponse();
            try {
				client.executeMethod(method);
				log.info("调接口返回的时间:"+(System.currentTimeMillis()-time1));
				response.setStringResult(method.getResponseBodyAsString());
			} catch (HttpException e) {
				method.releaseConnection();
				return null;
			} catch (IOException e) {
				method.releaseConnection();
				return null;
			}finally{
				method.releaseConnection();
			}
			return response;
    }
    
    /**
     * 
     * @param url 链接
     * @param formString 请求体
     * @throws IOException
     */
    public static void FormPost(URL url,String formString) throws IOException {
        URLConnection connection = url.openConnection();  
        connection.setDoOutput(true);  
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");  
        out.write(formString); // 向页面传递数据。post的关键所在！  
        out.flush();  
        out.close();  
        // 一旦发送成功，用以下方法就可以得到服务器的回应：  
        String sCurrentLine;  
        String sTotalString;  
        sCurrentLine = "";  
        sTotalString = "";  
        InputStream l_urlStream;  
        l_urlStream = connection.getInputStream();  
        // 传说中的三层包装阿！  
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(  
                l_urlStream));  
        while ((sCurrentLine = l_reader.readLine()) != null) {  
            sTotalString += sCurrentLine + "\r\n";
        }
    }  
}