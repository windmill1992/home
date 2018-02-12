<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css?v=1"/>
</head> 


<body>
<div id="pageContainer">
  <div class="publish">
  	<!--标题//-->
    <div class="top">
    	<a href="http://www.17xs.org/ucenter/core/personalCenter_h5.do" class="back">&lt;返回</a>
    	<div class="title">近况反馈</div>
     </div>
     
     <!----成功提示//-->
    	<div class="form_box">
        	<ul>
            	<li class="text"><textarea cols="" rows="" id="msg-text">请输入求助人近期状况</textarea></li>
              <li class="upload_list" id="upload_list">
              </li>
              <li class="upload_list">
              	<a href="#" id ="chooseImage">+</a>
              </li>
              <li class="btns"><a href="#" class="pub_btn" id="msg-submit">提 交</a></li>
            </ul>
        </div>
    <!--页脚
      <div class="footer">
      	<span>© 2015 杭州智善  版权所有</span>
        <img src="images/min-logo.jpg"></div>//-->
  </div>
</div>
<input id="projectId" type="hidden" value="${projectId}">
<span id="mediaId" style="display:none;"></span>
</body>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" /></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : ['chooseImage','previewImage','uploadImage' ]
	});
</script>
<script data-main="<%=resource_url%>res/js/h5/feedback.js?v=201510131036" src="<%=resource_url%>res/js/require.min.js"></script>
</html>