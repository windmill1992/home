package com.guangde.home.vo.user;

import java.io.Serializable;

public class UserCard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2479872833654983712L;
	private Integer uid;//企业账号id
	private String name;//开户名 ==用户的真实姓名
	private String bankName;//开户行
	private String cardNo;//卡号
	private String province;//开户行所在省
	private String city;//开户行所在市
	private String area;//开户行所在区，县
	private Integer bid;//绑定id
	private Double money;//提款金额
	private String bankType="2";//借记卡，信用卡
	private Integer page = 1;//第几页
	private Integer pageSize = 9;//每页几行
	private Integer typeDt = 0;//查询的时间
	private Integer accountType; //账户类型，个人：1 对公：0
	private Integer pid;//项目id
	private String imageId;//图片id
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getTypeDt() {
		return typeDt;
	}
	public void setTypeDt(Integer typeDt) {
		this.typeDt = typeDt;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
}
