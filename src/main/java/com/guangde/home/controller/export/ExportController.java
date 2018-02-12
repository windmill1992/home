package com.guangde.home.controller.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiGoodLibraryProple;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectMoneyConfig;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiProjectVolunteer;
import com.guangde.entry.ApiReport;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("export")
public class ExportController {
	private final Logger logger = LoggerFactory.getLogger(ExportController.class);
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	
	@RequestMapping("donate_detail_view")
	public ModelAndView donate_detail_view(ModelAndView view,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="extensionPeople",required=false)Integer extensionPeople){
		view = new ModelAndView("export/h5/donate_detail");
		//微信授权登录
		Integer home_userId = UserUtil.getUserId(request, response);
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
        if(home_userId ==null)
        {
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
        			home_userId = user.getId();
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
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		//==================微信登陆end================//
        if(home_userId==null){
        	home_userId=user.getId();
        }
        
        user = userFacade.queryById(home_userId);
        view.addObject("user", user);
		ApiProject project = projectFacade.queryProjectDetail(projectId);
        if(project!=null){
        	ApiFrontUser user2 = userFacade.queryById(project.getUserId());
        	project.setNickName(user2.getUserName());
        	view.addObject("project", project);
        }
        
        /*随机获取祝福语*/
		ApiConfig apiConfig = new ApiConfig();
    	apiConfig.setConfigKey(StringUtil.LEAVEWORD);
    	List<ApiConfig> configs = commonFacade.queryList(apiConfig);
    	if(configs!=null&&configs.get(0)!=null&&configs.get(0).getConfigValue()!=null&&configs.get(0).getConfigValue()!=""){
    		String[] configss = configs.get(0).getConfigValue().split("。");
    		Random random = new Random();
    		int i = random.nextInt(configss.length);
    		view.addObject("leaveWord", configss[i]);
    	}
    	view.addObject("extensionPeople", extensionPeople);
		return view;
	}
	
	/**
	 * project id list
	 * @param userId userName password
	 * @return
	 */
	@RequestMapping(value="/projectIds/list",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject projectIds_list(@RequestParam("userId")Integer userId,
			@RequestParam(value="userName")String userName,
			@RequestParam(value="password")String password,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		//校验用户是否存在
		ApiFrontUser user = new ApiFrontUser();
		user.setUserName(userName);
		user.setUserPass(password);
		ApiResult result = userFacade.localUserlogin(user);
		ApiFrontUser newF = (ApiFrontUser)result.getObject();
		item.put("code", result.getCode());
		if(result.getCode()!=1){
			item.put("msg", result.getMessage());
		}
		else if(result.getCode()==1 && newF!=null && newF.getId().equals(userId)){
			//查询项目ids
			List<Integer> list = projectFacade.queryProjectIds(userId);
			if(list!=null && list.size()>0){
				JSONObject json = new JSONObject();
				item.put("total", list.size());
				json.put("ids", list);
				item.put("result", json);
			}
		}else{
			item.put("msg", "用户异常！");
		}
		return item;
	}
	
	/**
	 * project list
	 * @param userId userName password
	 * @return
	 */
	@RequestMapping(value="/project/list",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject project_list(@RequestParam("userId")Integer userId,
			@RequestParam(value="userName")String userName,
			@RequestParam(value="password")String password,
			@RequestParam(value="id")Integer id,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject json = new JSONObject();
		//校验用户是否存在
		ApiFrontUser user = new ApiFrontUser();
		user.setUserName(userName);
		user.setUserPass(password);
		ApiResult result = userFacade.localUserlogin(user);
		ApiFrontUser newF = (ApiFrontUser)result.getObject();
		item.put("code", result.getCode());
		if(result.getCode()!=1){
			item.put("msg", result.getMessage());
		}
		else if(result.getCode()==1 && newF!=null && newF.getId().equals(userId)){
			//查询项目
			ApiProject p = projectFacade.queryProjectDetail(id);
			if(p!=null){
				json.put("id", p.getId());
				json.put("userId", p.getUserId());
				json.put("fieldChinese", p.getFieldChinese());
				json.put("tag", p.getTag());
				json.put("title", p.getTitle());
				json.put("subTitle", p.getSubTitle());
				json.put("content", p.getContent());
				json.put("coverImageUrl", p.getCoverImageUrl());
				json.put("location", p.getLocation());
				json.put("detailAddress", p.getDetailAddress());
				json.put("cryMoney", p.getCryMoney());
				json.put("donatAmount", p.getDonatAmount());
				json.put("donationNum", p.getDonationNum());
				json.put("state", p.getState());
				json.put("registrTime", p.getRegistrTime());
				json.put("deadline", p.getDeadline());
				json.put("issueTime", p.getIssueTime());
				json.put("isHide", p.getIsHide());
				JSONArray fileJsonArray = new JSONArray();
				ApiBFile file = new ApiBFile();
				if(p.getContentImageId()!=null && !"".equals(p.getContentImageId())){
					String[] contentImgIds = p.getContentImageId().split(",");
					for (String contentImgId : contentImgIds) {
						JSONObject fileObject = new JSONObject();
						file = fileFacade.queryBFileById(Integer.valueOf(contentImgId));
						if(file!=null){
							fileObject.put("contentImgUrl", file.getUrl());
							fileObject.put("description", file.getDescription());
							
						}
						fileJsonArray.add(fileObject);
					}
				}
				json.put("contentImgUrls", fileJsonArray);
			}
		}else{
			item.put("msg", "用户异常！");
		}
		item.put("result", json);
		return item;
	}
	
	
	@RequestMapping("view_h5")
	public ModelAndView view(
			@RequestParam(value = "projectId") Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException {
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		ModelAndView viewback = new ModelAndView("redirect:/project/view.do?projectId=" + projectId);
        String isMobile = (String)request.getSession().getAttribute("ua");
        if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople!=null){//h5专项基金页面
        	viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople==null){//h5专项基金页面
        	viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId);
            return viewback;
        }
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
            viewback.setViewName("redirect:/project/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if(!"mobile".equals(isMobile)){
        	return viewback;
        }
		ModelAndView view = new ModelAndView("h5/project/yunhuProject_detail");
		if(("crowdfunding").equals(project.getField())) {
			view = new ModelAndView("h5/project/project_detail_crowdfunding");
		}
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		
		//项目配捐
        ApiMatchDonate apiMatchDonate = new ApiMatchDonate();
        apiMatchDonate.setProjectId(project.getId());
        ApiPage<ApiMatchDonate> apiPage = redPacketsFacade.queryByParam(apiMatchDonate, 1, 5);
        List<ApiMatchDonate> matchDonates = apiPage.getResultData();
        if(matchDonates != null && matchDonates.size() >0){
        	view.addObject("matchDonate", matchDonates.get(0));
        }
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if(userId!=null){
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("user", user);
		}
		if (project != null) {
			view.addObject("desc",project.getSubTitle());
			process = 0.0;
			if(project.getCryMoney()>=0.001){
				process = project.getDonatAmount() / project.getCryMoney();
			}
			view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0));
			view.addObject("processbfb", StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				view.addObject("owner", true);
			}
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			key = PengPengConstants.PROJECT_USERINFO_LIST+"_"+projectId;
			userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
			List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
			for(ApiProjectUserInfo u:userInfos){
				if(u.getPersonType()==0){
					view.addObject("shouzhu", u);
				}else if(u.getPersonType()==1){
					view.addObject("zhengming", u);
				}else if(u.getPersonType()==2){
					view.addObject("fabu", u);
				}
			}
		}
		if (reports != null && reports.getTotal() > 0) {
			view.addObject("reports", reports.getResultData());
		}
		
		//判断是否加入善库
		ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade.queryByParam(apigp,1,50);
		if(goodLibrary != null && goodLibrary.getResultData().size() > 0){
			view.addObject("goodLibrary", 1);
		}else {
			view.addObject("goodLibrary", 0);
		}
		//企业助善信息
		ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
		good_help.setProject_id(projectId);
		good_help.setOrderBy("createTime");
		good_help.setOrderDirection("desc");
		good_help.setState(203);
		List<String> list = new ArrayList<String>(1);
		list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		good_help.initCache(true,DateUtil.DURATION_MIN_S,list,"project_id","state");
		ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade.queryCompanyGoodHelpListByCondition(good_help, 1 ,4);
		if(goodHelps!=null&&goodHelps.getTotal()>0){
			view.addObject("goodhelps", goodHelps.getResultData());
			view.addObject("firstgh",goodHelps.getResultData().get(0));
		}
        String userType = SSOUtil.getCurrentUserType(request, response);
		if(ProjectConstant.PROJECT_STATUS_COLLECT==project.getState()){
			 if("enterpriseUsers".equals(userType)&&process<1&&project.getState()==240&&userId!=null){
					if(goodHelps==null||goodHelps.getTotal()==0){
						ApiCompany apiCompany = new ApiCompany();
						apiCompany.setUserId(userId);
						apiCompany.setState(203);
						List<String> llist = new ArrayList<String>(1);
						llist.add(ApiCompany.getCacheRange(apiCompany.getClass().getName(), BaseBean.RANGE_WHOLE, userId));
						apiCompany.initCache(true,DateUtil.DURATION_MIN_S,llist,"userId","state");
						apiCompany = companyFacade.queryCompanyByParam(apiCompany);
						if(apiCompany!=null)
						view.addObject("isComapny", true);
					}
				}else if(userType==null){
					view.addObject("isComapny", true);
				}
		}
		//最新捐款列表
		
		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true,DateUtil.DURATION_MIN_S,llist,"projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		view.addObject("peopleNum", project.getDonationNum());
		String browser = UserUtil.Browser(request);
		
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
		
		
		ApiProjectVolunteer apiProjectVolunteer = new ApiProjectVolunteer();
		apiProjectVolunteer.setProjectId(projectId);
		int number = projectVolunteerFacade.count(apiProjectVolunteer);
		
		 
		ApiPage<ApiProjectVolunteer> pvs = projectVolunteerFacade.queryVolunteerList(apiProjectVolunteer,1, 5);
		List<ApiProjectVolunteer> alist = pvs.getResultData();
		
		view.addObject("alist",alist);
		view.addObject("number",number);
		view.addObject("projectId",projectId);
		view.addObject("extensionPeople",extensionPeople);
		view.addObject("browser",browser);
		view.addObject("userId", userId);
		
		String itemType = request.getParameter("itemType")==null?"":request.getParameter("itemType");
		view.addObject("itemType",itemType);
		/*随机获取祝福语*/
		ApiConfig apiConfig = new ApiConfig();
    	apiConfig.setConfigKey(StringUtil.LEAVEWORD);
    	List<ApiConfig> configs = commonFacade.queryList(apiConfig);
    	if(configs!=null&&configs.get(0)!=null&&configs.get(0).getConfigValue()!=null&&configs.get(0).getConfigValue()!=""){
    		String[] configss = configs.get(0).getConfigValue().split("。");
    		Random random = new Random();
    		int i = random.nextInt(configss.length);
    		view.addObject("leaveWord", configss[i]);
    	}
    	
    	//捐款金额配置
		ApiProjectMoneyConfig param = new ApiProjectMoneyConfig();
		param.setProjectId(projectId);
		List<ApiProjectMoneyConfig> moneyConfigs=projectFacade.queryMoneyConfigByParam(param);
		view.addObject("moneyConfigs", moneyConfigs);
		/*点击量统计start*/
		try
		{
			//获取当天剩余缓存时间
			long startTime = (DateUtil.getCurrentDayEnd().getTime() - new Date().getTime())/1000; 
			String clickRate = (String) redisService.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_H5+projectId);
			if(StringUtils.isEmpty(clickRate))
			{
				redisService.saveObjectData(PengPengConstants.PROJECT_CLICKRATE_H5+projectId, "t_h5_"+projectId+"_"+1,startTime);
			}
			else
			{
				String cstr[] = clickRate.split("_");
				if(null != cstr && cstr.length >= 4)
				{
					Integer click = Integer.parseInt(cstr[3]);
					if(null == click)
					{
						click = 1 ;
					}
					else
					{
						click += 1 ;
					}
					
					redisService.saveObjectData(PengPengConstants.PROJECT_CLICKRATE_H5+projectId, "t_h5_"+projectId+"_"+click,startTime);
				}
			}
		}
		catch(Exception e)
		{
			
		}
	
		/*点击量统计end*/
		
		return view;
	}
}
