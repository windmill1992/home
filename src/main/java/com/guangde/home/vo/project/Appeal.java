package com.guangde.home.vo.project;

import java.util.Date;
import java.util.List;

import com.guangde.home.vo.common.HomeFile;
/**
 * 
 * 受捐项目
 *
 */
public class Appeal {

	private Integer id;//项目id
	private String title;//项目标题
	private int status;//项目状态
	private Date uTime;//更新时间
	private String content;//项目内容
	private List<HomeFile> imgs;//图片列表
	private String imgIds;//图片id集合
	private List<String> imgsDetail;//图片的描述列表
    private String location;//地址
    private String detailAddress;//详细地址
    private double cryMoney;//求助金额
    private double donatAmount;//已助捐金额
    private double panyAmount;//已经提现金额
    private long cTime;
    private String identity;//身份
    private String identityInfo;//identity为otherCaller时，的身份信息
    private long deadline;//截止时间
    private Date deadDate;
    private String field;//领域
    private String fieldChinese;
    
    private int type;// 0:保存并预览  1：发布
    
    private String name;//发布人姓名
    private String Idcard;//发布人身份证Id
    private String Address;//发布人地址
    private String gzdw;//发布人工作单位
    private String zy;//发布人职业
    private String Phone;//发布人电话
    private String weixin;//发布人qq/微信
    private String linkMan;//项目联系人
    private String appealName;//受助人姓名
    private String appealSex;//受助人性别
    private int appealAge;//受助人年龄
    private String appealIdcard;//受助人身份证号
    private String appealAddress;//受助人地址
    private String appealgzdw;//受助人工作单位
    private String appealzy;//受助人职业
    private String appealPhone;//受助人电话
    private String relation;   //与发布人的关系
    
    private String rPhone;// reterence证明人电话
    private String rname;//证明人姓名	
    private String rAddress;//证明人地址
    private String rzw;//证明人职务
    private String rgzdw;//证明人工作单位
    
    private Integer helpType;//个人和机构的项目分类：1（为本人），2（为家人），3（为其他亲戚），4（机构为本人），5（机构为公益项目）
    public Integer getHelpType() {
		return helpType;
	}
	public void setHelpType(Integer helpType) {
		this.helpType = helpType;
	}
	public String getRgzdw() {
		return rgzdw;
	}
	public void setRgzdw(String rgzdw) {
		this.rgzdw = rgzdw;
	}
	/**
     * 捐款百分比
     */
    private String donatePercent ;
    /**
     * 是否需要自愿者
     */
    private Integer isNeedVolunteer ;
    
    
    
    public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Integer getIsNeedVolunteer() {
		return isNeedVolunteer;
	}
	public void setIsNeedVolunteer(Integer isNeedVolunteer) {
		this.isNeedVolunteer = isNeedVolunteer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return Idcard;
	}
	public void setIdcard(String idcard) {
		Idcard = idcard;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getGzdw() {
		return gzdw;
	}
	public void setGzdw(String gzdw) {
		this.gzdw = gzdw;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getAppealName() {
		return appealName;
	}
	public void setAppealName(String appealName) {
		this.appealName = appealName;
	}
	public String getAppealSex() {
		return appealSex;
	}
	public void setAppealSex(String appealSex) {
		this.appealSex = appealSex;
	}
	public int getAppealAge() {
		return appealAge;
	}
	public void setAppealAge(int appealAge) {
		this.appealAge = appealAge;
	}
	public String getAppealIdcard() {
		return appealIdcard;
	}
	public void setAppealIdcard(String appealIdcard) {
		this.appealIdcard = appealIdcard;
	}
	public String getAppealAddress() {
		return appealAddress;
	}
	public void setAppealAddress(String appealAddress) {
		this.appealAddress = appealAddress;
	}
	public String getAppealgzdw() {
		return appealgzdw;
	}
	public void setAppealgzdw(String appealgzdw) {
		this.appealgzdw = appealgzdw;
	}
	public String getAppealzy() {
		return appealzy;
	}
	public void setAppealzy(String appealzy) {
		this.appealzy = appealzy;
	}
	public String getAppealPhone() {
		return appealPhone;
	}
	public void setAppealPhone(String appealPhone) {
		this.appealPhone = appealPhone;
	}
	public String getrPhone() {
		return rPhone;
	}
	public void setrPhone(String rPhone) {
		this.rPhone = rPhone;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getrAddress() {
		return rAddress;
	}
	public void setrAddress(String rAddress) {
		this.rAddress = rAddress;
	}
	/**
     * 收款方式
     */
    private String payMethod;
    
    /**
     * 开户人姓名
     */
    private String accountName;
    
    /**
     * 开户银行
     */
    private String accountBank;
    
    /**
     * 收款号码
     */
    private String collectNum;
    
    
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
	public Date getuTime() {
		return uTime;
	}
	public void setuTime(Date uTime) {
		this.uTime = uTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public double getCryMoney() {
		return cryMoney;
	}
	public void setCryMoney(double cryMoney) {
		this.cryMoney = cryMoney;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public String getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}
	public String getImgIds() {
		return imgIds;
	}
	public void setImgIds(String imgIds) {
		this.imgIds = imgIds;
	}
	public String getIdentityInfo() {
		return identityInfo;
	}
	public void setIdentityInfo(String identityInfo) {
		this.identityInfo = identityInfo;
	}
	public Date getDeadDate() {
		return deadDate;
	}
	public void setDeadDate(Date deadDate) {
		this.deadDate = deadDate;
	}
	public List<HomeFile> getImgs() {
		return imgs;
	}
	public List<String> getImgsDetail() {
		return imgsDetail;
	}
	public void setImgsDetail(List<String> imgsDetail) {
		this.imgsDetail = imgsDetail;
	}
	public void setImgs(List<HomeFile> imgs) {
		this.imgs = imgs;
	}
	public double getDonatAmount() {
		return donatAmount;
	}
	public void setDonatAmount(double donatAmount) {
		this.donatAmount = donatAmount;
	}
	public double getPanyAmount() {
		return panyAmount;
	}
	public void setPanyAmount(double panyAmount) {
		this.panyAmount = panyAmount;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRzw() {
		return rzw;
	}
	public void setRzw(String rzw) {
		this.rzw = rzw;
	}
	public String getFieldChinese() {
		return fieldChinese;
	}
	public void setFieldChinese(String fieldChinese) {
		this.fieldChinese = fieldChinese;
	}
	public String getDonatePercent() {
		return donatePercent;
	}
	public void setDonatePercent(String donatePercent) {
		this.donatePercent = donatePercent;
	}
	
}