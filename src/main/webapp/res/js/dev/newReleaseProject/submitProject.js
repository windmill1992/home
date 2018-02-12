
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
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var reg={
		photoData:[],
		init:function(){
			
			var that=this;
			that.ajaxForm($('#form1'));
			
		    $('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');
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
					/*$(this).val(tmptxt.replace(/\D|^0/g,'')); 
					tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
					if(tmptxt==''||isNaN(tmptxt)){
						tmptxt=1;
						$(this).val(1);
					}*/
				});
		
			//当点击确认按钮是
			$('#submitProject').click(function(){
				//触发的行为
				that.releaseProject();

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
					var imgBox=$('#imgList'),addObj=imgBox.children(".add");
					//addObj.show();
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
		 releaseProject:function(){
			    var that=this 
			    var type=$('#institutionType').val();
			    var zlId=$('#zlId').val();
			    var field=$('#typeName_e').val();
	            var helpType=$('#helpType').val();
	            var projectId=$("#projectId").val();
	            var donateTime=$('#donateTime').val();
	            if(donateTime==""){//默认30天
	            	donateTime=30;
	            }
	            if(donateTime>60){
	            	d.alert({content:"项目募捐时间不能超过60天",type:'error'});
					return false;
	            }
	            if(donateTime<0){
	            	d.alert({content:"请填写正确的项目募捐时间",type:'error'});
					return false;
	            }
			    var title=$('#title').val();
	            var status=210;
	            
	            var cryMoney=$('#cryMoney').val();
	            //var content=$('#content').val();
	            
	            var identity="";
	            if(that.photoData.length>6){
					d.alert({content:"上传的图片不能超过6张",type:'error'});
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
								//去项目id
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