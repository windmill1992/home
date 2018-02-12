
var base = 'http://www.17xs.org/';
var dataUrl = {
	helpphotoDel: base + 'file/image/delete.do',				//删除图片
	getUserType: base + 'activity/getUserType.do',				//获取用户类型
	addTeamActivity: base + 'activity/addTeamActivity.do'		//添加团队信息
};

var groupReg = {
	photoData:[],
	userType:3,
	ua:'',
	init:function(){
		new MultiPicker({
		 	input: 'gAddr',//点击触发插件的input框的id
	        container: 'gAddrContainer',//插件插入的容器id
	        jsonData: $city,
	        success: function (arr) {
				if(arr.length==3){
					if(/请选择/.test(arr[2].value)){
		        			arr[2].value = '全部';
		        		}
					$("#gAddr").val(arr[0].value +"-"+arr[1].value+"-"+arr[2].value);
				}else{
					if(/请选择/.test(arr[1].value)){
	        			arr[1].value = '全部';
	        		}
					$("#gAddr").val(arr[0].value +"-"+arr[1].value);
				}
	        }
	    });
	    this.getUserType();
		this.ajaxForm($('#form2'));
		this.bindEvent();
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			$('body').append('<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"><\/script>');
			that.ua = 'wx';
		}else{
			$('body').append('<script src="/res/js/common/TouchSlide.1.1.js"><\/script>');
			that.ua = 'h5';
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
					that.userType = res.result.type;
					if(that.userType == 2 || that.userType == 1){
						alert('您已经有团队了！');
					}else{}
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=groupReg';
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
		$('body').on("change",".file-input",function(){
			var p=that.photoPic(this);
			if(p){
				var imgBox=$(this).closest(".addList"),addObj=imgBox.children(".add"),src='/res/images/common/bar1.gif';
				if(imgBox.find('.item:not(.add)').length>=2){
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
			if(that.ua == 'h5'){
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
				$('#bigImg').show()
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
		}).on('click','#submit',function(){
			var name = $('#gName').val();
			var prop = $('#gProperty').val();
			var addr = $('#gAddr').val();
			var cName = $('#gContactName').val();
			var tel = $('#gTel').val();
			var scope = $('#gServiceScope').val();
			var exp = $('#gServiceExp').val();
			var intro = $('#gIntro').val();
			var len = $('.addList .item:not(.add)').length;
			var imgIds = [];
			if(name==''){
				that.showTips('团队名称不能为空！');
				$('#gName').focus();
			}else if(prop==''){
				that.showTips('团队性质不能为空！');
				$('#gProperty').focus();
			}else if(addr==''){
				that.showTips('所在地不能为空！');
				setTimeout(function(){
					$('#gAddr').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else if(cName==''){
				that.showTips('联系人姓名不能为空！');
				$('#gContactName').focus();
			}else if(tel==''){
				that.showTips('手机号码不能为空！');
				$('#gTel').focus();
			}else if(!that.telValidate(tel)){
				that.showTips('电话格式不正确！');
				$('#gTel').focus();
			}else if(scope==''){
				that.showTips('服务领域不能为空！');
				$('#gServiceScope').focus();
			}else if(len<3){
				that.showTips('请上传完整图片！');
			}else{
				$('.addList .item:not(.add)').each(function(){
					imgIds += $(this).attr('id')+',';
				})
				$.ajax({
					type:"post",
					url:dataUrl.addTeamActivity,
					data:{
						name:name,
						type:2,
						address:addr,
						head:cName,
						mobile:tel,
						serviceField:scope,
						serviceExperience:exp,
						introduction:intro,
						contentImageId:imgIds
					},
					success:function(res){
						if(res.code == 1){
							that.showTips('添加成功！');
							setTimeout(function(){
								location.href = '../activity/activityEdit.html';
							},2000);
						}else if(res.code == 3){
							that.showTips('团队已存在，添加失败！');
						}else{
							alert(res.msg);
						}
					}
				});
			}
		}).on('input propertychange','#gServiceExp,#gIntro',function(){
			var h = $(this).height();
			var t = this.scrollTop;
			$(this).height(t+h);
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
					var id = obj.parent().parent().attr("id");
					obj.parent().remove();
					$('#'+id+" .add").show();
				}else{
					alert(result.errorMsg);
					return false;
				}
			}
		}); 
		return true;
	},
	photoPic:function(file){
	    var that=this, filepath =$(file).val();				
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
	//手机号格式验证
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(!reg.test(tel)){
	        return false;
	    }else{
	    	return true;
	    }
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
	groupReg.init();
});
