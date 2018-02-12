package com.guangde.home.controller.project;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

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
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.project.Appeal;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;

@Controller
@RequestMapping("test")
public class TestController {
	
	Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
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
	
	
    @RequestMapping("personReleaseNav_list")
	public ModelAndView personReleaseNav_list(ModelAndView view,@RequestParam(value="type",required=true) String type,String id){
		view=new ModelAndView("h5/releaseProject/personReleaseNav_list");
		view.addObject("type",type);
		//根据该id回显认证的资料
		view.addObject("zlId", id);
		//个人发起求助，查询图片信息
		if(type=="0"){
			
		}
		//type为机构发起求助，查询图片信息
		else if(type=="1"){
			
		}
		//根据id去查询图片然后回显
		return view;
	}
    
    @RequestMapping("typeConfigList")
    public ModelAndView queryTypeConfigList(@RequestParam(value="helpType",required=false)String helpType,
    		@RequestParam(value="zlId" ,required=false)String zlId,@RequestParam(value="type" ,required=false)String type	 
    		){
    	ModelAndView mv=new ModelAndView(); 
    	List<ApiTypeConfig> listTypeConfig= ICommonFacade.queryList();
    	
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
    	mv.addObject("helpType",helpType);
    	mv.addObject("type",type);
		//根据该id回显认证的资料
		mv.addObject("zlId", zlId);
    	mv.addObject("listTypeConfig",list);
    	mv.setViewName("h5/releaseProject/personReleaseNav");	
    	return mv; 	
    }
    
    @RequestMapping("personAided")
    public  ModelAndView personAided(@RequestParam(value="zlId",required=false)String zlId,
    		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
//    		 @RequestParam(value="projectId", required=true) String projectId,
    		 @RequestParam(value="type", required=false) String type,
    		 HttpServletRequest request, HttpServletResponse response 
    		){
    	ModelAndView mv=new ModelAndView();
        Integer userId=UserUtil.getUserId(request, response);
        if(userId==null){
        	mv.addObject("message",1);
        }else {
        	ApiFrontUser user=userFacade.queryById(userId);
        	mv.addObject("user", user);
        	mv.addObject("zlId",zlId);
        	mv.addObject("helpType",helpType);
        	mv.addObject("typeName_e", typeName_e);
        	mv.addObject("type", type);
		}     
    	mv.setViewName("h5/releaseProject/personaAided_info");
    	return mv;
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
    	mv.setViewName("h5/releaseProject/personReleaseList");
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
     * 
     * 为群体发布项目跳转
     */
    @RequestMapping("publicReleaseList")
    public  ModelAndView publicReleaseList(@RequestParam(value="type",required=false)String type,
   		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
   		 @RequestParam(value="zlId" ,required=false)String zlId){
    	ModelAndView mv=new ModelAndView();
    	mv.setViewName("h5/releaseProject/publicReleaseList");
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	//根据typeName_e去查询求助领域
    	ApiTypeConfig ag=new ApiTypeConfig();
    	ag.setTypeName_e(typeName_e);
    	ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
    	if(atc!=null){
    		mv.addObject("atc", atc);
    	}
    	
    	
    	return mv;
    }
    
    @RequestMapping("familyAided")
    public  ModelAndView familyAided(@RequestParam(value="type",required=false)String type,
      		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
      		 //@RequestParam(value="projectId", required=true) String projectId,
       		 @RequestParam(value="zlId" ,required=false)String zlId
    		){
    	ModelAndView mv=new ModelAndView();
    	
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	mv.setViewName("h5/releaseProject/familyAided_info");
    	return mv;
    }
    @RequestMapping("othersAided")
    public  ModelAndView othersAided(@RequestParam(value="type",required=false)String type,
      		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
      		//@RequestParam(value="projectId", required=true) String projectId,
       		 @RequestParam(value="zlId" ,required=false)String zlId
    		){
    	ModelAndView mv=new ModelAndView(); 	
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	//mv.addObject("projectId", projectId);
    	mv.setViewName("h5/releaseProject/othersAided_info");
    	return mv;
    }
    
    @RequestMapping("institutePublic")
    public  ModelAndView institutePublic(@RequestParam(value="type",required=false)String type,
      		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
      		//@RequestParam(value="projectId", required=true) String projectId,
       		 @RequestParam(value="zlId" ,required=false)String zlId
    		){
    	ModelAndView mv=new ModelAndView();
    	
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	
    	//mv.addObject("projectId", projectId);
    	mv.setViewName("h5/releaseProject/institutePublicAided_info");
    	return mv;
    }
    
    @RequestMapping("instituteToOne")
    public  ModelAndView instituteToOne(@RequestParam(value="type",required=false)String type,
      		 @RequestParam(value="helpType" ,required=false)String helpType,String typeName_e,
      		
      		 //@RequestParam(value="projectId", required=true) String projectId,
       		 @RequestParam(value="zlId" ,required=false)String zlId
    		){
    	ModelAndView mv=new ModelAndView();
    	mv.addObject("type",type);
    	mv.addObject("helpType",helpType);
    	mv.addObject("typeName_e", typeName_e);
    	mv.addObject("zlId", zlId);
    	mv.setViewName("h5/releaseProject/instituteToOneAided_info");
    	return mv;
    }
    
    
    /*
     * 返回成功页面
     */
    @RequestMapping("success")
    public ModelAndView success(){
    	ModelAndView mv=new ModelAndView("h5/releaseProject/releaseSuccess");
    	
    	return mv;
    }
    
    
    /*
     * 成功之后返回用户的个人中心查看项目
     */
    @RequestMapping("project_initiator")
    public ModelAndView project_initiator(){
    	ModelAndView mv=new ModelAndView("h5/userCenter/project_initiator");
    	
    	return mv;
    }
    
    /*
     * 申请求助项目详情的数据
     */
    @ResponseBody
    @RequestMapping("appealProjectInfoData")
    public Map<String, Object> appealViewSecondData(Appeal appel, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("appel:" + appel.toString() + "userId:" + userId);
        }
        String ids[] =appel.getImgIds().split(",");
        Integer titleImageId=0;
        for(int i=0;i<ids.length;i++){
        	if(ids[i]!=null && !"".equals(ids[i])){
        		titleImageId=Integer.parseInt(ids[i]);
        		break;
        	}
        }
        if (appel.getStatus() != ProjectConstant.PROJECT_STATUS_AUDIT1)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }
        else
        {
            if (appel.getCryMoney() < 200)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能小于200", null);
            }
            if (appel.getCryMoney() > 100000000)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能大于100000000", null);
            }
