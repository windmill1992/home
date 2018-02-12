
var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
	//togetherConfigList:base+'together/loadTogetherConfigList.do'
	togetherDonateList:base+'together/loadTogetherDonateList.do'
};
var page=1;
var record = {
	init:function(){
		var that = this;
		that.togetherDonateList(1,10);
	},
	togetherDonateList:function(page,pageNum){
		var projectId = $('#projectId').val();
		var data = [];
		$.ajax({
			url:dataUrl.togetherDonateList,
			data:{projectId: projectId,page: page,pageNum: pageNum,t: new Date().getTime()},
			type:'GET',
			success:function(result){
				var total = result.total;
				if(result.result != 2) {
					if(result.items.length>0){
						for(var i=0;i<result.items.length;i++){
							var datas = result.items;
							if(i==0 && page==1){
								data.push('<div class="list-top3">');
							}else if(i==3 && page==1){
								data.push('</div><div class="list">');
							}else{}
							var avatar = datas[i].coverImageUrl==null?'http://www.17xs.org/res/images/detail/people_avatar.jpg':datas[i].coverImageUrl;
							var name = datas[i].nickName==''?'爱心人士':datas[i].nickName;
							if(datas[i].type=='0'){
								data.push('<a href="http://www.17xs.org/together/raiseDetail_view.do?projectId='+projectId+'&shareUserId='+datas[i].userId+'">');
								data.push('<div class="list-item">');
								data.push('<div class="item-info clearFix">');
								data.push('<div class="pic">');
								data.push('<img src="'+avatar+'" width="100%" height="100%"/>');
								data.push('</div>');
								data.push('<div class="info">');
								data.push('<p><span class="nick">'+name+'</span></p><p>发起一起捐</p>');
								data.push('<p>和<span>'+datas[i].donateNum+'</span>位小伙伴</p>');
								data.push('</div><div class="money tog"><span>'+datas[i].donateMoney+'</span>元</div>');
								data.push('</div>');
								data.push('</div></a>');
								
							}else if(datas[i].type=='1'){
								data.push('<a href="javascript:void(0);"><div class="list-item">');
								data.push('<div class="item-info clearFix">');
								data.push('<div class="pic">');
								data.push('<img src="'+avatar+'" width="100%" height="100%"/>');
								data.push('</div>');
								data.push('<div class="info">');
								data.push('<p><span class="nick">'+name+'</span></p>');
								if(page==1 && i<3){
									data.push('<p>献爱心</p>');
								}else{}
								data.push('<p>累计捐赠<span>'+datas[i].donateNum+'</span>次</p>');
								data.push('</div><div class="money"><span>'+datas[i].donateMoney+'</span>元</div>');
								data.push('</div>');
								data.push('</div></a>');
							}
						}
						data.push('</div>');
					}
					if(page == 1){
						$('#recordList').append(data.join(''));
						var $a = $('#recordList .list-top3 a');
						$a.parent().height($a.parent().height());
						$a.eq(0).height($a.parent().height()+25);
						if($a.length == 1){
							$a.css({width:'50%'}).find('.list-item').addClass('first').parent().parent().css({'justify-content':'center'});
							$a.parent().css({'background-size':'55%'});
						}else if($a.length == 2){
							$a.parent().css({'padding':'0 10%'});
							$a.eq(0).css({width:'50%'}).find('.list-item').addClass('first');
							$a.eq(1).find('.list-item').addClass('second');
							$a.parent().css({'background-size':'45%','background-position':$a.offset().left-20+'px'});
						}else{
							$a.eq(0).find('.list-item').addClass('first');
							$a.eq(1).find('.list-item').addClass('second');
							$a.eq(0).before($a.eq(1));
						}
					}else{
						$('#recordList .list').append(data.join(''));
					}
					
					if(result.items.length<pageNum || total<=page*pageNum){
						$('.loadmore a').html("没有更多数据了").css({color:'#999'});
					}
				}else{
					$('.loadmore').html('暂无数据...');
				}
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','.loadmore a',function(){
			if($('.loadmore a').text()=='点击加载更多'){
				page++;
				that.togetherDonateList(page,10);
			}
		});
	}
};

record.init();
record.bindEvent();