package com.guangde.home.controller.project;

import java.io.IOException;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiRedpackets;
import com.guangde.entry.ApiRedpacketspool;
import com.guangde.entry.ApiReport;
import com.guangde.entry.ApiSystemNotify;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.ApiUser_Redpackets;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.home.vo.project.Appeal;
import com.guangde.home.vo.project.Donation;
import com.guangde.home.vo.project.PFeedBack;
import com.guangde.home.vo.project.ProjectForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;
/*
 * h5页面的显示控制
 */
@Controller
@RequestMapping("project")
public class H5OtherProjectController {
	Logger logger = LoggerFactory.getLogger(H5OtherProjectController.class);

	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IUserFacade  userFacade;

	@Autowired
	private ISystemNotifyFacade systemNotifyFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	
	@RequestMapping("otherIndex_h5")
	public ModelAndView index_h5() {
		ModelAndView view = new ModelAndView("h5/other/index");
		List<ApiTypeConfig> atc = commonFacade.queryList();
        view.addObject("atc", atc);
        view.addObject("size", atc.size());
        //对于页面要显示的捐款总金额，需要补0，同时只到亿位
  		int w = 9;
  		Object obj = redisService.queryObjectData(PengPengConstants.DONATION_TOTAL_MONEY);
  		if(obj != null){
  			view.addObject("totalMoney", Integer.valueOf(obj.toString()));
  		}else{
  			Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
  			String totalMoney = String.valueOf(Itotal);
  			if(totalMoney.length() < w){
  				int size = w - totalMoney.length();
  				for (int i = 0; i < size; i++) {
  					totalMoney="0"+totalMoney;
  				}
  			}
  			redisService.saveObjectData(PengPengConstants.DONATION_TOTAL_MONEY, totalMoney, DateUtil.DURATION_MIN_S);
  			view.addObject("totalMoney", Itotal);
  		}
		List<ApiBFile> bfiles = null;
		ApiBFile apiBfile = new ApiBFile(); 
		apiBfile.setCategory("banner");
		bfiles = commonFacade.queryApiBfile(apiBfile);
		view.addObject("bannerList", bfiles);
		return view;
	}
	
