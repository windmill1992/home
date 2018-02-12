package com.llpay.config;

/**
* 支付配置信息
* @author guoyx e-mail:guoyx@lianlian.com
* @date:2013-6-25 下午01:45:06
* @version :1.0
*
*/
public interface ServerURLConfig{
    String PAY_URL = "https://yintong.com.cn/payment/bankgateway.htm"; // 连连支付WEB收银台支付服务地址
    String QUERY_USER_BANKCARD_URL = "https://yintong.com.cn/traderapi/userbankcard.htm"; // 用户已绑定银行卡列表查询
    String QUERY_BANKCARD_URL = "https://yintong.com.cn/traderapi/bankcardquery.htm"; //银行卡卡bin信息查询
    String AUT_PAY_URL ="https://yintong.com.cn/payment/authpay.htm" ;//WEB 认证支付接口
}
