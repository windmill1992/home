package com.guangde.home.controller.project;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiDonateTime;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectMoneyConfig;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiReport;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("h5GardenProject")
public class H5GardenProjectController
{
    Logger logger = LoggerFactory.getLogger(H5GardenProjectController.class);
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IFileFacade fileFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private INewsFacade newsFacade;
	
	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	
	@Autowired
    private IGoodLibraryFacade goodLibraryFacade;
	
	@Autowired
	private ICompanyFacade companyFacade;
	
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
    
    @RequestMapping("projectDetail")
    public ModelAndView projectDetail(@RequestParam(value = "projectId") Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "message", required = false,defaultValue="0") Integer donateTimeId,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException
    {
    	ModelAndView mv=new ModelAndView();
        String isMobile = (String)request.getSession().getAttribute("ua");
       /* if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
        	 response.sendRedirect("http://sy.17xs.org/project/syview.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
        }
        if(!"mobile".equals(isMobile)){
        	response.sendRedirect("http://sy.17xs.org/project/syview.do?projectId="+projectId);
        }*/
        String weixin_code = request.getParameter("code");
    	if(weixin_code!=null&&!"authdeny".equals(weixin_code)){//授权登录
    		ApiFrontUser user1 = new ApiFrontUser();//用户
    		String openId ="";
    		String token = "";
    		String unionid = "";
    		Map<String, Object> mapToken = new HashMap<String, Object>(8);
    		openId = (String) request.getSession().getAttribute("openId");
    		Object OToken = redisService.queryObjectData("weixin_token");
    		token = (String)OToken;
    		if ("".equals(openId) || openId == null || OToken == null) {
    		mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
    		openId = mapToken.get("openid").toString();
			unionid = mapToken.get("unionid").toString();
			token = mapToken.get("access_token").toString();
			request.getSession().setAttribute("openId", openId);
			System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
			redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
			user1 = CommonUtils.queryUser(request,openId,token,unionid);
			try
    		{
    			// 自动登录
				SSOUtil.login(user1, request, response);
    		}
    		catch(Exception e)
    		{
    			logger.error("",e);
    			//return webUtil.resMsg(2, "", "发生错误", null);
    		}
    	}}
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	// 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
    	String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
    	report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
    	//ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1, 30);
		
    	double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			if(project.getSubTitle()!=null){
				String desc=StringUtil.delHTMLTag(project.getSubTitle());
				if(desc.length()>30){
					desc=desc.substring(0,30)+"...";
				}
				mv.addObject("desc",desc);
			}
			else{
				mv.addObject("desc",project.getSubTitle());
			}
			process = 0.0;
			if(project.getCryMoney()>=0.001){
				process = project.getDonatAmount() / project.getCryMoney();
			}
			mv.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),2));
			mv.addObject("processbfb", StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				mv.addObject("owner", true);
			}
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			key = PengPengConstants.PROJECT_USERINFO_LIST+"_"+projectId;
			userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
			List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
			for(ApiProjectUserInfo u:userInfos){
				if(u.getPersonType()==0){
					mv.addObject("shouzhu", u);
				}else if(u.getPersonType()==1){
					mv.addObject("zhengming", u);
				}else if(u.getPersonType()==2){
					mv.addObject("fabu", u);
					//获取当时发布项目人员的信息
					if(u.getHelpType()!=null && !"".equals(u.getHelpType())){
						ApiFrontUser frontUser=userFacade.queryById(project.getUserId());
						if(u.getHelpType()==4 || u.getHelpType()==5){
							ApiCompany ac=new ApiCompany();
							ac.setUserId(project.getUserId());
							ApiCompany company=companyFacade.queryCompanyByParam(ac);
							mv.addObject("company", company);
						}
						else if(u.getHelpType()==1 ||u.getHelpType()==2 || u.getHelpType()==3){
							mv.addObject("frontUser", frontUser);
						}
						else if(u.getHelpType()==6 ||u.getHelpType()==7 || u.getHelpType()==8){
							mv.addObject("individal", 1);
							mv.addObject("frontUser", frontUser);
						}else if(u.getHelpType()==9 ||u.getHelpType()==10 ){
							mv.addObject("individal",0);
							mv.addObject("frontUser", frontUser);
						}
					}
					else {
						//pc端发布的项目
						if(project.getUserId()!=null){
							ApiFrontUser frontUser=userFacade.queryById(project.getUserId());
							if("individualUsers".equals(frontUser.getUserType())){
								mv.addObject("individal", 1);
								mv.addObject("frontUser", frontUser);
							}else{
								mv.addObject("individal", 0);
								mv.addObject("frontUser", frontUser);
							}
						}
						
					}
				}
			}
		}
		/*if (reports != null && reports.getTotal() > 0) {
			mv.addObject("reports", reports.getResultData());
		}*/
		//判断是否加入善库
		/*ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade.queryByParam(apigp,1,50);
		if(goodLibrary != null && goodLibrary.getResultData().size() > 0){
			mv.addObject("goodLibrary", 1);
		}else {
			mv.addObject("goodLibrary", 0);
		}*/
		//企业助善信息
		/*ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
		good_help.setProject_id(projectId);
		good_help.setOrderBy("createTime");
		good_help.setOrderDirection("desc");
		good_help.setState(203);
		List<String> list = new ArrayList<String>(1);
		list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		good_help.initCache(true,DateUtil.DURATION_MIN_S,list,"project_id","state");
		ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade.queryCompanyGoodHelpListByCondition(good_help, 1 ,4);
		if(goodHelps!=null&&goodHelps.getTotal()>0){
			mv.addObject("goodhelps", goodHelps.getResultData());
			mv.addObject("firstgh",goodHelps.getResultData().get(0));
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
						mv.addObject("isComapny", true);
				}
			}else if(userType==null){
				mv.addObject("isComapny", true);
			}
		}*/
		//最新捐款列表
		/*ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true,DateUtil.DURATION_MIN_S,llist,"projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
						r, 1, 10);
		mv.addObject("donates", donats.getResultData());
		mv.addObject("peopleNum", project.getDonationNum());*/
		
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
			SortedMap<String, String> map;
			try {
				map = H5Demo.getConfigweixin(jsTicket,perfecturl);
				mv.addObject("appId",map.get("appId"));
				mv.addObject("timestamp",map.get("timeStamp"));
				mv.addObject("noncestr",map.get("nonceStr"));
				mv.addObject("signature",map.get("signature"));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
			
		}
		
		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		r.setState(302);
		double extensionDonateAmount = donateRecordFacade.countQueryByCondition(r);
		//捐款金额配置
		ApiProjectMoneyConfig param = new ApiProjectMoneyConfig();
		param.setProjectId(projectId);
		List<ApiProjectMoneyConfig> moneyConfigs=projectFacade.queryMoneyConfigByParam(param);
		mv.addObject("moneyConfigs", moneyConfigs);
		/*ApiProjectVolunteer apiProjectVolunteer = new ApiProjectVolunteer();
		apiProjectVolunteer.setProjectId(projectId);
		int number = projectVolunteerFacade.count(apiProjectVolunteer);
		
		 
		ApiPage<ApiProjectVolunteer> pvs = projectVolunteerFacade.queryVolunteerList(apiProjectVolunteer,1, 5);
		List<ApiProjectVolunteer> alist = pvs.getResultData();
		
		mv.addObject("alist",alist);
		mv.addObject("number",number);*/
		ApiFrontUser user = new ApiFrontUser();
		if(userId!=null){
			user=userFacade.queryById(userId);
		}
		mv.addObject("user", user);
		mv.addObject("projectId",projectId);
		mv.addObject("extensionPeople",extensionPeople);
		mv.addObject("browser",browser);
		mv.addObject("userId", userId);
		mv.addObject("extensionDonateAmount", extensionDonateAmount);
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
		{}
		/*点击量统计end*/
		if(donateTimeId>0&&userId!=null){//充值成功返回到项目详情页
			ApiDonateTime donate =new ApiDonateTime();
			donate.setId(donateTimeId);
			ApiPage<ApiDonateTime> page = donateRecordFacade.queryDonateTimeByParam(donate,1,20);
			if(page.getTotal()>0&&page.getResultData().get(0).getState()==200){//日捐草稿状态
				donate.setState(201);
				ApiResult result = donateRecordFacade.updateDonateTime(donate);
				if(result.getCode()==1){
					mv.addObject("message", donateTimeId);
				}
				else{
					mv.addObject("message", 0);
				}
			}
			else{
				mv.addObject("message", 0);
			}
		}
		else{
			mv.addObject("message", 0);
		}
    	mv.addObject("project", project);
    	mv.setViewName("h5/garden/project_detail");
    	return mv;
    }
    
    /*
     * 捐款金额配置查询
     */
    @RequestMapping("getMoneyConfigList")
    @ResponseBody
    public Map<String, Object> getMoneyConfigList(@RequestParam(value = "projectId", required = true) Integer projectId,
        @RequestParam(value = "priority", required = true) Integer priority, @RequestParam(value = "type", required = true) String type)
    {
    	int max;
    	ApiProjectMoneyConfig moneyConfig = new ApiProjectMoneyConfig();
    	moneyConfig.setProjectId(projectId);
    	List<ApiProjectMoneyConfig> list = projectFacade.queryMoneyConfigByParam(moneyConfig);
    	if(list.size()>0){
    		max=list.size()-1;
    		if(type.equals("add")){
        		if(priority<max){//查询下一个
        			moneyConfig.setPriority(priority+1);
        			List<ApiProjectMoneyConfig> list_new = projectFacade.queryMoneyConfigByParam(moneyConfig);
        			return webUtil.resMsg(1, "0000", "成功", list_new.get(0));
        		}
        		else{//到达最大值，不变
        			return webUtil.resMsg(1, "0000", "成功", null);
        		}
        	}
    		else{
    			if(priority>0){//查询上一个
    				moneyConfig.setPriority(priority-1);
        			List<ApiProjectMoneyConfig> list_new = projectFacade.queryMoneyConfigByParam(moneyConfig);
    				return webUtil.resMsg(1, "0000", "成功", list_new.get(0));
    			}
    			else{//达到最小值，不变
    				return webUtil.resMsg(1, "0000", "成功", null);
    			}
    		}
    	}
    	else{
    		return webUtil.resMsg(0, "0000", "项目异常", null);
    	}
    }
    
    /**
     * 开通日捐
     * @param request
     * @param response
     * @param projectIds
     * @param money
     * @param category
     * @return
     */
    @RequestMapping("openDayDonate")
    @ResponseBody
    public Map<String, Object> openDayDonate(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="projectIds",required=false)String projectIds,
    		@RequestParam(value="money",required=true)Double money,
    		@RequestParam(value="category",required=true)String category){
    	Integer userId = UserUtil.getUserId(request, response);
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		//StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = "http://www.17xs.org/h5GardenProject/projectDetail.do?projectId=" +projectIds ;//url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//用户
        if(userId != null && userId != 0)
        {
	     	 user = userFacade.queryById(userId); 
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
        					return webUtil.resMsg(-2, "0002", url_weixin_code, null);
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				unionid = mapToken.get("unionid").toString();
        				token = mapToken.get("access_token").toString();
        				request.getSession().setAttribute("openId", openId);
        				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        				user = CommonUtils.queryUser(request,openId,token,unionid);
        				try
                		{
                			// 自动登录
        					SSOUtil.login(user, request, response);
                		}
                		catch(Exception e)
                		{
                			logger.error("",e);
                			return webUtil.resMsg(2, "", "发生错误", null);
                		}
        				
        			}else{
        				user = CommonUtils.queryUser(request,openId,token,unionid);
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
        				}
        			
        		} catch (Exception e) {
        			logger.error("微信登录出现问题"+ e);
        			return webUtil.resMsg(2, "", "发生错误", null);
        		}
        	}
        	else{//返回到账号密码页面
        		return webUtil.loginFailedRes(null);
        	}
        	
        }
        ApiDonateTime dt = new ApiDonateTime();
		dt.setMoney(money);
		dt.setUserId(userId);
		dt.setType(0);
		dt.setNotice(-1);
		dt.setCategory(category);
		dt.setNumber(0);
		if(projectIds==null||"".equals(projectIds)){
			return webUtil.failedRes("0007", "指定的项目为空", null);
		}
		dt.setProjectIds(projectIds);
		//money = money*10; 
		if(money > user.getBalance()){
			dt.setState(200);
			ApiResult result = donateRecordFacade.saveDonateTimeReturnId(dt);
			return webUtil.failedRes("0003", result.getMessage(), null);
		}
		dt.setState(201);
		ApiResult result = donateRecordFacade.saveDonateTime(dt);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "日捐月捐发起失败！", null);
		}
		/*else
		{
			if(dt.getNotice() == 0) { //选择短信通知时
				//发起月捐成功短信通知
				ApiAnnounce apiAnnounce = new ApiAnnounce();
				if(dt.getType() == 0) { //日捐
					apiAnnounce.setCause("日捐通知");
					apiAnnounce.setContent(user.getNickName()+"，感谢您对公益事业的支持，您的日捐计划已经开始执行，每次捐助情况"
			    			+"将通过短信通知您，您还可以关注善园基金会公众号了解项目执行进展。");
				}else {
					apiAnnounce.setCause("月捐通知");
					apiAnnounce.setContent(user.getNickName()+"，感谢您对公益事业的支持，您的月捐计划已经开始执行，每次捐助情况"
			    			+"将通过短信通知您，您还可以关注善园基金会公众号了解项目执行进展。");
				}
		    	apiAnnounce.setDestination(dt.getMobileNum());
		    	apiAnnounce.setType(1);
		    	apiAnnounce.setPriority(1);
		    	commonFacade.sendSms(apiAnnounce, false);
			}
		}*/
    Map<String,Object> r = webUtil.successRes(null);
	return  r ;
    }
    
    /**
     * 项目列表1
     * @return
     */
    @RequestMapping("project_list")
    public ModelAndView project_list(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="message",required=false,defaultValue="0")Integer donateTimeId){
    	ModelAndView view = new ModelAndView("h5/garden/project_list");
    	String browser = UserUtil.Browser(request);
    	//String isMobile = (String)request.getSession().getAttribute("ua");
        /* if (!"mobile".equals(isMobile) && extensionPeople!=null)
         {
         	 response.sendRedirect("http://sy.17xs.org/project/syview.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
         }
         if(!"mobile".equals(isMobile)){
         	response.sendRedirect("http://sy.17xs.org/project/syview.do?projectId="+projectId);
         }*/
         String weixin_code = request.getParameter("code");
     	if(weixin_code!=null&&!"authdeny".equals(weixin_code)){//授权登录
     		ApiFrontUser user1 = new ApiFrontUser();//用户
     		String openId ="";
     		String token = "";
     		String unionid = "";
     		Map<String, Object> mapToken = new HashMap<String, Object>(8);
     		openId = (String) request.getSession().getAttribute("openId");
     		Object OToken = redisService.queryObjectData("weixin_token");
     		token = (String)OToken;
     		if ("".equals(openId) || openId == null || OToken == null) {
     		mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
     		openId = mapToken.get("openid").toString();
 			unionid = mapToken.get("unionid").toString();
 			token = mapToken.get("access_token").toString();
 			request.getSession().setAttribute("openId", openId);
 			System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
 			redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
 			user1 = CommonUtils.queryUser(request,openId,token,unionid);
 			try
     		{
     			// 自动登录
 				SSOUtil.login(user1, request, response);
     		}
     		catch(Exception e)
     		{
     			logger.error("",e);
     			//return webUtil.resMsg(2, "", "发生错误", null);
     		}
     	}}
     	Integer userId = UserUtil.getUserId(request, response);
     	if(userId!=null){
     		ApiFrontUser user = userFacade.queryById(userId);
     		view.addObject("user", user);
     	}
     	ApiProject param = new ApiProject();
     	param.setField("good");
     	List<ApiProject> onelevelList = projectFacade.queryOneLevel(param);
    	
    	param.setState(240);
    	ApiPage<ApiProject> pages = projectFacade.queryRandomProjectList(param, 1, 30);
    	if(pages.getTotal()>0){
    		view.addObject("projects", pages.getResultData());
    	}
    	if(donateTimeId>0&&userId!=null){//充值成功返回到项目详情页
			ApiDonateTime donate =new ApiDonateTime();
			donate.setId(donateTimeId);
			ApiPage<ApiDonateTime> page = donateRecordFacade.queryDonateTimeByParam(donate,1,20);
			if(page.getTotal()>0&&page.getResultData().get(0).getState()==200){//日捐草稿状态
				donate.setState(201);
				ApiResult result = donateRecordFacade.updateDonateTime(donate);
				if(result.getCode()==1){
					view.addObject("message", donateTimeId);
				}
				else{
					view.addObject("message", 0);
				}
			}
			else{
				view.addObject("message", 0);
			}
		}
		else{
			view.addObject("message", 0);
		}
     	view.addObject("list", onelevelList);
     	view.addObject("browser", browser);
    	return view;
    }
    
    /**
     * 加载项目列表1
     * @param address
     * @param type
     * @param page
     * @param pageNum
     * @return
     */
    @RequestMapping("getProject_list")
    @ResponseBody
    public Map<String, Object> getProject_list(@RequestParam(value="address",required=false)String address,
    		@RequestParam(value="type",required=true)String type,
    		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
    		@RequestParam(value = "pageNum", required = false, defaultValue = "20") Integer pageNum){
    	ApiProject param = new ApiProject();
        JSONArray items = new JSONArray();
    	if(!"".equals(address)&&address != null&&address.contains("省")){
    		String[] area = address.trim().split(">");
    		param.setLocation(area[0].replace("省",""));
    	}
    	//100：如法放生 200：供养佛宝 300：供养僧众 400：倡印法宝
    	/*if("如法放生".equals(type)){
    		type="100";
    	}
    	else if("供养佛宝".equals(type)){
    		type="200";
    	}
    	else if("供养僧众".equals(type)){
    		type="300";
    	}
    	else{
    		type="400";
    	}
    	*/
    	if(!"".equals(type)&&type!=null){
    		param.setOneLevel(type);
    	}
    	param.setField("good");
    	//param.setIsHide(0);
    	param.setState(240);
    	ApiPage<ApiProject> pages = projectFacade.queryProjectList(param, page, pageNum);
    	if(pages.getTotal()>0){
    		if(page!=1&&(pages.getTotal()-page*pageNum)<0){
    			return webUtil.successRes(null);
    		}
    		else{
    		ApiProjectUserInfo info = new ApiProjectUserInfo();
    		for(ApiProject p:pages.getResultData()){
    			JSONObject item = new JSONObject();
    			double donatePercent=(p.getDonatAmount()/p.getCryMoney())*100;
        		DecimalFormat df = new DecimalFormat("0.0");
        		String db = df.format(donatePercent);
        		int d_value = DateUtil.minutesBetween(p.getIssueTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(p.getIssueTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flag) {
					item.put("issueTime", DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日",p.getIssueTime()));
				} else {
					if (d_value / 60 >= 1) {
						item.put("issueTime", d_value / 60 + "小时前");
					} else {
						if (d_value == 0) {
							item.put("issueTime", "刚刚");
						} else {
							item.put("issueTime", d_value + "分钟前");
						}
					}
				}
    			info.setProjectId(p.getId());
    			info.setPersonType(1);
    			ApiProjectUserInfo info2 = projectFacade.queryProjectUserInfo(info);
    			item.put("projectId", p.getId());
    			item.put("title", p.getTitle());
    			//String content = StringUtil.delHTMLTag(p.getContent());
    			//p.setContent(content);
    			if(p.getSubTitle()!=null&&"".equals(p.getSubTitle())){
    				item.put("content", p.getSubTitle().length()>100?p.getSubTitle().substring(0, 45)+"...":p.getSubTitle());
    			}
    			else{
    				item.put("content", p.getSubTitle());
    			}
    			item.put("coverImageUrl", p.getCoverImageUrl());
    			item.put("donateAmount", p.getDonatAmount());
    			item.put("cryMoney", p.getCryMoney());
    			item.put("donatePercent", db+"%");
    			item.put("familyAddress", info2.getFamilyAddress());
    			item.put("workUnit", info2.getWorkUnit());
    			item.put("total", pages.getTotal());
    			items.add(item);
    		}
    		return webUtil.successRes(items);
    		}
    	}
    	return webUtil.successRes(null);
    }
    
    /**
     * 开通日捐(list页面)
     * @param request
     * @param response
     * @param projectIds
     * @param money
     * @param category
     * @return
     */
    @RequestMapping("openDayDonates")
    @ResponseBody
    public Map<String, Object> openDayDonates(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="projectIds",required=false)String projectIds,
    		@RequestParam(value="money",required=true)Double money,
    		@RequestParam(value="category",required=true)String category){
    	Integer userId = UserUtil.getUserId(request, response);
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		//StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = "http://www.17xs.org/h5GardenProject/project_list.do" ;//url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//用户
        if(userId != null && userId != 0)
        {
	     	 user = userFacade.queryById(userId); 
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
        					return webUtil.resMsg(-2, "0002", url_weixin_code, null);
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				unionid = mapToken.get("unionid").toString();
        				token = mapToken.get("access_token").toString();
        				request.getSession().setAttribute("openId", openId);
        				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        				user = CommonUtils.queryUser(request,openId,token,unionid);
        				try
                		{
                			// 自动登录
        					SSOUtil.login(user, request, response);
                		}
                		catch(Exception e)
                		{
                			logger.error("",e);
                			return webUtil.resMsg(2, "", "发生错误", null);
                		}
        				
        			}else{
        				user = CommonUtils.queryUser(request,openId,token,unionid);
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
        				}
        			
        		} catch (Exception e) {
        			logger.error("微信登录出现问题"+ e);
        			return webUtil.resMsg(2, "", "发生错误", null);
        		}
        	}
        	else{//返回到账号密码页面
        		return webUtil.loginFailedRes(null);
        	}
        	
        }
		ApiDonateTime dt = new ApiDonateTime();
		dt.setMoney(money);
		dt.setUserId(userId);
		dt.setType(0);
		dt.setNotice(-1);
		dt.setCategory(category);
		dt.setNumber(0);
		dt.setProjectIds(projectIds);
		if(money > user.getBalance()){
			dt.setState(200);
			ApiResult result = donateRecordFacade.saveDonateTimeReturnId(dt);
			//String projectss[] = projectIds.replace("，", ",").split(",");
			//money = money*10*projectss.length;
			return webUtil.failedRes("0003", result.getMessage(), null);
		}
		dt.setState(201);
		ApiResult result = donateRecordFacade.saveDonateTime(dt);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "日捐月捐发起失败！", null);
		}
    Map<String,Object> r = webUtil.successRes(null);
	return  r ;
    }
    
    
    @RequestMapping("goodProjectList")
    @ResponseBody
    public Map<String, Object> goodProjectList(@RequestParam(value="address",required=false)String address,
    		HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
    		@RequestParam(value = "pageNum", required = false, defaultValue = "20") Integer pageNum){
    	Integer userId = UserUtil.getUserId(request, response);
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		//StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = "http://www.17xs.org/h5GardenProject/project_list.do";//url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//用户
        if(userId != null && userId != 0)
        {
	     	 user = userFacade.queryById(userId); 
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
        					return webUtil.resMsg(-2, "0002", url_weixin_code, null);
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				unionid = mapToken.get("unionid").toString();
        				token = mapToken.get("access_token").toString();
        				request.getSession().setAttribute("openId", openId);
        				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        				user = CommonUtils.queryUser(request,openId,token,unionid);
        				try
                		{
                			// 自动登录
        					SSOUtil.login(user, request, response);
                		}
                		catch(Exception e)
                		{
                			logger.error("",e);
                			return webUtil.resMsg(2, "", "发生错误", null);
                		}
        				
        			}else{
        				user = CommonUtils.queryUser(request,openId,token,unionid);
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
        				}
        			
        		} catch (Exception e) {
        			logger.error("微信登录出现问题"+ e);
        			return webUtil.resMsg(2, "", "发生错误", null);
        		}
        	}
        	else{//返回到账号密码页面
        		//return webUtil.loginFailedRes(null);
        	}
        	
        }
        ApiProject param = new ApiProject();
        //JSONArray items = new JSONArray();
        if(!"".equals(address)&&address != null&&address.contains("省")){
    		String[] area = address.trim().split(">");
    		param.setLocation(area[0].replace("省",""));
    	}
    	//100：如法放生 200：供养佛宝 300：供养僧众 400：倡印法宝
    	/*if("如法放生".equals(type)){
    		type="100";
    	}
    	else if("供养佛宝".equals(type)){
    		type="200";
    	}
    	else if("供养僧众".equals(type)){
    		type="300";
    	}
    	else{
    		type="400";
    	}
    	param.setOneLevel(type);*/
    	param.setField("good");
    	//param.setIsHide(0);
    	param.setState(240);
    	ApiPage<ApiProject> pages = projectFacade.queryRandomProjectList(param, page, pageNum);
    	if(pages.getTotal()>0){
    		return webUtil.successRes(pages.getResultData());
    	}
    	return webUtil.successRes(null);
    }
    
    /**
     * goto留言框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("gotoAlterReply")
    public Map<String, Object> gotoAlterReply(ApiNewLeaveWord model,
    		//@RequestParam(value="code",required=true)String code,
    		HttpServletRequest request,HttpServletResponse response){
    	//1.反馈留言：type projectId content createTime updateTime freebackId userId1 userName1
    	//2.反馈回复留言：type projectId content createTime updateTime freebackId userId1 userName1 userId2 userName2
    	//3.捐助留言：type projectId content createTime updateTime donateId userId1 userName1
    	//4.捐助回复留言：type projectId content createTime updateTime donateId userId1 userName1 userId2 userName2
    	//===========微信用户自动登陆start==============//
    			String openId ="";
    			String token = "";
    			String unionid = "";
    			StringBuffer url = request.getRequestURL();
    			String queryString = request.getQueryString();
    			String perfecturl = "http://www.17xs.org/h5GardenProject/projectDetail.do?" + queryString;//url + "?" + queryString;
    			String browser = UserUtil.Browser(request);
    			ApiFrontUser user1 = new ApiFrontUser();//用户
    			Integer userId = UserUtil.getUserId(request, response);
    	        if(userId != null && userId != 0)
    	        {
    		     	 user1 = userFacade.queryById(userId); 
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
    	        					return webUtil.resMsg(-2, "", url_weixin_code, null);
    	        				}
    	        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
    	        				openId = mapToken.get("openid").toString();
    	        				unionid = mapToken.get("unionid").toString();
    	        				token = mapToken.get("access_token").toString();
    	        				request.getSession().setAttribute("openId", openId);
    	        				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
    	        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
    	        				user1 = CommonUtils.queryUser(request,openId,token,unionid);
    	        				try
    	                		{
    	                			// 自动登录
    	        					SSOUtil.login(user1, request, response);
    	                		}
    	                		catch(Exception e)
    	                		{
    	                			logger.error("",e);
    	                			return webUtil.resMsg(2, "", "发生错误", null);
    	                		}
    	        				
    	        			}else{
    	        				user1 = CommonUtils.queryUser(request,openId,token,unionid);
    	        				if (user1.getCoverImageId() == null) 
    	                		{
    	                			user1.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
    	                		}
    	                		else 
    	                		{
    	                			if(user1.getCoverImageId() != null && user1.getCoverImageId() == 0){
    	                				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user1.getCoverImageId()));
    	                				user1.setCoverImageUrl(aBFile.getUrl()); // 个人头像
    	                			}
    	                		}
    	        				}
    	        			
    	        		} catch (Exception e) {
    	        			logger.error("微信登录出现问题"+ e);
    	        			return webUtil.resMsg(2, "", "发生错误", null);
    	        		}
    	        	}
    	        	else{//返回到账号密码页面
    	        		String backUrlString="http://www.17xs.org/ucenter/user/Login_H5.do?flag=leadword_"+model.getProjectId();
    	        		return webUtil.resMsg(-2, "", backUrlString, null);
    	        	}
    	        	
    	        }
        ApiFrontUser user = userFacade.queryById(userId);
        if(model!=null){
        	if(model.getType()==0){
        		if(model.getLeavewordUserId()==null){//反馈留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return  webUtil.resMsg(101, "", "", model);
        		}
        		else{//反馈回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(102, "", "", model);
        		}
        	}
        	else{
        		if(model.getLeavewordUserId()==null){//捐助留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return webUtil.resMsg(201, "", "", model);
        		}
        		else{//捐助回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(202, "", "", model);
        		}
        	}
        }
        return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
}
