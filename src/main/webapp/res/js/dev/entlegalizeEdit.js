var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt"
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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.uCENnavEtSite="p-einformation";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		LEGALIZE:base+"/enterprise/coredata/updateRealName.do"//实名认证
	}
	var legalize={
		imgphoto:[],
		imgorga:'',
		photo1:'',
		photo2:'',
		init:function(){
			var that=this;
			
			var type=$('#entTypeSelect').val();
			var imgphoto=$('#enttype .list').eq(type).find('.pic').children('img').attr('data'),imgorga=$('#picorga img').attr('data'),photo1=$('#pic1 img').attr('data'),photo2=$('#pic2 img').attr('data');
			that.imgphoto[type]=imgphoto;
			that.imgorga=imgorga;
			that.photo1=photo1;
			that.photo2=photo2;
			
			
			$('#entTypeSelect').change(function(){
				var type=$('#entTypeSelect').val();
				that.entTypeText(type);
			})
			
			$("body").on("click","#entPicshow img",function(){ 
				var bigPic=$(this).attr('bigSrc'),html=[];
				html.push('<img src="'+bigPic+'" alt="" />');
				var o ={t:"营业执照图例",c:html.join("")},css={width:'600px',height:'430px'};  
				d.open(o,css,"entBigPic");
			})
			$('.idphoto').click(function(){
				var html=[],data=eval( '(' + $(this).attr('data') + ')' ),bgpic=base+"/res/images/login/idbg_03.jpg",
				tle='请准备本人<span class="ftorange">身份证（含信息页和国徽页）</span>或<span class="ftorange">临时身份证（信息页）</span>';
				if(data){
					data.tle?tle=data.tle:'';
					data.bgpic? bgpic=base+"/res/images/"+data.bgpic:'';
				}
				html.push('<h2 class="ym">按以下要求准备证件，可帮助你快速通过审核</h2>');
				html.push('<div class="idl">');
				html.push('<p>'+tle+'</p>');
				html.push('<p>证件图片要求：</p>');
				html.push('<p class="item">* 证件拍摄完整，不出现缺角</p>');
				html.push('<p class="item">* 各项信息及头像清晰可见，容易识别</p>');
				html.push('<p class="item">* 证件必须真实拍摄，不能使用复印件</p>');
				html.push('<p class="item">* 图片中无反光，无遮挡，无水印、logo等情况</p>');
				html.push('<p class="item">* 图片仅支持PNG、JPG、JPEG、BMP格式</p>');
				html.push('</div>');
				html.push('<div class="idr" style="background:url('+bgpic+') top center no-repeat">');
				html.push('</div>');
				var o ={t:"证件图片要求",c:html.join("")},css={width:'780px'};  
				d.open(o,css,"wregid");
			})
			
			
			$('#rlbokbtn').click(function(){
				that.legalizeSubmit($(this));
			})
			
			
			$('#legalPerson').change(function(){
			   en.isValidRealname($(this)); 
			})
			$('#legalIdentify').change(function(){
			  en.isVaildId($(this)); 
			})
			
			$('#linkPhone').blur(function(){
				en.isphone($(this));
			})
			$('#pSendT').click(function(){
				en.sendcode($(this),'#linkPhone','#linkPhone','#phoneCode',2);	
			})
			
			$('#phoneCode').blur(function(){
			  var msgObj=$('#phoneCode').parent().parent().children('.wrinfo')
			  var code=that.isCode($("#phoneCode"),0);
			  if(code){msgObj.html('').hide();}
			})
			
			$('#enttype .list input').focus(function(){
				var msgObj=$(this).parent().next('.wrinfo');
				msgObj.html('');
			})
			$('#enttype .list input').blur(function(){
				var type=$('#entTypeSelect').val();
				that.isIdentifi($(this),type);
			})
//			$('#enttype .list textarea').focus(function(){
//				var msgObj=$(this).next('.wrinfo');
//				msgObj.html('');
//			})
			$('#enttype .list textarea').blur(function(){
				var v=$(this).val(), msgObj=$(this).next('.wrinfo');;
				if(!v){
				 msgObj.html('<i class="error"></i><p>请输入业务范围！</p>');
				return false;
				}else{
					msgObj.html('<i class="right"></i>');
					return true;
				}
				
			})
			
			$('#organization').focus(function(){
				var msgObj=$(this).parent().next('.wrinfo');
				msgObj.html('');
			})
			
			$('#organization').blur(function(){
				that.isOrganization($(this));
			})
			
			$('#linkman').focus(function(){
				var msgObj=$(this).parent().next('.wrinfo');
				msgObj.html('');
			})
			
			$('#linkman').blur(function(){
				var obj=$(this),v=obj.val(),msgObj=obj.parent().next('.wrinfo');
				if(!v){
					msgObj.html('<i class="error"></i>请输入联系人姓名！');
					return false;
				}else{
				    msgObj.html('<i class="right"></i>');
					return true;
				}
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
			$('#imgFileIden0').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#picIden0 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formIden0').submit();
				}
				return false;
			});
			$('#imgFileIden1').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#picIden1 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formIden1').submit();
				}
				return false;
			});
			$('#imgFileIden2').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#picIden2 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formIden2').submit();
				}
				return false;
			});
			$('#imgFileIden3').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#picIden3 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formIden3').submit();
				}
				return false;
			});
			$('#imgFileorga').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#picorga img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formorga').submit();
				}
				return false;
			});
			that.ajaxForm($('#form1'),$('#pic1 img'),'photo1');
			that.ajaxForm($('#form2'),$('#pic2 img'),'photo2');
			that.ajaxForm($('#formIden0'),$('#picIden0 img'),'imgphoto[0]');
			that.ajaxForm($('#formIden1'),$('#picIden1 img'),'imgphoto[1]');
			that.ajaxForm($('#formIden2'),$('#picIden2 img'),'imgphoto[2]');
			that.ajaxForm($('#formIden3'),$('#picIden3 img'),'imgphoto[3]');
			that.ajaxForm($('#formorga'),$('#picorga img'),'imgorga');
				
		},
		entTypeText:function(type){
			var pichtml=[],html=[],htmlText=[],listTle=[];
			pichtml[0]='<img src="'+base+'res/images/Enterprise/yqsmill.jpg" alt="" bigsrc="'+base+'res/images/Enterprise/yqbig.jpg">';
			pichtml[1]='<img src="'+base+'res/images/Enterprise/sydwsmill.jpg" alt="" bigsrc="'+base+'res/images/Enterprise/sydwbig.jpg">';
			pichtml[2]='<img src="'+base+'res/images/Enterprise/shttsmaill.jpg" alt="" bigsrc="'+base+'res/images/Enterprise/shttbig.jpg">';
			pichtml[3]='<img src="'+base+'res/images/Enterprise/dzsmill.jpg" alt="" bigsrc="'+base+'res/images/Enterprise/dzbig.jpg">';
			listTle[0]="企业信息<span>按照证书上的内容逐字填写</span>";
			listTle[1]="事业单位信息<span>按照证书上的内容逐字填写</span>";
			listTle[2]="社会团体信息<span>按照证书上的内容逐字填写</span>";
			listTle[3]="党政及国家机关信息<span>按照证书上的内容逐字填写</span>";
			$('#enttype .list').hide().eq(type).show();
			$('#listTle').html(listTle[type]);
			$('#entPicshow').html(pichtml[type]);
		},
		photoPic:function(file){
			var filepath =$(file).val();
				var extStart=filepath.lastIndexOf(".")+1;		
				var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
				if(ext!='JPG' && ext!='PNG' && ext!='JPEG' &&  ext!='BMP')
				{	
					d.alert({content:"上传图片仅支持PNG、JPG、JPEG、BMP格式文件，请重新选择！",type:'error'});
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
		ajaxForm:function(obj,showobj,photoAdress){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'),msgObj=$(showobj).parent().parent().children('.wrinfo'),srcOld=base+"res/images/login/uploader.jpg"; 
					if(json.result==1){
						$(showobj).attr('src',srcOld);
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						$(showobj).attr('src',json.imageUrl);
						msgObj.html('');
						switch (photoAdress){ 
						case "imgorga" : that.imgorga=json.imageId; 
						break; 
						case "photo1" : that.photo1=json.imageId;  
						break; 
						case "photo2" : that.photo2=json.imageId;  
						break; 
						case "imgphoto[0]" : that.imgphoto[0]=json.imageId;  
						break;
						case "imgphoto[1]" : that.imgphoto[1]=json.imageId;  
						break;
						case "imgphoto[2]" : that.imgphoto[2]=json.imageId;  
						break;
						case "imgphoto[3]" : that.imgphoto[3]=json.imageId;  
						break;
						} 
						/*if(photoAdress=="imgorga"){
							that.imgorga=json.imageId;
						}
						console.log(that.imgorga)*/
					}
				},
				error: function(data) {
					var srcOld=base+"res/images/login/uploader.jpg";
					$(showobj).attr('src',srcOld);
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		legalizeSubmit:function(){
			 var that=this,type=$('#entTypeSelect').val(),identification=$('#identification'+type).val(),business=$('#business'+type).val(),organization=$('#organization').val(),legalPerson=$("#legalPerson").val(),legalIdentify=$('#legalIdentify').val(),phoneCode=$('#phoneCode').val(),linkPhone=$('#linkPhone').val(),linkman=$('#linkman').val(),m;
			// if(en.isIn){ 
			  
			  var code=that.isCode($("#phoneCode"),0);
			  if(!code){m="#phoneCode";}
			  var phone=en.isphone($('#linkPhone'));
			  if(!phone){m="#linkPhone";}
			  if(!linkman){m="#linkman";}
			  if(!that.photo1){
				   $('#msgphoto1').html('<i class="error"></i>请上传身份证正面')
				   m="#msgphoto1";
		      }
		      if(!that.photo2){
			   $('#msgphoto2').html('<i class="error"></i>请上传身份证背面');
			   m="#msgphoto2";
		      }
			  var Id = en.isVaildId($("#legalIdentify")); 
			  if(!Id){m="#legalIdentify";} 
			  var isName = en.isValidRealname($("#legalPerson")); 
			  if(!isName){m="#legalPerson";}
			  if(!that.imgorga){
				   $('#msgphotoorga').html('<i class="error"></i>请上传组织机构证件')
				   m="#msgphotoorga";
		      }
			  var isOrgan=that.isOrganization($('#organization'));
			  if(!isOrgan){m='#organization'}
//			  if(type>0){
//				  if(!business){m="#business"+type}
//			  }
			  
			  if(!that.imgphoto[type]){
				  var msgobj=$('#imgFileIden'+type).parents('a').next('.wrinfo');
			   msgobj.html('<i class="error"></i>请上传执照图片');
			   m="#imgFileIden"+type;
		      }
			  var ident=that.isIdentifi($('#identification'+type),type);
			  if(!ident){m="#identification"+type;}  
			   if(m){
				   that.click_scroll(m);
				   m="";
				   return false;
			   }
			   that.submitdata();	  
		},
		submitdata:function(){
			var that=this,p=[],type=$('#entTypeSelect').val(),identification=$('#identification'+type).val(),business=$('#business'+type).val(),organization=$('#organization').val(),legalPerson=$("#legalPerson").val(),legalIdentify=$('#legalIdentify').val(),phoneCode=$('#phoneCode').val(),linkPhone=$('#linkPhone').val(),linkman=$('#linkman').val(),vid=$('#vid').val();
			
			p.push('type='+type);
			p.push('identification='+identification);
			if(business){
				p.push('business='+business);
			}
			p.push('organization='+organization);
			p.push('idenId='+that.imgphoto[type]);
			p.push('orgaId='+that.imgorga);
			p.push('legalPerson='+legalPerson);
			p.push('legalPimg1='+that.photo1);
			p.push('legalPimg2='+that.photo2);
			p.push('legalIdentify='+legalIdentify);
			p.push('linkman='+linkman);
			p.push('linkPhone='+linkPhone);
			p.push('phoneCode='+phoneCode);
			p.push('vid='+vid);
			$.ajax({
				url     : WebAppUrl.LEGALIZE,
				data    : p.join('&') + '&t='+new Date().getTime(),
				type: 'POST',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag==-1){
						en.show(that.submitdata);
					}else{
						window.location.href=base+"/enterprise/core/realName.do";//跳转到成功页面
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
			},200);
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
			
		},
		isIdentifi:function(obj,type){
			var nameObj=$(obj),rst=nameObj.val(),len=rst.length,msgObj=nameObj.parent().next();
			if(len<1){
				msgObj.html('<i class="error"></i><p>请输入企业注册号！</p>');
				return false;
			}else{
			    msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		isOrganization:function(obj){
			var nameObj=$(obj),v=nameObj.val(),len=v.length,msgObj=nameObj.parent().next(),reg=/^[0-9-]{9,10}$/,rst=reg.test(v);
			if(len<1){
				msgObj.html('<i class="error"></i><p>请输入组织机构代码证！</p>');
				return false;	
			}
			if(!rst){
			    msgObj.html('<i class="error"></i><p>组织机构代码证长度需为9位或10位（例：00001234-1）</p>');
				return false;
			}else{
			    msgObj.html('<i class="right"></i>');
				return true;
			}
		}
		
	}
	 
	legalize.init();
});