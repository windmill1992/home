package com.guangde.home.controller.project;

import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.MvcNamespaceHandler;

import com.alibaba.fastjson.JSON;
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
import com.guangde.entry.ApiAuditProject;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiFrontUser_address;
import com.guangde.entry.ApiGoodLibraryProple;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiPayMoneyRecord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectCount;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiProjectVolunteer;
import com.guangde.entry.ApiReport;
import com.guangde.entry.ApiTypeConfig;
import com.guangde.entry.ApiUserCard;
import com.guangde.entry.BaseBean;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
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
@RequestMapping("commentBatch")
public class CommentBatchController {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);
	private static final String phonecodeprex = "phonecode_r_";
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
	
	@RequestMapping("newLeaveWordList")
	@ResponseBody
	public Map<String,Object> newLeaveWordList(ApiNewLeaveWord apiNewLeaveWord,@RequestParam(value="pageNum",required=true) int pageNum,
			HttpServletRequest request,HttpServletResponse response,@RequestParam(value="index",required=false)int index
			){
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){
			return webUtil.resMsg(0, "0000", "未登录",null);
		}
		ApiPage<ApiNewLeaveWord> apList=null;
		if(index==0){   //全部回复
			apList=projectFacade.queryApiNewLeaveWord(apiNewLeaveWord, pageNum, 20);
		}else if(index==1){ //已回复
			apiNewLeaveWord.setIsRead(1);
			apiNewLeaveWord.setLeavewordUserId(userId);
			apList=projectFacade.queryApiNewLeaveWord(apiNewLeaveWord, pageNum, 20);
		}else{  //未回复
			apiNewLeaveWord.setLeavewordUserId(userId);
			apList=projectFacade.queryNoReplyByParam(apiNewLeaveWord, pageNum , 20);
		}
		
		if(apList!=null){
			for (ApiNewLeaveWord apiNewLeaveWord2:apList.getResultData()) {
				JSONObject item = new JSONObject();
				item.put("id", apiNewLeaveWord2.getId());
				item.put("type", apiNewLeaveWord2.getType());
				item.put("projectId", apiNewLeaveWord2.getProjectId());
				item.put("projectDonateId", apiNewLeaveWord2.getProjectDonateId());
				item.put("projectFeedbackId", apiNewLeaveWord2.getProjectFeedbackId());
				item.put("leavewordUserId", apiNewLeaveWord2.getLeavewordUserId());
				item.put("replyUserId", apiNewLeaveWord2.getReplyUserId());
				item.put("leavewordName", apiNewLeaveWord2.getLeavewordName());
				item.put("replyName", apiNewLeaveWord2.getReplyName());
				item.put("content", apiNewLeaveWord2.getContent());
				item.put("updateTime", apiNewLeaveWord2.getUpdateTime());
				item.put("createTime", apiNewLeaveWord2.getCreateTime());
				items.add(item);
			}
			data.put("total",apList.getTotal());
			data.put("data", items);
			return webUtil.resMsg(1, "0001", "",data);
		}else{
			return webUtil.resMsg(-1, "0002","没有数据了",null);
		}
		
	}
	/**
     * 添加批量留言
     * @param model
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("addBatchNewLeaveWord")
    public Map<String, Object> addBatchNewLeaveWord(
    		HttpServletRequest request,HttpServletResponse response,
    		Integer projectId,String typess,String recordIdss,String leaveWordIdss,String content,
    		String replyNamess,String idss
    		){
    	Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)//未登录
        {
        	return webUtil.loginFailedRes(null);
        }
        ApiFrontUser user=userFacade.queryById(userId);
        if(user==null){
        	return webUtil.resMsg(0, "0002", "该用户不存在！", null);
        }
    	String []types=typess.split(",");
    	String []recordIds=recordIdss.split(",");
    	String []leaveWordIds=leaveWordIdss.split(",");
    	String []replyNames=replyNamess.split(",");
    	String []ids=idss.split(",");
    	for(int i=0;i< leaveWordIds.length;i++){
    	    ApiNewLeaveWord apiNewLeaveWord=new  ApiNewLeaveWord();
    	    apiNewLeaveWord.setContent(content);
    	    apiNewLeaveWord.setProjectId(projectId);
    	    apiNewLeaveWord.setLeavewordUserId(userId);
    	    apiNewLeaveWord.setReplyUserId(Integer.parseInt(leaveWordIds[i]));
    	    apiNewLeaveWord.setLeavewordName(user.getNickName());
    	    apiNewLeaveWord.setReplyName(replyNames[i]);
    	    apiNewLeaveWord.setCreateTime(new Date());
    	    apiNewLeaveWord.setUpdateTime(new Date());
    	    int type=Integer.parseInt(types[i]);
    	    if(type==0){
    	    	apiNewLeaveWord.setType(0);
    	    	apiNewLeaveWord.setProjectFeedbackId(Integer.parseInt(recordIds[i]));
    	    }else if(type==1){
    	    	apiNewLeaveWord.setType(1);
    	    	apiNewLeaveWord.setProjectDonateId(Integer.parseInt(recordIds[i]));
    	    }
    	    apiNewLeaveWord.setIsRead(1);
    	    ApiResult result = projectFacade.saveNewLeaveWord(apiNewLeaveWord);
    	    if(result.getCode()!=1)
    	    	return webUtil.resMsg(0, "0000", "回复失败！", null);
    	    else{
    	    	ApiNewLeaveWord alw=new ApiNewLeaveWord();
    	    	alw.setId(Integer.parseInt(ids[i]));
    	    	alw.setIsRead(1);
    	    	ApiResult result1 =projectFacade.saveNewLeaveWord(alw);
    	    	if(result1.getCode()!=1)
    	    		return webUtil.resMsg(0, "0000", "回复失败！", null);
    	    }
    	}
    	
	    return webUtil.resMsg(1, "0000", "回复成功！", null);
    	
    }
    
}