//        }
        int way = 0;
        ApiProject project = null;
        if (appel.getId() > 0)
        {
        	System.out.println(appel.getId());
            //project = projectFacade.queryProjectDetail(appel.getId());
        	project=projectFacade.queryProjectDetail(appel.getId());
            if (project == null)
            {
                way = 1;
                project = new ApiProject();
                project.setContentImageId(appel.getImgIds());
            }else {
            	if(project.getContentImageId()!=null){
            		project.setContentImageId(project.getContentImageId()+","+appel.getImgIds());
            	}else{
            		project.setContentImageId(appel.getImgIds());
            	}
			}
        }
        else
        {
            way = 1;
            project = new ApiProject();
        }
        project.setId(appel.getId());
        project.setTitle(appel.getTitle());
        project.setState(appel.getStatus());
        project.setContent(appel.getContent()); 
        project.setIdentity("未知");
        project.setPayMethod("alipay");
        project.setCryMoney(appel.getCryMoney());
        project.setDetailAddress(appel.getDetailAddress());
        project.setLocation(appel.getAppealAddress());
        project.setIssueTime(new Date());
        //设置为标题图
        project.setCoverImageId(titleImageId);
        if(appel.getDeadline()>365){
        	return webUtil.resMsg(0, "0008", "募捐时间不能超过一年", null);
        }
        Long dl=(long) (new Date().getTime()+appel.getDeadline()*24*3600*1000);
        appel.setDeadline(dl);
        if (appel.getDeadline() != 0)
        {
            project.setDeadline(new Date(appel.getDeadline()));
        }
        if (way== 0)
        {
            if (StringUtils.isNotEmpty(appel.getField()))
            {
                ApiTypeConfig ag = new ApiTypeConfig();
                ag.setTypeName_e(appel.getField());
                String key = PengPengConstants.SEEKHELP_FIELD_INFO + "_" + appel.getField();
                ag.initNormalCache(true, DateUtil.DURATION_WEEK_S, key);
                ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
                if (atc == null)
                {
                    return webUtil.resMsg(0, "0003", "没有这个领域", null);
                }
                project.setField(appel.getField());
                project.setFieldChinese(atc.getTypeName());
            }
            else
            {
                return webUtil.resMsg(0, "0003", "没有上传领域的参数值", null);
            }
        }
        project.setUserId(userId);
        project.setLastUpdateTime(new Date());
        
        project.setIsNeedVolunteer(appel.getIsNeedVolunteer());
        project.setIsCanHelp(0);
        ApiResult result = null;
        if (way == 0)
        {
            result = projectFacade.updateProject(project);
        }
        else
        {
            result = projectFacade.launchProject(project);
        }
        if (result.getCode() == 1)
        {
            //当项目发布成功时，设置标题图(默认为第一张)
        	
        	
        	
            JSONObject js = new JSONObject();
            if (way == 0)
            {
                js.put("projectId", project.getId());
            }
            else
            {
                js.put("projectId", result.getMessage());
            }
            
            js.put("help", appel.getIdentity());
       
            return webUtil.resMsg(1, "0000", "成功", js);
        }
        else
        {
            return webUtil.resMsg(0, "0003", "添加失败", null);
        }
        }
    }

    /*
     * 申请求助项目人员项目的数据
     */
	@ResponseBody
    @RequestMapping("appealUserInfoData")
    public Map<String, Object> appealViewThreeData(@RequestParam(value="type" ,required=true)String type, 
    		@RequestParam(value="zlId" ,required=true)String zlId,
    		@RequestParam(value="helpType" ,required=true)String helpType,
    		Appeal appel, HttpServletRequest request, HttpServletResponse response)
    {
    	
        Integer userId = UserUtil.getUserId(request, response);
        ApiFrontUser user=null;
        String workUnit="";
        String projectType="";
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }else{
        	//发布人信息为登录人员信息
        	if("0".equals(type)){
	            user = userFacade.queryById(userId);
	            appel.setAddress(user.getFamilyAddress());
	            appel.setPhone(user.getMobileNum());
	            appel.setName(user.getRealName());
	            appel.setIdcard(user.getIdCard());
	            appel.setWeixin(user.getQqOrWx());
	            appel.setGzdw(user.getWorkUnit());
	            appel.setZy(user.getPersition());
	            projectType="personalItems";
	            //当为本人求助的时候，获取前面验证的的图片
	            if("1".equals(helpType)){
	            	appel.setRelation("本人");

	            	if(user.getContentImageId()!=null&&!"".equals(user.getContentImageId())){
	            		appel.setImgIds(user.getContentImageId()+","+appel.getImgIds());
	            	}
	            }
        	}else {
        		ApiCompany ac=new ApiCompany();
                ac.setId(Integer.parseInt(zlId));
                ac.setUserId(userId);
             	ApiCompany company=companyFacade.queryCompanyByParam(ac);
             	if(company==null){
             		return webUtil.resMsg(0, "0005", "无此机构信息", null);
             	}
             	appel.setName(company.getName());
             	if(company.getName()!=null){
             		workUnit=company.getName();
             	}
             	appel.setIdcard(company.getGroupCode());
             	appel.setAddress(company.getAddress());
             	appel.setPhone(company.getMobile());
             	projectType="enterpriseProject";
             	if(company.getContentImageId()!=null && !"".equals(company.getContentImageId())){
             		appel.setImgIds(company.getContentImageId()+","+appel.getImgIds());
             	}
			}
            
            
        }
        
        
        if (logger.isDebugEnabled())
        {
            logger.debug("appel:" + appel.toString() + "userId:" + userId);
        }
        
        //ApiProject project = projectFacade.queryProjectDetail(appel.getId());
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
            api.setHelpType(Integer.parseInt(helpType));
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
            api.setHelpType(Integer.parseInt(helpType));
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
            api.setHelpType(Integer.parseInt(helpType));
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
            // todo projectid
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
    
    
    private Double Double(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     *  机构为群体发布项目
     *  
     */
    @ResponseBody
    @RequestMapping("InstitutionReleaseProject")
    public Map<String, Object> InstitutionReleaseProject(@RequestParam(value="helpType",required=true)String helpType,Appeal appel, 
    		@RequestParam(value="zlId",required=true)String zlId,
    		HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        Integer titleImageId=0;
        //默认设置第一张为标题图
        if(appel.getImgIds()!=null){
        	String ids[] =appel.getImgIds().split(",");
            for(int i=0;i<ids.length;i++){
            	if(ids[i]!=null && !"".equals(ids[i])){
            		titleImageId=Integer.parseInt(ids[i]);
            		break;
            	}
            }
        }
        
        if("5".equals(helpType)){
        	//根据id去查机构的额信息
            ApiCompany ac=new ApiCompany();
            ac.setId(Integer.parseInt(zlId));
            ac.setUserId(userId);
        	ApiCompany company=companyFacade.queryCompanyByParam(ac);
        	if(company==null){
         		return webUtil.resMsg(0, "0005", "无此机构信息", null);
         	}
        	appel.setName(company.getName());
        	appel.setIdcard(company.getGroupCode());
        	appel.setAddress(company.getAddress());
        	appel.setPhone(company.getMobile());
        	if(company.getContentImageId()!=null && !"".equals(company.getContentImageId())){
         		appel.setImgIds(company.getContentImageId()+","+appel.getImgIds());
         	}
        }else{
        	return webUtil.resMsg(0, "0005", "未群体发布失败", null);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("appel:" + appel.toString() + "userId:" + userId);
        }
        if (appel.getStatus() != ProjectConstant.PROJECT_STATUS_AUDIT1)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }
        else
        {
            if (appel.getCryMoney() < 200)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能小于200", null);
            }
            if (appel.getCryMoney() > 100000000)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能大于100000000", null);
            }
