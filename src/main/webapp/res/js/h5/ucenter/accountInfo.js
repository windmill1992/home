var base = 'http://www.17xs.org/';
var dataUrl = {
	uploadAvatar: base +'file/wximage.do',		//上传头像
	loginout: base +'user/loginout.do',			//退出登录
	helpphotoDel: base +'file/image/delete.do'	//删除图片
};
var accountInfo = {
	photoData:[],
	timer:null,
	init:function(){
		var that = this,
			ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			that.ua = 'wx';
			$('#h5Avatar').remove();
		}else{
			that.ajaxForm($('#form1'));
			that.ua = 'h5';
			$('#wxAvatar').remove();
		}
		$('.upload-pic').show();
		that.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		$("body").on("change",".file-input",function(){
			var p = that.photoPic(this);
			if(p){
				var src='/res/images/common/bar1.gif';
				$('#coverImageH5').attr('src',src);
				$('#form1').submit();
				var tempObj = $(this).parent();
				var id = $(this).attr('id');
				$(this).remove();
				tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
			}
			return false;
		}).on('click','.changeLogo',function(){
			that.wxUpload();
			
		}).on('click','#loginOut',function(){
			$('#logOutDialog').addClass('show');
			
		}).on('click','#cancel,#sure',function(){
			$('#logOutDialog').addClass('hide');
			setTimeout(function(){
				$('#logOutDialog').removeClass('show hide');
			},400);
			
		}).on('click','#sure',function(){
			$.ajax({
				type:"get",
				url:dataUrl.loginout,
				data:{},
				success:function(res){
					if(res.flag == 1){
						that.showTips('退出成功！');
						setTimeout(function(){
							location.href = '/ucenter/user/Login_H5.do';
						},1000);
					}else{
						that.showTips('退出失败！');
					}
				}
			});
		});
	},
	wxUpload:function(){
		var that = this;
		//  拍照、本地选图
		var images = {localId: [],serverId: []};
		//异步上传图片到微信服务器
		var syncUpload = function(localId){
		    wx.uploadImage({
	            localId: localId,
	            isShowProgressTips: 1,
	            success: function (res) {
	            	var id = $('#coverImageWx').parent().parent().attr('id');
	                images.serverId.push(res.serverId);// 返回图片的服务器端ID
	                syncDownload(res.serverId,id);
	            },
	            error: function(){
	           		alert("上传图片出现问题，请联系客服");
	            }
		    });
		};
		//异步下载图片到本地
		var syncDownload = function(mid,id){
			 $.ajax({
				url:dataUrl.uploadAvatar,
				data:{mId:mid,typeName:'personhead',t:new Date()},
				success: function(res){ 
					if(res.flag==1){
						if(id){
							that.delPic(id);
						}
						that.showTips("上传图片成功");
					}else{
						that.showTips("上传图片失败");
					}
				}
			}); 
		};
	    wx.chooseImage({
		    count: 1, // 默认9
		    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
		    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
		    success: function (res) {
	    		//销毁数据
	            images.localId = images.serverId  = [];
	          	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	          	for(var i = 0;i<images.localId.length;i++){
					syncUpload(images.localId[i]);
	          	}
	          	$('#coverImageWx')[0].src = images.localId[0];
		    }
	    });
	},
	photoPic:function(file){
	    var filepath = $(file).val(),
	    	extStart = filepath.lastIndexOf(".") + 1,
			ext = filepath.substring(extStart,filepath.length).toUpperCase();
		if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP'){
			alert('上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！');
			return false;
		}
		var file_size = file.files[0].size,
			size = file_size / 1024;
		if (size > 2048) {
			alert("上传的图片大小不能超过2M！");
			return false;
		} 
		return true;
	},
	delPic:function(delV){
		var that = this,
			delI = $.inArray(delV,that.photoData);
		$.ajax({
			url:dataUrl.helpphotoDel,
			data:{images:delV,t:new Date().getTime()},
			success: function(result){ 
				if(result.flag==1){
					that.photoData.splice(delI,1);
				}else{
					alert(result.errorMsg);
					return false;
				}
			}
		}); 
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
					alert(json.error);
					return false;
				}else{
					var $a = $('#coverImageH5').parent().parent();
					var id = $a.attr('id');
					$a.attr('id',json.imageId);
					that.photoData.push(json.imageId);
					$('#coverImageH5').attr('src',json.imageUrl);
					if(id){
						that.delPic(id);
					}
				}
			},
			error: function(data) {
				that.showTips('上传失败！');
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
	accountInfo.init();
});
