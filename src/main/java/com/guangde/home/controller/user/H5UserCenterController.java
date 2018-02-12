package com.guangde.home.controller.user;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.api.user.IUserRelationInfoFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiDonateTime;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiFrontUser_address;
import com.guangde.entry.ApiFrontUser_invoice;
import com.guangde.entry.ApiGift;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiThirdUser;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.ApiUser_Redpackets;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;
@Controller
@RequestMapping("ucenter")
public class H5UserCenterController {
	private final Logger logger = LoggerFactory.getLogger(H5UserCenterController.class);
	private static final String phonecodeprex = "phonecode_r_";
	private static final String imgcodeprex = "imgcode_r_";
	@Autowired
	private IProjectFacade projectFacade;
	
	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private IFileFacade fileFacade;
	
    @Autowired
    private ICommonFacade commonFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	/**
	 * 用户中心
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value = "core/personalCenter_h5")
	public ModelAndView personalCenter(HttpServletRequest request,
			HttpServletResponse response) throws JDOMException, IOException {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("h5/ucenter/pCenter");
		if (userId == null) {
			view = new ModelAndView("redirect:/index/index_h5.do");
			return view;
		}
	
		ApiFrontUser user = userFacade.queryById(userId);
		//可提取金额
		view.addObject("tiqu", 0);
		view.addObject("guideName", "首页");
		// 企业余额(账户余额)
		if (user.getCoverImageId() == null) {
			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
		} else {
			if(!StringUtils.isEmpty(user.getContentImageId())){
				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getContentImageId()));
				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
			}
		}
		   ApiProjectFeedback feedBack = new ApiProjectFeedback();
	       feedBack.setFeedbackPeople(userId);
	       feedBack.setAuditState(203);
	       
		//最新动态
		Integer careCount  = projectFacade.countCareProjectFeedbckByCondition(feedBack);
		view.addObject("user", user);
		view.addObject("careCount", careCount);
		view.addObject("itemType",request.getParameter("itemType")==null?"":request.getParameter("itemType"));
		view.addObject("pstate",request.getParameter("pstate")==null?"":request.getParameter("pstate"));
		//微信功能调用的操作授权
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		view = CommonUtils.wxView(view, request,perfecturl);
		return view;
	}
	
	
	
	/**
	 * h5发布页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coreHelp/publish_h5")
	public ModelAndView publish(HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView view = new ModelAndView("h5/ucenter/publish_1");
		
		  //Object obj = redisService.queryObjectData(PengPengConstants.SEEKHELP_FIELD);
	      //  if (obj != null)
	      //  {
	       //     view.addObject("atc", obj);
	       // }
	       // else
	       // {
	            List<ApiTypeConfig> atc = commonFacade.queryList();
	       //     redisService.saveObjectData(PengPengConstants.SEEKHELP_FIELD, atc, DateUtil.DURATION_WEEK_S);
	            view.addObject("atc", atc);
	      //  }
	        return view;

		
	}
	
	/**
	 * h5发布提交页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coreHelp/publish_submit")
	public ModelAndView publish_submit(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "typeName", required = true) String typeName
			,@RequestParam(value = "tName", required = true) String tName) {
	
		ModelAndView view = new ModelAndView("h5/ucenter/publish_2");
		view.addObject("typeName", typeName);
		view.addObject("tName", tName);
		
	    return view;

	}
	
	/**
	 * h5登录页面显示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "user/Login_H5")
	public ModelAndView Login_H5(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="flag",required=false)String flag) {
		//ModelAndView view = new ModelAndView("sso/login_h5");
		ModelAndView view = new ModelAndView("h5/ucenter/userLogin");
		String browser = UserUtil.Browser(request);
		if(flag != null){
			view.addObject("flag", flag);
		}
		view.addObject("browser", browser);
		return view;

	}
	
	@RequestMapping(value = "wechat_login")
	@ResponseBody
	public Map<String, Object> wechat_login(
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		
		String browser = UserUtil.Browser(request);
		//browser = "wx";
		if("wx".equals(browser) || userId !=null)
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
		else
		{
			return webUtil.resMsg(0, "0007", "登陆失败", null);
		}
	
	}
	
	/**
	 * 微信授权登陆
	 * @return
	 */
	
