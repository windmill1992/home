var base=window.baseurl;
var dataUrl={
	//helpsubmit:'/project/addproject.do'
	helpEditsubmit:base+'/project/updateproject.do',
	helpphotoDel:base+'file/image/delete.do'
	
};
require.config({
	baseUrl:window.baseurl+"/res/js", 
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages",
		"ajaxform"  : "util/ajaxform",
		"area":"util/area"
	},
	urlArgs:"v=20150511" 
});

define(["extend","head","entry","dialog","ajaxform","area"],function($,h,en,d,f,a){
	//window.sitePage="p-seekHelp";  --加入善管家-捐款人 -个人中心
	h.init();
	en.init();
	d.init();
	var local=$('#location').val();	
	if(local==''){
		local="0 0 0";
	}
	var localtion=local.split(' ');
	a.init({selectsId:["province","city","county"],def:[localtion[0],localtion[1],localtion[2]]});
	$("#submitData").on("click",function(){
		window.location=dataUrl.toRst;
	});
	
	$("#applyField i").not(".last").on("click",function(){
		$(this).toggleClass("ckb");
	});
	$('.btn-gray').hover(function(){
		$(this).next('.btn-point').show();
	},function(){
		$(this).next('.btn-point').hide();
	})
	
	
	var helpinfo={
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			var pic=$('.imgList li');
			for(var i=0;i<pic.length-1;i++){
				that.photoData.push(pic.eq(i).attr('data'));
			}
			
			$('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$(".imgList"),addObj=imgBox.children("li.last"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<li class="old"><i>×</i><img src="'+src+'"></li>'); 
					$('#form1').submit();
				}
				return false;
			});
			$(".imgList li").on("mouseover",function(){
				$(this).children('i').show();
			});
			$(".imgList li").on("mouseout",function(){
				$(this).children('i').hide();
			});
			$(".imgList li i").on("click",function(){
				var delV=$(this).parent().attr('data');
				var isDel=that.delPic(delV);
				var isDel=that.delPic(delV,$(this));
			});
			$('#identity').change(function(){
				var v=$(this).children('option:selected').val();
				if(v=="其它"){
					$('#identityInfo').show();
				}else{
					$('#identityInfo').hide().val('');
				}
			})
			
			
			$('.info-field span').click(function(){
				var v=$(this).attr('tel');
				var i=$('.info-field span').index(this);
				$('.info-field span').removeClass('sel').eq(i).addClass('sel');
				$('#field').val(v);
			})
			
			$('#cryMoney').blur(function(){
				var tmptxt=$(this).val();     
       			$(this).val(tmptxt.replace(/[^\-?\d.]/g,'')); 
				
				if(tmptxt==''||isNaN(tmptxt)){
					tmptxt=1;
					$(this).val(1);
				}
			})	
		    $('#hiSubmit').click(function(){
			  that.helpEditsubmit(1);
		    })
			
			 $('#hiDraft').click(function(){
			  that.helpEditsubmit();
		    })
		  
		  
		},
		click_scroll:function(id_){
			var scroll_offset = $(id_).offset();  //得到pos这个div层的offset，包含两个值，top和left
			$("body,html").animate({
				scrollTop:scroll_offset.top  //让body的scrollTop等于pos的top，就实现了滚动
			},500);
			return false;
		},
		delPic:function(delV,obj){
			var that=this;
			var delI=jQuery.inArray(delV,that.photoDtat);
			$.ajax({
				url:dataUrl.helpphotoDel,
				data:{images:delV},
				success: function(result){ 
					if(result.flag==1){
						that.photoData.splice(delI,1);
						obj.parent().remove();
						$('.imgList .last').show();
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
					var imgBox=$('.imgList'),addObj=imgBox.children("li.last");
					addObj.show();
					if(json.result==1){
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var imgBox=$('.imgList'),addObj=imgBox.children("li.last");
						addObj.show();
						that.photoData.push(json.imageId);
						//var html='<li data="'+json.imageId+'"><i>×</i><img src="'+json.imageUrl+'" alt=""></li>';
						//$('#imgFile').parent().before(html);
						imgBox.children('.old').attr('id',json.imageId).children('img').attr('src',json.imageUrl).end();
						imgBox.children('.old').removeClass('old');
						if(that.photoData.length==10){
							$('.imgList .last').hide();
						}
						//json.imageUrl;
						
					}
				},
				error: function(data) {
					var imgBox=$('.imgList'),addObj=imgBox.children("li.last");
					addObj.show();
					imgBox.children('.old').remove();
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		helpEditsubmit:function(type){
			var that=this,id,title,status=200,content,imgIds,location,detailAddress,cryMoney,identity,identityInfo,deadline=0,field,accountName,accountBank,collectNum;
			id = $('#projectId').val();
			title=$('#title').val();
			if(title.length>18){
				$('#warn').html('<em></em>标题不能超过18个字！');return false;
			}
			content=$('#content').val();
			if(content.length>1500){
				$('#warn').html('<em></em>内容不能超过1500个字！');return false;
			}
			var location=a.getArea();
			detailAddress=$('#detailAddress').val();
			if(detailAddress.length>200){
				$('#warn').html('<em></em>详细地址不能超过200个字！');return false;
			}
			
			cryMoney=$('#cryMoney').val()?$('#cryMoney').val():0;
			accountName=$('#accountName').val();
			accountBank=$('#accountBank').val();
			collectNum=$('#collectNum').val();
			$('#deadline').val()?deadline=new Date($('#deadline').val()).getTime():0;
			if(type){
			status=210;
				if(!title){$('#warn').html('<em></em>对不起，标题尚未填写');return false;}
				if(!content){$('#warn').html('<em></em>对不起，内容尚未填写');return false;}	
				if(!location){$('#warn').html('<em></em>对不起，所在地区尚未选择');return false;}
				
				if(!detailAddress){$('#warn').html('<em></em>对不起，详情地址尚未填写');return false;}
				if(!cryMoney){$('#warn').html('<em></em>对不起，求助金额尚未填写');return false;}
				if(!deadline){$('#warn').html('<em></em>对不起，项目截止时间尚为选择');return false;}

				//if(!accountName){$('#warn').html('<em></em>对不起，开户单位尚未填写');return false;}
				//if(!accountBank){$('#warn').html('<em></em>对不起，银行名尚未填写');return false;}
			}
			imgIds=that.photoData.join(',');
			identity=$('#identity').find("option:selected").attr('tel');
			identityInfo=$('#identityInfo').val();
			if(identity=='otherCaller'&&identityInfo==''){
				$('#warn').html('<em></em>请填写您的身份！');return false;
			}	
			field=$('#field').val();
			$.ajax({
				url:dataUrl.helpEditsubmit,
				type: 'POST',
				data:{id:id,title:title,status:status,content:content,imgIds:imgIds,location:location,detailAddress:detailAddress,cryMoney:cryMoney,identity:identity,identityInfo:identityInfo,deadline:deadline,field:field,accountName:accountName,accountBank:accountBank,collectNum:collectNum},
				success: function(result){ 
					if(result.flag==1){
						//跳着到成功页面
						if(type){
							window.location.href=base+"ucenter/psuccess.do";
						}else{
							$('#warn').html('');
							window.location=base+"/ucenter/pindex.do";
							//d.alert({content:'保存草稿已成功',type:'ok'});
						}
					}else if(result.flag==-1){
						en.show(that.hiSubmitdata);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
			
			
		}
			
	}
	helpinfo.init(); 
});