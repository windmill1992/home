<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园网 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css?v=20160317">
<link rel="stylesheet" type="text/css" href="/res/css/h5/mon_donation.css?v=20171129">
<link rel="stylesheet" type="text/css" href="/res/css/h5/prompt_box.css?v=20171129">
<link rel="stylesheet" type="text/css" href="/res/css/h5/monthly_list.css?v=20171129">
<script type="text/javascript">
  document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
</head>
<body>
	<div class="postul_top flex">
	    <a href="javascript:void(0);" onclick="history.go(-1);" class="back">返回</a>
	    <p>我的日捐/月捐</p>
	    <a href="/user/getDonateTime.do" class="btn-a">开通月捐</a>
	</div>
	<!--列表内容-->
	<section>
    <ul class="mainer">
        <!-- 数据 -->
    </ul>
    <div class="click_duo">
    	<a href="javascript:void(0);" id="loadmore">点击加载更多</a> 
    </div>
	</section>
	<!--弹框提示-->
	<div class="prompt_box" style="display: none">
	    <div class="cue_back"></div>
	    <div class="cue1">
	        <div class="cue_center">
	            <div class="cue_center1">
	                <p class="cue_p1" id="prompt_title">月捐计划 </p>
	                <p class="cue_p3" id="prompt_content">您已开通月捐计划，是否需要关闭~</p>
	            </div>
	            <div style="display: none">
	            	<span id="type"></span><span id="category"></span><span id="notice"></span>
	            	<span id="mobileNum"></span><span id="money"></span><span id="dtId"></span><span id="month_id"></span>
	            </div>
	            <div class="cue_center2">
	                <a href="javascript:void(0);" class="ui-link"><div class="cue_fl"><p class="cue_pl" id="promptConfirm">关闭月捐</p></div></a>
	                <a href="javascript:void(0);" class="ui-link"><div class="cue_fr"><p class="cue_pr" id="promptConcel">不关闭</p></div></a>
	            </div>
	        </div>
	    </div>
	</div>
	<!--善款充值提示-->
	  <div class="cue_f" id="charge_prompt" style="display: none">
	  	<div class="cue_background"></div>
		<div class="cue">
		    <div class="cue_center">
		        <div class="cue_center1">
		            <p class="cue_p1">善款充值</p>
		            <p class="cue_p2">请输入充值金额：<input type="text" placeholder="请输入充值金额" id="chargeMomeny" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">元</p>
		            <p class="cue_p3">充值的善款只能用于捐赠，不可以提现或者购物</p>
		        </div>
		        <div class="cue_center2">
		            <a href="javascript:void(0);"><div class="cue_fl" id="confirCharge"><p class="cue_pl">确认充值</p></div></a>
		            <a href="javascript:void(0);"><div class="cue_fr" id="concelCharge"><p class="cue_pr">取消</p></div></a>
		        </div>
		    </div>
		</div>
	  </div>
	<div style="display:none">
	 <span id="page"></span>
	 <span id="sum"></span>
	</div >
	<input type="hidden" id="userId" value="${user.id }">
	<input type="hidden" id="balance" value="${user.balance }">
	<span id="userName" style="display:none">${user.nickName }</span>
	<span id="mobile" style="display:none">${user.mobileNum }</span>
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/my_mon_donation.js?v=20171129"></script>
</body>
</html>