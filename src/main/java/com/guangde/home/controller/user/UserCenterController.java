package com.guangde.home.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.*;
import com.guangde.dto.ApiCompanyDto;
import com.guangde.entry.*;
import com.guangde.home.annotation.ActionLog;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.constant.ProjectConstant;
import com.guangde.home.utils.*;
import com.guangde.home.vo.common.HomeFile;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Appeal;
import com.guangde.home.vo.project.ProjectForm;
import com.guangde.home.vo.user.PUser;
import com.guangde.home.vo.user.UserCard;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("ucenter")
public class UserCenterController {

	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IPayMoneyRecordFacade payMoneyRecordFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ISystemNotifyFacade systemNotifyFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;
	@Autowired
	private ICommonFacade commonFacade;

	/**
	 * 用户中心消息界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "core/msg")
	@ActionLog(content = "查看用户中心消息")
	public ModelAndView msg(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/my_message");
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		view.addObject("userType", user1.getUserType());
		view = myhlep(view, userId);
		view.addObject("guideName","系统信息");
		return view;
	}

	@RequestMapping(value = "core/mygood", method = RequestMethod.GET)
	@ActionLog(content = "查看捐赠记录")
	public ModelAndView mygood(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/my_good");
		view.addObject("guideName", "捐赠记录");
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		view = myhlep(view, userId);
		return view;
	}
	/**
	 * 善款跟踪页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "core/personTracking")
	@ActionLog(content = "查看善款跟踪")
	public ModelAndView personTracking(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/use_tracking");
		ApiFrontUser user = new ApiFrontUser();
		user.setId(userId);
		view.addObject("guideName", "善款跟踪");
		user = userFacade.queryPersonCenter(user);
		
		ApiFrontUser user1 = userFacade.queryById(userId);
		if (user1.getLoveState() == 201 || user1.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		view.addObject("user", user1);
		view = myhlep(view, userId);
		return view;
	}
	
	
	/**
	 * 关注的项目 页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "core/focusProject")
	@ActionLog(content = "查看关注项目")
	public ModelAndView focusProject(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "projectId", required = false) Integer projectId) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/use_focus");
		ApiFrontUser user = new ApiFrontUser();
		user.setId(userId);
		view.addObject("guideName", "关注的项目");
		user = userFacade.queryPersonCenter(user);
		
		view.addObject("projectId", projectId);
		ApiFrontUser user1 = userFacade.queryById(userId);
		if (user1.getLoveState() == 201 || user1.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		view.addObject("user", user1);
		view = myhlep(view, userId);
		return view;
	}

	public ModelAndView myhlep(ModelAndView view, Integer userId) {
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		// 判断是否要显示我要求助的项目
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		return view;
	}

	/**
	 * 用户中心善管家
	 * 
	 * @param page
	 *            第几页
	 * @param pageNum
	 *            每页几条
	 * @param type
	 *            类型，进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/myloveGroupData")
	public Map<String, Object> myloveGroupData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") int pageNum,
			@RequestParam(value = "type", required = false, defaultValue = "1") int type) {
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		int flag = 1;
		String errorCode = "0000";
		String errorMsg = "成功";
		// 把取到的善园项目封装成json数据
		try {
			Integer userId = UserUtil.getUserId(request, response);
			if (userId == null) {
				flag = -1;
				errorCode = "0001";
				errorMsg = "没有登录";
				return webUtil.resMsg(flag, errorCode, errorMsg, data);
			}
			ApiProject apiProject = new ApiProject();
			apiProject.setUserId(userId);
			ApiPage<ApiProject> apiPage = null;
			if (type == 2) {
				// 审核中
				List<Integer> list = new ArrayList<Integer>();
				list.add(ProjectConstant.PROJECT_STATUS_AUDIT1);
				list.add(ProjectConstant.PROJECT_STATUS_AUDIT2);
				apiProject.setStates(list);
			} else if (type == 3) {
				// 募捐中和执行中
				List<Integer> list = new ArrayList<Integer>();
				list.add(ProjectConstant.PROJECT_STATUS_COLLECT);
				list.add(ProjectConstant.PROJECT_STATUS_EXECUTE);
				apiProject.setStates(list);
			}
			// else if (type == 4) {
			// //执行中
			// apiProject.setState(ProjectConstant.PROJECT_STATUS_EXECUTE);
			// }
			else if (type == 5) {
				// 未通过
				apiProject.setState(ProjectConstant.PROJECT_STATUS_BACK);
			} else if (type == 6) {
				// 已结束
				apiProject.setState(ProjectConstant.PROJECT_STATUS_DONE);
			} else if (type == 1) {
				// 代办事务,提交面试报告，结束项目等可操作的2,4
				List<Integer> list = new ArrayList<Integer>();
				list.add(ProjectConstant.PROJECT_STATUS_AUDIT1);
				list.add(ProjectConstant.PROJECT_STATUS_AUDIT2);
				list.add(ProjectConstant.PROJECT_STATUS_COLLECT);
				list.add(ProjectConstant.PROJECT_STATUS_EXECUTE);
				apiProject.setStates(list);
			}
			apiPage = projectFacade.queryKeeperProjectList(apiProject, page,
					pageNum);
			List<ApiProject> projects = apiPage.getResultData();
			// 无数据
			if (projects.size() == 0) {
				flag = 2;
				errorCode = "0007";
				errorMsg = "没数据";
				return webUtil.resMsg(flag, errorCode, errorMsg, null);
			} else {
				for (ApiProject project : projects) {
					JSONObject item = new JSONObject();
					item.put("itemId", project.getId());
					item.put("title", project.getTitle());
					item.put("role", project.getRole());
					ApiLoveGroupMent alove = new ApiLoveGroupMent();
					alove.setProjectId(project.getId());
					// 客服
					alove.setUserType(2);
					ApiPage<ApiLoveGroupMent> apiLove = projectFacade
							.queryLoveGroupMentList(alove, page, pageNum);
					if (apiLove.getTotal() > 0) {
						item.put("customer", apiLove.getResultData().get(0)
								.getUserName());
					}
					// 创建人
					if (project.getUserId() != null && project.getUserId() != 0) {
						ApiFrontUser user = userFacade.queryById(project
								.getUserId());
						if (user != null) {
							item.put("contact", user.getUserName());
						}
					}
					if (project.getState() == ProjectConstant.PROJECT_STATUS_AUDIT1) {
						item.put("status", "待初审");
						item.put("oprate", "提交面试报告");
					} else if (project.getState() == ProjectConstant.PROJECT_STATUS_AUDIT2) {
						item.put("status", "待复审");
						item.put("oprate", "");
					} else if (project.getState() == ProjectConstant.PROJECT_STATUS_BACK) {
						item.put("status", "未通过");
						item.put("oprate", "已结束");
					} else if (project.getState() == ProjectConstant.PROJECT_STATUS_COLLECT) {
						item.put("status", "募捐中");
						item.put("oprate", "结束项目");
					} else if (project.getState() == ProjectConstant.PROJECT_STATUS_EXECUTE) {
						item.put("status", "执行中");
						item.put("oprate", "结束项目");
					} else if (project.getState() == ProjectConstant.PROJECT_STATUS_DONE) {
						item.put("status", "已结束");
						item.put("oprate", "已结束");
					}
					item.put("state", project.getState());
					item.put(
							"schedule",StringUtil.doublePercentage(project.getDonatAmount(),project.getCryMoney(),0)+"%");// 进度
					item.put("endTime", DateUtil
							.parseToDefaultDateString(project.getDeadline()));
					item.put("checkTime", DateUtil
							.parseToDefaultDateTimeString(project
									.getLastUpdateTime()));
					item.put("stopTime", DateUtil
							.parseToDefaultDateTimeString(project
									.getLastUpdateTime()));
					items.add(item);
				}
				data.put("data", items);
				data.put("page", apiPage.getPageNum());// 当前页码
				data.put("pageNum", apiPage.getPageSize()); // 每页行数
				data.put("total", apiPage.getPages());// 总页数
			}
		} catch (Exception e) {
			// 后台服务发生异常
			e.printStackTrace();
			data.put("result", 2);
			return data;
		}

		return webUtil.resMsg(flag, errorCode, errorMsg, data);
	}

	/**
	 * 用户中心善管家项目详情显示
	 * 
	 * @param ProjectForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/viewItem")
	public Map<String, Object> viewItem(ProjectForm from) {
		JSONObject data = new JSONObject();
		ApiProject apiP = projectFacade.queryProjectDetail(from.getItemId());
		data.put("itemId", apiP.getId());
		data.put("title", apiP.getTitle());
		data.put("information", apiP.getContent());
		data.put("field", apiP.getField());
		JSONArray imagejsa = new JSONArray();
		for(ApiBFile img : apiP.getBfileList()){
			JSONObject imagedata = new JSONObject();
			imagedata.put("id", img.getId());
			imagedata.put("src", img.getUrl());
			imagejsa.add(imagedata);
		}
		data.put("images", imagejsa);
		return webUtil.successRes(data);
	}

	/**
	 * 用户中心善管家项目改变
	 * 
	 * @param ProjectForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/updatemyloveGroupData")
	public Map<String, Object> updateItem(ProjectForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		if (form.getItemId() == 0) {
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误",
					null);
		}
		// 校验当前用户是否有权限修改项目内容
		// ApiLoveGroupMent ment = new ApiLoveGroupMent();
		// ment.setProjectId(form.getItemId());
		// ment.setUserId(userId);
		// ApiPage<ApiLoveGroupMent> r1 = projectFacade.queryLoveGroupMentList(
		// ment, 1, 1);
		// if (r1 == null || r1.getTotal() == 0) {
		// return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "您没有权限修改项目内容",
		// null);
		// }
		ApiProject apiProject = projectFacade.queryProjectDetail(form
				.getItemId());
		if (apiProject == null) {
			return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "没有对应项目",
					null);
		}
		apiProject.setTitle(form.getTitle());
		apiProject.setContent(form.getInformation());
		apiProject.setContentImageId(form.getImagesId());
		apiProject.setLastUpdateTime(new Date());
		ApiResult r = projectFacade.updateProject(apiProject);
		if (r.getCode() == 1) {
			return webUtil.successRes(null);
		} else {
			return webUtil
					.failedRes(webUtil.ERROR_CODE_ADDFAILED, "修改失败", null);
		}
	}

	/**
	 * 提交面试报告
	 * 
	 * @param ProjectForm
	 * @return
	 */
	@RequestMapping(value = "coredata/checkReport")
	@ResponseBody
	public Map<String, Object> checkReport(ProjectForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		if (form.getItemId() == 0) {
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误",
					null);
		}
		// 校验当前用户是否有权限审核
		// ApiLoveGroupMent ment = new ApiLoveGroupMent();
		// ment.setProjectId(form.getItemId());
		// ment.setUserId(userId);
		// ApiPage<ApiLoveGroupMent> result = projectFacade
		// .queryLoveGroupMentList(ment, 1, 1);
		// if (result == null || result.getTotal() == 0) {
		// return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "您没有权限提交面试报告",
		// null);
		// }
		// 调用审核接口
		ApiProjectSchedule schedule = new ApiProjectSchedule();
		// schedule.setDescription(form.getReason());
		schedule.setState(220);
		schedule.setProjectId(form.getItemId());
		schedule.setOperatorTime(new Date());
		schedule.setOperator(userId);
		ApiResult r = projectFacade.saveProjectSchedule(schedule);

