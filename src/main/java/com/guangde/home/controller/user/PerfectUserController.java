package com.guangde.home.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiAuditStaff;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiEmployee;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;

@Controller
@RequestMapping("user")
public class PerfectUserController{

	private final Logger logger = LoggerFactory
			.getLogger(PerfectUserController.class);

	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private ICompanyFacade companyFacade;

	/**
	 * 实名验证页面显示
	 * 
	 * @return
	 */
	@RequestMapping("realname")
	public ModelAndView sso(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView view = new ModelAndView("sso/perpect");
		view.addObject("guideName","实名认证");
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
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
		if(user.getLoveState() == 201 || user.getLoveState() == 203){
			view.addObject("mylovegroupStatus", 1);
		}
		//用户统计
		view.addObject("user", user);
		view.addObject("stauts", 1);
		// 手机绑定
		if (user.getMobileState() == 203) {
			view.addObject("state", 1);
		}
		if (user.getRealState() == 203) {
//			view.addObject("stauts", 3);
//			return view;
			//跳到个人完善信息页面
			ApiEmployee apiEmployee = new ApiEmployee();
			apiEmployee.setUserId(userId);
			ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(apiEmployee, 1,1);
			if(result.getTotal()==0){
				view.setViewName("ucenter/pInfoCreate");
				ApiFrontUser temp = new ApiFrontUser();
				temp.setRealState(203);
				temp.setUserType("enterpriseUsers");
				temp.setOrderBy("totalAmount");
				temp.setOrderDirection("desc");
				temp.setKey(203+"enterpriseUsers"+"totalAmount"+"desc");
				temp.setCache(true);
				temp.setValidTime(5*60l);
				view.addObject("companys",userFacade.queryCompanyUserList(temp, 1, 20).getResultData());
			}else{
				view.setViewName("ucenter/pInfoSuccess");
				apiEmployee = result.getResultData().get(0);
				if(!StringUtils.isBlank(apiEmployee.getProvince())){
					String[] temp = StringUtils.split(apiEmployee.getProvince(), "#");
					if (temp.length != 3) {
						apiEmployee.setProvince(null);
					} else {
						apiEmployee.setProvince(temp[0]);
					}
				}
				if(StringUtils.isBlank(apiEmployee.getCompanyName())){
					view.addObject("cState",-1);
				}else{
				//获取员工企业信息
				ApiFrontUser cUser = new ApiFrontUser();
				cUser.setUserName(apiEmployee.getCompanyName());
				cUser = userFacade.queryUserByParam(cUser);
				if(cUser==null){
					view.addObject("cState",0);//企业未注册
				}else{
					ApiCompany company = new ApiCompany();
					company.setUserId(cUser.getId());
					company = companyFacade.queryCompanyByParam(company);
					if(company!=null&&company.getState()==203){
						view.addObject("cState",2);//企业认证通过
					}else{
						view.addObject("cState",1);//企业未认证
						view.addObject("companyId", cUser.getId());
					}
				}
				}
				view.addObject("employee", apiEmployee);
			}
			return view;
		}else if(user.getRealState() == 201){
			view.addObject("stauts", 2);
			return view;
		}
		return view;
	}

	/**
	 * 完善信息
	 * 
	 * @param hUser
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("perfect")
	public JSONObject sso(PUser user,HttpServletRequest request,HttpServletResponse response) {
		ApiFrontUser frontUser = new ApiFrontUser();
		JSONObject jObject = new JSONObject();
		Integer userId = UserUtil.getUserId(request, response);
		//1:error 0:success
		jObject.put("reslut", "1");
		if (userId == null) {
			jObject.put("error", "请登录。");
			return jObject;
		}
		frontUser.setRealName(user.getRealname());
		frontUser.setIdCard(user.getIdCard());
		ApiFrontUser userFrom = userFacade.queryById(userId);
		if (userFrom.getMobileState() != 203) {
			frontUser.setMobileNum(user.getPhone());
			// 手机验证
			String phonekey = CodeUtil.certificationprex + user.getPhone();
			// 可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					user.getPhoneCode(), false);
			if (codeR == -1) {
				jObject.put("error", "手机验证码过期");
				return jObject;
			} else if (codeR == 0) {
				jObject.put("error", "手机验证码错误");
				return jObject;
			}
			frontUser.setMobileState(203);
		}
		// 需要检查的身份信息图片的ID
		String imageId = user.getImageId();
		// 检查有没上传3张图片，类型与图片个数一致
		if (StringUtils.isBlank(user.getImageId())) {
			jObject.put("error", "上传图片份数不对。");
			return jObject;
		}
		String[] imageType = user.getImageId().split(",");
		if ( imageType.length != 3) {
			jObject.put("error", "上传图片份数不对。");
			return jObject;
		}
		ApiAuditStaff auditStaff = new ApiAuditStaff();
		auditStaff.setUserId(userId);
		auditStaff.setPersonType("helpPeople");
		auditStaff.setFileId(imageId);
		ApiResult result2 = userFacade.realName(auditStaff, frontUser);
		if (result2 != null && result2.getCode() == 1) {
			jObject.put("reslut", "0");
		} else {
			jObject.put("error", result2.getMessage());
		}
		return jObject;
	}

}
