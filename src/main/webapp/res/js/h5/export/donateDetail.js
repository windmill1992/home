/* 
 * 
 * 2017-6-13
 * 
 */
var base = window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl = {
	addTogetherConfig: base + "together/addTogetherConfig.do"
};
require.config({
	baseUrl: base + "/res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"ajaxform": "util/ajaxform"
	},
	urlArgs: "v=20170616"
});

define(["extend", "ajaxform"], function($, a) {
	var page = 1;
	var winH=$(window).height();
	var donateDetail = {
		init: function() {
			var that = this;
			$('#totalMoney').val(parseInt($('.dd-ul li.on').find('span').text()));
			$('.dd-ul li').on('click',function(){
				$('.dd-ul li').removeClass('on');
				$(this).addClass('on');
				if($(this).hasClass('user-defined')){
					$('.diyMoney').show();
					$('#dMoney').focus();
					$('.dd-money').height(150);
					$('#totalMoney').val(0);
				}else{
					$('.diyMoney').hide();
					$('.dd-money').height(100);
					$('#totalMoney').val(parseFloat($(this).find('span').text()));
				}
			});
			
			$('#dMoney').on('keyup',function(){
				$('#totalMoney').val(parseFloat($(this).val()).toFixed(2));
			});
			
			$('#chk').on('change',function(){
				var check = this.checked;
				if(!check){
					$('.payBtn').addClass('disable');
				}else{
					$('.payBtn').removeClass('disable');
				}
			});
			$('.payBtn').on('click',function(){
				if($(this).hasClass('disable'))return;
				var money = $('#totalMoney').val(),
				projectId = $("#projectId").val(),
				userId = $("#userId").val(),
				realName = $("#realName").val(),
				mobileNum = $("#mobileNum").val(),
				donateWord = $("#donateWord").val(),
				needmoney = $("#needmoney").val();
				if(donateWord == '') {
					donateWord = $('#donateWord').attr('placeholder');
				}
				if(money==0 || money==''){
					that.tips('请选择或填写有效金额!');
					return;
				}else if(!$('#chk').attr('checked')){
					that.tips('请先阅读并同意捐赠协议!');
					return;
				}
				needmoney = Number(needmoney);
				if(needmoney < money) {
					that.tips("最多捐款金额" + needmoney + "元");
					return;
				}
				location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?" +
					"projectId=" + projectId + "&amount=" + money + "&realName=" + realName + 
					"&extensionPeople="+$('#extensionPeople').val()+
					"&mobileNum=" + mobileNum + "&donateWord=" + donateWord+"&slogans=yunhu";
			});
		},
		tips:function(text){
			if($('.tips').is(':hidden')){
				$('.tips').text(text);
				$('.tips').show().animate({bottom:'120px'},'swing',function(){
					var _this = $(this);
					setTimeout(function(){
						_this.fadeOut(function(){
							$(this).css({bottom:'-40px'});
						});	
					},1000);
				});
			}else{return;}
		},
		page: function(pageSize, pageNum, type, flag) {
			$.ajax({
				url: dataUrl.newsList,
				data: {
					pageNum: pageNum,
					pageSize: pageSize,
					type: type,
					t: new Date().getTime()
				},
				success: function(result) {}
			});
		}
	};
	donateDetail.init();
});
