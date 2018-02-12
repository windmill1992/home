package com.tenpay.utils.refund;

import java.util.HashMap;
import java.util.Map;

public class WXReFundUtil {
	private WXPay wxpay;
    private WXPayConfigImpl config;
    private String out_trade_no;

    public WXReFundUtil() throws Exception {
        config = WXPayConfigImpl.getInstance();
        wxpay = new WXPay(config);
    }

    /**
     * 退款
     */
    public Map<String, String> doRefund(String out_trade_no,String total_fee,String refund_fee) {
    	HashMap<String, String> data = new HashMap<String,String>();
    	data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_trade_no);
        data.put("total_fee", total_fee);
        data.put("refund_fee", refund_fee);
    	data.put("op_user_id", config.getMchID());
    	data.put("refund_fee_type", "CNY");
    	Map<String, String> r = null;
        try {
            r = wxpay.refund(data);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 查询退款
     * 已经测试
     */
    public void doRefundQuery() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_refund_no", out_trade_no);
        try {
            Map<String, String> r = wxpay.refundQuery(data);
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
