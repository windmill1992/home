<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta name="baidu-site-verification" content="SxG3ais7R3" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./common/file_url.jsp" %>
<meta name="keywords"/>
<meta name="description"/>

<meta name="viewport"/>
<meta name="baidu-site-verification" content="LXxErKoY7i" />
<title>善园网—一起行善，互联网公益平台</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNewlunbo.css"/>
<script type='text/javascript'>window.BWEUM||(BWEUM={});BWEUM.info = {"stand":true,"agentType":"browser","agent":"bi-collector.oneapm.com/static/js/bw-send-411.4.5.js","beaconUrl":"bi-collector.oneapm.com/beacon","licenseKey":"I66F7~vv2ai6V3Nn","applicationID":2282681};</script>
<script type="text/javascript" src="//bi-collector.oneapm.com/static/js/bw-loader-411.4.5.js"></script>
</head> 
<style type="text/css">
	.headLogo{
		width: 90%;margin: 0 auto;border-bottom: 1px solid #FF9002;display: flex;display: -webkit-flex;overflow: hidden;height: 100px;
		justify-content: space-between;line-height: 100px;max-width: 1000px;
	}
	.headLogo .logo{margin-left: 20px;}
	.headLogo .logo img{width: 60%;vertical-align: middle;}
	.headLogo .about{margin-right: 20px;}
	.headLogo .about a{color: #666;font-size: 15px;}
	.errorMain{width: 60%;margin: 10% auto;text-align: center;max-width: 700px;}
	.errorMain .error-pic img{width: 100%;}
	.errorMain p{color: #666;margin-top: 40px;margin-bottom: 40px;}
	.errorMain .btn{width: 100px;margin: 20px auto;height: 40px;line-height: 40px;font-size: 15px;text-align: center;background: #FF9002;border-radius: 3px;}
	.errorMain .btn a{color: #fff;}
	@media only screen and (min-width:320px) and (max-width:640px){
		.errorMain{width: 80%;}
	}
	
</style>
<body>
<!--头部 start-->
<%-- <%@ include file="./common/newhead.jsp" %> --%>
<!--头部 end-->
<!--主体 start-->
<div class="headLogo">
	<div class="logo">
		<a href="http://www.17xs.org"><img src="<%=resource_url%>res/images/logo-lg.jpg"/></a>
	</div>
	<div class="about">
		<a href="http://www.17xs.org/help/about/" id="about-sy">关于善园</a>
	</div>
</div>
<div class="errorMain">
	<div class="error-pic">
		<img src="<%=resource_url%>res/images/error.png"/>
	</div>
	<p>啊哦~一不小心闯进了未知领域，请点击下面的按钮返回首页</p>
	<div class="btn">
		<a href="http://www.17xs.org">返回首页</a>
	</div>
</div>
<!--<div style="text-align: center;padding-top: 200px"><h1 style="font-size: 40px">error:页面没找到</h1></div>-->
<!--主体 end-->
<!--底部 start-->
<%-- <%@ include file="./common/newfooter.jsp" %> --%>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/indexNew.js" src="<%=resource_url%>res/js/require.min.js"></script>
<!-- 百度统计 -->
<!-- <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1257726653'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1257726653' type='text/javascript'%3E%3C/script%3E"));</script> -->
<script>
	(function(){
		var w = screen.width;
		if(w<=640){
			var oA = document.getElementById('about-sy');
			oA.href = 'http://www.17xs.org/h5News/news_view.do?type=%E5%96%84%E5%9B%AD%E5%BF%AB%E8%AE%AF';
			oA.innerHTML = '善园快讯';
		}
	})();
</script>
</body>
</html>
