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
	window.uCENnavEtSite="p-einformation";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		ENTERPRISE:base+"/enterprise/coredata/perfect.do"//企业完善资料接口
	}
	var legalize={
		photo1:'',
		photo2:'',
		init:function(){
			var that=this;
			var type=$('#entTypeSelect').val();
			var photo1=$('#pic1 img').attr('data'),photo2=$('#pic2 img').attr('data');
			that.photo1=photo1;
			that.photo2=photo2;
			
			$('#imgFileLogo').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#pic1 img').attr('src',base+"/res/images/common/bar2.gif");
					$('#formLogo').submit();
				}
				return false;
			});
			$('#imgFileFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#pic2 img').attr('src',base+"/res/images/common/bar3.gif");
					$('#formFile').submit();
				}
				return false;
			});
			
			that.ajaxForm($('#formLogo'),$('.entLogo img'),1);
			that.ajaxForm($('#formFile'),$('.entfileBtn img'),2);

			$('#entExplain').click(function(){
				var html=[];
				html.push('<h2>企业助善</h2>');
				html.push('<p class="text">通过微信、微博等互联网平台，号召网友在平台注册，企业将善款发放给网友，以网友名义进行捐款，企业和注册网友数据双向统计。</p>');
				html.push('<div class="line"></div>');
				html.push('<h2>发起助善</h2>');
				html.push('<p><img src="'+base+'/res/images/Enterprise/explainpic_03.jpg"></p>');
				html.push('<div class="center"><a href="'+base+'/project/index.do" title="" class="btn">发起助善</a></div>');
				var o ={t:"企业助善:",c:html.join("")},css={width:'600px',height:'385px'};  
				d.open(o,css,"entExplain"); 
			})	
			
			
			$('#entSubmit').click(function(){
				that.submitdata();
			})
		},		
		photoPic:function(file){
			var filepath =$(file).val();
				var extStart=filepath.lastIndexOf(".")+1;		
				var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
				if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='BMP')
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
						$(showobj).attr('src','');
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						if(showobj){
							$(showobj).attr('src',json.imageUrl);
						}
						if(type==1){
							that.photo1=json.imageId;
							$('#msgphoto1').html('');
						}else if(type==2){
							that.photo2=json.imageId;
							$('#msgphoto2').html('');
						}
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		submitdata:function(){
			var that=this,p=[],url=$('#url').val(),info=$('#info').val(),head=that.photo1,logo=that.photo2,vid=$('#vid').val(), re = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;
			/*if(!head||head==0){
				d.alert({content:"请上传公司头像！",type:'error'});
			    return false;
			}
			if(!logo){
				d.alert({content:"请上传公司Logo！",type:'error'});
			    return false;
			}
			if(!url){
				d.alert({content:"请填写URL地址！",type:'error'});
			    return false;
			}*/
			if(url){
				if(!re.test(url)){
					d.alert({content:"url格式不正确！",type:'error'});
					return false;
				}
			}
			
			/*if(!info){
				d.alert({content:"请填写公司简介！",type:'error'});
			    return false;
			}*/
			if(info.length>150){
				d.alert({content:"公司简介不能大于150个字！",type:'error'});
			    return false;
			}
			p.push('head='+head);
			p.push('logo='+logo);
			p.push('url='+url);
			p.push('info='+info);
			p.push('vid='+vid);
			$.ajax({
				url     : WebAppUrl.ENTERPRISE,
				data    : p.join('&') + '&t='+new Date().getTime(),
				type: 'POST',
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
 				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.submitdata)
					}else{
						window.location.href=base+"enterprise/core/perfectOk.do";
						
					}
				},
				error   : function(r){ 
					d.alert({content:'完善公司信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
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