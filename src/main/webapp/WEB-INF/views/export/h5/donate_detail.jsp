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
<title>捐赠详情</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/export/donateDetail.css"/>
</head>
<body>
	<header>
		<div class="dd-head">
			<a href="javascript:;" onclick="history.go(-1)" class="back"><img src="/res/images/h5/images/export/back.png"/></a>
			<span>捐赠详情</span>
		</div>
	</header>
	
	<div class="dd-body">
		<div class="dd-top">
			<a href="/export/view_h5.do?projectId=${project.id}&extensionPeople=${extensionPeople}">
				<div class="dd-project clearFix">
					<div class="dd-project-pic">
						<img src="${project.coverImageUrl }" />
					</div>
					<div class="dd-project-info">
						<div class="dd-project-title">
							<p>${project.title }</p>
						</div>
						<div class="dd-project-money">
							目标  : <span class="num">${project.cryMoney }</span>元
							<span class="line">|</span>已捐  :<span class="num">${project.donatAmount==null?0:project.donatAmount }</span>元
						</div>
					</div>
				</div>
			</a>
			<div class="dd-faqiren">
				<span class="left">发起人  : </span><span class="unit">${project.nickName }</span>
			</div>
			
			<div class="dd-receive">
				<span class="left">善款接收  : </span><span class="unit">宁波市善园公益基金会(是一家具有公开募捐资质的公益基金会)</span>
			</div>	
		</div>
		
		<div class="dd-mid">
			<div class="dd-donate-money">
				<span class="important">*</span>捐赠金额
			</div>
			<div class="dd-money">
				<ul class="dd-ul clearFix">
					<li class="on"><span>20</span>元</li>
					<li><span>50</span>元</li>
					<li><span>100</span>元</li>
					<li><span>200</span>元</li>
					<li><span>500</span>元</li>
					<li class="user-defined">自定义</li>
				</ul>
				<div class="diyMoney">
					<input type="number" name="dMoney" id="dMoney" value="" /><span>元</span>
				</div>
			</div>
		</div>
		
		<div class="dd-foot">
			<div class="dd-otherinfo">
				<p>其他信息(非必填)</p>
				<input type="text" id="realName" value="${user.realName }" placeholder="您的姓名" />
				<input type="text" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
				<input type="text" id="donateWord" value="" placeholder="${leaveWord }" />
			</div>
			
			<div class="dd-pay">
				<a href="javascript:;" class="payBtn">
					<div class="btn">确认支付</div>
				</a>
			</div>
			
			<div class="dd-prop">
				<input type="checkbox" name="chk" id="chk" checked="checked" />
				<label for="chk">我已阅读并同意</label><span class="c3cc">《捐赠协议》</span>
			</div>
		</div>
	</div>
	<div class="tips"></div>
	<input type="hidden" name="totalMoney" id="totalMoney" value="" />
	<input type="hidden" id="projectId" value="${project.id }" />
	<input type="hidden" id="userId" value="${user.id }" />
	<input type="hidden" id="yunhu_userId" value="${yunhu_userId }" />
	<input type="hidden" id="extensionPeople" value="${extensionPeople }" />
	<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value="${project.cryMoney-(project.donatAmount==null?0:project.donatAmount)}"/>">
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js" ></script>
	<script data-main="/res/js/h5/export/donateDetail.js" src="/res/js/require.min.js"></script>
</body>
</html>