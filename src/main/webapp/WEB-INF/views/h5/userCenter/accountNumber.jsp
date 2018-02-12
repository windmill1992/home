<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<title>收款账号</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=201712211600">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/aided_info.css?v=20171127">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=201712211600">
</head>
<body>
	<div class="header">
	    <a href="javascript:history.go(-1);" class="back">返回</a>
	    <div>收款账号</div>
	</div>
	<c:choose>
		<c:when test="${userCards!=null }">
		<div id="beforeUpdate">
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="开户名：" class="release_new" id="accountName" value="${userCards.accountName }" readonly>
			    </div>
			</div>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="开户行：" class="release_new" id="accountBank" value="${userCards.bankName }" readonly>
			    </div>
			</div>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="银行卡号：" class="release_new" id="collectNum" value="${userCards.card }" readonly>
			    </div>
			</div>
			<div class="bind_b1" style="display: none" id="yzm">
			    <div class="bind_fr1 release_fr release_new">
			        <input type="text" placeholder="发送验证码到${mobile.substring(0,3) }****${mobile.substring(7,11)}" id="code">
			        <a href="javascript:;" class="fsyz">发送验证码</a>
			    </div>
			</div>
		</div>
		</c:when>
		<c:otherwise>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="开户名：" class="release_new" id="accountName">
			    </div>
			</div>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="开户行：" class="release_new" id="accountBank">
			    </div>
			</div>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="银行卡号：" class="release_new" id="collectNum">
			    </div>
			</div>
			<div class="bind_b1">
			    <div class="bind_fr1 release_new">
			        <input type="text" placeholder="发送验证码到${mobile.substring(0,3) }****${mobile.substring(7,11)}" id="code">
			        <a  href="javascript:;" class="fsyz">发送验证码</a>
			    </div>
			</div>
		</c:otherwise>
	</c:choose>
    <footer>
	    <c:choose>
		    <c:when test="${userCards!=null }">
				<div class="bjqr">
				    <a href="/uCenterProject/gotoAccountMoney.do?money=${money}&projectId=${projectId}">
				        <p class="bj_fl">确认，下一步</p>
				    </a>
				    <a href="javascript:;">
				        <p class="bj_fr">编辑</p>
				    </a>
				</div>
		    </c:when>
		    <c:otherwise>
			    <div class="foot_qt">
			        <a href="javascript:void(0);" id="next">下一步</a>
			    </div>
		    </c:otherwise>
	    </c:choose>
	    <div class="foot_qt save-next" style="display: none">
            <a href="javascript:void(0);" id="saveNext">保存</a>  
        </div>
	</footer>
	<div class="dialog" id="yzmDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>请输入验证码</h3>
			<div class="yzm-pic">
				<img src="" id="codepic"/>
				<a href="javascript:void(0);" class="refreshCode"></a>
				<input type="number" name="yzmCode" id="yzmCode" maxlength="4" value="" />
			</div>
			<div class="close">
				<a href="javascript:void(0);" class="close-a left" id="cancel">取消</a>
				<a href="javascript:void(0);" class="close-a right" id="sure">确定</a>
			</div>
		</div>
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="mobile" value="${mobile }"/>
	<input type="hidden" id="projectId" value="${projectId }"/>
	<input type="hidden" id="money" value="${money }"/>
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/releaseProject/accountNumber.js?v=201712211600"></script>
</body>
</html>