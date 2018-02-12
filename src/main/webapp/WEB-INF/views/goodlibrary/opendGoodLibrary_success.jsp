<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>开通善库</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodlibrary/clear_success.css" />
</head> 
<!--头部 end-->
<!--主体 start-->
<body>
<%@ include file="./../common/newhead.jsp" %><!-- <div class="bodyer userCenter mygood"></div>  -->
<div class="page">
    <div class="page-bd">
				<c:choose>
				    <c:when test="${userType=='enterpriseUsers'}">
				        <div class="uCEN-L">
                   		<%@ include file="./../common/eleft_menu.jsp"%>
                   		</div>
                    </c:when>
                    <c:otherwise>
	                    <div class="uCEN-L">
						<%@ include file="./../common/pleft_menu.jsp"%>
					   </div>
                    </c:otherwise>  
				</c:choose>
        <div class="uCEN-R">
            <div class="library">
                <p class="libraryone">开通成功</p>
                <div class="cleartwo">
                    <img class="cleartwo_fl" src="<%=resource_url%>res/images/goodlibrary/succ1.png">
                    <div class="cleartwo_fr">
                        <p class="text1">恭喜您，善库已开通~</p>
                        <p class="text2">微信扫一扫邀请亲人、朋友、同事加入善库吧^_^！</p>
                    </div>
                </div>
                <div id="qrcode" class="pr_ewm" style=""></div>
                <input type="text" id="getval" style="display:none" />
                <div class="clearBtn">
                    <a href="javascript:void();" class="btn_one" id="addGoodLibraryPerson">添加善库成员</a>
                    <a href="<%=resource_url %>goodlibrary/gotoSetGoodLibrary.do?libraryId=${libraryId }" class="btn_two">设置加入条件</a>
                </div>
            </div>
        </div>
    </div>
    </div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="goodlibraryId" value="${libraryId }"/> 
<script type="text/javascript" src="<%=resource_url%>res/js/qrcode.js"></script>
<script data-main="<%=resource_url%>res/js/dev/goodlibrary/goodLibrarySuccess.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
	window.onload = function() {
		var goodlibraryId = $('#goodlibraryId').val();
		var qrcode = new QRCode(document.getElementById("qrcode"), {
		width : 200,//设置宽高
		height : 200
		});
		var url = 'http://www.17xs.org/goodlibrary/index.do?lirbraryId='
				+ goodlibraryId;
		qrcode.makeCode(url);
	}
</script>
</body>
</html>