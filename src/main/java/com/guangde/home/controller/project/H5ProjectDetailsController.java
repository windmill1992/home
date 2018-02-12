package com.guangde.home.controller.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.api.user.IUserRelationInfoFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiThirdUser;
import com.guangde.entry.ApiWeixinModel;
import com.guangde.entry.BaseBean;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Donation;
import com.guangde.home.vo.project.PFeedBack;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;

@Controller
@RequestMapping("h5ProjectDetails")
public class H5ProjectDetailsController {
	
	Logger logger = LoggerFactory.getLogger(H5ProjectController.class);

	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IUserFacade  userFacade;
	@Autowired
	private ISystemNotifyFacade systemNotifyFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	@Autowired
    private IGoodLibraryFacade goodLibraryFacade;
	
	/**
     * 
     * H5的项目反馈
     * @param name
     * @param passWord
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping(value = "H5ProjectFeedBackList")
    @ResponseBody
    public Map<String, Object> careProjectList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "projectId", required = true) Integer projectId)
    {     
    	  JSONArray items =null;
    	  
          Page p = new Page();
          p.setPage(page);
          p.setPageNum(pageNum);
          
          
          Page newLeaveWordPage=new Page();
          newLeaveWordPage.setPage(page);
          newLeaveWordPage.setPageNum(pageNum);
          
          
          List<PFeedBack> feedbacks = new ArrayList<PFeedBack>();
          p.setData(feedbacks);
          
          List<Object> list=new ArrayList<Object>();
          
          ApiProjectFeedback feedBack = new ApiProjectFeedback();
          feedBack.setAuditState(203);
          feedBack.setProjectId(projectId);
          
          ApiPage<ApiProjectFeedback> result = projectFacade.queryH5ProjectFeedbckByCondition(feedBack, p.getPage(), p.getPageNum());
         
          if (result!= null)
          {
              PFeedBack tempf = null;
              for (ApiProjectFeedback f : result.getResultData())
              {   
            	  //获取对应的反馈信息
            	  items= new JSONArray();
            	  Integer feedBackId=f.getId();
            	  //根据项目反馈id去查询对应的回复的信息
            	  ApiNewLeaveWord newLeaveWord=new ApiNewLeaveWord();
            	  newLeaveWord.setProjectFeedbackId(feedBackId);
            	  newLeaveWord.setProjectId(f.getProjectId());
            	  newLeaveWord.setType(0);
            	  ApiPage<ApiNewLeaveWord> apiPage=projectFacade.queryApiNewLeaveWord(newLeaveWord, page, 20);
            	  tempf = new PFeedBack();
                  tempf.setId(f.getId());
                  tempf.setContent(f.getContent());
                  if (f.getFeedbackTime() != null)
                  {
                      tempf.setcTime(f.getFeedbackTime().getTime());
                  }
                  //项目反馈的时间
                  int d_value = DateUtil.minutesBetween(f.getFeedbackTime(),
							DateUtil.getCurrentTimeByDate());
					Boolean flag = DateUtil.dateFormat(f.getFeedbackTime()).equals(DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
					if (d_value / 60 > 24 || !flag) {
						tempf.setShowTime(DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日",f.getFeedbackTime()));
					} else {
						if (d_value / 60 >= 1) {
							tempf.setShowTime(d_value / 60 + "小时前");
						} else {
							if (d_value == 0) {
								tempf.setShowTime("刚刚");
							} else {
								tempf.setShowTime(d_value + "分钟前");
							}
						}
					}
                  tempf.setcDate(f.getFeedbackTime());
                  tempf.setImgs(f.getContentImageUrl());
                  tempf.setPid(f.getProjectId());
                  //获取发布人的信息当做项目反馈的人员
                  /*ApiProjectUserInfo ap=new ApiProjectUserInfo();
                  ap.setPersonType(2);
                  ap.setProjectId(projectId);
                  ApiProjectUserInfo apiProjectUserInfo=projectFacade.queryProjectUserInfo(ap);
                  if(apiProjectUserInfo!=null){
                	  tempf.setuName(apiProjectUserInfo.getRealName());
                  }*/
                  