	/*
	 * @param projectId 项目ID 显示项目详情
	 */
	@RequestMapping("otherview_h5")
	public ModelAndView view(
			@RequestParam(value = "projectId") Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException {
		
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		ModelAndView view = new ModelAndView("h5/project/project_detail");
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
		if (project != null) {
			view.addObject("desc", StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
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
		
		view.addObject("projectId",projectId);
		view.addObject("extensionPeople",extensionPeople);
		view.addObject("browser",browser);
		
		String itemType = request.getParameter("itemType")==null?"":request.getParameter("itemType");
		view.addObject("itemType",itemType);
		
		return view;
	}
	
	
	
	/**
	 * 善园项目详情
	 * @param projectId
	 * @param request
	 * @param response
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping("othergardenview_h5")
	public ModelAndView gardenview(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException {
		
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		ModelAndView view = new ModelAndView("h5/garden/garden_project_detail");
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			view.addObject("desc", StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
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
		Date edate = new Date();
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true,DateUtil.DURATION_MIN_S,llist,"projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		view.addObject("peopleNum", donats.getTotal());
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
		view.addObject("projectId",projectId);
		return view;
	}
	

	
	@RequestMapping("otherview_list_h5")
	public ModelAndView view_list_h5(ProjectForm from,
			HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView("h5/other/project_list");
		List<ApiTypeConfig> atc = commonFacade.queryList();
        view.addObject("atc", atc);
        view.addObject("field", from.getField());
		return view;
	}
	
	@RequestMapping("otherview_paysuccess_h5")
	public ModelAndView view_paysucess_h5(DepositForm from,@RequestParam(value="red",required=false) Integer red,
			@RequestParam(value="uredId",required=false)Integer uredId,@RequestParam(value="fromWish",required=false) String fromWish,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException{
		
		ModelAndView view = new ModelAndView("h5/sharePay_success");
		ApiUser_Redpackets ur= redPacketsFacade.queryByRedPaperTradeNo(uredId); 
		if(ur != null && StringUtils.isNotEmpty(ur.getTradeNo())){
			from.setTradeNo(ur.getTradeNo());
		}
		view.addObject("title", from.getpName());
		view.addObject("amount", from.getAmount());
		view.addObject("nickName", from.getNickName());
		view.addObject("headImage", from.getHeadImage());
		view.addObject("projectId", from.getProjectId());
		view.addObject("tradeNo", from.getTradeNo());
		view.addObject("extensionPeople", from.getUserId());
		
		if(StringUtils.isNotEmpty(from.getTradeNo())){
			ApiDonateRecord record = donateRecordFacade.queryWeixinToPayNoticeByTranNum(from.getTradeNo());
			ApiMatchDonate md = new ApiMatchDonate();
			md.setRecordID(record.getId());
			md = redPacketsFacade.queryMatchDonateByParam(md);
			if(md != null && StringUtils.isNotEmpty(md.getCompanyname())){
				view.addObject("isMatchDonate", 1);
				view.addObject("matchDonate", md);
			}
		}else {
			view.addObject("isMatchDonate", 0);
		}
		boolean flag = false;
		boolean result = false;
		if(StringUtils.isNotEmpty(fromWish)&&fromWish.equals("fromWish")){
			flag = false;
			view.addObject("flag", flag);
			view.addObject("red", 1);
			view.addObject("result", false);
		}else {
			//判断是否是红包支付
			if(red != null &&red == 1){
				view.addObject("red", red);
			}else {
				view.addObject("red", 0);
			}
			//判断是否指定项目
			flag = CommonUtils.IsQueryRedPacket(from.getProjectId(),redPacketsFacade);
			view.addObject("flag", flag);
			if(red == null && flag ==true){
				ApiRedpackets apiRedpacket = new ApiRedpackets();
				apiRedpacket.setAppointproject(String.valueOf(from.getProjectId()));
				ApiPage<ApiRedpackets> redpackets = redPacketsFacade.queryByParam(apiRedpacket, 1, 20);
				List<Integer> redpacketIds = new ArrayList<Integer>();
				for(ApiRedpackets ar:redpackets.getResultData()){
					redpacketIds.add(ar.getId());
				}
				if(redpacketIds.size() > 0){
					ApiRedpacketspool aRp = new ApiRedpacketspool();
					aRp.setRedpackesIds(redpacketIds);
					result = redPacketsFacade.isNotRedPackets(aRp);
				}
				
			}
			
			view.addObject("result", result);
		}
		
		if(from.getUserId() == null){
			Integer userId = UserUtil.getUserId(request, response);
			if(userId == null || userId == 0)
			{
				ApiDonateRecord donat = donateRecordFacade.queryPayNoticeByTranNum(from.getTradeNo());
				if(donat != null)
				{
					userId = donat.getUserId();
				}
			}
			from.setUserId(userId);
		}
		if(from.getHeadImage() == null || from.getNickName() == null){
			ApiFrontUser user = userFacade.queryById(from.getUserId());
			view.addObject("nickName", user.getNickName() == null ? "" :user.getNickName());
			view.addObject("headImage", user.getCoverImageUrl() == null ? "" : user.getCoverImageUrl());
		}
		String browser = UserUtil.Browser(request);
		if(browser.equals("wx")){
			if (from.getUserId() != null &&red == null && flag ==true && result == true) {
				ApiUser_Redpackets apiUR = new ApiUser_Redpackets();
				apiUR.setUserId(from.getUserId());
				apiUR.setExtensionpeople(from.getUserId());
				apiUR.setSpare(from.getTradeNo());
				Integer count = redPacketsFacade.countRedPaperNumByCondition(apiUR);
				if(count == 0){///取红包
					CommonUtils.IsQueryRedPacket(from.getUserId(), from.getProjectId(),from.getTradeNo(),redPacketsFacade);
				}
			}
			
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
		return view;
		
		/*
		if(from.getExtensionPeople() != null && from.getExtensionPeople() !=0)
		{
			ModelAndView view = new ModelAndView("h5/pay_success");
			view.addObject("title", from.getpName());
			view.addObject("amount", from.getAmount());
			return view;
		}
		else
		{
			ModelAndView view = new ModelAndView("h5/sharePay_success");
			view.addObject("title", from.getpName());
			view.addObject("amount", from.getAmount());
			return view;
		}
		*/
	}
	
	@RequestMapping("othertest_sharepaysuccess_h5")
	public ModelAndView test_sharepaysucess_h5(DepositForm from,
			HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView view = new ModelAndView("h5/sharePay_success");
		view.addObject("title", from.getpName());
		view.addObject("amount", from.getAmount());
		view.addObject("nickName", from.getNickName());
		view.addObject("headImage", from.getHeadImage());
		view.addObject("projectId", from.getProjectId());
		view.addObject("tradeNo", from.getTradeNo());
		
		return view;
		
	}
	
		
	 /*
     * 查询最新捐款记录
     * @return 最新的捐款记录
     */
    @ResponseBody
    @RequestMapping("otherlatestdonationlist")
    public  Map<String,Object> latestdonationlist(
    		@RequestParam(value = "projectTitle",required =false) String projectTitle,
    		@RequestParam(value = "page",required =false, defaultValue = "1") Integer page,
    		@RequestParam(value = "pageSize",required =false, defaultValue = "20" ) Integer pageSize)
    {
        ApiDonateRecord r = new ApiDonateRecord();
        if(StringUtils.isNotEmpty(projectTitle)){
        	r.setProjectTitle(projectTitle);
        }
        r.setState(302);
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryLatestDonateRecordList(r, page, pageSize);
        List<ApiDonateRecord> list = donats.getResultData();
        for(ApiDonateRecord adr:list){
        	if (adr.getTouristMessage() != null) {
				JSONObject json = JSON.parseObject(adr.getTouristMessage());
				if (StringUtils.isNotEmpty(json.getString("name"))) {
					adr.setNickName(json.getString("name"));
				}
			}
        }
        donats.setResultData(list);
        return webUtil.successRes(donats);
    }
	
	/*
	 * @param projectId 项目ID 显示项目详情
	 */
	@RequestMapping("otherview_test_h5")
	public ModelAndView viewtest(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException {
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		ModelAndView view = new ModelAndView("h5/project/project_detail_test");
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			view.addObject("desc", StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
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
		Date edate = new Date();
		Date bdate = DateUtil.add(edate, -12 * 30);
		r.setQueryStartDate(bdate);
		r.setQueryEndDate(edate);
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true,DateUtil.DURATION_MIN_S,llist,"projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		view.addObject("peopleNum", donats.getTotal());
		
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
		return view;
	}
	
  /**
   * h5 项目列表
   * @param from
   *  sortType
	 * 0： 最新发布（时间倒序）
	 * 1： 关注最多（捐款人最多）
	 * 2： 最早发布（时间顺序）
	 * 3： 最新反馈（根据求助人反馈的时间，最新的反馈排在最前）
	 * 4： 捐助最多
   * @return
   */
    @ResponseBody
    @RequestMapping("otherlist_h5")
    public JSONObject list(ProjectForm from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
     
        try
        {
            ApiProject ap = new ApiProject();
  
            ap.setIsHide(0);
            //排序的处理
            if(from.getSortType() == null || from.getSortType() == 0)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("registrTime");
            }
            else if(from.getSortType() == 1)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donationNum");
            }
            else if(from.getSortType() == 2)
            {
            	ap.setOrderDirection("asc");
            	ap.setOrderBy("lastUpdateTime");
            }
            else if(from.getSortType() == 3)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastFeedbackTime");
            }
            else if(from.getSortType() == 4)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donatAmount");
            }
            //类别的处理
            if (StringUtils.isNotEmpty(from.getTypeName()))
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
            	ap.setStates(states);
            }
            
            
            
//            List<String> list = new ArrayList<String>(1);
//            list.add(ApiProject.getCacheRange(ap.getClass().getName(), BaseBean.RANGE_WHOLE, ap.getField()));
//            ap.initCache(false, DateUtil.DURATION_MIN_S, list, "field", "status");
            
            ap.setUserId(21976);
            
            ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, from.getPage(), from.getLen());
            
