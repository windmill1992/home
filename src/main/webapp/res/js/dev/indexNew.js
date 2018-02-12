var base = window.baseurl;
require.config({
	baseUrl: base + "/res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"dialog": "dev/common/dialog",
		"util": "dev/common/util",
		"head": "dev/common/headNew",
		"entry": "dev/common/entryNew"
	},
	urlArgs: "v=20180206"
});
define(["extend", "dialog", "head", "entry"], function($, d, h, en) {
	window.sitePage = "p-index"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init();
	var xsMain = {
		init: function() {
			var that = this;
			$('#projectTab a').click(function() {
				var i = $('#projectTab a').index(this);
				$('#projectTab a').removeClass('on').eq(i).addClass('on');
				$('#projectCon dl').hide().eq(i).show();
			})
			$("#ignore").click(function() {
				$("#prompt_weixin").hide();
			});
			$('#pjProjectTab a').click(function() {
				var i = $('#pjProjectTab a').index(this);
				$('#pjProjectTab a').removeClass('on').eq(i).addClass('on');
				$('#pjProject .cityProject').hide().eq(i).show();
			});
			$('#dtList').myScroll({
				speed: 40, //数值越大，速度越慢
				rowHeight: 63 //dl的高度
			});
			$('#houseTab a').click(function() {
				var i = $('#houseTab a').index(this);
				$('#houseTab a').removeClass('on').eq(i).addClass('on');
				$('#house .houseList').hide().eq(i).show();
			});
			$('body').on('click', '#refreshCode', function() {
				en.rashCode('login');
			});
		}
	};
	var xsindex = {
		cur: 0,
		size: 0,
		init: function(o) {
			var w = window.screen.width;
			/*if(w<1000){
				$('.indexBnner .bnnerpic').css('width',w)
				$('.indexBnner .bnnerpic img').css('width',w);
			}*/
			var k = $("#scroll").children('a'),
				h = [],
				i = 0;
			this.size = k.size();
			k.css({left: "100%"});
			k.eq(0).css({"left": "0"});
			for(i; i < this.size; i++) {
				if(i == 0) {
					h.push('<a class="on"></a>')
				} else {
					h.push('<a></a>')
				}
			}
			$("#scrollNum").html(h.join(''));
			$('#slide_div a').click(function() {
				var i = $('#slide_div a').index(this);
				if(i == 0) {
					xsindex.prev();
				} else {
					xsindex.next();
				}
			})
			$('#scrollNum a').click(function() {
				var i = $('#scrollNum a').index(this);
				xsindex.index(i);
			})
			var ints = setInterval(xsindex.next, 4000)
			$('#slide_div').hover(function() {
				$('#slide_div .sbtn').fadeIn();
				clearInterval(ints);
			}, function() {
				$('#slide_div .sbtn').fadeOut();
				ints = setInterval(xsindex.next, 4000);
			})
		},
		next: function() {
			var k = $("#scroll").children('a'),
				n, c;
			if(xsindex.cur == (xsindex.size - 1)) {
				c = xsindex.cur;
				xsindex.cur = 0;
				n = 0;
			} else {
				c = xsindex.cur;
				n = c + 1;
				xsindex.cur++;
			}
			$('#scrollNum a').removeClass('on').eq(n).addClass('on');
			k.eq(c).css({'left': 0}).stop().animate({"left": "-100%"}, 300);
			k.eq(n).css({'left': "100%"}).stop().animate({"left": "0"}, 300);
		},
		prev: function() {
			var k = $("#scroll").children('a'),
				p, c;
			if(xsindex.cur == 0) {
				c = 0;
				p = xsindex.size - 1;
				xsindex.cur = p;
			} else {
				c = xsindex.cur;
				p = c - 1;
				xsindex.cur--;
			}
			$('#scrollNum a').removeClass('on').eq(p).addClass('on');
			k.eq(p).css({'left': '-100%'}).stop().animate({"left": 0}, 300);
			k.eq(c).css({'left': 0}).stop().animate({"left": "100%"}, 300);
		},
		index: function(x) {
			var k = $("#scroll").children('a'),
				n, c = xsindex.cur;
			if(x != xsindex.cur) {
				xsindex.cur = x;
				$('#scrollNum a').removeClass('on').eq(x).addClass('on');
				k.eq(c).css({'left': 0}).stop().animate({"left": "-100%"}, 300);
				k.eq(x).css({'left': "100%"}).stop().animate({"left": "0"}, 300);
			}
		}
	};
	xsindex.init();
	xsMain.init();
});