package com.guangde.home.controller.deposit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiRedpackets;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.demo.WxPayDto;
import com.tenpay.utils.TenpayUtil;

/** 
 * 在手机，pad等无限的设备上微信的支付
 * 对于游客匿名的捐赠
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("visitorAlipay")
public class RechargeVisitorTenpayByH5Action extends DepositBaseAction {

	public static final String GetAccess_tokenRequest="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa09ee42dbe779694&secret=c8c9005d568c7575770df85d9c92a87c&grant_type=authorization_code";
	public static final String GetAccess_userinfoRequest = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN";
    // 批量捐项目id
    public static final int PROJECTID = 285;
	Logger logger = LoggerFactory.getLogger(RechargeVisitorTenpayByH5Action.class);

	@Autowired
	private IProjectFacade projectFacade;

	@Autowired
	private RedisService redisService;
	
    @Autowired
    private ICompanyFacade companyFacade;
    
	@Autowired
	private IRedPacketsFacade redPacketsFacade;

	@RequestMapping(value = "/tenpay/rechargedeposit")
	public ModelAndView weixindeposit2(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {

		ModelAndView view = new ModelAndView("h5/weixin_recharge");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
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
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					request.setAttribute("code", null);
				}
			} catch (Exception e) {
				logger.error("batchVisitorAlipay weixindeposit2 weixi chuli >> "+ e);
			}
			view.addObject("payway", browser);
		}
		
		ApiProject project = projectFacade.queryProjectDetail(PROJECTID);
		
		 view.addObject("pName", "善基金充值");
		 view.addObject("amount", from.getAmount());
		 view.addObject("perMoney", from.getAmount());
		 view.addObject("nickName", from.getTouristName());
		 view.addObject("mobile", from.getTouristMobile());
		 
		if(browser.equals("wx")){
			
			ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
			try
			{
				// 自动登录
				SSOUtil.login(user, request, response);
			}
			catch(Exception e)
			{
				logger.error("rechargedeposit weixindeposit2 >> SSOUtil.login : "+e);
			}
			
			 view.addObject("user",user);
        
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			tpWxPay1.setBody("善基金充值");
			if(from.getSlogans()!=null && !"".equals(from.getSlogans())){
				tpWxPay1.setAttach(from.getSlogans());
			}else{
				tpWxPay1.setAttach(String.valueOf(PROJECTID));
			}
			
			
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("H5");
			capitalinout.setMoney(from.getAmount());

			capitalinout.setPayType("tenpay");
		
			capitalinout.setUserId(user.getId());
			capitalinout.setBankType(from.getBank());
			
			//tpWxPay1.setAttach(String.valueOf(PROJECTID));
			
			ApiResult rt = companyFacade.companyReCharge(capitalinout);
			logger.debug("rechargedeposit weixindeposit2 companyReCharge rt >> "+rt);
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/batchpay/tenpay/asyncresult2/"+tpWxPay1.getOrderId()+"/");
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
						 view.addObject("projectId", PROJECTID);
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

		return view;
	}
	
	/**
	 * 发红包 充值支付
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenpay/rechargedeposit2")
	public ModelAndView weixindeposit3(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {

		ModelAndView view = new ModelAndView("h5/weixin_recharge2");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
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
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					request.setAttribute("code", null);
				}
			} catch (Exception e) {
				logger.error("batchVisitorAlipay weixindeposit2 weixi chuli >> "+ e);
			}
			view.addObject("payway", browser);
		}
		
		 view.addObject("pName", "善基金充值");
		 view.addObject("amount", from.getAmount());
		 view.addObject("perMoney", from.getAmount());
		 view.addObject("pcount", from.getPcount());
		 view.addObject("slogans", from.getSlogans());
		 
		if(browser.equals("wx")){
			
			ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
			try
			{
				// 自动登录
				SSOUtil.login(user, request, response);
			}
			catch(Exception e)
			{
				logger.error("rechargedeposit weixindeposit2 >> SSOUtil.login : "+e);
			}
			
			 view.addObject("user",user);
        
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			tpWxPay1.setBody("善基金充值");
			
			
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("H5");
			capitalinout.setMoney(from.getAmount());

			capitalinout.setPayType("tenpay");
		
			capitalinout.setUserId(user.getId());
			capitalinout.setBankType(from.getBank());
			
			//tpWxPay1.setAttach(String.valueOf(PROJECTID));
			
			ApiResult rt = companyFacade.companyReCharge(capitalinout);
			logger.debug("rechargedeposit weixindeposit2 companyReCharge rt >> "+rt);
			ApiRedpackets redpackets = new ApiRedpackets();
			redpackets.setType(0);// 0 - 随机 ， 1 - 平均
			redpackets.setBtype(0);
			redpackets.setHavedays(7);
			redpackets.setUserid(user.getId());
			redpackets.setStatus(200);
			redpackets.setCreatetime(new Date());
			redpackets.setUsednum(0);
			redpackets.setBackamount(0.0);
			redpackets.setUsedamount(0.0);
			redpackets.setAppointproject("person");// 个人红包
			redpackets.setTotalamount(from.getAmount());
			redpackets.setTotalnum(from.getPcount());
			
			if(user != null)
			{
				redpackets.setCompanyname(user.getNickName());
			}
			ApiResult res = redPacketsFacade.saveRedpacketDraft(redpackets);
			if(res.getCode() == 1)
			{
				 view.addObject("redpacketsId", res.getData());
				 tpWxPay1.setAttach(String.valueOf(res.getData()));
			}
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("4");
						tpWxPay1.setOpenId(openId);
		
						tpWxPay1.setNotifyUrl("http://www.17xs.org/batchpay/tenpay/asyncresult3/"+tpWxPay1.getOrderId()+"_"+user.getId()+"/");
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
						
						 view.addObject("amount", from.getAmount());
						 view.addObject("tradeNo", tpWxPay1.getOrderId());
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

		return view;
	}
	
	/**
	 * 善园充值
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenpay/rechargedeposit_sy")
	public ModelAndView weixindeposit_sy(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {

		ModelAndView view = new ModelAndView("h5/weixin_syRecharge");
		view.addObject("message", from.getDonateTimeId());
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
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
					redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					request.setAttribute("code", null);
				}
			} catch (Exception e) {
				logger.error("batchVisitorAlipay weixindeposit2 weixi chuli >> "+ e);
			}
			view.addObject("payway", browser);
		}
		
		ApiProject project = projectFacade.queryProjectDetail(PROJECTID);
		
		 view.addObject("pName", "善基金充值");
		 view.addObject("amount", from.getAmount());
		 view.addObject("perMoney", from.getAmount());
		 view.addObject("nickName", from.getTouristName());
		 view.addObject("mobile", from.getTouristMobile());
		 
		if(browser.equals("wx")){
			
			ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
			try
			{
				// 自动登录
				SSOUtil.login(user, request, response);
			}
			catch(Exception e)
			{
				logger.error("rechargedeposit weixindeposit2 >> SSOUtil.login : "+e);
			}
			
			 view.addObject("user",user);
        
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			tpWxPay1.setBody("善基金充值");
			tpWxPay1.setAttach(String.valueOf(PROJECTID));
			
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("H5");
			capitalinout.setMoney(from.getAmount());

			capitalinout.setPayType("tenpay");
		
			capitalinout.setUserId(user.getId());
			capitalinout.setBankType(from.getBank());
			
			//tpWxPay1.setAttach(String.valueOf(PROJECTID));
			
			ApiResult rt = companyFacade.companyReCharge(capitalinout);
			logger.debug("rechargedeposit weixindeposit2 companyReCharge rt >> "+rt);
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/batchpay/tenpay/asyncresult2/"+tpWxPay1.getOrderId()+"/");
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
						 if(from.getProjectId()!=0&&from.getProjectId()!=285){
							 view.addObject("projectId", from.getProjectId());
						 }
						 else{
							 view.addObject("projectId", PROJECTID);
						 }
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
		view.addObject("projectId", from.getProjectId());

		return view;
	}
	
}
