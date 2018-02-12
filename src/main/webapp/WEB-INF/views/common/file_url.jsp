<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  <%
	//--------------------------------------------------------------
	// 读取系统运行时配置
	// 这些属性 jsp 页面不能直接使用
	//--------------------------------------------------------------
// 	String public_resource = com.binguo.common.config.BGConfig.get("CONTEXT_URI_PUBLIC");
    String resource_url = "http://www.17xs.org/";
//  String resource_url = "http://192.168.14.67:8083/";
    String web_url = "http://www.17xs.org/";
// 	String resource_js =  com.binguo.common.config.BGConfig.get("CONTEXT_URI_RESOURCE")+"/js"; //String resource_js = "/resource/js";
// 	String resource_css = com.binguo.common.config.BGConfig.get("CONTEXT_URI_RESOURCE")+"/css";  //String resource_css = "/resource/css";
// 	String resource_img = com.binguo.common.config.BGConfig.get("CONTEXT_URI_RESOURCE")+"/images";   //String resource_img = "/resource/images";


	//--------------------------------------------------------------
	// 定义内部使用的属性
	// 这些属性只供 jsp 页面内部使用
	//--------------------------------------------------------------
    String webName = "彩票2元网";
	String fxUrl = "http://fx.cp2y.com";
	String bbsUrl = "http://bbs.cp2y.com";
	String newsUrl = "http://news.cp2y.com";
	String zqUrl = "http://zq.cp2y.com";
	String wapUrl = "http://wap.cp2y.com";
	String adPositionUrl="http://news.cp2y.com/cp2y/1418/adPosition/v_view.do";
	String servicePhone = "400-666-7575";
	String version = "201802111125";
	String uc_version = "201802081150";
	String base_version = "201801021000";
	String detail_version = "201802021700";
    Integer headTag=0;
%>
<link rel="shortcut icon" href="<%=resource_url%>res/images/favicon.ico"/>
<script>
window.baseurl="<%=resource_url%>";
</script>