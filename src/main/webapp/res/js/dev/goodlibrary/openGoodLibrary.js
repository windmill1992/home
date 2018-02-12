var base=window.baseurl;
var dataUrl={
	helpphotoDel:base+"file/image/delete.do",//删除图片
	addGoodLibrary:base+"goodlibrary/addGoodLibrary.do"//提交
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
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon",
		"area"	: "util/area"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon","area"],function($,d,h,en,uc,f,p,p1,a){
	//window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	a.init({selectsId:["province","city","county"],def:[0,0,0]});
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form'));
			
			$("#img").click(function(){
				var delV=$('.io').attr('id');
				var isDel=that.delPic(delV,$('.io'));
				$(".io").remove();
				$('#img').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></a></div>');
				$('#imgList').html(html.join(''));
				$('#imgFile').change(function(){
					var p=that.photoPic(this);
					if(p){
						var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
						addObj.hide();
						addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
						$('#form').submit();
					}
					return false;
				});
			});
			//input验证
			$('#balance').focus(function(){
				var nickName=$('#nickName').val();
				if(nickName==''){
					$("#nickName").attr("value","请输入善库名称！");
					$("#nickName").addClass("personInpg");
					setTimeout(function () {
						$("#nickName").attr("value","")
						$("#nickName").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#introduction').focus(function(){
				var balance=$('#balance').val();
				if(balance==''){
					$("#balance").attr("value","请输入行善金额！");
					$("#balance").addClass("personInpg");
					setTimeout(function () {
						$("#balance").attr("value","")
						$("#balance").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			//提交
			$('#openGoodLibrary_button').click(function(){
				var imgsid = $('.io').attr('id');
				if(typeof(imgsid)=="undefined"){
					imgsid="";
				}
				that.submitInfo(imgsid);
			})
			$('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form').submit();
				}
				return false;
			});
			
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
						$("#msg").html('<p>上传的图片大小不能超过2M！</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
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
				if(json.result==1){
					return false;
				}else{
					var html=[];
						data = data.replace("<PRE>", "").replace("</PRE>", "");
						var json = eval('(' + data + ')'); 
						var imgBox=$('#imgList'),addObj=imgBox.children(".add");
						addObj.show();
						if(json.result==1){
							imgBox.children('.old').remove();
							d.alert({content:json.error,type:'error'});
							return false;
						}else{
							html.push('<div class="item add"><i id="img">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io" name="io" style="width:80px;height: 80px"/></div>');
							$('#imgList').html(html.join(''));
							$("#img").click(function(){
								var delV=$('.io').attr('id');
								var isDel=that.delPic(delV,$('.io'));
								$(".io").remove();
								$('#img').hide();
								html=[];
								html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></a></div>');
								$('#imgList').html(html.join(''));
								$('#imgFile').change(function(){
									var p=that.photoPic(this);
									if(p){
										var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
										addObj.hide();
										addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
										$('#form').submit();
									}
									return false;
								});
							});
							
						}
				}
			},
			error: function(data) {
			}
		}); 
	},
	//删除图片
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
	//提交
	submitInfo: function(imgsid){
		var nickName=$('#nickName').val(),balance=$('#balance').val(),introduction=$('#introduction').val(),
		province=$('#province').val(),city=$('#city').val(),county=$('#county').val(),
		libraryId=$('#goodlibraryId').val();
		if(nickName==''){
			$("#nickName").attr("value","请输入善库名称！");
			$("#nickName").addClass("personInpg");
			setTimeout(function () {
				$("#nickName").attr("value","")
				$("#nickName").removeClass("personInpg");
			}, 2000);
			return false;
		}
		if(balance==''){
			/*$("#balance").attr("value","请输入行善金额！");
			$("#balance").addClass("personInpg");
			setTimeout(function () {
				$("#balance").attr("value","")
				$("#balance").removeClass("personInpg");
			}, 2000);
			return false;*/
			return d.alert({content:"请输入行善金额！",type:'error'}); 
		}
		if(introduction==''){
			$("#introduction").attr("value","请输入善库简介！");
			$("#introduction").addClass("personInpg");
			setTimeout(function () {
				$("#introduction").attr("value","")
				$("#introduction").removeClass("personInpg");
			}, 2000);
			return false;
		}
		if(province==''||city==''||county==''||province=='省份'||city=='地级市'||county=='区/县级市'){
			return d.alert({content:"请输入所在地！",type:'error'}); 
		}
		if(libraryId==''){
			libraryId=null;
		}
		if(imgsid==''){
			return d.alert({content:"请上传善库头像！",type:'error'}); 
		}
		var address=province+city+county;
		//添加数据
		$.ajax({
			url:dataUrl.addGoodLibrary,
			cache: false,
			type: "POST",
			data:{id:libraryId,nickName:nickName,balance:balance,introduction:introduction,
				familyAddress:address,logoId:imgsid},
			success: function(result){
				if(result.flag==1){//成功
					data=result.obj;
					window.location='http://www.17xs.org/goodlibrary/gotoGoodLibrarySuccess.do?libraryId='+data;
				}
				else if(result.flag==-1){//未登录
					window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/goodlibrary/gotoOpenGoodLibrary.do';
				}
				else if(result.flag==0){//异常
					return d.alert({content:result.errorMsg,type:'error'});
				}
			},
			error : function(result) { 
				return d.alert({content:"请求异常",type:'error'});
			}

		});
	}
	}
	
	reg.init()
})
