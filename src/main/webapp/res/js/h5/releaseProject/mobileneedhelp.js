var base=window.baseurl;
var dataUrl={
	appealUserInfoData: base + 'test/appealUserInfoData.do',
	helpphotoDel: base + "file/image/delete.do"
};
var needhelp={
	photoData:[],
	timer:null,
	init:function(){
		var that=this;
		if($('#form1').length > 0){
			that.ajaxForm($('#form1'));
			$('.addList .item:not(.add)').each(function(){
				that.photoData.push(this.id);
			});
		}
		that.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		$("body").on("change",".file-input",function(){
			var p=that.photoPic(this);
			if(p){
				var imgBox=$(this).closest(".addList"),
					addObj=imgBox.find(".add"),
					src='/res/images/common/bar1.gif';
				addObj.before('<div class="item old"><a href="javascript:;" class="preview"><img src="'+src+'"></a></div>');
				imgBox.parent().submit();
				if(imgBox.find('.item:not(.add)').length > 4){
					addObj.hide();
				}
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
		}).on('click','.addList .preview img',function(){
			that.previewImg($(this));
			
		}).on('click','#bigImg',function(){
			$(this).hide().find('.bd ul').remove();
			
		}).on('click','#comfirmSubmit',function(){
			that.comfirmSubmit();
		});
	},
	comfirmSubmit:function(){
    	var that = this,
    		imgIds = that.photoData.join(','),
    		appealName = $("#appealName").val(),
    		appealIdcard = $("#appealIdcard").val(),
    		appealPhone = $("#appealPhone").val(),
    		appealAddress = $("#addr").val(),
    		detailAddress = $("#detailAddress").val(),
    		typeName_e = $("#typeName_e").val(),
    		zlId = $("#zlId").val(),
    		type = $('#institutionType').val(),
    		helpType = $("#helpType").val(),
    		projectId = $("#projectId").val(),
    		relation = "";
    	if(helpType=="1"){
    		relation="本人";
    	}else{
    		relation=$("#relation").val();
    	}
    	if(helpType=="2"||helpType=="3"){
    		if(!relation){
    			that.showTips('与受助人的关系不能为空！');
				return false;
    		}
    	}
    	if(!appealName){
    		that.showTips('受助人名字不能为空！');
			return false;
		}
    	if(!appealIdcard){
    		that.showTips('受助人身份证号不能为空！');
			return false;
		}
    	if(!checkCard(appealIdcard)){
    		that.showTips('请输入正确的身份证号！');
    		return false;
    	}
    	if(!appealPhone){
    		that.showTips('受助人电话不能为空！');
			return false;
    	}
		if(!that.telValidate(appealPhone)){
			that.showTips('请输入正确的电话号码！');
			return false;
		}
    	if(!appealAddress){
    		that.showTips('受助人地址不能为空！');
			return false;
    	}
    	if(!detailAddress){
    		that.showTips('详细地址不能为空！');
			return false;
		}
    	$.ajax({ 
			url:dataUrl.appealUserInfoData + "?zlId="+zlId+"&type="+type+"&helpType="+helpType,
			type: 'POST',
			data:{
				relation:relation,
				appealName:appealName,
				appealIdcard:appealIdcard,
				appealPhone:appealPhone,
				appealAddress:appealAddress,
				detailAddress:detailAddress,
				imgIds:imgIds,
				t:new Date().getTime()
			},
			chase:false,
			success: function(result){
				if(result.flag==1){
				    var projectId=result.obj;
					location.href = "/test/personReleaseList.do?projectId="+projectId+"&zlId="+zlId+"&type="+type+"&typeName_e="+typeName_e;
						
				}else if(result.flag==0){
					that.showTips(result.errorMsg);
					return false;
				}else if(result.flag==-1){
					that.showTips('未登录,请先登录返回！');
					return false;
				}
			},
			error: function(){
				alert("网络异常,请联系客服");
			}
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
					imgBox.children('.old').attr('id',json.imageId).find('img').attr('src',json.imageUrl).attr('class','io'+num).end();
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
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(reg.test(tel)){
			return true;
		}else{
			return false;
		}
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
	needhelp.init();
});
