/*
 * 2017-6-27
 * 
 */
var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
		donationlist:base+'message/loadMessageWallList.do',
		addLeaveWord:base+'h5ProjectDetails/addNewLeaveWord.do'
};
var page=1;
var feedbackWall={
		init:function()
		{
			var that = this;
			//$('#overLay').hide();
			if($('#show').val()==1){
				$('#overLay').show();
				$('.leaveWordTips').show();
			}else{
				$('.leaveWordTips').hide();
				$('#overLay').hide();
			}
			$('body').on('click','.clickme',function(){
				$('#overLay').show();
				$('.selectTo').show();
			}).on('click','.leaveWordTips .close',function(){
				$('#overLay').hide();
				$('.leaveWordTips').hide();
			}).on('click','.leaveWordEdit .close2',function(){
				$('#overLay').hide();
				$('.leaveWordEdit').hide();
			}).on('click','.leaveWordContent .close3',function(){
				$('#overLay').hide();
				$('.leaveWordContent').hide();
				$('.leaveWordContent p').hide();
			}).on('click','.selectTo .close4',function(){
				$('#overLay').hide();
				$('.selectTo').hide();
			}).on('click','.share',function(){
				$('#overLay').show();
				$('.shareTips').show();
			}).on('click','.nodonate',function(){
				$('#overLay').show();
				$('.leaveWordContent').show();
			}).on('click','.selectTo .toLeaveWord',function(){
				$('.selectTo').fadeOut(function(){
					$('.leaveWordEdit').fadeIn();
				});
			}).on('click','#overLay',function(){
				$('.shareTips').hide();
				$('#overLay').hide();
				$('.leaveWordEdit').hide();
				$('.leaveWordTips').hide();
				$('.leaveWordEdit').hide();
				$('.leaveWordContent').hide();
				$('.leaveWordContent p').hide();
				$('.selectTo').hide();
			}).on('click','.loadmore',function(){
				var size = $('.fd-content ul').length-1;
				page++;
				that.leaveWordList(page, 20,size);
			}).on('click','#pub',function(){
				var projectId = $('#projectId').val();
				var userId = $('#userId').val();
				var content = $('#word').val();
				if(content==''){
					layer.msg('留言内容不能为空');
					return false;
				}
				that.addLeaveWord(userId,projectId,content);
				return true;
			});
			
			$('#overLay')[0].addEventListener('touchmove',function(event){
				event.preventDefault();
			},false);
			that.leaveWordList(1,20,0);
			
		},
		
		leaveWordList:function(page,pageNum,size){
			var projectId = $('#projectId').val();
			if($(".loadmore a").html()=='没有更多数据了'){
				return false;
			}else{
			$.ajax({
				url:dataUrl.donationlist,
				data:{projectId: projectId,page: page,pageNum: pageNum,t: new Date().getTime()},
				type:'GET',
				success:function(result){
					var colors = ['#FF8892','#97E279','#FFA350','#8193D1'];
					var html = [],userId=$('#userId').val();
					//var con = result.content;
					//判断有无数据
					if(result.result != 2) {
						if(result.items.length>0) {
							var total = result.total,
								datas = result.items,
								len = result.items.length;
								//html = [];
								for (var i=0;i<len;i++) {
									if(i%2==0){
										html.push('<ul class="odd">');
										html.push('<div class="item"><li class="null" data-id=""></li></div>');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div>');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div>');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div></ul>');
									}else{
										html.push('<ul class="even">');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div>');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div>');
										html.push('<div class="item"><li class="clickme" data-id=""></li></div>');
										html.push('<div class="item"><li class="null" data-id=""></li></div></ul>');
									}
								}
								$('.fd-content').append(html.join(''));
								for (var i=0,j=0;i<len;i++) {
									if(!datas[i].donated){
										if(datas[i].id == userId){
											$('.leaveWordContent').append('<p>我： '+datas[i].text+'</p><p class="time">'+datas[i].time+'</p>');
										}else{
											$('.leaveWordContent').append('<p>'+datas[i].name+'说: '+datas[i].text+'</p><p class="time">'+datas[i].time+'</p>');
										}
									}
								}
								for (var i=0,j=size;i<len;) {
									if(datas[i]){
										var r1 = Math.floor(Math.random()*3+1); //一行留言个数
										var r2 = Math.floor(Math.random()*3); //留言随机位置
										var ele = '',flag = 1;
										if(!datas[i].donated){
											if(datas[i].id == userId){
												ele = '<li class="nodonate self" data-id="'+datas[i].id+'"><p class="content">'+datas[i].name+'：</p><p>到此一游</p><p class="time">'+datas[i].time+'</p></li>';
											}else{
												ele = '<li class="nodonate" data-id="'+datas[i].id+'"><p class="content">'+datas[i].name+'：</p><p>到此一游</p><p class="time">'+datas[i].time+'</p></li>';
											}
										}else{
											if(datas[i].id == userId){
												ele = '<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i].id+'"><li class="donated self" data-id="'+datas[i].id+'"><p class="content">'+datas[i].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i].time+'</p></li></a>';
											}else{
												ele = '<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i].id+'"><li class="donated" data-id="'+datas[i].id+'"><p class="content">'+datas[i].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i].time+'</p></li></a>';
											}
										}
										if($('.fd-content ul').eq(j).hasClass('odd')){
											r2 += 1;
											flag = 2;
										}
										switch(r1){
											case 1:
												$('.fd-content ul').eq(j).find('.item').eq(r2).html(ele);
												i++;j++;
												break;
											case 2:
												if((i+1)<len){
													
													if(!datas[i+1].donated){
														if(datas[i+1].id == userId){
															ele += ',<li class="nodonate self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}else{
															ele += ',<li class="nodonate" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}
														
													}else{
														if(datas[i+1].id == userId){
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}else{
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}
													}
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(flag-1).html(ele.split(',')[0]);
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(flag).html(ele.split(',')[1]);
												}else{
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(1).html(ele);
												}
												i+=2;j++;
												break;
											case 3:
												if((i+2)<len){
													if(!datas[i+1].donated){
														if(datas[i+1].id == userId){
															ele += ',<li class="nodonate self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}else{
															ele += ',<li class="nodonate" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}
													}else{
														if(datas[i+1].id == userId){
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}else{
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}
													}
													if(!datas[i+2].donated){
														if(datas[i+2].id == userId){
															ele += ',<li class="nodonate self" data-id="'+datas[i+2].id+'"><p class="content">'+datas[i+2].name+'：</p><p>到此一游</p><p class="time">'+datas[i+2].time+'</p></li>';
														}else{
															ele += ',<li class="nodonate" data-id="'+datas[i+2].id+'"><p class="content">'+datas[i+2].name+'：</p><p>到此一游</p><p class="time">'+datas[i+2].time+'</p></li>';
														}
													}else{
														if(datas[i+2].id == userId){
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+2].id+'"><li class="donated self" data-id="'+datas[i+2].id+'"><p class="content">'+datas[i+2].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+2].time+'</p></li></a>';
														}else{
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+2].id+'"><li class="donated" data-id="'+datas[i+2].id+'"><p class="content">'+datas[i+2].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+2].time+'</p></li></a>';
														}
													}
													
													$('.fd-content ul').eq(j).find('.item').eq(flag-1).html(ele.split(',')[0]);
													$('.fd-content ul').eq(j).find('.item').eq(flag).html(ele.split(',')[1]);
													$('.fd-content ul').eq(j).find('.item').eq(flag+1).html(ele.split(',')[2]);
													i += 3;j++;
												}else if((i+1)<len){
													if(!datas[i+1].donated){
														if(datas[i+1].id == userId){
															ele += ',<li class="nodonate self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}else{
															ele += ',<li class="nodonate" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>到此一游</p><p class="time">'+datas[i+1].time+'</p></li>';
														}
													}else{
														if(datas[i+1].id == userId){
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated self" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}else{
															ele += ',<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&userId='+datas[i+1].id+'"><li class="donated" data-id="'+datas[i+1].id+'"><p class="content">'+datas[i+1].name+'：</p><p>为善园添了块砖</p><p class="time">'+datas[i+1].time+'</p></li></a>';
														}
													}
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(flag-1).html(ele.split(',')[0]);
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(flag).html(ele.split(',')[1]);
													i += 2;j++;
												}else{
													$('.fd-content ul').eq(j).find('.item:not(:eq('+r2+'))').eq(1).html(ele);
													i += 1;j++;
												}
												break;
											default:
												continue;
										}
											
									}
								}
								$('.donated').each(function(){
									var r = Math.floor(Math.random()*4);
									if($(this).css('background-color')=='rgba(0, 0, 0, 0)' || $(this).css('background-color')=='transparent'){
										$(this).css({'background':colors[r]});
									}
								});
								$('.nodonate').each(function(index,ele){
									$(this).on('click',function(){
										$('.leaveWordContent p').eq(index*2).show().next().show();
									});
								});
								var count = 0;
								for (var i=$('.fd-content ul').length-1;i>0;i--) {
									if($('.fd-content ul').eq(i).text() == ''){
										count++;
									}else{
										break;
									}
								}
								if(count>1){
									for (var i=0;i<count-1;i++) {
										$('.fd-content ul:last-child').remove();
									}
									if($('.fd-content ul:last-child').hasClass('odd')){
										$('.fd-content ul:last-child').remove();
									}
								}
							}
						if($('.fd-content ul').length<8){
							var length = $('.fd-content ul').length;
							for (var i=0;i<8-length;i++) {
								if($('.fd-content ul:last-child').hasClass('odd')){
									$('.fd-content').append('<ul class="even"><div class="item"><li class="clickme" data-id=""></li></div><div class="item"><li class="clickme" data-id=""></li></div><div class="item"><li class="clickme" data-id=""></li></div><div class="item"><li class="null" data-id=""></li></div></ul>');
								}else{
									$('.fd-content').append('<ul class="odd"><div class="item"><li class="null" data-id=""></li></div><div class="item"><li class="clickme" data-id=""></li></div><div class="item"><li class="clickme" data-id=""></li></div><div class="item"><li class="clickme" data-id=""></li></div></ul>');
								}
							}
							
						}
						if(result.items.length < pageNum) {
							$(".loadmore a").html("没有更多数据了");
						}
						}else{
							for (var i=0;i<8;i++) {
								if($('.fd-content ul:last-child').hasClass('odd')){
									$('.fd-content').append('<ul class="even"><a href="javascript:;"><li class="clickme" data-id=""></li></a><a href="javascript:;"><li class="clickme" data-id=""></li></a><a href="javascript:;"><li class="clickme" data-id=""></li></a><a href="javascript:;"><li class="null" data-id=""></li></a></ul>');
								}else{
									$('.fd-content').append('<ul class="odd"><a href="javascript:;"><li class="null" data-id=""></li></a><a href="javascript:;"><li class="clickme" data-id=""></li></a><a href="javascript:;"><li class="clickme" data-id=""></li></a><a href="javascript:;"><li class="clickme" data-id=""></li></a></ul>');
								}
							}
							$(".loadmore a").html("没有更多数据了");
						}
					
						
					}
					
			});
		}
	},
	addLeaveWord:function(userId,projectId,content){
		$.ajax({
			url:dataUrl.addLeaveWord,
			data:{leavewordUserId: userId,projectId: projectId,content: content,t: new Date().getTime()},
			type:'POST',
			success:function(res){
				if(res.flag==1){
					layer.msg(res.errorMsg);
					setTimeout(window.location=base+'message/messageWall_view.do?show=0&projectId='+projectId,500)
					
				}else{
					layer.msg(res.errorMsg);
				}
			},
			error:function(e){
				layer.msg(e);
			}
		})
	}
};
feedbackWall.init();