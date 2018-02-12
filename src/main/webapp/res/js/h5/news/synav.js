var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
	newsList:base+'h5News/ajaxNewsList.do'
	
};
var projectTopic={
		init:function()
		{
			var that=this;
			mui.init({
				pullRefresh: {
					container: '.scroll',
					up: {
						contentrefresh: '正在加载...',
						contentnomore:'没有更多内容了',
						callback:function(){$('.load-more').html(''); setTimeout(function(){that.page(Number($('#currentPage').val()), 10, $('#type').val(),1)},1000)}
					}
				}
			});
			mui('.scroll-list').on('tap', 'a', function(event) {
				window.location.href = this.href;
			});
			
			
			$(".nav ul").width(function(){
				var w = $(this).find("li").width();
				var len = $(this).find("li").length;
				return w*len+30*(len-1)+'px';
			});
			$(".nav ul").height(function(){return $(this).find("li").height()+'px'});
			
			that.page(1, 10, $('#type').val(),0);
			/*$('body').on("click","#cszs",function(){
				$('#type').val('慈善知识');
				that.page(1, 10, '慈善知识');
			}).on("click","#sykx",function(){
				$('#type').val('善园快讯');
				that.page(1, 10, '善园快讯');
			}).on("click","#hd",function(){
				$('#type').val('活动');
				that.page(1, 10, '活动');
			}).on("click","#gonggao",function(){
				$('#type').val('公告');
				that.page(1, 10, '公告');
			}).on("click","#axgs",function(){
				$('#type').val('爱心故事');
				that.page(1, 10, '爱心故事');
			});*/
			
		},
		
		page:function(pageSize,pageNum,type,flag){
			$.ajax({
				url:dataUrl.newsList,
				data:{pageNum:pageNum,pageSize:pageSize,type:type,t:new Date().getTime()},
				success: function(result){
					var html=[];
					var data=result.obj;
					var value = Number($('#currentPage').val());
					if(value==1 || result.total-value*10>0){
						for(var i=0;i<result.size;i++){
							var create_time=data[i].addTime/1000;
							var timeString = $.myTime.UnixToDate(create_time);
							if(type=="慈善知识"){
								html.push('<a href="'+base+'h5News/newsDetail_view.do?type=慈善知识&id='+data[i].id+'"><li>'+data[i].title+'</li></a>');
							}else if(type=="善园快讯"){
								html.push('<div class="list-item">');
								html.push('<a href="'+base+'h5News/newsDetail_view.do?type=善园快讯&id='+data[i].id+'">');
								if(data[i].imgUrl!=null && data[i].imgUrl!=''){
									html.push('<div class="item-img"><img src="'+data[i].imgUrl+'" width="100%" height="100%"/></div>');
								}
								html.push('<div class="item-info">');
								html.push('<div class="item-title"><span>'+data[i].title+'</span></div>');
								html.push('<div class="item-time"><span>发布时间  : </span><span class="time">'+timeString+'</span></div>');
								html.push('</div>');
								html.push('</a>');
								html.push('</div>');
							}else if(type=="活动"){
								html.push('<div class="list-item">');
								html.push('<a href="'+base+'h5News/newsDetail_view.do?type=活动&id='+data[i].id+'">');
								html.push('<div class="item-title">'+data[i].title+'</div>');
								html.push('<div class="item-img">');
								//图片
								if(data[i].contentImgUrl!=null && data[i].contentImgUrl!=''){
									var imgUrls = data[i].contentImgUrl.split(',');
									for(var j=0;j<imgUrls.length;j++){
										if(imgUrls[j]!=''){
											html.push('<img src="'+imgUrls[j]+'" width="100" height="65"/>');
										}
									}
								}
								html.push('</div>');
								html.push('<div class="item-time">');
								html.push('<span>发布时间 : </span><span class="time">'+timeString+'</span>');
								html.push('</div>');
								html.push('</a>');
								html.push('</div>');
							}else if(type=="公告"){
								html.push('<div class="list-item">');
								html.push('<a href="'+base+'h5News/newsDetail_view.do?type=公告&id='+data[i].id+'">');
								if(data[i].imgUrl!=null && data[i].imgUrl!=''){
									html.push('<div class="item-img"><img src="'+data[i].imgUrl+'" width="100%" height="100%"/></div>');
								}
								html.push('<div class="item-info">');
								html.push('<div class="item-title"><span>'+data[i].title+'</span></div>');
								html.push('<div class="item-time"><span>发布时间  : </span><span class="time">'+timeString+'</span></div>');
								html.push('</div>');
								html.push('</a>');
								html.push('</div>');
							}else if(type=="爱心故事"){
								html.push('<div class="list-item">');
								html.push('<a href="'+base+'h5News/newsDetail_view.do?type=爱心故事&id='+data[i].id+'">');
								if(data[i].imgUrl!=null && data[i].imgUrl!=''){
									html.push('<div class="item-img"><img src="'+data[i].imgUrl+'" width="100%" height="100%"/></div>');
								}
								html.push('<div class="item-info">');
								html.push('<div class="item-title"><span>'+data[i].title+'</span></div>');
								html.push('<div class="item-time"><span>发布时间  : </span><span class="time">'+timeString+'</span></div>');
								html.push('</div>');
								html.push('</a>');
								html.push('</div>');
							}
						}
						if(type=="慈善知识"){
							$('.knowledge-ul').append(html.join(''));
						}else if(type=="善园快讯"){
							$('.news-list .load-more').before(html.join(''));
						}else if(type=="活动"){
							$('.scroll-list .load-more').before(html.join(''));
						}else if(type=="公告"){
							$('.scroll-list .load-more').before(html.join(''));
						}else if(type=="爱心故事"){
							$('.scroll-list .load-more').before(html.join(''));
						}
					}
					if(flag==1){
						if(result.total<=Number($('#currentPage').val())*10){
							mui('.scroll').pullRefresh().endPullupToRefresh(true); //参数为true代表没有更多数据了。
						}
						else{
							$('.load-more').html('上拉加载更多>>');
							mui('.scroll').pullRefresh().endPullupToRefresh(false);
						}
					}
					$('#currentPage').val(Number($('#currentPage').val())+1);
				}
			});
		}
	};
	projectTopic.init();