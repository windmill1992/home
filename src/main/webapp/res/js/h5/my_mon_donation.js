/**
 * Created by Administrator on 2016/4/1.
 */
$(function(){
	
	var balance = $("#balance").val(),
		page = $("#page").text(),
		pageNum = $("#pageNum").text();
	if(Number(page)==0) page = 1;
	if(Number(pageNum)==0) pageNum = 10;
	data(page,pageNum);
	disposeClick();
	
	$(".click_duo").click(function(){
		var page = $("#page").text(),pageNum = $("#pageNum").text();
		page = Number(page) + 1;
		data(page,pageNum);
	});
	
	$("#promptConcel").click(function(){
        $(".prompt_box").hide();
    });
	$("#concelCharge").click(function(){
        $("#charge_prompt").hide();
    });
	
	$("#promptConfirm").click(function(){
		var flag = $(this).text();
		if(flag == '立即开通'){
			reAddMonthlyDonate()
		}else if(flag == '立即充值'){
			$("#charge_prompt").show();
		}else if(flag == '关闭月捐'){
			closeMonthlyDonate();
		}
        $(".prompt_box").hide();
    });
	
    /*充值*/
    $("#confirCharge").click(function(){
    	var chargeMomeny = Number($('#chargeMomeny').val()),
    		userName = $('#userName').text(),
    		mobile = $('#mobile').text();
    	if(chargeMomeny <= 0){
    		alert("请输入充值金额")
    		return false;
    	}
    	location.href='/visitorAlipay/tenpay/rechargedeposit.do?amount='+chargeMomeny+'&touristName='+userName+'&touristMobile='+mobile;
    });
    /*重新开通月捐*/
    function reAddMonthlyDonate(){
    	var money = $("#money").text(),
    		notice = $("#notice").text(),
    		category = $("#category").text(),
			type = $("#type").text(),
			mobileNum = $('#mobile').text(),
			id=$("#month_id").text();
		if(notice == 1){
			mobileNum = null;
		}
		$.ajax({
			url: 'http://www.17xs.org/user/addDonateTime.do',
			dataType: 'json',
			type: 'post',
			data: {
				id:id,
				type:type,
				category:category,
				notice:notice,
				mobileNum:mobileNum,
				money:money
			},
			success: function(result) {
				if(result.flag==1){
					location.href='/user/getMyDonateTime.do';
				}
				if(result.flag==0){
				   $(".prompt_box").hide();
				   $("#charge_prompt").show();
				   $('#chargeMomeny').val(money);
				}
			}				
	   	});
    }
    /*循环列表中的点击事件处理*/
	function disposeClick(){
		$('body').on('click','.mainer li',function(){
			var flag = $(this).attr("data-state");
			if(flag == '进行中'){
		        $("#prompt_title").text("月捐计划");
		        $("#prompt_content").text("您已开通月捐计划，是否需要关闭~");
		        $("#promptConfirm").text("关闭月捐");
		        $("#promptConcel").text("不关闭");
		        $(".prompt_box").show();
		        $("#dtId").text($(this).attr("v0"));
			}
			if(flag == '余额不足'){
		        $("#prompt_title").text("开通月捐");
		        $("#prompt_content").text("您的月捐计划因余额不足而无法进行，充值善款可继续开通月捐计划");
		        $("#promptConfirm").text("立即充值");
		        $("#promptConcel").text("以后再说");
		        $(".prompt_box").show();
			}
			if(flag == '已停止'){
		        $("#prompt_title").text("开通月捐");
		        $("#prompt_content").text("您已停止该月捐计划，是否重新开通");
		        $("#promptConfirm").text("立即开通");
		        $("#promptConcel").text("以后再说");
		        $(".prompt_box").show();
		        $("#type").text($(this).attr("v3"));
		        $("#category").text($(this).attr("v4"));
		        $("#notice").text($(this).attr("v2"));
		        $("#money").text($(this).attr("v1"));
		        $("#month_id").text($(this).attr("v0"));
			}	
		});
	}
    /*列表数据显示*/
    function data(page,pageNum){
		var userId = $("#userId").val();
	    var pageNum = pageNum;
		var page = page;
		$.ajax({
			url: 'http://www.17xs.org/user/myDonateTime.do',
			dataType: 'json',
			type: 'post',
			data: {
				pageNum: pageNum,
				page: page,
				userId : userId
			},
			success: function(data) {
			   if(data.flag == 1){
				   var ret = data.obj.data,page = data.obj.page,pageNum = data.obj.pageNum;
				   var sum = Number($('#sum').text()) + ret.length;
				   $('#page').text(page);
				   $('#sum').text(sum);
				   var nums = data.obj.nums;
				   var htmlStr = '',state = '',stateContent = '',money = '',totalMoney = '', createTime = '';
				   for (var i in ret) {
					   var r = ret[i];
					   money = r.money,notice = r.notice,type = r.type,category = r.category,dtId=r.id;
					   totalMoney = r.money * r.number ;
					   createTime = r.ctime ;
					   var c = r.categorys;
					   if(r.state == 201){
						   state = '进行中';
						   stateContent = '<div class="listTime_t fr">进行中</div>';
						   if(r.money > balance ){
							   state = '余额不足';
							   stateContent = '<div class="listTime_t fr weekness">余额不足</div>';
						   }
					   }else if(r.state == 203){
						   state = '已停止';
						   stateContent = '<div class="listTime_t fr stop">已停止</div>';
					   }
	                   htmlStr += '<li data-state="'+state+'" v0="'+dtId+'" v1="'+money.toFixed(2)+'" v2="'+notice+'" v3="'+type+'" v4="'+category+'">';
	                   htmlStr += '<a href="javaScript:;" class="list_click1"><div class="list_money">'
	                   if(r.type == 0){
	                	   htmlStr += '<p class="listMoney_o fl" id="perMoney">每日捐'+money.toFixed(2)+'元</p>';
	                   }else{
	                	   htmlStr += '<p class="listMoney_o fl" id="perMoney">每月捐'+money.toFixed(2)+'元</p>';
	                   }
	                   htmlStr += '<p class="listMoney_t fr">已捐出'+totalMoney.toFixed(2)+'元</p></div><div class="list_time">';
	                   htmlStr += '<span class="listTime_o fl">'+createTime+'</span>';
	                   htmlStr += stateContent+'</div><div class="hengxian"></div>';
	                   htmlStr += '<div class="list_type"><p class="listType_o fl">指定项目 :</p>';
	                   if(r.categorys.length == 0){
	                	   htmlStr += '<span class="listType_t">未指定</span>';
	                   }else{
	                	   for(var j in r.categorys){
	                		   if(j == (r.categorys.length-1)){
	                			   htmlStr += '<span class="listType_t">'+r.categorys[j]+'</span>';
	                		   }else{
	                			   htmlStr += '<span class="listType_t">'+r.categorys[j]+'、</span>';
	                		   }
	                	   }
	                   }
	                   htmlStr += '</div></a></li>';
					}
					$('.mainer').append(htmlStr);
					if(nums == 0){
						$("#loadmore").text('您还未开通月捐！').css({color:'#999'});
					}else{
					   	if(sum < nums){
						   $("#loadmore").text('点击加载更多');
						}else{
						   $("#loadmore").text('没有更多数据了').css({color:'#999'});
						}
					}
				}
			}					
		});
	}
    /*关闭月捐*/
    function closeMonthlyDonate(){
    	var dtId = $("#dtId").text(),state = 203;
		$.ajax({
    		url: 'http://www.17xs.org/user/updateDonateTime.do',
    		dataType: 'json',
    		type: 'post',
    		data: {
    			id:dtId,
    			state:state
    		},
    		success: function(result) {
    			if(result.flag==1){
    				location.href='/user/getMyDonateTime.do';
    			}
    		}					
	   	});
    }
});
