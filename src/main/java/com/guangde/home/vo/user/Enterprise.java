package com.guangde.home.vo.user;

import org.apache.commons.lang.StringUtils;

public class Enterprise {
	
	private int vid;
	private int type;//企业类别：0,企业;1,事业单位;2,社会团体;3,党政和国家机关
	private String identification;//证件号
	private String business;//业务
	private String organization;//组织
	private String idenId;//证件号图片
	private String orgaId;//组织号图片
	private String legalPerson;//法人
	private String legalPimg1;//正面图;
	private String legalPimg2;//反面图;
	private String legalIdentify;//法人身份证号
	private String linkman;//联系人
	private String linkPhone;//联系号码
	private String phoneCode;//验证码
	
	private String head;//头像
	private String logo;//logo
	private String url;//网址
	private String info;//简介
	private String name;//公司名称
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getIdenId() {
		return idenId;
	}
	public void setIdenId(String idenId) {
		this.idenId = idenId;
	}
	public String getOrgaId() {
		return orgaId;
	}
	public void setOrgaId(String orgaId) {
		this.orgaId = orgaId;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLegalPimg1() {
		return legalPimg1;
	}
	public void setLegalPimg1(String legalPimg1) {
		this.legalPimg1 = legalPimg1;
	}
	public String getLegalPimg2() {
		return legalPimg2;
	}
	public void setLegalPimg2(String legalPimg2) {
		this.legalPimg2 = legalPimg2;
	}
	public String getLegalIdentify() {
		return legalIdentify;
	}
	public void setLegalIdentify(String legalIdentify) {
		this.legalIdentify = legalIdentify;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVid() {
		return vid;
	}
	public void setVid(int vid) {
		this.vid = vid;
	}
	@Override
	public String toString() {
		return "Enterprise [type=" + type + ", identification="
				+ identification + ", business=" + business + ", organization="
				+ organization + ", idenId=" + idenId + ", orgaId=" + orgaId
				+ ", legalPerson=" + legalPerson + ", legalPimg1=" + legalPimg1
				+ ", legalPimg2=" + legalPimg2 + ", legalIdentify="
				+ legalIdentify + ", linkman=" + linkman + ", linkPhone="
				+ linkPhone + ", phoneCode=" + phoneCode + "]";
	}
	//实名认证参数校验
	public boolean verifyParameters(){
	    
		if(StringUtils.isBlank(identification)){
			return false;
		}
		/*if(StringUtils.isBlank(organization)){
			return false;
		}*/
		if(StringUtils.isBlank(idenId)){
			return false;
		}
		/*if(StringUtils.isBlank(orgaId)){
			return false;
		}*/
		if(StringUtils.isBlank(legalPerson)){
			return false;
		}
		if(StringUtils.isBlank(legalPimg1)){
			return false;
		}
		if(StringUtils.isBlank(legalPimg2)){
			return false;
		}
		if(StringUtils.isBlank(legalIdentify)){
			return false;
		}
		if(StringUtils.isBlank(linkman)){
			return false;
		}
		if(StringUtils.isBlank(linkPhone)){
			return false;
		}
		if(StringUtils.isBlank(phoneCode)){
			return false;
		}
		return true;
	}
}
