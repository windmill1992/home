package com.guangde.home.controller.together;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
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
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectMoneyConfig;
import com.guangde.entry.ApiTogetherConfig;
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
@RequestMapping("together")
public class TogetherConfigController {
	private final Logger logger = LoggerFactory.getLogger(TogetherConfigController.class);
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	
	/**
	 * 善园专用的一起捐详情页面
	 * @param projectId
	 * @return
	 */
	@RequestMapping("together_view")
	public ModelAndView together_view(@RequestParam(value="projectId",required=true)Integer projectId,
			HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("h5/together/together_detail");
		Integer userId = UserUtil.getUserId(request, response);
		
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
        if(userId ==null)
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
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		//==================微信登陆end================//
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		 double process = 0.0;
		if(project.getCryMoney()>=0.001){
			process = project.getDonatAmount() / project.getCryMoney();
			view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0));
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
		
		view.addObject("project", project);
		return view;
	}
	
	/**
	 * 添加或编辑一起捐内容
	 * @param config
	 * @return
	 */
	@RequestMapping("addTogetherConfig")
	@ResponseBody
	public JSONObject addTogetherConfig(ApiTogetherConfig config,
			HttpServletRequest request,HttpServletResponse response){
		//userId projectId totalMoney launchName content type coverImageId
		JSONObject item = new JSONObject();
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){
			item.put("code", -1);
			item.put("msg", "用户未登录！");
			return item;
		}
		config.setUserId(userId);
		ApiFrontUser user = userFacade.queryById(userId);
		if(config.getType().equals("personal") && (config.getLaunchName()==null||config.getLaunchName().equals(""))){
			config.setLaunchName(user.getNickName());
			config.setCoverImageId(user.getCoverImageId());
		}
		if(config.getType().equals("group") && (config.getCoverImageId()==null||config.getLaunchName().equals(""))){
			config.setCoverImageId(user.getCoverImageId());
		}
		
		if(config.getProjectId()==null){
			item.put("code", -1);
			item.put("msg", "项目id为空！");
			return item;
		}
		ApiTogetherConfig apiTogetherConfig = new ApiTogetherConfig();
		apiTogetherConfig.setProjectId(config.getProjectId());
		apiTogetherConfig.setUserId(config.getUserId());
		apiTogetherConfig = projectFacade.queryByTogetherConfig(apiTogetherConfig);
		if(apiTogetherConfig!=null){//update
			config.setId(apiTogetherConfig.getId());
			config.setUpdateTime(new Date());
			ApiResult result = projectFacade.updateTogetherConfig(config);
			if(result.getCode()==1){
				item.put("msg", "发起成功！");
			}
			else{
				item.put("msg", "发起失败！");
			}
			item.put("code", result.getCode());
			return item;
		}
		else{//add
			config.setClick(0);
			config.setDonateMoney(BigDecimal.ZERO);
			config.setDonateNum(0);
			config.setCreateTime(new Date());
			ApiResult result = projectFacade.saveTogetherConfig(config);
			if(result.getCode()==1){
				item.put("msg", "发起成功！");
			}
			else{
				item.put("msg", "发起失败！");
			}
			item.put("code", result.getCode());
			return item;
		}
	}
	
	/**
	 * 一起捐分享详情页面
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping("raiseDetail_view")
	public ModelAndView raiseDetail_view(@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="show",required=false,defaultValue="0")Integer show,
			@RequestParam(value="donateShow",required=false,defaultValue="0")Integer donateShow,
			@RequestParam(value="shareUserId",required=false)Integer shareUserId,
			Integer userId,
			HttpServletRequest request,HttpServletResponse response) throws JDOMException, IOException{
		ModelAndView view = new ModelAndView("h5/together/raise_detail");
		if(userId==null){
			userId = UserUtil.getUserId(request, response);
		}
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
        if(userId ==null)
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
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		//==================微信登陆end================//
        if(userId==null){
        	userId=user.getId();
        }
        
       
        if(shareUserId!=null){
        	 ApiFrontUser userT = userFacade.queryById(shareUserId);
        	 view.addObject("shareUser", userT);
        }
        
        user = userFacade.queryById(userId);
        view.addObject("user", user);
		view.addObject("userId", userId);
		view.addObject("show", show);
		//项目详情
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		view.addObject("project", project);
		ApiTogetherConfig togetherConfig = new ApiTogetherConfig();
		if(shareUserId!=null){
			togetherConfig.setUserId(shareUserId);
		}
		else{
			togetherConfig.setUserId(userId);
		}
		view.addObject("shareUserId", shareUserId);
		togetherConfig.setProjectId(projectId);
		togetherConfig = projectFacade.queryByTogetherConfig(togetherConfig);
		if(donateShow==1 && togetherConfig!=null && togetherConfig.getShareState()==0){
			//判断是否是第一次分享
			togetherConfig.setShareState(1);
			projectFacade.updateTogetherConfig(togetherConfig);
        	view.addObject("donateShow", donateShow);
        }
		view.addObject("togetherConfig", togetherConfig);
		/***/
		double process = 0.0;
		if(togetherConfig.getTotalMoney().doubleValue()>=0.0001){
			process = togetherConfig.getDonateMoney().doubleValue() / togetherConfig.getTotalMoney().doubleValue();
			view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(togetherConfig.getDonateMoney().doubleValue(),togetherConfig.getTotalMoney().doubleValue(),0));
		}
		/*增加点击量*/
		if(togetherConfig!=null){
			togetherConfig.setClick(togetherConfig.getClick()+1);
			projectFacade.updateTogetherConfig(togetherConfig);
		
		}
		
		//捐款金额配置
		ApiProjectMoneyConfig param = new ApiProjectMoneyConfig();
		param.setProjectId(projectId);
		List<ApiProjectMoneyConfig> moneyConfigs=projectFacade.queryMoneyConfigByParam(param);
		view.addObject("moneyConfigs", moneyConfigs);
		
		
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
			StringBuffer url2 = request.getRequestURL();
			String queryString2 = request.getQueryString();
			String perfecturl2 = url2 + "?" + queryString2;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
    	
		return view;
	}
	
	/**
	 * 查询金额配置
	 * @param config
	 * @return
	 */
	@RequestMapping("selectMoneyConfig")
	@ResponseBody
	public JSONObject selectMoneyConfig(ApiProjectMoneyConfig config){
		JSONObject item = new JSONObject();
		List<ApiProjectMoneyConfig> moneyConfigs=projectFacade.queryMoneyConfigByParam(config);
		if(moneyConfigs!=null && moneyConfigs.size()>0){
			item.put("content", moneyConfigs.get(0).getContent());
		}
		return item;
	}

	/**
	 * 善园专用的一起捐另外详情页面
	 * @param projectId
	 * @return
	 */
	@RequestMapping("detail_view")
	public ModelAndView detail_view(@RequestParam(value="projectId",required=false,defaultValue="3429")Integer projectId,
			HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("h5/together/detail");
		Integer userId = UserUtil.getUserId(request, response);
		//授权登录
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
        if(userId ==null)
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
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		//==================微信登陆end================//
        if(userId==null){
        	userId=user.getId();
        }else{
			user = userFacade.queryById(userId);
			view.addObject("user", user);
        }
        
        
		//判断用户是否留过言 是：不作处理；否：添加一条数据 --到此一游
        boolean leaveword = projectFacade.queryIsOrNotNewLeaveWordByUserId(userId);
        if(!leaveword){
        	ApiNewLeaveWord word = new ApiNewLeaveWord();
        	word.setType(2);
        	word.setProjectId(projectId);
        	word.setLeavewordUserId(userId);
        	word.setContent("到此一游");
        	word.setCreateTime(new Date());
        	word.setUpdateTime(word.getCreateTime());
        	projectFacade.saveNewLeaveWord(word);
        }
        
      //轮播图
        Object obj = redisService.queryObjectData("TOGETHER_BANNER_LIST_"+projectId);
		if(obj != null){
			view.addObject("bannerList", obj);
		}else{
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory("togetherBanner_"+projectId);
			bfiles = commonFacade.queryApiBfile(apiBfile);
			view.addObject("bannerList", bfiles);
			redisService.saveObjectData("TOGETHER_BANNER_LIST_"+projectId, bfiles, DateUtil.DURATION_FIVE_S);
		}
        
		//查询留言数 用户id不重复
		ApiNewLeaveWord apiNewLeaveWord = new ApiNewLeaveWord();
		apiNewLeaveWord.setProjectId(projectId);
		apiNewLeaveWord.setType(1);
        int totalWordNum = projectFacade.countNewLeaveWordByNoRepeatUserId(apiNewLeaveWord);
        view.addObject("totalWordNum", totalWordNum);
        
        
		//查询项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
		view.addObject("project", project);
		
		
		//查询今日募捐金额  / 1.68  得到砖块数，取整
		ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
		apiDonateRecord.setProjectId(projectId);
		apiDonateRecord.setState(302);
		apiDonateRecord.setLeaveWord("0");
		apiDonateRecord = donateRecordFacade.queryCompanyCenter(apiDonateRecord);
		if(apiDonateRecord!=null && apiDonateRecord.getGoodHelpAmount()!=null){
			Double dd = (Double)(apiDonateRecord.getGoodHelpAmount()/1.68d);
			//int ddInt = (int) Math.ceil(dd);
			String[] ddstr = dd.toString().split("\\.");
			view.addObject("todayDonateAmount", ddstr[0]);
			//进度条
			//view.addObject("processbfb", ddInt/100);
		}else{
			view.addObject("todayDonateAmount", 0);
			//进度条
			//view.addObject("processbfb", 0);
		}
		
		//今日捐款人数
		view.addObject("todayDonateCount", apiDonateRecord.getGoodHelpCount());
		
		//今日访客数
		apiNewLeaveWord.setType(0);
		int todayWordNum = projectFacade.countNewLeaveWordByNoRepeatUserId(apiNewLeaveWord);
		view.addObject("todayWordNum", todayWordNum);
		 
		 
		 
		//查询列表分页 （一起捐，捐款，留言）按捐款金额 创建时间 倒叙
		
		//总捐赠的人数 捐赠人的头像  昵称 按捐赠时间排序
		ApiPage<ApiDonateRecord> page = donateRecordFacade.queryDonateHeadImgByProjectId(projectId, 1, 8);
		if(page!=null){
			view.addObject("donateHeadImg", page.getResultData());
			view.addObject("totalDonateNum", page.getTotal());
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
    	
    	
    	//发起人数
		//int launchNum = projectFacade.countTogetherConfig(projectId);
		//view.addObject("launchNum", launchNum);
		//参与人数
		//int takepartNum = projectFacade.countTogetherConfigSum(projectId);
		//view.addObject("takepartNum", takepartNum);
		//一起捐列表(异步加载)
		
		return view;
	}
	
	/**
	 * 加载一起捐
	 * @param projectId
	 * @return
	 */
	@RequestMapping("loadTogetherConfigList")
	@ResponseBody
	public JSONObject togetherConfig_list(Integer projectId,
			@RequestParam(value="page",required=true,defaultValue="1")Integer page,
			@RequestParam(value="pageNum",required=true,defaultValue="10")Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		try {
			ApiPage<ApiTogetherConfig> apList =  projectFacade.queryTogetherConfigByProjectId(projectId, page, pageNum);
			List<ApiTogetherConfig> list = apList.getResultData();
	        if(list.size()==0){
	        	 //无数据
	        	 data.put("result", 1);
	         }
	         else {
				for(ApiTogetherConfig togetherConfig:list){
					 JSONObject item = new JSONObject();
					 item.put("userId", togetherConfig.getUserId());
					 item.put("nickName", togetherConfig.getLaunchName());
					 item.put("coverImageUrl", togetherConfig.getCoverImageUrl());
					 item.put("donateNum", togetherConfig.getDonateNum());
					 item.put("donateMoney", togetherConfig.getDonateMoney());
					 item.put("content", togetherConfig.getContent());
					 items.add(item);
				}
				data.put("result",0);
				data.put("total",apList.getTotal());
				data.put("items", items);
			}
	} catch (Exception e) {
		data.put("result", 2);
	}
	return data;
	}
	
	/**
	 * 加载一起捐 捐款 留言
	 * @param projectId
	 * @return
	 */
	@RequestMapping("loadDetailList")
	@ResponseBody
	public JSONObject loadDetail_list(Integer projectId,
			@RequestParam(value="page",required=true,defaultValue="1")Integer page,
			@RequestParam(value="pageNum",required=true,defaultValue="10")Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		try {
			ApiPage<ApiTogetherConfig> apList =  projectFacade.queryDetailByProjectId(projectId, page, pageNum);
			List<ApiTogetherConfig> list = apList.getResultData();
	        if(list.size()==0){
	        	 //无数据
	        	 data.put("result", 1);
	         }
	         else {
				for(ApiTogetherConfig togetherConfig:list){
					 JSONObject item = new JSONObject();
					 item.put("userId", togetherConfig.getUserId());
					 item.put("nickName", togetherConfig.getLaunchName());
					 item.put("coverImageUrl", togetherConfig.getCoverImageUrl());
					 item.put("donateNum", togetherConfig.getDonateNum());
					 item.put("donateMoney", togetherConfig.getDonateMoney());
					 item.put("content", togetherConfig.getContent());
					 item.put("type", togetherConfig.getType());
					 items.add(item);
				}
				data.put("result",0);
				data.put("total",apList.getTotal());
				data.put("items", items);
			}
	} catch (Exception e) {
		data.put("result", 2);
	}
	return data;
	}
	
	/**
	 * 善园添砖加瓦广告页面
	 * @param projectId
	 * @return
	 */
	@RequestMapping("index_view")
	public ModelAndView index_view(@RequestParam(value="projectId",required=false,defaultValue="3429")Integer projectId){
		ModelAndView view = new ModelAndView("h5/together/index");
		view.addObject("projectId", projectId);
		return view;
	}
	
	/**
	 * 善园添砖加瓦捐款列表详情
	 * @param projectId
	 * @return
	 */
	@RequestMapping("record_view")
	public ModelAndView record_view(@RequestParam("projectId")Integer projectId){
		ModelAndView view = new ModelAndView("h5/together/record");
		view.addObject("projectId", projectId);
		return view;
	}
	
	/**
	 * 加载一起捐 捐款
	 * @param projectId
	 * @return
	 */
	@RequestMapping("loadTogetherDonateList")
	@ResponseBody
	public JSONObject loadTogetherDonate_list(Integer projectId,
			@RequestParam(value="page",required=true,defaultValue="1")Integer page,
			@RequestParam(value="pageNum",required=true,defaultValue="10")Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		try {
			ApiPage<ApiTogetherConfig> apList =  projectFacade.queryTogetherDonateListByProjectId(projectId, page, pageNum);
			List<ApiTogetherConfig> list = apList.getResultData();
	        if(list.size()==0){
	        	 //无数据
	        	 data.put("result", 1);
	         }
	         else {
	        	 DecimalFormat df = new DecimalFormat("0.00");
				for(ApiTogetherConfig togetherConfig:list){
					 JSONObject item = new JSONObject();
					 item.put("userId", togetherConfig.getUserId());
					 item.put("nickName", togetherConfig.getLaunchName());
					 item.put("coverImageUrl", togetherConfig.getCoverImageUrl());
					 item.put("donateNum", togetherConfig.getDonateNum());
					 item.put("donateMoney", df.format(togetherConfig.getDonateMoney()));
					 item.put("content", togetherConfig.getContent());
					 item.put("type", togetherConfig.getType());
					 items.add(item);
				}
				data.put("result",0);
				data.put("total",apList.getTotal());
				data.put("items", items);
			}
	} catch (Exception e) {
		data.put("result", 2);
	}
	return data;
	}
	
	@RequestMapping(value="oneProjectTogetherNum",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject oneProjectTogetherNum(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item=new JSONObject();
		JSONObject result = new JSONObject();
		
		
		ApiProject p=projectFacade.queryProjectDetail(projectId);
		if(p==null){
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "该项目不存在");
		}else{
			ApiTogetherConfig togetherConfig=new ApiTogetherConfig();
			togetherConfig.setProjectId(projectId);
			List<ApiTogetherConfig> list=projectFacade.oneApiTogetherConfigTotal(togetherConfig);
			BigDecimal totalDonateMoney=list.get(0).getTotalDonateMoney();
			Integer totalDonateNum=list.get(0).getTotalDonateNum();
			Integer totalFaqiNum=list.get(0).getTotalFaqiNum();
			//一起捐发起人数
			result.put("totalFaqiNum", totalFaqiNum);
			//单项目的捐赠总金额
			result.put("totalDonateMoney", totalDonateMoney==null?0:totalDonateMoney);
			//单项目的捐赠总人数
			result.put("totalDonateNum", totalDonateNum==null?0:totalDonateNum);
			
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		}
		return item;
	}
}
