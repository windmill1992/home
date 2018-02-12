package com.guangde.home.controller.newRelProjectPC;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IHomePageFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.*;
import com.guangde.entry.*;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.constant.ResultEnum;
import com.guangde.home.controller.project.ProjectController;
import com.guangde.home.utils.*;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.common.Result;
import com.guangde.home.vo.project.Appeal;
import com.guangde.home.vo.user.UserCard;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("newReleaseProject")
public class NewReleaseProjectController {
	Logger logger = LoggerFactory.getLogger(ProjectController.class);
	private static final String phonecodeprex = "phonecode_r_";
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IHomePageFacade homePageFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	
	@RequestMapping("success")
	public ModelAndView success(ModelAndView view ,@RequestParam(value="projectId",required=true)Integer projectId){
		view.setViewName("releaseProject/release_success");
		view.addObject("type", 1);
		view.addObject("projectId", projectId);
		return view;
	}
	
	/**
	 * 跳转到发布页面路径
	 * @param view
	 * @return
	 */
	@RequestMapping("institutionReleaseProject")
	public ModelAndView institutionReleaseProject(ModelAndView view,@RequestParam(value="companyId",required=true)Integer companyId){
		view.setViewName("releaseProject/recipients_ infojg");
		view.addObject("helpType", 4);
		view.addObject("type", 1);
		view.addObject("zlId", companyId);
		return view;
	}
	
