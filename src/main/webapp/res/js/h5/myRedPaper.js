var base = 'http://www.17xs.org/';
var dataUrl = {
	mySendRedpackets: base + 'redPackets/mySendRedpackets.do',
	redPaperUsed: base + 'redPackets/redPaperUsed.do'
};
var page = 1, state = '1';
var queryRedPackets = {
	init: function() {
		var that = this;
		$(".tabBox li").on('click', function() {
			page = 1, state = $(this).attr('data-state');
			$('#redPaperList').html('');
			$(this).addClass("active").siblings().removeClass("active");
			if(state == '1'){
				that.sendPacketsList(page,10);//已发出
			}else if(state == '2'){
				that.queryPacketsList(page,10);//已捐出
			}else{
				that.sendPacketsList(page,10,202);//已退回
			}
		});
		$(document).on('click', '#loadMoreA', function() {
			if($(this).hasClass('no'))return;
			if(state == '1'){
				that.sendPacketsList(++page,10);
			}else if(state == '2'){
				that.queryPacketsList(++page,10);
			}else{
				that.sendPacketsList(++page,10,202);
			}
		});
		that.sendPacketsList(page,10);
	},
	sendPacketsList: function(pageNum,pageSize,status) {
		$.ajax({
			url: dataUrl.mySendRedpackets,
			dataType: 'json',
			type: 'post',
			data: {
				pageNum: pageSize,
				page: pageNum,
				status: status
			},
			success: function(data) {
				if(data.flag == 1) {
					var ret = data.obj.data;
					var html = [];
					var total = data.obj.nums;
					if(status == undefined){
						for(var i in ret) {
							var r = ret[i], redName = "";
							if(r.type == 1) {
								redName = "平均红包";
							} else {
								redName = "随机红包";
							}
							detailUrl = "/redPackets/receiveRedPackets.do?redpacketsId=" + r.id;
							html.push('<a href="' + detailUrl + '" class="left"><li><h3>' + redName + '</h3>');
							html.push('<p>' + r.spare + '</p><span class="money">¥ ' + r.totalamount + '元</span>');
							html.push('<em class="munber_em">' + r.usednum + '/' + r.totalnum + '个</em></li></a>');
						}	
					}else{
						for(var i in ret) {
							var r = ret[i], redName = "";
							if(r.type == 1) {
								redName = "平均红包";
							} else {
								redName = "随机红包";
							}
							html.push('<a href="javascript:;" class="right"><li><h3>' + redName + '</h3><p>已过期</p>');
							html.push('<span class="money">¥ ' + r.totalamount + '元</span></li>');
						}
					}
					
					$('#redPaperList').append(html.join(''));
					if(total > 0 && total <= pageNum * pageSize) {
						$('#loadMoreA').html('没有更多数据了').addClass('no');
					}else if(total == 0){
						$('#loadMoreA').html('暂无红包数据！').addClass('no');
					}else{
						$('#loadMoreA').html('查看更多红包！').removeClass('no');
					}
				} else {
					$('#loadMoreA').html('暂无红包数据！').addClass('no');
				}
			}
		});
	},
	queryPacketsList: function(pageNum,pageSize) {
		//<!-- 已使用start-->
		$.ajax({
			url: dataUrl.redPaperUsed,
			dataType: 'json',
			type: 'post',
			data: {
				pageNum: pageSize,
				page: pageNum,
				status: 402
			},
			success: function(data) {
				if(data.flag == 1) {
					var ret = data.obj.data;
					var html = [];
					var total = data.obj.nums;
					for(var i in ret) {
						var r = ret[i];
						html.push('<a href="javascript:;" class="center"><li><div class="redPaperLeft">');
						html.push('<img src="' + r.coverImageUrl + '"></div><div class="redPaperRight">');
						html.push('<h3>' + r.pTitle + '</h3><p><span>献出<i>' + r.amount + '</i>元红包</span>');
						html.push('<span class="fr date">' + r.showDate + '</span></p></div></li>');
					}
					$('#redPaperList').append(html.join(''));
					if(total > 0 && total <= pageNum * pageSize) {
						$('#loadMoreA').html('没有更多数据了').addClass('no');
					}else if(total == 0){
						$('#loadMoreA').html('暂无红包数据！').addClass('no');
					}else{
						$('#loadMoreA').html('查看更多红包！').removeClass('no');
					}
				} else {
					$('#loadMoreA').html('暂无红包数据！').addClass('no');
				}
			}
		});
	}
};
$(function(){
	queryRedPackets.init();
});