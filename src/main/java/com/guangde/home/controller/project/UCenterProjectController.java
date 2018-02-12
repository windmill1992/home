package com.guangde.home.controller.project;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IPayMoneyRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiAnnounce;
import com.guangde.entry.ApiAuditProject;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiCommonFormUser;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiDonateTime;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiGoodLibraryProple;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiOneAid;
import com.guangde.entry.ApiPayMoneyRecord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectArea;
import com.guangde.entry.ApiProjectCount;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiProjectVolunteer;
import com.guangde.entry.ApiReport;
import com.guangde.entry.ApiSpecialFund;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.ApiUserCard;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.project.PFeedBack;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("uCenterProject")
public class UCenterProjectController {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);
	private static final String phonecodeprex = "phonecode_r_";
	private static final String imgcodeprex = "imgcode_r_";
	@Autowired
    private ICommonFacade ICommonFacade;
	
	@Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired 
    
    private ICompanyFacade companyFacade;
    
    //对用户进行操作的方法调用
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
	private IProjectVolunteerFacade projectVolunteerFacede ;
	
	@Autowired
    private IGoodLibraryFacade goodLibraryFacade;
	
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	
	/**
	 * 加载更多是的一部请求项目的列表
	 * 
	 */
	@ResponseBody
	@RequestMapping("ajaxProjectList")
	public JSONObject ajaxProjectList(@RequestParam(value="state",required=true)String state,HttpServletRequest request,
			 HttpServletResponse response,@RequestParam(value="currentPage",required=true)String currentPage){
		Integer userId=UserUtil.getUserId(request, response);
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
		try {
			 ApiProject ap=new ApiProject();
			 ap.setOrderDirection("desc");
	         ap.setOrderBy("id");
	         ap.setIsHide(0);
	         ap.setUserId(userId);
	         ap.setClaimUserId(userId);
	         
	         if("210".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("230".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("240".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("260".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         int pageNum=Integer.parseInt(currentPage);
	         int pageSize=10;
	         ApiPage<ApiProject> apList=projectFacade.queryUCenterProjectlist(ap, pageNum, pageSize);
	         List<ApiProject> list = apList.getResultData();
	         if(list.size()==0){
	        	 //无数据
	        	 data.put("result", 1);
	         }
	         else {
	        	for(int j=0;j<list.size();j++){
	        		double donatePercent=(list.get(j).getDonatAmount()/list.get(j).getCryMoney())*100;
	        		DecimalFormat df = new DecimalFormat("0.0");
	        		 
	        		String db = df.format(donatePercent);
	        		list.get(j).setDonatePercent(db+"%");
	        	}
				for(ApiProject project:list){
					 JSONObject item = new JSONObject();
					 item.put("recommendedPerson", project.getRecommendedPerson());
					 item.put("coverImageUrl", project.getCoverImageUrl());
					 item.put("title", project.getTitle());
					 item.put("cryMoney", project.getCryMoney());
					 item.put("donatePercent", project.getDonatePercent());
					 item.put("lastUpdateTime", DateUtil.parseToFormatDateString(project.getLastUpdateTime(),"yyyy-MM-dd"));
					 item.put("id", project.getId());
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
	 * 根据不同的状态查询某个人的项目的列表
	 * @param  项目的状态  state
	 * @userId  用户的id
	 * @return   projectList
	 */
	@RequestMapping("uCenterProjectList")
	public ModelAndView uCenterProjectList(@RequestParam(value="state",required=true)String state,HttpServletRequest request,
			 HttpServletResponse response,String currentPage){
		ModelAndView mv=new ModelAndView("h5/userCenter/project_initiator");
		//判断是否是微信登录
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user1=null;
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){
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
	    					mv = new ModelAndView("redirect:" + url_weixin_code);
	    					return mv;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				token = mapToken.get("access_token").toString();
	    				request.getSession().setAttribute("openId", openId);
	    				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user1 = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("微信登录处理出现问题"+ e);
	    		}
	    		
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
	    		
	    		try
	    		{
	    			// 自动登录
	    			SSOUtil.login(user1, request, response);
	    			logger.info("user1>>>>>>>"+user1.toString());
	    		}
	    		catch(Exception e)
	    		{
	    			logger.error("",e);
	    		}
	    		
	    	}else{
	    		mv = new ModelAndView("h5/ucenter/userLogin");
				return mv;
	    	}
		}
		try {
			 ApiProject ap=new ApiProject();
			 ap.setOrderDirection("desc");
	         ap.setOrderBy("id");
	         if(userId==null){
	        	 userId=user1.getId();
	        	 if(state.contains("STATE"))
	        			 state=state.split(",")[0];
	         }
	         ap.setUserId(userId);
	         ap.setClaimUserId(userId);
	         
	         if("210".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("230".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("240".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         else if("260".equals(state)){
	        	 ap.setState(Integer.parseInt(state));
	         }
	         int pageNum=Integer.parseInt(currentPage);
	         int pageSize=10;
	         ApiPage<ApiProject> apList=projectFacade.queryUCenterProjectlist(ap, pageNum, pageSize);
	         List<ApiProject> list = apList.getResultData();
	         if(list.size()==0){
	        	 mv.addObject("tips",1);
	         }
	         else {
	        	for(int j=0;j<list.size();j++){
	        		double donatePercent=(list.get(j).getDonatAmount()/list.get(j).getCryMoney())*100;
	        		DecimalFormat df = new DecimalFormat("0.0");
	        		 
	        		String db = df.format(donatePercent);
	        		list.get(j).setDonatePercent(db+"%");
	        	}
				mv.addObject("list", list);
				mv.addObject("total", apList.getTotal());
			}
			 	
		} catch (Exception e) {
			
		}
		mv.addObject("state", state);
		return mv;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
     * 编辑项目
     * @return
     */
    @RequestMapping("editPublicRelease")
    public  ModelAndView editPublicRelease(@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="state",required=true)String state
    		){
    	ModelAndView view=new ModelAndView();
    	
    	ApiProject apiProject = projectFacade.queryProjectDetail(projectId);
    	//查询受助人的信息
    	ApiProjectUserInfo apiProjectUserInfo=new ApiProjectUserInfo();
    	apiProjectUserInfo.setProjectId(apiProject.getId());
    	apiProjectUserInfo.setPersonType(0);
    	ApiProjectUserInfo  projectUserInfo=projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    	view.addObject("projectUserInfo", projectUserInfo);
    	//查询受助领域
    	if(apiProject.getField()!=null){
    		ApiTypeConfig ag = new ApiTypeConfig();
    		ag.setTypeName_e(apiProject.getField());
            ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
            if(atc==null){
            	//为空时返回一个页面
            	view.setViewName("");
            	return  view;
            }else {
				view.addObject("atc",atc);
			}
    	}
    	Long date = ((long)(apiProject.getDeadline().getTime()-new Date().getTime()))/(1000*3600*24);
    	//System.out.println(date);
    	view.addObject("project", apiProject);
    	view.addObject("donateTimeNum",date);
    	if(projectUserInfo.getLinkMobile()==null && projectUserInfo.getLinkMan()==null){
    		view.setViewName("h5/userCenter/institutionEditPublicRelease");
    	}else{
    		view.setViewName("h5/userCenter/editPublicRelease");
    	}
    	view.addObject("state", state);
    	return view;
    }
    
    /**
     * 更新项目
     * @return
     */
    @RequestMapping("updatePublicRelease")
    @ResponseBody
    public Map<String, Object> updatePublicRelease (ApiProject model,ApiProjectUserInfo projectUserInfo, 
    		@RequestParam(value="donateTimeNum",required=true)Long donateTimeNum,
    		@RequestParam(value="imgIds",required=true)String imgIds,String projectUserInfoId){
    	ApiProject apiProject = new ApiProject();
    	if(model!=null&&model.getId()!=null){
    		apiProject = projectFacade.queryProjectDetail(model.getId());
    		if(apiProject!=null){
    			Long d1=((long)(apiProject.getDeadline().getTime()-new Date().getTime()))/(1000*3600*24);//数据库天数
    			if(d1!=donateTimeNum){//判断数据库里的天数是否与用户填的天相同
    				Long d2=(long) (new Date().getTime()+donateTimeNum*24*3600*1000);
    				apiProject.setDeadline(new Date(d2));
    			}
    			apiProject.setTitle(model.getTitle());
    			apiProject.setContent(model.getContent());
    			apiProject.setCryMoney(model.getCryMoney());
    			apiProject.setContentImageId(imgIds);
    			//将第一张默认为标题图
    			String ids[] =imgIds.split(",");
    	        Integer titleImageId=0;
    	        for(int i=0;i<ids.length;i++){
    	        	if(ids[i]!=null && !"".equals(ids[i])){
    	        		titleImageId=Integer.parseInt(ids[i]);
    	        		break;
    	        	}
    	        }
    	        apiProject.setCoverImageId(titleImageId);
    			apiProject.setState(model.getState());
    			ApiResult result = projectFacade.updateProject(apiProject);
    	    	if(result!=null&& result.getCode()==1){
    	    		//项目修改成功之后再修改受助人的信息
    	    		if(projectUserInfo!=null && projectUserInfoId!=null){
    	    			projectUserInfo.setId(Integer.parseInt(projectUserInfoId));
    	    			List<ApiProjectUserInfo> list=new ArrayList<ApiProjectUserInfo>();
    	    			list.add(projectUserInfo);
    	    		    ApiResult result2=projectFacade.updateProjectUserInfo(list);
    	    		    if(result2!=null&& result2.getCode()==1){
    	    		    	return webUtil.resMsg(1, "0000", "修改成功！", null);
    	    		    }else{
    	    		    	//受助人修改失败回滚
    	    		    	return webUtil.resMsg(0, "0010", "修改失败！", null);
    	    		    }
    	    			
    	    		}else{
    	    			return webUtil.resMsg(1, "0000", "修改成功！", null);
    	    		}
    	    	 }	
    	    	else {
    				return webUtil.resMsg(0, "0010", "修改失败！", null);
    			}
    		}
    		else{
    			return webUtil.resMsg(0, "0002", "参数错误", null);
    		}
    	}
    	else{
			return webUtil.resMsg(0, "0002", "参数错误", null);
		}
    	
    }
    
    /**
     * 跳到收款账号
     * @return
     */
    @RequestMapping("showAccountNumber")
    public ModelAndView showAccountNumber(@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request, HttpServletResponse response){
    	//1.判断用户是否登录
    	//2.判断用户是否填过收款账号
    	ModelAndView view = new ModelAndView();
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
        	view.setViewName("/h5/ucenter/userLogin");
            return view;
        }
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        if(project!=null){
        	ApiUserCard paramcCard = new ApiUserCard();
        	paramcCard.setUserId(project.getUserId());
        	paramcCard.setProjectId(projectId);
            ApiPage<ApiUserCard> userCards= userFacade.queryUserCardList(paramcCard, 0, 20);
            if(userCards.getTotal()>0){
            	ApiProjectUserInfo param = new ApiProjectUserInfo();
        		param.setProjectId(projectId);
        		param.setPersonType(2);
        		ApiProjectUserInfo info=projectFacade.queryProjectUserInfo(param);
        		if(info!=null&&info.getLinkMobile()!=null){
        			view.addObject("mobile",info.getLinkMobile());
        		}
            	double money = MathUtil.sub(project.getDonatAmount(),project.getPanyAmount());//剩余未打款金额
        		view.addObject("money", money);
        		view.addObject("projectId", project.getId());
            	view.addObject("userCards", userCards.getResultData().get(0));
        		view.setViewName("h5/userCenter/accountNumber");
        		return view;
            }
        	else{//未填过收款信息或不全
        		view.setViewName("h5/userCenter/accountNumber");
        		ApiProjectUserInfo param = new ApiProjectUserInfo();
        		param.setProjectId(projectId);
        		param.setPersonType(2);
        		ApiProjectUserInfo info=projectFacade.queryProjectUserInfo(param);
        		if(info!=null&&info.getLinkMobile()!=null){
        			view.addObject("mobile",info.getLinkMobile());
        			view.addObject("projectId", info.getProjectId());
        		}
        		return view;
        	}
        }
    	return view;
    }
    
    /**
     * 提交收款账号
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("addAccountNum")
    public Map<String, Object> addAccountNum(ApiUserCard model,@RequestParam(value="id",required=true)Integer projectId,
    		@RequestParam(value="phoneCode",required=true)String phoneCode,
    		@RequestParam(value="code",required=true)String code,
    		@RequestParam(value="mobile",required=true)String mobile,HttpServletRequest request, HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null || userId==0){//未登录
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		String codekey = getImgCode(request);
		
		codeR = CodeUtil.VerifiCode(codekey,storeManage,code,true);
		if (codeR == -1) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码过期", null);
		} else if (codeR == 0) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码错误", null);
		}
		// 1.校验参数
		if (StringUtil.isMobile(mobile.toString())) {
			// 校验手机验证码
			phonekey = CodeUtil.certificationprex + mobile;

			 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					 phoneCode, true);
			 if(codeR==-1){
			 return webUtil.resMsg(0, "0005","手机验证码过期", null);
			 }else if(codeR==0){
			 return webUtil.resMsg(0, "0005","手机验证码错误", null);
			 }
		} else {
			// 手机格式错误
			msg = "0001";
			return webUtil.resMsg(0, msg, "手机格式错误", null);
		}
		int flag = 0;
		String errCode="0030";
		String errMsg="提交失败！";
		model.setUserId(userId);
		model.setIsSelected(0);//默认
		model.setUseState(100);//启用
		model.setBindingTime(new Date());
		ApiUserCard paramcCard = new ApiUserCard();
    	paramcCard.setUserId(userId);
    	paramcCard.setProjectId(projectId);
		 ApiPage<ApiUserCard> userCards= userFacade.queryUserCardList(paramcCard, 0, 20);
		 ApiResult result =new ApiResult();
         if(userCards.getTotal()>0){//存在银行卡
        	 model.setId(userCards.getResultData().get(0).getId());
        	 result = userFacade.updateUserCard(model);
         }
         else{
        	 model.setId(null);
        	 model.setProvince("未知");
        	 model.setCity("未知");
        	 model.setArea("未知");
        	 model.setBankType("8");
        	 model.setProjectId(projectId);
        	 result = userFacade.saveNewUserCard(model);
         }
		if(result.getCode()==1){
			flag=1;
			errCode="0000";
			errMsg="提交成功";
		}
    	return webUtil.resMsg(flag, errCode, errMsg, null);
    }
    
    /**
     * 打款金额
     * @return
     */
    @ResponseBody
    @RequestMapping("accountMoney")
    public Map<String, Object> accountMoney(@RequestParam(value="id",required=false)Integer projectId,
    		@RequestParam(value="panMoney",required=true)Double panMoney,
    		@RequestParam(value="imagId",required=false)String imagId,
    		HttpServletRequest request, HttpServletResponse response){
    	// 没有登录
    	Integer userId = UserUtil.getUserId(request, response);
    	if (userId == null) {
    		return webUtil.loginFailedRes(null);
    	}
    	if(projectId != null ){
			ApiProject apiProject = projectFacade.queryProjectDetail(projectId);
			if(apiProject == null){
				return webUtil.failedRes("0011", "没有这个项目", null);
			}
			/*if(!userId.equals(apiProject.getUserId())){
				return webUtil.failedRes("0010", "不是本人发起的项目", null);
			}*/
		}else {
			return webUtil.failedRes("0009", "参数错误", null);
		}
    	// 查看有没这张卡数据
    	ApiPayMoneyRecord apiPayMoneyRecord = new ApiPayMoneyRecord();
		apiPayMoneyRecord.setApplyMoney(panMoney);
    	ApiUserCard apiUserCard = new ApiUserCard();
    	apiUserCard.setUserId(userId);
    	apiUserCard.setProjectId(projectId);
    	ApiPage<ApiUserCard> apiPage = userFacade.queryUserCardList(
    			apiUserCard,0,20);
    	List<ApiUserCard> list = apiPage.getResultData();
    	if (list.size() > 0) {
    		ApiUserCard apcd = list.get(0);
    		apiPayMoneyRecord.setProjectId(projectId);
    		apiPayMoneyRecord.setTranNum(StringUtil.uniqueCode());
    		apiPayMoneyRecord.setRecipientName(apcd.getBankName());
    		apiPayMoneyRecord.setAccount(apcd.getCard());
    		apiPayMoneyRecord.setReceiptImageId(imagId);//发票图片id
    		apiPayMoneyRecord.setUserId(userId);
    		apiPayMoneyRecord.setRecipientBankType(apcd.getBankType());
    		apiPayMoneyRecord.setSource("H5");
    		apiPayMoneyRecord.setAccountName(apcd.getAccountName());
    	} else {
    		return webUtil.failedRes("10001", "没有这张卡", null);
    	}
    	ApiResult res = payMoneyRecordFacade.drawMoney(apiPayMoneyRecord);
		if (res != null && res.getCode() == 1) {
			//短信通知
			ApiProjectUserInfo info = new ApiProjectUserInfo();
			info.setProjectId(projectId);
			info.setPersonType(0);
			info = projectFacade.queryProjectUserInfo(info);
			
			if(info!=null&&info.getLinkMobile()!=null&&!"".equals(info.getLinkMobile())){//受助人
				ApiAnnounce announce = new ApiAnnounce();
				announce.setCause("善款申请提醒");
				announce.setType(1);
				announce.setPriority(1);
				announce.setState(100);
				announce.setTryCount(0);
				announce.setCreateTime(new Date());
				announce.setContent(info.getRealName()+"，您好，您的筹款项目已发起一笔提款申请，提款金额："+panMoney+"元，收款人："+list.get(0).getAccountName()+"，如有疑问请联系我们，电话0571-87165191，QQ：2777819027");
				announce.setDestination(info.getLinkMobile().trim());
				commonFacade.sendSms(announce, false);
			}
			return webUtil.successRes(null);
		}else if (res.getCode() == 90014) {
			return webUtil.failedRes("90014", res.getMessage(), null);
		}else if (res.getCode() == 90004) {
			return webUtil.failedRes("90004", res.getMessage(), null);
		} else {
			return webUtil.failedRes("0009", res.getMessage(), null);
		}
    }
    
    /**
     * 项目提前结束
     * @return
     */
    @ResponseBody
    @RequestMapping("beforeEndProject")
    public Map<String, Object> beforeEndProject(ApiProject apiProject,HttpServletRequest request, HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	if (userId == null) {
    		return webUtil.loginFailedRes(null);
    	}
    	if(apiProject!=null&&apiProject.getId()!=null&&apiProject.getDeadExplain()!=null){
    		ApiProject p = projectFacade.queryProjectDetail(apiProject.getId());
    		if(p != null && p.getState() == 240){
    			apiProject.setState(260);
    			apiProject.setDeadline(new Date());
    			ApiResult result = projectFacade.updateProject(apiProject);
    			return webUtil.resMsg(result.getCode(), "", result.getMessage(), null);
    		}else{
    			return webUtil.resMsg(0, "0002", "不是筹款中的项目！", null);
    		}
			
    	}
    	return webUtil.resMsg(0, "0002", "参数有误！", null);
    }
    
    /**
     * goto回复列表页面
     * @param view
     * @return
     */
    @RequestMapping("gotoCommentBatch")
    public ModelAndView gotoCommentBatch(@RequestParam(value="projectId",required=true)String projectId){
    	ModelAndView view = new ModelAndView();
    	view.addObject("projectId", projectId);
    	view.setViewName("h5/userCenter/commentBatch");
    	return view;
    }
    
    /**
     * 返回到项目反馈页面
     * @param view
     * @return
     */
    @RequestMapping("projectFeedBack")
    public ModelAndView projectFeedBack(@RequestParam(value="projectId",required=true)String projectId,
    		@RequestParam(value="state",required=true)String state
    		){
    	ModelAndView view = new ModelAndView();
    	view.addObject("projectId", projectId);
    	System.out.println(state);
    	view.addObject("state", state);
    	view.setViewName("h5/userCenter/projectFeedback");
    	return view;
    }
    /**
     * 跳转到项目详情页
     */
    @RequestMapping("projectDetails")
    @ResponseBody
    public Map<String, Object> projectDetails(@RequestParam(value="projectId",required=true)String projectId,
    		@RequestParam(value="state",required=false)String state
    		){
    	ModelAndView view = new ModelAndView();
    	view.addObject("projectId", projectId);
    	//判断该项目是否存在
    	ApiProject project=projectFacade.queryProjectDetail(Integer.parseInt(projectId));
    	if(project==null){
    		return webUtil.resMsg(0, "", "该项目存在", null); 
    	}else{
    		return webUtil.resMsg(1, "", "", project);
    	}
    	
    }
    /**
     * 进入项目详情页面
     * @param projectId
     * @return
     */
    @RequestMapping("project_details")
    public ModelAndView project_details(@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
    		){
    	ModelAndView mv=new ModelAndView("redirect:/uCenterProject/view.do?projectId=" + projectId);
    	String isMobile = (String)request.getSession().getAttribute("ua");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
            mv.setViewName("redirect:/uCenterProject/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return mv;
        }
        if(!"mobile".equals(isMobile)){
        	return mv;
        }
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
    		}
    	}}
    	
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	// 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
    	String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
    	report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
    	ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
    					30);
    	//项目配捐
    	ApiMatchDonate apiMatchDonate = new ApiMatchDonate();
    	if(project.getId()!=null && project.getId()!=0){
    		apiMatchDonate.setProjectId(project.getId());
            ApiPage<ApiMatchDonate> apiPage = redPacketsFacade.queryByParam(apiMatchDonate, 1, 5);
            List<ApiMatchDonate> matchDonates = apiPage.getResultData();
            if(matchDonates != null && matchDonates.size() >0){
            	mv.addObject("matchDonate", matchDonates.get(0));
            }
    	}
		
    	double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			//去掉html标签,转发时控制转发的内容的字数
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
		if (reports != null && reports.getTotal() > 0) {
			mv.addObject("reports", reports.getResultData());
		}
		//判断是否加入善库
		ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade.queryByParam(apigp,1,50);
		if(goodLibrary != null && goodLibrary.getResultData().size() > 0){
			mv.addObject("goodLibrary", 1);
		}else {
			mv.addObject("goodLibrary", 0);
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
		mv.addObject("donates", donats.getResultData());
		mv.addObject("peopleNum", project.getDonationNum());
		
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
		ApiProjectVolunteer apiProjectVolunteer = new ApiProjectVolunteer();
		apiProjectVolunteer.setProjectId(projectId);
		int number = projectVolunteerFacade.count(apiProjectVolunteer);
		
		 
		ApiPage<ApiProjectVolunteer> pvs = projectVolunteerFacade.queryVolunteerList(apiProjectVolunteer,1, 5);
		List<ApiProjectVolunteer> alist = pvs.getResultData();
		
		mv.addObject("alist",alist);
		mv.addObject("number",number);
		mv.addObject("projectId",projectId);
		mv.addObject("extensionPeople",extensionPeople);
		mv.addObject("browser",browser);
		mv.addObject("userId", userId);
		
		//获取帮助受助人证实的信息
		ApiAuditProject auditProject=new  ApiAuditProject();
		auditProject.setProjectId(projectId);
		ApiResult res=userFacade.selectAuditProjectCountByParam(auditProject);
		if(res.getCode()==1){
			mv.addObject("auditProjectCount", res.getMessage());
		}
		//获取证实人列表
		ApiAuditProject apiAuditProject=new ApiAuditProject();
		apiAuditProject.setProjectId(projectId);
		ApiPage<ApiAuditProject> pageList=userFacade.queryApiAuditProjectList(apiAuditProject, 1, 10);
		List<ApiAuditProject> list1=pageList.getResultData();
		if(list1!=null && list1.size()>0){
			String pageContextUrl="http://res.17xs.org/picture/";
			for(ApiAuditProject ap:list1){
				if(ap!=null){
					if(ap.getHeadUrl()==null)
						ap.setHeadUrl("http://www.17xs.org/res/images/user/4.jpg");
					else if(!ap.getHeadUrl().contains("wx.qlogo.cn"))
						ap.setHeadUrl(pageContextUrl+ap.getHeadUrl());
					System.out.println(ap.getHeadUrl());
				}
			}
			mv.addObject("auditProjectList", list1);
		}else{
			mv.addObject("auditProjectTips", 0);
		}
		
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
	    //
		/*点击量统计end*/
    	mv.addObject("project", project);
    	mv.setViewName("h5/userCenter/project_detail");
    	return mv;
    }
    
    /*
     * 查看受助人或发布人的详情页面
     */
    @RequestMapping("releaseAndAppealDetails")
    public ModelAndView releaseAndAppealDetails(@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="type",required=false)Integer type,
    		@RequestParam(value="personType",required=true)Integer personType){
    	 ModelAndView mv=new ModelAndView();
    	 if(personType==0){
    		 ApiProjectUserInfo projectUserInfo=new ApiProjectUserInfo();
        	 projectUserInfo.setProjectId(projectId);
        	 projectUserInfo.setPersonType(personType);
        	 List<ApiProjectUserInfo> list=projectFacade.queryProjectUserInfoList(projectUserInfo);
        	 ApiProject project=projectFacade.queryProjectDetail(projectId);
        	 if(list.get(0)!=null&&list.get(0).getIndetity()!=null){
        		 list.get(0).setIndetity(StringUtil.hideNumberLatestSix(list.get(0).getIndetity()));
        	 }
        	 mv.addObject("userInfo", list.get(0));
        	 mv.addObject("project", project);
        	 mv.setViewName("h5/userCenter/appealPerson");
    	 }
    	 else{
    		 if(type==0){//个人
    			 ApiProjectUserInfo projectUserInfo=new ApiProjectUserInfo();
    			 projectUserInfo.setProjectId(projectId);
    			 projectUserInfo.setPersonType(personType);
    			 List<ApiProjectUserInfo> list=projectFacade.queryProjectUserInfoList(projectUserInfo);
    			 if(list.get(0)!=null&&list.get(0).getIndetity()!=null){
            		 list.get(0).setIndetity(StringUtil.hideNumberLatestSix(list.get(0).getIndetity()));
            	 }
    			 ApiProject project=projectFacade.queryProjectDetail(projectId);
    			 ApiFrontUser user = userFacade.queryById(project.getUserId());
    			 mv.addObject("userInfo", list.get(0));
    			 mv.addObject("project", project);
    			 mv.addObject("user", user);
    			 mv.setViewName("h5/userCenter/releasePerson");
    		 }
    		 else{//机构
    			 ApiProject project = projectFacade.queryProjectDetail(projectId);
    			 ApiCompany company= new ApiCompany();
    			 company.setUserId(project.getUserId());
    			 company = companyFacade.queryCompanyByParam(company);
    			 mv.addObject("company", company);
    			 mv.addObject("project", project);
    			 mv.setViewName("h5/userCenter/institution");
    		 }
    	 }
    	return mv;
    }
    
    /**
     * goto项目数据
     * @param view
     * @return
     */
    @RequestMapping("gotoInitatorDetal")
    public ModelAndView gotoInitatorDetal(HttpServletRequest request,HttpServletResponse response
    		,@RequestParam(value="projectId",required=true)Integer projectId){
    	ModelAndView view = new ModelAndView();
    	Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
        	view.setViewName("h5/ucenter/userLogin");
       	 	return view;
        }
        ApiProject model = new ApiProject();
        model.setUserId(userId);
        model.setOrderBy("id");
        model.setOrderDirection("desc");
        ApiPage<ApiProject> listApiPage = projectFacade.queryProjectList(model, 1, 10);
    	if(listApiPage.getTotal()>0&&listApiPage.getResultData()!=null)
    		view.addObject("list", listApiPage.getResultData());
        ApiProject project = projectFacade.queryProjectDetail(projectId);
    	if(project!=null)
    		view.addObject("project", project);
    	ApiDonateRecord donate = new ApiDonateRecord();
    	donate.setState(302);
    	donate.setProjectId(projectId);
    	donate.setLeaveWord("0");//此时的leaveword为一个判断参数0：今天的捐款笔数和金额；1：昨天的捐款笔数和金额
    	ApiDonateRecord donate1 = donateRecordFacade.queryCompanyCenter(donate);
    	if(donate1!=null)
    		view.addObject("donate1", donate1);
    	donate.setLeaveWord("1");
    	ApiDonateRecord donate2 = donateRecordFacade.queryCompanyCenter(donate);
    	if(donate2!=null)
    		view.addObject("donate2", donate2);
    	donate.setLeaveWord(null);
    	ApiDonateRecord donate3 = donateRecordFacade.queryCompanyCenter(donate);
    	if(donate2!=null)
    		view.addObject("donate3", donate3);
    	
    	int countUserNum = donateRecordFacade. countDistDonateUserNum(projectId);
    	view.addObject("countUserNum", countUserNum);
    	//项目访问量
    	ApiProjectCount apiProjectCount = new ApiProjectCount();
    	apiProjectCount.setProjectId(projectId);
    	Integer countProjectHit = projectFacade.countProjectHit(apiProjectCount);
    	if(countProjectHit==null)
    		countProjectHit=0;
    	Object objh5 = redisService.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_H5+projectId);
    	Object objpc = redisService.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_PC+projectId);
    	int num = 0;
    	if(objh5!=null&&objh5.toString().split("_").length==4){
    		String[] numh5= objh5.toString().split("_");
    		num=Integer.parseInt(numh5[3]);
    	}
    	if(objpc!=null&&objpc.toString().split("_").length==4){
    		String[] numpc= objpc.toString().split("_");
    		num=num+Integer.parseInt(numpc[3]);
    	}
    	//证实人数
    	ApiAuditProject auditProject = new ApiAuditProject();
    	auditProject.setProjectId(projectId);
    	ApiResult res=userFacade.selectAuditProjectCountByParam(auditProject);
		if(res.getCode()==1){
			view.addObject("auditProjectCount", res.getMessage());
		}
		//项目转发量
		String clickRate = (String) redisService.queryObjectData(PengPengConstants.Project_ShareClick+projectId);
    	if(StringUtils.isEmpty(clickRate)){
    		view.addObject("shareNum", 0);
    	}
    	else{
    		String cstr[] = clickRate.split("_");
    		if(null != cstr && cstr.length >= 4){
    			view.addObject("shareNum", cstr[3]);
    		}
    	}
    	//项目评论量
    	ApiNewLeaveWord newLeaveWord = new ApiNewLeaveWord();
    	newLeaveWord.setProjectId(projectId);
    	ApiPage<ApiNewLeaveWord> apList=projectFacade.queryApiNewLeaveWord(newLeaveWord, 1, 1);
    	long totalLeaveNum=0;
    	if(apList!=null)
    		totalLeaveNum=apList.getTotal();//projectFacade.countLeaveWordNum(newLeaveWord);//收到的评论量
    	//已回复
    	ApiNewLeaveWord newLeaveWord1= new ApiNewLeaveWord();
    	newLeaveWord1.setProjectId(projectId);
    	newLeaveWord1.setIsRead(1);
    	newLeaveWord1.setLeavewordUserId(userId);
		apList=projectFacade.queryApiNewLeaveWord(newLeaveWord1, 1, 1);
    	
		long leavedNum=0;
		if(apList!=null)	
			leavedNum=apList.getTotal(); //projectFacade.countLeaveWordNum(newLeaveWord1);//已回复的评论量
    	//未回复
    	ApiNewLeaveWord newLeaveWord2= new ApiNewLeaveWord();
    	newLeaveWord2.setProjectId(projectId);
    	newLeaveWord2.setLeavewordUserId(userId);
		apList=projectFacade.queryNoReplyByParam(newLeaveWord2, 1 , 1);
		long leaveNum=0;
		if(apList!=null)
			leaveNum=apList.getTotal();
    	
    	view.addObject("totalLeaveNum", totalLeaveNum);
    	view.addObject("leavedNum", leavedNum);
    	view.addObject("leaveNum", leaveNum);
    	view.addObject("clickNum", num+countProjectHit);
    	view.addObject("projectId", projectId);
    	view.setViewName("h5/userCenter/initatorDetal");
    	return view;
    }
    
    /**
     * goto打款金额页面
     * @param projectId
     * @param money
     * @return
     */
    @RequestMapping("gotoAccountMoney")
    public ModelAndView gotoAccountMoney(@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="money",required=true)String money){
    	ModelAndView view = new ModelAndView("h5/userCenter/accountMoney");
    	view.addObject("money", money);
		view.addObject("projectId", projectId);
		return view;
    }
    

    /**
     * 判断用户是否证实过此项目
     * @param auditProject
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("isOrNotAuditProject")
    @ResponseBody
    public Map<String, Object> isOrNotAuditProject(@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="extensionPeople",required=false)String extensionPeople,
    		@RequestParam(value="type",required=true)Integer type,
    		HttpServletRequest request,HttpServletResponse response){
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		String queryString = request.getQueryString();
		String perfecturl = "http://www.17xs.org/uCenterProject/gotoAuditProject.do?" + queryString;//url + "?" + queryString;
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
        		String _extensionPeople_=extensionPeople==null?"":extensionPeople;
        		String backUrlString="http://www.17xs.org/ucenter/user/Login_H5.do?flag=audit_projectId_"+projectId+"_type_"+type+"_extensionPeople_"+_extensionPeople_;
        		return webUtil.resMsg(-1, "0001", backUrlString, null);
        	}
        	
        }
        if(userId==null)
        	userId=user1.getId();
        	ApiAuditProject param = new ApiAuditProject();
    	 	param.setUserId(userId);
    	 	param.setProjectId(projectId);
    	 	ApiAuditProject auditProject = userFacade.selectByParam(param);
    	 	if(auditProject!=null){
    	 		return webUtil.resMsg(1, "0010", "您已经证实过了，不需要再次证实！", null);
    	 	}else{
    	 		return webUtil.resMsg(0, "", "http://www.17xs.org/uCenterProject/gotoAuditProject.do?type="+type+"&projectId="+projectId, null);//没有证实过   
    	 	}
    }

    
    /**
     * goto到证实页面
     * @param auditProject
     * @param projectId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("gotoAuditProject")
    public ModelAndView gotoAuditProject(ApiAuditProject auditProject,
		@RequestParam(value="projectId",required=true)Integer projectId,
		@RequestParam(value="type",required=true)Integer type,
		HttpServletRequest request,HttpServletResponse response){
	ModelAndView view = new ModelAndView();
	//===========微信用户自动登陆start==============//
			String openId ="";
			String token = "";
			String unionid = "";
			String queryString = request.getQueryString();
			String perfecturl = "http://www.17xs.org/uCenterProject/gotoAuditProject.do?" + queryString;//url + "?" + queryString;
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
	        					view.setViewName("redirect:"+url_weixin_code);
	        					return view;
	        				}
	        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	        				openId = mapToken.get("openid").toString();
	        				unionid = mapToken.get("unionid").toString();
	        				token = mapToken.get("access_token").toString();
	        				request.getSession().setAttribute("openId", openId);
	        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	        				user1 = CommonUtils.queryUser(request,openId,token,unionid);
	        				userId=user1.getId();
	        				try
	                		{
	                			// 自动登录
	        					SSOUtil.login(user1, request, response);
	                		}
	                		catch(Exception e)
	                		{
	                			logger.error("",e);
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
	        		}
	        	}
	        	else{//返回到账号密码页面
	        		String backUrlString="/ucenter/user/Login_H5.do?flag=audit_projectId_"+projectId+"type_"+type+"_extensionPeople_";
	        		view.setViewName("redirect:"+backUrlString);
	        		return view;
	        	}
	        	
	        }
    
    if (userId == null)//用户未登录
    {
    	view.setViewName("/h5/ucenter/userLogin");
    	return view;
    }

    ApiFrontUser user = userFacade.queryById(userId);
    //隐藏后六位
    //user.setIdCard(StringUtil.hideNumberLatestSix(user.getIdCard()));
    if(user!=null)
    	view.addObject("user", user);
    view.setViewName("/h5/userCenter/auditProject");
    view.addObject("projectId", projectId);
    view.addObject("type", type);
	return view;
    }
    


    /**
     * 
     * @param model
     * @param projectId 项目id
     * @param mobile 手机号
     * @param code 图片验证码
     * @param phoneCode 手机验证码
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("addAuditProject")
    public Map<String, Object> addAuditProject(ApiAuditProject model,
    		@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="mobileNum",required=true)String mobile,
    		@RequestParam(value="phoneCode",required=true)String phoneCode,
    		@RequestParam(value="code",required=true)String code,
    		HttpServletRequest request,HttpServletResponse response){
    	 Integer userId = UserUtil.getUserId(request, response);
         if (userId == null)
         {
        	 return webUtil.loginFailedRes(null);
         }
        String msg = "0000";
 		String phonekey = null;
 		int codeR;
 		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
 		String codekey = getImgCode(request);
		
		codeR = CodeUtil.VerifiCode(codekey,storeManage,code,true);
		if (codeR == -1) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码过期", null);
		} else if (codeR == 0) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码错误", null);
		}
 		
 		// 1.校验参数
		if (StringUtil.isMobile(mobile.toString())) {
			// 校验手机验证码
			phonekey = CodeUtil.certificationprex + mobile;

			 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					 phoneCode, true);
			 if(codeR==-1){
			 return webUtil.resMsg(0, "0005","手机验证码过期", null);
			 }else if(codeR==0){
			 return webUtil.resMsg(0, "0005","手机验证码错误", null);
			 }
		} else {
			// 手机格式错误
			msg = "0001";
			return webUtil.resMsg(0, msg, "手机格式错误", null);
		}
		//判断是否证实过
 		ApiAuditProject param = new ApiAuditProject();
 		param.setUserId(userId);
 		param.setProjectId(projectId);
 		ApiAuditProject project = userFacade.selectByParam(param);
    	if(project!=null)//已经证实过
    		return webUtil.resMsg(0, "0003", "您已经证实过了，不需要再次证实！", null);
    	//没证实过
    	model.setUserId(userId);
    	ApiResult result = userFacade.saveAuditProject(model);
    	if(result.getCode()==1)
    		return webUtil.resMsg(1, "0000", "证实成功！", null);
    	else 
    		return webUtil.resMsg(0, "0010", "证实失败", null);
    }
    
    //查看证实人信息
    @RequestMapping("lookConfirmPeople")
    public ModelAndView  lookConfirmPeople(@RequestParam(value="type",required=true)Integer type,@RequestParam(value="projectId",required=true)Integer projectId){
    	ModelAndView mv=new ModelAndView();
    	ApiAuditProject apiAuditProject=new ApiAuditProject();
		apiAuditProject.setProjectId(projectId);
		ApiPage<ApiAuditProject> pageList=userFacade.queryApiAuditProjectList(apiAuditProject, 1, 10);
		ApiResult res=userFacade.selectAuditProjectCountByParam(apiAuditProject);
		if(res.getCode()==1){
			mv.addObject("auditProjectCount", res.getMessage());
		}
		List<ApiAuditProject> list=pageList.getResultData();
		if(list!=null && list.size()>0){
			String pageContextUrl="http://res.17xs.org/picture/";
			for(ApiAuditProject ap:list){
				if(ap!=null){
					if(ap.getHeadUrl()==null)
						ap.setHeadUrl("http://www.17xs.org/res/images/user/4.jpg");
					else if(!ap.getHeadUrl().contains("wx.qlogo.cn"))
						ap.setHeadUrl(pageContextUrl+ap.getHeadUrl());
					System.out.println(ap.getHeadUrl());
				}
			}
			mv.addObject("auditProjectList", list);
			mv.addObject("total",pageList.getTotal());
			mv.addObject("auditProjectTips", 0);
		}else{
			mv.addObject("auditProjectTips", 1);
		}
		mv.addObject("type", type);
		mv.addObject("projectId", projectId);
		mv.setViewName("h5/userCenter/authenticate_list");
    	return mv;
    }
    
    //证实人加载更多
    @RequestMapping("lookMoreConfirmPeople")
    @ResponseBody
    public JSONObject lookMoreConfirmPeople(@RequestParam(value="projectId",required=true)Integer projectId, Integer currentPage){
    	JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        ApiAuditProject apiAuditProject=new ApiAuditProject();
		apiAuditProject.setProjectId(projectId);
		ApiPage<ApiAuditProject> pageList=userFacade.queryApiAuditProjectList(apiAuditProject, currentPage, 10);
		List<ApiAuditProject> list=pageList.getResultData();
		String pageContextUrl="http://res.17xs.org/picture/";
		if(list!=null && list.size()>0){
			for(ApiAuditProject ap:list){
				if(ap!=null){
					if(ap.getHeadUrl()==null)
						ap.setHeadUrl("http://www.17xs.org/res/images/user/4.jpg");
					else if(!ap.getHeadUrl().contains("wx.qlogo.cn"))
						ap.setHeadUrl(pageContextUrl+ap.getHeadUrl());
					System.out.println(ap.getHeadUrl());
				}
			}
			for(ApiAuditProject apiAuditProject2:list){
				JSONObject item = new JSONObject();
				item.put("headUrl",apiAuditProject2.getHeadUrl());
				item.put("realName",apiAuditProject2.getRealName());
				item.put("relationship",apiAuditProject2.getRelationship());
				item.put("information",apiAuditProject2.getInformation());	
				items.add(item);
			}
			data.put("result",0);
			data.put("total",pageList.getTotal());
			data.put("items", items);
		}else{
			data.put("result", 1);
		}
		return data;
    }
    

    /*
     * 网页端访问详情页
     * @param projectId 项目ID 显示项目详情
     */
    @RequestMapping("view")
    public ModelAndView view(@RequestParam(value = "projectId") Integer projectId,
    		@RequestParam(value = "itemType", required = false, defaultValue = "") Integer itemType,
    		@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
    		HttpServletRequest request, HttpServletResponse response)
    {
    	ModelAndView viewback = new ModelAndView("redirect:/uCenterProject/project_details.do?projectId=" + projectId);
        String isMobile = (String)request.getSession().getAttribute("ua");
        if ("mobile".equals(isMobile) && extensionPeople!=null)
        {
            viewback.setViewName("redirect:/uCenterProject/project_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if("mobile".equals(isMobile)){
        	return viewback;
        } 
        // 项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        
        // 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
        ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1, 30);
        ModelAndView view = new ModelAndView("project/project_detail");
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
        	view.addObject("desc", StringUtil.delHTMLTag(project.getContent()));
            process = 0.0;
            if (project.getCryMoney() >= 0.001)
            {
                process = project.getDonatAmount() / project.getCryMoney();
            }
            view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 2));
            //为百分比显示
            view.addObject("processbfb", StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0));
            if (userId != null && project.getUserId().equals(userId))
            {
                // 是否是项目发起人
                view.addObject("owner", true);
            }
            ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
            userInfo.setProjectId(projectId);
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
        //推广员id
        view.addObject("extensionPeople",extensionPeople);
        
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
	 * 跳转到以前项目的机构详情页面
	 * @param view
	 * @return
	 */
	@RequestMapping(value="gotoinstitutioned")
	public ModelAndView gotoinstitutioned(ModelAndView view,@RequestParam(value="projectId",required=true)Integer projectId){
		view.setViewName("h5/userCenter/institutioned"); 
		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
         userInfo.setProjectId(projectId);
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
         ApiProject project = new ApiProject();
         ApiFrontUser user=new ApiFrontUser();
         project = projectFacade.queryProjectDetail(projectId);
         if(project!=null&&project.getUserId()!=null){
             user = userFacade.queryById(project.getUserId());
             if(user!=null)
            	 view.addObject("user", user);
         }
		return view;
	}
	
	/**
	 * 项目发布成功之后插入一条项目反馈记录
	 * @param feedBack
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addfeedback")
    @ResponseBody
    public Map<String, Object> addfeedback(@ModelAttribute PFeedBack feedBack, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.loginFailedRes(null);
        }
        if (feedBack.getPid() == 0)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        
        ApiProject project = new ApiProject();
        project.setId(feedBack.getPid());
        project.setUserId(userId);
        ApiPage<ApiProject> rp = projectFacade.queryProjectList(project, 1, 1);
        if (rp.getTotal() == 0)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "项目提交成功,生成反馈信息失败", null);
        }
        
        ApiProjectFeedback f = new ApiProjectFeedback();
        f.setProjectId(feedBack.getPid());
        //反馈的项目类容
        ApiProjectUserInfo ap=new ApiProjectUserInfo();
        ap.setProjectId(rp.getResultData().get(0).getId());
        ap.setPersonType(2);
        ApiProjectUserInfo apiProjectUserInfo=projectFacade.queryProjectUserInfo(ap);
        if(apiProjectUserInfo==null){
        	return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "项目提交成功,生成反馈信息失败", null);
        }
        int betweenDays=0;
        if(rp.getResultData().get(0).getDeadline()!=null && rp.getResultData().get(0).getIssueTime()!=null){
            betweenDays=DateUtil.hoursBetween(rp.getResultData().get(0).getDeadline(), rp.getResultData().get(0).getIssueTime());
        }else{
        	 return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "参数错误", null);
        }
        f.setContent(apiProjectUserInfo.getRealName()+"发起筹款,筹款目标为"+rp.getResultData().get(0).getCryMoney()+"元,筹款时间"+betweenDays+"天");
        f.setFeedbackPeople(userId);
        f.setFeedbackTime(new Date());
        List<String> list = new ArrayList<String>(1);
        list.add(ApiProjectFeedback.getCacheRange(f.getClass().getName(), BaseBean.RANGE_WHOLE, feedBack.getPid()));
        f.initCache(true, 0, list);
        ApiResult result = projectFacade.saveProjectFeedback(f);
        if (result.getCode() == 1)
        {
            return webUtil.successRes(null);
        }
        else if (result.getCode() == 10002)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "评论包含非法字符", null);
        }
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED, "添加失败", null);
        }
    }
	
	/**
	 * 保存转发量到redis
	 * @param projectId
	 * @return
	 */
	@RequestMapping("saveRedisShareCount")
	@ResponseBody
	public Map<String, Object> saveRedisShareCount(@RequestParam(value="projectId",required=true)Integer projectId){
		/*转发量start*/
		try
		{
			//获取当天剩余缓存时间
			String clickRate = (String) redisService.queryObjectData(PengPengConstants.Project_ShareClick+projectId);
			if(StringUtils.isEmpty(clickRate))
			{
				redisService.saveObjectData(PengPengConstants.Project_ShareClick+projectId, "t_share_"+projectId+"_"+1);
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
					redisService.saveObjectData(PengPengConstants.Project_ShareClick+projectId, "t_share_"+projectId+"_"+click);
				}
			}
		}
		catch(Exception e)
		{
			
		}
	    //
		/*点击量统计end*/
		return webUtil.resMsg(1, "", "", null);
	}
	
	
	@RequestMapping("auditPersonDetails")
	@ResponseBody
	public Map<String ,Object> auditPersonDetails( @RequestParam(value="id") Integer auditPersonId){
		
		ApiAuditProject auditProject=new ApiAuditProject();
		auditProject.setId(auditPersonId);
		@SuppressWarnings("unchecked")
		ApiAuditProject apiAuditProject=userFacade.selectByParam(auditProject);
		;
		if(apiAuditProject!=null){
			JSONObject item = new JSONObject();
			item.put("id",apiAuditProject.getId());
			item.put("information",apiAuditProject.getInformation());
			item.put("relationship",apiAuditProject.getRelationship());
			item.put("realName",apiAuditProject.getRealName());
			return webUtil.resMsg(1, "0000", "查询成功", item);
		}
		else{
			return webUtil.resMsg(0, "0001", "查询失败", null);
		}
		
	}
	
	/**
	 * 跳转到项目认领页面
	 * @param model
	 * @return
	 */
	@RequestMapping("projectClaim_view")
	public ModelAndView projectClaim_view(HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView("seekhelp/projectClaim");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){
			view.setViewName("redirect:/user/sso/login.do");
			return view;
		}
		//查看用户是否是公益基金会用户
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setType(4);
		apiCompany.setUseState(100);
		apiCompany.setState(203);
		apiCompany.setUserId(userId);
		ApiCompany company = companyFacade.queryCompanyByParam(apiCompany);
		if(company!=null){
			view.addObject("showState", 1);
		}
		else{
			view.addObject("showState", 0);
		}
		//查询所有项目地址
		ApiPage<ApiProjectArea> page = projectFacade.queryProjectAreaList(null, 1, 34);
		view.addObject("list", page.getResultData());
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}
	
	/**
	 * 认领项目列表
	 * @param model
	 * @param project
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("projectClaimList")
	@ResponseBody
	public Map<String, Object> projectClaimList(ApiProject project,
			@RequestParam(value="pageSize",required=false,defaultValue="1")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="10")Integer pageNum) throws ParseException{
		project.setState(240);
		project.setIsHide(0);
		project.setClaimUserId(0);
		ApiPage<ApiProject> page = projectFacade.queryProjectListNew(project,pageSize,pageNum);
		if(page.getTotal()>0){
			for(int i=0;i<page.getResultData().size();i++){
				page.getResultData().get(i).setContent("");
				if(page.getResultData().get(i).getIssueTime()!=null){
				String formatDate =DateUtil.dateString2(page.getResultData().get(i).getIssueTime());
			    page.getResultData().get(i).setContent(formatDate);
				}
			}
			return webUtil.successRes(page);
		}
		return webUtil.successRes(null);
		
	}
	
	/**
	 * 发起人用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping("faqiUserInfo")
	@ResponseBody
	public Map<String, Object> faqiUserInfo(@RequestParam(value="id",required=true)Integer id){
		ApiFrontUser user = userFacade.queryById(id);
		if(user.getIdCard()!=null && user.getIdCard() != ""){
			user.setIdCard(StringUtil.getSafeNumber(user.getIdCard()));
		}
		if(user.getMobileNum()!=null && user.getMobileNum() != ""){
			user.setMobileNum(StringUtil.getSafeNumber(user.getMobileNum()));
		}
		return webUtil.successRes(user);
	}
	
	/**
	 * 认领项目
	 * @param projectId
	 * @return
	 */
	@RequestMapping("projectClaim")
	@ResponseBody
	public Map<String, Object> projectClaim(@RequestParam(value="projectId",required=true)Integer projectId,
			HttpServletRequest request, HttpServletResponse response){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId!=null){
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setType(4);
			apiCompany.setUseState(100);
			apiCompany.setState(203);
			apiCompany.setUserId(userId);
			ApiCompany company = companyFacade.queryCompanyByParam(apiCompany);
			if(company!=null){//检验用户是否审核通过
				ApiProject project = projectFacade.queryProjectDetail(projectId);
				if(project!=null && (project.getClaimUserId()==null || project.getClaimUserId() == 0)){//检查项目是否认领过
					project = new ApiProject();
					project.setId(projectId);
					project.setClaimUserId(userId);
					try {
						projectFacade.updateProject(project);
						return webUtil.resMsg(1, "0000", "认领成功！", null);
					} catch (Exception e) {
						return webUtil.resMsg(0, "0010", "认领失败！", null);
					}
				}
				return webUtil.resMsg(0, "0040", "项目已经认领过，无需再次认领！", null);
			}
			return webUtil.resMsg(0, "0040", "您没有权限认领项目！", null);
		}
		return webUtil.loginFailedRes(null);
	}
	
	/**
     * 进入项目详情页面
     * @param projectId
     * @return
     */
    @RequestMapping("specialProject_details")
    public ModelAndView specialProject_details(@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
    		){
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	ModelAndView mv=new ModelAndView("redirect:/project/view.do?projectId=" + projectId);
    	String isMobile = (String)request.getSession().getAttribute("ua");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
            mv.setViewName("redirect:/project/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return mv;
        }
        if(!"mobile".equals(isMobile)){
        	return mv;
        }
        if("mobile".equals(isMobile)&&(project.getSpecial_fund_id()==null||project.getSpecial_fund_id()==0) && project.getDaydayDonate()==0){//h5页面
        	mv.setViewName("redirect:/project/view_h5.do?projectId=" + projectId);
        	return mv;
        }
        if("mobile".equals(isMobile)&&(project.getSpecial_fund_id()==null||project.getSpecial_fund_id()==0) && project.getDaydayDonate()==1 && extensionPeople!=null){//h5日捐页面
        	mv.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
        	return mv;
        }
        if("mobile".equals(isMobile)&&(project.getSpecial_fund_id()==null||project.getSpecial_fund_id()==0) && project.getDaydayDonate()==1 && extensionPeople==null){//h5日捐页面
        	mv.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId=" + projectId);
        	return mv;
        }
        /*if("mobile".equals(isMobile) && !StringUtils.isBlank(project.getJumbleSale())){//义卖
        	if(extensionPeople == null){
        		mv.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId);
        	}else{
        		mv.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId + "&extensionPeople="+extensionPeople);
        	}
        	return mv;
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
    		}
    	}}
    	// 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
    	String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
    	report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
    	ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
    					30);
    	//项目配捐
    	ApiMatchDonate apiMatchDonate = new ApiMatchDonate();
    	if(project.getId()!=null && project.getId()!=0){
    		apiMatchDonate.setProjectId(project.getId());
            ApiPage<ApiMatchDonate> apiPage = redPacketsFacade.queryByParam(apiMatchDonate, 1, 5);
            List<ApiMatchDonate> matchDonates = apiPage.getResultData();
            if(matchDonates != null && matchDonates.size() >0){
            	mv.addObject("matchDonate", matchDonates.get(0));
            }
    	}
		
    	double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			//去掉html标签,转发时控制转发的内容的字数
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
			
		}
		if (reports != null && reports.getTotal() > 0) {
			mv.addObject("reports", reports.getResultData());
		}
		//判断是否加入善库
		ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade.queryByParam(apigp,1,50);
		if(goodLibrary != null && goodLibrary.getResultData().size() > 0){
			mv.addObject("goodLibrary", 1);
		}else {
			mv.addObject("goodLibrary", 0);
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
		}
		mv.addObject("peopleNum", project.getDonationNum());
		
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
		mv.addObject("projectId",projectId);
		mv.addObject("extensionPeople",extensionPeople);
		mv.addObject("browser",browser);
		mv.addObject("userId", userId);
		
		/**查询专项基金信息*/
		ApiSpecialFund sFund = projectFacade.querySpecialFundById(project.getSpecial_fund_id());
		mv.addObject("sFund", sFund);
		/**查询报名头像等信息*/
		long totalUser=0;
		ApiPage<ApiCommonFormUser> cUsers = projectFacade.queryCommonFormUserBySpecialFundId(project.getSpecial_fund_id(),1,8);
		if(cUsers!=null&&cUsers.getTotal()>0){
			mv.addObject("cUsers", cUsers.getResultData());
			totalUser=cUsers.getTotal();
		}
		mv.addObject("totalUser", totalUser);
		/*随机获取祝福语*/
		ApiConfig apiConfig = new ApiConfig();
    	apiConfig.setConfigKey(StringUtil.LEAVEWORD);
    	List<ApiConfig> configs = commonFacade.queryList(apiConfig);
    	if(configs!=null&&configs.get(0)!=null&&configs.get(0).getConfigValue()!=null&&configs.get(0).getConfigValue()!=""){
    		String[] configss = configs.get(0).getConfigValue().split("。");
    		Random random = new Random();
    		int i = random.nextInt(configss.length);
    		mv.addObject("leaveWord", configss[i]);
    	}
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
	    //
		/*点击量统计end*/
    	mv.addObject("project", project);
    	mv.setViewName("h5/specialProject/specialProject_detail");
    	return mv;
    }

    /**
     * 进入项目详情页面
     * @param projectId
     * @return
     */
    @RequestMapping("dayDonateProject_details")
    public ModelAndView dayDonateProject_details(@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
    		){
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	ModelAndView mv=new ModelAndView("redirect:/project/view.do?projectId=" + projectId);
    	
    	String isMobile = (String)request.getSession().getAttribute("ua");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)//pc端详情页面  带extentionPeople
        {
            mv.setViewName("redirect:/project/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return mv;
        }
        if(!"mobile".equals(isMobile)){//pc端详情页面  不带extentionPeople
        	return mv;
        }
        if("mobile".equals(isMobile)&&(project.getSpecial_fund_id()==null||project.getSpecial_fund_id()==0) && project.getDaydayDonate()==0){//h5页面
        	mv.setViewName("redirect:/project/view_h5.do?projectId=" + projectId);
        	return mv;
        }
        if("mobile".equals(isMobile) && project.getSpecial_fund_id()!=null && project.getSpecial_fund_id()!=0){//h5专项基金页面
        	mv.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId);
        	return mv;
        }
        /*if("mobile".equals(isMobile) && !StringUtils.isBlank(project.getJumbleSale())){//义卖
        	if(extensionPeople == null){
        		mv.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId);
        	}else{
        		mv.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId + "&extensionPeople="+extensionPeople);
        	}
        	return mv;
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
    		}
    	}}
    	
    	//日行一善人数   头像 
    	long totalUser=0;
    	ApiPage<ApiDonateRecord> cUsers = donateRecordFacade.queryDayDonateDetail(projectId,1,8);
    	if(cUsers!=null&&cUsers.getTotal()>0){
			mv.addObject("cUsers", cUsers.getResultData());
			totalUser=cUsers.getTotal();
		}
		mv.addObject("totalUser", totalUser);
		//发起一起捐人数
		int countTogetherConfig = projectFacade.countTogetherConfig(projectId);
		mv.addObject("countTogether", countTogetherConfig);
    	// 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
    	String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
    	report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
    	ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,
    					30);
    	//项目配捐
    	ApiMatchDonate apiMatchDonate = new ApiMatchDonate();
    	if(project.getId()!=null && project.getId()!=0){
    		apiMatchDonate.setProjectId(project.getId());
            ApiPage<ApiMatchDonate> apiPage = redPacketsFacade.queryByParam(apiMatchDonate, 1, 5);
            List<ApiMatchDonate> matchDonates = apiPage.getResultData();
            if(matchDonates != null && matchDonates.size() >0){
            	mv.addObject("matchDonate", matchDonates.get(0));
            }
    	}
		
    	double process = 0.0;
		Integer userId = UserUtil.getUserId(request, response);
		if(userId!=null){
			ApiFrontUser user = userFacade.queryById(userId);
			mv.addObject("user", user);
		}
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
			
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
            userInfo.setProjectId(projectId);
            List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
            for (ApiProjectUserInfo u : userInfos)
            {
                if (u.getPersonType() == 0)
                {
                    mv.addObject("shouzhu", u);
                }
                else if (u.getPersonType() == 2)
                {
                    mv.addObject("fabu", u);
                }
            }
			
		}
		if (reports != null && reports.getTotal() > 0) {
			mv.addObject("reports", reports.getResultData());
		}
		//判断是否加入善库
		ApiGoodLibraryProple apigp = new ApiGoodLibraryProple();
		apigp.setUserId(userId);
		apigp.setState(201);
		ApiPage<ApiGoodLibraryProple> goodLibrary = goodLibraryFacade.queryByParam(apigp,1,50);
		if(goodLibrary != null && goodLibrary.getResultData().size() > 0){
			mv.addObject("goodLibrary", 1);
		}else {
			mv.addObject("goodLibrary", 0);
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
		}
		//mv.addObject("peopleNum", project.getDonationNum());
		
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
		mv.addObject("projectId",projectId);
		mv.addObject("extensionPeople",extensionPeople);
		mv.addObject("browser",browser);
		mv.addObject("userId", userId);
		
		/*随机获取祝福语*/
		ApiConfig apiConfig = new ApiConfig();
    	apiConfig.setConfigKey(StringUtil.LEAVEWORD);
    	List<ApiConfig> configs = commonFacade.queryList(apiConfig);
    	if(configs!=null&&configs.get(0)!=null&&configs.get(0).getConfigValue()!=null&&configs.get(0).getConfigValue()!=""){
    		String[] configss = configs.get(0).getConfigValue().split("。");
    		Random random = new Random();
    		int i = random.nextInt(configss.length);
    		mv.addObject("leaveWord", configss[i]);
    	}
		
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
    	mv.addObject("project", project);
    	mv.setViewName("h5/specialProject/dayDonateProject_detail");
    	return mv;
    }
    
    @RequestMapping(value="dayDonate",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject dayDonate(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam("projectId")Integer projectId){
    	JSONObject item = new JSONObject();
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null){
    		item.put("code", 0);
    		item.put("msg", "未登录");
    		return item;
    	}
    	
    	ApiResult res = new ApiResult();
    	ApiFrontUser user = userFacade.queryById(userId);
    	ApiProject project = projectFacade.queryProjectDetail(projectId);
    	//查询用户的日捐
    	ApiDonateTime param = new ApiDonateTime();
    	param.setUserId(userId);
    	param.setProjectIds(projectId.toString());
    	param.setState(201);//进行中
    	ApiPage<ApiDonateTime> page = donateRecordFacade.queryDonateTimeByParam(param, 1, 1);
    	if(page!=null && page.getTotal()>0){
    		//日捐金额
    		Double money = page.getResultData().get(0).getMoney();
    		//用户余额
    		Double balance = user.getBalance();
    		Double availableBalance = user.getAvailableBalance();
    		balance=MathUtil.sub(balance, money);
    		availableBalance=MathUtil.sub(availableBalance, money);
    		
    		ApiCapitalinout cp = new ApiCapitalinout();
    		cp.setUserId(user.getId());
			cp.setTranNum(StringUtil.uniqueCode());
			cp.setPayState(302);
			cp.setType(0);
			cp.setInType(0);
			cp.setSource("H5");
			cp.setBalance(balance);
			cp.setPayType("freezType");// 余额支付
			cp.setPayNum("日捐发起扣款");
			cp.setMoney(money);
			cp.setCreateTime(new Date());
			//TODO ca save
			
			
			ApiDonateRecord dr = new ApiDonateRecord();
			dr.setCapitalinoutId(cp.getId());
			dr.setProjectId(project.getId());
			dr.setUserId(user.getId());
			dr.setDonorType("InternalPers");
			dr.setDonatAmount(money);
			dr.setState(302);
			dr.setDonateCopies(0);
			dr.setDonatTime(DateUtil.getCurrentTimeByDate());
			dr.setDonatType("daylyDonation");
			
			dr.setMonthDonatId(page.getResultData().get(0).getId());
			//TODO donate save
			
			
			//更新项目
			project.setDonatAmount(MathUtil.add(project.getDonatAmount()==null?0.00:project.getDonatAmount(),money));
			project.setDonationNum(project.getDonationNum()==null?0:project.getDonationNum() + 1);
			if(project.getDonatAmount() >= project.getCryMoney()){
				project.setState(260);
			}
			//TODO project update
			
			
			// 更新用户捐款总额
			user.setBalance(MathUtil.sub(user.getBalance(), money));
			user.setAvailableBalance(MathUtil.sub(user.getAvailableBalance(), money));
			user.setTotalAmount(MathUtil.add(user.getTotalAmount(), money));
			//TODO user update
			
			//更新捐款次数
			page.getResultData().get(0).setNumber(page.getResultData().get(0).getNumber() + 1);
			
			res = donateRecordFacade.addDayDonate(cp,dr,project,user);
			if(res.getCode()==1){
				//发起月捐成功短信通知
				ApiAnnounce apiAnnounce = new ApiAnnounce();
				apiAnnounce.setCause("日捐通知");
				apiAnnounce.setContent(user.getNickName()+"，您的日捐已开通，每日捐赠"+page.getResultData().get(0).getMoney()+"元，持续"+page.getResultData().get(0).getDayNumber()+"天，感谢您的善举");
		    	apiAnnounce.setDestination(page.getResultData().get(0).getMobileNum());
		    	apiAnnounce.setType(1);
		    	apiAnnounce.setPriority(1);
		    	commonFacade.sendSms(apiAnnounce, false);
				item.put("code", 1);
				item.put("msg", "success");
			}else{
				item.put("code", -1);
				item.put("msg", "error");
			}
			//TODO donatetime update
			page.getResultData().get(0).setDayNumber(page.getResultData().get(0).getDayNumber()-1);
			donateRecordFacade.updateDonateTime(page.getResultData().get(0));
    	}
    	return item;
    }
    
    
    @RequestMapping(value="wxLogin",method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> wxLogin(@RequestParam(value="projectId",required=true)Integer projectId,
    		@RequestParam(value="extensionPeople",required=false)String extensionPeople,
    		@RequestParam(value="url",required=true)String url,
    		HttpServletRequest request,HttpServletResponse response){
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		String browser = UserUtil.Browser(request);
		ApiFrontUser user1 = new ApiFrontUser();//用户
		Integer userId = UserUtil.getUserId(request, response);
        if(userId != null && userId != 0)
        {
	     	user1 = userFacade.queryById(userId); 
	     	return webUtil.resMsg(1, "0000", "已登录", null);
        }
        else
        {
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		try {
          			 openId = (String) request.getSession().getAttribute("openId");
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				
        			 if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(url);
        					return webUtil.resMsg(1, "0001", url_weixin_code, null);
        				}
        			}
        			 return webUtil.resMsg(1, "0000", "已登录", null);
        			
        		} catch (Exception e) {
        			logger.error("微信登录出现问题"+ e);
        			return webUtil.resMsg(-1, "0002", "error", null);
        		}
        		
        	}
        	else{//返回到账号密码页面
        		String backUrlString="http://www.17xs.org/ucenter/user/Login_H5.do";
        		return webUtil.resMsg(1, "0001", backUrlString, null);
        	}
        	
        }
    }
    
    
    /**
     * 进入项目详情页面
     * @param projectId
     * @return
     */
    @RequestMapping("oneAid")
    public ModelAndView oneAid(@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
    		){
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	ModelAndView mv=new ModelAndView("redirect:/project/view.do?projectId=" + projectId);
    	String isMobile = (String)request.getSession().getAttribute("ua");
        if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
            mv.setViewName("redirect:/project/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return mv;
        }
        if(!"mobile".equals(isMobile)){
        	return mv;
        }
        if("mobile".equals(isMobile)&&(project.getSpecial_fund_id()==null&&project.getSpecial_fund_id()==0)){//h5页面
        	mv.setViewName("redirect:/project/view_h5.do?projectId=" + projectId);
        }
    	// 项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
    	String key = PengPengConstants.PROJECT_SCHEDUlE_LIST+"_"+projectId;
    	report.initNormalCache(true, DateUtil.DURATION_TEN_S, key);
    	ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1,30);
		
		Integer userId = UserUtil.getUserId(request, response);
		if (project != null) {
			//去掉html标签,转发时控制转发的内容的字数
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
		}
		if (reports != null && reports.getTotal() > 0) {
			mv.addObject("reports", reports.getResultData());
		}
		
		//累计帮扶
		ApiOneAid param = new ApiOneAid();
		param.setProject_id(projectId);
		param.setState(201);
		int countOneAid = projectFacade.countOneAid(param);
		mv.addObject("countOneAid", countOneAid);
		//判断相符是否帮扶完成
		param.setState(200);
		int isFinshed = projectFacade.countOneAid(param);
		mv.addObject("isFinshed", isFinshed);
		//发起机构   发起单位  详细 受助人
		 ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
         userInfo.setProjectId(projectId);
         List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
         for (ApiProjectUserInfo u : userInfos)
         {
             if (u.getPersonType() == 0)
             {
                 mv.addObject("shouzhu", u);
             }
             /*else if (u.getPersonType() == 1)
             {
                 mv.addObject("zhengming", u);
             }*/
             else if (u.getPersonType() == 2)
             {
                 mv.addObject("fabu", u);
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
		mv.addObject("projectId",projectId);
		mv.addObject("extensionPeople",extensionPeople);
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
    	mv.addObject("project", project);
    	if(project!=null && project.getContentImageId()!=null && !"".equals(project.getContentImageId())){
    		String[] contentImageUrls = project.getContentImageId().split("\\,");
    		List<String> contentImageUrl = new ArrayList<String>();
    		for (String contentImageUrlStr : contentImageUrls) {
				ApiBFile bFile = fileFacade.queryBFileById(Integer.valueOf(contentImageUrlStr));
				if(bFile!=null && bFile.getUrl()!=null){
					contentImageUrl.add(bFile.getUrl());
				}
			}
    		mv.addObject("imgs", contentImageUrl);
    	}
    	mv.setViewName("h5/specialProject/oneAid");
    	return mv;
    }
    
    @RequestMapping("planSet_view")
    public ModelAndView planSet(ModelAndView mv,@RequestParam("projectId")Integer projectId,
    		HttpServletRequest request,HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	String browser = UserUtil.Browser(request);
    	mv.setViewName("h5/specialProject/planSet");
    	ApiProject p = projectFacade.queryProjectDetail(projectId);
    	mv.addObject("title", p.getTitle());
    	mv.addObject("cause", p.getCryCause());
    	mv.addObject("projectId", projectId);
    	mv.addObject("userId", userId);
    	mv.addObject("browser", browser);
    	return mv;
    }
    
    /**
     * 根据projectId随机获取获取一条帮扶
     * @param projectId
     * @return
     */
    @RequestMapping("oneAid_random")
    @ResponseBody
    public JSONObject oneAid_random(@RequestParam("projectId")Integer projectId){
    	JSONObject item = new JSONObject();
    	ApiOneAid model = projectFacade.queryOneAidByRandom(projectId);
    	if(model != null){
    		item.put("id", model.getId());
    		item.put("name", model.getName());
    		item.put("age", model.getAge());
    		item.put("sex", model.getSex());
    		item.put("addr", model.getLocation().trim());
    		item.put("content", model.getIntroduction());
    		item.put("total_money", model.getTotal_money());
    		List<String> contentImages = new ArrayList<String>();
    		if(model.getContentImageUrl()!=null && !"".equals(model.getContentImageUrl())){
    			String[] contentImageStrs=model.getContentImageUrl().split("\\,");
    			for (String contentImageStr : contentImageStrs) {
    				contentImages.add(contentImageStr);
				}
    		}
    		item.put("imgList", contentImages);
    		item.put("code", 1);
    	}
    	else{
    		item.put("code", 0);
    	}
    	return item;
    }
    
    /**
	 * 根据不同的状态查询某个人的报名列表
	 * @param  项目的状态  state
	 * @userId  用户的id
	 * @return   projectList
	 */
	@RequestMapping("myEntryFormList")
	public ModelAndView myEntryFormList(HttpServletRequest request,@RequestParam(value="state",required=true)Integer state,
			 HttpServletResponse response,String currentPage){
		ModelAndView mv=new ModelAndView("h5/specialProject/myEntryForm");
		//判断是否是微信登录
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user1=null;
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){
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
	    					mv = new ModelAndView("redirect:" + url_weixin_code);
	    					return mv;
	    				}
	    				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
	    				openId = mapToken.get("openid").toString();
	    				unionid = mapToken.get("unionid").toString();
	    				token = mapToken.get("access_token").toString();
	    				request.getSession().setAttribute("openId", openId);
	    				System.out.println("batch_project_list >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
	    				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
	    			}
	    			user1 = CommonUtils.queryUser(request,openId,token,unionid);
	    		} catch (Exception e) {
	    			logger.error("微信登录处理出现问题"+ e);
	    		}
	    		
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
	    		
	    		try
	    		{
	    			// 自动登录
	    			SSOUtil.login(user1, request, response);
	    			logger.info("user1>>>>>>>"+user1.toString());
	    			userId=user1.getId();
	    		}
	    		catch(Exception e)
	    		{
	    			logger.error("",e);
	    		}
	    		
	    	}else{
	    		mv = new ModelAndView("h5/ucenter/userLogin");
				return mv;
	    	}
		}
		ApiCommonFormUser param = new ApiCommonFormUser();
		if(state==-1){
			param.setState(null);
		}
		else{
			param.setState(state);
		}
		param.setUserId(userId);
		List<ApiCommonFormUser> list = projectFacade.queryCommonFormUserByParam(param);
		mv.addObject("list", list);
		mv.addObject("num", list.size());
		mv.addObject("state", state);
		return mv;
	}
	
	/**
	 * 一对一愿望列表
	 * @param request
	 * @param response
	 * @param projectId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="getOneAidHelpList",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject oneAidHelpList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws ParseException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		ApiOneAid apiOneAid = new ApiOneAid();
		apiOneAid.setState(200);
		apiOneAid.setProject_id(projectId);
		ApiPage<ApiOneAid> page = projectFacade.queryOneAidByParam(apiOneAid, pageNum, pageSize);
		if(page!=null && page.getTotal()>0 && (page.getTotal()-pageNum*pageSize>=0 || pageNum*pageSize-page.getTotal()<pageSize)){
			for (ApiOneAid o: page.getResultData()) {
				JSONObject userJson = new JSONObject();
				userJson.put("id", o.getId());
				userJson.put("introduction", o.getIntroduction());
				userJson.put("total_money", o.getTotal_money());
				userJson.put("name", o.getName());
				userJson.put("age", o.getAge());
				userJson.put("location", o.getLocation());
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
	 * 一对一心愿详情
	 * @param projectId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/wishProjectDetailView")
	public ModelAndView wish_project_detail_view(@RequestParam(value = "projectId", required = true)Integer projectId,
			HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView("h5/project/wish_project_detail");
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		Integer userId = UserUtil.getUserId(request, response);
		if(project != null){
			ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
			userInfo.setProjectId(projectId);
			userInfo.setPersonType(2);
			userInfo = projectFacade.queryProjectUserInfo(userInfo);
			Integer count=projectFacade.queryOneAidHelpByprojectId(projectId);
			view.addObject("projectId", projectId);
			view.addObject("projectLogo", project.getCoverImageUrl());
			view.addObject("projectTitle", project.getTitle());
			view.addObject("realName", userInfo.getRealName());
			view.addObject("donationNum", project.getDonationNum());
			view.addObject("count", count);
		}
		if(userId != null){
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("user", user);
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
		return view;
	}
	
	private String getImgCode(HttpServletRequest request) {
		return imgcodeprex + getUuid(request);
	}
	
	private String getUuid(HttpServletRequest request) {
		String uuid = CookieManager.retrieve(CookieManager.COOKIE_UUID_NAME,
				request, true);
		if (uuid == null) {
			return request.getSession().getId();
		} else {
			return uuid;
		}
	}
}
