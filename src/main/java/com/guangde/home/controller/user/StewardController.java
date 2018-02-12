package com.guangde.home.controller.user;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiFrontUser;
import com.guangde.home.annotation.ActionLog;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("user")
public class StewardController{
	
	private final Logger logger = LoggerFactory.getLogger(StewardController.class);
	@Autowired 
	private IUserFacade userFacade;	
	
	/**
	 * 申请加入善管第一步
	 * 
	 * @return
	 */
	@RequestMapping(value = "steward/first")
	@ActionLog(content = "申请加入善管第一步")
	public ModelAndView first() {
		ModelAndView view = new ModelAndView("steward/applyLegalize");
		view.addObject("legalize", 2);
		return view;
	}
	
	@RequestMapping(value = "steward/second")
	@ActionLog(content = "申请加入善管第二步")
	public ModelAndView second(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView view = new ModelAndView("steward/applyInfo");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return  new ModelAndView("redirect:/user/sso/login.do");
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("userState", user.getRealState());
		return view;
	}
	
	/*
	 * 兴趣爱好提交
	 */
	@ResponseBody
	@RequestMapping(value = "steward/apply")
	@ActionLog(content = "兴趣爱好提交")
	public Map<String,Object> three(PUser user,HttpServletRequest request,HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.resMsg(-1, "0001", "未登陆", 0);
		}
		ApiFrontUser apiFrontUser = userFacade.queryById(userId);
		if(StringUtils.isNotEmpty(user.getAddress())){
			String[] adrs=user.getAddress().split("#")[0].split(" ");
			apiFrontUser.setProvince(adrs[0]);
			apiFrontUser.setCity(adrs[1]);
			apiFrontUser.setArea(adrs[2]);
			apiFrontUser.setFieldExpert(user.getField());
			apiFrontUser.setDescription(user.getReason());
			apiFrontUser.setLoveState(201);//提交未审核
			apiFrontUser.setContentImageId(user.getImageId());
			ApiResult result = userFacade.updateFrontUser(apiFrontUser);
			if (result != null && result.getCode() == 1) {
				return webUtil.resMsg(1, "0009", "审核中", 1);
			}else {
				return webUtil.resMsg(0, "0003", "添加失败", 2);
			}
		}else {
			return webUtil.resMsg(0, "0003", "添加失败", 2);
		}
	}
	
	@RequestMapping(value = "steward/three")
	@ActionLog(content = "申请加入善管第三步")
	public ModelAndView three(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView view = new ModelAndView("steward/applyResult");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return  new ModelAndView("redirect:/user/sso/login.do");
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}
	
	@RequestMapping(value = "steward/index.do")
	@ActionLog(content = "查看善管家首页")
	public ModelAndView steward(HttpServletRequest request,HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("redirect:/user/steward/first.do");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		//未提交
		int state = 200;
		if(user != null){
			state = user.getLoveState();
		}
		if(state == 203){
			view = new ModelAndView("/dogood/joinGood");
			view.addObject("state", state);
			return view;
//			return webUtil.resMsg(1, "0007", "审核通过", 4);
		}else if (state == 201) {
			//审核中
			view = new ModelAndView("redirect:/user/steward/three.do");
			return view;
		}else {
			if(user.getRealState() == 203 || user.getRealState() == 201){
				//兴趣填写
				view = new ModelAndView("redirect:/user/steward/second.do");
				return view;
			}else if (user.getRealState() == 201) {
				//实名认证审核中
				view = new ModelAndView("redirect:/user/realname.do");
				return view;
			}else {
				//未实名认证
				view = new ModelAndView("redirect:/user/steward/first.do");
				return view;
			}
		}
	}
	
}