	@RequestMapping(value = "user/login_wechat")
	public ModelAndView login_wechat(HttpServletRequest request,
			HttpServletResponse response)
	{
		
		ModelAndView view = new ModelAndView("h5/ucenter/userCenter");
		
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		ApiThirdUser tuser = null ;
		  //验证是否登入
        Integer  loginUserId = UserUtil.getUserId(request, response);
        if(loginUserId != null && loginUserId != 0)
        {
     	  user = userFacade.queryById(loginUserId);
     	  view.addObject("user", user);
        }
        else
        {
        	//browser = "wx";
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("giftDetail >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
	   				//openId = "oxmc3uBBzySbantAofWrdLQgYzq0" ;
	   				//OToken = "test";
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				unionid = mapToken.get("unionid").toString();
        				token = mapToken.get("access_token").toString();
        				System.out.println("giftDetail >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			e.printStackTrace();
        			logger.error("giftDetail >> "+ e);
        		}
        	
        		try
        		{
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        		
        			logger.error("login_wechat >>  : ",e);
        		}
        	}
        	else
        	{
				view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
				return view;
        	}
        	
        }
        return  view ;
	}
	

	
	/**
	 * 用户登入
	 * 
	 * @param type
	 * @param phone
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login")
	@ResponseBody
	public Map<String, Object> login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "passWord", required = false) String passWord,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "entrance", required = false) String entrance,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type) {


		if (UserUtil.verifyUserName(name) != null) {
			return webUtil.resMsg(0, "0001", "登入名错误", null);
		} else {
			if (StringUtil.isMobile(name)) {
				// 手机登入
				// todo
			}
			// 账号登入
			// todo
			ApiFrontUser user = new ApiFrontUser();
			user.setUserName(name);
			user.setUserPass(passWord);
			Map<String, Object> temp = login(user, request, response,
					userFacade);
			String msg = (String) temp.get("msg");
			if (msg != null) {
				return webUtil.resMsg(0, "0002", msg, null);
			} else {
				return webUtil.resMsg(1, "0000", "成功", temp.get("user"));
			}
		}
	}
	
	private Map<String, Object> login(ApiFrontUser user,
			HttpServletRequest request, HttpServletResponse response,
			IUserFacade userFacade) {
		// 1.调用服务端接口
		if (logger.isDebugEnabled()) {
			logger.debug("登入：user " + user);
		}
		ApiResult result = userFacade.localUserlogin(user);
		if (logger.isDebugEnabled()) {
			logger.debug("登入：result " + result);
		}
		Map<String, Object> temp = new HashMap<String, Object>();
		if (result.getCode() != 1) {
			if (result.getCode() == -1) {
				temp.put("msg", "参数错误");
			} else if (result.getCode() == -2) {
				temp.put("msg", "用户不存在");
			} else if (result.getCode() == -3) {
				if("5".equals(user.getDescription())){
					temp.put("msg", "旧密码错误");
				}else {
					temp.put("msg", "密码错误");
				}
			} else if (result.getCode() == -101) {
				temp.put("msg", "此用户已停用");
			} else if (result.getCode() == -102) {
				temp.put("msg", "此用户已暂停");
			} else if (result.getCode() == -103) {
				temp.put("msg", "此用户已锁定");
			} else {
				temp.put("msg", "登入失败");
			}
		} else {
			user = (ApiFrontUser)result.getObject();
			PUser huser = new PUser();
			huser.setId(Integer.parseInt(result.getData()));
			huser.setName(user.getUserName());

			// 2.保存cookie,session
			SSOUtil.login(user, request, response);
			temp.put("user", huser);
		}
		return temp;
	}
	/**
	 * 新版h5个人中心
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "userCenter_h5")
	public ModelAndView userCenter(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("h5/ucenter/userCenter");
		/*
		if (userId == null) {
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view;
		}
		*/
		//===========微信用户自动登陆start==============//
		
