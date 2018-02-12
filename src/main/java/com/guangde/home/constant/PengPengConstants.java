package com.guangde.home.constant;

/**
 * 系统常量，运行时不可配置.
 *
 */
public class PengPengConstants {

	/**
     * 系统缺省编码.
     */
    public static final String ENCODING = "GBK";
    
    /**
     * 未注册用户
     */
    public static final String DONOR_TYPE_OUT = "externalPers";
    
    /**
     * 用户类型-个人
     */
    public static final String PERSON_USER = "individualUsers";
    /**
     * 用户类型-游客
     */
    public static final String PERSON_TOURIST_USER = "touristUsers";
    
    /**
     * 未注册-用户名
     * 默认：游客+时间戳
     */
    public static final String VISITOR = "游客";
    
    public static final String WENXIN = "weixin";
    /** 求助领域的缓存标识  */
    public static final String SEEKHELP_FIELD = "SEEKHELP_FIELD";
    
    /** 求助领域的准备材料缓存标识  */
    public static final String SEEKHELP_FIELD_INFO = "SEEKHELP_FIELD_INFO";
    
    /** 求助领域的准备材料列表缓存标识  */
    public static final String SEEKHELP_FIELD_INFO_LIST = "SEEKHELP_FIELD_INFO_LIST";
    
    /** 首页轮播图缓存标识  */
    public static final String INDEX_BANNER_LIST = "INDEX_BANNER_LIST";
    public static final String INDEX_PROJECT_BANNER_LIST = "INDEX_PROJECT_BANNER_LIST";
    /** 778首页轮播图缓存标识 */
    public static final String INDEX_778_BANNER_LIST = "INDEX_778_BANNER_LIST";
    
    /** 捐款总金额金额缓存标识  */
    public static final String DONATION_TOTAL_MONEY = "DONATION_TOTAL_MONEY";
    
    /** 累计帮助家庭缓存标识  */
    public static final String HELP_FAMILY_NUMBER = "HELP_FAMILY_NUMBER";
    
    /** 累计捐款人缓存标识  */
    public static final String DONATION_TOTAL_PEOPLE_NUMBER = "DONATION_TOTAL_PEOPLE_NUMBER";
    
    /** 首页爱心故事缓存标识  */
    public static final String INDEX_LOVE_LIST = "INDEX_LOVE_LIST";
    
    /** 首页最新新闻缓存标识  */
    public static final String INDEX_NEWS_LIST = "INDEX_NEWS_LIST";
    
    /** 778首页中心大事件新闻缓存标识  */
    public static final String INDEX_BIGEVENTS_LIST = "INDEX_BIGEVENTS_LIST";
    
    /** 778首页公益科普新闻缓存标识  */
    public static final String INDEX_SICENCE_POPULATION_LIST = "INDEX_SICENCE_POPULATION_LIST";
    
    /** 首页最新捐款缓存标识  */
    public static final String INDEX_NEWS_DONATE_LIST = "INDEX_NEWS_DONATE_LIST";
    
    /** 首页善管家列表缓存标识  */
    public static final String INDEX_LOVEGROUP_LIST = "INDEX_LOVEGROUP_LIST";
    
    /** 首页本周推荐项目缓存标识  */
    public static final String INDEX_WEEKPROJECT_LIST = "INDEX_WEEKPROJECT_LIST";
    public static final String INDEX_WEEKPROJECT_NEW_LIST = "INDEX_WEEKPROJECT_NEW_LIST";
    
    /** 首页众筹 推荐项目缓存标识  */ 
    public static final String INDEX_CROWDFUND_ROJECT_LIST = "INDEX_CROWDFUND_ROJECT_LIST";
    
    /** 项目执行进度详情缓存标识  */
    public static final String PROJECT_SCHEDUlE_LIST = "PROJECT_SCHEDUlE_LIST";
    
    /** 项目人员信息列表缓存标识  */
    public static final String PROJECT_USERINFO_LIST = "PROJECT_USERINFO_LIST";
    
    /** 项目爱心故事列表缓存标识  */
    public static final String PROJECT_NEWS_LIST = "PROJECT_NEWS_LIST";
    
    /** 首页的推荐项目缓存存标识  */
    public static final String INDEX_PROJECT_FIELD_LIST = "INDEX_PROJECT_FIELD_LIST";
   /*             善园缓存标识            start              */
    /** 最新捐赠记录缓存存标识  */
    public static final String GARDEN_PROJECT_DONATION_MOST_LIST = "GARDEN_PROJECT_DONATION_MOST_LIST";
    
