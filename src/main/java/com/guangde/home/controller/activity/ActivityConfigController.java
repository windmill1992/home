package com.guangde.home.controller.activity;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IHomePageFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiActivityConfig;
import com.guangde.entry.ApiActivitySign;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCommonForm;
import com.guangde.entry.ApiCommonFormUser;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiProjectVolunteer;
import com.guangde.home.controller.help.HelpController;
import com.guangde.home.utils.AppletUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.PFeedBack;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;

@Controller
@RequestMapping("activity")
public class ActivityConfigController {
	Logger logger = LoggerFactory.getLogger(HelpController.class);
	
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private IHomePageFacade homePageFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	/**
	 * 跳转到活动详情页
	 * @param view
	 * @return
	 */
	@RequestMapping("activityDetail")
	public ModelAndView activityDetail(ModelAndView view,@RequestParam(value="id",required=true)Integer id,
			HttpServletRequest request,HttpServletResponse response){
		view.setViewName("h5/activity/activity_detail");
		//===========微信用户自动登陆start==============//		
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
		Integer userId = UserUtil.getUserId(request, response);
		if(userId != null && userId != 0)
		{
			user = userFacade.queryById(userId);
			view.addObject("user", user);
		}
		else
		{
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
						unionid = mapToken.get("unionid").toString();
						token = mapToken.get("access_token").toString();
						request.getSession().setAttribute("openId", openId);
						System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
						redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
					}
					user = CommonUtils.queryUser(request,openId,token,unionid);
					logger.info("user>>>"+user);
				} catch (Exception e) {
					logger.error("微信登录处理出现问题"+ e);
				}
				view.addObject("payway", browser);

