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
<title>商品众筹</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/goods_crowdfunding.css?v=20171117">
<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head> 

<body>
<div class="header">
    <div class="head_fl"><a href="javascript:;" onclick="history.go(-1)" class="back">返回</a></div>
    <div class="head_fr"><p>请选择支持类型</p></div>
</div>
<div class="clear"></div>
<!--列表-->
<section>
	<c:forEach items="${projectPrize}" var="projectPrize">
		<c:choose>
			<c:when test="${(project.cryMoney-project.donatAmount) >= (projectPrize.score/100)}">
				<a href="<%=resource_url%>project/confirmCrowdfundingPrize.do?projectId=${project.id}
					&giftId=${projectPrize.id}&extensionPeople=${extensionPeople}"><div class="goods">
			        <div class="goods_one">
			            <span class="goods_fl fl">￥${projectPrize.score/100 }</span>
			            <span class="goods_fr fr">${projectPrize.supportNum }人支持</span>
			        </div>
				<div class="hengxian"></div>
			        <h3>${projectPrize.giftName }</h3>
			        <div class="goods_con">${projectPrize.content }</div>
			    	</div>
			    </a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0);"><div class="goods">
			        <div class="goods_one">
			            <span class="goods_fl fl">￥${projectPrize.score/100 }</span>
			            <span class="goods_fr fr">${projectPrize.supportNum }人支持</span>
			        </div>
				<div class="hengxian"></div>
			        <h3>${projectPrize.giftName }</h3>
			        <div class="goods_con">${projectPrize.content }</div>
			    	</div>
			    </a>
			</c:otherwise>
		</c:choose>
		
	</c:forEach>
</section>
<footer>
    <div class="goods">
        <p class="h4">无私支持</p>
       <div class="goods_two">
           <input type="text" placeholder="请输入金额" class="goods_twofl fl" id="money" onkeyup="this.value=this.value.replace(/[^0-9-.]+/,'');">
               <a href="javascript:void(0);" id="support"><p class="goods_twofr fr">无私支持</p></a>
       </div>
    </div>
</footer>
<input type="hidden" id="projectId" value="${project.id }">
<input type="hidden" id="extensionPeople" value="${extensionPeople}">
<input type="hidden" id="cryMoney" value="${project.cryMoney}">
<input type="hidden" id="donatAmount" value="${project.donatAmount}">
<!--弹框-->
<div class="goods_ts" style="display: none">
    <p>请输入支持金额</p>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$("#support").click(function(){
		var money = $("#money").val(),projectId = $("#projectId").val(),extensionPeople = $("#extensionPeople").val();
		if(money == ''){
			$(".goods_ts").show();
			setTimeout(function(){
				$(".goods_ts").hide();
			},2000);
		}else{
			var cryMoney = Number($('#cryMoney').val()),donatAmount=Number($('#donatAmount').val());
			if(Number((cryMoney-donatAmount).toFixed(2)) < money){
				money = (cryMoney-donatAmount).toFixed(2);
			}
			window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
		}
		
	});
});
</script>
</html>