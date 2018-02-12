package com.guangde.home.controller.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IHomePageFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiActivityConfig;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCommonForm;
import com.guangde.entry.ApiCommonFormUser;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiHospitalAppoint;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.message.PNews;
import com.guangde.pojo.ApiPage;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("resposibilityReport")
public class ResposibilityReportController{
	private final Logger logger = LoggerFactory.getLogger(ResposibilityReportController.class);
	
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	
	@Autowired
	private INewsFacade newsFacade;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IFileFacade fileFacade;
	
	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private IHomePageFacade homePageFacade;
	
	@Autowired
	private IProjectFacade projectFacade;
	
	@Autowired
	private ICommonFacade commonFacade;
	
	@Autowired
	private ICompanyFacade companyFacade;
	
	@RequestMapping(value="entryFormView")
	public ModelAndView entryFormView(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id){
		ModelAndView view = new ModelAndView("commonForm/entryForm");
		ApiCommonForm model = new ApiCommonForm();
		model.setId(id);
		ApiCommonForm apiCommonForm = projectVolunteerFacade.selectByParam(model);
		Date currentTimeDate = new Date();
		view.addObject("apiCommonForm", apiCommonForm);
		view.addObject("currentTime", currentTimeDate);
		view.addObject("userId",UserUtil.getUserId(request, response));
		return view;
	}
	
