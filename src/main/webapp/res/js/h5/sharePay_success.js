$(function() {
	var pageNum = 10;
	var page = 1;
	var projectId = $('#projectId').val();
	var show = $('#show').val();
	if(show=='1'){
		$('#overLay').show();
		$('.paySuccess-dialog').show();
	}else{}
	if(projectId == '3793'){
		$('#cardDialog').addClass('show').find('.card').height($(window).height()/2-20);
	}else{}
	$.ajax({
		url: 'http://www.17xs.org/project/extensionDonation.do',
		dataType: 'json',
		type: 'post',
		data: {
			pageNum: pageNum,
			page: page,
			projectId:projectId
		},
		success: function(data) {
			if(data.flag == 1){
			   var nums = data.obj.nums;
			   $('#nums').text(nums);
			   var page = data.obj.page;
			   $('#page').text(page);
			   var pageNum = data.obj.pageNum;
			   $('#pageNum').text(pageNum);
			   var total = data.obj.total;
			   var result = new Array;
			   var id = "";
			   var name = "";
			   var imagesurl = "";
			   var dMoney = "";
			   var leaveWord = "";
			   var showTime = "";
			   var htmlStr = "";
			   var ret = data.obj.data;
			   for (var i in ret) {
				  var r = ret[i];
				  name = r.name;
				  imagesurl = r.imagesurl;
				  dMoney = r.dMoney;
				  leaveWord = r.leaveWord;
				  showTime = r.showTime;
				  htmlStr = htmlStr + '<li class="clearfix"><div class="donateListImg"><img src='+imagesurl+'></div><div class="donateListCon"><h3 class="listConTitle clearfix"><span class="fl">'+name+'</span><span class="fr">捐助<i>'+dMoney+'</i>元</span></h3><div class="listCon clearfix"><div class="listConMes fl">'+leaveWord+'</div><div class="listConDate fr" >'+showTime+'</div></div></li>';
			   }
			   $('.mainer').append(htmlStr);
			}
		}
	});
/*				//索取发票
				$(document).on('click','#sq_bill',function(){
					var pageNum = 10;
					var page = 1;
					$.ajax({
						url: 'http://www.17xs.org/user/userAddressList.do',
						dataType: 'json',
						type: 'post',
						data: {
							pageNum: pageNum,
							page: page
						},
						success: function(data) {
							if(data.flag == 1){
							   var nums = data.obj.nums;
                               if(nums > 0){
                            	   location.href = "http://www.17xs.org/user/toInvoice.do";
                               }else{
                            	   location.href = "http://www.17xs.org/user/toAddress.do?type="+'1';
                               }
							}
						}
					});
					
			    });*/
/*				//查看捐赠记录
				$(document).on('click','#seeRecord',function(){
				     location.href = "http://www.17xs.org/ucenter/core/personalCenter_h5.do?itemType=1";
				});*/
	//查看捐赠记录
	$(document).on('click','#seeRecord',function(){
		var extensionPeople = $('#ePeople').val();
	     location.href = 'http://www.17xs.org/ucenter/myDonate.do?extensionPeople='+extensionPeople;
	});
	//捐助其他项目
	$(document).on('click','#donateProject',function(){
		var extensionPeople = $('#ePeople').val();
	     location.href = 'http://www.17xs.org/index/index_h5.do?extensionPeople='+extensionPeople;
	});
	//close弹框
	$('.togterCha').click(function(){
		$('.payTogter').hide();
	});
/*				//领取礼品
				$(document).on('click','#sq_gift',function(){
					var pageNum = 10;
					var page = 1;
					$.ajax({
						url: 'http://www.17xs.org/user/userAddressList.do',
						dataType: 'json',
						type: 'post',
						data: {
							pageNum: pageNum,
							page: page
						},
						success: function(data) {
							if(data.flag == 1){
							   var nums = data.obj.nums;
                               if(nums > 0){
                            	   location.href = "http://www.17xs.org/user/giftDetail.do";
                               }else{
                            	   location.href = "http://www.17xs.org/user/toAddress.do?type="+'2';
                               }
							}
						}
					});
					
			    });*/
	//捐赠证书
	$(document).on('click','#sq_gift',function(){
	     location.href = "http://www.17xs.org/ucenter/myCertificate.do?shareType=1 ";
	});
	//开票记录
	$(document).on('click','#sq_bill',function(){
	     location.href = "http://www.17xs.org/ucenter/myInvoice.do";
	});
	$(document).on('click','#loadMoreA',function(){
		var nums = $("#nums").text() ;
		var page = $("#page").text() ;
		var pageNum = $("#pageNum").text();
		page = Number(page) + 1;
		$('#page').text(page);
		var projectId = $('#projectId').val();
		$.ajax({
			url: 'http://www.17xs.org/project/extensionDonation.do',
			dataType: 'json',
			type: 'post',
			data: {
				pageNum: pageNum,
				page: page,
				projectId:projectId
			},
			success: function(data) {
				if(data.flag == 1){
				   var total = data.obj.total;
				   var result = new Array;
				   var id = "";
				   var name = "";
				   var imagesurl = "";
				   var dMoney = "";
				   var leaveWord = "";
				   var showTime = "";
				   var htmlStr = "";
				   var ret = data.obj.data;
				   for (var i in ret) {
					  var r = ret[i];
					  name = r.name;
					  imagesurl = r.imagesurl;
					  dMoney = r.dMoney;
					  leaveWord = r.leaveWord;
					  showTime = r.showTime;
					  htmlStr = htmlStr + '<li class="clearfix"><div class="donateListImg"><img src='+imagesurl+'></div><div class="donateListCon"><h3 class="listConTitle clearfix"><span class="fl">'+name+'</span><span class="fr">捐助<i>'+dMoney+'</i>元</span></h3><div class="listCon clearfix"><div class="listConMes fl">'+leaveWord+'</div><div class="listConDate fr" >'+showTime+'</div></div></li>';
				   }
				   if(ret.length < pageNum){
					   $(".more").css('display','none'); 
					   $(".no").css('display','block'); 
				   }
				   $('.mainer').append(htmlStr);
				}			
			}
		});
	});
	
	
	var h = $(window).height() - $('.paySuccess-dialog').height();
	$('.paySuccess-dialog').css({'top':h/2-30+'px'});
	
	$(document).on('click','.paySuccess-dialog .pd-close',function(){
		$('#overLay').hide();
		$('.paySuccess-dialog').fadeOut();
	}).on('click','.paySuccess-dialog .submit',function(){
		var name = $('#name').val(),
		tel = $('#tel').val(),
		addr = $('#addr').val(),
		detailAddr = $('#detailAddr').val();
		
		if(tel!="" && !/^1[34578]\d{9}$/.test(tel)){
			showTips('请正确填写手机号');
			return;
		}
		if(/请选择/.test(addr)){
			showTips('请选完地址信息');
			return;
		}
		var name=$('#name').val(),tel=$('#tel').val(),addr=$('#addr').val(),detailAddr=$('#detailAddr').val();
		var province='',city='',area='', addrs=addr.split('-');
		if(addrs.length==1){
			province=addrs[0];
		}else if(addrs.length==2){
			province=addrs[0];
			city=addrs[1];
		}else if(addrs.length==3){
			province=addrs[0];
			city=addrs[1];
			area=addrs[2];
		}
		$.ajax({
			type:"post",
			url:"http://www.17xs.org/user/updateUserInfo.do",
			data:{realName:name,mobileNum:tel,province:province,city:city,area:area,familyAddress:detailAddr},
			success:function(result){
				$('#overLay').hide();
				$('.paySuccess-dialog').hide();
				showTips('添加成功');
			}
		});
	});
	
	/*翻转卡片*/
	
	$('body').on('click','#cardDialog .card',function(){
		var $card = $(this).closest('.card');
		$card.addClass('rev');
		$.ajax({
			type:"get",
			url:"http://www.17xs.org/file/randomPic.do",
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					var html = [];
					html.push('<div class="pic">');
					if(r.description.substr(0,1) == '1'){
						html.push('<img src="'+r.thumbUrl+'" class="h"/>');
					}else{
						html.push('<img src="'+r.thumbUrl+'" class="v"/>');
					}
					html.push('</div><p>'+r.description.substr(1)+'</p>');
					if(r.url == null){
						html.push('<a href="'+r.thumbUrl+'" class="download">');
					}else{
						html.push('<a href="'+r.url+'" class="download">');
					}
					html.push('<img src="/res/images/h5/images/download.png"/><span>保存图片</span></a>');
					$card.parent().next().html(html.join(''));
					setTimeout(function(){
						$card.removeClass('rev').hide();
						$card.parent().next().show();
						$('#txtTip').html('<p class="txt">恭喜您获得</p>');
					},2000);
				}else{
					$card.removeClass('rev');
					alert('没有可领取的图片');return;
				}
			}
		});
	}).on('click','#cardDialog .close',function(){
		$('#cardDialog').addClass('hide1');
		setTimeout(function(){
			$('#cardDialog').removeClass('show hide1');
		},400)
	});
	
	function showTips(text){
		if(!$('.tips').is(':hidden')){
			return;
		}
		$('.tips').html(text).fadeIn();
		setTimeout(function(){
			$('.tips').fadeOut();
		},2000);
	}
});