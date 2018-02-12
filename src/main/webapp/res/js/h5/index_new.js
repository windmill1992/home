var base = window.baseurl;
//var base="http://www.17xs.org:8080/"
var dataUrl = {
		loadFeedBackAndReportList: base + 'project/loadFeedBackAndReportList.do'
};
require.config({
	baseUrl : base + "/res/js",
	paths : {
		"jquery" : [ "jquery-1.8.2.min" ],
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util" : "dev/common/util",
		"head" : "dev/common/head",
		"entry" : "dev/common/entry",
		"userCenter" : "dev/common/userCenter",
		"ajaxform" : "util/ajaxform",
		"pages" : "dev/common/pages",
		"pageCommon" : "dev/common/pageCommon"
	},
	urlArgs : "v=20150511"
});

define([ "extend", "dialog", "head", "entry", "userCenter", "ajaxform",
		"pages", "pageCommon" ], function($, d, h, en, uc, f, p, p1) {
	window.uCENnavEtSite = "p-donationdetail";// 如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init();
	uc.init();
	p.init({
		afterObj : $("#pageNum")
	});
	p.changeCallback = function() {
		h5detail.delRecordList(p.curPage, 10);
	};
	uc.selectCallback = function() {
		h5detail.delRecordList(1, 10);
	};

	var slider = Swipe(document.getElementById('scroll_img'), {
		auto : 3000,
		continuous : true
	});

	var slider2=Swipe(document.getElementById('category'),{
		auto: false,
		continuous : false
	});
	
});

$(function(){
	var h5Index = {
		init:function(){
			var page = 1;
			var that = this;
			var timer = null;
			setInterval(function(){
				$.ajax({
					url:base+'index/loadTotalMoney.do',
					data:{},
					type:'GET',
					success:function(res){
						$("#cur_num").val(res);
					},
					error:function(e){}
				});
			},3000);
			var num = $("#cur_num").val();
			that.show_num(num);
			setInterval(function(){num = $("#cur_num").val();that.show_num(num);},3000);	
			$('body').on('click','.feed-ul li',function(){
				var sliders = '';
				var _this = $(this);
				$('#overLay').show();
				$('.big-pic').show().append('<div class="index"></div>');
				$('.big-pic .scroll').append('<div class="scroll_box" id="big-img"><ul class="scroll_wrap"></ul></div>');
				var lis = $(this).parent().find('li');
				for(var i=0;i<lis.length;i++){
					var src = lis.eq(i).find('img').attr("src");
					$('.big-pic .scroll_wrap').append('<li><img src="'+src+'" width="100%" /></li>');
					
				}
				var length = lis.length;
				var h1 = $('#overLay').height();
				sliders = Swipe(document.getElementById('big-img'), {
					auto : 0,
					continuous : false,
					startSlide:_this.index(),
					callback:function(){
						var h = $('.big-pic li').eq(sliders.getPos()).find('img').height();
						$('.big-pic .index').css({'margin-top':(h1-h)/2-30+'px'}).text(sliders.getPos()+1+'/'+length);
					}
				});
				$('.big-pic li').each(function(index,ele){
					var h2 = $('.big-pic li').eq(index).find('img').height();
					var h3 = $('.big-pic li').eq(_this.index()).find('img').height();
					$('.big-pic .index').css({'margin-top':(h1-h3)/2-30+'px'}).text(_this.index()+1+'/'+lis.length);
					$('.big-pic li').eq(index).find('img').css({'margin-top':(h1-h2)/2+'px'});	
				});
				
			}).on('click','.big-pic',function(){
				$('#overLay').hide();
				$('.big-pic').hide();
				$('.big-pic .scroll_wrap').html('').width(0);
				clearInterval(timer);
				$('.big-pic .index').remove();
				$('.big-pic #big-img').remove();
			}).on('click','.loadmore',function(){
				var _this = $(this);
				if($(this).find('a').length<1){return;}
				$(this).find('a').html('<img src="../res/images/h5/images/loading.gif" width="20" />正在加载...');
				setTimeout(function(){
					page++;
					that.projectFeadbackList(page,5);
				},800);
			});	
			
			$('.big-pic')[0].addEventListener('touchmove',function(event){
				event.preventDefault();
				event.stopPropagation();
			},false);
			
			
			that.projectFeadbackList(1, 5);
		},
		projectFeadbackList: function(pageNum, pageSize) {
			var that = this;
			$.ajax({
				url: dataUrl.loadFeedBackAndReportList,
				data: {
					page: pageNum,
					pageNum: pageSize,
					t: new Date().getTime()
				},
				type:'GET',
				success: function(result) {
					if(pageNum <= result.total) {
						var data = result.result,
							datas = data,
							len = data.length;
						var html = [];
						for(var i = 0; i < len; i++) {
							html.push('<div class="list-item">');
							html.push('<div class="user-pic"><a href="http://www.17xs.org/project/view/?projectId='+datas[i].projectId+'"><img src="'+datas[i].headImgUrl+'" width="40" /></a></div>');
							html.push('<div class="user-info">');
							html.push('<p class="user"><a href="http://www.17xs.org/project/view/?projectId='+datas[i].projectId+'" class="user-name">'+(datas[i].userName==null?'':datas[i].userName)+'</a></p>');
							html.push('<a href="http://www.17xs.org/project/view/?projectId='+datas[i].projectId+'"><p class="content">'+datas[i].content+'</p></a>');
							html.push('<div class="pic-list scroll-pic"><ul class="feed-ul swiper-wrapper">');
							if(datas[i].contentImgUrl!=null && datas[i].contentImgUrl.length>0){
								for(var j=0;j<datas[i].contentImgUrl.length;j++){
									if(datas[i].contentImgUrl.length>4){
										html.push('<li class="pic-slide"><a href="javascript:;"><img src="'+datas[i].contentImgUrl[j]+'" /></a></li>');
									}else{
										html.push('<li><a href="javascript:;"><img src="'+datas[i].contentImgUrl[j]+'" /></a></li>');
									}
								}
							}
							html.push('</ul>');
							html.push('</div>');
							html.push('</div>');
							html.push('<div style="clear: both;"></div>');
							html.push('</div>');
						}
						$('.feed-list').append(html.join(''));
						$(".loadmore").find('a').html('点击加载更多');
					} else {
						$(".loadmore").html("没有更多数据了");
					}
					
					new Swiper('.pic-list', {
						slidesPerView: 329/75,
						spaceBetween: 10,
						freeMode: true,
						slideClass:'pic-slide',
						slidesOffsetAfter:10
					});	
					$('.pic-list li').not('.pic-slide').width(($('.pic-list ul').width()+10)/329*75-10);
					$('.pic-list li').height(function(){return $(this).width();});
				}
			});
			
		},
		show_num:function(n){
			var it = $(".num i");
			var len = String(n).length;
			for(var i = 0; i < len; i++) {
				if(it.length <= i) {
					$(".num").append("<i></i>");
				}
				var num = String(n).charAt(i);
				var y = -parseInt(num) * 16.3 - (num==0?0:1);
				var obj = $(".num i").eq(i);
				obj.animate({
					backgroundPosition: '(0 ' + String(y) + 'px)'
				}, 1400, 'swing', function() {});
			}	
		}
	};

	h5Index.init();
	
});