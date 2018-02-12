var base=window.baseurl;
var dataUrl={
	SSO_MobileCode:base+'user/phoneCode.do',//验证码请求
	helpphotoDel:base+"file/image/delete.do",//删除求助图片
	authentication:base+"ucenter/authentication.do",//个人提交
	authenticationTeam:base+"ucenter/authenticationTeam.do"//机构提交
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var reg={
		type:0,
		timer:null,
		timer1:null,
		timer2:null,
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			that.ajaxForm($('#form2'));
			that.ajaxForm($('#form3'));
			that.ajaxForm($('#form4'));
			that.ajaxForm($('#form5'));
			that.bindEvent();
			$('.addList').each(function(){
				if($(this).find('.item').not('.add').length > 0){
					$(this).find('.add').hide();
				}
			});
			
		},
		bindEvent:function(){
			var that = this;
			$("body").on("change",".file-input",function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$(this).closest(".addList"),
						addObj=imgBox.find(".add"),
						src='/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><a href="javascript:;" class="preview"><img src="'+src+'"></a></div>');
					imgBox.parent().submit();
					var tempObj = $(this).parent();
					var id = $(this).attr('id');
					$(this).remove();
					tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
				}
				return false;
			}).on("click",".addList .item .del",function(e){
				e.stopPropagation();
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
				if(isDel){
					that.showTips('删除成功！');
				}else{
					that.showTips('删除失败！');
				}
			}).on('click','#bigImg',function(){
				$(this).hide().find('.bd ul').remove();
				
			}).on('click','.nav_fr',function(){
				$("#typestate").val('1');
				$(".geren").hide();
				$(".nav_fl").removeClass("nav_gg1");
				$(".jigou").show();
				$(".nav_fr").addClass("nav_gg2");
				$('#changeWord').text('我是机构');
				
			}).on('click','.nav_fl',function(){
				$('#typestate').val('0');
				$(".jigou").hide();
				$(".geren").show();
				$(".nav_fl").addClass("nav_gg1");
				$(".nav_fr").removeClass("nav_gg2");
				$('#changeWord').text('我是个人');
				
			}).on('click','.foot_qt',function(){	//下一步
				var type = $('#typestate').val();
				that.verify2(type);
				
			}).on('click','#usercode',function(){	//个人发送验证码
				if($(this).hasClass('disabled')) return;
				
				var phone = $('#MobileNum').val();
				if(phone == '' || !that.telValidate(phone)){
					that.showTips('请输入正确的手机号！');
					$('#MobileNum').focus();
					return;
				}
				en.rashCode();
				$('#yzmDialog').addClass('show');
				$('#yzmCode').val('').focus();
				that.btn = $(this);
				
			}).on('click','#sendTeamVerCode',function(){	//机构发送验证码
				if($(this).hasClass('disabled')) return;
				
				var phone = $('#mobile').val();
				if(phone == '' || !that.telValidate(phone)){
					that.showTips('请输入正确的手机号！');
					$('#mobile').focus();
					return;
				}
				en.rashCode();
				$('#yzmDialog').addClass('show');
				$('#yzmCode').val('').focus();
				that.btn = $(this);
				
			}).on('click','#cancel',function(){
				
				$('#yzmDialog').addClass('hide');
				setTimeout(function(){
					$('#yzmDialog').removeClass('show hide');
					$('#yzmCode').val('');
				},400);
				
			}).on('click','#sure',function(){
				var code = $('#yzmCode').val(), 
					state = $('#typestate').val(), 
					phone;
				if(code == '' || !that.isCode(code)){
					$('#yzmCode').focus();
					return;
				}
				if(state == 0){
					phone = $('#MobileNum').val();
				}else{
					phone = $('#mobile').val();
				}
				$('#yzmDialog').addClass('hide');
				setTimeout(function(){
					$('#yzmDialog').removeClass('show hide');
					that.sendcode2(that.btn,code,phone,1);
				},400);
				
			}).on('click','.refreshCode',function(){
				en.rashCode();
				
			}).on('click','.addList .preview img',function(){
				that.previewImg($(this));
			});
		},
		previewImg:function(obj){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){
				var imgArray = [];
				var curImgSrc = obj.attr('src');
				imgArray.push(curImgSrc);
				 wx.previewImage({
					current: curImgSrc,
					urls: imgArray
				});
			}else{
				var html = [];
				html.push('<ul>');
				html.push('<li><img src="'+obj[0].src+'" /></li>');
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show();
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
			}
		},
		photoPic:function(file){
		    var filepath = $(file).val();				
		    var extStart=filepath.lastIndexOf(".")+1;		
			var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
			if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP'){	
				alert('上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！');
				return false;
			}
			var file_size = 0;
				file_size = file.files[0].size;
				var size = file_size / 1024;
				if (size > 2048) {
					alert("上传的图片大小不能超过2M！");
					return false;
				} 
			return true;
		},
		ajaxForm:function(obj){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {},
				uploadProgress: function(event, position, total, percentComplete) {},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					if(json.result==1){
						var imgBox=obj.find('.addList');
						imgBox.children('.old').remove();
						imgBox.find('.add').show();
						alert(json.error);
						return false;
					}else{
						var imgBox=obj.find('.addList'),addObj=imgBox.children(".add");
						that.photoData.push(json.imageId);
						var num = obj.attr('id').substr(4);
						imgBox.children('.old').attr('id',json.imageId).find('img').attr('src',json.imageUrl).attr('class','io'+num).end();
						imgBox.children('.old').removeClass('old');
						imgBox.find(".item").not(".add").append('<i class="del"><img src="/res/images/h5/images/close.png"/></i>');
						$('.file-input').removeAttr('disabled');
						that.showTips('上传成功！');
					}
				},
				error: function(data) {
					var imgBox=obj.find('.addList'),addObj=imgBox.children(".add");
					addObj.show();
					imgBox.children('.old').remove();
					alert('上传失败！');
				}
			}); 
		},
		//删除图片
		delPic:function(delV,obj){
			var that=this;
			var delI=jQuery.inArray(delV,that.photoData);
			$.ajax({
				url:dataUrl.helpphotoDel,
				data:{images:delV,t:new Date().getTime()},
				success: function(result){ 
					if(result.flag==1){
						that.photoData.splice(delI,1);
						var $box = obj.closest('.addList');
						obj.parent().remove();
						$box.find('.add').show();
					}else{
						alert(result.errorMsg);
						return false;
					}
				}
			}); 
			return true;
		},
		//提交
		verify2:function(type){
			var that = this;
			if(type==0){//个人
				var userName = $('#userName').val(),
					idCard = $('#idCard').val(),
					MobileNum = $('#MobileNum').val(),
					mcode = $.trim($('#personalVerCode').val()),
					ycode = $('#yzmCode').val();
				if(userName==''){
					that.showTips('请输入姓名！');
					$('#userName').focus();
				}else if(idCard=='' || !checkCard(idCard)){
					that.showTips('请输入正确的身份证号！');
					$('#idCard').focus();
				}else if(MobileNum=='' || !that.telValidate(MobileNum)){
					that.showTips('请输入正确的手机号！');
					$('#MobileNum').focus();
				}else if(mcode == ''){
					that.showTips('请输入手机验证码！');
					$('#personalVerCode').focus();
				}else if(!that.isMobelCode(mcode)){
					that.showTips('手机验证码错误！');
					$('#personalVerCode').focus();
				}else{
					var aIds = [];
					$('.geren .addList').each(function(){
						var formid = $(this).closest('form').attr('id'),
							$item = $(this).find('.item:not(.add)');
						if(formid == 'form1' && $item.length == 0){
							that.showTips('请上传身份证正面！');
							aIds = [];
							return false;
						}else if(formid == 'form2' && $item.length == 0){
							that.showTips('请上传身份证反面！');
							aIds = [];
							return false;
						}else if(formid == 'form3' && $item.length == 0){
							that.showTips('请上传手持身份证！');
							aIds = [];
							return false;
						}else{
							aIds.push($item[0].id);
						}
					});
					if(aIds.length < 3){return;}
					
					var ids = aIds.join(',');
					$.ajax({
						url:dataUrl.authentication,
						data:{ids:ids,realName:userName,idCard:idCard,mobileNum:MobileNum,phoneCode:mcode,code:ycode},
						success: function(result){
							if(result.flag == 1){//成功
								 var canshu = result.errorMsg.split(',');
								location.href = '/test/personReleaseNav_list.do?type='+canshu[0]+'&id='+canshu[2];
							}else if(result.flag == 0){//失败
								that.showTips(result.errorMsg);
							}else{//未登录
								location.href = '/ucenter/user/Login_H5.do';
							}
						}
					});
				}
			}else if(type==1){//机构
				var	address = $('#address').val(),
					name = $('#name').val(),
					groupCode = $('#groupCode').val(),
					head = $('#head').val(),
					mobile = $('#mobile').val(),
					mcode = $.trim($('#teamVerCode').val()),
					ycode = $('#yzmCode').val();
				if(name==''){
					that.showTips('请输入组织/机构名称！');
					$('#name').focus();
				}else if(groupCode==''){
					that.showTips('请输入组织结构代码！');
					$('#groupCode').focus();
				}else if(address==''){
					that.showTips('请输入单位详细地址！');
					$('#address').focus();
				}else if(head==''){
					that.showTips('请输入联系人姓名！');
					$('#head').focus();
				}else if(mobile=='' || !that.telValidate(mobile)){
					that.showTips('请输入正确的手机号！');
					$('#mobile').focus();
				}else if(mcode == ''){
					that.showTips('请输入手机验证码！');
					$('#teamVerCode').focus();
				}else if(!that.isMobelCode(mcode)){
					that.showTips('手机验证码错误！');
					$('#teamVerCode').focus();
				}else{
					var aIds = [];
					$('.jigou .addList').each(function(){
						var formid = $(this).closest('form').attr('id'),
							$item = $(this).find('.item:not(.add)');
						if(formid == 'form4' && $item.length == 0){
							that.showTips('请上传机构证件！');
							aIds = [];
							return false;
						}else if(formid == 'form5' && $item.length == 0){
							that.showTips('请上传单位授权书！');
							aIds = [];
							return false;
						}else{
							aIds.push($item[0].id);
						}
					});
					if(aIds.length < 2){return;}
					
					var ids = aIds.join(',');
					$.ajax({
						url:dataUrl.authenticationTeam,
						data:{ids:ids,name:name,groupCode:groupCode,head:head,mobile:mobile,phoneCode:mcode,code:ycode,address:address},
						success: function(result){
							if(result.flag == 1){//成功
								 var canshu = result.errorMsg.split(',')
									location.href = '/test/personReleaseNav_list.do?type='+canshu[0]+'&id='+canshu[2];
							}else if(result.flag == 0){//失败
								that.showTips(result.errorMsg);
							}else if(result.flag == 10005){//已经提交审核了
								that.showTips('您申请的企业已经在审核了，请耐心等待审核信息！');
							}else{//未登录
								location.href = '/ucenter/user/Login_H5.do';
							}
						}
					});
				}
			}
		},
		//图片验证码格式验证
		isCode:function(code){
			var reg = /^\d{4}$/;
			if(reg.test(code)){
				return true;
			}else{
				return false;
			}
		},
		//手机验证码格式验证
		isMobelCode:function(code){
			var reg = /^\d{6}$/;
			if(reg.test(code)){
				return true;
			}else{
				return false;
			}
		},
		//手机号格式验证
		telValidate:function(tel){
			var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
			if(!reg.test(tel)){
		        return false;
		    }else{
		    	return true;
		    }
		},
		countDown:function(obj,second,timerx) {
			var that = this;
			var str = "s后重新<br>获得验证码";
			$(obj).addClass('disabled');
			var setext = second + str;
			$(obj).html(setext);
			timerx = setInterval(function(){
				second--;
				if(second >= 0){
					setext = second + str;
					$(obj).html(setext);
				}else{
					$(obj).removeClass('disabled');
					$(obj).html('发送验证码');
					clearInterval(timerx);
				}
			},1000);
		},
		/*
		 *obj:发送验证码按钮对象;code:图片验证码;tel:手机号;type:验证码类型
		 */
		sendcode2:function(obj,code,tel,type){
			var that = this, types = '';
			if(type){
				types = 'certification';
			}
			$.ajax( {
				url: dataUrl.SSO_MobileCode,
				cache: false,
				type: "POST",
				data: {phone:tel,code:code,type:types},
				success : function(result) { 
					if(result.flag==1){
						that.showTips('验证码发送成功！');
						if($(obj).attr('id') == 'usercode'){
							that.countDown(obj,60,that.timer1);
						}else{
							that.countDown(obj,60,that.timer2);
						}
					}else{
						that.showTips(result.errorMsg);
					}
				},
				error : function() { 
					that.showTips('发送失败，请稍后重试。');
				}
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
	    }
	};
	$(function(){
		reg.init();
	});
});