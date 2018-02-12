
package com.guangde.home.controller.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IHomePageFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IPayMoneyRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.*;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.*;
import com.guangde.home.vo.index.IndexForm;
import com.guangde.pojo.ApiPage;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("index")
public class IndexController {
	Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private IHomePageFacade homePageFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade CommonFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private INewsFacade newsFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	
	@Autowired
	private IPayMoneyRecordFacade payMoenyRecordFacade;
	
	IndexForm from = new IndexForm();

	@RequestMapping("index")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView("index");
		//对于页面要显示的捐款总金额，需要补0，同时只到亿位
		int w = 9;
//		Object obj = redisService.queryObjectData(PengPengConstants.DONATION_TOTAL_MONEY);
//		if(obj != null){
//			view.addObject("totalMoney", obj);
//		}else{
//			Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
//			String totalMoney = String.valueOf(Itotal);
//			if(totalMoney.length() < w){
//				int size = w - totalMoney.length();
//				for (int i = 0; i < size; i++) {
//					totalMoney="0"+totalMoney;
//				}
//			}
//			redisService.saveObjectData(PengPengConstants.DONATION_TOTAL_MONEY, totalMoney, DateUtil.DURATION_MIN_S);
//			view.addObject("totalMoney", totalMoney);
//		}
		
		return view;
	}
	
	@RequestMapping("newindex")
	public ModelAndView newindex(@RequestParam(value="flag",required=false)String flag,
			@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
			HttpServletRequest request, HttpServletResponse response) {

		if(flag == null){
			String isMobile = (String)request.getSession().getAttribute("ua");
			ModelAndView viewback = new ModelAndView("redirect:/index/index_h5.do");
	        if ("mobile".equals(isMobile) && extensionPeople!=null)
	        {
	             viewback.setViewName("redirect:/index/index_h5.do?extensionPeople="+extensionPeople);
	            return viewback;
	        }
	        if("mobile".equals(isMobile)){
	        	return viewback;
	        }
		}
		ModelAndView view = new ModelAndView("newindex");
		//推广员
		logger.info("extensionPeople >>>>" +extensionPeople);
		if(extensionPeople!=null){
			request.getSession().setAttribute("extensionPeople", extensionPeople);
		}
		view.addObject("extensionPeople", extensionPeople);
		//轮播图片
		banner(view);
		//首页总数统计
		totalCount(view);
		//专项基金 specialFund
		specialFund(view);
		//本周推荐
		weekProject(view);
		//最新新闻,爱心故事
		newsList(view);
		//最新捐款列表
		donateRecord(view);
		//善管家列表
		loveGroup(view);
		
        // 1.验证是否登入
        Integer  userId = UserUtil.getUserId(request, response);
        if(userId != null) {
        	ApiFrontUser user = userFacade.queryById(userId);
        	view.addObject("user", user);
        }
        
        view.addObject("userId", userId);
        
        
        
        /*
         * 领域
         */
       setField(view);


        /*
         * 省份模块数据
         */
       
       setProvince(view,request);

       
       /*
        * 募捐项目模块数据
        */
       
       setProjectList(view);
      
       /*
        * 善款去向
        */
       setDonateGo(view);
       
       /*
        *友情链接 
        */
       setFriendLink(view);
       
       //爱心企业链接
       setLoveCompany(view);
       
		return view;
	}
	
	/**
	 * 爱心企业链接
	 * @param view
	 */
	public void setLoveCompany(ModelAndView view){
		Object lobjImg = redisService.queryObjectData(PengPengConstants.INDEX_LOVECOMPANY);
		if(lobjImg != null){
		view.addObject("loveCompany", lobjImg);
		}else {
			List<ApiBFile> bFiles = null;
			ApiBFile apiBFile = new ApiBFile();
			apiBFile.setCategory("loveCompany");
			bFiles = CommonFacade.queryApiBfile(apiBFile);
			redisService.saveObjectData(PengPengConstants.INDEX_LOVECOMPANY, bFiles, DateUtil.DURATION_MONTH_S);
			view.addObject("loveCompany", bFiles);
		}
	}
	
	/**
	 * 友情链接
	 * @param view
	 */
	public void setFriendLink(ModelAndView view){
		try {
			Object objImg = redisService.queryObjectData(PengPengConstants.INDEX_FRIENDLINK_IMG);
			//Object textImg = redisService.queryObjectData(PengPengConstants.INDEX_FRIENDLINK_TEXT);
			if(objImg !=null){
				view.addObject("linkImg", objImg);
				//view.addObject("textImg", textImg);
			}else{
				List<ApiBFile> bfiles = null;
				ApiBFile apiBfile = new ApiBFile(); 
				apiBfile.setCategory("friendLink");
				bfiles = CommonFacade.queryApiBfile(apiBfile);
				view.addObject("linkImg", bfiles);
				redisService.saveObjectData(PengPengConstants.INDEX_FRIENDLINK_IMG, bfiles, DateUtil.DURATION_MONTH_S);
				/*if(bfiles.size()<=6){
					view.addObject("linkImg", bfiles);
				}else{
					List<ApiBFile> imgBfiles = new ArrayList<ApiBFile>();
					List<ApiBFile> textBfiles = new ArrayList<ApiBFile>();
					for(int i = 0 ; i<bfiles.size();i++){
						if(i<=5){
							imgBfiles.add(bfiles.get(i));
						}else{
							textBfiles.add(bfiles.get(i));
						}
					}
					redisService.saveObjectData(PengPengConstants.INDEX_FRIENDLINK_IMG, bfiles, DateUtil.DURATION_MONTH_S);
					redisService.saveObjectData(PengPengConstants.INDEX_FRIENDLINK_TEXT, textBfiles, DateUtil.DURATION_MONTH_S);
					view.addObject("linkImg", bfiles);
					view.addObject("textImg", textBfiles);
				}*/
			}
		
		} catch (Exception e) {
			// 后台服务发生异常
			e.printStackTrace();
			
		}
	}
	
	/**
	 * 领域数据
	 * @param view
	 */
	public void setField(ModelAndView view){
		
	       Object objAtc = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_FIELD);
			if(objAtc != null){
				view.addObject("atc", objAtc);
			}else{

				List<ApiTypeConfig> atc = commonFacade.queryList();
				List<ApiTypeConfig> atcnew = new ArrayList<ApiTypeConfig>();
				if(atc.size()>7){
					for(int i = 0 ; i<7;i++){
						atcnew.add(atc.get(i));
					}
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_FIELD, atcnew, DateUtil.DURATION_WEEK_S);
					view.addObject("atc", atcnew);
				}else{
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_FIELD, atc, DateUtil.DURATION_WEEK_S);
					view.addObject("atc", atc);
				}
			}
	}
	/**
	 * 省份数据
	 * @param view
	 */
	public void setProvince(ModelAndView view,HttpServletRequest request){
     	Object objProvince = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_PROVINCE);
		if(objProvince != null){
			view.addObject("provinces", objProvince);
		}else{
			ApiProjectArea  area  = new ApiProjectArea();
	    	/*String  province = "";
		   	String  ip = "";
		   	ip = request.getRemoteAddr();
		   	ip = "ip="+ip;

		   	try {
				  province = IPAddressUtil.getAddress(ip, "utf-8");
				  logger.info("IPAddressUtil >> province = "+province);
			} catch (UnsupportedEncodingException e) {
				logger.error("IPAddressUtil.getAddress()>> ",e);
			}
	   	
			if(!StringUtils.isEmpty(province)&&!"0".equals(province)){
				province = province.replaceAll("省", "");
				area.setProvince(province);
			}*/
			
			ApiPage<ApiProjectArea> apiPage1 =  projectFacade.queryProjectAreaList(area, 1, 7);
	   	
	       List<ApiProjectArea> projects = apiPage1.getResultData();
	   	
	       List<String> provinces = new ArrayList<String>();
	       
	       for(ApiProjectArea pa:projects)
	       {
	    	   provinces.add(pa.getProvince());
	       }
	      
	       /*if(!provinces.contains(province))
	       {
	       		provinces.add(0,province);
	       }else
	       {
	    	   provinces.remove(province);
	    	   provinces.add(0, province);
	       }*/
      
			redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_PROVINCE, provinces, DateUtil.DURATION_TEN_S);
			view.addObject("provinces", provinces);
		}
	}
	/**
	 * 募捐项目数据
	 * @param view
	 */
	public void setProjectList(ModelAndView view){
		
		 try
	       {
	    	Object objDonate = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_DONATEPROJECTLIST);
	   		if(objDonate != null){
	   			view.addObject("projectList", objDonate);
	   		}else{
	           ApiProject project = new ApiProject();
	           
	          /* project.setOrderDirection("desc");
	           project.setOrderBy("lastUpdateTime");*/
			/*	project.setOrderDirection("desc");
				project.setOrderBy("issueTime desc,donatAmount/cryMoney");
	           project.setIsHide(0);
	           project.setIsRecommend(0);
	           List<Integer> states = new ArrayList<Integer>();
		       	states.add(ProjectConstant.PROJECT_STATUS_COLLECT);
		       	project.setStates(states);*/
	       	
	           ApiPage<ApiProject> proPage = projectFacade.queryProjectBy30(1, 9);//queryProjectList(project, 1, 9);
	           
	           List<ApiProject> projectList = proPage.getResultData();
	           // 无数据
	           if (projectList.size() > 0)
	           {
	           	String donatePercent = "" ; 
	           	double leaveCryAmount = 0.0 ;
	           	double process = 0.0;
	           	for(ApiProject pro : projectList){
					if(pro.getIsOverMoney() != null && pro.getIsOverMoney() == 1){
						leaveCryAmount = pro.getOverMoney().doubleValue() - pro.getDonatAmount();
					}else {
						leaveCryAmount = pro.getCryMoney()-pro.getDonatAmount();
					}
	           		//leaveCryAmount = pro.getCryMoney()-pro.getDonatAmount();
	           		leaveCryAmount = new BigDecimal(leaveCryAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	           		if(leaveCryAmount<0){
	           			leaveCryAmount = 0.0 ;
	           		}
	           		 process = 0.0;
	                    if (pro.getCryMoney() >= 0.001)
	                    {
	                        process = pro.getDonatAmount() / pro.getCryMoney();
	                    }
	                    donatePercent = process > 1 ? "100" : StringUtil.doublePercentage(pro.getDonatAmount(), pro.getCryMoney(), 0);
	                  
	           		pro.setDonatePercent(donatePercent);
	           		pro.setLeaveCryMoney(leaveCryAmount);
	           	}
	           	view.addObject("projectList", projectList);
	           }
	   			redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_DONATEPROJECTLIST, projectList, DateUtil.DURATION_MIN_S);
	   		}

	       }
	       catch (Exception e)
	       {
	       	e.printStackTrace();
	       	logger.error("queryProjectListNew>> ",e);
	       }

	}
	/**
	 * 善款去向数据
	 * @param view
	 */
	public void setDonateGo(ModelAndView view){
		
		  Object objGo = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_DONATEGO);
			if(objGo != null){
				view.addObject("payList", objGo);
			}else{
			       ApiPage<ApiPayMoneyRecord>  payPage = payMoenyRecordFacade.queryPayMoneyRecordGO(null, 1, 4);
			       List<ApiPayMoneyRecord> payList = payPage.getResultData();
				       for(ApiPayMoneyRecord pay :payList){
				    	   pay.setPayDate(DateUtil.parseToDefaultDateString(pay.getPayMoneyTime()));
				       }
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_DONATEGO, payList, DateUtil.DURATION_TEN_S);
					view.addObject("payList", payList);
				
			}

	}
	
	@RequestMapping("index_h5")
	public ModelAndView index_h5(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="extensionPeople",required=false)Integer extensionPeople) throws JDOMException, IOException {
		//判断是pc端访问还是h5访问
		String isMobile = (String)request.getSession().getAttribute("ua");
		ModelAndView viewback = new ModelAndView("redirect:/index/newindex.do");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
             viewback.setViewName("redirect:/index/newindex.do?extensionPeople="+extensionPeople);
            return viewback;
        }
        if(!"mobile".equals(isMobile)){
        	return viewback;
        }
		
		ModelAndView view = new ModelAndView("h5/index");
		List<ApiTypeConfig> atc = commonFacade.queryList();
		logger.info("extensionPeople >>>>" +extensionPeople);
		request.getSession().setAttribute("extensionPeople", extensionPeople);
		view.addObject("extensionPeople", extensionPeople);
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
		bfiles = CommonFacade.queryApiBfile(apiBfile);
		view.addObject("bannerList", bfiles);
		
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
			//String queryString = request.getQueryString();
			String perfecturl = url.toString() ;//+ "?" + queryString;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
		return view;
	}
	
	@RequestMapping("index_h5_new")
	public ModelAndView index_h5_new(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="extensionPeople",required=false)Integer extensionPeople) {
		//判断是pc端访问还是h5访问
		String isMobile = (String)request.getSession().getAttribute("ua");
		ModelAndView viewback = new ModelAndView("redirect:/index/newindex.do");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
             viewback.setViewName("redirect:/index/newindex.do?extensionPeople="+extensionPeople);
            return viewback;
        }
        if(!"mobile".equals(isMobile)){
        	return viewback;
        }
		
		ModelAndView view = new ModelAndView("h5/index_new");
		List<ApiTypeConfig> atc = commonFacade.queryList();
		logger.info("extensionPeople >>>>" +extensionPeople);
		request.getSession().setAttribute("extensionPeople", extensionPeople);
		view.addObject("extensionPeople", extensionPeople);
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
  		//banner
  		banner(view);
  		//projectBanner
  		projectBanner(view);
		//专项基金
		specialFund(view);
		//推荐项目
		weekProject_new(view);
		//项目tag
		ApiConfig config = new ApiConfig();
		config.setConfigKey("projectTag");
		List<ApiConfig> apiConfigs =  commonFacade.queryList(config);
		if(apiConfigs!=null && apiConfigs.size()>0){
			view.addObject("projectTag", apiConfigs.get(0).getConfigValue());
		}
		return view;
	}
	
	/**
	 * 加载累计捐款金额
	 * @return
	 */
	@RequestMapping("loadTotalMoney")
	@ResponseBody
	public Integer loadTotalMoney(){
		 //对于页面要显示的捐款总金额，需要补0，同时只到亿位
  		int w = 9;
		Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
		String totalMoney = String.valueOf(Itotal);
		if(totalMoney.length() < w){
			int size = w - totalMoney.length();
			for (int i = 0; i < size; i++) {
				totalMoney="0"+totalMoney;
			}
		}
		redisService.saveObjectData(PengPengConstants.DONATION_TOTAL_MONEY, totalMoney, DateUtil.DURATION_MIN_S);
		return Itotal;
	}
	/*
	 * 返回首页的推荐项目
	 * 
	 * @param type 类型
	 * 
	 * @return 推荐项目json数据
	 */
	@ResponseBody
	@RequestMapping("donationlist")
	public JSONObject donationList(
			@RequestParam(value = "type", required = false, defaultValue = "1") String type) {
		int hot = -1;
		int count = 9;
		if (StringUtils.isNotEmpty(type)) {
			if ("0".equals(type)) {
				hot = 1;
			}
		}
		type = ProjectUtil.field(Integer.valueOf(type));
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		// 把取到的热门项目封装成json数据
		try {
			List<ApiProject> projects = null;
			String key = PengPengConstants.INDEX_PROJECT_FIELD_LIST+"_"+type;
//			Object obj = redisService.queryObjectData(key);
//			if(obj != null){
//				projects = (List<ApiProject>)obj;
//			}else{
				if (hot == 1) {
					ApiProject ap = new ApiProject();
					ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
					ap.setIsHide(0);
					ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap,
							1, 9);
					projects = apiPage.getResultData();
					
				}else {
					projects = projectFacade.queryDonation(hot, type,
							count);
				}
//				redisService.saveObjectData(key, projects, DateUtil.DURATION_HOUR_S);
//			}
			//无数据
			if (projects == null || projects.size() == 0) {
				data.put("result", 1);
			} else {
				 for (ApiProject project : projects) {
				 JSONObject item = new JSONObject();
				 item.put("itemId", project.getId());
				 item.put("type", project.getType());
				 item.put("title", project.getTitle());
				 item.put("information", project.getInformation());
				 item.put("donaAmount", project.getDonatAmount());
				 item.put("cryMoney", project.getCryMoney());
				 item.put("imageurl", project.getCoverImageUrl());
				 item.put("url", "url");
				 item.put("endtime", project.getDeadline());
				 items.add(item);
				}
				data.put("items", items);
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
	
	//轮播图
	public void banner (ModelAndView view){
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_BANNER_LIST);
		if(obj != null){
			view.addObject("bannerList", obj);
		}else{
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory("banner");
			bfiles = CommonFacade.queryApiBfile(apiBfile);
			view.addObject("bannerList", bfiles);
			redisService.saveObjectData(PengPengConstants.INDEX_BANNER_LIST, bfiles, DateUtil.DURATION_FIVE_S);
		}
	}
	
	//轮播图
	public void projectBanner (ModelAndView view){
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_BANNER_LIST);
		if(obj != null){
			view.addObject("projectbannerList", obj);
		}else{
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory("ProjectBanner");
			bfiles = CommonFacade.queryApiBfile(apiBfile);
			view.addObject("projectbannerList", bfiles);
			redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_BANNER_LIST, bfiles, DateUtil.DURATION_FIVE_S);
		}
	}
	
	//总数统计
	public void totalCount(ModelAndView view){
		//帮助的家庭数
//		Object objHelp = redisService.queryObjectData(PengPengConstants.HELP_FAMILY_NUMBER);
//		if(objHelp != null){
//			view.addObject("family", objHelp);
//		}else{
			Integer donateProject = userFacade.countDonateProject(true,"",1);
//			redisService.saveObjectData(PengPengConstants.HELP_FAMILY_NUMBER, donateProject, DateUtil.DURATION_TEN_S);
			view.addObject("family", donateProject);
//		}
		//捐款的总金额
//		Object obj = redisService.queryObjectData(PengPengConstants.DONATION_TOTAL_MONEY);
//		if(obj != null){
//			view.addObject("totalMoney", obj);
//		}else{
			Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
//			redisService.saveObjectData(PengPengConstants.DONATION_TOTAL_MONEY, Itotal, DateUtil.DURATION_TEN_S);
			view.addObject("totalMoney", Itotal);
//		}
		//累计捐款人数
//		Object peopleobj = redisService.queryObjectData(PengPengConstants.DONATION_TOTAL_PEOPLE_NUMBER);
//		if(peopleobj != null){
//			view.addObject("people", peopleobj);
//		}else{
			Integer donatePeople = userFacade.countDonatePeople(true);
//			redisService.saveObjectData(PengPengConstants.DONATION_TOTAL_PEOPLE_NUMBER, donatePeople, DateUtil.DURATION_TEN_S);
			view.addObject("people", donatePeople);
//		}
		//累计帮助人数
//		Object helpPeople = redisService.queryObjectData(PengPengConstants.INDEX_HELP_PEOPLE_TOTAL);
//		if(helpPeople != null){
//			view.addObject("helpPeople", helpPeople);
//		}else {
			ApiConfig config = new ApiConfig();
			config.setConfigKey("helpPeople");
			List<ApiConfig> list = commonFacade.queryList(config);
			if(list != null && list.size() > 0){
//				redisService.saveObjectData(PengPengConstants.INDEX_HELP_PEOPLE_TOTAL, list.get(0).getConfigValue(), DateUtil.DURATION);
				view.addObject("helpPeople", list.get(0).getConfigValue());
			}
//		}
		
	}
	
	//专项基金  specialFund
	public void specialFund(ModelAndView view){
		Object specialFund1 = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_1");
		Object specialFund2 = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_2");
		if(specialFund1 != null && specialFund2 != null){
			//专项基金的项目
	        view.addObject("specialFundList", (List<String>)specialFund2);
	        //专项基金的tab标题名字
	        view.addObject("tvbTitleList", (List<ApiProject>)specialFund1);
		}else{
			ApiConfig aConfig = new ApiConfig();
			aConfig.setConfigKey("specialFund");
			List<ApiConfig> acg = commonFacade.queryList(aConfig);
			if(acg.size() > 0){
				aConfig = acg.get(0);
				String tvbTitle = null;
				String tvbProjectId = null;
				String[] pNum = aConfig.getConfigValue().split(";");
				int totalSF = pNum.length;
				List<ApiProject> specialFundList = new ArrayList<ApiProject>(totalSF);
				List<String> tvbTitleList = new ArrayList<String>(totalSF);
				if (!aConfig.getConfigValue().contains(":")) {
					//不显示专项基金
					view.addObject("isShowSF", 0);
				}else {
					for (int i = 0; i < pNum.length; i++) {
						tvbTitle = pNum[i].split(":")[0];
						tvbTitleList.add(tvbTitle);
						tvbProjectId = pNum[i].split(":")[1];
						ApiProject apiProject = projectFacade.queryProjectDetail(Integer.valueOf(tvbProjectId));
						if(StringUtils.isNotEmpty(apiProject.getSubTitle()) && apiProject.getSubTitle().length() > 34){
							apiProject.setSubTitle(apiProject.getSubTitle().subSequence(0, 34)+"...");
						}
						specialFundList.add(apiProject);
					}
					//专项基金的项目
			        view.addObject("specialFundList", specialFundList);
			        //专项基金的tab标题名字
			        view.addObject("tvbTitleList", tvbTitleList);
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_1", tvbTitleList, DateUtil.DURATION_TEN_S);
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_2", specialFundList, DateUtil.DURATION_TEN_S);
				}
			}else{
				view.addObject("isShowSF", 0);
			}
		}
	}
	
	public void weekProject(ModelAndView view) {
		//本周推荐
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST);
		if (obj != null) {
			view.addObject("rdList", obj);
		} else {
			 ApiProject ap = new ApiProject();
	         ap.setOrderDirection("desc");
	         ap.setIsHide(0);
	         ap.setIsRecommend(1);
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, 1, 3);
	         view.addObject("rdList", apiPage.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}
		//众筹项目
		/*Object obj2 = redisService.queryObjectData(PengPengConstants.INDEX_CROWDFUND_ROJECT_LIST);
		if (obj2 != null) {
			view.addObject("gardenList", obj2);
		} else {
			 ApiProject ap = new ApiProject();
			 ap.setField("garden");
			ap.setState(240);
			ap.setIsHide(0);
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, 1, 2);
	         view.addObject("gardenList", apiPage.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_CROWDFUND_ROJECT_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}*/
	}
	
	public void weekProject_new(ModelAndView view) {
		//本周推荐
		
		/*Object obj = redisService.queryObjectData(PengPengConstants.INDEX_WEEKPROJECT_NEW_LIST);
		if (obj != null) {
			view.addObject("rdList", obj);
		} else {*/
			ApiProject ap2 = new ApiProject();
	         ap2.setOrderDirection("desc");
	         ap2.setIsHide(0);
	         ap2.setIsRecommend(1);
	         ApiPage<ApiProject> apiPage2 = projectFacade.queryProjectList(ap2, 1, 3);
			
			 ApiProject ap = new ApiProject();
			 List<Integer> states = new ArrayList();
             states.add(Integer.valueOf(240));
             states.add(Integer.valueOf(260));
             ap.setStates(states);
             ap.setOrderDirection("desc");
             ap.setOrderBy("registrTime");
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, 1, 7);
	         for (ApiProject p : apiPage.getResultData()) {
	        	 apiPage2.getResultData().add(p);
	        	 apiPage2.setPageSize(10);
	        	 apiPage2.setTotal(10);
			 }
	        view.addObject("rdList", apiPage2.getResultData());
			/*redisService.saveObjectData(PengPengConstants.INDEX_WEEKPROJECT_NEW_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}*/
	}
	
	public void newsList(ModelAndView view) {
		//最新新闻
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_NEWS_LIST);
		if (obj != null) {
			view.addObject("latestNews", obj);
		} else {
			ApiNews apiNews = new ApiNews();
			 apiNews.setNews_column("公告");
			 apiNews.setOrderBy("ordertime");
			 apiNews.setOrderDirection("desc");
			 ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, 1, 6);
			 view.addObject("latestNews",page.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_NEWS_LIST,
					page.getResultData(), DateUtil.DURATION_TEN_S);
		}
		//爱心故事
		Object obj2 = redisService.queryObjectData(PengPengConstants.INDEX_LOVE_LIST);
		if (obj2 != null) {
			view.addObject("loveNews", obj2);
		} else {
			ApiNews apiNews = new ApiNews();
			apiNews.setNews_column("爱心故事");
			apiNews.setOrderBy("ordertime");
			apiNews.setOrderDirection("desc");
			 ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, 1, 3);
			List<ApiNews> list = page.getResultData();
			for(ApiNews an:list){
				if(an.getCoverImageId() != null){
					ApiBFile apiBfileNews = commonFacade.queryBFileById(an.getCoverImageId());
					if(apiBfileNews != null){
						an.setCoverImageUrl(apiBfileNews.getUrl());
					}
				}
			}
			view.addObject("loveNews",list);
			redisService.saveObjectData(PengPengConstants.INDEX_LOVE_LIST,
					page.getResultData(), DateUtil.DURATION_TEN_S);
		}
	}
	
	//最新捐款列表
	public void donateRecord(ModelAndView view){
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_NEWS_DONATE_LIST);
		if (obj != null) {
			view.addObject("donateRecord", obj);
		} else {
			ApiDonateRecord r = new ApiDonateRecord();
			//r.setState(302);
			r.setQueryStartDate(DateUtil.dateStart(new Date()));
			ApiPage<ApiDonateRecord> donats = donateRecordFacade
					.queryLatestDonateRecordList(r, 1, 10);
			//logger.info("最新捐款：>>>>"+donats.getResultData().toString());
			//System.out.println("最新捐款：>>>>"+donats.getResultData().toString());
			List<ApiDonateRecord> apiDonateRecords = donats.getResultData();
			for (ApiDonateRecord adr : apiDonateRecords) {
				if (adr.getTouristMessage() != null) {
					JSONObject json = JSON.parseObject(adr.getTouristMessage());
					if (StringUtils.isNotEmpty(json.getString("name"))) {
						adr.setNickName(json.getString("name"));
					}
				}
				int d_value = DateUtil.minutesBetween(adr.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(adr.getDonatTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flag) {
					adr.setTimeStamp(DateUtil.dateFormat(adr.getDonatTime()));
				} else {
					if (d_value / 60 >= 1) {
						adr.setTimeStamp(d_value / 60 + "小时前");
					} else {
						if (d_value == 0) {
							adr.setTimeStamp("刚刚");
						} else {
							adr.setTimeStamp(d_value + "分钟前");
						}
					}
				}
			}
			view.addObject("donateRecord", donats.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_NEWS_DONATE_LIST,
					donats.getResultData(), DateUtil.DURATION_MIN_S);
		}
	}

	public void loveGroup(ModelAndView view) {
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_LOVEGROUP_LIST +"_P");
		Object obj2 = redisService.queryObjectData(PengPengConstants.INDEX_LOVEGROUP_LIST +"_E");
		if (obj != null && obj2 !=null) {
			view.addObject("ploveGroup", obj);
			view.addObject("eloveGroup", obj2);
		} else {
			// 善管家列表
			ApiFrontUser user = new ApiFrontUser();
			user.setUserType("individualUsers");// 个人
			user.setLoveGroupMent(1);
			user.setLoveState(203);
			ApiPage<ApiFrontUser> apfronUsers = userFacade.queryUserList(user,
					1, 4);
			List<ApiFrontUser> users = apfronUsers.getResultData();
			if (users != null && users.size() > 0) {
				for (ApiFrontUser fu : users) {
					if (fu.getProvince() != null && fu.getCity() != null) {
						if ((fu.getProvince() + fu.getCity()).length() > 8) {
							fu.setArea((fu.getProvince() + fu.getCity())
									.substring(0, 8) + "...");
						} else {
							fu.setArea(fu.getProvince() + fu.getCity() + "");
						}
					}
					if (fu.getCoverImageId() != null) {
						ApiBFile apiBfileNews = commonFacade.queryBFileById(fu
								.getCoverImageId());
						if (apiBfileNews != null) {
							fu.setCoverImageUrl(apiBfileNews.getUrl());
						}
					}
				}
			}
			view.addObject("ploveGroup", users);
			redisService.saveObjectData(PengPengConstants.INDEX_NEWS_DONATE_LIST +"_P",
					users, DateUtil.DURATION_TEN_S);
			user.setUserType("enterpriseUsers");// 企业
			apfronUsers = userFacade.queryUserList(user, 1, 4);
			users = apfronUsers.getResultData();
			if (users != null && users.size() > 0) {
				for (ApiFrontUser fu : users) {
					if (fu.getProvince() != null && fu.getCity() != null) {
						if ((fu.getProvince() + fu.getCity()).length() > 8) {
							fu.setArea((fu.getProvince() + fu.getCity())
									.substring(0, 8) + "...");
						} else {
							fu.setArea(fu.getProvince() + fu.getCity() + "");
						}
					}
					if (fu.getCoverImageId() != null) {
						ApiBFile apiBfileNews = commonFacade.queryBFileById(fu
								.getCoverImageId());
						if (apiBfileNews != null) {
							fu.setCoverImageUrl(apiBfileNews.getUrl());
						}
					}
				}
			}
			view.addObject("eloveGroup", users);
			redisService.saveObjectData(PengPengConstants.INDEX_NEWS_DONATE_LIST +"_E",
					users, DateUtil.DURATION_TEN_S);
		}
	}
	/**
	 * banner 图
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("bannerlist")
	public JSONObject bannerlist(
			@RequestParam(value = "type", required = false, defaultValue = "banner") String type) {

		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
	
		try {
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory(type);
			bfiles = CommonFacade.queryApiBfile(apiBfile);

			//无数据
			if (bfiles == null || bfiles.size() == 0) {
				data.put("result", 1);
			} else {
				 for (ApiBFile bfile : bfiles) {
				 JSONObject item = new JSONObject();
				 item.put("imageId", bfile.getId());
				 item.put("url", bfile.getUrl());
				 item.put("middleUrl", bfile.getMiddleUrl());
				 item.put("linkUrl", bfile.getLinkUrl());
				 items.add(item);
				}
				data.put("items", items);
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
	
	@RequestMapping("donationWhere")
	public ModelAndView donationWhere() {
		ModelAndView view = new ModelAndView("donation/gardenWhereListNew");
		return view;
	}
	
	@RequestMapping("donationsListNew")
	public ModelAndView donationsListNew() {
		ModelAndView view = new ModelAndView("donation/donationsListNew");
		return view;
	}
	@RequestMapping("indexService")
	public ModelAndView indexService(HttpServletRequest requrest,HttpServletResponse response)
	{
		ModelAndView  view = new ModelAndView("service/indexServicelist");
		return view ;
	}
	@RequestMapping("toAddService")
	public ModelAndView toAddService(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="type",required=false,defaultValue="2") Integer type,
			@RequestParam(value="projectId",required=false) Integer projectId)
	{
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView  view = null;
		String item = "" ; 
		if(type == 1)
		{
			item = "我要建议" ;
			view = new ModelAndView("service/addAdvice");
			view.addObject("item",item);
		}
		else if(type == 2)
		{
			item = "我要投诉" ; 
			view = new ModelAndView("service/addService");
			view.addObject("item",item);
		}
		else if(type == 0)
		{
			item = "我要咨询" ;
			view = new ModelAndView("service/addConsult");
			view.addObject("item",item);
		}
		view.addObject("type",type);
		view.addObject("userId", userId);
		view.addObject("projectId", projectId);
		return view ;
	}
	
	/**
	 * 数据可视化页面
	 * @return
	 */
	@RequestMapping("specialFunds_view")
	public ModelAndView specialFunds_view(){
		ModelAndView view = new ModelAndView("specialFunds/specialFunds");
		return view;
	}
	
	/**
	 * 数据可视化页面
	 * @return
	 */
	@RequestMapping("visualization_view")
	public ModelAndView visualization_view(){
		ModelAndView view = new ModelAndView("visualization");
		Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
		char[] ss= Itotal.toString().toCharArray();
		view.addObject("totalMoney", ss);
		return view;
	}
	
	/**
	 * 加载数据
	 * @return
	 */
	@RequestMapping("visualization_list")
	@ResponseBody
	public Map<String, Object> visualization_list(){
		/*List<Integer> states = new ArrayList<Integer>();
		states.add(240);
		states.add(260);
		ApiProject param = new ApiProject();
		param.setStates(states);*/
		List<ApiProject> list = projectFacade.queryProjectAndUserInfo(null);
		List<ApiDonateRecord> list2 = donateRecordFacade.queryDonateBy15();
		JSONArray objs = new JSONArray();
		JSONObject obj2 = new JSONObject();
		List<String> jinwei = new ArrayList<String>();
		for(ApiProject p : list){
			if(p.getLongitude()!=null&&p.getLatitude()!=null){
			JSONObject obj = new JSONObject();
			if( p.getRealName()!=null&& !"".equals(p.getRealName())){
			obj.put("name", p.getRealName());
			obj.put("value", 60);
			/*if(p.getDonatAmount()<1000){
				obj.put("value", 15);
			}
			else if(p.getDonatAmount()>=1000&&p.getDonatAmount()<3000){
				obj.put("value", 25);
			}
			else if(p.getDonatAmount()>=3000&&p.getDonatAmount()<5000){
				obj.put("value", 45);
			}
			else if(p.getDonatAmount()>=5000&&p.getDonatAmount()<7000){
				obj.put("value", 50);
			}
			else if(p.getDonatAmount()>=7000&&p.getDonatAmount()<10000){
				obj.put("value", 60);		
			}
			else if(p.getDonatAmount()>=10000&&p.getDonatAmount()<20000){
				obj.put("value", 70);
			}
			else if(p.getDonatAmount()>=2000&&p.getDonatAmount()<5000){
				obj.put("value", 90);
			}
			else{
				obj.put("value", 100);
			}*/
			objs.add(obj);
			jinwei = new ArrayList<String>();
			jinwei.add(p.getLongitude().toString());//经度
			jinwei.add(p.getLatitude().toString());//纬度
			jinwei.add(p.getDonatAmount().toString());//获捐金额
			jinwei.add(p.getDonationNum().toString());//获捐次数
			jinwei.add(p.getInformation());//
			obj2.put(p.getRealName(), jinwei);
			}
		}}
		if(list2!=null&&list2.size()>0){
			for(ApiDonateRecord d : list2){
				if(d.getLongitude()!=null&&d.getLatitude()!=null){
				JSONObject obj = new JSONObject();
				obj.put("name", d.getNickName());
				obj.put("value", "60");
				objs.add(obj);
				
				
				jinwei = new ArrayList<String>();
				jinwei.add(d.getLongitude().toString());
				jinwei.add(d.getLatitude().toString());
				jinwei.add(d.getDonatAmountpt().toString());
				jinwei.add("0");
				obj2.put(d.getNickName(), jinwei);
				//objs2.add(obj2);
			}}
		}
		//id,name,donateNum,money,long,la
		return webUtil.successResDoubleObject(objs,obj2);
	}
	
	/**
	 * 加载数据2
	 * @return
	 */
	@RequestMapping("visualization_userlist")
	@ResponseBody
	public Map<String, Object> visualization_showlist(){
		/*List<Integer> states = new ArrayList<Integer>();
		states.add(240);
		states.add(260);
		ApiProject param = new ApiProject();
		param.setStates(states);*/
		List<ApiProject> listpro = projectFacade.queryProjectAndUserInfo(null);
		List<ApiDonateRecord> list = donateRecordFacade.queryDonateBy15();
		JSONArray objs = new JSONArray();
		//JSONArray objs2 = new JSONArray();
		JSONObject obj2 = new JSONObject();
		List<String> jinwei = new ArrayList<String>();
		for(ApiProject p : listpro){
			if(p.getLongitude()!=null&&p.getLatitude()!=null){
			jinwei = new ArrayList<String>();
			jinwei.add(p.getLongitude().toString());
			jinwei.add(p.getLatitude().toString());
			jinwei.add(p.getDonatAmount().toString());
			jinwei.add(p.getDonationNum().toString());
			obj2.put(p.getTitle(), jinwei);
		}}
		if(list!=null&&list.size()>0){
			for(ApiDonateRecord d : list){
				if(d.getLongitude()!=null&&d.getLatitude()!=null){
				JSONObject obj = new JSONObject();
				obj.put("name", d.getNickName());
				obj.put("value", "60");
				objs.add(obj);
				
				
				jinwei = new ArrayList<String>();
				jinwei.add(d.getLongitude().toString());
				jinwei.add(d.getLatitude().toString());
				jinwei.add(d.getDonatAmountpt().toString());
				jinwei.add("0");
				obj2.put(d.getNickName(), jinwei);
				//objs2.add(obj2);
			}}
		}
		return webUtil.successResDoubleObject(objs,obj2);
	}
	
	/**
	 * 加载数据2
	 * @return
	 */
	@RequestMapping("visualization_feixianlist")
	@ResponseBody
	public Map<String, Object> visualization_feixianlist(){
		List<ApiDonateRecord> list = donateRecordFacade.queryDonateBy15();
		JSONArray objs = new JSONArray(); 
		if(list!=null&&list.size()>0){
			for(ApiDonateRecord d : list){
				if(d.getLongitude()!=null&&d.getLatitude()!=null){
					JSONObject obj = new JSONObject();
					JSONObject obj2 = new JSONObject();
					JSONArray obj3 = new JSONArray();
					obj.put("name", d.getNickName());
					obj2.put("name", d.getRealName());
					obj2.put("value", "30");
					obj3.add(obj);
					obj3.add(obj2);
					objs.add(obj3);
			}}
		}
		return webUtil.successRes(objs);
	}
	
	/**
	 * 多个子域名授权回调中心
	 * @param request
	 * @return
	 */
	@RequestMapping("change_weixin_redirect_uri")
	public ModelAndView change_weixin_redirect_uri(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		String queryString = request.getQueryString();
		//String weixin_code = request.getParameter("code");
		String state = request.getParameter("state");
		if("flt".equals(state)){//福路通授权回调
			logger.info("flt授权回调地址>>>> http://admin.flt.17xs.org/tenpay/h5/deposit.do?"+queryString);
			view = new ModelAndView("redirect:http://admin.flt.17xs.org/tenpay/h5/deposit.do?"+queryString);
		}
		else{
			view = new ModelAndView("redirect:http://www.17xs.org");
		}
		return view;
	}
}
