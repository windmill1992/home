package com.guangde.home.vo.user;

public class PUser {
	
	public static String USER_TYPE_P = "individualUsers";//个人
	public static String USER_TYPE_E = "enterpriseUsers";//企业用户
	//info
	private int id;//用户id
	private int type;//用户类别:0,捐款人1,求助人
	private String name;//用户名
	private String nickName; //用户昵称
	private String passWord;//登入密码
	private String newpassWord;//新密码
	private String phone;//手机号
	//
	private String code;//验证码
	private String phoneCode;//手机验证码
	private String spreadCode;//推广code
	
	private String imageId;//图片id字符串
	private String realname;	//真实姓名
	private String idCard;	//身份证号
	
	private String address; //居住地址
	private String detailAddress; //详细地址
	private String reason;	//原因
	private String field;	//领域
	private String status;	//显示状态 1：填写页面 2：正在审核中。。。 3：已实名认证
	private String vocation; //职业 
	private String workUnit; //工作单位
	private String weixin; //微信
	
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getNewpassWord() {
		return newpassWord;
	}
	public void setNewpassWord(String newpassWord) {
		this.newpassWord = newpassWord;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSpreadCode() {
		return spreadCode;
	}
	public void setSpreadCode(String spreadCode) {
		this.spreadCode = spreadCode;
	}
	@Override
	public String toString() {
		return "PUser [id=" + id + ", type=" + type + ", name=" + name
				+ ", passWord=" + passWord + ", phone=" + phone + ", code="
				+ code + ", phoneCode=" + phoneCode + ", spreadCode="
				+ spreadCode + ", imageId=" + imageId + ", realname="
				+ realname + ", idCard=" + idCard + ", address=" + address
				+ ", reason=" + reason + ", field=" + field + ", stauts="
				+ status + ", getAddress()=" + getAddress() + ", getReason()="
				+ getReason() + ", getField()=" + getField() + ", getId()="
				+ getId() + ", getType()=" + getType() + ", getName()="
				+ getName() + ", getPassWord()=" + getPassWord()
				+ ", getPhone()=" + getPhone() + ", getCode()=" + getCode()
				+ ", getPhoneCode()=" + getPhoneCode() + ", getImageId()="
				+ getImageId() + ", getRealname()=" + getRealname()
				+ ", getIdCard()=" + getIdCard() + ", getStauts()="
				+ getStatus() + ", getSpreadCode()=" + getSpreadCode()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVocation() {
		return vocation;
	}
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
}