	@RequestMapping(value="saveForm")
	@ResponseBody
	public Map<String, Object>  saveForm(ApiCommonFormUser model,@RequestParam(value="hospitalAppointId",required=false)Integer hospitalAppointId){
		if(model != null && model.getFormId() != null && model.getUserId() != null){
			if(hospitalAppointId != null){//医生填写涂氟详情
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				ApiHospitalAppoint apiHospitalAppoint = projectVolunteerFacade.queryHospitalAppointById(hospitalAppointId);
				//判断时间是否达到177天
				if(apiHospitalAppoint != null){
					String hospitalTime = apiHospitalAppoint.getFluoride_time();
					if(hospitalTime==null || "".equals(hospitalTime)){
						projectVolunteerFacade.save(model);
						ApiCommonForm form = new ApiCommonForm();
						form.setId(model.getFormId());
						ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
						apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
						projectVolunteerFacade.update(apiCommonForm);
						
						apiHospitalAppoint.setFluoride_time(sdf.format(date));
						projectVolunteerFacade.updateHospitalAppoint(apiHospitalAppoint);
						return webUtil.resMsg(1, "0000", "报名成功！", apiCommonForm);
					}
					else {
						//判断上次涂氟时间 与本次涂氟时间  相差177天以上再记录
						String[] hospitalTimes = hospitalTime.split("\\_");
						int days=0;
						try {
							days = DateUtil.hoursBetween(date, DateUtil.toDefaultDate(hospitalTimes[0]));
							System.out.println("距离上次涂氟时间：" + days + "天");
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(days>=177){
							projectVolunteerFacade.save(model);
							ApiCommonForm form = new ApiCommonForm();
							form.setId(model.getFormId());
							ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
							apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
							projectVolunteerFacade.update(apiCommonForm);
							
							apiHospitalAppoint.setFluoride_time(sdf.format(date)+"_"+hospitalTime);
							projectVolunteerFacade.updateHospitalAppoint(apiHospitalAppoint);
							return webUtil.resMsg(1, "0000", "报名成功！", apiCommonForm);
						}
					}
				}
			}else{
				int result = projectVolunteerFacade.save(model);
				if(result>0){
					ApiCommonForm form = new ApiCommonForm();
					form.setId(model.getFormId());
					ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
					apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
					int result2=projectVolunteerFacade.update(apiCommonForm);
					if(result2>0){
						return webUtil.resMsg(1, "0000", "报名成功！", apiCommonForm);
					}
				}
			}
		}
		return webUtil.resMsg(0, "0002", "参数错误", null);
	}
	
	
	
	@RequestMapping("resposibilityReport")
	public ModelAndView resposibilityReport(@RequestParam("id")Integer id,HttpServletRequest request, 
			HttpServletResponse response) throws JDOMException, IOException{
		ModelAndView view = new ModelAndView("resposibility/resposibility_report");
		ApiNews oneNew = newsFacade.queryNewsDetail(id);
		ApiNews temp = new ApiNews();
		temp.setId(id);
		temp.setVisits(oneNew.getVisits()+1);
		newsFacade.updateVisits(temp);
		if(oneNew!=null)
		  oneNew.setContent(PNews.dealContent(oneNew.getContent()));
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
		view.addObject("oneNew", oneNew);
		return view;
	}
	
	@RequestMapping(value="entryFormNewView")
	public ModelAndView entryFormNewView(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id) throws JDOMException, IOException{
		ModelAndView view = new ModelAndView("commonForm/entryFormNew");
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
        				System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
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
        			//view = new ModelAndView("redirect:" + perfecturl);
					//return view;
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
				view.addObject("flag", "entryFormNew_"+id);
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        
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
			//StringBuffer url = request.getRequestURL();
			//String queryString = request.getQueryString();
			//String perfecturl = url + "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
        
		/*ApiCommonForm model = new ApiCommonForm();
		model.setId(id);
		ApiCommonForm apiCommonForm = projectVolunteerFacade.selectByParam(model);
		Date currentTimeDate = new Date();
		view.addObject("apiCommonForm", apiCommonForm);
		view.addObject("currentTime", currentTimeDate);
		view.addObject("userId",UserUtil.getUserId(request, response));*/
        ApiCommonForm commonForm = new ApiCommonForm();
		commonForm.setId(id);
		commonForm = projectVolunteerFacade.selectByParam(commonForm);
		view.addObject("title", commonForm.getTitle());
		view.addObject("content", commonForm.getContent());
		view.addObject("formId", id);
		view.addObject("user", user);
		view.addObject("special_fund_id", commonForm.getSpecial_fund_id());
		return view;
	}
	
	/**
	 * 加载表单
	 * @param id
	 * @return
	 */
	@RequestMapping("entryFormNewList")
	@ResponseBody
	public Map<String, Object> entryFormNewList(@RequestParam(value = "id") Integer id){
		ApiCommonForm model = new ApiCommonForm();
		model.setId(id);
		ApiCommonForm apiCommonForm = projectVolunteerFacade.selectByParam(model);
		return webUtil.successRes(apiCommonForm);
	}
	
	/**
	 * 牙齿报名记录
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping("toothForm_view")
	public ModelAndView toothForm_view(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="hospitalAppointName",required=false)String hospitalAppointName,
			@RequestParam(value="hospitalAppointId",required=true)Integer hospitalAppointId,
			@RequestParam(value = "id", required = true) Integer id){
		ModelAndView view = new ModelAndView("commonForm/toothForm");
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
        				System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
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
        			//view = new ModelAndView("redirect:" + perfecturl);
					//return view;
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        	}
        	else
        	{
        		//to do >> 暂时跳转到登陆页
				view = new ModelAndView("redirect:/user/sso/login.do?entrance=http://www.17xs.org/resposibilityReport/toothForm_view.do?id="+id+"&hospitalAppointId="+hospitalAppointId+"&hospitalAppointName="+hospitalAppointName);
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        
		/*ApiCommonForm model = new ApiCommonForm();
		model.setId(id);
		ApiCommonForm apiCommonForm = projectVolunteerFacade.selectByParam(model);
		Date currentTimeDate = new Date();
		view.addObject("apiCommonForm", apiCommonForm);
		view.addObject("currentTime", currentTimeDate);
		view.addObject("userId",UserUtil.getUserId(request, response));*/
        ApiHospitalAppoint apiHospitalAppoint = projectVolunteerFacade.queryHospitalAppointById(hospitalAppointId);
        ApiCommonForm commonForm = new ApiCommonForm();
		commonForm.setId(id);
		commonForm = projectVolunteerFacade.selectByParam(commonForm);
		view.addObject("title", commonForm.getTitle());
		view.addObject("content", commonForm.getContent());
		view.addObject("formId", id);
		view.addObject("user", user);
		view.addObject("special_fund_id", commonForm.getSpecial_fund_id());
		view.addObject("apiHospitalAppoint", apiHospitalAppoint);
		view.addObject("hospitalAppointId", hospitalAppointId);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		view.addObject("checkTime", sdf.format(date));
		return view;
	}
	