		String openId ="";
		String unionid = "";
		String token = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		
        if(userId != null && userId != 0)
        {
     	  user = userFacade.queryById(userId);
     	  view.addObject("user", user);
        }
        else
        {
        	/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
        			 openId = (String) request.getSession().getAttribute("openId");
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("userCenter_h5 >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				System.out.println("userCenter_h5 >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        			userId = user.getId();
        		} catch (Exception e) {
        			logger.error("微信支付处理出现问题"+ e);
        		}
        		view.addObject("payway", browser);
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        		
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		
		//===========微信用户自动登陆end==============//
		try
		{
			
			ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
			apiDonateRecord.setUserId(userId);
			apiDonateRecord.setState(302);
			// 捐款总金额
			Double  totalAmount = donateRecordFacade.countQueryByCondition(apiDonateRecord);
			// 捐款次数
			Integer donationNum = donateRecordFacade.countNumQueryByCondition(apiDonateRecord);
			// 待付款
			apiDonateRecord = new ApiDonateRecord();
			apiDonateRecord.setUserId(userId);
			apiDonateRecord.setState(300);
			Integer donationWait = donateRecordFacade.countNumQueryByCondition(apiDonateRecord);
			// 筹款中
			apiDonateRecord = new ApiDonateRecord();
			apiDonateRecord.setUserId(userId);
			apiDonateRecord.setPstate(240);
			Integer donatingNum = donateRecordFacade.countQueryMyDonateRecordList(apiDonateRecord);
			// 已结束
			apiDonateRecord = new ApiDonateRecord();
			apiDonateRecord.setUserId(userId);
			apiDonateRecord.setPstate(260);
			Integer donatedNum = donateRecordFacade.countQueryMyDonateRecordList(apiDonateRecord);
			
			ApiProjectFeedback feedBack = new ApiProjectFeedback();
			feedBack.setFeedbackPeople(userId);
			feedBack.setAuditState(203);
			//最新动态
			Integer careCount  = projectFacade.countCareProjectFeedbckByCondition(feedBack);
			
			//用户可用红包数
			ApiUser_Redpackets apiUserRedpackets = new ApiUser_Redpackets();
			apiUserRedpackets.setStatus(401);
			apiUserRedpackets.setUserId(userId);
			apiUserRedpackets.setStatusList(null);
			Integer redPaperNum = redPacketsFacade.countRedPaperNumByCondition(apiUserRedpackets);
			
			
			//可开票金额()
			ApiDonateRecord r = new ApiDonateRecord();
			r.setUserId(userId);
			r.setState(302);
			ApiFrontUser_invoice inv = new ApiFrontUser_invoice();
			inv.setUserId(userId);
			ApiPage<ApiFrontUser_invoice> invoices = userRelationInfoFacade.queryInvoiceByCodition(inv, 0, 1000000);
			List<Integer> idList = new ArrayList<Integer>();
			try
			{
				if(invoices.getResultData() != null && invoices.getResultData().size()>0)
				{
					for(ApiFrontUser_invoice invo : invoices.getResultData())
					{
						if(!StringUtils.isEmpty(invo.getInfo()))
						{
							String[] idary = invo.getInfo().split("_");
							if(idary != null && idary.length>0)
							{
								for(int i = 0 ; i<idary.length;i++)
								{
									
									idList.add(Integer.parseInt(idary[i]));
								}
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.error("idList >> ", e);
			}
			
			if(idList != null && idList.size()>0)
			{
				r.setIdList(idList);
			}
			// 可开发票总金额
			Double ticketAmount = donateRecordFacade.sumQueryDonateListByInvoice(r);
			
			ApiGift gift = new ApiGift();
			gift.setUserId(userId);
			//可兑换礼品数量
			Integer giftNum = userRelationInfoFacade.countQueryGiftByCoditionNew(gift);
			
			ApiProject p = new ApiProject();
			p.setUserId(userId);
			//是否开通月捐
			ApiPage<ApiDonateTime> apiDonateTimes = donateRecordFacade.queryByUserId(userId,1,5);
			if(apiDonateTimes != null && apiDonateTimes.getResultData().size() > 0) {
				view.addObject("monthlyDonate", "已开通");
			}else {
				view.addObject("monthlyDonate", "未开通");
			}
			
			// 分享的项目参与的人数
			Integer peoNum = projectFacade.coutQueryShareProjectUser(p);
			
			ApiProject ap = new ApiProject();
			/*
	    List<Integer> states = new ArrayList<Integer>();
	    states.add(ProjectConstant.PROJECT_STATUS_COLLECT);
	    states.add(ProjectConstant.PROJECT_STATUS_DONE);
	    ap.setStates(states);
			 */
			ap.setUserId(userId);
			Integer cryDonatNum = projectFacade.countDonatePeopleByUser(ap);
			
			 user = userFacade.queryById(userId);
			
			view.addObject("donationWait",donationWait);// 待付款
			view.addObject("totalAmount",totalAmount);// 捐款金额
			view.addObject("donationNum",donationNum);// 捐赠次数
			view.addObject("donatingNum",donatingNum);// 筹款中
			view.addObject("donatedNum",donatedNum);// 已结束
			view.addObject("careCount",careCount);// 最新动态
			view.addObject("ticketAmount",ticketAmount);// 可开票金额
			view.addObject("giftNum",giftNum);// 可开票金额
			view.addObject("peoNum",peoNum);// 分享的项目参与的人数
			view.addObject("cryDonatNum",cryDonatNum);// 求助项目 参与捐助的爱心人数
			view.addObject("redPaperNum", redPaperNum);  //用户可用红包数
			view.addObject("user",user);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("h5 userCenter >> ",e);
		}

		return view;
	}
	
	/**
	 * 开票记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "myInvoice")
	public ModelAndView toInvoice(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/ucenter/invoice");
		Integer userId = UserUtil.getUserId(request, response);
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		
        if(userId != null && userId != 0)
        {
     	  user = userFacade.queryById(userId);
     	  view.addObject("user", user);
        }
        else
        {
        	/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("toInvoice >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
	   			
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				System.out.println("toInvoice >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        			userId = user.getId();
        		} catch (Exception e) {
        			logger.error("微信支付处理出现问题"+ e);
        		}
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        		
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		
		//==================微信登陆end================//
       
		ApiDonateRecord record = new ApiDonateRecord();
		record.setUserId(userId);
		//可开票金额()
        ApiDonateRecord r = new ApiDonateRecord();
        r.setUserId(userId);
        r.setState(302);
        ApiFrontUser_invoice inv = new ApiFrontUser_invoice();
        inv.setUserId(userId);
        ApiPage<ApiFrontUser_invoice> invoices = userRelationInfoFacade.queryInvoiceByCodition(inv, 0, 1000000);
        List<Integer> idList = new ArrayList<Integer>();
        try
        {
        	if(invoices.getResultData() != null && invoices.getResultData().size()>0)
        	{
        		for(ApiFrontUser_invoice invo : invoices.getResultData())
        		{
        			if(!StringUtils.isEmpty(invo.getInfo()))
        			{
        				String[] idary = invo.getInfo().split("_");
        				if(idary != null && idary.length>0)
        				{
        					for(int i = 0 ; i<idary.length;i++)
        					{
        						
        						idList.add(Integer.parseInt(idary[i]));
        					}
        				}
        			}
        		}
        	}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	logger.error("idList >> ", e);
        }
        
        if(idList != null && idList.size()>0)
        {
        	r.setIdList(idList);
        }
        // 可开发票总金额
		Double totalAmount = donateRecordFacade.sumQueryDonateListByInvoice(r);
		
		// 待发货
		ApiFrontUser_invoice apiInvoice = new ApiFrontUser_invoice();
		apiInvoice.setUserId(userId);
		apiInvoice.setState(300);
		Integer dnum = userRelationInfoFacade.countQueryInvoiceByCodition(apiInvoice);
		
		//已发货
		apiInvoice = new ApiFrontUser_invoice();
		apiInvoice.setUserId(userId);
		apiInvoice.setState(302);
		Integer ynum = userRelationInfoFacade.countQueryInvoiceByCodition(apiInvoice);
		
		
		view.addObject("totalAmount",totalAmount);
		view.addObject("dnum",dnum);
		view.addObject("ynum",ynum);
		
		return view;
	}
	
	/**
	 * 我的礼品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "myGift")
	public ModelAndView myGift(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/ucenter/gift");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		
		ApiGift gift = new ApiGift();
    	gift.setUserId(userId);
    	//可兑换数量
    	Integer num = userRelationInfoFacade.countQueryGiftByCoditionNew(gift);
    	
    	
    	gift = new ApiGift();
    	gift.setIsUnder(1);
    	gift.setUserId(userId);
    	//失效礼品数量
    	Integer sxNum = userRelationInfoFacade.countQueryGiftParam(gift);
    	
    	// 已领取
    	gift = new ApiGift();
    	gift.setUserId(userId);
    	Integer hasNum = userRelationInfoFacade.countQueryHasGiftList(gift);
    	
    	view.addObject("num", num);
    	view.addObject("sxNum", sxNum);
    	view.addObject("hasNum", hasNum);
    	
		return view;
	}
	
	/**
	 * 礼品详情页
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "exchangeGift")
	public ModelAndView exchangeGift(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id) {
		ModelAndView view = new ModelAndView("h5/ucenter/exchangeDetail");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		view.addObject("id",id);
		return view;
	}
	
	/**
	 * 我的证书
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "myCertificate")
	public ModelAndView myCertificate(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "userId", required = false) Integer userId) {
		ModelAndView view = new ModelAndView("h5/ucenter/MyCertificate");
		if(userId ==  null || userId == 0)
		{
			userId = UserUtil.getUserId(request, response);
		}
		
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		ApiDonateRecord  record = new ApiDonateRecord();
		record.setUserId(userId);
		// 捐款过的项目数
		Integer myDonatePro = donateRecordFacade.countQueryMyDonateRecordList(record);
		
		ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
		apiDonateRecord.setUserId(userId);
		apiDonateRecord.setState(302);
		// 捐款总金额
		Double  totalAmount = donateRecordFacade.countQueryByCondition(apiDonateRecord);
		// 捐款次数
		Integer donationNum = donateRecordFacade.countNumQueryByCondition(apiDonateRecord);
		
		SimpleDateFormat sm = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year = sm.format(date);
		sm =  new SimpleDateFormat("MM");
		String month = sm.format(date);
		sm =  new SimpleDateFormat("dd");
		String day = sm.format(date);
		
		
		String browser = UserUtil.Browser(request);
		try
		{
			
			if(browser.equals("wx")){
				//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
				String jsTicket = (String) redisService.queryObjectData("JsapiTicket");
				String accessToken = (String) redisService.queryObjectData("AccessToken");
				if(jsTicket == null || accessToken == null){
					accessToken = TenpayUtil.queryAccessToken();
					redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
					jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
					redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
				}
				StringBuffer url = request.getRequestURL();
				String queryString = request.getQueryString();
				String perfecturl = url + "?" + queryString;
				SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
				view.addObject("appId",map.get("appId"));
				view.addObject("timestamp",map.get("timeStamp"));
				view.addObject("noncestr",map.get("nonceStr"));
				view.addObject("signature",map.get("signature"));
			}
		}
		catch(Exception e)
		{
			logger.error("myCertificate >> ",e);
		}
		
        ApiDonateRecord r = new ApiDonateRecord();
        r.setUserId(userId);
        r.setState(302);
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryMyDonateRecordList(r, 1,2);
        List<ApiDonateRecord> list =  donats.getResultData() ;
        view.addObject("list",list);
		ApiFrontUser user = userFacade.queryById(userId);
		if(user.getCoverImageUrl() == null)
		{
			user.setCoverImageUrl("http://www.17xs.org/res/images/user/user-icon-gt.png");
		}
		view.addObject("browser",browser);
		view.addObject("user",user);
		view.addObject("year",year);// 年
		view.addObject("month",month);// 月
		view.addObject("day",day);// 日
		view.addObject("totalAmount",totalAmount);// 捐款总金额
		view.addObject("donationNum",donationNum);// 捐款次数
		view.addObject("myDonatePro",myDonatePro);// 捐款过的项目数
		
		String shareType = request.getParameter("shareType") == null?"":request.getParameter("shareType");
		view.addObject("shareType",shareType);
		
		return view;
	}
	
	/**
	 * 我的分享
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "myShare")
	public ModelAndView myShare(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/ucenter/share");
		Integer userId = UserUtil.getUserId(request, response);
		
		//==================微信登陆start================//
		
		
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		
        if(userId != null && userId != 0)
        {
     	  user = userFacade.queryById(userId);
     	  view.addObject("user", user);
        }
        else
        {
        	/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("myShare >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
	   			
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				System.out.println("myShare >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        			userId = user.getId();
        		} catch (Exception e) {
        			logger.error("微信支付处理出现问题"+ e);
        		}
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        		
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		
		//==================微信登陆end================//
		
		ApiProject p = new ApiProject();
		p.setUserId(userId);
		// 分享的项目数
		Integer proNum = projectFacade.coutQueryShareProject(p);
		
		// 分享的项目参与的人数
		Integer peoNum = projectFacade.coutQueryShareProjectUser(p);
		
		// 分享的项目 募集到的善款总额
		Double  donatAmount = projectFacade.sumQueryShareProject(p);
	
		view.addObject("proNum",proNum);
		view.addObject("peoNum",peoNum);
		view.addObject("donatAmount",donatAmount);
		
		return view;
	}
	
	
	/**
	 * 账户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value = "accountInfo")
	public ModelAndView accountInfo(HttpServletRequest request,
			HttpServletResponse response) throws JDOMException, IOException {
		ModelAndView view = new ModelAndView("h5/ucenter/accountInfo");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		ApiFrontUser user = userFacade.queryById(userId);
		ApiFrontUser_address add = new ApiFrontUser_address();
		add.setUserId(userId);
		
		List<ApiFrontUser_address> ret = userRelationInfoFacade.queryUserAddress(add, 1, 10);
	     if(StringUtils.isEmpty(user.getCoverImageUrl()))
	     {
	    	 user.setCoverImageUrl("http://www.17xs.org/res/images/user/user-icon-gt.png");
	     }
		view.addObject("addsNum",ret.size());
		view.addObject("user",user);
		
		//微信功能调用的操作授权
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		view = CommonUtils.wxView(view, request,perfecturl);
		return view;
	}
	
	/**
	 * 收货地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addressList")
	public ModelAndView addressList(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/ucenter/addressList");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		return view;
	}
	
	/**
	 * 编辑昵称
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "editUserName")
	public ModelAndView editUserName(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/ucenter/updateName");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
		String userName = request.getParameter("userName") == null ? "" :request.getParameter("userName");
		
		view.addObject("userName",userName);
		
		return view;
	}
	

	
	@RequestMapping(value = "editUser")
	@ResponseBody
	public Map<String, Object> editUser(
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String nickName = request.getParameter("nickName");
		ApiFrontUser user = new ApiFrontUser();
		user.setId(userId);
		user.setNickName(nickName);
		
		if(StringUtils.isEmpty(nickName))
		{
			return webUtil.resMsg(0, "0001", "用户昵称不能为空", null);
		}
		
		/*
		ApiResult ck = userFacade.userExistence(nickName);
		if(ck.getCode()== -4)
		{
			return webUtil.resMsg(0, "0007", "用户名已存在", null);
		}
		*/
		ApiResult result = userFacade.updateUser(user);
		
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "用户昵称更新失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "register")
	public ModelAndView register(HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView view = new ModelAndView("h5/ucenter/register");
	
		return view;
	}
	
	
	/**
	 * 我的捐助项目列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "myDonate")
	public ModelAndView myDonate(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "userId", required = false) Integer userId) 
	{
		
		ModelAndView view = new ModelAndView("h5/ucenter/myDonate");
		if(userId == null || userId ==0)
		{
			userId = UserUtil.getUserId(request, response);
		}
		//==================微信登陆start================//
		
		Integer extensionPeople = (Integer)request.getSession().getAttribute("extensionPeople");
		view.addObject("extensionPeople",extensionPeople);
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		
        if(userId != null && userId != 0)
        {
     	  user = userFacade.queryById(userId);
     	  view.addObject("user", user);
        }
        else
        {
        	/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("myDonate >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
	   			
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				System.out.println("myDonate >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			logger.error("微信支付处理出现问题"+ e);
        		}
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        		
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        	}
        }
		
		//==================微信登陆end================//
        
		return view;
	}

	/**
	 * 个人实名认证
	 * @param request
	 * @param response
	 * @param apiFrontUser
	 * @param ids
	 * @param personalVerCode
	 * @return
	 */
	@RequestMapping("authentication")
	@ResponseBody
	public Map<String, Object> authentication(HttpServletRequest request,
			HttpServletResponse response,ApiFrontUser apiFrontUser,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "phoneCode", required = true) String phoneCode,
			@RequestParam(value = "code", required = true) String code){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null || userId==0){//未登录
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		
		String codekey = getImgCode(request);
		codeR = CodeUtil.VerifiCode(codekey,storeManage,code,true);
		if (codeR == -1) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码过期", null);
		} else if (codeR == 0) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码错误", null);
		}
		// 1.校验参数
		ApiFrontUser user = userFacade.queryById(userId);
		if (StringUtil.isMobile(apiFrontUser.getMobileNum())) {
			// 校验手机验证码
			phonekey = CodeUtil.certificationprex + apiFrontUser.getMobileNum();

			 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					 phoneCode, true);
			 if(codeR==-1){
			 return webUtil.resMsg(0, "0005","手机验证码过期", null);
			 }else if(codeR==0){
			 return webUtil.resMsg(0, "0005","手机验证码错误", null);
			 }
		} else {
			// 手机格式错误
			msg = "0001";
			return webUtil.resMsg(0, msg, "手机格式错误", null);
		}
		apiFrontUser.setId(userId);
		int res = userFacade.updateUserAndAudit(apiFrontUser, ids);
		int flag=0;
		if(res>0)
			flag=1;
		return webUtil.resMsg(flag, "0000", "0,"+userId+","+res, null);
	}
	
