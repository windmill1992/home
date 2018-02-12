<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园网 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/util/city.css"/>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/base.css?v=201512111">
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/donate.css?v=201512112">
	<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/dialog.css" />
	<script src="../../../../res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script src="../../../../res/js/h5/sharePay_success.js?v=20151212" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body>
	<div class="container" style="background-color: #f4f3f0;">
		<div class="jkTopBox">
			<c:choose>
				<c:when test="${headImage == null || headImage == ''}">
				<a href="http://www.17xs.org/ucenter/userCenter_h5.do"><img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" class="topUserImg"></a>
				</c:when>
				<c:otherwise>
					<img src="${headImage }" class="topUserImg">
				</c:otherwise>
			</c:choose>
			<p class="topUserName">感谢${nickName}</p>
			<c:choose>
				<c:when test="${isMatchDonate == 1}">
					<p class="topUserInfo">成功捐款了${amount}元<br>“${matchDonate.companyname}”为该项目配捐${matchDonate.money}元</p>
				</c:when>
				<c:otherwise>
					<p class="topUserInfo">为“${title }”捐款了${amount }元</p>
				</c:otherwise>
			</c:choose>
			<img src="../../../../res/images/h5/images/thanks.png" class="thankPic">
		</div>
		<div class="jkMainBox">
			<ul class="jkOptionList">
				<li class="jzjl"><a href="javascript:void(0);" id="seeRecord">查看捐赠记录</a></li>
				<li class="lqlp"><a href="javascript:void(0);" id="sq_gift">捐赠证书</a></li>
				<li class="sqfp"><a href="javascript:void(0);" id="sq_bill">索取发票</a></li>
				<li class="jzqt"><a href="javascript:void(0);" id="donateProject">捐助其他项目</a></li>
			</ul>
			<div class="otherDonateBox">
				<h3 class="otherTitle">还有<span id="nums"></span>位小伙伴一起捐款了</h3>
				<ul class="otherDonateList">
                    <div class="mainer"></div>				
					<div class="loadMore">
					    <div style="display:none">
						 <span id="page"></span>
						 <span id="pageNum"></span>
						 <span id="total"></span>
						</div >
						<div class="more">
						   <a href="javascript:;" class="loadMoreA" id="loadMoreA">查看更多捐款</a>
					     </div>
						<div class="no" style="display:none">
						<span class="loadMoreA" >没有更多捐款</span></div>
					</div>
				</ul>
			</div>
		 
		</div>
	</div>
	<div class="donateBtn clearfix">
		<%-- <a href="http://www.17xs.org/project/view_share_h5.do?projectId=${projectId}&shareType=0" class="left-btn fl">邀请朋友一起参与</a> --%>
		<%-- <a href="http://www.17xs.org/project/togetherDonate_view.do?projectId=${projectId }" class="left-btn fl">邀请朋友一起参与</a> --%>
		<a href="http://www.17xs.org/together/together_view.do?projectId=${projectId }" class="left-btn fl">邀请朋友一起参与</a>
		<%--<a href="http://www.17xs.org/project/toSendWish.do?tradeNo=${tradeNo}&headImage=${headImage}&nickName=${nickName}&amount=${amount}&title=${title}&projectId=${projectId}" class="right-btn fr">发送祝福</a>--%>
		<c:choose>
			<c:when test="${fn:contains(slogans,'together_') }">
				<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId=${projectId }&shareUserId=${fn:replace(slogans,'together_','')}" class="right-btn fr">返回项目</a>
			</c:when>
			<c:otherwise>
				<a href="http://www.17xs.org/project/view_h5.do?projectId=${projectId}" class="right-btn fr">返回项目</a>
			</c:otherwise>
		</c:choose>
	</div>
	
	<!-- 红包 -->
	<input type="hidden" id="red" value="${red }">
	<input type="hidden" id="flag" value="${flag }">
	<input type="hidden" id="result" value="${result }">
	<div class="popup" style="display: block;"></div>
	<div class="activityInfo" style="display: none" >
		<span class="close" id="closeBtn"></span>
		<a href="javascript:;" class="getBtn" id="goShareBtn">领红包，看土豪</a>
	</div>
	<!-- 分享弹出框 -->
	<div class="payTips" id="notiesInfo" style="display: none">
		<div class="investTip">
			点分享，撒红包，让更多小伙伴一起参与公益<span class="point"></span>
		</div>
	</div>
