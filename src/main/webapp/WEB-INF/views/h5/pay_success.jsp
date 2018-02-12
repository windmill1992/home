<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>支付成功</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/pay.css"/>
</head> 

<body>
<div id="pageContainer">
<div class="project">
    <!------按钮//---->
    <div class="btns">
      <div style="margin:5px;">
        <table style="width:100%">
          <tbody>
            <tr>
<!--               <td width="49%"><a href="detail4.html" id="btn_submit1"> -->
<!--                 <div class="btn"><img src="images/btn_icon1.png">邀请别人</div> -->
<!--                 </a></td> -->
                <td width="49%"><a href="http://www.17xs.org/h5/list/?extensionPeople=${extensionPeople }" id="btn_submit1">
                <div class="btn"><img src="<%=resource_url%>res/images/h5/images//btn_icon1.png">捐助其他项目</div>
                </a></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!--成功信息//-->
    <div class="top_info">
    	<img src="<%=resource_url%>res/images/h5/images/thank_bg.png">
		<div class="txt">
            <div class="thank">感谢您！</div>
            <div class="info">善园基金会感谢您为“${title}”捐助了<fmt:formatNumber type="number" pattern="###0.00#" value = "${amount}"/>元</div>
      	</div>    	
    </div>
    <!--详情信息//-->
    <div class="detail_info">      
      <!--关注项目//-->
      <ul class="content_tabs">
        <li class="cur">最新捐助</li>
      </ul>
      <div class="clear"></div>
      <!----捐助人--//-->
      <div class="list" id="list_latest"> 
        </div>
	</div>
    <!--页脚//-->
  </div>
</div>
<input type="hidden" id="pprim" value="${project.title}">
<input id="projectId" type="hidden" value="${project.id}">
<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
<script data-main="<%=resource_url%>res/js/h5/project_pay.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>