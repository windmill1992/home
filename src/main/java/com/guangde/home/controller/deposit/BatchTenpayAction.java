package com.guangde.home.controller.deposit;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.zxing.WriterException;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiAnnounce;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiDonateTime;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiRedpackets;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.Demo;
import com.tenpay.demo.QrCode;
import com.tenpay.demo.WxPayDto;
import com.tenpay.demo.Wx_Send_Model.SendModel;
import com.tenpay.utils.TenpayUtil;

/**
 * PC微信支付成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("batchpay")
public class BatchTenpayAction extends DepositBaseAction {

	// 服务器异步通知页面路径
	public static final String notify_url = "http://www.17xs.org/batchpay/tenpay/async.do";
	// 页面跳转同步通知页面路径
	public static final String return_url = "http://www.17xs.org/batchpay/tenpay/return_url.do";
	//二维码的底版
	public static final String imgPath = "/res/images/charge/wx-pay-code.jpg"; 
	//二维ima的图标
	public static final String srcPath = "/res/images/charge/logo-pay.png"; 
	
	Logger logger = LoggerFactory.getLogger(BatchTenpayAction.class);
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
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private ICommonFacade commonFacade;
	
	@RequestMapping(value = "tenpay/deposit", produces = "application/json")
	public ModelAndView deposit(DepositForm from, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView("redirect:/batchpay/tenpay/pay.do");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = null;
        if(userId !=null){
        	user = userFacade.queryById(userId);
        }else {
        	//判段这个IP是否已经注册过
            String adressIp = SSOUtil.getUserIP(request);
            user = new ApiFrontUser();
            user.setRegisterIP(adressIp);
            user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
            user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
            user = userFacade.queryUserByParam(user);
            //根据IP创建用户
            if (user != null)
            {
                userId = user.getId();
            }
            else
            {
                user = new ApiFrontUser();
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
                user.setUserName(PengPengConstants.VISITOR + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
                user.setUserPass(StringUtil.randonNums(8));
                user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
                user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
                user.setRegisterIP(adressIp);
                ApiResult apr = userFacade.registered(user);
                if (apr == null || apr.getCode() != 1)
                {
                    view.addObject("alipay", "没有这个项目");
                    return view;
                }
                user = new ApiFrontUser();
                user.setRegisterIP(adressIp);
                user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
                user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
                user = userFacade.queryUserByParam(user);
                userId = user.getId();
            }
            
        	try
    		{
    			// 自动登录
    			SSOUtil.login(user, request, response);
    		}
    		catch(Exception e)
    		{
    			logger.error("deposit >> SSOUtil.login : "+e);
    		}
		}
		String total_fee = String.valueOf(from.getAmount());
		//扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(StringUtil.uniqueCode());
	    tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
	    tpWxPay1.setTotalFee(total_fee);
		
		ApiDonateRecord dRecord = new ApiDonateRecord();
		 //需要捐款的项目    
        List<Integer> pList = from.getpList();
        //不需要捐款的项目    
        List<Integer> cList = from.getcList();
        
		for (int i = 0; i < cList.size(); i++) {
			for (int j = 0; j < pList.size(); j++) {
				if(cList.get(i).equals(pList.get(j))){
					pList.remove(j);
				}
			}
		}
        	
        if(pList.size() > 0){
        	String p ="";
        	for (int i = 0; i < pList.size(); i++) {
            	p = p + String.valueOf(pList.get(i))+",";
    		}
        	dRecord.setLeaveWord(p);
        }else {
        	view.addObject("没有项目选择");
        	 return view;
		}
        
		tpWxPay1.setBody("批量捐款项目分别为"+dRecord.getLeaveWord());
		dRecord.setUserId(userId);
		dRecord.setProjectId(285);//采用默认的项目id
		dRecord.setProjectTitle("批量捐款");
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
		
		ApiResult rt = null;
		if("enterpriseUsers".equals(user.getUserType())){
			if(from.getAmount() == 0){
				rt = donateRecordFacade.batchBuyDonate(dRecord, null,"freezType");
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
			rt = donateRecordFacade.batchBuyDonate(dRecord, capitalinout,"freezType");
		}else{
			//6：个人用户
			tpWxPay1.setAttach("6");
			rt = donateRecordFacade.batchBuyDonate(dRecord, null,"tenpay");
		}
		// 成功:1
		if (rt != null && rt.getCode() == 1) {
			// 建立请求
			try {
				//生成二维码
			    String codeurl = Demo.getCodeurl(tpWxPay1);
			    Date date = new Date();
			    view.addObject("pName", "批量捐款");
			    view.addObject("amount", from.getAmount());
			    view.addObject("tradeNo", tpWxPay1.getOrderId());
			    view.addObject("codeurl", codeurl);
			    view.addObject("projectId", from.getProjectId());
			    view.addObject("date", date.getTime());
				return view;
			} catch (Exception e) {
				view.addObject("error", "支付出现异常，请联系客服。");
				return view;
			}
		} else {
			view.addObject("error", "支付出现异常，请联系客服。");
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
	@RequestMapping(value = "tenpay/pay")
	public ModelAndView commondpay(DepositForm from, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView("deposit/weixin");
		String field = "";
		ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
		if(project != null){
			field = project.getField();
		}
		view.addObject("field", "batch");
		view.addObject("title", from.getpName());
	    view.addObject("amount", from.getAmount());
	    view.addObject("tradeNo", from.getTradeNo());
	    SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    view.addObject("date", sdf.format(from.getDate()));
	    view.addObject("codeurl", from.getCodeurl());
	    view.addObject("projectId", from.getProjectId());
	    view.addObject("payType", from.getPayType());
	    view.addObject("nickName", from.getNickName());
	    view.addObject("list", 1);
	    
		return view;
	}
	
	/**
	 * 异步通知付款状态的Controller
	 * 
	 */
	@RequestMapping(value = "tenpay/async")
	public String async(HttpServletRequest request, HttpServletResponse response) {
		String tradeNo = request.getParameter("out_trade_no");
		String tradeStatus = request.getParameter("result_code");
		String notifyId = request.getParameter("transaction_id");
		String ep = request.getParameter("attach");
			if (StringUtils.isNotEmpty(tradeStatus) && tradeStatus.equals("SUCCESS")) {
				if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
					if("4".equals(ep)){
						donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","freezType");
					}else if("5".equals(ep)) {
						donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","alipay");
					}else{
						donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","");
					}
				}else {
					logger.debug("对交易号"+tradeNo+"：：支付宝ID"+notifyId+"重复回调的屏蔽");
				}
				// 要写的逻辑。自己按自己的要求写
				System.out.println(">>>>>充值成功 微信" + tradeNo);
			}
			return "/deposit/success";

	}
	@ResponseBody
	@RequestMapping(value = "tenpay/result")
	public Map<String, Object> getPayResult(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response){
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	Integer pId = donateRecordFacade.queryProjectIdByCapitalinout(OrderId);
	    	Integer userId = UserUtil.getUserId(request, response);
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else{
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","");
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
	@RequestMapping(value = "tenpay/h5result")
	public ModelAndView getPayH5Result(@RequestParam(value = "OrderId", required = true) String OrderId,
			@RequestParam(value = "projectId", required = true) String projectId,
			HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView("redirect:/project/view_h5.do?projectId="+projectId);
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else{
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","");
				}
			}
	    }
	    return view;
	}
	
	@ResponseBody
	@RequestMapping(value = "tenpay/asyncresult")
	public void getPayAsyncResult(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
				if("4".equals(attach)){
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","freezType");
				}else if("5".equals(attach)) {
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","tenpay");
				}else{
					donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "","");
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
				ApiDonateRecord record = donateRecordFacade
						.queryWeixinToPayNoticeByTranNum(tradeNo);
				if(record != null){
					ApiMatchDonate md = new ApiMatchDonate();
					md.setRecordID(record.getId());
					md = redPacketsFacade.queryMatchDonateByParam(md);
					String returnUrl = "http://www.17xs.org/ucenter/myDonate.do";
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

	@ResponseBody
	@RequestMapping(value = "tenpay/asyncresult2")
	public void getPayAsyncResult2(@RequestParam(value = "OrderId", required = true) String OrderId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
	    		
	    		companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
	    		
	    		if(msg[3].contains("dayDonate")){logger.info("attach>>>"+msg[3]);
	    			String attach[] = msg[3].split("_");
	    			Integer userId = Integer.valueOf(attach[1]);
	    			Integer projectId = Integer.valueOf(Integer.valueOf(attach[2]));
	    			//TODO 日捐出一笔 发送短信
	    			ApiResult res = new ApiResult();
	    	    	ApiFrontUser user = userFacade.queryById(userId);
	    	    	ApiProject project = projectFacade.queryProjectDetail(projectId);
	    	    	//查询用户的日捐
	    	    	ApiDonateTime param = new ApiDonateTime();
	    	    	param.setUserId(userId);
	    	    	param.setProjectIds(projectId.toString());
	    	    	param.setState(200);//草稿
	    	    	ApiPage<ApiDonateTime> page = donateRecordFacade.queryDonateTimeByParam(param, 1, 10);
	    	    	if(page!=null && page.getTotal()>0){
	    	    		//日捐金额
	    	    		Double money = page.getResultData().get(0).getMoney();
	    	    		//用户余额
	    	    		Double balance = user.getBalance();
	    	    		Double availableBalance = user.getAvailableBalance();
	    	    		balance=MathUtil.sub(balance, money);
	    	    		availableBalance=MathUtil.sub(availableBalance, money);
	    	    		
	    	    		ApiCapitalinout cp = new ApiCapitalinout();
	    	    		cp.setUserId(user.getId());
	    				cp.setTranNum(StringUtil.uniqueCode());
	    				cp.setPayState(302);
	    				cp.setType(0);
	    				cp.setInType(0);
	    				cp.setSource("H5");
	    				cp.setBalance(balance);
	    				cp.setPayType("freezType");// 余额支付
	    				cp.setPayNum("日捐发起扣款");
	    				cp.setMoney(money);
	    				cp.setCreateTime(new Date());
	    				//TODO ca save
	    				
	    				
	    				ApiDonateRecord dr = new ApiDonateRecord();
	    				dr.setCapitalinoutId(cp.getId());
	    				dr.setProjectId(project.getId());
	    				dr.setUserId(user.getId());
	    				dr.setDonorType("InternalPers");
	    				dr.setDonatAmount(money);
	    				dr.setState(302);
	    				dr.setDonateCopies(0);
	    				dr.setDonatTime(DateUtil.getCurrentTimeByDate());
	    				dr.setDonatType("daylyDonation");
	    				
	    				dr.setMonthDonatId(page.getResultData().get(0).getId());
	    				//TODO donate save
	    				
	    				
	    				//更新项目
	    				project.setDonatAmount(MathUtil.add(project.getDonatAmount()==null?0.00:project.getDonatAmount(),money));
	    				project.setDonationNum(project.getDonationNum()==null?0:project.getDonationNum() + 1);
	    				if(project.getDonatAmount() >= project.getCryMoney()){
	    					project.setState(260);
	    				}
	    				//TODO project update
	    				
	    				
	    				// 更新用户捐款总额
	    				user.setBalance(MathUtil.sub(user.getBalance(), money));
	    				user.setAvailableBalance(MathUtil.sub(user.getAvailableBalance(), money));
	    				user.setTotalAmount(MathUtil.add(user.getTotalAmount(), money));
	    				//TODO user update
	    				
	    				//更新捐款次数
	    				page.getResultData().get(0).setNumber(page.getResultData().get(0).getNumber() + 1);
	    				
	    				res = donateRecordFacade.addDayDonate(cp,dr,project,user);
	    				logger.info("result>>>>>>>>"+res.getCode());
	    				if(res.getCode()==1){
	    					//发起月捐成功短信通知
	    					ApiAnnounce apiAnnounce = new ApiAnnounce();
	    					apiAnnounce.setCause("日捐通知");
	    					apiAnnounce.setContent(user.getNickName()+"，您的日捐已开通，每日捐赠"+page.getResultData().get(0).getMoney()+"元，持续"+page.getResultData().get(0).getDayNumber()+"天，感谢您的善举");
	    			    	apiAnnounce.setDestination(page.getResultData().get(0).getMobileNum());
	    			    	apiAnnounce.setType(1);
	    			    	apiAnnounce.setPriority(1);
	    			    	apiAnnounce.setDestination(page.getResultData().get(0).getMobileNum());
	    			    	commonFacade.sendSms(apiAnnounce, false);
	    				}
	    				page.getResultData().get(0).setDayNumber(page.getResultData().get(0).getDayNumber()-1);
	    				page.getResultData().get(0).setState(201);
	    				donateRecordFacade.updateDonateTime(page.getResultData().get(0));
	    	    	}
	    		}
	    		
				// 微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
				String accessToken = (String) redisService.queryObjectData("AccessToken");
				
				if (accessToken == null) {
					
					accessToken = TenpayUtil.queryAccessToken();
					redisService.saveObjectData("AccessToken", accessToken,
							DateUtil.DURATION_HOUR_S);
					redisService.saveObjectData("JsapiTicket",TenpayUtil.queryJsapiTicket(accessToken),
							DateUtil.DURATION_HOUR_S);
				}
				
			}
	    	response.getWriter().print("SUCCESS");
	    }
	}
	
	@ResponseBody
	@RequestMapping(value = "tenpay/asyncresult3")
	public void getPayAsyncResult3(@RequestParam(value = "OrderId", required = true) String OrderId,
			@RequestParam(value = "userId", required = true) Integer userId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setOrderId(OrderId);
	    String[] msg = Demo.getPayResult(tpWxPay1).split("\\|");
	    
	    if("SUCCESS".equals(msg[0]) && !"null".equals(msg[2])){
	    	
	    	String tradeNo = msg[1];
	    	String notifyId = msg[2];
	    	String attach=msg[3];
	    	
	    	logger.error("BatchTenpayAction tenpay/asyncresult2 >> tradeNo = "+tradeNo + " , notifyId = "+notifyId +" ,attach = "+attach );
	    	
	    	if(companyFacade.checkAlipayResult(tradeNo, notifyId)){
	    		
	    		ApiResult res=companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
	    		// 充值成功后对红包做处理（生成红包） to do 
	    		
	    		try{
	    			logger.error(" res code = "+res.getCode());
	    			if(res.getCode() == 1)
	    			{
	    				int redpacketsId = Integer.parseInt(attach);
	    				ApiRedpackets redpackets = new ApiRedpackets();
	    				redpackets.setId(redpacketsId);
	    				redpackets.setStatus(203);
	    				if(userId == null)
	    				{
	    					userId = UserUtil.getUserId(request, response);
	    				}
	    				logger.error(" asyncresult2 >> userId = "+userId);
	    				if(userId != null)
	    				{
	    					redpackets.setUserid(userId);
	    				}
	    				ApiResult r=redPacketsFacade.updateRedpacket(redpackets);
	    				
	    				logger.error("updateRedpacket result >> "+r.getCode());
	    			}
	    		}
	    		catch(Exception e)
	    		{
	    			logger.error(" BatchTenpayAction tenpay/asyncresult2  updateRedpacket >> "+e.getMessage());
	    		}
	    		
				
			}
	    	response.getWriter().print("SUCCESS");
	    }
	}
	
}
