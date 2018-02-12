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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/goods_crowdfunding.css">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	function divOnclick(){
		var projectId = $("#projectId").val(),giftId = $("#giftId").val();
		var addreId= $("#addressId").val();
		if(addreId==""){
			window.location.href='http://www.17xs.org/user/toAddress.do?type='+projectId+'_'+giftId+'&pgId='+projectId+'_'+giftId+'&id='+addreId;
		}
		else{
			window.location.href='http://www.17xs.org/user/toAddress.do?type=6&pgId='+projectId+'_'+giftId+'&id='+addreId;
		}
	}
</script>
</head> 

<body>
<div class="header">
    <div class="head_fl"><a href="javascript:void(0);" onclick="history.go(-1)" class="back">返回</a></div>
    <div class="head_fr"><p>订单提交</p></div>
</div>
<div class="clear"></div>
<section>
    <div class="goods">
        <p class="goods_h4">${project.title }</p>
        <div class="hengxian"></div>
        <div class="order_one" onclick="divOnclick()">
                <p class="oreder_one1">收件人：<span class="order_sp1" id="name">${address.name }</span></p>
                <p class="oreder_one2">电&nbsp;&nbsp;&nbsp;话：<span class="order_sp2" id="mobile">${address.mobile }</span></p>
                <p class="oreder_one3">地&nbsp;&nbsp;&nbsp;址：<span class="order_sp3" id="address">${address.province }${address.city }${address.area }${address.detailAddress }</span></p>
        </div>
    </div>
    <div class="goods">
        <p class="goods_h5">支持金额：<span class="order_sp4">￥${gift.score/100 }</span></p>
        <div class="hengxian"></div>
        <div class="order_one">
            <p class="order_one4">${gift.giftName }</p>
            <p class="order_one5">${gift.content }</p>
        </div>
    </div>
</section>
<div class="order_foot"><p>提交订单</p></div>
<input type="hidden" id="projectId" value="${project.id }">
<input type="hidden" id="giftId" value="${gift.id }">
<input type="hidden" id="addressId" value="${address.id }">
<input type="hidden" id="money" value="${gift.score/100 }">
<input type="hidden" id="extensionPeople" value="${extensionPeople}">
<!--弹框提示-->
<div class="prompt_box" style="display: none" id="prompt">
    <div class="cue_back"></div>
    <div class="cue1">
        <div class="cue_center">
            <div class="cue_center1">
                <p class="cue_p1">您还未填写收件人信息 </p>
                <p class="cue_p3">为了确保您能准确收到物品，请您完善收件人信息~</p>
            </div>
            <div class="cue_center2">
                <a href="javascript:void(0);" class="ui-link"><div class="cue_fl"><p class="cue_pl">完善信息</p></div></a>
                <a href="javascript:void(0);" class="ui-link"><div class="cue_fr"><p class="cue_pr">现场领取</p></div></a>
            </div>
        </div>
    </div>
</div>
</body>
<script data-main="<%=resource_url%>res/js/h5/confirm_crowdfunding.js?v=20170419" src="<%=resource_url%>res/js/require.min.js"></script>
</html>