	/**
	 * 预约
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("appointmentForm_view")
	public ModelAndView appointmentForm_view(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id) throws ParseException{
		ModelAndView view = new ModelAndView("commonForm/appointmentForm");
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
        				System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
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
        			//view = new ModelAndView("redirect:" + perfecturl);
					//return view;
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
				view.addObject("flag", "appointmentForm_"+id);
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        ApiCommonForm commonForm = new ApiCommonForm();
  		commonForm.setId(id);
  		commonForm = projectVolunteerFacade.selectByParam(commonForm);
        //1.将form的医院加载到页面
        //2.报名人数查询（按时间段） 获取明天 04月15日 +（上午9:00-10:30）
        String counts="[";
        ApiCommonFormUser apiCommonFormUser = new ApiCommonFormUser();
        //apiCommonFormUser.setHospital(hospital);
        apiCommonFormUser.setFormId(id);
        Date date = new Date();
        for(int k=0;k<4;k++){
        	//date = new Date();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
        	date = sdf.parse("2017-05-01");//String-->Date   
        	counts+="[";
        	if(k==0){apiCommonFormUser.setHospital("宁波牙科医院(东部新城宁穿路1821号)");}
        	else if(k==1){apiCommonFormUser.setHospital("鄞州口腔医院(天静巷28号)");}
        	else if(k==2){apiCommonFormUser.setHospital("鄞州儿童口腔医院(嵩江东路669号)");}
        	else if(k==3){apiCommonFormUser.setHospital("北仑口腔医院(北仑区星中路8号)");}
	        for (int i = 0; i < 14; i++) {
				date = DateUtil.add(date, 1);
				String dateString=DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日 HH时MM分ss秒", date);
				String week=DateUtil.getWeekStr(date);
				counts+="[";
				for (int j = 0; j < 4; j++) {
					if(j==0){
						String dateString2=dateString+"("+week+")15:30-16:30";
						apiCommonFormUser.setDateTime(dateString2);
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						//System.out.println("dateString2>>>"+dateString2);
						//System.out.println("count1>>>"+count1);
						//counts[j]=count1;
						counts+=count1+",";
					}else if(j==1){
						String dateString2=dateString+"("+week+")16:30-17:30";
						apiCommonFormUser.setDateTime(dateString2);
						int count2 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						//System.out.println(dateString2);
						//System.out.println("count2>>>"+count2);
						counts+=count2+",";
					}else if(j==2){
						String dateString2=dateString+"("+week+")17:30-18:30";
						apiCommonFormUser.setDateTime(dateString2);
						int count3 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						//System.out.println(dateString2);
						//System.out.println("count3>>>"+count3);
						counts+=count3+",";
					}else if(j==3){
						String dateString2=dateString+"("+week+")18:30-19:30";
						apiCommonFormUser.setDateTime(dateString2);
						int count4 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						//System.out.println(dateString2);
						//System.out.println("count4>>>"+count4);
						counts+=count4;
					}
				}
				if(i==13){
					counts+="]";
				}else{
					counts+="],";
				}
	        }
	        if(k==3){
				counts+="]";
			}else{
				counts+="],";
			}
        }
        counts+="]";
        if(commonForm.getContent()!=null){
        	commonForm.setContent(StringUtil.delHTMLTag(commonForm.getContent()));
        }
        view.addObject("commonForm", commonForm);
  		view.addObject("formId", id);
  		view.addObject("user", user);
		view.addObject("counts", counts);
		return view;
	}
	
	/**
	 * 保存医院预约报名
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveAppointmentForm")
	@ResponseBody
	public Map<String, Object>  saveAppointmentForm(HttpServletRequest request,HttpServletResponse response,
			ApiCommonFormUser model)throws Exception{
		if(model!=null&&model.getFormId()!=null&&model.getUserId()!=null&&model.getInformation()!=null){
			//String info=model.getInformation().split(";")[2].split(":")[1].substring(1, 7);
			ApiCommonFormUser apiCommonFormUser = new ApiCommonFormUser();
			apiCommonFormUser.setUserId(model.getUserId());
			//apiCommonFormUser.setDateTime(info);
			apiCommonFormUser.setHospital(model.getHospital());
			apiCommonFormUser.setFormId(model.getFormId());
			int count = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
			if(count==0){
				int result = projectVolunteerFacade.save(model);
				if(result>0){
					ApiCommonForm form = new ApiCommonForm();
					form.setId(model.getFormId());
					ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
					apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
					int result2=projectVolunteerFacade.update(apiCommonForm);
					if(result2>0)
						return webUtil.resMsg(1, "0000", "报名成功！", apiCommonForm);
				}
			}
			else {
				
				return webUtil.resMsg(0, "0002", "半年内只能预约一次！", null);
			}
		}
		return webUtil.resMsg(0, "0002", "参数错误！", null);
	}
	
	/**
	 * 加载医院预约报名人数
	 * @param model
	 * @return
	 */
	@RequestMapping(value="loadCountAppointmentForm")
	@ResponseBody
	public Map<String, Object>  loadCountAppointmentForm(@RequestParam(value="formId")Integer formId)throws Exception{
		/*ApiCommonForm commonForm = new ApiCommonForm();
  		commonForm.setId(id);
  		commonForm = projectVolunteerFacade.selectByParam(commonForm);*/
        //1.将form的医院加载到页面
        //2.报名人数查询（按时间段） 获取明天 04月15日 +（上午9:00-10:30）
		//int[][][] counts= new int[4][15][4];
		int[][][] counts={{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
			{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}},{{0,0,0,0},
				{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
				{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}},{{0,0,0,0},{0,0,0,0},
					{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
					{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}},{{0,0,0,0},{0,0,0,0},{0,0,0,0},
						{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},
						{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}}}; 
        //String counts="[";
        /*ApiCommonFormUser apiCommonFormUser = new ApiCommonFormUser();
        apiCommonFormUser.setFormId(formId);
        for(int k=0;k<4;k++){
        	 Date date = new Date();
             date = DateUtil.add(date, 2);
        	//date = new Date();
        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
        	//date = sdf.parse("2017-05-01");//String-->Date   
        	//counts+="[";
        	if(k==0){apiCommonFormUser.setHospital("宁波牙科医院(东部新城宁穿路1821号)");}
        	else if(k==1){apiCommonFormUser.setHospital("鄞州口腔医院(天静巷28号)");}
        	else if(k==2){apiCommonFormUser.setHospital("鄞州儿童口腔医院(嵩江东路669号)");}
        	else if(k==3){apiCommonFormUser.setHospital("北仑口腔医院(北仑区星中路8号)");}
	        for (int i = 0; i < 14; i++) {
				date = DateUtil.add(date, 1);
				String dateString=DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日 HH时MM分ss秒", date);
				String week=DateUtil.getWeekStr(date);
				//counts+="[";
				for (int j = 0; j < 4; j++) {
					StringBuffer buffer = new StringBuffer();
					if(k==0 && j==0){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")08:30-10:00");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==0 && j==1){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")10:00-11:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}else if(k==0 && j==2){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")14:00-15:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}else if(k==0 && j==3){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")15:30-17:00");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}else if(k==1 && j==0){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")15:30-16:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==1 && j==1){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")16:30-17:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==2 && j==0){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")15:30-16:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==2 && j==1){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")16:30-17:30");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==3 && j==0){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")18:00-19:00");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					else if(k==3 && j==1){
						buffer = new StringBuffer();
						buffer.append(dateString+"("+week+")19:00-20:00");
						apiCommonFormUser.setDateTime(buffer.toString());
						int count1 = projectVolunteerFacade.countCommonFormUserByParam(apiCommonFormUser);
						counts[k][i][j]=count1;
					}
					
				}
	        }
        }*/
		return webUtil.successRes(counts);
	}
	
	/**
	 * 报名详情页面
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping("entryForm_detail")
	public ModelAndView entryForm_detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=true)Integer id){
		ModelAndView view = new ModelAndView("commonForm/entryForm_detail");
		Integer userId=UserUtil.getUserId(request, response);
		if(userId!=null){
			ApiCommonFormUser formUser = new ApiCommonFormUser();
			formUser.setId(id);
			formUser = projectVolunteerFacade.queryCommonFormUserDetailByParam(formUser);
			view.addObject("information", formUser.getInformation());
			ApiCommonForm commonForm = new ApiCommonForm();
			commonForm.setId(formUser.getFormId());
			commonForm = projectVolunteerFacade.selectByParam(commonForm);
			commonForm.setContent(StringUtil.delHTMLTag(commonForm.getContent()));
			view.addObject("commonForm", commonForm);
		}
		return view;
	}
	
	/**
	 * 根据formId查询报名信息
	 * @param request
	 * @param response
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="signForm_detail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject signForm_detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="formId",required=true)Integer formId,
			@RequestParam(value="state",required=true)Integer state,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws ParseException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Date nowTime=new Date();
		
		ApiCommonFormUser apiCommonFormUser=new ApiCommonFormUser();
		apiCommonFormUser.setFormId(formId);
		if(state==1){
			apiCommonFormUser.setRemark("1");//标识remark不为空
		}
		ApiPage<ApiCommonFormUser> page = projectVolunteerFacade.queryCommonFormList(apiCommonFormUser, pageNum, pageSize);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-pageNum*pageSize>=0 || pageNum*pageSize-page.getTotal()<pageSize)){
			for (ApiCommonFormUser p: page.getResultData()) {
				JSONObject userJson = new JSONObject();
				userJson.put("userId", p.getUserId());
				userJson.put("nickName", p.getUser_nick_name());
				userJson.put("headImage", p.getHeadImage());
				userJson.put("day", DateUtil.hoursBetween(DateUtil.dateStart(nowTime),DateUtil.dateStart(p.getCreateTime())));
				jsonArray.add(userJson);
			}
		}
		result.put("count", page.getTotal());
		result.put("data", jsonArray);
		item.put("code", 1);
		item.put("msg","success");
		item.put("result", result);
		return item;
	}
	
	
	
	
	/**
	 * 项目报名详情接口（项目ID，项目标题，项目logo，报名人数，单价，自定义选项，内容，报名列表（昵称，用户id，头像），截止天数）   分享参数：...
	 * @param request
	 * @param response
	 * @param activityId
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping(value="itemSignForm_detail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject itemSignForm_detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="activityId",required=true)Integer activityId, 
			String share) throws JDOMException, IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Integer userId = UserUtil.getUserId(request, response);
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		String openId ="";
		String token = "";
		String unionid = "";
		ApiFrontUser user = new ApiFrontUser();
	    if(userId == null || userId == 0)
	    {
	    	if(browser.equals("wx")){
	    		String weixin_code = request.getParameter("code");
	    		if(weixin_code != null && !"".equals(weixin_code)){
	    			Map<String, Object> mapToken = new HashMap<String, Object>(8);
		    		try {
		   				Object OToken = redisService.queryObjectData("weixin_token");
		   				token = (String)OToken;
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				token = mapToken.get("access_token").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
		    			
		    			user = CommonUtils.queryUser(request,openId,token,unionid);
		    		} catch (Exception e) {
		    			logger.error("resposibilityReport >> itemSignForm_detail"+ e);
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
		    			logger.error("resposibilityReport >> itemSignForm_detail",e);
		    		}
	    		}
	    	}
	    }
	//===========微信用户自动登陆end==============//
		
		Date nowTime=new Date();
		try {
			ApiActivityConfig config=homePageFacade.queryById(activityId);
			result.put("activityId", config.getId());
			result.put("projectId", config.getProjectId());
			result.put("unitPrice", config.getUnitPrice());
			result.put("content", config.getContent());
			result.put("type", config.getType());
			JSONArray jsArray = new JSONArray();
			List<String> cLists = new ArrayList<String>();
			String cus = config.getCustomItem();
			if(cus!=null && !"".equals(cus)){
				try {
					String[] c1 = cus.replace("：", ":").replace("；", ";").split(";");
					for (String string : c1) {
						String[] c2 = string.split(":");
						for (int i=0;i<c2.length;i++) {
							cLists.add(c2[i]);
						}
						jsArray.add(c2);
					}
				} catch (Exception e) {
					logger.info("resposibilityReport >> itemSignForm_detail "+e.getMessage());
					item.put("code", -1);
					item.put("msg", "自选参数error");
					return item;
				}
			}
			result.put("customItem", jsArray);
			
			
			//ApiProject p=projectFacade.queryProjectDetail(config.getProjectId());
			ApiCommonForm apiCommonForm=new ApiCommonForm();
			apiCommonForm.setId(config.getFormId());
			apiCommonForm=projectVolunteerFacade.selectByParam(apiCommonForm);
			result.put("projectTitle", config.getName());
			result.put("projectSubTitle", "");
			result.put("projectLogo", config.getLogoUrl());
			
			//团长名称
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(config.getUserId());
			apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			if(apiCompany == null){
				apiCompany = new ApiCompany();
				apiCompany.setActivityAdminIds(config.getUserId()+"");
				apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			}
			result.put("companyName", apiCompany.getName());
			
			result.put("number", apiCommonForm.getNumber());
			result.put("defaultOption", apiCommonForm.getDefaultOption());//默认选项
			result.put("information", apiCommonForm.getForm());
			result.put("gotoUrl", apiCommonForm.getGotoUrl());
			String date = DateUtil.dateString2(apiCommonForm.getEndTime());
			result.put("endTime", date.substring(11, 13) + "时" + date.substring(14,16) + "分");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			apiCommonForm.setEndTime(sdf.parse(sdf.format(apiCommonForm.getEndTime())));
			nowTime = sdf.parse(sdf.format(nowTime));
			
			result.put("endDay", DateUtil.hoursBetween(apiCommonForm.getEndTime(),nowTime));
			result.put("limit", apiCommonForm.getLimit());
			result.put("formId", config.getFormId());
			ApiCommonFormUser apiCommonFormUser=new ApiCommonFormUser();
			apiCommonFormUser.setFormId(config.getFormId());
			if(config.getUnitPrice().compareTo(BigDecimal.ZERO)==1){//单价大于0
				apiCommonFormUser.setRemark("1");//标识remark不为空
			}
			ApiPage<ApiCommonFormUser> page = projectVolunteerFacade.queryCommonFormList(apiCommonFormUser, 1, 7);
			if(page!=null && page.getTotal()>0){
				for (ApiCommonFormUser commonFormUser: page.getResultData()) {
					JSONObject userJson = new JSONObject();
					userJson.put("userId", commonFormUser.getUserId());
					userJson.put("nickName", commonFormUser.getUser_nick_name());
					userJson.put("headImage", commonFormUser.getHeadImage());
					//userJson.put("day", DateUtil.hoursBetween(nowTime,apiCommonFormUser.getCreateTime()));
					jsonArray.add(userJson);
				}
			}
			result.put("signList", jsonArray);
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
				StringBuffer url = new StringBuffer("http://www.17xs.org/visitorAlipay/tenpay/entryPay.html");//request.getRequestURL();
				String queryString = share;//request.getQueryString();
				
				String perfecturl = url + "?" + queryString;System.out.println("url="+perfecturl);
				SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
				result.put("appId",map.get("appId"));
				result.put("timestamp",map.get("timeStamp"));
				result.put("noncestr",map.get("nonceStr"));
				result.put("signature",map.get("signature"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("resposibilityReport >> itemSignForm_detail ");
			item.put("code", -1);
			item.put("msg", "活动配置参数有误");
			return item;
		}
		if(userId == null){
			if(user != null){
				result.put("realName", user.getRealName());
				result.put("mobile", user.getMobileNum());
			}
		}else{
			user = userFacade.queryById(userId);
			result.put("realName", user.getRealName());
			result.put("mobile", user.getMobileNum());
		}
		
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}
	
	/**
	 * 保存报名信息，报名支付专用
	 * @param model
	 * @param request
	 * @param response
	 * @param formId
	 * @return
	 */
	@RequestMapping(value="saveFormNew",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject  saveFormNew(ApiCommonFormUser model,HttpServletRequest request , HttpServletResponse response,
			@RequestParam(value = "formId", required = true) Integer formId,
			@RequestParam(value = "state", required = true) Integer state){
		JSONObject item  = new JSONObject();
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
	    			logger.error("resposibilityReport >> saveFormNew"+ e);
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
	    			logger.error("resposibilityReport >> saveFormNew",e);
	    			item.put("code", -1);
					item.put("msg", "login error");
					return item;
	    		}
	    	}
	    	else
	    	{
	    		result.put("url", "http://www.17xs.org/ucenter/user/Login_H5.do?flag=entryFormNew_"+formId);
				item.put("result", result);
				item.put("code", 2);
				item.put("msg", "账号密码登陆，非wx浏览器");
				return item;
	    	}
	    }else {
	    	user = userFacade.queryById(userId);
	    }
	//===========微信用户自动登陆end==============//
		if(model!=null&&model.getFormId()!=null){
			if(state==1){
			ApiCommonFormUser apiCommonFormUser=new ApiCommonFormUser();
			apiCommonFormUser.setFormId(model.getFormId());
			apiCommonFormUser.setUserId(userId);
			try {
			apiCommonFormUser=projectVolunteerFacade.queryCommonFormUserDetailByParam(apiCommonFormUser);
			if(apiCommonFormUser == null){
				ApiCommonForm form = new ApiCommonForm();
				form.setId(model.getFormId());
				form = projectVolunteerFacade.selectByParam(form);
				Date newDate=new Date();
				model.setUserId(userId);
				model.setCreateTime(newDate);
				model.setSpecial_fund_id(0);
				model.setUser_nick_name(user.getNickName());
				model.setTitle(form.getTitle());
				model.setState(0);
				int res = projectVolunteerFacade.save(model);
				if(res>0){
					/*ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
					apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
					int result2=projectVolunteerFacade.update(apiCommonForm);*/
					/*if(result2>0){*/
						result.put("gotoUrl", form.getGotoUrl());
						result.put("userFormId", res);
						item.put("result", result);
						item.put("code", 1);
						item.put("msg", "success");
						return item;
					/*}
					item.put("code", -1);
					item.put("msg", "update commonForm num error");
					return item;*/	
				}else{
					item.put("code", -1);
					item.put("msg", "save commonFormUser error");
					return item;
				}
			}else if(apiCommonFormUser != null && 
					(apiCommonFormUser.getRemark() == null || "".equals(apiCommonFormUser.getRemark()))){
				ApiCommonForm form = new ApiCommonForm();
				form.setId(model.getFormId());
				form = projectVolunteerFacade.selectByParam(form);
				
				model.setId(apiCommonFormUser.getId());
				int res = projectVolunteerFacade.update(model);
				if(res>0){
					result.put("gotoUrl", form==null?null:form.getGotoUrl());
					result.put("userFormId", apiCommonFormUser.getId());
					item.put("result", result);
					item.put("code", 1);
					item.put("msg", "success");
					return item;
				}else{
					item.put("code", -1);
					item.put("msg", "update commonFormUser error");
					return item;
				}
			}else{
				item.put("code", 3);
				item.put("msg", "重复报名");
				return item;
			}
			
			} catch (Exception e) {
				logger.info("resposibilityReport >> saveFormNew >>"+e.getMessage());
				item.put("code", -1);
				item.put("msg", "报名失败");
				return item;
			}
		}else{
			ApiCommonFormUser apiCommonFormUser=new ApiCommonFormUser();
			apiCommonFormUser.setFormId(model.getFormId());
			apiCommonFormUser.setUserId(userId);
			try {
			apiCommonFormUser=projectVolunteerFacade.queryCommonFormUserDetailByParam(apiCommonFormUser);
			if(apiCommonFormUser == null){
				ApiCommonForm form = new ApiCommonForm();
				form.setId(model.getFormId());
				form = projectVolunteerFacade.selectByParam(form);
				Date newDate=new Date();
				model.setUserId(userId);
				model.setCreateTime(newDate);
				model.setSpecial_fund_id(0);
				model.setUser_nick_name(user.getNickName());
				model.setTitle(form.getTitle());
				model.setState(0);
				int res = projectVolunteerFacade.save(model);
				if(res>0){
					ApiCommonForm apiCommonForm =projectVolunteerFacade.selectByParam(form);
					apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
					int result2=projectVolunteerFacade.update(apiCommonForm);
					if(result2>0){
						result.put("gotoUrl", form.getGotoUrl());
						result.put("userFormId", res);
						item.put("result", result);
						item.put("code", 1);
						item.put("msg", "success");
						return item;
					}
					item.put("code", -1);
					item.put("msg", "update commonForm num error");
					return item;	
				}else{
					item.put("code", -1);
					item.put("msg", "save commonFormUser error");
					return item;
				}
			}else{
				item.put("code", 3);
				item.put("msg", "重复报名");
				return item;
			}
			
			} catch (Exception e) {
				logger.info("resposibilityReport >> saveFormNew >>"+e.getMessage());
				item.put("code", -1);
				item.put("msg", "报名失败");
				return item;
			}
		}
		
		}	
		else{
			item.put("code", 2);
			item.put("msg", "参数错误");
			return item;
		}
	}
	
