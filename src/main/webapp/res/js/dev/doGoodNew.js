var base = window.baseurl;
var batchNum = 10,
	batchFlag = 0;
var dataUrl = {
	items: base + "project/list_new.do",
	provinceUrl: base + "project/provinceData.do",
	viewItem: base + "project/view/"
};
require.config({
	baseUrl: baseurl + "/res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"util": "dev/common/util",
		"dialog": "dev/common/dialog",
		"head": "dev/common/headNew",
		"entry": "dev/common/entryNew",
		"pages": "dev/common/pages"
	},
	urlArgs: "v=20151116"
});
define(["extend", "head", "entry", "pages"], function($, h, en, p) {
	window.sitePage = "p-doGood";
	h.init();
	en.init();
	p.init({
		afterObj: $("#doGoodList")
	});
	p.changeCallback = function() {
		items.getItem(p.curPage);
	}
	/*====items====*/
	/*200:保存(保存未提交); 201:提交(提交未审核); 202:审核未通过; 203:审核通过; 204:募捐中; 205:募捐完成; 206:执行中;*/
	var items = {
		typeFlag: -1,
		statusFlag: -1,
		locationFlag: -1,
		tagFlag: -1,
		sortArray: 0,
		sortArrayOld: 0,
		init: function() {
			var that = this,
				typeName = $("#typeName").val(),
				userId = $("#userId").val(),
				tagName = $('#tagName').val();
			that.provinceShow();
			if(typeName) {
				for(var i = 0; i <= $('#fieldList a').length; i++) {
					if($('#fieldList a').eq(i).html() == typeName) {
						$('#fieldList a').removeClass('curTab').eq(i).addClass('curTab');
					};
				}
			}
			if(tagName) {
				for(var i = 0; i <= $('#tagList a').length; i++) {
					if($('#tagList a').eq(i).html() == tagName) {
						$('#tagList a').removeClass('curTab').eq(i).addClass('curTab');
					};
				}
			}
			$('.filterNew .checkbox').click(function() {
				if($(this).hasClass('on')) {
					$('.filterNew .checkbox').removeClass('on');
					$('.doGoodList li .checkbox').removeClass('on');
					batchFlag = 0;
					$(".backBtn").addClass("forbidBtn");
				} else {
					$('.filterNew .checkbox').addClass('on');
					$('.doGoodList li .checkbox').each(function() {
						if((!$(this).parents("li").hasClass("finish")) && (!$(this).hasClass("on"))) {
							$(this).trigger("click");
						}
					});
					batchNum = batchFlag;
				}
			});
			$("body").on("click", ".selectorNew dd a", function() {
				if(!$(this).hasClass("curTab")) {
					$(this).addClass("curTab").siblings().removeClass("curTab");
					that.getItem();
				}
			});
			$('.sort a').click(function() {
				var i = $('.sort a').index(this);
				$('.sort a').removeClass('on').eq(i).addClass('on');
				var sortStatu = $(this).children('em').hasClass('desc');
				$('.sort a').children('em').removeClass('asc').removeClass('desc').addClass('desc');
				sortStatu ? $(this).children('em').removeClass('desc').addClass('asc') : $(this).children('em').removeClass('asc').addClass('desc');
				if(i == 0) {
					that.sortArray = 0;
				} else if(i == 1) {
					sortStatu ? that.sortArray = 5 : that.sortArray = 1;
				} else if(i == 2) {
					sortStatu ? that.sortArray = 6 : that.sortArray = 2;
				} else if(i == 3) {
					sortStatu ? that.sortArray = 7 : that.sortArray = 3;
				}
				that.getItem();
			});
		},
		provinceShow: function() {
			var that = this, province = $('#province').val();
			$.ajax({
				url: dataUrl.provinceUrl,
				data: {
					page: 1,
					t: new Date().getTime()
				},
				cache: false,
				success: function(result) {
					if(result.flag == '1') {
						var html = [],
							data = result.obj,
							len = data.length;
						province ? html.push('<a href="javascript:void(0);" title="" v="0">全部</a>') : html.push('<a href="javascript:void(0);" title="" v="0" class="curTab">全部</a>');
						if(len > 0) {
							for(var i = 0; i < len; i++) {
								if(data[i].province == province) {
									html.push('<a href="javascript:void(0);" v="' + data[i].province + '" title="" class="curTab">' + data[i].province + '(' + data[i].roadAddress + ')' + '</a>');
								} else if(data[i].province != null) {
									html.push('<a href="javascript:void(0);" v="' + data[i].province + '" title="">' + data[i].province + '(' + data[i].roadAddress + ')' + '</a>');
								}
							}
							$('#provinceList').html(html.join(''));
						}
					} else if(result.flag == '0') {
						d.alert({
							content: result.errorMsg,
							type: 'error'
						});
					}
					that.getItem();
				},error:function(){
					that.getItem();
				}
			});
		},
		getItem: function(page) {
			var that = this,
				typeName = $('#fieldList a.curTab'),
				curTabs = $(".selectorNew a.curTab"),
				typeName = curTabs.eq(2).attr("v"),
				locations = curTabs.eq(0).attr("v"),
				statu = curTabs.eq(3).attr("v"),
				sortType = that.sortArray,
				tag = curTabs.eq(1).attr("v");
				
			if(curTabs.length == 2) {
				typeName = curTabs.eq(0).attr("v");
				var province = $('#province').val();
				province ? locations = province : locations = 0;
				statu = curTabs.eq(1).attr("v");
			}
			if(!statu) {
				statu = 0;
			}
			$('.filterNew .checkbox').removeClass('on');
			var keyWords = $('#keyW').val();
			var flag = false;
			if(this.typeFlag != typeName || this.statusFlag != statu || this.locationFlag != locations || this.sortArrayOld != sortType || this.tagFlag != tag) {
				flag = true;
				this.tagFlag = tag;
				this.typeFlag = typeName;
				this.statusFlag = statu;
				this.locationFlag = locations;
				this.sortArrayOld = sortType;
				$("#lstPages").hide();
			}
			var userId = $("#userId").val();
			if(!page) {
				page = 1;
			}
			$.ajax({
				url: dataUrl.items,
				data: {
					len: 10,
					page: page,
					typeName: typeName,
					tag: tag,
					location: locations,
					status: statu,
					sortType: sortType,
					keyWords: keyWords,
					t: new Date().getTime()
				},
				cache: false,
				success: function(data) {
					if(!!data) {
						data = eval(data);
						if(data.result == 0) {
							var total = data.total,
								pageNum = data.pageNum,
								items = data.items,
								len = items.length,
								html = [];
							len = len > pageNum ? pageNum : len;
							var itemDtil = dataUrl.viewItem + "?projectId=";
							//if(total > 1){flag = true}
							for(var i = 0; i < len; i++) {
								var _item = items[i];
								var donateMoney = _item.donaAmount,
									helpMoney = _item.cryMoney,
									schedule;
								schedule = ((donateMoney / helpMoney) * 100);
								schedule = schedule > 100 ? 100 : parseInt(schedule);
								//if((_item.donatePercent == 100 || _item.state != 240 && (_item.special_fund_id == null || _item.special_fund_id == 0)) && _item.isNeedVolunteer != 1) {
								if((_item.state == 260 && (_item.special_fund_id == null || _item.special_fund_id == 0)) && _item.isNeedVolunteer != 1) {
									html.push('<li class="finish">');
								} else {
									html.push('<li>');
								}
								if(_item.imageUrl == null){
									_item.imageUrl = 'http://www.17xs.org/res/images/logo-def.jpg';
								}
								html.push('<div class="goodPic"><a href="' + itemDtil + _item.itemId + '" title="" target="_blank"><img src="' + _item.imageUrl + '" alt=""></a></div>');
								html.push('<div class="goodDetail">');
								html.push('<h2><a href="' + itemDtil + _item.itemId + '" title="" target="_blank">' + _item.title + '</a></h2>');
								if(_item.information != null && _item.information != 'null') {
									html.push('<p class="info">' + _item.information + '</p>');
								} else {
									html.push('<p class="info"></p>');
								}
								if(_item.special_fund_id == null || _item.special_fund_id == 0) {
									html.push('<p>筹款目标：<strong class="fontArial">' + _item.cryMoney + '</strong>元</p>');
								}
								html.push('<p>发布时间：<em class="fontArial">' + (new Date(_item.publishTime)).pattern("yyyy-MM-dd") + '</em></p>');
								if(_item.userType == "individualUsers") {
									html.push('<p>发 起 人：' + _item.realName + '</p>');
								} else {
									html.push('<p>发 起 人：' + _item.workUnit + '</p>');
								}
								html.push('</div>');
								html.push('<div class="goodOpear">');
								//if(_item.donatePercent == 100 || _item.state != 240 && (_item.special_fund_id == null || _item.special_fund_id == 0)) {} else {
								if(_item.state == 260 && (_item.special_fund_id == null || _item.special_fund_id == 0)) {} else {
									html.push('<a href="javascript:void(0);" onclick=sign(this) class="checkbox"></a>');
								}
								//if(_item.donatePercent == 100 || _item.state != 240 && (_item.special_fund_id == null || _item.special_fund_id == 0)) {
								if(_item.state == 260 && (_item.special_fund_id == null || _item.special_fund_id == 0)) {
									html.push('<p>项目状态：募捐完成</p>');
									html.push('<p>已募集<span class="orangeText fontArial">' + $.formatNum(_item.donatAmount) + '</span>元,共捐款<span class="ml15 orangeText fontArial">' + _item.donationNum + '</span>人次</p>');
								} else {
									html.push('<p>项目状态：募捐中</p>');
									html.push('<p>已募集<span class="orangeText fontArial">' + $.formatNum(_item.donatAmount) + '</span>元,共捐款<span class="ml15 orangeText fontArial">' + _item.donationNum + '</span>人次</p>');
								}
								html.push('<div class="progress"><span class="pgGray"><span class="pgRed" style="width:' + _item.donatePercent + '%"></span></span><span class="pgText">' + _item.donatePercent + '%</span></div>');
								//if((_item.donatePercent == 100 || _item.state != 240 && (_item.special_fund_id == null || _item.special_fund_id == 0)) && _item.isNeedVolunteer != 1) {
								if((_item.state == 260 && (_item.special_fund_id == null || _item.special_fund_id == 0)) && _item.isNeedVolunteer != 1) {
									html.push('<p><b href="javascript:void(0);" title="" class="projectBtn grayBtn">募捐完成</b></p>');
								//} else if((_item.donatePercent == 100 || _item.state != 240) && _item.isNeedVolunteer == 1) {
								} else if((_item.state == 260) && _item.isNeedVolunteer == 1) {
									html.push('<p><a href="http://www.17xs.org/project/addVolunteerView.do?itemId=' + _item.itemId + '" title="" class="projectBtn">志愿者报名</a></p>');
								} else {
									if(userId == null || userId == "") {
										html.push('<p><a href="http://www.17xs.org/visitorAlipay/visitorpay.do?projectId=' + _item.itemId + '&amount=' + _item.leaveCryAmount + '&extensionPeople=' + $('#extensionPeople').val() + '&pName=' + _item.title + '" title="" class="projectBtn">我要捐款</a>');
									} else {
										html.push('<p><a href="http://www.17xs.org/alipay/commondpay.do?projectId=' + _item.itemId + '&amount=' + _item.leaveCryAmount + '&extensionPeople=' + $('#extensionPeople').val() + '&pName=' + _item.title + '" title="" class="projectBtn">我要捐款</a>');
									}
									if(_item.isNeedVolunteer == 1) {
										html.push('&nbsp;<a href="http://www.17xs.org/project/addVolunteerView.do?itemId=' + _item.itemId + '" title="" class="projectBtn">志愿者报名</a>');
									}
									html.push('</p>');
								}
								html.push('</div>');
								html.push('</li>');
							}
							itemsObj.html(html.join(''));
							if(total > 1 && flag) {
								/*var firstObj=$("#items").children().first(),
									itemH=firstObj.height();
									itemGapT=firstObj.css("margin-top"),
									itemGapB=firstObj.css("margin-bottom");
									itemH=itemH+parseInt(itemGapT)+parseInt(itemGapB); 
								
								$("#items").height(itemH*3+"px"); */
								p.pageInit({
									pageLen: total,
									isShow: true
								});
							} else if(total < 2) {
								p.pageInit({
									pageLen: total,
									isShow: false
								});
							}
							$("html,body").scrollTop(0); //.animate({scrollTop: "0px"}, 500); 
						} else if(data.result == 1) {
							//itemsObj.html('<li class="prompt"><div class="nodata"></div></li>');
							if(status == 0) {
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有募捐中项目 <a title="">请查看其它类别~</a></div></li>');
							} else if(status == 1) {
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有执行中项目 <a title="">请查看其它类别~</a></div></li>');
							} else {
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有已完成项目 <a title="">请查看其它类别~</a></div></li>');
							}
						} else if(data.result == 2) {
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');
						}
					}
				}
			});
		}
	};
	var itemsObj = $("#doGoodList"),
		itemW = 280;
	$(".selectorNew dd").first().children("a").first().addClass("curTab");
	$(".selectorNew dd").eq(1).children("a").first().addClass("curTab");
	$(".selectorNew dd").last().children("a").first().addClass("curTab");
	items.init();
});

function sign(e) {
	if(!$(e).parents("li").hasClass("finish")) {
		if(!$(e).hasClass("on")) {
			$(e).addClass("on");
			batchFlag += 1;
		} else {
			$(e).removeClass("on");
			batchFlag -= 1;
		}
	}
	if(batchNum > batchFlag) {
		$('.filterNew .checkbox').removeClass('on');
	} else {
		$('.filterNew .checkbox').addClass('on');
	}
	if(batchFlag >= 2) {
		$(".backBtn").removeClass("forbidBtn");
	} else {
		$(".backBtn").addClass("forbidBtn");
	}
}

function batchTab() {
	if(batchFlag >= 2) {
		var url = "http://www.17xs.org/batchpay/batchpay.do?pList=";
		$('.doGoodList li .checkbox').each(function() {
			if($(this).hasClass("on")) {
				var href = $(this).siblings("p").children("a").attr("href");
				var projectId = href.split("?")[1].split("&")[0].split("=")[1];
				url = url + projectId + ",";
			}
		});
		url = url.substring(0, url.length - 1) + '&extensionPeople=' + $('#extensionPeople').val();
		location.href = url;
	} else {
		return false;
	}
}