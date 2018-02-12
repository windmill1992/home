package com.guangde.home.vo.user;

import java.util.Date;

public class Activity {

	private int aid;//活动id(助善id)
	private Date cDate;//创建时间
	private long cTime;
	private int state;//活动状态
	
	private Integer pId;//项目id
	private String pName;//项目名称
	
	private Double perMoney;//助善时，号召一人，捐款的钱
	private Double totalMoney;//助善总金额
	private Double payMoney;//已助善
	private String process;//助善进度
	private int peopleNums;//号召人数

	private double credit;//余额不够的充值金额
	private String payType;//充值方式
	
	private String companyCode;//助善口令
	private boolean isOwn = false;//是否是当前用户发起
	
	private String stopReason;//审核失败原因或结束原因
		
	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}
	public Double getPerMoney() {
		return perMoney;
	}
	public void setPerMoney(Double perMoney) {
		this.perMoney = perMoney;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public int getPeopleNums() {
		return peopleNums;
	}
	public void setPeopleNums(int peopleNums) {
		this.peopleNums = peopleNums;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public boolean isOwn() {
		return isOwn;
	}
	public void setOwn(boolean isOwn) {
		this.isOwn = isOwn;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public boolean valideCreateParam(){
		if(pId==null)
			return false;
		if(perMoney==null)
			return false;
		if(totalMoney==null)
			return false;
		return true;
	}
}
