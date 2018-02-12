var base=window.baseurl;
var dataUrl={
	applyInfo:'http://www.17xs.org/user/steward/apply.do',
	three:baseurl+'user/steward/three.do',
	login:baseurl+'user/sso/login.do'
};
	
require.config({
	baseUrl:window.baseurl+"/res/js", 
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"area"	: "util/area",
		"ajaxform": "util/ajaxform" 
	},
	urlArgs:"v=20150511" 
});

define(["extend","head","entry","dialog","area","ajaxform"],function($,h,en,d,a,af){
	window.sitePage="p-joinGood";
	//window.sitePage="p-seekHelp";  --加入善管家-捐款人 -个人中心
	d.init();
	h.init();
	en.init(); 
	a.init({selectsId:["province","city","county"],def:[12,5,6]});
 
 	
	var donaterInfo={
		init:function(){
			var that=this;
			$("#applyField i").not(".last").on("click",function(){
				$(this).toggleClass('ckb');
			}); 
			
			$("#submitData").on("click",function(){
				that.submitData();
			}); 
			
			$("#applyReason").blur(function(){
				if(!that.applyReason()){
					$(this).focus();
				}
			});
			$("body").on("click",".imgList img",function(){
				var parent=$(this).closest("li"); 
				parent.children("i").toggle();
			}).on("mouseover",".imgList li",function(){
				$(this).children('i').show(); 
			}).on("mouseout",".imgList li",function(){
				$(this).children('i').hide(); 
			}).on("click",".imgList i",function(){
				var parent=$(this).closest("li");
				that.removeImg(parent); 
			}).on("change","#proofImgFile",function(){
				var format=that.photoFormat(this);  
				if(format){
					var imgBox=$("#proofImgList"),addObj=imgBox.children("li.add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<li class="old"><i>×</i><img src="'+src+'"></li>'); 
					$('#proofImgForm').submit();
				}
				return false;
			}); 
			that.ajaxForm($("#proofImgForm"),6,'#proofImgList');
			
		},
		photoFormat:function(file){ 
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
		ajaxForm:function(obj,type,imgBox){
			var that=this,imgBoxs="#proofImgList"; 
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					var imgBox=$(imgBoxs),addObj=imgBox.children("li.add");
					if(json.result==1){
						addObj.show();
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{//type:4 项目图片 type:5 执行报告图片
						that.imgShow(imgBoxs,json.imageUrl,json.imageId);   
					}
				},
				error: function(data) {
					var imgBox=$(imgBoxs),addObj=imgBox.children("li.add");
					addObj.show();
					d.alert({content:data.responseText,type:'error'}); 
				}
			}); 
		}, 
		imgShow:function(imgBox,src,id){ 
			var imgBox=$(imgBox),addObj=imgBox.children("li.add");
			addObj.show();
			if(imgBox.children().size()>5){
				addObj.hide();
			}
			imgBox.children('.old').attr('id',id).children('img').attr('src',src).end();
			imgBox.children('.old').removeClass('old');
			//addObj.before('<li id="'+id+'"><i>×</i><img src="'+src+'"></li>'); 
		},
		delImgList:[],
		removeImg:function(li){
			var imgId=$(li).attr("id"),list=$(li).closest(".imgList").children();
			this.delImgList.push(imgId);
			li.remove(); 
			if(!list.filter(".add").is(":visible")){//list.length<11
				list.filter(".add").show(); 
			}
		}, 
	 	applyField:function(){
			var fields=["疾病","教育","救灾","环保","动物保护","农业农村","劳工","老人","宗教信仰","文化艺术","残障"], 
				applyFields=[];
			$("#applyField i").each(function(index, element) {
				if($(this).hasClass('ckb')){
					applyFields.push(fields[index]);
				}
			});
			
			return applyFields;
		},
	 	applyReason:function(){
			var reason=$("#applyReason").val(),
				maxWord=500,
				//count=$.chartLen(reason),
				count=reason.length;
				that=this; 
			if(count==0){
				that.msgShow("请输入您的申请理由"); 
				return false;
			}else if(count>maxWord){
				that.msgShow("申请理由最多不能超过"+maxWord+"个字");   
				return false;
			} 
			return reason; 
		},
		msgShow:function(msg){
			$("#dataMsg").show().children('span').html(msg); 
		},
		submitData:function(){ 
			var that=this,address=a.getArea(),msgObj=$("#dataMsg");
			if(!address){
				return that.msgShow("您的地址不完整"); 
			}
			var field=this.applyField().join(' ');
			if(!field.length){ 
				return that.msgShow("请选择您感兴趣的领域"); 
			} 
			var reason=this.applyReason();
			if(!reason){return;} 
			$("#dataMsg").hide();
			
			var imgList=$("#proofImgList").children().not(".add"),
				img=[],
				len=imgList.length;  
			for(var i=0;i<len;i++){
				img.push($(imgList[i]).attr("id"));
			} 
			$.ajax({
				url:dataUrl.applyInfo,
				data:{field:field,address:address,reason:reason,imageId:img.join(',')},
				success: function(result){
					if(!!result){
						if(result.flag==1){			
							window.location.href=dataUrl.three;
						}else if(result.flag==0){
							d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							en.show(that.submitData);
							//window.location.href=dataUrl.login;
						} 
						return;
					}
				} 
			}); 
		} 
	}; 
	 
	donaterInfo.init();
});