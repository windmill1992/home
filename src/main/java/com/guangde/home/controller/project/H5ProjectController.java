package com.guangde.home.controller.project;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.*;
import com.guangde.entry.*;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
/*
 * h5页面的显示控制
 */
@Controller
@RequestMapping("project")
public class H5ProjectController {
	Logger logger = LoggerFactory.getLogger(H5ProjectController.class);

	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IUserFacade userFacade;
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
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;

	/*
	 * @param projectId 项目ID 显示项目详情
	 */
	@RequestMapping("view_h5")
	public ModelAndView view(
			@RequestParam(value = "gotoType", defaultValue = "0", required = false) Integer gotoType,
			@RequestParam(value = "projectId") Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "slogans", required = false) String slogans,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);

		ModelAndView viewback = new ModelAndView(
				"redirect:/project/view.do?projectId=" + projectId);
		String isMobile = (String) request.getSession().getAttribute("ua");
		if ("mobile".equals(isMobile) && project.getSpecial_fund_id() != null
				&& project.getSpecial_fund_id() != 0 && extensionPeople != null) {// h5专项基金页面
			viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId="
					+ projectId + "&extensionPeople=" + extensionPeople);
			return viewback;
		}
		if ("mobile".equals(isMobile) && project.getSpecial_fund_id() != null
				&& project.getSpecial_fund_id() != 0 && extensionPeople == null) {// h5专项基金页面
			viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId="
					+ projectId);
			return viewback;
		}
		if (!"mobile".equals(isMobile) && extensionPeople != null) {
			viewback.setViewName("redirect:/project/view.do?projectId="
					+ projectId + "&extensionPeople=" + extensionPeople);
			return viewback;
		}
		if (!"mobile".equals(isMobile)) {
			return viewback;
		}
		ModelAndView view = new ModelAndView("h5/project/project_detail");
		if (("crowdfunding").equals(project.getField())) {
			view = new ModelAndView("h5/project/project_detail_crowdfunding");
		}
		if ("mobile".equals(isMobile) && project.getDaydayDonate() == 1
				&& extensionPeople != null) {// h5日捐页面
			viewback.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId="
					+ projectId + "&extensionPeople=" + extensionPeople);
			return viewback;
		}
		if ("mobile".equals(isMobile) && project.getDaydayDonate() == 1
				&& extensionPeople == null) {// h5日捐页面
			viewback.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId="
					+ projectId);
			return viewback;
		}
		// 用于跳转选项卡
		view.addObject("gotoType", gotoType);
		ApiOneAid oneAid = new ApiOneAid();
		oneAid.setProject_id(projectId);
		ApiPage<ApiOneAid> oneApiPage = projectFacade.queryOneAidByParam(
				oneAid, 1, 1);
		if ("mobile".equals(isMobile)
				&& !StringUtils.isBlank(project.getJumbleSale())) {// 义卖
			view.addObject("type", 1);
			/*
			 * if(extensionPeople == null){ viewback.setViewName(
			 * "redirect:/newReleaseProject/project_detail.do?projectId=" +
			 * projectId); }else{ viewback.setViewName(
			 * "redirect:/newReleaseProject/project_detail.do?projectId=" +
			 * projectId + "&extensionPeople="+extensionPeople); } return
			 * viewback;
			 */
		} else if (oneApiPage != null && oneApiPage.getTotal() > 0) {// 一对一心愿
			view.addObject("type", 2);
		} else {
			view.addObject("type", 0);
		}
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST + "_" + projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);

		// 项目配捐
		ApiMatchDonate apiMatchDonate = new ApiMatchDonate();
		apiMatchDonate.setProjectId(project.getId());
		ApiPage<ApiMatchDonate> apiPage = redPacketsFacade.queryByParam(
				apiMatchDonate, 1, 5);
		List<ApiMatchDonate> matchDonates = apiPage.getResultData();
		if (matchDonates != null && matchDonates.size() > 0) {
			view.addObject("matchDonate", matchDonates.get(0));
		}
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (userId != null) {
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("user", user);
		}
		if (project != null) {
			// 捐款笔数
			ApiDonateRecord donate = new ApiDonateRecord();
			donate.setState(302);
			donate.setProjectId(projectId);
			ApiDonateRecord donate1 = donateRecordFacade
					.queryCompanyCenter(donate);
			if (donate1 != null)
				view.addObject("peopleNum", donate1.getGoodHelpCount());
			// 判断发起人类型 1：认证后的企业 2：个人 0：未认证的企业
			if (project.getUserId() == null) {
				view.addObject("isCompany", 2);
			} else {
				ApiFrontUser user = userFacade.queryById(project.getUserId());
				if (user == null) {
					view.addObject("isCompany", 2);
				} else if (!user.getUserType().equals("enterpriseUsers")) {
					view.addObject("isCompany", 2);
				} else {
					view.addObject("isCompany", 1);
				}
			}
			view.addObject("desc", project.getSubTitle());
			process = 0.0;
			if (project.getCryMoney() >= 0.001) {
				process = project.getDonatAmount() / project.getCryMoney();
			}
			view.addObject(
					"process",
					process > 1 ? "100" : StringUtil.doublePercentage(
							project.getDonatAmount(), project.getCryMoney(), 0));
			view.addObject("processbfb", StringUtil.doublePercentage(
					project.getDonatAmount(), project.getCryMoney(), 0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				view.addObject("owner", true);
			}
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
			userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
			List<ApiProjectUserInfo> userInfos = projectFacade
					.queryProjectUserInfoList(userInfo);
			for (ApiProjectUserInfo u : userInfos) {
				if (u.getPersonType() == 0) {
					view.addObject("shouzhu", u);
				} else if (u.getPersonType() == 1) {
					view.addObject("zhengming", u);
				} else if (u.getPersonType() == 2) {
					view.addObject("fabu", u);
				}
			}
		}
		if (reports != null && reports.getTotal() > 0) {
			view.addObject("reports", reports.getResultData());
		}

		// 判断是否加入善库
		ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade
				.queryByParam(apigp, 1, 50);
		if (goodLibrary != null && goodLibrary.getResultData().size() > 0) {
			view.addObject("goodLibrary", 1);
		} else {
			view.addObject("goodLibrary", 0);
		}
		// 企业助善信息
		ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
		good_help.setProject_id(projectId);
		good_help.setOrderBy("createTime");
		good_help.setOrderDirection("desc");
		good_help.setState(203);
		List<String> list = new ArrayList<String>(1);
		list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass()
				.getName(), BaseBean.RANGE_WHOLE, projectId));
		good_help.initCache(true, DateUtil.DURATION_MIN_S, list, "project_id",
				"state");
		ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade
				.queryCompanyGoodHelpListByCondition(good_help, 1, 4);
		if (goodHelps != null && goodHelps.getTotal() > 0) {
			view.addObject("goodhelps", goodHelps.getResultData());
			view.addObject("firstgh", goodHelps.getResultData().get(0));
		}
		String userType = SSOUtil.getCurrentUserType(request, response);
		if (ProjectConstant.PROJECT_STATUS_COLLECT == project.getState()) {
			if ("enterpriseUsers".equals(userType) && process < 1
					&& project.getState() == 240 && userId != null) {
				if (goodHelps == null || goodHelps.getTotal() == 0) {
					ApiCompany apiCompany = new ApiCompany();
					apiCompany.setUserId(userId);
					apiCompany.setState(203);
					List<String> llist = new ArrayList<String>(1);
					llist.add(ApiCompany.getCacheRange(apiCompany.getClass()
							.getName(), BaseBean.RANGE_WHOLE, userId));
					apiCompany.initCache(true, DateUtil.DURATION_MIN_S, llist,
							"userId", "state");
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					if (apiCompany != null)
						view.addObject("isComapny", true);
				}
			} else if (userType == null) {
				view.addObject("isComapny", true);
			}
		}
		// 最新捐款列表

		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		r.setState(302);
		if (userId != null && userId != 0) {
			r.setGoodLibraryId(userId);
		}
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(),
				BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		// view.addObject("peopleNum", project.getDonationNum());
		String browser = UserUtil.Browser(request);

		if (browser.equals("wx")) {
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
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,
					perfecturl);
			view.addObject("appId", map.get("appId"));
			view.addObject("timestamp", map.get("timeStamp"));
			view.addObject("noncestr", map.get("nonceStr"));
			view.addObject("signature", map.get("signature"));
		}

		ApiProjectVolunteer apiProjectVolunteer = new ApiProjectVolunteer();
		apiProjectVolunteer.setProjectId(projectId);
		int number = projectVolunteerFacade.count(apiProjectVolunteer);

		ApiPage<ApiProjectVolunteer> pvs = projectVolunteerFacade
				.queryVolunteerList(apiProjectVolunteer, 1, 5);
		List<ApiProjectVolunteer> alist = pvs.getResultData();

		view.addObject("alist", alist);
		view.addObject("number", number);
		view.addObject("projectId", projectId);
		view.addObject("extensionPeople", extensionPeople);
		view.addObject("browser", browser);
		view.addObject("userId", userId);
		view.addObject("slogans", slogans);

		String itemType = request.getParameter("itemType") == null ? ""
				: request.getParameter("itemType");
		view.addObject("itemType", itemType);
		/* 随机获取祝福语 */
		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setConfigKey(StringUtil.LEAVEWORD);
		List<ApiConfig> configs = commonFacade.queryList(apiConfig);
		if (configs != null && configs.get(0) != null
				&& configs.get(0).getConfigValue() != null
				&& configs.get(0).getConfigValue() != "") {
			String[] configss = configs.get(0).getConfigValue().split("。");
			Random random = new Random();
			int i = random.nextInt(configss.length);
			view.addObject("leaveWord", configss[i]);
		}

		// 捐款金额配置
		ApiProjectMoneyConfig param = new ApiProjectMoneyConfig();
		param.setProjectId(projectId);
		List<ApiProjectMoneyConfig> moneyConfigs = projectFacade
				.queryMoneyConfigByParam(param);
		view.addObject("moneyConfigs", moneyConfigs);
		/* 点击量统计start */
		try {
			// 获取当天剩余缓存时间
			long startTime = (DateUtil.getCurrentDayEnd().getTime() - new Date()
					.getTime()) / 1000;
			String clickRate = (String) redisService
					.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_H5
							+ projectId);
			if (StringUtils.isEmpty(clickRate)) {
				redisService.saveObjectData(
						PengPengConstants.PROJECT_CLICKRATE_H5 + projectId,
						"t_h5_" + projectId + "_" + 1, startTime);
			} else {
				String cstr[] = clickRate.split("_");
				if (null != cstr && cstr.length >= 4) {
					Integer click = Integer.parseInt(cstr[3]);
					if (null == click) {
						click = 1;
					} else {
						click += 1;
					}

					redisService.saveObjectData(
							PengPengConstants.PROJECT_CLICKRATE_H5 + projectId,
							"t_h5_" + projectId + "_" + click, startTime);
				}
			}
		} catch (Exception e) {

		}

		/* 点击量统计end */

		return view;
	}

	/**
	 * 善园项目详情
	 * 
	 * @param projectId
	 * @param request
	 * @param response
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping("gardenview_h5")
	public ModelAndView gardenview(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {

		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST + "_" + projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		ModelAndView view = new ModelAndView("h5/garden/garden_project_detail");
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			view.addObject("desc",
					StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
			process = 0.0;
			if (project.getCryMoney() >= 0.001) {
				process = project.getDonatAmount() / project.getCryMoney();
			}
			view.addObject(
					"process",
					process > 1 ? "100" : StringUtil.doublePercentage(
							project.getDonatAmount(), project.getCryMoney(), 0));
			view.addObject("processbfb", StringUtil.doublePercentage(
					project.getDonatAmount(), project.getCryMoney(), 0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				view.addObject("owner", true);
			}
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
			userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
			List<ApiProjectUserInfo> userInfos = projectFacade
					.queryProjectUserInfoList(userInfo);
			for (ApiProjectUserInfo u : userInfos) {
				if (u.getPersonType() == 0) {
					view.addObject("shouzhu", u);
				} else if (u.getPersonType() == 1) {
					view.addObject("zhengming", u);
				} else if (u.getPersonType() == 2) {
					view.addObject("fabu", u);
				}
			}
		}
		if (reports != null && reports.getTotal() > 0) {
			view.addObject("reports", reports.getResultData());
		}
		// 企业助善信息
		ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
		good_help.setProject_id(projectId);
		good_help.setOrderBy("createTime");
		good_help.setOrderDirection("desc");
		good_help.setState(203);
		List<String> list = new ArrayList<String>(1);
		list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass()
				.getName(), BaseBean.RANGE_WHOLE, projectId));
		good_help.initCache(true, DateUtil.DURATION_MIN_S, list, "project_id",
				"state");
		ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade
				.queryCompanyGoodHelpListByCondition(good_help, 1, 4);
		if (goodHelps != null && goodHelps.getTotal() > 0) {
			view.addObject("goodhelps", goodHelps.getResultData());
			view.addObject("firstgh", goodHelps.getResultData().get(0));
		}
		String userType = SSOUtil.getCurrentUserType(request, response);
		if (ProjectConstant.PROJECT_STATUS_COLLECT == project.getState()) {
			if ("enterpriseUsers".equals(userType) && process < 1
					&& project.getState() == 240 && userId != null) {
				if (goodHelps == null || goodHelps.getTotal() == 0) {
					ApiCompany apiCompany = new ApiCompany();
					apiCompany.setUserId(userId);
					apiCompany.setState(203);
					List<String> llist = new ArrayList<String>(1);
					llist.add(ApiCompany.getCacheRange(apiCompany.getClass()
							.getName(), BaseBean.RANGE_WHOLE, userId));
					apiCompany.initCache(true, DateUtil.DURATION_MIN_S, llist,
							"userId", "state");
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					if (apiCompany != null)
						view.addObject("isComapny", true);
				}
			} else if (userType == null) {
				view.addObject("isComapny", true);
			}
		}
		// 最新捐款列表

		ApiDonateRecord r = new ApiDonateRecord();
		Date edate = new Date();
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(),
				BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		view.addObject("peopleNum", donats.getTotal());
		String browser = UserUtil.Browser(request);
		if (browser.equals("wx")) {
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
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,
					perfecturl);
			view.addObject("appId", map.get("appId"));
			view.addObject("timestamp", map.get("timeStamp"));
			view.addObject("noncestr", map.get("nonceStr"));
			view.addObject("signature", map.get("signature"));
		}
		view.addObject("projectId", projectId);
		return view;
	}

	@RequestMapping("view_list_h5")
	public ModelAndView view_list_h5(ProjectForm from,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/project/project_list");
		List<ApiTypeConfig> atc = commonFacade.queryList();
		view.addObject("atc", atc);
		view.addObject("extensionPeople",
				request.getSession().getAttribute("extensionPeople"));
		view.addObject("field", from.getField());
		view.addObject("tag", from.getTag());
		return view;
	}

	@RequestMapping("view_paysuccess_h5")
	public ModelAndView view_paysucess_h5(
			DepositForm from,
			@RequestParam(value = "red", required = false) Integer red,
			@RequestParam(value = "uredId", required = false) Integer uredId,
			@RequestParam(value = "fromWish", required = false) String fromWish,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {

		ModelAndView view = new ModelAndView("h5/sharePay_success");
		ApiUser_Redpackets ur = redPacketsFacade.queryByRedPaperTradeNo(uredId);
		if (ur != null && StringUtils.isNotEmpty(ur.getTradeNo())) {
			from.setTradeNo(ur.getTradeNo());
		}
		Integer extensionPeople = (Integer) request.getSession().getAttribute(
				"extensionPeople");
		view.addObject("ePeople", extensionPeople);
		logger.info("ePeople >>>" + extensionPeople);
		view.addObject("title", from.getpName());
		view.addObject("amount", from.getAmount());
		view.addObject("nickName", from.getNickName());
		view.addObject("headImage", from.getHeadImage());
		view.addObject("projectId", from.getProjectId());
		view.addObject("tradeNo", from.getTradeNo());
		view.addObject("extensionPeople", from.getUserId());
		view.addObject("slogans", from.getSlogans());// 用于捐款成功分享和返回项目标识
		if (from.getProjectId() != 0) {
			ApiProject p = projectFacade
					.queryProjectDetail(from.getProjectId());
			view.addObject("subTitle", p.getSubTitle());
			view.addObject("coverImageUrl", p.getCoverImageUrl());
		}

		if (StringUtils.isNotEmpty(from.getTradeNo())) {
			logger.info("from.getTradeNo()>>>" + from.getTradeNo());
			ApiDonateRecord record = donateRecordFacade
					.queryWeixinToPayNoticeByTranNum(from.getTradeNo());
			logger.info("record.getId()>>>" + record.getId());
			ApiMatchDonate md = new ApiMatchDonate();
			md.setRecordID(record.getId());
			md = redPacketsFacade.queryMatchDonateByParam(md);
			if (md == null) {
				md = new ApiMatchDonate();
				md.setRecordID(record.getId());
				md = redPacketsFacade.queryMatchDonateByParam2(md);
				logger.info("redPacketsFacade.queryMatchDonateByParam>>>" + md);
			}
			logger.info("md>>>" + md);
			if (md != null && StringUtils.isNotEmpty(md.getCompanyname())) {
				ApiFrontUser user = userFacade.queryById(md.getUserid());
				view.addObject("companyHeadImg", user.getCoverImageUrl());
				view.addObject("isMatchDonate", 1);
				view.addObject("matchDonate", md);
				logger.info("isMatchDonate>>>1");
			}
		} else {
			view.addObject("isMatchDonate", 0);
		}
		boolean flag = false;
		boolean result = false;
		if (StringUtils.isNotEmpty(fromWish) && fromWish.equals("fromWish")) {
			flag = false;
			view.addObject("flag", flag);
			view.addObject("red", 1);
			view.addObject("result", false);
		} else {
			// 判断是否是红包支付
			if (red != null && red == 1) {
				view.addObject("red", red);
			} else {
				view.addObject("red", 0);
			}
			// 判断是否指定项目
			flag = CommonUtils.IsQueryRedPacket(from.getProjectId(),
					redPacketsFacade);
			view.addObject("flag", flag);
			if (red == null && flag == true) {
				ApiRedpackets apiRedpacket = new ApiRedpackets();
				apiRedpacket.setAppointproject(String.valueOf(from
						.getProjectId()));
				ApiPage<ApiRedpackets> redpackets = redPacketsFacade
						.queryByParam(apiRedpacket, 1, 20);
				List<Integer> redpacketIds = new ArrayList<Integer>();
				for (ApiRedpackets ar : redpackets.getResultData()) {
					redpacketIds.add(ar.getId());
				}
				if (redpacketIds.size() > 0) {
					ApiRedpacketspool aRp = new ApiRedpacketspool();
					aRp.setRedpackesIds(redpacketIds);
					result = redPacketsFacade.isNotRedPackets(aRp);
				}

			}

			view.addObject("result", result);
		}

		if (from.getUserId() == null) {
			Integer userId = UserUtil.getUserId(request, response);
			if (userId == null || userId == 0) {
				ApiDonateRecord donat = donateRecordFacade
						.queryPayNoticeByTranNum(from.getTradeNo());
				if (donat != null) {
					userId = donat.getUserId();
				}
			}
			from.setUserId(userId);
		}
		if (from.getHeadImage() == null || from.getNickName() == null) {
			ApiFrontUser user = userFacade.queryById(from.getUserId());
			view.addObject("nickName",
					user.getNickName() == null ? "" : user.getNickName());
			view.addObject("headImage", user.getCoverImageUrl() == null ? ""
					: user.getCoverImageUrl());
		}
		String browser = UserUtil.Browser(request);
		if (browser.equals("wx")) {
			if (from.getUserId() != null && red == null && flag == true
					&& result == true) {
				ApiUser_Redpackets apiUR = new ApiUser_Redpackets();
				apiUR.setUserId(from.getUserId());
				apiUR.setExtensionpeople(from.getUserId());
				apiUR.setSpare(from.getTradeNo());
				apiUR.setStatusList(null);
				Integer count = redPacketsFacade
						.countRedPaperNumByCondition(apiUR);
				if (count == 0) {// /取红包
					CommonUtils.IsQueryRedPacket(from.getUserId(),
							from.getProjectId(), from.getTradeNo(),
							redPacketsFacade);
				}
			}

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
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,
					perfecturl);
			view.addObject("appId", map.get("appId"));
			view.addObject("timestamp", map.get("timeStamp"));
			view.addObject("noncestr", map.get("nonceStr"));
			view.addObject("signature", map.get("signature"));
		}
		// 商品众筹支付成更改状态
		if (!"".equals(from.getCrowdFunding())) {
			ApiFrontUserProjectPrize apiupPrize = new ApiFrontUserProjectPrize();
			apiupPrize.setOrderNum(from.getCrowdFunding());
			List<ApiFrontUserProjectPrize> listPrizes = userRelationInfoFacade
					.queryByParam(apiupPrize);
			if (listPrizes != null && listPrizes.size() > 0) {
				apiupPrize.setId(listPrizes.get(0).getId());
				apiupPrize.setState(201);// 待发货
				userRelationInfoFacade.updateFrontUserProjectPrize(apiupPrize);
			}
		}
		// 判断是否是一对一项目，弹出填写用户信息
		ApiOneAid model = new ApiOneAid();
		model.setOrder_num(from.getTradeNo());
		ApiPage<ApiOneAid> page = projectFacade
				.queryOneAidByParam(model, 1, 10);
		if (page != null && page.getTotal() > 0) {
			view.addObject("show", 1);
		} else {
			view.addObject("show", 0);
		}
		//判断openName是否匿名，更新捐款明细
		if (from.getUserId() != null && from.getNameOpen() != null) {
			ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
			apiDonateRecord.setUserId(from.getUserId());
			ApiPage<ApiDonateRecord> list = donateRecordFacade.queryByCondition(apiDonateRecord, 1, 1);
			if(list != null && list.getTotal() > 0){
				apiDonateRecord = list.getResultData().get(0);
				ApiDonateRecord newDonateRecord = new ApiDonateRecord();
				newDonateRecord.setId(apiDonateRecord.getId());
				newDonateRecord.setNameOpen(from.getNameOpen());
				ApiResult result2 =  donateRecordFacade.updateDonateRecord(newDonateRecord);
				logger.info("是否匿名：" + result2.toString());
			}
		}
		return view;

	}

	@RequestMapping("test_sharepaysuccess_h5")
	public ModelAndView test_sharepaysucess_h5(DepositForm from,
			HttpServletRequest request, HttpServletResponse response) {

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
	 * 
	 * @return 最新的捐款记录
	 */
	@ResponseBody
	@RequestMapping("latestdonationlist")
	public Map<String, Object> latestdonationlist(
			Integer state,
			@RequestParam(value = "projectTitle", required = false) String projectTitle,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		ApiDonateRecord r = new ApiDonateRecord();
		if (StringUtils.isNotEmpty(projectTitle)) {
			r.setProjectTitle(projectTitle);
		}
		r.setState(state);
		// 查询总数
		ApiPage<ApiDonateRecord> donatIds = donateRecordFacade
				.queryLatestDonateRecordIdList(r, page, pageSize);
		List<Integer> ids = new ArrayList<Integer>();
		for (ApiDonateRecord d : donatIds.getResultData()) {
			ids.add(d.getId());
			;
		}
		r.setIdList(ids);
		if (ids.size() == 0) {
			return webUtil.successResDoubleObject(null, donatIds);
		}
		// 查询详情
		List<ApiDonateRecord> donats = donateRecordFacade
				.queryLatestDonateRecordIdDetailList(r);
		for (ApiDonateRecord adr : donats) {
			if (adr.getTouristMessage() != null) {
				JSONObject json = JSON.parseObject(adr.getTouristMessage());
				if (StringUtils.isNotEmpty(json.getString("name"))) {
					adr.setNickName(json.getString("name"));
				}
			}
		}
		return webUtil.successResDoubleObject(donats, donatIds);
	}

	/*
	 * 查询最新捐款记录（搜索）
	 * 
	 * @return 最新的捐款记录
	 */
	@ResponseBody
	@RequestMapping("latestdonationlistSearch")
	public Map<String, Object> latestdonationlistSearch(
			Integer state,
			@RequestParam(value = "projectTitle", required = false) String projectTitle,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

		int count = donateRecordFacade.countDonateRecord(state, projectTitle);
		ApiPage<ApiDonateRecord> donats = new ApiPage<ApiDonateRecord>();
		List<ApiDonateRecord> list = new ArrayList<ApiDonateRecord>();
		if (count > 0) {
			ApiDonateRecord r = new ApiDonateRecord();
			if (StringUtils.isNotEmpty(projectTitle)) {
				r.setProjectTitle(projectTitle);
			}
			r.setState(state);
			donats = donateRecordFacade.queryLatestDonateRecordList(r, page,
					pageSize);
			list = donats.getResultData();
			for (ApiDonateRecord adr : list) {
				if (adr.getTouristMessage() != null) {
					JSONObject json = JSON.parseObject(adr.getTouristMessage());
					if (StringUtils.isNotEmpty(json.getString("name"))) {
						adr.setNickName(json.getString("name"));
					}
				}
			}
			int pages = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			donats.setPages(pages);
			donats.setTotal(count);
		}
		donats.setResultData(list);
		return webUtil.successRes(donats);
	}

	/*
	 * @param projectId 项目ID 显示项目详情
	 */
	@RequestMapping("view_test_h5")
	public ModelAndView viewtest(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {
		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		// 项目进度
		ApiReport report = new ApiReport();
		report.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_SCHEDUlE_LIST + "_" + projectId;
		report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
		ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
				30);
		ModelAndView view = new ModelAndView("h5/project/project_detail_test");
		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			view.addObject("desc",
					StringUtil.convertTodelete(project.getContent()));
			project.setContent(StringUtil.convertToHtml(project.getContent()));
			process = 0.0;
			if (project.getCryMoney() >= 0.001) {
				process = project.getDonatAmount() / project.getCryMoney();
			}
			view.addObject(
					"process",
					process > 1 ? "100" : StringUtil.doublePercentage(
							project.getDonatAmount(), project.getCryMoney(), 0));
			view.addObject("processbfb", StringUtil.doublePercentage(
					project.getDonatAmount(), project.getCryMoney(), 0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				view.addObject("owner", true);
			}
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
			userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
			List<ApiProjectUserInfo> userInfos = projectFacade
					.queryProjectUserInfoList(userInfo);
			for (ApiProjectUserInfo u : userInfos) {
				if (u.getPersonType() == 0) {
					view.addObject("shouzhu", u);
				} else if (u.getPersonType() == 1) {
					view.addObject("zhengming", u);
				} else if (u.getPersonType() == 2) {
					view.addObject("fabu", u);
				}
			}
		}
		if (reports != null && reports.getTotal() > 0) {
			view.addObject("reports", reports.getResultData());
		}
		// 企业助善信息
		ApiCompany_GoodHelp good_help = new ApiCompany_GoodHelp();
		good_help.setProject_id(projectId);
		good_help.setOrderBy("createTime");
		good_help.setOrderDirection("desc");
		good_help.setState(203);
		List<String> list = new ArrayList<String>(1);
		list.add(ApiCompany_GoodHelp.getCacheRange(good_help.getClass()
				.getName(), BaseBean.RANGE_WHOLE, projectId));
		good_help.initCache(true, DateUtil.DURATION_MIN_S, list, "project_id",
				"state");
		ApiPage<ApiCompany_GoodHelp> goodHelps = companyFacade
				.queryCompanyGoodHelpListByCondition(good_help, 1, 4);
		if (goodHelps != null && goodHelps.getTotal() > 0) {
			view.addObject("goodhelps", goodHelps.getResultData());
			view.addObject("firstgh", goodHelps.getResultData().get(0));
		}
		String userType = SSOUtil.getCurrentUserType(request, response);
		if (ProjectConstant.PROJECT_STATUS_COLLECT == project.getState()) {
			if ("enterpriseUsers".equals(userType) && process < 1
					&& project.getState() == 240 && userId != null) {
				if (goodHelps == null || goodHelps.getTotal() == 0) {
					ApiCompany apiCompany = new ApiCompany();
					apiCompany.setUserId(userId);
					apiCompany.setState(203);
					List<String> llist = new ArrayList<String>(1);
					llist.add(ApiCompany.getCacheRange(apiCompany.getClass()
							.getName(), BaseBean.RANGE_WHOLE, userId));
					apiCompany.initCache(true, DateUtil.DURATION_MIN_S, llist,
							"userId", "state");
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					if (apiCompany != null)
						view.addObject("isComapny", true);
				}
			} else if (userType == null) {
				view.addObject("isComapny", true);
			}
		}
		// 最新捐款列表

		ApiDonateRecord r = new ApiDonateRecord();
		Date edate = new Date();
		Date bdate = DateUtil.add(edate, -12 * 30);
		r.setQueryStartDate(bdate);
		r.setQueryEndDate(edate);
		r.setProjectId(projectId);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(),
				BaseBean.RANGE_WHOLE, projectId));
		r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 10);
		view.addObject("donates", donats.getResultData());
		view.addObject("peopleNum", donats.getTotal());

		String browser = UserUtil.Browser(request);
		if (browser.equals("wx")) {
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
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,
					perfecturl);
			view.addObject("appId", map.get("appId"));
			view.addObject("timestamp", map.get("timeStamp"));
			view.addObject("noncestr", map.get("nonceStr"));
			view.addObject("signature", map.get("signature"));
		}
		return view;
	}

	/**
	 * h5 项目列表
	 * 
	 * @param from
	 *            sortType 0： 最新发布（时间倒序） 1： 关注最多（捐款人最多） 2： 最早发布（时间顺序） 3：
	 *            最新反馈（根据求助人反馈的时间，最新的反馈排在最前） 4： 捐助最多
	 * @return
	 */
	@ResponseBody
	@RequestMapping("list_h5")
	public JSONObject list(ProjectForm from, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		// Integer userId = UserUtil.getUserId(request, response);
		try {
			ApiProject ap = new ApiProject();

			ap.setIsHide(Integer.valueOf(0));
			if ((from.getSortType() == null)
					|| (from.getSortType().intValue() == 0)) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("registrTime");
			} else if (from.getSortType().intValue() == 1) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("donationNum");
			} else if (from.getSortType().intValue() == 2) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("lastUpdateTime");
			} else if (from.getSortType().intValue() == 3) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("lastFeedbackTime");
			} else if (from.getSortType().intValue() == 4) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("donatAmount");
			} else if (from.getSortType().intValue() == 5) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("issueTime desc,donatAmount/cryMoney");
			}
			if (StringUtils.isNotEmpty(from.getTypeName())) {
				ap.setField(from.getTypeName());
			}
			if (from.getStatus() == null) {
				List<Integer> states = new ArrayList();
				states.add(Integer.valueOf(240));
				states.add(Integer.valueOf(260));
				ap.setStates(states);
			} else if (from.getStatus().intValue() == 1) {
				ap.setState(Integer.valueOf(240));
			} else if (from.getStatus().intValue() == 2) {
				ap.setState(Integer.valueOf(260));
			} else {
				List<Integer> states = new ArrayList();
				states.add(Integer.valueOf(240));
				states.add(Integer.valueOf(260));
				ap.setStates(states);
			}
			if (from != null && from.getTag() != null) {
				ap.setTag(from.getTag());
			}
			// ap.setDonateState(from.getState());

			// ap.setUserId(userId);
			ApiPage<ApiProject> apiPage = this.projectFacade
					.queryProjectListNew(ap, from.getPage(), from.getLen());

			// Integer noDonateNum = this.projectFacade.countUserDonateNum(ap,
			// userId.intValue(), 0);

			// Integer donateNum = this.projectFacade.countUserDonateNum(ap,
			// userId.intValue(), 1);
			if (apiPage != null
					&& apiPage.getTotal() > 0
					&& (apiPage.getTotal() - from.getPage() * from.getLen() >= 0 || from
							.getPage() * from.getLen() - apiPage.getTotal() < from
								.getLen())) {

				List<ApiProject> projects = apiPage.getResultData();
				if (projects.size() == 0) {
					data.put("result", Integer.valueOf(1));
				} else {
					for (ApiProject project : projects) {
						JSONObject item = new JSONObject();
						item.put("itemId", project.getId());
						item.put("field", project.getField());
						item.put("type", project.getType());
						item.put("title", project.getTitle());
						item.put("information", project.getInformation());
						item.put("donaAmount", project.getDonatAmount());
						item.put("cryMoney", project.getCryMoney());
						item.put(
								"process",
								StringUtil.doublePercentage(
										project.getDonatAmount(),
										project.getCryMoney(), 0));
						item.put("imageurl", project.getCoverImageUrl());
						item.put("endtime", new Date());
						item.put("state", project.getState());
						item.put("special_fund_id",
								project.getSpecial_fund_id());
						items.add(item);
					}
					data.put("items", items);
					data.put("page", Integer.valueOf(apiPage.getPageNum()));
					data.put("pageNum", Integer.valueOf(apiPage.getPageSize()));
					data.put("total", apiPage.getTotal());
					data.put("result", Integer.valueOf(0));
					// data.put("donateNum", donateNum);
					// data.put("noDonateNum", noDonateNum);
				}
			} else {
				data.put("total", 0);
				data.put("result", Integer.valueOf(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("result", Integer.valueOf(2));
			return data;
		}
		return data;
	}

	/**
	 * h5 按最近30天，捐款比例排序的项目列表
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("list_h5_30")
	public JSONObject list_h5_30(ProjectForm from, HttpServletRequest request,
						   HttpServletResponse response) {
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		try {
			ApiPage<ApiProject> apiPage = this.projectFacade
					.queryProjectBy30(from.getPage(), from.getLen());
			if (apiPage != null
					&& apiPage.getTotal() > 0
					&& (apiPage.getTotal() - from.getPage() * from.getLen() >= 0 || from
					.getPage() * from.getLen() - apiPage.getTotal() < from
					.getLen())) {

				List<ApiProject> projects = apiPage.getResultData();
				if (projects.size() == 0) {
					data.put("result", Integer.valueOf(1));
				} else {
					for (ApiProject project : projects) {
						JSONObject item = new JSONObject();
						item.put("itemId", project.getId());
						item.put("field", project.getField());
						item.put("type", project.getType());
						item.put("title", project.getTitle());
						item.put("information", project.getInformation());
						item.put("donaAmount", project.getDonatAmount());
						item.put("cryMoney", project.getCryMoney());
						item.put(
								"process",
								StringUtil.doublePercentage(
										project.getDonatAmount(),
										project.getCryMoney(), 0));
						item.put("imageurl", project.getCoverImageUrl());
						item.put("endtime", new Date());
						item.put("state", project.getState());
						item.put("special_fund_id",
								project.getSpecial_fund_id());
						items.add(item);
					}
					data.put("items", items);
					data.put("page", Integer.valueOf(apiPage.getPageNum()));
					data.put("pageNum", Integer.valueOf(apiPage.getPageSize()));
					data.put("total", apiPage.getTotal());
					data.put("result", Integer.valueOf(0));
					// data.put("donateNum", donateNum);
					// data.put("noDonateNum", noDonateNum);
				}
			} else {
				data.put("total", 0);
				data.put("result", Integer.valueOf(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("result", Integer.valueOf(2));
			return data;
		}
		return data;
	}
	/**
	 * h5 批量捐
	 * 
	 * @param from
	 *            sortType 0： 最新发布（时间倒序） 1： 关注最多（捐款人最多） 2： 最早发布（时间顺序） 3：
	 *            最新反馈（根据求助人反馈的时间，最新的反馈排在最前） 4： 捐助最多
	 * @return
	 */
	@ResponseBody
	@RequestMapping("list_batch_h5")
	public JSONObject list_batch_h5(ProjectForm from,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "topicId", required = false) Integer topicId) {
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		Integer userId = UserUtil.getUserId(request, response);
		// /////////////////////
		if (userId == null) {
			String browser = UserUtil.Browser(request);
			String openId = "";
			String Token = "";
			String unionid = "";
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			if (browser.equals("wx")) {
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					if ("".equals(openId) || openId == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo
									.getCodeRequest(perfecturl);
							data.put("code", url_weixin_code);
							data.put("result", 2);
							// view = new ModelAndView("redirect:" +
							// url_weixin_code);
							// return view;
						}
						mapToken = CommonUtils
								.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						Token = mapToken.get("access_token").toString();
						unionid = mapToken.get("unionid").toString();
						redisService.saveObjectData("weixin_token", Token,
								DateUtil.DURATION_HOUR_S);
					}
				} catch (Exception e) {
					logger.error("微信登陆出现问题" + e);
				}

				ApiFrontUser user = CommonUtils.queryUser(request, openId,
						Token, unionid);
				try {
					// 自动登录
					SSOUtil.login(user, request, response);
				} catch (Exception e) {
					logger.error("weixindeposit2 >> SSOUtil.login : " + e);
				}
				// view.addObject("user",user);
			} else if (userId == null && from.getKey() == 0) {
				data.put("result", -1);
				data.put("key", from.getKey());
				return data;
			}
		}

		try {
			ApiProject ap = new ApiProject();

			ap.setIsHide(Integer.valueOf(0));
			if ((from.getSortType() == null)
					|| (from.getSortType().intValue() == 0)) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("registrTime");
			} else if (from.getSortType().intValue() == 1) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("donationNum");
			} else if (from.getSortType().intValue() == 2) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("lastUpdateTime");
			} else if (from.getSortType().intValue() == 3) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("lastFeedbackTime");
			} else if (from.getSortType().intValue() == 4) {
				ap.setOrderDirection("desc");
				ap.setOrderBy("donatAmount");
			}
			if (StringUtils.isNotEmpty(from.getTypeName())) {
				ap.setField(from.getTypeName());
			}
			if (from.getStatus() == null) {
				List<Integer> states = new ArrayList();
				states.add(Integer.valueOf(240));
				states.add(Integer.valueOf(260));
				ap.setStates(states);
			} else if (from.getStatus().intValue() == 1) {
				ap.setState(Integer.valueOf(240));
			} else if (from.getStatus().intValue() == 2) {
				ap.setState(Integer.valueOf(260));
			} else {
				List<Integer> states = new ArrayList();
				states.add(Integer.valueOf(240));
				states.add(Integer.valueOf(260));
				ap.setStates(states);
			}
			ap.setDonateState(from.getState());
			if (topicId != null && topicId != 0) {// 说明是从专题页面传过来的
				// 根据topicId查询项目id,根据项目id查询项目列表
				List<Integer> ids = new ArrayList<Integer>();
				ApiProjectTopic apTopic = new ApiProjectTopic();
				apTopic = projectFacade.selectProjectTopicById(topicId);
				if (ap != null && apTopic.getProjectIds() != ""
						&& apTopic.getProjectIds() != null) {
					String[] projectIds = apTopic.getProjectIds().split(",");
					for (int i = 0; i < projectIds.length; i++) {
						ids.add(Integer.valueOf(projectIds[i]));
					}
					ap.setIds(ids);
					ap.setTotalCopies(-1);
				}
			}
			ApiPage<ApiProject> apiPage = new ApiPage<ApiProject>();
			Integer noDonateNum = 0;
			Integer donateNum = 0;
			if (userId != null) {
				ap.setUserId(userId);
				apiPage = this.projectFacade.queryProjectListNew(ap,
						from.getPage(), from.getLen(), 0, 0);

				ap.setTotalCopies(null);
				noDonateNum = this.projectFacade.countUserDonateNum(ap,
						userId.intValue(), 0);

				donateNum = this.projectFacade.countUserDonateNum(ap,
						userId.intValue(), 1);
			} else {
				if (from.getState() == 0) {// 未捐
					apiPage = this.projectFacade.queryProjectListNew(ap,
							from.getPage(), from.getLen());
					noDonateNum = this.projectFacade.countUserDonateNum(ap, 0,
							0);
				} else {
					ap.setUserId(0);
					apiPage = this.projectFacade.queryProjectListNew(ap,
							from.getPage(), from.getLen());
					ap.setDonateState(0);
					noDonateNum = this.projectFacade.countUserDonateNum(ap, 0,
							0);
				}

			}
			List<ApiProject> projects = apiPage.getResultData();
			if (projects.size() == 0) {
				data.put("result", Integer.valueOf(1));
				data.put("donateNum", donateNum);
				data.put("noDonateNum", noDonateNum);
			} else {
				for (ApiProject project : projects) {
					JSONObject item = new JSONObject();
					item.put("itemId", project.getId());
					item.put("field", project.getField());
					item.put("type", project.getType());
					item.put("title", project.getTitle());
					item.put("information", project.getInformation());
					item.put("donaAmount", project.getDonatAmount());
					item.put("cryMoney", project.getCryMoney());
					item.put(
							"process",
							StringUtil.doublePercentage(
									project.getDonatAmount(),
									project.getCryMoney(), 0));
					item.put("imageurl", project.getCoverImageUrl());
					item.put("endtime", new Date());
					item.put("state", project.getState());
					items.add(item);
				}
				data.put("items", items);
				data.put("page", Integer.valueOf(apiPage.getPageNum()));
				data.put("pageNum", Integer.valueOf(apiPage.getPageSize()));
				data.put("total", Integer.valueOf(apiPage.getPages()));
				data.put("result", Integer.valueOf(0));
				data.put("donateNum", donateNum);
				data.put("noDonateNum", noDonateNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("result", Integer.valueOf(2));
			return data;
		}
		return data;
	}

	/**
	 * 推荐项目列表
	 * 
	 * @param from
	 * @return
	 */
	@ResponseBody
	@RequestMapping("recommendList_h5")
	public JSONObject recommentList(ProjectForm from) {
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		// 把取到的善园项目封装成json数据
		try {
			ApiProject ap = new ApiProject();
			ap.setOrderDirection("desc");
			ap.setIsHide(0);
			ap.setIsRecommend(1);
			from.setPage(1);
			from.setLen(2);
			ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap,
					from.getPage(), from.getLen());
			List<ApiProject> projects = apiPage.getResultData();

			if (projects.size() == 0) {
				ap = new ApiProject();
				ap.setIsHide(0);
				ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
				apiPage = projectFacade.queryProjectList(ap, from.getPage(),
						from.getLen());
				projects = apiPage.getResultData();
			}

			// 无数据
			if (projects.size() == 0) {
				data.put("result", 1);
			} else {
				for (ApiProject project : projects) {
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
					item.put("special_fund_id", project.getSpecial_fund_id());
					items.add(item);
				}
				data.put("items", items);
				data.put("page", apiPage.getPageNum());// 当前页码
				data.put("pageNum", apiPage.getPageSize()); // 每页行数
				data.put("total", apiPage.getPages());// 总页数
				data.put("result", 0);
			}
		} catch (Exception e) {
			// 后台服务发生异常
			e.printStackTrace();
			data.put("result", 2);
			return data;
		}

		return data;
	}

	/**
	 * 我的求救
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @return
	 */
	@RequestMapping("appeallist_h5")
	@ResponseBody
	public Map<String, Object> appeallist(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {
		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
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

		ApiPage<ApiProject> projects = projectFacade.queryProjectList(ap,
				p.getPage(), p.getPageNum());
		Appeal pp = null;
		if (projects != null) {
			for (ApiProject project : projects.getResultData()) {
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
				String percent = StringUtil.doublePercentage(
						project.getDonatAmount(), project.getCryMoney(), 0);
				if (percent == null || percent.equals("null")) {
					percent = "0";
				}
				pp.setDonatePercent(percent);
				list.add(pp);
			}
			p.setNums(projects.getTotal());
		} else {
			p.setNums(0);
		}
		if (p.getTotal() == 0) {
			return webUtil.resMsg(2, "0002", "受捐列表为空", p);
		} else {
			return webUtil.resMsg(1, "0000", "成功", p);
		}
	}

	/**
	 * 我的捐助
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@RequestMapping("donationlist_h5")
	@ResponseBody
	public Map<String, Object> donationlist(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "pstate", required = false) Integer pstate) {

		if (userId == null) {
			// 1.验证是否登入
			userId = UserUtil.getUserId(request, response);
			if (userId == null) {
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
		ApiPage<ApiDonateRecord> donats = donateRecordFacade
				.queryMyDonateRecordList(r, p.getPage(), p.getPageNum());
		Donation d = null;
		if (donats != null && donats.getResultData().size() > 0) {
			for (ApiDonateRecord donat : donats.getResultData()) {
				d = new Donation();
				d.setdMoney(donat.getTotalAmount() == null ? 0.0 : donat
						.getTotalAmount()); // 捐款金额
				d.setCryMoney(donat.getCryMoney() == null ? 0.0 : donat
						.getCryMoney()); // 求助金额
				d.setDonatAmountpt(donat.getDonatAmountpt() == null ? 0.0
						: donat.getDonatAmountpt()); // 已募捐金额
				d.setdTime(donat.getDonatTime()); // 捐款时间

				d.setPid(donat.getProjectId()); // 项目id

				d.setTitle(donat.getProjectTitle()); // 项目名称
				d.setStatus(donat.getPstate() == null ? 300 : donat.getPstate()); // 项目状态
				d.setField(donat.getField() == null ? "" : donat.getField());

				d.setImagesurl(donat.getCoverImageurl());
				if (donat.getDonatType().equals("enterpriseDonation")) {
					d.setdType(1); // 助善
				} else {
					d.setdType(2);// 捐款
				}
				d.setDonateNum(donat.getDonateNum());
				d.setDonationNum(donat.getDonationNum());
				d.setName(donat.getUserName());
				list.add(d);
			}
			p.setNums(donats.getTotal());
		} else {
			p.setNums(0);
		}
		if (p.getTotal() == 0) {
			return webUtil.resMsg(2, "0002", "捐款列表为空", p);
		} else {
			return webUtil.resMsg(1, "0000", "成功", p);
		}
	}

	/**
	 * 客服代发
	 * 
	 * @param name
	 * @param qq
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping("appealByCustom_h5")
	public Map<String, Object> appealByCustom(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "qq", required = false) String qq,
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "reasion", required = true) String reasion) {
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
		if (rt != null && rt.getCode() == 1) {
			return webUtil.successRes(1);
		} else {
			return webUtil.failedRes("0101", "客服代发提交，没有成功", -1);
		}

	}

	@RequestMapping("view_feedback_h5")
	public ModelAndView view_feedback_h5(ProjectForm from,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {

		ModelAndView view = new ModelAndView("h5/project/feedback");
		view.addObject("projectId", from.getItemId());

		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		view = CommonUtils.wxView(view, request, perfecturl);

		return view;
	}

	/**
	 * 
	 * H5的项目反馈 * @param name
	 * 
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "H5careProjectList")
	@ResponseBody
	public Map<String, Object> careProjectList(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
			@RequestParam(value = "projectId", required = false) Integer projectId) {
		Page p = new Page();
		p.setPage(page);
		p.setPageNum(pageNum);
		List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
		p.setData(feedbacks);

		ApiProjectFeedback feedBack = new ApiProjectFeedback();
		feedBack.setAuditState(203);
		feedBack.setProjectId(projectId);

		ApiPage<ApiProjectFeedback> result = projectFacade
				.queryH5ProjectFeedbckByCondition(feedBack, p.getPage(),
						p.getPageNum());

		if (result != null) {
			PFeedBack tempf = null;
			for (ApiProjectFeedback f : result.getResultData()) {
				tempf = new PFeedBack();
				tempf.setId(f.getId());
				tempf.setContent(f.getContent());
				if (f.getFeedbackTime() != null) {
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
		} else {
			p.setNums(0);
		}
		if (p.getTotal() == 0) {
			return webUtil.resMsg(2, "0002", "没有数据", p);
		} else {
			return webUtil.successRes(p);
		}
	}

	/**
	 * 分享项目页
	 * 
	 * @param projectId
	 * @param extensionPeople
	 * @param request
	 * @param response
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping("view_share_h5")
	public ModelAndView view_share_h5(
			@RequestParam(value = "projectId", required = false) Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "shareType", required = false, defaultValue = "1") Integer shareType,
			HttpServletRequest request, HttpServletResponse response)
			throws JDOMException, IOException {

		ModelAndView view = new ModelAndView("h5/project/project_detail_share");
		String openId = "";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();// 捐款用户
		ApiFrontUser extensionUser = new ApiFrontUser();// 分享链接用户

		// 验证是否登入
		Integer loginUserId = UserUtil.getUserId(request, response);
		if (loginUserId != null && loginUserId != 0) {
			user = userFacade.queryById(loginUserId);
			view.addObject("user", user);
		} else {
			/*
			 * shareType : 0 分享指引 shareType : 1 分享成功
			 */
			System.out.println("view_share_h5 browser >> " + browser
					+ "  shareType >> " + shareType);
			if (browser.equals("wx") && shareType != null) {
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					Object OToken = redisService
							.queryObjectData("weixin_token");
					token = (String) OToken;
					System.out.println("view_share_h5 >> weixin_code = "
							+ weixin_code + "  openId = " + openId
							+ "  OToken = " + OToken);
					if ("".equals(openId) || openId == null || OToken == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo
									.getCodeRequest(perfecturl);
							view = new ModelAndView("redirect:"
									+ url_weixin_code);
							return view;
						}
						mapToken = CommonUtils
								.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						token = mapToken.get("access_token").toString();
						unionid = mapToken.get("unionid").toString();
						System.out
								.println("view_share_h5 >> tenpay.getAccessTokenAndopenidRequest openId "
										+ openId + " token = " + token);
						redisService.saveObjectData("weixin_token", token,
								DateUtil.DURATION_HOUR_S);
					}
					user = CommonUtils.queryUser(request, openId, token,
							unionid);
				} catch (Exception e) {
					logger.error("view_share_h5  >> " + e);
				}
				view.addObject("payway", browser);

				if (user.getCoverImageId() == null) {
					user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
				} else {
					if (user.getCoverImageId() != null
							&& user.getCoverImageId() == 0) {
						ApiBFile aBFile = fileFacade.queryBFileById(Integer
								.valueOf(user.getCoverImageId()));
						user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
					}
				}

			} else {
				// to do >> 暂时跳转到登陆页
				view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
				return view;

			}
		}

		if (extensionPeople != null) {
			CookieManager.create(CookieManager.PROJECTID_EXTENSIONPEOPLE,
					projectId + "_" + extensionPeople,
					CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			extensionUser = userFacade.queryById(extensionPeople);
			view.addObject("extensionUser", extensionUser);

		} else {
			CookieManager.create(CookieManager.PROJECTID_EXTENSIONPEOPLE,
					projectId + "_" + user.getId(),
					CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			view.addObject("extensionUser", user);
		}

		view.addObject("user", user);

		// 项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);

		view.addObject("project", project);
		double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			String contents = StringUtil.delHTMLTag(project.getContent());
			if (contents.length() > 100) {
				contents = contents.substring(0, 100);
			}
			view.addObject("desc", contents);
			project.setContent(StringUtil.convertToHtml(project.getContent()));
			process = 0.0;
			if (project.getCryMoney() >= 0.001) {
				process = project.getDonatAmount() / project.getCryMoney();
			}
			view.addObject(
					"process",
					process > 1 ? "100" : StringUtil.doublePercentage(
							project.getDonatAmount(), project.getCryMoney(), 0));
			view.addObject("processbfb", StringUtil.doublePercentage(
					project.getDonatAmount(), project.getCryMoney(), 0));
			if (userId != null && project.getUserId().equals(userId)) {
				// 是否是项目发起人
				view.addObject("owner", true);
			}

		}

		// 邀请的朋友捐款记录
		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		if (extensionPeople != null && extensionPeople != 0) {
			r.setExtensionPeople(extensionPeople);
		} else {
			r.setExtensionPeople(userId);
		}
		r.setState(302);
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, 1, 5);
		List<ApiDonateRecord> drList = new ArrayList<ApiDonateRecord>();
		drList = donats.getResultData();
		for (ApiDonateRecord dr : drList) {
			if (!StringUtils.isEmpty(dr.getUserName())
					&& (dr.getUserName().contains("游客") || dr.getUserName()
							.contains("weixin"))) {
				if (!StringUtils.isEmpty(dr.getTouristMessage())) {
					JSONObject dataJson = (JSONObject) JSONObject.parse(dr
							.getTouristMessage());
					// name = dataJson.getString("name");
					dr.setCoverImageurl(dataJson.getString("headimgurl"));
				} else if (dr.getCoverImageId() > 0) {
					ApiBFile bFile = fileFacade.queryBFileById(dr
							.getCoverImageId());
					dr.setCoverImageurl(bFile.getUrl());
				}
			} else {
				if (dr.getCoverImageId() > 0) {
					ApiBFile bFile = fileFacade.queryBFileById(dr
							.getCoverImageId());
					dr.setCoverImageurl(bFile.getUrl());
				}
			}
			if (StringUtils.isEmpty(dr.getCoverImageurl())) {
				dr.setCoverImageurl("http://www.17xs.org/res/images/detail/people_avatar.jpg");
			}
		}
		Integer total = donateRecordFacade.countNumQueryByCondition(r);
		double extensionDonateAmount = donateRecordFacade
				.countQueryByCondition(r);

		view.addObject("peopleNum", total); // 参与人次
		view.addObject("extensionDonateAmount", extensionDonateAmount); // 参与人捐款总额
		view.addObject("drList", drList); // 参与人
		// 目标金额与邀朋友捐金额的百分比
		view.addObject(
				"processPoint",
				process > 1 ? "100" : StringUtil.doublePercentage(
						extensionDonateAmount, project.getPointMoney(), 0));
		view.addObject(
				"processbPointfb",
				StringUtil.doublePercentage(extensionDonateAmount,
						project.getPointMoney(), 0));
		if (browser.equals("wx")) {
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
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,
					perfecturl);
			view.addObject("appId", map.get("appId"));
			view.addObject("timestamp", map.get("timeStamp"));
			view.addObject("noncestr", map.get("nonceStr"));
			view.addObject("signature", map.get("signature"));
		}
		view.addObject("projectId", projectId);
		view.addObject("shareType", shareType);
		return view;
	}

	/**
	 * 分享链接捐款记录
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@RequestMapping("extensionDonation")
	@ResponseBody
	public Map<String, Object> extensionDonation(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "projectId", required = false) Integer projectId) {

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
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, p.getPage(), p.getPageNum());
		Donation d = null;
		if (donats != null) {
			for (ApiDonateRecord donat : donats.getResultData()) {
				d = new Donation();
				d.setdMoney(donat.getDonatAmount()); // 捐款金额
				d.setCryMoney(donat.getCryMoney()); // 求助金额
				d.setDonatAmountpt(donat.getDonatAmountpt()); // 已募捐金额
				d.setdTime(donat.getDonatTime()); // 捐款时间

				d.setPid(donat.getProjectId()); // 项目id

				d.setTitle(donat.getProjectTitle()); // 项目名称
				d.setStatus(donat.getPstate()); // 项目状态
				d.setField(donat.getField());

				if (!StringUtils.isEmpty(donat.getUserName())
						&& (donat.getUserName().contains("游客") || donat
								.getUserName().contains("weixin"))) {
					if (!StringUtils.isEmpty(donat.getTouristMessage())) {
						JSONObject dataJson = (JSONObject) JSONObject
								.parse(donat.getTouristMessage());
						d.setImagesurl(dataJson.getString("headimgurl"));
					} else if (donat.getCoverImageId() > 0) {
						ApiBFile bFile = fileFacade.queryBFileById(donat
								.getCoverImageId());
						d.setImagesurl(bFile.getUrl());
					}
				} else {
					if (donat.getCoverImageId() > 0) {
						ApiBFile bFile = fileFacade.queryBFileById(donat
								.getCoverImageId());
						d.setImagesurl(bFile.getUrl());
					}
				}

				String name = donat.getNickName();
				if (!StringUtils.isEmpty(name) && name.contains("游客")) {
					if (!StringUtils.isEmpty(donat.getTouristMessage())) {
						JSONObject dataJson = (JSONObject) JSONObject
								.parse(donat.getTouristMessage());
						name = dataJson.getString("name");
						if (StringUtils.isEmpty(name)) {
							name = "爱心人士";
						}

					} else {
						name = "爱心人士";
					}
				}

				if (donat.getDonatType().equals("enterpriseDonation")) {
					d.setdType(1); // 助善
				} else {
					d.setdType(2);// 捐款
				}
				d.setDonateNum(donat.getDonateNum());
				d.setDonationNum(donat.getDonationNum());
				d.setName(name);
				d.setLeaveWord(donat.getLeaveWord() == null ? "" : donat
						.getLeaveWord());

				int d_value = DateUtil.minutesBetween(donat.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(donat.getDonatTime())
						.equals(DateUtil.dateFormat(DateUtil
								.getCurrentTimeByDate()));
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
		} else {
			p.setNums(0);
		}
		if (p.getTotal() == 0) {
			return webUtil.resMsg(2, "0002", "捐款列表为空", p);
		} else {
			return webUtil.resMsg(1, "0000", "成功", p);
		}
	}

	@RequestMapping("sendWish")
	@ResponseBody
	public Map<String, Object> sendWish(
			@RequestParam(value = "tranNum", required = false) String tranNum,
			@RequestParam(value = "leaveWord", required = false) String leaveWord,
			HttpServletRequest request, HttpServletResponse response) {

		if (tranNum == null) {
			return webUtil.resMsg(0, "0003", "交易号不能为空", null);
		}
		if (leaveWord == null) {
			return webUtil.resMsg(0, "0003", "祝福内容不能为空", null);
		}
		ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
		apiDonateRecord.setTranNum(tranNum);
		apiDonateRecord.setLeaveWord(leaveWord);
		int result = donateRecordFacade.updateLeaveWord(apiDonateRecord);

		if (result > 0) {
			return webUtil.resMsg(1, "0000", "成功", null);
		} else {
			return webUtil.resMsg(0, "0006", "更改失败", null);
		}
	}

	@RequestMapping("toSendWish")
	public ModelAndView toSendWish(
			@RequestParam(value = "tradeNo", required = false) String tradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/userRelation/sendWish");
		view.addObject("tradeNo", tradeNo);
		String projectId = request.getParameter("projectId") == null ? ""
				: request.getParameter("projectId");
		String headImage = request.getParameter("headImage") == null ? ""
				: request.getParameter("headImage");
		String nickName = request.getParameter("nickName") == null ? ""
				: request.getParameter("nickName");
		String amount = request.getParameter("amount") == null ? "" : request
				.getParameter("amount");
		String title = request.getParameter("title") == null ? "" : request
				.getParameter("title");
		view.addObject("projectId", projectId);
		view.addObject("headImage", headImage);
		view.addObject("nickName", nickName);
		view.addObject("amount", amount);
		view.addObject("title", title);
		view.addObject("fromWish", "fromWish");
		return view;
	}

	@RequestMapping("feedBackList_h5")
	public ModelAndView feedBackList(
			@RequestParam(value = "projectId", required = false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/project/feedBackList");
		view.addObject("projectId", projectId);
		return view;
	}

	@RequestMapping("feedBack_h5")
	public ModelAndView feedBack(
			@RequestParam(value = "projectId", required = false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/project/feedBack_h5");
		view.addObject("projectId", projectId);
		return view;
	}

	@RequestMapping("volunteer")
	public ModelAndView volunteer(
			@RequestParam(value = "projectId", required = false) Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/project/postulant");
		view.addObject("projectId", projectId);
		return view;
	}

	@RequestMapping("batch_list")
	public ModelAndView batch_project_list(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			@RequestParam(value = "topicId", required = false, defaultValue = "0") Integer topicId) {
		ModelAndView view = new ModelAndView("h5/project/project_list_batch");

		// ===========微信用户自动登陆start==============//

		String openId = "";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();// 捐款用户
		Integer userId = UserUtil.getUserId(request, response);
		view.addObject("extensionPeople", extensionPeople);
		if (userId != null && userId != 0) {
			user = userFacade.queryById(userId);
			view.addObject("user", user);
		} else {
			// browser = "wx" ;
			if (browser.equals("wx")) {
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					openId = (String) request.getSession().getAttribute(
							"openId");
					Object OToken = redisService
							.queryObjectData("weixin_token");
					token = (String) OToken;
					System.out.println("userCenter_h5 >> weixin_code = "
							+ weixin_code + "  openId = " + openId
							+ "  OToken = " + OToken);
					// openId = "oxmc3uOwjzQxu3u18E37e-n1ytwU";
					// OToken = "test" ;
					if ("".equals(openId) || openId == null || OToken == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo
									.getCodeRequest(perfecturl);
							view = new ModelAndView("redirect:"
									+ url_weixin_code);
							return view;
						}
						mapToken = CommonUtils
								.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						unionid = mapToken.get("unionid").toString();
						token = mapToken.get("access_token").toString();
						request.getSession().setAttribute("openId", openId);
						System.out
								.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "
										+ openId + " token = " + token);
						redisService.saveObjectData("weixin_token", token,
								DateUtil.DURATION_HOUR_S);
					}
					user = CommonUtils.queryUser(request, openId, token,
							unionid);
				} catch (Exception e) {
					logger.error("微信支付处理出现问题" + e);
				}
				view.addObject("payway", browser);

				if (user.getCoverImageId() == null) {
					user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
				} else {
					if (user.getCoverImageId() != null
							&& user.getCoverImageId() == 0) {
						ApiBFile aBFile = fileFacade.queryBFileById(Integer
								.valueOf(user.getCoverImageId()));
						user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
					}
				}

				try {
					// 自动登录
					SSOUtil.login(user, request, response);
					view.addObject("user", user);
				} catch (Exception e) {
					logger.error("", e);
				}

			}

		}
		// ===========微信用户自动登陆end==============//
		view.addObject("topicId", topicId);// 专题id
		return view;
	}
	/**
	 * 项目众筹选择奖励页
	 * 
	 * @param projectId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("toCrowdfundingPrize")
	public ModelAndView toCrowdfundingPrize(
			@RequestParam(value = "projectId", required = true) Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView view = new ModelAndView("h5/project/crowdfunding_prize");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null || userId == 0) {
			String browser = UserUtil.Browser(request);
			String openId = "";
			String Token = "";
			String unionid = "";
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			if (browser.equals("wx")) {
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					if ("".equals(openId) || openId == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo
									.getCodeRequest(perfecturl);
							view = new ModelAndView("redirect:"
									+ url_weixin_code);
							return view;
						}
						mapToken = CommonUtils
								.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						Token = mapToken.get("access_token").toString();
						unionid = mapToken.get("unionid").toString();
						redisService.saveObjectData("weixin_token", Token,
								DateUtil.DURATION_HOUR_S);
					}
				} catch (Exception e) {
					logger.error("微信支付处理出现问题" + e);
				}

				ApiFrontUser user = CommonUtils.queryUser(request, openId,
						Token, unionid);
				try {
					// 自动登录
					SSOUtil.login(user, request, response);
				} catch (Exception e) {
					logger.error("weixindeposit2 >> SSOUtil.login : " + e);
				}
				view.addObject("user", user);
			}
		}
		ApiProject p = projectFacade.queryProjectDetail(projectId);
		if (StringUtils.isNotEmpty(p.getProjectPrize())) {
			List<Integer> GiftIds = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(p.getProjectPrize())) {
				String[] ids = p.getProjectPrize().split(",");
				for (String str : ids) {
					if (StringUtils.isNotEmpty(str)) {
						GiftIds.add(Integer.valueOf(str));
					}
				}
			}
			ApiGift apiGift = new ApiGift();
			apiGift.setIds(GiftIds);
			apiGift.setOrderBy("score");
			apiGift.setOrderDirection("asc");
			ApiPage<ApiGift> gifts = userRelationInfoFacade
					.queryGiftByCodition(apiGift, 1, GiftIds.size());
			if (gifts != null && gifts.getResultData().size() > 0) {
				Integer count = 0;
				ApiFrontUserProjectPrize aProjectPrize = new ApiFrontUserProjectPrize();
				aProjectPrize.setProjectId(projectId);
				for (ApiGift g : gifts.getResultData()) {
					aProjectPrize.setGiftId(g.getId());
					count = userRelationInfoFacade
							.countSupportNum(aProjectPrize);
					g.setSupportNum(count);
				}
			}
			view.addObject("projectPrize", gifts.getResultData());
		}

		view.addObject("project", p);
		view.addObject("extensionPeople", extensionPeople);

		return view;
	}

	@RequestMapping(value = "confirmCrowdfundingPrize")
	public ModelAndView confirmCrowdfundingPrize(
			@RequestParam(value = "projectId", required = true) Integer projectId,
			@RequestParam(value = "giftId", required = true) Integer giftId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/project/goods_order");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser_address r = new ApiFrontUser_address();
		r.setUserId(userId);
		List<ApiFrontUser_address> addresses = userRelationInfoFacade
				.queryUserAddress(r, 1, 5);
		if (addresses != null && addresses.size() > 0) {
			view.addObject("address", addresses.get(0));
		}
		ApiProject p = projectFacade.queryProjectDetail(projectId);
		view.addObject("project", p);
		ApiGift gift = userRelationInfoFacade.queryGiftById(giftId);
		view.addObject("gift", gift);
		view.addObject("extensionPeople", extensionPeople);
		return view;
	}

	@RequestMapping(value = "isEmptyUserId")
	public JSONObject isEmptyUserId(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject data = new JSONObject();
		Integer userId = UserUtil.getUserId(request, response);
		if (userId != null)
			data.put("userId", userId);
		else
			data.put("userId", 0);
		return data;
	}

	/**
	 * 加载项目反馈和项目进度
	 * 
	 * @param page
	 * @param pageNum
	 * @return
	 */
	@RequestMapping("loadFeedBackAndReportList")
	@ResponseBody
	public JSONObject loadFeedBackAndReportList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "5") Integer pageNum) {
		JSONObject item = new JSONObject();
		JSONArray items = new JSONArray();
		ApiPage<ApiProjectFeedback> apiPage = projectFacade
				.queryFeedbackAndReportList(page, pageNum);
		if (apiPage != null && apiPage.getTotal() > 0) {
			for (ApiProjectFeedback feedback : apiPage.getResultData()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("headImgUrl", feedback.getHeadImageUrl());
				jsonObject.put("userName", feedback.getUserName());
				jsonObject.put("feedbackTime", feedback.getFeedbackTime());
				jsonObject.put("content", feedback.getContent());
				jsonObject.put("contentImgUrl", feedback.getContentImageUrl());
				jsonObject.put("projectId", feedback.getProjectId());
				items.add(jsonObject);
			}
		}
		item.put("result", items);
		item.put("total", apiPage.getTotal());
		return item;
	}

	/**
	 * 单个发起人项目发布列表发起人信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "oneProjectFaqiUserinformation", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject oneProjectFaqiUserinformation(
			@RequestParam(value = "userId", required = true) Integer userId) {
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiProject aProject = projectFacade
				.countDonatePeopleByUserId(apiProject);
		if (aProject != null) {
			// 捐赠总次数
			result.put("donationNum", aProject.getDonationNum() == null ? 0
					: aProject.getDonationNum());
			// 捐赠总金额
			result.put("donatAmount", aProject.getDonatAmount() == null ? 0.00
					: aProject.getDonatAmount());
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(userId);
			apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			if (apiCompany != null) {
				result.put("name", apiCompany.getName());
				result.put("introduction", apiCompany.getIntroduction());
				result.put("coverImageUrl", apiCompany.getCoverImageUrl());
				item.put("code", 1);
				item.put("msg", "success");
				item.put("result", result);
			} else {
				item.put("code", 3);
				item.put("msg", "企业不存在");
			}
		} else {
			item.put("code", 3);
			item.put("msg", "项目不存在");
		}
		return item;
	}

	/**
	 * 单个发起人项目发布列表
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "oneProjectFaqiUserList", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject oneProjectFaqiUserList(
			@RequestParam(value = "pageSize", required = false, defaultValue = "1") Integer pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
			@RequestParam(value = "userId", required = true) Integer userId) {
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();
		ApiProject apiProject = new ApiProject();
		List<Integer> states = new ArrayList<Integer>();
		states.add(240);
		states.add(260);
		apiProject.setStates(states);
		apiProject.setUserId(userId);
		apiProject.setIsHide(0);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, pageNum, pageSize);
		if (apiPage != null
				&& apiPage.getTotal() > 0
				&& (apiPage.getTotal() - pageNum * pageSize >= 0 || pageNum
						* pageSize - apiPage.getTotal() < pageSize)) {
			for (ApiProject ap : apiPage.getResultData()) {
				JSONObject data = new JSONObject();
				data.put("projectId", ap.getId());
				data.put("cryMoney", ap.getCryMoney());
				data.put("donatAmount", ap.getDonatAmount());
				data.put("donationNum", ap.getDonationNum());
				data.put("coverImageUrl", ap.getCoverImageUrl());
				data.put("title", ap.getTitle());
				data.put("state", ap.getState());
				items.add(data);
			}
			result.put("count", apiPage.getTotal());
			result.put("data", items);
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}

	/**
	 * 义卖项目详情
	 * 
	 * @param projectId
	 * @param request
	 * @return
	 */
	@RequestMapping("/bazaarProjectDetail")
	public ModelAndView bazaar_project_detail(
			@RequestParam(value = "projectId", required = true) Integer projectId,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		// 判断浏览器 项目类型 普通项目 日日捐项目 专项基金项目
		String isMobile = (String) request.getSession().getAttribute("ua");
		// pc端带extensionPeople和不带extensionPeople
		if (!"mobile".equals(isMobile) && extensionPeople != null) {
			view.setViewName("redirect:/project/view.do?projectId=" + projectId
					+ "&extensionPeople=" + extensionPeople);
			return view;
		}
		if (!"mobile".equals(isMobile)) {
			view.setViewName("redirect:/project/view.do?projectId=" + projectId);
			return view;
		}
		// h5
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		if ("mobile".equals(isMobile) && project.getSpecial_fund_id() > 0) {// 专项基金
			if (extensionPeople == null) {
				view.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId="
						+ projectId);
			} else {
				view.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId="
						+ projectId + "&extensionPeople=" + extensionPeople);
			}
			return view;
		}
		if ("mobile".equals(isMobile) && project.getDaydayDonate() == 1) {// 日日捐
			if (extensionPeople == null) {
				view.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId="
						+ projectId);
			} else {
				view.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId="
						+ projectId + "&extensionPeople=" + extensionPeople);
			}
		}
		/*
		 * if("mobile".equals(isMobile) &&
		 * StringUtils.isBlank(project.getJumbleSale())){//普通项目
		 * if(extensionPeople == null){
		 * view.setViewName("redirect:/project/view_h5.do?projectId=" +
		 * projectId); }else{
		 * view.setViewName("redirect:/project/view_h5.do?projectId=" +
		 * projectId + "&extensionPeople="+extensionPeople); } return view; }
		 */

		String browser = UserUtil.Browser(request);
		view.addObject("project", project);

		String[] giftIds = project.getJumbleSale().split(",");
		List<ApiGift> list = new ArrayList<ApiGift>();
		Integer nums = 0;
		for (String giftId : giftIds) {
			ApiGift gift = userRelationInfoFacade.queryGiftById(Integer
					.valueOf(giftId));
			gift.setBuyPrice(Double.valueOf(gift.getScore()) / 100);
			list.add(gift);
			nums += gift.getBuyNumber();
		}
		view.addObject("gifts", list);
		// 支持数量
		view.addObject("nums", nums == null ? 0 : nums);

		// 分享参数
		if (browser.equals("wx")) {
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
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map;
			try {
				map = H5Demo.getConfigweixin(jsTicket, perfecturl);
				view.addObject("appId", map.get("appId"));
				view.addObject("timestamp", map.get("timeStamp"));
				view.addObject("noncestr", map.get("nonceStr"));
				view.addObject("signature", map.get("signature"));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}

		/* 随机获取祝福语 */
		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setConfigKey(StringUtil.LEAVEWORD);
		List<ApiConfig> configs = commonFacade.queryList(apiConfig);
		if (configs != null && configs.get(0) != null
				&& configs.get(0).getConfigValue() != null
				&& configs.get(0).getConfigValue() != "") {
			String[] configss = configs.get(0).getConfigValue().split("。");
			Random random = new Random();
			int i = random.nextInt(configss.length);
			view.addObject("leaveWord", configss[i]);
		}

		/* 点击量统计start */
		try {
			// 获取当天剩余缓存时间
			long startTime = (DateUtil.getCurrentDayEnd().getTime() - new Date()
					.getTime()) / 1000;
			String clickRate = (String) redisService
					.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_H5
							+ projectId);
			if (StringUtils.isEmpty(clickRate)) {
				redisService.saveObjectData(
						PengPengConstants.PROJECT_CLICKRATE_H5 + projectId,
						"t_h5_" + projectId + "_" + 1, startTime);
			} else {
				String cstr[] = clickRate.split("_");
				if (null != cstr && cstr.length >= 4) {
					Integer click = Integer.parseInt(cstr[3]);
					if (null == click) {
						click = 1;
					} else {
						click += 1;
					}

					redisService.saveObjectData(
							PengPengConstants.PROJECT_CLICKRATE_H5 + projectId,
							"t_h5_" + projectId + "_" + click, startTime);
				}
			}
		} catch (Exception e) {
		}
		/* 点击量统计end */
		// 用户电话、姓名
		Integer userId = UserUtil.getUserId(request, response);
		if (userId != null) {
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("realName", user.getRealName());
			view.addObject("mobile", user.getMobileNum());
		}
		// 项目发起人
		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
		userInfo.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
		userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
		List<ApiProjectUserInfo> userInfos = projectFacade
				.queryProjectUserInfoList(userInfo);
		for (ApiProjectUserInfo u : userInfos) {
			if (u.getPersonType() == 2) {
				view.addObject("fabu", u);
			}
		}
		view.setViewName("h5/project/bazaar_project_detail");
		return view;
	}

	/**
	 * 获取醒目发布人信息
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "getProjectFaBuUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject projectUserInfo(
			@RequestParam("projectId") Integer projectId) {
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
		userInfo.setProjectId(projectId);
		String key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + projectId;
		userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
		List<ApiProjectUserInfo> userInfos = projectFacade
				.queryProjectUserInfoList(userInfo);
		for (ApiProjectUserInfo u : userInfos) {
			if (u.getPersonType() == 2) {
				result.put("workUnit", u.getWorkUnit());
				result.put("linkMan", u.getRealName());
				result.put("linkMobile", u.getLinkMobile());
				result.put("familyAddress", u.getFamilyAddress());
				result.put("headImageUrl", u.getHeadImageUrl());
			}
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}

}