	/**
	 * 查询表单提交信息与答案
	 * @param response
	 * @param request
	 * @param formId
	 * @return
	 */
	@RequestMapping(value="getCommonFormUserInfo",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getCommonFormUserInfo(HttpServletResponse response ,HttpServletRequest request,
			@RequestParam(value = "formId" ,required = false) Integer formId){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		Integer userId = UserUtil.getUserId(request, response);
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		String openId ="";
		String token = "";
		String unionid = "";
		//StringBuffer url = request.getRequestURL();
		//String queryString = request.getQueryString();
		//String perfecturl = url + "?" + queryString;
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
	    					//String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
	    					/*result.put("url", url_weixin_code);
	    					item.put("result", result);*/
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
	    			logger.error("resposibilityReport >> saveFormNew"+ e);
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
	    			logger.error("resposibilityReport >> saveFormNew",e);
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
		try {
			ApiCommonFormUser commonFormUser = new ApiCommonFormUser();
			commonFormUser.setFormId(formId);
			commonFormUser.setUserId(userId);
			commonFormUser = projectVolunteerFacade.queryCommonFormUserDetailByParam(commonFormUser);
			if(commonFormUser != null){
				String content = commonFormUser.getInformation();
				String[] content1s = content.split(";");
				
				ApiConfig apiConfig = new ApiConfig();
				apiConfig.setConfigKey("form_answer_"+formId);;
				List<ApiConfig> configs = commonFacade.queryList(apiConfig);
				String[] cons = configs.get(0).getConfigValue().split(",");
				for (int i=0;i<content1s.length;i++) {
					String[] content2s = content1s[i].replace("\"", "").replace("{", "").replace("}", "").split(":");
					List<String> list = new ArrayList<String>();
					list.add(cons[i]);
					list.add(content2s[1]);
					array.add(list);
				}
				result.put("answer", array);
				item.put("result", result);
				item.put("code", 1);
				item.put("msg", "success");
				return item;
			}
			item.put("code", 3);
			item.put("msg", "报名信息为空");
			return item;
		} catch (Exception e) {
			logger.info("resposibilityReport >> getCommonFormUserInfo >>"+e.getMessage());
			item.put("code", -1);
			item.put("msg", "error");
			return item;
		}
	}
	
	/**
	 * 发起人接口
	 * @param request
	 * @param response
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getInitiatorDetail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject initiator_detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		
		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
		userInfo.setProjectId(projectId);
		userInfo.setPersonType(2);
		String key = PengPengConstants.PROJECT_USERINFO_LIST+"_"+projectId;
		userInfo.initNormalCache(false, DateUtil.DURATION_HOUR_S, key);
		List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
		if(userInfos!=null && userInfos.size()>0){
			result.put("headImage", (userInfos.get(0).getHeadImageUrl()==null||userInfos.get(0).getHeadImageUrl()=="")?"http://www.17xs.org/res/images/detail/people_avatar.jpg":userInfos.get(0).getHeadImageUrl());
			result.put("realName", userInfos.get(0).getRealName());
			result.put("linkMan", userInfos.get(0).getLinkMan());
			result.put("mobile", userInfos.get(0).getLinkMobile());
			result.put("familyAddress", userInfos.get(0).getFamilyAddress());
		
		}else{
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "error");
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}
	
	/**
	 * 项目详情接口
	 * @param request
	 * @param response
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getProjectDetail",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject project_detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="userId",required=false)Integer userId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		ApiFrontUser user = new ApiFrontUser();
		if(userId != null){
			user = userFacade.queryById(userId);
			result.put("realName", user.getRealName());
			result.put("mobileNum", user.getMobileNum());
		}else{
			result.put("realName", "");
			result.put("mobileNum", "");
		}
		ApiProject p=projectFacade.queryProjectDetail(projectId);
		if(p !=null){
			result.put("projectId", p.getId());
			result.put("projectLogo", p.getCoverImageUrl());
			result.put("projectTitle", p.getTitle());
			result.put("projectSubTitle", p.getSubTitle());
			result.put("totalMoney", p.getCryMoney());
			result.put("donatAmount", p.getDonatAmount());
			result.put("donationNum", p.getDonationNum());
			double process = 0.0;
			if(p.getCryMoney()>=0.001){
				process = p.getDonatAmount() / p.getCryMoney();
			}
			result.put("donatePercent", process > 1 ? "100" : StringUtil.doublePercentage(p.getDonatAmount(),p.getCryMoney(),0));
			//随机留言
			ApiConfig apiConfig = new ApiConfig();
	    	apiConfig.setConfigKey(StringUtil.LEAVEWORD);
	    	List<ApiConfig> configs = commonFacade.queryList(apiConfig);
	    	if(configs!=null&&configs.get(0)!=null&&configs.get(0).getConfigValue()!=null&&configs.get(0).getConfigValue()!=""){
	    		String[] configss = configs.get(0).getConfigValue().split("。");
	    		Random random = new Random();
	    		int i = random.nextInt(configss.length);
	    		result.put("leaveWord", configss[i]);
	    	}
		}else{
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "项目不存在error");
			return item;
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}
	
	/**
	 * 项目内容转化成小程序识别的html格式
	 * @param request
	 * @param response
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getProjectContent",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject project_content(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		
		ApiProject p=projectFacade.queryProjectDetail(projectId);
		if(p !=null){//详情接口(content)  span(text) img(image)  strong、b(<view class="b"></view) p(view class="pp")  br(\n) &nbsp( )
			/*String content = p.getContent().replaceAll("span", "text").replaceAll("img", "image").replaceAll("<br/>", "\n")
					.replaceAll("<strong", "<view class=\"b\"").replaceAll("</strong>", "</view>")
					.replaceAll("<b", "<view class=\"b\"").replaceAll("</b>", "</view>")
					.replaceAll("<p", "<view class=\"pp\"").replaceAll("</p>", "</view>")
					.replaceAll("&nbsp", " ");*/
			
			result.put("projectId", p.getId());
			result.put("projectContent", p.getContent());
		}else{
			item.put("result", result);
			item.put("code", -1);
			item.put("msg", "项目不存在error");
			return item;
		}
		item.put("result", result);
		item.put("code", 1);
		item.put("msg", "success");
		return item;
	}
}
