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
<title>支付选择</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/library_bind.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css?v=2017">
<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head> 
<body>
<div class="feed">
    <div class="header">
        <a href="javascript:;" onclick="history.go(-1);" class="back">返回</a></div>
        <div>确认订单</div>
    </div>
</div>
<a href="javascript:;">
    <div class="donation_take">
        <span class="take_img1 fl"></span>
        <p class="take_text fl">善款接收：</p>
        <p class="take_text1 fr">宁波市善园公益基金会</p>
    </div>
</a>
<a href="javascript:;">
    <div class="donation_take donation_take2">
        <span class="take_img1 take_sp2 fl"></span>
        <p class="take_text fl">善款使用：</p>
        <p class="take_text1 fr">善款将根据求助项目实际发生的费用拨付给相关单位或求助者；实施救助后如善款尚有剩余，剩余部分并入基金会善库，用于其他求助者的求援；善款使用全程公示。</p>
    </div>
</a>
<a href="javascript:;">
    <div class="donation_take">
        <span class="take_img1 take_sp3 fl"></span>
        <p class="take_text fl">认捐主题：</p>
        <p class="take_text1 fr take_text2">${project.title }</p>
    </div>
</a>
<a href="javascript:;">
    <div class="donation_take">
        <span class="take_img1 take_sp4 fl"></span>
        <p class="take_text fl">认捐金额：</p>
        <p class="take_text1 fr">${from.amount}元</p>
    </div>
</a>
<div class="donation_money">
    <div class="donation_take1">
        <span class="take_img1 take_sp5 fl"></span>
        <p class="take_text">请选择支付方式：</p>
    </div>
    <c:forEach var="goodLibrary" items="${goodLibrary}" varStatus="status">
    	<c:if test="${goodLibrary.libraryBalance >= from.amount && goodLibrary.balance >= from.amount && goodLibrary.tag == true}">
        	<a href="javascript:;"><p class="money_qiye" v="${goodLibrary.libraryId}">${goodLibrary.nickName}(￥
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance == -1}"> ${goodLibrary.libraryBalance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance != -1}"> ${goodLibrary.balance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance< goodLibrary.balance}"> ${goodLibrary.libraryBalance}</c:if>
        	)</p></a>
        </c:if>
        <c:if test="${goodLibrary.balance >= from.amount && goodLibrary.tag ==false}">
        	<a href="javascript:;"><p class="money_qiye1" v="${goodLibrary.libraryId}">${goodLibrary.nickName}(￥
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance == -1}"> ${goodLibrary.libraryBalance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance != -1}"> ${goodLibrary.balance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance< goodLibrary.balance}"> ${goodLibrary.libraryBalance}</c:if>
        	)</p></a>
        </c:if>
        <c:if test="${goodLibrary.balance == -1 && goodLibrary.libraryBalance < from.amount}">
        	<a href="javascript:;"><p class="money_qiye1" v="${goodLibrary.libraryId}">${goodLibrary.nickName}(￥
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance == -1}"> ${goodLibrary.libraryBalance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance != -1}"> ${goodLibrary.balance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance< goodLibrary.balance}"> ${goodLibrary.libraryBalance}</c:if>
        	)</p></a>
        </c:if>
        <c:if test="${goodLibrary.balance != -1 && (goodLibrary.balance < from.amount ||goodLibrary.libraryBalance < from.amount)}">
        	<a href="javascript:;"><p class="money_qiye1" v="${goodLibrary.libraryId}">${goodLibrary.nickName}(￥
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance == -1}"> ${goodLibrary.libraryBalance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance>= goodLibrary.balance && goodLibrary.balance != -1}"> ${goodLibrary.balance}</c:if>
        	<c:if test="${goodLibrary.libraryBalance< goodLibrary.balance}"> ${goodLibrary.libraryBalance}</c:if>
        	)</p></a>
        </c:if>
        <c:if test="${goodLibrary.libraryBalance >= from.amount && goodLibrary.balance == -1 && goodLibrary.tag == true}">
        	<a href="javascript:;"><p class="money_qiye1" v="${goodLibrary.libraryId}">${goodLibrary.nickName}(￥${goodLibrary.libraryBalance})</p></a>
        </c:if>    		
    </c:forEach>
</div>
<input type="hidden" id="projectId" value="${from.projectId }">
<input type="hidden" id="extensionPeople" value="${from.extensionPeople}">
<input type="hidden" id="amount" value="${from.amount}">
<input type="hidden" id="userId" value="${userId}">
<input type="hidden" id="balance" value="${user.balance}">
<input type="hidden" id="donateWord" value="${from.donateWord}">
</body>
<script type="text/javascript">
$(function(){
	var money = $("#amount").val(),
		projectId = $("#projectId").val(),
		balance=$("#balance").val(),
		extensionPeople = $("#extensionPeople").val(),
		userId = $("#userId").val(),
		donateWord=$("#donateWord").val();
	
	//余额支付
	$("#freezePay").click(function(){
		if(money > balance){
			alert("您的余额不足！请先充值");
			return false;
		}
		if(userId == null || userId ==''){
			return false;
		}
		location.href="http://www.17xs.org/alipay/freezeDeposit.do?projectId="
			+projectId+"&sumMoney="+money+"&extensionPeople="+extensionPeople+"&donateWord="+donateWord;
		
	});
	//善库支付 
	$(".money_qiye1").each(function(){
    	$(this).click(function(){
    		var goodLibraryId = $(this).attr("v");
            location.href="http://www.17xs.org/WapAlipay/goodLibraryPay.do?projectId="
				+projectId+"&sumMoney="+money+"&extensionPeople="+extensionPeople+"&goodLibraryId="+goodLibraryId+"&donateWord="+donateWord;
        });
    });
	//直接支付
	$("#support").click(function(){
		location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
			+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+donateWord;
	});
});
</script>
</html>
