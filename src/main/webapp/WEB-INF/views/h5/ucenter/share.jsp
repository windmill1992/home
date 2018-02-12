<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userCenter.css?v=20171130">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body style="background-color: #f4f3f0;">
	<!-- 头部 -->
	<%@ include file="./../common/head.jsp" %>
	<div class="wrapper">
		<div class="mainBox">
			<div class="mainTitle flex spb fcen">
				<div class="left flex fcen">
					<i class="function-icon icon7"></i>
					<h2>我的分享</h2>	
				</div>
				<span class="moreInfo no">获得积分：0</span>
			</div>
			<ul class="tabBox flex">
				<li class="stateItem w33">
					<p>分享项目</p>
					<span>${proNum}个</span>
				</li>
				<li class="stateItem w33">
					<p>参与人数</p>
					<span>${peoNum}人</span>
				</li>
				<li class="stateItem w33 last">
					<p>募捐金额</p>
					<span>${donatAmount==null?0:donatAmount}元</span>
				</li>
			</ul>
		</div>
		<ul class="shareList"></ul>
		<div style="display:none">
			<span id="page"></span>
			<span id="pageNum"></span>
		</div>
		<div class="roadMoreBox" id="moreRecord">
			<a href="javascript:;" class="loadMoreA" id="loadMoreA" style="color: #707070;">查看更多项目</a>
		</div>
	    <div class="roadMoreBox" id="noneRecord" style="display:none">没有更多了</div>

	</div>
	<!-- 公共底部 -->
	<%@ include file="./../common/footer.jsp" %>
	<script type="text/javascript">
		
		function formatNumber(num, precision, separator) {
			var parts;
			// 判断是否为数字
			if (!isNaN(parseFloat(num)) && isFinite(num)) {
				// 把类似 .5, 5. 之类的数据转化成0.5, 5, 为数据精度处理做准, 至于为什么
				// 不在判断中直接写 if (!isNaN(num = parseFloat(num)) && isFinite(num))
				// 是因为parseFloat有一个奇怪的精度问题, 比如 parseFloat(12312312.1234567119)
				// 的值变成了 12312312.123456713
				num = Number(num);
				// 处理小数点位数
				num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
				// 分离数字的小数部分和整数部分
				parts = num.split('.');
				// 整数部分加[separator]分隔, 借用一个著名的正则表达式
				parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || ','));

				return parts.join('.');
			}
			return NaN;
		}

	    $(function() {
	    	var page = 1;
			$("h1").css('display','none'); 
			donationlist(page,10);
			//<!-- 具体详情页start-->
			$(document).on('click','.shareList li > a',function(){
				var id = $(this).children().eq(0).text();
				location.href = "/project/view_share_h5.do?shareType=1&projectId="+id;
			});
			$(document).on('click','#loadMoreA',function(){
			    donationlist(++page,10);
			});
			$('#linkToUcenter')[0].href = 'http://www.17xs.org/ucenter/userCenter_h5.do';
			
			function donationlist(page,pageNum){
				$.ajax({
					url: 'http://www.17xs.org/user/shareProjectList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page
					},
					success: function(data) {
						if(data.flag == 1){
						   var ret = data.obj.data;
						   var title = '';
						   var cryMoney = '';
						   var donatAmount = '';
						   var htmlStr = '';
						   var state = '';
						   var coverImageUrl = '';
						   var donatAmountShare = '';
						   var id = '';
						   var money = '';
						   var nums = data.obj.nums;   
						   for (var i in ret) {
							   var r = ret[i];
							   title = r.title;
							   id = r.id;
							   cryMoney = r.cryMoney ;
							   donatAmount = r.donatAmount;
							   state = r.state ;
							   money = cryMoney - donatAmount;
							   if(money <= 0){
								   state = 280;
							   }
							   money = formatNumber(money,2);
							   donationNum = r.donationNum ;
							   coverImageUrl = r.coverImageUrl;
							   donatAmountShare = r.donatAmountShare;
							   donatAmountShare = formatNumber(donatAmountShare,2);
							   htmlStr = htmlStr + '<li class="shareItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+title+'</div>';
							   if(state == 240){
								   htmlStr = htmlStr + '<p>已募集<span>'+donatAmount+'</span>元</p>';
							   }else if(state == 260){
								   htmlStr = htmlStr + '<p>已结束</p>';
							   }else if(state == 280){
								   htmlStr = htmlStr + '<p>已筹满</p>';
							   }
							   htmlStr = htmlStr + '<p><i id="">'+donationNum+'</i>人参与捐助，捐助金额<i id="">'+donatAmountShare+'</i>元</p></div></a></li>';
						   }
						   $('.shareList').append(htmlStr);
						   if(nums <= pageNum * page){
							   $("#moreRecord").css('display','none'); 
							   $("#noneRecord").css('display','block'); 
							   if(nums == 0){
							   		$("#noneRecord").html('暂无分享数据！');
							   }
						   }
					   }else{
						   $("#moreRecord").css('display','none'); 
						   $("#noneRecord").css('display','block').html('暂无分享数据！'); 
					   }
					}					
				});
			}
		});
	</script>
</body>
</html>