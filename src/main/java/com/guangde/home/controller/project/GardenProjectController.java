package com.guangde.home.controller.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiReport;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Donation;
import com.guangde.home.vo.project.ProjectForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;

@Controller
@RequestMapping("project")
public class GardenProjectController
{
    Logger logger = LoggerFactory.getLogger(GardenProjectController.class);
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IFileFacade fileFacade;
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private INewsFacade newsFacade;
	
	@Autowired
	private IUserFacade userFacade;
    
    @RequestMapping("gardenindex")
    public ModelAndView index()
    {
        ModelAndView view = new ModelAndView("garden/garden_list");
        ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
        apiDonateRecord.setState(302);
        apiDonateRecord.setField("garden");
        ApiPage<ApiDonateRecord> apiPage = donateRecordFacade.queryByCondition(apiDonateRecord, 1, 5);
        view.addObject("record", apiPage.getResultData());
        return view;
    }
    
    /*
     * 善园项目
     * 
     * @param type 类型
     * 
     * @return 推荐项目json数据
     */
    @ResponseBody
    @RequestMapping("gardenlist")
    public JSONObject donationList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "len", required = false, defaultValue = "9") Integer len)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        // 把取到的善园项目封装成json数据
        try
        {
            ApiProject ap = new ApiProject();
            ap.setField("garden");
            ap.setIsHide(0);
            String key = PengPengConstants.INDEX_PROJECT_FIELD_LIST + "_garden";
            ap.initNormalCache(true, DateUtil.DURATION_MIN_S / 6, key);
            ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, page, len);
            
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
                    item.put("title", project.getTitle());
                    item.put("information", project.getInformation());
                    //剩余份数
                    item.put("surplusnum", project.getLeaveCopies());
                    //已捐赠金额
                    item.put("donationnum", project.getDonatAmount());
                    //要捐赠金额
                    item.put("totalnum", project.getCryMoney());
                    //要捐赠的每份金额
                    item.put("price", project.getPerMoney());
                    item.put("imageurl", project.getCoverImageUrl());
                    item.put("state", project.getState());
                    items.add(item);
                }
                data.put("items", items);
                data.put("page", apiPage.getPageNum());//当前页码
                data.put("pageNum", apiPage.getPageSize()); //每页行数
                data.put("total", apiPage.getPages());//总页数
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
     * @param projectForm 项目详情from 创建项目
     */
    @RequestMapping("gardencreate")
    public ModelAndView create(ProjectForm projectForm)
    {
        ModelAndView view = new ModelAndView("index");
        // from.setFocusList(homePageFacade.queryFocusMap());
        view.addObject("name", "guo");
        return view;
    }
    
    /*
     * @param projectId 项目ID 显示要编辑的项目
     */
    @RequestMapping("gardenviewedit")
    public ModelAndView viewedit(@RequestParam(value = "projectId") String projectId)
    {
        ModelAndView view = new ModelAndView("index");
        // from.setFocusList(homePageFacade.queryFocusMap());
        view.addObject("name", "guo");
        return view;
    }
    
    /*
     * @param projectForm 项目详情from 保存编辑完的项目
     */
    @RequestMapping("gardenedit")
    public ModelAndView edit(ProjectForm projectForm)
    {
        ModelAndView view = new ModelAndView("index");
        // from.setFocusList(homePageFacade.queryFocusMap());
        view.addObject("name", "guo");
        return view;
    }
    
    /*
     * @param projectId 项目ID 显示项目详情
     */
    @RequestMapping("gardenview")
    public ModelAndView view(@RequestParam(value = "projectId") Integer projectId)
    {
        
        //获取项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
        if (project != null)
        {
            
        }
        ApiDonateRecord r = new ApiDonateRecord();
        r.setState(302);
        r.setField("garden");
        //获取最新捐赠记录
        String key = PengPengConstants.GARDEN_PROJECT_DONATION_NEW_LIST;
        r.initNormalCache(true, DateUtil.DURATION_MIN_S / 6, key);
        ApiPage<ApiDonateRecord> newDonat = donateRecordFacade.queryByCondition(r, 1, 8);
        //获取捐赠记录排行榜
        String key2 = PengPengConstants.GARDEN_PROJECT_DONATION_MOST_LIST;
        r.initNormalCache(true, DateUtil.DURATION_MIN_S, key2);
        ApiPage<ApiDonateRecord> mostDonat = donateRecordFacade.queryByDonate(r, 1, 5);
        
        ModelAndView view = new ModelAndView("garden/gardengood_detail");
        project.setContent(StringUtil.convertToHtml(project.getContent()));
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
        view.addObject("project", project);
        view.addObject("newDonat", newDonat.getResultData());
        view.addObject("mostDonat", mostDonat.getResultData());
        return view;
    }
    
    /**
     * 公益众筹详情页
     * @param projectId
     * @return
     */
    @RequestMapping("newGardenView")
    public ModelAndView gardenView(@RequestParam(value = "projectId") Integer projectId)
    {
        
        //获取项目详情
        ApiProject project = projectFacade.queryProjectDetail(projectId);
      
        ApiDonateRecord r = new ApiDonateRecord();
        r.setState(302);
        r.setField("garden");
        r.setProjectId(projectId);
     
        //获取捐赠记录
        ApiPage<ApiDonateRecord> newDonat = donateRecordFacade.queryByCondition(r, 1, 100);
        
        //获取捐赠记录排行榜
        //ApiPage<ApiDonateRecord> mostDonat = donateRecordFacade.queryByDonate(r, 1, 5);
        
        ModelAndView view = new ModelAndView("garden/gardenProject_detail");
        if (project != null)
        {
        	project.setContent(StringUtil.convertToHtml(project.getContent()));
        }
        
        //项目发起人
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

        
        //项目进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
        ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1, 40);
        List<ApiReport> rlist = reports.getResultData();
        view.addObject("reports", rlist);
        List<ApiDonateRecord> rList = new ArrayList<ApiDonateRecord>();
        rList = newDonat.getResultData();
        for (ApiDonateRecord dr : rList)
        {
           
            String name = dr.getNickName();
            if(!StringUtils.isEmpty(name) && name.contains("游客")){
            	name = "爱心人士";
            	if (!StringUtils.isEmpty(dr.getTouristMessage())){
                    JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
                    name = dataJson.getString("name");
                    if (StringUtils.isEmpty(name)){
                        name = "爱心人士";
                    }
                    dr.setCoverImageurl(dataJson.getString("headimgurl"));
                    dr.setNickName(name);
                }
            }else{
            	if(dr.getCoverImageId() >0){
            		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
            		dr.setCoverImageurl(bFile.getUrl());
            	}
			}
			int d_value = DateUtil.minutesBetween(dr.getDonatTime(),
					DateUtil.getCurrentTimeByDate());
			Boolean flag = DateUtil.dateFormat(dr.getDonatTime()).equals(
					DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
			if (d_value / 60 > 24 || !flag) {
				dr.setTimeStamp(DateUtil.dateFormat(dr.getDonatTime()));
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
         
        }
        
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
        
        view.addObject("newDonat", rList);
        view.addObject("project", project);
        //view.addObject("mostDonat", mostDonat.getResultData());
    	/*点击量统计start*/
        try
        {
    		String clickRate = (String) redisService.queryObjectData(PengPengConstants.PROJECT_CLICKRATE_PC+projectId);
    		long startTime = DateUtil.getCurrentDayEnd().getTime() - new Date().getTime(); 
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
     * 当前项目的捐赠列表
     */
    @RequestMapping("gardendonationlist")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) Integer id,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum)
    {
    	List<Object> listObject=new ArrayList<Object>();
        if (id == null)
        {
            return webUtil.resMsg(0, "0001", "项目id不能为空", null);
        }
        //分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiDonateRecord r = new ApiDonateRecord();
        Integer userId = UserUtil.getUserId(request, response);
        if(userId != null && userId != 0){
			r.setGoodLibraryId(userId);
		}
        r.setProjectId(id);
        r.setState(302);
        List<String> llist = new ArrayList<String>(1);
        llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, id));
        r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId", "state");
        ApiPage<ApiDonateRecord> resultPage = donateRecordFacade.queryByCondition(r, p.getPage(), p.getPageNum());
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        Donation d;
        if (resultPage.getTotal() == 0)
        {
            p.setNums(0);
        }
        else
        {
        	ApiNewLeaveWord newLeaveWord=null;
            JSONArray items =null;
            for (ApiDonateRecord dr : resultPage.getResultData())
            {
            	//获取每条记录的留言根据捐赠记录的id
        	    items= new JSONArray();
        	    newLeaveWord=new ApiNewLeaveWord();
        	    newLeaveWord.setProjectDonateId(dr.getId());
        	    newLeaveWord.setProjectId(dr.getProjectId());
        	    newLeaveWord.setType(1);
          	    ApiPage<ApiNewLeaveWord> apiPage=projectFacade.queryApiNewLeaveWord(newLeaveWord, page, 20);
                if(apiPage!=null){
                	 for(ApiNewLeaveWord apiNewLeaveWord:apiPage.getResultData() ){
                   	  JSONObject item = new JSONObject();
                   	  item.put("id", apiNewLeaveWord.getId());
                   	  item.put("projectId", apiNewLeaveWord.getProjectId());
                   	  item.put("projectDonateId", apiNewLeaveWord.getProjectDonateId());
                   	  item.put("projectFeedbackId", apiNewLeaveWord.getProjectFeedbackId());
                   	  item.put("leavewordUserId", apiNewLeaveWord.getLeavewordUserId());
                   	  item.put("replyUserId", apiNewLeaveWord.getReplyUserId());
                   	  item.put("leavewordName", apiNewLeaveWord.getLeavewordName());
                   	  item.put("replyName", apiNewLeaveWord.getReplyName());
                   	  item.put("content", apiNewLeaveWord.getContent());
                   	  item.put("updateTime", apiNewLeaveWord.getUpdateTime());
                   	  item.put("createTime", apiNewLeaveWord.getCreateTime()); 
                   	  item.put("total", apiPage.getTotal());
                   	  item.put("currentPage", page);
                   	  items.add(item);
                      }
                } 
          	    listObject.add(items);
            	
                d = new Donation();
                
                d.setdMoney(dr.getDonatAmount());
                d.setdTime(dr.getDonatTime());
                if (dr.getDonateCopies() != null)
                {
                    d.setCopies(dr.getDonateCopies());
                }
                String name = dr.getNickName();
                if(!StringUtils.isEmpty(name) && name.contains("游客")){
                	name="爱心人士";
                	if (!StringUtils.isEmpty(dr.getTouristMessage())){
                        JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
                        name = dataJson.getString("name");
                        if (StringUtils.isEmpty(name)){
                            name = "爱心人士";
                        }
                        d.setImagesurl(dataJson.getString("headimgurl"));
                    }
                }else {
                	d.setImagesurl(dr.getCoverImageurl());
                	/*if(dr.getCoverImageId() >0){
                		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
                    	d.setImagesurl(bFile.getUrl());
                	}*/
				}
                d.setName(name);
                
                d.setTitle(dr.getProjectTitle());
                if(dr.getLeaveWord()==null){
                	d.setLeaveWord(null);
                }else if (dr.getLeaveWord().contains("transfer:")) {
                	d.setLeaveWord(dr.getLeaveWord().replace("transfer:", ""));
                	d.setdType(6);
				}else {
					d.setLeaveWord(dr.getLeaveWord());
				}
                
                int d_value = DateUtil.minutesBetween(dr.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(dr.getDonatTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flag) {
					dr.setTimeStamp(DateUtil.dateFormat(dr.getDonatTime()));
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
                d.setId(dr.getId());
                d.setUserId(dr.getUserId());
                list.add(d);
            }
            p.setNums(resultPage.getTotal());
        }
        
        //return webUtil.resMsg(1, "0000", "成功", p);
        return webUtil.successResDoubleObject(p,listObject);
    }
    
    /*
     * 当前项目的捐赠列表
     */
    @RequestMapping("gardendonationlist_new")
    @ResponseBody
    public Map<String, Object> gardendonationlist_new(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value = "id", required = false) Integer id,
    		@RequestParam(value="extensionPeople",required=false)Integer extensionPeople,
    		@RequestParam(value="userId",required=false)Integer userId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum)
    {
    	List<Object> listObject=new ArrayList<Object>();
        if (id == null)
        {
            return webUtil.resMsg(0, "0001", "项目id不能为空", null);
        }
        //分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        //邀请的朋友捐款记录
  		ApiDonateRecord r = new ApiDonateRecord();
  		r.setProjectId(id);
  		if (extensionPeople != null && extensionPeople != 0) 
  		{
  			r.setExtensionPeople(extensionPeople);
  		}
  		else
  		{
  			r.setExtensionPeople(userId);
  		}
  		r.setState(302);
        
      
        List<String> llist = new ArrayList<String>(1);
        llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, id));
        r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId", "state");
        
        ApiPage<ApiDonateRecord> resultPage = donateRecordFacade.queryByCondition(r, p.getPage(), p.getPageNum());
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        Donation d;
        if (resultPage.getTotal() == 0)
        {
            p.setNums(0);
        }
        else
        {
        	ApiNewLeaveWord newLeaveWord=null;
            JSONArray items =null;
            for (ApiDonateRecord dr : resultPage.getResultData())
            {
            	//获取每条记录的留言根据捐赠记录的id
        	    items= new JSONArray();
        	    newLeaveWord=new ApiNewLeaveWord();
        	    newLeaveWord.setProjectDonateId(dr.getId());
        	    newLeaveWord.setProjectId(dr.getProjectId());
        	    newLeaveWord.setType(1);
          	    ApiPage<ApiNewLeaveWord> apiPage=projectFacade.queryApiNewLeaveWord(newLeaveWord, page, 20);
                if(apiPage!=null){
                	 for(ApiNewLeaveWord apiNewLeaveWord:apiPage.getResultData() ){
                   	  JSONObject item = new JSONObject();
                   	  item.put("id", apiNewLeaveWord.getId());
                   	  item.put("projectId", apiNewLeaveWord.getProjectId());
                   	  item.put("projectDonateId", apiNewLeaveWord.getProjectDonateId());
                   	  item.put("projectFeedbackId", apiNewLeaveWord.getProjectFeedbackId());
                   	  item.put("leavewordUserId", apiNewLeaveWord.getLeavewordUserId());
                   	  item.put("replyUserId", apiNewLeaveWord.getReplyUserId());
                   	  item.put("leavewordName", apiNewLeaveWord.getLeavewordName());
                   	  item.put("replyName", apiNewLeaveWord.getReplyName());
                   	  item.put("content", apiNewLeaveWord.getContent());
                   	  item.put("updateTime", apiNewLeaveWord.getUpdateTime());
                   	  item.put("createTime", apiNewLeaveWord.getCreateTime()); 
                   	  item.put("total", apiPage.getTotal());
                   	  item.put("currentPage", page);
                   	  items.add(item);
                      }
                } 
          	    listObject.add(items);
            	
                d = new Donation();
                
                d.setdMoney(dr.getDonatAmount());
                d.setdTime(dr.getDonatTime());
                if (dr.getDonateCopies() != null)
                {
                    d.setCopies(dr.getDonateCopies());
                }
                String name = dr.getNickName();
                if(!StringUtils.isEmpty(name) && name.contains("游客")){
                	name="爱心人士";
                	if (!StringUtils.isEmpty(dr.getTouristMessage())){
                        JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
                        name = dataJson.getString("name");
                        if (StringUtils.isEmpty(name)){
                            name = "爱心人士";
                        }
                        d.setImagesurl(dataJson.getString("headimgurl"));
                    }
                }else {
                	d.setImagesurl(dr.getCoverImageurl());
                	/*if(dr.getCoverImageId() >0){
                		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
                    	d.setImagesurl(bFile.getUrl());
                	}*/
				}
                d.setName(name);
                
                d.setTitle(dr.getProjectTitle());
                if(dr.getLeaveWord()==null){
                	d.setLeaveWord(null);
                }else if (dr.getLeaveWord().contains("transfer:")) {
                	d.setLeaveWord(dr.getLeaveWord().replace("transfer:", ""));
                	d.setdType(6);
				}else {
					d.setLeaveWord(dr.getLeaveWord());
				}
                
                int d_value = DateUtil.minutesBetween(dr.getDonatTime(),
						DateUtil.getCurrentTimeByDate());
				Boolean flag = DateUtil.dateFormat(dr.getDonatTime()).equals(
						DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
				if (d_value / 60 > 24 || !flag) {
					dr.setTimeStamp(DateUtil.dateFormat(dr.getDonatTime()));
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
                d.setId(dr.getId());
                d.setUserId(dr.getUserId());
                list.add(d);
            }
            p.setNums(resultPage.getTotal());
        }
        
        //return webUtil.resMsg(1, "0000", "成功", p);
        return webUtil.successResDoubleObject(p,listObject);
    }
    
    /*
     * 求助人申请的项目
     */
    @RequestMapping("gardenappeallist")
    @ResponseBody
    public Map<String, Object> appeallist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum)
    {
        // 1.验证是否登入
        String uid = SSOUtil.verifyAuth(request, response);
        if (uid == null)
        {
            return webUtil.resMsg(-1, "0001", "未登入", null);
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
            // 一个月
            bdate = DateUtil.add(edate, -30);
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        // 调用获取消息总数的接口
        // todo
        p.setNums(200);
        // 调用获取消息列表的接口
        // todo
        
        List<ApiProject> list = new ArrayList<ApiProject>();
        ApiProject pp = new ApiProject();
        pp.setId(02);
        pp.setTitle("test");
        pp.setLastUpdateTime(new Date());
        pp.setState(200);
        list.add(pp);
        p.setData(list);
        
        return webUtil.resMsg(1, "0000", "成功", p);
    }
    
    /**
     * 配置一起捐页面
     * @param projectId
     * @return
     */
    @RequestMapping("togetherDonate_view")
    public ModelAndView togetherDonate_view(@RequestParam(value="projectId",required=true)Integer projectId){
    	ModelAndView view = new ModelAndView("h5/project/project_detail_together");
    	ApiProject project = projectFacade.queryProjectDetail(projectId);
    	view.addObject("project", project);
    	ApiDonateRecord r = new ApiDonateRecord();
		r.setProjectId(projectId);
		r.setState(302);
		double extensionDonateAmount = donateRecordFacade.countQueryByCondition(r);
		view.addObject("extensionDonateAmount", MathUtil.sub(project.getCryMoney(), extensionDonateAmount)); // 还需捐款金额
    	return view;
    }
    /*
     * 求助人申请的项目
     */
    @RequestMapping("saveTogetherDonate")
    @ResponseBody
    public Map<String, Object> saveTogetherDonate(@RequestParam(value = "projectId", required = true) Integer projectId,
        @RequestParam(value = "pointMoney", required = true) Double pointMoney, @RequestParam(value = "launchExplain", required = true) String launchExplain)
    {
    	ApiProject project = projectFacade.queryProjectDetail(projectId);
    	if(project==null){
    		return webUtil.resMsg(0, "0000", "项目异常", project);
    	}
        project.setPointMoney(pointMoney);
        project.setLaunchExplain(launchExplain);
        ApiResult result = projectFacade.updateProject(project);
        if(result.getCode()==1){
        	return webUtil.resMsg(1, "0000", "成功", null);
        }
        return webUtil.resMsg(0, "0000", "网络异常", null);
    }
}
