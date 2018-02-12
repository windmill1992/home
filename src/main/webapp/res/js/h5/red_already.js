var base=window.baseurl;
var dataUrl={
	redPacketsList:base+'redPackets/redPaperList.do',
};
var page = 1;
var h5RedPacketsList={
	init:function(){
		var that=this;
		h5RedPacketsList.redList(1,10);
		$('#loadMoreA').on('click',function(){
			if($(this).hasClass('no'))return;
			h5RedPacketsList.redList(++page,10);
		});
	},
	redList:function(pageNum,pageSize){
		var redpacketsId = $("#redpacketsId").val();
		$.ajax({
			url:dataUrl.redPacketsList,
			data:{page:pageNum,len:pageSize,redpacketsId:redpacketsId,t:new Date().getTime()},
			success: function(result){
				if(data.flag == 1){
					var html=[];
					var ret = result.obj.data;
					var total = result.obj.total;
					var nums = result.obj.nums;   
					for (var i in ret) {
						var r = ret[i];
						html.push('<div class="redy_o">');
						html.push('<span class="alredy_fl fl">');
						
						if(r.headImageUrl == null){
							html.push('<img src="/res/images/logo-def.jpg" />');
						}else{
							html.push('<img src="'+r.headImageUrl+'" />');
						}
						html.push('</span><div class="alredy_fr fr">');
						html.push('<div class="alredy_1">');
						html.push('<p class="alredy_p1">'+r.nickName+'</p>');
						html.push('<span class="alredy_sp1">￥'+r.amount+'元</span>');
						html.push('</div><div class="alredy_2">');
						html.push('<p class="alredy_p2">'+r.showDate+'</p>');
						if(r.status == 403){
							html.push('<span class="alredy_sp2">已失效</span>');
						}else if(r.status == 402){
							html.push('<span class="alredy_sp2">已使用</span>');
						}else if(r.status == 401){
							html.push('<span class="alredy_sp2">未使用</span>');
						}
						html.push('</div></div></div>');
					}
					$('#redList').append(html.join(''));  
					if(total >0 && pageNum * pageSize >= total){
						$('#loadMoreA').html('没有更多数据了').addClass('no');
					}else if(total == 0){
						$('#loadMoreA').html('暂无红包数据！').addClass('no');
					}else{
						$('#loadMoreA').html('查看更多红包').removeClass('no');
					}
				}
			}
		});
	}
};
$(function(){
	h5RedPacketsList.init();
});
