
var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
	//togetherConfigList:base+'together/loadTogetherConfigList.do'
	togetherConfigList:base+'together/loadDetailList.do'
};
var page=1;
var detail = {
	init:function(){
		var that = this;
		if($('.lookmore').length > 0) {
			that.lookmore();
		}
		that.togetherConfigList(page, 10);
		$('.loadmore').on('click',function(){
			if($('.loadmore a').text()=='点击加载更多'){
				page++;
				that.togetherConfigList(page, 10);
			}
		});
		$('.fill').width(function(){
			var des = Number($('.dt-progress .progress-dest .destination').text());
			var com = Number($('.dt-progress .progress-dest .complete').text());
			return com/des*100+'%';
		});
	},
	togetherConfigList:function(page,pageNum){
		var projectId = $('#projectId').val();
		var data = [];
		$.ajax({
			url:dataUrl.togetherConfigList,
			data:{projectId: projectId,page: page,pageNum: pageNum,t: new Date().getTime()},
			type:'GET',
			async:false,
			success:function(result){
				var total = result.total;
				if(result.result != 2) {
					if(result.items.length>0){
						for(var i=0;i<result.items.length;i++){
							var datas = result.items;
							if(datas[i].type=='0'){
								data.push('<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&shareUserId='+datas[i].userId+'"><div class="list-item">');
								data.push('<div class="item-info clearFix">');
								data.push('<div class="pic">');
								data.push('<img src="'+(datas[i].coverImageUrl==null?'http://www.17xs.org/res/images/detail/people_avatar.jpg':datas[i].coverImageUrl)+'" width="100%" height="100%"/>');
								data.push('</div>');
								data.push('<div class="info">');
								data.push('<p><span>'+datas[i].nickName+'</span>发起一起捐</p>');
								data.push('<p>和<span>'+datas[i].donateNum+'</span>位小伙伴筹得<span>'+datas[i].donateMoney+'</span>元</p>');
								data.push('</div>');
								data.push('</div>');
								data.push('<div class="item-word">');
								data.push('<p>"'+datas[i].content+'"</p>');
								data.push('</div>');
								data.push('</div></a>');
								
							}else if(datas[i].type=='1'){
								data.push('<a href="javascript:void(0);"><div class="list-item">');
								data.push('<div class="item-info clearFix">');
								data.push('<div class="pic">');
								data.push('<img src="'+(datas[i].coverImageUrl==null?'http://www.17xs.org/res/images/detail/people_avatar.jpg':datas[i].coverImageUrl)+'" width="100%" height="100%"/>');
								data.push('</div>');
								data.push('<div class="info">');
								data.push('<p><span>'+datas[i].nickName+'</span>为善园添砖加瓦</p>');
								data.push('<p>个人捐赠<span>'+datas[i].donateMoney+'</span>元</p>');
								data.push('</div>');
								data.push('</div>');
								data.push('<div class="item-word">');
								data.push('<p>"'+datas[i].content+'"</p>');
								data.push('</div>');
								data.push('</div></a>');
								
							}else if(datas[i].type=='2'){
								data.push('<a href="javascript:void(0);"><div class="list-item">');
								data.push('<div class="item-info clearFix">');
								data.push('<div class="pic">');
								data.push('<img src="'+(datas[i].coverImageUrl==null?'http://www.17xs.org/res/images/detail/people_avatar.jpg':datas[i].coverImageUrl)+'" width="100%" height="100%"/>');
								data.push('</div>');
								data.push('<div class="info">');
								data.push('<p><span>'+datas[i].nickName+'</span>到此一游</p>');
								data.push('</div>');
								data.push('</div>');
								data.push('<div class="item-word">');
								if(datas[i].content=='到此一游'){
									data.push('<p>"支持善园"</p>');
								}else{
									data.push('<p>"'+datas[i].content+'"</p>');
								}
								
								data.push('</div>');
								data.push('</div></a>');
							}
							
						}
					}
				}
				$('.faqiren-list').append(data.join(''));
				if(result.items.length<pageNum || total<=page*pageNum){
					$('.loadmore a').html("没有更多数据了");
				}
			}
		});
	},
	lookmore: function() {
		var p = $('.dt-content .content');
		if(p) {
			var content = p.html();
			var smallCon = p.html().substr(0, 300) + '...';
			if(p.height() > 250) {
				p.height(250);
//				p.html(smallCon);
			}
			$('.lookmore').on('click', function() {
				if($(this).hasClass('lookless')) {
//					p.html(smallCon);
					p.height(250);
					$(this).removeClass('lookless').text('查看全文');
					$(this).parent().addClass('detail-mask-bg');
					$('#dt-content')[0].scrollIntoView();
				} else {
					p.html(content);
					p.css({height:'auto'});
					$(this).addClass('lookless').text('收起');
					$(this).parent().removeClass('detail-mask-bg');
				}
			});
		}
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','.dt-dest .close5',function(){
			$('#overLay').hide();
			$('.dt-dest').fadeOut();
		}).on('click','.dt-foot .btn',function(){
			$('#overLay').show();
			$('.dt-selectTo').fadeIn();
		}).on('click','.dt-selectTo .toDonate',function(){
			$('.dt-selectTo').fadeOut(function(){
				$('.dt-dest').fadeIn();
			});
		}).on('click','.dt-money .add',function(){
			var num = 0;
			
			if($(this).siblings('.num2').find('input').length==0 ){
				num = Number($('#number2').text());
				$('#number2').text(num + 10);
				$('#total-money').text(parseFloat(num * 1.68).toFixed(2));	
				
			}else{
				num = Number($('input#number2').val());
				if(num){
					$(this).siblings('.num2').html('支持<span id="number2">'+num+'</span>块砖');
					$('#number2').text(num + 10);
					$('#total-money').text(parseFloat(num * 1.68).toFixed(2));
				}
			}
		}).on('click','.dt-money .min',function(){
			var num = Number($('#number2').text());
			if(num>=20){
				$('#number2').text(num - 10);
				$('#total-money').text(parseFloat(num * 1.68).toFixed(2));
			}else if(num>10){
				num = 10;
				$('#number2').text(num);
				$('#total-money').text(16.80);
			} else {
				if(num<=1){return false;}
				num--;
				$('.num2').html('<input type="number" id="number2" value="'+num+'" placeholder="至少输入一块砖哦" />');
				$('#total-money').text(parseFloat(num*1.68).toFixed(2));
				$('#number2').focus();
			}
		}).on('click','.dt-money .num2',function(){
			var num = 0;
			if($(this).find('input').length==0){
				num = Number($('#number2').text());
				num = num>=1?num:'1';
				$(this).html('<input type="number" id="number2" value="'+num+'" placeholder="至少输入一块砖哦" />');
				$('#number2').focus();
				$('.total').html('合计<span id="total-money">'+parseFloat(num*1.68).toFixed(2)+'</span>元');
			}else{
				return false;
			}
		}).on('keyup','.dt-money .num2 input',function(){
			$('#total-money').text(parseFloat(Number($('#number2').val()) * 1.68).toFixed(2));
		}).on('blur','.dt-money .num2 input',function(){
			var num = Number($(this).val());
			var s = '';
			if(num){
				if(num%1!==0){
					s = ''+num;
					num = Number(s.split('.')[0]);
				}
				$(this).parent().html('支持<span id="number2">'+num+'</span>块砖');
				$('#total-money').text(parseFloat(num*1.68).toFixed(2));
			}else{
				$(this).parent().html('支持<span id="number2">1</span>块砖');
				$('.total').html('合计<span id="total-money">1.68</span>元');
			}
		}).on('focus','.dt-money .num2 input',function(){
			$(this).val($(this).val()?$(this).val():'');
		}).on('click','.toBtn',function(){
			var money = $(".total #total-money").text(),
				projectId = $("#projectId").val(),
				userId = $("#userId").val(),
				realName = $("#realName").val(),
				mobileNum = $("#mobileNum").val(),
				donateWord = $("#donateWord").val(),
				needmoney = $("#needmoney").val();
			if(donateWord == '') {
				donateWord = $('#donateWord').attr('placeholder');
			}

			var paymoney = Number(money);
			if(isNaN(paymoney)) {
				layer.msg("捐款金额不低于0.01元");
				return;
			}
			needmoney = Number(needmoney);
			if(needmoney < money) {
				layer.msg("最多捐款金额" + needmoney + "元");
				return;
			}
			if((userId == null || userId == '')) {
				window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId=" + projectId + 
					"&amount=" + money + "&realName=" +
					realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord;
			} else {
				window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId=" + projectId +
					"&amount=" + money + "&realName=" +
					realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord;
			}
		}).on('click','#overLay',function(){
			$('#overLay').hide();
			$('.dt-dest').fadeOut();
			$('.dt-selectTo').fadeOut();
		});
	}
};

detail.init();
detail.bindEvent();