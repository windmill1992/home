<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" /> 
<title>${project.title }</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/together/raising.css?v=20180124"/>
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<div class="rd-head">
				<div class="head-pic">
					<img src="${project.coverImageUrl }" width="100%"/>
				</div>
				<div class="head-content">
					<img src="${togetherConfig.coverImageUrl }" class="user-pic"/><br />
					<p style="font-size:1.8rem"><b>${togetherConfig.launchName }</b></p>
					<p>发起筹款，邀请您一起行善</p>
				</div>
				<div class="head-txt">
					<p>${togetherConfig.content }</p>
				</div>
				<div class="head-donation">
					<p>已筹<span class="raised">${togetherConfig.donateMoney }</span>元  /
					<c:choose>
					<c:when test="${togetherConfig.totalMoney>-1 }">目标  <span class="raised-dest">${togetherConfig.totalMoney }</span>元</c:when>
					<c:otherwise>目标不限</c:otherwise>
					</c:choose></p>
				</div>
				
				<div class="rd-progress raise-progress">
					<div class="progress-bar">
						<span class="track">
							<i class="fill"></i>
						</span>
					</div>
				</div>
				<div class="kong"></div>
				
				<div class="rd-project">
					<p class="project-title" style="font-size:1.8rem">${project.title }</p>
					<div class="content">
						<div class="project-content" style="font-size:1.5rem">
							${project.content }
						</div>
						<%--项目详情图片--%>
						<c:forEach items="${project.bfileList}" var="img">
							<div class="xqImg">
								<c:if test="${img.fileType !='video'}">
									<img src="${img.url}" width="100%" alt="">
									<div class="imgText">${img.description }</div>
								</c:if>
								<c:if test="${img.fileType =='video'}">
									<iframe src="${img.description}" allowfullscreen=""
										frameborder="0" width="100%" height="100%"> </iframe>
								</c:if>
							</div>
						</c:forEach>
					</div>
					
					<div class="detail-mask detail-mask-bg">
						<a href="javascript:;" class="lookmore">查看全文</a>
					</div>
				</div>
				<div class="kong"></div>
				
				<div class="rd-record">
					<div class="record-hd">
						<p class="fl">一起捐记录</p>
						<p class="fr">已有${togetherConfig.donateNum }位朋友参与</p>
					</div>
					<div class="record-list-box"></div>
					
					<div class="load-more">
						<a href="javascript:;" class="aload">点击加载更多</a>
					</div>
				</div>
				
				<div class="rd-btn">
					<a href="javascript:;" class="btn donate">我要捐款</a>
				</div>
			</div>
		</div>
		
		<div class="rd-donate">
			<div class="dd-donate-money">
				捐赠金额<a href="javascript:;" class="close fr"></a>
			</div>
			<div class="dd-money">
				<ul class="dd-ul clearFix">
					<c:choose>
						<c:when test="${!empty moneyConfigs}">
							<c:forEach items="${moneyConfigs }" var="conf" varStatus="money">
								<c:choose>
									<c:when test="${money.first }">
										<li class="on"><span>${conf.money }</span>元</li>
									</c:when>
									<c:otherwise>
										<li><span>${conf.money }</span>元</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<li class="on"><span>10</span>元</li>
							<li><span>20</span>元</li>
							<li><span>50</span>元</li>
							<li><span>100</span>元</li>
							<li><span>500</span>元</li>
						</c:otherwise>
					</c:choose>
					
					<li class="user-defined">自定义</li>
				</ul>
				<div class="diyMoney">
					<input type="text" name="dMoney" id="dMoney" value="" readonly="readonly" />
				</div>
			</div>
			
			<div class="dd-otherinfo">
				<p>其他信息(非必填)</p>
				<input type="text" name="" id="realName" value="${user.realName }" placeholder="您的姓名" />
				<input type="text" name="" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
				<input type="text" name="" id="donateWord" value="" placeholder="${leaveWord }" />
			</div>
			
			<div class="dd-pay">
				<a href="javascript:;" class="payBtn">
					<div class="btn">立即捐赠</div>
				</a>
			</div>
			
			<div class="dd-prop">
				<input type="checkbox" name="chk" id="chk" checked="checked" />
				<label for="chk">我已阅读并同意<a class="prop" href="/redPackets/getAgreement.do">《捐赠协议》</a></label>
			</div>
		</div>
		<!--回复留言-->
		<div class="moved_up" style="display: none" >
			<div class="back"></div>
			<div class="end_center">
				<div class="end_hfk">
					<span class="end_r">发表评论：</span>
					<textarea class="end_text" id="content" placeholder=""></textarea>
				</div>
				<div class="end_foot">
					<a href="javascript:void(0);">
						<div class="end_fl1 fl" id="leaveWordSubmit">确定</div>
					</a> <a href="javascript:void(0);">
						<div class="end_fl1 fr">取消</div>
					</a>
				</div>
			</div>
		</div>
		
		<div class="tishi">点击右上角，邀请朋友一起参加公益<img src="/res/images/h5/images/point.gif" /></div>
		<div class="tishi2">
			<div class="back"></div>
			<div class="success">
				<div class="success-pic">
					<img src="/res/images/h5/images/together/success.png"/>
				</div>
				<h2>一起捐发起成功</h2>
				<p>您可以点此捐出第一笔</p>
				<a href="javascript:;" class="close">
					<img src="/res/images/h5/images/icon_close.png"/>
				</a>
			</div>
		</div>	
		<div id="overLay"></div>
	</div>
	
	<input type="hidden" name="raised-per" id="raised-per" value="${process==''?0:process }%" />
	<input type="hidden" id="projectId" value="${project.id }"/>
	<input type="hidden" id="userId" value="${userId }"/>
	<input type="hidden" id="shareUserId" value="${shareUserId }"/>
	<input type="hidden" id="show" value="${show }"/>
	<input type="hidden" id="donateShow" value="${donateShow }"/>
	<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
	<input id="projectDonateId" type="hidden">
	<input id="projectFeedbackId" type="hidden">
	<input id="leavewordUserId" type="hidden">
	<input id="replyUserId" type="hidden">
	<input id="leavewordName" type="hidden">
	<input id="index" type="hidden">
	<input id="replyName" type="hidden">
	<input id="donateWord" type="hidden">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js" ></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript" src="/res/layer/layer.js" ></script>
	<script type="text/javascript">
		wx.config({
			debug : false,
			appId : '${appId}',
			timestamp : '${timestamp}',
			nonceStr : '${noncestr}',
			signature : '${signature}',
			jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','previewImage' ]
		});
	
		wx.ready(function(){
			wx.onMenuShareAppMessage({
				title : '${togetherConfig.launchName}邀请您一起捐', // 分享标题
				desc : '${togetherConfig.content}', // 分享描述
				link : 'http://www.17xs.org/together/raiseDetail_view.do?projectId=${project.id}&shareUserId=${shareUserId==null?userId:shareUserId}', // 分享链接
				imgUrl : '${togetherConfig.coverImageUrl}', // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					//用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/together/raiseDetail_view.do?projectId=${project.id}&shareUserId=${shareUserId==null?userId:shareUserId}&donateShow=1';
				},
				cancel : function() {
					//用户取消分享后执行的回调函数
				}
			});
					
			wx.onMenuShareTimeline({
				title : '${togetherConfig.launchName}邀请您一起捐', // 分享标题
				link : 'http://www.17xs.org/together/raiseDetail_view.do?projectId=${project.id}&shareUserId=${shareUserId==null?userId:shareUserId}', // 分享链接
				imgUrl : '${project.coverImageUrl}', // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/together/raiseDetail_view.do?projectId=${project.id}&shareUserId=${shareUserId==null?userId:shareUserId}&donateShow=1';
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
		
		if($('#show').val()=='1'){
			$('#overLay').show();
			$('.tishi').show();
		}
		if($('#donateShow').val()=='1'){
			$('.tishi2').show();
		}
		var loginUrl = "/ucenter/user/Login_H5.do?flag=raiseDetail_${project.id}";
	</script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/together/raising.js?v=20180124" src="/res/js/require.min.js"></script>
</body>
</html>