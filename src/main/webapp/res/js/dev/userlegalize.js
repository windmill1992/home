var base=window.baseurl;
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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-name";//如需导航显示当前页则加此设置
	var lzTpe=$('#legalize').val();
	if(lzTpe==2){
		window.sitePage="p-joinGood";
	}else if(lzTpe==3){
		window.sitePage="p-seekHelp";
	}
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		LEGALIZE:base+"user/perfect.do"//实名认证
	}
	var legalize={
		photo1:'',
		photo2:'',
		photo3:'',
		init:function(){
			var that=this;
			var v=$('#msg').val();
			if(v){
				d.alert({content:v,type:'error'});
			}
			$('#realname').change(function(){
			   en.isValidRealname($(this)); 
			})
			$('#idCard').change(function(){
			  en.isVaildId($(this)); 
			})
			$('#phone').blur(function(){
				en.isphone($(this));
			})
			$('#pSend').click(function(){
				en.sendcode($(this),'#phone','#phone','#phoneCode',1);	
			})
			$('#phoneCode').blur(function(){
			  var msgObj=$('#phoneCode').parent().parent().children('.wrinfo')
			  var code=that.isCode($("#phoneCode"),0);
			  if(code){msgObj.html('').hide();}
			})
			$('#imgFile1').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#pic1 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#form1').submit();
				}
				return false;
			});
			$('#imgFile2').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#pic2 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#form2').submit();
				}
				return false;
			});
			$('#imgFile3').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#pic3 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#form3').submit();
				}
				return false;
			});
			that.ajaxForm($('#form1'),$('#pic1 img'),3);
			that.ajaxForm($('#form2'),$('#pic2 img'),2);
			that.ajaxForm($('#form3'),$('#pic3 img'),1);
			$('#rlbokbtn').click(function(){
				that.fastSignT($(this));
			})
			$('.idphoto').click(function(){
				var html=[];
				html.push('<h2 class="ym">按以下要求准备证件，可帮助你快速通过审核</h2>');
				html.push('<div class="idl">');
				html.push('<p>请准备本人<span class="ftorange">身份证（含信息页和国徽页）</span>或<span class="ftorange">临时身份证（信息页）</span></p>');
				html.push('<p>证件图片要求：</p>');
				html.push('<p class="item">* 证件拍摄完整，不出现缺角</p>');
				html.push('<p class="item">* 各项信息及头像清晰可见，容易识别</p>');
				html.push('<p class="item">* 证件必须真实拍摄，不能使用复印件</p>');
				html.push('<p class="item">* 图片中无反光，无遮挡，无水印、logo等情况</p>');
				html.push('<p class="item">* 图片仅支持PNG、JPG、JPEG、GIF、BMP格式</p>');
				html.push('</div>');
				html.push('<div class="idr">');
				html.push('</div>');
				var o ={t:"证件图片要求",c:html.join("")},css={width:'780px'};  
				d.open(o,css,"wregid"); 
			})	
		},
		photoPic:function(file){
			var filepath =$(file).val();
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
		ajaxForm:function(obj,showobj,type){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'),srcOld=base+"res/images/login/uploader.jpg"; 
					if(json.result==1){
						$(showobj).attr('src',srcOld);
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						$(showobj).attr('src',json.imageUrl);
						if(type==1){
							that.photo1=json.imageId;
							$('#msgphoto3').html('');
						}else if(type==2){
							that.photo2=json.imageId;
							$('#msgphoto2').html('');
						}else{
							that.photo3=json.imageId;
							$('#msgphoto1').html('');
						}
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		fastSignT:function(){
			 var that=this,realname=$("#realname").val(),Id=$('#idCard').val(),m,isshow=$('#state').val();
			 if(en.isIn){ 
			  if(!that.photo1){
				   $('#msgphoto3').html('<i class="error"></i>请上传身份证背面')
				   m="#msgphoto3";
			   }
			   if(!that.photo2){
				   $('#msgphoto2').html('<i class="error"></i>请上传身份证正面');
				   m="#msgphoto2";
			   }
			   if(!that.photo3){
				   $('#msgphoto1').html('<i class="error"></i>请上传手持身份证照片');
				   m="#msgphoto1";
			  }
			 if(!isshow){
			  var code=that.isCode($("#phoneCode"),0);
			  if(!code){m="#phoneCode";}
			  var phone=en.isphone($('#phone'));
			  if(!phone){m="#phone";}
			 }
			  var Id = en.isVaildId($("#idCard")); 
			  if(!Id){m="#idCard";} 
			  var isName = en.isValidRealname($("#realname")); 
			  if(!isName){m="#realname";}  
			   if(m){
				   that.click_scroll(m);
				   m="";
				   return false;
			   }
			   isshow?that.submitdata(0):that.submitdata(1);
			 }else{
				 en.show(that.fastSignT);
			 }
			   //实名认证		  
		},
		submitdata:function(type){
			var that=this,p=[],realname=encodeURI($('#realname').val()),idCard=$('#idCard').val(),phone=$('#phone').val(),phoneCode=$('#phoneCode').val(),imageId=[];
			imageId.push(that.photo1);
			imageId.push(that.photo2);
			imageId.push(that.photo3);
			p.push('realname='+realname);
			p.push('idCard='+idCard);
			if(type){
				p.push('phone='+phone);
				p.push('phoneCode='+phoneCode);
			}
			p.push('imageId='+imageId.join(','));
			$.ajax({
				url     : WebAppUrl.LEGALIZE,
				data    : p.join('&') + '&t='+new Date().getTime(),
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.reslut=='0'){
						var lzType=$('#legalize').val();
						if(lzType==1){
							$('#rzpage').html('<div class="lgl-state yh"><em></em>实名认证进入审核中...</div>');
							$("body,html").animate({
								scrollTop:0  //让body的scrollTop等于pos的top，就实现了滚动
							},500);
							return false;
						}else if(lzType==2){
							window.location=base+"/user/steward/second.do";
							return false;
						}else if(lzType==3){
							window.location=base+"/ucenter/pcreate.do";
							return false;
						}
					}else{
						d.alert({content:result.error,type:'error'});
						return false;
					}
				},
				error   : function(r){ 
					d.alert({content:'实名认证失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		click_scroll:function(id_){
			var scroll_offset = $(id_).offset();  //得到pos这个div层的offset，包含两个值，top和left
			$("body,html").animate({
				scrollTop:scroll_offset.top  //让body的scrollTop等于pos的top，就实现了滚动
			},500);
			return false;
		},
		isCode:function(nameObj,type){
			var nameObj=$(nameObj),msgObj=nameObj.parent().parent().children('.wrinfo'),nameVal = nameObj.val();
			var len = nameVal.cnLength(); 
			if(len<6||len>6){
				msgObj.html('<i class="error"></i><p>验证码不正确</p>').show();
	            return false;
			}
			if(type){//验证码验证
				$.ajax({
				url:WebAppUrl.SSO_code,
				data:data,
				//async:false,//同步
				success:function(result){
				   //验证码请求后处理 
				   var rst=$.trim(result);
				   if(rst=="1") { return true;}else{ return false }
				   
				} 
			  });
			}else{return true;}
			
		}
	}
	 
	legalize.init();
});