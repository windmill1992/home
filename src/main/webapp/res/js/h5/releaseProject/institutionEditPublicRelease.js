var base=window.baseurl;
var dataUrl={
	helpphotoDel:base+"file/image/delete.do"//删除图片
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform"],function($,d,h,en,uc,f){
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var needhelp={
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			if($('#projectId').length>0){
				//编辑
				var len=$('#imgList .item').not('.add').length;
				if(len){
					for(var i=0;i<len;i++){
						var imageId=$('#imgList .item').eq(i).attr('id');
						that.photoData.push(imageId);
					}
				}
				if(len==20){$('#imgList .add').hide()}
			}
			//上传图片
			$('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="95px" height="80px"><i>×</i></div>');
					$('#form1').submit();
				}
				return false;
			});
			
			
			$("body").on("click","#needhelpCon dd b.select",function(){
					$(this).removeClass('select').addClass('ok').html('');
			}).on("mouseover","#imgList .item",function(){ 
				$(this).children('i').show();
			}).on("mouseout","#imgList .item",function(){ 
				$(this).children('i').hide();
			}).on("click","#imgList .item i",function(){ 
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
			}).on("blur","#phone",function(){
				var tmptxt=$(this).val();
			});
			
			/*保存*/
			$('#needhelpsubmit2').click(function(){
				that.needhelpsubmit2();
			});
			
		},	
		
		delPic:function(delV,obj){
			var that=this;
			var delI=jQuery.inArray(delV,that.photoDtat);
			$.ajax({
				url:dataUrl.helpphotoDel,
				data:{images:delV,t:new Date().getTime()},
				success: function(result){ 
					if(result.flag==1){
						that.photoData.splice(delI,1);
						obj.parent().remove();
						$('#imgList .add').show();
					}else if(result.flag==-1){
						en.show(that.delPic);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
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
				if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP')
				{	
					d.alert({content:"上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！",type:'error'});
					return false;
				}
				var file_size = 0;
				  if (!$.browser.msie) {
						file_size = file.files[0].size;
						var size = file_size / 1024;
						if (size > 2048) {
							d.alert({content:"上传的图片大小不能超过2M！",type:'error'});
							return false;
						} 
					}
			return true;
		},
		ajaxForm:function(obj){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					var imgBox=$('#imgList'),addObj=imgBox.children(".add");
					addObj.show();
					if(json.result==1){
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var imgBox=$('#imgList'),addObj=imgBox.children(".add");
						that.photoData.push(json.imageId);
						imgBox.children('.old').attr('id',json.imageId).children('img').attr('src',json.imageUrl).end();
						imgBox.children('.old').find('.imgCon').attr('id','imgCon'+json.imageId);
						imgBox.children('.old').removeClass('old');
//						if(that.photoData.length==20){
//							$('#imgList .add').hide();
//						}
						if(that.photoData.length>6){
							d.alert({content:"上传的图片不能超过六张",type:'error'});
						}
						
						//json.imageUrl;
					}
				},
				error: function(data) {
					var imgBox=$('#imgList'),addObj=imgBox.children(".add");
					addObj.show();
					imgBox.children('.old').remove();
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		phone:function(str){
			 var cellphone = /^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
			if(cellphone.test(str)){
				return true;
			}else{
				return false;
			}
		},
		 needhelpsubmit2:function(){
			    var that=this
			    var state=$("#state").val();
			    if(state==""){
			    	$("#msg").html('<p>修改出现错误请返回重新修改</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
			    }
			    var projectId=$('#projectId').val();
			    if(projectId==""){
			    	$("#msg").html('<p>修改出现错误重新修改</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
			    }
	            var donateTimeNum=$('#donateTimeNum').val();
	            if(donateTimeNum==""){
	            	donateTime=30;
	            }
			    var title=$('#title').val();
	            var cryMoney=$('#cryMoney').val();
	            var content=$('#content').val();
	            var imgIds=that.photoData.join(',');
	            var conData=[];
	            var imgsDetail='';
				if(!title){ 
					$("#msg").html('<p>项目标题不能为空</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(title.length>20){
					$("#msg").html('<p>求助标题不能超过20个字</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(!cryMoney){
					$("#msg").html('<p>求助金额不能为空</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(cryMoney<200){
					$("#msg").html('<p>求助金额必须大于200元</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(!content){
					$("#msg").html('<p>求助内容不能为空</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(!imgIds){
					$("#msg").html('<p>图片不能为空</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				};
				if(projectId == ''){projectId=-1;}
				var url="";
				url=window.baseurl+"uCenterProject/updatePublicRelease.do";
				$.ajax({ 
						url:url,
						type: 'POST',
						data:{id:projectId,
							donateTimeNum:donateTimeNum,
							title:title,cryMoney:cryMoney,
							state:state,
							content:content,
							imgIds:imgIds,
							t:new Date().getTime()},
						chase:false,
						success: function(result){
							if(result.flag==1){
								$("#msg").html('<p>项目修改成功！</p>');
								$("#msg").show();
								setTimeout(function () {
						            $("#msg").hide();
						        }, 2000);
								window.location.href=base+"uCenterProject/uCenterProjectList.do?state="+state+"&currentPage=1";
								return true;
							}else{
								$("#msg").html('<p>'+result.errorMsg+'</p>');
								$("#msg").show();
								setTimeout(function () {
						            $("#msg").hide();
						        }, 2000);
								return false;
							}
						},
						error: function(){
							$("#msg").html('<p>网络异常,请联系客服</p>');
							$("#msg").show();
							setTimeout(function () {
					            $("#msg").hide();
					        }, 2000);
							return false;
						}
				});
				
	      
	    },
		
	};
	 
	needhelp.init();
});