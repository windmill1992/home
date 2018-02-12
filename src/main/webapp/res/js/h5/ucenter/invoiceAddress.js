var base = 'http://www.17xs.org/';
var dataUrl = {
	getUserInfo: base +'user/userInfo.do',			//获取用户信息
	getDetailAddr: base +'user/detailAddress.do',	//获取用户地址信息
	editAddr: base +'user/editAdress.do',			//编辑保存
	addAddr: base +'user/addAdress.do'				//新增保存
};
var invoiceAddress = {
	timer:null,
	init:function(){
		var that = this, shtml = [];
		that.id = that.getParamString("id");
		that.pgId = that.getParamString("pgId");
		that.getUserInfo();
		that.getAddress();
		for(var p in objZhArea.area0) {
			shtml.push('<option value="' + p + '">' + objZhArea.area0[p] + '</option>');
		}
		$("#province").html(shtml.join(''));
		$('#province').scroller('destroy').scroller({
			preset: 'select',
			onSelect: function(text, obj) {
				that.initcity(obj.values[0], true, true);
			}
		});
		that.initcity('110000', true, true);
		that.bindEvent();
	},
	bindEvent:function(){
		var that = this,
			nameReg = /^[\u4e00-\u9fa5]{2,4}$/i,
			phoneReg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/,
			emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		//姓名
		$("#name").blur(function() {
			var uname = $("#name").val();
			var b = uname.match(nameReg);
			if(uname == '' || !b) {
				$("#name").parent().addClass('error');
			} else {
				$("#name").parent().removeClass('error');
			}
		});
		//手机号码
		$("#phone").blur(function() {
			var uphone = $("#phone").val();
			var b = uphone.match(phoneReg);
			if(uphone == '' || !b) {
				$("#phone").parent().addClass('error');
			} else {
				$("#phone").parent().removeClass('error');
			}
		});
		//地址
		$("#detailAddress").blur(function() {
			var udetailAddress = $("#detailAddress").val();
			if(udetailAddress == '') {
				$("#detailAddress").parent().addClass('error');
			} else {
				$("#detailAddress").parent().removeClass('error');
			}
		});
		//邮箱
		$("#email").blur(function() {
			var uemail = $("#email").val();
			var b = uemail.match(emailReg);
			if(uemail == '' || !b) {
				$("#email").parent().addClass('error');
			} else {
				$("#email").parent().removeClass('error');
			}
		});
	
		//提交按钮按下时，根据type不同，跳转到礼品(2)或者发票(1)页面或者地址添加页面(3)。
		var type = that.getParamString("type");
		$('#type').text(type);
		
		$('body').on('click', '#submitBtn', function() {
			var uname = $("#name").val(),
				uphone = $("#phone").val(),
				udetailAddress = $("#detailAddress").val(),
				uemail = $("#email").val(),
				uprovince = $("#province option:selected").text(),
				ucity = $("#city option:selected").text(),
				uarea = $("#area option:selected").text();
			if(uname == '' || !uname.match(nameReg)) {
				that.showTips('请输入正确的姓名！');
				$("#name").parent().addClass('error');
				return;
			}
			if(uphone == '' || !uphone.match(phoneReg)) {
				that.showTips('请输入正确的联系电话！');
				$("#phone").parent().addClass('error');
				return;
			}
			if(udetailAddress == '') {
				that.showTips('请输入正确的地址！');
				$("#detailAddress").parent().addClass('error');
				return;
			}
			if(uemail == '' || !uemail.match(emailReg)) {
				that.showTips('请输入正确的邮箱！');
				$("#email").parent().addClass('error');
				return;
			}
			$('.formItem').removeClass('error');
			if(type == 6) {
				$.ajax({
					url: dataUrl.editAddr,
					dataType: 'json',
					type: 'post',
					data: {
						id: that.id,
						name: uname,
						mobile: uphone,
						province: uprovince,
						city: ucity,
						area: uarea,
						detailAddress: udetailAddress,
						email: uemail
					},
					success: function(data) {
						if(data.flag == 1) {
							if(that.pgId != null && that.pgId.match("_")) {
								location.href = '/project/confirmCrowdfundingPrize.do?projectId=' + that.pgId.substring(0, that.pgId.indexOf("_")) + '&giftId=' + that.pgId.substring(that.pgId.indexOf("_") + 1);
							} else {
								location.href = "/ucenter/addressList.do";
							}
						} else {
							that.showTips('系统异常，请重新尝试！');
							return;
						}
					}
				});
			} else {
				$.ajax({
					url: dataUrl.addAddr,
					dataType: 'json',
					type: 'post',
					data: {
						name: uname,
						mobile: uphone,
						province: uprovince,
						city: ucity,
						area: uarea,
						detailAddress: udetailAddress,
						email: uemail
					},
					success: function(data) {
						if(data.flag == 1) {
							if(type == 1) {
								location.href = "/user/toInvoice.do";
							} else if(type == 2) {
								location.href = "/user/giftDetail.do";
							} else if(type == 3) {
								location.href = "/ucenter/addressList.do";
							} else if(type == 4) {
								location.href = "/ucenter/myInvoice.do";
							} else if(type == 5) {
								location.href = "/ucenter/myGift.do";
							} else if(type.match("_")) {
								location.href = '/project/confirmCrowdfundingPrize.do?projectId=' + type.substring(0, type.indexOf("_")) + '&giftId=' + type.substring(type.indexOf("_") + 1);
							} else {
								that.showTips('系统异常，请重新尝试！');
								return;
							}
						} else {
							that.showTips('系统异常，请重新尝试！');
						}
					}
				});
			}
		});
	},
	getUserInfo:function(){
		$.ajax({
			url: dataUrl.getUserInfo,
			dataType: 'json',
			type: 'get',
			data: {},
			success: function(data) {
				if(data.result == 0) {
					var item = data.item;
					if(item.headImageUrl != null){
						$('#headImageUrl').attr("src", item.headImageUrl);
					}
					$('#nickName').text(item.nickName);
				}
			}
		});
	},
	getAddress:function(){
		if(this.id == null) return;
		$.ajax({
			url: dataUrl.getDetailAddr,
			dataType: 'json',
			type: 'get',
			data: {id: this.id},
			success: function(data) {
				if(data.result == 0) {
					var item = data.item;
					$("#area_dummy").val(item.area);
					$("#email").val(item.email);
					$("#name").val(item.name);
					$("#province_dummy").val(item.province);
					$("#detailAddress").val(item.detailAddress);
					$("#city_dummy").val(item.city);
					$("#phone").val(item.mobile);
				} else {
					$('.formItem :input').val('');
				}
			}
		});
	},
	/*
	生成市
	isldqu:改变后区是否改变
	isinitqu:是否同时初始化区
	 */
	initcity:function(shengcode, isldqu, isinitqu){
		var that = this, citys = objZhArea.area1[shengcode],
			htmlcity = [];
		for (var j = 0; j < citys.length; j++) {
			htmlcity.push('<option value="' + citys[j][1] + '">'
					+ citys[j][0] + '</option>');
		}
		var options = {
			preset : 'select'
		};
		if (isldqu) {
			options.onSelect = function(text, obj) {
				that.initqu(obj.values[0]);
			};
		}
		$('#city').html(htmlcity.join('')).scroller('destroy').scroller(options);
		if (isinitqu) {
			that.initqu(citys[0][1]);
		}
	},
	/*生成区*/
	initqu:function(shicode) {
		var qus = objZhArea.area2[shicode];
		var htmlqu = [];
		for(var k = 0; k < qus.length; k++) {
			htmlqu.push('<option value="' + qus[k][1] + '">' + qus[k][0] + '</option>');
		}
		$('#area').html(htmlqu.join('')).scroller('destroy').scroller({
			preset: 'select',
			onSelect: function(text, obj) {}
		});
	},
	showTips:function(txt){
		var $msg = $('#msg');
    	if(!$msg.is(':hidden')){
    		$msg.hide();
    		this.timer = null;
    	}
    	$msg.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$msg.fadeOut();
    	},2000);
	},
	getParamString:function(name){
		var url = location.href,
			paramString = url.substring(url.indexOf("?") + 1, url.length).split("&"),
			param = null,
			value = null;
		for(var i = 0; i < paramString.length; i++) {
			param = paramString[i];
			if(param.substring(0, param.indexOf("=")) == name) {
				value = param.substring(param.indexOf("=") + 1, param.length);
				return value;
			}
		}
		return null;
	}
};
$(function() {
	invoiceAddress.init();
});