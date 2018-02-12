package com.guangde.home.utils;

import org.apache.commons.lang.StringUtils;

import com.guangde.entry.ApiProject;


public class ProjectUtil {
	/*
	 * 判断领域的类型
	 */
	public static String field(int key){
		String field="";
		switch (key) {
		case 1:
			//疾病	
			field="disease";
			break;
		case 2:
			//教育	
			field="education";
			break;
		case 3:
			//救灾	
			field="disasterRelief";
			break;
		case 4:
			//扶贫	
			field="povertyAlleviation";
			break;
		case 5:
			//环保	
			field="environmentalProtection";
			break;
		case 6:
			//动物保护	
			field="animalProtection";
			break;
		case 7:
			//农业农村	
			field="agricultureRuralAreas";
			break;
		case 8:
			//劳工	
			field="worker";
			break;
		case 9:
			//老人	
			field="elderly";
			break;
		case 10:
			//宗教信仰	
			field="religiousBelief";
			break;
		case 11:
			//文件艺术	
			field="artFile";
			break;
		case 12:
			//残障	
			field="disabled";
			break;
		case 13:
			//善园	
			field="garden";
			break;
		default:
			field="hot";
			break;
		}
		return field;
	}
	
	public static String verifyProject(ApiProject project){
		if(project==null){
			return "没有对应项目";
		}
		if(StringUtils.isBlank(project.getTitle())){
			return "标题不能为空";
		}
		if(StringUtils.isBlank(project.getContent())){
			return "求助内容不能为空";
		}
		if(StringUtils.isBlank(project.getLocation())){
			return "地址不能为空";
		}
		if(StringUtils.isBlank(project.getDetailAddress())){
			return "详细地址不能为空";
		}
		if(project.getCryMoney()<1000){
			return "求助金额不能小于1000";
		}
		if(project.getDeadline()==null){
			return "截止日期不能为空";
		}
		return null;
	}
}
