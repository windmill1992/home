package com.guangde.home.controller.project;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectTopic;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;

@Controller
@RequestMapping("projectTopic")
public class ProjectTopicController {
	Logger logger = LoggerFactory.getLogger(ProjectTopicController.class);
	
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICommonFacade commonFacade;
	/**
	 * goto到专题创建页面
	 * @return
	 */
	@RequestMapping("gotoProjectTopicAdd")
	public ModelAndView gotoProjectTopicAdd(){
		ModelAndView view = new ModelAndView();
		view.setViewName("projectTopic/topic_add");
		return view;
	}
	
	/**
	 * 加载项目标签
	 * @return
	 */
	@RequestMapping("loadTags")
	@ResponseBody
	public Map<String, Object> loadTags(){
		ApiConfig param = new ApiConfig();
		param.setConfigKey("projectTag");
		List<ApiConfig> list = commonFacade.queryList(param);
		if(list!=null&&list.get(0)!=null){
			return webUtil.successRes(list.get(0));
		}
		else{
			return webUtil.successRes(null);
		}
	}
	
	/**
	 * 按项目标签查询项目
	 * @param request
	 * @param response
	 * @param tag
	 * @return
	 */
	@RequestMapping("loadProjects")
	@ResponseBody
	public Map<String, Object> loadProjects(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="tag",required=true)String tag){
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			return webUtil.loginFailedRes(null);
		}
		List<String> tagList = new ArrayList<String>();
		if(!"".equals(tag))
			tagList.add(tag);
		ApiProject param = new ApiProject();
		param.setUserId(userId);
		param.setTaglist(tagList);
		param.setState(240);
		ApiPage<ApiProject> page = projectFacade.queryUCenterProjectlist(param, 1, 100);
		if(page!=null&&page.getTotal()>0){
			return webUtil.successRes(page.getResultData());
		}
		else{
			return webUtil.successRes(null);
		}
	}
	
	/**
	 * 按项目ids加载项目标题
	 * @param request
	 * @param response
	 * @param tag
	 * @return
	 */
	@RequestMapping("loadProjectTitles")
	@ResponseBody
	public Map<String, Object> loadProjectTitles(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="ids",required=true)String ids){
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			return webUtil.loginFailedRes(null);
		}
		List<Integer> pList = new ArrayList<Integer>();
		if(!"".equals(ids)){
			String[] projectIds = ids.replace("，", ",").split(",");
			for (int i = 0; i < projectIds.length; i++) {
				pList.add(Integer.valueOf(projectIds[i]));
			}
		}
		ApiProject param = new ApiProject();
		param.setUserId(userId);
		param.setpList(pList);
		param.setState(240);
		ApiPage<ApiProject> page = projectFacade.queryUCenterProjectlist(param, 1, 100);
		if(page!=null&&page.getTotal()>0){
			return webUtil.successRes(page);
		}
		else{
			return webUtil.successRes(null);
		}
	}
	
	/**
	 * 添加项目专题
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("addProjectTopic")
	@ResponseBody
	public Map<String, Object> addProjectTopic(HttpServletRequest request,HttpServletResponse response,
			ApiProjectTopic model){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			return webUtil.loginFailedRes(null);
		}
		model.setUserId(userId);
		ApiResult result=projectFacade.saveProjectTopic(model);
		if(result.getCode()==1){//添加成功
			return webUtil.resMsg(1, "0000", result.getMessage(), null);
		}
		return webUtil.resMsg(0, "0010", "网络异常！", null);
	}
	
	/**
	 * goto到专题成功页
	 * @param id
	 * @return
	 */
	@RequestMapping("gotoProjectTopicSuccess")
	public ModelAndView gotoProjectTopicSuccess(@RequestParam(value="id",required=true)Integer id){
		ModelAndView view= new ModelAndView();
		view.setViewName("projectTopic/success");
		view.addObject("projectTopicId", id);
		return view;
	}
	
	/**
	 * goto到我的专题页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("gotoMyTopic")
	public ModelAndView gotoMyTopic(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView();
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			view.setViewName("redirect:/user/sso/login.do?entrance=http://www.17xs.org/projectTopic/gotoMyTopic.do");
			return view;
		}
		view.setViewName("projectTopic/topic_list");
		return view;
	}
	
	/**
	 * 加载用户专题列表
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("loadProjectTopicList")
	@ResponseBody
	public Map<String, Object> loadProjectTopicList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="20")int pageSize) throws ParseException{
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			return webUtil.loginFailedRes(null);
		}
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Double totalMoney=0.0;
		ApiProjectTopic param = new ApiProjectTopic();
		param.setUserId(userId);
		ApiPage<ApiProjectTopic> page = projectFacade.queryProjectTopicParam(param,pageNum,pageSize);
		if(page!=null&&page.getTotal()>0){
			for (int i = 0; i < page.getResultData().size(); i++) {
				String[] ids = page.getResultData().get(i).getProjectIds().replace("，", ",").split(",");
				for (int j = 0; j < ids.length; j++) {
					ApiProject project = projectFacade.queryProjectDetail(Integer.valueOf(ids[j]));
					if(project!=null){
						totalMoney=MathUtil.add(totalMoney, project.getCryMoney());
					}
				}
				page.getResultData().get(i).setSumProjectMoney(totalMoney);
				page.getResultData().get(i).setProjectCount(ids.length);
			    String d = format.format(page.getResultData().get(i).getCreateTime());  
			    //Date date=format.parse(d);  
			    page.getResultData().get(i).setTimeString(d);
			}
		}
		return webUtil.successRes(page);
	}
	
	/**
	 * 删除专题图片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("delectTopic")
	@ResponseBody
	public Map<String, Object> delectTopic(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=true)Integer id){
		Integer userId=UserUtil.getUserId(request, response);
		if(userId==null){//未登录
			return webUtil.loginFailedRes(null);
		}
		ApiResult result = projectFacade.deleteProjectTopic(id);
		if(result.getCode()==1){
			return webUtil.successRes(null);
		}
		return webUtil.resMsg(0, "0002", result.getMessage(), null);
	}
}
