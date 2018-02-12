<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<!--<%@page pageEncoding="UTF-8"%>-->
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ include file="./../../common/file_url.jsp"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
    <!--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">-->
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体" />
	<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人" />
    <title>${project.title}</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/project/bazaar.css?v=20171130" />
</head>
<body>
	<div id="pageContainer">
		<div class="head">
			<a href="javascript:;" onclick="history.go(-1);" class="back"></a>
			<span>捐赠详情</span>
		</div>
		<div class="dm-hd">
			<a href="/project/view_h5.do?projectId=${project.id}">
				<div class="project">
					<div class="pic">
						<img src="${project.coverImageUrl}" />
					</div>
					<div class="info">
						<div class="title">
							<p>${project.title}</p>
						</div>
						<div class="support">
							支持人数：<span class="num">${project.donationNum}</span>
						</div>
						<div class="support">
							支持物品：<span class="num">${nums}</span>&nbsp;份
						</div>
					</div>
				</div>
			</a>
			<div class="other">
				<span class="left">发起人 : </span><span class="unit">${fabu.workUnit}</span>
			</div>
			<div class="other">
				<span class="left">善款接收 : </span>
				<span class="unit">宁波市善园公益基金会
					<a href="/resposibilityReport/resposibilityReport.do?id=368">
						<img src="/res/images/h5/images/donateStep/wen.png"/>
					</a>
				</span>
			</div>	
		</div>
		
		<div class="dm-bd">
			<h3>请选择捐赠物资</h3>
			<div class="materials" id="materialList">
				<c:forEach items="${gifts}" var="gift">
					<div class="item" data-id="${gift.id}">
						<div class="pic">
							<a href="javascript:;" class="preview">
								<img src="${gift.coverImageUrl!=null?gift.coverImageUrl:'/res/images/h5/images/gift.png'}"/>
							</a>
						</div>
						<div class="info">
							<p class="title">${gift.giftName}</p>
							<p class="size">${gift.content}</p>
							<p class="price">
								<span class="p-num">${gift.buyPrice}</span>元 
								<span class="through">${gift.orginPrice}元</span>
							</p>
						</div>
						<div class="operate">
							<a href="javascript:;" class="minus">
								<img src="/res/images/h5/images/minus.png"/>
							</a>
							<span class="num">0</span>
							<a href="javascript:;" class="add">
								<img src="/res/images/h5/images/plus.png"/>
							</a>
						</div>
					</div>	
				</c:forEach>
				
			</div>
			<div class="other-op" id="otherOp">
				<a href="javascript:;" class="donate-num" data-num="1">每样捐1份</a>
				<a href="javascript:;" class="donate-num" data-num="2">每样捐2份</a>
				<a href="javascript:;" class="random">随机捐1份</a>
				<a href="javascript:;" class="other-money">自定义金额</a>
			</div>
			<div class="diy-money">
				<input type="number" id="dMoney" />
			</div>
		</div>
		
		<div class="dm-ft">
			<div class="user-info">
				<input type="text" id="realName" placeholder="您的姓名" value="${realName}" />
				<input type="text" id="mobile" placeholder="您的电话" value="${mobile}" />
				<textarea id="leaveWord" rows="3" placeholder="${leaveWord}"></textarea>
			</div>
			<p class="money">支持金额：<span id="supportMoney">0.00</span> 元</p>
			<div class="btn disable">
				<a href="javascript:;" id="support">
					<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要支持
				</a>
			</div>
		</div>
		
		<div id="tips"></div>
		<div id="bigImg">
			<div class="bd"></div>
			<div class="intro"></div>
		</div>
		
	</div>
	<div id="nopage">
		<div class="head">
			<a href="javascript:;" onclick="history.go(-1);" class="back"></a>
			<span>捐赠详情</span>
		</div>
		<p>项目不存在！</p>
	</div>
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/res/js/h5/project/bazaar.js"></script>
</body>
</html>