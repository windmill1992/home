/*
* 2017-8-3
* 
*/
var base = 'http://www.17xs.org';
var dataUrl = {
	newsList:base+'/778/news/ajaxLoadNewList.do'
};
var page = 1;
var resCenter = {
	init:function(){
		this.bindEvent();
		if($('#type').length>0){
			this.newsList(page,10);
		}
		var that = this;
		
		$(function(){
			
			$('#banner .hd').css({'margin-left':'-'+$('#banner .hd').width()/2+'px'});
		
			if($('.res-project .showBox .d_img li').length<=2){
				$('.res-project .showBox .arrow').hide();
			}
			$('.res-news .bd li .info-news p').each(function(){
				if($(this).text().length>100){
					$(this).text($(this).text().substr(0,101)+'...');
				}
			});
			$('.picScroll-left').each(function(){
				if($(this).find('.bd li').length>3){
					$(this).slide({mainCell:".bd ul",autoPage:true,effect:"leftLoop",autoPlay:true,vis:3,scroll:3,delayTime:1000});
				}else{
					var w = $(this).find('.bd ul').width();
					$(this).find('.bd ul').width(w+50);
				}
			});
			$('.picMarquee-left').each(function(){
				if($(this).find('.bd li').length>5){
					$(".picMarquee-left").slide({mainCell:".bd ul",autoPlay:true,effect:"leftMarquee",vis:5,interTime:50});
				}else{
					var w = $(this).find('.bd ul').width();
					$(this).find('.bd ul').width(w+50);
				}
			});
			$('#d_tab1').each(function(){
				var _this = $(this);
				if($(this).find('li').length>2){
					$(this).banner3d({key:''+Math.random()*100,moveSpeed: 200,autoRollingTime: 4000,count:3,callback:function(t){
						if(t>=_this.find('li').length-1){t = -1;}
						_this.find('li').removeClass('active').eq(t+1).addClass('active');
					}});	
				}
			});
			$('.d_tab li .brief').each(function(){
				var text = $(this).text().trim();
				var len = 0;
				if($(this).parents('li').hasClass('active')){
					len = 45;
				} else {len = 32;}
				if(text.length>len){
					$(this).text(text.substr(0,len+1)+'...');
				}
			});
		});
	},
	bindEvent:function(){
		var that = this;
		var flag = 0;
		$('body').on('click','.res-project .c-name',function(){
			$(this).siblings().removeClass('active');
			$(this).addClass('active');
			var name = $(this).attr('href');
			$('.res-project .c-summary .dg-container').removeClass('showBox');
			$('.res-project .c-summary .dg-container[name="'+name+'"]').addClass('showBox');
			if($('.res-project .showBox .d_img li').length<=2){
				$('.res-project .showBox .arrow').hide();
			}
			if(!flag){
				$('#d_tab2').each(function(){
					var _this = $(this);
					if($(this).find('li').length>2){
						$(this).banner3d({key:''+Math.random()*100,moveSpeed: 200,autoRollingTime: 4000,count:3,callback:function(t){
							if(t>=_this.find('li').length-1){t = -1;}
							_this.find('li').removeClass('active').eq(t+1).addClass('active');
						}});	
					}
				});	
				flag = 1;
			}
		}).on('mouseenter','.res-project .c-name:not(.active)',function(){
			$(this).addClass('hover');
		}).on('mouseleave','.res-project .c-name:not(.active)',function(){
			$(this).removeClass('hover');
		}).on('mouseenter','.res-news .bd li',function(){
			$(this).find('.info-news').css({'left':0}).find('.btn-news a').fadeIn();
			$(this).find('.info-news p').css({'left':0});
			$(this).find('.pic img').css({'-webkit-transform':'scale(1.2)','transform':'scale(1.2)'});
		}).on('mouseleave','.res-news .bd li',function(){
			$(this).find('.info-news').css({'left':'-370px'}).find('.btn-news a').fadeOut();
			$(this).find('.info-news p').css({'left':'-370px'});
			$(this).find('.pic img').css({'-webkit-transform':'scale(1)','transform':'scale(1)'});
		}).on('mouseenter','.res-body .r-tab .tab-left a:not(.active)',function(){
			$(this).addClass('hover');
		}).on('mouseleave','.res-body .r-tab .tab-left a:not(.active)',function(){
			$(this).removeClass('hover');
		}).on('mouseenter','.res-news .info-news .btn-news a',function(){
			$(this).css({'background':'#ff9002','border-color':'#ff9002'});
		}).on('mouseleave','.res-news .info-news .btn-news a',function(){
			$(this).css({'background':'none','border-color':'#fff'});
		}).on('click','.be-list .loadmore',function(){
			if($(this).find('a').html() == '没有更多数据了' || $(this).find('a').html() == '暂无数据!' ){return;}
			$(this).find('a').html('<img src="http://www.17xs.org/res/images/h5/images/loading.gif" />正在加载...');
			setTimeout(function(){
				that.newsList(++page,10);
			},1000);
		});
	},
	newsList:function(pageNum,pageSize){
		var that = this;
		var type = $('#type').val();
		$.ajax({
			type:"get",
			url:dataUrl.newsList,
			data:{pageSize:pageSize,pageNum:pageNum,type:type},
			success:function(results){
				var res = results.result;
				if(!results.total){
					$('.be-list .loadmore a').html('暂无数据!').addClass('nomore');	
					return false;
				}
				var html = [];
				for(var i=0;i<res.length;i++){
					html.push('<a href="/778/news/detail.do?id='+res[i].id+'&type='+type+'"><div class="list-item clearFix">');
					html.push('<h3>'+res[i].title+'<span>'+res[i].createTime+'</span></h3>');
					html.push('<div class="info clearFix">');
					if(res[i].coverImageUrl!=='' && res[i].coverImageUrl!==null && res[i].coverImageUrl!==undefined){
						html.push('<div class="pic"><img src="'+res[i].coverImageUrl+'"/></div>');
					}
					html.push('<div class="content"><p>'+res[i].abstracts+'</p></div></div></div></a>');	
				}
				$('.be-list .event-list').append(html.join(''));
				if(results.state && pageSize*pageNum < results.total){
					$('.be-list .loadmore a').html('点击加载更多');	
				}else{
					$('.be-list .loadmore a').html('没有更多数据了').addClass('nomore');	
				}
			}
		});
	}
};

resCenter.init();
