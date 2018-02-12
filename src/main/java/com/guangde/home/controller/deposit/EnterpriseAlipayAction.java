package com.guangde.home.controller.deposit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiProject;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;

@Controller
@RequestMapping("eAlipay")
public class EnterpriseAlipayAction {
		// 服务器异步通知页面路径
		public static final String notify_url = "http://www.17xs.org/eAlipay/async.do";
		// 页面跳转同步通知页面路径
		public static final String return_url = "http://www.17xs.org/eAlipay/return_url.do";

		Logger logger = LoggerFactory.getLogger(EnterpriseAlipayAction.class);
		@Autowired
		private IDonateRecordFacade donateRecordFacade;
		@Autowired
		private IProjectFacade projectFacade;
		@Autowired
		private  ICompanyFacade companyFacade;

		@RequestMapping(value = "/deposit", produces = "application/json")
		public ModelAndView deposit(DepositForm from, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ModelAndView view = new ModelAndView("deposit/alipay");
			int payType =from.getPayType();//支付错误返回方式
			String bank = from.getBank();//银行代号
			Integer userId = UserUtil.getUserId(request, response);
			if (userId == null) {
				view = new ModelAndView("redirect:/user/sso/login.do");
				return view;
			}
			String total_fee = String.valueOf(from.getAmount());
			Map<String, String> sParaTemp = alipayMap(request, response);
			sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
			sParaTemp.put("total_fee", total_fee);// 价格
			if(StringUtils.isNotEmpty(bank)){
				sParaTemp.put("paymethod", "bankPay");
				sParaTemp.put("defaultbank", from.getBank());
			}
			ApiResult rt = null;
			//企业的充值
			if(payType == 4){
				sParaTemp.put("subject", "善基金充值");
				sParaTemp.put("extra_common_param", "4");
				ApiCapitalinout capitalinout = new ApiCapitalinout();
				capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
				capitalinout.setSource("PC");
				capitalinout.setMoney(from.getAmount());
				if(StringUtils.isNotEmpty(bank)){
					capitalinout.setPayType("bankpay");
					capitalinout.setBankType(BankConstants.BANK_NAME.get(bank));
				}else{
					capitalinout.setPayType("alipay");
				}
				capitalinout.setUserId(userId);
				capitalinout.setBankType(from.getBank());
				rt = companyFacade.companyReCharge(capitalinout);
			}else if(payType == 5) {//企业助善充值
				ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
				sParaTemp.put("subject", "助善“"+project.getTitle()+"”项目");
				sParaTemp.put("extra_common_param", "5");
				ApiCapitalinout capitalinout = new ApiCapitalinout();
				capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
				capitalinout.setSource("PC");
				capitalinout.setMoney(from.getAmount());
				if(StringUtils.isNotEmpty(bank)){
					capitalinout.setPayType("bankpay");
					capitalinout.setBankType(BankConstants.BANK_NAME.get(bank));
				}else{
					capitalinout.setPayType("alipay");
				}
				capitalinout.setUserId(userId);
				capitalinout.setBankType(from.getBank());
				
			ApiCompany_GoodHelp apiGoodHelp = new ApiCompany_GoodHelp();
			apiGoodHelp.setUserId(userId);
			apiGoodHelp.setProject_id(from.getProjectId());
			apiGoodHelp.setFreezAmount(from.getSumMoney());
			apiGoodHelp.setPerMoney(from.getPerMoney());

			// 获取companyId 统一加缓存处理
			ApiCompany apiCompany = new ApiCompany();
			apiCompany.setUserId(userId);
			apiCompany = companyFacade.queryCompanyByParam(apiCompany);
			apiGoodHelp.setCompany_id(apiCompany.getId());
			rt = companyFacade.launchCompanyGoodHelp(apiGoodHelp, capitalinout);
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
					view = getview(payType);
					view.addObject("error", "支付出现异常，请联系客服。");
					return view;
				}
			}else if (rt.getCode() == 90010) {
				view.addObject("alipay", rt.getMessage());
				return view;
			} else if (rt.getCode() == 90001) {
				view = getview(payType);
				view.addObject("tokenCode", from.getTokenCode());
				view.addObject("projectId", from.getProjectId());
				view.addObject("copies", from.getCopies());
				view.addObject("amount", from.getAmount());
				view.addObject("error", rt.getMessage());
				return view;
			} else {
				view = getview(payType);
				view.addObject("error", "支付出现异常，请联系客服。");
				return view;
			}
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
			ApiResult rt = null;
			if (tradeStatus.equals("TRADE_FINISHED")
				|| tradeStatus.equals("TRADE_SUCCESS")) {
				if("4".equals(ep)){
					if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
						companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
					}else {
						logger.debug("对交易号"+tradeNo+"：：支付宝ID"+notifyId+"重复回调的屏蔽");
					}
					view = new ModelAndView("redirect:/ucenter/core/chargeRecord.do");
				}else if("5".equals(ep)){
					if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
						companyFacade.launchCompanyGoodHelpResult(tradeNo, notifyId, true, "");
					}else {
						logger.debug("对交易号"+tradeNo+"：：支付宝ID"+notifyId+"重复回调的屏蔽");
					}
					view = new ModelAndView("redirect:/enproject/core/zhuShanList.do");
				}
				return view;
			} else {
				// 查看错误alipay文档
				companyFacade.companyReChargeResult(tradeNo, notifyId, false, "");
				return view = new ModelAndView("/deposit/error");
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
					//判断充值记录是否已经回调完成
					if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
						if("4".equals(ep)){
							companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
						}else if("5".equals(ep)){
							companyFacade.launchCompanyGoodHelpResult(tradeNo, notifyId, true, "");
						}
					}else {
						logger.debug("对交易号"+tradeNo+"：：支付宝ID"+notifyId+"重复回调的屏蔽");
					}
					// 要写的逻辑。自己按自己的要求写
					System.out.println(">>>>>充值成功" + tradeNo);
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
		public ModelAndView getview(int type){
			ModelAndView view = null;
			if(type == 1){
				view = new ModelAndView("/deposit/pay");
			}else if(type == 5){
				view = new ModelAndView("/deposit/common_pay");
			}else {
				view = new ModelAndView("/deposit/common_pay");
			}
			return view;
		}
}