	@RequestMapping("releaseProjectDetails")
	public ModelAndView releaseProjectDetails(ModelAndView view,@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="zlId",required=true)Integer zlId,String typeName_e
			){
		view.setViewName("releaseProject/project_information");
		view.addObject("helpType", 4);
		view.addObject("type", 1);
		view.addObject("projectId", projectId);
		view.addObject("zlId", zlId);
		view.addObject("typeName_e", typeName_e);
		ApiTypeConfig ag=new ApiTypeConfig();
    	ag.setTypeName_e(typeName_e);
    	ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
    	view.addObject("atc", atc);
		return view;
	}
	
	@RequestMapping("gotoReleaseProject")
	public ModelAndView gotoReleaseProject(ModelAndView view,
			HttpServletRequest request,HttpServletResponse response){
		//1.判断是否登录
		//2.是否证实过（true：跳过，进行下一步；false：填写信息，进行下一步）
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){
			view.setViewName("redirect:/user/sso/login.do");
			view.addObject("entrance", "http://www.17xs.org/newReleaseProject/gotoReleaseProject.do");
			return view;
		}
		ApiAuditStaff apiAuditStaff = new ApiAuditStaff();
		apiAuditStaff.setUserId(userId);
		apiAuditStaff.setPersonType("helpPeople");
		ApiAuditStaff user=userFacade.queryAuditStaffByParam(apiAuditStaff);
		ApiCompany param = new ApiCompany();
		param.setUserId(userId);
		ApiCompany company = companyFacade.queryCompanyByParam(param);
		if(company!=null&&company.getState()==203){//企业认证通过
			view = new ModelAndView("redirect:/newReleaseProject/institutionReleaseProject.do?companyId="+company.getId());
			return view;
		}
		else if(user!=null&&user.getState()==203){//实名认证通过
			view = new ModelAndView("redirect:/newReleaseProject/gotoAidedPerson.do?userId="+user.getId());
			return view;
		}
		String covIds =null;
		if(user!=null){
			covIds=user.getFileId();
		}
		logger.info("covIds>>>>>"+covIds);
        if (covIds!=null)
        {
            String[] covString = covIds.split(",");
            if(covString.length==3){
            	//System.out.println(Integer.parseInt(covString[0]));
            	ApiBFile userimg1 = fileFacade.queryBFileById(Integer.parseInt(covString[0]));
            	ApiBFile userimg2 = fileFacade.queryBFileById(Integer.parseInt(covString[1]));
            	ApiBFile userimg3 = fileFacade.queryBFileById(Integer.parseInt(covString[2]));
            	view.addObject("userimg1", userimg1==null?"":userimg1.getUrl());
            	view.addObject("userimg2", userimg2==null?"":userimg2.getUrl());
            	view.addObject("userimg3", userimg3==null?"":userimg3.getUrl());
            	view.addObject("userimgid1", userimg1==null?"":userimg1.getId());
            	view.addObject("userimgid2", userimg2==null?"":userimg2.getId());
            	view.addObject("userimgid3", userimg3==null?"":userimg3.getId());
            }
        }
        String comIds=null;
        if(company!=null){
        	comIds = company.getContentImageId();
        }
        if (comIds!=null)
        {
            String[] comStrings = comIds.split(",");
            if(comStrings.length==2){
            	ApiBFile companyimg1 = fileFacade.queryBFileById(Integer.parseInt(comStrings[0]));
            	ApiBFile companyimg2 = fileFacade.queryBFileById(Integer.parseInt(comStrings[1]));
            	view.addObject("companyimg1", companyimg1==null?"":companyimg1.getUrl());
            	view.addObject("companyimg2", companyimg2==null?"":companyimg2.getUrl());
            	view.addObject("companyimgid1", companyimg1==null?"":companyimg1.getId());
            	view.addObject("companyimgid2", companyimg2==null?"":companyimg2.getId());
            }
        }
        ApiFrontUser user2=userFacade.queryById(userId);
        view.addObject("user", user2);
        view.addObject("company", company);
        
		view.setViewName("releaseProject/releaseProject");
		return view;
	}
	
	/**
	 * PC个人实名认证
	 * @param request
	 * @param response
	 * @param apiFrontUser
	 * @param ids
	 * @param personalVerCode
	 * @return
	 */
	@RequestMapping("authenticationPerson")
	@ResponseBody
	public Map<String, Object> authenticationPerson(HttpServletRequest request,HttpServletResponse response,
			ApiFrontUser apiFrontUser,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "personalVerCode", required = true) String personalVerCode){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null || userId==0){//未登录
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		// 1.校验参数
			if (StringUtil.isMobile(apiFrontUser.getMobileNum())) {
				// 校验手机验证码
				phonekey = phonecodeprex + apiFrontUser.getMobileNum();

				 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
						 personalVerCode, true);
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
		apiFrontUser.setId(userId);
		int res = userFacade.updateUserAndAudit(apiFrontUser, ids);
		int flag=0;
		if(res>0)
			flag=1;
		return webUtil.resMsg(flag, "0000", "0,"+userId+","+res, null);
	}
	
	/**
	 * 机构实名认证
	 * @param request
	 * @param response
	 * @param apiCompany
	 * @param ids
	 * @param personalVerCode
	 * @return
	 */
	@RequestMapping("authenticationTeam")
	@ResponseBody
	public Map<String, Object> authenticationTeam(HttpServletRequest request,
			HttpServletResponse response,ApiCompany apiCompany,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "personalVerCode", required = true) String personalVerCode){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null || userId==0){//未登录
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		// 1.校验参数
		if (StringUtil.isMobile(apiCompany.getMobile())) {
			// 校验手机验证码
			phonekey = phonecodeprex + apiCompany.getMobile();

			 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					 personalVerCode, true);
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
		int result=0;
		ApiCompany param = new ApiCompany();
		param.setUserId(userId);
		ApiCompany company = companyFacade.queryCompanyByParam(param);
		apiCompany.setUserId(userId);
		apiCompany.setContentImageId(ids);
		apiCompany.setState(201);
		apiCompany.setMobileState(203);
		if(company!=null){
			apiCompany.setId(company.getId());
			companyFacade.updateCompany(apiCompany);
			result=company.getId();
		}
		else{
			apiCompany.setIdentity("");
			apiCompany.setRegisterNo("");
			apiCompany.setLegalName("");
			apiCompany.setRegisterTime(new Date());
			apiCompany.setLastUpdateTime(new Date());
			result = companyFacade.save(apiCompany);
		}
		logger.info("result>>>>"+result);
		if(result>0&&result!=10005){
			return webUtil.resMsg(1, "0000", "1,"+userId+","+result, null);
		}
		else if(result>0&&result==10005){
			return webUtil.resMsg(10005, "0001", "1,"+userId+","+result, null);
		}
		return webUtil.resMsg(0, "0010", "添加失败！", null);
	}
	
	/**
	 * 跳转到受助人信息页面
	 * @param view
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("gotoAidedPerson")
	public ModelAndView gotoAidedPerson(ModelAndView view,@RequestParam(value="userId")Integer presonReleaseId,
			HttpServletRequest request,HttpServletResponse response){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){
			view.setViewName("redirect:/user/sso/login.do");
			view.addObject("entrance", "http://www.17xs.org/newReleaseProject/gotoAidedPerson.do");
			return view;
		}
		//为本人回显信息
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("oneself", user);
		view.addObject("presonReleaseId", presonReleaseId);
		view.setViewName("releaseProject/recipients_ infogr");
		//查询
		return view;
	}
	
	@RequestMapping("queryTypeConfigByTypeName_e")
	@ResponseBody
	public Map<String, Object> queryTypeConfigByTypeName_e(@RequestParam(value="typeName_e",required=true)String typeName_e){
		    ApiTypeConfig apiTypeConfig=new ApiTypeConfig();
		    apiTypeConfig.setTypeName_e(typeName_e);
			ApiTypeConfig typeConfig=commonFacade.queryApiTypeConfig(apiTypeConfig);
			JSONObject data = new JSONObject();
        	JSONArray items = new JSONArray();
			if(typeConfig!=null){
				
	        	JSONObject item=new JSONObject();
	        	item.put("id", typeConfig.getId());
	        	item.put("typeName", typeConfig.getTypeName());
	        	item.put("typeName_e", typeConfig.getTypeName_e());
	        	item.put("model", typeConfig.getModel());
	        	item.put("uploadImageDirection", typeConfig.getUploadImageDirection());
	        	
	        	items.add(item);
	        	data.put("items", items);
	        	return webUtil.resMsg(1, "0001", "成功", data);
			}
			else{
				data.put("items", items);
	        	return webUtil.resMsg(0, "0001", "成功", null);
			}
            
        
		
	}
	
	/**
	 * 求助类目列表
	 * @param helpType
	 * @param zlId
	 * @param type
	 * @return
	 */
	@RequestMapping("typeConfigList")
	@ResponseBody
	public Map<String, Object> typeConfigList(@RequestParam(value="helpType",required=false)String helpType,
    		@RequestParam(value="type" ,required=false)String type){
		List<ApiTypeConfig> listTypeConfig= commonFacade.queryList();
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
    	List<ApiTypeConfig> list=new  ArrayList<ApiTypeConfig>();
    	for (int i = 0; i < listTypeConfig.size(); i++) {
    		String fields=listTypeConfig.get(i).getHelpType();
    		
    		if(fields!=null){
    			String[] ids=fields.split(",");
        		for(int j=0;j<ids.length;j++){
        			if(helpType.equals(ids[j])){
        				list.add(listTypeConfig.get(i));
        			}
        		}
    		}
		}
        
        for(ApiTypeConfig typeConfig:list){
        	JSONObject item=new JSONObject();
        	item.put("id", typeConfig.getId());
        	item.put("typeName", typeConfig.getTypeName());
        	item.put("typeName_e", typeConfig.getTypeName_e());
        	items.add(item);
        }
        data.put("items", items);
		return webUtil.resMsg(1, "0001", "成功", data);
	}
	
	/**
	 * 添加受助人信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addAidedPerson")
	@ResponseBody
	public Map<String, Object> addAidedPerson(HttpServletRequest request,HttpServletResponse response,
			Appeal appel,@RequestParam(value="type",required=true)Integer type){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){
			return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		ApiFrontUser user=null;
		String workUnit="";
        String projectType="";
        user = userFacade.queryById(userId);
        appel.setAddress(user.getFamilyAddress());
        appel.setPhone(user.getMobileNum());
        appel.setName(user.getRealName());
        appel.setIdcard(user.getIdCard());
        appel.setWeixin(user.getQqOrWx());
        appel.setGzdw(user.getWorkUnit());
        appel.setZy(user.getPersition());
        projectType="personalItems";
        //获取前面验证的的图片
        if(type==1){//为个人发起
        	appel.setRelation("本人");
        	if(user.getContentImageId()!=null&&!"".equals(user.getContentImageId())){
        		appel.setImgIds(user.getContentImageId()+","+appel.getImgIds());
        	}
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("appel:" + appel.toString() + "userId:" + userId);
        }

         ApiProject project=new ApiProject();
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
         project.setField(appel.getField());
         project.setFieldChinese(appel.getFieldChinese());
         if(appel.getImgIds()!=null && !"".equals(appel.getImgIds())){
        	 project.setContentImageId(appel.getImgIds());
         }
         
         ApiResult result1 = projectFacade.launchProject(project);
        if (result1 == null)
        {
            return webUtil.resMsg(0, "0005", "没有这个项目", null);
        }else{
        	appel.setId(Integer.parseInt(result1.getMessage()));
        }
        
        //判断项目是否符合要求
        if (project.getState() != ProjectConstant.PROJECT_STATUS_DRAFT)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }
        if (appel.getType() != 0)
        {
            project.setState(ProjectConstant.PROJECT_STATUS_AUDIT1);
        }
        project.setLastUpdateTime(new Date());
        List<ApiProjectUserInfo> aList = new ArrayList<ApiProjectUserInfo>(3);
        ApiProjectUserInfo api = new ApiProjectUserInfo();
        
        try
        {
            // 证明人
            api.setProjectId(appel.getId());
            if(appel.getRname()!=null){
            	api.setRealName(webUtil.encodeTodecode(appel.getRname()));
            }if(appel.getRname()!=null){
            	api.setLinkMan(webUtil.encodeTodecode(appel.getRname()));
            }
            if(appel.getrAddress()!=null){
            	api.setWorkUnit(webUtil.encodeTodecode(appel.getrAddress()));
            }
            api.setLinkMobile(appel.getrPhone());
            if(appel.getRzw()!=null){
            	api.setPersition(webUtil.encodeTodecode(appel.getRzw()));
            }
            api.setPersonType(1);
            api.setHelpType(Integer.parseInt(type.toString()));
            api.setCreateTime(new Date());
            aList.add(api);
            // 发布人
            api = new ApiProjectUserInfo();
            api.setProjectId(appel.getId());
            api.setRelation(appel.getRelation());
            if(appel.getName()!=null){
            	api.setRealName(webUtil.encodeTodecode(appel.getName()));
            }
            if(workUnit!=null &&!"".equals(workUnit)){
            	api.setWorkUnit(workUnit);
            }
            if("0".equals(type)){
            	 api.setIndetity(appel.getIdcard());
            }
            if(appel.getAddress()!=null){
            	api.setFamilyAddress(webUtil.encodeTodecode(appel.getAddress()));
            }
            if(appel.getZy()!=null){
            	api.setVocation(webUtil.encodeTodecode(appel.getZy()));
            }
            if(appel.getGzdw()!=null){
            	api.setWorkUnit(webUtil.encodeTodecode(appel.getGzdw()));
            }
            if(appel.getIdentity()!=null){
            	api.setRelation(webUtil.encodeTodecode(appel.getIdentity()));
            }
            api.setLinkMobile(appel.getPhone());
            if(appel.getWeixin()!=null){
            	 api.setQqOrWx(webUtil.encodeTodecode(appel.getWeixin()));
            }
            api.setPersonType(2);
            api.setHelpType(Integer.parseInt(type.toString()));
            api.setCreateTime(new Date());
            aList.add(api);
            // 受助人
            api = new ApiProjectUserInfo();
            api.setProjectId(appel.getId());
            api.setPersonType(0);
            if(appel.getAppealName()!=null){
            	api.setRealName(webUtil.encodeTodecode(appel.getAppealName()));
            }
            if(appel.getAppealName()!=null){
            	api.setLinkMan(webUtil.encodeTodecode(appel.getAppealName()));
            }
            if(appel.getAppealName()!=null){
            	api.setRealName(webUtil.encodeTodecode(appel.getAppealName()));
            }
            if(appel.getAppealSex()!=null){
            	 api.setSex(webUtil.encodeTodecode(appel.getAppealSex()));
            }
            api.setAge(appel.getAppealAge());
            api.setIndetity(appel.getAppealIdcard());
            if(appel.getAppealAddress()!=null){
            	api.setFamilyAddress(webUtil.encodeTodecode(appel.getAppealAddress()));
            }
            if(appel.getDetailAddress()!=null){
            	api.setFamilyAddress(api.getFamilyAddress()+webUtil.encodeTodecode(appel.getDetailAddress()));
            }
            if(appel.getAppealzy()!=null){
            	api.setVocation(webUtil.encodeTodecode(appel.getAppealzy()));
            }
            if(appel.getAppealgzdw()!=null){
            	api.setWorkUnit(webUtil.encodeTodecode(appel.getAppealgzdw()));
            }
            api.setLinkMobile(appel.getAppealPhone());
            api.setHelpType(Integer.parseInt(type.toString()));
            api.setCreateTime(new Date());
            aList.add(api);
        }
        catch (UnsupportedEncodingException e)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("不支持编码异常" + e.getStackTrace());
            }
        }
        ApiResult res = projectFacade.updateProject(project);
        if (res != null && res.getCode() == 1)
        {
            api = new ApiProjectUserInfo();
            api.setProjectId(appel.getId());
            List<ApiProjectUserInfo> alist = projectFacade.queryProjectUserInfoList(api);
            if (alist.size() > 0)
            {
                if (alist.size() == 3)
                {
                    res = projectFacade.updateProjectUserInfo(aList);
                }
                else
                {
                    return webUtil.resMsg(0, "0004", "这个项目的人员信息有误，请另外换个项目发布", null);
                }
            }
            else
            {
                res = projectFacade.saveProjectUserInfo(aList);
            }
            String key = PengPengConstants.PROJECT_USERINFO_LIST + "_" + appel.getId();
            redisService.deleteData(key);
            if (res != null && res.getCode() == 1)
            {
                return webUtil.resMsg(1, "0000", "成功", appel.getId());
            }
            else
            {
                return webUtil.resMsg(0, "0003", "添加相关联系人信息失败", null);
            }
        }
        else
        {
            return webUtil.resMsg(0, "0004", "修改项目失败", null);
        }
	}
	
	/*
     * 为个人发布项目页面跳转
     * 
     */
    
    @RequestMapping("personReleaseList")
    public  ModelAndView personReleaseList(@RequestParam(value="type",required=false)String type,
   		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
   		 @RequestParam(value="projectId", required=true) String projectId,
   		 @RequestParam(value="zlId" ,required=false)String zlId){
    	ModelAndView mv=new ModelAndView();
    	mv.setViewName("releaseProject/project_information");
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	mv.addObject("projectId", projectId);
    	//根据typeName_e去查询求助领域
    	ApiTypeConfig ag=new ApiTypeConfig();
    	ag.setTypeName_e(typeName_e);
    	ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
    	if(atc!=null){
    		mv.addObject("atc", atc);
    	}
    	return mv;
    }
    
    
    
    /**
     * 发布项目接口（标题、筹款金额、筹款内容、封面图片、内容图片）
     * @param project
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="releaseProject",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject releaseProject(ApiProject project, Integer recommendedId,
    		Integer recommendedPerson,
    		Integer charityId,
    		HttpServletRequest request,HttpServletResponse response){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	JSONObject data = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	
    	ApiResult result = new ApiResult();
    	if(project.getId() != null){
    		 result = projectFacade.updateProject(project);
    	}else{
    		if(recommendedPerson!=null && charityId==null){//个人在医院通道发起
        		project.setRecommendedPerson(recommendedPerson);
        		project.setType("personalItems");
        		project.setIsHide(1);
        	}
        	if(recommendedPerson==null && charityId!=null){//机构发起项目
        		 project.setUserId(charityId);
        		 project.setType("enterpriseProject");
        	}else{
        		project.setRecommendedId(recommendedId);
        		project.setUserId(userId);
        		project.setType("personalItems");
        		project.setIsHide(1);
        	}
        	project.setDeadline(DateUtil.add(new Date(), 30));
        	project.setSubTitle(project.getTitle());
        	project.setAuditState(201);
            project.setPayMethod("alipay");
            project.setLocation("未知");
            project.setDetailAddress("未知");
            project.setIdentity("未知");
            project.setState(210);
            project.setIssueTime(new Date());
           
            project.setField("disease");
            project.setFieldChinese("医疗救助");
            result = projectFacade.launchProject(project);
    	}
        
    	if(result.getCode()==1){
    		if(project.getId()!=null){
    			logger.info("projectId="+project.getId());
        		data.put("projectId", project.getId());
    		}else{
    			logger.info("projectId="+result.getMessage());
        		data.put("projectId", result.getMessage());
    		}
    		
    		item.put("result", data);
    		item.put("code", 1);
    		item.put("msg", "success");
    	}
    	else{
    		item.put("code", 0);
    		item.put("msg", result.getMessage());
    	}
    	
    	return item;
    }
    
    /**
     * 添加受助人信息接口1.（姓名，联系人，联系电话）；2。（姓名，身份证号，家庭住址，图片：存到项目里隐藏掉）
     * @param info
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="addAidedPerson",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addAidedPerson(ApiProjectUserInfo info,
    		@RequestParam(value="charityId",required=false)Integer charityId,
    		HttpServletRequest request,HttpServletResponse response){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	ApiResult result = new ApiResult();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(info.getProjectId()==null){
    		item.put("code", -2);
    		item.put("msg", "projectId is null");
    		return item;
    	}
    	info.setAuditState(201);
    	info.setPersonType(0);
    	List<ApiProjectUserInfo> list = new ArrayList<ApiProjectUserInfo>();
    	list.add(info);
    	//查询记录是否存在
    	ApiProjectUserInfo apiProjectUserInfo = new ApiProjectUserInfo();
    	apiProjectUserInfo.setProjectId(info.getProjectId());
    	apiProjectUserInfo.setPersonType(0);
    	apiProjectUserInfo = projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    	if(apiProjectUserInfo!=null){//realName indetity familyAddress contentImageId
    		result = projectFacade.updateProjectUserInfo(list);
    		/*if(info.getPersition()!=null){//存的受助人审核图片
    			//1.隐藏；2.存入项目
    			for (String imgId : info.getPersition().split(",")) {
    				ApiBFile file = new ApiBFile();
    				file.setId(Integer.valueOf(imgId));
    				file.setIsHide(1);
    				fileFacade.updateBfile(file);
				}
    			ApiProject p = projectFacade.queryProjectDetail(info.getProjectId());
    			if(p!=null){
    				String contentImge = (p.getContentImageId()==null?"":p.getContentImageId())+info.getPersition();
    				p.setContentImageId(contentImge);
    				projectFacade.updateProject(p);
    			}
    		}*/
    	}else{//realName linkMan linkMobile
    		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
    		userInfo.setPersonType(1);
    		userInfo.setProjectId(info.getProjectId());
    		list.add(userInfo);
    		userInfo = new ApiProjectUserInfo();
    		userInfo.setProjectId(info.getProjectId());
    		userInfo.setPersonType(2);
    		//地址  电话  联系人 真实姓名
    		if(charityId != null){//机构
    			ApiFrontUser user = userFacade.queryById(charityId);
    			userInfo.setLinkMan(user.getRealName());
    			userInfo.setFamilyAddress(user.getFamilyAddress());
    			userInfo.setLinkMobile(user.getMobileNum());
    			userInfo.setWorkUnit(user.getWorkUnit());
    			userInfo.setRealName(user.getRealName());
        		userInfo.setHelpType(4);//机构为本人
    		}
    		list.add(userInfo);
    		result = projectFacade.saveProjectUserInfo(list);
    	}
    	if(result.getCode()==1){
    		item.put("code", 1);
    		item.put("msg", "success");
    	}
    	else{
    		item.put("code", 0);
    		item.put("msg", result.getMessage());
    	}
    	
    	return item;
    }
    
    /**
     * 查询医院信息接口（医院名称，收款账号，收款银行）
     * @param recommendedPerson
     * @param response
     * @return
     */
    @RequestMapping(value="getHostipalInfo",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject getHostipalInfo(@RequestParam(value="recommendedPerson",required=false)Integer recommendedPerson,
    		@RequestParam(value="charityId",required=false)Integer charityId,
    		HttpServletResponse response,HttpServletRequest request){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	JSONObject result = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(recommendedPerson==null && charityId==null){
    		item.put("code", -2);
    		item.put("msg", "recommendedPerson or charityId is null");
    		return item;
    	}
    	//从系统参数获取
    	ApiConfig config = new ApiConfig();
    	if(recommendedPerson!=null && charityId!=null){
    		item.put("code", -2);
    		item.put("msg", "参数错误！");
    		return item;
    	}
    	if(recommendedPerson!=null && charityId==null){
    		config.setConfigKey("RecommendedPerson_"+recommendedPerson);
    	}else{
    		config.setConfigKey("CharityId_"+charityId);
    	}
    	
    	List<ApiConfig> list = commonFacade.queryList(config);
    	if(list!=null && list.size()>0){
    		String[] con = list.get(0).getConfigValue().split("@");
    		if(con.length==3){
    			result.put("hospital", con[0]);
        		result.put("bankName", con[1]);
        		result.put("card", con[2]);
        		item.put("code", 1);
        		item.put("msg", "success");
    		}
    		else{
    			item.put("code", 0);
        		item.put("msg", "data to configure error");
    		}
    	}else{
    		item.put("code", 0);
    		item.put("msg", "data is null");
    	}
    	item.put("result", result);
    	
    	return item;
    }
    
    /**
     * 保存诊断信息
     * @param response
     * @param hospitalProve
     * @return
     */
    @RequestMapping(value="addHospitalProve",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addHospitalProve(HttpServletResponse response,
    		HttpServletRequest request,ApiHospitalProve hospitalProve){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	JSONObject item = new JSONObject();
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(hospitalProve.getProjectId()==null){
    		item.put("code", -2);
    		item.put("msg", "projectId is null");
    		return item;
    	}
    	ApiHospitalProve h = homePageFacade.queryApiHospitalProveByProjectId(hospitalProve.getProjectId());
    	ApiResult result = new ApiResult();
    	if(h!=null){
    		hospitalProve.setId(h.getId());
    		result = homePageFacade.updateHospitalProve(hospitalProve);
    	}else{
    		hospitalProve.setState(201);
    		result = homePageFacade.saveHospitalProve(hospitalProve);
    	}
    	
    	if(result.getCode()==1){
    		logger.info("HospitalProve ID IS "+result.getMessage());
    		item.put("code", 1);
    		item.put("msg", "success");
    	}else{
    		item.put("code", 0);
    		item.put("msg", result.getMessage());
    	}
    	
    	
    	return item;
    }
    
    /**
     * 获取信息状态
     * @param projectId
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="getAidedPersonState",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject getAidedPersonState(Integer projectId,HttpServletResponse response,
    		HttpServletRequest request){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	JSONObject data = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(projectId==null){
    		item.put("code", -2);
    		item.put("msg", "projectId is null");
    		return item;
    	}
    	
    	//受助人信息
    	ApiProjectUserInfo apiProjectUserInfo = new ApiProjectUserInfo();
    	apiProjectUserInfo.setProjectId(projectId);
    	apiProjectUserInfo.setPersonType(0);
    	apiProjectUserInfo = projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    	if(apiProjectUserInfo!=null && apiProjectUserInfo.getIndetity()!=null &&!"".equals(apiProjectUserInfo.getIndetity())){
    		data.put("aidedPersonState",1);
    	}else{
    		data.put("aidedPersonState",0);
    	}
    	//诊断说明
    	ApiHospitalProve prove = new ApiHospitalProve();
    	prove.setProjectId(projectId);
    	prove = homePageFacade.queryApiHospitalProveByProjectId(projectId);
    	if(prove!=null && prove.getDiseaseName()!=null && !"".equals(prove.getDiseaseName())){
    		data.put("hospitalProveState",1);
    	}else{
    		data.put("hospitalProveState",0);
    	}
    	//其他说明
    	if(prove!=null && ((prove.getHouseHoldIncome()!=null && !"".equals(prove.getHouseHoldIncome())||
    			prove.getHouseDetail()!=null && !"".equals(prove.getHouseDetail()))||(prove.getCarDetail()!=null && !"".equals(prove.getCarDetail())
    			||(prove.getMedicalInsurance()!=null && !"".equals(prove.getMedicalInsurance()))
    			||(prove.getHouseDetailImageId()!=null && !"".equals(prove.getHouseDetailImageId()))))
    			){
    		data.put("otherState",1);
    	}else{
    		data.put("otherState",0);
    	}
    	//收款账号
    	ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setProjectId(projectId);
		apiUserCard.setIsSelected(0);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(apiUserCard, 1, 1);
    	if(mapiPage!=null && mapiPage.getTotal()>0){
    		data.put("receivableState",1);
    	}else{
    		data.put("receivableState",0);
    	}
    	item.put("code", 1);
		item.put("msg", "success");
    	item.put("result", data);
    	return item;
    }
    
    /**
     * 获取受助人信息
     * @param projectId
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="getAidedPersonInfo",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject getAidedPersonInfo(Integer projectId,HttpServletResponse response,
    		HttpServletRequest request){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	JSONObject data = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(projectId==null){
    		item.put("code", -2);
    		item.put("msg", "projectId is null");
    		return item;
    	}
    	//查询记录是否存在
    	ApiProjectUserInfo apiProjectUserInfo = new ApiProjectUserInfo();
    	apiProjectUserInfo.setProjectId(projectId);
    	apiProjectUserInfo.setPersonType(0);
    	apiProjectUserInfo = projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    	if(apiProjectUserInfo != null){
    		data.put("name", apiProjectUserInfo.getRealName());
    		data.put("idCrad", apiProjectUserInfo.getIndetity());
    		data.put("address", apiProjectUserInfo.getFamilyAddress());
    		List<String> contentImgUrl = new ArrayList<String>();
    		List<Integer> contentImgInteger = new ArrayList<Integer>();
    		if(apiProjectUserInfo.getContentImageId()!=null && !"".equals(apiProjectUserInfo.getContentImageId())){
    			String[] contentImgIds = apiProjectUserInfo.getContentImageId().split(",");
    			for (String contentImgId : contentImgIds) {
					ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(contentImgId));
					if(file!=null){
						contentImgUrl.add(file.getUrl());
						contentImgInteger.add(Integer.valueOf(contentImgId));
					}
				}
    		}
    		data.put("contentImgUrl", contentImgUrl);
    		data.put("contentImgId", contentImgInteger);
    		item.put("code", 1);
    		item.put("msg", "success");
    	}
    	else{
    		item.put("code", 0);
    		item.put("msg", "data is null");
    	}
    	item.put("result", data);
    	return item;
    			
    }
    
    /**
     * 获取诊断证明信息
     * @param projectId
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="getHospitalProveInfo",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject getHospitalProveInfo(Integer projectId,Integer type,HttpServletResponse response,
    		HttpServletRequest request){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	JSONObject data = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(projectId==null){
    		item.put("code", -2);
    		item.put("msg", "projectId is null");
    		return item;
    	}
    	if(type==null){
    		item.put("code", -2);
    		item.put("msg", "type is null");
    		return item;
    	}
    	ApiHospitalProve prove = new ApiHospitalProve();
    	prove.setProjectId(projectId);
    	prove = homePageFacade.queryApiHospitalProveByProjectId(projectId);
    	if(type==0){//诊断信息
    		if(prove != null){
        		data.put("diseaseName", prove.getDiseaseName());
        		data.put("hospitalBedNumber", prove.getHospitalBedNumber());
        		List<String> contentImgUrl = new ArrayList<String>();
        		List<Integer> contentImgInteger = new ArrayList<Integer>();
        		if(prove.getDiseaseImageId()!=null && !"".equals(prove.getDiseaseImageId())){
        			String[] contentImgIds = prove.getDiseaseImageId().split(",");
        			for (String contentImgId : contentImgIds) {
    					ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(contentImgId));
    					if(file!=null){
    						contentImgUrl.add(file.getUrl());
    						contentImgInteger.add(Integer.valueOf(contentImgId));
    					}
    				}
        		}
        		data.put("contentImgUrl", contentImgUrl);
        		data.put("contentImgId", contentImgInteger);
        		item.put("code", 1);
        		item.put("msg", "success");
        	}
        	else{
        		item.put("code", 0);
        		item.put("msg", "data is null");
        	}
    	}else{//其他信息
    		if(prove != null){
        		data.put("houseDetail", prove.getHouseDetail());
        		data.put("houseHoldIncome", prove.getHouseHoldIncome());
        		data.put("medicalInsurance", prove.getMedicalInsurance());
        		data.put("carDetail", prove.getCarDetail());
        		List<String> contentImgUrl = new ArrayList<String>();
        		List<Integer> contentImgInteger = new ArrayList<Integer>();
        		if(prove.getHouseDetailImageId()!=null && !"".equals(prove.getHouseDetailImageId())){
        			String[] contentImgIds = prove.getHouseDetailImageId().split(",");
        			for (String contentImgId : contentImgIds) {
    					ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(contentImgId));
    					if(file!=null){
    						contentImgUrl.add(file.getUrl());
    						contentImgInteger.add(Integer.valueOf(contentImgId));
    					}
    				}
        		}
        		data.put("contentImgUrl", contentImgUrl);
        		data.put("contentImgId", contentImgInteger);
        		item.put("code", 1);
        		item.put("msg", "success");
        	}
        	else{
        		item.put("code", 0);
        		item.put("msg", "data is null");
        	}
    	}
    	item.put("result", data);
    	return item;
    }
    
    /**
     * 删除图片
     * @param imgId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="delImg",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject delImg(Integer imgId,HttpServletRequest request,HttpServletResponse response){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	Integer userId = UserUtil.getUserId(request, response);
    	JSONObject item = new JSONObject();
    	if(userId==null){
    		item.put("code", -2);
    		item.put("msg", "userId not login");
    		return item;
    	}
    	if(imgId==null){
    		item.put("code", -2);
    		item.put("msg", "imgId is null");
    		return item;
    	}
		ApiConfig config = new ApiConfig();
        config.setConfigKey("fileBasicURL");
        List<ApiConfig> list = commonFacade.queryList(config);
        String fileBasicURL = list.get(0).getConfigValue();
        if (fileBasicURL=="")
        {
        	item.put("code", -2);
    		item.put("msg", "图片原路径未配置，请联系技术人员");
        	return item;
        }
        String realPath = fileBasicURL + "/upload/picture/";
        ApiBFile bFile = fileFacade.queryBFileById(imgId);
        logger.info("real url path >>>>>"+realPath + bFile.getUrl().substring(28));
        ApiResult result = new ApiResult();
        boolean result2 = ImageUtil.deletePicture(realPath + bFile.getUrl().substring(28));//删除图片
        logger.info("file delete result is "+result2);
        result = fileFacade.deleteBfile(imgId);
		
		if(result == null || result.getCode() != 1){
			item.put("code", -2);
    		item.put("msg", "图片删除失败");
		}
		item.put("code", 1);
		item.put("msg", "success");
    	
    	return item;
    }
    
    /**
     * 进入项目详情页面
     * @param projectId
     * @return
     */
    @RequestMapping("project_detail")
    public ModelAndView project_detail(@RequestParam(value = "gotoType", defaultValue = "0", required = false)Integer gotoType, 
    		@RequestParam(value="projectId",required=true)Integer projectId,
    		HttpServletRequest request,HttpServletResponse response,
    		@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
    		){
    	ModelAndView mv=new ModelAndView("redirect:/uCenterProject/view.do?projectId=" + projectId);
    	String isMobile = (String)request.getSession().getAttribute("ua");
    	ModelAndView viewback = new ModelAndView();
    	//项目详情
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
		if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople!=null){//h5专项基金页面
			viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
	        return viewback;
	    }
	    if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople==null){//h5专项基金页面
	     	viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId);
	        return viewback;
	    }
	    if("mobile".equals(isMobile)&&project.getDaydayDonate()==1 && extensionPeople!=null){//h5日捐页面
        	viewback.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
	    if("mobile".equals(isMobile)&&project.getDaydayDonate()==1 && extensionPeople==null){//h5日捐页面
        	viewback.setViewName("redirect:/uCenterProject/dayDonateProject_details.do?projectId=" + projectId);
            return viewback;
        }
	    
	    if (!"mobile".equals(isMobile) && extensionPeople!=null)
        {
            viewback.setViewName("redirect:/project/view.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if(!"mobile".equals(isMobile)){
        	return viewback;
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
    	//用于跳转选项卡
    	mv.addObject("gotoType", gotoType);
    	if(project!=null && project.getUserId()!=null){
    		ApiFrontUser user = userFacade.queryById(project.getUserId());
    		mv.addObject("headImgUrl", user.getCoverImageUrl());
    		mv.addObject("nickName", user.getNickName());
    		
    	}
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
			//捐款笔数
        	ApiDonateRecord donate = new ApiDonateRecord();
        	donate.setState(302);
        	donate.setProjectId(projectId);
        	ApiDonateRecord donate1 = donateRecordFacade.queryCompanyCenter(donate);
        	if(donate1!=null)
        		mv.addObject("peopleNum", donate1.getGoodHelpCount());
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
		if(userId != null && userId != 0){
			r.setGoodLibraryId(userId);
		}
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
						r, 1, 10);
		mv.addObject("donates", donats.getResultData());
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
		ApiPage<ApiAuditProject> pageList=userFacade.queryApiAuditProjectList(apiAuditProject, 1, 7);
		List<ApiAuditProject> list1=pageList.getResultData();
		if(list1!=null && list1.size()>0){
			String pageContextUrl="http://res.17xs.org/picture/";
			for(ApiAuditProject ap:list1){
				if(ap!=null){
					if(ap.getHeadUrl()==null)
						ap.setHeadUrl("http://www.17xs.org/res/images/user/4.jpg");
					else if(!ap.getHeadUrl().contains("wx.qlogo.cn"))
						ap.setHeadUrl(pageContextUrl+ap.getHeadUrl());
				}
			}
			mv.addObject("auditProjectList", list1);
			if(list1!=null && list1.size()>0){
				mv.addObject("auditProject", list1.get(0));
			}
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
	    
		/*点击量统计end*/
    	mv.addObject("project", project);
    	//剩余发布天数
    	if(project.getDeadline()!=null){
    		int num = (int)DateUtil.hoursBetween(project.getDeadline(), new Date());
    		mv.addObject("oddDayNum", num<0?0:num);
    	}else{
    		mv.addObject("oddDayNum", 0);
    	}
    	
    	int submitState=2;
    	//受助人信息
    	ApiProjectUserInfo apiProjectUserInfo = new ApiProjectUserInfo();
    	apiProjectUserInfo.setProjectId(projectId);
    	apiProjectUserInfo.setPersonType(0);
    	apiProjectUserInfo = projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    	
    	//诊断信息
    	ApiHospitalProve prove = new ApiHospitalProve();
    	prove.setProjectId(projectId);
    	prove = homePageFacade.queryApiHospitalProveByProjectId(projectId);
    	Integer auState=0;
    	//判断用户是否登录
    	if(userId!=null && userId.equals(project.getUserId())){//登录
    		//未完整提交资料（诊断信息、受助人信息）
    		if(apiProjectUserInfo==null || apiProjectUserInfo.getIndetity()==null || 
    				"".equals(apiProjectUserInfo.getIndetity()) ||
    				prove==null || prove.getDiseaseName()==null || 
    				"".equals(prove.getDiseaseName())){
    			submitState=0;
    		}else{//已提交资料
    			submitState=1;
    		}
    	}
    	if(userId!=null){
    		//判断用户是否证实过
    		ApiAuditProject param = new ApiAuditProject();
    	 	param.setUserId(userId);
    	 	param.setProjectId(projectId);
    	 	ApiAuditProject auditProject2 = userFacade.selectByParam(param);
    	 	if(auditProject2!=null){
    	 		auState=1;
    	 	}
    	}
    	mv.addObject("auState",auState);
    	mv.addObject("recommendedPerson",project.getRecommendedPerson());
    	if(project.getRecommendedPerson()!=null){
    		ApiConfig config = new ApiConfig();
        	config.setConfigKey("RecommendedPerson_"+project.getRecommendedPerson());
        	List<ApiConfig> apiConfigs = commonFacade.queryList(config);
        	if(apiConfigs!=null && apiConfigs.size()>0){
        		String[] con = apiConfigs.get(0).getConfigValue().split("@");
        		if(con.length==3){
        			mv.addObject("hospital", con[0]);
        		}
        	}
    	}else{
    		ApiUserCard apiUserCard = new ApiUserCard();
			apiUserCard.setUserId(project.getUserId());
			apiUserCard.setProjectId(projectId);
			apiUserCard.setIsSelected(0);
			apiUserCard.setUseState(100);
			ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(
					apiUserCard, 1, 1);
			if(mapiPage!=null && mapiPage.getTotal()>0){
				mv.addObject("hospital", mapiPage.getResultData().get(0).getAccountName());
			}
    	}
    	
    	mv.addObject("submitState", submitState);
    	mv.addObject("userInfo", apiProjectUserInfo);
    	mv.addObject("prove", prove);
    	
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
    	
    	ApiFrontUser user = userFacade.queryById(userId);
    	mv.addObject("user", user);
    	mv.setViewName("h5/userCenter/publish_project_detail");
    	return mv;
    }

    /**
     * 查询帮助人数和捐款总金额
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value ="indexHead",method=RequestMethod.GET)
    @ResponseBody
    public JSONObject indexHead(HttpServletRequest request,HttpServletResponse response){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	JSONObject item = new JSONObject();
    	JSONObject data = new JSONObject();
    	
    	ApiConfig config = new ApiConfig();
		config.setConfigKey("helpPeople");
		List<ApiConfig> list = commonFacade.queryList(config);
		if(list != null && list.size() > 0){
			data.put("helpPeople", list.get(0).getConfigValue());
		}else{
			data.put("helpPeople", 0);
		}
		Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
    	data.put("totalMoney", Itotal==null?0:Itotal);
		
    	item.put("code", 1);
		item.put("msg", "success");
    	item.put("result", data);
    	
    	return item;
    }
    
    /**
     * 医院涂氟信息
     * @param mobile
     * @return
     */
    @RequestMapping(value="getHospitalAppoint",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getHospitalAppoint(HttpServletResponse response,
    		@RequestParam("mobile")Long mobile){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	JSONObject item = new JSONObject();
    	JSONObject result = new JSONObject();
    	//未涂氟     涂氟未达到时间     达到时间可以涂氟 
    	try {
    		//查询涂氟过的用户报名信息
	    	List<ApiHospitalAppoint> list = projectVolunteerFacade.queryHospitalByMobile(mobile,1);
	    	if(list!=null && list.size()>0){
	    		ApiHospitalAppoint appoint = list.get(0);
	    		result.put("id", appoint.getId());
	    		result.put("childName", appoint.getName());
	    		result.put("iDCard", appoint.getId_card());
	    		result.put("parentName", appoint.getFamily_name());
	    		result.put("tufuHospital", appoint.getHospital());
	    		result.put("appointmentTime", appoint.getAppointment_time());
	    		String fluoride = appoint.getFluoride_time();
	    		int tufuNum = 0;
	    		String tufuTime = null;
	    		if(fluoride!=null && !"".equals(fluoride)){
	    			tufuNum = fluoride.split("_").length;
	    			tufuTime = fluoride.split("_")[0];
	    		}
	    		result.put("tufuNum", tufuNum);
	    		result.put("lastTufuTime", tufuTime);//最近的涂氟时间
	    		//判断时间是否达到二次涂氟要求
	    		int tufuState = 0;
	    		int day = DateUtil.hoursBetween(DateUtil.add(DateUtil.toDefaultDateTime(tufuTime),177),new Date());
	    		int day2=0;
	    		
	    		//拼接预约时间
	    		String year = appoint.getC3().split("\\-")[0];	    		//获取年
	    		//获取月日
	    		if(appoint.getAppointment_time()!=null && !"".equals(appoint.getAppointment_time())){
	    			String[] months = appoint.getAppointment_time().replace("月", "-").replace("日", "-").split("-");
		    		if(months.length>=2){
		    			String month = months[0]+"-"+months[1];
			    		day2 = DateUtil.hoursBetween(DateUtil.toDefaultDate(year+"-"+month),DateUtil.add(DateUtil.toDefaultDateTime(tufuTime),177));
			    		if(day2>=0){//已预约
			    			tufuState=2;
			    		}else if(day<=3){//已涂氟
			    			tufuState=1;
			    		}
		    		}
	    		}
	    		
	    		result.put("tufuState", tufuState);
	    		item.put("result", result);
	    		item.put("msg", "success");
		    	item.put("code", 1);
	    	}else{
	    		//查询报名过但未涂氟的报名信息
	    		List<ApiHospitalAppoint> noTufulist = projectVolunteerFacade.queryHospitalByMobile(mobile,0);
	    		if(noTufulist!=null && noTufulist.size()>0){
	    			ApiHospitalAppoint appoint = noTufulist.get(0);
		    		result.put("id", appoint.getId());
		    		result.put("childName", appoint.getName());
		    		result.put("iDCard", appoint.getId_card());
		    		result.put("parentName", appoint.getFamily_name());
		    		result.put("tufuHospital", appoint.getHospital());
		    		result.put("appointmentTime", appoint.getAppointment_time());
		    		result.put("tufuNum", 0);
		    		result.put("lastTufuTime", null);//最近的涂氟时间
		    		result.put("tufuState", 3);
		    		item.put("result", result);
		    		item.put("msg", "success");
			    	item.put("code", 1);
	    		}else{
	    			item.put("msg", "data is null");
			    	item.put("code", 2);
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    		item.put("msg", "error");
        	item.put("code", -1);
		}
    	return item;
    }
    
    /**
     * 涂氟
     * @param id
     * @return
     */
    @RequestMapping(value="updateHospitalAppoint",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateHospitalAppoint(HttpServletResponse response,
    		@RequestParam("id")Integer id,@RequestParam("time")String time){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	JSONObject item = new JSONObject();
    	try {
    		ApiHospitalAppoint model = new ApiHospitalAppoint();
    		model.setId(id);
    		model.setAppointment_time(time);
    		model.setC3(DateUtil.parseToDefaultDateSecondString(new Date()));
    		int result = projectVolunteerFacade.updateHospitalAppoint(model);
    		if(result>0){
    			item.put("msg", "success");
    	    	item.put("code", 1);
    		}else{
    			item.put("msg", "error");
            	item.put("code", -1);
    		}
	    	
    	} catch (Exception e) {
    		logger.info("NewReleaseProjectController >> updateHospitalAppoint"+e.getMessage());
    		item.put("msg", "error");
        	item.put("code", -1);
		}
    	return item;
    }
    
    /*
	 * 银行卡的绑定
	 * 收款单位/人 accountName  收款账号 cardNo  所属银行bankName  项目编号pid
	 */
	@RequestMapping(value = "/boundbankcard" ,method=RequestMethod.POST)
	@ResponseBody
	public Result<UserCard> BoundBankCard(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
    		return new Result<UserCard>(ResultEnum.NOT_LOGIN);
		}
		if(usercard.getPid()==null){
			return new Result<UserCard>(ResultEnum.NOTBOUND_PROJECTID);
		}
		ApiResult res = new ApiResult();
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setProjectId(usercard.getPid());
		apiUserCard.setIsSelected(0);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(
				apiUserCard, 1, 1);
		List<ApiUserCard> list = mapiPage.getResultData();
		if(list!=null && list.size()>0){//更新
			list.get(0).setAccountName(usercard.getName());
			list.get(0).setCard(usercard.getCardNo());
			list.get(0).setBankName(usercard.getBankName());
			try {
				res = userFacade.updateUserCard(list.get(0));
				if (res != null && res.getCode() == 1) {
					return new Result<UserCard>(ResultEnum.SUCCESS);
				} 
				return new Result<UserCard>(ResultEnum.INNER_ERROR);
			} catch (Exception e) {
				logger.info("NewReleaseProjectController >> boundbankcard"+e.getMessage());
				return new Result<UserCard>(ResultEnum.INNER_ERROR);
			}
		}
		else{//添加
			apiUserCard = new ApiUserCard();
			if (StringUtils.isEmpty(usercard.getBankName())
					|| StringUtils.isEmpty(usercard.getCardNo())
					|| StringUtils.isEmpty(usercard.getName())) {
				return new Result<UserCard>(ResultEnum.PARAMETER_ERROR);
			}
			apiUserCard.setProvince("未知");
			apiUserCard.setCity("未知");
			apiUserCard.setUserId(userId);
			apiUserCard.setBankName(usercard.getBankName());
			apiUserCard.setCard(usercard.getCardNo());
			apiUserCard.setUseState(100);
			apiUserCard.setBankType(usercard.getBankType());
			apiUserCard.setAccountName(usercard.getName());
			apiUserCard.setAccountType(usercard.getAccountType());
			apiUserCard.setProjectId(usercard.getPid());
			try {
				res = userFacade.saveNewUserCard(apiUserCard);
				if (res != null && res.getCode() == 1) {
					return new Result<UserCard>(ResultEnum.SUCCESS);
				} else if (res.getCode() == 10003) {//
					return new Result<UserCard>(ResultEnum.CARD_OVERLIMIT);
				} else if (res.getCode() == 10004) {//
					return new Result<UserCard>(ResultEnum.CARD_BOUNDED);
				} else {
					return new Result<UserCard>(ResultEnum.PARAMETER_ERROR);
				}
			} catch (Exception e) {
				logger.info("NewReleaseProjectController >> boundbankcard"+e.getMessage());
				return new Result<UserCard>(ResultEnum.INNER_ERROR);
			}
		}
	}
	
	/**
	 * 获取项目收款账号信息
	 * @param projectId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getBoundbankcard" ,method=RequestMethod.GET)
	@ResponseBody
	public Result<UserCard> getBoundbankcard(@RequestParam(value="projectId",required=false)Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
    		return new Result<UserCard>(ResultEnum.NOT_LOGIN);
		}
		if (projectId==null) {
			return new Result<UserCard>(ResultEnum.PARAMETER_ERROR);
		}
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setProjectId(projectId);
		apiUserCard.setIsSelected(0);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(
				apiUserCard, 1, 1);
		List<ApiUserCard> list = mapiPage.getResultData();
		if(list.size()>0){
			UserCard card = new UserCard();
			card.setBankName(list.get(0).getBankName());
			card.setName(list.get(0).getAccountName());
			card.setCardNo(list.get(0).getCard());
			return new Result<UserCard>(ResultEnum.SUCCESS,card);
		}
		return new Result<UserCard>(ResultEnum.DATAISNULL);
	}
	
	/**
     * 发起筹款项目接口
     * @param response
     * @param request
     * @param projectId
     * @return
     */
    @RequestMapping(value="faqiDonateProjectDetail",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject faqiDonateProjectDetail(HttpServletResponse response,HttpServletRequest request,
    		@RequestParam("projectId")Integer projectId){
    	response.setHeader("Access-Control-Allow-Orgin", "*");
    	JSONObject item=new JSONObject();
    	JSONObject result=new JSONObject();
    	
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	if(project !=null){
    		List<ApiBFile> b=project.getBfileList();
    		result.put("contentImg", b);
    		result.put("content", project.getContent());
    		result.put("title", project.getTitle());
    		result.put("coverImageId", project.getCoverImageId());
    		result.put("cryMoney", project.getCryMoney());
    		result.put("coverImagerUrl", project.getCoverImageUrl());
    		result.put("state", project.getState());
    		
    		item.put("result", result);
    		item.put("code", 1);
    		item.put("msg", "success");
    	}else{
    		item.put("result", result);
			item.put("code", -1);
			item.put("msg", "项目不存在error");
    	}
    	return item;
    }
    /**
     * 受助人信息接口
     * @param response
     * @param request
     * @param projectId
     * @return
     */
    @RequestMapping(value="needPersonInfo",method =RequestMethod.GET)
    @ResponseBody
    public JSONObject needPersonInfo(HttpServletResponse response,HttpServletRequest request,
    		@RequestParam("projectId")Integer projectId){
    	response.setHeader("Access-Control-Allow-Orgin", "*");
    	JSONObject item=new JSONObject();
    	JSONObject result=new JSONObject();
    	ApiProject project=projectFacade.queryProjectDetail(projectId);
    	if(project !=null){
    		ApiProjectUserInfo apiProjectUserInfo = new ApiProjectUserInfo();
    		apiProjectUserInfo.setProjectId(projectId);
    		apiProjectUserInfo.setPersonType(0);
    		apiProjectUserInfo = projectFacade.queryProjectUserInfo(apiProjectUserInfo);
    		if(apiProjectUserInfo==null){
    			item.put("result", result);
    			item.put("code", 2);
    			item.put("msg", "数据不完善error");
    		}else{
    			//受助人姓名
        		result.put("needName", apiProjectUserInfo.getRealName());
        		//联系人姓名
        		result.put("linkMan", apiProjectUserInfo.getLinkMan());
        		//联系人电话
        		result.put("linkMobile", apiProjectUserInfo.getLinkMobile());
        		item.put("result", result);
        		item.put("code", 1);
        		item.put("msg", "success");
    		}
    	}else{
    		item.put("result", result);
			item.put("code", -1);
			item.put("msg", "项目不存在error");
    	}
    	
    	return item;
    }
}
