
var base = 'http://www.17xs.org/';
var dataUrl = {
	helpphotoDel: base + 'file/image/delete.do',				//删除图片
	getActivityDetil: base + 'activity/getActivityDetil.do',	//查询活动信息
	getUserType: base + 'activity/getUserType.do',				//获取用户类型
	addActivity: base + 'activity/addActivity.do'				//添加活动
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		userType:0,
		title:'',
		content:'',
		hasLogo:false,
		logoUrl:'',
		hasConImg:false,
		tishi:'',
		contentImgs:[]
	}
});

var activityEdit = {
	photoData:[],
	init:function(){
		var that = this;
		that.getUserType();
		var aid = that.GetQueryString('activityId');
		if(aid != null){
			vm.aid = aid;
		}
		that.ajaxForm($('#form'));
		that.ajaxForm($('#form1'));
		that.bindEvent();
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			$('body').append('<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"><\/script>');
			vm.ua = 'wx';
		}else{
			$('body').append('<script src="/res/js/common/TouchSlide.1.1.js"><\/script>');
			vm.ua = 'h5';
		}
	},
	getUserType:function(){
		var that = this;
		var code = that.GetQueryString('code');
		$.ajax({
			type:"get",
			url:dataUrl.getUserType,
			data:{code:code},
			success:function(res){
				if(res.code == 1){
					vm.userType = res.result.type;
					that.getActivityDetail();
					if(vm.userType == 2){
						vm.tishi = '您不是管理员，无法发布活动，请联系管理员';
						$('#userDialog .sbtn:first-child').hide();
						$('#userDialog').addClass('show');
					}else if(vm.userType == 3){
						vm.tishi = '您不是管理员，无法发布活动，是否去创建团队后再发布活动？';
						$('#userDialog').addClass('show');
					}else{}
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					if(vm.aid){
						location.href = '/ucenter/user/Login_H5.do?flag=editAct_'+vm.aid;
					}else{
						location.href = '/ucenter/user/Login_H5.do?flag=editAct_';
					}
				}else if(res.code == 3){
					that.showTips(res.msg);
				}else{
					alert(res.msg);
				}
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$("body").on("change",".file-input",function(){
			var p=that.photoPic(this);
			if(p){
				var imgBox=$(this).closest(".addList"),addObj=imgBox.children(".add"),src='/res/images/common/bar1.gif';
				if(imgBox.attr('id')=='imgList'){
					addObj.hide();
				}else if(imgBox.find('.item:not(.add)').length>=5){
					addObj.hide();
				}else{
					addObj.show();
				}
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
		}).on('click','.addList .item:not(.add)',function(){
			if(vm.ua == 'h5'){
				var $imgs = $(this).closest('.addList').find('.preview img');
				var idx = $(this).index();
				var html = [];
				html.push('<ul>');
				$imgs.each(function(){
					html.push('<li><img src="'+this.src+'" /></li>');
				});
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show();
				TouchSlide({ slideCell:"#bigImg",mainCell:".bd ul",effect:"left",autoPlay:false,defaultIndex:idx});
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
			}else{
				var $img = $(this).find('.preview img');
				var currentSrc = $img.attr('src');
				var urlsSrc = [];
				$('.addList .item .preview img').each(function(){
					urlsSrc.push($(this).attr('src'));
				});
				wx.previewImage({
					current:currentSrc,
					urls:urlsSrc
				});	
			}
		}).on('click','#bigImg',function(){
			$(this).hide().find('.bd ul').remove();
		}).on('click','#userDialog .closeDialog, #userDialog .quxiao',function(){
			$('#userDialog').addClass('hide');
			setTimeout(function(){
				$('#userDialog').removeClass('show hide');
			},400);
		}).on('click','#userDialog .sure',function(){
			$('#userDialog').addClass('hide');
			setTimeout(function(){
				$('#userDialog').removeClass('show hide');
				if(vm.userType == 3){
					location.href = '../register/groupReg.html';
				}
			},400);
		}).on('click','#submit',function(){
			if(vm.userType == 2 || vm.userType == 3){
				$('#userDialog').addClass('show');
			}else{
				var title = $('#actTitle').val();
				var con = $('#actContent').val();
				var $item = $('#imgList .item:not(.add)');
				var $item1 = $('#imgList1 .item:not(.add)');
				var arr = that.photoData;
				if(title == ''){
					that.showTips('活动标题不能为空！');
					$('#actTitle').focus();
				}else if(con == ''){
					that.showTips('活动内容不能为空！');
					$('#actContent').focus();
				}else if($item.length == 0){
					that.showTips('请上传封面图！');
				}else if($item1.length == 0){
					that.showTips('请上传内容图！');
				}else{
					var id = $item.attr('id');
					var id1 = '';
					$item1.each(function(){
						id1 += $(this).attr('id')+',';
					});
					var $data = {name:title,content:con,logoId:id,contentImageId:id1};
					if(vm.aid != '' && vm.aid != null && vm.aid != undefined){
						$data['id'] = vm.aid;
					}
					$.ajax({
						type:"post",
						url:dataUrl.addActivity,
						data:$data,
						success:function(res){
							if(res.code == 1){
								that.showTips('添加成功！');
								setTimeout(function(){
									location.href = 'activityPub.html?activityId='+res.result.activityId;
								},2000);
							}else{
								that.showTips(res.msg);
							}
						}
					});
				}
			}
		});
	},
	getActivityDetail:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getActivityDetil,
			data:{activityId:vm.aid},
			success:function(res){
				if(res.code == 1){
					var r = res.result.data;
					vm.title = r.title;
					vm.content = r.content;
					if(r.logoUrl.length > 0){
						vm.logo = r.logoUrl;
						vm.hasLogo = true;
						$('#form .add').hide();
					}
					if(r.contentImgs.length > 0){
						vm.contentImgs = r.contentImgs;
						vm.hasConImg = true;
						if(r.contentImgs.length > 5){
							$('#form1 .add').hide();
						}
					}
				}else{
//					that.showTips(res.msg);
					return;
				}
			}
		});
	},
	delPic:function(delV,obj){
		var that=this;
		var delI=jQuery.inArray(delV,that.photoData);
		$.ajax({
			url:dataUrl.helpphotoDel,
			data:{images:delV,t:new Date().getTime()},
			success: function(result){ 
				if(result.flag==1){
					that.photoData.splice(delI,1);
					var id = obj.parent().parent().attr('id');
					obj.parent().remove();
					$('#'+id+' .add').show();
				}else{
					alert(result.errorMsg);
					return false;
				}
			}
		}); 
		return true;
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
					alert(json.error);
					return false;
				}else{
					var imgBox=obj.find('.addList'),addObj=imgBox.children(".add");
					that.photoData.push(json.imageId);
					var num = obj.attr('id').substr(4);
					imgBox.children('.old').attr('id',json.imageId).find('img').attr('src',json.imageUrl).attr('class','io'+(num==1?'':num)).end();
					imgBox.children('.old').removeClass('old');
					imgBox.find(".item").not(".add").append('<i class="del"><img src="/res/images/h5/images/close.png"/></i>');
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
	//提示信息
	showTips:function(txt){
		var $tips = $('#tips');
		if($tips.is(':hidden')){
			$tips.text(txt).fadeIn();
			setTimeout(function(){
				$tips.css({'transform':'scale(0.5)'}).fadeOut(function(){
					$(this).text('').css({'transform':'scale(1)'});
				});
			},2000);	
		}else{
			return;
		}
	},
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};
$(function(){
	activityEdit.init();
});