				if (user.getCoverImageId() == null) 
				{
					user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
				}
				else 
				{
					if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
						ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
						user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
					}
				}

				try
				{
					// 自动登录
					SSOUtil.login(user, request, response);
					view.addObject("user", user);
				}
				catch(Exception e)
				{
					logger.error("",e);
				}

			}
			else{
				view = new ModelAndView("h5/ucenter/userLogin");
				view.addObject("flag", "activity_"+id);
				return view;
			}

		}
	   //===========微信用户自动登陆end==============//
		ApiActivityConfig model = homePageFacade.queryById(id);
		if(model!=null&&model.getProjectId()!=null){
			ApiProject project = projectFacade.queryProjectDetail(model.getProjectId());
			Integer donateCount = donateRecordFacade.countDistDonateUserNum(model.getProjectId());
			view.addObject("project", project);
			view.addObject("num", donateCount==null?0:donateCount);
		}
		view.addObject("activity", model);
		return view;
	}
	
	/**
	 * 初始加载或加载更多捐款详情
	 * @param projectId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@RequestMapping("loadDonateRecord")
	@ResponseBody
	public JSONObject loadDonateRecore(@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="pageSize",defaultValue="20")Integer pageSize,
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum ){
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
		try {
			ApiDonateRecord dRecord = new ApiDonateRecord();
			dRecord.setState(302);
			dRecord.setProjectId(projectId);
			ApiPage<ApiDonateRecord> page = donateRecordFacade.queryByParamBycount(dRecord, pageNum, pageSize);
			List<ApiDonateRecord> list=null;
			if(page.getTotal()>0){
		    	list=page.getResultData();
		    }
			if(list.size()==0){
				//无数据
				data.put("result", 1);
			}
			else{
				for(ApiDonateRecord donate:list){
					JSONObject item = new JSONObject();
					item.put("coverImageUrl", donate.getCoverImageurl());
					item.put("nickName", donate.getNickName());
					item.put("donateTime", donate.getDonatTimeStr());
					item.put("donatAmount", donate.getDonatAmount());
					items.add(item);
				}
				data.put("result",0);
				data.put("total",page.getTotal());
				data.put("items", items);
			}
		
	} catch (Exception e) {
		data.put("result", 2);
	}
	return data;
	}
	
	//TODO 闲置
	/**
	 * 团队/个人详情
	 * 团队：（标题，头像，简介，活动总数，志愿者总数，服务时长，签到人次）
	 * 个人：（标题，头像，简介，活动总数，签到次数，服务时长）
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getCompanyInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject company_info(HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getCompanyInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getCompanyInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		//判断用户是是团队还是个人
		try {
			boolean iscompany = companyFacade.isCompany(userId);
			if(iscompany){//团队
				result.put("type", 1);
				List<ApiCompany> list = companyFacade.queryCompanyDetails(userId);
				if(list != null && list.size() > 0){
					result.put("id", list.get(0).getId());
					result.put("title", list.get(0).getName());
					result.put("introduction", list.get(0).getIntroduction());
					result.put("headImg", list.get(0).getCoverImageUrl());
					result.put("totalActivityNum", list.size());
				}
				List<Integer> formIds = new ArrayList<Integer>();
				List<Integer> activityIds = new ArrayList<Integer>();
				for (ApiCompany apiCompany : list) {
					if(apiCompany.getRegisterNo() != null && !"".equals(apiCompany.getRegisterNo())){
						formIds.add(Integer.valueOf(apiCompany.getRegisterNo()));
					}
					if(apiCompany.getGroupCode() != null && !"".equals(apiCompany.getGroupCode())){
						activityIds.add(Integer.valueOf(apiCompany.getGroupCode()));
					}
				}
				ApiCompany apiCompany = new ApiCompany();
				if(activityIds != null && activityIds.size() > 0){
					apiCompany.setActivityIds(activityIds);
					apiCompany = companyFacade.countTimeAndSignNum(apiCompany);
				}
				result.put("serviceTimeLength", apiCompany.getTotalTimeNum());
				result.put("signNum", apiCompany.getCount());
				int volunteerNum = 0;
				if(formIds != null){
					apiCompany = new ApiCompany();
					apiCompany.setFormIds(formIds);
					volunteerNum = companyFacade.countVolunteer(apiCompany);
				}
				result.put("totalVolunteerNum", volunteerNum);
				item.put("result", result);
				item.put("code", 1);
				item.put("msg", "success");
				return item;
				
			}else{//个人
				result.put("type", 0);
				ApiCompany company = companyFacade.queryPersonDetail(userId);
				if(company != null){
					result.put("title", company.getName());
					result.put("introduction", company.getIntroduction());
					result.put("headImg", company.getCoverImageUrl());
				}
				ApiCompany apiCompany = companyFacade.countPersonal(userId);
				if(apiCompany != null){
					result.put("serviceTimeLength", apiCompany.getTotalTimeNum());
					result.put("signNum", apiCompany.getTotalVolunteerNum());
					result.put("totalActivityNum", apiCompany.getActivityNum());
				}
				item.put("result", result);
				item.put("code", 1);
				item.put("msg", "success");
				return item;
			}
		} catch (Exception e) {
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "error");
			return item;
		}
	}
	
	//TODO 我发起的活动
	/**
	 * 团队/个人详情
	 * 团队：（标题，头像，简介，活动总数，志愿者总数，服务时长，签到人次）
	 * 个人：（标题，头像，简介，活动总数，签到次数，服务时长）
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getLaunchActivityInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject launch_activity_info(HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getCompanyInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getCompanyInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		//判断用户是是团队还是个人
		try {
			List<ApiCompany> list = companyFacade.queryCompanyDetails(userId);
			if(list != null && list.size() > 0){
				result.put("id", list.get(0).getId());
				result.put("title", list.get(0).getName());
				result.put("introduction", list.get(0).getIntroduction());
				result.put("headImg", list.get(0).getCoverImageUrl());
				result.put("totalActivityNum", list.size());
			}
			List<Integer> formIds = new ArrayList<Integer>();
			List<Integer> activityIds = new ArrayList<Integer>();
			for (ApiCompany apiCompany : list) {
				if(apiCompany.getRegisterNo() != null && !"".equals(apiCompany.getRegisterNo())){
					formIds.add(Integer.valueOf(apiCompany.getRegisterNo()));
				}
				if(apiCompany.getGroupCode() != null && !"".equals(apiCompany.getGroupCode())){
					activityIds.add(Integer.valueOf(apiCompany.getGroupCode()));
				}
			}
			ApiCompany apiCompany = new ApiCompany();
			if(activityIds != null && activityIds.size() > 0){
				apiCompany.setActivityIds(activityIds);
				apiCompany = companyFacade.countTimeAndSignNum(apiCompany);
			}
			result.put("serviceTimeLength", apiCompany.getTotalTimeNum());
			result.put("signNum", apiCompany.getCount());
			int volunteerNum = 0;
			if(formIds != null && formIds.size() > 0){
				apiCompany = new ApiCompany();
				apiCompany.setFormIds(formIds);
				volunteerNum = companyFacade.countVolunteer(apiCompany);
			}
			result.put("totalVolunteerNum", volunteerNum);
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
			return item;
		} catch (Exception e) {
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "error");
			return item;
		}
	}
	
	//TODO 我报名的活动
	/**
	 * 团队/个人详情
	 * 团队：（标题，头像，简介，活动总数，志愿者总数，服务时长，签到人次）
	 * 个人：（标题，头像，简介，活动总数，签到次数，服务时长）
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getEntryActivityInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject entry_activity_info(HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getCompanyInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getCompanyInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		try {
			if(user == null || StringUtils.isBlank(user.getNickName())){
				user = userFacade.queryById(userId);
			}
			result.put("title", user.getNickName());
			result.put("introduction", "");
			result.put("headImg", user.getCoverImageUrl());
			/*ApiCompany company = companyFacade.queryPersonDetail(userId);
			if(company != null){
				result.put("title", company.getName());
				result.put("introduction", company.getIntroduction());
				result.put("headImg", company.getCoverImageUrl());
			}*/
			ApiCompany apiCompany = companyFacade.countPersonal(userId);
			if(apiCompany != null){
				result.put("serviceTimeLength", apiCompany.getTotalTimeNum());
				result.put("signNum", apiCompany.getTotalVolunteerNum());
				result.put("totalActivityNum", apiCompany.getActivityNum());
			}
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
			return item;
			
		} catch (Exception e) {
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "error");
			return item;
		}
	}
	
	/**
	 * 团队详情
	 * @param activityUserId
	 * @return
	 */
	@RequestMapping(value = "/getTeamDetail", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTeamDetail(@RequestParam("activityUserId") Integer activityUserId){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		List<ApiCompany> list = companyFacade.queryCompanyDetails(activityUserId);
		if(list != null && list.size() > 0){
			result.put("title", list.get(0).getName());
			result.put("introduction", list.get(0).getIntroduction());
			result.put("headImg", list.get(0).getCoverImageUrl());
			result.put("totalActivityNum", list.size());
		
			List<Integer> formIds = new ArrayList<Integer>();
			List<Integer> activityIds = new ArrayList<Integer>();
			for (ApiCompany apiCompany : list) {
				if(apiCompany.getRegisterNo() != null && !"".equals(apiCompany.getRegisterNo())){
					formIds.add(Integer.valueOf(apiCompany.getRegisterNo()));
				}
				if(apiCompany.getGroupCode() != null && !"".equals(apiCompany.getGroupCode())){
					activityIds.add(Integer.valueOf(apiCompany.getGroupCode()));
				}
			}
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setActivityIds(activityIds);
			apiCompany = companyFacade.countTimeAndSignNum(apiCompany);
			result.put("serviceTimeLength", apiCompany.getTotalTimeNum());
			result.put("signNum", apiCompany.getCount());
			int volunteerNum = 0;
			if(formIds != null){
				apiCompany = new ApiCompany();
				apiCompany.setFormIds(formIds);
				volunteerNum = companyFacade.countVolunteer(apiCompany);
			}
			result.put("totalVolunteerNum", volunteerNum);
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		}else{
			item.put("code", 3);
			item.put("msg", "活动不存在");
		}
		return item;
	}
	
	/**
	 * 团队活动列表
	 * @param activityUserId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/getTeamActivityList", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTeamActivityList(@RequestParam("activityUserId") Integer activityUserId, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			String activityAdminIds = null;
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(activityUserId);
			apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			if(apiCompany != null){
				activityAdminIds = apiCompany.getActivityAdminIds();
			}else{
				apiCompany = new ApiCompany();
				apiCompany.setActivityAdminIds(activityUserId + "");
				apiCompany = companyFacade.queryCompanyByParam(apiCompany);
				activityUserId = apiCompany.getUserId();
				activityAdminIds = apiCompany.getActivityAdminIds();
			}
			Date nowTime = new Date();
			ApiPage<ApiActivityConfig> page = homePageFacade.queryActivityInfo(activityUserId, activityAdminIds, 2, pageNum, pageSize);		
			if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum * pageSize >= 0 
					|| pageNum * pageSize - page.getTotal() < pageSize)){
				for (ApiActivityConfig ac: page.getResultData()) {
					JSONObject acJson = new JSONObject();
					acJson.put("name", ac.getName());
					//1.报名中   0.已截止
					if(ac.getLimit() <= ac.getNumber()){
						acJson.put("state", 0);
					}else if(ac.getEndTime() != null &&  DateUtil.hoursBetween(ac.getEndTime(), nowTime) < 0){
						acJson.put("state", 0);
					}else{
						acJson.put("state", 1);
					}
					acJson.put("activityId", ac.getId());
					acJson.put("number", ac.getNumber());
					acJson.put("contentImgs", ac.getContentImageUrls());
					acJson.put("address", ac.getAddress());
					acJson.put("createTime", ac.getCreatetime() == null ? "" : DateUtil.dateStringChinese("yyyy年MM月dd日", ac.getCreatetime()));
					jsonArray.add(acJson);
				}
			}
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		
		return item;
	}
	
	/**
	 * 根据团队和活动状态查询活动列表     团队（活动标题，状态，报名人数，活动相关图片，创建时间，地点）
	 * 个人
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getActivityInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject activity_info(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "state", defaultValue = "-1") Integer state){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getActivityInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getActivityInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		//判断是个人还是团队
		Date nowTime = new Date();
		try {
			String activityAdminIds = null;
			boolean iscompany = companyFacade.isCompany(userId);
			if(iscompany){//团队
				ApiCompany apiCompany = new ApiCompany();
				apiCompany.setUserId(userId);
				apiCompany = companyFacade.queryCompanyByParam(apiCompany);
				if(apiCompany != null){
					activityAdminIds = apiCompany.getActivityAdminIds();
				}else{
					apiCompany = new ApiCompany();
					apiCompany.setActivityAdminIds(userId + "");
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					userId = apiCompany.getUserId();
					activityAdminIds = apiCompany.getActivityAdminIds();
				}
				
				ApiPage<ApiActivityConfig> page = homePageFacade.queryActivityInfo(userId, activityAdminIds, state, pageNum, pageSize);		
				if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum * pageSize >= 0 
						|| pageNum * pageSize - page.getTotal() < pageSize)){
					for (ApiActivityConfig ac: page.getResultData()) {
						JSONObject acJson = new JSONObject();
						acJson.put("name", ac.getName());
						//1.报名中   0.已截止
						if(ac.getLimit() >= ac.getNumber()){
							acJson.put("state", 0);
						}else if(ac.getEndTime() != null && ac.getEndTime().getTime() < nowTime.getTime()){
							acJson.put("state", 0);
						}else{
							acJson.put("state", 1);
						}
						acJson.put("activityId", ac.getId());
						acJson.put("number", ac.getNumber());
						acJson.put("contentImgs", ac.getContentImageUrls());
						acJson.put("address", ac.getAddress());
						acJson.put("createTime", ac.getCreatetime() == null ? "" : DateUtil.dateStringChinese("yyyy年MM月dd日", ac.getCreatetime()));
						jsonArray.add(acJson);
					}
				}
				result.put("type", 1);
				result.put("data", jsonArray);
				result.put("count", page.getTotal());
			}else{//个人
				ApiPage<ApiActivityConfig> page = homePageFacade.queryActivityPersonalInfo(userId, pageNum, pageSize);		
				if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum * pageSize >= 0 
						|| pageNum * pageSize - page.getTotal() < pageSize)){
					for (ApiActivityConfig ac: page.getResultData()) {
						JSONObject acJson = new JSONObject();
						acJson.put("activityId", ac.getId());
						acJson.put("name", ac.getName());
						acJson.put("signNum", ac.getSignNum());
						acJson.put("timeNum", ac.getTimeNum());
						//acJson.put("number", ac.getNumber());
						//acJson.put("contentImgs", ac.getContentImageUrls());
						acJson.put("address", ac.getAddress());
						acJson.put("createTime", ac.getCreatetime() == null ? "" : DateUtil.dateStringChinese("yyyy年MM月dd日", ac.getCreatetime()));
						jsonArray.add(acJson);
					}
				}
				result.put("type", 0);
				result.put("data", jsonArray);
				result.put("count", page.getTotal());
			}
			
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	
	//TODO 我发起的活动
	/**
	 * 根据团队和活动状态查询活动列表     团队（活动标题，状态，报名人数，活动相关图片，创建时间，地点）
	 * 个人
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getMyActivityInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject my_activity_info(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "state", defaultValue = "-1") Integer state){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getActivityInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getActivityInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    
		Date nowTime = new Date();
		try {
			String activityAdminIds = null;
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(userId);
			apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			if(apiCompany != null){
				activityAdminIds = apiCompany.getActivityAdminIds();
			}else{
				apiCompany = new ApiCompany();
				apiCompany.setActivityAdminIds(userId + "");
				apiCompany = companyFacade.queryCompanyByParam(apiCompany);
				userId = apiCompany.getUserId();
				activityAdminIds = apiCompany.getActivityAdminIds();
			}
			
			ApiPage<ApiActivityConfig> page = homePageFacade.queryActivityInfo(userId, activityAdminIds, state, pageNum, pageSize);		
			if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum * pageSize >= 0 
					|| pageNum * pageSize - page.getTotal() < pageSize)){
				for (ApiActivityConfig ac: page.getResultData()) {
					JSONObject acJson = new JSONObject();
					acJson.put("name", ac.getName());
					//1.报名中   0.已截止
					if(ac.getLimit() >= ac.getNumber()){
						acJson.put("state", 0);
					}else if(ac.getEndTime() != null && ac.getEndTime().getTime() < nowTime.getTime()){
						acJson.put("state", 0);
					}else{
						acJson.put("state", 1);
					}
					acJson.put("activityId", ac.getId());
					acJson.put("number", ac.getNumber());
					acJson.put("contentImgs", ac.getContentImageUrls());
					acJson.put("address", ac.getAddress());
					acJson.put("createTime", ac.getCreatetime() == null ? "" : DateUtil.dateStringChinese("yyyy年MM月dd日", ac.getCreatetime()));
					jsonArray.add(acJson);
				}
			}
			result.put("type", 1);
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	
	//TODO 我报名的活动
	/**
	 * 根据团队和活动状态查询活动列表     团队（活动标题，状态，报名人数，活动相关图片，创建时间，地点）
	 * 个人
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getMyEntryActivityInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject my_entry_activity_info(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "state", defaultValue = "-1") Integer state){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getActivityInfo"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getActivityInfo",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    
		try {
			ApiPage<ApiActivityConfig> page = homePageFacade.queryActivityPersonalInfo(userId, pageNum, pageSize);		
			if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum * pageSize >= 0 
					|| pageNum * pageSize - page.getTotal() < pageSize)){
				for (ApiActivityConfig ac: page.getResultData()) {
					JSONObject acJson = new JSONObject();
					acJson.put("activityId", ac.getId());
					acJson.put("name", ac.getName());
					acJson.put("signNum", ac.getSignNum());
					acJson.put("timeNum", ac.getTimeNum());
					acJson.put("address", ac.getAddress());
					acJson.put("createTime", ac.getCreatetime() == null ? "" : DateUtil.dateStringChinese("yyyy年MM月dd日", ac.getCreatetime()));
					jsonArray.add(acJson);
				}
			}
			result.put("type", 0);
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}

	/**
	 * 团队查看签到记录，根据活动，按照时间分组查询（活动logo，真实姓名，头像，签到时间，签出时间）
	 * @param pageNum
	 * @param pageSize
	 * @param activityId
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/getCompanySign", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject company_signs(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam("activityId") Integer activityId,
			@RequestParam("time") String time,
			Integer userIdWx){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null){
			userId = userIdWx;
		}
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getCompanySign"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getCompanySign",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }/*else if(!apiActivityConfig.getUserId().equals(userId)){
	    	item.put("code", 3);
	    	item.put("msg", "没有权限");
	    	return item;
	    }*/
		try {
			ApiActivityConfig activityConfig = homePageFacade.queryById(activityId);
			if(activityConfig != null){
				result.put("logoUrl", activityConfig.getTwoCodePicUrl());
			}
			ApiPage<ApiActivitySign> page = homePageFacade.queryActivitysByActIdAndTime(activityId, time, pageNum, pageSize);		
			if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum*pageSize >= 0 
					|| pageNum * pageSize-page.getTotal() < pageSize)){
				for (ApiActivitySign acs: page.getResultData()) {
					JSONObject acsJson = new JSONObject();
					acsJson.put("signTime", acs.getSignTime() == null ? "" : DateUtil.parseToDefaultTimeString(acs.getSignTime()));
					acsJson.put("signOutTime", acs.getSignOutTime() == null ? "" : DateUtil.parseToDefaultTimeString(acs.getSignOutTime()));
					acsJson.put("realName", acs.getName());
					acsJson.put("headImg", acs.getCoverImageUrl());
					acsJson.put("timeNum", acs.getTimeNum());
					jsonArray.add(acsJson);
				}
			}
			
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	

	/**
	 * 个人查询签到记录（签到时间，签出时间）
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/getPersonalSign", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject personal_signs(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam("activityId") Integer activityId){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getPersonalSign"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getPersonalSign",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		try {
			ApiPage<ApiActivitySign> page = homePageFacade.queryPersonalSign(userId, activityId, pageNum, pageSize);		
			if(page != null && page.getTotal() > 0 && (page.getTotal() - pageNum*pageSize >= 0 
					|| pageNum*pageSize-page.getTotal()<pageSize)){
				for (ApiActivitySign acs: page.getResultData()) {
					JSONObject acsJson = new JSONObject();
					//格式化时间 2017年11月11日（周五）
					String week="";
					String time="";
					if(acs.getSignTime() != null){
						time = DateUtil.dateStringChinese("yyyy年MM月dd日", acs.getSignTime());
						week = DateUtil.getWeekStr(acs.getSignTime());
						acsJson.put("time", time + "（" + week + "）");
					}
					acsJson.put("signTime", acs.getSignTime() == null ? "" : DateUtil.parseToDefaultTimeString(acs.getSignTime()));
					acsJson.put("signOutTime", acs.getSignOutTime() == null ? "" : DateUtil.parseToDefaultTimeString(acs.getSignOutTime()));
					acsJson.put("timeNum", acs.getTimeNum());
					jsonArray.add(acsJson);
				}
			}
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}

	/**
	 * 查询活动（标题、内容、封面图、活动相关图，服务费，自定义项）
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/getActivityDetil", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject activity_detail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("activityId") Integer activityId){
		JSONObject item = new JSONObject();
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<String> data1 = new ArrayList<String>();
		JSONArray array = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getActivityDetil"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getActivityDetil",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }/*else if(!apiActivityConfig.getUserId().equals(userId)){
	    	item.put("code", 3);
	    	item.put("msg", "没有权限");
	    	return item;
	    }*/
	    
	    ApiCompany company = new ApiCompany();
	    company.setUserId(userId);
	    company = companyFacade.queryCompanyByParam(company);
	    if(company != null){
	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
	    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
	    		item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	    	}
	    }else{
	    	company = new ApiCompany();
	 	    company.setActivityAdminIds(userId + "");
	 	    company = companyFacade.queryCompanyByParam(company);
	 	    if(company != null){
	 	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
		    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
		    		item.put("code", 3);
			    	item.put("msg", "没有权限");
			    	return item;
		    	}
	 	    	
	 	    }else{
	 	    	item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	 	    }
	    }
	    
		try {
			ApiActivityConfig config = homePageFacade.queryById(activityId);
			if(config != null){
				json.put("title", config.getName());
				json.put("content", config.getContent());
				if(config.getLogoId() != null){
					data1.add(config.getLogoId().toString());
					data1.add(config.getLogoUrl());
				}
				
				String[] urlIds = config.getContentImageId().split(",");
				if(urlIds.length == config.getContentImageUrls().size()){
					for(int i=0;i<urlIds.length;i++){
						List<String> data3 = new ArrayList<String>();
						data3.add(urlIds[i]);
						data3.add(config.getContentImageUrls().get(i));
						array.add(data3);
					}
				}
				
				json.put("logoUrl", data1);
				json.put("contentImgs", array);
				json.put("price", config.getUnitPrice());
				json.put("customItem", config.getCustomItem());
			}
			
			result.put("data", json);
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	
	/**
	 * 报名明细统计(0:未签到；1：已签到；2：全部)
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/getCountEntry", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject count_entry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("activityId") Integer activityId){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject json = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addTeamActivity"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getCountEntry",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }/*else if(!apiActivityConfig.getUserId().equals(userId) ){
	    	item.put("code", 3);
	    	item.put("msg", "没有权限");
	    	return item;
	    }*/
	    ApiCompany company = new ApiCompany();
	    company.setUserId(userId);
	    company = companyFacade.queryCompanyByParam(company);
	    if(company != null){
	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
	    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
	    		item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	    	}
	    }else{
	    	company = new ApiCompany();
	 	    company.setActivityAdminIds(userId + "");
	 	    company = companyFacade.queryCompanyByParam(company);
	 	    if(company != null){
	 	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
		    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
		    		item.put("code", 3);
			    	item.put("msg", "没有权限");
			    	return item;
		    	}
	 	    	
	 	    }else{
	 	    	item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	 	    }
	    }
		try {
			int noSignCount = projectVolunteerFacade.countEntry(activityId, 0);
			int signedCount = projectVolunteerFacade.countEntry(activityId, 1);
			json.put("noSignCount", noSignCount);
			json.put("signedCount", signedCount);
			json.put("totalSignCount", noSignCount + signedCount);
			result.put("data", json);
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	
	/**
	 * 报名明细（姓名，性别，地址，职业，签到次数，累计时长）
	 * @param pageNum
	 * @param pageSize
	 * @param activityId
	 * @param state(0:未签到；1：已签到；2：全部)
	 * @return
	 */
	@RequestMapping(value = "/getEntryInfos", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject entry_infos(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam("activityId") Integer activityId,
			@RequestParam(value="state", defaultValue="2") Integer state){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> getEntryInfos"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> getEntryInfos",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }/*else if(!apiActivityConfig.getUserId().equals(userId)){
	    	item.put("code", 3);
	    	item.put("msg", "没有权限");
	    	return item;
	    }*/
	    ApiCompany company = new ApiCompany();
	    company.setUserId(userId);
	    company = companyFacade.queryCompanyByParam(company);
	    if(company != null){
	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
	    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
	    		item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	    	}
	    }else{
	    	company = new ApiCompany();
	 	    company.setActivityAdminIds(userId + "");
	 	    company = companyFacade.queryCompanyByParam(company);
	 	    if(company != null){
	 	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
		    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
		    		item.put("code", 3);
			    	item.put("msg", "没有权限");
			    	return item;
		    	}
	 	    	
	 	    }else{
	 	    	item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	 	    }
	    }
		try {
			ApiPage<ApiProjectVolunteer> page = projectVolunteerFacade.queryEntryInfo(activityId, state, pageNum, pageSize);		
			if(page != null && page.getTotal() >0 && (page.getTotal() - pageNum * pageSize >= 0 
					|| pageNum * pageSize - page.getTotal() < pageSize)){
				for (ApiProjectVolunteer volunteer: page.getResultData()) {
					JSONObject json = new JSONObject();
					json.put("name", volunteer.getName());
					json.put("sex", volunteer.getSex());
					json.put("address", volunteer.getAddress());
					json.put("position", volunteer.getPosition());
					json.put("countSign", volunteer.getCountSign());
					json.put("totalTimeNum", volunteer.getTotalTimeNum());
					json.put("formId", volunteer.getProjectId());
					jsonArray.add(json);
				}
			}
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}

	/**
	 * 签到,针对小程序
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject sign(@RequestParam("userId") Integer userId,
			@RequestParam("activityId") Integer activityId){
		JSONObject item = new JSONObject();
		ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }
	    
	    Date nowTimeDate = new Date();
	    ApiCommonForm model = new ApiCommonForm();
	    model.setId(apiActivityConfig.getFormId());
	    model = projectVolunteerFacade.selectByParam(model);
	    if(model != null){
	    	//判断活动是否截止：截止时间
	    	if(nowTimeDate.getTime() >= model.getEndTime().getTime()){
	    		item.put("code", 3);
		    	item.put("msg", "活动已结束");
		    	return item;
	    	}else{
	    		//判断是否报名
	    	    ApiCommonFormUser param = new ApiCommonFormUser();
	    	    param.setFormId(model.getId());
	    	    param.setUserId(userId);
	    	    ApiCommonFormUser apiCommonFormUser = projectVolunteerFacade
	    	    		.queryCommonFormUserDetailByParam(param);
	    	    if(apiCommonFormUser == null){
	    	    	item.put("code", 3);
			    	item.put("msg", "你未报名此活动");
			    	return item;
	    	    }
	    	}
	    }else{
	    	item.put("code", 3);
	    	item.put("msg", "活动已结束");
	    	return item;
	    }
	    
	    //判断用户今天有无签到数据
	    boolean res = homePageFacade.isSignToday(userId, activityId);
	    if(res){
	    	item.put("code", 3);
	    	item.put("msg", "已经签到过了");
	    	return item;
	    }
	    //判断是否报名过这个活动
	    ApiActivitySign param = new ApiActivitySign();
	    param.setUserId(userId);
	    param.setActivityId(activityId);
	    param.setCreateTime(new Date());
	    param.setSignTime(param.getCreateTime());
	    ApiResult apiResult = homePageFacade.saveSign(param);
	    if(apiResult.getCode() == 1){
	    	item.put("code", 1);
	    	item.put("msg", "success");
	    }else{
	    	item.put("code", -1);
	    	item.put("msg", apiResult.getMessage());
	    }
		return item;
	}
	
	/**
	 * 判断用户是否有团队
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/isTeamActivity",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject isTeamActivity(HttpServletRequest request,HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> isTeamActivity"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> isTeamActivity",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		
		ApiCompany aCompany=new ApiCompany();
		aCompany.setUserId(userId);
		ApiCompany aCompany2=companyFacade.queryCompanyByParam(aCompany);
		if(aCompany2==null){
			item.put("code", 3);
    		item.put("msg", "userId have not team");
    		return item;
		}else{
			item.put("code", 1);
    		item.put("msg", "success");
		}
		return item;
	}
	
	/**
	 * 添加团队信息
	 * @param apiCompany
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/addTeamActivity",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addTeamActivity(ApiCompany apiCompany,HttpServletRequest request,HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addTeamActivity"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addTeamActivity",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    if(apiCompany.getId() != null && apiCompany.getCoverImageId() != null){
	    	ApiResult result2 = companyFacade.updateCompany(apiCompany);
			if(result2.getCode() == 1){
				item.put("code", 4);
	    		item.put("msg", "头像上传成功");
	    		return item;
			}else{
				item.put("code", -1);
	    		item.put("msg", "error");
	    		return item;
			}
	    }else{
	    	ApiCompany aCompany=new ApiCompany();
			aCompany.setUserId(userId);
			aCompany=companyFacade.queryCompanyByParam(aCompany);
			int res = 0;
			if(aCompany ==null){
				apiCompany.setUserId(userId);
				res =companyFacade.save(apiCompany);
				if(res > 0){
					item.put("code", 1);
		    		item.put("msg", "add team success");
				}else{
					item.put("code", -1);
		    		item.put("msg", "error");
				}
			}else{
				item.put("code", 3);
	    		item.put("msg", "team have exist");
	    		return item;
			}
			
	    }
		
		return item;
	}
	
	/**
	 * 添加/编辑活动
	 * @param activityConfig
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/addActivity",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addActivity(ApiActivityConfig activityConfig,HttpServletRequest request,HttpServletResponse response){
		JSONObject item=new JSONObject();
		ApiResult result1=new ApiResult();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addActivity"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addActivity",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    //添加活动
	    JSONObject jsonObject = new JSONObject();
	    ApiActivityConfig apiActivityConfig= companyFacade.queryByActivityConfig(activityConfig.getId());
		if(apiActivityConfig ==null){
			activityConfig.setUserId(userId);
			activityConfig.setCreatetime(new Date());
			
			//需要志愿者
			activityConfig.setType(1);
			//默认项目
			activityConfig.setProjectId(521);
			
			result1=companyFacade.saveActivityConfig(activityConfig);
			if(result1.getCode()==1){
				try {
					//TODO 
					String path = "/mnt/usr/local/www.17xs.org/upload/picture/activity/";
					//String path = "/usr/local/tomcat-7.0.55/webapps/upload/picture/activity/";
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		            Calendar calendar = Calendar.getInstance();
		            String imgName = df.format(calendar.getTime()) + ".jpg";// 以当前时间为文件名
					//生成二维码
					AppletUtil.getminiqrQr(Integer.valueOf(result1.getMessage()), path, imgName);
					
					ApiBFile apiBFile = new ApiBFile();
					apiBFile.setCategory("activity");
					apiBFile.setFileType("picture");
					apiBFile.setIsHide(0);
					apiBFile.setUrl("activity/" + imgName);
					ApiResult res = fileFacade.saveBfile(apiBFile);
					if(res.getCode() == 1){
						ApiActivityConfig config = new ApiActivityConfig();
						config.setId(Integer.valueOf(result1.getMessage()));
						config.setTwoCodePicId(Integer.valueOf(res.getMessage()));
						companyFacade.updateActivityConfig(config);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				jsonObject.put("activityId", result1.getMessage());
				item.put("result", jsonObject);
				item.put("code", 1);
	    		item.put("msg", "add activity success");
			}else{
				item.put("code", -1);
	    		item.put("msg", result1.getMessage());
			}
		}else if(apiActivityConfig !=null && apiActivityConfig.getUserId().equals(userId)){//编辑活动
			result1=companyFacade.updateActivityConfig(activityConfig);
			if(result1.getCode()==1){
				jsonObject.put("activityId", activityConfig.getId());
				item.put("result", jsonObject);
				item.put("code", 1);
	    		item.put("msg", "edit activity success");
			}else{
				item.put("code", -1);
	    		item.put("msg", result1.getMessage());
			}
		}else{
			item.put("code", 3);
    		item.put("msg", "not your userId");
    		return item;
		}
	    
		
		return item;
	}
	
	/**
	 * 添加志愿者
	 * @param apiProjectVolunteer
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="addProjectVolunteer",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addProjectVolunteer(ApiProjectVolunteer apiProjectVolunteer,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="name", required = false) String name){
		JSONObject item=new JSONObject();
		ApiResult result1=new ApiResult();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addProjectVolunteer"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addProjectVolunteer",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
		
	    ApiProjectVolunteer v = new ApiProjectVolunteer();
	    v.setUserId(userId);
	    ApiPage<ApiProjectVolunteer> page = projectVolunteerFacade.queryVolunteerList(v, 1, 1);
	    if(page !=null && page.getTotal() > 0){
	    	item.put("code", 3);
    		item.put("msg", "your are already a volunteer");
    		return item;
	    }else{
	    	apiProjectVolunteer.setUserId(userId);
	    	result1=projectVolunteerFacade.save(apiProjectVolunteer);
	    	//更新用户信息
	    	try {
		    	ApiFrontUser apiFrontUser = new ApiFrontUser();
		    	apiFrontUser.setId(userId);
		    	apiFrontUser.setRealName(apiProjectVolunteer.getName());
		    	apiFrontUser.setMobileNum(apiProjectVolunteer.getMobile());
		    	apiFrontUser.setIdCard(apiProjectVolunteer.getIndentity());
		    	apiFrontUser.setPersition(apiProjectVolunteer.getPosition());
		    	userFacade.updateUser(apiFrontUser);
	    	} catch (Exception e) {
	    		e.printStackTrace();
			}
			if(result1.getCode()==1){
				item.put("code", 1);
	    		item.put("msg", "add projectVolunteer success");
			}else{
				item.put("code", -1);
	    		item.put("msg", result1.getMessage());
			}
	    }
		return item;
	}
	
	/**
	 * 添加表单
	 * @param apiCommonForm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/addForm",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addForm(@RequestParam(value="activityId",required=true)Integer activityId, 
			//String time,
			ApiCommonForm apiCommonForm,HttpServletRequest request,HttpServletResponse response){
		JSONObject item=new JSONObject();
		ApiResult result1=new ApiResult();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addForm"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addForm",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
    	
    	ApiActivityConfig activityConfig=new ApiActivityConfig();
    	activityConfig=companyFacade.queryByActivityConfig(activityId);
    	if(activityConfig ==null && "".equals(activityConfig)){
    		item.put("code", 3);
    		item.put("msg", "activity not exist");
    	}else{
    		if(apiCommonForm.getId() != null){
    			ApiResult flag = projectFacade.updateCommonForm(apiCommonForm);
    			if(flag.getCode() == 1){
    				item.put("code", 1);
    	    		item.put("msg", "update form success");
    			}else{
    				item.put("code", -1);
    	    		item.put("msg", flag.getMessage());
    			}
    		}else{
    			apiCommonForm.setCreateTime(new Date());
        		apiCommonForm.setNumber(0);
        		apiCommonForm.setDefaultOption(1);
        		
        		apiCommonForm.setTitle(activityConfig.getName());
        		result1=projectFacade.saveCommonForm(apiCommonForm);
        		ApiActivityConfig config = new ApiActivityConfig();
        		config.setFormId(Integer.valueOf(result1.getMessage()));
        		config.setId(activityConfig.getId());
        		//TODO 测试环境默认项目1507    线上暂时用521接收报名费用
        		config.setProjectId(521);
    			ApiResult flag=companyFacade.updateActivityConfig(config);
    			if(result1.getCode()==1 && flag.getCode()==1){
    				item.put("code", 1);
    	    		item.put("msg", "add form success");
    			}else{
    				item.put("code", -1);
    	    		item.put("msg", result1.getMessage());
    			}
    		}
    	}
		return item;
	}
	
	/**
	 * 查询活动的表单信息
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "getFormInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getFormInfo(@RequestParam("activityId") Integer activityId){
		JSONObject item=new JSONObject();
		JSONObject result=new JSONObject();
		ApiActivityConfig config = homePageFacade.queryById(activityId);
		if(config != null){
			if(config.getFormId() != null){
				ApiCommonForm param = new ApiCommonForm();
				param.setId(config.getFormId());
				ApiCommonForm commonForm = projectVolunteerFacade.selectByParam(param);
				if(commonForm != null){
					result.put("id", commonForm.getId());
					result.put("form", commonForm.getForm());
					result.put("defaultOption", commonForm.getDefaultOption());
					result.put("limit", commonForm.getLimit());
					result.put("endTime", commonForm.getEndTime()==null?null:
						DateUtil.dateStringChinese("yyyy年MM月dd日", commonForm.getEndTime()));
					item.put("result", result);
					item.put("code", 1);
		    		item.put("msg", "success");
				}else {
					item.put("code", 3);
		    		item.put("msg", "活动不存在");
				}
			}else{
				item.put("code", 4);
	    		item.put("msg", "添加表单");
			}
		}else{//活动不存在
			item.put("code", 3);
    		item.put("msg", "活动不存在");
		}
		return item;
	}
	
	
	
	/**
	 * 添加项目活动反馈表
	 * @param activityId
	 * @param apiProjectFeedback
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/addProjectActivityFeedBack",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addProjectActivityFeedBack(@RequestParam(value="activityId",required=true)Integer activityId,ApiProjectFeedback apiProjectFeedback,HttpServletRequest request,HttpServletResponse response){
		JSONObject item=new JSONObject();
		ApiResult res=new ApiResult();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addProjectActivityFeedBack"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addProjectActivityFeedBack",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig=companyFacade.queryByActivityConfig(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
    		item.put("msg", "activity have not exist");
	    }else{
	    	apiProjectFeedback.setFeedbackPeople(userId);
	    	apiProjectFeedback.setSource("home");
	    	apiProjectFeedback.setFeedbackTime(new Date());
	    	res=projectFacade.saveProjectFeedback(apiProjectFeedback);
	    	if(res.getCode()==1){
				item.put("code", 1);
	    		item.put("msg", "add projectFeedBack success");
			}else{
				item.put("code", -1);
	    		item.put("msg", res.getMessage());
			}
	    }
	    
	    return item;
	}
	
	/**
	 * 用户详情
	 * @param formId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="activityUserDetail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject activityUserDetail(@RequestParam(value="formId",required=true)Integer formId,HttpServletRequest request,HttpServletResponse response){
		JSONObject item=new JSONObject();
		JSONObject result=new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addProjectActivityFeedBack"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addProjectActivityFeedBack",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiProjectVolunteer apiProjectVolunteer=projectVolunteerFacade.queryByVolunteerIdAndFormId(userId, formId);
	    if(apiProjectVolunteer !=null){
	    	result.put("name", apiProjectVolunteer.getName());
		    result.put("sex", apiProjectVolunteer.getSex());
		    result.put("mobile", apiProjectVolunteer.getMobile());
		    result.put("address", apiProjectVolunteer.getAddress());
		    result.put("position", apiProjectVolunteer.getPosition());
		    result.put("field", apiProjectVolunteer.getField());
		    result.put("historyService", apiProjectVolunteer.getHistoryService());
		    result.put("groupName", apiProjectVolunteer.getGroupName());
		    result.put("information", apiProjectVolunteer.getInformation());
	    }else{
	    	item.put("result", result);
			item.put("code", 3);
			item.put("msg", "暂无数据");
			return item;
	    }
	    item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
	    return item;
	}
	
	/**
	 * 单人签出
	 * @param activityId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "singleSignOutTime", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject singleSignOutTime(@RequestParam(value = "activityId",required=true) Integer activityId, 
			HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiResult res = new ApiResult();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> singleSignOutTime"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> singleSignOutTime",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    
	    ApiActivitySign activitySign = homePageFacade.queryByActivityIdAndUserId(activityId, userId);
	    if(activitySign != null){
	    	Date nowTime = new Date();
	    	activitySign.setSignOutTime(nowTime);
	    	Double hour = DateUtil.calDoubleHours2(nowTime, activitySign.getSignTime());
	    	activitySign.setTimeNum(new BigDecimal(hour));
	    	res = homePageFacade.updateApiActivitySign(activitySign);
	    	if(res.getCode() == 1){
				item.put("code", 1);
	    		item.put("msg", "update activitySign success");
			}else{
				item.put("code", -1);
	    		item.put("msg", res.getMessage());
			}
	    }else{
	    	item.put("code", 3);
    		item.put("msg", "No sign in");
	    }
	    
	    return item;
	}
	
	/**
	 * 批量签出
	 * @param activityId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "batchSignOutTime", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject batchSignOutTime(@RequestParam(value = "activityId", required = true) Integer activityId, HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiResult res = new ApiResult();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> singleSignOutTime"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> singleSignOutTime",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
	    if(apiActivityConfig == null){
	    	item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
	    }/*else if(!apiActivityConfig.getUserId().equals(userId)){
	    	item.put("code", 3);
	    	item.put("msg", "没有权限");
	    	return item;
	    }*/
	    ApiCompany company = new ApiCompany();
	    company.setUserId(userId);
	    company = companyFacade.queryCompanyByParam(company);
	    if(company != null){
	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
	    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
	    		item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	    	}
	    }else{
	    	company = new ApiCompany();
	 	    company.setActivityAdminIds(userId + "");
	 	    company = companyFacade.queryCompanyByParam(company);
	 	    if(company != null){
	 	    	if(!company.getUserId().equals(apiActivityConfig.getUserId()) 
		    			&& !company.getActivityAdminIds().contains(apiActivityConfig.getUserId()+"")){
		    		item.put("code", 3);
			    	item.put("msg", "没有权限");
			    	return item;
		    	}
	 	    	
	 	    }else{
	 	    	item.put("code", 3);
		    	item.put("msg", "没有权限");
		    	return item;
	 	    }
	    }
	    int count=homePageFacade.countByActivityId(activityId);
	    if(count > 0){
	    	res = homePageFacade.updateApiActivitySigns(activityId);
		    if(res.getCode() == 1){
				item.put("code", 1);
	    		item.put("msg", "update activitySign success");
			}else{
				item.put("code", -1);
	    		item.put("msg", res.getMessage());
			}
	    }else{
	    	item.put("code", 3);
    		item.put("msg", "已经签出过了");
	    }
	    
	    return item;
	}
	
	/**
	 * 判断用户类型 type  1:管理员、2:成员、3:普通用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserType", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getUserType(HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> singleSignOutTime"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> singleSignOutTime",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    
	    //1:管理员、2:成员、3:普通用户
	    try {
	    	item.put("code", 1);
	    	item.put("msg", "success");
		    boolean iscompany = companyFacade.isCompany(userId);
			if(iscompany){
				result.put("type", 1);
			}else{
				boolean iscompanyMember = companyFacade.isOrNotCompanyMember(userId);
				if(iscompanyMember){
					result.put("type", 2);
				}else{
					result.put("type", 3);
				}
			}
			item.put("result", result);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	item.put("code", -1);
	    	item.put("msg", "error");
		}
	    return item;
	}
	
	/**
	 * 所属团队名称列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/groupNameList", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject groupNameList(HttpServletRequest request, HttpServletResponse response){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		List<ApiCompany> list=companyFacade.queryByCompanyGroupName();
		List<String> names = new ArrayList<String>();
		if(list != null && list.size()>0){
			for (ApiCompany apiCompany : list) {
				names.add(apiCompany.getName());
			}
			result.put("groupName", names);
			item.put("code", 1);
			item.put("msg", "success");
			item.put("result", result);
		}else{
			item.put("code", 3);
			item.put("msg", "date is null");
			item.put("result", result);
		}
		return item;
	}

	/**
	 * 判断用户今日是否已经签到
	 * @param userId
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/getUserIsOrNotTodaySign")
	@ResponseBody
	public JSONObject getUserIsOrNotTodaySign(@RequestParam("userId") Integer userId,
			@RequestParam("activityId") Integer activityId){
		JSONObject item = new JSONObject();
		 //判断用户今天有无签到数据
	    boolean res = homePageFacade.isSignToday(userId, activityId);
	    if(res){
	    	item.put("code", 3);
	    	item.put("msg", "已经签到过了");
	    }else{
	    	item.put("code", 1);
	    	item.put("msg", "可以签到");
	    }
		return item;
	}
	
	/**
	 * 判断用户是否已经成为此活动的志愿者
	 * @param request
	 * @param response
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/isOrNotActivityVolunteer", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject isOrNotActivityVolunteer(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("activityId") Integer activityId,
			Integer userIdWx){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null){
			userId = userIdWx;
		}
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
	    		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	    			if ("".equals(openId) || openId == null || OToken == null) {
	    				if ("".equals(weixin_code) || weixin_code == null
	    						|| weixin_code.equals("authdeny")) {
	    					item.put("code", 0);
	    					item.put("msg", "获取code");
	    					return item;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("activity >> addProjectActivityFeedBack"+ e);
	    			item.put("code", -1);
					item.put("msg", "获取openId unionId Token error");
					return item;
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
	    			logger.error("activity >> addProjectActivityFeedBack",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do");
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
		//===========微信用户自动登陆end==============//
	    if(userId == null){
	    	userId = user.getId();
	    }
	    //判断用户是否是此活动的团长
	    ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
		if(apiActivityConfig == null){
			item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
		}
		ApiCompany vo = new ApiCompany();
		vo.setUserId(userId);
		vo.setType(2);
		vo =  companyFacade.queryCompanyByParam(vo);
	    if(apiActivityConfig.getUserId().equals(userId) && vo != null){
	    	//团长
			item.put("code", 6);
			item.put("msg", "团长");
	    }else{
	    	//是否是志愿者
	    	ApiProjectVolunteer apiProjectVolunteer = new ApiProjectVolunteer();
			apiProjectVolunteer.setUserId(userId);
			ApiPage<ApiProjectVolunteer> page = projectVolunteerFacade.queryVolunteerList(apiProjectVolunteer, 1, 1);
			if(page != null && page.getTotal() > 0){//已经是志愿者
				item.put("code", 1);
				item.put("msg", "可以报名");
			}else{
				//不是志愿者
				item.put("code", 4);
				item.put("msg", "还不是志愿者");
			}
	    }
		return item;
	}
	
	/**
	 * 活动反馈列表
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "getActivityFeedBack", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getActivityFeedBack(HttpServletResponse response, 
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam("activityId") Integer activityId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		Page p = new Page();
        p.setPage(pageNum);
        p.setPageNum(pageSize);
        List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
        p.setData(feedbacks);
        
        ApiProjectFeedback feedBack = new ApiProjectFeedback();
        feedBack.setAuditState(203);
        feedBack.setActivityId(activityId);
        
        try {
        	ApiPage<ApiProjectFeedback> page = projectFacade.queryH5ProjectFeedbckByCondition(feedBack, p.getPage(), p.getPageNum());		
			if(page!=null && page.getTotal() > 0 && (page.getTotal() - pageNum*pageSize >= 0 
					|| pageNum * pageSize-page.getTotal() < pageSize)){
				for (ApiProjectFeedback apiFeedback: page.getResultData()) {
					JSONObject json = new JSONObject();
					json.put("id", apiFeedback.getId());
					json.put("content", apiFeedback.getContent());
					json.put("feedbackTime", DateUtil.parseToDefaultDateTimeString(apiFeedback.getFeedbackTime()));
					json.put("contentImgs", apiFeedback.getContentImageUrl());
					json.put("activityId", apiFeedback.getActivityId());
					json.put("userName", apiFeedback.getUserName());
					json.put("title", apiFeedback.getTitle());
					json.put("headImg", apiFeedback.getHeadImageUrl());
					jsonArray.add(json);
				}
			}
			result.put("data", jsonArray);
			result.put("count", page.getTotal());
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			item.put("code", -1);
			item.put("msg", "error");
		}
		return item;
	}
	
	/**
	 * 判断用户是否是该活动的管理员
	 * @param activityId
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "isOrNotActivityAdmin", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject isOrNotActivityAdmin(@RequestParam("activityId") Integer activityId, 
			HttpServletResponse response, HttpServletRequest request){
		JSONObject item = new JSONObject();
		Integer userId = UserUtil.getUserId(request, response);
		ApiActivityConfig apiActivityConfig = homePageFacade.queryById(activityId);
		if(apiActivityConfig == null){
			item.put("code", 3);
	    	item.put("msg", "活动不存在");
	    	return item;
		}
		if(userId != null){
			ApiCompany company = new ApiCompany();
		    company.setUserId(apiActivityConfig.getUserId());
		    company = companyFacade.queryCompanyByParam(company);
		    if(company != null){
		    	if(!company.getUserId().equals(userId)){
		    		item.put("code", 4);
			    	item.put("msg", "不是管理员");
			    	return item;
		    	}
		    }else{
		    	company = new ApiCompany();
		 	    company.setActivityAdminIds(apiActivityConfig.getUserId()+"");
		 	    company = companyFacade.queryCompanyByParam(company);
		 	    if(company != null){
		 	    	if(!company.getActivityAdminIds().contains(userId+"")){
		 	    		item.put("code", 4);
				    	item.put("msg", "不是管理员");
				    	return item;
			    	}
		 	    	
		 	    }else{
		 	    	item.put("code", 4);
			    	item.put("msg", "不是管理员");
			    	return item;
		 	    }
		    }
		}else{//未登录
			item.put("code", 2);
	    	item.put("msg", "未登录");
	    	return item;
		}
		item.put("code", 1);
    	item.put("msg", "是管理员");
		return item;
	}
	
	
}
