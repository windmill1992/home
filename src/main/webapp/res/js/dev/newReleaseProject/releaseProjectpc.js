/**
 * Created by Administrator on 2016/8/10.
 */
var base=window.baseurl;
var dataUrl={
	SSO_MobileCode:base+'goodlibrary/phoneCode.do',//验证码请求
	helpphotoDel:base+"file/image/delete.do",//删除图片
	authentication:base+"newReleaseProject/authenticationPerson.do",//个人提交
	authenticationTeam:base+"newReleaseProject/authenticationTeam.do"//机构提交
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
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			that.ajaxForm($('#form2'));
			that.ajaxForm($('#form3'));
			that.ajaxForm($('#form4'));
			that.ajaxForm($('#form5'));
			//机构个人点击按钮
			$(".jigou").click(function(){
				$("#typestate").val('1');
				$(this).addClass("navgg");
				$(".geren").removeClass("navgg");
				$(".wsjg").show();
				$(".wsgr").hide();
			});
			$(".geren").click(function(){
				$("#typestate").val('0');
				$(this).addClass("navgg");
				$(".jigou").removeClass("navgg");
				$(".wsgr").show();
				$(".wsjg").hide();
			});
			//input验证
			$('#idCard').focus(function(){
				var userName=$('#userName').val();
				if(userName==''){
					$("#userName").attr("value","请输入姓名！");
					$("#userName").addClass("personInpg");
					setTimeout(function () {
						$("#userName").attr("value","")
						$("#userName").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#idCard').change(function(){
				that.isVaildId($(this));
			});
			$('#MobileNum').focus(function(){
				var idCard=$('#idCard').val();
				if(idCard==''){
					$("#idCard").attr("value","请输入身份证号！");
					$("#idCard").addClass("personInpg");
					setTimeout(function () {
						$("#idCard").attr("value","")
						$("#idCard").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});

			$('#personalVerCode').focus(function(){
				var idCard=$('#MobileNum').val();
				if(idCard==''){
					$("#MobileNum").attr("value","请输入手机号！");
					$("#MobileNum").addClass("personInpg");
					setTimeout(function () {
						$("#MobileNum").attr("value","")
						$("#MobileNum").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			//我是机构
			$('#groupCode').focus(function(){
				var idCard=$('#name').val();
				if(idCard==''){
					$("#name").attr("value","请输入组织机构名称！");
					$("#name").addClass("personInpg");
					setTimeout(function () {
						$("#name").attr("value","")
						$("#name").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#address').focus(function(){
				var idCard=$('#groupCode').val();
				if(idCard==''){
					$("#groupCode").attr("value","请输入组织机构代码！");
					$("#groupCode").addClass("personInpg");
					setTimeout(function () {
						$("#groupCode").attr("value","")
						$("#groupCode").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#head').focus(function(){
				var idCard=$('#address').val();
				if(idCard==''){
					$("#address").attr("value","请输入单位地址！");
					$("#address").addClass("personInpg");
					setTimeout(function () {
						$("#address").attr("value","")
						$("#address").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#mobile').focus(function(){
				var idCard=$('#head').val();
				if(idCard==''){
					$("#head").attr("value","请输入联系人姓名！");
					$("#head").addClass("personInpg");
					setTimeout(function () {
						$("#head").attr("value","")
						$("#head").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#teamVerCode').focus(function(){
				var idCard=$('#mobile').val();
				if(idCard==''){
					$("#mobile").attr("value","请输入联系电话！");
					$("#mobile").addClass("personInpg");
					setTimeout(function () {
						$("#mobile").attr("value","")
						$("#mobile").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			
			$('#imgFile1').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList1"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form1').submit();
				}
				return false;
			});
			$('#imgFile2').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList2"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form2').submit();
				}
				return false;
			});
			$('#imgFile3').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList3"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form3').submit();
				}
				return false;
			});
			$('#imgFile4').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList4"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form4').submit();
				}
				return false;
			});
			$('#imgFile5').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList5"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
					$('#form5').submit();
				}
				return false;
			});
			//下一步按钮
		    $("#footButtom").click(function(){
		    	var imgsid1 = $('.io1').attr('id');
				var imgsid2 = $('.io2').attr('id');
				var imgsid3 = $('.io3').attr('id');
				var imgsid4 = $('.io4').attr('id');
				var imgsid5 = $('.io5').attr('id');
				var type = $('#typestate').val();
				if(typeof(imgsid1)=="undefined"){
					imgsid1="";
				}
				if(typeof(imgsid2)=="undefined"){
					imgsid2="";
				}
				if(typeof(imgsid3)=="undefined"){
					imgsid3="";
				}
				if(typeof(imgsid4)=="undefined"){
					imgsid4="";
				}
				if(typeof(imgsid5)=="undefined"){
					imgsid5="";
				}
				if(type==0){
					that.verify2(imgsid1,imgsid2,imgsid3,0,0,type);
				}
				else{
					that.verify2(0,0,0,imgsid4,imgsid5,type);
				}
		    });
			
			
			$('#usercode').click(function(){
				var nameObj = $('#MobileNum').val();
				//alert($('#usercode').text())
				if($('#usercode').text() != '发送验证码'){
					return false;
				}
				that.sendcode($(this),'#MobileNum','#wrcode','#wrcode',0);
			});
			
			$('#sendTeamVerCode').click(function(){
				var nameObj = $('#mobile').val();
				if($('#sendTeamVerCode').text() != '发送验证码'){
					return false;
				}
				that.sendcode($(this),'#mobile','#wrcode','#wrcode',0);
			});
			$("#img1").click(function(){
				var delV=$('.io1').attr('id');
				//alert(delV);
				var isDel=that.delPic(delV,$('.io1'));
				$(".io1").remove();
				$('#img1').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>身份证正面</span>');
				$('#imgList1').html(html.join(''));
				$('#imgFile1').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form1').submit();
					}
					return false;
				});
			});	
			$("#img2").click(function(){
				var delV=$('.io2').attr('id');
				//alert(delV);
				var isDel=that.delPic(delV,$('.io2'));
				$(".io2").remove();
				$('#img2').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>身份证反面</span></div>');
				$('#imgList2').html(html.join(''));
				$('#imgFile2').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form2').submit();
					}
					return false;
				});
			});	
			$("#img3").click(function(){
				var delV=$('.io3').attr('id');
				//alert(delV);
				var isDel=that.delPic(delV,$('.io3'));
				$(".io3").remove();
				$('#img3').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile3" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>手持身份证</span></div>');
				$('#imgList3').html(html.join(''));
				$('#imgFile3').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form3').submit();
					}
					return false;
				});
			});
			$("#img4").click(function(){
				var delV=$('.io4').attr('id');
				//alert(delV);
				var isDel=that.delPic(delV,$('.io4'));
				$(".io4").remove();
				$('#img4').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile4" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>机构证件</span></div>');
				$('#imgList4').html(html.join(''));
				$('#imgFile4').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form4').submit();
					}
					return false;
				});
			});
			$("#img5").click(function(){
				var delV=$('.io5').attr('id');
				//alert(delV);
				var isDel=that.delPic(delV,$('.io5'));
				$(".io5").remove();
				$('#img5').hide();
				html=[];
				html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile5" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>单位授权书</span></div>');
				$('#imgList5').html(html.join(''));
				$('#imgFile5').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form5').submit();
					}
					return false;
				});
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
					if(obj.selector=='#form1'){
					html.push('<div class="item add"><i id="img1">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io1" name="io1"/><span>身份证正面</span></div>');
					$('#imgList1').html(html.join(''));
					$("#img1").click(function(){
						var delV=$('.io1').attr('id');
						var isDel=that.delPic(delV,$('.io'));
						$(".io").remove();
						$('#img').hide();
						html=[];
						html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>身份证正面</span>');
						$('#imgList1').html(html.join(''));
						$('#imgFile1').change(function(){
							var p=that.photoPic(this);
							if(p){
								var imgBox=$("#imgList1"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
								addObj.hide();
								addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
								$('#form1').submit();
							}
							return false;
						});
					});				
				}
					else if(obj.selector=="#form2"){
						html.push('<div class="item add"><i id="img2">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io2" name="io2"/><span>身份证正面</span></div>');
						$('#imgList2').html(html.join(''));
						$("#img2").click(function(){
							var delV=$('.io2').attr('id');
							var isDel=that.delPic(delV,$('.io2'));
							$(".io2").remove();
							$('#img2').hide();
							html=[];
							html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>身份证反面</span>');
							$('#imgList2').html(html.join(''));
							$('#imgFile2').change(function(){
								var p=that.photoPic(this);
								if(p){
									var imgBox=$("#imgList2"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
									addObj.hide();
									addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
									$('#form2').submit();
								}
								return false;
							});
						});	
					}
					else if(obj.selector=="#form3"){
						html.push('<div class="item add"><i id="img3">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io3" name="io3"/><span>手持身份证</span></div>');
						$('#imgList3').html(html.join(''));
						$("#img3").click(function(){
							var delV=$('.io3').attr('id');
							var isDel=that.delPic(delV,$('.io3'));
							$(".io3").remove();
							$('#img3').hide();
							html=[];
							html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile3" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>身份证正面</span>');
							$('#imgList3').html(html.join(''));
							$('#imgFile3').change(function(){
								var p=that.photoPic(this);
								if(p){
									var imgBox=$("#imgList3"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
									addObj.hide();
									addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
									$('#form3').submit();
								}
								return false;
							});
						});	
					}
					else if(obj.selector=="#form4"){
						html.push('<div class="item add"><i id="img4">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io4" name="io4"/><span>机构证件</span></div>');
						$('#imgList4').html(html.join(''));
						$("#img4").click(function(){
							var delV=$('.io4').attr('id');
							var isDel=that.delPic(delV,$('.io4'));
							$(".io4").remove();
							$('#img4').hide();
							html=[];
							html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile4" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>机构证件</span>');
							$('#imgList4').html(html.join(''));
							$('#imgFile4').change(function(){
								var p=that.photoPic(this);
								if(p){
									var imgBox=$("#imgList4"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
									addObj.hide();
									addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
									$('#form4').submit();
								}
								return false;
							});
						});}
						else if(obj.selector=="#form5"){
							html.push('<div class="item add"><i id="img5">×</i><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io5" name="io5"/><span>单位授权书</span></div>');
							$('#imgList5').html(html.join(''));
							$("#img5").click(function(){
								var delV=$('.io5').attr('id');
								var isDel=that.delPic(delV,$('.io5'));
								$(".io5").remove();
								$('#img5').hide();
								html=[];
								html.push('<div class="item add"><a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile5" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><span>单位授权书</span>');
								$('#imgList5').html(html.join(''));
								$('#imgFile5').change(function(){
									var p=that.photoPic(this);
									if(p){
										var imgBox=$("#imgList5"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
										addObj.hide();
										addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
										$('#form5').submit();
									}
									return false;
								});
							});}	
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
	verify2:function(imgsid1,imgsid2,imgsid3,imgsid4,imgsid5,type){
		if(type==0){//个人
			var that=this,userName=$('#userName').val(),idCard=$('#idCard').val(),MobileNum=$('#MobileNum').val(),
			personalVerCode=$.trim($('#personalVerCode').val());
	        if(userName==''){
	            $("#userName").attr("value","请输入姓名！");
	            $("#userName").addClass("personInpg");
	            setTimeout(function () {
	                $("#userName").attr("value","")
	                $("#userName").removeClass("personInpg");
	            }, 2000);
	            return false;
	        };
	        if(idCard==''){
	            $("#idCard").attr("value","请输入身份证号！");
	            $("#idCard").addClass("personInpg");
	            setTimeout(function () {
	                $("#idCard").attr("value","")
	                $("#idCard").removeClass("personInpg");
	            }, 2000);
	            return false;
	        }
			that.isVaildId($("#idCard"));
	        if(MobileNum==''){
	            $("#MobileNum").attr("value","请输入手机号！");
	            $("#MobileNum").addClass("personInpg");
	            setTimeout(function () {
	                $("#MobileNum").attr("value","")
	                $("#MobileNum").removeClass("personInpg");
	            }, 2000);
	            return false;
	        }
	        if(personalVerCode==''){
	        	return d.alert({content:"请输入验证码！",type:'error'});
	            /*$("#personalVerCode").attr("value","请输入验证码！");
	            $("#personalVerCode").addClass("personInpg");
	            setTimeout(function () {
	                $("#personalVerCode").attr("value","")
	                $("#personalVerCode").removeClass("personInpg");
	            }, 2000);
	            return false;*/
	        }
			
	        var mobleCode = $.trim(that.isMobelCode("#personalVerCode"));
	        if(!mobleCode){
	        	return d.alert({content:"手机验证码错误！",type:'error'});
	        }
			if(imgsid1==''){
				return d.alert({content:"请上传身份证正面！",type:'error'});
			}
			if(imgsid2==''){
				return d.alert({content:"请上传身份证反面！",type:'error'});
			}
			if(imgsid3==''){
				return d.alert({content:"请上传手持身份证！",type:'error'});
			}
			var ids=imgsid1+','+imgsid2+','+imgsid3;
			$.ajax({
				url:dataUrl.authentication,
				data:{ids:ids,realName:userName,idCard:idCard,MobileNum:MobileNum,personalVerCode:personalVerCode},
				success: function(result){
					if(result.flag == 1){//成功
						 var canshu = result.errorMsg.split(',')
						window.location='http://www.17xs.org/newReleaseProject/gotoAidedPerson.do?userId='+canshu[2];
					}else if(result.flag == 0){//失败
						return d.alert({content:result.errorMsg,type:'error'});
					}
					else{//未登录
						window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/newReleaseProject/gotoReleaseProject.do';
					}
				}
			});
			
		}
		else if(type==1){//机构
			var that=this,address=$('#address').val(),name=$('#name').val(),groupCode=$('#groupCode').val(),head=$('#head').val(),
			mobile=$('#mobile').val(),teamVerCode=$.trim($('#teamVerCode').val());
			if(name==''){
				$("#name").attr("value","请输入组织/机构名称！");
				$("#name").addClass("personInpg");
				setTimeout(function () {
					$("#name").attr("value","")
					$("#name").removeClass("personInpg");
				}, 2000);
				return false;
			};
			if(groupCode==''){
				$("#groupCode").attr("value","请输入组织/结构代码！");
				$("#groupCode").addClass("personInpg");
				setTimeout(function () {
					$("#groupCode").attr("value","")
					$("#groupCode").removeClass("personInpg");
				}, 2000);
				return false;
			};
			if(address==''){
				$("#address").attr("value","请输入单位地址！");
				$("#address").addClass("personInpg");
				setTimeout(function () {
					$("#address").attr("value","")
					$("#address").removeClass("personInpg");
				}, 2000);
				return false;
			};
			if(head==''){
				$("#head").attr("value","请输入联系人姓名！");
				$("#head").addClass("personInpg");
				setTimeout(function () {
					$("#head").attr("value","")
					$("#head").removeClass("personInpg");
				}, 2000);
				return false;
			};
			if(mobile=='' || !that.checkMobileStr(mobile)){
				return d.alert({content:"请输入正确的手机号码！",type:'error'});
			}
			if(teamVerCode==''){
				return d.alert({content:"请输入验证码！",type:'error'});
				/*$("#teamVerCode").attr("value","请输入验证码！");
				$("#teamVerCode").addClass("personInpg");
				setTimeout(function () {
					$("#teamVerCode").attr("value","")
					$("#teamVerCode").removeClass("personInpg");
				}, 2000);
				return false;*/
			};
			var mobleCode = $.trim(that.isMobelCode("#teamVerCode"));
			if(!mobleCode){
				return d.alert({content:"手机验证码错误！",type:'error'});
			}
			if(imgsid4==''){
				return d.alert({content:"请上传机构证件！",type:'error'});
			}
			if(imgsid5==''){
				return d.alert({content:"请上传单位授权书！",type:'error'});
			}
			var ids=imgsid4+","+imgsid5;
			$.ajax({
				url:dataUrl.authenticationTeam,
				data:{ids:ids,name:name,groupCode:groupCode,head:head,mobile:mobile,personalVerCode:teamVerCode,address:address},
				success: function(result){
					if(result.flag == 1){//成功
						 var canshu = result.errorMsg.split(',')
							window.location='http://www.17xs.org/newReleaseProject/institutionReleaseProject.do?companyId='+canshu[2];
					}else if(result.flag == 0){//失败
						return d.alert({content:result.errorMsg,type:'error'});
					}
					else if(result.flag == 10005){//已经提交审核了
						return d.alert({content:"您申请的企业已经在审核了，请耐心等待审核信息！",type:'error'});
					}
					else{//未登录
						window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/newReleaseProject/gotoReleaseProject.do';
					}
				}
			});
		}
	},
	
	//---------------------------------------------------------------
	isValidName: function(nameObj) {//nameId, promptId
    	var that=this,nameVal = nameObj,ret;
         
		var reg=/^[1-9]\d*$|^0$/,c=that.checkMobileStr(nameVal);  
        if(!c){ 
        	$("#msg").html('<p>请输入正确的手机号码！</p>'); 
		    $("#msg").show();
		    setTimeout(function () {
	            $("#msg").hide();
	        }, 2000);
		    return false; 
        }
	   return true;
    }, 
		isMobelCode:function(obj){
			var nameObj=$(obj),nameVal = nameObj.val();
			var len = nameVal.length; 
			var reg=/^\d{6}$/;
			if(reg.test(nameVal)){
					return true;	
			}else{
				return false;
			}
			
		},
		//发送手机验证码
		sendcode:function(obj,phoneobj,codeobj,phonecodeboj,types){
			var that=this,wait=60,ret;
			if(wait <60){ return false; }
			var phone = $(phoneobj).val();
			var cause = "用户验证码";
			if(phone=='' || phone=='用户名/手机号码'){
				$("#msg").html('<p>请输入正确的手机号码！</p>');
				$("#msg").show();
				setTimeout(function () {
		            $("#msg").hide();
		        }, 2000);
				//$(phoneobj).focus();
				return false;
			} 
			var ret = that.isValidName(phone);
			if(!ret){
				return false;
			}
			if($('#usercode').text() != '发送验证码'){
				return false;
			}
			if($('#sendTeamVerCode').text() != '发送验证码'){
				return false;
			}
			if(phoneobj=='#MobileNum'){
				that.countDown('#usercode', 60);
			}
			
			if(phoneobj=='#mobile'){
				that.countDown("#sendTeamVerCode", 60);
			}
			if(types==0){
				//var codes=this.isCode($(codeobj),0);
				if(false){return;} 
			}else if(type==1){
				var c=that.checkMobileStr(phone);
				if(!c){return false;}
				types="certification";
			}else{
				var c=that.checkMobileStr(phone);
				if(!c){return false;}
				types="enterprise_validate";
			}
			$.ajax( {
				url: dataUrl.SSO_MobileCode,
				cache   : false,
				type: "POST",
				data: {phone:phone,flag:'h5Register'},
				success : function(result) { 
						if(result.flag==1){
							//msgObj.html('手机验证码发送成功，请查收！');
							
							ret = true;
						}else{
							$("#msg").html('<p>'+result.errorMsg+'</p>');
							$("#msg").show();
							setTimeout(function () {
					            $("#msg").hide();
					        }, 2000);
							 $(phonecodeboj).focus();
							 ret = false;
						}
				},
				error : function(result) { 
					$("#msg").html('<p>发送失败，请稍后重试。</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
				}
			});
		},
		isphone:function(nameObj){
			var that=this,nameObj=$(nameObj),msgObj=nameObj.parent().next(''),nameVal = nameObj.val();
	        var len = nameVal.length;
			
			var reg=/^[1-9]\d*$|^0$/;  
	        if(reg.test(nameVal)==true&&len!=11){ 
			    msgObj.html('<i class="error"></i><p>手机号码格式不正确</p>'); 
			    return false; 
	        }
			
			var data={},c=that.checkMobileStr(nameVal);
			if(!c){
				msgObj.html('<i class="error"></i><p>请输入正确的手机号码</p>'); 
				return false;
			}else{
				 msgObj.html('<i class="right"></i>'); return true;
			}
		},
		checkMobileStr:function(str) {
			if(str.length!= 11) return false;
			var re = /^1[3|4|5|7|8][0-9]\d{8}$/;
			if (!re.test(str)) {
				return false;
			}
			return true;
		},
		countDown:function(obj,second){

	        // 如果秒数还是大于0，则表示倒计时还没结束
	        if(second>=0){
	            // 获取默认按钮上的文字
	            var buttonDefaultValue ="发送验证码";
	            if(buttonDefaultValue === 'undefined' ){
	                buttonDefaultValue =  $(obj).html();
	            }
	            // 按钮置为不可点击状态
	            $(obj).disabled = true;
	            // 按钮里的内容呈现倒计时状态
	            var setext =  buttonDefaultValue+'('+second+')';
	            $(obj).html(setext);
	            // 时间减一
	            second--;
	            // 一秒后重复执行
	            setTimeout(function(){reg.countDown(obj,second);},1000);
	            // 否则，按钮重置为初始状态
	        }else{
	            // 按钮置未可点击状态
	            $(obj).disabled = false;
	            // 按钮里的内容恢复初始状态
	            $(obj).html("发送验证码");
	        }   
	    },
	    isVaildId:function(IdObj){
			IdObj = $(IdObj);
			var msgObj=IdObj.parent().next(),IdVal=$.trim(IdObj.val()).toUpperCase(); 
			var rst = this.checkId(IdObj);
			if(rst!=null){
				return d.alert({content:rst,type:'error'});
			}
			  
			if(IdVal=="" || IdVal.empty()){ 
				return d.alert({content:"请输入正确定的身份证件号码！",type:'error'});
			}else{
				return true;
			}
		},
	    checkId: function(IdObj) {
			IdObj = $(IdObj);
			var IdVal=$.trim(IdObj.val()),len=IdVal.length; 
			if(len<4){
				return '请正确输入身份证号！';
			}
			if(len!=15 && len!=18){
				return '身份证号码应为15位或18位！';
			}
			var idResult = this.isIDFormat(IdVal);
			if (idResult != 0) {
				return "身份证号码有误，请检查确认！";
			}
	
			if(len==18){
				 var age = 0;
				 var date = new Date();
				 var year = date.getFullYear();
				 var month = date.getMonth()+1;
				 var day = date.getDate();
				 if (month > parseInt(IdVal.substr(10, 2)))// 判断当前月分与编码中的月份大小
				 age = year - IdVal.substr(6, 4);
				 else if (month = parseInt(IdVal.substr(10, 2)) && day >= parseInt(IdVal.substr(12, 2)))
				 age = year - IdVal.substr(6, 4);
				 else
				 age = year - IdVal.substr(6, 4) - 1;
				 //不需要判断年龄
				 /*if(age<18)
				   return "未满18周岁不允许购买彩票";*/
			
			}
			return null;
       }, 
       isIDFormat:function(idcard){
			var errors=['0', '身份证号码位数不对!', '身份证号码出生日期超出范围或含有非法字符!', '身份证号码校验错误!', '身份证地区非法!'];
			var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
			var idcard,Y,JYM,S,M; 
			var idcard_array=idcard.split(''); 
			//地区检验
			if(!area[parseInt(idcard.substr(0,2))]) return errors[4];
			//身份号码位数及格式检验
			switch(idcard.length)
			{
				case 15: 
					var ereg;
					 if ((parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )) 
						ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;  
					  else
						ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;  
					if(ereg.test(idcard))
						return errors[0];
					else 
						return errors[2];
					break;
				case 18:
					if(parseInt(idcard.substr(6,4))%4==0||(parseInt(idcard.substr(6,4))%100==0&&parseInt(idcard.substr(6,4))%4==0))
						ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合
					else
						ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
					if(ereg.test(idcard))
					{
						S=(parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 
						+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 
						+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 
						+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 
						+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 
						+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 
						+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 
						+ parseInt(idcard_array[7]) * 1
						+ parseInt(idcard_array[8]) * 6
						+ parseInt(idcard_array[9]) * 3; 
						Y = S % 11; 
						M = "F"; 
						JYM = "10X98765432"; 
						M = JYM.substr(Y,1);//判断校验位 
						//下面if else 校验身份证最后一位 00后身份证校验不成功 暂时去除判断
						/*if(M == idcard_array[17])
							return errors[0]; //检测ID的校验位 
						else 
							return errors[3];*/
						return errors[0];
					}
					else 
						return errors[2]; 
					break; 
				default: 
					return errors[1]; 
			}
		}
	}
	
	reg.init()
})