package com.guangde.home.vo.deposit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 充值的参数
 *
 */
public class DepositForm implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2858452067238618705L;
    
    private int projectId;//捐款id
    
    private String tokenCode; //是否本站提交识别，防止重复提交
    
    private String pName;//捐款人名称
    
    private double amount;//捐款金额
    
    private double sumMoney;//总金额
    
    private double perMoney;//每份金额
    
    private int copies;//份数
    
    private int dType;//捐款类型: 1 ,个人捐 2,企业捐 3,月捐 4,一起捐
    
    private Date dTime;//捐款时间
    
    private int payType = 1;//1:善园  2:其它 4:企业充值,5:wap支付宝
    
    private String bank;//选择的银行
    
    private String donateName;//您的姓名(公司名)
    
    private String donateWord;//祝福语
    
    private String tradeNo;//交易号
    
    private String codeurl;//二维码链接
    
    private long date;//创建订单时间
    
    private String touristName; // 游客姓名
    
    private String touristMobile; // 游客手机号
    
    private Integer extensionPeople ; // 推广人
    
    private List<Integer> pList;//需要捐款的项目列表
    
    private List<Integer> cList;//不需要捐款的项目列表
    
    private String  slogans; // 宣言
    
    private String realName;//真实姓名
    
    private String mobileNum;//电话
    /**
     * 捐款人姓名
     */
    private String nickName ;
    /**
     * 捐款人id
     */
    private Integer userId ; 
    /**
     * 捐款人头像
     */
    private String headImage ;
    /**
     * 批量捐的项目个数
     */
    private Integer pcount ; 
    /**
     * 批量捐项目id字符
     */
    private String  plistId;
    /**
     * 商品众筹唯一标识
     */
    private String crowdFunding;
    /**
     * 善库余额支付的善库id
     */
    private Integer goodLibraryId;
    /**
     * 日捐月捐id
     */
    private Integer donateTimeId;
    /**
     * 是否匿名
     */
    private Integer nameOpen;
    
    public Integer getDonateTimeId() {
		return donateTimeId;
	}

	public void setDonateTimeId(Integer donateTimeId) {
		this.donateTimeId = donateTimeId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getGoodLibraryId() {
		return goodLibraryId;
	}

	public void setGoodLibraryId(Integer goodLibraryId) {
		this.goodLibraryId = goodLibraryId;
	}

	public String getCrowdFunding() {
		return crowdFunding;
	}

	public void setCrowdFunding(String crowdFunding) {
		this.crowdFunding = crowdFunding;
	}

	public double getPerMoney()
    {
        return perMoney;
    }
    
    public void setPerMoney(double perMoney)
    {
        this.perMoney = perMoney;
    }
    
	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getExtensionPeople() {
		return extensionPeople;
	}

	public void setExtensionPeople(Integer extensionPeople) {
		this.extensionPeople = extensionPeople;
	}

	public String getTouristName()
    {
        return touristName;
    }
    
    public void setTouristName(String touristName)
    {
        this.touristName = touristName;
    }
    
    public String getTouristMobile()
    {
        return touristMobile;
    }
    
    public void setTouristMobile(String touristMobile)
    {
        this.touristMobile = touristMobile;
    }
    
    public String getBank()
    {
        return bank;
    }
    
    public void setBank(String bank)
    {
        this.bank = bank;
    }
    
    public String getDonateName()
    {
        return donateName;
    }
    
    public void setDonateName(String donateName)
    {
        this.donateName = donateName;
    }
    
    public String getDonateWord()
    {
        return donateWord;
    }
    
    public void setDonateWord(String donateWord)
    {
        this.donateWord = donateWord;
    }
    
    public int getProjectId()
    {
        return projectId;
    }
    
    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }
    
    public String getTokenCode()
    {
        return tokenCode;
    }
    
    public void setTokenCode(String tokenCode)
    {
        this.tokenCode = tokenCode;
    }
    
    public String getpName()
    {
        return pName;
    }
    
    public void setpName(String pName)
    {
        this.pName = pName;
    }
    
    public double getAmount()
    {
        return amount;
    }
    
    public void setAmount(double amount)
    {
        this.amount = amount;
    }
    
    public int getCopies()
    {
        return copies;
    }
    
    public void setCopies(int copies)
    {
        this.copies = copies;
    }
    
    public int getdType()
    {
        return dType;
    }
    
    public void setdType(int dType)
    {
        this.dType = dType;
    }
    
    public Date getdTime()
    {
        return dTime;
    }
    
    public void setdTime(Date dTime)
    {
        this.dTime = dTime;
    }
    
    public int getPayType()
    {
        return payType;
    }
    
    public void setPayType(int payType)
    {
        this.payType = payType;
    }
    
    public double getSumMoney()
    {
        return sumMoney;
    }
    
    public void setSumMoney(double sumMoney)
    {
        this.sumMoney = sumMoney;
    }
    
    public String getTradeNo()
    {
        return tradeNo;
    }
    
    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }
    
    public String getCodeurl()
    {
        return codeurl;
    }
    
    public void setCodeurl(String codeurl)
    {
        this.codeurl = codeurl;
    }
    
    public long getDate()
    {
        return date;
    }
    
    public void setDate(long date)
    {
        this.date = date;
    }

	public List<Integer> getpList() {
		return pList;
	}

	public void setpList(List<Integer> pList) {
		this.pList = pList;
	}

	public List<Integer> getcList() {
		return cList;
	}

	public void setcList(List<Integer> cList) {
		this.cList = cList;
	}

	public Integer getPcount() {
		return pcount;
	}

	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	public String getPlistId() {
		return plistId;
	}

	public void setPlistId(String plistId) {
		this.plistId = plistId;
	}

	public String getSlogans() {
		return slogans;
	}

	public void setSlogans(String slogans) {
		this.slogans = slogans;
	}

	public Integer getNameOpen() {
		return nameOpen;
	}

	public void setNameOpen(Integer nameOpen) {
		this.nameOpen = nameOpen;
	}
	
}