                  tempf.setuName(f.getNickName());
                  tempf.setUserType(f.getUserType());
                  tempf.setTitle(f.getTitle());
                  tempf.setUserImageUrl(f.getHeadImageUrl());
                  tempf.setPid(f.getProjectId());
                  tempf.setSource(f.getSource());
                  feedbacks.add(tempf);
                 //当不为空是才能循环
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
                   	  //总的条数
                   	  item.put("total", apiPage.getTotal());
                   	  item.put("currentPage", page);
                   	  items.add(item);
                     }
                 } 
                 list.add(items);
              }
              p.setNums(result.getTotal());
              p.setData(feedbacks);
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
              return webUtil.successResDoubleObject(p,list);
          }	
    } 
    
    
    /*
     * 当前项目的捐赠列表
     */
    @RequestMapping("projectDeatilsdonationlist")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) Integer id,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum)
    {
        Integer userId = UserUtil.getUserId(request, response);
        if (id == null)
        {
            return webUtil.resMsg(0, "0001", "项目id不能为空", null);
        }
        List<Object> listObject=new ArrayList<Object>();
        //分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiDonateRecord r = new ApiDonateRecord();
        r.setProjectId(id);
        r.setState(302);
        List<String> llist = new ArrayList<String>(1);
        llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, id));
        r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId", "state");
        if(userId != null && userId != 0){
			r.setGoodLibraryId(userId);
		}
        ApiPage<ApiDonateRecord> resultPage = donateRecordFacade.queryByCondition(r, p.getPage(), p.getPageNum());
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        Donation d;
        if (resultPage.getTotal() == 0)
        {
            p.setNums(0);
        }
        else
        {   ApiNewLeaveWord newLeaveWord=null;
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
              	    
              	    //捐款记录
	                d = new Donation();
	                d.setField(dr.getDonatType());
	                d.setId(dr.getId());
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
	                	if(dr.getCoverImageId() >0){
	                		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
	                    	d.setImagesurl(bFile.getUrl());
	                	}
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
						dr.setTimeStamp(DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日",dr.getDonatTime()));
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
        
        return webUtil.successResDoubleObject(p,listObject);
    }
    
    /**
     * goto留言框PC(增加未登录用游客身份留言)
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("gotoAlterReplyPC")
    public Map<String, Object> gotoAlterReplyPC(ApiNewLeaveWord model,
    		//@RequestParam(value="code",required=true)String code,
    		HttpServletRequest request,HttpServletResponse response){
    	//1.反馈留言：type projectId content createTime updateTime freebackId userId1 userName1
    	//2.反馈回复留言：type projectId content createTime updateTime freebackId userId1 userName1 userId2 userName2
    	//3.捐助留言：type projectId content createTime updateTime donateId userId1 userName1
    	//4.捐助回复留言：type projectId content createTime updateTime donateId userId1 userName1 userId2 userName2
    	//===========微信用户自动登陆start==============//
    	Integer userId = UserUtil.getUserId(request, response);
    	ApiFrontUser user = new ApiFrontUser();
    	if(userId == null)
    	{
    		user.setUserName("游客");
    		user = userFacade.queryUserByParam(user);
    		userId=user.getId();
    	}
    	else{
    		user = userFacade.queryById(userId);
    	}
        if(model!=null){
        	if(model.getType()==0){
        		if(model.getLeavewordUserId()==null){//反馈留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return  webUtil.resMsg(101, "", "", model);
        		}
        		else{//反馈回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(102, "", "", model);
        		}
        	}
        	else{
        		if(model.getLeavewordUserId()==null){//捐助留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return webUtil.resMsg(201, "", "", model);
        		}
        		else{//捐助回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(202, "", "", model);
        		}
        	}
        }
        return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
    
    /**
     * goto留言框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("gotoAlterReply")
    public Map<String, Object> gotoAlterReply(ApiNewLeaveWord model,
    		//@RequestParam(value="code",required=true)String code,
    		HttpServletRequest request,HttpServletResponse response){
    	//1.反馈留言：type projectId content createTime updateTime freebackId userId1 userName1
    	//2.反馈回复留言：type projectId content createTime updateTime freebackId userId1 userName1 userId2 userName2
    	//3.捐助留言：type projectId content createTime updateTime donateId userId1 userName1
    	//4.捐助回复留言：type projectId content createTime updateTime donateId userId1 userName1 userId2 userName2
    	//===========微信用户自动登陆start==============//
    			String openId ="";
    			String token = "";
    			String unionid = "";
    			StringBuffer url = request.getRequestURL();
    			String queryString = request.getQueryString();
    			String perfecturl = "http://www.17xs.org/newReleaseProject/project_detail.do?" + queryString;//url + "?" + queryString;
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
    	        		String backUrlString="http://www.17xs.org/ucenter/user/Login_H5.do?flag=leadword_"+model.getProjectId();
    	        		return webUtil.resMsg(-2, "", backUrlString, null);
    	        	}
    	        	
    	        }
        ApiFrontUser user = userFacade.queryById(userId);
        if(model!=null){
        	if(model.getType()==0){
        		if(model.getLeavewordUserId()==null){//反馈留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return  webUtil.resMsg(101, "", "", model);
        		}
        		else{//反馈回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(102, "", "", model);
        		}
        	}
        	else{
        		if(model.getLeavewordUserId()==null){//捐助留言
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			return webUtil.resMsg(201, "", "", model);
        		}
        		else{//捐助回复留言
        			ApiFrontUser newuser = userFacade.queryById(model.getLeavewordUserId());
        			model.setReplyUserId(model.getLeavewordUserId());
        			model.setReplyName(newuser.getNickName()==null?"":newuser.getNickName());
        			model.setLeavewordUserId(userId);
        			model.setLeavewordName(user.getNickName()==null?"":user.getNickName());
        			
        			return webUtil.resMsg(202, "", "", model);
        		}
        	}
        }
        return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
    
    /**
     * 添加留言
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("addNewLeaveWord")
    public Map<String, Object> addNewLeaveWord(ApiNewLeaveWord model,
    		HttpServletRequest request,HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)//未登录
        {
        	return webUtil.loginFailedRes(null);
        }
		ApiProject project=projectFacade.queryProjectDetail(model.getProjectId());
        if(project.getUserId().equals(userId)){
        	model.setIsRead(1);
        }else{
        	model.setIsRead(0);
        }
    	if(model!=null){
    		model.setCreateTime(new Date());
    		model.setUpdateTime(new Date());
    		if(model.getProjectFeedbackId()!=null && model.getProjectDonateId()==null){
    			model.setType(0);
    		}
    		if(model.getProjectFeedbackId()==null && model.getProjectDonateId()!=null){
    			model.setType(1);
    			//TODO 推送微信消息
    			ApiDonateRecord d = donateRecordFacade.queryByDonateId(model.getProjectDonateId());
    			if(d != null){
    				ApiFrontUser user = userFacade.queryById(d.getUserId());
    				ApiThirdUser thirdUser = new ApiThirdUser();
    				thirdUser.setUserId(d.getUserId());
    				thirdUser = userFacade.queryThirdUserByParam(thirdUser);
    				if(thirdUser != null && thirdUser.getAccountNum() != null){
    					ApiWeixinModel weiXinModel = new ApiWeixinModel();
    	    		 	weiXinModel.setModel(7);
    		           	weiXinModel.setState(0);
    		           	weiXinModel.setUserId(d.getUserId());
    		           	weiXinModel.setOpenId(thirdUser.getAccountNum());
    		           	weiXinModel.setProjectId(project.getId());
    		           	weiXinModel.setCreateTime(new Date());
    		           	weiXinModel.setValue1(user.getNickName() + "，你有一条新的消息！");
    		           	weiXinModel.setValue2(model.getContent());
    		           	weiXinModel.setValue3(DateUtil.parseToDefaultDateTimeString(weiXinModel.getCreateTime()));
    		           	weiXinModel.setValue4("请再帮【" + project.getTitle() + "】转发一下，让更多人帮助TA\n→点击帮助转发");
    	    			userFacade.saveWeixinModel(weiXinModel);
    				}
    			}
    		}
    		if(model.getProjectFeedbackId()==null && model.getProjectDonateId()==null){
    			model.setType(2);
    		}
    		ApiResult result = projectFacade.saveNewLeaveWord(model);
    		if(result.getCode()==1)
    			return webUtil.resMsg(1, "0000", "添加成功！", null);
    	}
    	return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
    
    /**
     * 添加留言pc端
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("addNewLeaveWordPc")
    public Map<String, Object> addNewLeaveWordPc(ApiNewLeaveWord model,
    		HttpServletRequest request,HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	ApiFrontUser user = new ApiFrontUser();
        if (userId == null)//未登录
        {
        	user.setUserName("游客");
    		user = userFacade.queryUserByParam(user);
    		userId=user.getId();
        	//return webUtil.loginFailedRes(null);
        }
		ApiProject project=projectFacade.queryProjectDetail(model.getProjectId());
        if(project.getUserId().equals(userId)){
        	model.setIsRead(1);
        }else{
        	model.setIsRead(0);
        }
    	if(model!=null){
    		model.setCreateTime(new Date());
    		model.setUpdateTime(new Date());
    		if(model.getProjectFeedbackId()!=null && model.getProjectDonateId()==null){
    			model.setType(0);
    		}
    		if(model.getProjectFeedbackId()==null && model.getProjectDonateId()!=null){
    			model.setType(1);
    		}
    		ApiResult result = projectFacade.saveNewLeaveWord(model);
    		if(result.getCode()==1)
    			return webUtil.resMsg(1, "0000", "添加成功！", null);
    	}
    	return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
    /**
     * loadmore留言
     * @param model
     * @param i
     * @param currentPage
     * @return
     */
    @ResponseBody
    @RequestMapping("loadMoreLeaveWord")
    public Map<String, Object> loadMoreLeaveWord(ApiNewLeaveWord model,
    		@RequestParam(value="currentPage",required=true)int currentPage,
    		int surplusTotal
    		){
    	 JSONArray items = new JSONArray();
    	if(model!=null){
    		ApiNewLeaveWord param = new ApiNewLeaveWord();
    		param.setProjectId(model.getProjectId());
    		if(model.getProjectDonateId()==null && model.getProjectFeedbackId()!=null){
    			param.setProjectFeedbackId(model.getProjectFeedbackId());
    			param.setType(0);
    		}
    		else if(model.getProjectDonateId()!=null && model.getProjectFeedbackId()==null){
				param.setProjectDonateId(model.getProjectDonateId());
				param.setType(1);
			}else{
				return webUtil.resMsg(0, "0002", "参数错误！", null);
			}
    		//加一因为最新插入了一条
    		int pageSize=100;
    	
    		ApiPage<ApiNewLeaveWord> page = projectFacade.queryApiNewLeaveWord(param,currentPage,pageSize);
    		if(page.getTotal()==0){
    			return webUtil.resMsg(0, "0040", "没有更多数据了 ！", null);
    		}
    		else if(page!=null){
    			
    			for(ApiNewLeaveWord apiNewLeaveWord:page.getResultData() ){
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
              	  item.put("currentPage",currentPage);
              	  item.put("total",page.getTotal());
              	  items.add(item);
                 }
    		  return webUtil.resMsg(1, "0000", "成功！",items);
    		}
    	}
    	return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
    
    /**
     * 评论之后刷新回复的信息
     * @param model
     * @param currentPage
     * @param surplusTotal
     * @return
     */
    @ResponseBody
    @RequestMapping("refleshLeaveWord")
    public Map<String, Object> refleshLeaveWord(ApiNewLeaveWord model,
    		@RequestParam(value="currentPage",required=true)int currentPage
    		){
    	 JSONArray items = new JSONArray();
    	if(model!=null){
    		ApiNewLeaveWord param = new ApiNewLeaveWord();
    		param.setProjectId(model.getProjectId());
    		if(model.getProjectDonateId()==null && model.getProjectFeedbackId()!=null){
    			param.setProjectFeedbackId(model.getProjectFeedbackId());
    			param.setType(0);
    		}
    		else if(model.getProjectDonateId()!=null && model.getProjectFeedbackId()==null){
				param.setProjectDonateId(model.getProjectDonateId());
				param.setType(1);
			}else{
				return webUtil.resMsg(0, "0002", "参数错误！", null);
			}
    	
    		ApiPage<ApiNewLeaveWord> page = projectFacade.queryApiNewLeaveWord(param,currentPage,20);
    		if(page.getTotal()==0){
    			return webUtil.resMsg(0, "0040", "没有更多数据了 ！", null);
    		}
    		else if(page!=null){
    			for(ApiNewLeaveWord apiNewLeaveWord:page.getResultData() ){
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
              	  item.put("currentPage",currentPage);
              	  item.put("total",page.getTotal());
              	  items.add(item);
                 }
    		  return webUtil.resMsg(1, "0000", "成功！",items);
    		}
    	}
    	return webUtil.resMsg(0, "0002", "参数错误！", null);
    }
}
