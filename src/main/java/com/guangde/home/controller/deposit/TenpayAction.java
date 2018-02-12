package com.guangde.home.controller.deposit;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.zxing.WriterException;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.homepage.IProjectVolunteerFacade;
import com.guangde.api.user.*;
import com.guangde.entry.*;
import com.guangde.home.utils.*;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.Demo;
import com.tenpay.demo.QrCode;
import com.tenpay.demo.WxPayDto;
import com.tenpay.demo.Wx_Send_Model.SendModel;
import com.tenpay.utils.TenpayUtil;
import com.tenpay.utils.http.HttpConnect;
import com.tenpay.utils.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("tenpay")
public class TenpayAction extends DepositBaseAction {

	// 服务器异步通知页面路径
	public static final String notify_url = "http://www.17xs.org/tenpay/async.do";
	// 页面跳转同步通知页面路径
	public static final String return_url = "http://www.17xs.org/tenpay/return_url.do";
	//二维码的底版
	public static final String imgPath = "/res/images/charge/wx-pay-code.jpg";
	//二维ima的图标
	public static final String srcPath = "/res/images/charge/logo-pay.png";
	public static final String huzhu_order_state_url="http://hz.17xs.org/huzhu/pay";
	public static final String huzhu_order_state_url_balance="http://hz.17xs.org/huzhu/recharge";
	public static final String yunhu_url="http://yhbk.tw0577.com/native_notify.php?headImg=HeadImg&nickName=NickName&amount=Amount&projectId=ProjectId&donateTime=DonateTime&yunhu_userId=Yunhu_userId";

	Logger logger = LoggerFactory.getLogger(TenpayAction.class);

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

	@Autowired
	private ICommonFacade commonFacade;

	@Autowired
	private IRedPacketsFacade redPacketsFacade;

	@Autowired
	private IProjectVolunteerFacade projectVolunteerFacade;

	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;

