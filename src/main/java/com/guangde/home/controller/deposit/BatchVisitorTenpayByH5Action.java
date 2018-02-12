package com.guangde.home.controller.deposit;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiThirdUser;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
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
public class BatchVisitorTenpayByH5Action extends DepositBaseAction {

	// 服务器异步通知页面路径
	public static final String notify_url = "http://www.17xs.org/visitorAlipay/async.do";
	// 页面跳转同步通知页面路径
	public static final String return_url = "http://www.17xs.org/visitorAlipay/return_url.do";
	public static final String GetAccess_tokenRequest="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa09ee42dbe779694&secret=c8c9005d568c7575770df85d9c92a87c&grant_type=authorization_code";
	public static final String GetAccess_userinfoRequest = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN";
    // 批量捐项目id
    public static final int PROJECTID = 285;
	Logger logger = LoggerFactory.getLogger(BatchVisitorTenpayByH5Action.class);
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;

	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/tenpay/batchdeposit")
	public ModelAndView weixindeposit2(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
    	String touristName = from.getTouristName() == null?"":from.getTouristName();
    	String touristMobile = from.getTouristMobile() == null?"":from.getTouristMobile();
		ModelAndView view = new ModelAndView("h5/weixin_3");
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
		
		 view.addObject("pName", project.getTitle());
		 view.addObject("amount", from.getAmount());
		 view.addObject("projectId", PROJECTID);
		 view.addObject("copies", from.getCopies());
		 view.addObject("plistId", from.getPlistId());
		 view.addObject("pcount", from.getPcount());
		 view.addObject("perMoney", from.getPerMoney());
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
				logger.error("batchVisitorAlipay weixindeposit2 >> SSOUtil.login : "+e);
			}
			
			 view.addObject("user",user);
             String nickName = user.getNickName() == null ?"":user.getNickName();
             String mobileNum = user.getMobileNum() == null ?"":user.getMobileNum();
             if(!touristName.equals(nickName) || !touristMobile.equals(mobileNum))
				 {
				 ApiFrontUser  fur = new ApiFrontUser();
				 fur.setNickName(StringUtil.filterEmoji(touristName));
				 fur.setMobileNum(touristMobile);
				 fur.setId(user.getId());
				 userFacade.updateUser(fur);
			 }
			
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
			dRecord.setLeaveWord(from.getPlistId());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }

			//6：个人用户
			tpWxPay1.setAttach(String.valueOf(PROJECTID));
			ApiResult rt = donateRecordFacade.batchBuyDonate(dRecord, null, "tenpay");
			logger.debug("batchVisitorAlipay weixindeposit2 batchBuyDonate rt >> "+rt);
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/batchpay/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
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
}
