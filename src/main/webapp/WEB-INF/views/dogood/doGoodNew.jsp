<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="cache-control" content="no-store" />
	<meta property="wb:webmaster" content="8375316845f5cb81" />
	<meta property="qc:admins" content="3641564675617036727" />
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>善园 - 一起行善，温暖前行！</title>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css" /> 
</head>
<body>
	<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
	<!--头部 end-->
	<!--主体 start-->
	<div class="bodyNew">
		<div class="w1000 breadcrumb"> 当前位置：
			<a href="#" title="" target="_blank">首页</a> > 我要行善 </div>
		<div class="w1000">
			<div class="selectorNew">
				<dl> <dt>按领域： </dt>
					<dd id="fieldList">
						<a href="javascript:void(0);" title="" class="curTab">全部</a>
						<a v="education" href="javascript:void(0);">求学</a>
						<a v="disease" href="javascript:void(0);">助医</a>
						<a v="povertyAlleviation" href="javascript:void(0);">扶贫</a>
						<a v="disasterRelief" href="javascript:void(0);">赈灾</a>
						<a v="elderly" href="javascript:void(0);">安老</a>
					</dd>
				</dl>
				<dl> <dt>按地区：  </dt>
					<dd id="provinceList"></dd>
				</dl>
				<dl> <dt>按状态： </dt>
					<dd class="stateList">
						<a href="javascript:void(0);" title="" v="0" class="curTab">全部</a>
						<a href="javascript:void(0);" title="" v="1">募捐中</a>
						<a href="javascript:void(0);" title="" v="2">已结束</a>
					</dd>
				</dl>
			</div>
			<div class="filterNew mt15">
				<div class="sort">
					<a href="javascript:void(0);" title="" v="0" class="on">默认排序</a>
					<a href="javascript:void(0);" title="" v="1">金额<em class="desc"></em></a>
					<a href="javascript:void(0);" title="" v="2">人气<em class="desc"></em></a>
					<a href="javascript:void(0);" title="" v="3">时间<em class="desc"></em></a>
				</div>
				<div class="opear">
					<a href="javascript:void(0);" class="batch">批量捐款</a> <span>全选项目<em class="checkbox"></em></span> </div>
			</div>
			<div class="mt15 doGoodList">
				<ul id="doGoodList">
					<!--10条数据-->
					<li>
						<div class="goodPic">
							<a href="#" title=""><img src="res/images/newIndex/index/pimg2.jpg" alt=""></a>
						</div>
						<div class="goodDetail">
							<h2>混血双胞胎母亲戴云仙患重病</h2>
							<p class="info">戴云仙，浙江金华，女，44岁</p>
							<p>筹款目标：<strong>50000</strong>元</p>
							<p>发布时间：2015-11-04</p>
							<p>发 起 人：邱杨宁</p>
						</div>
						<div class="goodOpear">
							<a href="javascript:void(0);" class="checkbox"></a>
							<p>项目状态：募款中</p>
							<p>还需：<span class="orangeText">3255.25</span>元 <span class="ml10 orangeText">255</span>人捐款</p>
							<div class="progress"> <span class="pgGray">
                                <span class="pgRed" style="width:88%"></span> </span> <span class="pgText">88%</span> </div>
							<p>
								<a href="#" title="" class="projectBtn">我要捐款</a>
							</p>
						</div>
					</li>
					<li>
						<div class="goodPic">
							<a href="#" title=""><img src="res/images/newIndex/index/pimg2.jpg" alt=""></a>
						</div>
						<div class="goodDetail">
							<h2>混血双胞胎母亲戴云仙患重病</h2>
							<p class="info">戴云仙，浙江金华，女，44岁</p>
							<p>筹款目标：<strong>50000</strong>元</p>
							<p>发布时间：2015-11-04</p>
							<p>发 起 人：邱杨宁</p>
						</div>
						<div class="goodOpear">
							<a href="javascript:void(0);" class="checkbox"></a>
							<p>项目状态：募款中</p>
							<p>还需：<span class="orangeText">3255.25</span>元 <span class="ml10 orangeText">255</span>人捐款</p>
							<div class="progress"> <span class="pgGray">
                                <span class="pgRed" style="width:88%"></span> </span> <span class="pgText">88%</span> </div>
							<p>
								<a href="#" title="" class="projectBtn">我要捐款</a>
							</p>
						</div>
					</li>
				</ul>
				<div id="lstPages" class="lstPages" style=""><span class="lstP-con"><a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a><span class="pages"><i class="curPage" p="1">1</i><i p="2">2</i></span>
					<a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a>
					</span>
				</div>
			</div>
		</div>
	</div>
	<!--主体 end-->
	<!--底部 start-->
	<%@ include file="./../common/newfooter.jsp" %>
	<!--底部 end-->
	<script data-main="<%=resource_url%>res/js/dev/doGood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>