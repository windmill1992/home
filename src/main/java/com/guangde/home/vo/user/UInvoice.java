package com.guangde.home.vo.user;

import java.io.Serializable;

public class UInvoice implements Serializable{
	
	private static final long serialVersionUID = 4368854269564129627L;
	
	private Integer id;

    
    /**
     * 用户id
     */
    private Integer userId;
    
    /**
     * 收货地址
     */
    private Integer addressId;
    private String province;
    
    private String city ;
    
    private String area ; 
    
    private String detailAddress ;
    
    private String name;
    
    private String mobile ;
    /**
     * 开票金额
     */
    private Double invoiceAmount ;
    /**
     * 开票内容
     */
    private String content ;
    /**
     * 是否包邮
     */
    private Integer isFree ;
    
    /**
     * 开票抬头
     */
    private String invoiceHead ; 
    
    /**
     * 快递费用
     */
    private Double mailAmount ;
    /**
     * 快递公司
     */
    private String mailCompany  ;
    /**
     * 发票状态
     */
    private Integer state ;
    /**
     * 快递单号
     */
    private String mailCode ;
    
    private String createTime ;
    
    private String lastUpdateTime ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public Double getMailAmount() {
		return mailAmount;
	}

	public void setMailAmount(Double mailAmount) {
		this.mailAmount = mailAmount;
	}

	public String getMailCompany() {
		return mailCompany;
	}

	public void setMailCompany(String mailCompany) {
		this.mailCompany = mailCompany;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
}
