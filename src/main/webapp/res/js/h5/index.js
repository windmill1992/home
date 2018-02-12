var base = window.baseurl;
var dataUrl = {
	//projectList : base + 'project/list_h5.do',
	projectList : base + 'project/list_h5_30.do',
	recommendList : base + 'project/recommendList_h5.do',// 推荐项目
	latestdonationlist : base + 'project/latestdonationlist.do'
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
	
	var slider2 = Swipe(document.getElementById('category'),{
		auto: false,
		continuous : false
	});
	
	var exp = $("#extensionPeople").val();
	var page = 1;
	var h5detail = {
		totle: 0,
		pageCurrent: 1,
		init: function() {
			var that = this;
			that.getTotalDonation();
			that.projectList(1, 10);
			that.recommendList(1, 2);
		},
		projectList: function(pageNum, pageSize) {
			var nt = new Date().getTime();
			$.ajax({
				url: dataUrl.projectList,
				data: {
					page: pageNum,
					len: pageSize,
					t: nt
				},
				success: function(result) {
					if(pageNum <= result.total) {
						var data = result.items,
							datas = data,
							len = data.length,
							html = [];
						for(var i = 0; i < len; i++) {
							if(datas[i].field == 'garden') {
								html.push('<a href="http://www.17xs.org/project/gardenview_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + exp + '&ts='+nt+'">');
							} else {
								html.push('<a href="http://www.17xs.org/project/view_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + exp + '&ts='+nt+'">');
							}
							html.push('<div class="item bg2">');
							if(datas[i].imageurl != null) {
								html.push('<img class="fl" src="' + datas[i].imageurl + '">');
							} else {
								html.push('<img class="fl" src="' + base + 'res/images/logo-def.jpg">');
							}
							if(datas[i].donaAmount >= datas[i].cryMoney) {
								html.push('<i class="corner"><img src="' + base + 'res/images/h5/images/corner_success.png"></i>');
							} else if(datas[i].state == 260 && (datas[i].special_fund_id == 0 || datas[i].special_fund_id == null)) {
								html.push('<i class="corner"><img src="' + base + 'res/images/h5/images/corner_finish.png"></i>');
							}
							html.push('<div class="t"><div class="tn"></div>');
							html.push('<div class="tt">' + datas[i].title + '</div>');
							if(datas[i].special_fund_id == 0 || datas[i].special_fund_id == null) {
								html.push('<div class="tp"><label>目标<span class="r">' + datas[i].cryMoney + '</span>元</label>');
								html.push('<label>已完成<span class="r">' + datas[i].process + '%</span></label></div>');
							}
							html.push('</div><div class="clear"></div></div></a>');
						}
						$('.list-box').append(html.join(''));
					} else {
						$(".load-more").html("没有更多数据了");
					}
				}
			});
		},
		recommendList: function(page, pageSize) {
			var nt = new Date().getTime();
			$.ajax({
				url: dataUrl.recommendList,
				data: {
					page: page,
					len: pageSize,
					t: nt
				},
				success: function(result) {
					if(result.result == 0) {
						var data = result.items,
							datas = data,
							len = data.length,
							html = [];
						for(var i = 0; i < len; i++) {
							if(datas[i].field == 'garden') {
								html.push('<a href="http://www.17xs.org/project/gardenview_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + exp + '&ts='+nt+'">');
							} else {
								html.push('<a href="http://www.17xs.org/project/view_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + exp + '&ts='+nt+'">');
							}
							html.push('<div class="li"><div class="ctx">');
							if(datas[i].imageurl != null) {
								html.push('<img src="' + datas[i].imageurl + '" class="img">');
							} else {
								html.push('<img src="' + base + 'res/images/logo-def.jpg" class="img">');
							}
							html.push('<p class="pp">' + datas[i].title + '</p>');
							if(datas[i].state == 240 && (datas[i].special_fund_id == 0 || datas[i].special_fund_id == null)) {
								html.push('<p class="ds">已募集<span>' + parseInt(datas[i].donaAmount) + '</span>元</p>');
							} else if(datas[i].state == 260 && (datas[i].special_fund_id == 0 || datas[i].special_fund_id == null)) {
								html.push('<p class="ds">已结束</p>');
							}
							html.push('<div style="clear:both;"></div>');
							html.push('</div></div></a>');
						}
						$('#hot').html(html.join(''));
					}
				}
			});
		},
		getTotalDonation:function(){
			var that = this;
			setInterval(function() {
				$.ajax({
					url: base + 'index/loadTotalMoney.do',
					data: {},
					type: 'GET',
					success: function(res) {
						$("#cur_num").val(res);
					}
				});
			}, 3000);
			var num = $("#cur_num").val();
			that.show_num(num);
			setInterval(function() {
				num = $("#cur_num").val();
				that.show_num(num);
			}, 3000);
		},
		show_num:function(n){
			n = String(n);
			var it = $(".num i"),
				len = n.length,
				temp = 0,
				numi = 0,
				y = 0,
				obj = null;
			for(var i = 0; i < len; i++) {
				if(it.length <= i) {
					$(".num").append("<i></i>");
				}
				numi = Number(n.charAt(i));
				if(numi == 0){
					temp = 1;
				}else if(numi == 9){
					temp = 0.8;
				}else{
					temp = 0.6;
				}
				y = -numi * 1600 / 100 + temp;
				obj = $(".num i").eq(i);
				obj.animate({
					'background-position-y': String(y) + 'px'
				}, 1400, 'swing', function() {});
			}
		}
	};
	
	$(function(){
		h5detail.init();
	});
});