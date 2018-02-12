package com.guangde.home.controller.user;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.huzhu.IMaUserFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.api.user.IUserRelationInfoFacade;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiFrontUser_address;
import com.guangde.entry.ApiIptable;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;

@Controller
@RequestMapping("user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String HUZHU_USER_ADD="http://hz.17xs.org/huzhu/user/add";
	private static final String imgcodeprex = "imgcode_r_";
	private static final String phonecodeprex = "phonecode_r_";
	private static final String logincodeprex = "logincode_";

	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IMaUserFacade maUserFacade;
	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	
	/**
	 * 登入页面显示
	 * 
	 * @return
	 */
	@RequestMapping(value = "sso/login")
	public ModelAndView ssoLogin() {
		ModelAndView view = new ModelAndView("sso/login");
		return view;
	}

	/**
	 * 注册页面显示
	 * 
	 * @return
	 */
	@RequestMapping(value = "sso")
	public ModelAndView sso(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "projectId", required = false) Integer projectId,
			@RequestParam(value = "companyCode", required = false,defaultValue="") String companyCode) {
		if(projectId!=null){
			CookieManager.create(CookieManager.SPREAD_PROJECTID,projectId.toString(), CookieManager.EXPIRED_DEFAULT_MINUTE, response, true);
		}
		ModelAndView view = new ModelAndView("sso/register");
		view.addObject("companyCode", companyCode);
		return view;
	}
	/**
	 * h5注册页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="sso_h5")
	public ModelAndView sso_h5(@RequestParam(value="flag",required=false)String flag,
			HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("sso/register_h5");
		view.addObject("flag", flag);
		return view;
	}

	/**
	 * 用户注册
	 * @param hUser
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "register")
	@ResponseBody
	public Map<String, Object> register(@ModelAttribute PUser hUser,
			//@RequestParam(value="flag",required=false)String flag,
			@RequestParam(value="huzhu",required=false)String huzhu,
			HttpServletRequest request, HttpServletResponse response){
		// msg:0000成功，0001手机格式不对,0003密码格式错误，004无法识别用户身份
		// msg:0005验证码错误,0006手机验证码错误
		if (logger.isDebugEnabled()) {
			logger.debug("注册信息：" + hUser.toString());
		}
		 String adressIp = SSOUtil.getUserIP(request);
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		// 1.校验参数

		// 校验图片验证码
		/*if(flag != null && flag.equals("h5Register")){
			//TODO 手机注册没有图片校验
		}else {*/
			String codekey = getImgCode(request);
			
			//可以替换为其他的存储方式
			
			codeR = CodeUtil.VerifiCode(codekey,storeManage,hUser.getCode(),true);
			if (codeR == -1) {
				msg = "0005";
				return webUtil.resMsg(0, msg, "验证码过期", null);
			} else if (codeR == 0) {
				msg = "0005";
				return webUtil.resMsg(0, msg, "验证码错误", null);
			}
		/*}*/
		// 校验密码格式
		msg = UserUtil.verifyPassword(hUser.getPassWord());
		if (msg != null) {
			// 密码格式错误
			return webUtil.resMsg(0, "0003", msg, null);
		}
		
		ApiFrontUser user = new ApiFrontUser();
		user.setMobileState(201);
		if (isPhoneUser(hUser)) {
			// 手机注册用户
			if (StringUtil.isMobile(hUser.getPhone())) {
				// 校验手机验证码
				phonekey = phonecodeprex + hUser.getPhone();

				 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
				 hUser.getPhoneCode(), true);
				 if(codeR==-1){
				 return webUtil.resMsg(0, "0005","手机验证码过期", null);
				 }else if(codeR==0){
				 return webUtil.resMsg(0, "0005","手机验证码错误", null);
				 }
				user.setMobileNum(hUser.getPhone());
				user.setMobileState(203);
				hUser.setName(hUser.getPhone());
			} else {
				// 手机格式错误
				msg = "0001";
				return webUtil.resMsg(0, msg, "手机格式错误", null);
			}
		} else {
			// 账号注册用户
			msg = UserUtil.verifyUserName(hUser.getName());
			if (msg != null) {
				// 用户名格式错误
				msg = "0002";
				return webUtil.resMsg(0, msg, msg, null);
			}
		}
		 
		//2.调用注册服务

		user.setUserName(hUser.getName());
		user.setNickName(hUser.getNickName());
		user.setUserPass(hUser.getPassWord());
		if(hUser.getType()==1){
			user.setUserType("enterpriseUsers");
		}else{
			user.setUserType("individualUsers");
			//如果是个人，有注册口令，传注册口令
			String projectIdStr = CookieManager.retrieve(CookieManager.SPREAD_PROJECTID, request, true);
			logger.info("注册projeictId"+projectIdStr);
			if(!StringUtils.isBlank(hUser.getSpreadCode())&&!StringUtils.isBlank(projectIdStr)&&hUser.getSpreadCode().length()==10){
				if(StringUtils.isNumeric(projectIdStr)){
					user.setGoodPassWord(hUser.getSpreadCode());
					user.setProjectId(new Integer(projectIdStr));
				}
			}
		}
		//获取cookie中的推广人id
		if(CookieManager.retrieve("ePeopleId", request, false)!=null){
			user.setExtensionPeople(Integer.parseInt(CookieManager.retrieve("ePeopleId", request, false)));
		}
		try {
			user.setRegisterIP(adressIp);
        	long ip = IPAddressUtil.ipToLong(adressIp);
        	ApiIptable iptable = new ApiIptable();
        	iptable.setEndIPNum(String.valueOf(ip));
        	iptable.setStartIPNum(String.valueOf(ip));
        	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
        	if(ipList!=null && ipList.size()>0){
        		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
        		if(contents.length==1){
        			user.setProvince(contents[0]);
        		}
        		else if(contents.length==2){
        			user.setProvince(contents[0]);
        			user.setCity(contents[1]);
        		}
        		else if(contents.length==3){
        			user.setProvince(contents[0]);
        			user.setCity(contents[1]);
        			user.setArea(contents[2]);
        		}
        	}
		
	} catch (Exception e) {
	}
		ApiResult result = userFacade.registered(user);
		Object donate = null;
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "注册失败", null);
		}else{
			if(result.getObject()!=null){
				donate = result.getObject();
			}
		}
		/*//----huzhu_statrt---------
		//验证是否是互助系统的用户,不是就添加
		if("huzhu".equals(huzhu)){
			ApiFrontUser apiFrontUser = new ApiFrontUser();
			apiFrontUser=userFacade.queryById(user.getId());
			ApiMaUser apiMaUser = new ApiMaUser();
			apiMaUser.setUser_id(user.getId());
			apiMaUser = maUserFacade.selectByParam(apiMaUser);
			if(apiMaUser==null){
				HttpConnect httpConnect = new HttpConnect();
				URL url2 = new URL(HUZHU_USER_ADD);
				if(apiFrontUser.getRealName()!=null && apiFrontUser.getIdCard()!=null){
					httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&realName="+apiFrontUser.getRealName()+"&idNumber="+apiFrontUser.getIdCard()+"&status=0");
				}
				else{
					httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&status=0");
				}
				apiMaUser = new ApiMaUser();
				apiMaUser.setUser_id(user.getId());
				apiMaUser.setReal_name(apiFrontUser.getRealName());
				apiMaUser.setId_number(apiFrontUser.getIdCard());
				apiMaUser.setStatus(0);
				apiMaUser.setCreate_time(new Date());
				apiMaUser.setUpdate_time(new Date());
				maUserFacade.save(apiMaUser);
			}
			//主站添加互助用户标志
			if(apiFrontUser.getHuzhu_state()==0){
				apiFrontUser.setHuzhu_state(1);
				userFacade.updateUser(apiFrontUser);
			}
			//加入注册的用户存入redis
			//redisService.saveObjectData(PengPengConstants.LOGIN_USERID, user.getId(),DateUtil.DURATION_TEN_S);
		}
		//-----hzuhu_end-----------*/
		// 调用一下登入接口
		Map<String, Object> temp = login(user, request, response, userFacade);
		Map<String,Object> r = webUtil.successRes(temp.get("user"));
		r.put("donate", donate);
		return r;
	}

	/**
	 * 验证用户名格式和用户名是否已经存在
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "existuser")
	@ResponseBody
	public Map<String, Object> existuser(
			@RequestParam(value = "name", required = false) String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("name: " + name);
		}
		if (UserUtil.verifyUserName(name) != null) {
			return webUtil.resMsg(0, "0001", "用户名格式错误", null);
		}
		// 2.调用服务
		ApiResult result = userFacade.userExistence(name);
		if (result.getCode() == 1) {
			return webUtil.resMsg(1, "0000", "成功", null);
		} else {
			return webUtil
					.resMsg(0,
							"0001",
							"已存在，<a href='http://www.17xs.org/user/sso/login.do'>立即登入</a>",
							null);
		}
	}

	/**
	 * 获取页面图片验证码
	 * 
	 * @param type
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "code")
	public void code(
			@RequestParam(value = "type", required = false, defaultValue = "register") String type,
			HttpServletRequest request, HttpServletResponse response) {
		String codekey = null;
		if (CodeUtil.icode_type_r.equals(type)) {
			codekey = getImgCode(request);
		} else if (CodeUtil.login.equals(type)) {
			codekey = getLoginCode(request, response);
		}

		try {
			BufferedImage image = CodeUtil.imgCode(
					codekey,
					StoreManage.create(StoreManage.STROE_TYPE_SESSION,
							request.getSession()));
			// 禁止图像缓存。
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			// 将图像输出到输出流中。
			ServletOutputStream sos = response.getOutputStream();
			ImageIO.write(image, "jpeg", sos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 输出验证码错误的图片
	}

	/**
	 * 获取手机验证码
	 * 
	 * @param type
	 * @param phone
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "phoneCode", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> phoneCode(
			@RequestParam(value = "type", required = false, defaultValue = "register") String type,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "flag",required = false ) String flag,
			HttpServletRequest request) {
		String tempType = null;
		// 验证业务参数
		if (CodeUtil.icode_type_r.equals(type)) {
			//h5手机注册没有图片验证码
			/*if(flag != null && flag.equals("h5Register")){ 
				tempType = phonecodeprex;
			}else {*/
				// 校验图片验证码
				String codekey = getImgCode(request);
				// 可以替换为其他的存储方式
				StoreManage storeManage = StoreManage.create(
						StoreManage.STROE_TYPE_SESSION, request.getSession());
				int codeR = CodeUtil.VerifiCode(codekey, storeManage, code, false);
				if (codeR == -1) {
					return webUtil.resMsg(0, "0005", "验证码过期", null);
				} else if (codeR == 0) {
					return webUtil.resMsg(0, "0005", "验证码错误", null);
				}
				tempType = phonecodeprex;
			/*}*/
		} else if (CodeUtil.certificationprex.equals(type)) {
			//用户实名验证业务
			// 校验图片验证码
			String codekey = getImgCode(request);
			// 可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(codekey, storeManage, code, false);
			if (codeR == -1) {
				return webUtil.resMsg(0, "0005", "验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "0005", "验证码错误", null);
			}
			tempType = CodeUtil.certificationprex;
		}else if(CodeUtil.enterprise_validate.equals(type)){
			//用户实名验证业务
			// 校验图片验证码
			String codekey = getImgCode(request);
			// 可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(codekey, storeManage, code, false);
			if (codeR == -1) {
				return webUtil.resMsg(0, "0005", "验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "0005", "验证码错误", null);
			}
			tempType = CodeUtil.enterprise_validate;
		}else{
			return  webUtil.resMsg(0,"0001","未知业务类型", null);
		}

		// 发送验证码
		if (StringUtil.isMobile(phone)) {
			String phonekey = tempType + phone;
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			String result = CodeUtil.phoneCode(phonekey, phone, storeManage);
			if (result == null) {
				return webUtil.resMsg(0, "0003", "发送失败", null);
			}
		} else {
			return webUtil.resMsg(0, "0002", "手机格式错误", null);
		}

		return webUtil.resMsg(1, "0000", "成功", null);
	}

	/**
	 * 用户登入
	 * 
	 * @param type
	 * @param phone
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login")
	@ResponseBody
	public Map<String, Object> login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "passWord", required = false) String passWord,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "entrance", required = false) String entrance,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type,
			@RequestParam(value = "flag", required = false) String flag) {

		StoreManage storeManage = StoreManage.create(
				StoreManage.STROE_TYPE_SESSION, request.getSession());
		// int nums = webUtil.visitNums("visit_nums_"+getUuid(request),
		// storeManage, session);
//		int nums = 1;  
//		if(nums>2||code!=null){
		if(type==2){
			int codeR = CodeUtil.VerifiCode(getLoginCode(request,response),storeManage,code,false);
			if(codeR==-1){
				return webUtil.resMsg(0, "0003","验证码过期", null); 
			}else if(codeR==0){
				return webUtil.resMsg(0, "0003","验证码错误", null); 
			}
		}
//		}
		if (UserUtil.verifyUserName(name) != null) {
			return webUtil.resMsg(0, "0001", "登入名错误", null);
		} else {
			if (StringUtil.isMobile(name)) {
				// 手机登入
				// todo
			}
			// 账号登入
			// todo
			ApiFrontUser user = new ApiFrontUser();
			user.setUserName(name);
			user.setUserPass(passWord);
			Map<String, Object> temp = login(user, request, response,
					userFacade);
			String msg = (String) temp.get("msg");
			if (msg != null) {
				return webUtil.resMsg(0, "0002", msg, null);
			} else {
				return webUtil.resMsg(1, "0000", "成功", temp.get("user"));
			}
		}
	}

	/**
	 * 用户退出
	 * 
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loginout")
	@ResponseBody
	public Map<String, Object> loginOut(HttpServletRequest request,
			HttpServletResponse response) {
		// 1.调用退出服务
		SSOUtil.loginOut(request, response);
		return webUtil.resMsg(1, "0000", "退出成功", null);
	}

	/**
	 * 检查是否登入
	 * 
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Map<String, Object> checkLogin(HttpServletRequest request,
			HttpServletResponse response) {
		// 1.调用退出服务
		String uid = SSOUtil.verifyAuth(request, response);
		if (uid != null) {
			return webUtil.resMsg(1, "0000", "已登入", null);
		} else {
			return webUtil.resMsg(0, "0001", "未登入", null);
		}
	}

	@RequestMapping(value = "core/test")
	@ResponseBody
	public Map<String, Object> test(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "passWord", required = false) String passWord,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	private boolean isPhoneUser(PUser hUser) {
		return StringUtils.isBlank(hUser.getName());
	}

	private String getImgCode(HttpServletRequest request) {
		return imgcodeprex + getUuid(request);
	}

	private String getLoginCode(HttpServletRequest request,
			HttpServletResponse response) {
		return logincodeprex + getUuid(request);
	}

	private String getUuid(HttpServletRequest request) {
		String uuid = CookieManager.retrieve(CookieManager.COOKIE_UUID_NAME,
				request, true);
		if (uuid == null) {
			return request.getSession().getId();
		} else {
			return uuid;
		}
	}

	/*
	 * 跳转到密码的重置页面
	 */
	@RequestMapping("changepassword")
	public ModelAndView changePassword(PUser user, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("sso/backPw-Account");
		String status = user.getStatus();
		view.addObject("step", 1);
		if ("2".equals(status)) {
			view = new ModelAndView("sso/backPw-list");
			view.addObject("step", 2);
		} 
		if(user.getType() == 1){//手机
			view = new ModelAndView("sso/backPw-phone");
		}else if (user.getType() == 2) {//身份证
			view = new ModelAndView("sso/backPw-id");
		}
		view.addObject("name", user.getName());
		ApiFrontUser apiFrontUser = new ApiFrontUser();
		apiFrontUser.setUserName(user.getName());
		apiFrontUser = userFacade.queryUserByParam(apiFrontUser);
		if(apiFrontUser.getMobileNum()==null){
			view.addObject("phone", null);
		}else{
			view.addObject("phone", apiFrontUser.getMobileNum());
		}
		return view;
	}

	/*
	 * 对于密码进行修改的
	 */
	@ResponseBody
	@RequestMapping("cpassword")
	public Map<String, Object> cPassword(PUser user, HttpServletRequest request,
			HttpServletResponse response) {
		String status = user.getStatus();
		if ("3".equals(status)) {// 取到身份证信息，验证成功跳到改密码界面
			// 校验密码格式
			String msg = UserUtil.verifyPassword(user.getPassWord());
			if (msg != null) {
				// 密码格式错误
				return webUtil.resMsg(0, "2003", "密码有误", null);
			}
			//通过验证并且修改密码
			if (Reset(1, user, true)) {
				return webUtil.successRes(3);
			} 
		} else if ("4".equals(status)) {// 取到手机号码正确，跳到改密码界面
			// 手机验证
			String phonekey = CodeUtil.certificationprex + user.getPhone();
			// 可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					user.getPhoneCode(), false);
			if (codeR == -1) {
				return webUtil.resMsg(0, "2004", "手机验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "2005", "手机验证码错误", null);
			}
			//通过验证并且修改密码
			if (Reset(2, user, true)) {
				return webUtil.successRes(4);
			} 
		} else if ("5".equals(status)) {// 取到原密码，进行修改
			ApiFrontUser aPuser = new ApiFrontUser();
			String name = SSOUtil.getCurrentUserName(request, response);
			aPuser.setUserName(name);
			aPuser.setUserPass(user.getPassWord());
			aPuser.setDescription("5");
			Map<String, Object> temp = login(aPuser, request, response,
					userFacade);
			String msg = (String) temp.get("msg");
			if (msg != null) {
				return webUtil.resMsg(0, "0002", msg, null);
			} else {
				//通过验证并且修改密码
				user.setName(name);
				if (Reset(3, user, true)) {
					return webUtil.successRes(5);
				} 
			}
		}
		return webUtil.resMsg(0, "2006", "参数错误", null);
	}
	/*
	 * 对于是否存在进行判断，及用户的密码修改
	 * 
	 * @param type 通过验证的手段
	 * 
	 * @param reset 通过验证是否进行密码修改
	 */
	private boolean Reset(int type, PUser user, boolean reset) {
		ApiFrontUser apiFrontUser = new ApiFrontUser();
		// 通过身份证修改
		if (type == 1) {
			apiFrontUser.setUserName(user.getName());
			apiFrontUser.setRealName(user.getRealname());
			apiFrontUser.setIdCard(user.getIdCard());
			apiFrontUser.setRealState(203);
			apiFrontUser = userFacade.queryUserByParam(apiFrontUser);
			if (apiFrontUser != null ) {
				if(reset){
					apiFrontUser.setUserPass(user.getPassWord());
					userFacade.resetFrontUser(apiFrontUser);
				}
				
			}else {
				//不存在用户
				return false;
			}
			return true;
		} else if (type == 2) {// 通过手机修改
			apiFrontUser.setUserName(user.getName());
			apiFrontUser.setMobileNum(user.getPhone());
			apiFrontUser.setMobileState(203);
			apiFrontUser = userFacade.queryUserByParam(apiFrontUser);
			if (apiFrontUser != null ) {
				if(reset){
					apiFrontUser.setUserPass(user.getPassWord());
					userFacade.resetFrontUser(apiFrontUser);
				}
				
			}else {
				//不存在用户
				return false;
			}
			return true;
		}else if (type == 3) {
			apiFrontUser.setUserName(user.getName());
			apiFrontUser = userFacade.queryUserByParam(apiFrontUser);
			if(reset){
				apiFrontUser.setUserPass(user.getNewpassWord());
				userFacade.resetFrontUser(apiFrontUser);
			}
			return true;
		}
		return false;
	}

	@ResponseBody
	@RequestMapping("psdCheck")
	public Map<String, Object> psdCheck(PUser user, HttpServletRequest request,
			HttpServletResponse response) {
		String status = user.getStatus();
		Map<String, Object> psdMap = new HashMap<String, Object>();
		if ("2".equals(status)) {
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(getImgCode(request), storeManage,
					user.getCode(), true);
			if (codeR == -1) {
				return webUtil.resMsg(0, "0003", "验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "0003", "验证码错误", null);
			}
			if (UserUtil.verifyUserName(user.getName()) != null) {
				return webUtil.resMsg(0, "0001", "登入名错误", null);
			}
			psdMap.put("status", 2);
			psdMap.put("name", user.getName());
			return webUtil.successRes(psdMap);
		} else if ("3".equals(status)) {
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(getImgCode(request), storeManage,
					user.getCode(), true);
			if (codeR == -1) {
				return webUtil.resMsg(0, "0003", "验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "0003", "验证码错误", null);
			}
			if (UserUtil.verifyUserName(user.getName()) != null) {
				return webUtil.resMsg(0, "0001", "登入名错误", null);
			}
			String realname = user.getRealname();
			String idCard = user.getIdCard();
			if (StringUtils.isNotEmpty(realname)
					&& StringUtils.isNotEmpty(idCard)) {
				if (Reset(1, user, false)) {
					return webUtil.successRes(1);
				} else {
					return webUtil.failedRes("0004", "不是该用户绑定的身份证号", null);
				}
			} else {
				return webUtil.failedRes("0004", "不是该用户绑定的身份证号", null);
			}
		} else if ("4".equals(status)) {
			// 手机验证
			String phonekey = CodeUtil.certificationprex + user.getPhone();
			// 可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(phonekey, storeManage,
					user.getPhoneCode(), false);
			if (codeR == -1) {
				return webUtil.failedRes("0003", "手机验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.failedRes("0004", "手机验证码错误", null);
			}
			//判断是否通过验证
			if (Reset(2, user, false)) {
				return webUtil.successRes(2);
			} else {
				return webUtil.failedRes("0004", "不是该用户绑定的手机号", null);
			}
		}
		return webUtil.resMsg(0, "0001", "未登入", null);
	}
	//跳转到重置密码界面
	@RequestMapping("viewReset")
	public ModelAndView viewReset(PUser user, HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView view = new ModelAndView("sso/backPw-newpassword");
		if(user.getPhone() == null){
			user.setStatus("3");
		}else {
			user.setStatus("4");
		}
		view.addObject("user", user);
		return view;
	}
	
	//跳转到重置密码界面
	@RequestMapping("bankReset")
	public ModelAndView bankReset(){
		ModelAndView view = new ModelAndView("sso/backPw-ok");
		return view;
	}

	private Map<String, Object> login(ApiFrontUser user,
			HttpServletRequest request, HttpServletResponse response,
			IUserFacade userFacade) {
		// 1.调用服务端接口
		if (logger.isDebugEnabled()) {
			logger.debug("登入：user " + user);
		}
		ApiResult result = userFacade.localUserlogin(user);
		if (logger.isDebugEnabled()) {
			logger.debug("登入：result " + result);
		}
		Map<String, Object> temp = new HashMap<String, Object>();
		if (result.getCode() != 1) {
			if (result.getCode() == -1) {
				temp.put("msg", "参数错误");
			} else if (result.getCode() == -2) {
				temp.put("msg", "用户不存在");
			} else if (result.getCode() == -3) {
				if("5".equals(user.getDescription())){
					temp.put("msg", "旧密码错误");
				}else {
					temp.put("msg", "密码错误");
				}
			} else if (result.getCode() == -101) {
				temp.put("msg", "此用户已停用");
			} else if (result.getCode() == -102) {
				temp.put("msg", "此用户已暂停");
			} else if (result.getCode() == -103) {
				temp.put("msg", "此用户已锁定");
			} else {
				temp.put("msg", "登入失败");
			}
		} else {
			user = (ApiFrontUser)result.getObject();
			/*//----huzhu_statrt---------
			//验证是否是互助系统的用户,不是就添加
			ApiFrontUser apiFrontUser = new ApiFrontUser();
			apiFrontUser=userFacade.queryById(user.getId());
			ApiMaUser apiMaUser = new ApiMaUser();
			apiMaUser.setUser_id(user.getId());
			apiMaUser = maUserFacade.selectByParam(apiMaUser);
			if(apiMaUser==null){
				logger.info("login》》》》apiMaUser==null进入添加系统用户信息》》》》");
				/*HttpConnect httpConnect = new HttpConnect();
				URL url2 = null;
				try {
					url2 = new URL(HUZHU_USER_ADD);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				if(apiFrontUser.getRealName()!=null && apiFrontUser.getIdCard()!=null){
					try {
						httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&realName="+apiFrontUser.getRealName()+"&idNumber="+apiFrontUser.getIdCard()+"&status=0");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&status=0");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				apiMaUser = new ApiMaUser();
				apiMaUser.setUser_id(user.getId());
				apiMaUser.setReal_name(apiFrontUser.getRealName());
				apiMaUser.setId_number(apiFrontUser.getIdCard());
				apiMaUser.setStatus(0);
				apiMaUser.setCreate_time(new Date());
				apiMaUser.setUpdate_time(new Date());
				maUserFacade.save(apiMaUser);
			}
			//主站添加互助用户标志
			if(apiFrontUser!=null && apiFrontUser.getHuzhu_state()==0){
				logger.info("login》》》》进入更新主站用户的互助用户标志》》》》");
				apiFrontUser.setHuzhu_state(1);
				userFacade.updateUser(apiFrontUser);
			}
			//-----hzuhu_end-----------*/
			PUser huser = new PUser();
			huser.setId(Integer.parseInt(result.getData()));
			huser.setName(user.getUserName());

			// 2.保存cookie,session
			SSOUtil.login(user, request, response);
			temp.put("user", huser);
		}
		logger.info("login》》》返回值temp》》》"+temp);
		return temp;
	}
	//H5的登录页面
	@RequestMapping("login_h5")
	public ModelAndView login_h5() {
		ModelAndView view = new ModelAndView("h5/login");
		return view;
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("updateUserInfo")
	@ResponseBody
	public JSONObject updateUserInfo(ApiFrontUser user,HttpServletRequest request,HttpServletResponse response){
		JSONObject item = new JSONObject();
		Integer userId = UserUtil.getUserId(request, response);
		user.setId(userId);
		ApiFrontUser_address address = new ApiFrontUser_address();
		address.setUserId(userId);
		List<ApiFrontUser_address> list = userRelationInfoFacade.queryUserAddress(address, 1, 10);
		if(user.getProvince()!=null && !"".equals(user.getProvince())){
			address.setProvince(user.getProvince());
		}
		if(user.getCity()!=null && !"".equals(user.getCity())){
			address.setCity(user.getCity());
		}
		if(user.getArea()!=null && !"".equals(user.getArea())){
			address.setArea(user.getArea());
		}
		if(user.getFamilyAddress()!=null && !"".equals(user.getFamilyAddress())){
			address.setDetailAddress(user.getFamilyAddress());
		}
		if(list!=null && list.size()>0){
			logger.info(">>>>>>>>地址id"+list.get(0).getId());
			address.setId(list.get(0).getId());
			userRelationInfoFacade.updateUserAddress(address);
			logger.info(">>>>>>>>>>>>更新用户地址");
		}
		else{
			address.setName(user.getNickName());
			address.setMobile(user.getMobileNum());
			address.setIsSelected(1);
			address.setCreateTime(new Date());
			address.setLastUpdateTime(address.getCreateTime());
			userRelationInfoFacade.saveUserAddress(address);
			logger.info(">>>>>>>>>>>>添加用户地址");
		}
		
		user.setProvince(null);
		user.setCity(null);
		user.setArea(null);
		user.setFamilyAddress(null);
		ApiResult result = userFacade.updateUser(user);
		item.put("code", result.getCode());
		return item;
	}
	
}
