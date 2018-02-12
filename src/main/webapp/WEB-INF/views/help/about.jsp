<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园-关于善园基金会</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="">首页</a> > 关于善园基金会
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight aboutCon">
            <div class="boederbox">
            	<h2>
                	<span class="lText">善园基金会介绍</span>
                </h2>
                <%--<div class="about-aim">--%>
                 	<%--<i class="lineTL"></i>--%>
                    <%--<i class="lineBR"></i>--%>
                    <%--互联网公益 公益互联网<br>--%>
                    <%--致力于让公益更高效，更简单，更有趣，更时尚<br>--%>
                    <%--善，是流淌于每个人心中不竭的生命源泉<br>--%>
                    <%--传递出去，就能滋养更多，帮助别人，就是善待自己<br>--%>
                    <%--善善与共  天下大同 --%>
                 <%--</div>--%>
                 <div class="about-brief">
                    <h2 style="font-size:15px;font-weight:bold">登记注册信息：</h2>
                    <p>
                        名称：宁波市善园公益基金会<br>
                        类型：具有公开募捐资格的慈善组织/地方性公募基金会<br>
                        机构代码：53330200MJ8959573D<br>
                        登记机关：宁波市民政局<br>
                        注册资金：2000万元<br>
                         法定代表人：严意娜<br>
                         成立时间：2015年3月27日<br>
                             有效期限：2021年11月2日<br>
                    </p>
                    <h2 style="font-size:15px;font-weight:bold">业务内容：</h2>
                    <p class="p1">宗旨：弘扬义善文化，倡导人人公益。</p>
                    <p class="p1">主要业务范围：运用互联网技术开展扶贫、救助、教育等公益慈善活动，向公众募集、对接公益慈善资源和需求。</p>
                    <p class="p1">基金会开发、推出善园网（www.17xs.org）平台，实现“救急难”和“公益众筹”两大业务。平台运用互联网应用技术及移动互联技术，将“救急难”需求和资源进行有效对接，以PC+移动端为载体，包括项目申请、审核、发布，善款募集、给付、跟踪，效果查询、评估、存档等功能，让捐赠轨迹透明，捐赠信息对称，捐赠成果可视，捐赠反馈及时。为急难人群提供便捷的求助平台，为企业提供履行社会责任的高效平台，为社会组织提供联合劝募的资源平台，为爱心人士提供行善的简易平台。</p>
                    <div class="about-show">
                        <img src="http://www.17xs.org/res/images/about/ss.png" title="" alt="">
                        <img src="http://www.17xs.org/res/images/about/gongmu.jpg" title="" alt="">
                        <img src="http://www.17xs.org/res/images/about/faren.jpg" title="" alt="">
                    </div>
                    <h2 style="font-size:15px;font-weight:bold">联系我们：</h2>
                    <p>地址：宁波市鄞州区泰康西路399号（宁波·善园）</p>
                    <p>联系方式：0571-87165191(杭州团队)</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0574-87412436(宁波团队)</p>
                 </div>

                
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/about.js" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
	(function(){
	    var bp = document.createElement('script');
	    var curProtocol = window.location.protocol.split(':')[0];
	    if (curProtocol === 'https') {
	        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
	    }
	    else {
	        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
	    }
	    var s = document.getElementsByTagName("script")[0];
	    s.parentNode.insertBefore(bp, s);
	})();
</script>
</body>
</html>