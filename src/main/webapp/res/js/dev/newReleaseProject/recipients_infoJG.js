
var base=window.baseurl;
var dataUrl={
	typeConfigList:base+'newReleaseProject/typeConfigList.do',//验证码请求
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
		"pageCommon":"dev/common/pageCommon",
		"area"	: "util/area"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon","area"],function($,d,h,en,uc,f,p,p1,a){
	window.sitePage="p-seekHelp";//选中导航
	h.init();
	d.init();
	en.init(); 
	uc.init();
	a.init({selectsId:["province","city","county"],def:[0,0,0]});
	var reg={
		type:0,
		photoData:[],
		init:function(){
			//初始加载显示机构为个人
			$(".wgr").show();
		    $(".gyxm").hide();
		    
			var that=this;
			that.ajaxForm1($('#form1'));
	    	that.ajaxForm($('#form2'));
			that.helpFields(1,4)    //初始加载为机构与个人
			var helpType=$("#helpType").val();
			
			
			$("body").on("click","#needhelpCon dd b.select",function(){
						$(this).removeClass('select').addClass('ok').html('');
				}).on("mouseover","#imgList1 .item",function(){ 
					$(this).children('i').show();
				}).on("mouseout","#imgList1 .item",function(){ 
					$(this).children('i').hide();
				}).on("click","#imgList1 .item i",function(){ 
					var delV=$(this).parent().attr('id');
					var isDel=that.delPic(delV,$(this));
				}).on("mouseover","#imgList2 .item",function(){ 
					$(this).children('i').show();
				}).on("mouseout","#imgList2 .item",function(){ 
					$(this).children('i').hide();
				}).on("click","#imgList2 .item i",function(){ 
					var delV=$(this).parent().attr('id');
					var isDel=that.delPic(delV,$(this));
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
			//当点击为个人发布项目
			$('#institutionToOne').click(function(){
				//触发的行为
				$("#helpType").val(4);
				$("#typeName_e").val("");
				$(".wgr").show();
			    $(".gyxm").hide();
			    that.helpFields(1,4)
			    $(".confirm").html("确认，下一步");
			    $("#institutionToOne").addClass("curTab").siblings().removeClass("curTab");
			});
			//当点击未群体发布项目
			$('#institutionToPublic').click(function(){
				//触发的行为
				$("#institutionToPublic").addClass("curTab").siblings().removeClass("curTab");
				$("#helpType").val(5);
				$("#typeName_e").val("");
				$(".gyxm").show();
				$(".wgr").hide();
				that.helpFields(1,5)
				$(".confirm").html("确认发布");
			});
			//当点击确认按钮是
			$('.confirm').click(function(){
				//触发的行为
				var helpType=$("#helpType").val();
				if(helpType==4){    //为4是是下一步
					that.submitAppealUserInfo();
				}
				if(helpType==5){     //为5时则确认发布项目
					//调用发布项目
					that.releaseProject();
				}
			});
		},
		
		//获取求助领域 type判断为个人还是机构 helpType:机构为个人为4，机构为群体为5
		helpFields:function(type,helpType){
			var that=this;
			$.ajax({
				url:dataUrl.typeConfigList,
				data:{
					helpType:helpType
				},
				success: function(result){ 
					if(result.flag==1){
						datas=result.obj;
						data=datas.items;
						html=[];
						for(var i=1;i<data.length;i++){
							if (i==0){
								html.push('<a class="curTab" v="deport"  class="curTab'+i+'" onclick="onclickFields(\''+data[i].typeName_e+'\','+i+')">'+data[i].typeName+'<i ></i>');
								html.push('</a>')
							}else {
								html.push('<a v="deport"  class="curTab'+i+'" onclick="onclickFields(\''+data[i].typeName_e+'\','+i+')">'+data[i].typeName+'<i ></i>');
								html.push('</a>')
							}

						}
						$("#fieldList").html(html.join(""));
					}else{
						d.alert({content:"出现异常请联系管理员",type:'error'});
						return false;
					}
				}
			}); 
		},
	    submitAppealUserInfo:function(){
	        	
	    	var that=this;    
	    	var imgIds=that.photoData.join(','); 
	    	if(that.photoData.length>6){
	    		d.alert({content:"上传的图片不能超过六张！",type:'error'});
	    		return false;
			}

	    	var appealName=$("#appealName").val();
	    	var appealIdcard=$("#appealIdcard").val();
	    	var appealPhone=$("#appealPhone").val();
	    	//获取省市区
	    	var loc_province=$("#province").val();
	    	var loc_city=$("#city").val();
	    	var loc_county=$("#county").val();
	    	var appealAddress=loc_province+loc_city+loc_county;
	    	//var appealAddress=$("#appealAddress").val();
	    	var detailAddress=$("#detailAddress").val();
	    	var typeName_e=$("#typeName_e").val();
	    	var zlId=$("#zlId").val();
	    	var type=$('#institutionType').val();
	    	var helpType=$("#helpType").val();
	    	if(!typeName_e){
	    		d.alert({content:"请选择受助的领域！",type:'error'});
				return false;
			};
	    	if(!appealName){
	    		$("#appealName").attr("value","请输如受助人姓名！");
				$("#appealName").addClass("personInpg");
				setTimeout(function () {
					$("#appealName").attr("value","")
					$("#appealName").removeClass("personInpg");
				}, 2000);
				return false;
			};
	    	if(!appealIdcard){
	    		$("#appealIdcard").attr("value","身份证！");
				$("#appealIdcard").addClass("personInpg");
				setTimeout(function () {
					$("#appealIdcard").attr("value","")
					$("#appealIdcard").removeClass("personInpg");
				}, 2000);
				return false;
			};
	    	if(!detailAddress){
	    		$("#detailAddress").attr("value","详细地址不能为空");
				$("#detailAddress").addClass("personInpg");
				setTimeout(function () {
					$("#detailAddress").attr("value","")
					$("#detailAddress").removeClass("personInpg");
				}, 2000);
				return false;
			};
			
	    	var isAppealIdcard=en.isVaildId($("#appealIdcard"));
			if(!isAppealIdcard){
				$("#isAppealIdcard").attr("value","详细地址不能为空");
				$("#isAppealIdcard").addClass("personInpg");
				setTimeout(function () {
					$("#isAppealIdcard").attr("value","")
					$("#isAppealIdcard").removeClass("personInpg");
				}, 2000);
				return false;
			}
	    	
	    	if(!appealPhone)
	    	{
	    		$("#appealPhone").attr("value","受助人电话不能为空");
				$("#appealPhone").addClass("personInpg");
				setTimeout(function () {
					$("#appealPhone").attr("value","")
					$("#appealPhone").removeClass("personInpg");
				}, 2000);
				return false;
	    	}else{
	    		if(!that.phone(appealPhone)){
	    			$("#appealPhone").attr("value","受助人电话不能为空");
					$("#appealPhone").addClass("personInpg");
					setTimeout(function () {
						$("#appealPhone").attr("value","")
						$("#appealPhone").removeClass("personInpg");
					}, 2000);
					return false;
	    		}
	    	}
	    	if(!appealAddress){
	    		$("#appealAddress").attr("value","受助人电话不能为空");
				$("#appealAddress").addClass("personInpg");
				setTimeout(function () {
					$("#appealAddress").attr("value","")
					$("#appealAddress").removeClass("personInpg");
				}, 2000);
				return false;
	    	}
	    	if(imgIds==null||imgIds==''){
	    		d.alert({content:"上传相关图片不能为空",type:'error'});
	    		return false;};
	    	
	    	$.ajax({ 
				url:window.baseurl+"test/appealUserInfoData.do?zlId="+zlId+"&type="+type+"&helpType="+helpType,
				type: 'POST',
				data:{appealName:appealName,appealIdcard:appealIdcard,appealPhone:appealPhone,appealAddress:appealAddress,detailAddress:detailAddress,imgIds:imgIds,t:new Date().getTime()},
				chase:false,
				success: function(result){
					if(result.flag==1){
						    var projectId=result.obj;
							window.location.href=window.baseurl+"newReleaseProject/releaseProjectDetails.do?projectId="+projectId+"&zlId="+zlId+"&type="+type+"&typeName_e="+typeName_e;
							return true
						 //成功后跳转下一步
						 
					}else if(result.flag==0){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag==-1){
						d.alert({content:"未登录,请先登录返回",type:'error'});
						return false;

					}
				},
				error: function(){
					return d.alert({content:"出现异常",type:'error'});
				}
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
						$('#imgList1 .add').show();
						$('#imgList2 .add').show();
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
		phone:function(str){
			 var cellphone = /^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
			if(cellphone.test(str)){
				return true;
			}else{
				return false;
			}
		},
		photoPic:function(file){
			    var that=this, filepath =$(file).val();				
			    var extStart=filepath.lastIndexOf(".")+1;		
				var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
				if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP')
				{	$("#msg").html('<p>上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！</p>');
					$("#msg").show();
					setTimeout(function () {
						$("#msg").hide();
					}, 2000);
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
					var imgBox=$('#imgList2'),addObj=imgBox.children(".add");
					addObj.show();
					if(json.result==1){
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var imgBox=$('#imgList2'),addObj=imgBox.children(".add");
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
		ajaxForm1:function(obj){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					var imgBox=$('#imgList1'),addObj=imgBox.children(".add");
					//addObj.show();
					if(json.result==1){
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var imgBox=$('#imgList1'),addObj=imgBox.children(".add");
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
		 releaseProject:function(){
			    var that=this 
			    var type=$('#institutionType').val();
			    var zlId=$('#zlId').val();
			    var field=$('#typeName_e').val();
	            var helpType=$('#helpType').val();
	            var projectId=$("#projectId").val();
	            var donateTime=$('#donateTime').val();
	            if(donateTime==""){
	            	donateTime=30;
	            }
			    var title=$('#title').val();
	            var status=210;
	            
	            var cryMoney=$('#cryMoney').val();
	            var content=$('#content').val();
	            
	            var identity="";
	            if(that.photoData.length>6){
					d.alert({content:"上传的图片不能超过六张",type:'error'});
					return false;
				}
	            var imgIds=that.photoData.join(',');
	            var conData=[];
	            var imgsDetail='';
				if(!title){
					
					$("#title").attr("value","标题不能为空");
					$("#title").addClass("personInpg");
					setTimeout(function () {
						$("#title").attr("value","")
						$("#title").removeClass("personInpg");
					}, 2000);
					return false;};
				if(title.length>20){
					$("#title").attr("value","标题不能为空");
					$("#title").addClass("personInpg");
					setTimeout(function () {
						$("#title").attr("value","")
						$("#title").removeClass("personInpg");
					}, 2000);
					return false;}
				if(!cryMoney){
					$("#cryMoney").attr("value","求助的金额不能为空");
					$("#cryMoney").addClass("personInpg");
					setTimeout(function () {
						$("#cryMoney").attr("value","")
						$("#cryMoney").removeClass("personInpg");
					}, 2000);
					return false;};
				if(cryMoney<200){
					$("#cryMoney").attr("value","求助金额必须大于200元");
					$("#cryMoney").addClass("personInpg");
					setTimeout(function () {
						$("#cryMoney").attr("value","")
						$("#cryMoney").removeClass("personInpg");
					}, 2000);
					return false;};
					var content = UE.getEditor('content').getContent();
				if(!content){
					$("#content").attr("value","求助内容不能为空");
					$("#content").addClass("personInpg");
					setTimeout(function () {
						$("#content").attr("value","")
						$("#content").removeClass("personInpg");
					}, 2000);
					return false;};
				if(!imgIds){
					d.alert({content:"上传图片不能为空",type:'error'})
					return false;};
				if(projectId == ''){projectId=-1;}
				var url="";
				if(helpType=="5"){
					url=window.baseurl+"test/InstitutionReleaseProject.do?helpType="+helpType+"&zlId="+zlId+"&type="+type;
				}else{
					url=window.baseurl+"test/appealProjectInfoData.do?helpType="+helpType+"&zlId="+zlId+"&type="+type;
				}
				$.ajax({ 
						url:url,
						type: 'POST',
						data:{id:projectId,deadline:donateTime,title:title,cryMoney:cryMoney,status:status,field:field,identity:identity,content:content,imgIds:imgIds,t:new Date().getTime()},
						chase:false,
						success: function(result){
							if(result.flag==1){
								projectid=result.obj.projectId;
								$('#typeName_e').val("");      //发布成功之后将领域置为空
								window.location.href=window.baseurl+"newReleaseProject/success.do?projectId="+projectid;
							}else if(result.flag==0){
								d.alert({content:result.errorMsg,type:'error'});
								return false;
							}
						},
						error: function(){
							d.alert({content:"网络异常,请联系客服",type:'error'});
						}
				});
				
	      
	    },
	}
	reg.init()
})











































