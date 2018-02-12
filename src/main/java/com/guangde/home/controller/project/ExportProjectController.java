package com.guangde.home.controller.project;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IPayMoneyRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CompressPicUtil;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.ImageUtil;
import com.guangde.home.utils.MD5Utils;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Appeal;
import com.guangde.home.vo.project.Donation;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.utils.RequestHandler;

@Controller
@RequestMapping("releaseProject")
public class ExportProjectController {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ICommonFacade ICommonFacade;

	@Autowired
	private IProjectFacade projectFacade;

	@Autowired
	private IDonateRecordFacade donateRecordFacade;

	@Autowired
	private ICompanyFacade companyFacade;

	// 对用户进行操作的方法调用
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
	private IProjectVolunteerFacade projectVolunteerFacede;

	/**
	 * md5Utils用于接收加密的参数 申请求助项目人员项目的数据
	 */
	@ResponseBody
	@RequestMapping(value="appealUserInfoData")
	public Map<String, Object> appealViewThreeData(
			@RequestParam(value = "type", defaultValue = "1") String type,
			@RequestParam(value = "companyId") Integer zlId,
			@RequestParam(value = "userId", required = true) Integer userId,
			@RequestParam(value = "helpType") String helpType, Appeal appel,
			MD5Utils md5Utils, HttpServletRequest request,
			HttpServletResponse response) {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 进行加密判断
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("type", type);
		packageParams.put("companyId", zlId + "");
		packageParams.put("userId", userId + "");
		packageParams.put("helpType", helpType);
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("relation", appel.getRelation());
		packageParams.put("appealName", appel.getAppealName());
		packageParams.put("appealIdcard", appel.getAppealIdcard());
		packageParams.put("appealPhone", appel.getAppealPhone());
		packageParams.put("appealAddress", appel.getAppealAddress());
		packageParams.put("detailAddress", appel.getDetailAddress());
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flag = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		if (!flag) {
			// 返回为-2时验签不正确
			String backSign = reqHandler.createSignNoKey(sm);
			item.put("backSign", backSign);
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		ApiFrontUser user = null;
		String workUnit = "";
		String projectType = "";
		if (userId == null || "".equals(userId)) {
			String backSign = reqHandler.createSignNoKey(sm);
			item.put("backSign", backSign);
			return webUtil.resMsg(0, "0001", "发布人id不能为空", item);
		} else {
			// 发布人信息为登录人员信息
			if ("0".equals(type)) {
				user = userFacade.queryById(userId);
				appel.setAddress(user.getFamilyAddress());
				appel.setPhone(user.getMobileNum());
				appel.setName(user.getRealName());
				appel.setIdcard(user.getIdCard());
				appel.setWeixin(user.getQqOrWx());
				appel.setGzdw(user.getWorkUnit());
				appel.setZy(user.getPersition());
				projectType = "personalItems";
				// 当为本人求助的时候，获取前面验证的的图片
				if ("1".equals(helpType)) {
					appel.setRelation("本人");

					if (user.getContentImageId() != null
							&& !"".equals(user.getContentImageId())) {
						appel.setImgIds(user.getContentImageId() + ","
								+ appel.getImgIds());
					}
				}
			} else {
				ApiCompany ac = new ApiCompany();
				ac.setId(zlId);
				ac.setUserId(userId);
				ApiCompany company = companyFacade.queryCompanyByParam(ac);
				if (company == null) {
					String backSign = reqHandler.createSignNoKey(sm);
					item.put("backSign", backSign);
					return webUtil.resMsg(0, "0005", "无此机构信息", item);
				}
				appel.setName(company.getName());
				if (company.getName() != null) {
					workUnit = company.getName();
				}
				appel.setIdcard(company.getGroupCode());
				appel.setAddress(company.getAddress());
				appel.setPhone(company.getMobile());
				projectType = "enterpriseProject";
				if (company.getContentImageId() != null
						&& !"".equals(company.getContentImageId())) {
					appel.setImgIds(company.getContentImageId() + ","
							+ appel.getImgIds());
				}
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("appel:" + appel.toString() + "userId:" + userId);
		}

		// ApiProject project = projectFacade.queryProjectDetail(appel.getId());
		ApiProject project = new ApiProject();
		project.setUserId(userId);
		project.setPayMethod("支付宝");
		project.setTitle("暂无");
		project.setLocation("未知");
		project.setDetailAddress("未知");
		project.setCryMoney(0.00);
		project.setIdentity("未知");
		project.setState(200);
		project.setContent("暂无");
		project.setIssueTime(new Date());
		project.setType(projectType);
		if (appel.getImgIds() != null && !"".equals(appel.getImgIds())) {
			project.setContentImageId(appel.getImgIds());
		}
		ApiResult result1 = projectFacade.launchProject(project);
		if (result1 == null) {
			String backSign = reqHandler.createSignNoKey(sm);
			item.put("backSign", backSign);
			return webUtil.resMsg(0, "0005", "没有这个项目", item);
		} else {
			appel.setId(Integer.parseInt(result1.getMessage()));
		}

		// 判断项目是否符合要求
		if (project.getState() != ProjectConstant.PROJECT_STATUS_DRAFT) {
			String backSign = reqHandler.createSignNoKey(sm);
			item.put("backSign", backSign);
			return webUtil.resMsg(0, "0002", "状态错误", item);
		}
		if (appel.getType() != 0) {
			project.setState(ProjectConstant.PROJECT_STATUS_AUDIT1);
		}
		project.setLastUpdateTime(new Date());
		List<ApiProjectUserInfo> aList = new ArrayList<ApiProjectUserInfo>(3);
		ApiProjectUserInfo api = new ApiProjectUserInfo();

		try {
			// 证明人
			api.setProjectId(appel.getId());
			if (appel.getRname() != null) {
				api.setRealName(webUtil.encodeTodecode(appel.getRname()));
			}
			if (appel.getRname() != null) {
				api.setLinkMan(webUtil.encodeTodecode(appel.getRname()));
			}
			if (appel.getrAddress() != null) {
				api.setWorkUnit(webUtil.encodeTodecode(appel.getrAddress()));
			}
			api.setLinkMobile(appel.getrPhone());
			if (appel.getRzw() != null) {
				api.setPersition(webUtil.encodeTodecode(appel.getRzw()));
			}
			api.setPersonType(1);
			api.setHelpType(Integer.parseInt(helpType));
			api.setCreateTime(new Date());
			aList.add(api);
			// 发布人
			api = new ApiProjectUserInfo();
			api.setProjectId(appel.getId());
			api.setRelation(appel.getRelation());
			user = userFacade.queryById(userId);
			if (user.getRealName() != null) {
				api.setRealName(webUtil.encodeTodecode(user.getRealName()));
			}
			if (workUnit != null && !"".equals(workUnit)) {
				api.setWorkUnit(workUnit);
			}
			if ("0".equals(type)) {
				api.setIndetity(appel.getIdcard());
			}
			if (appel.getAddress() != null) {
				api.setFamilyAddress(webUtil.encodeTodecode(appel.getAddress()));
			}
			if (appel.getZy() != null) {
				api.setVocation(webUtil.encodeTodecode(appel.getZy()));
			}
			if (appel.getGzdw() != null) {
				api.setWorkUnit(webUtil.encodeTodecode(appel.getGzdw()));
			}
			if (appel.getIdentity() != null) {
				api.setRelation(webUtil.encodeTodecode(appel.getIdentity()));
			}
			api.setLinkMobile(appel.getPhone());
			if (user.getQqOrWx() != null) {
				api.setQqOrWx(webUtil.encodeTodecode(user.getQqOrWx()));
			}
			api.setPersonType(2);
			api.setHelpType(Integer.parseInt(helpType));
			api.setCreateTime(new Date());
			aList.add(api);
			// 受助人
			api = new ApiProjectUserInfo();
			api.setProjectId(appel.getId());
			api.setPersonType(0);
			if (appel.getAppealName() != null) {
				api.setRealName(webUtil.encodeTodecode(appel.getAppealName()));
			}
			if (appel.getAppealName() != null) {
				api.setLinkMan(webUtil.encodeTodecode(appel.getAppealName()));
			}
			if (appel.getAppealName() != null) {
				api.setRealName(webUtil.encodeTodecode(appel.getAppealName()));
			}
			if (appel.getAppealSex() != null) {
				api.setSex(webUtil.encodeTodecode(appel.getAppealSex()));
			}
			api.setAge(appel.getAppealAge());
			api.setIndetity(appel.getAppealIdcard());
			if (appel.getAppealAddress() != null) {
				api.setFamilyAddress(webUtil.encodeTodecode(appel
						.getAppealAddress()));
			}
			if (appel.getDetailAddress() != null) {
				api.setFamilyAddress(api.getFamilyAddress()
						+ webUtil.encodeTodecode(appel.getDetailAddress()));
			}
			if (appel.getAppealzy() != null) {
				api.setVocation(webUtil.encodeTodecode(appel.getAppealzy()));
			}
			if (appel.getAppealgzdw() != null) {
				api.setWorkUnit(webUtil.encodeTodecode(appel.getAppealgzdw()));
			}
			api.setLinkMobile(appel.getAppealPhone());
			api.setHelpType(Integer.parseInt(helpType));
			api.setCreateTime(new Date());
			aList.add(api);
		} catch (UnsupportedEncodingException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("不支持编码异常" + e.getStackTrace());
			}
		}
		ApiResult res = projectFacade.updateProject(project);
		if (res != null && res.getCode() == 1) {
			// todo projectid
			api = new ApiProjectUserInfo();
			api.setProjectId(appel.getId());
			List<ApiProjectUserInfo> alist = projectFacade
					.queryProjectUserInfoList(api);
			if (alist.size() > 0) {
				if (alist.size() == 3) {
					res = projectFacade.updateProjectUserInfo(aList);
				} else {
					String backSign = reqHandler.createSignNoKey(sm);
					item.put("backSign", backSign);
					return webUtil.resMsg(0, "0004", "这个项目的人员信息有误，请另外换个项目发布",
							item);
				}
			} else {
				res = projectFacade.saveProjectUserInfo(aList);
			}
			String key = PengPengConstants.PROJECT_USERINFO_LIST + "_"
					+ appel.getId();
			redisService.deleteData(key);
			if (res != null && res.getCode() == 1) {

				String backSign = reqHandler.createSignNoKey(sm);
				item.put("backSign", backSign);
				item.put("projectId", appel.getId());
				return webUtil.resMsg(1, "0000", "成功", item);
			} else {
				String backSign = reqHandler.createSignNoKey(sm);
				item.put("backSign", backSign);
				return webUtil.resMsg(0, "0003", "添加相关联系人信息失败", item);
			}
		} else {
			String backSign = reqHandler.createSignNoKey(sm);
			item.put("backSign", backSign);
			return webUtil.resMsg(0, "0004", "修改项目失败", item);
		}
	}

	/*
	 * 申请求助项目详情的数据
	 */
	@ResponseBody
	@RequestMapping(value="appealProjectInfoData",produces={"application/json"})
	public Map<String, Object> appealViewSecondData(Appeal appel,
			Integer userId, MD5Utils md5Utils, HttpServletRequest request,
			HttpServletResponse response) {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("userId", userId + "");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("title", appel.getTitle());
		packageParams.put("cryMoney", df.format(appel.getCryMoney()));
		packageParams.put("deadline",appel.getDeadline()+"");
		packageParams.put("status", appel.getStatus()+"");
		packageParams.put("field", appel.getField());
		packageParams.put("timeStamp", md5Utils.getTimeStamp()+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flag = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flag) {
			// 返回为-2时验签不正确
			//return webUtil.resMsg(-2, "0001", "验签错误", item);
		}

		if (userId == null || "".equals(userId)) {
			return webUtil.resMsg(-1, "0001", "该用户不存在，无法发布项目", item);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("appel:" + appel.toString() + "userId:" + userId);
		}
		String ids[] = appel.getImgIds().split(",");
		Integer titleImageId = 0;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null && !"".equals(ids[i])) {
				titleImageId = Integer.parseInt(ids[i]);
				break;
			}
		}
		if (appel.getStatus() != ProjectConstant.PROJECT_STATUS_AUDIT1) {
			return webUtil.resMsg(0, "0002", "状态错误", item);
		} else {
			if (appel.getCryMoney() < 200) {
				return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,
						"求助金额不能小于200", item);
			}
			if (appel.getCryMoney() > 100000000) {
				return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,
						"求助金额不能大于100000000", item);
			}
			// }
			int way = 0;
			ApiProject project = null;
			if (appel.getId()!=null&&appel.getId() > 0) {
				project = projectFacade.queryProjectDetail(appel.getId());
				if (project == null) {
					way = 1;
					project = new ApiProject();
					project.setContentImageId(appel.getImgIds());
				} else {
					project.setContentImageId(appel.getImgIds());
					if(project.getState()==240||project.getState()==260)
						return webUtil.resMsg(-2, "0001", "项目在审核中或已结束，不能修改项目信息", item);
				}
			} else {
				way = 1;
				project = new ApiProject();
				project.setContentImageId(appel.getImgIds());
			}
			project.setId(appel.getId());
			project.setTitle(appel.getTitle());
			project.setState(appel.getStatus());
			project.setContent(appel.getContent());
			project.setIdentity("未知");
			project.setPayMethod("alipay");
			project.setCryMoney(appel.getCryMoney());
			project.setDetailAddress(appel.getDetailAddress());
			project.setLocation(appel.getLocation());
			project.setIssueTime(new Date());
			ApiTypeConfig apiTypeConfig = new ApiTypeConfig();
			if(appel.getField()!=null){
				apiTypeConfig.setTypeName_e(appel.getField());
				apiTypeConfig = commonFacade.queryApiTypeConfig(apiTypeConfig);
			}
			if(apiTypeConfig!=null){
				project.setField(apiTypeConfig.getTypeName_e());
				project.setFieldChinese(apiTypeConfig.getTypeName());
			}
			project.setType("enterpriseProject");
			// 设置为标题图
			project.setCoverImageId(titleImageId);
			if (appel.getDeadline() > 365) {
				return webUtil.resMsg(0, "0008", "募捐时间不能超过一年", item);
			}
			Long dl = (long) (new Date().getTime() + appel.getDeadline() * 24 * 3600 * 1000);
			appel.setDeadline(dl);
			if (appel.getDeadline() != 0) {
				project.setDeadline(new Date(appel.getDeadline()));
			}
			if (way == 0) {
				if (StringUtils.isNotEmpty(appel.getField())) {
					ApiTypeConfig ag = new ApiTypeConfig();
					ag.setTypeName_e(appel.getField());
					String key = PengPengConstants.SEEKHELP_FIELD_INFO + "_"
							+ appel.getField();
					ag.initNormalCache(true, DateUtil.DURATION_WEEK_S, key);
					ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
					if (atc == null) {
						return webUtil.resMsg(0, "0003", "没有这个领域", item);
					}
					project.setField(appel.getField());
					project.setFieldChinese(atc.getTypeName());
				} else {
					return webUtil.resMsg(0, "0003", "没有上传领域的参数值", item);
				}
			}
			project.setUserId(userId);
			project.setLastUpdateTime(new Date());

			project.setIsNeedVolunteer(appel.getIsNeedVolunteer());
			project.setIsCanHelp(0);
			ApiResult result = null;
			if (way == 0) {
				result = projectFacade.updateProject(project);
			} else {
				result = projectFacade.launchProject(project);
			}
			if (result.getCode() == 1) {
				// 当项目发布成功时，设置标题图(默认为第一张)

				JSONObject js = new JSONObject();
				if (way == 0) {
					js.put("projectId", project.getId());
				} else {
					js.put("projectId", result.getMessage());
				}

				js.put("help", appel.getIdentity());
				item.put("projectId", js.get("projectId"));
				item.put("help", js.get("help"));
				return webUtil.resMsg(1, "0000", "成功", item);
			} else {
				return webUtil.resMsg(0, "0003", "添加失败", item);
			}
		}
	}

	/*
	 * 当前项目的捐赠列表
	 */
	@RequestMapping("projectDetailsdonationlist")
	@ResponseBody
	public Map<String, Object> donationlist(
			HttpServletRequest request,
			HttpServletResponse response,MD5Utils md5Utils,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {
        
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp()+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flag = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flag) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		if (id == null) {
			return webUtil.resMsg(0, "0001", "项目id不能为空", item);
		}
		// 分页计算
		Page p = new Page();
		p.setPageNum(pageNum);
		p.setPage(page);
		ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(id);
		r.setState(302);
		List<String> llist = new ArrayList<String>(1);
		llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(),
				BaseBean.RANGE_WHOLE, id));
		r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId", "state");
		ApiPage<ApiDonateRecord> resultPage = donateRecordFacade
				.queryByCondition(r, p.getPage(), p.getPageNum());
		List<Donation> list = new ArrayList<Donation>();
		p.setData(list);
		Donation d;
		if (resultPage.getTotal() == 0) {
			p.setNums(0);
		} else {
			for (ApiDonateRecord dr : resultPage.getResultData()) {
				// 捐款记录
				d = new Donation();
				d.setId(dr.getId());
				d.setdMoney(dr.getDonatAmount());
				d.setdTime(dr.getDonatTime());
				if (dr.getDonateCopies() != null) {
					d.setCopies(dr.getDonateCopies());
				}
				String name = dr.getNickName();
				if (!StringUtils.isEmpty(name) && name.contains("游客")) {
					name = "爱心人士";
					if (!StringUtils.isEmpty(dr.getTouristMessage())) {
						JSONObject dataJson = (JSONObject) JSONObject.parse(dr
								.getTouristMessage());
						name = dataJson.getString("name");
						if (StringUtils.isEmpty(name)) {
							name = "爱心人士";
						}
						d.setImagesurl(dataJson.getString("headimgurl"));
					}
				} else {
					if (dr.getCoverImageId() > 0) {
						ApiBFile bFile = fileFacade.queryBFileById(dr
								.getCoverImageId());
						d.setImagesurl(bFile.getUrl());
					}
				}
				d.setName(name);
				d.setTitle(dr.getProjectTitle());
				if (dr.getLeaveWord() == null) {
					d.setLeaveWord(null);
				} else if (dr.getLeaveWord().contains("transfer:")) {
					d.setLeaveWord(dr.getLeaveWord().replace("transfer:", ""));
					d.setdType(6);
				} else {
					d.setLeaveWord(dr.getLeaveWord());
				}

				int d_value = DateUtil.minutesBetween(dr.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flags = DateUtil.dateFormat(dr.getDonatTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flags) {
					dr.setTimeStamp(DateUtil.dateStringChinesePatternYD(
							"yyyy年MM月dd日", dr.getDonatTime()));
				} else {
					if (d_value / 60 >= 1) {
						dr.setTimeStamp(d_value / 60 + "小时前");
					} else {
						if (d_value == 0) {
							dr.setTimeStamp("刚刚");
						} else {
							dr.setTimeStamp(d_value + "分钟前");
						}
					}
				}
				d.setShowTime(dr.getTimeStamp());

				list.add(d);
			}
			p.setNums(resultPage.getTotal());
		}

		return webUtil.successResDoubleObject(p, item);
	}

	@ResponseBody
	@RequestMapping(value = "upload", produces = { "text/html;charset=UTF-8" })
	public Map<String, Object> upload3(@RequestParam("file") CommonsMultipartFile file,
			@RequestParam("type") String type, HttpServletRequest request,
			HttpServletResponse response,MD5Utils md5Utils) {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp()+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flag = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flag) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = (InputStream) file.getInputStream();
				type = ImageUtil.typename(type);
				int rcode = ImageUtil.checkImg(file, type);
				if (1 == rcode) {
					// 文件的大小
					item.put("result", "1");
					item.put("error", "上传图片太大，请保持2M内。");
					return webUtil.resMsg(0, "0001", "上传文件大小错误", item);
				}
				if (2 == rcode) {
					// 文件的扩展名不符合要求
					item.put("result", "1");
					item.put("error", "上传图片格式出现问题，上传的图片格式jpg、png、gif、bmp.");
					return webUtil.resMsg(0, "0001", "上传图片格式不符合要求", item);
				}

				// 等比压缩图片 长宽不变
				InputStream in = CompressPicUtil.compressPic(inputStream, file,
						request, "local");

				ApiResult result = fileFacade.upload(type,
						ImageUtil.checkExt(file, type), in);

				String imageId = result.getData().split(";")[0];
				String imageUrl = result.getData().split(";")[1];

				logger.info("compressPic   imageId = " + imageId
						+ " imageUrl = " + imageUrl);

				item.put("result", "0");
				item.put("imageId", imageId);
				item.put("imageUrl", imageUrl);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("上传出错" + e.getMessage());
				item.put("result", "1");
				item.put("error", "上传图片出现问题。");
				return webUtil.resMsg(0, "0001", "上传图片出现问题", item);
			}
		}

		return webUtil.resMsg(1, "0001", "上传成功", item);
	}
	
	@RequestMapping("get_charitylist")
	@ResponseBody
	public JSONObject get_charitylist(
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "platformId", required = false) Integer platformId,
			@RequestParam(value = "timeStamp", required = false) Integer timeStamp,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {     
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		//key
		ApiConfig config = new ApiConfig();
		config.setConfigKey("API_KEY");
		List<ApiConfig> list = commonFacade.queryList(config);
		if(list==null || list.size()==0 || list.get(0).getConfigValue()==null || "".equals(list.get(0).getConfigValue()) ){
			item.put("Status", false);
			item.put("Msg", "key未配置");
			item.put("Data", null);
			return item;
		}
		String key = list.get(0).getConfigValue();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// key字符串为双方本地验签字段，不可外泄
		packageParams.put("platformId", platformId + "");
		packageParams.put("key", key);
		packageParams.put("timeStamp", timeStamp+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		String signs = reqHandler.createSign_new(packageParams);
		if (platformId==null) {
			item.put("Status", false);
			item.put("Msg", "平台ID为空");
			item.put("Data", null);
			return item;
		}
		if (key==null) {
			item.put("Status", false);
			item.put("Msg", "key为空");
			item.put("Data", null);
			return item;
		}
		if (timeStamp==null) {
			item.put("Status", false);
			item.put("Msg", "时间戳为空");
			item.put("Data", null);
			return item;
		}
		if (signs.equals(sign)) {
			flag = true;
		}
		if (!flag) {
			item.put("Status", false);
			item.put("Msg", "验签错误");
			item.put("Data", null);
			return item;
		}
		
		JSONObject data = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//查询用户信息
		ApiFrontUser user = userFacade.queryById(platformId);
		if(user==null){
			item.put("Status", false);
			item.put("Msg", "平台id不存在");
			item.put("Data", null);
			return item;
		}
		//查询所有的项目发起人id
		ApiPage<ApiProject> page = projectFacade.queryProjectUserIds(index, size);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-index*size>=0 || index*size-page.getTotal()<size)){
			/*if(page.getTotal()-index*size>0){
			}else{
				data.put("Count", 0);
			}*/
			for (ApiProject p: page.getResultData()) {
				JSONObject userJson = new JSONObject();
				userJson.put("CharityId", p.getUserId());
				userJson.put("CharityName", p.getNickName());
				userJson.put("CharityLogo", p.getCoverImageUrl());
				userJson.put("ProvinceCode", null);
				userJson.put("Province", p.getWorkUnit());
				userJson.put("CityCode", null);
				userJson.put("City", p.getLocation());
				userJson.put("AreaCode", null);
				userJson.put("Area", p.getDetailAddress());
				userJson.put("AddTime", DateUtil.dateString2(p.getRegistrTime()));
				jsonArray.add(userJson);
			}
		}
		data.put("Count", page.getTotal());
		data.put("DataList", jsonArray);

		item.put("Status", true);
		item.put("Msg","查询成功");
		item.put("Data", data);
		return item;
	}
	
	@RequestMapping("get_projectlist")
	@ResponseBody
	public JSONObject get_projectlist(
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "platformId", required = false) Integer platformId,
			@RequestParam(value = "timeStamp", required = false) Integer timeStamp,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "extensionPeople", required = false, defaultValue = "200805") Integer extensionPeople,
			@RequestParam(value = "charityId", required = false) Integer charityId) {
        
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		JSONObject item = new JSONObject();
		
		//key
		ApiConfig config = new ApiConfig();
		config.setConfigKey("API_KEY");
		List<ApiConfig> list = commonFacade.queryList(config);
		if(list==null || list.size()==0 || list.get(0).getConfigValue()==null || "".equals(list.get(0).getConfigValue()) ){
			item.put("Status", false);
			item.put("Msg", "key未配置");
			item.put("Data", null);
			return item;
		}
		String key = list.get(0).getConfigValue();
		
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// key字符串为双方本地验签字段，不可外泄
		packageParams.put("platformId", platformId + "");
		packageParams.put("key", key);
		packageParams.put("timeStamp", timeStamp+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		String signs = reqHandler.createSign_new(packageParams);
		if (platformId==null) {
			item.put("Status", false);
			item.put("Msg", "平台ID为空");
			item.put("Data", null);
			return item;
		}
		if (key==null) {
			item.put("Status", false);
			item.put("Msg", "key为空");
			item.put("Data", null);
			return item;
		}
		if (charityId==null) {
			item.put("Status", false);
			item.put("Msg", "机构ID为空");
			item.put("Data", null);
			return item;
		}
		ApiFrontUser user = userFacade.queryById(platformId);
		if(user==null){
			item.put("Status", false);
			item.put("Msg", "平台ID不存在");
			item.put("Data", null);
			return item;
		}
		if (timeStamp==null) {
			item.put("Status", false);
			item.put("Msg", "时间戳为空");
			item.put("Data", null);
			return item;
		}
		if (signs.equals(sign)) {
			flag = true;
		}
		if (!flag) {
			item.put("Status", false);
			item.put("Msg", "验签错误");
			item.put("Data", null);
			return item;
		}
		
		
		JSONObject data = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//查询项目信息
		ApiProject apiProject=new ApiProject();
		apiProject.setState(240);
		apiProject.setUserId(charityId);
		ApiPage<ApiProject> page =projectFacade.queryProjectList(apiProject, index, size);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-index*size>=0 || index*size-page.getTotal()<size)){
			/*if(page.getTotal()-index*size>0){
			}else{
				data.put("Count", 0);
			}*/
			for (ApiProject p : page.getResultData()) {
				JSONObject projectJson = new JSONObject();
				projectJson.put("ProjectId", p.getId());
				projectJson.put("ProjectName", p.getTitle());
				projectJson.put("ProjectImage", p.getCoverImageUrl());
				projectJson.put("ProjectDetail", p.getContent());
				projectJson.put("bigtype", p.getSpecial_fund_id()==0?1:2);
				projectJson.put("ProjectDesc", p.getSubTitle());
				projectJson.put("CodeUrl", "http://www.17xs.org/project/view_h5.do?slogans=export&projectId="+p.getId()+"&extensionPeople="+extensionPeople);
				projectJson.put("CharityId", p.getUserId());
				projectJson.put("AddTime",  DateUtil.dateString2(p.getRegistrTime()));
				jsonArray.add(projectJson);
			}
		}
		data.put("Count", page.getTotal());
		data.put("DataList", jsonArray);

		item.put("Status", true);
		item.put("Msg","查询成功");
		item.put("Data", data);
		return item;
			
	}
	
	/**
	 * 获取项目捐款数据
	 * @param request
	 * @param response
	 * @param platformId
	 * @param projectId
	 * @param type
	 * @param timeStamp
	 * @param sign
	 * @param index
	 * @param size
	 * @return
	 */
	@RequestMapping("get_donatelist")
	@ResponseBody
	public JSONObject get_donatelist(
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "platformId", required = false) Integer platformId,
			@RequestParam(value = "projectId", required = false) Integer projectId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "timeStamp", required = false) Integer timeStamp,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		
		if (projectId==null) {
			item.put("Status", false);
			item.put("Msg", "项目ID为空");
			item.put("Data", null);
			return item;
		}
		
		//key
		ApiConfig config = new ApiConfig();
		config.setConfigKey("API_KEY");
		List<ApiConfig> list = commonFacade.queryList(config);
		if(list==null || list.size()==0 || list.get(0).getConfigValue()==null || "".equals(list.get(0).getConfigValue()) ){
			item.put("Status", false);
			item.put("Msg", "key未配置");
			item.put("Data", null);
			return item;
		}
		String key = list.get(0).getConfigValue();
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// key字符串为双方本地验签字段，不可外泄
		packageParams.put("platformId", platformId + "");
		packageParams.put("key", key);
		packageParams.put("timeStamp", timeStamp+"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flag = false;
		String signs = reqHandler.createSign_new(packageParams);
		if (platformId==null) {
			item.put("Status", false);
			item.put("Msg", "平台ID为空");
			item.put("Data", null);
			return item;
		}
		if (key==null) {
			item.put("Status", false);
			item.put("Msg", "时间戳为空");
			item.put("Data", null);
			return item;
		}
		if (timeStamp==null) {
			item.put("Status", false);
			item.put("Msg", "时间戳为空");
			item.put("Data", null);
			return item;
		}
		if (signs.equals(sign)) {
			flag = true;
		}
		if (!flag) {
			// 返回为-2时验签不正确
			item.put("Status", false);
			item.put("Msg", "验签错误");
			item.put("Data", null);
			return item;
		}
		
		JSONObject data = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//查询用户信息
		ApiFrontUser user = userFacade.queryById(platformId);
		if(user==null){
			item.put("Status", false);
			item.put("Msg", "平台id不存在");
			item.put("Data", null);
			return item;
		}
		
		//根据项目id查询捐款记录
		ApiDonateRecord r = new ApiDonateRecord();
        r.setProjectId(projectId);
        r.setState(302);
		ApiPage<ApiDonateRecord> resultPage = donateRecordFacade.queryByCondition(r,index,size);
		if(resultPage!=null && resultPage.getTotal()>0 && (resultPage.getTotal()-index*size>=0 || index*size-resultPage.getTotal()<size)){
			/*if(resultPage.getTotal()-index*size>0){
			}else{
				data.put("Count", 0);
			}*/
			
			for (ApiDonateRecord dRecord : resultPage.getResultData()) {
				//项目名称    捐赠人姓名   联系方式    留言    匿名   支付方式    捐赠凭证
				//捐赠时间  所属机构  捐赠金额   头像
				JSONObject donateJson = new JSONObject();
				donateJson.put("ProjectName", dRecord.getProjectTitle());
				donateJson.put("DonateName", dRecord.getNickName());
				donateJson.put("Remark", dRecord.getLeaveWord());
				donateJson.put("IsAnonymous", 0);
				donateJson.put("PayType", dRecord.getBankType().equals("tenpay")?2:1);
				donateJson.put("Certificate", dRecord.getTranNum());
				donateJson.put("DonateTime",  DateUtil.dateString2(dRecord.getDonatTime()));
				donateJson.put("CharityId", dRecord.getGoodHelpCount());
				donateJson.put("Amount", dRecord.getDonatAmount());
				if(dRecord.getCoverImageId() >0){
            		ApiBFile bFile = fileFacade.queryBFileById(dRecord.getCoverImageId());
            		donateJson.put("DonateHeadPic", bFile.getUrl());
            	}else{
            		donateJson.put("DonateHeadPic", "http://www.17xs.org/res/images/detail/people_avatar.jpg");
            	}
				
				jsonArray.add(donateJson);
			}
		}
		data.put("Count", resultPage.getTotal());
		data.put("DataList", jsonArray);

		item.put("Status", true);
		item.put("Msg","查询成功");
		item.put("Data", data);
		return item;
	}

}
