package com.guangde.home.controller.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.dto.ApiCapitalinoutDto;
import com.guangde.entry.ApiAuditStaff;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiEmployee;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectFeedback;
import com.guangde.home.constant.EnterpriseConstants;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Donation;
import com.guangde.home.vo.user.Activity;
import com.guangde.home.vo.user.Employe;
import com.guangde.home.vo.user.Enterprise;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;

@Controller
@RequestMapping("enterprise")
public class EnterpriseController {
	
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@RequestMapping(value = "core/realName")
	public ModelAndView enterpriseR(HttpServletRequest request,
			HttpServletResponse response) {
		  
		 ModelAndView  view = null;
		 Integer userId = UserUtil.getUserId(request, response);
		 
		 ApiCompany apiCompany = new ApiCompany();
		 apiCompany.setUserId(userId);
		 ApiFrontUser user = userFacade.queryById(userId);
		 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
		 //判断企业审核状态
		 if(company==null||company.getState()==EnterpriseConstants.STATUS_DRAFT){
			 //跳到企业认证创建页面
			 view =  new ModelAndView("enterprise/eCreate");
		 }else if(company.getState()==EnterpriseConstants.STATUS_AUDIT){
			 view =  new ModelAndView("enterprise/eAudit");
		 }else if(company.getState()==EnterpriseConstants.STATUS_BACK){
			 view =  new ModelAndView("enterprise/eBack");
			//查询失败原因
			 ApiAuditStaff audit = new ApiAuditStaff();
			 audit.setUserId(userId);
			 audit.setPersonType("company");
			 audit = userFacade.queryAuditStaffByParam(audit);
			 view.addObject("back", audit);
		 }else if(company.getState()==EnterpriseConstants.STATUS_SUCCESS){
			 dealCompany(company);
			 view =  new ModelAndView("enterprise/eSuccess");
		 }
		 //todo,状态错误页面
		 if(view==null){
			 view =  new ModelAndView("enterprise/eCreate");
		 }
		 view.addObject("user", user);
		 view.addObject("company", company);
		 view.addObject("guideName", "机构认证");
		 return view;
	}
	/**
	 * 
	 * 隐藏敏感信息
	 */
	private void dealCompany(ApiCompany comapny){
		if(comapny==null){
			return;
		}
		String temp = null;
		temp = comapny.getRegisterNo();
		if(!StringUtils.isBlank(temp)){
			if(temp.length()<=6){
				comapny.setRegisterNo("******");
			}else{
				comapny.setRegisterNo(temp.substring(0,temp.length()-6)+"******");
			}
		}
		temp = comapny.getGroupCode();
		if(!StringUtils.isBlank(temp)){
			int i = temp.indexOf("-");
			if(i!=-1){
				String temp1 = temp.substring(i,temp.length());
				temp = temp.substring(0, i);
				if(temp.length()<=4){
					temp = "****";
				}else{
					temp = (temp.substring(0,temp.length()-4)+"****");
				}
				comapny.setGroupCode(temp+temp1);
			}else{
				if(temp.length()<=4){
					temp = "****";
				}else{
					temp = (temp.substring(0,temp.length()-4)+"****");
				}
				comapny.setGroupCode(temp);
			}
		}
		temp = comapny.getLegalName();
		if(!StringUtils.isBlank(temp)){
			if(temp.length()<2){
				comapny.setLegalName("**");
			}else if(temp.length()==2){
				comapny.setLegalName(temp.substring(0,temp.length()-1)+"*");
			}else{
				comapny.setLegalName(temp.substring(0,temp.length()-2)+"**");
			}
		}
		temp = comapny.getIdentity();
		if(!StringUtils.isBlank(temp)){
		    if(temp.length()>9){
		    	comapny.setIdentity(temp.substring(0,9)+"*********");
		    }
		}
		temp = comapny.getHead();
		if(!StringUtils.isBlank(temp)){
			if(temp.length()<2){
				comapny.setHead("**");
			}else if(temp.length()==2){
				comapny.setHead(temp.substring(0,temp.length()-1)+"*");
			}else{
				comapny.setHead(temp.substring(0,temp.length()-2)+"**");
			}
		}
		temp = comapny.getMobile();
		if(!StringUtils.isBlank(temp)){
			if(temp.length()>4){
				int i = temp.length()/2;
				String temp1 = temp.substring(i+2, temp.length());
				temp = temp.substring(0,i-2);
				comapny.setMobile(temp+"****"+temp1);
			}
		}
	}

