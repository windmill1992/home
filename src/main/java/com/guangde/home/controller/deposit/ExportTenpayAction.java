package com.guangde.home.controller.deposit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiPayConfig;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiThirdUser;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.MD5Utils;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.Demo;
import com.tenpay.demo.H5Demo;
import com.tenpay.demo.WxPayDto;
import com.tenpay.utils.RequestHandler;
import com.tenpay.utils.TenpayUtil;

/**
 * 对于游客匿名的捐赠 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("exportTenpay")
public class ExportTenpayAction extends DepositBaseAction {

	// 服务器异步通知页面路径
	public static final String notify_url = "http://www.17xs.org/exportTenpay/async.do";

	// 页面跳转同步通知页面路径
	public static final String return_url = "http://www.17xs.org/exportTenpay/return_url.do";

	Logger logger = LoggerFactory.getLogger(ExportTenpayAction.class);

	@Autowired
	private IDonateRecordFacade donateRecordFacade;

	@Autowired
	private IProjectFacade projectFacade;

	@Autowired
	private IUserFacade userFacade;

	@Autowired
	private ICompanyFacade companyFacade;
	
	@Autowired
	private RedisService redisService;
	
	/**
	 * 余额捐款
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @param md5Utils
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/availableMoneyDeposit")
	@ResponseBody
	public Map<String, Object> availableMoneyDeposit(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response, MD5Utils md5Utils) throws Exception {
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		ModelAndView view = new ModelAndView("deposit/alipay");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		if (from.getProjectId() == 0 || "".equals(from.getProjectId())) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "项目id为空", item);
		}
		// 判段这个IP是否已经注册过
		// 未注册的用户//当做一个用户的唯一字段
		ApiFrontUser fronUser1 = new ApiFrontUser();
		fronUser1.setRegUsersType(frontUser.getRegUsersType());
		fronUser1.setUserName(frontUser.getUserName());
		fronUser1.setUserPass(frontUser.getUserPass());
		ApiFrontUser af = userFacade.queryUserByParam(fronUser1);
		Integer userId = null;
		if (af == null) {
			// view.addObject("alipay", "没有这个项目");
			return webUtil.resMsg(0, "0000", "余额支付出现问题,请联系管理员", item);
		} else {
			if (af.getId() != null) {
				userId = af.getId();
				if(from.getAmount()>af.getAvailableBalance()){
					return webUtil.resMsg(0, "0001", "捐款金额大于余额", item);
				}
				else if(from.getAmount()<=0){
					return webUtil.resMsg(0, "0001", "捐款金额不能低于0", item);
				}
			} else {
				return webUtil.resMsg(0, "0001", "添加该用户信息出现异常，请联系管理员", item);
			}
		}

		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);

		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from
				.getProjectId());
		tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_USER);
		dRecord.setDonatType(af.getUserType());
		dRecord.setDonatAmount(Double.valueOf(total_fee));
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		if (request.getSession().getAttribute("extensionPeople") != null
				&& from.getExtensionPeople() == null) {
			dRecord.setExtensionPeople(Integer.valueOf(request.getSession()
					.getAttribute("extensionPeople").toString()));
			ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request
					.getSession().getAttribute("extensionPeople").toString()));
			if (auser != null && auser.getExtensionPeople() != null
					&& auser.getExtensionPeople() == 1) {
				dRecord.setExtensionEnabled(1);// 推广员的有效推广
			}
		} else {
			if (from.getExtensionPeople() != null) {
				dRecord.setExtensionPeople(from.getExtensionPeople());
				ApiFrontUser auser = userFacade.queryById(from
						.getExtensionPeople());
				if (auser != null && auser.getExtensionPeople() != null
						&& auser.getExtensionPeople() == 1) {
					dRecord.setExtensionEnabled(1);// 推广员的有效推广
				}
			}
		}
		if (from.getDonateWord() != null) {
			dRecord.setLeaveWord(from.getDonateName() + "|"
					+ from.getDonateWord());
		}

		// 6：个人用户
		tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
		ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "freezType",null,"");
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				Date date = new Date();
				item.put("pName", project.getTitle());
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());

				item.put("projectId", from.getProjectId());
				item.put("date", date.getTime());
				item.put("nickName", af.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);
			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
		}
	}

	/**
	 * 微信充值接口
	 * 
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "tenPayRecharge")
	@ResponseBody
	public Map<String, Object> tenPayRecharge(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response, MD5Utils md5Utils) throws Exception {
		ModelAndView view = new ModelAndView(
				"redirect:/exportTenpay/weixinChargePay.do");
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		// 判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId = null;
		ApiFrontUser user = new ApiFrontUser();
		user.setUserName(frontUser.getUserName());
		ApiFrontUser user11 = new ApiFrontUser();
		user11.setUserName(frontUser.getUserName());
		ApiFrontUser ar = userFacade.queryUserByParam(user11);
		boolean flag = false;
		if (ar == null) {
			try {
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
			user.setNickName(frontUser.getNickName());
			user.setUserPass(frontUser.getUserPass());
			user.setUserType(PengPengConstants.PERSON_USER);
			// 未注册的用户//当做一个用户的唯一字段
			user.setRegUsersType(frontUser.getRegUsersType());
			user.setRegisterIP(adressIp);
			logger.debug("募捐归类需要的参数：adressIp:" + adressIp);
			ApiResult apr = userFacade.registered(user);
			if (apr != null && apr.getCode() == 1) {
				ApiFrontUser user1 = new ApiFrontUser();
				user1 = userFacade.queryUserByParam(user);
				userId = user1.getId();
				flag = true;
			}
		} else {
			userId = ar.getId();
			flag = true;
		}
		if (!flag) {
			return webUtil.resMsg(0, "0000", "该用户无权支付", item);
			// view.addObject("error", "该用户无权支付");
		}
		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);
		tpWxPay1.setBody("施乐会用户" + frontUser.getNickName() + "充值");
		// 附加数据
		tpWxPay1.setAttach(userId + "," + total_fee);
		ApiCapitalinout capitalinout = new ApiCapitalinout();
		capitalinout.setTranNum(tpWxPay1.getOrderId());
		capitalinout.setSource("PC");
		capitalinout.setMoney(from.getAmount());

		capitalinout.setPayType("tenpay");

		capitalinout.setUserId(userId);
		capitalinout.setBankType(from.getBank());
		// 资金进
		ApiResult rt = companyFacade.companyReCharge(capitalinout);
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				// 生成二维码
				String codeurl = Demo.getCodeurl(tpWxPay1);
				Date date = new Date();
				item.put("pName", "善基金充值");
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());
				item.put("codeurl", codeurl);
				item.put("date", date.getTime());
				item.put("nickName", user.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);

			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
		}

	}

	/*
	 * 正常项目的支付页面
	 */
	@RequestMapping(value = "/weixinChargePay")
	public ModelAndView weixinChargePay(DepositForm from,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView view = new ModelAndView("deposit/exportWeixin");
		view.addObject("title", from.getpName());
		view.addObject("amount", from.getAmount());
		view.addObject("tradeNo", from.getTradeNo());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		view.addObject("date", sdf.format(from.getDate()));
		view.addObject("codeurl", from.getCodeurl());
		view.addObject("projectId", from.getProjectId());
		view.addObject("payType", from.getPayType());
		view.addObject("nickName", from.getNickName());
		return view;
	}

	/**
	 * 异步通知付款状态的Controller
	 * 
	 */
	@RequestMapping(value = "recharge/async")
	@ResponseBody
	public Map<String, Object> rechargeAsync(HttpServletRequest request,
			HttpServletResponse response) {

		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("signType", MD5Utils.signType_Constant);
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);

		// 返回为-2时验签不正确
		RequestHandler reqHandler = new RequestHandler(null, null);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		item.put("result", "SUCCESS");
		return webUtil.resMsg(1, "0001", "支付成功", item);
	}

	@ResponseBody
	@RequestMapping(value = "result")
	public Map<String, Object> getPayResult(
			@RequestParam(value = "orderId", required = true) String orderId,
			MD5Utils md5Utils, HttpServletRequest request,
			HttpServletResponse response) {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("orderId", orderId +"");
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		WxPayDto tpWxPay1 = new WxPayDto();
		tpWxPay1.setOrderId(orderId);
		String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
		if ("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])) {

			String tradeNo = msg[1];
			String notifyId = msg[2];
			String attach = msg[3];
			if (companyFacade.checkAlipayResult(tradeNo, notifyId)) {
				companyFacade
						.companyReChargeResult(tradeNo, notifyId, true, "");
				logger.debug("支付宝渠道充值成功:: 对交易号" + tradeNo + "：：支付宝ID"
						+ notifyId);
				return webUtil.resMsg(1, "0001", "支付成功", item);
			} else {
				logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId
						+ "重复回调的屏蔽");
			}
			return webUtil.failedRes("0123", "该条数据已支付", item);
		}
		return webUtil.failedRes("0123", "没有这条数据", item);
	}

	@RequestMapping(value = "/deposit", produces = "application/json")
	public ModelAndView deposit(DepositForm from, ApiFrontUser frontUser,
			MD5Utils md5Utils, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		ModelAndView view = new ModelAndView("deposit/alipay");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		if (!flags) {
			// 返回为-2时验签不正确
			view = new ModelAndView(
					"redirect:/exportRechargeAlipay/submitAlipayRecharge.do");
			view.addObject("userName", frontUser.getUserName());
			view.addObject("nickName", frontUser.getNickName());
			view.addObject("userPass", frontUser.getUserPass());
			view.addObject("amount", from.getAmount() + "");
			return view;
		}

		view = new ModelAndView("deposit/alipay");
		JSONObject item = new JSONObject();
		int payType = from.getPayType();// 支付错误返回方式
		String bank = from.getBank();
		// 判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);

		// 根据IP创建用户
		Integer userId = null;

		ApiFrontUser user11 = new ApiFrontUser();
		user11.setUserName(frontUser.getUserName());
		ApiFrontUser user = userFacade.queryUserByParam(user11);
		boolean flag = false;
		if (user == null) {
			try {
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
			user.setUserName(frontUser.getUserName());
			user.setNickName(frontUser.getNickName());
			user.setUserPass(frontUser.getUserPass());
			user.setUserType(PengPengConstants.PERSON_USER);
			// 未注册的用户//当做一个用户的唯一字段
			user.setRegUsersType(frontUser.getRegUsersType());
			user.setRegisterIP(adressIp);
			logger.debug("募捐归类需要的参数：adressIp:" + adressIp);
			ApiResult apr = userFacade.registered(user);
			if (apr != null && apr.getCode() == 1) {
				ApiFrontUser user1 = new ApiFrontUser();
				user1 = userFacade.queryUserByParam(user);
				userId = user1.getId();
				flag = true;
			}
		} else {
			userId = user.getId();
			flag = true;
		}
		if (!flag) {
			view = new ModelAndView("deposit/error");
			view.addObject("alipay", "用户信息添加失败。");
			return view;
		}

		String total_fee = String.valueOf(from.getAmount());
		Map<String, String> sParaTemp = alipayMap(request, response);
		sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
		sParaTemp.put("total_fee", total_fee);// 价格
		if (StringUtils.isNotEmpty(bank)) {
			sParaTemp.put("paymethod", "bankPay");
			sParaTemp.put("defaultbank", bank);
		}
		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from
				.getProjectId());
		if (project == null) {
			view = new ModelAndView("deposit/error");
			view.addObject("alipay", "支付出现异常，请联系客服。");
			return view;
		}
		sParaTemp.put("subject", "认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
		dRecord.setDonatType(user.getUserType());
		dRecord.setDonatAmount(from.getSumMoney());
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(sParaTemp.get("out_trade_no"));
		dRecord.setSource("PC");
		dRecord.setCompanyId(user.getCompanyId());
		if (request.getSession().getAttribute("extensionPeople") != null
				&& from.getExtensionPeople() == null) {
			dRecord.setExtensionPeople(Integer.valueOf(request.getSession()
					.getAttribute("extensionPeople").toString()));
			ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request
					.getSession().getAttribute("extensionPeople").toString()));
			if (auser != null && auser.getExtensionPeople() != null
					&& auser.getExtensionPeople() == 1) {
				dRecord.setExtensionEnabled(1);// 推广员的有效推广
			}
		} else {
			if (from.getExtensionPeople() != null) {
				dRecord.setExtensionPeople(from.getExtensionPeople());
				ApiFrontUser auser = userFacade.queryById(from
						.getExtensionPeople());
				if (auser != null && auser.getExtensionPeople() != null
						&& auser.getExtensionPeople() == 1) {
					dRecord.setExtensionEnabled(1);// 推广员的有效推广
				}
			}
		}

		if (payType == 1) {
			if (StringUtils.isNotEmpty(from.getDonateName())
					|| StringUtils.isNotEmpty(from.getDonateWord())) {
				if (from.getDonateName().length() > 20
						|| from.getDonateWord().length() > 20) {
					view = new ModelAndView("deposit/error");
					view.addObject("alipay", "请控制在20个字内");
					return view;
				}
			}
			dRecord.setLeaveWord(from.getDonateName() + "|"
					+ from.getDonateWord());
		}
		ApiResult rt = null;
		// 6：个人用户
		sParaTemp.put("extra_common_param", payType + "");
		/*
		 * String nickName = user.getNickName()== null?"":user.getNickName();
		 * String tranNum = dRecord.getTranNum()== null?"":dRecord.getTranNum();
		 * String pName = from.getpName()== null?"":from.getpName(); Double
		 * donateAmount = from.getSumMoney();
		 * 
		 * sParaTemp.put("extra_common_param",
		 * String.valueOf(from.getProjectId()
		 * )+"_"+payType+"_"+nickName+"_"+tranNum+"_"+pName+"_"+donateAmount);
		 */
		if (StringUtils.isNotEmpty(bank)) {
			dRecord.setBankType(BankConstants.BANK_NAME.get(bank));
			rt = donateRecordFacade.buyDonate(dRecord, null, "bankpay",null,"");
		} else {
			rt = donateRecordFacade.buyDonate(dRecord, null, "alipay",null,"");
		}

		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post",
						"确认");
				view.addObject("alipay", sHtmlText);
				return view;
			} catch (Exception e) {
				view = new ModelAndView("deposit/error");
				view.addObject("alipay", "支付出现异常，请联系客服。");
				return view;
			}
		} else {
			view = new ModelAndView("deposit/error");
			view.addObject("error", "支付出现异常，请联系客服。");
			return view;
		}
	}

	/**
	 * 
	 * @param from
	 * @param frontUser
	 *            1、用户类型，2、注册用户类型,每个用户的唯一标识
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/weixin/deposit")
	@ResponseBody
	public Map<String, Object> weixindeposit(DepositForm from,
			ApiFrontUser frontUser, MD5Utils md5Utils,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00");//格式double为0.00
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		// 判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId = null;

		ApiFrontUser user11 = new ApiFrontUser();
		user11.setUserName(frontUser.getUserName());
		ApiFrontUser user = userFacade.queryUserByParam(user11);
		boolean flag = false;
		if (user == null) {
			try {
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
			user.setUserName(frontUser.getUserName());
			user.setNickName(frontUser.getNickName());
			user.setUserPass(frontUser.getUserPass());
			user.setUserType(PengPengConstants.PERSON_USER);
			// 未注册的用户//当做一个用户的唯一字段
			user.setRegUsersType(frontUser.getRegUsersType());
			user.setRegisterIP(adressIp);
			logger.debug("募捐归类需要的参数：adressIp:" + adressIp);
			ApiResult apr = userFacade.registered(user);
			if (apr != null && apr.getCode() == 1) {
				ApiFrontUser user1 = new ApiFrontUser();
				user1 = userFacade.queryUserByParam(user);
				userId = user1.getId();
				flag = true;
			}
		} else {
			userId = user.getId();
			flag = true;
		}
		if (!flag) {
			return webUtil.resMsg(0, "0000", "用户信息添加失败", null);
		}
		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);

		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from
				.getProjectId());
		tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_USER);
		dRecord.setDonatType(user.getUserType());
		dRecord.setDonatAmount(Double.valueOf(total_fee));
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		if (request.getSession().getAttribute("extensionPeople") != null
				&& from.getExtensionPeople() == null) {
			dRecord.setExtensionPeople(Integer.valueOf(request.getSession()
					.getAttribute("extensionPeople").toString()));
			ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request
					.getSession().getAttribute("extensionPeople").toString()));
			if (auser != null && auser.getExtensionPeople() != null
					&& auser.getExtensionPeople() == 1) {
				dRecord.setExtensionEnabled(1);// 推广员的有效推广
			}
		} else {
			if (from.getExtensionPeople() != null) {
				dRecord.setExtensionPeople(from.getExtensionPeople());
				ApiFrontUser auser = userFacade.queryById(from
						.getExtensionPeople());
				if (auser != null && auser.getExtensionPeople() != null
						&& auser.getExtensionPeople() == 1) {
					dRecord.setExtensionEnabled(1);// 推广员的有效推广
				}
			}
		}
		if (from.getDonateWord() != null) {
			dRecord.setLeaveWord(from.getDonateName() + "|"
					+ from.getDonateWord());
		}

		// 6：个人用户
		tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
		ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "tenpay",null,"");
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				// 生成二维码
				String codeurl = Demo.getCodeurl(tpWxPay1);
				Date date = new Date();
				item.put("pName", project.getTitle());
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());
				item.put("codeurl", codeurl);
				item.put("projectId", from.getProjectId());
				item.put("date", date.getTime());
				item.put("nickName", user.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);
			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", null);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", null);
		}
	}

	/*
	 * 正常项目的支付页面
	 */
	@RequestMapping(value = "/visitorpay", produces = "application/json")
	public ModelAndView commondpay(DepositForm from,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView view = new ModelAndView("deposit/notDirectApliy");
		view.addObject("projectId", from.getProjectId());
		view.addObject("pName", from.getpName());
		view.addObject("amount", from.getAmount());
		view.addObject("extensionPeople", from.getExtensionPeople());
		return view;
	}

	/**
	 * 同步通知的页面的Controller
	 * 
	 */
	@RequestMapping(value = "/return_url")
	public ModelAndView Return_url(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = null;
		String tradeStatus = request.getParameter("trade_status");
		String tradeNo = request.getParameter("out_trade_no");
		String notifyId = request.getParameter("trade_no");
		String ep = request.getParameter("extra_common_param");
		/*String tradeStatus = "TRADE_FINISHED";
		String tradeNo = "1245789";
		String notifyId = "1245369";
		String ep = "12154857";*/
		
		RequestHandler reqHandler = new RequestHandler(null, null);
		Date date= new Date();
		// 进行加密判断
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("tradeNo", tradeNo);
		packageParams.put("timeStamp", date.getTime()+"");
		String sign = reqHandler.createSignNoKey(packageParams);
		/*
		 * String ep = request.getParameter("extra_common_param")==null
		 * ?"":request.getParameter("extra_common_param"); String[] items =
		 * ep.split("_"); String projectId = "" ; String payType = ""; String
		 * nickName = "" ; String tranNum = ""; String pName = ""; String
		 * donateAmount = "";
		 * 
		 * if(items != null && items.length>4) { projectId = items[0]; payType =
		 * items[1]; nickName = items[2]; tranNum = items[3]; pName = items[4];
		 * donateAmount = items[5]; }
		 */
		logger.info("tradeStatus >>>>>" + tradeStatus);
		if (tradeStatus.equals("TRADE_FINISHED")
				|| tradeStatus.equals("TRADE_SUCCESS")) {
			if (companyFacade.checkAlipayResult(tradeNo, notifyId)) {
				donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "",
						"freezType");
				logger.debug("微信渠道充值成功:: 对交易号" + tradeNo + "：：微信ID"
						+ notifyId);
				/*
				 * if(payType.equals("1")) { view = new
				 * ModelAndView("redirect:/project/newGardenView.do"); } else {
				 * view = new ModelAndView("redirect:/project/view.do"); }
				 */
				view = new ModelAndView(
						"redirect:http://www.shilehui.com/assistui/syPayReturn.aspx?sign="+sign+"&tradeNo="+tradeNo+"&timeStamp="+date.getTime());
				//view.addObject("tradeNo", tradeNo);
				/*
				 * view.addObject("projectId", projectId);
				 * view.addObject("nickName", nickName); view.addObject("pName",
				 * pName); view.addObject("amount", donateAmount);
				 */
			} else {
				view = new ModelAndView(
						"redirect:http://www.shilehui.com/assistui/syPayReturn.aspx?sign="+sign+"&tradeNo="+tradeNo+"&timeStamp="+date.getTime());
				view.addObject("tradeNo", tradeNo);
				logger.debug("微信渠道充值成功::对交易号" + tradeNo + "：：微信ID" + notifyId
						+ "重复回调的屏蔽");
				logger.info("微信渠道充值成功::对交易号" + tradeNo + "：：微信ID" + notifyId
						+ "重复回调的屏蔽");
			}
			return view;
		} else {
			// 查看错误alipay文档
			donateRecordFacade.buyDonateResult(tradeNo, notifyId, false,
					tradeStatus, "");
			return view = new ModelAndView("redirect:http://www.shilehui.com");
		}
	}

	/**
	 * 异步通知付款状态的Controller
	 * 
	 */
	@RequestMapping(value = "/async")
	public String async(HttpServletRequest request, HttpServletResponse response) {
		String tradeNo = request.getParameter("out_trade_no");
		String tradeStatus = request.getParameter("trade_status");
		String notifyId = request.getParameter("trade_no");
		String ep = request.getParameter("extra_common_param");
		if (tradeStatus.equals("TRADE_FINISHED")
				|| tradeStatus.equals("TRADE_SUCCESS")) {
			// 检查是否已经充值
			if (companyFacade.checkAlipayResult(tradeNo, notifyId)) {
				donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "",
						"");
				logger.info("微信渠道充值成功::对交易号" + tradeNo + "：：微信ID" + notifyId);
			} else {
				logger.info("微信渠道充值成功::对交易号" + tradeNo + "：：微信ID" + notifyId
						+ "重复回调的屏蔽");
			}
		}
		return "/deposit/success";

	}

	private Map<String, String> alipayMap(HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException,
			DocumentException, IOException {
		// 支付类型
		String payment_type = "1";
		// 订单名称
		String subject = "善基金充值";
		// 防钓鱼时间戳
		// 若要使用请调用类文件submit中的query_timestamp函数
		String anti_phishing_key = AlipaySubmit.query_timestamp();
		// 客户端的IP地址
		// 非局域网的外网IP地址，如：221.0.0.1
		String exter_invoke_ip = SSOUtil.getUserIP(request);
		String body = "善基金";
		// 商品展示地址
		String show_url = "http://www.17xs.org";

		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_donate_trade_p");// 接口服务----即时到账
		sParaTemp.put("partner", AlipayConfig.partner);// 支付宝PID
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 统一编码
		sParaTemp.put("payment_type", payment_type);// 支付类型
		sParaTemp.put("notify_url", notify_url);// 异步通知页面
		sParaTemp.put("return_url", return_url);// 页面跳转同步通知页面
		sParaTemp.put("seller_email", AlipayConfig.seller_email);// 卖家支付宝账号
		sParaTemp.put("subject", subject);// 商品名称
		sParaTemp.put("sign_type", AlipayConfig.sign_type);// 支付类型
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		return sParaTemp;
	}

	public ModelAndView getview(int type) {
		ModelAndView view = null;
		if (type == 1) {
			view = new ModelAndView("/deposit/pay");
		} else {
			view = new ModelAndView("/deposit/common_pay");
		}
		return view;
	}
	
	//-------------------------------------------------------
	/**
	 * 
	 * @param from
	 * @param frontUser
	 *            1、用户类型，2、注册用户类型,每个用户的唯一标识
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/weixinPublic/deposit")
	@ResponseBody
	public Map<String, Object> weixindepositPublic(DepositForm from,
			ApiFrontUser frontUser, MD5Utils md5Utils,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00");//格式double为0.00
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		// 判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId = null;
		ApiFrontUser user11 = new ApiFrontUser();
		user11.setUserName(frontUser.getUserName());
		ApiFrontUser user = userFacade.queryUserByParam(user11);
		boolean flag = false;
		if (user == null) {
			try {
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
			user.setUserName(frontUser.getUserName());
			user.setNickName(frontUser.getNickName());
			user.setUserPass(frontUser.getUserPass());
			user.setUserType(PengPengConstants.PERSON_USER);
			// 未注册的用户//当做一个用户的唯一字段
			user.setRegUsersType(frontUser.getRegUsersType());
			user.setRegisterIP(adressIp);
			logger.debug("募捐归类需要的参数：adressIp:" + adressIp);
			ApiResult apr = userFacade.registered(user);
			if (apr != null && apr.getCode() == 1) {
				ApiFrontUser user1 = new ApiFrontUser();
				user1 = userFacade.queryUserByParam(user);
				userId = user1.getId();
				flag = true;
			}
		} else {
			userId = user.getId();
			flag = true;
		}
		if (!flag) {
			return webUtil.resMsg(0, "0000", "用户信息添加失败", null);
		}
		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);
		
		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from
				.getProjectId());
		//查询支付配置id
		ApiPayConfig config = new ApiPayConfig();
		config.setUserId(project.getUserId());
		List<ApiPayConfig> payConfigs = projectFacade.queryByParam(config);
		
		tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_USER);
		dRecord.setDonatType(user.getUserType());
		dRecord.setDonatAmount(Double.valueOf(total_fee));
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		if (request.getSession().getAttribute("extensionPeople") != null
				&& from.getExtensionPeople() == null) {
			dRecord.setExtensionPeople(Integer.valueOf(request.getSession()
					.getAttribute("extensionPeople").toString()));
			ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request
					.getSession().getAttribute("extensionPeople").toString()));
			if (auser != null && auser.getExtensionPeople() != null
					&& auser.getExtensionPeople() == 1) {
				dRecord.setExtensionEnabled(1);// 推广员的有效推广
			}
		} else {
			if (from.getExtensionPeople() != null) {
				dRecord.setExtensionPeople(from.getExtensionPeople());
				ApiFrontUser auser = userFacade.queryById(from
						.getExtensionPeople());
				if (auser != null && auser.getExtensionPeople() != null
						&& auser.getExtensionPeople() == 1) {
					dRecord.setExtensionEnabled(1);// 推广员的有效推广
				}
			}
		}
		if (from.getDonateWord() != null) {
			dRecord.setLeaveWord(from.getDonateName() + "|"
					+ from.getDonateWord());
		}
		// 6：个人用户
		tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
		//添加支付配置id,根据发起人userId查询支付配置id
		ApiCapitalinout capitalinout = new ApiCapitalinout();
		if(payConfigs.size()>0){
			capitalinout.setPayConfigId(payConfigs.get(0).getId());
			dRecord.setPayConfigId(payConfigs.get(0).getId());
		}
		
		ApiResult rt = donateRecordFacade.buyDonate(dRecord,capitalinout, "tenpay",null,"");
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				// 生成二维码
				String codeurl;
				if(payConfigs.size()>0){
					codeurl= Demo.getCodeurlPublic(tpWxPay1,payConfigs.get(0));
				}
				else{
					codeurl= Demo.getCodeurl(tpWxPay1);
				}
				
				Date date = new Date();
				item.put("pName", project.getTitle());
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());
				item.put("codeurl", codeurl);
				item.put("projectId", from.getProjectId());
				item.put("date", date.getTime());
				item.put("nickName", user.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);
			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", null);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", null);
		}
	}
	
	/**
	 * 微信充值接口(相比之前多加字段Id:标识配置微信参数)
	 * 
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "tenPayRechargePublic")
	@ResponseBody
	public Map<String, Object> tenPayRechargePublic(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response, MD5Utils md5Utils) throws Exception {
		ModelAndView view = new ModelAndView(
				"redirect:/exportTenpay/weixinChargePay.do");
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		// 如果验签不正确则返回错误,返回的结果也需要验签
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		// 判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId = null;
		ApiFrontUser user = new ApiFrontUser();
		user.setUserName(frontUser.getUserName());
		ApiFrontUser user11 = new ApiFrontUser();
		user11.setUserName(frontUser.getUserName());
		ApiFrontUser ar = userFacade.queryUserByParam(user11);
		boolean flag = false;
		if (ar == null) {
			try {
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
			user.setNickName(frontUser.getNickName());
			user.setUserPass(frontUser.getUserPass());
			user.setUserType(PengPengConstants.PERSON_USER);
			// 未注册的用户//当做一个用户的唯一字段
			user.setRegUsersType(frontUser.getRegUsersType());
			user.setRegisterIP(adressIp);
			logger.debug("募捐归类需要的参数：adressIp:" + adressIp);
			ApiResult apr = userFacade.registered(user);
			if (apr != null && apr.getCode() == 1) {
				ApiFrontUser user1 = new ApiFrontUser();
				user1 = userFacade.queryUserByParam(user);
				userId = user1.getId();
				flag = true;
			}
		} else {
			userId = ar.getId();
			flag = true;
		}
		if (!flag) {
			return webUtil.resMsg(0, "0000", "该用户无权支付", item);
		}
		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);
		tpWxPay1.setBody("用户" + frontUser.getNickName() + "充值");
		// 附加数据
		tpWxPay1.setAttach(userId + "," + total_fee);
		ApiCapitalinout capitalinout = new ApiCapitalinout();
		capitalinout.setTranNum(tpWxPay1.getOrderId());
		capitalinout.setSource("PC");
		capitalinout.setMoney(from.getAmount());
		capitalinout.setPayType("tenpay");
		capitalinout.setUserId(userId);
		capitalinout.setBankType(from.getBank());
		//查询支付配置id
		List<ApiPayConfig> payConfigs=null;
		if(frontUser.getId()==null||"".equals(frontUser.getId())){
			return webUtil.resMsg(0, "0002", "id不能为空！", item);
		}
		else{
			ApiPayConfig config = new ApiPayConfig();
			config.setUserId(frontUser.getId());
			payConfigs = projectFacade.queryByParam(config);
		}
		if(payConfigs==null||payConfigs.size()==0){
			return webUtil.resMsg(0, "0002", "id参数错误！", item);
		}
		capitalinout.setPayConfigId(payConfigs.get(0).getId());
		// 资金进
		ApiResult rt = companyFacade.companyReCharge(capitalinout);
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				// 生成二维码
				String codeurl;
				if(payConfigs.size()>0){
					codeurl= Demo.getCodeurlPublic(tpWxPay1,payConfigs.get(0));
				}
				else{
					codeurl= Demo.getCodeurl(tpWxPay1);
				}
				Date date = new Date();
				item.put("pName", "善基金充值");
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());
				item.put("codeurl", codeurl);
				item.put("date", date.getTime());
				item.put("nickName", user.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);
			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
		}
	}
	
	/**
	 * 余额捐款
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @param md5Utils
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/availableMoneyDepositPublic")
	@ResponseBody
	public Map<String, Object> availableMoneyDepositPublic(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response, MD5Utils md5Utils) throws Exception {
		
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		DecimalFormat df = new DecimalFormat("######0.00"); 
		ModelAndView view = new ModelAndView("deposit/alipay");
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// sign_Check_field字符串为双方本地验签字段，不可外泄
		packageParams.put("sign_Check_field", MD5Utils.sign_Check_field);
		packageParams.put("timeStamp", md5Utils.getTimeStamp() + "");
		packageParams.put("userName", frontUser.getUserName());
		packageParams.put("nickName", frontUser.getNickName());
		packageParams.put("userPass", frontUser.getUserPass());
		packageParams.put("amount", df.format(from.getAmount()));
		RequestHandler reqHandler = new RequestHandler(null, null);
		boolean flags = false;
		if (MD5Utils.signType_Constant.equals(md5Utils.getSignType())) {
			String sign = reqHandler.createSignNoKey(packageParams);
			if (sign.equals(md5Utils.getSign())) {
				flags = true;
			}
		}
		long localTimeStamp = new Date().getTime(); // 时间戳保证每次生成的签名不一样，该字段为1970年开始的毫秒数
		JSONObject item = new JSONObject();
		SortedMap<String, String> sm = new TreeMap<String, String>();
		sm.put("timeStamp", localTimeStamp + "");
		sm.put("sign_Check_field", MD5Utils.sign_Check_field);
		item.put("timeStamp", localTimeStamp);
		item.put("signType", MD5Utils.signType_Constant);
		String backSign = reqHandler.createSignNoKey(sm);
		item.put("backSign", backSign);
		if (!flags) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "验签错误", item);
		}
		if (from.getProjectId() == 0 || "".equals(from.getProjectId())) {
			// 返回为-2时验签不正确
			return webUtil.resMsg(-2, "0001", "项目id为空", item);
		}
		//查询支付配置id
		List<ApiPayConfig> payConfigs=null;
		if(frontUser.getId()==null||"".equals(frontUser.getId())){
			return webUtil.resMsg(0, "0002", "id不能为空！", item);
		}
		else{
			ApiPayConfig config = new ApiPayConfig();
			config.setUserId(frontUser.getId());
			payConfigs = projectFacade.queryByParam(config);
		}
		if(payConfigs==null||payConfigs.size()==0){
			return webUtil.resMsg(0, "0002", "id参数错误！", item);
		}
		// 判段这个IP是否已经注册过
		// 未注册的用户//当做一个用户的唯一字段
		ApiFrontUser fronUser1 = new ApiFrontUser();
		fronUser1.setRegUsersType(frontUser.getRegUsersType());
		fronUser1.setUserName(frontUser.getUserName());
		fronUser1.setUserPass(frontUser.getUserPass());
		ApiFrontUser af = userFacade.queryUserByParam(fronUser1);
		Integer userId = null;
		if (af == null) {
			// view.addObject("alipay", "没有这个项目");
			return webUtil.resMsg(0, "0000", "余额支付出现问题,请联系管理员", item);
		} else {
			if (af.getId() != null) {
				userId = af.getId();
				if(from.getAmount()>af.getAvailableBalance()){
					return webUtil.resMsg(0, "0001", "捐款金额大于余额", item);
				}
			} else {
				return webUtil.resMsg(0, "0001", "添加该用户信息出现异常，请联系管理员", item);
			}
		}
		String total_fee = String.valueOf(from.getAmount());
		// 扫码支付
		WxPayDto tpWxPay1 = new WxPayDto();
		// 生成订单号
		tpWxPay1.setOrderId(StringUtil.uniqueCode());
		// 获取ip地址
		tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		tpWxPay1.setTotalFee(total_fee);
		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from
				.getProjectId());
		tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(PengPengConstants.PERSON_USER);
		dRecord.setDonatType(af.getUserType());
		dRecord.setDonatAmount(Double.valueOf(total_fee));
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		if (request.getSession().getAttribute("extensionPeople") != null
				&& from.getExtensionPeople() == null) {
			dRecord.setExtensionPeople(Integer.valueOf(request.getSession()
					.getAttribute("extensionPeople").toString()));
			ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request
					.getSession().getAttribute("extensionPeople").toString()));
			if (auser != null && auser.getExtensionPeople() != null
					&& auser.getExtensionPeople() == 1) {
				dRecord.setExtensionEnabled(1);// 推广员的有效推广
			}
		} else {
			if (from.getExtensionPeople() != null) {
				dRecord.setExtensionPeople(from.getExtensionPeople());
				ApiFrontUser auser = userFacade.queryById(from
						.getExtensionPeople());
				if (auser != null && auser.getExtensionPeople() != null
						&& auser.getExtensionPeople() == 1) {
					dRecord.setExtensionEnabled(1);// 推广员的有效推广
				}
			}
		}
		if (from.getDonateWord() != null) {
			dRecord.setLeaveWord(from.getDonateName() + "|"
					+ from.getDonateWord());
		}
		// 6：个人用户
		tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
		dRecord.setPayConfigId(payConfigs.get(0).getId());
		ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "freezType",null,"");
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				Date date = new Date();
				item.put("pName", project.getTitle());
				item.put("amount", from.getAmount());
				item.put("tradeNo", tpWxPay1.getOrderId());
				item.put("projectId", from.getProjectId());
				item.put("date", date.getTime());
				item.put("nickName", af.getNickName());
				return webUtil.resMsg(1, "0001", "成功", item);
			} catch (Exception e) {
				return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
			}
		} else {
			return webUtil.resMsg(0, "0002", "支付出现异常，请联系客服", item);
		}
	}
	
	@RequestMapping(value = "/tenpay/depositTest")
	public ModelAndView weixindeposit2Test(DepositForm from, HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		ModelAndView view = new ModelAndView("h5/weixin_2");
		String browser = UserUtil.Browser(request);
		String openId ="";
		String Token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		//查询支付配置
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
      	ApiPayConfig config = new ApiPayConfig();
      	config.setUserId(project.getUserId());
      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
		if(browser.equals("wx")){
			String weixin_code = request.getParameter("code");
			logger.info("weixin_code>>>>"+weixin_code);
			Map<String, Object> mapToken = new HashMap<String, Object>(8);
			try {
				if ("".equals(openId) || openId == null) {
					
					if ("".equals(weixin_code) || weixin_code == null
							|| weixin_code.equals("authdeny")) {
						String url_weixin_code="";
						if(list.size()>0){
							url_weixin_code=H5Demo.getCodeRequest(perfecturl,list.get(0));
						}
						else{
							url_weixin_code=H5Demo.getCodeRequest(perfecturl);
						}
						view = new ModelAndView("redirect:" + url_weixin_code);
						return view;
					}
					if(list.size()>0){
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code,list.get(0));
					}
					else{
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
					}
					logger.info("mapToken>>>"+mapToken);
					openId = mapToken.get("openid").toString();
					Token = mapToken.get("access_token").toString();
					if(mapToken.get("unionid")!=null){
						unionid = mapToken.get("unionid").toString();
					}
					logger.info("weixinpay>>>>openId>>>"+openId+">>Token>>>"+Token+">>>unionid>>"+unionid);
					if(list.size()==0){
						redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					}
				}
			} catch (Exception e) {
				logger.error("微信支付处理出现问题"+ e);
			}
			view.addObject("payway", browser);
		}
		
		
		
		 view.addObject("pName", project.getTitle());
		 view.addObject("amount", from.getAmount());
		 view.addObject("projectId", from.getProjectId());
		 view.addObject("copies", from.getCopies());
		 view.addObject("crowdFunding", from.getCrowdFunding());
				 
		if(browser.equals("wx")){
			ApiFrontUser user = new ApiFrontUser();
				if(list.size()>0){
					 user = CommonUtils.queryUser(request,openId,Token,unionid,list.get(0));
				}
				else{
					 user = CommonUtils.queryUser(request,openId,Token,unionid);
				}
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
			 }
			 view.addObject("user",user);
			
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setOrderId(StringUtil.uniqueCode());
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			
			ApiDonateRecord dRecord = new ApiDonateRecord();
			tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
	      	if(list.size()>0){
	      		dRecord.setPayConfigId(list.get(0).getId());
	      	}
			dRecord.setExtensionPeople(from.getExtensionPeople());
			dRecord.setUserId(user.getId());
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);
			dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
			dRecord.setDonatType(user.getUserType());
			dRecord.setDonatAmount(Double.valueOf(total_fee));
			dRecord.setDonateCopies(from.getCopies());
			dRecord.setTranNum(tpWxPay1.getOrderId());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			dRecord.setDonateCopies(from.getCopies());
			//dRecord.setLeaveWord(from.getCrowdFunding());
			dRecord.setLeaveWord(from.getDonateWord());
			if(from.getExtensionPeople() != null){
	        	dRecord.setExtensionPeople(from.getExtensionPeople());
	        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
	        	if(auser != null && auser.getExtensionPeople() != null 
	    				&& auser.getExtensionPeople()==1) {
	        		dRecord.setExtensionEnabled(1);//推广员的有效推广
	        	}
	        }
			//6：个人用户
			tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
			ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
	      	if(list.size()>0){
	      		apiCapitalinout.setPayConfigId(list.get(0).getId());
	      	}
			ApiResult rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout,"tenpay",null,"");
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(openId);
						tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
						SortedMap<String, String> map;
						if(list.size()>0){
							map = H5Demo.getPayweixinviewPublic(tpWxPay1,list.get(0));
						}
						else{
							map = H5Demo.getPayweixinview(tpWxPay1);
						}
						logger.info("map>>>"+map);
						view.addObject("appId",map.get("appId"));
						view.addObject("timestamp",map.get("timeStamp"));
						view.addObject("noncestr",map.get("nonceStr"));
						view.addObject("packageValue",map.get("packageValue"));
						view.addObject("paySign",map.get("paySign"));
						view.addObject("paysignType",map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						view.addObject("config_timestamp",config_timestamp);
						view.addObject("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket,accessToken;
							if(list.size()>0){
								accessToken = TenpayUtil.queryAccessToken(list.get(0));
								logger.info("accessToken2>>>"+accessToken);
							}
							else{
								accessToken = TenpayUtil.queryAccessToken();
								logger.info("accessToken1>>>"+accessToken);
							}
							redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
							jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
							logger.info("jsTicket>>>"+jsTicket);
							redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						view.addObject("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
						
						 view.addObject("pName", project.getTitle());
						 view.addObject("amount", from.getAmount());
						 view.addObject("tradeNo", tpWxPay1.getOrderId());
						 view.addObject("projectId", from.getProjectId());
						return view;
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					view.addObject("error", "wx-1");
					return view;
				}
			} else {
				//系统的后台交互
				view.addObject("error", "wx-2");
				return view;
			}
		}
		// 区分分享页捐款成功跳转
		view.addObject("extensionPeople", from.getExtensionPeople());
        view.addObject("donateWord", from.getDonateWord());
		return view;
	}
	
	/**
	 * 微信H5充值接口
	 * @param from
	 * @param frontUser
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenPayh5Recharge/tenPay")
	@ResponseBody
	public String tenPayh5Recharge(DepositForm from,
			ApiFrontUser frontUser, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		String callback = request.getParameter("callback");
		JSONObject items = new JSONObject();
		JSONObject item = new JSONObject();
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
			Integer userId = UserUtil.getUserId(request, response);
			if(userId!=null){
				ApiThirdUser thirdUser = new ApiThirdUser();
				thirdUser = userFacade.queryThirdUserByParam(thirdUser);
			String total_fee = String.valueOf(from.getAmount());
			//扫码支付
		    WxPayDto tpWxPay1 = new WxPayDto();
		    String orderNumber=StringUtil.uniqueCode();
		    item.put("orderNumber", orderNumber);
		    tpWxPay1.setOrderId(orderNumber);
		    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
		    tpWxPay1.setTotalFee(total_fee);
			tpWxPay1.setBody("充值互助项目");
			tpWxPay1.setAttach("huzhu");
			
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("H5");
			capitalinout.setMoney(from.getAmount());

			capitalinout.setPayType("tenpay");

			capitalinout.setUserId(userId);
			capitalinout.setBankType(from.getBank());
			capitalinout.setId(13);//互助充值
			// 资金进
			ApiResult rt = companyFacade.companyReCharge(capitalinout);
			
			// 成功:1
			if (rt != null && rt.getCode() == 1) {
				// 建立请求
				try {
						tpWxPay1.setPayWay("1");
						tpWxPay1.setOpenId(thirdUser.getAccountNum());
						tpWxPay1.setNotifyUrl("http://www.17xs.org/tenpay/asyncresult/"+tpWxPay1.getOrderId()+"/");
						SortedMap<String, String> map;
						map = H5Demo.getPayweixinview(tpWxPay1);
						logger.info("map>>>"+map);
						item.put("appId", map.get("appId"));
						item.put("timestamp", map.get("timeStamp"));
						item.put("noncestr", map.get("nonceStr"));
						item.put("packageValue", map.get("packageValue"));
						item.put("paySign", map.get("paySign"));
						item.put("paysignType", map.get("signType"));
						
						String config_timestamp = String.valueOf(new Date().getTime());
						String config_noncestr = H5Demo.getNonceStr();
						item.put("config_timestamp",config_timestamp);
						item.put("config_noncestr",config_noncestr);
						
						//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
						String jsTicket,accessToken;
						accessToken = TenpayUtil.queryAccessToken();
						logger.info("accessToken1>>>"+accessToken);
						redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
						jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
						logger.info("jsTicket>>>"+jsTicket);
						redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
						item.put("signature",H5Demo.getSignature(jsTicket, config_timestamp, config_noncestr, perfecturl));
						
						items.put("msg", "成功");
						items.put("code", 1);
						items.put("data", item);
				} catch (Exception e) {
					//订单交互
					logger.error("微信支付订单交互"+e.getStackTrace());
					items.put("msg", "失败");
					items.put("code", 0);
					items.put("data", item);
				}
			} else {
				items.put("msg", "失败");
				items.put("code", 0);
				items.put("data", item);
			}}
			else{
				items.put("msg", "用户未登录");
				items.put("code", 0);
				items.put("data", item);
			}
			return callback+"("+items+")";
	}
}
