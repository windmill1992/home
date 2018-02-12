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
		"area"  : "util/area" ,
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","head","entry","dialog","userCenter","ajaxform","area"],function($,h,en,d,uc,f,a){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-name";//如需导航显示当前页则加此设置
	h.init();
	en.init();  
	d.init();
	uc.init();
	var len=$('#province').length;
	if(len>0){
	a.init({selectsId:["province","city","county"],def:[0,0,0]});
	}
	WebAppUrl={
		PERSONPERFET:base+"enterprise/coredata/personPerfet.do",// 个人完善资料接口
		VERIFYEMPLOYE:base+"enterprise/coredata/verifyEmploye.do",//获取验证企业员工
		DOVERIFY:base+"enterprise/coredata/doVerify.do",
		remind:base+'message/coredata/cmsg.do'
	}
	legalize={
		photo:'',
		init:function(){
			var that=this;
			
			var imgphoto=$('#entLogo img').attr('data');
			that.photo=imgphoto;
			
			that.ajaxForm($('#formLogo'),$('.entLogo img'),1);
			$('#imgFileLogo').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#formLogo').submit();
				}
				return false;
			});
			
			$('#rlbokbtn').click(function(){
				that.submitdata();
			})
			$('#companyId').change(function(){
				var v=$(this).children('option:selected').val();
				if(v=="其它"){
					$('#companyName').show();
				}else{
					$('#companyName').hide().val('');
				}
			})
			$('#pInvitation').click(function(){
				$('#company').attr('data',$(this).attr('data'));
				that.share();
			})
			
			$('#remind').click(function(){
				var v=$(this).attr('data').split('|');
				if(v[1]){
					that.remind(v[1]);
				}
			})
			$("body").on("click",".qqim",function(){ 
				 that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				 that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				 that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				 that.bshare(event,'weixin',0);
			}).on("mousover",".delLogo",function(){ 
				 $(this).children('em').show();
			}).on("mousout",".delLogo",function(){ 
				 $(this).children('em').hide();
			})
			
				
		},
		remind:function(v){
			var p=[];
			p.push('type=eCertify');
			p.push('receiver='+v);
			$.ajax({
				url     : WebAppUrl.remind,
				type: 'POST',
				data    : p.join('&') + '&t='+new Date().getTime(),
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.remind);
					}else{
						d.alert({content:'提醒成功！',type:'ok'});
					}
				},
				error   : function(r){ 
					d.alert({content:'完善资料失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		share:function(){
			var html=[],v=$('#company').attr('data').split('|'),name=$.cookie.get("sjj_username");
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+v[0]+'公司，您好，'+name+'邀请您加入善园基金会，爱心传递，一起行善</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" >QQ好友</a>');
			html.push('<a class="qzone">QQ空间</a>');
			html.push('<a class="sinaminiblog" >新浪微博</a>');
			html.push('<a class="weixin">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
		bshare:function(event,o){
			var v=$('#company').attr('data').split('|'),name=$.cookie.get("sjj_username")
			 bShare.addEntry({
			  title: "【善园基金会】",
			  url: base+"/user/sso.do?projectId="+v[1],
			  summary:v[0]+'公司，您好，'+name+'邀请您加入善园基金会，爱心传递，一起行善',
			  pic: ""
			});
			bShare.share(event,o,0);
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
					var json = eval('(' + data + ')'); 
					if(json.result==1){
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						if(showobj){
							$(showobj).attr('src',json.imageUrl);
						}
						that.photo=json.imageId;
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		submitdata:function(){
			var that=this,p=[],companyId,companyName=$('#companyName').val(),location=a.getArea(),detailAddress=$('#detailAddress').val(),postion=$('#postion').val(),headImg=that.photo;
			!headImg?headImg=0:'';
			companyId=$('#companyId').find("option:selected").attr('tel');
			p.push('headImg='+headImg);
			p.push('companyId='+companyId);
			p.push('companyName='+companyName);
			p.push('location='+location);
			p.push('detailAddress='+detailAddress);
			p.push('postion='+postion);
			$.ajax({
				url     : WebAppUrl.PERSONPERFET,
				type: 'POST',
				data    : p.join('&') + '&t='+new Date().getTime(),
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.submitdata);
					}else{
						window.location.href = base+"enterprise/core/pInfoSuccess.do";
						
					}
				},
				error   : function(r){ 
					d.alert({content:'完善资料失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		click_scroll:function(id_){
			var scroll_offset = $(id_).offset();  //得到pos这个div层的offset，包含两个值，top和left
			$("body,html").animate({
				scrollTop:scroll_offset.top  //让body的scrollTop等于pos的top，就实现了滚动
			},500);
			return false;
		}
		
	}
	legalize.init();
});