/**
 * Created by Administrator on 2016/8/10.
 */
/**
 * Created by Administrator on 2016/8/10.
 */
var base=window.baseurl;
var dataUrl={
	SSO_MobileCode:base+'goodlibrary/phoneCode.do',//验证码请求
	helpphotoDel:base+"file/image/delete.do",//删除图片
	addAidedPerson:base+"newReleaseProject/addAidedPerson.do",//个人提交
	typeConfigList:base+"newReleaseProject/typeConfigList.do"//
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
	//a.init({selectsId:["province","city","county"],def:[0,0,0]});
	//a2.init({selectsId:["appealProvince","appealCity","appealCounty"],def:[0,0,0]});
	a.init({selectsId:["province","city","county"],def:[0,0,0]});
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			that.ajaxForm($('#form2'));
			that.filedListData(1);
			
			//标签
			$("#fieldList>a").click(function(){
				$(this).addClass("curTab").siblings().removeClass("curTab");
			});
			$("#tagList>a").click(function(){
				$(this).addClass("curTab").siblings().removeClass("curTab");
			});
			//为家庭发起求助
			$("#family2").click(function(){
				$("#typeState").val("2");
				a.init({selectsId:["appealProvince","appealCity","appealCounty"],def:[0,0,0]});
				$(".jr").show();
				$(".br").hide();
				$(".qy").hide();
				that.filedListData(2);
			});
			//为其他亲朋好友发起求助
			$("#kin3").click(function(){
				$("#typeState").val("3");
				a.init({selectsId:["otherProvince","otherCity","otherCounty"],def:[0,0,0]});
				$(".jr").hide();
				$(".br").hide();
				$(".qy").show();
				that.filedListData(3);
			});
			//为本人发起求助
			$("#me1").click(function(){
				$("#typeState").val("1");
				a.init({selectsId:["province","city","county"],def:[0,0,0]});
				$(".jr").hide();
				$(".br").show();
				$(".qy").hide();
				that.filedListData(1);
			});
			$("body").on("click","#needhelpCon dd b.select",function(){
				$(this).removeClass('select').addClass('ok').html('');
			}).on("mouseover","#imgList .item",function(){ 
				$(this).children('i').show();
			}).on("mouseout","#imgList .item",function(){ 
				$(this).children('i').hide();
			}).on("click","#imgList1 .item i",function(){ 
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
			}).on("blur","#phone",function(){
				var tmptxt=$(this).val();
			}).on("click","#imgList2 .item i",function(){ 
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
			});
			//input验证
			//1.为个人
			$('#idCard').focus(function(){
				var name=$('#name').val();
				if(name==''){
					$("#name").attr("value","请输入受助人姓名！");
					$("#name").addClass("personInpg");
					setTimeout(function () {
						$("#name").attr("value","")
						$("#name").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#idCard').change(function(){
				that.isVaildId($(this));
			});
			$('#mobile').focus(function(){
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

			$('#detailAddress').focus(function(){
				var mobile=$('#mobile').val();
				if(mobile==''){
					$("#mobile").attr("value","请输入手机号！");
					$("#mobile").addClass("personInpg");
					setTimeout(function () {
						$("#mobile").attr("value","")
						$("#mobile").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			//为家人
			$('#appealName').focus(function(){
				var appealRelation=$('#appealRelation').val();
				if(appealRelation==''){
					$("#appealRelation").attr("value","请输入您与受助人的关系！");
					$("#appealRelation").addClass("personInpg");
					setTimeout(function () {
						$("#appealRelation").attr("value","")
						$("#appealRelation").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#appealIdcard').focus(function(){
				var appealName=$('#appealName').val();
				if(appealName==''){
					$("#appealName").attr("value","请输入受助人姓名！");
					$("#appealName").addClass("personInpg");
					setTimeout(function () {
						$("#appealName").attr("value","")
						$("#appealName").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#appealIdcard').change(function(){
				that.isVaildId($(this));
			});
			$('#appealMobile').focus(function(){
				var appealIdcard=$('#appealIdcard').val();
				if(appealIdcard==''){
					$("#appealIdcard").attr("value","请输入身份证号！");
					$("#appealIdcard").addClass("personInpg");
					setTimeout(function () {
						$("#appealIdcard").attr("value","")
						$("#appealIdcard").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#appealDetailAddress').focus(function(){
				var appealMobile=$('#appealMobile').val();
				if(appealMobile==''){
					$("#appealMobile").attr("value","请输入手机号！");
					$("#appealMobile").addClass("personInpg");
					setTimeout(function () {
						$("#appealMobile").attr("value","")
						$("#appealMobile").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			//为其他亲朋好友
			$('#otherName').focus(function(){
				var otherRelation=$('#otherRelation').val();
				if(otherRelation==''){
					$("#otherRelation").attr("value","请输入您与受助人的关系！");
					$("#otherRelation").addClass("personInpg");
					setTimeout(function () {
						$("#otherRelation").attr("value","")
						$("#otherRelation").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#otherIdCard').focus(function(){
				var otherName=$('#otherName').val();
				if(otherName==''){
					$("#otherName").attr("value","请输入受助人姓名！");
					$("#otherName").addClass("personInpg");
					setTimeout(function () {
						$("#otherName").attr("value","")
						$("#otherName").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#otherIdCard').change(function(){
				that.isVaildId($(this));
			});
			$('#otherMobile').focus(function(){
				var otherIdCard=$('#otherIdCard').val();
				if(otherIdCard==''){
					$("#otherIdCard").attr("value","请输入身份证号！");
					$("#otherIdCard").addClass("personInpg");
					setTimeout(function () {
						$("#otherIdCard").attr("value","")
						$("#otherIdCard").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('#otherDetailAddress').focus(function(){
				var otherMobile=$('#otherMobile').val();
				if(otherMobile==''){
					$("#otherMobile").attr("value","请输入手机号！");
					$("#otherMobile").addClass("personInpg");
					setTimeout(function () {
						$("#otherMobile").attr("value","")
						$("#otherMobile").removeClass("personInpg");
					}, 2000);
					return false;
				}
			});
			$('.footBtn').click(function(){
				var typeState=$('#typeState').val();
				that.submitInfo(typeState);
				
			})
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
			//下一步按钮
		    $("#footButtom").click(function(){
		    	/*var imgsid1 = $('.io1').attr('id');
				var imgsid2 = $('.io2').attr('id');
				var type = $('#typestate').val();
				if(typeof(imgsid1)=="undefined"){
					imgsid1="";
				}
				if(typeof(imgsid2)=="undefined"){
					imgsid2="";
				}
				if(type==2){
					that.verify2(imgsid1,,0,0,type);
				}
				else{
					that.verify2(0,0,0,imgsid4,imgsid5,type);
				}*/
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
					if(obj.selector=="#form1"){
						data = data.replace("<PRE>", "").replace("</PRE>", "");
						var json = eval('(' + data + ')'); 
						var imgBox=$('#imgList1'),addObj=imgBox.children(".add");
						addObj.show();
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
							if(that.photoData.length>6){
								d.alert({content:"上传的图片不能超过六张",type:'error'});
							}
						}
					}
					else if(obj.selector=="#form2"){
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
							if(that.photoData.length>6){
								d.alert({content:"上传的图片不能超过六张",type:'error'});
							}
						}
					
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
	submitInfo: function(typeState){
		var field=$('#field').val(),fieldChinese=$('#fieldChinese').val();
		if(field==''||fieldChinese==''){
			return d.alert({content:"请选择项目领域！",type:'error'});
		}
		if(typeState==1){//为本人
			var name=$('#name').val(),idCard=$('#idCard').val(),mobile=$('#mobile').val(),
			province=$('#province').val(),city=$('#city').val(),county=$('#county').val(),
			detailAddress=$('#detailAddress').val();
			if(name==''){
				$("#name").attr("value","请输入受助人姓名！");
				$("#name").addClass("personInpg");
				setTimeout(function () {
					$("#name").attr("value","")
					$("#name").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(idCard==''){
				$("#idCard").attr("value","请输入身份证号！");
				$("#idCard").addClass("personInpg");
				setTimeout(function () {
					$("#idCard").attr("value","")
					$("#idCard").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(mobile==''){
				$("#mobile").attr("value","请输入手机号！");
				$("#mobile").addClass("personInpg");
				setTimeout(function () {
					$("#mobile").attr("value","")
					$("#mobile").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(province==''||city==''||county==''||province=='省份'||city=='地级市'||county=='区/县级市'){
				return d.alert({content:"请输入所在地！",type:'error'}); 
			}
			if(detailAddress==''){
				$("#detailAddress").attr("value","请输入详细地址！");
				$("#detailAddress").addClass("personInpg");
				setTimeout(function () {
					$("#detailAddress").attr("value","")
					$("#detailAddress").removeClass("personInpg");
				}, 2000);
				return false;
			}
			//if(idCard!=''){
				//that.isVaildId($(this));
			//}
			var address=province+city+county+detailAddress
			//添加数据
			$.ajax({
				url:dataUrl.addAidedPerson,
				cache: false,
				type: "POST",
				data:{field:field,fieldChinese:fieldChinese,type:typeState,appealName:name,appealIdcard:idCard,appealPhone:mobile,appealAddress:address},
				success: function(result){
					if(result.flag==1){
						data=result.obj;
						window.location='http://www.17xs.org/newReleaseProject/personReleaseList.do?type=0&helpType=3&typeName_e='+field+'&projectId='+data+'&zlId='+presonReleaseId;
					}
					else if(result.flag==-1){//未登录
						window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/newReleaseProject/gotoAidedPerson.do';
					}
					else if(result.flag==0){
						return d.alert({content:result.errorMsg,type:'error'});
					}
				},
				error : function(result) { 
					return d.alert({content:"请求异常",type:'error'});
				}
				
			});
		}
		else if(typeState==2){//为家人
			var that=this; 
			var imgIds=that.photoData.join(','); 
			var appealRelation=$('#appealRelation').val(),appealName=$('#appealName').val(),appealIdcard=$('#appealIdcard').val(),
			appealMobile=$('#appealMobile').val(),appealProvince=$('#appealProvince').val(),appealCity=$('#appealCity').val(),
			appealCounty=$('#appealCounty').val(),appealDetailAddress=$('#appealDetailAddress').val();
			if(appealRelation==''){
				$("#appealRelation").attr("value","请输入您与受助人的关系！");
				$("#appealRelation").addClass("personInpg");
				setTimeout(function () {
					$("#appealRelation").attr("value","")
					$("#appealRelation").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(appealName==''){
				$("#appealName").attr("value","请输入受助人姓名！");
				$("#appealName").addClass("personInpg");
				setTimeout(function () {
					$("#appealName").attr("value","")
					$("#appealName").removeClass("personInpg");
				}, 2000);
				return false;
			}

			if(appealIdcard==''){
				$("#appealIdcard").attr("value","请输入身份证号！");
				$("#appealIdcard").addClass("personInpg");
				setTimeout(function () {
					$("#appealIdcard").attr("value","")
					$("#appealIdcard").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(appealMobile==''){
				$("#appealMobile").attr("value","请输入手机号！");
				$("#appealMobile").addClass("personInpg");
				setTimeout(function () {
					$("#appealMobile").attr("value","")
					$("#appealMobile").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(appealProvince==''||appealCity==''||appealCounty==''){
				return d.alert({content:"请输入所在地！",type:'error'}); 
			}
			if(appealDetailAddress==''){
				$("#appealDetailAddress").attr("value","请输入详细地址！");
				$("#appealDetailAddress").addClass("personInpg");
				setTimeout(function () {
					$("#appealDetailAddress").attr("value","")
					$("#appealDetailAddress").removeClass("personInpg");
				}, 2000);
				return false;
			}
			//that.isVaildId($(this));
			//添加数据
			var address=appealProvince+appealCity+appealCounty+appealDetailAddress;
			$.ajax({
				url:dataUrl.addAidedPerson,
				cache: false,
				type: "POST",
				data:{field:field,fieldChinese:fieldChinese,type:typeState,
					relation:appealRelation,appealName:appealName,appealIdcard:appealIdcard,
					appealPhone:appealMobile,imgIds:imgIds,appealAddress:address},
				success: function(result){
					if(result.flag==1){
						data=result.obj;
						window.location='http://www.17xs.org/newReleaseProject/personReleaseList.do?type=0&helpType=3&typeName_e='+field+'&projectId='+data+'&zlId='+presonReleaseId;
					}
					else if(result.flag==-1){//未登录
						window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/newReleaseProject/gotoAidedPerson.do';
					}
					else if(result.flag==0){
						return d.alert({content:result.errorMsg,type:'error'});
					}
				},
				error : function(result) { 
					return d.alert({content:"请求异常",type:'error'});
				}
				
			});
		}
		else if(typeState==3){//为其他亲朋好友
			var otherRelation=$('#otherRelation').val(),otherName=$('#otherName').val(),otherIdCard=$('#otherIdCard').val(),
			otherMobile=$('#otherMobile').val(),otherProvince=$('#otherProvince').val(),otherCity=$('#otherCity').val(),
			otherCounty=$('#otherCounty').val(),otherDetailAddress=$('#otherDetailAddress').val();
			if(otherRelation==''){
				$("#otherRelation").attr("value","请输入您与受助人的关系！");
				$("#otherRelation").addClass("personInpg");
				setTimeout(function () {
					$("#otherRelation").attr("value","")
					$("#otherRelation").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(otherName==''){
				$("#otherName").attr("value","请输入受助人姓名！");
				$("#otherName").addClass("personInpg");
				setTimeout(function () {
					$("#otherName").attr("value","")
					$("#otherName").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(otherIdCard==''){
				$("#otherIdCard").attr("value","请输入身份证号！");
				$("#otherIdCard").addClass("personInpg");
				setTimeout(function () {
					$("#otherIdCard").attr("value","")
					$("#otherIdCard").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(otherMobile==''){
				$("#otherMobile").attr("value","请输入手机号！");
				$("#otherMobile").addClass("personInpg");
				setTimeout(function () {
					$("#otherMobile").attr("value","")
					$("#otherMobile").removeClass("personInpg");
				}, 2000);
				return false;
			}
			if(otherCity==''||otherCounty==''||otherProvince==''){
				return d.alert({content:"请输入所在地！",type:'error'}); 
			}
			if(otherDetailAddress==''){
				$("#otherDetailAddress").attr("value","请输入详细地址！");
				$("#otherDetailAddress").addClass("personInpg");
				setTimeout(function () {
					$("#otherDetailAddress").attr("value","")
					$("#otherDetailAddress").removeClass("personInpg");
				}, 2000);
				return false;
			}
			//that.isVaildId($(this));
			//添加数据
			var address=otherProvince+otherCity+otherCounty+otherDetailAddress,presonReleaseId=$('#presonReleaseId').val();
			$.ajax({
				url:dataUrl.addAidedPerson,
				cache: false,
				type: "POST",
				data:{field:field,fieldChinese:fieldChinese,type:typeState,
					relation:otherRelation,appealName:otherName,appealIdcard:otherIdCard,
					appealPhone:otherMobile,imgIds:imgIds,appealAddress:address},
				success: function(result){
					if(result.flag==1){
						data=result.obj;
						window.location='http://www.17xs.org/newReleaseProject/personReleaseList.do?type=0&helpType=3&typeName_e='+field+'&projectId='+data+'&zlId='+presonReleaseId;
					}
					else if(result.flag==-1){//未登录
						window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/newReleaseProject/gotoAidedPerson.do';
					}
					else if(result.flag==0){
						return d.alert({content:result.errorMsg,type:'error'});
					}
				},
				error : function(result) { 
					return d.alert({content:"请求异常",type:'error'});
				}
				
			});
		}
		else{
			alert('请求异常！');
		}
	},
	//查询求助类目
	filedListData: function(type){
		var html=[];
		$.ajax({
			url:dataUrl.typeConfigList,
			data:{helpType:type},
			success: function(result){
				var obj = result.obj.items;
				for(var i=0;i<obj.length;i++){
					if(i==0){
						html.push('<a class="curTab" onclick="selectTypeList(\''+obj[i].typeName_e+'\',\''+obj[i].typeName+'\','+i+')" v="'+obj[i].typeName_e+'" href="javascript:void(0);" id="typeName'+i+'">'+obj[i].typeName+'</a><i></i>');
					}else{
						html.push('<a onclick="selectTypeList(\''+obj[i].typeName_e+'\',\''+obj[i].typeName+'\','+i+')" v="'+obj[i].typeName_e+'" href="javascript:void(0);" id="typeName'+i+'">'+obj[i].typeName+'</a><i></i>');
					}
				}
				$('#fieldList').html(html.join(''));
			}
		});
	},
	//---------------------------------------------------------------
	isValidName: function(nameObj) {//nameId, promptId
    	var that=this,nameVal = nameObj,ret;
         
		var reg=/^[1-9]\d*$|^0$/,c=that.checkMobileStr(nameVal);  
        if(!c){ 
		    return d.alert({content:"请输入正确的手机号码！",type:'error'});
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
