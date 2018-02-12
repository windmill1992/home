<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ include file="./../../common/file_url.jsp"%>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>${project.title}</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/specialProject/planSet.css?v=20171201" />
</head>
<body>
	<div id="pageContainer">
		<div class="page-wrapper page-set">
			<div class="ps-cells-wrap chooseBox">
				<div class="ps-cell">
					<div class="ps-cell-m">捐助对象</div>
					<div>
						<a href="javascript:;" id="change_plan" class="cff9">换一换</a>
					</div>
				</div>
				<div class="ps-cell">
					<div class="ps-cell-m" id="plan_box"></div>
				</div>
			</div>
			<div class="set-item pt8">
				<div class="ps-cell set-cell">
					<div class="ps-cell-m">
						<div class="cell-label">捐助项目</div>
						<h3 class="f15" id="projTitle">${title }</h3></div>
				</div>
				<div class="ps-cell set-cell">
					<div class="ps-cell-m">
						<div class="cell-label">捐助金额</div>
						<div>
							<h3 class="cff9">
								<span id="plan_money"></span>元
								<span class="c999 f12" id="plan_aid_intro">${cause }</span>
							</h3>
						</div>
					</div>
				</div>
				<!--<div class="ps-cell ps-cell-switch">
					<div class="cell-label ps-cell-m">匿名捐赠</div>
					<div class="ps-switch-wp"><input class="ps-switch" type="checkbox" id="btn_anno"></div>
				</div>-->
			</div>
			<div class="ps-cell">
				<div class="ps-cell-m">
					<a href="javascript:;" class="ps-btn" id="submit_pay">确认资助</a>
				</div>
			</div>
			<div class="protocol txtC f13">
				<label class="ps-agreeBox" for="payProto">
					<input type="checkbox" name="" value="1" id="payProto" checked="">
					<i></i>同意
				</label>
				<a href="/redPackets/getAgreement.do">《善园基金会捐赠协议》</a>
			</div>
			<div class="ps-cell">
				<div class="ps-cell-m cb2b"> 注意事项：
					<ol class="attentionOl">
						<li>1：为保护受助人隐私，将加密展示受助人的姓名信息</li>
						<li>2：捐款支付会跳转网页，捐款成功后请返回善园公益填写您的收信地址，后续将不定期收到善款使用情况</li>
					</ol>
				</div>
			</div>
			<div class="ps-footLogo">
				<img src="/res/images/logo-lt.jpg">
			</div>
		</div>
	</div>
	
	<div class="bigPic">
		<div id="banner">
			<div class="hd"><ul></ul></div>
			<div class="bd">
				<ul></ul>
			</div>
		</div>
		<div class="num"></div>
	</div>
	
	<div id="overLay"></div>
	<div class="tips"></div>
	
	<input type="hidden" name="id" id="id" value="" />
	<input type="hidden" name="projectId" id="projectId" value="${projectId}" />
	<input type="hidden" name="extensionPeople" id="extensionPeople" value="${extensionPeople}" />
	<input type="hidden" name="userId" id="userId" value="${userId}" />
	<input type="hidden" name="browser" id="browser" value="${browser}" />

	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/common/TouchSlide.1.1.js"></script>
	<script data-main="/res/js/h5/specialProject/planSet.js?v=20171122" src="/res/js/require.min.js"></script>

</body>
</html>