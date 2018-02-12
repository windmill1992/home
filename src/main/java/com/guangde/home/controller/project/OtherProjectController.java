package com.guangde.home.controller.project;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IAuctionProjectFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IPayMoneyRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiAnnounce;
import com.guangde.entry.ApiDonateTime;
import com.guangde.entry.ApiAuctionProject;
import com.guangde.entry.ApiBid;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiReport;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.project.ProjectForm;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("project")
public class OtherProjectController
{
    Logger logger = LoggerFactory.getLogger(OtherProjectController.class);

    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private INewsFacade newsFacade;
    
    @Autowired
    private ISystemNotifyFacade systemNotifyFacade;
    
    @Autowired
    private IFileFacade fileFacade;
    
    @Autowired
    private ICommonFacade commonFacade;
    
    @Autowired
    private RedisService redisService;
    
	@Autowired
	private IPayMoneyRecordFacade payMoneyRecordFacade;
	
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	
	@Autowired
	private IAuctionProjectFacade auctionProjectFacade;
	
	public static final String GetAccess_tokenRequest="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa09ee42dbe779694&secret=c8c9005d568c7575770df85d9c92a87c&grant_type=authorization_code";
	public static final String GetAccess_userinfoRequest = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN";
    @RequestMapping("otherindex")
    public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response)
    {
    	  String isMobile = (String)request.getSession().getAttribute("ua");
          if ("mobile".equals(isMobile))
          {
              ModelAndView viewback = new ModelAndView("redirect:/project/otherview_list_h5.do");
              return viewback;
          }
        ModelAndView view = new ModelAndView("otherProject/dogood_list");
        return view;
    }
    
    /*
     * @param projectId 项目ID 显示项目详情
     */
    @RequestMapping("otherview")
    public ModelAndView view(@RequestParam(value = "projectId") Integer projectId,@RequestParam(value = "itemType", required = false, defaultValue = "") Integer itemType,HttpServletRequest request, HttpServletResponse response)
    {
        // 项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        
        // 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
//        String key = PengPengConstants.PROJECT_SCHEDUlE_LIST + "_" + projectId;
//        report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
        ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1, 30);
        ModelAndView view = new ModelAndView("otherProject/project_detail");
        //判断发起人类型
        ApiFrontUser afu = userFacade.queryById(project.getUserId());
        if(afu != null){
        	view.addObject("faqiren_type", afu.getUserType());
        }
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
        if (project != null)
        {
            project.setContent(StringUtil.convertToHtml(project.getContent()));
            process = 0.0;
            if (project.getCryMoney() >= 0.001)
            {
                process = project.getDonatAmount() / project.getCryMoney();
            }
            view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0));
            //为百分比显示
            view.addObject("processbfb", StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0));
            if (userId != null && project.getUserId().equals(userId))
            {
                // 是否是项目发起人
                view.addObject("owner", true);
            }
            ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
            userInfo.setProjectId(projectId);
