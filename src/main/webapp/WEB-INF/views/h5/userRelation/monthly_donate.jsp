<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园网 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css?v=2017">
<link rel="stylesheet" type="text/css" href="/res/css/h5/mon_donation.css?v=20171129">
<script type="text/javascript">
  document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body>
	<div class="header">
	    <a href="javascript:;" onclick="history.go(-1);" class="back">返回</a>
	    <div>月捐计划</div>
	</div>
	<div class="clear"></div>
	<section>
	    <div class="mon_1">
	        <div class="mon_t fl"></div>
	        <p>请选择捐助类型</p>
	    </div>
	    <div class="clear"></div>
	    <div class="mon_2">
	        <div class="mon_fl mon_zsys fl " id="mon_mon">
	            <a href="javascript:;" class="mon_button">
	            	<p class="p1">按月捐</p>
	            </a>
	        </div>
	        <div class="mon_fr mon_zsys1 fr" id="mon_day">
	          <a href="javascript:;" class="mon_button">
	            	<p class="p2">按天捐</p>
            </a>
	        </div>
	    </div>
	    <div class="clear"></div>
	    <div class="mon_1">
	        <div class="mon_t fl"></div>
	        <p>请选择项目类型</p>
	    </div>
	    <div class="clear"></div>
	    <div class="mon_2">
	        <div class="mon_fl mon_zsys fl " id="mon_mon1">
	            <a href="javascript:;" class="mon_button">
	           		<p class="p1">任意捐10个项目</p>
	            </a>
	        </div>
	        <div class="mon_fr mon_zsys1 fr" id="mon_day1">
	            <a href="javascript:;" class="mon_button ">
	            	<p class="p2">指定项目类型</p>
	            </a>
	        </div>
	    </div>
	    <div class="clear"></div>
	
	<div class="mon_conment">
	    <div class="mon_bei">
	        <img src="/res/images/h5/images/list1.png">
	    </div>
	    <div class="mon_bei1">
	    <p>任意捐10个项目</p>
	    </div>
	    <ul class="mon_list flex">
	    	<c:forEach var="record" items="${atc}" varStatus="status">
				<c:if test="${record.typeName_e != 'good'}">
					<li><a v="${record.typeName_e}" href="javascript:;" id="list_zsys" class="list_zsys" >${record.typeName}</a></li>
				</c:if>
			</c:forEach>
	    </ul>
	</div>
	    <div class="clear"></div>
	    <div class="mon_1">
	        <div class="mon_t fl"></div>
	        <p>请选择通知类型</p>
	    </div>
	    <div class="clear"></div>
	    <div class="mon_2">
	        <div class="mon_fl mon_zsys1 fl" id="mon_mon2">
	            <p class="p1"><a href="javascript:;">微信通知</a></p>
	        </div>
	        <div class="mon_fr mon_zsys fr" id="mon_day2">
	            <p class="p2"><a href="javascript:;">短信通知</a></p>
	        </div>
	    </div>
	    <div class="clear"></div>
	</section>
	<footer>
	    <div class="foot_sh1">
	        <div class="foot_1 flex spb fcen">
	            <p class="foot_p">请输入手机号:</p>
	            <c:if test="${user.mobileNum == null || user.mobileNum == ''}">
	            <input type="text" class="input_s1" placeholder="请输入电话号码" id="mobileNum" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" />
	            </c:if>
	            <c:if test="${user.mobileNum != null && user.mobileNum != ''}">
	            <input type="text" class="input_s1" placeholder="${user.mobileNum }" id="mobileNum" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" />
	            </c:if>
	        </div>
	    </div>
	    <div class="foot_1 flex spb fcen">
	        <p class="foot_p">每次捐助的金额:</p>
	        <input type="text" class="input_s" placeholder="" id="money" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" />
	        <p class="foot_p2">元</p>
	    </div>
	    <div class="foot_3">
	        <p class="foot_p3">注：系统会从您的善园账户中扣除资金，捐赠给求助项目，当您的 善园账户余额不足每次捐助时，自动停止月捐，续费后再次开通。</p>
	    </div>
	    <a href="javascript:;">
	    	<div class="foot_4">
	        <p class="foot_p4">开通月捐计划</p>
	   		</div>
	    </a>
	    <div class="foot_5">
	        <input type="checkbox" checked="true" id="checkbox"/>
	        <label for="checkbox">已阅读并同意</label><a href="">《善园月捐计划》</a>
	    </div>
	</footer>
	<!--善款充值提示-->
    <div class="cue_f">
	    <div class="cue_background"></div>
			<div class="cue">
	    	<div class="cue_center">
	        <div class="cue_center1">
	            <p class="cue_p1">善款充值</p>
	            <p class="cue_p2">请输入充值金额：<input type="text" placeholder="" id="chargeMomeny" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">元</p>
	            <p class="cue_p3">充值的善款只能用于捐赠，不可以提现或者购物</p>
	        </div>
	        <div class="cue_center2">
	            <a href="javascript:void(0);"><div class="cue_fl" id="confirCharge"><p class="cue_pl">确认充值</p></div></a>
	            <a href="javascript:void(0);"><div class="cue_fr" id="concelCharge"><p class="cue_pr">取消</p></div></a>
	        </div>
	    	</div>
			</div>
    </div>
	    <!--确认开通月捐计划提示-->
    <div class="cue_f1">
	    <div class="cue_background"></div>
			<div class="cue1">
	    	<div class="cue_center">
	        <div class="cue_center1">
	            <p class="cue_p1">善款确认</p>
	            <p class="cue_p3">您确认要开通月捐计划吗？</p>
	        </div>
	        <div class="cue_center2">
	            <a href="javascript:void(0);"><div class="cue_fl" id="confirmDonate"><p class="cue_pl">确认</p></div></a>
	            <a href="javascript:void(0);"><div class="cue_fr" id="concelDonate"><p class="cue_pr">取消</p></div></a>
	        </div>
	    	</div>
			</div>
    </div>
	<span id="userName" style="display:none">${user.nickName }</span>
	<span id="mobile" style="display:none">${user.mobileNum }</span>
	<%--勾选提示--%>
	<div class="cue2" style="display:none" id="agreeRule">
		<p>您还没有同意善园月捐计划协议</p>
	</div>
	<div class="cue3" style="display:none" id="agreeRule1" >
    <p id="prompt_info">请输入金额</p>
	</div>
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/mon_donation.js?v=20171129"></script>
</body>
</html>