	@RequestMapping(value = "core/updateRealName")
	public ModelAndView rReAudit(HttpServletRequest request,
			HttpServletResponse response) {
		 ModelAndView  view = null;
		 Integer userId = UserUtil.getUserId(request, response);
		 ApiCompany apiCompany = new ApiCompany();
		 apiCompany.setUserId(userId);
		 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
		 //判断企业审核状态
		 if(company==null){
			 //跳到企业认证创建页面
			 view =  new ModelAndView("enterprise/eCreate");
		 }else if(company.getState()==EnterpriseConstants.STATUS_AUDIT){
			 view =  new ModelAndView("enterprise/eAudit");
		 }else if(company.getState()==EnterpriseConstants.STATUS_BACK){
			 view =  new ModelAndView("enterprise/eUpdate");
			
		 }else if(company.getState()==EnterpriseConstants.STATUS_SUCCESS){
			 view =  new ModelAndView("enterprise/eSuccess");
		 }
		 //todo,状态错误页面
		 if(view==null){
			 view =  new ModelAndView("enterprise/eCreate");
		 }
		 view.addObject("company", company);
		 return view;
	}
	
	@RequestMapping(value = "core/perfect")
	public ModelAndView enterprisePerfect(HttpServletRequest request,
			HttpServletResponse response) {
		 ModelAndView  view = null;
		 Integer userId = UserUtil.getUserId(request, response);
		 ApiCompany apiCompany = new ApiCompany();
		 apiCompany.setUserId(userId);
		 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
		 //判断企业审核状态
		 if(company==null){
			 //跳到企业认证创建页面
			 view =  new ModelAndView("enterprise/eCreate");
		 }else if(company.getState()==EnterpriseConstants.STATUS_AUDIT){
			 view =  new ModelAndView("enterprise/eAudit");
		 }else if(company.getState()==EnterpriseConstants.STATUS_BACK){
			 view =  new ModelAndView("enterprise/eBack");
			//查询失败原因
			 ApiAuditStaff audit = new ApiAuditStaff();
			 audit.setUserId(userId);
			 audit.setPersonType("company");
			 audit = userFacade.queryAuditStaffByParam(audit);
			 view.addObject("back", audit);
			
		 }else if(company.getState()==EnterpriseConstants.STATUS_SUCCESS){
			 view =  new ModelAndView("enterprise/ePerfect");
		 }
		 //todo,状态错误页面
		 if(view==null){
			 view =  new ModelAndView("enterprise/eCreate");
		 }
		 view.addObject("company", company);
		 return view;
	}
	
	@RequestMapping(value = "core/perfectOk")
	public ModelAndView enterprisePerfectOk(HttpServletRequest request,
			HttpServletResponse response) {
		 ModelAndView  view = null;
		 Integer userId = UserUtil.getUserId(request, response);
		 ApiCompany apiCompany = new ApiCompany();
		 apiCompany.setUserId(userId);
		 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
		 //判断企业审核状态
		 if(company==null){
			 //跳到企业认证创建页面
			 view =  new ModelAndView("enterprise/eCreate");
		 }else if(company.getState()==EnterpriseConstants.STATUS_AUDIT){
			 view =  new ModelAndView("enterprise/eAudit");
		 }else if(company.getState()==EnterpriseConstants.STATUS_BACK){
			 view =  new ModelAndView("enterprise/eBack");
			//查询失败原因
			 ApiAuditStaff audit = new ApiAuditStaff();
			 audit.setUserId(userId);
			 audit.setPersonType("company");
			 audit = userFacade.queryAuditStaffByParam(audit);
			 view.addObject("back", audit);
			
		 }else if(company.getState()==EnterpriseConstants.STATUS_SUCCESS){
			 if((company.getCoverImageId()!=null&&company.getCoverImageId()!=0)||!StringUtils.isBlank(company.getUrl())||!StringUtils.isBlank(company.getInfomation())||(company.getConpanyImageId()!=null&&company.getConpanyImageId()!=0)){
				 view =  new ModelAndView("enterprise/ePerfectOk");
			 }else{
				 view =  new ModelAndView("enterprise/ePerfect"); 
			 }
		 }
		 //todo,状态错误页面
		 if(view==null){
			 view =  new ModelAndView("enterprise/eCreate");
		 }
		 view.addObject("company", company);
		 return view;
	}
	
