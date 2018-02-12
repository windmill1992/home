package com.guangde.home.controller.donateStep;

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

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IHomePageFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiRunCompany;
import com.guangde.entry.ApiThirdUser;
import com.guangde.entry.ApiWeRundata;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("/donateStep")
public class DonateStepController {

	Logger logger = LoggerFactory.getLogger(DonateStepController.class);
	
	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private IHomePageFacade homePageFacade;
	
	@Autowired
	private IProjectFacade projectFacade;
	
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IFileFacade fileFacade;
	
	@Autowired
	private ICommonFacade commonFacade;
	
	//更新或添加微信运动步数
	@RequestMapping(value="/addUserWeRundata",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject addUserWeRundata(ApiFrontUser user,HttpServletRequest request,HttpServletResponse response,
			@RequestParam("unionId")String unionId, @RequestParam("openId")String openId,
			@RequestParam("runData")Integer runData){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Integer userId=null;
		JSONObject item = new JSONObject();
		//根据unionId判断用户是否注册过
		ApiThirdUser thirdUser = new ApiThirdUser();
		thirdUser.setUnionid(unionId);
		thirdUser = userFacade.queryThirdUserByParam(thirdUser);
		if(thirdUser==null){//未注册
			String adressIp = SSOUtil.getUserIP(request);
			String nickName = StringUtil.filterEmoji(user.getNickName());
			user.setRegisterIP(adressIp);
			user.setNickName(nickName);
			user.setPersition(user.getCoverImageUrl());
			user.setUserType(PengPengConstants.PERSON_USER);
			user.setUserPass(StringUtil.randonNums(8));
			user.setUserName(PengPengConstants.WENXIN + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
			try {
            	long ip = IPAddressUtil.ipToLong(adressIp);
            	ApiIptable iptable = new ApiIptable();
            	iptable.setEndIPNum(String.valueOf(ip));
            	iptable.setStartIPNum(String.valueOf(ip));
            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
            	if(ipList!=null && ipList.size()>0){
            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
            		if(contents.length==1){
            			user.setProvince(contents[0]);
            		}
            		else if(contents.length==2){
            			user.setProvince(contents[0]);
            			user.setCity(contents[1]);
            		}
            		else if(contents.length==3){
            			user.setProvince(contents[0]);
            			user.setCity(contents[1]);
            			user.setArea(contents[2]);
            		}
            	}
			
		} catch (Exception e) {
		}
		thirdUser = new ApiThirdUser();
		thirdUser.setAccountNum(openId);
		thirdUser.setType("weixin");
		thirdUser.setUnionid(unionId);
		ApiResult result = userFacade.thirdUserRegistered(user, thirdUser);
		userId = Integer.valueOf(result.getMessage());
		user = userFacade.queryById(userId);
		try{
			// 自动登录
			SSOUtil.login(user, request, response);
		}
		catch(Exception e)
		{
			logger.error("",e);
		}
		}
		else{
			userId=thirdUser.getUserId();
		}
		logger.info("WeRunData----userId>>>>>>>>>"+userId);
		//查询今日微信运动是否添加
		ApiResult res = null;
		ApiWeRundata weRundata = new ApiWeRundata();
		weRundata.setUserId(userId);
		weRundata = homePageFacade.queryTodayWeRundata(weRundata);
		if(weRundata!=null){//更新微信运动数据
			if(weRundata.getDonateState()==100){
				weRundata.setCreateTime(new Date());
				weRundata.setStepCount(runData);
				res = homePageFacade.updateWeRundata(weRundata);
			}
		}else{//添加微信运动数据
			weRundata = new ApiWeRundata();
			weRundata.setUserId(userId);
			weRundata.setNickName(user.getNickName());
			weRundata.setCoverImageUrl(user.getCoverImageUrl());
			weRundata.setStepCount(runData);
			weRundata.setDonateState(100);
			weRundata.setCreateTime(new Date());
			res = homePageFacade.saveWeRundata(weRundata);
		}
		item.put("code", res.getCode());
		if(res.getCode()==1){
			item.put("msg", "success！");
		}else{
			item.put("msg", res.getMessage());
		}
		
		return item;
	}
	
	/**
	 * 随机选取企业（剩余可捐金额，名称，标语，头像），h5随机从企业选取一个项目
	 * 若是用户已捐过
	 */
	@RequestMapping(value="getRandomCompanyInfo",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getRandomCompanyInfo(@RequestParam("userId")Integer userId, 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject project= new JSONObject();
		//判断用户是否步捐过
		ApiWeRundata data = new ApiWeRundata();
		data.setUserId(userId);
		data = homePageFacade.queryTodayWeRundata(data);
		if(data!=null && data.getDonateState()==101){
			item.put("code", "2");
			item.put("msg", "success！");
			//捐步步数，捐步状态，企业名称，企业随机捐赠金额，企业logo，项目标题，用户头像，项目id，用户昵称，企业id
			ApiDonateRecord dd = donateRecordFacade.queryDonateStepByTranNum(data.getOrderNum());
			ApiRunCompany runCompany = new ApiRunCompany();
			runCompany.setUserId(dd.getExtensionPeople());
			ApiPage<ApiRunCompany> page = homePageFacade.queryRunCompanyByParam(runCompany, 1, 1);
			if(page!=null && page.getTotal()>0){
				result.put("companyImgUrl", page.getResultData().get(0).getCompanyImageUrl());
				result.put("name", page.getResultData().get(0).getCompanyName());
				result.put("slogan", page.getResultData().get(0).getSlogan());
				result.put("donateAmount", page.getResultData().get(0).getTotalAmount().subtract(page.getResultData().get(0).getDonateAmount()));
			}
			result.put("userDonatAmount", dd.getDonatAmount());
			result.put("nickName", dd.getNickName());
			result.put("coverImageurl", dd.getCoverImageurl());
			result.put("stepCount", data.getStepCount());
			//project.put("state", data.getDonateState());
			project.put("projectTitle", dd.getProjectTitle());
			project.put("projectId", dd.getProjectId());
			project.put("projectImgUrl", dd.getTouristMessage());
			result.put("companyId", dd.getExtensionPeople());
			result.put("project", project);
		}
		else{
		ApiRunCompany runCompany = homePageFacade.queryRandomCompany();
		if(runCompany!=null && runCompany.getRelationItems()!=null && !"".equals(runCompany.getRelationItems())){
			item.put("code", "1");
			item.put("msg", "success！");
			String projectIds[] = runCompany.getRelationItems().trim().split(",");
			if(projectIds.length>0){
				int max=projectIds.length;
				int min=1;
			    Random random = new Random();
			    int s = random.nextInt(max)%(max-min+1) + min;
			    try {
			    	ApiProject p = projectFacade.queryProjectDetail(Integer.valueOf(projectIds[s-1]));
			    	result.put("companyId", runCompany.getUserId());
			    	project.put("projectId", p.getId());
				    project.put("projectImgUrl", p.getCoverImageUrl());
				    project.put("projectTitle", p.getTitle());
				    result.put("project", project);
				} catch (Exception e) {
					logger.info("get random project error!");
				}
			}
	       result.put("donateAmount", runCompany.getTotalAmount().subtract(runCompany.getDonateAmount()));
	       result.put("name", runCompany.getCompanyName());
	       result.put("slogan", runCompany.getSlogan());
	       result.put("companyImgUrl", runCompany.getCompanyImageUrl());
	        
		}else{
			item.put("code", "0");
			item.put("msg", "没有可选的企业！");
			logger.info("get random company is null");
		}
		}
		item.put("result", result);
		return item;
	}
	
	/**
	 * 随机从企业选取一个项目
	 */
	@RequestMapping(value="getRandomProjectInfo",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getRandomProjectInfo(@RequestParam("companyId")Integer companyId, 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject project= new JSONObject();
		ApiRunCompany param = new ApiRunCompany();
		param.setUserId(companyId);
		ApiPage<ApiRunCompany> page = homePageFacade.queryRunCompanyByParam(param, 1, 1);
		if(page!=null && page.getTotal()>0){
			item.put("code", "1");
			item.put("msg", "success！");
			String projectIds[] = page.getResultData().get(0).getRelationItems().trim().split(",");
			if(projectIds.length>0){
				int max=projectIds.length;
				int min=1;
			    Random random = new Random();
			    int s = random.nextInt(max)%(max-min+1) + min;
			    try {
			    	ApiProject p = projectFacade.queryProjectDetail(Integer.valueOf(projectIds[s-1]));
				    project.put("companyId", page.getResultData().get(0).getUserId());
			    	project.put("projectId", p.getId());
				    project.put("projectImgUrl", p.getCoverImageUrl());
				    project.put("projectTitle", p.getTitle());
				    result.put("project", project);
				} catch (Exception e) {
					logger.info("get random project error!");
				}
			}
		}else{
			item.put("code", "0");
			item.put("msg", "没有可选的项目！");
			logger.info("get random project is null");
		}
		item.put("result", result);
		return item;
	}
	
	/**
	 * 统计（总捐步人数，参与企业数，总金额）
	 * @return
	 */
	@RequestMapping(value="getStatisticsData",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getStatisticsData(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiRunCompany data = homePageFacade.countCompany();
		if(data!=null){
			item.put("code", 1);
			item.put("msg", "success!");
			result.put("peopleNum", data.getTotalDonateTimes());
			result.put("companyNum", data.getTotalCompany());
			result.put("donatedTotalMony", data.getTotalDonateAmount());
		}else{
			item.put("code", 0);
			item.put("msg", "statistics data is null");
		}
		
		item.put("result", result);
		return item;
	}
	
	/**
	 * 排行分页（企业名称，logo，捐赠次数，捐赠金额），按照捐赠金额排序
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value="getDonateStepRank",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getDonateStepRank(@RequestParam(value="pageSize", required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum", required=false,defaultValue="10")Integer pageNum,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONArray items = new JSONArray();
		ApiRunCompany param = new ApiRunCompany();
		param.setOrderBy("donateAmount");
		param.setOrderDirection("desc");
		ApiPage<ApiRunCompany> page = homePageFacade.queryRunCompanyByParam(param, pageNum, pageSize);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-pageNum*pageSize>=0 || pageNum*pageSize-page.getTotal()<pageSize)){
			if(page.getTotal()-pageNum*pageSize>0){
				item.put("state", 1);
			}else{
				item.put("state", 0);
			}
			for (ApiRunCompany runCompany : page.getResultData()) {
				JSONObject json = new JSONObject();
				json.put("companyId", runCompany.getUserId());
				json.put("companyName", runCompany.getCompanyName());
				json.put("companyImgUrl", runCompany.getCompanyImageUrl());
				json.put("donateNum", runCompany.getPopulation());
				json.put("donateAmount", runCompany.getDonateAmount());
				items.add(json);
			}
		}else{
			item.put("state", 0);
		}
		item.put("result", items);
		return item;
	}
	
	/**
	 * 时间，捐赠步数，捐赠金额，项目名称，项目id，项目logo
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value="getUserDonateStepRecord",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getUserDonateStepRecord(@RequestParam(value="pageSize", required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum", required=false,defaultValue="10")Integer pageNum,HttpServletResponse response,
			@RequestParam("userId")Integer userId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONArray items = new JSONArray();
		ApiWeRundata param = new ApiWeRundata();
		param.setUserId(userId);
		ApiPage<ApiWeRundata> page = homePageFacade.queryWeRunUserdataByParam(param, pageNum, pageSize);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-pageNum*pageSize>=0 || pageNum*pageSize-page.getTotal()<pageSize)){
			if(page.getTotal()-pageNum*pageSize>0){
				item.put("state", 1);
			}else{
				item.put("state", 0);
			}
			for (ApiWeRundata weRundata : page.getResultData()) {
				JSONObject json = new JSONObject();
				json.put("donateTime", weRundata.getDonatTime());
				json.put("donateStep", weRundata.getStepCount());
				json.put("donateAmount", weRundata.getDonatAmount());
				json.put("projectTitle", weRundata.getTitle());
				json.put("projectId", weRundata.getProjectId());
				json.put("projectCoverImageUrl", weRundata.getCoverImageUrl());
				items.add(json);
			}
		}else{
			item.put("state", 0);
		}
		item.put("result", items);
		return item;
	}
	
	/**
	 * 统计（累计捐赠步数，累计捐赠金额）
	 * @return
	 */
	@RequestMapping(value="getUserStatistics",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getUserStatistics(@RequestParam("userId")Integer userId,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiWeRundata data = new ApiWeRundata();
		data.setUserId(userId);
		data = homePageFacade.queryUserWeRundata(data);
		if(data!=null){
			item.put("code", 1);
			item.put("msg", "success!");
			result.put("totalStep", data.getTotalStep());
			result.put("totalMoney", data.getTotalMonay());
		}else{
			item.put("code", 0);
			item.put("msg", "user statistics data is null");
		}
		
		item.put("result", result);
		return item;
	}
	
	
	/**
	 * 捐步
	 * @param pName amount projectId extensionPeople
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="donateStep",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject donateStep(DepositForm from,HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		String orderNum = StringUtil.uniqueCode();
		Integer userId = from.getUserId();
		/*Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
        	//未登录
        	item.put("code", -1);
        	item.put("msg", "not login！");
        }*/
		//判断用户是否捐过
		ApiWeRundata data = new ApiWeRundata();
		data.setUserId(userId);
		data = homePageFacade.queryTodayWeRundata(data);
		if(data!=null && data.getDonateState()==101){
			item.put("code", 3);
        	item.put("msg", "今天已捐过");
		}else if(data != null && data.getDonateState() != 101){
        ApiFrontUser user = userFacade.queryById(userId);
        ApiDonateRecord dRecord = new ApiDonateRecord();
        ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        dRecord.setProjectTitle(project.getTitle());
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(user.getUserType());
        dRecord.setDonatType("weRunDonation");//捐步 冻结支付
        dRecord.setDonatAmount(from.getAmount());
        dRecord.setDonateCopies(0);
        dRecord.setTranNum(orderNum);
        dRecord.setSource("H5");
        dRecord.setCompanyId(user.getCompanyId());
		dRecord.setNameOpen(0);//默认不匿名
        if(request.getSession().getAttribute("extensionPeople")!=null&&from.getExtensionPeople()==null){
        	dRecord.setExtensionPeople(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
        else{
        if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }}
       
        ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "weRunType",null,"");//weRunType:捐步
        item.put("code", rt.getCode());
        item.put("msg", rt.getMessage());
		}else{
			item.put("code", 3);
        	item.put("msg", "未获取微信步数");
		}
		return item;
	}
	
	/**
	 * 判断用户10分钟内是否更新过微信运动  换算金额
	 * @return
	 */
	@RequestMapping(value="getUserUpdateWeRundata",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getUserUpdateWeRundata(HttpServletResponse response,@RequestParam("userId")Integer userId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiWeRundata data = new ApiWeRundata();
		data.setUserId(userId);
		data = homePageFacade.queryTodayWeRundata(data);
		
		boolean res = homePageFacade.queryToday10WeRundata(userId);
		if(data!=null){
			if(res){
				ApiConfig config = new ApiConfig();
				config.setConfigKey("weRun_rate");
				List<ApiConfig> list = commonFacade.queryList(config);
				if(list!=null && list.size()>0){
					try {
						String[] rate = list.get(0).getConfigValue().split(":");//10000:1
						Double config_step = Double.valueOf(rate[0]);
						Double config_money = Double.valueOf(rate[1]);
						Double step = Double.valueOf(data.getStepCount());
						Double resDouble= MathUtil.div((MathUtil.mul(step, config_money)),config_step,2);
						result.put("amount", resDouble);//换算后的金额
						result.put("runData", step);//步数
						//result.put("state", data.getDonateState());
					} catch (Exception e) {
						item.put("code", -1);
						item.put("msg", "config error！");
					}
				}
				item.put("code", 1);
				item.put("msg", "10分钟内获取过微信步数！");
			}else{
				item.put("code", 0);
				item.put("msg", "10分钟内未获取微信步数！");
			}
		}
		else{
			item.put("code", 2);
			item.put("msg", "未获取微信步数！");
		}
		item.put("result", result);
		return item;
	}
	
	/**
	 * 微信登录
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value="wxLogin",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject wxLogin(HttpServletResponse response,HttpServletRequest request) throws JDOMException, IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
 		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
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
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					result.put("url", url_weixin_code);
        					item.put("code", 0);
        					item.put("msg", "获取code！");
        					item.put("result", result);
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
        			item.put("code", -1);
					item.put("msg", "error！");
					item.put("result", result);
        			logger.error("H5RedPacketsController>>getRedPaket>>"+ e);
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
        			item.put("code", -1);
        			item.put("msg", "error！");
        			item.put("result", result);
        			logger.error("",e);
        		}
        	}
        	else
        	{
        		//不是微信浏览器
        		item.put("code", 2);
        		item.put("msg", "不是wx浏览器！");
        		item.put("result", result);
        		return item;
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        
        if(userId==null){
        	userId=user.getId();
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
			result.put("appId", map.get("appId"));
			result.put("timeStamp", map.get("timeStamp"));
			result.put("nonceStr", map.get("nonceStr"));
			result.put("signature", map.get("signature"));
		}
        
        item.put("code", 1);
        item.put("msg", "success！");
        result.put("userId", userId);
        item.put("result", result);
        return item;
	}
	
	/**
	 * 微信登录
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value="wxLoginHtml",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject wxLoginHtml(@RequestParam(value="url",required=true)String url,
			HttpServletResponse response,HttpServletRequest request) throws JDOMException, IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
 		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		logger.info("user id is "+userId);
		String openId ="";
		String token = "";
		String unionid = "";
		/*StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;*/
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
        					String url_weixin_code = H5Demo.getCodeRequest(url);
        					result.put("url", url_weixin_code);
        					item.put("code", 0);
        					item.put("msg", "获取code！");
        					item.put("result", result);
        					return item;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        				redisService.saveObjectData("AccessToken", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			item.put("code", -1);
					item.put("msg", "error！");
					item.put("result", result);
        			logger.error("H5RedPacketsController>>getRedPaket>>"+ e);
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
        			item.put("code", -1);
        			item.put("msg", "error！");
        			item.put("result", result);
        			logger.error("",e);
        		}
        	}
        	else
        	{
        		//不是微信浏览器
        		item.put("code", 2);
        		item.put("msg", "不是wx浏览器！");
        		item.put("result", result);
        		return item;
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        
        if(userId==null){logger.info("userId is null userId=user.getId()");
        	userId=user.getId();logger.info("userId is "+userId);
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
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,url);
			result.put("appId", map.get("appId"));
			result.put("timeStamp", map.get("timeStamp"));
			result.put("nonceStr", map.get("nonceStr"));
			result.put("signature", map.get("signature"));
		}
        
        item.put("code", 1);
        item.put("msg", "success！");
        result.put("userId", userId);
        item.put("result", result);
        return item;
	}
	
	/**
	 * 企业详情数据
	 */
	@RequestMapping(value="getCompanyDetail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getCompanyDetail(@RequestParam("companyId")Integer companyId, 
			HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray projects = new JSONArray();
		ApiRunCompany param = new ApiRunCompany();
		param.setUserId(companyId);
		ApiPage<ApiRunCompany> page = homePageFacade.queryRunCompanyByParam(param, 1, 1);
		if(page!=null && page.getTotal()>0){
			String projectIds[] = page.getResultData().get(0).getRelationItems().trim().split(",");
			result.put("projectCount", projectIds.length);
			result.put("donateAmount", page.getResultData().get(0).getDonateAmount());
			result.put("name", page.getResultData().get(0).getCompanyName());
			result.put("companyImageUrl", page.getResultData().get(0).getCompanyImageUrl());
			result.put("introduction", page.getResultData().get(0).getIntroduction());
			
			if(projectIds.length>0){
				item.put("code", "1");
				item.put("msg", "success！");
			    try {
			    	ApiProject p = new ApiProject();
			    	List<Integer> ids = new ArrayList<Integer>();
			    	for (String id : projectIds) {
						ids.add(Integer.valueOf(id));
					}
			    	p.setpList(ids);
			    	ApiPage<ApiProject> projectPage = projectFacade.queryProjectList(p, 1, 15);
			    	if(projectPage!=null && projectPage.getTotal()>0){
			    		for (ApiProject proApiProject : projectPage.getResultData()) {
			    			JSONObject project= new JSONObject();
			    			project.put("projectId", proApiProject.getId());
						    project.put("projectImgUrl",proApiProject.getCoverImageUrl());
						    project.put("projectTitle", proApiProject.getTitle());
						    ApiDonateRecord newApiDonateRecord = new ApiDonateRecord();
						    newApiDonateRecord.setExtensionPeople(companyId);
						    newApiDonateRecord.setProjectId(proApiProject.getId());
						    newApiDonateRecord = donateRecordFacade.queryStepProjectByParam(newApiDonateRecord);
						    if(newApiDonateRecord!=null){
						    	 project.put("totalAmount", newApiDonateRecord.getTotalAmount()==null?0:newApiDonateRecord.getTotalAmount());
						    	 project.put("totalUser", newApiDonateRecord.getGoodHelpCount());
						    	 project.put("totalStep", newApiDonateRecord.getDonateNum()==null?0:newApiDonateRecord.getDonateNum());
						    }
						    projects.add(project);
			    		}
			    		result.put("project", projects);
			    	}
				} catch (Exception e) {
					logger.info("get company projectInfo error!");
				}
			}
			else{
				item.put("code", "0");
				item.put("msg", "没有项目！");
				logger.info("get projects is null");
			}
		}else{
			item.put("code", "0");
			item.put("msg", "企业异常！");
			logger.info("get company is null");
		}
		item.put("result", result);
		return item;
	}
	
}
