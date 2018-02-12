package com.guangde.home.controller.deposit;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.api.user.IUserRelationInfoFacade;
import com.guangde.entry.*;
import com.guangde.home.constant.BankConstants;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("alipay")
public class AlipayAction extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public static final String notify_url = "http://www.17xs.org/alipay/async.do";
    
    // 页面跳转同步通知页面路径
    public static final String return_url = "http://www.17xs.org/alipay/return_url.do";
    
    Logger logger = LoggerFactory.getLogger(AlipayAction.class);
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    @Autowired
    private IUserRelationInfoFacade userRelationInfoFacade;
    
    @RequestMapping(value = "/deposit", produces = "application/json")
    public ModelAndView deposit(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/alipay");
        int payType = from.getPayType();//支付错误返回方式
        String bank = from.getBank();
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            view = new ModelAndView("redirect:/user/sso/login.do");
            return view;
        }
        ApiFrontUser user = userFacade.queryById(userId);
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
        //查询支付配置
      	ApiPayConfig config = new ApiPayConfig();
      	config.setUserId(project.getUserId());
      	List<ApiPayConfig> list = projectFacade.queryByParam(config);
      	if(list.size()>0){
      		dRecord.setPayConfigId(list.get(0).getId());
      		dRecord.setPartnerName(list.get(0).getName());
      	}
        sParaTemp.put("subject", "认捐“" + project.getTitle() + "”项目");
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        dRecord.setProjectTitle(project.getTitle());
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(user.getUserType());
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(from.getSumMoney());
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(sParaTemp.get("out_trade_no"));
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
        /*
        if ("enterpriseUsers".equals(user.getUserType()))
        {
        	*/
            if (from.getAmount() == 0)
            {
                rt = donateRecordFacade.buyDonate(dRecord, null, "freezType",null,"");
                view = new ModelAndView("redirect:/project/donateSuccess.do");
                view.addObject("tradeNo", dRecord.getTranNum());
                return view;
            }
            ApiCapitalinout capitalinout = new ApiCapitalinout();
          	if(list.size()>0){
          		capitalinout.setPayConfigId(list.get(0).getId());
          		capitalinout.setPartnerName(list.get(0).getName());
          	}
            //4 ：表示企业用户
            sParaTemp.put("extra_common_param", "4");
            capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
            capitalinout.setSource("PC");
            capitalinout.setMoney(from.getAmount());
            if (StringUtils.isNotEmpty(bank))
            {
                capitalinout.setPayType("bankpay");
                capitalinout.setBankType(BankConstants.BANK_NAME.get(bank));
            }
            else
            {
                capitalinout.setPayType("alipay");
            }
            capitalinout.setInType(1);
            capitalinout.setUserId(userId);
            capitalinout.setBankType(from.getBank());
            rt = donateRecordFacade.buyDonate(dRecord, capitalinout, "freezType",null,"");
            /*
        }
        else
        {
            //6：个人用户
            sParaTemp.put("extra_common_param", "6");
            if (StringUtils.isNotEmpty(bank))
            {
                dRecord.setBankType(BankConstants.BANK_NAME.get(bank));
                rt = donateRecordFacade.buyDonate(dRecord, null, "bankpay");
            }
            else
            {
                rt = donateRecordFacade.buyDonate(dRecord, null, "alipay");
            }
        }
        */
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
            	  System.out.println("支付测试1"+rt.getCode());
                view = getview(payType);
                view.addObject("error", "支付出现异常，请联系客服。");
                return view;
            }
        }
        else if (rt.getCode() == 90001)
        {
        	logger.error(""+rt.getCode());
            view = getview(payType);
            System.out.println("支付测试2"+rt.getCode());
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
    /**
     * H5余额支付
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/freezeDeposit", produces = "application/json")
    public ModelAndView freezeDeposit(DepositForm from,HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView();
        
        Integer userId = UserUtil.getUserId(request, response);
        if (userId == null)
        {
            view = new ModelAndView("redirect:/user/sso/login.do");
            return view;
        }
        ApiFrontUser user = userFacade.queryById(userId);
        
        ApiDonateRecord dRecord = new ApiDonateRecord();
        ApiProject project = projectFacade.queryProjectDetail(from.getProjectId());
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        dRecord.setProjectTitle(project.getTitle());
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(user.getUserType());
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(from.getSumMoney());
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(StringUtil.uniqueCode());
        dRecord.setSource("H5");
        dRecord.setCompanyId(user.getCompanyId());
        dRecord.setLeaveWord(from.getDonateWord());
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
        
        ApiResult rt = donateRecordFacade.buyDonate(dRecord, null, "freezType",null,"");
        
        view = new ModelAndView("redirect:/project/view_paysuccess_h5.do?projectId="
    			+project.getId()+"&amount="+from.getSumMoney()+"&tradeNo="+dRecord.getTranNum());
        view.addObject("pName", project.getTitle());
        return view;
           
            
    }
    
    /*
     * 正常项目的支付页面
     */
    @RequestMapping(value = "/commondpay", produces = "application/json")
    public ModelAndView commondpay(@RequestParam(value="special_fund_id",required=false)Integer special_fund_id,DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView view = new ModelAndView("deposit/common_pay");
        Integer userId = UserUtil.getUserId(request, response);
        /*
        if (userId == null) {
        	view = new ModelAndView("redirect:/user/sso/login.do");
        	return view;
        }
        */
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
        view.addObject("project", p);
        view.addObject("projectId", from.getProjectId());
        view.addObject("pName", from.getpName());
        view.addObject("amount", from.getAmount());
        view.addObject("extensionPeople", from.getExtensionPeople());//推广人id
        view.addObject("special_fund_id", special_fund_id);
        if (userId != null)
        {
            ApiFrontUser user = userFacade.queryById(userId);
            if (user != null)
            {
                view.addObject("balance", user.getAvailableBalance());
                view.addObject("userType", user.getUserType());
            }
        }
        
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
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
        	logger.info("ep>>>>>>" + ep);
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                if ("4".equals(ep))
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "freezType");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                    //view = new ModelAndView("redirect:/ucenter/core/mygood.do");
                }
                else if ("5".equals(ep))
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "alipay");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                   // view = new ModelAndView("redirect:/ucenter/core/mygood.do");
                }else if(ep.contains("bazaar")) {
                	donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                	//更新礼品购买人数 bazaar_123@1_124@1
    				try {
    					String[] bazaars = ep.split("_");
    					for (int i = 1; i < bazaars.length; i++) {
    						String[] gifts = bazaars[i].split("@");
    						userRelationInfoFacade.updateGift(Integer.valueOf(gifts[0]), Integer.valueOf(gifts[1]));
    					}
    					logger.info("更新礼品购买数量成功》》》》》》" + ep);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
				}
                else
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                    //view = new ModelAndView("redirect:/ucenter/core/mygood.do");
                }
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                if ("4".equals(ep))
                {
                   // view = new ModelAndView("redirect:/ucenter/core/mygood.do");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                else
                {
                   // view = new ModelAndView("redirect:/ucenter/core/mygood.do");
                    view = new ModelAndView("redirect:/project/donateSuccess.do");
                    view.addObject("tradeNo", tradeNo);
                }
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            return view;
        }
        else
        {
            // 查看错误alipay文档
            donateRecordFacade.buyDonateResult(tradeNo, notifyId, false, tradeStatus, "");
            return view = new ModelAndView("/deposit/error");
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
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                if ("4".equals(ep))
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "freezType");
                }
                else if ("5".equals(ep))
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "alipay");
                }
                else if(ep.contains("bazaar")) {
                	donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                	//更新礼品购买人数 bazaar_123@1_124@1
    				try {
    					String[] bazaars = ep.split("_");
    					for (int i = 1; i < bazaars.length; i++) {
    						String[] gifts = bazaars[i].split("@");
    						userRelationInfoFacade.updateGift(Integer.valueOf(gifts[0]), Integer.valueOf(gifts[1]));
    					}
    					logger.info("更新礼品购买数量成功》》》》》》" + ep);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
				}
                else
                {
                    donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                }
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
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
    
    public static void main(String[] args) throws Exception {
    	Map<String, String> sParaTemp = new HashMap<String, String>();
    	 sParaTemp.put("service", "single_trade_query");// 接口服务----即时到账
    	 sParaTemp.put("partner", AlipayConfig.partner);// 支付宝PID
    	 sParaTemp.put("out_trade_no", "14573616163111896");// 支付宝PID
//    	 sParaTemp.put("trade_no", "2016022200001000320089201414");// 支付宝PID
         sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 统一编码
         System.out.println(AlipaySubmit.buildRequest("", "", sParaTemp));
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
    
}
