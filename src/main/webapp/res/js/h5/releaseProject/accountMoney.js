var base = window.baseurl;
var dataUrl={
	User_AccountMoney: base + 'uCenterProject/accountMoney.do',	//提交收款账号
	invoiceDel: base + "file/image/delete.do"					//删除发票图片
};
var reg = {
	photoData:[],
	timer:null,
	init:function(){
		var that=this;
		that.ajaxForm($('#form1'));
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
				if(imgBox.find('.item:not(.add)').length > 10){
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
			
		}).on('click','.foot_qt',function(){
			that.verify();
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
			url:dataUrl.invoiceDel,
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
	//提交
	verify:function(){
		var that = this,
		panMoney = $('#panMoney').val(),
		money = $('#tixian').val(),
		projectId = $('#projectId').val(),
		$item = $('.addList .item:not(.add)'),
		imgsid = '';
		if(panMoney==''){
			that.showTips('请输入打款金额！');
			$('#panMoney').focus().val('');
			return false;
		}
		if(panMoney>money){
			that.showTips('可提现金额不足！');
			return false;
		}
		if($item.length == 0){
			that.showTips('请上传发票图片！');
			return false;
		}else{
			$item.each(function(){
				imgsid += this.id+',';
			});
		}
		$.ajax({
			url:dataUrl.User_AccountMoney,
			data:{id:projectId,panMoney:panMoney,imagId:imgsid},
			success: function(result){
				if(result.errorCode == "0000"){//成功
					that.showTips('提现成功！自动跳转...');
					setTimeout(function(){
						location.href = '/uCenterProject/uCenterProjectList.do?state=210&currentPage=1';
					},2000);
				}else if(result.errorCode == "0001"){//未登录
					location.href = '/ucenter/user/Login_H5.do';
				}
				else{//失败
					that.showTips(result.errorMsg);
					return ;
				}
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