<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/project_detail_share.css">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/base.css?v=201512111">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/donate.css?v=201512112">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
    <script src="http://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="jkHeadBox">
		<div class="userLogo">
			<img src="" id="headImageUrl"></img>
		</div>
		<div class="userInfo hasInfo">
			<h2 class="name" id="nickName"></h2>
			<p class="spec phone" id="mobile"></p>
			<p class="address" id="address"></p>
			<p id="addressId" style="display:none"></p>
		</div>
	</div>
	<div class="jkMain02Box">
		<div class="jkMainTitle">
			<i class="receipt"></i>请选择开票订单
			<!-- 全选处加no显示不勾取状态的图片，不加no显示勾取状态的图片 -->
			<span class="checkAll"><i class="check no" id="checkAll"></i>全选</span>
		</div>
		<div class="fpDetailBox">
			<ul class="fpdonateList">
                 <div class="mainer"></div>
					<div class="loadMore">
						<div style="display:none">
						 <span id="page"></span>
						 <span id="pageNum"></span>
						 <span id="total"></span>
						</div >
						<div class="moreRecord">
						   <a href="javascript:;" class="loadMoreA" id="loadMoreA" style="color: #707070;">查看更多记录</a>
						 </div>
						<div class="noneRecord" style="display:none">
						<span class="loadMoreA" style="color: #707070;">没有更多记录了</span></div>
					</div>
			</ul>

		</div>
		<div class="fpSumBox">
			<div class="fpSumItem">
				<p>开票金额：<span id="money_sum"></span>元</p>
				<p class="remarkItem">注：开票金额少于100元，邮寄费用由爱心人士承担</p>
			</div>
			<div class="fpSumItem"  >
				<label>抬头</label>
				<input type="text" id="invoiceHead" data-role="none" placeholder="请输入发票的抬头号">
			</div>
		</div>
		<div class="btnGroup" >
			<input type="button" value="提&nbsp;&nbsp;交" data-role="none" class="submitBtn">
		</div>
	</div>
	<!-- 错误提示信息 控制显示/消失  -->
	<div class="errorBox" style="display: none;"></div>
	<div class="payTips" id="payTips" style="display: none;"><div class="investTip2">提交成功</div></div>

	<script type="text/javascript">
	$(function(){
	$(".submitBtn").click(function(){
		$(".prompt_box").show();
	});
	})
		function codefans(){

           $('.errorBox').css('display','none');
        }
	    $(function() {
			
			
			  $.ajax({
					url: 'http://www.17xs.org/user/userInfo.do',
					dataType: 'json',
					type: 'post',
					data: {

					},
					success: function(data) {
						   if(data.result == 0){
						   var item = data.item;
						   var headImageUrl = item.headImageUrl;
						   $('#headImageUrl').attr("src",headImageUrl);
						   var nickName = item.nickName;
						   $('#nickName').text(nickName);
					   }
					}					
				});
				
				var pageNum1 = 10;
				var page1 = 1;
				$.ajax({
					url: 'http://www.17xs.org/user/userAddressList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum1,
						page: page1
					},
					success: function(data) {
						   if(data.flag == 1){
						   var ret = data.obj.data;
						   var mobile = '';
						   var address = '';
						   var addressId = '';
						   for (var i in ret) {
							   var r = ret[0];
							   mobile = r.mobile ;
							   address = r.province + r.city + r.area + r.detailAddress;
							   addressId = r.id ;
							   $('#mobile').text(mobile);
							   $('#address').text(address);
							   $('#addressId').text(addressId);
						   }

					   }
					}					
				});
			    
				
				var pageNum = 10;
				var page = 1;
				var temp = 0;
				var checkSum = 0;
				var mSum = 0;
				var arr = new Array();
				$('#money_sum').text(mSum.toFixed(2));
				$.ajax({
					url: 'http://www.17xs.org/user/donationlist_invoice.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page
					},
					success: function(data) {
						if(data.flag == 1){
						   var nums = data.obj.nums ;
						   var page = data.obj.page;
						   $('#page').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNum').text(pageNum);
							var title = '';
							var dMoney = '';
							var id = '';
							var htmlStr = '';
							var ret = data.obj.data;
							for (var i in ret) {
								var r = ret[i];
								title = r.title;
								dMoney = r.dMoney;
								id = r.id;
								    htmlStr = htmlStr +'<li class="fpdonateItem clearfix"><p>为"'+title+'"捐助了'+dMoney+'元</p><span class="check no"></span><i id="money" style="display:none">'+dMoney+'</i><p style="display:none">'+id+'</p></li>';
							}
							$('.mainer').append(htmlStr);
							if(nums <= pageNum){
								   $(".moreRecord").css('display','none'); 
								   $(".noneRecord").css('display','block'); 
							}
                         
					    }else{
							$(".moreRecord").css('display','none'); 
							$(".noneRecord").css('display','block'); 
						}	
				}					
				});
				   //发票抬头
                   $("#invoiceHead").blur(function(){
					var invoiceHead =  $("#invoiceHead").val();
					if(invoiceHead == ''){
						$("#invoiceHead").parent().removeClass('error');
						return;
					}
					var b = invoiceHead.match(/^[0-9]*$/);
					if(!b){
						$("#invoiceHead").parent().removeClass('error');
					}else{
						$('.errorBox').html('请输入正确的发票抬头！');
						$('.errorBox').css('display','block');
						setTimeout("codefans()",2000);//2秒
					}
				});
			   
				$(document).on('tap','li > span',function(){
					if($(this).attr('class') != 'check'){
						$(this).removeClass("no");
                        mSum = (Number(mSum) + Number($(this).next("i").text())).toFixed(2);
						arr.push($(this).next().next("p").text()); 
						checkSum = checkSum + 1;
					}else{
						$(this).addClass("no");
						mSum = (Number(mSum) - Number($(this).next("i").text())).toFixed(2);
						arr.splice(jQuery.inArray($(this).next().next("p").text(),arr),1); 
						checkSum = checkSum - 1; 
					}
					var liLength = $("li").length;
					if(liLength == checkSum){
						$("#checkAll").removeClass("no");
					}else{
						$("#checkAll").addClass("no");
					}
					$('#money_sum').text(mSum);
					
				});
				$(document).on('tap','#checkAll',function(){
					
					if(temp == 0){
						$("#checkAll").removeClass("no");
						$("ul.fpdonateList").each(function(){
						$(this).find("i").each(function(i){
							 mSum = (Number(mSum) + Number($(this).text())).toFixed(2);
                             arr.push($(this).next("p").text());
						});	
						 $(this).find("span").each(function(i){
							 $(this).removeClass("no");
						});
					  });
					  temp = 1;
					}else{
						$("#checkAll").addClass("no");
						$("ul.fpdonateList").each(function(){
                             mSum = (1-1).toFixed(2);
							 arr = new Array();
							 $(this).find("span").each(function(i){
								 $(this).addClass("no");
							});
					  });
					  temp = 0;
					}
					$('#money_sum').text(mSum);

				});
				$(document).on('tap','.submitBtn',function(){
					if(arr.length == 0){
						$('.errorBox').html('请至少选择一条记录！');
						$('.errorBox').css('display','block');
						setTimeout("codefans()",2000);//2秒
						return;
					}
					var invoiceHead =  $("#invoiceHead").val();
					if(invoiceHead == ''){
						$('.errorBox').html('请输入正确的发票抬头！');
						$('.errorBox').css('display','block');
						setTimeout("codefans()",2000);//2秒
						return;
					}
					var addressId = $("#addressId").text();
					var money_sum = $("#money_sum").text();
					var info =  arr.join("_");
					$.ajax({
						url: 'http://www.17xs.org/user/addInvoice.do',
						dataType: 'json',
						type: 'post',
						data: {
							invoiceHead: invoiceHead,
							addressId: addressId,
							info:info,
							content:'',
							isFree:'',
							mailAmount:'',
							mailCompany:'',
							mailCode:'',
							invoiceAmount:money_sum
						},
						success: function(data) {
							if(data.flag == 1){
							   $('#payTips').css('display','block');
							   //("codefans()",2000);//2秒
								setTimeout(function(){
								window.location.href="http://www.17xs.org/user/invoiceRecordList.do";
								},1000);
	                           //location.href = "http://www.17xs.org/user/invoiceRecordList.do";
							}			
						}
					});
				});
				$(document).on('tap','#loadMoreA',function(){
					var page = $("#page").text() ;
					var pageNum = $("#pageNum").text();
					page = Number(page) + 1;
					$('#page').text(page);
				   $.ajax({
					url: 'http://www.17xs.org/user/donationlist_invoice.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page
					},
					success: function(data) {
						if(data.flag == 1){
						   var nums = data.obj.nums ;
						   $('#page').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNum').text(pageNum);
						   var sum = page * pageNum;
							var title = '';
							var dMoney = '';
							var id = '';
							var htmlStr = '';
							var ret = data.obj.data;
							for (var i in ret) {
								var r = ret[i];
								title = r.title;
								dMoney = r.dMoney;
								id = r.id;
								    htmlStr = htmlStr +'<li class="fpdonateItem clearfix"><p>为"'+title+'"捐助了'+dMoney+'元</p><span class="check no"></span><i id="money" style="display:none">'+dMoney+'</i><p style="display:none">'+id+'</p></li>';
							}
							$('.mainer').append(htmlStr);
							if(sum >= pageNum){
								   $(".moreRecord").css('display','none'); 
								   $(".noneRecord").css('display','block'); 
							}
                         
					    }else{
							$(".moreRecord").css('display','none'); 
							$(".noneRecord").css('display','block'); 
						}	
				}					
				});
				});
			});
	</script>
</body>
</html>