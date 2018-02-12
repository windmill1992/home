package com.guangde.home.constant;


public enum ResultEnum {
	SUCCESS(1,"成功"),
	NOT_LOGIN(0,"未登录"),
	PARAMETER_ERROR(2,"参数错误"),
	INNER_ERROR(-1,"系统异常"),
	NOTBOUND_PROJECTID(3,"没有绑定的项目id"),
	CARD_OVERLIMIT(4,"银行卡超出限制"),
	CARD_BOUNDED(5,"银行卡已绑定"),
	DATAISNULL(6,"数据为空");;
	private int state;
	
	private String stateInfo;
	
	ResultEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public static ResultEnum stateOf(int index){
		for (ResultEnum state : values()) {
			if(state.getState() == index){
				return state;
			}
		}
		return null;
	}
}