	@RequestMapping("authenticationTeam")
	@ResponseBody
	public Map<String, Object> authenticationTeam(HttpServletRequest request,
			HttpServletResponse response,ApiCompany apiCompany,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "phoneCode", required = true) String phoneCode,
			@RequestParam(value = "code", required = true) String code){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null || userId==0){//未登录
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		String codekey = getImgCode(request);
		codeR = CodeUtil.VerifiCode(codekey,storeManage,code,true);
		if (codeR == -1) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码过期", null);
		} else if (codeR == 0) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码错误", null);
		}
		// 1.校验参数
		ApiFrontUser user = userFacade.queryById(userId);
		if (StringUtil.isMobile(apiCompany.getMobile())) {
			// 校验手机验证码
			phonekey = CodeUtil.certificationprex + apiCompany.getMobile();

			 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					 phoneCode, true);
			 if(codeR==-1){
			 return webUtil.resMsg(0, "0005","手机验证码过期", null);
			 }else if(codeR==0){
			 return webUtil.resMsg(0, "0005","手机验证码错误", null);
			 }
		} else {
			// 手机格式错误
			msg = "0001";
			return webUtil.resMsg(0, msg, "手机格式错误", null);
		}
		int result=0;
		ApiCompany param = new ApiCompany();
		param.setUserId(userId);
		ApiCompany company = companyFacade.queryCompanyByParam(param);
		apiCompany.setUserId(userId);
		apiCompany.setContentImageId(ids);
		apiCompany.setState(201);
		apiCompany.setMobileState(203);
		if(company!=null){
			apiCompany.setId(company.getId());
			companyFacade.updateCompany(apiCompany);
			result=company.getId();
		}
		else{
			apiCompany.setIdentity("");
			apiCompany.setRegisterNo("");
			apiCompany.setLegalName("");
			apiCompany.setRegisterTime(new Date());
			apiCompany.setLastUpdateTime(new Date());
			result = companyFacade.save(apiCompany);
		}
		logger.info("result>>>>"+result);
		if(result>0&&result!=10005){
			return webUtil.resMsg(1, "0000", "1,"+userId+","+result, null);
		}
		else if(result>0&&result==10005){
			return webUtil.resMsg(10005, "0001", "1,"+userId+","+result, null);
		}
		return webUtil.resMsg(0, "0010", "添加失败！", null);
	}
	
	private String getImgCode(HttpServletRequest request) {
		return imgcodeprex + getUuid(request);
	}
	
	private String getUuid(HttpServletRequest request) {
		String uuid = CookieManager.retrieve(CookieManager.COOKIE_UUID_NAME,
				request, true);
		if (uuid == null) {
			return request.getSession().getId();
		} else {
			return uuid;
		}
	}

}