//        }
        int way = 0;
        ApiProject project = null;
        if (appel.getId() > 0)
        {
            project = projectFacade.queryProjectDetail(appel.getId());
            if (project == null)
            {
                way = 1;
                project = new ApiProject();
            }
        }
        else
        {
            way = 1;
            project = new ApiProject();
        }
        
        project.setTitle(appel.getTitle());
        project.setState(appel.getStatus());
        project.setContent(appel.getContent());
        project.setContentImageId(appel.getImgIds());
        project.setLocation("未知");
        project.setDetailAddress("未知");
        project.setIdentity("未知");
        project.setPayMethod("alipay");
        project.setCryMoney(appel.getCryMoney());
        project.setIssueTime(new Date());
        project.setCoverImageId(titleImageId);
        if(appel.getDeadline()>365){
        	return webUtil.resMsg(0, "0008", "募捐时间不能超过一年", null);
        }
        Long dl=(long) (new Date().getTime()+appel.getDeadline()*24*3600*1000);
        appel.setDeadline(dl);
        if (appel.getDeadline() != 0)
        {
            project.setDeadline(new Date(appel.getDeadline()));
        }
        if (way != 0)
        {
            if (StringUtils.isNotEmpty(appel.getField()))
            {
                ApiTypeConfig ag = new ApiTypeConfig();
                ag.setTypeName_e(appel.getField());
                String key = PengPengConstants.SEEKHELP_FIELD_INFO + "_" + appel.getField();
                ag.initNormalCache(true, DateUtil.DURATION_WEEK_S, key);
                ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
                if (atc == null)
                {
                    return webUtil.resMsg(0, "0003", "没有这个领域", null);
                }
                project.setField(appel.getField());
                project.setFieldChinese(atc.getTypeName());
            }
            else
            {
                return webUtil.resMsg(0, "0003", "没有上传领域的参数值", null);
            }
        }
        project.setType("enterpriseProject");
        project.setUserId(userId);
        project.setLastUpdateTime(new Date());
        
        project.setIsNeedVolunteer(appel.getIsNeedVolunteer());
        project.setIsCanHelp(0);
        ApiResult result = null;
        if (way == 0)
        {
            result = projectFacade.updateProject(project);
        }
        else
        {
            result = projectFacade.launchProject(project);
        }
        if (result.getCode() == 1)
        {
            JSONObject js = new JSONObject();
            if (way == 0)
            {
                js.put("projectId", project.getId());
            }
            else
            {
                js.put("projectId", result.getMessage());
            }
            //当项目添加成功之后、插入发起人的信息
            if(result.getMessage()!=null && result.getCode()==1){
            	appel.setId(Integer.parseInt(result.getMessage()));
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
                     api.setHelpType(Integer.parseInt(helpType));
                     api.setCreateTime(new Date());
                     aList.add(api);
                     // 发布人
                     api = new ApiProjectUserInfo();
                     api.setProjectId(appel.getId());
                     api.setRelation(appel.getRelation());
                     if(appel.getName()!=null){
                     	api.setRealName(webUtil.encodeTodecode(appel.getName()));
                     }if(appel.getName()!=null){
                    	 api.setWorkUnit(webUtil.encodeTodecode(appel.getName()));
                     }
                     //api.setIndetity(appel.getIdcard());
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
                     api.setHelpType(Integer.parseInt(helpType));
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
                     if(appel.getAppealzy()!=null){
                     	api.setVocation(webUtil.encodeTodecode(appel.getAppealzy()));
                     }
                     if(appel.getAppealgzdw()!=null){
                     	api.setWorkUnit(webUtil.encodeTodecode(appel.getAppealgzdw()));
                     }
                     api.setLinkMobile(appel.getAppealPhone());
                     api.setHelpType(Integer.parseInt(helpType));
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
                    // todo projectid
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
                       // return webUtil.resMsg(1, "0000", "成功", appel.getId());
                    	return webUtil.resMsg(1, "0000", "成功", js);
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
            else {
            	return webUtil.resMsg(0, "0007", "参数不对",null);
			}          
        }
        else
        {
            return webUtil.resMsg(0, "0003", "添加失败", null);
        }
        }
    }
    
    /**
     * 编辑项目
     * @return
     */
    @RequestMapping("editPublicRelease")
    public  ModelAndView editPublicRelease(@RequestParam(value="projectId",required=true)Integer projectId){
    	ModelAndView view=new ModelAndView();
    	view.setViewName("h5/userCenter/editPublicRelease");
    	ApiProject apiProject = projectFacade.queryProjectDetail(projectId);
    	Long date = ((long)(apiProject.getDeadline().getTime()-new Date().getTime()))/(1000*3600*24);
    	//System.out.println(date);
    	view.addObject("project", apiProject);
    	view.addObject("donateTimeNum",date);
    	return view;
    }
    
    /**
     * 更新项目
     * @return
     */
    @RequestMapping("updatePublicRelease")
    @ResponseBody
    public Map<String, Object> updatePublicRelease (ApiProject model,
    		@RequestParam(value="donateTimeNum",required=true)Long donateTimeNum,
    		@RequestParam(value="imgIds",required=true)String imgIds){
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
    			apiProject.setState(200);
    			ApiResult result = projectFacade.updateProject(apiProject);
    	    	if(result!=null&& result.getCode()==1)
    	    		return webUtil.resMsg(1, "0000", "修改成功！", null);
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
    	ModelAndView view = new ModelAndView("h5/userCenter/accountNumber");
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
        	view = new ModelAndView("");
            return view;
        }
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        if(project!=null){
        	if(project.getAccountBank()!=null&&project.getAccountBank()!=""
        			&&project.getAccountName()!=null&&project.getAccountName()!=""
        			&&project.getCollectNum()!=null&&project.getCollectNum()!=""){//已填过收款信息
        		return view;
        	}
        	else{//未填过收款信息或不全
        		return view;
        	}
        }
    	return view;
    }
    
    /**
     * 跳转到协议页面
     * @param view
     * @return
     */
    @RequestMapping("gotoProtocolRelease")
    public ModelAndView gotoProtocolRelease(HttpServletRequest request,
			HttpServletResponse response,ModelAndView view){
    	//===========微信用户自动登陆start==============//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user1 = new ApiFrontUser();//捐款用户
		Integer userId = UserUtil.getUserId(request, response);
        if(userId != null && userId != 0)
        {
     	  user1 = userFacade.queryById(userId);
     	  view.addObject("user", user1);
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
	   				//openId = "oxmc3uOwjzQxu3u18E37e-n1ytwU"; 
	   				//OToken = "test" ;
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
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
        			logger.error("微信登录出现问题"+ e);
        		}
        		view.addObject("payway", browser);
        		
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
        			view.addObject("user", user1);
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        		
        	}
        	else{
        		view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			view.addObject("flag", "protocolRelease");
        		return view;
        	}
        	
        }
		//===========微信用户自动登陆end==============//
    	view.setViewName("h5/releaseProject/protocolRelease");
    	return view;
    }
}
