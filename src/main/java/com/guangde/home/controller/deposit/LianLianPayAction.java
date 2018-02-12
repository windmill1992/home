package com.guangde.home.controller.deposit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiFrontUser;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.deposit.LianlianForm;
import com.llpay.config.PartnerConfig;
import com.llpay.config.ServerURLConfig;
import com.llpay.conn.HttpRequestSimple;
import com.llpay.utils.LLPayUtil;
import com.llpay.vo.PayDataBean;
import com.llpay.vo.PaymentInfo;
import com.llpay.vo.RetBean;

/**
 * 
 * @author guoqw
 * 
 */
@Controller
@RequestMapping("lianlian")
public class LianLianPayAction extends DepositBaseAction {

	Logger logger = LoggerFactory.getLogger(LianLianPayAction.class);
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IUserFacade userFacade;

	@RequestMapping("toPay")
	private ModelAndView pay(HttpServletRequest request,
			HttpServletResponse response, LianlianForm from,int type) throws Exception {
		ModelAndView view = new ModelAndView("deposit/gotoPrepositPay");
		logger.info("进入连连支付");
		Date date = new Date();
		// 进行用户的登陆拦截
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		from.setNo_order(date.getTime() + StringUtil.randonNums(4));// 商品订单编号
		from.setMoney_order("0.02");
		from.setCardNumber("6212261202010088137");
		ApiFrontUser user = userFacade.queryById(userId);
		if (user.getRealState() != 203) {
			view = new ModelAndView("redirect:/user/sso/perpect.do");
			return view;
		}
		if(type == 1){
			from.setPartner(PartnerConfig.OID_PARTNER);
			from.setMd5Key(PartnerConfig.MD5_KEY);
			from.setPayUrl(ServerURLConfig.PAY_URL);
		}else {
			from.setPartner(PartnerConfig.AUT_OID_PARTNER);
			from.setMd5Key(PartnerConfig.AUT_MD5_KEY);
			from.setPayUrl(ServerURLConfig.AUT_PAY_URL);
		}
		from.setRealname(user.getRealName());
		from.setIdNumber(user.getIdCard());
		prepositPay(request, from, user);
		return view;
	}

	
	/**
	 * 同步通知的页面的Controller
	 * 
	 */
	@RequestMapping(value = "/return_url")
	public ModelAndView Return_url(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("/deposit/success");
        System.out.println("进入支付同步通知数据接收处理");
        String reqStr = LLPayUtil.readReqStr(request);
        if (LLPayUtil.isnull(reqStr))
        {
        	view = new ModelAndView("/deposit/error");
        	return view;
        }
        System.out.println("接收支付同步通知数据：【" + reqStr + "】");
        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		try
        {
			if("D".equals(payDataBean.getPay_type())){
				if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY,
	                    PartnerConfig.AUT_MD5_KEY))
	            {
	            	System.out.println("支付同步通知验签失败");
	            	view = new ModelAndView("/deposit/error");
	            	return view;
	            }
			}else {
				if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY,
	                    PartnerConfig.MD5_KEY))
	            {
	            	System.out.println("支付同步通知验签失败");
	            	view = new ModelAndView("/deposit/error");
	            	return view;
	            }
			}
            
        } catch (Exception e)
        {
            System.out.println("同步通知报文解析异常：" + e);
            view = new ModelAndView("/deposit/error");
        	return view;
        }
        System.out.println("支付同步通知数据接收处理成功");
        // 解析异步通知对象
        String tradeNo = payDataBean.getNo_order();//商户的订单号
        String payNo = payDataBean.getOid_paybill();//连连的订单号
        donateRecordFacade.buyDonateResult(tradeNo, payNo, true,
					"","");
        return view;
	}

	/**
	 * 异步通知付款状态的Controller
	 * 
	 */
	@RequestMapping(value = "/async")
	public String async(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入支付异步通知数据接收处理");
        String reqStr = LLPayUtil.readReqStr(request);
        if (LLPayUtil.isnull(reqStr))
        {
        	return "/deposit/error";
        }
        System.out.println("接收支付异步通知数据：【" + reqStr + "】");
        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		try
        {
			if("D".equals(payDataBean.getPay_type())){
				if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY,
	                    PartnerConfig.AUT_MD5_KEY))
	            {
	            	System.out.println("支付异步通知验签失败");
	            	return"/deposit/error";
	            }
			}else {
				if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY,
	                    PartnerConfig.MD5_KEY))
	            {
	            	System.out.println("支付异步通知验签失败");
	            	return "/deposit/error";
	            }
			}
            
        } catch (Exception e)
        {
            System.out.println("异步通知报文解析异常：" + e);
            return "/deposit/error";
        }
        System.out.println("支付同步通知数据接收处理成功");
        // 解析异步通知对象
        String tradeNo = payDataBean.getNo_order();//商户的订单号
        String payNo = payDataBean.getOid_paybill();//连连的订单号
        donateRecordFacade.buyDonateResult(tradeNo, payNo, true,
					"","");
        return "success";
	}
