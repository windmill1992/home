package com.guangde.home.utils;

public class MD5Utils {
	
	
	public static final String signType_Constant="MD5";
	
	public static final String sign_Check_field="sdg44xcvjasdf8vzxcvz";
	
	private String signType;    //加密类型
	
	private String  sign;       //加密之后的字符串，用于验证
	
	private long timeStamp;     //时间戳，用于生成不同的串

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
