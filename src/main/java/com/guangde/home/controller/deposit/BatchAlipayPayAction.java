package com.guangde.home.controller.deposit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
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
import com.guangde.entry.ApiProject;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;

/**
 * 对于批量捐助项目的批量支付
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("batchpay")
public class BatchAlipayPayAction extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public static final String notify_url = "http://www.17xs.org/batchpay/alipay/async.do";
    
    // 页面跳转同步通知页面路径
    public static final String return_url = "http://www.17xs.org/batchpay/alipay/return_url.do";
    
    Logger logger = LoggerFactory.getLogger(BatchAlipayPayAction.class);
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    /*
     * 批量捐助项目的批量支付页面
     */
    @RequestMapping(value = "/batchpay", produces = "application/json")
    public ModelAndView batchpay(DepositForm from, HttpServletRequest request, 
    		@RequestParam(value = "extensionPeople", required = false) Integer extensionPeople, 
    		HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/common_amountpay");
        Integer userId = UserUtil.getUserId(request, response);
        if(userId != null){
        	ApiFrontUser user = userFacade.queryById(userId);
        	view.addObject("balance", user.getAvailableBalance());
        	view.addObject("userType", user.getUserType());
        }
        
        view.addObject("pList", from.getpList());
        ApiProject ap = new ApiProject();
        ap.setpList(from.getpList());
        ApiPage<ApiProject> apiPage  = projectFacade.queryProjectList(ap, 1, from.getpList().size());
        List<ApiProject> projects = apiPage.getResultData();
        view.addObject("projects", projects);
        view.addObject("extensionPeople", extensionPeople);//推广人id
        return view;
    }
    
    
    @RequestMapping(value = "/alipay/deposit", produces = "application/json")
    public ModelAndView deposit(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/alipay");
        int payType = from.getPayType();//支付错误返回方式
        String bank = from.getBank();
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
        Map<String, String> sParaTemp = alipayMap(request, response);
        sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
        sParaTemp.put("total_fee", total_fee);// 价格
        if (StringUtils.isNotEmpty(bank))
        {
            sParaTemp.put("paymethod", "bankPay");
            sParaTemp.put("defaultbank", bank);
        }
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
        
        sParaTemp.put("subject", "批量捐款项目分别为"+dRecord.getLeaveWord());
        dRecord.setUserId(userId);
        dRecord.setProjectId(285);//采用默认的项目id
        dRecord.setProjectTitle("批量捐款");
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(from.getSumMoney());
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(sParaTemp.get("out_trade_no"));
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
        //6：个人用户
        sParaTemp.put("extra_common_param", payType+"");
        
        if ("enterpriseUsers".equals(user.getUserType()))
        {
            if (from.getAmount() == 0 && user.getAvailableBalance() >= from.getSumMoney())
            {
                rt = donateRecordFacade.batchBuyDonate(dRecord, null, "freezType");
                view = new ModelAndView("redirect:/project/donateSuccess.do");
                view.addObject("tradeNo", dRecord.getTranNum());
                return view;
            }
            ApiCapitalinout capitalinout = new ApiCapitalinout();
            //4 ：表示企业用户
            sParaTemp.put("extra_common_param", "4");
            capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
            capitalinout.setSource("PC");
            capitalinout.setMoney(from.getAmount());
            capitalinout.setPayType("alipay");
            capitalinout.setInType(12);
            capitalinout.setUserId(userId);
            capitalinout.setBankType(from.getBank());
            rt = donateRecordFacade.batchBuyDonate(dRecord, capitalinout, "freezType");
        }
        else
        {
        	if (from.getAmount() == 0)
            {
        		if(user.getAvailableBalance() >= from.getSumMoney()){
        			rt = donateRecordFacade.batchBuyDonate(dRecord, null, "freezType");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", dRecord.getTranNum());
                    return view;
        		} 
            }
            //6：个人用户
            sParaTemp.put("extra_common_param", "6");
            rt = donateRecordFacade.batchBuyDonate(dRecord, null, "alipay");
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
                view.addObject("error", "支付出现异常，请联系客服。");
                return view;
            }
        }else {
        	 view.addObject("error", "支付出现异常，请联系客服。");
             return view;
		}
    }
    
    
    
    /**
     * 同步通知的页面的Controller
     * 
     */
    @RequestMapping(value = "/alipay/return_url")
    public ModelAndView Return_url(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView view = null;
        String tradeStatus = request.getParameter("trade_status");
        String tradeNo = request.getParameter("out_trade_no");
        String notifyId = request.getParameter("trade_no");
        String ep = request.getParameter("extra_common_param");
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                if ("4".equals(ep))
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "freezType");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                else if ("5".equals(ep))
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "alipay");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                else
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                logger.debug("支付宝批量捐渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                if ("4".equals(ep))
                {
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                else
                {
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                logger.debug("支付宝批量捐渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            return view;
        }
        else
        {
            // 查看错误alipay文档
            donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, false, tradeStatus, "");
            return view = new ModelAndView("/deposit/error");
        }
    }
    
    /**
     * 异步通知付款状态的Controller
     * 
     */
    @RequestMapping(value = "/alipay/async")
    public String async(HttpServletRequest request, HttpServletResponse response)
    {
        String tradeNo = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        String notifyId = request.getParameter("trade_no");
        String ep = request.getParameter("extra_common_param");
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                if ("4".equals(ep))
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "freezType");
                }
                else if ("5".equals(ep))
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "alipay");
                }
                else
                {
                    donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "");
                }
                logger.debug("支付宝批量捐渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.debug("支付宝批量捐渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            // 要写的逻辑。自己按自己的要求写
            System.out.println(">>>>>充值成功" + tradeNo);
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
}