    /** 捐赠记录排行榜缓存存标识  */
    public static final String GARDEN_PROJECT_DONATION_NEW_LIST = "GARDEN_PROJECT_DONATION_NEW_LIST";
    /*             善园缓存标识             end             */
    
    /*             首页缓存标识            start              */
    /** 专项基金缓存存标识  */
    public static final String INDEX_PROJECT_SPECIALFUND = "INDEX_PROJECT_SPECIALFUND";
    /**领域缓存存标识  */
    public static final String INDEX_PROJECT_FIELD = "INDEX_PROJECT_INDEX_FIELD";
    
    /**省份模块数据缓存存标识 首页 */
    public static final String INDEX_PROJECT_PROVINCE = "INDEX_PROJECT_INDEX_PROVINCE";
    
    /**省份模块数据缓存存标识  */
    public static final String INDEX_PROJECT_PROVINCES = "INDEX_PROJECT_INDEX_PROVINCES";
    
    /**募捐项目模块数据缓存存标识  */
    public static final String INDEX_PROJECT_DONATEPROJECTLIST = "INDEX_PROJECT_INDEX_DONATEPROJECTLIST";
    
    /**善款去向模块数据缓存存标识  */
    public static final String INDEX_PROJECT_DONATEGO = "INDEX_PROJECT_INDEX_DONATEGO";
    
    /**友情链接数据缓存存标识  */
    public static final String INDEX_FRIENDLINK_IMG = "INDEX_FRIENDLINK_IMG";
    /**友情链接数据缓存存标识  */
    public static final String INDEX_FRIENDLINK_TEXT = "INDEX_FRIENDLINK_TEXT";
    /**爱心企业链接数据缓存标识 */
    public static final String INDEX_LOVECOMPANY = "INDEX_LOVECOMPANY";
    /** 累计帮助人数缓存标识*/
    public static final String INDEX_HELP_PEOPLE_TOTAL = "INDEX_HELP_PEOPLE_TOTAL";
    
    /** 项目点击量缓存H5  */
    public static final String PROJECT_CLICKRATE_H5 = "PROJECT_CLICKRATE_H5";
    
    /** 项目点击量缓存PC  */
    public static final String PROJECT_CLICKRATE_PC = "PROJECT_CLICKRATE_PC";
    
    /** 项目分享次数缓存 */
    public static final String Project_ShareClick = "Project_ShareClick";
    
    /**行善记录-折线图-捐款次数（近30天）*/
    public static final String DATASTATISTICS_CHARTS_NUM = "DATASTATISTICS_CHARTS_NUM";
    /**行善记录-折线图-捐款金额（近30天）*/
    public static final String DATASTATISTICS_CHARTS_MONEY = "DATASTATISTICS_CHARTS_MONEY";
    /**行善记录-折线图-捐款时间（近30天）*/
    public static final String DATASTATISTICS_CHARTS_TIME = "DATASTATISTICS_CHARTS_TIME";
    /**上周捐款排行（前十名）*/
    public static final String DATASTATISTICS_WEEK_LIST = "DATASTATISTICS_WEEK_LIST";
    /**上月捐款排行（前十名）*/
    public static final String DATASTATISTICS_MONTH_LIST = "DATASTATISTICS_MONTH_LIST";
    /**本年捐款排行（前十名）*/
    public static final String DATASTATISTICS_YEAR_LIST = "DATASTATISTICS_YEAR_LIST";
    /**总捐款排行（前十名）*/
    public static final String DATASTATISTICS_TOTAL_LIST = "DATASTATISTICS_TOTAL_LIST";
    /**上周捐款总额*/
    public static final String DATASTATISTICS_WEEK_MONEY = "DATASTATISTICS_WEEK_MONEY";
    /**上月捐款总额*/
    public static final String DATASTATISTICS_MONTH_MONEY = "DATASTATISTICS_MONTH_MONEY";
    /**本年捐款总额*/
    public static final String DATASTATISTICS_YEAR_MONEY = "DATASTATISTICS_YEAR_MONEY";
    /**用户登录id*/
    public static final String LOGIN_USERID = "LOGIN_USERID";
    
    public static final String SESSION_KEY = "SESSION_KEY";
}
