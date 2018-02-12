package com.guangde.home.constant;

public class ProjectConstant {

	/** 保存（草稿）*/
	public static final int PROJECT_STATUS_DRAFT =200;//保存（草稿）
	/** 待面试*/
	public static final int PROJECT_STATUS_AUDIT1 =210;//待面试
	/** 待审核*/
	public static final int PROJECT_STATUS_AUDIT2 =220;//待审核
	/** 审核未通过*/
	public static final int PROJECT_STATUS_BACK =230;//审核未通过
	/** 募捐中*/
	public static final int PROJECT_STATUS_COLLECT =240;//募捐中
	/** 执行中*/
	@Deprecated
	public static final int PROJECT_STATUS_EXECUTE =250;//执行中
	/** 结束*/
	public static final int PROJECT_STATUS_DONE =260;//结束
}