//	/**
//	 * 银行卡卡bin信息查询
//	 * 
//	 * @param req
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("coredata/cardbin")
//	public Map<String, Object> queryCardBin(String cardno,int type) {
//
//		JSONObject reqObj = new JSONObject();
//		if(type == 1){
//			reqObj.put("oid_partner", PartnerConfig.AUT_OID_PARTNER);
//		}else {
//			reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
//		}
//		reqObj.put("card_no", cardno);
//		reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
//		reqObj.put("pay_type", "D");
//		reqObj.put("flag_amt_limit", "1");
//		String sign = null;
//		if(type == 1){
//			sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY,PartnerConfig.AUT_MD5_KEY);
//		}else {
//			sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY,PartnerConfig.MD5_KEY);
//		}
//		reqObj.put("sign", sign);
//		String reqJSON = reqObj.toString();
//		String resJSON = HttpRequestSimple.getInstance().postSendHttp(
//				ServerURLConfig.QUERY_BANKCARD_URL, reqJSON);
//		logger.info("卡号"+cardno+"：：银行卡卡bin信息查询响应报文[" + resJSON + "]");
//		JSONObject jo = JSONObject.parseObject(resJSON);
//		Map<String, Object> bankMap = new HashMap<String, Object>();
//		if("0000".equals(jo.get("ret_code"))){
//			bankMap.put("bank_name", jo.get("bank_name"));
//			bankMap.put("bank_code", jo.get("bank_code"));
//			bankMap.put("card_type", jo.get("card_type"));
//			bankMap.put("day_amt", jo.get("day_amt"));
//			bankMap.put("single_amt", jo.get("single_amt"));
//			bankMap.put("month_amt", jo.get("month_amt"));
//			if(type != 1 && BankConstants.AUTBANK.contains(jo.get("bank_code"))){
//				return queryCardBin(cardno,1);
//			}else {
//				return webUtil.successRes(bankMap);
//			}
//		}else {
//			return webUtil.failedRes("20001", jo.get("ret_msg").toString(), null);
//		}
//	}
	
	/**
	 * 银行卡卡bin信息查询
	 * 
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("coredata/cardbin")
	public Map<String, Object> queryCardBin(String cardno,int type) {

		JSONObject reqObj = new JSONObject();
		reqObj.put("oid_partner", PartnerConfig.OID_PARTNER);
		reqObj.put("card_no", cardno);
		reqObj.put("sign_type", PartnerConfig.SIGN_TYPE);
		reqObj.put("pay_type", "D");
		reqObj.put("flag_amt_limit", "1");
		String sign = null;
		sign = LLPayUtil.addSign(reqObj, PartnerConfig.TRADER_PRI_KEY,PartnerConfig.MD5_KEY);
		reqObj.put("sign", sign);
		String reqJSON = reqObj.toString();
		String resJSON = HttpRequestSimple.getInstance().postSendHttp(
				ServerURLConfig.QUERY_BANKCARD_URL, reqJSON);
		logger.info("卡号"+cardno+"：：银行卡卡bin信息查询响应报文[" + resJSON + "]");
		JSONObject jo = JSONObject.parseObject(resJSON);
		Map<String, Object> bankMap = new HashMap<String, Object>();
		if("0000".equals(jo.get("ret_code"))){
			bankMap.put("bank_name", jo.get("bank_name"));
			bankMap.put("bank_code", jo.get("bank_code"));
			bankMap.put("card_type", jo.get("card_type"));
			bankMap.put("day_amt", jo.get("day_amt"));
			bankMap.put("single_amt", jo.get("single_amt"));
			bankMap.put("month_amt", jo.get("month_amt"));
			return webUtil.successRes(bankMap);
		}else {
			return webUtil.failedRes("20001", jo.get("ret_msg").toString(), null);
		}
	}
	public static void main(String[] args) {
//		String json = queryCardBin("6212261202010088137",1);
//		JSONObject jo = JSONObject.parseObject(json);
//		System.out.println(jo.get("bank_code"));
	}

	/**
	 * 卡前置支付处理
	 * 
	 * @param req
	 * @param order
	 */
	private void prepositPay(HttpServletRequest req, LianlianForm from,
			ApiFrontUser user) {
		// 构造支付请求对象
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setVersion(PartnerConfig.VERSION);// 版本号
		paymentInfo.setOid_partner(from.getPartner());// 支付交易商户编号
		paymentInfo.setUser_id(String.valueOf(user.getId()));// 商户用户唯一编号
		paymentInfo.setSign_type(PartnerConfig.SIGN_TYPE);// 签名方式
		paymentInfo.setBusi_partner(PartnerConfig.BUSI_PARTNER);// 商户业务类型
		paymentInfo.setNo_order(from.getNo_order());// 商户唯一订单号
		paymentInfo.setDt_order(LLPayUtil.getCurrentDateTimeStr());// 商户订单时间
		paymentInfo.setName_goods("善基金充值");// 商品名称
		paymentInfo.setInfo_order("用户善款善基金充值");// 订单描述
		paymentInfo.setMoney_order(from.getMoney_order());// 交易金额
		paymentInfo.setNotify_url(PartnerConfig.NOTIFY_URL);// 服务器异步通知地址
		paymentInfo.setUrl_return(PartnerConfig.URL_RETURN);// 支付结束回显url
		paymentInfo.setUserreq_ip(LLPayUtil.getIpAddr(req));// 用户端申请 IP
		paymentInfo.setUrl_order("");// 订单地址
		paymentInfo.setValid_order(PartnerConfig.VALID_ORDER);// 单位分钟，可以为空，默认7天
		paymentInfo.setRisk_item(createRiskItem());
		paymentInfo.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
		paymentInfo.setNo_agree(from.getNo_agree());
		// 从系统中获取用户身份信息
		paymentInfo.setId_type("0");
		paymentInfo.setId_no(from.getIdNumber());
		paymentInfo.setAcct_name(from.getRealname());
		paymentInfo.setFlag_modify("1");
		paymentInfo.setCard_no(from.getCardNumber());
		paymentInfo.setBack_url("http://www.17xs.org/");
		// 加签名
		String sign = LLPayUtil.addSign(
				JSON.parseObject(JSON.toJSONString(paymentInfo)),
				PartnerConfig.TRADER_PRI_KEY, from.getMd5Key());
		paymentInfo.setSign(sign);

		req.setAttribute("version", paymentInfo.getVersion());
		req.setAttribute("oid_partner", paymentInfo.getOid_partner());
		req.setAttribute("user_id", paymentInfo.getUser_id());
		req.setAttribute("sign_type", paymentInfo.getSign_type());
		req.setAttribute("busi_partner", paymentInfo.getBusi_partner());
		req.setAttribute("no_order", paymentInfo.getNo_order());
		req.setAttribute("dt_order", paymentInfo.getDt_order());
		req.setAttribute("name_goods", paymentInfo.getName_goods());
		req.setAttribute("info_order", paymentInfo.getInfo_order());
		req.setAttribute("money_order", paymentInfo.getMoney_order());
		req.setAttribute("notify_url", paymentInfo.getNotify_url());
		req.setAttribute("url_return", paymentInfo.getUrl_return());
		req.setAttribute("userreq_ip", paymentInfo.getUserreq_ip());
		req.setAttribute("url_order", paymentInfo.getUrl_order());
		req.setAttribute("valid_order", paymentInfo.getValid_order());
		req.setAttribute("timestamp", paymentInfo.getTimestamp());
		req.setAttribute("sign", paymentInfo.getSign());
		req.setAttribute("risk_item", paymentInfo.getRisk_item());
		req.setAttribute("no_agree", paymentInfo.getNo_agree());
		req.setAttribute("id_type", paymentInfo.getId_type());
		req.setAttribute("id_no", paymentInfo.getId_no());
		req.setAttribute("acct_name", paymentInfo.getAcct_name());
		req.setAttribute("flag_modify", paymentInfo.getFlag_modify());
		req.setAttribute("card_no", paymentInfo.getCard_no());
		req.setAttribute("back_url", paymentInfo.getBack_url());
		req.setAttribute("req_url", from.getPayUrl());
	}

	/**
	 * 根据连连支付风控部门要求的参数进行构造风控参数
	 * 
	 * @return
	 */
	private String createRiskItem() {
		JSONObject riskItemObj = new JSONObject();
		riskItemObj.put("user_info_full_name", "你好");
		riskItemObj.put("frms_ware_category", "1999");
		return riskItemObj.toString();
	}
}