            List<ApiProject> projects = apiPage.getResultData();
            // 无数据
            if (projects.size() == 0)
            {
                data.put("result", 1);
            }
            else
            {
                for (ApiProject project : projects)
                {
                    JSONObject item = new JSONObject();
                    item.put("itemId", project.getId());
                    item.put("field", project.getField());
                    item.put("type", project.getType());
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    item.put("donaAmount", project.getDonatAmount());
                    item.put("cryMoney", project.getCryMoney());
                    item.put("process", StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0));
                    item.put("imageurl", project.getCoverImageUrl());
                    item.put("endtime", new Date());
                    item.put("state", project.getState());
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
    
    /**
	 * 推荐项目列表
	 * @param from
	 * @return
	 */
    @ResponseBody
    @RequestMapping("otherrecommendList_h5")
    public JSONObject recommentList(ProjectForm from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        // 把取到的善园项目封装成json数据
        try
        {
            ApiProject ap = new ApiProject();
            ap.setOrderDirection("desc");
            ap.setIsHide(0);
            ap.setIsRecommend(1);
            from.setPage(1);
            from.setLen(2);
            ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, from.getPage(), from.getLen());
            List<ApiProject> projects = apiPage.getResultData();
            
            if (projects.size() == 0){
            	ap = new  ApiProject();
            	ap.setIsHide(0);
            	ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
            	apiPage = projectFacade.queryProjectList(ap, from.getPage(), from.getLen());
            	projects = apiPage.getResultData();
            }
            
            
            // 无数据
            if (projects.size() == 0)
            {
                data.put("result", 1);
            }
            else
            {
                for (ApiProject project : projects)
                {
                    JSONObject item = new JSONObject();
                    item.put("itemId", project.getId());
                    item.put("field", project.getField());
                    item.put("type", project.getType());
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    item.put("donaAmount", project.getDonatAmount());
                    item.put("cryMoney", project.getCryMoney());
                    item.put("imageurl", project.getCoverImageUrl());
                    item.put("endtime", new Date());
                    item.put("state", project.getState());
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
    
    
    
    /**
     * 我的求救
     * @param request
     * @param response
     * @param page
     * @param pageNum
     * @return
     */
    @RequestMapping("otherappeallist_h5")
    @ResponseBody
    public Map<String, Object> appeallist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum)
    {
        // 1.验证是否登入
        String uid = SSOUtil.verifyAuth(request, response);
        if (uid == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        List<Appeal> list = new ArrayList<Appeal>();
        p.setData(list);
        ApiProject ap = new ApiProject();
    	List<Integer> states = new ArrayList<Integer>();
    	states.add(ProjectConstant.PROJECT_STATUS_COLLECT);
    	states.add(ProjectConstant.PROJECT_STATUS_DONE);
    	ap.setStates(states);
        ap.setUserId(new Integer(uid));
        
        ApiPage<ApiProject> projects = projectFacade.queryProjectList(ap, p.getPage(), p.getPageNum());
        Appeal pp = null;
        if (projects != null)
        {
            for (ApiProject project : projects.getResultData())
            {
                pp = new Appeal();
                pp.setId(project.getId());
                pp.setStatus(project.getState());
                pp.setTitle(project.getTitle());
                pp.setuTime(project.getLastUpdateTime());
                pp.setField(project.getField());
                pp.setFieldChinese(project.getFieldChinese());
                pp.setCryMoney(project.getCryMoney());
                pp.setDonatAmount(project.getDonatAmount());
                pp.setPanyAmount(project.getPanyAmount());
                pp.setcTime(project.getRegistrTime().getTime());
                String percent = StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0);
                if(percent==null||percent.equals("null")){
                	percent = "0";
                }
                pp.setDonatePercent(percent);
                list.add(pp);
            }
            p.setNums(projects.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "受捐列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    /**
     * 我的捐助
     * @param request
     * @param response
     * @param type
     * @param page
     * @param pageNum
     * @param userId
     * @return
     */
    @RequestMapping("otherdonationlist_h5")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId,
        @RequestParam(value = "pstate", required = false) Integer pstate)
    {
        
        if (userId == null)
        {
            // 1.验证是否登入
            userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
                return webUtil.loginFailedRes(null);
            }
        }

        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        
        ApiDonateRecord r = new ApiDonateRecord();
        r.setUserId(userId);
        r.setState(302);
        r.setPstate(pstate);
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryMyDonateRecordList(r, p.getPage(), p.getPageNum());
        Donation d = null;
        if (donats != null &&donats.getResultData().size()>0)
        {
            for (ApiDonateRecord donat : donats.getResultData())
            {
            	d = new Donation();
                d.setdMoney(donat.getTotalAmount()== null ?0.0:donat.getTotalAmount()); //捐款金额
                d.setCryMoney(donat.getCryMoney() == null ?0.0:donat.getCryMoney()); //求助金额
                d.setDonatAmountpt(donat.getDonatAmountpt()==null?0.0:donat.getDonatAmountpt()); //已募捐金额
                d.setdTime(donat.getDonatTime());   //捐款时间
           
                d.setPid(donat.getProjectId());  //项目id
           
                d.setTitle(donat.getProjectTitle()); //项目名称
                d.setStatus(donat.getPstate()==null?300:donat.getPstate());  //项目状态
                d.setField(donat.getField()==null?"":donat.getField());
                
                d.setImagesurl(donat.getCoverImageurl());
                if(donat.getDonatType().equals("enterpriseDonation")){
                	d.setdType(1); //助善
                }else{
                	d.setdType(2);//捐款
                }
                d.setDonateNum(donat.getDonateNum());
                d.setDonationNum(donat.getDonationNum());
                d.setName(donat.getUserName());
                list.add(d);
            }
            p.setNums(donats.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "捐款列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
	
  /**
   * 客服代发
   * @param name
   * @param qq
   * @param phone
   * @return
   */
    @ResponseBody
    @RequestMapping("otherappealByCustom_h5")
    public Map<String, Object> appealByCustom(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "qq", required = false) String qq,
        @RequestParam(value = "phone", required = true) String phone,@RequestParam(value = "reasion", required = true) String reasion)
    {
        JSONObject personData = new JSONObject();
        personData.put("name", name);
        personData.put("qq", qq);
        personData.put("phone", phone);
        personData.put("reasion", reasion);
        ApiSystemNotify apiSystemNotify = new ApiSystemNotify();
        apiSystemNotify.setState(0);
        apiSystemNotify.setUserId(-1);
        apiSystemNotify.setSubject("客服协助");
        apiSystemNotify.setSender("客服协助");
        apiSystemNotify.setContent(personData.toString());
        apiSystemNotify.setIsShow(0);
        ApiResult rt = systemNotifyFacade.save(apiSystemNotify);
        if (rt != null && rt.getCode() == 1)
        {
            return webUtil.successRes(1);
        }
        else
        {
            return webUtil.failedRes("0101", "客服代发提交，没有成功", -1);
        }
        
    }



	@RequestMapping("otherview_feedback_h5")
	public ModelAndView view_feedback_h5(ProjectForm from,
			HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView view = new ModelAndView("h5/project/feedback");
        view.addObject("projectId", from.getItemId());
       
		return view;
	}
    
	/**
     * 
     * H5的项目反馈
     *      * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "otherH5careProjectList")
    @ResponseBody
    public Map<String, Object> careProjectList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "projectId", required = false) Integer projectId)
    {
          Page p = new Page();
          p.setPage(page);
          p.setPageNum(pageNum);
          List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
          p.setData(feedbacks);
          
          ApiProjectFeedback feedBack = new ApiProjectFeedback();
          feedBack.setAuditState(203);
          feedBack.setProjectId(projectId);
          
          ApiPage<ApiProjectFeedback> result = projectFacade.queryH5ProjectFeedbckByCondition(feedBack, p.getPage(), p.getPageNum());
         
          if (result != null)
          {
              PFeedBack tempf = null;
              for (ApiProjectFeedback f : result.getResultData())
              {
                  tempf = new PFeedBack();
                  tempf.setId(f.getId());
                  tempf.setContent(f.getContent());
                  if (f.getFeedbackTime() != null)
                  {
                      tempf.setcTime(f.getFeedbackTime().getTime());
                  }
                  tempf.setImgs(f.getContentImageUrl());
                  tempf.setPid(f.getProjectId());
                  tempf.setuName(f.getUserName());
                  tempf.setUserType(f.getUserType());
                  tempf.setTitle(f.getTitle());
                  tempf.setUserImageUrl(f.getHeadImageUrl());
                  tempf.setPid(f.getProjectId());
                 // tempf.setSource(f.getSource());
                  feedbacks.add(tempf);
              }
              p.setNums(result.getTotal());
          }
          else
          {
              p.setNums(0);
          }
          if (p.getTotal() == 0)
          {
              return webUtil.resMsg(2, "0002", "没有数据", p);
          }
          else
          {
              return webUtil.successRes(p);
          }	
    } 
    /**
     * 分享项目页
     * @param projectId
     * @param extensionPeople
     * @param request
     * @param response
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping("otherview_share_h5")
	public ModelAndView view_share_h5(@RequestParam(value = "projectId",required = false) Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "shareType", required = false,defaultValue="1") Integer shareType,
			HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException{
		
    	ModelAndView view = new ModelAndView("h5/project/project_detail_share");
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		ApiFrontUser extensionUser = new ApiFrontUser();//分享链接用户
		
		  //验证是否登入
        Integer  loginUserId = UserUtil.getUserId(request, response);
        if(loginUserId != null && loginUserId != 0)
        {
     	  user = userFacade.queryById(loginUserId);
     	  view.addObject("user", user);
        }
        else
        {
        	/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
        	System.out.println("view_share_h5 browser >> "+browser+"  shareType >> "+shareType);
        	if(browser.equals("wx") && shareType !=null){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
        			 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("view_share_h5 >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
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
        				System.out.println("view_share_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			logger.error("view_share_h5  >> "+ e);
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
        		
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		
		
		if (extensionPeople != null) 
		{
			CookieManager.create(CookieManager.PROJECTID_EXTENSIONPEOPLE, projectId+"_"+extensionPeople, CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			extensionUser = userFacade.queryById(extensionPeople);
			view.addObject("extensionUser", extensionUser);
          
		}
		else 
		{
			CookieManager.create(CookieManager.PROJECTID_EXTENSIONPEOPLE, projectId+"_"+user.getId(), CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			view.addObject("extensionUser", user);
		}
		
		view.addObject("user", user);
		
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);

		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			view.addObject("desc", StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
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

		}

		//邀请的朋友捐款记录
		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		if (extensionPeople != null && extensionPeople != 0) 
		{
			r.setExtensionPeople(extensionPeople);
		}
		else
		{
			r.setExtensionPeople(userId);
		}
		r.setState(302);
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, 1, 5);
		List<ApiDonateRecord> drList = new ArrayList<ApiDonateRecord>();
		drList = donats.getResultData();
		for(ApiDonateRecord dr :drList){
            if(!StringUtils.isEmpty(dr.getUserName()) && (dr.getUserName().contains("游客")||dr.getUserName().contains("weixin"))){
            	if (!StringUtils.isEmpty(dr.getTouristMessage())){
                    JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
                    //name = dataJson.getString("name");
                    dr.setCoverImageurl(dataJson.getString("headimgurl"));
                }
            	else if(dr.getCoverImageId() > 0)
                {
            		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
            		dr.setCoverImageurl(bFile.getUrl());
                }
            }else {
            	if(dr.getCoverImageId() >0)
            	{
            		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
            		dr.setCoverImageurl(bFile.getUrl());
            	}
			}
            if(StringUtils.isEmpty(dr.getCoverImageurl())){
            	dr.setCoverImageurl("http://www.17xs.org/res/images/detail/people_avatar.jpg");
            }
		}
		Integer total = donateRecordFacade.countNumQueryByCondition(r);
		double extensionDonateAmount = donateRecordFacade.countQueryByCondition(r);

		view.addObject("peopleNum", total); // 参与人次
		view.addObject("extensionDonateAmount", extensionDonateAmount); // 参与人捐款总额
		view.addObject("drList", drList); // 参与人
		
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
		view.addObject("projectId",projectId);
		view.addObject("shareType",shareType);
		return view;
	}
    
	   
    
    /**
     * 分享链接捐款记录
     * @param request
     * @param response
     * @param type
     * @param page
     * @param pageNum
     * @param userId
     * @return
     */
    @RequestMapping("otherextensionDonation")
    @ResponseBody
    public Map<String, Object> extensionDonation(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,@RequestParam(value = "projectId", required = false) Integer projectId)
    {

        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        
        ApiDonateRecord r = new ApiDonateRecord();
        r.setExtensionPeople(extensionPeople);
        r.setProjectId(projectId);
        r.setState(302);
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, p.getPage(), p.getPageNum());
        Donation d = null;
        if (donats != null)
        {
            for (ApiDonateRecord donat : donats.getResultData())
            {
            	d = new Donation();
                d.setdMoney(donat.getDonatAmount()); //捐款金额
                d.setCryMoney(donat.getCryMoney()); //求助金额
                d.setDonatAmountpt(donat.getDonatAmountpt()); //已募捐金额
                d.setdTime(donat.getDonatTime());   //捐款时间
           
                d.setPid(donat.getProjectId());  //项目id
           
                d.setTitle(donat.getProjectTitle()); //项目名称
                d.setStatus(donat.getPstate());  //项目状态
                d.setField(donat.getField());
              
                if(!StringUtils.isEmpty(donat.getUserName()) && (donat.getUserName().contains("游客")||donat.getUserName().contains("weixin"))){
                	if (!StringUtils.isEmpty(donat.getTouristMessage())){
                        JSONObject dataJson = (JSONObject)JSONObject.parse(donat.getTouristMessage());
                        d.setImagesurl(dataJson.getString("headimgurl"));
                    }
                	else if(donat.getCoverImageId() > 0)
                    {
                		ApiBFile bFile = fileFacade.queryBFileById(donat.getCoverImageId());
                		d.setImagesurl(bFile.getUrl());
                    }
                }else {
                	if(donat.getCoverImageId() >0)
                	{
                		ApiBFile bFile = fileFacade.queryBFileById(donat.getCoverImageId());
                		d.setImagesurl(bFile.getUrl());
                	}
    			}
                
                
                String name = donat.getNickName();
                if(!StringUtils.isEmpty(name) && name.contains("游客")){
                	if (!StringUtils.isEmpty(donat.getTouristMessage())){
                        JSONObject dataJson = (JSONObject)JSONObject.parse(donat.getTouristMessage());
                        name = dataJson.getString("name");
                        if (StringUtils.isEmpty(name)){
                            name = "爱心人士";
                        }
                      
                    }
                	else
                	{
                		name="爱心人士";
                	}
                }
                
                if(donat.getDonatType().equals("enterpriseDonation")){
                	d.setdType(1); //助善
                }else{
                	d.setdType(2);//捐款
                }
                d.setDonateNum(donat.getDonateNum());
                d.setDonationNum(donat.getDonationNum());
                d.setName(name);
                d.setLeaveWord(donat.getLeaveWord()==null ? "" :donat.getLeaveWord());
                
				int d_value = DateUtil.minutesBetween(donat.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(donat.getDonatTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flag) {
					d.setShowTime(DateUtil.dateFormat(donat.getDonatTime()));
				} else {
					if (d_value / 60 >= 1) {
						d.setShowTime(d_value / 60 + "小时前");
					} else {
						if (d_value == 0) {
							d.setShowTime("刚刚");
						} else {
							d.setShowTime(d_value + "分钟前");
						}
					}
				}
                
                list.add(d);
            }
            p.setNums(donats.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "捐款列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    

    @RequestMapping("othersendWish")
    @ResponseBody
    public Map<String, Object> sendWish(@RequestParam(value = "tranNum",required = false) String tranNum,@RequestParam(value = "leaveWord",required = false) String leaveWord,
    		HttpServletRequest request, HttpServletResponse response)
    {
     

        if (tranNum == null)
        {
            return webUtil.resMsg(0, "0003", "交易号不能为空", null);
        }
        if (leaveWord == null)
        {
            return webUtil.resMsg(0, "0003", "祝福内容不能为空", null);
        }
        ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
        apiDonateRecord.setTranNum(tranNum);
        apiDonateRecord.setLeaveWord(leaveWord);
        int result = donateRecordFacade.updateLeaveWord(apiDonateRecord);

        if (result > 0)
        {
            return webUtil.resMsg(1, "0000", "成功", null);
        }
        else
        {
            return webUtil.resMsg(0, "0006", "更改失败", null);
        }
    }
    
	@RequestMapping("othertoSendWish")
	public ModelAndView toSendWish(
			@RequestParam(value = "tradeNo",required=false) String tradeNo,
			HttpServletRequest request, HttpServletResponse response)
		{
				ModelAndView view = new ModelAndView("h5/userRelation/sendWish");
				view.addObject("tradeNo",tradeNo);
				String projectId = request.getParameter("projectId")==null?"":request.getParameter("projectId");
				String headImage = request.getParameter("headImage")==null?"":request.getParameter("headImage");
				String nickName = request.getParameter("nickName")==null?"":request.getParameter("nickName");
				String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
				String title = request.getParameter("title")==null?"":request.getParameter("title");
				view.addObject("projectId",projectId);
				view.addObject("headImage",headImage);
				view.addObject("nickName",nickName);
				view.addObject("amount",amount);
				view.addObject("title",title);
				view.addObject("fromWish", "fromWish");
				return  view ;
		}
	
	@RequestMapping("otherfeedBackList_h5")
	public ModelAndView feedBackList(
			@RequestParam(value = "projectId",required=false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response)
		{
				ModelAndView view = new ModelAndView("h5/project/feedBackList");
				view.addObject("projectId",projectId);
				return  view ;
		}
	@RequestMapping("otherfeedBack_h5")
	public ModelAndView feedBack(
			@RequestParam(value = "projectId",required=false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response)
		{
				ModelAndView view = new ModelAndView("h5/project/feedBack_h5");
				view.addObject("projectId",projectId);
				return  view ;
		}
	@RequestMapping("othervolunteer")
	public ModelAndView volunteer(@RequestParam(value = "projectId",required=false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView view = new ModelAndView("h5/project/postulant");
		view.addObject("projectId",projectId);
		return  view ;
	}
	
    
}
