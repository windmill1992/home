var base = 'http://www.17xs.org/';
var dataUrl = {
	helpphotoDel: base + "file/image/delete.do",									//删除图片
	addActivityFeedBack: base + 'activity/addProjectActivityFeedBack.do'		//添加项目反馈表信息
};

var feedback = {
	photoData:[],
	aid:'',
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(aid == null){
			$('#pageContainer').hide();
			$('.no-page').show();
		}else{
			that.aid = aid;
			that.ajaxForm($('#form1'));
			that.bindEvent();
		}
	},
	bindEvent:function(){
		var that = this;
		$("body").on("change",".file-input",function(){
			var p=that.photoPic(this);
			if(p){
				var imgBox=$(this).closest(".addList"),addObj=imgBox.children(".add"),src='/res/images/common/bar1.gif';
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
			var $img = $(this).find('img');
			var currentSrc = $img.attr('src');
			var urlsSrc = [];
			$('.addList .item .preview img').each(function(){
				urlsSrc.push($(this).attr('src'));
			});
			wx.previewImage({
				current:currentSrc,
				urls:urlsSrc
			});
		}).on('click','#submit',function(){
			var con = $('#con').val();
			if(con == ''){
				that.showTips('反馈内容不能为空！');
			}else{
				$.ajax({
					type:"post",
					url:dataUrl.addActivityFeedBack,
					data:{content:con,contentImageId:that.photoData.join(','),activityId:that.aid},
					success:function(res){
						if(res.code == 1){
							that.showTips('添加成功！');
							setTimeout(function(){
								location.href = base + 'webhtml/view/h5/volunteer/uCenter/management.html';
							},2000);
						}else if(res.code == 0){
							that.showTips('添加失败！');
						}else{
							alert('登录错误');
						}
					}
				});
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
	feedback.init();
});
