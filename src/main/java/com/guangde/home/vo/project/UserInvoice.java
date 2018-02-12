package com.guangde.home.vo.project;

import java.io.Serializable;

import com.guangde.entry.ApiProject;



public class UserInvoice implements Serializable{

	private static final long serialVersionUID = 5218358284746182869L;
	/**
	 * 发票id
	 */
	private int id ;
	
	/**
	 * 快递单号
	 */
	private int mailCode ;
	
	/**
	 * 礼品id
	 */
	private Integer giftId ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public int getMailCode() {
		return mailCode;
	}

	public void setMailCode(int mailCode) {
		this.mailCode = mailCode;
	}

	@Override
	public String toString() {
		return "UserInvoice [id=" + id + ", mailCode=" + mailCode + "]";
	} 
	
	

}
