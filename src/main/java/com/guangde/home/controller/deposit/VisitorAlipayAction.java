package com.guangde.home.controller.deposit;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.*;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.*;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;
import com.tenpay.demo.Demo;
import com.tenpay.demo.WxPayDto;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于游客匿名的捐赠
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("visitorAlipay")
public class VisitorAlipayAction extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public static final String notify_url = "http://www.17xs.org/visitorAlipay/async.do";
    
    // 页面跳转同步通知页面路径
    public static final String return_url = "http://www.17xs.org/visitorAlipay/return_url.do";
    
    Logger logger = LoggerFactory.getLogger(VisitorAlipayAction.class);
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    @RequestMapping(value = "/deposit", produces = "application/json")
    public ModelAndView deposit(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/alipay");
        int payType = from.getPayType();//支付错误返回方式
        String bank = from.getBank();
        //判段这个IP是否已经注册过
        String adressIp = SSOUtil.getUserIP(request);
        ApiFrontUser user = new ApiFrontUser();
        user.setRegisterIP(adressIp);
        user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
        user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
        user = userFacade.queryUserByParam(user);
        //根据IP创建用户
        Integer userId = null;
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
        
        String total_fee = String.valueOf(from.getAmount());
        Map<String, String> sParaTemp = alipayMap(request, response);
        sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
        sParaTemp.put("total_fee", total_fee);// 价格
        if (StringUtils.isNotEmpty(bank))
        {
            sParaTemp.put("paymethod", "bankPay");
            sParaTemp.put("defaultbank", bank);
        }
        ApiDonateRecord dRecord = new ApiDonateRecord();
        ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
        if (project == null)
        {
            view.addObject("error", "支付出现异常，请联系客服。");
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
        //查询支付配置
      	ApiPayConfig config = new ApiPayConfig();
      	config.setUserId(project.getUserId());
      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
      	if(list.size()>0){
      		dRecord.setPayConfigId(list.get(0).getId());
      	}
        if(request.getSession().getAttribute("extensionPeople")!=null&&from.getExtensionPeople()==null){
        	dRecord.setExtensionPeople(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
        else{
        if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }}
        //新增游客信息
        if (!StringUtils.isEmpty(from.getTouristName()) || !StringUtils.isEmpty(from.getTouristMobile()))
        {
            
        	String touristMessage = "{\"name\":\"" + from.getTouristName().replaceAll("\"", "\'") + "\",\"mobile\":\"" + from.getTouristMobile().replaceAll("\"", "\'") + "\"}";
            dRecord.setTouristMessage(touristMessage);
        }
        
        if (payType == 1)
        {
            if (StringUtils.isNotEmpty(from.getDonateName()) || StringUtils.isEmpty(from.getDonateWord()))
            {
                if (from.getDonateName().length() > 20 || from.getDonateWord().length() > 20)
                {
                    view = new ModelAndView("/deposit/pay");
                    view.addObject("error", "请控制在20个字内");
                    return view;
                }
            }
            dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
        }
        ApiResult rt = null;
        //6：个人用户
        sParaTemp.put("extra_common_param", payType+"");
        /*
        String nickName = user.getNickName()== null?"":user.getNickName();
        String tranNum = dRecord.getTranNum()== null?"":dRecord.getTranNum();
        String pName = from.getpName()== null?"":from.getpName();
        Double donateAmount = from.getSumMoney();
        
        sParaTemp.put("extra_common_param", String.valueOf(from.getProjectId())+"_"+payType+"_"+nickName+"_"+tranNum+"_"+pName+"_"+donateAmount);
        */
        ApiCapitalinout apiCapitalinout = new ApiCapitalinout();
        if(list.size()>0){
        	apiCapitalinout.setPayConfigId(list.get(0).getId());
        }
        if (StringUtils.isNotEmpty(bank))
        {
            dRecord.setBankType(BankConstants.BANK_NAME.get(bank));
            rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout, "bankpay",null,"");
        }
        else
        {
            rt = donateRecordFacade.buyDonate(dRecord, apiCapitalinout, "alipay",null,"");
        }
        
        // 成功:1
        if (rt != null && rt.getCode() == 1)
        {
            // 建立请求
            try
            {
                String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
                view.addObject("alipay", sHtmlText);
                return view;
            }
            catch (Exception e)
            {
                view = getview(payType);
                view.addObject("error", "支付出现异常，请联系客服。");
                return view;
            }
        }
        else if (rt.getCode() == 90001)
        {
            view = getview(payType);
            view.addObject("tokenCode", from.getTokenCode());
            view.addObject("projectId", from.getProjectId());
            view.addObject("pName", project.getTitle());
            view.addObject("copies", from.getCopies());
            view.addObject("amount", from.getAmount());
            view.addObject("error", rt.getMessage());
            return view;
        }
        else
        {
            view = getview(payType);
            view.addObject("error", "支付出现异常，请联系客服。");
            return view;
        }
    }
    
    @RequestMapping(value = "/weixin/deposit")
    public ModelAndView weixindeposit(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("redirect:/tenpay/pay.do");
        //判段这个IP是否已经注册过
        String adressIp = SSOUtil.getUserIP(request);
        ApiFrontUser user = new ApiFrontUser();
        user.setRegisterIP(adressIp);
        user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
        user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
        user = userFacade.queryUserByParam(user);
        //根据IP创建用户
        Integer userId = null;
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
            logger.debug("游客募捐归类需要的参数：adressIp:" + adressIp);
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
        
        String total_fee = String.valueOf(from.getAmount());
        //扫码支付
        WxPayDto tpWxPay1 = new WxPayDto();
        tpWxPay1.setOrderId(StringUtil.uniqueCode());
        tpWxPay1.setSpbillCreateIp(SSOUtil.getUserIP(request));
        tpWxPay1.setTotalFee(total_fee);
        
        ApiDonateRecord dRecord = new ApiDonateRecord();
        ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
        tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        dRecord.setProjectTitle(project.getTitle());
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(Double.valueOf(total_fee));
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(tpWxPay1.getOrderId());
        dRecord.setSource("PC");
        dRecord.setCompanyId(user.getCompanyId());
        if(request.getSession().getAttribute("extensionPeople")!=null&&from.getExtensionPeople()==null){
        	dRecord.setExtensionPeople(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
        else{
        if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }}
        if(from.getDonateWord() !=null){
        	dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
        }
        //新增游客信息
        if (!StringUtils.isEmpty(from.getTouristName()) || !StringUtils.isEmpty(from.getTouristMobile()))
        {
            
            String touristMessage = "{\"name\":\"" + from.getTouristName().replaceAll("\"", "\'") + "\",\"mobile\":\"" + from.getTouristMobile().replaceAll("\"", "\'") + "\"}";
            dRecord.setTouristMessage(touristMessage);
        }
        
        //6：个人用户
        tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
        ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "tenpay",null,"");
        
        // 成功:1
        if (rt != null && rt.getCode() == 1)
        {
            // 建立请求
            try
            {
                //生成二维码
               	String codeurl = Demo.getCodeurl(tpWxPay1);
                Date date = new Date();
                view.addObject("pName", project.getTitle());
                view.addObject("amount", from.getAmount());
                view.addObject("tradeNo", tpWxPay1.getOrderId());
                view.addObject("codeurl", codeurl);
                view.addObject("projectId", from.getProjectId());
                view.addObject("date", date.getTime());
                view.addObject("nickName",user.getNickName());
                
                return view;
            }
            catch (Exception e)
            {
                view.addObject("error", "支付出现异常，请联系客服。");
                return view;
            }
        }
        else
        {
            view.addObject("error", "支付出现异常，请联系客服。");
            return view;
        }
    }
    
    /*
     * 正常项目的支付页面
     */
    @RequestMapping(value = "/visitorpay", produces = "application/json")
    public ModelAndView commondpay(@RequestParam(value="special_fund_id",required=false)Integer special_fund_id,DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/notDirectApliy");
        view.addObject("projectId", from.getProjectId());
        ApiProject p = projectFacade.queryProjectDetail(from.getProjectId());
        if(p!=null) {
            double process = 0.0;
            if (p.getCryMoney() >= 0.001) {
                process = p.getDonatAmount() / p.getCryMoney();
            }
            view.addObject("process", StringUtil.doublePercentage(
                    p.getDonatAmount(), p.getCryMoney(), 0));
        }else{
            view.addObject("process",0);
        }

        if(from.getpName()!=null&&from.getpName()!=""){//过滤
        	from.setpName(RepairVulnerabilityUtil.HTMLEncode(from.getpName()));
        	from.setpName(RepairVulnerabilityUtil.StringFilter(from.getpName()));
        	System.out.println("province>>>>"+from.getpName());
        }
        view.addObject("project", p);
        view.addObject("pName", from.getpName());
        view.addObject("amount", from.getAmount());
        view.addObject("extensionPeople", from.getExtensionPeople());
        view.addObject("special_fund_id", special_fund_id);
        return view;
    }
    
    /**
     * 同步通知的页面的Controller
     * 
     */
    @RequestMapping(value = "/return_url")
    public ModelAndView Return_url(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView view = null;
        String tradeStatus = request.getParameter("trade_status");
        String tradeNo = request.getParameter("out_trade_no");
        String notifyId = request.getParameter("trade_no");
        String ep = request.getParameter("extra_common_param");
        /*
        String ep = request.getParameter("extra_common_param")==null ?"":request.getParameter("extra_common_param");
        String[] items = ep.split("_");
        String projectId = "" ;
        String payType = "";
        String nickName = "" ;
        String tranNum = "";
        String pName = "";
        String donateAmount = "";
        
        if(items != null && items.length>4)
        {
        	projectId = items[0];
        	payType = items[1];
        	nickName = items[2];
        	tranNum = items[3];
        	pName = items[4];
        	donateAmount = items[5];
        }
        */
        logger.info("tradeStatus >>>>>"+tradeStatus);
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                logger.debug("支付宝渠道充值成功:: 对交易号" + tradeNo + "：：支付宝ID" + notifyId);
                /*
                if(payType.equals("1"))
                {
                	view = new ModelAndView("redirect:/project/newGardenView.do");
                }
                else 
                {
                	view = new ModelAndView("redirect:/project/view.do");
                }
                */
                view = new ModelAndView("redirect:/project/donateSuccess.do");
                view.addObject("tradeNo", tradeNo);
                /*
                view.addObject("projectId", projectId);
                view.addObject("nickName", nickName);
                view.addObject("pName", pName);
                view.addObject("amount", donateAmount);
                */
            }
            else
            {
                view = new ModelAndView("redirect:/project/donateSuccess.do");
                view.addObject("tradeNo", tradeNo);
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
                logger.info("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            return view;
        }
        else
        {
            // 查看错误alipay文档
            donateRecordFacade.buyDonateResult(tradeNo, notifyId, false, tradeStatus, "");
            return view = new ModelAndView("redirect:/project/index.do");
        }
    }
    
    /**
     * 异步通知付款状态的Controller
     * 
     */
    @RequestMapping(value = "/async")
    public String async(HttpServletRequest request, HttpServletResponse response)
    {
        String tradeNo = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        String notifyId = request.getParameter("trade_no");
        String ep = request.getParameter("extra_common_param");
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            //检查是否已经充值
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
        }
        return "/deposit/success";
        
    }
    
    private Map<String, String> alipayMap(HttpServletRequest request, HttpServletResponse response)
        throws MalformedURLException, DocumentException, IOException
    {
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
    
    public ModelAndView getview(int type)
    {
        ModelAndView view = null;
        if (type == 1)
        {
            view = new ModelAndView("/deposit/pay");
        }
        else
        {
            view = new ModelAndView("/deposit/common_pay");
        }
        return view;
    }
    
    /**
     * new pc端捐款
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/weixin/depositPublic")
    public ModelAndView weixindepositPublic(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("redirect:/tenpay/pay.do");
        //判段这个IP是否已经注册过
        String adressIp = SSOUtil.getUserIP(request);
        ApiFrontUser user = new ApiFrontUser();
        user.setRegisterIP(adressIp);
        user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
        user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
        user = userFacade.queryUserByParam(user);
        //根据IP创建用户
        Integer userId = null;
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
            logger.debug("游客募捐归类需要的参数：adressIp:" + adressIp);
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
      	if(list.size()>0){
      		dRecord.setPayConfigId(list.get(0).getId());
      		dRecord.setPartnerName(list.get(0).getName());
      	}
        tpWxPay1.setBody("认捐“" + project.getTitle() + "”项目");
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        dRecord.setProjectTitle(project.getTitle());
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(Double.valueOf(total_fee));
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(tpWxPay1.getOrderId());
        dRecord.setSource("PC");
        dRecord.setCompanyId(user.getCompanyId());
        if(request.getSession().getAttribute("extensionPeople")!=null&&from.getExtensionPeople()==null){
        	dRecord.setExtensionPeople(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	ApiFrontUser auser = userFacade.queryById(Integer.valueOf(request.getSession().getAttribute("extensionPeople").toString()));
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
        else{
        if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }}
        if(from.getDonateWord() !=null){
        	dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
        }
        //新增游客信息
        if (!StringUtils.isEmpty(from.getTouristName()) || !StringUtils.isEmpty(from.getTouristMobile()))
        {
            
            String touristMessage = "{\"name\":\"" + from.getTouristName().replaceAll("\"", "\'") + "\",\"mobile\":\"" + from.getTouristMobile().replaceAll("\"", "\'") + "\"}";
            dRecord.setTouristMessage(touristMessage);
        }
        
        //6：个人用户
        tpWxPay1.setAttach(String.valueOf(from.getProjectId()));
        ApiCapitalinout capitalinout = new ApiCapitalinout();
        if(list.size()>0){
        	capitalinout.setPayConfigId(list.get(0).getId());
        	capitalinout.setPartnerName(list.get(0).getName());
        }
        ApiResult rt = donateRecordFacade.buyDonate(dRecord, capitalinout, "tenpay",null,"");
        
        // 成功:1
        if (rt != null && rt.getCode() == 1)
        {
            // 建立请求
            try
            {
                //生成二维码
            	String codeurl; 
			    if(list.size()==1){
			    	codeurl = Demo.getCodeurlPublic(tpWxPay1, list.get(0));
			    }
			    else{
			    	codeurl = Demo.getCodeurl(tpWxPay1);
			    }
                Date date = new Date();
                view.addObject("pName", project.getTitle());
                view.addObject("amount", from.getAmount());
                view.addObject("tradeNo", tpWxPay1.getOrderId());
                view.addObject("codeurl", codeurl);
                view.addObject("projectId", from.getProjectId());
                view.addObject("date", date.getTime());
                view.addObject("nickName",user.getNickName());
                
                return view;
            }
            catch (Exception e)
            {
                view.addObject("error", "支付出现异常，请联系客服。");
                return view;
            }
        }
        else
        {
            view.addObject("error", "支付出现异常，请联系客服。");
            return view;
        }
    }
}