	/**
	 * 企业实名认证-->创建
	 * @param enterprise
	 * @param request
	 * @return
	 */
	@RequestMapping("coredata/realname")
	@ResponseBody
	public Map<String,Object> enterpriseRealName(@ModelAttribute Enterprise enterprise,HttpServletRequest request,HttpServletResponse response){
		if(!enterprise.verifyParameters()){
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"企业认证信息不完全",null);
		}
		String phonekey = CodeUtil.enterprise_validate+enterprise.getLinkPhone();
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		int codeR = CodeUtil.VerifiCode(phonekey, storeManage, enterprise.getPhoneCode(), false);
		if(codeR==-1){
			return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码过期", null); 
		}else if(codeR==0){
			return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码错误", null); 
		}
		Integer userId = UserUtil.getUserId(request, response);
		String userName = SSOUtil.getCurrentUserName(request, response);
		//1.校验当前状态
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setUserId(userId);
		ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
		if(company!=null){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"已存在企业认证信息", null);
		}
		//保存企业认证数据
		apiCompany.setType(enterprise.getType());
		apiCompany.setRegisterNo(enterprise.getIdentification());
		apiCompany.setServiceField(enterprise.getBusiness());
		apiCompany.setContentImageId(enterprise.getIdenId()+","+enterprise.getOrgaId()+","+enterprise.getLegalPimg1()+","+enterprise.getLegalPimg2());
		apiCompany.setGroupCode(enterprise.getOrganization());
		apiCompany.setLegalName(enterprise.getLegalPerson());
		apiCompany.setIdentity(enterprise.getLegalIdentify());
		apiCompany.setHead(enterprise.getLinkman());
		apiCompany.setMobile(enterprise.getLinkPhone());
		apiCompany.setUserId(userId);
		apiCompany.setName(userName);
		apiCompany.setState(EnterpriseConstants.STATUS_AUDIT);
		apiCompany.setMobileState(203);
		ApiResult result = companyFacade.saveCompany(apiCompany);
		if(result.getCode()==1){
			return webUtil.successRes(null);
		}else{
			return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED,"添加企业认证信息失败", null);
		}
	}
	/**
	 * 企业实名认证-->修改
	 * @param enterprise
	 * @param request
	 * @return
	 */
	@RequestMapping("coredata/updateRealName")
	@ResponseBody
	public Map<String,Object> eUpdateRealName(@ModelAttribute Enterprise enterprise,HttpServletRequest request,HttpServletResponse response){
		if(!enterprise.verifyParameters()){
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"企业认证信息不完全",null);
		}
		String phonekey = CodeUtil.enterprise_validate+enterprise.getLinkPhone();
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		int codeR = CodeUtil.VerifiCode(phonekey, storeManage, enterprise.getPhoneCode(), false);
		if(codeR==-1){
			return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码过期", null); 
		}else if(codeR==0){
			return webUtil.failedRes(webUtil.ERROR_CODE_VALIDATEWRONG,"手机验证码错误", null); 
		}
		Integer userId = UserUtil.getUserId(request, response);
		//1.校验当前状态
		ApiCompany  company = new ApiCompany();
		company.setId(enterprise.getVid());
		company.setUserId(userId);
		ApiPage<ApiCompany> list = companyFacade.queryCompanyList(company, 1, 1);
		if(list.getTotal()!=1||list.getResultData().get(0).getState()==EnterpriseConstants.STATUS_SUCCESS){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"企业认证信息不可修改", null);
		}
		//保存企业认证数据
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setId(enterprise.getVid());
		apiCompany.setType(enterprise.getType());
		apiCompany.setRegisterNo(enterprise.getIdentification());
		//todo 企业范围
		apiCompany.setContentImageId(enterprise.getIdenId()+","+enterprise.getOrgaId()+","+enterprise.getLegalPimg1()+","+enterprise.getLegalPimg2());
		apiCompany.setGroupCode(enterprise.getOrganization());
		apiCompany.setLegalName(enterprise.getLegalPerson());
		apiCompany.setIdentity(enterprise.getLegalIdentify());
		apiCompany.setHead(enterprise.getLinkman());
		apiCompany.setMobile(enterprise.getLinkPhone());
		apiCompany.setState(EnterpriseConstants.STATUS_AUDIT);
		apiCompany.setMobileState(203);
		apiCompany.setUserId(userId);
		ApiResult result = companyFacade.updateCompany(apiCompany);
		if(result.getCode()==1){
			return webUtil.successRes(null);
		}else{
			return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED,"修改企业认证信息失败", null);
		}
	}
	
	/**
	 * 企业实名认证-->完善信息
	 * @param enterprise
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("coredata/perfect")
	@ResponseBody
	public Map<String,Object> enterprisePerfect(@ModelAttribute Enterprise enterprise,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Integer userId = UserUtil.getUserId(request, response);
		//1.校验当前状态
		ApiCompany  company = new ApiCompany();
		company.setId(enterprise.getVid());
		company.setUserId(userId);
		ApiPage<ApiCompany> list = companyFacade.queryCompanyList(company, 1, 1);
		if(list.getTotal()!=1||list.getResultData().get(0).getState()!=EnterpriseConstants.STATUS_SUCCESS){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"企业认证后才能完善信息", null);
		}
		
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setId(enterprise.getVid());
		if(!StringUtils.isEmpty(enterprise.getHead())){
			apiCompany.setCoverImageId(new Integer(enterprise.getHead()));
		}
		if(!StringUtils.isEmpty(enterprise.getLogo())){
			apiCompany.setConpanyImageId(new Integer(enterprise.getLogo()));
		}
		apiCompany.setInfomation(enterprise.getInfo());
		apiCompany.setUrl(enterprise.getUrl());
		ApiResult result = companyFacade.updateCompany(apiCompany);
		if(result.getCode()==1){
			if(!StringUtils.isBlank(enterprise.getHead())&&!"0".equals(enterprise.getHead())){
				ApiFrontUser u = new ApiFrontUser();
				u.setId(userId);
				u.setCoverImageId(new Integer(enterprise.getHead()));
				result = userFacade.updateUser(u);
				if(result.getCode()==1){
					ApiBFile file = userFacade.queryBFileById(u.getCoverImageId());
					if(file!=null){
						SSOUtil.writeUserHead(response,file.getUrl());
					}
				}
			}
			return webUtil.successRes(null);
		}else{
			return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED,"修改企业认证信息失败", null);
		}
	}
	
	/**
	 * 获取企业员工接口
	 * @return
	 */
	@RequestMapping("/core/employeList")
	public ModelAndView employeList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view =  new ModelAndView("enterprise/employeList");
		view.addObject("userName", SSOUtil.getCurrentUserName(request, response));
		view.addObject("guideName", "善员工");
		return view;
	}
	
	
	/**
	 * 获取企业员工接口
	 * @return
	 */
	@RequestMapping("/coredata/employe")
	@ResponseBody
	public Map<String,Object> employe(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer pageIndex,
			@RequestParam(value = "pageNum", required = false,defaultValue="10") Integer pageNum){
		
		Integer userId = UserUtil.getUserId(request, response);
		ApiEmployee employee = new ApiEmployee();
		employee.setCompany_userId(userId);
		employee.setState(203);
		ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(employee, pageIndex, pageNum);
		Page  p = new Page();
		List<Employe> list = new ArrayList<Employe>();
		p.setPage(pageIndex);
		p.setPageNum(pageNum);
		p.setData(list);
		Employe temp = null;
		if(result!=null){
			for(ApiEmployee e:result.getResultData()){
			     temp = new Employe();
			     temp.seteId(e.getId());
			     temp.setbId(employee.getCompany_userId());
			     temp.setName(e.getNickName());
			     temp.setRealName(e.getRealName());
			     //temp.setLevel(e.getLevel())
			     //temp.setState(e.getState());
			     //todo
				list.add(temp);	
			}
			p.setNums(result.getTotal());
		}else{
			p.setNums(0);
		}
		p.setInfo(companyFacade.countCompanyEmployeeDonate(employee));
		return webUtil.successRes(p);
	}
	/**
	 * 获取验证企业员工
	 * @return
	 */
	@RequestMapping("/coredata/verifyEmploye")
	@ResponseBody
	public Map<String,Object> verifyEmploye(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer pageIndex,
			@RequestParam(value = "pageNum", required = false,defaultValue="10") Integer pageNum){
		
		Integer userId = UserUtil.getUserId(request, response);
		ApiEmployee employee = new ApiEmployee();
		employee.setCompany_userId(userId);
		employee.setState(Employe.status_audit);
		ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(employee, pageIndex, pageNum);
		Page  p = new Page();
		List<Employe> list = new ArrayList<Employe>();
		p.setPage(pageIndex);
		p.setPageNum(pageNum);
		p.setData(list);
		Employe temp = null;
		if(result!=null){
			for(ApiEmployee e:result.getResultData()){
			     temp = new Employe();
			     temp.seteId(e.getId());
			     temp.setbId(employee.getCompany_userId());
			     temp.setName(e.getNickName());
			     temp.setRealName(e.getRealName());
				list.add(temp);	
			}
			p.setNums(result.getTotal());
		}else{
			p.setNums(0);
		}
		
		return webUtil.successRes(p);
	}
	/**
	 * 验证企业员工
	 * @return
	 */
	@RequestMapping("/coredata/doVerify")
	@ResponseBody
	public Map<String,Object> doVerify(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "eId") Integer eId,
			@RequestParam(value = "ok", required = false, defaultValue = "0") Integer ok){
		Integer userId = UserUtil.getUserId(request, response);
		if(eId==null||eId==0){
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"缺少参数", null);
		}
		ApiEmployee employee = companyFacade.queryEmployeeById(eId);
		if(employee==null){
			return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,"数据错误", null);
		}
		if(employee.getState()!=Employe.status_audit||!employee.getCompany_userId().equals(userId)){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"非法操作", null);
		}
		
		employee = new ApiEmployee();
		employee.setId(eId);
		if(ok==1){
			employee.setState(Employe.status_success);
		}else{
			employee.setState(Employe.status_back);
		}
		ApiResult r = companyFacade.ExamineEmployee(employee);
		if(r.getCode()==1){
			//todo 发消息善员工审核是否通过
			return webUtil.successRes(null);
		}else{
			return webUtil.failedRes(webUtil.ERROR_CODE_UPDATEFAILED,"验证失败", null);
		}
	}
	
	/**
	 * 个人完善资料接口
	 * @return
	 */
	@RequestMapping("/coredata/personPerfet")
	@ResponseBody
	public Map<String,Object> personPerfet(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute Employe employe){
		Integer userId = UserUtil.getUserId(request, response);
		ApiEmployee apiEmployee = new ApiEmployee();
		apiEmployee.setUserId(userId);
		ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(apiEmployee, 1,1);
		if(result.getTotal()>0){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"已存在用户信息，无法添加", null);
		}
		if(employe.getHeadImg()!=0){
			//更新用户头像
			ApiFrontUser frontUser = new ApiFrontUser();
			frontUser.setId(userId);
			frontUser.setCoverImageId(employe.getHeadImg());
			ApiResult  r = userFacade.resetFrontUser(frontUser);
			if(r.getCode()==1){
				ApiBFile file = userFacade.queryBFileById(employe.getHeadImg());
				if(file!=null){
					SSOUtil.writeUserHead(response,file.getUrl());
				}
			}
		}
		if(employe.getCompanyId()!=0){
			//todo 加缓存性能优化
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(employe.getCompanyId());
			 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
			 if(company!=null){
				 employe.setCompanyName(company.getName());
			 }
			
	    }else if(!StringUtils.isBlank(employe.getCompanyName())){
	    	//查找对应公司
			ApiCompany company = new ApiCompany();
			company.setName(employe.getCompanyName());
			ApiPage<ApiCompany> r2 = companyFacade.queryCompanyList(company, 1, 1);
			if(r2.getTotal()>0){
				company = r2.getResultData().get(0);
				employe.setCompanyId(company.getUserId());
			}
	    }
		apiEmployee.setAddress(employe.getDetailAddress());
		apiEmployee.setProvince(employe.getLocation());
		apiEmployee.setCompany_userId(employe.getCompanyId());
		apiEmployee.setCompanyName(employe.getCompanyName());
		apiEmployee.setPosition(employe.getPostion());
		apiEmployee.setState(Employe.status_audit);
		//todo加缓存性能优化
		ApiFrontUser frontuser =  userFacade.queryById(userId);
		if(frontuser!=null){
			apiEmployee.setNickName(frontuser.getUserName());
			apiEmployee.setRealName(frontuser.getRealName());
		}
		 ApiResult er = companyFacade.saveEmployee(apiEmployee);
		 if(er.getCode()==1){
			 return webUtil.successRes(null); 
		 }else{
			 return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED,"完善公司信息失败", null);
		 }
	}
	
	/**
	 * 修改个人完善资料接口
	 * @return
	 */
	@RequestMapping("/coredata/upadatePPerfet")
	@ResponseBody
	public Map<String,Object> upadatePPerfet(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute Employe employe){
		Integer userId = UserUtil.getUserId(request, response);
		ApiEmployee apiEmployee = new ApiEmployee();
		apiEmployee.setUserId(userId);
		apiEmployee.setId(employe.geteId());
		ApiEmployee oldEmployee = companyFacade.queryEmployeeById(employe.geteId());
		if(oldEmployee==null){
			return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL,"没有对应数据", null);
		}
		
		if(employe.getHeadImg()!=0){
			//更新用户头像
			ApiFrontUser frontUser = new ApiFrontUser();
			frontUser.setId(userId);
			frontUser.setCoverImageId(employe.getHeadImg());
			ApiResult r = userFacade.resetFrontUser(frontUser);
			if(r.getCode()==1){
				ApiBFile file = userFacade.queryBFileById(employe.getHeadImg());
				if(file!=null){
					SSOUtil.writeUserHead(response,file.getUrl());
				}
				ApiCompany apiCompany = new ApiCompany();
				apiCompany.setUserId(userId);
				apiCompany = companyFacade.queryCompanyByParam(apiCompany);
				if(apiCompany != null){
					apiCompany.setCoverImageId(employe.getHeadImg());
					companyFacade.updateCompany(apiCompany);
				}
			}
		}
		if(employe.getCompanyId()!=0&&!oldEmployee.getCompany_userId().equals(employe.getCompanyId())){
			//todo 加缓存性能优化
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(employe.getCompanyId());
			 ApiCompany  company = companyFacade.queryCompanyByParam(apiCompany);
			 if(company!=null){
				 employe.setCompanyName(company.getName());
			 }
			
	    }else if(!StringUtils.isBlank(employe.getCompanyName())){
	    	//查找对应公司
			ApiCompany company = new ApiCompany();
			company.setName(employe.getCompanyName());
			ApiPage<ApiCompany> r2 = companyFacade.queryCompanyList(company, 1, 1);
			if(r2.getTotal()>0){
				company = r2.getResultData().get(0);
				employe.setCompanyId(company.getUserId());
			}
	    }else{
	    	employe.setCompanyId(0);
	    	employe.setCompanyName("");
	    }
		apiEmployee.setAddress(employe.getDetailAddress());
		apiEmployee.setProvince(employe.getLocation());
		apiEmployee.setCompany_userId(employe.getCompanyId());
		apiEmployee.setCompanyName(employe.getCompanyName());
		apiEmployee.setPosition(employe.getPostion());
		apiEmployee.setState(Employe.status_audit);
		 ApiResult er = companyFacade.updateEmployee(apiEmployee);
		 if(er.getCode()==1){
			 return webUtil.successRes(null); 
		 }else{
			 return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED,"完善公司信息失败", null);
		 }
	}
	

	/**
	 * 修改个人完善信息界面
	 * @return
	 */
	@RequestMapping("/core/pInfoUpdate")
	public ModelAndView pInfoUpdate(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("ucenter/pInfoUpdate");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = userFacade.queryById(userId);
		ApiEmployee apiEmployee = new ApiEmployee();
		apiEmployee.setUserId(userId);
		ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(apiEmployee, 1,1);
		if(result.getTotal()>0){
			apiEmployee = result.getResultData().get(0);
			if(!StringUtils.isBlank(apiEmployee.getProvince())){
				String[] temp = StringUtils.split(apiEmployee.getProvince(), "#");
				if (temp.length != 3) {
					apiEmployee.setProvince(null);
				} else {
					apiEmployee.setProvince(temp[1]);
				}
			}
		}
		// 判断是否要显示我要求助的项目
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		//显示善管家选项
		if (user.getLoveState() == 201 || user.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		ApiFrontUser user1 = userFacade.queryPersonCenter(user);
		user1.setCoverImageId(user.getCoverImageId());
		user1.setCoverImageUrl(user.getCoverImageUrl());
		view.addObject("user", user1);
		view.addObject("employee", apiEmployee);
		if(apiEmployee.getCompany_userId()!=0){
			ApiCompany c = new ApiCompany();
			c.setUserId(apiEmployee.getCompany_userId());
			c = companyFacade.queryCompanyByParam(c);
			view.addObject("myc", c);
		}
		
		ApiFrontUser temp = new ApiFrontUser();
		temp.setRealState(203);
		temp.setUserType("enterpriseUsers");
		temp.setOrderBy("totalAmount");
		temp.setOrderDirection("desc");
		temp.setKey(203+"enterpriseUsers"+"totalAmount"+"desc");
		temp.setCache(true);
		temp.setValidTime(5*60l);
		view.addObject("companys",userFacade.queryCompanyUserList(temp, 1, 20).getResultData());
		return view;
	}
	/**
	 * 个人完善信息界面
	 * @return
	 */
	@RequestMapping("/core/pInfoSuccess")
	public ModelAndView pInfoSuccess(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("ucenter/pInfoSuccess");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = userFacade.queryById(userId);
		ApiEmployee apiEmployee = new ApiEmployee();
		apiEmployee.setUserId(userId);
		ApiPage<ApiEmployee> result = companyFacade.queryEmployeeList(apiEmployee, 1,1);
		if(result.getTotal()>0){
			apiEmployee = result.getResultData().get(0);
			if(!StringUtils.isBlank(apiEmployee.getProvince())){
				String[] temp = StringUtils.split(apiEmployee.getProvince(), "#");
				if (temp.length != 3) {
					apiEmployee.setProvince(null);
				} else {
					apiEmployee.setProvince(temp[0]);
				}
			}
			ApiFrontUser cUser = new ApiFrontUser();
			cUser.setUserName(apiEmployee.getCompanyName());
			cUser = userFacade.queryUserByParam(cUser);
			if(StringUtils.isBlank(apiEmployee.getCompanyName())){
				view.addObject("cState",-1);
			}else{
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
		}
		// 判断是否要显示我要求助的项目
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		//显示善管家选项
		if (user.getLoveState() == 201 || user.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		ApiFrontUser user1 = userFacade.queryPersonCenter(user);
		user1.setCoverImageId(user.getCoverImageId());
		user1.setCoverImageUrl(user.getCoverImageUrl());
		 view.addObject("user", user1);
		view.addObject("employee", apiEmployee);
		return view;
	}
	
	/**
	 * 验证助善口令
	 * @return
	 */
	@RequestMapping("verifyHelpCode")
	@ResponseBody
	public Map<String,Object> verifyHelpCode(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "code") String code){
		String projectIdStr = CookieManager.retrieve(CookieManager.SPREAD_PROJECTID, request, true);
		if(StringUtils.isBlank(code)||StringUtils.isBlank(projectIdStr)){
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"助善口令无效", null);
		}
		Integer projectId = new Integer(projectIdStr);
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setProjectId(projectId);
		apiCompany.setGoodPassWord(code);
		ApiResult r = companyFacade.checkCompanyGoodPassWord(apiCompany);
		if(r.getCode()==1){
			return webUtil.successRes(null);
		}else if(r.getCode()==90011){
			return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,"口令不存在", null);
		}else if(r.getCode()==1){
			return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,"助善项目和口令不一致", null);
		}else {
			return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG,"助善口令无效", null);
		}
	}
	
	/*
	 * 企业的参与项目
	 */
	@RequestMapping("/coredata/donationlist")
	@ResponseBody
	public Map<String, Object> donationlist(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {

		// 1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid == null) {
			return webUtil.resMsg(-1, "0001", "未登入", null);
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

		// 分页计算
		Page p = new Page();
		p.setPageNum(pageNum);
		p.setPage(page);
		List<Donation> list = new ArrayList<Donation>();
		p.setData(list);

		ApiDonateRecord r = new ApiDonateRecord();
		if(type ==0 || type ==1 || type ==2){
			r.setQueryStartDate(bdate);
			r.setQueryEndDate(edate);
		}
		r.setUserId(new Integer(uid));
		r.setState(302);
//		r.setDonatType("enterpriseUsers");
		ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryByCondition(
				r, p.getPage(), p.getPageNum());
		Donation d = null;
		if (donats != null) {
			for (ApiDonateRecord donat : donats.getResultData()) {
				d = new Donation();
				d.setdMoney(donat.getDonatAmount());
				d.setCryMoney(donat.getCryMoney());
				d.setDonatAmountpt(donat.getDonatAmountpt());
				d.setdTime(donat.getDonatTime());
				// d.setdType(0);
				d.setId(donat.getId());
				d.setPid(donat.getProjectId());
				d.setdStatus(donat.getState());
				d.setTitle(donat.getProjectTitle());
				d.setStatus(donat.getPstate());
				d.setField(donat.getField());
				d.setProcess(StringUtil.doublePercentage(donat.getDonatAmountpt(),donat.getCryMoney(),0));
				ApiProjectFeedback apiProjectFeedback = new ApiProjectFeedback();
				apiProjectFeedback.setProjectId(donat.getProjectId());
				d.setFeedBackAmount(projectFacade.countProjectFeedbackByParam(apiProjectFeedback));
				list.add(d);
			}
			p.setNums(donats.getTotal());
		} else {
			p.setNums(0);
		}
		p.setInfo(donateRecordFacade.countQueryByCondition(r));//捐款总金额统计
		if (p.getTotal() == 0) {
			return webUtil.resMsg(2, "0002", "企业捐款列表为空", p);
		} else {
			return webUtil.resMsg(1, "0000", "成功", p);
		}
	}
	
	/*
	 * 企业捐款明细的页面
	 */
	@RequestMapping(value = "core/viewdonationlist")
	public ModelAndView enterpriseCenter(HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/egiftList");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "助善明细");
		return view;
	}
	
	/*
	 * 企业助善的发起页面
	 */
	@RequestMapping(value = "core/entcreatehelpgood")
	public ModelAndView enterpriseCreateHelpGood(
			@RequestParam(value = "projectId", required = true) Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/eAlipay");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		//企业是否认证，后期去掉
		ApiCompany apiCompany = new ApiCompany();
		apiCompany.setUserId(userId);
		apiCompany.setState(203);
		apiCompany = companyFacade.queryCompanyByParam(apiCompany);
		if(apiCompany==null){
			view = new ModelAndView("redirect:/project/view.do?projectId="+projectId);
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		//企业余额(账户余额)
		 if(user.getBalance() == null){
			 view.addObject("balance", "0.0");//企业的余额
		 }else{
			 view.addObject("balance", user.getBalance());//企业的余额
		 }
		 ApiProject project = projectFacade.queryProjectDetail(projectId);
		view.addObject("title", project.getTitle());
		view.addObject("project", project);
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		return view;
	}
	
	/*
	 * 企业充值明细的页面
	 */
	@RequestMapping(value = "core/entDetailsCharge")
	public ModelAndView enterpriseDetailsCharge(PUser user,HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/details_charge");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		view.addObject("guideName", "资金明细");
		return view;
	}
	
	/*
	 * 企业资金明细的页面
	 */
	@RequestMapping(value = "core/entDetailsFunds")
	public ModelAndView enterpriseDetailsFunds(PUser user,HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/details_funds");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		view.addObject("guideName", "资金明细");
		return view;
	}
	
	/*
	 * 企业资金明细的数据接口
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/entDetailsFundsData")
	public Map<String, Object> DetailsFundsData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
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
		if(type ==0 || type ==1 || type ==2){
			apiCapitalinout.setQueryStartDate(bdate);
			apiCapitalinout.setQueryEndDate(edate);
		}
		apiCapitalinout.setUserId(userId);
		ApiPage<ApiCapitalinout>  ap = companyFacade.queryCapitalinoutList(apiCapitalinout, page, pageNum);
		List<ApiCapitalinoutDto> adtList = new ArrayList<ApiCapitalinoutDto>(ap.getResultData().size());
		for (int i = 0; i < ap.getResultData().size(); i++) {
			apiCapitalinout=ap.getResultData().get(i);
			ApiCapitalinoutDto aDto = new ApiCapitalinoutDto();
			aDto = companyFacade.queryCapitalinoutDetail(apiCapitalinout);
			adtList.add(aDto);
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("baseLsit", ap);
		result.put("adtList", adtList);
		return webUtil.successRes(result);
	}
	
	/*
	 * 企业资金明细详情的数据接口
	 */
	@ResponseBody
	@RequestMapping(value = "coredata/entDetailsFundsDetail")
	public Map<String, Object> entDetailsFundsDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "capitalinoutId") Integer capitalinoutId,
			@RequestParam(value = "inType") Integer inType,
			@RequestParam(value = "type") Integer type) {
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			return webUtil.loginFailedRes(null);
		}
		ApiCapitalinout at = new ApiCapitalinout();
		at.setId(capitalinoutId);
		at.setInType(inType);
		at.setType(type);
		ApiCapitalinoutDto apd = companyFacade.queryCapitalinoutDetail(at);
		return webUtil.successRes(apd);
	}
	/*
	 * 企业提款明细的页面
	 */
	@RequestMapping(value = "core/entDetailsWithdrawal")
	public ModelAndView enterpriseDetailsWithdrawal(PUser user,HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/details_withdrawal");
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user1 = userFacade.queryById(userId);
		view.addObject("user", user1);
		return view;
	}
	
	/**
	 * 企业助善明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="core/enterpriseCharityDetail")
	public ModelAndView enterpriseCharityDetail(HttpServletRequest request,HttpServletResponse response){
		Integer userId = UserUtil.getUserId(request, response);
		ModelAndView view = new ModelAndView("enterprise/eCharity_detail");
		if(userId == null){
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		view.addObject("guideName", "助善明细");
		return view;
	}
}
