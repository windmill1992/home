package com.guangde.home.vo.user;

/**
 * @author Administrator
 *
 */
public class Employe{
	
	public static final int status_audit = 201;
	public static final int status_back = 202;
	public static final int status_success = 203;
	
	private int uid;//员工账号id
	private int eId;//员工id
	private String name;//员工账号名
	private String realName;//员工真实姓名
	private int level;//员工等级
	private int state;//员工状态
	private int bId;//公司账号id
	
	private int headImg;//头像
	private String companyName;//公司名称
	private int companyId;
	private String location;//公司地址
	private String detailAddress;//详细地址
	private String postion;//职位
	
	private int verifyId;//员工验证时使用
	
	public int geteId() {
		return eId;
	}
	public void seteId(int eId) {
		this.eId = eId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getVerifyId() {
		return verifyId;
	}
	public void setVerifyId(int verifyId) {
		this.verifyId = verifyId;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getHeadImg() {
		return headImg;
	}
	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getPostion() {
		return postion;
	}
	public void setPostion(String postion) {
		this.postion = postion;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}
