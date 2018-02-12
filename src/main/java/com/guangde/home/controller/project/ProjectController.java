package com.guangde.home.controller.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.*;
import com.guangde.entry.*;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.*;
import com.guangde.home.vo.common.HomeFile;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.home.vo.project.*;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("project")
public class ProjectController
{
    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
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
    
    @RequestMapping("index")
    public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "typeName", required = false) String typeName
			,@RequestParam(value="tagName",required=false)String tagName
			,@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople
			,@RequestParam(value = "province", required = false) String province
			,@RequestParam(value = "keyWords", required = false) String keyWords)
    {
        ModelAndView view = new ModelAndView("dogood/dogood_list");
        List<ApiTypeConfig> atc = commonFacade.queryList();
        ApiConfig config = new ApiConfig();
        config.setConfigKey("projectTag");
        List<ApiConfig> list = commonFacade.queryList(config);
        if(list != null && list.size() > 0 ) {
        	view.addObject("tag", list.get(0).getConfigValue());
        }
        if(province!=null&&!"".equals(province)){//过滤
        	province=RepairVulnerabilityUtil.HTMLEncode(province);
        	province=RepairVulnerabilityUtil.StringFilter(province);
        	System.out.println("province>>>>"+province);
        }
        if(keyWords!=null&&!"".equals(keyWords)){//过滤
        	keyWords=RepairVulnerabilityUtil.HTMLEncode(keyWords);
        	keyWords=RepairVulnerabilityUtil.StringFilter(keyWords);
        	System.out.println("keyWords>>>>"+keyWords);
        }
        if(typeName!=null&&!"".equals(typeName)){//过滤
        	typeName=RepairVulnerabilityUtil.HTMLEncode(typeName);
        	typeName=RepairVulnerabilityUtil.StringFilter(typeName);
        	System.out.println("typeName>>>>"+typeName);
        }
        view.addObject("atc", atc);
        view.addObject("tagName", tagName);
        view.addObject("typeName",typeName);
        view.addObject("province",province);
        view.addObject("keyWords",keyWords);
        view.addObject("userId", UserUtil.getUserId(request, response));
        view.addObject("extensionPeople", extensionPeople);//推广人id
        return view;
    }
    /*
    @RequestMapping("indexNew")
    public ModelAndView indexNew()
    {
        ModelAndView view = new ModelAndView("dogood/dogood_list");
        List<ApiTypeConfig> atc = commonFacade.queryList();
        view.addObject("atc", atc);
        return view;
    }
    */
    @RequestMapping("joinGood")
    public ModelAndView joinGood()
    {
        ModelAndView view = new ModelAndView("dogood/joinGood");
        return view;
    }
    
    @RequestMapping("create")
    public ModelAndView create()
    {
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_index");
        return view;
    }
    
    /*
     * 返回项目
     * 
     * @param type 类型
     * 
     * @return 推荐项目json数据
     */
    @ResponseBody
    @RequestMapping("list")
    public JSONObject list(ProjectForm from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        // 把取到的善园项目封装成json数据
        try
        {
            ApiProject ap = new ApiProject();
            ap.setOrderDirection("desc");
            ap.setOrderBy("lastUpdateTime");
            ap.setIsHide(0);
            if (StringUtils.isNotEmpty(from.getTypeName()))
            {
                ap.setField(from.getTypeName());
            }
            if (from.getStatus() == 1)
            {
                ap.setState(ProjectConstant.PROJECT_STATUS_EXECUTE);
            }
            else if (from.getStatus() == 2)
            {
                ap.setState(ProjectConstant.PROJECT_STATUS_DONE);
            }
            else
            {
                ap.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
            }
            List<String> list = new ArrayList<String>(1);
            list.add(ApiProject.getCacheRange(ap.getClass().getName(), BaseBean.RANGE_WHOLE, ap.getField()));
            ap.initCache(false, DateUtil.DURATION_MIN_S, list, "field", "status");
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
                    item.put("type", project.getType());
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    item.put("donaAmount", project.getDonatAmount());
                    item.put("cryMoney", project.getCryMoney());
                    item.put("imageurl", project.getCoverImageUrl());
                    item.put("donationNum", project.getDonationNum());
                    item.put("endtime", new Date());
                    item.put("state", project.getState());
                    item.put("isOverMoney", project.getIsOverMoney());
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
     * 新项目列表
     * @param from
     * @return
     */
    /**
     * @param from
     * @return
     */
    @ResponseBody
    @RequestMapping("list_new")
    public JSONObject list2(ProjectForm from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        // 把取到的善园项目封装成json数据
        try
        {

            ApiProject ap = new ApiProject();
            if(!"0".equals(from.getLocation())&&!StringUtils.isEmpty(from.getLocation())){
            	ap.setLocation(from.getLocation());
            }
            if(!"0".equals(from.getTag())&&!StringUtils.isEmpty(from.getTag())) {
            	ap.setTag(from.getTag());
            }
            ap.setIsHide(0);
            //排序的处理
            if(from.getSortType() == null || from.getSortType() == 0)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastUpdateTime");
            }
            else if(from.getSortType() == 2)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donationNum");
            }
            else if(from.getSortType() == 3)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastFeedbackTime");
            }
            else if(from.getSortType() == 1)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("donatAmount");
            }
            else if(from.getSortType() == 5)
            {
            	ap.setOrderDirection("asc");
            	ap.setOrderBy("donatAmount");
            }
            else if(from.getSortType() == 6)
            {
            	ap.setOrderDirection("asc");
            	ap.setOrderBy("donationNum");
            }
            else if(from.getSortType() == 7)
            {
            	ap.setOrderDirection("desc");
            	ap.setOrderBy("lastFeedbackTime");
            }
            //类别的处理
            if (StringUtils.isNotEmpty(from.getTypeName())&&!"0".equals(from.getTypeName()))
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
            	states.add(ProjectConstant.PROJECT_STATUS_DONE);
            	ap.setStates(states);
            }
            
            if(from.getKeyWords() != null&&!"".equals(from.getKeyWords()))
            {
            	ap.setKeyword(from.getKeyWords());
            }
        	/*
            List<String> list = new ArrayList<String>(1);
            list.add(ApiProject.getCacheRange(ap.getClass().getName(), BaseBean.RANGE_WHOLE, ap.getField()));
            ap.initCache(false, DateUtil.DURATION_MIN_S, list, "field", "status");
            */
            ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, from.getPage(), from.getLen());
            
            List<ApiProject> projects = apiPage.getResultData();
            // 无数据
            if (projects.size() == 0)
            {
                data.put("result", 1);
            }
            else
            {
               	String donatePercent = "" ; 
               	double leaveCryAmount = 0.0 ;
               	double process = 0.0;
                for (ApiProject project : projects)
                {
                    if(project.getIsOverMoney() != null && project.getIsOverMoney() == 1){
                        leaveCryAmount = project.getOverMoney().doubleValue() - project.getDonatAmount();
                    }else {
                        leaveCryAmount = project.getCryMoney()-project.getDonatAmount();
                    }

                	leaveCryAmount = new BigDecimal(leaveCryAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
               		if(leaveCryAmount<0){
               			leaveCryAmount = 0.0 ;
               		}
               		 process = 0.0;
                        if (project.getCryMoney() >= 0.001)
                        {
                            process = project.getDonatAmount() / project.getCryMoney();
                        }
                        donatePercent = process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0);
                        
                    JSONObject item = new JSONObject();
                    item.put("itemId", project.getId());
                    item.put("type", project.getType());
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    item.put("donatAmount", project.getDonatAmount());
                    item.put("cryMoney", project.getCryMoney());
                    item.put("imageUrl", project.getCoverImageUrl());
                    item.put("donationNum", project.getDonationNum());
                    item.put("sex", project.getSex());
                    item.put("age", project.getAge());
                    item.put("realName", project.getRealName());
                    item.put("familyAddress", project.getFamilyAddress());
                    item.put("publishTime", project.getIssueTime());
                    item.put("state", project.getState());
                    item.put("leaveCryAmount", leaveCryAmount);
                    item.put("donatePercent", donatePercent);
                    item.put("publicName", project.getUname());
                    item.put("userType", project.getUserType());
                    item.put("workUnit", project.getWorkUnit());
                    item.put("isNeedVolunteer", project.getIsNeedVolunteer());
                    item.put("special_fund_id", project.getSpecial_fund_id());
                    item.put("isOverMoney", project.getIsOverMoney());
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
    
    
    /*
     * @param projectId 项目ID 显示要编辑的项目
     */
    @RequestMapping("viewedit")
    public ModelAndView viewedit(@RequestParam(value = "projectId") String projectId)
    {
        ModelAndView view = new ModelAndView("index");
        //		 from.setFocusList(homePageFacade.queryFocusMap());
        return view;
    }
    
    /*
     * @param projectForm 项目详情from 保存编辑完的项目
     */
    @RequestMapping("edit")
    public ModelAndView edit(ProjectForm projectForm)
    {
        ModelAndView view = new ModelAndView("index");
        // from.setFocusList(homePageFacade.queryFocusMap());
        return view;
    }
    
    /*
     * @param projectId 求助人修改项目接口
     */
    @RequestMapping("updateproject")
    @ResponseBody
    public Map<String, Object> updateproject(@ModelAttribute Appeal appel, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        if (appel.getStatus() != ProjectConstant.PROJECT_STATUS_DRAFT && appel.getStatus() != ProjectConstant.PROJECT_STATUS_AUDIT1)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }
        if (appel.getStatus() == ProjectConstant.PROJECT_STATUS_AUDIT1)
        {
            if (appel.getCryMoney() < 1000)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能小于1000", null);
            }
            if (appel.getCryMoney() > 100000000)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能大于100000000", null);
            }
        }
        ApiProject ap = new ApiProject();
        ap.setId(appel.getId());
        ap.setUserId(userId);
        ApiPage<ApiProject> result = projectFacade.queryProjectList(ap, 1, 1);
        if (result == null || result.getTotal() == 0)
        {
            return webUtil.resMsg(0, "0003", "没有对应项目", null);
        }
        ApiProject project = result.getResultData().get(0);
        if (project.getState() != ProjectConstant.PROJECT_STATUS_DRAFT)
        {
            return webUtil.resMsg(0, "0004", "非草稿状态不能修改", null);
        }
        if (!project.getUserId().equals(userId))
        {
            logger.error("非法修改项目信息：userId" + userId);
            return webUtil.resMsg(0, "0005", "非本人不能修改", null);
        }
        // ApiProject project = new ApiProject();
        project.setTitle(appel.getTitle());
        project.setState(appel.getStatus());
        project.setContent(appel.getContent());
        project.setContentImageId(appel.getImgIds());
        project.setLocation(appel.getLocation());
        project.setDetailAddress(appel.getDetailAddress());
        project.setCryMoney(appel.getCryMoney());
        if ("otherCaller".equals(appel.getIdentity()))
        {
            project.setIdentity(appel.getIdentity() + "," + appel.getIdentityInfo());
        }
        else
        {
            project.setIdentity(appel.getIdentity());
        }
        if (appel.getDeadline() != 0)
        {
            project.setDeadline(new Date(appel.getDeadline()));
        }
        project.setField(appel.getField());
        project.setAccountBank(appel.getAccountBank());
        project.setAccountName(appel.getAccountName());
        project.setCollectNum(appel.getCollectNum());
        ApiResult result2 = projectFacade.updateProject(project);
        if (result2.getCode() == 1)
        {
            // todo projectid
            return webUtil.resMsg(1, "0000", "成功", null);
        }
        else
        {
            return webUtil.resMsg(0, "0006", "添加失败", null);
        }
    }
    
    /*
     * @param projectId 求助人删除项目接口
     */
    @RequestMapping("delproject")
    @ResponseBody
    public Map<String, Object> delproject(@RequestParam(value = "pid") Integer projectId, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        ApiProject ap = new ApiProject();
        ap.setId(projectId);
        ap.setUserId(userId);
        ApiPage<ApiProject> result = projectFacade.queryProjectList(ap, 1, 1);
        if (result == null || result.getTotal() == 0)
        {
            return webUtil.resMsg(0, "0003", "没有对应项目", null);
        }
        ApiProject project = result.getResultData().get(0);
        if (project.getState() != 200 && project.getState() != 230)
        {
            return webUtil.resMsg(0, "0004", "未发布项目才能删除", null);
        }
        if (!project.getUserId().equals(userId))
        {
            logger.error("非法删除项目信息：userId" + userId + "projectId:" + projectId);
            return webUtil.resMsg(0, "0005", "非本人不能删除", null);
        }
        ApiResult result2 = projectFacade.deleteProject(projectId);
        if (result2.getCode() == 1)
        {
            return webUtil.resMsg(1, "0000", "成功", null);
        }
        else
        {
            return webUtil.resMsg(0, "0006", "删除失败", null);
        }
    }
    
    /*
     * @param projectId 求助人提交项目接口
     */
    @RequestMapping("submit")
    @ResponseBody
    public Map<String, Object> submitProject(@RequestParam(value = "pid") Integer projectId, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        ApiProject ap = new ApiProject();
        ap.setId(projectId);
        ap.setUserId(userId);
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        if (project == null)
        {
            return webUtil.resMsg(0, "0003", "没有对应项目", null);
        }
        String msg = ProjectUtil.verifyProject(project);
        if (!StringUtils.isBlank(msg))
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, msg, null);
        }
        if (project.getState() != 200)
        {
            return webUtil.resMsg(0, "0004", "非草稿状态不能提交", null);
        }
        if (!project.getUserId().equals(userId))
        {
            logger.error("非法提交项目信息：userId" + userId);
            return webUtil.resMsg(0, "0005", "非本人不能提交", null);
        }
        
        project = new ApiProject();
        project.setId(projectId);
        project.setUserId(userId);
        project.setState(ProjectConstant.PROJECT_STATUS_AUDIT1);
        ApiResult result2 = projectFacade.updateProject(project);
        if (result2.getCode() == 1)
        {
            return webUtil.resMsg(1, "0000", "成功", null);
        }
        else
        {
            return webUtil.resMsg(0, "0006", "更改失败", null);
        }
    }
    
    /*
     * @param projectId 项目预览接口
     */
    @RequestMapping("spview")
    @ResponseBody
    public Map<String, Object> spview(@RequestParam(value = "pid") Integer projectId, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
        }
        if (projectId == null)
            return webUtil.resMsg(0, "0002", "参数不能为空", null);
        ApiProject p = projectFacade.queryProjectDetail(projectId);
        if (p == null)
        {
            return webUtil.resMsg(0, "0003", "没有对应项目", null);
        }
        Appeal appel = new Appeal();
        appel.setId(p.getId());
        appel.setTitle(p.getTitle());
        appel.setContent(p.getContent());
        List<ApiBFile> bfiles = p.getBfileList();
        if (bfiles != null && bfiles.size() > 0)
        {
            List<HomeFile> imgs = new ArrayList<HomeFile>(bfiles.size());
            HomeFile img = null;
            for (ApiBFile b : bfiles)
            {
                img = new HomeFile();
                img.setId(b.getId());
                img.setUrl(b.getUrl());
                imgs.add(img);
            }
            appel.setImgs(imgs);
        }
        //appel.setImgs();
        return webUtil.resMsg(1, "0000", "成功", appel);
    }
    
    /*
     * @param projectId 项目ID 显示项目详情
     */
    @RequestMapping("view")
    public ModelAndView view(@RequestParam(value = "projectId") Integer projectId,
    		@RequestParam(value = "itemType", required = false, defaultValue = "") Integer itemType,
    		@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople,
    		HttpServletRequest request, HttpServletResponse response)
    {
    	ModelAndView viewback = new ModelAndView("redirect:/project/view_h5.do?projectId=" + projectId);
    	// 项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
       
        String isMobile = (String)request.getSession().getAttribute("ua");
        if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople!=null){//h5专项基金页面
        	viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if("mobile".equals(isMobile)&&project.getSpecial_fund_id()!=null&&project.getSpecial_fund_id()!=0&&extensionPeople==null){//h5专项基金页面
        	viewback.setViewName("redirect:/uCenterProject/specialProject_details.do?projectId=" + projectId);
            return viewback;
        }
        if ("mobile".equals(isMobile) && extensionPeople!=null)
        {
            viewback.setViewName("redirect:/project/view_h5.do?projectId=" + projectId+"&extensionPeople="+extensionPeople);
            return viewback;
        }
        if("mobile".equals(isMobile)){
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
       /* if("mobile".equals(isMobile) && !StringUtils.isBlank(project.getJumbleSale())){//义卖
        	if(extensionPeople == null){
        		viewback.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId);
        	}else{
        		viewback.setViewName("redirect:/newReleaseProject/project_detail.do?projectId=" + projectId + "&extensionPeople="+extensionPeople);
        	}
        	return viewback;
	    	
        }*/
        
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
        	view.addObject("matchDonate", matchDonates);
        }
        view.addObject("project", project);
        double process = 0.0;
        Integer userId = UserUtil.getUserId(request, response);
        if (project != null)
        {
        	//捐款笔数
        	ApiDonateRecord donate = new ApiDonateRecord();
        	donate.setState(302);
        	donate.setProjectId(projectId);
        	ApiDonateRecord donate1 = donateRecordFacade.queryCompanyCenter(donate);
        	if(donate1!=null)
        		view.addObject("donate1", donate1);
        	
            process = 0.0;
            if (project.getCryMoney() >= 0.001)
            {
                process = project.getDonatAmount() / project.getCryMoney();
            }
            view.addObject("process", process > 1 ? "100" : StringUtil.doublePercentage(project.getDonatAmount(), project.getCryMoney(), 0));
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
        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, 1, 100);
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
    
    /*
     * 捐赠人的参与项目
     */
    @RequestMapping("donationlist")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId)
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
        // 处理时间
        Date bdate = null;
        Date edate = new Date();
        if (type == 1)
        {
            // 3个月内
            bdate = DateUtil.add(edate, -3 * 30);
        }
        else if (type == 2)
        {
            // 半年
            bdate = DateUtil.add(edate, -6 * 30);
        }
        else if (type == 3)
        {
            // 一年
            bdate = DateUtil.add(edate, -12 * 30);
        }
        else
        {
            // 全部
            bdate = null;
            edate = null;
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        
        ApiDonateRecord r = new ApiDonateRecord();
        r.setQueryStartDate(bdate);
        r.setQueryEndDate(edate);
        r.setUserId(userId);
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
                d.setId(donat.getId());  //捐款id
                d.setPid(donat.getProjectId());  //项目id
                d.setdStatus(donat.getState());
                d.setTitle(donat.getProjectTitle()); //项目名称
                d.setStatus(donat.getPstate());  //项目状态
                d.setField(donat.getField());
                d.setProcess(StringUtil.doublePercentage(donat.getDonatAmountpt(), donat.getCryMoney(), 0));
                if(donat.getDonatType().equals("enterpriseDonation")){
                	d.setdType(1); //助善
                }else{
                	d.setdType(2);//捐款
                }
                
                ApiProjectFeedback projectFeedback = new ApiProjectFeedback();
                projectFeedback.setProjectId(donat.getProjectId());
                Integer  FeedBackAmount= projectFacade.countProjectFeedbackByParam(projectFeedback);
                d.setFeedBackAmount(FeedBackAmount);  //反馈条数
                
                ApiProjectUserInfo projectUserInfo = new ApiProjectUserInfo();
                projectUserInfo.setProjectId(donat.getProjectId());
                List<ApiProjectUserInfo> projectUserInfos = projectFacade.queryProjectUserInfoList(projectUserInfo);
                for(ApiProjectUserInfo userInfo :projectUserInfos){
                	if(userInfo.getPersonType()==0){
                		d.setRecipients(userInfo.getRealName()); // 受助人
                	}
                }
                //如果没有受助人，用发起人代替
                if(d.getRecipients()==null){
                	ApiProject project = projectFacade.queryProjectDetail(donat.getProjectId());
                	if(project.getUserId()==0){
                		d.setRecipients("善基金");
                	}else{
                		ApiFrontUser frontUser = userFacade.queryById(project.getUserId());
                    	d.setRecipients(frontUser.getRealName()); //受助人
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
    
    /*
     * 求助人申请的项目
     */
    @RequestMapping("appeallist")
    @ResponseBody
    public Map<String, Object> appeallist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "20") Integer pageNum,@RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "state", required = false) Integer state)
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
        ap.setUserId(new Integer(uid));
        ap.setClaimUserId(new Integer(uid));
        if(title!=null&&!"".equals(title)){
        	ap.setTitle(title);
        }
        if(state!=null){
        	ap.setState(state);
        }
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
                pp.setIsNeedVolunteer(project.getIsNeedVolunteer());
                if(project.getIssueTime() != null){
                	pp.setcTime(project.getIssueTime().getTime());
                }else {
                	pp.setcTime(project.getRegistrTime().getTime());
				}
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
    
    @RequestMapping("buyproject")
    public ModelAndView buyproject(@ModelAttribute ApiDonateRecord donate, HttpServletRequest request, HttpServletResponse response)
    {
        // 校验参数 userid projectId,copies,money,componyid
        if (logger.isDebugEnabled())
        {
            logger.debug("donate: " + donate.toString());
        }
        ModelAndView view = null;
        int projectId = 0;
        // 1.验证是否登入
        String uid = SSOUtil.verifyAuth(request, response);
        if (donate.getProjectId() == null)
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=-1");
        }
        else
        {
            projectId = donate.getProjectId();
        }
        /*
        if (uid == null)
        {
            view = new ModelAndView("redirect:/user/sso/login.do");
            view.addObject("url", "/project/gardenview.do?projectId=" + projectId);
            return view;
        }
        */
        if (donate.getDonateCopies() == 0)
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=" + projectId);
            view.addObject("errormsg", "参数错误");
            return view;
        }
        if (donate.getDonatAmount() == 0d)
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=" + projectId);
            view.addObject("errormsg", "参数错误");
            return view;
        }
        // 校验copies和money的一致性
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        if (project == null)
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=" + projectId);
            view.addObject("errormsg", "数据错误");
            return view;
        }
        if (donate.getDonateCopies() > project.getLeaveCopies())
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=" + projectId);
            view.addObject("errormsg", "库存不够");
            return view;
        }
        double temp = new BigDecimal(project.getPerMoney() * donate.getDonateCopies()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (temp != donate.getDonatAmount())
        {
            view = new ModelAndView("redirect:/project/newGardenView.do?projectId=" + projectId);
            view.addObject("errormsg", "金额错误");
            return view;
        }
        // 添加code
        view = new ModelAndView("/deposit/pay");
        // String tokenCode = CodeUtil.tokenCode(CodeUtil.getBuyKey(new
        // Integer(uid), projectId),
        // StoreManage.create(StoreManage.STROE_TYPE_SESSION, session));
        // view.addObject("tokenCode",tokenCode);
        
        Integer userId = UserUtil.getUserId(request, response);
        if(userId !=null && userId >0)
        {
        	ApiFrontUser user = userFacade.queryById(userId);
        	view.addObject("balance", user.getBalance());
        	view.addObject("userType", user.getUserType());
        }
        view.addObject("projectId", projectId);
        view.addObject("pName", project.getTitle());
        view.addObject("copies", donate.getDonateCopies());
        view.addObject("amount", donate.getDonatAmount());
        view.addObject("permoney", project.getPerMoney());
        view.addObject("leaveCopies", project.getLeaveCopies());
        if (donate.getExtensionPeople()!=null) {//推广人id
        	view.addObject("extensionPeople", donate.getExtensionPeople());
		}
        
        return view;
    }
    
    /*
     * 项目进度（有审核信息）
     */
    @RequestMapping("pcheck")
    @ResponseBody
    public Map<String, Object> projectCheck(@RequestParam(value = "pid") Integer projectId, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.loginFailedRes(null);
        }
        if (projectId == null)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        ApiProjectSchedule pSchedule = new ApiProjectSchedule();
        pSchedule.setProjectId(projectId);
        pSchedule.setState(230);
        ProjectSchedule p = new ProjectSchedule();
        
        ApiPage<ApiProjectSchedule> result = projectFacade.queryProjectScheduleList(pSchedule, 1, 1);
        if (result != null && result.getTotal() > 0)
        {
            pSchedule = result.getResultData().get(0);
            p.setId(pSchedule.getId());
            p.setPid(pSchedule.getProjectId());
            if (pSchedule.getOperatorTime() != null)
            {
                p.setTime(pSchedule.getOperatorTime().getTime());
            }
            p.setComment(pSchedule.getDescription());
        }
        return webUtil.successRes(p);
    }
    
    /**
     * 项目反馈列表
     * 
     * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "pfeedback")
    @ResponseBody
    public Map<String, Object> pfeedback(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum, @RequestParam(value = "pid") Integer projectId)
    {
        if (projectId == null)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        Page p = new Page();
        p.setPage(page);
        p.setPageNum(pageNum);
        List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
        p.setData(feedbacks);
        
        ApiProjectFeedback feedBack = new ApiProjectFeedback();
        feedBack.setProjectId(projectId);
        feedBack.setAuditState(203);
        
//        List<String> list = new ArrayList<String>(1);
//        list.add(ApiProjectFeedback.getCacheRange(feedBack.getClass().getName(), BaseBean.RANGE_WHOLE, projectId));
//        feedBack.initCache(true, DateUtil.DURATION_WEEK_S, list, "projectId", "auditState");
       
        ApiPage<ApiProjectFeedback> result = projectFacade.queryProjectFeedbackList(feedBack, p.getPage(), p.getPageNum());
        if (result != null)
        {
            PFeedBack tempf = null;
            for (ApiProjectFeedback f : result.getResultData())
            {
                tempf = new PFeedBack();
                tempf.setId(f.getId());
                tempf.setContent(StringUtil.convertToHtml(f.getContent()));
                if (f.getFeedbackTime() != null)
                {
                    tempf.setcTime(f.getFeedbackTime().getTime());
                }
                tempf.setImgs(f.getContentImageUrl());
                tempf.setPid(f.getProjectId());
                tempf.setuName(f.getUserName());
                tempf.setLeaveWordList(f.getLeaveWordList());
                tempf.setUserImageUrl(f.getHeadImageUrl());
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
     * 
     * 关注的项目
     * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "careProjectList")
    @ResponseBody
    public Map<String, Object> careProjectList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum, @RequestParam(value = "userId") Integer userId,@RequestParam(value = "type", required = false, defaultValue = "0") Integer type
        ,@RequestParam(value = "projectId", required = false) Integer projectId)
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
    		// 处理时间
  		Date bdate = null;
  		Date edate = new Date();
  		if (type == 1) {
  			// 3个月内
  			bdate = DateUtil.add(edate, -3 * 30);
  		} else if (type == 2) {
  			// 半年
  			bdate = DateUtil.add(edate, -6 * 30);
  		} else if (type == 3) {
  			// 一年
  			bdate = DateUtil.add(edate, -12 * 30);
  		} else {
  			// 全部
  		}
    	  
          Page p = new Page();
          p.setPage(page);
          p.setPageNum(pageNum);
          List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
          p.setData(feedbacks);
          
          ApiProjectFeedback feedBack = new ApiProjectFeedback();
          feedBack.setFeedbackPeople(userId);
          feedBack.setAuditState(203);
          feedBack.setProjectId(projectId);
          if(type ==1 || type ==2 || type ==3){
        	  feedBack.setQueryStartDate(bdate);
        	  feedBack.setQueryEndDate(edate);
  			}
          
//          List<String> list = new ArrayList<String>(1);
//          list.add(ApiProjectFeedback.getCacheRange(feedBack.getClass().getName(), BaseBean.RANGE_WHOLE, userId));
//          feedBack.initCache(true, DateUtil.DURATION_WEEK_S, list, "projectId", "auditState");
          
          ApiPage<ApiProjectFeedback> result = projectFacade.queryCareProjectFeedbckByCondition(feedBack, p.getPage(), p.getPageNum());
         
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
                  tempf.setField(f.getField());
                  tempf.setUserImageUrl(f.getHeadImageUrl());
                  tempf.setPid(f.getProjectId());
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
     * 
     * 善款跟踪
     * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "projectDonateFollowList")
    @ResponseBody
    public Map<String, Object> followPayMoneyRecordList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum, @RequestParam(value = "userId") Integer userId)
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
    	
    	  
          Page p = new Page();
          p.setPage(page);
          p.setPageNum(pageNum);
          List<ProjectPayMoneyRecord> projectPayMoneyRecords = new ArrayList<ProjectPayMoneyRecord>();
          p.setData(projectPayMoneyRecords);
          
          ApiPayMoneyRecord apiPayMoneyRecord = new ApiPayMoneyRecord();
          apiPayMoneyRecord.setUserId(userId);
          
          
          
          
          ApiPage<ApiPayMoneyRecord> result = payMoneyRecordFacade.queryFollowPayMoneyRecordList(apiPayMoneyRecord, p.getPage(), p.getPageNum());
         
          if (result != null)
          {
        	  ProjectPayMoneyRecord tempf = null;
              for (ApiPayMoneyRecord f : result.getResultData())
              {
                  tempf = new ProjectPayMoneyRecord();
                  tempf.setWithdrawAmount(f.getPanMoney());
                  tempf.setAccount(f.getAccount()==null?"":f.getAccount());
                  tempf.setDonateAmount(f.getDonatAmount());
                  tempf.setWithdrawNum(f.getPanMoneyNum()==null?0:f.getPanMoneyNum());
                  tempf.setTime(f.getPayMoneyTime());
                  tempf.setProjectId(f.getProjectId());
                  tempf.setTitle(f.getProjectTitle());
                  tempf.setField(f.getField());
                  tempf.setRecipientName(f.getRecipientName()==null?"":f.getRecipientName());
                  tempf.setRealName(f.getRealName()==null?"":f.getRealName());
                  tempf.setUserId(f.getUserId());
                  projectPayMoneyRecords.add(tempf);
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
            return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "你无权反馈", null);
        }
        
        ApiProjectFeedback f = new ApiProjectFeedback();
        f.setProjectId(feedBack.getPid());
        f.setContent(feedBack.getContent());
        f.setContentImageId(feedBack.getImgIds());
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
    
    @RequestMapping("addpreport")
    @ResponseBody
    public Map<String, Object> addProjectReport(@ModelAttribute ProjectReport projectReport, HttpServletRequest request, HttpServletResponse response)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            return webUtil.loginFailedRes(null);
        }
        if (projectReport.getPid() == 0)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null);
        }
        //校验当前用户是否有权限审核
        // 		 ApiLoveGroupMent ment = new ApiLoveGroupMent();
        // 		 ment.setProjectId(projectReport.getPid());
        // 		 ment.setUserId(userId);
        // 		 ApiPage<ApiLoveGroupMent> r1 = projectFacade.queryLoveGroupMentList(ment, 1, 1);
        // 		 if(r1==null||r1.getTotal()==0){
        // 			 return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"您没有权限提交报告", null);
        // 		 }
        ApiReport report = new ApiReport();
        report.setContent(projectReport.getContent());
        report.setContentImageId(projectReport.getImgIds());
        report.setProjectId(projectReport.getPid());
        if (projectReport.getType() == 0)
        {
            report.setType(1);
        }
        else
        {
            report.setType(projectReport.getType());
        }
        report.setReportPeople(userId);
        report.setOperatorTime(new Date());
        ApiResult r2 = projectFacade.saveReport(report);
        if (r2.getCode() == 1)
        {
            return webUtil.successRes(null);
        }
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_UPDATEFAILED, "添加失败", null);
        }
    }
    
    /*显示留言的列表信息*/
    @RequestMapping("leaveWordList")
    @ResponseBody
    public Map<String, Object> leaveWordList(@RequestParam(value = "id", required = false, defaultValue = "1") Integer id,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "5") Integer pageNum,
        HttpServletRequest request, HttpServletResponse response)
    {
        if (id == null)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        Page p = new Page();
        ApiLeaveWord lword = new ApiLeaveWord();
        lword.setProjectFeedback_id(id);
        List<String> llist = new ArrayList<String>(1);
        llist.add(ApiLeaveWord.getCacheRange(lword.getClass().getName(), BaseBean.RANGE_WHOLE, id));
        lword.initCache(true, DateUtil.DURATION_WEEK_S, llist, "projectFeedback_id");
        ApiPage<ApiLeaveWord> ap = projectFacade.queryLeaveWordList(lword, page, pageNum);
        if (ap != null)
        {
            List<PFeedBack> pList = new ArrayList<PFeedBack>();
            List<ApiLeaveWord> list = ap.getResultData();
            for (int i = 0; i < list.size(); i++)
            {
                ApiLeaveWord awd = list.get(i);
                PFeedBack pebBack = new PFeedBack();
                pebBack.setContent(awd.getContent());
                pebBack.setcDate(awd.getCreateTime());
                if (awd.getUserId() != null)
                {
                    ApiFrontUser user = userFacade.queryById(awd.getUserId());
                    pebBack.setuName(user.getUserName());
                }
                else
                {
                    pebBack.setuName("游客");
                }
                pList.add(pebBack);
            }
            p.setData(pList);
            p.setPage(ap.getPageNum());//当前页码
            p.setPageNum(pageNum);//每条页数
            p.setNums(ap.getTotal());//总数
            return webUtil.successRes(p);
        }
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null);
        }
    }
    
    /*添加留言*/
    @RequestMapping("addleaveWord")
    @ResponseBody
    public Map<String, Object> addleaveWord(@ModelAttribute PFeedBack feedBack, HttpServletRequest request, HttpServletResponse response)
    {
        if (feedBack.getId() == 0)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        Integer userId = UserUtil.getUserId(request, response);
        ApiLeaveWord lword = new ApiLeaveWord();
        lword.setProjectFeedback_id(feedBack.getId());
        lword.setContent(feedBack.getContent());
        lword.setPid(feedBack.getPid());
        lword.setUserId(userId);
        List<String> list = new ArrayList<String>(1);
        list.add(ApiLeaveWord.getCacheRange(lword.getClass().getName(), BaseBean.RANGE_WHOLE, feedBack.getId()));
        lword.initCache(true, 0, list);
        ApiResult res = projectFacade.saveLeaveWord(lword);
        if (res != null && res.getCode() == 1)
        {
            return webUtil.successRes(1);
        }else if (res.getCode() == 10002)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "评论包含非法字符", null);
        }
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null);
        }
    }
    
    /*
     * 选择求助项目的页面
     */
    @RequestMapping("appealFirest")
    public ModelAndView appealViewFirest(HttpServletRequest request,HttpServletResponse response)
    {
    	Integer userId = UserUtil.getUserId(request, response);
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_1");
        Object obj = redisService.queryObjectData(PengPengConstants.SEEKHELP_FIELD);
        if (obj != null)
        {
            view.addObject("atc", obj);
        }
        else
        {
            List<ApiTypeConfig> atc = commonFacade.queryList();
            redisService.saveObjectData(PengPengConstants.SEEKHELP_FIELD, atc, DateUtil.DURATION_WEEK_S);
            view.addObject("atc", atc);
        }
        view.addObject("userId", userId);
        return view;
    }
    
    /*
     * 选择求助内容的页面
     * @param help 0:我帮别人发布 1：我为自己发布
     * @param type 领域
     */
    @RequestMapping("appealSecond")
    public ModelAndView appealViewSecond(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "help", required = false) String help,
        @RequestParam(value = "projectId", required = false, defaultValue = "-1") int projectId, HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_2");
        if (projectId != -1)
        {
            ApiProject apt = projectFacade.queryProjectDetail(projectId);
            if (apt != null && (apt.getState() != ProjectConstant.PROJECT_STATUS_DONE))
            {
                view.addObject("project", apt);
                if (StringUtils.isNotEmpty(apt.getContentImageId()))
                {
                    String imgs[] = apt.getContentImageId().split(",");
                    int no = 0;
                    List<ApiBFile> list = new ArrayList<ApiBFile>();
                    for (int i = 0; i < imgs.length; i++)
                    {
                    	if (StringUtils.isNotEmpty(imgs[i])) {
                    		no = Integer.valueOf(imgs[i]);
                            ApiBFile abf = fileFacade.queryBFileById(no);
                            list.add(abf);
						}
                    }
                    view.addObject("bflist", list);
                }
                return view;
            }
            else
            {
                view = new ModelAndView("common/error");
                view.addObject("error", "这个项目已完成");
                return view;
            }
        }
        ApiTypeConfig ag = new ApiTypeConfig();
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(help))
        {
            view = new ModelAndView("common/error");
            view.addObject("error", "选择发布的求助出问题了!");
            return view;
        }
        ag.setTypeName_e(type);
        String key = PengPengConstants.SEEKHELP_FIELD_INFO + "_" + type;
        ag.initNormalCache(true, DateUtil.DURATION_WEEK_S, key);
        ApiTypeConfig atc = commonFacade.queryApiTypeConfig(ag);
        if (atc != null)
        {
            view.addObject("model", atc.getModel());
            view.addObject("field", atc.getTypeName_e());
        }
        view.addObject("help", help);
        return view;
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
       /* if (appel.getStatus() != ProjectConstant.PROJECT_STATUS_DRAFT)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }
        else
        {*/
            if (appel.getCryMoney() < 200)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能小于200", null);
            }
            if (appel.getCryMoney() > 100000000)
            {
                return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "求助金额不能大于100000000", null);
            }
       /* }*/
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
        
        if(way != 0){
        	 project.setState(appel.getStatus());
        	 project.setLocation("未知");
             project.setDetailAddress("未知");
             project.setIdentity("未知");
             project.setPayMethod("alipay");
             project.setType("personalItems");
             project.setUserId(userId);
        }
        
        project.setTitle(appel.getTitle());
        project.setContent(appel.getContent());
        project.setCoverImageId(Integer.valueOf(appel.getImgIds()));
        project.setCryMoney(appel.getCryMoney());
        
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
            if (StringUtils.isNotEmpty(appel.getImgIds()))
            {
                List<String> dList = appel.getImgsDetail();
                String[] id = appel.getImgIds().split(",");
                for (int i = 0; i < id.length; i++)
                {
                    ApiBFile apiBFile = new ApiBFile();
                    apiBFile.setId(Integer.valueOf(id[i]));
                    if (!"1".equals(dList.get(i)))
                    {
                        apiBFile.setDescription(dList.get(i));
                    }
                    else
                    {
                        apiBFile.setDescription("");
                    }
                    fileFacade.updateBfile(apiBFile);
                }
            }
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
    
    /*
     * 申请求助项目人员项目的页面
     *  @param help 0:我帮别人发布 1：我为自己发布 
     */
    @RequestMapping("appealThree")
    public ModelAndView appealViewThree(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "help", required = true) String help,
        @RequestParam(value = "projectId", required = true) Integer projectId)
    {
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_3");
        view.addObject("help", help);
        view.addObject("projectId", projectId);
        ApiProject apiProject = projectFacade.queryProjectDetail(projectId);
        String key = PengPengConstants.PROJECT_USERINFO_LIST;
        Integer userId = UserUtil.getUserId(request, response);
        if(userId == null){
        	
        }else{
        if (apiProject != null)
        {
            /*if (apiProject.getState() == 200)
            {*/
                ApiProjectUserInfo ao = new ApiProjectUserInfo();
                ao.setProjectId(projectId);
                key = key + "_" + projectId;
                ao.initNormalCache(true, DateUtil.DURATION_HOUR_S, key);
                List<ApiProjectUserInfo> list = projectFacade.queryProjectUserInfoList(ao);
                //查询用户是否实名，企业是否实名
                ApiAuditStaff apiAuditStaff = new ApiAuditStaff();
                apiAuditStaff.setUserId(userId);
                apiAuditStaff.setPersonType("helpPeople");
                apiAuditStaff.setState(203);
                apiAuditStaff = userFacade.queryAuditStaffByParam(apiAuditStaff);
                if(apiAuditStaff!=null){
                	ApiFrontUser user = userFacade.queryById(userId);
                	view.addObject("user", user);
                	view.addObject("auditStaff_type", 1);
                }
                view.addObject("apiAuditStaff", apiAuditStaff);
                ApiCompany apiCompany = new ApiCompany();
                apiCompany.setUserId(userId);
                apiCompany.setState(203);
                apiCompany = companyFacade.queryCompanyByParam(apiCompany);
                if(apiCompany!=null){
                	view.addObject("company_type", 1);
                }
                view.addObject("apiCompany", apiCompany);
                if (list.size() > 0)
                {
                    for (int i = 0; i < list.size(); i++)
                    {
                        ao = list.get(i);
                        if (ao.getPersonType() == 0)
                        {//受助人
                            view.addObject("szr", ao);
                            //StringBuffer imgsUrl= new StringBuffer();
                            String[] imgsIds= apiProject.getContentImageId().split(",");
                            if(ao.getHelpType()==7 && apiAuditStaff==null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//6,7,8,4,5,9
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId6", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl6", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl6", "");
	                              		}
									}else if(j==1){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId7", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl7", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl7", "");
	                              		}
									}else if(j==2){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId8", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl8", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl8", "");
	                              		}
									}else if(j==3){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId4", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl4", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl4", "");
	                              		}
									}else if(j==4){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId5", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl5", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl5", "");
	                              		}
									}else if(j==5){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId9", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl9", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl9", "");
	                              		}
									}
								}
                            }else if(ao.getHelpType()==9 && apiCompany==null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//3,4,5,9,10
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId3", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl3", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl3", "");
	                              		}
									}else if(j==1){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId4", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl4", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl4", "");
	                              		}
									}else if(j==2){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId5", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl5", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl5", "");
	                              		}
									}else if(j==3){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId9", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl9", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl9", "");
	                              		}
									}else if(j==4){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId10", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl10", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl10", "");
	                              		}
									}
								}
                            }else if(ao.getHelpType()==10 && apiCompany==null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//3,10
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId3", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl3", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl3", "");
	                              		}
									}else if(j==1){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId10", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl10", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl10", "");
	                              		}
									}
								}
                            }
                            if(ao.getHelpType()==7 && apiAuditStaff!=null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//4,5,9
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId4", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl4", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl4", "");
	                              		}
									}else if(j==1){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId5", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl5", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl5", "");
	                              		}
									}else if(j==2){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId9", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl9", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl9", "");
	                              		}
									}
								}
                            }else if(ao.getHelpType()==9 && apiCompany!=null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//4,5,9,10
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId4", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl4", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl4", "");
	                              		}
									}else if(j==1){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId5", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl5", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl5", "");
	                              		}
									}else if(j==2){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId9", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl9", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl9", "");
	                              		}
									}else if(j==3){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId10", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl10", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl10", "");
	                              		}
									}
								}
                            }else if(ao.getHelpType()==10 && apiCompany!=null && apiProject!=null && apiProject.getContentImageId()!=null && !"".equals(apiProject.getContentImageId())){//10
                            	for (int j = 0; j < imgsIds.length; j++) {
									if(j==0){
										ApiBFile file = fileFacade.queryBFileById(Integer.valueOf(imgsIds[j]));
	                              		view.addObject("imgId10", imgsIds[j]);
	                              		if(file!=null){
	                              			view.addObject("imgUrl10", file.getUrl());
	                              		}else{
	                              			view.addObject("imgUrl10", "");
	                              		}
									}
								}
                            }
                        }
                        else if (ao.getPersonType() == 1)
                        {//证明人
                            view.addObject("zmr", ao);
                        }
                        else if (ao.getPersonType() == 2)
                        {//发布人
                            view.addObject("fbr", ao);
                        }
                    }
                }
                else
                {
                    /*apiProject = new ApiProject();
                    apiProject.setUserId(userId);
                    apiProject.setState(ProjectConstant.PROJECT_STATUS_COLLECT);
                    apiProject.setOrderDirection("desc");
                    apiProject.setOrderBy("registrTime");
                    ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(apiProject, 1, 1);
                    if (apiPage.getTotal() > 0)
                    {
                        List<ApiProject> alist = apiPage.getResultData();
                        apiProject = alist.get(0);
                        ao = new ApiProjectUserInfo();
                        ao.setProjectId(apiProject.getId());
                        key = key + "_" + apiProject.getId();
                        ao.initNormalCache(true, DateUtil.DURATION_HOUR_S, key);
                        list = projectFacade.queryProjectUserInfoList(ao);
                        for (int i = 0; i < list.size(); i++)
                        {
                            ao = list.get(i);
                            if (ao.getPersonType() == 0)
                            {//受助人
                                //								view.addObject("szr", ao);
                            }
                            else if (ao.getPersonType() == 1)
                            {//证明人
                                //								view.addObject("zmr", ao);
                            }
                            else if (ao.getPersonType() == 2)
                            {//发布人
                                view.addObject("fbr", ao);
                            }
                        }
                    }*/
                   /* if("1".equals(help)){//为自己发布，读取用户信息到受助人
                    	ApiFrontUser user = userFacade.queryById(userId);
                    	view.addObject("user", user);
                    }*/
                }
                
            /*}*/
        }}
        return view;
    }
    
    /*
     * 申请求助项目成功的页面
     */
    @RequestMapping("appealFour")
    public ModelAndView appealViewFour()
    {
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_4");
        return view;
    }
    
    /*
     * 申请客服代发求助项目成功的页面
     */
    @RequestMapping("appealFive")
    public ModelAndView appealViewFive()
    {
        ModelAndView view = new ModelAndView("seekhelp/seekhelp_5");
        return view;
    }
    
    /*
     * 申请求助项目人员项目的数据
     */
    @ResponseBody
    @RequestMapping("appealUserInfoData")
    public Map<String, Object> appealViewThreeData(Appeal appel, HttpServletRequest request, HttpServletResponse response)
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
        ApiProject project = projectFacade.queryProjectDetail(appel.getId());
        if (project == null)
        {
            return webUtil.resMsg(0, "0005", "没有这个项目", null);
        }
        
        //判断项目是否符合要求
        /*if (project.getState() != ProjectConstant.PROJECT_STATUS_DRAFT)
        {
            return webUtil.resMsg(0, "0002", "状态错误", null);
        }*/
        if (project.getState() == ProjectConstant.PROJECT_STATUS_DRAFT && appel.getType() != 0)
        {
            project.setState(ProjectConstant.PROJECT_STATUS_AUDIT1);
        }
        project.setLastUpdateTime(new Date());
        project.setContentImageId(appel.getImgIds());
        List<ApiProjectUserInfo> aList = new ArrayList<ApiProjectUserInfo>(3);
        ApiProjectUserInfo api = new ApiProjectUserInfo();
        
        try
        {
            // 证明人
            api.setProjectId(appel.getId());
            if(appel.getRname()!=null && !"".equals(appel.getRname())){
            	api.setRealName(webUtil.encodeTodecode(appel.getRname()));
            }
            if(appel.getrAddress()!=null && !"".equals(appel.getrAddress())){
            	api.setWorkUnit(webUtil.encodeTodecode(appel.getrAddress()));
            }
            //api.setLinkMan(webUtil.encodeTodecode(appel.getRname()));
            if(appel.getrPhone()!=null && !"".equals(appel.getrPhone())){
            	api.setLinkMobile(appel.getrPhone());
            }
            if(appel.getRzw()!=null && !"".equals(appel.getRzw())){
            	api.setPersition(webUtil.encodeTodecode(appel.getRzw()));
            }
            api.setPersonType(1);
            api.setHelpType(appel.getHelpType());
            api.setCreateTime(new Date());
            aList.add(api);
            // 发布人
            api = new ApiProjectUserInfo();
            api.setProjectId(appel.getId());
            api.setRealName(webUtil.encodeTodecode(appel.getName()));
            api.setWorkUnit(webUtil.encodeTodecode(appel.getName()));
            api.setLinkMobile(appel.getPhone());
            api.setIndetity(appel.getIdcard());
            
            if(appel.getRelation()!=null && !"".endsWith(appel.getRelation())){
            	api.setRelation(webUtil.encodeTodecode(appel.getRelation()));
            }
            if(appel.getWeixin()!=null &&!"".equals(appel.getWeixin())){
            	api.setQqOrWx(webUtil.encodeTodecode(appel.getWeixin()));
            }
            //详细地址
            if(appel.getAddress()!=null && !"".equals(appel.getAddress())){
            	api.setFamilyAddress(webUtil.encodeTodecode(appel.getAddress()));
            }
            //项目联系人
            if(appel.getLinkMan()!=null && !"".equals(appel.getLinkMan())){
            	api.setRealName(webUtil.encodeTodecode(appel.getLinkMan()));
            }
            api.setHelpType(appel.getHelpType());
            api.setPersonType(2);
            api.setCreateTime(new Date());
            aList.add(api);
            // 受助人
            api = new ApiProjectUserInfo();
            api.setProjectId(appel.getId());
            api.setHelpType(appel.getHelpType());
            api.setPersonType(0);
            api.setRealName(webUtil.encodeTodecode(appel.getAppealName()));
            //api.setLinkMan(webUtil.encodeTodecode(appel.getAppealName()));
            //api.setSex(webUtil.encodeTodecode(appel.getAppealSex()));
            //api.setAge(appel.getAppealAge());
            if(appel.getAppealIdcard()!=null && !"".equals(appel.getAppealIdcard())){
                api.setIndetity(webUtil.encodeTodecode(appel.getAppealIdcard()));
            }
            api.setFamilyAddress(webUtil.encodeTodecode(appel.getAppealAddress()));
            //api.setVocation(webUtil.encodeTodecode(appel.getAppealzy()));
            //api.setWorkUnit(webUtil.encodeTodecode(appel.getAppealgzdw()));
            api.setLinkMobile(appel.getAppealPhone());
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
    
    /*
     * 选择客服代发
     * @param type
     */
    @ResponseBody
    @RequestMapping("appealByCustom")
    public Map<String, Object> appealByCustom(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "qq", required = false) String qq,
        @RequestParam(value = "phone", required = true) String phone,@RequestParam(value = "reasion", required = false) String leaveWord)
    {
        JSONObject personData = new JSONObject();
        personData.put("name", name);
        personData.put("qq", qq);
        personData.put("phone", phone);
        personData.put("leaveWord", leaveWord);
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
    
    /*
     * 选择求助项目的数据
     * @param type
     */
    @ResponseBody
    @RequestMapping("appealFirestData")
    public Map<String, Object> appealViewFirestData(@RequestParam(value = "type", required = true) String type)
    {
        //String key = PengPengConstants.SEEKHELP_FIELD_INFO_LIST + "_" + type;
        ApiTypeConfig aConfig = new ApiTypeConfig();
        aConfig.setTypeName_e(type);
       // aConfig.initNormalCache(true, DateUtil.DURATION_WEEK_S, key);
        List<ApiConfig> ac = fileFacade.queryApiConfigList(aConfig);
        return webUtil.successRes(ac);
    }
    
/**
 * 省
 * @param request
 * @param response
 * @return
 */
    @RequestMapping("provinceData")
    @ResponseBody
    public Map<String, Object> provinceData(HttpServletRequest request, HttpServletResponse response,
    		 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
    		 @RequestParam(value = "pageNum", required = false, defaultValue = "50") Integer pageNum)

    {

		List<ApiProjectArea> projects  = null ;
		Object objProvince = null ;
		 objProvince = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_PROVINCES);
		if(objProvince !=null){
			projects = (List<ApiProjectArea>)objProvince;
		}else{
	    	String  province = "";
	    	String  ip = "";
	    	ip = request.getRemoteAddr();
	    	ip = "ip="+ip;
	    	ApiProjectArea  area  = new ApiProjectArea();
	    	try {
				  province = IPAddressUtil.getAddress(ip, "utf-8");
				  if(province!=null){
					  province = province.replaceAll("省", "");
				  }
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}

			/*if(!StringUtils.isEmpty(province)&&!"0".equals(province)){
				
				area.setProvince(province);
			}*/
			ApiPage<ApiProjectArea> apiPage =  projectFacade.queryProjectAreaList(area, page, 100);
			
			projects = apiPage.getResultData();
			
			/*List<String> provinces = new ArrayList<String>();
			
			for(ApiProjectArea pa:projects){
				provinces.add(pa.getProvince());
			}
			if(!StringUtils.isEmpty(province)){
				if(!provinces.contains(province)){
					ApiProjectArea areaa = new ApiProjectArea();
					areaa.setProvince(province);
					projects.add(0,areaa);
					//provinces.add(0,province);
				}
			}*/
			redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_PROVINCES, projects, DateUtil.DURATION_TEN_S);
		}
        
        return webUtil.successRes(projects);
    }
    
    /**
     * 
     * 善款去向
     * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "projectDonateGo")
    @ResponseBody
    public Map<String, Object> projectDonateGo(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "20") Integer pageNum,@RequestParam(value = "title", required = false, defaultValue = "") String title)
    {

          Page p = new Page();
          p.setPage(page);
          p.setPageNum(pageNum);
          
          ApiPayMoneyRecord  pay = new ApiPayMoneyRecord();
          if(title!=null&&!"".equals(title)){
        	  title=RepairVulnerabilityUtil.StringFilter(title);
        	  System.out.println("title>>>>"+title);
        	  pay.setProjectTitle(title);
          }
          ApiPage<ApiPayMoneyRecord> result = payMoneyRecordFacade.queryPayMoneyRecordGO(pay, p.getPage(), p.getPageNum());
         
          if (result != null)
          {
        	  p.setData(result.getResultData());
              p.setNums(result.getTotal());
              /*
              System.out.println("page = "+page+" pageNum = "+pageNum+" size = "+result.getResultData().size()+" total = "+result.getTotal());
              for(ApiPayMoneyRecord py:result.getResultData()){
            	  System.out.println(py.getProjectTitle());
              }
              */
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
    
    @RequestMapping("donateSuccess")
    public ModelAndView donateSuccess(DepositForm from)
    {
        ModelAndView view = new ModelAndView("deposit/donateSuccess");
        ApiDonateRecord donateRecord = donateRecordFacade.queryPayNoticeByTranNum(from.getTradeNo());
        if(donateRecord != null)
        {
        	view.addObject("projectId", donateRecord.getProjectId());
        	view.addObject("tradeNo", donateRecord.getTranNum());
        	view.addObject("nickName", donateRecord.getNickName());
        	view.addObject("pName", donateRecord.getProjectTitle());
        	view.addObject("amount", donateRecord.getDonatAmount());
        	if(donateRecord.getDonatTime() != null)
        	{
        		view.addObject("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(donateRecord.getDonatTime()));
        	}
        }
        
        return view;
    }
    @RequestMapping("addVolunteerView")
    public ModelAndView addVolunteerView(ProjectForm projectForm,HttpServletRequest request)
    {
        ModelAndView view = new ModelAndView("project/project_volunteer");
        String isMobile = (String)request.getSession().getAttribute("ua");
        if ("mobile".equals(isMobile))
        {
            view = new ModelAndView("h5/project/project_volunteer_h5");
        }
        view.addObject("projectId",projectForm.getItemId());
        return view;
    }
    
    @RequestMapping("addCryPeopleView")
    public ModelAndView addCryPeopleView(ProjectForm projectForm,HttpServletRequest request)
    {
        ModelAndView view = new ModelAndView("project/project_cryPeople");
        String isMobile = (String)request.getSession().getAttribute("ua");
        if ("mobile".equals(isMobile))
        {
            view = new ModelAndView("h5/project/project_cryPeople");
        }
        view.addObject("projectId",projectForm.getItemId());
        return view;
    }
    
    /**
     * 志愿者入库
     * @param projectVolunteer
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addVolunteer")
    @ResponseBody
    public Map<String, Object> addVolunteer(@ModelAttribute ApiProjectVolunteer projectVolunteer, HttpServletRequest request, HttpServletResponse response)
    {
      
    	if(projectVolunteer.getPersonType() == 0)
    	{
    		if (StringUtils.isEmpty(projectVolunteer.getIndentity()) || StringUtils.isEmpty(projectVolunteer.getName()) 
    				|| projectVolunteer.getProjectId() == null || StringUtils.isEmpty(projectVolunteer.getMobile()) )
    		{
    			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
    		}
    	}
    	else if(projectVolunteer.getPersonType() == 1)
    	{
    		if ( StringUtils.isEmpty(projectVolunteer.getIndentity())|| StringUtils.isEmpty(projectVolunteer.getGroupName()) 
    				|| projectVolunteer.getProjectId() == null || StringUtils.isEmpty(projectVolunteer.getMobile()) )
    		{
    			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
    		}
    	}
    	
        
        ApiResult result = projectVolunteerFacede.save(projectVolunteer);
        if (result.getCode() == 1)
        {
            return webUtil.successRes(null);
        }
    
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED, "添加失败", null);
        }
    }
    
    /**
     * 求救者入库
     * @param projectCryPeople
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addCryPeople")
    @ResponseBody
    public Map<String, Object> addCryPeople(@ModelAttribute ApiProjectCryPeople projectCryPeople, HttpServletRequest request, HttpServletResponse response)
    {
      
        if (StringUtils.isEmpty(projectCryPeople.getName()) || projectCryPeople.getProjectId() == null || projectCryPeople.getMobile()==null)
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数不能为空", null);
        }
        
        ApiResult result = projectVolunteerFacede.save(projectCryPeople);
        if (result.getCode() == 1)
        {
            return webUtil.successRes(null);
        }
    
        else
        {
            return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED, "添加失败", null);
        }
    }
    
    @RequestMapping("releaseH5Project")
    public ModelAndView releaseH5Project(HttpServletRequest request,
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
    	        			logger.error("微信支付处理出现问题"+ e);
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
    	    			view.addObject("flag", "releaseH5Project");
    	        		return view;
    	        	}
    	        	
    	        }
    			//===========微信用户自动登陆end==============//
    	//1.判断用户是否登录
    	//2.判断用户是否实名认证
    	view = new ModelAndView("h5/releaseProject/projectRelase");
    	//Integer userId = UserUtil.getUserId(request, response);
		/*if(userId==null || userId==0){//未登录
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view;
		}*/
    	if(userId==null)
    		userId=user1.getId();
    	logger.info("userId>>>>>>"+userId.toString());
		ApiAuditStaff apiAuditStaff = new ApiAuditStaff();
		apiAuditStaff.setUserId(userId);
		apiAuditStaff.setPersonType("helpPeople");
		//apiAuditStaff.setState(203);
		ApiAuditStaff user=userFacade.queryAuditStaffByParam(apiAuditStaff);
		ApiCompany param = new ApiCompany();
		param.setUserId(userId);
		//param.setState(203);
		ApiCompany company = companyFacade.queryCompanyByParam(param);
		if(company!=null&&company.getState()==203){//企业认证通过
			view = new ModelAndView("redirect:/test/personReleaseNav_list.do?type=1&id="+company.getId());
		}
		else if(user!=null&&user.getState()==203){//实名认证通过
			view = new ModelAndView("redirect:/test/personReleaseNav_list.do?type=0&id="+user.getId());
		}
		//ApiFrontUser user2=userFacade.queryById(userId);
		String covIds =null;
		if(user!=null){
			covIds=user.getFileId();
		}
		logger.info("covIds>>>>>"+covIds);
        String resUrl = "http://res.17xs.org/picture//projectContent/";
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
    	return view;
    	
    }
    
    @RequestMapping("personReleaseNav_list")
	public ModelAndView personReleaseNav(ModelAndView view){
		view=new ModelAndView("h5/releaseProject/personReleaseNav_list");
		return view;
	}
    
    @RequestMapping("typeConfigList")
    public ModelAndView queryTypeConfigList(@RequestParam(value="field",required=true)String helpType){
    	ModelAndView mv=new ModelAndView();
    	
    	List<ApiTypeConfig> listTypeConfig= commonFacade.queryList();
    	
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
    	mv.addObject("listTypeConfig",list);
    	mv.setViewName("h5/releaseProject/personReleaseNav");	
    	return mv;
    	
    }
    
}
