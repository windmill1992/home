var base=window.baseurl;
var dataUrl={
	appealUserInfoData:base+"test/appealUserInfoData.do",
	helpphotoDel:base+"file/image/delete.do",
	projectSuccessFeedBack:base+"uCenterProject/addfeedback.do"
};
var needhelp={
	photoData:[],
	timer:null,
	init:function(){
		var that=this;
		that.ajaxForm($('#form1'));
		if($('#projectId').length>0){
			//编辑
			var $item = $('.addList .item').not('.add');
			if($item.length > 0){
				$item.each(function(){
					that.photoData.push(this.id);
				});
			}
			if($item.length>=20){
				$('.addList .add').hide();
			}
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
				if(imgBox.find('.item:not(.add)').length > 18){
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
			
		}).on('click','#needhelpsubmit2',function(){
			that.needhelpsubmit2();
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
	needhelpsubmit2:function(){
	    var that = this,
       		projectId = $('#projectId').val(),
	    	type = $('#institutionType').val(),
	    	zlId = $('#zlId').val(),
	    	field = $('#typeName_e').val(),
        	helpType = $('#helpType').val(),
        	donateTime = $('#donateTime').val(),
        	title = $('#title').val(),
        	cryMoney = $('#cryMoney').val(),
        	content = $('#content').val(),
        	imgIds = that.photoData.join(',');
        donateTime = donateTime==''?30:donateTime;	
		if(!title){
			that.showTips('标题不能为空！');
			return false;
		}
		if(!cryMoney){
			that.showTips('求助金额不能为空！');
			return false;
		}
		if(cryMoney<200){
			that.showTips('求助金额必须大于200元！');
			return false;};
		if(!content){
			that.showTips('求助内容不能为空！');
			return false;};
		if(!imgIds){
			that.showTips('上传图片不能为空！');
			return false;
		}
		projectId = projectId==''?-1:projectId;
		var urls = "";
		if(helpType=="5"){
			urls = base + "test/InstitutionReleaseProject.do?helpType="+helpType+"&zlId="+zlId+"&type="+type;
		}else{
			urls = base + "test/appealProjectInfoData.do?helpType="+helpType+"&zlId="+zlId+"&type="+type;
		}
		$.ajax({ 
			url:urls,
			type: 'POST',
			data:{
				id:projectId,
				deadline:donateTime,
				title:title,
				cryMoney:cryMoney,
				status:210,
				field:field,
				identity:'',
				content:content,
				imgIds:imgIds,
				t:new Date().getTime()
			},
			chase:false,
			success: function(result){
				if(result.flag==1){
					var obj=result.obj,help=obj.help,projectId=obj.projectId;
					//发布成功之后插入一条反馈的项目
					$.ajax({ 
						url:dataUrl.projectSuccessFeedBack,
						type: 'POST',
						data:{pid:projectId},
						chase:false,
						success: function(res){
							if(res.flag==1){
								location.href = "/test/success.do?projectId="+projectId;
								that.showTips('发布成功');
							}else{
								that.showTips(res.errorMsg);
							}
						},
						error: function(){
							alert("网络异常,请联系客服");
						}
					});
				}else if(result.flag==0){
					that.showTips(result.errorMsg);
				}else if(result.flag==-1){
					that.showTips(result.errorMsg);
				}
			},
			error: function(){
				alert("网络异常,请联系客服");
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