	@RequestMapping(value = "/deposit", produces = "application/json")
	public ModelAndView deposit(DepositForm from, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView("redirect:/tenpay/pay.do");
		int payType = from.getPayType();
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		String total_fee = String.valueOf(from.getAmount());
		//扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(StringUtil.uniqueCode());
	    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
	    tpWxPay1.setTotalFee(total_fee);

		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(user.getUserType());
		dRecord.setDonatType(user.getUserType());
		dRecord.setDonatAmount(from.getSumMoney());
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		dRecord.setCompanyId(user.getCompanyId());
		if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
		if(payType == 1){
			if (StringUtils.isNotEmpty(from.getDonateName())
					|| StringUtils.isEmpty(from.getDonateWord())) {
				if (from.getDonateName().length() > 20
						|| from.getDonateWord().length() > 20) {
					view = new ModelAndView("/deposit/pay");
					view.addObject("error", "请控制在20个字内");
					return view;
				}
			}
			dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
		}

		ApiResult rt = null;
//		if("enterpriseUsers".equals(user.getUserType())){
			if(from.getAmount() == 0){
				rt = donateRecordFacade.buyDonate(dRecord, null,"freezType",null,"");
				view = new ModelAndView("redirect:/ucenter/core/mygood.do");
				return view;
			}
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			//4 ：表示企业用户
			tpWxPay1.setAttach("4");
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("PC");
			capitalinout.setMoney(from.getAmount());
			capitalinout.setPayType("tenpay");
			capitalinout.setInType(1);
			capitalinout.setUserId(userId);
			capitalinout.setBankType(from.getBank());
			logger.info("updateDonate receive param>>" + dRecord);
			logger.info("addCapitalinout receive param>>" + capitalinout);
			rt = donateRecordFacade.buyDonate(dRecord, capitalinout,"freezType",null,"");
//		}else{
//			//6：个人用户
//			tpWxPay1.setAttach("6");
//			rt = donateRecordFacade.buyDonate(dRecord, null,"tenpay");
//		}
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				//生成二维码
			    String codeurl = Demo.getCodeurl(tpWxPay1);
			    logger.info(codeurl);
			    Date date = new Date();
			    view.addObject("pName", project.getTitle());
			    view.addObject("amount", from.getAmount());
			    view.addObject("tradeNo", tpWxPay1.getOrderId());
			    view.addObject("codeurl", codeurl);
			    view.addObject("projectId", from.getProjectId());
			    view.addObject("date", date.getTime());
				return view;
			} catch (Exception e) {
				logger.error("二维码异常", e);
				view.addObject("error", "支付出现异常，请联系客服!");
				System.out.println("支付出现异常，请联系客服");
				return view;
			}
		} else {
			logger.info("rt is null");
			view.addObject("error", "支付出现异常，请联系客服!!!");
			System.out.println("支付出现异常，请联系客服22");
			return view;
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
			@RequestParam(value = "codeurl", required = true, defaultValue = "codeurl") String codeurl,
			HttpServletRequest request, HttpServletResponse response) {
		String codekey = null;
		String path = request.getSession().getServletContext().getRealPath("/");
		try {
			ServletOutputStream sos = response.getOutputStream();
	    	BufferedImage image = QrCode.genBarcode(codeurl, 300, 300, path+srcPath);
			// 禁止图像缓存。
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			// 将图像输出到输出流中。
			ImageIO.write(image, "jpeg", sos);
		}catch (WriterException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		// 输出验证码错误的图片
	}
	/*
	 * 正常项目的支付页面
	 */
	@RequestMapping(value = "/pay")
	public ModelAndView commondpay(DepositForm from, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView("deposit/weixin");
		String field = "";
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		if(project != null){
			field = project.getField();
		}
		view.addObject("field", field);
		view.addObject("title", from.getpName());
	    view.addObject("amount", from.getAmount());
	    view.addObject("tradeNo", from.getTradeNo());
	    SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
	@RequestMapping(value = "/async")
	public String async(HttpServletRequest request, HttpServletResponse response) {
		String tradeNo = request.getParameter("out_trade_no");
		String tradeStatus = request.getParameter("result_code");
		String notifyId = request.getParameter("transaction_id");
		String ep = request.getParameter("attach");
		logger.info("ep>>>"+ep);
		logger.info("tradeStatus>>>"+tradeStatus);
			if (StringUtils.isNotEmpty(tradeStatus) && tradeStatus.equals("SUCCESS")) {
				if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
					if("4".equals(ep)){logger.info("ep>>>freezType");
						donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","freezType");
					}else if("5".equals(ep)) {logger.info("ep>>>alipay");
						donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","alipay");
					}else if("huzhu".equals(ep)){logger.info("ep>>>huzhu");
						donateRecordFacade.reChargeResult(tradeNo,notifyId,true,"","tenpay");
						//通知互助系统支付成功

						String url = huzhu_order_state_url+"/"+UserUtil.getUserId(request, response)+"/"+tradeNo+"/1";
						HttpConnect connect = new HttpConnect();
						connect.doPostStr(url);
					}else{logger.info("ep>>>null");
						donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					}
				}else {
					logger.info("对交易号"+tradeNo+"：：微信ID"+notifyId+"重复回调的屏蔽");
				}
				// 要写的逻辑。自己按自己的要求写
				logger.info("微信充值成功>>>"+tradeNo);
				System.out.println(">>>>>充值成功 微信" + tradeNo);
			}
			return "/deposit/success";

	}
	@ResponseBody
	@RequestMapping(value = "result")
	public Map<String, Object> getPayResult(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response){
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    ApiDonateRecord dd = donateRecordFacade.queryPayNoticeByTranNum(OrderId);
	    String[] msg;
	    if(dd!=null&&dd.getProjectId()!=null){
	    	ApiProject project = projectFacade.queryProjectDetail(dd.getProjectId());
	    	ApiPayConfig config = new ApiPayConfig();
	      	config.setUserId(project.getUserId());
	      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
	      	if(list.size()>0){
	      		msg = Demo.getPayResult(tpWxPay1,list.get(0)).split("\\|");
	      	}
	      	else{
	      		msg = Demo.getPayResult(tpWxPay1).split("\\|");
	      	}
	    }
	    else{
	    	msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    }
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	Integer pId = donateRecordFacade.queryProjectIdByCapitalinout(OrderId);
	    	Integer userId = UserUtil.getUserId(request, response);
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else{
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
				}
			}
	    	if(userId == null){
	    		return webUtil.successRes(pId);
	    	}else {
	    		ApiFrontUser user = userFacade.queryById(userId);
	    		if("individualUsers".equals(user.getUserType())){
	    			return webUtil.successRes("individualUsers");
	    		}else if ("enterpriseUsers".equals(user.getUserType())) {
	    			return webUtil.successRes("enterpriseUsers");
				}
	    		else if("touristUsers".equals(user.getUserType()))
				{
	    			return webUtil.successRes(pId);
				}
			}
	    }
		return webUtil.failedRes("0123", "没有这条数据", null) ;
	}

	@ResponseBody
	@RequestMapping(value = "h5result")
	public ModelAndView getPayH5Result(@RequestParam(value = "OrderId", required = true) String OrderId,
			@RequestParam(value = "projectId", required = true) String projectId,
			HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView("redirect:/project/view_h5.do?projectId="+projectId);
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    ApiDonateRecord dd = donateRecordFacade.queryPayNoticeByTranNum(OrderId);
	    String[] msg;
	    if(dd!=null&&dd.getProjectId()!=null){
	    	ApiProject project = projectFacade.queryProjectDetail(dd.getProjectId());
	    	ApiPayConfig config = new ApiPayConfig();
	      	config.setUserId(project.getUserId());
	      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
	      	if(list.size()>0){
	      		msg = Demo.getPayResult(tpWxPay1,list.get(0)).split("\\|");
	      	}
	      	else{
	      		msg = Demo.getPayResult(tpWxPay1).split("\\|");
	      	}
	    }
	    else{
	    	msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    }
	    //String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else{
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
				}
			}
	    }
	    return view;
	}

	@ResponseBody
	@RequestMapping(value = "asyncresult")
	public void getPayAsyncResult(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    logger.info("param orderId >>"+ OrderId);
	    ApiDonateRecord dd = donateRecordFacade.queryPayNoticeByTranNum(OrderId);
	    //logger.info("dd>>>>>>"+dd.toString());
	    String[] msg;
	    if(dd!=null&&dd.getProjectId()!=null){logger.info("shanyuan>>>>>>>>>");
	    	ApiProject project = projectFacade.queryProjectDetail(dd.getProjectId());
	    	ApiPayConfig config = new ApiPayConfig();
	      	config.setUserId(project.getUserId());
	      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
	      	if(list.size()>0){
			    msg = Demo.getPayResult(tpWxPay1,list.get(0)).split("\\|");
	      		logger.info("Demo.getPayResult(tpWxPay1,config)>>>"+Demo.getPayResult(tpWxPay1,list.get(0)));
	      	}
	      	else{
	      		logger.info("Demo.getPayResult(tpWxPay1)>>>>>>"+Demo.getPayResult(tpWxPay1));
			    msg = Demo.getPayResult(tpWxPay1).split("\\|");
	      	}
	    }
	    else{//flt支付回调
	    	logger.info("》》》》》》》》》调取flt");
	    	String url = "http://admin.flt.17xs.org/tenpay/asyncresult.do?OrderId=T"+OrderId;
			HttpConnect connect = new HttpConnect();
			HttpResponse httpResponse = connect.doGetStr(url);
			logger.info("调取end");

	    	logger.info("Demo.getPayResult(tpWxPay1)>>>>>>"+Demo.getPayResult(tpWxPay1));
		    msg = Demo.getPayResult(tpWxPay1).split("\\|");
		    if(msg.length<4){
		    	tpWxPay1.setOrderId("T"+OrderId);
		    	logger.info("Demo.getPayResult(tpWxPay1)>>>>>>"+Demo.getPayResult(tpWxPay1));
			    msg = Demo.getPayResult(tpWxPay1).split("\\|");
		    }
	    }
	    if(msg.length<4){
	    	return ;
	    }
	    logger.info("Demo.getPayResult>>>>" + msg[0] + ">>>" + msg[1] + ">>>" + msg[2] + ">>>" + msg[3]);

	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else if("huzhu".equals(attach)){logger.info("ep>>>huzhu");
					donateRecordFacade.reChargeResult(tradeNo,notifyId,true,"","tenpay");
					//通知互助系统支付成功
					//根据订单号查询用户id
					ApiCapitalinout capitalinout = new ApiCapitalinout();
					capitalinout.setTranNum(tradeNo);
					capitalinout= donateRecordFacade.queryCapitalinoutByParam(capitalinout);
					logger.info("homeUserId>>>>>>"+capitalinout.getUserId());
					String url = huzhu_order_state_url+"/"+capitalinout.getUserId()+"/"+tradeNo+"/1";
					HttpConnect connect = new HttpConnect();
					HttpResponse httpResponse = connect.doPostStr(url);
					logger.info("调取end");
				}else if("huzhuBalance".equals(attach)){logger.info("ep>>>huzhuBalance");
					donateRecordFacade.reChargeResult(tradeNo,notifyId,true,"","tenpay");
					//通知互助系统支付成功
					//根据订单号查询用户id
					ApiCapitalinout capitalinout = new ApiCapitalinout();
					capitalinout.setTranNum(tradeNo);
					capitalinout= donateRecordFacade.queryCapitalinoutByParam(capitalinout);
					logger.info("homeUserId>>>>>>"+capitalinout.getUserId());
					logger.info("money>>>>>>"+capitalinout.getMoney());
					String url = huzhu_order_state_url_balance+"/"+capitalinout.getUserId()+"/"+tradeNo+"/"+capitalinout.getMoney()+"/1";
					HttpConnect connect = new HttpConnect();
					HttpResponse httpResponse = connect.doPostStr(url);
					logger.info("调取end");
				}else if("flt_donate".equals(attach)){logger.info("ep>>>flt_donate");
					String url = "http://admin.flt.17xs.org/tenpay/asyncresult.do?OrderId=T"+OrderId;
					HttpConnect connect = new HttpConnect();
					HttpResponse httpResponse = connect.doGetStr(url);
					logger.info("调取end");
				}else if(attach.contains("together_")){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					String[] ssStrings = attach.split("_");
					//回调将捐赠次数 金额累加到togetherConfig
					ApiDonateRecord d = donateRecordFacade.queryPayNoticeByTranNum(tradeNo);
					logger.info("tradeNo>>>>"+tradeNo);
					if(d!=null && d.getProjectId()!=null && d.getUserId()!=null){
						ApiProject project = projectFacade.queryProjectDetail(d.getProjectId());
						logger.info("projectId>>>>"+d.getProjectId());
						ApiTogetherConfig apiTogetherConfig = new ApiTogetherConfig();
						apiTogetherConfig.setProjectId(d.getProjectId());
						if(ssStrings.length==2 && ssStrings[1]!="0"){
							apiTogetherConfig.setUserId(Integer.valueOf(ssStrings[1]));
						}else{
							apiTogetherConfig.setUserId(d.getUserId());
						}
						apiTogetherConfig = projectFacade.queryByTogetherConfig(apiTogetherConfig);
						BigDecimal money = new BigDecimal(d.getDonatAmount());
						if(apiTogetherConfig!=null){
							apiTogetherConfig.setDonateMoney(apiTogetherConfig.getDonateMoney().add(money));
							apiTogetherConfig.setDonateNum(apiTogetherConfig.getDonateNum()+1);
							projectFacade.updateTogetherConfig(apiTogetherConfig);
						}
						//微信推送消息，一起捐
						if(ssStrings.length==2 && ssStrings[1]!="0"){
							ApiFrontUser user = userFacade.queryById(d.getUserId());
		    				ApiThirdUser thirdUser = new ApiThirdUser();
		    				thirdUser.setUserId(apiTogetherConfig.getUserId());
		    				thirdUser = userFacade.queryThirdUserByParam(thirdUser);
							ApiWeixinModel weiXinModel = new ApiWeixinModel();
	    	    		 	weiXinModel.setModel(8);
	    		           	weiXinModel.setState(0);
	    		           	weiXinModel.setUserId(Integer.valueOf(ssStrings[1]));
	    		           	weiXinModel.setOpenId(thirdUser.getAccountNum());
	    		           	weiXinModel.setProjectId(d.getProjectId());
	    		           	weiXinModel.setCreateTime(new Date());
	    		           	DecimalFormat df = new DecimalFormat("0.00");
	    		           	weiXinModel.setValue1(apiTogetherConfig.getLaunchName() +"您好，"+ user.getNickName() +"为您的一起捐项目（"+ project.getTitle() +"）捐赠"+ df.format(d.getDonatAmount()) +"元。感谢您的爱心传播。");
	    		           	weiXinModel.setValue2(apiTogetherConfig.getDonateNum()+"");
	    		           	weiXinModel.setValue3(df.format(apiTogetherConfig.getDonateMoney()));
	    		           	weiXinModel.setValue4(project.getDonationNum()+"");
	    		           	weiXinModel.setValue5(df.format(project.getDonatAmount()));
	    		           	weiXinModel.setValue6("感谢您的爱心！");
	    	    			userFacade.saveWeixinModel(weiXinModel);
						}
					}

				}/*else if(attach.contains("yunhu_")){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					String[] ssStrings = attach.split("_");
					//返回 yunhu_userId 捐赠人     头像  昵称  捐赠金额  捐赠的项目ID 捐赠时间
					ApiDonateRecord d = donateRecordFacade.queryPayNoticeByTranNum(tradeNo);
					logger.info("tradeNo>>>>"+tradeNo);
					if(d!=null && d.getProjectId()!=null && d.getUserId()!=null){
						//查询捐赠，返回云湖处理 yunhu_url
						//http://yhbk.tw0577.com/native_notify.php?headImg=HeadImg&nickName=NickName&amount=Amount&projectId=ProjectId&donateTime=DonateTime&yunhu_userId=Yunhu_userId
						if(ssStrings.length==2 && ssStrings[1]!="0"){//云湖的用户ID：ssStrings[1]
							yunhu_url.replace("Yunhu_userId", ssStrings[1]);
						}else{
							yunhu_url.replace("Yunhu_userId", "0");
						}
						//头像
						if(d.getCoverImageurl()!=null && !"".equals(d.getCoverImageurl())){
							yunhu_url.replace("HeadImg", d.getCoverImageurl());
						}
						//昵称
						if(d.getNickName()!=null && !"".equals(d.getNickName())){
							yunhu_url.replace("NickName", d.getNickName());
						}
						//金额
						if(d.getDonatAmount()!=null){
							yunhu_url.replace("Amount", d.getDonatAmount()+"");
						}
						//项目id
						if(d.getProjectId()!=null){
							yunhu_url.replace("ProjectId", d.getProjectId()+"");
						}
						//时间
						if(d.getDonatTime()!=null){
							yunhu_url.replace("DonateTime", d.getDonatTime()+"");
						}
						logger.info("yunhui>>>>回调>>>>>url>>>>"+yunhu_url);
						HttpConnect connect = new HttpConnect();
						HttpResponse httpResponse = connect.doPostStr(yunhu_url);
						logger.info("调取end");
					}
				}*/else if(attach.contains("oneAid_")){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					ApiOneAid model = new ApiOneAid();
					model.setOrder_num(tradeNo);
					ApiPage<ApiOneAid> page = projectFacade.queryOneAidByParam(model, 1, 20);
					if(page!=null && page.getTotal() > 0){
						for (ApiOneAid oneAid : page.getResultData()) {
							oneAid.setState(201);
						}
						projectFacade.updateOneAids(page.getResultData());
						logger.info("微信》》》》》》》》》更新一对一帮扶成功");
					}
				}
				else if("export".equals(attach)){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					//TODO 项目id  项目名称   捐款金额   捐款时间    捐款人昵称    留言    头像   订单号   捐款类型 （Null）  捐款类型名称(weixin)    手机号 (outDonor)   平台id（固定）
					ApiDonateRecord d = donateRecordFacade.queryDonateInfoByTradeNo(tradeNo);
					StringBuffer buff = new StringBuffer();
					ApiProject pp = projectFacade.queryProjectDetail(d.getProjectId());
					buff.append("?CharityId="+pp.getUserId());
					buff.append("&Certificate="+tradeNo);
					buff.append("&ProjectId="+d.getProjectId());
					buff.append("&ProjectName="+URLEncoder.encode(d.getProjectTitle(),"utf-8"));
					buff.append("&DonateName="+URLEncoder.encode(d.getNickName(),"utf-8"));
					buff.append("&Amount="+d.getDonatAmount());
					buff.append("&DonateTime="+URLEncoder.encode(DateUtil.dateString2(d.getDonatTime()),"utf-8"));
					buff.append("&BankId=");
					buff.append("&BankName=weixin");
					/*buff.append("&DonatePhone="+d.getOutDonor());*/
					buff.append("&Remark="+URLEncoder.encode(d.getLeaveWord(),"utf-8"));
					buff.append("&DonateHeadPic="+d.getCoverImageurl());
					StringBuffer url = new StringBuffer();
					url.append("http://gyb.wedream365.cn/redcs/api/gybapi/nbnotify");
					url.append(buff);
					logger.info("export   url  >>>>>>>>>>>"+url);
					HttpConnect connect = new HttpConnect();
					HttpResponse httpResponse = connect.doPostStr(url.toString());
					logger.info("EXPORT调取end");
				}else if(attach.contains("commonForm")){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					//更新报名信息
					try {
						ApiCommonFormUser commonFormUser = new ApiCommonFormUser();
						commonFormUser.setId(Integer.valueOf(attach.split("_")[1]));
						commonFormUser.setRemark(tradeNo);
						projectVolunteerFacade.update(commonFormUser);
						//增加报名人数
						ApiCommonForm apiCommonForm = new ApiCommonForm();
						apiCommonForm.setId(Integer.valueOf(attach.split("_")[2]));
						apiCommonForm =projectVolunteerFacade.selectByParam(apiCommonForm);
						apiCommonForm.setNumber(apiCommonForm.getNumber()+1);
						projectVolunteerFacade.update(apiCommonForm);
					} catch (Exception e) {
						logger.info("tenpay >> asyncresult >> error >>"+e.getMessage());
					}
				}else if(attach.contains("bazaar")){logger.info("ep>>>"+attach);
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
					//更新礼品购买人数 bazaar_123@1_124@1
					try {
						String[] bazaars = attach.split("_");
						for (int i = 1; i < bazaars.length; i++) {
							String[] gifts = bazaars[i].split("@");
							ApiGift apiGift = userRelationInfoFacade.queryGiftById(Integer.valueOf(gifts[0]));
							userRelationInfoFacade.updateGift(Integer.valueOf(gifts[0]), apiGift.getBuyNumber()+Integer.valueOf(gifts[1]));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "","");
				}
				SendModel sd = new SendModel();
				// 微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
				String accessToken = (String) redisService.queryObjectData("AccessToken");
				if (accessToken == null) {
					accessToken = TenpayUtil.queryAccessToken();
					redisService.saveObjectData("AccessToken", accessToken,
							DateUtil.DURATION_HOUR_S);
					redisService.saveObjectData("JsapiTicket",TenpayUtil.queryJsapiTicket(accessToken),
							DateUtil.DURATION_HOUR_S);
				}
				ApiDonateRecord record = donateRecordFacade.queryWeixinToPayNoticeByTranNum(tradeNo);
				if(record != null){
					ApiMatchDonate md = new ApiMatchDonate();
					md.setRecordID(record.getId());
					md = redPacketsFacade.queryMatchDonateByParam(md);
					String returnUrl = "http://www.17xs.org/project/view_h5.do?projectId="+record.getProjectId()+"&itemType=2";
					if(md !=null && md.getCapitalinoutid() !=null && md.getCapitalinoutid() > 0){
						//配捐
						sd.Donation_Success_Notice(accessToken, null, returnUrl, record,1,md);
					}else {
						sd.Donation_Success_Notice(accessToken, null, returnUrl, record);
					}
				}
			}
	    	response.getWriter().print("SUCCESS");
	    }
	}

	public static void main(String[] args) {
		/*String url = "http://admin.flt.17xs.org/tenpay/asyncresult.do?OrderId=T149493011970573";
		HttpConnect connect = new HttpConnect();
		HttpResponse httpResponse = connect.doGetStr(url);*/
		StringBuffer buff = new StringBuffer();
		Date date = new Date();
		buff.append("?CharityId="+200608);
		buff.append("&Certificate=123456");
		buff.append("&ProjectId=1");
		buff.append("&ProjectName=测试");
		buff.append("&DonateName=捐赠人名称");
		buff.append("&Amount=100");
		buff.append("&DonateTime="+date.getTime());
		buff.append("&BankId=");
		buff.append("&BankName=weixin");
		buff.append("&DonatePhone=13260541677");
		buff.append("&Remark=留言");
		buff.append("&DonateHeadPic=http://wx.qlogo.cn/mmopen/yBFNCdWrtcHf65iaiaC3iaKEcsRkoVDGgTFlhUGkf07XWsU7rZl952HI6dg1NAA5SzUwoB13v9icQpDma0ahT2iatlZvbc2eEqbxm/0");
		StringBuffer url = new StringBuffer();
		url.append("http://gyb.wedream365.cn/redcs/api/gybapi/nbnotify");
		url.append(buff);
		System.out.println("url="+url);
		HttpConnect connect = new HttpConnect();
		HttpResponse httpResponse = connect.doPostStr(url.toString());
		System.out.println("end");
	}
	/*@RequestMapping(value = "aa")
	public String dd(){
		String accessToken ="iR4590yDKjxOFiJDEBFsYTeg4GWxYMW5AojGywWn-fwFFS1KzzpOremyn8byRcsGYCu7HmDgC9iHVXuKxeVLefaChXXmsfBrIMHBCE7fVTxS85xCkfZevtzKiqTVBqZXCYVhAFAWSP";
		ApiDonateRecord record = new ApiDonateRecord();
		record.setNickName("小李");
		record.setProjectTitle("为夫妇二人生命接力，救救这个快要坍塌的家庭！");
		record.setDonatAmount(12.0);
		record.setOpenId("oxmc3uBBzySbantAofWrdLQgYzq0");
		ApiMatchDonate md = new ApiMatchDonate();
		md.setRecordID(3426);
		md = redPacketsFacade.queryMatchDonateByParam(md);
		String returnUrl = "http://www.17xs.org/project/view_h5.do?projectId="+record.getProjectId()+"&itemType=2";
		SendModel sd = new SendModel();
		sd.Donation_Success_Notice(accessToken, null, returnUrl, record,1,md);
		return returnUrl;
	}*/

	@ResponseBody
	@RequestMapping(value = "asyncresult_flt")
	public void asyncresult_flt(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response){
		String url = "http://admin.flt.17xs.org/tenpay/asyncresult.do?OrderId="+OrderId;
		HttpConnect connect = new HttpConnect();
		HttpResponse httpResponse = connect.doGetStr(url);
		logger.info("调取end");
	}


	/**
	 * new pc端捐款
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/depositPublic", produces = "application/json")
	public ModelAndView depositPublic(DepositForm from, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView("redirect:/tenpay/pay.do");
		int payType = from.getPayType();
		Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
		ApiFrontUser user = userFacade.queryById(userId);
		String total_fee = String.valueOf(from.getAmount());
		//扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(StringUtil.uniqueCode());
	    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
	    tpWxPay1.setTotalFee(total_fee);

		ApiDonateRecord dRecord = new ApiDonateRecord();
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		//查询支付配置
		ApiPayConfig config = new ApiPayConfig();
		config.setUserId(project.getUserId());
		List<ApiPayConfig> list = projectFacade.queryByParam(config);
		if(list.size()==1){
			dRecord.setPayConfigId(list.get(0).getId());
			dRecord.setPartnerName(list.get(0).getName());
		}
		tpWxPay1.setBody("认捐“"+project.getTitle()+"”项目");
		dRecord.setUserId(userId);
		dRecord.setProjectId(project.getId());
		dRecord.setProjectTitle(project.getTitle());
		dRecord.setMonthDonatId(123);
		dRecord.setDonorType(user.getUserType());
		dRecord.setDonatType(user.getUserType());
		dRecord.setDonatAmount(from.getSumMoney());
		dRecord.setDonateCopies(from.getCopies());
		dRecord.setTranNum(tpWxPay1.getOrderId());
		dRecord.setSource("PC");
		dRecord.setCompanyId(user.getCompanyId());
		if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
		if(payType == 1){
			if (StringUtils.isNotEmpty(from.getDonateName())
					|| StringUtils.isEmpty(from.getDonateWord())) {
				if (from.getDonateName().length() > 20
						|| from.getDonateWord().length() > 20) {
					view = new ModelAndView("/deposit/pay");
					view.addObject("error", "请控制在20个字内");
					return view;
				}
			}
			dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
		}

		ApiResult rt = null;
//		if("enterpriseUsers".equals(user.getUserType())){
			if(from.getAmount() == 0){
				rt = donateRecordFacade.buyDonate(dRecord, null,"freezType",null,"");
				view = new ModelAndView("redirect:/ucenter/core/mygood.do");
				return view;
			}
			ApiCapitalinout capitalinout = new ApiCapitalinout();
			if(list.size()==1){
				capitalinout.setPayConfigId(list.get(0).getId());
				capitalinout.setPartnerName(list.get(0).getName());
			}
			//4 ：表示企业用户
			tpWxPay1.setAttach("4");
			capitalinout.setTranNum(tpWxPay1.getOrderId());
			capitalinout.setSource("PC");
			capitalinout.setMoney(from.getAmount());
			capitalinout.setPayType("tenpay");
			capitalinout.setInType(1);
			capitalinout.setUserId(userId);
			capitalinout.setBankType(from.getBank());
			logger.info("updateDonate receive param>>" + dRecord);
			logger.info("addCapitalinout receive param>>" + capitalinout);
			rt = donateRecordFacade.buyDonate(dRecord, capitalinout,"freezType",null,"");
//		}else{
//			//6：个人用户
//			tpWxPay1.setAttach("6");
//			rt = donateRecordFacade.buyDonate(dRecord, null,"tenpay");
//		}
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				//生成二维码
			    String codeurl;
			    if(list.size()==1){
			    	codeurl = Demo.getCodeurlPublic(tpWxPay1, list.get(0));
			    }
			    else{
			    	codeurl = Demo.getCodeurl(tpWxPay1);
			    }
			    logger.info(codeurl);
			    Date date = new Date();
			    view.addObject("pName", project.getTitle());
			    view.addObject("amount", from.getAmount());
			    view.addObject("tradeNo", tpWxPay1.getOrderId());
			    view.addObject("codeurl", codeurl);
			    view.addObject("projectId", from.getProjectId());
			    view.addObject("date", date.getTime());
				return view;
			} catch (Exception e) {
				logger.error("二维码异常", e);
				view.addObject("error", "支付出现异常，请联系客服!");
				System.out.println("支付出现异常，请联系客服");
				return view;
			}
		} else {
			logger.info("rt is null");
			view.addObject("error", "支付出现异常，请联系客服!!!");
			System.out.println("支付出现异常，请联系客服22");
			return view;
		}
	}

}
