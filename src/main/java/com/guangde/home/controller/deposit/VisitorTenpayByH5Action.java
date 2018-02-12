package com.guangde.home.controller.deposit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IAuctionProjectFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiAuctionProject;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiOneAid;
import com.guangde.entry.ApiPayConfig;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiThirdUser;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.MD5Utils;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.demo.WxPayDto;
import com.tenpay.utils.RequestHandler;
import com.tenpay.utils.TenpayUtil;

/**
 * 在手机，pad等无限的设备上微信的支付
 * 对于游客匿名的捐赠
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("visitorAlipay")
public class VisitorTenpayByH5Action extends DepositBaseAction {

	Logger logger = LoggerFactory.getLogger(VisitorTenpayByH5Action.class);
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IFileFacade facade;
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IAuctionProjectFacade auctionProjectFacade;
	
	
	@RequestMapping(value = "/tenpay/depositTest")
	public ModelAndView weixindeposit2(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		ModelAndView view = new ModelAndView("h5/weixin_2");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
			logger.info("weixin_code>>>>"+weixin_code);
			Map<String, Object> mapToken = new HashMap<String, Object>(8);
			try {
				if ("".equals(openId) || openId == null) {
					
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					unionid = mapToken.get("unionid").toString();
					logger.info("weixinpay>>>>openId>>>"+openId+">>Token>>>"+Token+">>>unionid>>"+unionid);
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
				}
			} catch (Exception e) {
				logger.error("微信支付处理出现问题"+ e);
			}
			view.addObject("payway", browser);
		}
		
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		
		 view.addObject("pName", project.getTitle());
		 view.addObject("amount", from.getAmount());
		 view.addObject("projectId", from.getProjectId());
		 view.addObject("copies", from.getCopies());
		 view.addObject("crowdFunding", from.getCrowdFunding());
				 
		if(browser.equals("wx")){
			
			 ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
			 }
			 view.addObject("user",user);
			
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
			
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(tpWxPay1.getOrderId());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			//dRecord.setLeaveWord(from.getCrowdFunding());
			dRecord.setLeaveWord(from.getDonateWord());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }

			//6：个人用户
			if(from.getSlogans()!=null && !"".equals(from.getSlogans())){
				tpWxPay1.setAttach(from.getSlogans());
			}else{
				tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
			}
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, null,"tenpay",null,"");
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
						SortedMap<String, String> map= H5Demo.getPayweixinview(tpWxPay1);
						view.addObject("appId",map.get("appId"));
						view.addObject("timestamp",map.get("timeStamp"));
						view.addObject("noncestr",map.get("nonceStr"));
						view.addObject("packageValue",map.get("packageValue"));
						view.addObject("paySign",map.get("paySign"));
						view.addObject("paysignType",map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						view.addObject("config_timestamp",config_timestamp);
						view.addObject("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket = (String) redisService.queryObjectData("JsapiTicket");
						String accessToken = (String) redisService.queryObjectData("AccessToken");
						if(jsTicket == null || accessToken == null){
							accessToken = TenpayUtil.queryAccessToken();
							redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
							jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
							redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						}
						view.addObject("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
						
						 view.addObject("pName", project.getTitle());
						 view.addObject("amount", from.getAmount());
						 view.addObject("tradeNo", tpWxPay1.getOrderId());
						 view.addObject("projectId", from.getProjectId());
						return view;
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					view.addObject("error", "wx-1");
					return view;
				}
			} else {
				//系统的后台交互
				view.addObject("error", "wx-2");
				return view;
			}
		}
		// 区分分享页捐款成功跳转
		view.addObject("extensionPeople", from.getExtensionPeople());
        view.addObject("donateWord", from.getDonateWord());
		return view;
	}
	
	@RequestMapping(value = "/tenpay/deposit")
	public ModelAndView weixindeposit2Test(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		ModelAndView view = new ModelAndView("h5/weixin_2");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		//查询支付配置
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
      	ApiPayConfig config = new ApiPayConfig();
      	config.setUserId(project.getUserId());
      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
			logger.info("weixin_code>>>>"+weixin_code);
			Map<String, Object> mapToken = new HashMap<String, Object>(8);
			try {
				if ("".equals(openId) || openId == null) {
					
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code="";
						if(list.size()>0){
							url_weixin_code=H5Demo.getCodeRequest(perfecturl,list.get(0));
						}
						else{
							url_weixin_code=H5Demo.getCodeRequest(perfecturl);
						}
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					if(list.size()>0){
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code,list.get(0));
					}
					else{
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
					}
					logger.info("mapToken>>>"+mapToken);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					if(mapToken.get("unionid")!=null){
						unionid = mapToken.get("unionid").toString();
					}
					logger.info("weixinpay>>>>openId>>>"+openId+">>Token>>>"+Token+">>>unionid>>"+unionid);
					if(list.size()==0){
						redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					}
				}
			} catch (Exception e) {
				logger.error("微信支付处理出现问题"+ e);
			}
			view.addObject("payway", browser);
		}
		
		 view.addObject("pName", project.getTitle());
		 view.addObject("amount", from.getAmount());
		 view.addObject("projectId", from.getProjectId());
		 view.addObject("copies", from.getCopies());
		 view.addObject("crowdFunding", from.getCrowdFunding());
		 view.addObject("slogans",from.getSlogans());
		 view.addObject("nameOpen", from.getNameOpen());
		 String orderId=StringUtil.uniqueCode();
		if(browser.equals("wx")){
			ApiFrontUser user = new ApiFrontUser();
				if(list.size()>0){
					 user = CommonUtils.queryUser(request,openId,Token,unionid,list.get(0));
				}
				else{
					 user = CommonUtils.queryUser(request,openId,Token,unionid);
				}
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
			 }
			 view.addObject("user",user);
			 //用户电话、真实姓名写入用户
			 ApiFrontUser apiFrontUser = new ApiFrontUser();
			 apiFrontUser.setId(user.getId());
			 apiFrontUser.setNickName(user.getNickName());
			 if(from.getRealName()!=null&&from.getRealName()!=""){
				 apiFrontUser.setRealName(from.getRealName());
	         }
	         if(from.getMobileNum()!=null&&from.getMobileNum()!=""){
	        	 apiFrontUser.setMobileNum(from.getMobileNum());
	         }
	         
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(orderId);
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
	      	if(list.size()>0){
	      		dRecord.setPayConfigId(list.get(0).getId());
	      		dRecord.setPartnerName(list.get(0).getName());
	      	}
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(tpWxPay1.getOrderId());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			//dRecord.setLeaveWord(from.getCrowdFunding());
			 String slogans = from.getSlogans();
			/*if(from.getSlogans()!=null && from.getSlogans().contains("oneAid")){
				dRecord.setLeaveWord(from.getSlogans());
			}else{*/
				dRecord.setLeaveWord(from.getDonateWord());
			/*}*/
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }

			//6：个人用户
			if(from.getSlogans()!=null && !"".equals(from.getSlogans())){
				tpWxPay1.setAttach(from.getSlogans());
			}else{
				tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
			}
			ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
	      	if(list.size()>0){
	      		apiCapitalinout.setPayConfigId(list.get(0).getId());
	      		apiCapitalinout.setPartnerName(list.get(0).getName());
	      	}
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout,"tenpay", apiFrontUser, slogans);
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
						SortedMap<String, String> map;
						if(list.size()>0){
							map = H5Demo.getPayweixinviewPublic(tpWxPay1,list.get(0));
						}
						else{
							map = H5Demo.getPayweixinview(tpWxPay1);
						}
						logger.info("map>>>"+map);
						view.addObject("appId",map.get("appId"));
						view.addObject("timestamp",map.get("timeStamp"));
						view.addObject("noncestr",map.get("nonceStr"));
						view.addObject("packageValue",map.get("packageValue"));
						view.addObject("paySign",map.get("paySign"));
						view.addObject("paysignType",map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						view.addObject("config_timestamp",config_timestamp);
						view.addObject("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket,accessToken;
						//jsTicket = (String) redisService.queryObjectData("JsapiTicket");
						//accessToken = (String) redisService.queryObjectData("AccessToken");
						//if(jsTicket == null || accessToken == null){
							if(list.size()>0){
								accessToken = TenpayUtil.queryAccessToken(list.get(0));
								logger.info("accessToken2>>>"+accessToken);
							}
							else{
								accessToken = TenpayUtil.queryAccessToken();
								logger.info("accessToken1>>>"+accessToken);
							}
							redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
							jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
							logger.info("jsTicket>>>"+jsTicket);
							redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						//}
						view.addObject("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
						System.out.println("url >> "+perfecturl);
						 view.addObject("pName", project.getTitle());
						 view.addObject("amount", from.getAmount());
						 view.addObject("tradeNo", tpWxPay1.getOrderId());
						 view.addObject("projectId", from.getProjectId());
						 view.addObject("donateWord", from.getDonateWord());
					     view.addObject("realName", from.getRealName());
					     view.addObject("mobileNum", from.getMobileNum());
						return view;
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					view.addObject("error", "wx-1");
					return view;
				}
			} else {
				//系统的后台交互
				view.addObject("error", "wx-2");
				return view;
			}
		}
		// 区分分享页捐款成功跳转
		view.addObject("extensionPeople", from.getExtensionPeople());
        view.addObject("donateWord", from.getDonateWord());
        view.addObject("realName", from.getRealName());
        view.addObject("mobileNum", from.getMobileNum());
        view.addObject("tradeNo", orderId);
		return view;
	}
	
	 /**
     * 支付
     */
    @RequestMapping("/tenpay/jpPay")
	public ModelAndView pay(@RequestParam(value = "id", required = true) Integer id,
			HttpServletRequest request,HttpServletResponse response) {
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		ApiAuctionProject apiAuctionProject = auctionProjectFacade
				.queryAuctionProjectDetail(id);
		ModelAndView view = new ModelAndView("h5/weixin_2");
		String total_fee = String.valueOf(apiAuctionProject.getCurrentPrice());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);
		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(apiAuctionProject.getProjectId());
		ApiThirdUser user = new ApiThirdUser();
		Integer userId = UserUtil.getUserId(request, response);
    	//判断用户有没登录
    	if(userId == null){
    		view = new ModelAndView("otherProject/auction/gylist");
    		return view;
    	}
		user.setUserId(userId);
		user = userFacade.queryThirdUserByParam(user);
		tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(user.getUserId());
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
		dRecord.setDonatType("individualUsers");
		dRecord.setDonatAmount(Double.valueOf(total_fee));
		dRecord.setDonateCopies(0);
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("H5");
		dRecord.setCompanyId(0);
		// 6：个人用户
		view.addObject("payway", "wx");
		view.addObject("pName", apiAuctionProject.getTitle());
		 view.addObject("amount", apiAuctionProject.getCurrentPrice());
		 view.addObject("projectId", apiAuctionProject.getId());
		 view.addObject("copies", 0);
		tpWxPay1.setAttach("慈善竞拍"+apiAuctionProject.getProjectId());
		ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "tenpay",null,"");
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				tpWxPay1.setPayWay("1");
				tpWxPay1.setOpenId(user.getAccountNum());
				tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"
						+ tpWxPay1.getOrderId() + "/");
				SortedMap<String, String> map = H5Demo
						.getPayweixinview(tpWxPay1);
				view.addObject("appId", map.get("appId"));
				view.addObject("timestamp", map.get("timeStamp"));
				view.addObject("noncestr", map.get("nonceStr"));
				view.addObject("packageValue", map.get("packageValue"));
				view.addObject("paySign", map.get("paySign"));
				view.addObject("paysignType", map.get("signType"));

				String config_timestamp = String.valueOf(new Date().getTime());
				String config_noncestr = H5Demo.getNonceStr();
				view.addObject("config_timestamp", config_timestamp);
				view.addObject("config_noncestr", config_noncestr);

				// 微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
				String jsTicket = (String) redisService
						.queryObjectData("JsapiTicket");
				String accessToken = (String) redisService
						.queryObjectData("AccessToken");
				if (jsTicket == null || accessToken == null) {
					accessToken = TenpayUtil.queryAccessToken();
					redisService.saveObjectData("AccessToken", accessToken,
							DateUtil.DURATION_HOUR_S);
					jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
					redisService.saveObjectData("JsapiTicket", jsTicket,
							DateUtil.DURATION_HOUR_S);
				}
				view.addObject("signature", H5Demo.getSignature(jsTicket,
						config_timestamp, config_noncestr, perfecturl));

				return view;
			} catch (Exception e) {
				// 订单交互
				logger.error("微信支付订单交互" + e.getStackTrace());
				view.addObject("error", "wx-1");
				return view;
			}
		}
		return view;
	}
    
    /**
     * h5微信捐款对外
     * @param from
     * @param notifyUrl
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tenpay/depositPublic")
	public ModelAndView depositPublic(DepositForm from, @RequestParam(value="notifyUrl",required=true)String notifyUrl,
			@RequestParam(value="gotoUrl",required=true)String gotoUrl,
			HttpServletRequest request,HttpServletResponse response
			) throws Exception {
		ModelAndView view = new ModelAndView("h5/weixin_shilehui");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		//查询支付配置
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		ApiPayConfig config = new ApiPayConfig();
		config.setUserId(project.getUserId());
		List<ApiPayConfig> list = projectFacade.queryByParam(config);
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
			logger.info("weixin_code>>>>"+weixin_code);
			Map<String, Object> mapToken = new HashMap<String, Object>(8);
			try {
				if ("".equals(openId) || openId == null) {
					
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code="";
						if(list.size()>0){
							url_weixin_code=H5Demo.getCodeRequest(perfecturl,list.get(0));
						}
						else{
							url_weixin_code=H5Demo.getCodeRequest(perfecturl);
						}
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					if(list.size()>0){
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code,list.get(0));
					}
					else{
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
					}
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					if(mapToken.get("unionid")!=null){
						unionid = mapToken.get("unionid").toString();
					}
					logger.info("weixinpay>>>>openId>>>"+openId+">>Token>>>"+Token+">>>unionid>>"+unionid);
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
				}
			} catch (Exception e) {
				logger.error("微信支付处理出现问题"+ e);
			}
			view.addObject("payway", browser);
		}
		
		 view.addObject("pName", project.getTitle());
		 view.addObject("amount", from.getAmount());
		 view.addObject("projectId", from.getProjectId());
		 view.addObject("copies", from.getCopies());
		 view.addObject("crowdFunding", from.getCrowdFunding());
				 
		if(browser.equals("wx")){
			
			 ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
			 }
			 view.addObject("user",user);
			
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    
		    RequestHandler reqHandler = new RequestHandler(null, null);
			Date date= new Date();
			long date2=date.getTime();
			// 进行加密判断
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			// sign_Check_field字符串为双方本地验签字段，不可外泄
			packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
			packageParams.put("tradeNo", tpWxPay1.getOrderId());
			packageParams.put("timeStamp", date2+"");
			String sign = reqHandler.createSignNoKey(packageParams);
		    
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
	      	if(list.size()>0){
	      		dRecord.setPayConfigId(list.get(0).getId());
	      	}
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(tpWxPay1.getOrderId());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			//dRecord.setLeaveWord(from.getCrowdFunding());
			dRecord.setLeaveWord(from.getDonateWord());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }

			//6：个人用户
			tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
			ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
	      	if(list.size()>0){
	      		apiCapitalinout.setPayConfigId(list.get(0).getId());
	      	}
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout,"tenpay",null,"");
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("notifyUrl?sign="+sign+"&tradeNo="+tpWxPay1.getOrderId()+"&timeStamp="+date2+"/");
						SortedMap<String, String> map;
						if(list.size()>0){
							map = H5Demo.getPayweixinviewPublic(tpWxPay1,list.get(0));
							logger.info("map2>>>"+map);
						}
						else{
							map = H5Demo.getPayweixinview(tpWxPay1);
							logger.info("map1>>>"+map);
						}
						
						view.addObject("appId",map.get("appId"));
						view.addObject("timestamp",map.get("timeStamp"));
						view.addObject("noncestr",map.get("nonceStr"));
						view.addObject("packageValue",map.get("packageValue"));
						view.addObject("paySign",map.get("paySign"));
						view.addObject("paysignType",map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						view.addObject("config_timestamp",config_timestamp);
						view.addObject("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket = (String) redisService.queryObjectData("JsapiTicket");
						String accessToken = (String) redisService.queryObjectData("AccessToken");
						if(jsTicket == null || accessToken == null){
							accessToken = TenpayUtil.queryAccessToken();
							redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
							jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
							redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						}
						view.addObject("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
						
						 view.addObject("pName", project.getTitle());
						 view.addObject("amount", from.getAmount());
						 view.addObject("tradeNo", tpWxPay1.getOrderId());
						 view.addObject("projectId", from.getProjectId());
						 view.addObject("gotoUrl", gotoUrl);
						return view;
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					view.addObject("error", "wx-1");
					return view;
				}
			} else {
				//系统的后台交互
				view.addObject("error", "wx-2");
				return view;
			}
		}
		// 区分分享页捐款成功跳转
		view.addObject("extensionPeople", from.getExtensionPeople());
        view.addObject("donateWord", from.getDonateWord());
		return view;
	}
    
    /**
	 * 微信H5充值接口
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenpay/tenPayh5Recharge")
	@ResponseBody
	public String tenPayh5Recharge(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		String callback = request.getParameter("callback");
		JSONObject items = new JSONObject();
		JSONObject item = new JSONObject();
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
			Integer userId = from.getUserId();//UserUtil.getUserId(request, response);
			if(userId!=null){
				ApiThirdUser thirdUser = new ApiThirdUser();
				thirdUser.setUserId(userId);
				thirdUser = userFacade.queryThirdUserByParam(thirdUser);
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    if(thirdUser!=null&&thirdUser.getAccountNum()!=null){
		    	logger.info("openid>>>>>"+thirdUser.getAccountNum());
			    tpWxPay1.setOpenId(thirdUser.getAccountNum());
		    } 
		    String orderNumber=StringUtil.uniqueCode();
		    item.put("orderNumber", orderNumber);
		    tpWxPay1.setOrderId(orderNumber);
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			tpWxPay1.setBody("充值互助项目");
			if("0".equals(from.getCrowdFunding())){//充值加入项目余额
				tpWxPay1.setAttach("huzhuBalance");
			}
			else{
				tpWxPay1.setAttach("huzhu");
			}
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("H5");
			capitalinout.setMoney(from.getAmount());

			capitalinout.setPayType("tenpay");

			capitalinout.setUserId(userId);
			capitalinout.setBankType(from.getBank());
			capitalinout.setInType(13);//互助充值
			// 资金进
			ApiResult rt = companyFacade.companyReCharge(capitalinout);
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						//tpWxPay1.setOpenId(thirdUser.getAccountNum());
						tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
						SortedMap<String, String> map;
						map = H5Demo.getPayweixinview(tpWxPay1);
						logger.info("map>>>"+map);
						item.put("appId", map.get("appId"));
						item.put("timestamp", map.get("timeStamp"));
						item.put("noncestr", map.get("nonceStr"));
						item.put("packageValue", map.get("packageValue"));
						item.put("paySign", map.get("paySign"));
						item.put("paysignType", map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						item.put("config_timestamp",config_timestamp);
						item.put("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket,accessToken;
						accessToken = TenpayUtil.queryAccessToken();
						logger.info("accessToken1>>>"+accessToken);
						redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
						jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
						logger.info("jsTicket>>>"+jsTicket);
						redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						item.put("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, url.toString()));
						
						items.put("msg", "成功");
						items.put("code", 1);
						items.put("data", item);
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					items.put("msg", "失败");
					items.put("code", 0);
					items.put("data", item);
				}
			} else {
				items.put("msg", "失败");
				items.put("code", 0);
				items.put("data", item);
			}}
			else{
				items.put("msg", "用户未登录");
				items.put("code", 0);
				items.put("data", item);
			}
			return callback+"("+items+")";
	}
	
	/**TODO
	 * 微信退款
	 * @param out_trade_no 订单号
	 * @param total_fee 订单总金额
	 * @param refund_fee 退款金额
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="refund",method=RequestMethod.GET)
	@ResponseBody
	public String refund(@RequestParam(value="out_trade_no")String out_trade_no,
			@RequestParam(value="total_fee")String total_fee,
			@RequestParam(value="refund_fee")String refund_fee) throws Exception{
		//JSONObject item = new JSONObject();
		WXReFundUtil ruFundUtil = new WXReFundUtil();
		Map<String, String> map = ruFundUtil.doRefund(out_trade_no,total_fee,refund_fee);
		
		return map.toString();
	}*/
	
	
	/**
	 * 小程序支付
	 * @param request
	 * @param response
	 * @param openId
	 * @param unionId
	 * @param from
	 * @return
	 */
	@RequestMapping(value="appletPay",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject appletPay(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("openid") String openId,@RequestParam("unionId") String unionId,ApiFrontUser user,
			DepositForm from){
		JSONObject item  = new JSONObject();
		JSONObject result  = new JSONObject();
		//查询支付配置
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		WxPayDto wxPayDto = new WxPayDto();
		wxPayDto.setOpenId(openId);
		wxPayDto.setOrderId(StringUtil.uniqueCode());
		wxPayDto.setSpbillCreateIp(SSOUtil.getUserIP(request));
		wxPayDto.setTotalFee(from.getAmount()+"");
		wxPayDto.setBody("认捐“"+project.getTitle()+"”项目");
		wxPayDto.setPayWay("1");
		wxPayDto.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+wxPayDto.getOrderId()+"/");
		
		//String browser = UserUtil.Browser(request);
		//String accessToken = "";//(String)redisService.queryObjectData("AccessToken");
		//if(accessToken==null){
			//accessToken = TenpayUtil.queryAppletAccessToken();
			//redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
		//}
		
		//if(browser.equals("wx")){
			/*ApiFrontUser user = new ApiFrontUser();
			user = CommonUtils.queryUser(request,openId,accessToken,unionId);
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("appletPay >> SSOUtil.login : "+e);
			 }*/
			 
			Integer userId=null;
			//根据unionId判断用户是否注册过
			ApiThirdUser thirdUser = new ApiThirdUser();
			thirdUser.setUnionid(unionId);
			thirdUser = userFacade.queryThirdUserByParam(thirdUser);
			if(thirdUser==null){//未注册
				String adressIp = SSOUtil.getUserIP(request);
				String nickName = StringUtil.filterEmoji(user.getNickName());
				user.setRegisterIP(adressIp);
				user.setNickName(nickName);
				user.setPersition(user.getCoverImageUrl());
				user.setUserType(PengPengConstants.PERSON_USER);
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserName(PengPengConstants.WENXIN + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
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
			thirdUser = new ApiThirdUser();
			thirdUser.setAccountNum(openId);
			thirdUser.setType("weixin");
			thirdUser.setUnionid(unionId);
			ApiResult result2 = userFacade.thirdUserRegistered(user, thirdUser);
			userId = Integer.valueOf(result2.getMessage());
			user = userFacade.queryById(userId);
			try{
				// 自动登录
				SSOUtil.login(user, request, response);
			}
			catch(Exception e)
			{
				logger.error("",e);
			}
			}
			else{
				userId=thirdUser.getUserId();
				user = userFacade.queryById(userId);
			}
			
			 //更新用户电话、真实姓名
			 if(from.getRealName()!=null&&from.getRealName()!=""){
	            user.setRealName(from.getRealName());
	         }
	         if(from.getMobileNum()!=null&&from.getMobileNum()!=""){
	            user.setMobileNum(from.getMobileNum());
	         }
	         if((from.getRealName()!=null&&from.getRealName()!="")||(from.getMobileNum()!=null&&from.getMobileNum()!="")){
	            userFacade.updateUser(user);
	         }
	         
	         //更新一对一
	         ApiOneAid model = new ApiOneAid();
			 model.setOrder_num(wxPayDto.getOrderId());
			 ApiPage<ApiOneAid> page = projectFacade.queryOneAidByParam(model, 1, 10);
			 if(page!=null && page.getTotal()==1 && page.getResultData().get(0).getState()==200){
				 model = new ApiOneAid();
				 model.setId(page.getResultData().get(0).getId());
				 model.setSupport_user_id(user.getId());
				 model.setSupport_user_name(user.getNickName());
				 model.setDonate_money(new BigDecimal(from.getAmount()));
				 projectFacade.updateOneAid(model);
				 logger.info("微信》》》》》》》》》更新一对一帮扶捐款用户信息");
			 }
	         
			String total_fee = String.valueOf(from.getAmount());
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(wxPayDto.getOrderId());
			dRecord.setSource("applet");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setLeaveWord(from.getDonateWord());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }
			
			if(from.getSlogans()!=null && !"".equals(from.getSlogans())){
				wxPayDto.setAttach(from.getSlogans());
			}else{
				wxPayDto.setAttach(String.valueOf(from.getProjectId()));
			}
			ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
			
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout,"tenpay",null,"");
			
			if (rt != null && rt.getCode() == 1) {
				try {
					SortedMap<String, String> map;
					map = H5Demo.getAppletPayview(wxPayDto);
					logger.info("map>>>"+map);
					result.put("appId", map.get("appId"));
					result.put("timestamp", map.get("timeStamp"));
					result.put("noncestr", map.get("nonceStr"));
					result.put("packageValue", map.get("packageValue"));
					result.put("paySign", map.get("paySign"));
					result.put("paysignType", map.get("signType"));
					item.put("result", result);
					item.put("code", 1);
					item.put("msg", "success");
					//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
					//String jsTicket;
					//accessToken = TenpayUtil.queryAccessToken();
					//logger.info("accessToken1>>>"+accessToken);
					//redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
					//jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
					//logger.info("jsTicket>>>"+jsTicket);
					//redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);	
				} catch (Exception e) {
					logger.error("微信小程序支付订单"+e.getStackTrace());
					item.put("code", -1);
					item.put("msg", "error");
				}
			} else {
				item.put("code", -1);
				item.put("msg", "error");
			}
		//}
		return item;
	}
	
	/**
	 * 项目捐款通用接口，不考虑多个公众号支付的情况,里面包含：1.一对一帮扶，普通项目捐款
	 * @param request
	 * @param response
	 * @param from
	 * @return
	 */
	@RequestMapping(value="/tenpay/commonPay",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject commonPay(HttpServletRequest request,HttpServletResponse response,
			DepositForm from,@RequestParam("weixinUrl") String weixinUrl){
		JSONObject item  = new JSONObject();
		JSONObject result  = new JSONObject();
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		//查询支付配置
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		if(browser.equals("wx")){
			if(userId==null){
			String weixin_code = request.getParameter("code");
			Map<String, Object> mapToken = new HashMap<String, Object>(8);
			try {
				if ("".equals(openId) || openId == null) {
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code="";
						url_weixin_code=H5Demo.getCodeRequest(perfecturl);
						result.put("url", url_weixin_code);
						item.put("result", result);
						item.put("code", 0);
						item.put("msg", "获取code");
						return item;
						/*view.setViewName("redirect:"+url_weixin_code);
						return view;*/
					}
					mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					if(mapToken.get("unionid")!=null){
						unionid = mapToken.get("unionid").toString();
					}
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
				}
			} catch (Exception e) {
				logger.error("获取微信openId access_token  unionId出现问题"+ e);
				item.put("code", -1);
				item.put("msg", "获取微信openId access_token  unionId出现问题");
				return item;
			}
			user = CommonUtils.queryUser(request,openId,Token,unionid);
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				logger.error("commonPay >> SSOUtil.login : "+e);
				item.put("code", -1);
				item.put("msg", "commonPay SSOUtil.login error");
				return item;
			 }
		}
		else{
			user = userFacade.queryById(userId);
			ApiThirdUser tuser = new ApiThirdUser();
			tuser.setUserId(userId);
			tuser = userFacade.queryThirdUserByParam(tuser);
			openId=tuser.getAccountNum();
		}
			
		 String orderId=StringUtil.uniqueCode();
		 //根据attach来判断是否是一对一帮扶 更新上订单号
		 if(from.getSlogans()!=null && from.getSlogans().contains("oneAid")){
			String[] attachs = from.getSlogans().split("\\_");
			if(attachs.length==2){
				ApiOneAid oneAid = new ApiOneAid();
				oneAid.setId(Integer.valueOf(attachs[1]));
				oneAid.setOrder_num(orderId);
				projectFacade.updateOneAid(oneAid);
			}
		 }		 
			
			 //更新用户电话、真实姓名
			 if(from.getRealName()!=null&&from.getRealName()!=""){
	            user.setRealName(from.getRealName());
	         }
	         if(from.getMobileNum()!=null&&from.getMobileNum()!=""){
	            user.setMobileNum(from.getMobileNum());
	         }
	         if((from.getRealName()!=null&&from.getRealName()!="")||(from.getMobileNum()!=null&&from.getMobileNum()!="")){
	            userFacade.updateUser(user);
	         }
	         //更新一对一
	         ApiOneAid model = new ApiOneAid();
			 model.setOrder_num(orderId);
			 ApiPage<ApiOneAid> page = projectFacade.queryOneAidByParam(model, 1, 10);
			 if(page!=null && page.getTotal()==1 && page.getResultData().get(0).getState()==200){
				 model = new ApiOneAid();
				 model.setId(page.getResultData().get(0).getId());
				 model.setSupport_user_id(user.getId());
				 model.setSupport_user_name(user.getNickName());
				 model.setDonate_money(new BigDecimal(from.getAmount()));
				 projectFacade.updateOneAid(model);
				 logger.info("微信》》》》》》》》》更新一对一帮扶捐款用户信息");
			 }
	         
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(orderId);
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(tpWxPay1.getOrderId());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setLeaveWord(from.getDonateWord());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }

			//6：个人用户
			if(from.getSlogans()!=null && !"".equals(from.getSlogans())){
				tpWxPay1.setAttach(from.getSlogans());
			}else{
				tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
			}
			ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout,"tenpay",null,"");
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
					tpWxPay1.setPayWay("1");
					tpWxPay1.setOpenId(openId);
					tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
					SortedMap<String, String> map;
					map = H5Demo.getPayweixinview(tpWxPay1);
					logger.info("commonPay map>>>"+map);
					result.put("appId",map.get("appId"));
					result.put("timestamp",map.get("timeStamp"));
					result.put("noncestr",map.get("nonceStr"));
					result.put("packageValue",map.get("packageValue"));
					result.put("paySign",map.get("paySign"));
					result.put("paysignType",map.get("signType"));
					/**/
					/*view.addObject("appId",map.get("appId"));
					view.addObject("timestamp",map.get("timeStamp"));
					view.addObject("noncestr",map.get("nonceStr"));
					view.addObject("packageValue",map.get("packageValue"));
					view.addObject("paySign",map.get("paySign"));
					view.addObject("paysignType",map.get("signType"));*/
					/**/
					String config_timestamp = String.valueOf(new Date().getTime());
					String config_noncestr = H5Demo.getNonceStr();
					result.put("config_timestamp",config_timestamp);
					result.put("config_noncestr",config_noncestr);
					/**/
					/*view.addObject("config_timestamp",config_timestamp);
					view.addObject("config_noncestr",config_noncestr);*/
					/**/
					//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
					String jsTicket,accessToken;
					accessToken = TenpayUtil.queryAccessToken();
					redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
					jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
					redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
					result.put("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, weixinUrl));
					System.out.println("weixinUrl >> "+weixinUrl);
					/***/
					//view.addObject("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
					
					item.put("result", result);
					item.put("code", 1);
					item.put("msg", "success");
					return item;
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					item.put("code", -1);
					item.put("msg", "获取支付参数error");
					return item;
				}
			} else {
				//系统的后台交互
				item.put("code", -1);
				item.put("msg", "add order error");
				return item;
			}
		}else{
			item.put("code", 2);
			item.put("msg", "不是微信浏览器");
			return item;
		}
	}
}
