package com.tenpay.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.alibaba.fastjson.JSONObject;
import com.guangde.entry.ApiPayConfig;
import com.tenpay.utils.RequestHandler;
import com.tenpay.utils.TenpayUtil;
import com.tenpay.utils.http.HttpConnect;
import com.tenpay.utils.http.HttpResponse;


/**
 * @author ex_yangxiaoyi
 * 
 */
public class H5Demo {
	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	public static String appid = "wxa09ee42dbe779694";
	public static String appsecret = "c8c9005d568c7575770df85d9c92a87c";
	private static String partner = "1241263502";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = "12345QWERT12345QWERT1234567890qw";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
	private static String openId = "";
	//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	private static String notifyurl = "http://www.17xs.org/tenpay/async.do";// Key
	 //获取微信的code
	public static String GetCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	 //获取微信的access_token
	public static String GetAccess_tokenRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public static String GetAccess_token = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	public static String REDIRECT_URI = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId=374&payType=0&amount=0.01";
	//创建订单
	public static String UNI_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//微信小程序获取sessionkey
	public static String APPLET_APPID = "wxcb81ce7549d08030";
	public static String APPLET_APPSECRET = "b06f2328404268185c5455e74e94e237";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
//		String code = "041d8e2631da7b1248d25942d9009eaS";
//		getAccessTokenRequest(code);
//		getCodeRequest("222");
	    //扫码支付
//	    WxPayDto tpWxPay1 = new WxPayDto();
//	    tpWxPay1.setBody("商品信息");
//	    tpWxPay1.setOrderId("14405590661414911");
//	    tpWxPay1.setSpbillCreateIp("127.0.0.1");
//	    tpWxPay1.setTotalFee("0.01");
//	    tpWxPay1.setOpenId("oxmc3uBBzySbantAofWrdLQgYzq0");
//	    tpWxPay1.setNotifyUrl("http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId=374&payType=0&amount=0.01");
//	    tpWxPay1.setAttach("222");
//	    System.out.println(TenpayUtil.getCurrTime().substring(4));
//	    getCodeurl(tpWxPay1);
//	    try {
//	    	SortedMap<Object, Object> aaMap = Gopay(tpWxPay1);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		getCodeurl(tpWxPay1);
//		String Ticket = getjsTicket();
//		System.out.println("Ticket="+Ticket);
//		String tmsp =TenpayUtil.getCurrTime().substring(2,12);
//		System.out.println("tmsp="+tmsp);
//		String NonceStr =getNonceStr();
//		System.out.println("NonceStr="+NonceStr);
//		String url="http://www.17xs.org/visitorAlipay/weixindeposit.do?projectId=374&payType=0&amount=0.01";
//		getCodeurl();
	}
	
	/**
	 * 获取微信扫码支付二维码连接
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static String getCodeurl(WxPayDto tpWxPayDto) throws JDOMException, IOException{
		

		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = "JSAPI";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		String openid = tpWxPayDto.getOpenId();
		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("openid", openid);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<openid>" + openid
				+ "</openid>"+ "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		/*String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>"
				+ "</xml>";
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/orderquery";*/
		 System.out.println("setxml"+xml);
		 
		 /** 使用POST请求统一下单接口，获取预支付单号prepay_id */
	        HttpClient client = new HttpClient();
	        PostMethod myPost = new PostMethod(createOrderURL);
	        client.getParams().setSoTimeout(300 * 1000);
	        String result = null;
	        try {
	            myPost.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
	            int statusCode = client.executeMethod(myPost);
	            if (statusCode == HttpStatus.SC_OK) {
	                //使用流的方式解析微信服务器返回的xml结构的字符串
	                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
	                byte[] bytes = new byte[1024];
	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                int count = 0;
	                while ((count = bis.read(bytes)) != -1) {
	                    bos.write(bytes, 0, count);
	                }
	                byte[] strByte = bos.toByteArray();
	                result = new String(strByte, 0, strByte.length, "utf-8");
	                bos.close();
	                bis.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        /** 需要释放掉、关闭连接 */
	        myPost.releaseConnection();
	        client.getHttpConnectionManager().closeIdleConnections(0);
//		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
	        Map<String, String> map = doXMLParse(result);
//		System.out.println("code_url----------------"+code_url);
//		String[] msg =null;
//		if(StringUtils.isNotEmpty(code_url)){
//			msg = code_url.split("\\|");
//			if("1".equals(tpWxPayDto.getPayWay())){
				return map.get("prepay_id");
//			}
			
//		}
//		return msg[0];
	}
	
	/**
	 * 获取微信扫码支付二维码连接
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static String getAppletCodeurl(WxPayDto tpWxPayDto) throws JDOMException, IOException{
		

		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = "JSAPI";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		String openid = tpWxPayDto.getOpenId();
		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", APPLET_APPID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("openid", openid);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(APPLET_APPID, APPLET_APPSECRET, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + APPLET_APPID + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<openid>" + openid
				+ "</openid>"+ "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		 System.out.println("setxml"+xml);
		 
		 /** 使用POST请求统一下单接口，获取预支付单号prepay_id */
	        HttpClient client = new HttpClient();
	        PostMethod myPost = new PostMethod(createOrderURL);
	        client.getParams().setSoTimeout(300 * 1000);
	        String result = null;
	        try {
	            myPost.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
	            int statusCode = client.executeMethod(myPost);
	            if (statusCode == HttpStatus.SC_OK) {
	                //使用流的方式解析微信服务器返回的xml结构的字符串
	                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
	                byte[] bytes = new byte[1024];
	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                int count = 0;
	                while ((count = bis.read(bytes)) != -1) {
	                    bos.write(bytes, 0, count);
	                }
	                byte[] strByte = bos.toByteArray();
	                result = new String(strByte, 0, strByte.length, "utf-8");
	                bos.close();
	                bis.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        /** 需要释放掉、关闭连接 */
	        myPost.releaseConnection();
	        client.getHttpConnectionManager().closeIdleConnections(0);
	        Map<String, String> map = doXMLParse(result);
			return map.get("prepay_id");

	}
	
	/**
	 * 获取微信扫码支付二维码连接(对外)
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static String getCodeurlPublic(WxPayDto tpWxPayDto,ApiPayConfig apiPayConfig) throws JDOMException, IOException{
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = "JSAPI";

		// 商户号
		String mch_id,appidConfig,appsecretConfig,partnerkeyConfig;
		if(apiPayConfig!=null){
			mch_id=apiPayConfig.getWeixinPartner();
			appidConfig=apiPayConfig.getWeixinAppId();
			appsecretConfig=apiPayConfig.getWeixinAppSecret();
			partnerkeyConfig=apiPayConfig.getWeixinPartnerKey();
		}
		else{
			mch_id=partner;
			appidConfig=appid;
			appsecretConfig=appsecret;
			partnerkeyConfig=partnerkey;
		}
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		String openid = tpWxPayDto.getOpenId();
		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appidConfig);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("openid", openid);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appidConfig, appsecretConfig, partnerkeyConfig);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appidConfig + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<openid>" + openid
				+ "</openid>"+ "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		/*String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>"
				+ "</xml>";
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/orderquery";*/
		 System.out.println("setxml"+xml);
		 
		 /** 使用POST请求统一下单接口，获取预支付单号prepay_id */
	        HttpClient client = new HttpClient();
	        PostMethod myPost = new PostMethod(createOrderURL);
	        client.getParams().setSoTimeout(300 * 1000);
	        String result = null;
	        try {
	            myPost.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
	            int statusCode = client.executeMethod(myPost);
	            if (statusCode == HttpStatus.SC_OK) {
	                //使用流的方式解析微信服务器返回的xml结构的字符串
	                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
	                byte[] bytes = new byte[1024];
	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                int count = 0;
	                while ((count = bis.read(bytes)) != -1) {
	                    bos.write(bytes, 0, count);
	                }
	                byte[] strByte = bos.toByteArray();
	                result = new String(strByte, 0, strByte.length, "utf-8");
	                bos.close();
	                bis.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        /** 需要释放掉、关闭连接 */
	        myPost.releaseConnection();
	        client.getHttpConnectionManager().closeIdleConnections(0);
//		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
	        Map<String, String> map = doXMLParse(result);
//		System.out.println("code_url----------------"+code_url);
//		String[] msg =null;
//		if(StringUtils.isNotEmpty(code_url)){
//			msg = code_url.split("\\|");
//			if("1".equals(tpWxPayDto.getPayWay())){
				return map.get("prepay_id");
//			}
			
//		}
//		return msg[0];
	}
	
	/**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            
            m.put(k, v);
        }
        
        //关闭流
        in.close();
        
        return m;
    }
    
    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    @SuppressWarnings("rawtypes")
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        
        return sb.toString();
    }
	public static String getjsTicket() {
        String result = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&"
        		+ "appid="+appid+"&secret="+appsecret+"";
        System.out.println("查看js_url:" + url);
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
			System.out.println("mz=" + result);
			is.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
		String url2="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+result+"&type=jsapi";
		try {
			URL urlGet = new URL(url2);
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
			System.out.println("ticket=" + result);
			is.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
        return result;
    }
	
	// 获得js signature
	public static String getSignature(String jsapi_ticket,String timestamp, String nonce_str,String url) {
        String string1;
//        jsapi_ticket=getjsTicket();
        String signature = "";
 
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println("String==="+string1);
 
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        System.out.println("signature="+signature);
        return signature;
    }
 
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
 
	/**
	 * 网页端调起支付API
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static SortedMap<String, String> getPayweixinview(WxPayDto tpWxPayDto) throws JDOMException, IOException{
		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		Date date = new Date();
		String timestamp = String.valueOf(new Date().getTime());//TenpayUtil.getCurrTime().substring(4);
		// 商户号
		String partnerid = partner;
		// 随机字符串
		String nonce_str = getNonceStr();
		String wpackage ="prepay_id="+getCodeurl(tpWxPayDto);;
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appId", appid);
		packageParams.put("timeStamp", timestamp);
		packageParams.put("nonceStr", nonce_str);
		packageParams.put("package",wpackage );
		packageParams.put("signType", "MD5");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		packageParams.put("packageValue",wpackage );
		packageParams.put("paySign", sign);
		
		return packageParams;
	}	

	/*
	 * 小程序调起支付API
	 */
	public static SortedMap<String, String> getAppletPayview(WxPayDto tpWxPayDto) throws JDOMException, IOException{
		
		// 1 参数
		// 订单号
		//String orderId = tpWxPayDto.getOrderId();
		//Date date = new Date();
		String timestamp = String.valueOf(new Date().getTime());//TenpayUtil.getCurrTime().substring(4);
		// 商户号
		//String partnerid = partner;
		// 随机字符串
		String nonce_str = getNonceStr();
		String wpackage ="prepay_id="+getAppletCodeurl(tpWxPayDto);;
		// 商品描述根据情况修改
		//String body = tpWxPayDto.getBody();

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appId", APPLET_APPID);
		packageParams.put("timeStamp", timestamp);
		packageParams.put("nonceStr", nonce_str);
		packageParams.put("package",wpackage );
		packageParams.put("signType", "MD5");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(APPLET_APPID, APPLET_APPSECRET, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		packageParams.put("packageValue",wpackage );
		packageParams.put("paySign", sign);
		
		return packageParams;
	}
	
	/**
	 * 微信中调起分享的功能的配置信息API
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static SortedMap<String, String> getConfigweixin(String jsapi_ticket,String url) throws JDOMException, IOException{
		
		String timestamp = String.valueOf(new Date().getTime());//TenpayUtil.getCurrTime().substring(4);
		// 随机字符串
		String nonce_str = getNonceStr();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appId", appid);
		packageParams.put("timeStamp", timestamp);
		packageParams.put("nonceStr", nonce_str);
		
        //注意这里参数名必须全部小写，且必须有序
        String  string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println("String==="+string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			string1 = byteToHex(crypt.digest());
			packageParams.put("signature", string1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return packageParams;
	}
	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}


	public static String getCodeRequest(String returnurl) {
		String GetCodeRequestUrl = GetCodeRequest+"appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String result = null;
		GetCodeRequestUrl = GetCodeRequestUrl.replace("APPID",
				urlEnodeUTF8(appid));

		GetCodeRequestUrl = GetCodeRequestUrl.replace("REDIRECT_URI",
				urlEnodeUTF8(returnurl));

		GetCodeRequestUrl = GetCodeRequestUrl.replace("SCOPE", "snsapi_userinfo");

		result = GetCodeRequestUrl;
		return result;

	}

	public static String getCodeRequest(String returnurl,ApiPayConfig apiPayConfig) {

		String GetCodeRequestUrl = GetCodeRequest+"appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String result = null;
		String appidConfig;
		if(apiPayConfig!=null){
			appidConfig=apiPayConfig.getWeixinAppId();
		}
		else{
			appidConfig=appid;
		}
		GetCodeRequestUrl = GetCodeRequestUrl.replace("APPID",
				urlEnodeUTF8(appidConfig));

		GetCodeRequestUrl = GetCodeRequestUrl.replace("REDIRECT_URI",
				urlEnodeUTF8(returnurl));

		GetCodeRequestUrl = GetCodeRequestUrl.replace("SCOPE", "snsapi_userinfo");

		result = GetCodeRequestUrl;
		return result;

	}

	public static String getAccessTokenRequest(String code) {

		String result = null;
		GetAccess_tokenRequest = GetAccess_tokenRequest.replace("APPID",
				urlEnodeUTF8(appid));

		GetAccess_tokenRequest = GetAccess_tokenRequest.replace("SECRET",
				urlEnodeUTF8(appsecret));

		GetAccess_tokenRequest = GetAccess_tokenRequest.replace("CODE", code);

		result = GetAccess_tokenRequest;

		try {
			URL urlGet = new URL(result);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("POST"); // POST方式请求
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
			System.out.println("取到openid返回的信息"+demoJson);
			result = demoJson.getString("openid");
			is.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return result;

	}
	
	//TODO  getAppletAccessTokenRequest
	public static String getAppletAccessTokenRequest() {

		String result = null;
		GetAccess_token = GetAccess_token.replace("APPID",
				urlEnodeUTF8(APPLET_APPID));

		GetAccess_token = GetAccess_token.replace("SECRET",
				urlEnodeUTF8(APPLET_APPSECRET));
		
		result = GetAccess_token;

		try {
			URL urlGet = new URL(result);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("POST"); // POST方式请求
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
			is.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return result;

	}
	
	/**
	 * 获取sessionkey
	 * @param js_code
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static JSONObject getSessionKeyRequest(String js_code) throws UnsupportedEncodingException {
		String sessionkey_url="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
		sessionkey_url = sessionkey_url.replace("APPID", urlEnodeUTF8(APPLET_APPID));

		sessionkey_url = sessionkey_url.replace("SECRET",urlEnodeUTF8(APPLET_APPSECRET));

		sessionkey_url = sessionkey_url.replace("JSCODE", js_code);
		
		JSONObject item = new JSONObject();
		HttpConnect connect = new HttpConnect();
		HttpResponse httpResponse = connect.doPostStr(sessionkey_url);
		item = JSONObject.parseObject(httpResponse.getStringResult());
		return item;

	}
	
	public static String urlEnodeUTF8(String str) {

		String result = str;

		try {

			result = URLEncoder.encode(str, "UTF-8");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return result;

	}	
	
	/**
	 * 网页端调起支付API
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static SortedMap<String, String> getPayweixinviewPublic(WxPayDto tpWxPayDto,ApiPayConfig apiPayConfig) throws JDOMException, IOException{
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		Date date = new Date();
		String timestamp = String.valueOf(new Date().getTime());//TenpayUtil.getCurrTime().substring(4);
		// 商户号
		String partnerid,appidConfig,appsecretConfig,partnerkeyConfig;
		if(apiPayConfig!=null){
			partnerid=apiPayConfig.getWeixinPartner();
			appidConfig=apiPayConfig.getWeixinAppId();
			appsecretConfig=apiPayConfig.getWeixinAppSecret();
			partnerkeyConfig=apiPayConfig.getWeixinPartnerKey();
		}
		else{
			partnerid=partner;
			appidConfig=appid;
			appsecretConfig=appsecret;
			partnerkeyConfig=partnerkey;
		}
		// 随机字符串
		String nonce_str = getNonceStr();
		String wpackage ="prepay_id="+getCodeurlPublic(tpWxPayDto,apiPayConfig);;
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appId", appidConfig);
		packageParams.put("timeStamp", timestamp);
		packageParams.put("nonceStr", nonce_str);
		packageParams.put("package",wpackage );
		packageParams.put("signType", "MD5");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appidConfig, appsecretConfig, partnerkeyConfig);

		String sign = reqHandler.createSign(packageParams);
		packageParams.put("packageValue",wpackage );
		packageParams.put("paySign", sign);
		
		return packageParams;
	}
}