//            key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
//            userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
            List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
            for (ApiProjectUserInfo u : userInfos)
            {
                if (u.getPersonType() == 0)
                {
                    view.addObject("shouzhu", u);
                }
                else if (u.getPersonType() == 1)
                {
                    view.addObject("zhengming", u);
                }
                else if (u.getPersonType() == 2)
                {
                    view.addObject("fabu", u);
                }
            }
        }
        if (reports != null && reports.getTotal() > 0)
        {
            view.addObject("reports", reports.getResultData());
        }
        //企业助善信息
        ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
        good_help.setProject_id(projectId);
        good_help.setOrderBy("createTime");
        good_help.setOrderDirection("desc");
        good_help.setState(203);
        List<String> list = new ArrayList<String>(1);
        list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
        good_help.initCache(true, DateUtil.DURATION_MIN_S, list, "project_id", "state");
        ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade.queryCompanyGoodHelpListByCondition(good_help, 1, 4);
        if (goodHelps != null && goodHelps.getTotal() > 0)
        {
            view.addObject("goodhelps", goodHelps.getResultData());
            view.addObject("firstgh", goodHelps.getResultData().get(0));
        }
        String userType = SSOUtil.getCurrentUserType(request, response);
        if (ProjectConstant.PROJECT_STATUS_COLLECT == project.getState())
        {
            if ("enterpriseUsers".equals(userType) && process < 1 && project.getState() == 240 && userId != null)
            {
                if (goodHelps == null || goodHelps.getTotal() == 0)
                {
                    ApiCompany apiCompany = new ApiCompany();
                    apiCompany.setUserId(userId);
                    apiCompany.setState(203);
                    List<String> llist = new ArrayList<String>(1);
                    llist.add(ApiCompany.getCacheRange(apiCompany.getClass().getName(), BaseBean.RANGE_WHOLE, userId));
                    apiCompany.initCache(true, DateUtil.DURATION_MIN_S, llist, "userId", "state");
                    apiCompany = companyFacade.queryCompanyByParam(apiCompany);
                    if (apiCompany != null)
                        view.addObject("isComapny", true);
                }
            }
            else if (userType == null)
            {
                view.addObject("isComapny", true);
            }
        }
        //最新捐款列表
        
        ApiDonateRecord r = new ApiDonateRecord();
        Date edate = new Date();
        Date bdate = DateUtil.add(edate, -12 * 30);
        r.setQueryStartDate(bdate);
        r.setQueryEndDate(edate);
        r.setProjectId(projectId);
        r.setState(302);
        List<String> llist = new ArrayList<String>(1);
        llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
        r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId");
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, 1, 10);
        List<ApiDonateRecord> dlist = donats.getResultData();
        for (ApiDonateRecord dr : dlist)
        {
            if (!StringUtils.isEmpty(dr.getUserName()) && dr.getUserName().contains("游客"))
            {
                if (!StringUtils.isEmpty(dr.getTouristMessage()))
                {
                    JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
                    String name = dataJson.getString("name");
                    if (!StringUtils.isEmpty(name))
                    {
                        dr.setNickName(name);
                        
                    }
                    
                }
                
            }
        }
        view.addObject("donates", dlist);
        view.addObject("peopleNum", donats.getTotal());
        //新闻列表
        ApiNews apiNews = new ApiNews();
        apiNews.setNews_column("爱心故事");
        apiNews.initNormalCache(true, DateUtil.DURATION_MIN_S, PengPengConstants.PROJECT_NEWS_LIST);
        ApiPage<ApiNews> news = newsFacade.queryNewsList(apiNews, 1, 5);
        view.addObject("loveNews", news.getResultData());
        
        //查看凭证
        view.addObject("itemType",itemType);
        
    	/*点击量统计start*/
        try
        {
    		String clickRate = (String) redisService.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_PC+projectId);
    		long startTime = (DateUtil.getCurrentDayEnd().getTime() - new Date().getTime())/1000; 
    		if(StringUtils.isEmpty(clickRate))
    		{
    			redisService.saveObjectData(PengPengConstants.PROJECT_CLICKRATE_PC+projectId, "t_pc_"+projectId+"_"+1,startTime);
    		}
    		else
    		{
    			String cstr[] = clickRate.split("_");
  
    			if(null != cstr && cstr.length >= 4)
    			{
    				Integer click = Integer.parseInt(cstr[3]);
    				if(null == click)
    				{
    					click = 0 ;
    				}
    				else
    				{
    					click += 1 ;
    				}
    				
    				redisService.saveObjectData(PengPengConstants.PROJECT_CLICKRATE_PC+projectId, "t_pc_"+projectId+"_"+click,startTime);
    			}
    		}
        }
        catch(Exception e)
        {
        	
        }

		/*点击量统计end*/
        
        return view;
    }
    
    
    /**
     * 新项目列表
     * @param from
     * @return
     */
    @ResponseBody
    @RequestMapping("list_person")
    public JSONObject list2(ProjectForm from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        // 把取到的善园项目封装成json数据
        try
        {

            ApiProject ap = new ApiProject();
            if(!"0".equals(from.getLocation())&&!StringUtils.isEmpty(from.getLocation())){
            	ap.setLocation(from.getLocation());
            }
            ap.setIsHide(0);
            //排序的处理
            if(from.getSortType() == null || from.getSortType() == 0)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("registrTime");
            }
            else if(from.getSortType() == 2)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donationNum");
            }
            else if(from.getSortType() == 3)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastFeedbackTime");
            }
            else if(from.getSortType() == 1)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donatAmount");
            }
            else if(from.getSortType() == 5)
            {
            	ap.setOrderDirection("asc");
            	ap.setOrderBy("donatAmount");
            }
            else if(from.getSortType() == 6)
            {
            	ap.setOrderDirection("asc");
            	ap.setOrderBy("donationNum");
            }
            else if(from.getSortType() == 7)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastFeedbackTime");
            }
            //类别的处理
            if (StringUtils.isNotEmpty(from.getTypeName())&&!"0".equals(from.getTypeName()))
            {
                ap.setField(from.getTypeName());
            }
            //状态的处理
            if(from.getStatus() == null) {
            	List<Integer> states = new ArrayList<Integer>();
            	states.add(ProjectConstant.PROJECT_STATUS_COLLECT);
            	states.add(ProjectConstant.PROJECT_STATUS_DONE);
            	ap.setStates(states);
			}else if (from.getStatus() == 1)
            {
                ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
            }
            else if (from.getStatus() == 2)
            {
                ap.setState(ProjectConstant.PROJECT_STATUS_DONE);
            }
            // 默认显示 募捐中  跟 已结束 
            else
            {
               // ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
            	List<Integer> states = new ArrayList<Integer>();
            	states.add(ProjectConstant.PROJECT_STATUS_COLLECT);
            	states.add(ProjectConstant.PROJECT_STATUS_DONE);
            	ap.setStates(states);
            }
            if(from.getUserID() > 0 ){
            	ap.setUserId(from.getUserID());
            }
        	/*
            List<String> list = new ArrayList<String>(1);
            list.add(ApiProject.getCacheRange(ap.getClass().getName(), BaseBean.RANGE_WHOLE, ap.getField()));
            ap.initCache(false, DateUtil.DURATION_MIN_S, list, "field", "status");
            */
            ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, from.getPage(), from.getLen());
            
            List<ApiProject> projects = apiPage.getResultData();
            // 无数据
            if (projects.size() == 0)
            {
                data.put("result", 1);
            }
            else
            {
               	String donatePercent = "" ; 
               	double leaveCryAmount = 0.0 ;
               	double process = 0.0;
                for (ApiProject project : projects)
                {
                	leaveCryAmount = project.getCryMoney()-project.getDonatAmount();
                	leaveCryAmount = new BigDecimal(leaveCryAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
               		if(leaveCryAmount<0){
               			leaveCryAmount = 0.0 ;
               		}
               		 process = 0.0;
                        if (project.getCryMoney() >= 0.001)
                        {
                            process = project.getDonatAmount() / project.getCryMoney();
                        }
                        donatePercent = process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0);
                        
                    JSONObject item = new JSONObject();
                    item.put("itemId", project.getId());
                    item.put("type", project.getType());
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    item.put("donatAmount", project.getDonatAmount());
                    item.put("cryMoney", project.getCryMoney());
                    item.put("imageUrl", project.getCoverImageUrl());
                    item.put("donationNum", project.getDonationNum());
                    item.put("sex", project.getSex());
                    item.put("age", project.getAge());
                    item.put("realName", project.getRealName());
                    item.put("familyAddress", project.getFamilyAddress());
                    item.put("publishTime", project.getRegistrTime());
                    item.put("state", project.getState());
                    item.put("leaveCryAmount", leaveCryAmount);
                    item.put("donatePercent", donatePercent);
                    item.put("publicName", project.getUname());
                    item.put("userType", project.getUserType());
                    item.put("workUnit", project.getWorkUnit()); 
                    item.put("isNeedVolunteer", project.getIsNeedVolunteer());
                    items.add(item);
                }
                data.put("items", items);
                data.put("page", apiPage.getPageNum());// 当前页码
                data.put("pageNum", apiPage.getPageSize()); // 每页行数
                data.put("total", apiPage.getPages());// 总页数
                data.put("result", 0);
            }
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    
    @RequestMapping("gylist")
    public ModelAndView gylist(HttpServletRequest request,
			HttpServletResponse response) throws JDOMException, IOException
    {
        ModelAndView view = new ModelAndView("otherProject/auction/gylist");
        Integer userId = UserUtil.getUserId(request, response);
    	//判断用户有没登录
    	
        String browser = UserUtil.Browser(request);
        String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String weixin_code = request.getParameter("code");
		request.getSession().setAttribute("weicode", weixin_code);
		if(userId == null){
			try {
				if ("".equals(openId) || openId == null) {
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					String kk = (String) request.getSession().getAttribute("weicode");
					Map<String, Object> mapToken = CommonUtils.getAccessTokenAndopenidRequest(kk);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					unionid = mapToken.get("unionid").toString();
					request.getSession().setAttribute("openId", openId);
				}
				ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
					 // 自动登录
				 SSOUtil.login(user, request, response);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
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
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
        return view;
    }
    
    
    @RequestMapping("gylistdata")
    @ResponseBody
    public Map<String,Object> gylistdata(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageNum", required = false, defaultValue = "15") Integer pageNum,HttpServletRequest request,
			HttpServletResponse response)
    {
    	Integer userId = UserUtil.getUserId(request, response);
    	List<Integer> states = new ArrayList<Integer>(4);
    	states.add(201);
    	states.add(202);
    	states.add(203);
    	states.add(204);
    	ApiAuctionProject auction = new ApiAuctionProject();
    	auction.setIsHide(0);
    	auction.setStates(states);
    	auction.setBelong(userId);
        ApiPage<ApiAuctionProject> apg= auctionProjectFacade.queryAuctionProjectList(auction,page,pageNum);
        return webUtil.successRes(apg);
    }
    
    @RequestMapping("gydetail")
    public ModelAndView gydetail(@RequestParam(value = "id", required = true) Integer id,
    		HttpServletRequest request,
			HttpServletResponse response) throws JDOMException, IOException
    {
        ModelAndView view = new ModelAndView("otherProject/auction/gydetail");
        Integer userId = UserUtil.getUserId(request, response);
    	
        String browser = UserUtil.Browser(request);
        String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String weixin_code = request.getParameter("code");
		request.getSession().setAttribute("weicode", weixin_code);
		//判断用户有没登录
    	if(userId == null){
			try {
				if ("".equals(openId) || openId == null) {
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					String kk = (String) request.getSession().getAttribute("weicode");
					Map<String, Object> mapToken = CommonUtils.getAccessTokenAndopenidRequest(kk);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					unionid = mapToken.get("unionid").toString();
					request.getSession().setAttribute("openId", openId);
				}
				ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
				userId = user.getId();
				// 自动登录
				 SSOUtil.login(user, request, response);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
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
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
        ApiAuctionProject auction = auctionProjectFacade.queryAuctionProjectDetail(id);
        if(auction.getNumber() > 0 ){
        	view.addObject("price", auction.getCurrentPrice());
        	auction.setCurrentPrice(auction.getCurrentPrice()+10);
        }else{
        	view.addObject("price", auction.getReservePrice());
        	auction.setCurrentPrice(auction.getReservePrice());
        }
        view.addObject("status", 1);
        if(auction.getState() == 202){
        	ApiBid b = new ApiBid();
        	b.setAuction_project_id(auction.getId());
        	b.setState(201);
        	ApiPage<ApiBid> apiPage = auctionProjectFacade.queryBidList(b, 1, 1);
        	if(apiPage.getTotal() > 0){
        		b = apiPage.getResultData().get(0);
            	//判断用户有没登录
            	if(userId != null){
            		ApiFrontUser user = userFacade.queryById(userId);
            		if (user.getId().equals(b.getUserId())) {
            			view.addObject("status", 0);
					}
            	}
        	}
        }
        view.addObject("auction", auction);
        return view;
    }
    
    /**
     * 竞价的资格判断
     */
    @RequestMapping("gydoauction")
    @ResponseBody
    public Map<String,Object> gydoauction(@RequestParam(value = "id", required = true) Integer id,
    		@RequestParam(value = "price", required = false ,defaultValue ="0") Double price,
    		HttpServletRequest request,HttpServletResponse response)
    {
    	
    	Integer userId = UserUtil.getUserId(request, response);
    	//判断用户有没登录
    	if(userId == null){
    		return webUtil.loginFailedRes(null); 
    	}
    	ApiFrontUser user = userFacade.queryById(userId);
    	//判断用户有没实名
    	if(user.getMobileState() != 203 || StringUtils.isEmpty(user.getRealName())){
    		return webUtil.failedRes("1023","未实名", null); 
    	}
    	ApiAuctionProject auction = auctionProjectFacade.queryAuctionProjectDetail(id);
    	//判断竞拍是否正常
    	Date date = new Date();
    	if(auction.getState() != 201 || auction.getDeadline().getTime() < date.getTime()){
    		return webUtil.failedRes("1022","竞拍结束", null); 
    	}
    	//判断能否以当前的价格进行竞拍
        if (auction.getNumber() > 0) {
        	if(price <= auction.getCurrentPrice()){
	        	return webUtil.failedRes("1021","价格过低", null); 
	        }
		}else {
			 if(price < auction.getReservePrice()){
		        	return webUtil.failedRes("1021","价格过低", null); 
		       }
		}
        auction.setCurrentPrice(price);
        auction.setNumber(auction.getNumber() +1);
        auction.setUserId(userId);
        ApiResult res = auctionProjectFacade.updateAuctionProject(auction);
        if(res.getCode() == 1){
        	return webUtil.successRes(null);
        }else {
        	return webUtil.failedRes("1020", "", null);
		}
        
    }
    
    @RequestMapping("gyauction")
    public ModelAndView gyauction(@RequestParam(value = "id", required = true) Integer id,
    		@RequestParam(value = "price", required = false) Integer price,
    		HttpServletRequest request,
			HttpServletResponse response) throws JDOMException, IOException
    {
    	
        ModelAndView view = new ModelAndView("otherProject/auction/gyauction");
        Integer userId = UserUtil.getUserId(request, response);
        String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String weixin_code = request.getParameter("code");
		request.getSession().setAttribute("weicode", weixin_code);
		//判断用户有没登录
    	if(userId == null){
			try {
				if ("".equals(openId) || openId == null) {
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					String kk = (String) request.getSession().getAttribute("weicode");
					Map<String, Object> mapToken = CommonUtils.getAccessTokenAndopenidRequest(kk);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					request.getSession().setAttribute("openId", openId);
					unionid = mapToken.get("unionid").toString();
				}
				ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
					 // 自动登录
				 SSOUtil.login(user, request, response);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
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
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
        ApiAuctionProject auction = auctionProjectFacade.queryAuctionProjectDetail(id);
        //判断能否以当前的价格进行竞拍
        if(auction.getNumber() > 0 ){
        	view.addObject("price", auction.getCurrentPrice());
        	auction.setCurrentPrice(auction.getCurrentPrice()+10);
        }else{
        	view.addObject("price", auction.getReservePrice());
        	auction.setCurrentPrice(auction.getReservePrice());
        }
        if(auction.getState() == 202){
        	ApiBid b = new ApiBid();
        	b.setAuction_project_id(auction.getId());
        	b.setState(201);
        	ApiPage<ApiBid> apiPage = auctionProjectFacade.queryBidList(b, 1, 1);
        	if(apiPage.getTotal() > 0){
        		b = apiPage.getResultData().get(0);
            	//判断用户有没登录
            	if(userId != null){
            		ApiFrontUser user = userFacade.queryById(userId);
            		if (user.getId().equals(b.getUserId())) {
            			view.addObject("status", 0);
					}
            	}
        	}
        }
        Date date = new Date();
        view.addObject("nowtime", date.getTime());
        view.addObject("endtime", auction.getDeadline().getTime());
        view.addObject("auction", auction);
        return view;
    }
    
    
    @RequestMapping("gyauctiondata")
    @ResponseBody
    public Map<String,Object> gyauctiondata( @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageNum", required = false, defaultValue = "15") Integer pageNum)
    {
    	ApiBid bid = new ApiBid();
        bid.setAuction_project_id(id);
        bid.setState(201);
        ApiPage<ApiBid>   apg= auctionProjectFacade.queryBidList(bid, page, pageNum);
        return webUtil.successRes(apg);
    }
    
    @RequestMapping("gyrealname")
    public ModelAndView gyrealname(@RequestParam(value = "auctionId", required = false) Integer auctionId,
    		HttpServletRequest request,
			HttpServletResponse response)
    {
        ModelAndView view = new ModelAndView("otherProject/auction/gyverify");
        view.addObject("auctionId", auctionId);
        return view;
    }
    
    @RequestMapping("gyresult")
    @ResponseBody
    public Map<String,Object> gyresult(HttpServletRequest request,
			HttpServletResponse response,PUser user)
    {
        String phonekey = CodeUtil.certificationprex+user.getPhone();
    	StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
    	int codeR = CodeUtil.VerifiCode(phonekey, storeManage, user.getPhoneCode(), false);
    	if(codeR==-1){
    		return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码过期", null); 
    	}else if(codeR==0){
    		return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码错误", null); 
    	}
    	
    	Integer userId = UserUtil.getUserId(request, response);
    	//判断用户有没登录
    	if(userId == null){
    		return webUtil.loginFailedRes(null); 
    	}
    	ApiFrontUser user1 = userFacade.queryById(userId);
    	user1.setMobileNum(user.getPhone());
    	user1.setRealName(user.getRealname());
    	user1.setMobileState(203);//手机验证通过
    	userFacade.resetFrontUser(user1);
    	
        return webUtil.successRes(null);
    }
    
    @RequestMapping("nowtime")
    @ResponseBody
    public Map<String,Object> nowtime()
    {
    	Date date = new Date();
        return webUtil.successRes(date);
    }
    
    @RequestMapping("launchDonateView")
    public ModelAndView launchDonateView(HttpServletRequest request,
			HttpServletResponse response){
    	ModelAndView view =new ModelAndView();
    	//校验用户是否登录
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId == null){
    		view = new ModelAndView("redirect:/user/sso/login.do");
    		view.addObject("entrance", "http://www.17xs.org/project/launchDonateView.do");
			/*view.addObject("errormsg", "先登陆");
			view.addObject("errorcode", -1);*/
			return view;
    	}
    	ApiFrontUser user = userFacade.queryById(userId);
    	String MobileNum="";
    	if(user.getMobileNum()!=null)
    		MobileNum=user.getMobileNum().toString();
    	List<ApiTypeConfig> categories = commonFacade.queryList();
    	view =new ModelAndView("monthlyDonate/launchDonate_add");
    	view.addObject("atc", categories);
    	view.addObject("MobileNum", MobileNum);
    	return view;
    }
    
    @RequestMapping("launchDonate")
    @ResponseBody
    public  Map<String, Object> launchDonate(HttpServletRequest request,
			HttpServletResponse response,ApiDonateTime dt){
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(dt == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
    	ApiFrontUser user = userFacade.queryById(userId);
		dt.setUserId(userId);
		if(dt.getMoney() > user.getBalance()) {
			dt.setState(200);
			ApiResult result = donateRecordFacade.saveDonateTime(dt);
			Map<String,Object> r = webUtil.failedRes("0007", "您的余额不足！", result.getData());
			return r;
		}
		dt.setState(201);
		ApiResult result = donateRecordFacade.saveDonateTime(dt);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "日捐月捐发起失败！", null);
		}
		else
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
			
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
    }
}
