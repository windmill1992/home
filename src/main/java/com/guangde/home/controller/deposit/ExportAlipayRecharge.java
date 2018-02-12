package com.guangde.home.controller.deposit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.AlipaySubmit_wap;
import com.alipay.util.UtilDate;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiProject;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.MD5Utils;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.Demo;
import com.tenpay.demo.WxPayDto;
import com.tenpay.utils.RequestHandler;

/**
 * 
 * 对于支付宝无线端的，支付渠道
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("exportRechargeAlipay")
public class ExportAlipayRecharge extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public static final String notify_url = "http://www.17xs.org/exportRechargeAlipay/async.do";
    
    // 页面跳转同步通知页面路径
    public static final String return_url = "http://www.17xs.org/exportRechargeAlipay/return_url.do";
    //项目的缓存key
    public static final	String PROJECT_PAY_SUCCESS_KEY= "PROJECT_PAY_SUCCESS";
    // 批量捐项目id
    public static final int PROJECTID = 285;
    
    Logger logger = LoggerFactory.getLogger(ExportAlipayRecharge.class);
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    @Autowired
    private RedisService redisService;
    
    
    @RequestMapping(value = "/submitAlipayRecharge", produces = "application/json")
    @ResponseBody
    public Map<String,Object> submitAlipayRecharge(DepositForm from, ApiFrontUser frontUser,
    		HttpServletRequest request, HttpServletResponse response,MD5Utils md5Utils)
        throws Exception
    { 
    	     
    			//让其进行跨域请求，所有请求都允许访问
    			response.setHeader("Access-Control-Allow-Origin", "*");
    			// 如果验签不正确则返回错误,返回的结果也需要验签
    			long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
    			JSONObject item = new JSONObject();
    			SortedMap<String, String> sm = new TreeMap<String, String>();
    			sm.put("timeStamp", localTimeStamp + "");
    			sm.put("sign_Check_field", MD5Utils.sign_Check_field);
    			item.put("timeStamp", localTimeStamp);
    			item.put("signType", MD5Utils.signType_Constant);
    			
    				// 返回为-2时验签不正确
    			RequestHandler reqHandler = new RequestHandler(null, null);
    			String backSign = reqHandler.createSignNoKey(sm);
    			item.put("backSign", backSign);
    			return webUtil.resMsg(-2, "0001", "验签错误", item);

    }
    @RequestMapping(value = "/alipayRecharge", produces = "application/json")
    public ModelAndView alipayRecharge(DepositForm from, ApiFrontUser frontUser,
    		HttpServletRequest request, HttpServletResponse response,MD5Utils md5Utils)
        throws Exception
    { 
    	//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
    	ModelAndView view = new ModelAndView("deposit/alipay");
    	DecimalFormat df = new DecimalFormat("######0.00");
    	SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp()+"");
		packageParams.put("userName",frontUser.getUserName());
		packageParams.put("nickName",frontUser.getNickName());
		packageParams.put("userPass",frontUser.getUserPass());
		packageParams.put("amount",df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
//		if (!flags) {
//			// 返回为-2时验签不正确
//			view=new ModelAndView("redirect:/exportRechargeAlipay/submitAlipayRecharge.do");
//			view.addObject("userName", frontUser.getUserName());
//			view.addObject("nickName",frontUser.getNickName());
//			view.addObject("userPass",frontUser.getUserPass());
//			view.addObject("amount",from.getAmount()+"");
//			return view;
//		}
        //判段这个IP是否已经注册过
        String adressIp = SSOUtil.getUserIP(request);
        Integer userId=null;
        ApiFrontUser user = new ApiFrontUser();
        user.setUserName(frontUser.getUserName());
        //先判断该用户是否存在
        ApiFrontUser user11 = new ApiFrontUser();
        user11.setUserName(frontUser.getUserName());
        ApiFrontUser ar=userFacade.queryUserByParam(user11);
        boolean flag=false;
        if(ar==null){
        	try {
            	long ip = IPAddressUtil.ipToLong(adressIp);
            	ApiIptable iptable = new ApiIptable();
            	iptable.setEndIPNum(String.valueOf(ip));
            	iptable.setStartIPNum(String.valueOf(ip));
            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
            	if(ipList!=null && ipList.size()>0){
            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
            		if(contents.length==1){
            			user.setProvince(contents[0]);
            		}
            		else if(contents.length==2){
            			user.setProvince(contents[0]);
            			user.setCity(contents[1]);
            		}
            		else if(contents.length==3){
            			user.setProvince(contents[0]);
            			user.setCity(contents[1]);
            			user.setArea(contents[2]);
            		}
            	}
			
		} catch (Exception e) {
		}
        	user.setNickName(frontUser.getNickName());
            user.setUserPass(frontUser.getUserPass());
            user.setUserType(PengPengConstants.PERSON_USER);
            //未注册的用户//当做一个用户的唯一字段
            user.setRegUsersType(frontUser.getRegUsersType());  
            user.setRegisterIP(adressIp);
            logger.debug("募捐归类需要的参数：adressIp:" + adressIp); 
            ApiResult apr = userFacade.registered(user);
            if (apr != null && apr.getCode() == 1)
            {
            	ApiFrontUser user1 = new ApiFrontUser();
            	user1=userFacade.queryUserByParam(user);
            	userId=user1.getId();
                flag=true;
            }
        }else{
        	userId=ar.getId();
        	flag=true;
        }
        if (!flag)
        {
            //view.addObject("alipay", "没有这个项目");
             return view.addObject("alipay", "用户名添加失败");
          }else{
            	//根据刚刚注册的信息去查询用户的id
            	ApiFrontUser af=userFacade.queryUserByParam(user);
            	if(af.getId()!=null){
            		userId=af.getId();
            	}
            	else{
            		return view.addObject("alipay", "添加该用户信息出现异常，请联系管理员");
            	}
            }
        String total_fee = String.valueOf(from.getAmount());
        Map<String, String> sParaTemp = alipayMap(request,response);
        sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
        sParaTemp.put("total_fee", total_fee);// 价格
        if (StringUtils.isNotEmpty(from.getBank()))
        {
            sParaTemp.put("paymethod", "bankPay");
            sParaTemp.put("defaultbank", from.getBank());
        }
        sParaTemp.put("subject", "施乐会基金用户:"+ frontUser.getNickName()+"户充值");
        sParaTemp.put("extra_common_param", 1+"");
		ApiCapitalinout capitalinout = new ApiCapitalinout();
		capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
		capitalinout.setSource("PC");
		capitalinout.setMoney(from.getAmount());

		capitalinout.setPayType("alipay");
	
		capitalinout.setUserId(userId);
		capitalinout.setBankType(from.getBank());
		ApiResult rt = companyFacade.companyReCharge(capitalinout);
      
        
        // 成功:1
        if (rt != null && rt.getCode() == 1)
        {
            // 建立请求
            try
            {
            	//建立请求
            	String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
                view.addObject("alipay", sHtmlText);
                return view;
            }
            catch (Exception e)
            {
                
                view.addObject("alipay", "充值失败");
                e.printStackTrace();
                return view;
            }
        }
        else{
        	return view.addObject("alipay", "充值失败");
        }
    }
    
    /**
     * 同步通知的页面的Controller
     * 
     */
    @RequestMapping(value = "/return_url")
   
    public ModelAndView Return_url(HttpServletRequest request, HttpServletResponse response)
    {   
    	//充值成功
        ModelAndView view = new ModelAndView();
        
        String tradeStatus = request.getParameter("trade_status");
        String tradeNo = request.getParameter("out_trade_no");
        String notifyId = request.getParameter("trade_no");
        //String ep = request.getParameter("extra_common_param");
        logger.info("施乐会》》回调》支付宝渠道充值:: 对交易号" + tradeNo + "：：支付宝ID" + notifyId);
        logger.info("tradeStatus>>>>"+tradeStatus);
        RequestHandler reqHandler = new RequestHandler(null, null);
		Date date= new Date();
		// 进行加密判断
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("tradeNo", tradeNo);
		packageParams.put("timeStamp", date.getTime()+"");
		String sign = reqHandler.createSignNoKey(packageParams);
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
            	companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
                logger.info("支付宝渠道充值成功:: 对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.info("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
          
        }
        else
        {
            // 查看错误alipay文档
        	logger.info("施乐会充值》》》》未支付");
        	companyFacade.companyReChargeResult(tradeNo, notifyId, false, "");
        	
        }
        view.setViewName("redirect:http://www.shilehui.com/assistui/syPayReturn.aspx?sign="+sign+"&tradeNo="+tradeNo+"&timeStamp="+date.getTime());
        return view;
        
    }
    
    /**
     * 异步通知付款状态的Controller
     * 
     */
    @RequestMapping(value = "/async")
    public String async(HttpServletRequest request, HttpServletResponse response)
    {
        String tradeNo = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        String notifyId = request.getParameter("trade_no");
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            //检查是否已经充值
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
            	companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
                logger.info("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.info("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            
        }
        else
        {
            // 查看错误alipay文档
        	logger.info("施乐会充值》》》》未支付");
        	companyFacade.companyReChargeResult(tradeNo, notifyId, false, "");
        }
        return "/deposit/success";
        
    }
    
    /**
     * 根据订单号查询订单是否支付成功
     * @param request
     * @param response
     * @param md5Utils
     * @return
     */
    @RequestMapping("isOrNotSuccessByOrerId")
    @ResponseBody
    public Map<String, Object> isOrNotSuccessByOrerId(@RequestParam(value="orderId",required=true)String orderId,
    		HttpServletRequest request, HttpServletResponse response,MD5Utils md5Utils){
    	//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("orderId", orderId +"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		if(companyFacade.checkAlipayResultByTranNum(orderId)){
			logger.debug("支付宝渠道充值成功::对交易号" + orderId);
			return webUtil.resMsg(1, "0001", "成功", item);
		}
		return webUtil.failedRes("-1", "订单未支付成功", item);
    	
    }
    
    
    private Map<String, String> alipayMap(HttpServletRequest request, HttpServletResponse response)
            throws MalformedURLException, DocumentException, IOException
        {
            // 支付类型
            String payment_type = "1";
            // 订单名称
            String subject = "善基金充值";
            // 防钓鱼时间戳
            // 若要使用请调用类文件submit中的query_timestamp函数
            String anti_phishing_key = AlipaySubmit.query_timestamp();
            // 客户端的IP地址
            // 非局域网的外网IP地址，如：221.0.0.1
            String exter_invoke_ip = SSOUtil.getUserIP(request);
            String body = "善基金";
            // 商品展示地址
            String show_url = "http://www.17xs.org";
            
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "create_donate_trade_p");// 接口服务----即时到账
            sParaTemp.put("partner", AlipayConfig.partner);// 支付宝PID
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 统一编码
            sParaTemp.put("payment_type", payment_type);// 支付类型
            sParaTemp.put("notify_url", notify_url);// 异步通知页面
            sParaTemp.put("return_url", return_url);// 页面跳转同步通知页面
            sParaTemp.put("seller_email", AlipayConfig.seller_email);// 卖家支付宝账号
            sParaTemp.put("subject", subject);// 商品名称
            sParaTemp.put("sign_type", AlipayConfig.sign_type);// 支付类型
            sParaTemp.put("body", body);
            sParaTemp.put("show_url", show_url);
            sParaTemp.put("anti_phishing_key", anti_phishing_key);
            sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
            return sParaTemp;
        }
    
    
}
