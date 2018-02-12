package com.guangde.home.vo.project;

import java.util.Date;


public class ProjectPayMoneyRecord {
	/**
	 * 提现金额
	 */
	private Double withdrawAmount ; 
	/**
	 * 提现次数
	 */
	private Integer withdrawNum ; 
	/**
	 * 最近提现时间
	 */
	private Date time;
	/**
	 * 提现账号
	 */
	private String account ;
	/**
	 * 所属银行名称
	 */
	private String recipientName ; 
	
	private Integer userId ; 
	
	private Integer projectId ; 
	/**
	 * 项目名称
	 */
	private String title ;
	/**
	 * 项目领域
	 */
	private String field;
	/**
	 * 项目以募集金额
	 */
	private Double donateAmount ; 
	/**
	 * 开户名     真实姓名
	 */
	private String realName ;

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Double getDonateAmount() {
		return donateAmount;
	}
	public void setDonateAmount(Double donateAmount) {
		this.donateAmount = donateAmount;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public Integer getWithdrawNum() {
		return withdrawNum;
	}
	public void setWithdrawNum(Integer withdrawNum) {
		this.withdrawNum = withdrawNum;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	
}
