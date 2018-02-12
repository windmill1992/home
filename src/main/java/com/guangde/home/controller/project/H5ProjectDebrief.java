package com.guangde.home.controller.project;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.entry.ApiProjectUserInfo;
import com.guangde.entry.ApiReport;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.pojo.ApiPage;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("project")
public class H5ProjectDebrief {
	
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private ICommonFacade commonFacade;
	
	/**
	 * 单条项目反馈页面
	 * @param projectId
	 * @return
	 */
	@RequestMapping("project_debrief_view")
	public ModelAndView project_debrief_view(@RequestParam("projectId")Integer projectId,
			@RequestParam("id")Integer id){
		ModelAndView mv = new ModelAndView("h5/project/project_debriefing");
		//项目logo 项目标题   项目捐款人次
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		mv.addObject("project", project);
		//项目反馈: 发布人姓名 发布时间（2017年08月01日）  反馈内容
		ApiProjectFeedback feedback = new ApiProjectFeedback();
		feedback.setId(id);
		ApiPage<ApiProjectFeedback> page = projectFacade.queryProjectFeedbackList(feedback, 1, 1);
		if(page!=null && page.getTotal()>0){
			mv.addObject("feedback", page.getResultData().get(0));
		}
		return mv;
	}
	
	/**
	 * 项目反馈详情页面
	 * @param projectId
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping("project_debriefDetail_view")
	public ModelAndView project_debriefDetail_view(@RequestParam("projectId")Integer projectId,
			HttpServletRequest request,HttpServletResponse response) throws JDOMException, IOException{
		ModelAndView mv = new ModelAndView("h5/project/project_debrief_detail");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId!=null){
			ApiFrontUser user = userFacade.queryById(userId);
			mv.addObject("user", user);
		}
		//项目logo 项目标题  项目简介 已筹金额 
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		
		ApiProjectUserInfo userInfo = new ApiProjectUserInfo();
		userInfo.setProjectId(projectId);
		List<ApiProjectUserInfo> userInfos = projectFacade.queryProjectUserInfoList(userInfo);
		for(ApiProjectUserInfo u:userInfos){
			if(u.getPersonType()==2){
				project.setNickName(u.getWorkUnit());
			}
		}
		
		mv.addObject("project", project);
		//执行进度
        ApiReport report = new ApiReport();
        report.setProjectId(projectId);
        ApiPage<ApiReport> reports = projectFacade.queryReportList(report, 1, 1);
        if(reports!=null && reports.getTotal()>0){
        	mv.addObject("report", reports.getResultData().get(0));
        }
		//项目反馈   反馈时间   反馈人  反馈内容   反馈图片
		ApiProjectFeedback feedback = new ApiProjectFeedback();
		feedback.setAuditState(203);
		feedback.setProjectId(projectId);
		ApiPage<ApiProjectFeedback> page = projectFacade.queryProjectFeedbackList(feedback, 1, 20);
		if(page!=null && page.getTotal()>0){
			mv.addObject("feedbacks", page.getResultData());
		}
		
		//推荐项目
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST);
		if (obj != null) {
			mv.addObject("rdList", obj);
		} else {
			 ApiProject ap = new ApiProject();
	         ap.setOrderDirection("desc");
	         ap.setIsHide(0);
	         ap.setIsRecommend(1);
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, 1, 3);
	         mv.addObject("rdList", apiPage.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}
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
		String browser = UserUtil.Browser(request);
		mv.addObject("browser", browser);
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
			mv.addObject("appId",map.get("appId"));
			mv.addObject("timestamp",map.get("timeStamp"));
			mv.addObject("noncestr",map.get("nonceStr"));
			mv.addObject("signature",map.get("signature"));
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
		return mv;
	}
}