<%--配捐弹框--%>
<c:choose>
<c:when test="${isMatchDonate == 1}">
	<div class="payTogter">
		<div class="togter_center">
			<div class="togterCha">
				<img src="../../../../res/images/h5/images/cha2.png" />
			</div>
			<div style="clear:both"></div>
			<div class="togterImg">
				<li><img src="${companyHeadImg==null||companyHeadImg=='http://www.17xs.org/res/images/detail/people_avatar.jpg'?'':companyHeadImg }" class="togImg1"></li>
				<li><img src="${headImage==null||headImage=='http://www.17xs.org/res/images/detail/people_avatar.jpg'?'':headImage }" class="togImg2"></li>
			</div>
			<div style="clear:both"></div>
			<p class="togter_p1">${matchDonate.companyname}出资捐助该项目</p>
			<p class="togter_p2">因为您的善举企业同时捐赠了<span>${matchDonate.money}</span>元</p>
			<%-- <a href="http://www.17xs.org/project/view_share_h5.do?projectId=${projectId}&shareType=0" class="togterBtn">邀请朋友一起捐</a> --%>
			<%-- <a href="http://www.17xs.org/project/togetherDonate_view.do?projectId=${projectId }" class="togterBtn">邀请朋友一起捐</a> --%>
			<a href="http://www.17xs.org/together/together_view.do?projectId=${projectId }" class="togterBtn">邀请朋友一起捐</a>
			<p class="togter_p3">企业剩余配捐金额<span>${matchDonate.leaveamount }</span>元</p>
			<p class="togter_p4">您出多少企业捐多少，马上邀请朋友一起行善</p>
		</div>
	</div>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>

	<div class="paySuccess-dialog">
		<div class="pd-hd">
			<div class="thanks">
				<img src="../../../../res/images/h5/images/heart.png"/>
				<span>感谢您的善举~</span>
			</div>
			<div class="text">
				我们希望您能留下联系方式，以便向您汇报资助对象的后续情况，以下信息不会对外公开~
			</div>
			<a class="pd-close" href="javascript:;">&times;</a>
		</div>
		<div class="pd-bd">
			<ul class="pd-bd-ul">
				<li>
					<span>您的姓名</span>
					<input type="text" name="name" id="name" value="" />
				</li>
				<li>
					<span>您的电话</span>
					<input type="number" name="tel" id="tel" value="" />
				</li>
				<li>
					<span>所在地区</span>
					<input type="text" name="addr" id="addr" value="" placeholder="点击选择省市区" />
					<div id="addrContainer"></div>
				</li>
				<li>
					<span>详细地址</span>
					<input type="text" name="detailAddr" id="detailAddr" value="" />
				</li>
			</ul>
		</div>
		<div class="pd-ft">
			<div class="submitBtn">
				<a href="javascript:;" class="submit">提交</a>
			</div>
		</div>
	</div>
	
	<div class="dialog" id="cardDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<div class="reverse">
				<div id="txtTip">
					<p class="txt">浙江工业大学之江学院欢迎您~</p>
					<p class="txt">点击翻转卡片</p>	
				</div>
				<a href="javascript:void(0);">
				<div class="card">
					<img src="/res/images/h5/images/cardlogo.jpg"/>
					<p>有惊喜哦~</p>
				</div></a>
				<div class="gift"></div>
				
			</div>	
			
			<a href="javascript:void(0);" class="close"></a>
		</div>
		
	</div>
	<div id="overLay"></div>
	<div class="tips"></div>

<script type="text/javascript" src="../../../../res/js/common/city.js"></script>
<script type="text/javascript" src="../../../../res/js/common/city2.min.js"></script>

<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	var state='http://www.17xs.org/project/view_h5.do?projectId=${projectId}';
	var slogans='${slogans}';
	if(slogans.indexOf('together_')>=0){
		var userId=slogans.replace('together_','');
		state='http://www.17xs.org/together/raiseDetail_view.do?projectId=${projectId }&shareUserId='+userId;
	}
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline' ]
	});

	wx.ready(function(){
		wx.onMenuShareAppMessage({
					title : '${title}', // 分享标题
					desc : '${subTitle}', // 分享描述
					link : state, // 分享链接
					imgUrl : '${coverImageUrl}', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
						//window.location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '${title}', // 分享标题
			link : state, // 分享链接
			imgUrl : '${coverImageUrl}', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				//window.location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}';
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	});
	
		/* wx.onMenuShareAppMessage({
					title : '${nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
					desc : '点击领取礼包，开启行善之路', // 分享描述
					link : 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}', // 分享链接
					imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
						window.location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '${nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
			link : 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}', // 分享链接
			imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				window.location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${extensionPeople}&tradeNo=${tradeNo}&projectId=${projectId}';
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	}); */
		});
</script>
<script type="text/javascript">
	$(function(){
		var red = $("#red").val();
		var flag = $("#flag").val();
		var result = $("#result").val();
		if(red == 1 || flag =='false'||result == 'false'){
			$(".activityInfo").hide();
			$(".popup").hide();
		}else{
			// 红包弹出框关闭按钮
			$("#closeBtn").click(function(){
				$(".popup").hide();
				$(this).parent().hide();
			});

			// 红包弹出框立即分享按钮
			$("#goShareBtn").click(function(){
				$("#notiesInfo").show();
				$(".activityInfo").hide();
			});
		}
		new MultiPicker({
		 	input: 'addr',//点击触发插件的input框的id
	        container: 'addrContainer',//插件插入的容器id
	        jsonData: $city,
	        success: function (arr) {
				if(arr.length==3){
					$("#addr").val(arr[0].value +"-"+arr[1].value+"-"+arr[2].value);
				}else{$("#addr").val(arr[0].value +"-"+arr[1].value);}
				
	        }//回调
	    });
		
	});
</script>
<input type="hidden" value = "${projectId}"  id = "projectId">
<input type="hidden" id="ePeople" value="${ePeople }">
<input type="hidden" id="show" value="${show }">
</body>
</html>