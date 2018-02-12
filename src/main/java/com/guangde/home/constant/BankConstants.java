package com.guangde.home.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankConstants {
	
	/*可以进行认证支付的银行	 */
	public final static List<String> AUTBANK = new ArrayList<String>();  
	/*银行的图片Id	 */
	public final static Map<String,String> BANK_IMAGE = new  HashMap<String,String>(); 
	/*根据代号取银行的名称 */
	public final static Map<String,String> BANK_NAME = new  HashMap<String,String>(); 
	static {  
		AUTBANK.add("01020000");//工商银行
		AUTBANK.add("01030000");//农业银行
		AUTBANK.add("01040000");//中国银行
		AUTBANK.add("01050000");//建设银行
		AUTBANK.add("03080000");//招商银行
		AUTBANK.add("03100000");//浦发银行
		AUTBANK.add("03030000");//光大银行
		AUTBANK.add("03070000");//平安银行
		AUTBANK.add("03040000");//华夏银行
		AUTBANK.add("03090000");//兴业银行
		AUTBANK.add("03020000");//中信银行
		AUTBANK.add("03060000");//广发银行
		AUTBANK.add("03050000");//民生银行
		AUTBANK.add("03010000");//交通银行
		
		BANK_IMAGE.put("中国工商银行", "01");
		BANK_IMAGE.put("建设银行", "02");
		BANK_IMAGE.put("农业银行", "03");
		BANK_IMAGE.put("招商银行", "04");
		BANK_IMAGE.put("中国银行", "05");
		BANK_IMAGE.put("邮政银行", "06");
		BANK_IMAGE.put("交通银行", "07");
		BANK_IMAGE.put("广发银行", "08");
		BANK_IMAGE.put("广大银行", "09");
		BANK_IMAGE.put("兴业银行", "10");
		BANK_IMAGE.put("平安银行", "11");
		BANK_IMAGE.put("中兴银行", "12");
		BANK_IMAGE.put("浦发银行", "13");
		BANK_IMAGE.put("民生银行", "14");
		BANK_IMAGE.put("北京银行", "15");
		BANK_IMAGE.put("上海银行", "16");
		BANK_IMAGE.put("杭州银行", "17");
		BANK_IMAGE.put("宁波银行", "18");
		BANK_IMAGE.put("富镇银行", "19");
		BANK_IMAGE.put("北京农业银行", "20");
		
		BANK_NAME.put("ICBCB2C", "中国工商银行");
		BANK_NAME.put("CCB", "中国建设银行");
		BANK_NAME.put("ABC", "中国农业银行");
		BANK_NAME.put("CMB", "招商银行");
		BANK_NAME.put("BOCB2C", "中国银行");
		BANK_NAME.put("POSTGC", "中国邮政储蓄银行");
		BANK_NAME.put("COMM-DEBIT", "交通银行");
		BANK_NAME.put("GDB", "广发银行");
		BANK_NAME.put("CEB-DEBIT", "中国光大银行");
		BANK_NAME.put("CIB", "兴业银行");
		BANK_NAME.put("SPABANK", "平安银行");
		BANK_NAME.put("CITIC-DEBIT", "中信银行");
		BANK_NAME.put("SPDB", "上海浦东发展银行");
		BANK_NAME.put("CMBC", "中国民生银行");
		BANK_NAME.put("BJBANK", "北京银行");
		BANK_NAME.put("SHBANK", "上海银行");
		BANK_NAME.put("HZCBB2C", "杭州银行");
		BANK_NAME.put("NBBANK", "宁波银行");
		BANK_NAME.put("FDB", "富滇银行");
		BANK_NAME.put("BJRCB", "北京农村商业银行");
		BANK_NAME.put("ICBCBTB", "中国工商银行（B2B");
		BANK_NAME.put("CCBBTB", "中国建设银行（B2B");
		BANK_NAME.put("ABCBTB", "中国农业银行（B2B");
		BANK_NAME.put("CMBBTB", "招商银行（B2B）");
		BANK_NAME.put("BOCBTB", "中国银行（B2B）");
	} 
}
