package com.guangde.home.vo.deposit;

import java.io.Serializable;

/**
 * 
 * 连连充值的参数
 *
 */
public class LianlianForm extends DepositForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7029992301236621420L;
	
	private String promptStr;
	private String realname;
	private String idNumber;
	private String money;
	private String pay_type;
	private String cardNumber;
	private String no_order;
	private String orderNum;
	private String money_order;
	private String bindIdentity;
	private String bank_code;
	private String useCard;
	private String no_agree;
	private String payUrl;
	private String md5Key;	//加密的key
	private String partner; //商户编号
	private String coupon;// 优惠券

	
	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getPromptStr() {
		return promptStr;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String payType) {
		pay_type = payType;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String noOrder) {
		no_order = noOrder;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String moneyOrder) {
		money_order = moneyOrder;
	}

	public String getBindIdentity() {
		return bindIdentity;
	}

	public void setBindIdentity(String bindIdentity) {
		this.bindIdentity = bindIdentity;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bankCode) {
		bank_code = bankCode;
	}

	public String getUseCard() {
		return useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String noAgree) {
		no_agree = noAgree;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	
	
	
	
}
