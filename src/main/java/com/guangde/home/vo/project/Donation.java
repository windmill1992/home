package com.guangde.home.vo.project;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 捐款项目
 *
 */
public class Donation {
	
	private int id;//捐款id
	private String name;//捐款人名称
	private double dMoney;//捐款金额
	private double donatAmountpt;//已募捐金额
	private double cryMoney;//求救金额
	private int copies;//份数
	private int dType;//捐款类型: 1 ,个人捐 2,企业捐 3,月捐 4,一起捐,5,项目转捐
	private Date dTime;//捐款时间
	private int dStatus;//捐款状态
	private String field;//领域
	private String tranNum; // 订单号
	private Integer userId;//捐助人id
	
	private int pid;//项目id
	private String title;//项目名称
	private int status;//项目状态  
	private String process;//项目捐款进度
	private int feedBackAmount ;//受赠人反馈条数
	private String imagesurl;//捐赠人员的头像
	private String recipients; //受助人
	private Integer donateNum ;// 捐款次数
	private Integer donationNum ; // 捐款的人数
	private String  leaveWord;//留言
	private String 	showTime ;

	/**
	 *  状态码：
	 *  
	    200：保存
		210：待面试
		220：待审核
		230：审核未通过
		240：募捐中
		250：执行中
		260：结束
	 * 
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getLeaveWord() {
		return leaveWord;
	}
	public void setLeaveWord(String leaveWord) {
		this.leaveWord = leaveWord;
	}
	public Integer getDonationNum() {
		return donationNum;
	}
	public void setDonationNum(Integer donationNum) {
		this.donationNum = donationNum;
	}
	public Integer getDonateNum() {
		return donateNum;
	}
	public void setDonateNum(Integer donateNum) {
		this.donateNum = donateNum;
	}
	public double getdMoney() {
		return dMoney;
	}
	public void setdMoney(double dMoney) {  
		this.dMoney  =   new   BigDecimal(dMoney).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public int getdType() {
		return dType;
	}
	public void setdType(int dType) {
		this.dType = dType;
	}
	public Date getdTime() {
		return dTime;
	}
	public void setdTime(Date dTime) {
		this.dTime = dTime;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		if(StringUtils.isBlank(name))
			return "";
		if(name.startsWith("游客"))
			return "游客";
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	public int getdStatus() {
		return dStatus;
	}
	public void setdStatus(int dStatus) {
		this.dStatus = dStatus;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getFeedBackAmount() {
		return feedBackAmount;
	}
	public void setFeedBackAmount(int feedBackAmount) {
		this.feedBackAmount = feedBackAmount;
	}
	
	public double getCryMoney() {
		return cryMoney;
	}
	public void setCryMoney(double cryMoney) {
		this.cryMoney = cryMoney;
	}
	public double getDonatAmountpt() {
		return donatAmountpt;
	}
	public void setDonatAmountpt(double donatAmountpt) {
		this.donatAmountpt = donatAmountpt;
	}
	public String getImagesurl() {
		return imagesurl;
	}
	public void setImagesurl(String imagesurl) {
		this.imagesurl = imagesurl;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public String getTranNum() {
		return tranNum;
	}
	public void setTranNum(String tranNum) {
		this.tranNum = tranNum;
	}
	
}
