package com.tenpay.demo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Formatter;
import java.util.SortedMap;
import java.util.TreeMap;





import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.guangde.entry.ApiPayConfig;
import com.tenpay.utils.GetWxOrderno;
import com.tenpay.utils.RequestHandler;
import com.tenpay.utils.Sha1Util;
import com.tenpay.utils.TenpayUtil;


/**
 * @author ex_yangxiaoyi
 * 
 */
public class Demo {
	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	private static String appid = "wxa09ee42dbe779694";
	private static String appsecret = "c8c9005d568c7575770df85d9c92a87c";
	private static String partner = "1241263502";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = "12345QWERT12345QWERT1234567890qw";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
	private static String openId = "";
	//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	private static String notifyurl = "http://www.17xs.org/tenpay/async.do";																	 // Key
	private static String notifyurl2 = "http://www.17xs.org/tenpay/asyncresult/";
	//测试其他平台的微信支付
	//private static String appid = "wxb156d3f0d535220f";
	//private static String appsecret = "5fff0fe22ad56ab39346e90c7d02192f";
	//private static String partner = "10028175";
	//private static String partnerkey = "shanjijin2016facshanjijin2016fac";
	/**
	 * @param args
	 * @throws BiffException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public static void main(String[] args) {
		WxPayDto wxPayDto = new WxPayDto();
		wxPayDto.setOrderId("4578lkt1264");
		wxPayDto.setBody("测试");
		wxPayDto.setTotalFee("0.01");
		wxPayDto.setAttach("111");
		wxPayDto.setSpbillCreateIp("192.168.0.1");
		getCodeurl(wxPayDto);
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId("14776309480206067");
	    ApiPayConfig config=new ApiPayConfig();
	    config.setWeixinAppId("wxb156d3f0d535220f");
	    config.setWeixinAppSecret("5fff0fe22ad56ab39346e90c7d02192f");
	    config.setWeixinPartner("10028175");
	    config.setWeixinPartnerKey("shanjijin2016facshanjijin2016fac");
		String msg = getPayResult(tpWxPay1, config);
		System.out.println(msg);
	}
	
	/**
	 * 获取微信扫码支付二维码连接
	 */
	public static String getCodeurl(WxPayDto tpWxPayDto){
		
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
		String notify_url = notifyurl;
		if(tpWxPayDto.getNotifyUrl()!=null){//传过来的回调地址不为空，则赋值
			notify_url=tpWxPayDto.getNotifyUrl();
		}
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

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
				+ "</notify_url>" + "<trade_type>" + trade_type
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
		code_url = GetWxOrderno.getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------"+code_url);
		String[] msg =null;
		if(StringUtils.isNotEmpty(code_url)){
			msg = code_url.split("\\|");
			if("1".equals(tpWxPayDto.getPayWay())){
				return msg[1];
			}
			
		}
		return msg[0];
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
	 */
	public static SortedMap<String, String> getPayweixinview(WxPayDto tpWxPayDto){
		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		Date date = new Date();
		String timestamp = TenpayUtil.getCurrTime().substring(2,12);
		// 商户号
		String partnerid = partner;
		// 随机字符串
		String nonce_str = getNonceStr();
		String wpackage ="prepayid="+getCodeurl(tpWxPayDto);;
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("partnerid", partnerid);
		packageParams.put("noncestr", nonce_str);
		packageParams.put("package",wpackage );
		packageParams.put("timestamp", timestamp);
		packageParams.put("signType", "MD5");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		packageParams.put("paySign", sign);
		
		return packageParams;
	}
	/**
	 * 查询订单支付结果
	 */
	public static String getPayResult(WxPayDto tpWxPayDto){
		
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
		String notify_url = notifyurl;
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String code_url = "";
		
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>"
				+ "<sign>" + sign + "</sign>"
				+ "</xml>";
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/orderquery";
		
		code_url = new GetWxOrderno().getPayResult(createOrderURL, xml);
//		System.out.println("code_url----------------"+code_url);
		
		return code_url;
	}
	
	/**
	 * 查询订单支付结果
	 */
	public static String getPayResult(WxPayDto tpWxPayDto,ApiPayConfig config){
		
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
		String notify_url = notifyurl;
		String trade_type = "NATIVE";
		
		// 商户号
		String mch_id = config.getWeixinPartner();
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", config.getWeixinAppId());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(config.getWeixinAppId(), config.getWeixinAppSecret(), config.getWeixinPartnerKey());

		String sign = reqHandler.createSign(packageParams);
		String code_url = "";
		
		String xml = "<xml>" + "<appid>" + config.getWeixinAppId() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>"
				+ "<sign>" + sign + "</sign>"
				+ "</xml>";
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/orderquery";
		
		code_url = new GetWxOrderno().getPayResult(createOrderURL, xml);
//		System.out.println("code_url----------------"+code_url);
		
		return code_url;
	}
	
	/**
	 * 获取请求预支付id报文
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getPackage(WxPayDto tpWxPayDto) {
		
		String openId = tpWxPayDto.getOpenId();
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "JSAPI";

		// ---必须参数
		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

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

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

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
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String prepay_id = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		
		prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);

		System.out.println("获取到的预支付ID：" + prepay_id);
		
		
		//获取prepay_id后，拼接最后请求支付所需要的package
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", appid);  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		
		String finaPackage = "\"appId\":\"" + appid + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";

		System.out.println("V3 jsApi package:"+finaPackage);
		return finaPackage;
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
	
	/**
	 * 获取微信扫码支付二维码连接(接口对外)
	 */
	public static String getCodeurlPublic(WxPayDto tpWxPayDto,ApiPayConfig apiPayConfig){
		
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
		String notify_url = notifyurl;//2+tpWxPayDto.getOrderId()+"/";
		if(tpWxPayDto.getNotifyUrl()!=null){//传过来的回调地址不为空，则赋值
			notify_url=tpWxPayDto.getNotifyUrl();
		}
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = apiPayConfig.getWeixinPartner();
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", apiPayConfig.getWeixinAppId());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(apiPayConfig.getWeixinAppId(), apiPayConfig.getWeixinAppSecret(), apiPayConfig.getWeixinPartnerKey());

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + apiPayConfig.getWeixinAppId() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
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
		code_url = GetWxOrderno.getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------"+code_url);
		String[] msg =null;
		if(StringUtils.isNotEmpty(code_url)){
			msg = code_url.split("\\|");
			if("1".equals(tpWxPayDto.getPayWay())){
				return msg[1];
			}
			
		}
		return msg[0];
	}
}