		ApiReport report = new ApiReport();
		report.setContent(form.getReason());
		report.setProjectId(form.getItemId());
		report.setType(3);
		report.setReportPeople(userId);
		ApiResult r2 = projectFacade.saveReport(report);

		if (r.getCode() == 1) {
			return webUtil.successRes(null);
		} else {
			return webUtil
					.failedRes(webUtil.ERROR_CODE_ADDFAILED, "提交失败", null);
		}
	}

	/**
	 * 结束项目
	 * 
	 * @param ProjectForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/endItem")
	public Map<String, Object> endItem(ProjectForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiProject ap = new ApiProject();
		ap.setId(form.getItemId());
		ApiPage<ApiProject> result = projectFacade.queryProjectList(ap, 1, 1);
		if (result == null || result.getTotal() == 0) {
			return webUtil
					.failedRes(webUtil.ERROR_CODE_DATAWRONG, "数据错误", null);
		}
		ApiProject project = result.getResultData().get(0);
		// if(project.getState()!=ProjectConstant.PROJECT_STATUS_EXECUTE){
		// return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"非执行状态，不能结束项目",
		// null);
		// }
		// 校验当前用户是否有权限审核
		// ApiLoveGroupMent ment = new ApiLoveGroupMent();
		// ment.setProjectId(form.getItemId());
		// ment.setUserId(userId);
		// ApiPage<ApiLoveGroupMent> r1 = projectFacade.queryLoveGroupMentList(
		// ment, 1, 1);
		// if (r1 == null || r1.getTotal() == 0) {
		// return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "您没有权限结束项目",
		// null);
		// }

		project = new ApiProject();
		project.setId(form.getItemId());
		project.setState(ProjectConstant.PROJECT_STATUS_DONE);
		ApiResult result2 = projectFacade.updateProject(project);

		// todo 后期修改
		ApiReport report = new ApiReport();
		report.setProjectId(form.getItemId());
		report.setType(5);
		report.setContent(form.getReason());
		report.setReportPeople(userId);
		report.setOperatorTime(new Date());

		ApiResult result3 = projectFacade.saveReport(report);

		if (result2.getCode() == 1) {
			return webUtil.resMsg(1, "0000", "成功", null);
		} else {
			return webUtil.resMsg(0, "0006", "更改失败", null);
		}
	}

	/**
	 * 用户中心善管家
	 * 
	 * @param page
	 *            第几页
	 * @param pageNum
	 *            每页几条
	 * @param type
	 *            类型，进度
	 * @return
	 */
	@RequestMapping(value = "core/myloveGroup")
	@ActionLog(content = "查看善管家")
	public ModelAndView myloveGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/my_loveGroup");
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		view.addAllObjects(countProjectByState(userId, projectFacade));
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		if (user1.getLoveState() == 201) {// 未审核
			view.addObject("mylovegroupStatus", 1);
		} else if (user1.getLoveState() == 203) {// 通过
			view.addObject("mylovegroupStatus", 2);
		}

		view = myhlep(view, userId);
		Map<String, Integer> nums = countProjectByState(userId, projectFacade);
		view.addAllObjects(nums);
		view.addObject("guideName", "善管家");
		return view;
	}

	/**
	 * 获取善管家各状态项目的数目
	 * 
	 */
	@RequestMapping(value = "coredata/myloveGroupNums")
	@ResponseBody
	public Map<String, Object> myloveGroupNums(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		Map<String, Integer> nums = countProjectByState(userId, projectFacade);
		return webUtil.successRes(nums);
	}

	private Map<String, Integer> countProjectByState(int userId,
			IProjectFacade projectFacade) {
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		List<ApiProject> apipage = projectFacade
				.countProjectBystate(apiProject);
		int s = 0, m = 0, b = 0, e = 0;
		if (apipage != null && apipage.size() > 0) {
			for (ApiProject p : apipage) {
				if (ProjectConstant.PROJECT_STATUS_AUDIT1 == p.getState()
						|| ProjectConstant.PROJECT_STATUS_AUDIT2 == p
								.getState()) {
					s += p.getNumber();
				} else if (ProjectConstant.PROJECT_STATUS_COLLECT == p
						.getState()
						|| ProjectConstant.PROJECT_STATUS_EXECUTE == p
								.getState()) {
					m += p.getNumber();
				} else if (ProjectConstant.PROJECT_STATUS_BACK == p.getState()) {
					b += p.getNumber();
				} else if (ProjectConstant.PROJECT_STATUS_DONE == p.getState()) {
					e += p.getNumber();
				}
			}
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("audits", s);
		result.put("collect", m);
		result.put("back", b);
		result.put("end", e);
		return result;
	}

	/**
	 * 
	 * 求助列表页面
	 * 
	 */
	@RequestMapping("pindex")
	public ModelAndView projectIndex(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="flag",required=false,defaultValue="") String flag) {
		ModelAndView view = null;
		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "先登入");
			view.addObject("errorcode", -1);
			return view;
		}
		view = new ModelAndView("seekhelp/seekhelp_list");
		/*ApiFrontUser user = new ApiFrontUser();
		user.setId(new Integer(uid));
		user = userFacade.queryPersonCenter(user);
		view.addObject("user", user);*/
		ApiFrontUser user1 = userFacade.queryById(new Integer(uid));
		if (user1.getLoveState() == 201 || user1.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		view.addObject("user", user1);
		view.addObject("helpStatus", 1);
		if(flag != null && flag.equals("withdraw")){
			view.addObject("guideName", "我要提现");
		}
		if(flag != null && flag.equals("realtionProject")){
			view.addObject("guideName", "相关项目");//用于导航标签
		}
		if(!flag.equals("withdraw") && !flag.equals("realtionProject")){
			view.addObject("guideName", "我的求助");//用于导航标签
		}
		view.addObject("flag", flag);//用于左边菜单选中
		return view;
	}

	/**
	 * 
	 * 求助详情页面
	 * 
	 */
	@RequestMapping("pdetail")
	public ModelAndView projectDetail(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = null;
		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "先登入");
			view.addObject("errorcode", "0000");
			return view;
		}
		view = new ModelAndView("seekhelp/seekhelp_detail");
		if (projectId == null || projectId == 0) {
			view.addObject("errormsg", "参数错误");
			view.addObject("errorcode", "0001");
			return view;
		}
		ApiProject ap = new ApiProject();
		ap.setId(projectId);
		ap.setUserId(new Integer(uid));
		ApiPage<ApiProject> result = projectFacade.queryProjectList(ap, 1, 1);
		if (result == null || result.getTotal() == 0) {
			view.addObject("errormsg", "没有对项目");
			view.addObject("errorcode", "0002");
			return view;
		}
		view.addObject("project", result.getResultData().get(0));
		return view;
	}

	/**
	 * 
	 * 求助发起页面
	 * 
	 */
	@RequestMapping("/pcreate")
	@ActionLog(content = "发起求助")
	public ModelAndView projectCreate(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = null;
		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "先登入");
			view.addObject("errorcode", "0000");
			return view;
		}
		// 2.验证是否实名验证
		ApiFrontUser user = userFacade.queryById(new Integer(uid));
		if (user == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "用户无效");
			view.addObject("errorcode", "0001");
			return view;
		}
		if (user.getRealState() == 200 || user.getRealState() == 202) {
			// 未实名验证或实名验证未通过，跳实名验证页面
			view = new ModelAndView("steward/applyLegalize");
			view.addObject("legalize", 3);
			return view;
		}
		view = new ModelAndView("seekhelp/seekhelp_create");
		view.addObject("username", user.getUserName());
		view.addObject("userState", user.getRealState());
		return view;
	}

	/**
	 * 
	 * 求助编辑页面
	 * 
	 */
	@RequestMapping("pedit")
	public ModelAndView projectEdit(
			@RequestParam(value = "projectId") Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = null;
		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "先登入");
			view.addObject("errorcode", "0000");
			return view;
		}
		// 2.验证是否实名验证
		ApiFrontUser user = userFacade.queryById(new Integer(uid));
		if (user == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg", "用户无效");
			view.addObject("errorcode", "0001");
			return view;
		}
		if (user.getRealState() == 200 || user.getRealState() == 202) {
			view = new ModelAndView("steward/applyLegalize");
			view.addObject("legalize", 3);
			return view;
		}
		view = new ModelAndView("seekhelp/seekhelp_edit");
		if (projectId != null && projectId != 0) {
			ApiProject ap = projectFacade.queryProjectDetail(projectId);
			if (ap != null) {
				Appeal appel = new Appeal();
				appel.setTitle(ap.getTitle());
				appel.setContent(ap.getContent());
				if (StringUtils.isEmpty(ap.getLocation())) {
					appel.setLocation(null);
				} else {
					String[] temp = StringUtils.split(ap.getLocation(), "#");
					if (temp.length != 3) {
						appel.setLocation(null);
					} else {
						appel.setLocation(temp[1]);
					}
				}
				appel.setDetailAddress(ap.getDetailAddress());
				List<ApiBFile> bfiles = ap.getBfileList();
				if(bfiles!=null&&bfiles.size()>0){
					List<HomeFile> imgs = new ArrayList<HomeFile>(bfiles.size());
					HomeFile img = null;
					for(ApiBFile b:bfiles){
						img = new HomeFile();
						img.setId(b.getId());
						img.setUrl(b.getUrl());
			            imgs.add(img);
					}
					appel.setImgs(imgs);
				}
				appel.setCryMoney(ap.getCryMoney());
				appel.setField(ap.getField());
				appel.setAccountName(ap.getAccountName());
				appel.setAccountBank(ap.getAccountBank());
				appel.setCollectNum(ap.getCollectNum());
				appel.setId(ap.getId());
				if (ap.getDeadline() != null) {
					appel.setDeadline(ap.getDeadline().getTime());
					appel.setDeadDate(ap.getDeadline());
				}
				if (ap.getIdentity() != null
						&& ap.getIdentity().indexOf("otherCaller") > -1) {
					String[] strs = StringUtils.split(ap.getIdentity(), ",");
					if (strs.length == 1) {
						appel.setIdentity(strs[0]);
					} else {
						appel.setIdentity(strs[0]);
						appel.setIdentityInfo(strs[1]);
					}
				} else {
					appel.setIdentity(ap.getIdentity());
				}

				view.addObject("project", appel);
				view.addObject("projectId", projectId);
			}
		}
		view.addObject("user", user);
		return view;
	}

	@RequestMapping(value = "psuccess")
	public ModelAndView psuccess(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("seekhelp/seekhelp_success");
		ApiFrontUser user = new ApiFrontUser();
		user.setId(userId);
		user = userFacade.queryPersonCenter(user);
		view.addObject("user", user);
		return view;
	}

	/*
	 * 银行卡的绑定
	 * 
	 * @param cardNo 卡号
	 * 
	 * @param bankName 开户行
	 * 
	 * @param province 开户行所在省
	 * 
	 * @param city 开户行所在市
	 * 
	 * @param bankType 卡类型
	 */
	@RequestMapping(value = "coredata/boundbankcard")
	@ResponseBody
	public Map<String, Object> BoundBankCard(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiUserCard apiUserCard = new ApiUserCard();
		if (StringUtils.isEmpty(usercard.getBankName())
				|| StringUtils.isEmpty(usercard.getCardNo())
				|| StringUtils.isEmpty(usercard.getProvince())
				|| StringUtils.isEmpty(usercard.getBankType())) {

			return webUtil.failedRes("0009", "参数错误", null);
		} else {
			String[] adr = usercard.getProvince().split(" ");
			if (adr.length != 3) {
				return webUtil.failedRes("0009", "参数错误", null);
			} else {
				usercard.setCity(adr[1]);
				usercard.setProvince(adr[0]);
				usercard.setArea(adr[2]);
			}
		}
		/*if (usercard.getPid() != null) {
			ApiProjectUserInfo ao = new ApiProjectUserInfo();
			ao.setProjectId(usercard.getPid());
			List<ApiProjectUserInfo> list = projectFacade.queryProjectUserInfoList(ao);
			System.out.println("list.size()>>>>>>>>>>>>"+list.size());
			int count= 0;
			for (int i = 0; i < list.size(); i++) {
				ao = list.get(i);
				if (ao.getPersonType() != 1) {
					if(ao.getRealName().trim().equals(usercard.getName().trim())){
						count++;
					}
				}
			}
//			if(usercard.getAccountType() != 0){
//				if (count == 0) {
//					return webUtil.failedRes("90012", "开户名与受助人,发布人的姓名不一样", null);
//				}
//			}
			
		}else{
			return webUtil.failedRes("90013", "没有指定的提款项目，不能绑定银行卡", null);
		}*/
		if(usercard.getPid()==null){
			return webUtil.failedRes("90013", "没有指定的提款项目，不能绑定银行卡", null);
		}
		apiUserCard.setUserId(userId);
		apiUserCard.setBankName(usercard.getBankName());
		apiUserCard.setCard(usercard.getCardNo());
		apiUserCard.setCity(usercard.getCity());
		apiUserCard.setArea(usercard.getArea());
		apiUserCard.setUseState(100);
		apiUserCard.setBankType(usercard.getBankType());
		apiUserCard.setProvince(usercard.getProvince());
		apiUserCard.setAccountName(usercard.getName());
		apiUserCard.setAccountType(usercard.getAccountType());
		//增加projectId
		apiUserCard.setProjectId(usercard.getPid());
		ApiResult res = userFacade.saveNewUserCard(apiUserCard);
		if (res != null && res.getCode() == 1) {
			return webUtil.successRes(null);
		} else if (res.getCode() == 10003) {
			return webUtil.failedRes("0009", "银行卡超出限制", null);
		} else if (res.getCode() == 10004) {
			return webUtil.failedRes("0009", "银行卡已绑定", null);
		} else {
			return webUtil.failedRes("0009", "参数错误", null);
		}
	}

	/*
	 * 获取绑定的银行卡List
	 * 
	 * @param 根据用户ID
	 */
	@RequestMapping(value = "coredata/boundbankcardlist")
	@ResponseBody
	public Map<String, Object> BoundBankCardList(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> apiPage = userFacade.queryUserCardList(
				apiUserCard, usercard.getPage(), usercard.getPageSize());
		List<ApiUserCard> list = apiPage.getResultData();
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		for (ApiUserCard card : list) {
			JSONObject item = new JSONObject();
			item.put("id", card.getId());
			item.put("bankName", card.getBankName());
			item.put("bankCard", StringUtil.getSafeNumber(card.getCard()));
			item.put("cardType", card.getBankType());
			item.put("isdefault", card.getIsSelected());
			items.add(item);
		}
		data.put("data", items);
		data.put("total", apiPage.getTotal());// 总页数
		return webUtil.successRes(data);
	}

	/*
	 * 设置默认银行卡
	 * 
	 * @param uid 是usercard表的主键
	 */
	@RequestMapping(value = "coredata/defaultbankcard")
	@ResponseBody
	public Map<String, Object> defaultBankcard(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		// 绑定表的主键ID改变表状态
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setId(usercard.getBid());
		apiUserCard.setIsSelected(0);
		ApiResult res = userFacade.resetUserCardSelected(apiUserCard);

		if (res != null && res.getCode() == 1) {
			return webUtil.successRes(null);
		} else {
			return webUtil.failedRes("0009", "参数错误", null);
		}
	}

	/*
	 * 解除绑定的银行卡
	 * 
	 * @param uid 是usercard表的主键
	 */
	@RequestMapping(value = "coredata/deletebankcard")
	@ResponseBody
	public Map<String, Object> deleteBankCard(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		// 绑定表的主键ID改变表状态
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setUseState(102);
		apiUserCard.setId(usercard.getBid());
		ApiResult res = userFacade.updateUserCard(apiUserCard);

		if (res != null && res.getCode() == 1) {
			return webUtil.successRes(null);
		} else {
			return webUtil.failedRes("0009", "参数错误", null);
		}
	}

	/*
	 * 提款的页面
	 */
	@RequestMapping(value = "core/viewWithdrawDeposit")
	@ActionLog(content = "查看提款")
	public ModelAndView ViewWithdrawDeposit(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/w_quick");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiUserCard apiUserCard = new ApiUserCard();
		// 如果用户通过点击提款按钮来操作，自动填写已选的信息
		/** 页面的默认选择卡号 end **/
		if (usercard.getBid() != null) {
			apiUserCard.setUserId(userId);
			apiUserCard.setId(usercard.getBid());
			apiUserCard.setUseState(100);
		} else {
			apiUserCard.setUserId(userId);
			apiUserCard.setIsSelected(0);
			apiUserCard.setUseState(100);
		}
		ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(
				apiUserCard, usercard.getPage(), usercard.getPageSize());
		List<ApiUserCard> mlist = mapiPage.getResultData();
		if (mlist.size() > 0) {
			apiUserCard = mlist.get(0);
			apiUserCard
					.setCard(StringUtil.getSafeNumber(apiUserCard.getCard()));
			apiUserCard.setBankName(BankConstants.BANK_IMAGE.get(apiUserCard
					.getBankName()));
			view.addObject("selectcard", apiUserCard);
		}
		/** 页面的默认选择卡号 end **/
		apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> apiPage = userFacade.queryUserCardList(
				apiUserCard, usercard.getPage(), usercard.getPageSize());
		List<ApiUserCard> list = apiPage.getResultData();
		for (int i = 0; i < list.size(); i++) {
			ApiUserCard card = list.get(i);
			card.setCard(StringUtil.getSafeNumber(card.getCard()));
			card.setBankName(BankConstants.BANK_IMAGE.get(card.getBankName()));
		}
		view.addObject("cardList", list);
		view.addObject("size", list.size());
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}

	/*
	 * 提款申请
	 * 
	 * @param uid 是usercard表的主键
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/WithdrawDeposit")
	public Map<String, Object> withdrawDeposit(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		ApiPayMoneyRecord apiPayMoneyRecord = new ApiPayMoneyRecord();
		apiPayMoneyRecord.setApplyMoney(usercard.getMoney());
		Integer userId = UserUtil.getUserId(request, response);
		// 没有登录
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setId(usercard.getBid());
		if(usercard.getPid() != null ){
			ApiProject apiProject = projectFacade.queryProjectDetail(usercard.getPid());
			if(apiProject == null){
				return webUtil.failedRes("0011", "没有这个项目", null);
			}
			/*if(!userId.equals(apiProject.getUserId())){
				return webUtil.failedRes("0010", "不是本人发起的项目", null);
			}*/
			apiUserCard.setProjectId(usercard.getPid());
		}else {
			return webUtil.failedRes("0009", "参数错误", null);
		}
		// 查看有没这张卡数据
		ApiPage<ApiUserCard> apiPage = userFacade.queryUserCardList(
				apiUserCard, usercard.getPage(), usercard.getPageSize());
		List<ApiUserCard> list = apiPage.getResultData();
		if (list.size() > 0) {
			ApiUserCard apcd = list.get(0);
			//-------------
			apcd.setBankName(usercard.getBankType());
			apcd.setCard(usercard.getCardNo());
			apcd.setAccountName(usercard.getBankName());
			ApiResult apiResult = userFacade.updateUserCard(apcd);
			//更新Card表
			apiPayMoneyRecord.setProjectId(usercard.getPid());
			apiPayMoneyRecord.setTranNum(StringUtil.uniqueCode());
			apiPayMoneyRecord.setRecipientName(apcd.getBankName());
			//TODO 增加开户名 accountName
			apiPayMoneyRecord.setAccountName(apcd.getAccountName());
			
			apiPayMoneyRecord.setAccount(apcd.getCard());
			apiPayMoneyRecord.setReceiptImageId(usercard.getImageId());
			apiPayMoneyRecord.setUserId(userId);
			apiPayMoneyRecord.setRecipientBankType(apcd.getBankType());
			apiPayMoneyRecord.setSource("PC");
		} else {
			return webUtil.failedRes("10001", "没有这张卡", null);
		}
		// 提款申请记入
		ApiResult res = payMoneyRecordFacade.drawMoney(apiPayMoneyRecord);
		if (res != null && res.getCode() == 1) {
			//短信通知
			ApiProjectUserInfo info = new ApiProjectUserInfo();
			info.setProjectId(usercard.getPid());
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
				announce.setContent(info.getRealName()+"，您好，您的筹款项目已发起一笔提款申请，提款金额："+usercard.getMoney()+"元，收款人："+list.get(0).getAccountName()+"，如有疑问请联系我们，电话0571-87165191，QQ：2777819027");
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

	/*
	 * 提款的数据记录的页面显示
	 */
	@RequestMapping(value = "core/WithdrawDepositData")
	@ActionLog(content = "查看提款的数据记录")
	public ModelAndView WithdrawDepositRecord(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/w_record");
		// 没有登录
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}

	/*
	 * 提款的数据记录，接口返回值
	 */
	@ResponseBody
	@RequestMapping(value = "core/WithdrawDepositRecordData")
	@ActionLog(content = "查看提款的数据记录")
	public Map<String, Object> WithdrawDepositData(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		// 没有登录
		if (userId == null) {
			return webUtil.loginFailedRes(1);
		}
		ApiPayMoneyRecord apiPayMoneyRecord = new ApiPayMoneyRecord();
		// 处理时间
		Date bdate = null;
		Date edate = new Date();
		if (usercard.getTypeDt() == 0) {
			// 3个月内
			bdate = DateUtil.add(edate, -3 * 30);
			apiPayMoneyRecord.setQueryStartDate(bdate);
		} else if (usercard.getTypeDt() == 1) {
			// 半年
			bdate = DateUtil.add(edate, -6 * 30);
			apiPayMoneyRecord.setQueryStartDate(bdate);
		} else if (usercard.getTypeDt() == 2) {
			// 一年
			bdate = DateUtil.add(edate, -12 * 30);
			apiPayMoneyRecord.setQueryStartDate(bdate);
		} else {
			// 全部
		}
		apiPayMoneyRecord.setUserId(userId);
		apiPayMoneyRecord.setQueryEndDate(edate);

		// 提款申请记入
		ApiPage<ApiPayMoneyRecord> apiPage = payMoneyRecordFacade
				.queryPayMoneyRecordList(apiPayMoneyRecord, usercard.getPage(),
						usercard.getPageSize());
		List<ApiPayMoneyRecord> list = apiPage.getResultData();
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		for (ApiPayMoneyRecord record : list) {
			JSONObject item = new JSONObject();
			item.put("createtime", record.getPayMoneyTime());
			item.put("bank", record.getRecipientName());
			item.put("bankNumber",StringUtil.getSafeNumber(record.getAccount()));
			item.put("money", record.getApplyMoney());
			item.put("title", record.getProjectTitle());
			item.put("state", record.getState());
			item.put("trannum", record.getTranNum());
			item.put("paydate", record.getPayMoneyTime());
			items.add(item);
		}

		data.put("data", items);
		data.put("total", apiPage.getTotal());// 总行数
		data.put("pages", apiPage.getPages());// 总页数
		data.put("page", apiPage.getPageNum());// 第几页
		return webUtil.successRes(data);
	}

	/*
	 * 充值的数据记录
	 */
	@RequestMapping(value = "core/rechargeDepositData")
	@ActionLog(content = "查看充值的数据记录")
	public Map<String, Object> rechargeDepositData(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		// 没有登录
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
		apiCapitalinout.setUserId(userId);
		apiCapitalinout.setPayState(302);
		// 提款申请记入
		ApiPage<ApiCapitalinout> apiPage = payMoneyRecordFacade
				.queryCapitalinout(apiCapitalinout, usercard.getPage(),
						usercard.getPageSize());
		List<ApiCapitalinout> list = apiPage.getResultData();
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		for (ApiCapitalinout record : list) {
			JSONObject item = new JSONObject();
			item.put("createtime", record.getCreateTime());
			item.put("paytype", record.getPayType());
			item.put("money", record.getMoney());
			item.put("trannum", record.getTranNum());
			items.add(item);
		}
		data.put("data", items);
		data.put("total", apiPage.getTotal());// 总行数
		data.put("pages", apiPage.getPages());// 总页数
		data.put("page", apiPage.getPageNum());// 第几页
		return webUtil.successRes(data);
	}

	/*
	 * 卡管理的页面
	 */
	@RequestMapping(value = "core/WithdrawDepositManage")
	@ActionLog(content = "查看卡")
	public ModelAndView WithdrawDepositManage(UserCard usercard,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/w_manage");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiCompany acy = new ApiCompany();
		acy.setUserId(userId);
		acy = companyFacade.queryCompanyByParam(acy);
		view.addObject("realname", acy.getName());
		return view;
	}

	/*
	 * 企业网银支付的页面
	 */
	@RequestMapping(value = "core/chargeonlineBank")
	@ActionLog(content = "查看企业网银支付的页面")
	public ModelAndView chargeonlineBank(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_onlineBank");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "充值");
		return view;
	}

	/*
	 * 企业银行汇款的页面
	 */
	@RequestMapping(value = "core/chargeTransfer")
	@ActionLog(content = "查看企业银行汇款")
	public ModelAndView chargeTransfer(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_transfer");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "充值");
		return view;
	}

	/*
	 * 企业支付宝的页面
	 */
	@RequestMapping(value = "core/chargeAlipay")
	@ActionLog(content = "查看企业支付宝")
	public ModelAndView chargeAlipay(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_alipay");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "充值");
		return view;
	}

	/*
	 * 企业快捷支付的页面
	 */
	@RequestMapping(value = "core/chargeQuick")
	@ActionLog(content = "查看企业快捷支付")
	public ModelAndView chargeQuick(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_quick");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}

	/*
	 * 企业充值记录的页面
	 */
	@RequestMapping(value = "core/chargeRecord")
	@ActionLog(content = "查看企业充值记录")
	public ModelAndView chargeRecord(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_record");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "充值");
		return view;
	}

	/*
	 * 企业充值记录的数据接口
	 */
	@ResponseBody
	@RequestMapping(value = "core/chargeRecordData")
	public Map<String, Object> chargeRecordData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("deposit/enterprise/c_record");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return webUtil.loginFailedRes(null);
		}
		// 处理时间
		Date bdate = null;
		Date edate = new Date();
		if (type == 0) {
			// 3个月内
			bdate = DateUtil.add(edate, -3 * 30);
		} else if (type == 1) {
			// 半年
			bdate = DateUtil.add(edate, -6 * 30);
		} else if (type == 2) {
			// 一年
			bdate = DateUtil.add(edate, -12 * 30);
		} else {
			// 全部
		}

		ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
		if (type == 0 || type == 1 || type == 2) {
			apiCapitalinout.setQueryStartDate(bdate);
			apiCapitalinout.setQueryEndDate(edate);
		}
		apiCapitalinout.setUserId(userId);
		apiCapitalinout.setInType(1);
		apiCapitalinout.setPayState(302);
		ApiPage<ApiCapitalinout> ap = companyFacade.queryCapitalinoutList(
				apiCapitalinout, page, pageNum);
		double TchargeMoney = companyFacade
				.queryCapitalinoutListCountMoeny(apiCapitalinout);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TchargeMoney", TchargeMoney);
		map.put("data", ap);
		return webUtil.successRes(map);
	}
	
	/*
	 * 个人账户中心的页面
	 */
	@RequestMapping(value = "core/personalCenter")
	@ActionLog(content = "查看个人账户中心")
	public ModelAndView personalCenter(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/pCenter");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		//可提取金额
		view.addObject("tiqu", 0);
		view.addObject("guideName", "首页");
		// 企业余额(账户余额)
		if (user.getCoverImageId() == null) {
			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
		} else {
			ApiBFile aBFile = fileFacade.queryBFileById(user.getCoverImageId());
			user.setCoverImageUrl(aBFile.getUrl());// 个人头像
		}
		view.addObject("user", user);
//		ApiSystemNotify systemNotify = new ApiSystemNotify();
//		systemNotify.setUserId(userId);
//		systemNotify.setSender("获捐消息");
//		systemNotify.setState(0);
//		//获捐次数
//		view.addObject("hqread", systemNotifyFacade.countRead(systemNotify));
//		systemNotify = new ApiSystemNotify();
//		systemNotify.setSender("系统消息");
//		systemNotify.setUserId(userId);
//		systemNotify.setState(0);
		//未读消息
//		view.addObject("newsRead", systemNotifyFacade.countRead(systemNotify));
		//项目反馈数
		ApiProjectFeedback feedback = new ApiProjectFeedback();
		feedback.setFeedbackPeople(userId);
		feedback.setAuditState(203);
		Date edate = DateUtil.add(new Date(), -7);
		feedback.setQueryStartDate(edate);
		ApiPage<ApiProjectFeedback> ap = projectFacade.queryCareProjectFeedbckByCondition(feedback, 1, 1);
		view.addObject("fktotal", ap.getTotal());
		//我的助善
		ApiDonateRecord apiDonateRecord = new ApiDonateRecord();
		apiDonateRecord.setUserId(userId);
		apiDonateRecord.setCompanyId(user.getCompanyId());
		ApiPage<ApiDonateRecord> apm = companyFacade.queryCapitalinoutListByCompanyHelp(apiDonateRecord, 1, 1);
		view.addObject("zstotal", apm.getTotal());
		//查询捐款次数
		ApiDonateRecord r = new ApiDonateRecord();
		r.setUserId(userId);
		r.setState(302);
		//ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, 1, 5);
		// 捐款总金额
		//Double  totalAmount = donateRecordFacade.countQueryByCondition(r);
		Double totalAmount = donateRecordFacade.countQueryDonateRecordList(r);
		// 捐款次数
		Integer donationNum = donateRecordFacade.countNumQueryByCondition(r);
		view.addObject("totalAmount", totalAmount);
		view.addObject("donationNum", donationNum);
		

		return view;
	}

	/*
	 * 企业账户中心的页面
	 */
	@RequestMapping(value = "core/enterpriseCenter")
	@ActionLog(content = "查看企业账户中心")
	public ModelAndView enterpriseCenter(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/eCenter");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		if(user != null && StringUtils.isEmpty(user.getCoverImageUrl())){
			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 用户头像
		}
		view.addObject("user", user);
		view.addObject("guideName", "首页");
		ApiCompany company = new ApiCompany();
		company.setUserId(userId);
		//可提取金额
		view.addObject("tiqu", 0);
		// 企业余额(账户余额)
		company = companyFacade.queryCompanyByParam(company);
		if (company == null || company.getCoverImageUrl() == null) {
			view.addObject("conpanyImageUrl",
					"http://www.17xs.org/res/images/user/4.jpg"); // 企业头像
		} else {
			if(company.getType() == 0){
				view.addObject("entType", "企业");
			}else if (company.getType() == 1) {
				view.addObject("entType", "事业单位");
			}else if (company.getType() == 2) {
				view.addObject("entType", "社会团体");
			}else if (company.getType() == 3){
				view.addObject("entType", "党政及国家机关");
			}
			view.addObject("conpanyImageUrl", company.getCoverImageUrl()); // 企业头像
		}
		view.addObject("company", company);
		ApiCompanyDto com = companyFacade.queryCompanyCenterHeadInfo(userId);
		if (com.getBalanbe() == null) {
			view.addObject("balance", 0.00);// 企业的余额
		} else {
			view.addObject("balance", com.getBalanbe());// 企业的余额
		}
		view.addObject("employee", com.getEmployeeNumber());// 企业员工数
		view.addObject("donatCount", com.getDonateNumber()); // 个人的捐助次数
		if (com.getDonateTotalAmount() == null) {
			view.addObject("donatMoney", user.getTotalAmount());// 个人的捐助金额
		} else {
			view.addObject("donatMoney", com.getDonateTotalAmount());// 个人的捐助金额
		}
		view.addObject("companyGoodCount", com.getGoodHelpNumber());// 助善次数
		view.addObject("companydonatCount", com.getTotalCallNumber());// 企业的号召网友
		view.addObject("companydonatMoney", com.getGoodHelpTotalAmount());// 企业的助善金额
		view.addObject("newsRead", systemNotifyFacade.countRead(userId, 0));
		//查询捐款次数
		ApiDonateRecord r = new ApiDonateRecord();
		r.setUserId(userId);
		r.setState(302);
		//ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(r, 1, 5);
		// 捐款总金额
		Double  totalAmount = donateRecordFacade.countQueryByCondition(r);
		// 捐款次数
		Integer donationNum = donateRecordFacade.countNumQueryByCondition(r);
		view.addObject("totalAmount",totalAmount);
		view.addObject("donationNum",donationNum);
		//查询助善次数
		ApiCompany_GoodHelp goodHelp = new ApiCompany_GoodHelp();
		goodHelp.setUserId(userId);
		goodHelp.setPayState(302);
		ApiPage<ApiCompany_GoodHelp> result = companyFacade
				.queryCompanyGoodHelpList(goodHelp, 1, 5);
		view.addObject("agList", result.getTotal());
		return view;
	}

	/*
	 * 个人中心的详细信息修改
	 */
	@ResponseBody
	@RequestMapping("coredata/personalUserInfo")
	public Map<String, Object> personalUserInfo(HttpServletRequest request,
			HttpServletResponse response,PUser puser) {
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = userFacade.queryById(userId);
		user.setUserName(puser.getName());
		user.setRealName(puser.getRealname());
		user.setNickName(puser.getNickName());
		user.setMobileNum(puser.getPhone());
		user.setPersition(puser.getVocation());//职业
		user.setWorkUnit(puser.getWorkUnit());
		user.setQqOrWx(puser.getWeixin());
		user.setFamilyAddress(puser.getDetailAddress());
		
		if (!"false".equals(puser.getAddress())) {
			String address[] = puser.getAddress().split("#");
			String adr1[] = address[0].split(" ");
			user.setProvince(adr1[0]);
			user.setCity(adr1[1]);
			user.setArea(adr1[2]);
			user.setHj_area(address[1]);
		}
		ApiResult result = userFacade.updateUser(user);
		if(result.getCode()==1&&StringUtils.isNotEmpty(puser.getPassWord())){
			user.setUserPass(puser.getPassWord());
			userFacade.resetFrontUser(user);
			SSOUtil.login(user, request, response);
		}
		return webUtil.successRes(result);
	}
	
	/*
	 * 获捐记录的页面
	 */
	@RequestMapping(value = "core/donatelistByuser")
	@ActionLog(content = "查看获捐记录")
	public ModelAndView donatelistByuserview(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("ucenter/use_querydonate");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		view.addObject("guideName", "获捐记录");
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}
	
	/*
	 * 获捐记录数据生成
	 */
	@ResponseBody
	@RequestMapping("coredata/donatelistByuser")
	public Map<String, Object> donatelistByuser(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "20") Integer page,
	        @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
		Integer userId = UserUtil.getUserId(request, response);
		ApiDonateRecord adr = new ApiDonateRecord();
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
		if(type ==1 || type ==2 || type ==3){
			adr.setQueryStartDate(bdate);
			adr.setQueryEndDate(edate);
		}
		adr.setUserId(userId);
		adr.setState(302);
		ApiPage<ApiDonateRecord> ap = donateRecordFacade.queryDonateRecordListByUser(adr, page, pageNum);
		return webUtil.successRes(ap);
	}
	
	
	/*
	 * 用户中心的密码修改
	 */
	@RequestMapping("core/uCenterPassword")
	@ActionLog(content = "修改密码")
	public ModelAndView uCenterPassword(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("ucenter/new_password");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("userType", user.getUserType());
		// 判断是否要显示我要求助的项目
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		if (user.getLoveState() == 201 || user.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		view.addObject("user", user);
		view.addObject("guideName", "修改密码");
		return view;
	}
	
	/*
	 * 取到企业的号召人数的详细信息
	 */
	@ResponseBody
	@RequestMapping("coredata/callList")
	public Map<String, Object> callList(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {
		
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
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = userFacade.queryById(userId);
		ApiDonateRecord apiDonateRecord = new ApiDonateRecord(); 
		apiDonateRecord.setUserId(userId);
		apiDonateRecord.setCompanyId(user.getCompanyId());
		apiDonateRecord.setQueryStartDate(bdate);
		apiDonateRecord.setQueryEndDate(edate);
		ApiPage<ApiDonateRecord> ap = companyFacade.queryCapitalinoutListByCompanyHelp(apiDonateRecord, page, pageNum);
		return webUtil.successRes(ap);
	}
	
	/**
	 * 个人的银行卡绑定页面，提款及资金管理页面
	 * 
	 * @return
	 */
	@RequestMapping("core/personalcapital")
	@ActionLog(content = "查看银行卡绑定")
	public ModelAndView personalcapital(@RequestParam(value = "projectId", required = false) Integer projectId,
			@RequestParam(value="flag",required=false) String flag,
			HttpServletRequest request,HttpServletResponse response) {
		ModelAndView view = new ModelAndView("ucenter/my_addcard");
		view.addObject("guideName", "提现记录");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		
		ApiUserCard apiUserCard = new ApiUserCard();
		apiUserCard.setUserId(userId);
		apiUserCard.setProjectId(projectId);
		apiUserCard.setIsSelected(0);
		apiUserCard.setUseState(100);
		ApiPage<ApiUserCard> mapiPage = userFacade.queryUserCardList(
				apiUserCard, 1, 3);
		List<ApiUserCard> mlist = mapiPage.getResultData();
		if(mlist.size() > 0 ){
			view = new ModelAndView("ucenter/my_capital");
			if(projectId == null){
				view.addObject("state", "0");
			}else {
				view.addObject("card", mlist.get(0));
				view.addObject("state", "1");
			}
		}else if(projectId == null) {
			view = new ModelAndView("ucenter/my_capital");
			view.addObject("state", "0");
		}
		// 判断是否要显示我要求助的项目
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		ApiFrontUser user = userFacade.queryById(userId);
		//用户的基本信息
		view.addObject("projectId", projectId);
		view.addObject("user", user);
		//flag用于左边菜单选中
		if(projectId != null){
			view.addObject("flag", flag);
		}
		return view;
	}
	
	/**
	 * 索取发票页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getInvoicePage")
	public ModelAndView getInvoicePage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("ucenter/invoice/getInvoice");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		view.addObject("guideName", "索取发票");
		ApiFrontUser user = userFacade.queryById(userId);
		ApiFrontUser_address r = new ApiFrontUser_address();
        r.setUserId(userId);
        List<ApiFrontUser_address> addresses = userRelationInfoFacade.queryUserAddress(r, 1, 10);
        if(addresses != null && addresses.size()>0){
        	view.addObject("address", addresses.get(0));
        }
		view.addObject("user", user);
		return view;
	}
	/**
	 * 发票详情页
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="getInvoiceDetail")
	public ModelAndView getInvoiceDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=true) Integer id){
		ModelAndView view = new ModelAndView("ucenter/invoice/invoice_detail");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		view.addObject("guideName", "发票详情");
		ApiFrontUser user = userFacade.queryById(userId);
		if(id == null){
			return null;
		}
		ApiFrontUser_invoice userInvoice = userRelationInfoFacade.queryInvoiceById(id);
		ApiFrontUser_address  userAddress = userRelationInfoFacade.queryById(userInvoice.getAddressId());
		String[] donateIds = userInvoice.getInfo().split("_");
		List<Integer> idList = new ArrayList<Integer>();
		if(donateIds != null && donateIds.length > 0){
			for (String donateId:donateIds) {
				if(StringUtils.isNotEmpty(donateId)){
					idList.add(Integer.parseInt(donateId));
				}
			}
		}
		ApiDonateRecord dr = new ApiDonateRecord();
		dr.setIdList(idList);
		List<ApiDonateRecord> list = donateRecordFacade.queryByIdList(dr);
		view.addObject("userAddress", userAddress);
		view.addObject("user", user);
		view.addObject("list", list);
		view.addObject("invoice", userInvoice);
		return view;
	}
	/**
	 * 开票记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getInvoicList")
	public ModelAndView getInvoicList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("ucenter/invoice/invoiceList");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		view.addObject("guideName", "开票记录");
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}
	@RequestMapping(value="ProjectVolunteerView")
	public ModelAndView ProjectVolunteerView(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "projectId", required = false) Integer projectId){
		ModelAndView view = new ModelAndView("ucenter/volunteer_list");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("projectId", projectId);
		return view;
	}
	@RequestMapping(value="ProjectCryPeopleView")
	public ModelAndView ProjectCryPeopleView(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "projectId", required = false) Integer projectId){
		ModelAndView view = new ModelAndView("ucenter/cryPeople_list");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("projectId", projectId);
		return view;
	}
	/**
	 * 志愿者列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getProjectVolunteer")
	@ResponseBody
	public Map<String, Object> getProjectVolunteer(HttpServletRequest request, HttpServletResponse response,
	        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
	        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
	        @RequestParam(value = "userId", required = false) Integer userId,
	        @RequestParam(value = "projectId", required = false) Integer projectId){
		/*
		if (userId == null)
        {
            // 1.验证是否登入
            userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
                return webUtil.loginFailedRes(null);
            }
        }
		*/
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiProjectVolunteer pv = new ApiProjectVolunteer();
        pv.setProjectId(projectId);
        ApiPage<ApiProjectVolunteer> pvs = projectVolunteerFacade.queryVolunteerList(pv,p.getPage(), p.getPageNum());
        if (pvs != null)
        {
        	p.setData(pvs.getResultData());
            p.setNums(pvs.getTotal());
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
            return webUtil.resMsg(1, "0000", "成功", p);
        }
	}
	/**
	 * 求助者列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getProjectCryPeople")
	@ResponseBody
	public Map<String, Object> getProjectCryPeople(HttpServletRequest request, HttpServletResponse response,
	        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
	        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
	        @RequestParam(value = "userId", required = false) Integer userId,
	        @RequestParam(value = "projectId", required = false) Integer projectId){
		if (userId == null)
        {
            // 1.验证是否登入
            userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
                return webUtil.loginFailedRes(null);
            }
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiProjectCryPeople pc = new ApiProjectCryPeople();
        pc.setProjectId(projectId);
        ApiPage<ApiProjectCryPeople> pcs = projectVolunteerFacade.queryCryPeopleList(pc,p.getPage(), p.getPageNum());
        if (pcs != null)
        {
        	p.setData(pcs.getResultData());
            p.setNums(pcs.getTotal());
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
            return webUtil.resMsg(1, "0000", "成功", p);
        }
	}
	/**
	 * 求助者详情
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="cryPeopleDetail")
	public ModelAndView cryPeopleDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=true) Integer id){
		ModelAndView view = new ModelAndView("ucenter/creyPeople_detail");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		ApiProjectCryPeople cp = projectVolunteerFacade.queryByCryPeopleId(id);
		view.addObject("cp", cp);
		view.addObject("user", user);
		return view;
	}
	/**
	 * 志愿者详情
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="volunteerDetail")
	public ModelAndView volunteerDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=true) Integer id){
		ModelAndView view = new ModelAndView("ucenter/volunteer_personal");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		ApiProjectVolunteer pv = projectVolunteerFacade.queryByVolunteerId(id);
		if(pv.getPersonType()==1){
			view = new ModelAndView("ucenter/volunteer_organization");
		}
		view.addObject("user", user);
		view.addObject("pv", pv);
		return view;
	}
	/**
	 * 修改用户详情信息页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getUserDetail")
	public ModelAndView getUserDetail(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("ucenter/user_detial");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}

}
