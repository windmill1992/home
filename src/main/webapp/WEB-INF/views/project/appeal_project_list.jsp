<%@page import="com.guangde.home.utils.UserUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园基金会</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/userCenter.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer userCenter mygood">
	<div class="mygood">
    	<div class="uCEN-hd"><h2 class="w1000">&nbsp;王小二，您好！欢迎回来！这里是您的求助人页面 : )  </h2></div>
        <div class="uCEN-bd">
        	<div class="uCEN-bdL">
            	<div class="uCEN-user wuCEN-user">
                	<div class="uCEN-user-show"><i class="user-icon-gt">
                	     <% 
		                   String url = UserUtil.getUserHead(request, response);
		                   if(url!=null){
		                     out.print("<img src='"+url+"' width='120px' height='120px'>");
		                   }
		                %>
                	</i>
                	</div>
                </div>
                <ul id="uCENNav" class="uCEN-nav"> 
                	<li class="uCENCur">我的求助申请</li>
                    <li>消息与通知</li>
                </ul> 
            </div>
            <div class="uCEN-bdR">
            	<div class="uCEN-list list">
                	<div class="list-hd">
                    	<span class="list-tit">我的求助申请</span>
                    	<dl class="select">
                        	<dt class="sel-checked sel-btn">
                            	<a href="#" title="">发布申请</a>
                            </dt>
                        </dl>
                    	
                    </div>
                    <div class="list-bd">
                        <div class="list-data wlist-data">
                             	<!--<div class="list-prompt">提示信息</div>-->
                        	 <ul class="list-data-hd">
                             	<li class="wlst-col1">&nbsp; 项目名称</li>
                                <li class="wlst-col2">参与项目</li>
                                <li class="wlst-col3">状态</li>
                                <li class="wlst-col4">更新时间</li>
                                <li class="wlst-col5">操作</li>
                             </ul>
                             <div class="list-data-bd">
                             	<ul class="bgcolor1">
                                    <li class="wlst-col1">&nbsp;<a href="javascript:void(0);">北京雾霾天气研究项目</a></li>
                                    <li class="wlst-col2">激励行动-友邦青年领袖计划</li>
                                    <li class="wlst-col3">草稿</li>
                                    <li class="wlst-col4">2015-05-14 17:34:00</li>
                                    <li class="wlst-col5 wlst-br">
                                    	<a href="#" title="">预览</a>|<a href="#" title="">编辑</a>|<a href="#" title="">提交</a>|<a href="#" title="">删除</a>
                                    </li>
                                </ul>
                                <ul >
                                    <li class="wlst-col1">&nbsp;<a href="javascript:void(0);">北京雾霾天气研究项目</a></li>
                                    <li class="wlst-col2">激励行动-友邦青年领袖计划</li>
                                    <li class="wlst-col3">未通过</li>
                                    <li class="wlst-col4">2015-05-14 17:34:00</li>
                                    <li class="wlst-col5 wlst-br">
                                    	<a href="#" title="">预览</a>|<a href="#" title="">详情</a>
                                    </li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="wlst-col1">&nbsp;<a href="javascript:void(0);">北京雾霾天气研究项目</a></li>
                                    <li class="wlst-col2">激励行动-友邦青年领袖计划</li>
                                    <li class="wlst-col3">草稿</li>
                                    <li class="wlst-col4">2015-05-14 17:34:00</li>
                                    <li class="wlst-col5 wlst-br">
                                    	<a href="#" title="">预览</a>|<a href="#" title="">编辑</a>|<a href="#" title="">提交</a>|<a href="#" title="">删除</a>
                                    </li>
                                </ul>
                             </div>
                        </div>
                        <div class="wlist-remark">
                        	<h2>备注</h2>
                            <div class="remark-con">
                            	<p>草稿：尚未提交的项目，您可以编辑，提交，删除该项目，该状态下资助方看不到您的项目信息</p>
                                <p>提交：项目编辑完毕后，提交后工作人员对您提交的内容进行审核，提交后项目不可修改</p>
                                <p>编辑：项目在草稿状态下可以编辑</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mygood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
