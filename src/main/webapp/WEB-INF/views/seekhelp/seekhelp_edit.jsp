<!DOCTYPE html>
<%@page import="com.guangde.home.vo.common.HomeFile"%>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.guangde.home.vo.project.Appeal,java.util.*"%>
<%@page import="com.guangde.home.utils.UserUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园基金会-求助更新</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/helpinfo.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/util/jquery-ui.css"/>


<!--<link rel="stylesheet" type="text/css" href="res/css/dev/helpList.css"/>-->
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer userCenter helpEdit">
	<div class="page"> 
        <div class="page-hd"><h2 class="w1000">&nbsp;${user.userName}，您好！欢迎回来！这里是您的个人中心:)</h2></div>
        <div class="page-bd">
        	<div class="uCEN-L">
                <div class="uCEN-user">
                    <div class="uCEN-user-show"><i class="user-icon-gt">
                     <% 
	                   String url = UserUtil.getUserHead(request, response);
	                   if(url!=null){
	                     out.print("<img src='"+url+"' width='120px' height='120px'>");
	                   }
	                %>
                    </i></div>
                    <div class="uCEN-user-info">
                        Hi，${user.userName}，
                        <br/>在过去的${user.days}天里
                        <br/>您一共参与了<i class="em-color">${user.countProject}个项目</i>
                        <br/>您总共捐赠了<i class="em-color">${user.totalAmount}元</i>
                        <br/>高于<i class="em-color">${user.percent*100}%</i>的善园基金会友
                        <br/><i class="biger em-color">爱心爆棚！</i> 
                    </div>
                </div>
                <ul id="uCENNav" class="uCEN-nav"> 
                     <li><a href="<%=resource_url%>ucenter/core/mygood.do" target="_self">捐款明细</a></li>
                    <li><a href="<%=resource_url%>ucenter/core/msg.do" target="_self">消息与通知</a></li>
                    <li><a href="<%=resource_url%>ucenter/core/myloveGroup.do" target="_self">善管家</a></li>
                    <li><a href="<%=resource_url%>user/realname.do" target="_self">实名认证</a></li>
                    <li class="uCENCur"><a href="<%=resource_url%>ucenter/pindex.do" target="_self">我的求助</a></li>
                </ul>
            </div>
            <div class="uCEN-R"> 
                <div class="uCEN-hd">
                    <span class="uCEN-tit">求助申请</span>  
                    <a class="back" href="javascript:window.history.back()" title="">返回</a> 
                </div>
                <div class="uCEN-bd">
                	<div class="helpEdit helpInfo">
                    <dl class="info-box info-tit fdef">
                        <dt><label for="tit"><i>*</i>标题：</label></dt>
                        <dd><input id="title" type="text" placeholder="标题不能超过18个字" value="${project.title}"></dd>
                    </dl>
                    <dl class="info-box info-con fdef">
                        <dt><label for="con"><i>*</i>内容：</label></dt>
                        <dd><textarea id="content">${project.content}</textarea></dd>
                    </dl>
                    <dl class="info-box info-imgs fdef">
                        <dt><label>图片：</label></dt>
                        <dd>
                       <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">							
                        <ul class="imgList">
                             <% Appeal appel = (Appeal)request.getAttribute("project");
                                 List<HomeFile> imgs = appel.getImgs();
                                 if(imgs.size()>0){
                                    for(HomeFile img:imgs){
                                    out.print("<li data='"+img.getId()+"'><i>×</i><img src='"+img.getUrl()+"' alt=''></li>");
                                 }
                                 }
                                 if(imgs.size()<10){
                                    out.print("<li class='last'>"+"+"+"<input type='file' name='file' hidefocus='true' id='imgFile' class='file-input'><input type='hidden' name='type' id='type' value='4'></li>");
                                 }
                             %>
                        </ul>
                        </form>
                        <p class="prim clear">
                            注：图片大小每张请控制在2M以内，最多可上传10张图片
                        </p>
                        </dd>
                    </dl>
                    <dl class="info-box info-area">
                        <dt><label><i>*</i>所在地区：</label></dt>
                        <dd>
                        <select name="province_id" id="province"></select>
                        <select class="city" id="city"></select>
                        <select class="area" id="county"></select> 
                        </dd>
                    </dl>
                    <dl class="info-box info-street">
                        <dt><label for="street"><i>*</i>详情地址：</label></dt>
                        <dd><textarea id="detailAddress" placeholder="建议您如实填写详细地址，例如街道名称，门牌号码，楼层和房间号等信息" class="address">${project.detailAddress}</textarea></dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="helpMoney"><i>*</i>求助金额：</label></dt>
                        <dd><input id="cryMoney" type="text" class="itemtext textmoney" value="${project.cryMoney}">元</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label><i>*</i>您的身份：</label></dt>
                        <dd>
                        <select class="itemselect" id="identity">
                            <option tel="callerI" <c:if test="${project.identity=='callerI'}"> selected = "selected"</c:if>>求助者本人</option>
                            <option tel="callerFather"<c:if test="${project.identity=='callerFather'}"> selected = "selected"</c:if>>求助人父亲</option>
                            <option tel="callerMother"<c:if test="${project.identity=='callerMother'}"> selected = "selected"</c:if>>求助人母亲</option>
                            <option tel="volunteers"<c:if test="${project.identity=='volunteers'}"> selected = "selected"</c:if>>爱心志愿者</option>
                            <option tel="otherCaller"<c:if test="${project.identity=='otherCaller'}"> selected = "selected"</c:if>>其它</option>
                        </select>
                        <c:choose>
						    <c:when test="${project.identity=='otherCaller'}">
								<input type="text" id="identityInfo" class="itemtext textmoney" style="display:inline-block" value="${project.identityInfo}">
						    </c:when>
							<c:otherwise>
				                <input type="text" id="identityInfo" class="itemtext textmoney" style="display:none">
						    </c:otherwise>   
						 </c:choose>
						</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label><i>*</i>项目截止时间：</label></dt>
                        <dd>
                           <input id="deadline"  type="text" class="itemtext textmoney" readonly="readonly"  value="<fmt:formatDate  value='${project.deadDate}' pattern='yyyy/MM/dd'/>">（请根据您求助的紧急程度填写，上限为180天，不填写默认为最大值）
                    </dl>
                    <dl class="info-box info-field">
                    	<input type="hidden" id="field" value="${project.field}">
                        <dt><label><i>*</i>求助领域：</label></dt>
                        <dd><span <c:if test="${project.field=='disease'}"> class="sel"</c:if> tel="disease">疾病</span><span <c:if test="${project.field=='education'}"> class="sel"</c:if> tel="education">助学</span></dd>
                    </dl>
                    <p class="promot-bank">请填写您所在机构的银行账户，以便我们拨款</p>
                    <dl class="info-box nopadT">
                        <dt><label class="bankUser">开户单位：</label></dt>
                        <dd> <input id="accountName" type="text" value="${project.accountName}">例：宁波善园基金会</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankName">银行名：</label></dt>
                        <dd><input id="accountBank" type="text" value="${project.accountBank}">例：招商银行总行营业部</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankAccount">银行账号：</label></dt>
                        <dd><input id="collectNum" type="text" value="${project.collectNum}">例：755917671010888</dd>
                    </dl>
                    <div class="btn-box"></div>
                    <div class="btn-box">
                    	<div class="info-warn" id="warn"> </div>
                        <div class="btn btn-gray" id="hiDraft">
                            保存草稿 
                        </div>
                         <p class="btn-point"><i></i>如果您对一些内容还需要斟酌，可先保存为草稿（草稿文件可再次编辑后提交）</p>
                        <div class="btn btn-green" id="hiSubmit">提交资料</div>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="projectId" value="${projectId}">
<input type="hidden" id="location" value="${project.location}">
<%@ include file="./../common/footer.jsp" %>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js?v=20150511"></script>
<script src="<%=resource_url%>res/js/util/datepicker/jquery-ui.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-ar.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-fr.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-he.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-zh-TW.js"></script>
<script>
$(function(){
$( "#deadline" ).datepicker( "option", "dateFormat", 'yy-mm-dd');
$( "#deadline" ).datepicker( "option",$.datepicker.regional[ 'zh-TW'] );
$( "#deadline" ).datepicker({ minDate: "7D", maxDate: "+6M +1D" });
})
</script>
<script data-main="<%=resource_url%>res/js/dev/helpEdit.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
