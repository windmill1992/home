var base=window.baseurl;
//var base="http://www.17xs.org:8080/"
var dataUrl={
	appealFirestData:base+"project/appealFirestData.do",//不同项目类型的条件
	appealByCustom:base+"project/appealByCustom.do",//客服代发
	addproject:base+"project/appealProjectInfoData.do",
	appealUserInfoData:base+"project/appealUserInfoData.do",
	appealSecond:base+"project/appealSecond.do",
	appealFour:base+"project/appealFour.do",
	appealFive:base+"project/appealFive.do",
	projectview:base+"project/view.do",
	helpphotoDel:base+"file/image/delete.do"
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
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var needhelp={
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form1'));
			that.ajaxForm($('#form3'));
			that.ajaxForm($('#form4'));
			that.ajaxForm($('#form5'));
			that.ajaxForm($('#form6'));
			that.ajaxForm($('#form7'));
			that.ajaxForm($('#form8'));
			that.ajaxForm($('#form9'));
			that.ajaxForm($('#form10'));
			if($('#projectId').length>0){
				//编辑
				var len=$('#imgList .item').not('.add').length;
				if(len){
					for(var i=0;i<len;i++){
						var imageId=$('#imgList .item').eq(i).attr('id');
						that.photoData.push(imageId);
					}
				}
				if(len==1){
					$('#imgList .add').hide();
				}
			}
//			that.appealFirestData(0,0);默认选择
			$("body").on("change",".file-input",function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$(this).parents(".add_list"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="80px"></div>');
					imgBox.parent().submit();
					var tempObj = $(this).parent();
					var id = $(this).attr('id');
					$(this).remove();
					tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
//					$(this).val('');
				}
				return false;
			})
			
			$('#needhelptype a').click(function(){
				var i=$('#needhelptype a').index(this),i2=$('#ReleaseType a').index($('#ReleaseType a.cur'));
				$('#needhelptype a').removeClass('cur').eq(i).addClass('cur');
				that.appealFirestData(i,i2);
			});
			$('#ReleaseType a').click(function(){
				var i=$('#ReleaseType a').index(this),i2=$('#needhelptype a').index($('#needhelptype a.cur'));
				$('#ReleaseType a').removeClass('cur').eq(i).addClass('cur');
				that.appealFirestData(i2,i);
			});
			
			$('#needhelpNext').click(function(){
				that.needhelpNext();
			});
			
			$("body").on("click","#needhelpCon dd b.select",function(){
					$(this).removeClass('select').addClass('ok').html('');
			}).on("mouseenter",".add_list .item",function(){ 
				$(this).children('i').show();
			}).on("mouseleave",".add_list .item",function(){ 
				$(this).children('i').hide();
			}).on("click",".add_list .item i",function(){ 
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
			}).on("blur","#phone",function(){
				var tmptxt=$(this).val();
			});
			
			/*2步*/
			$('#needHelp2Submit').click(function(){
				that.needHelp2Submit();
			});
			
			/*3 保存*/
			$('#needHelp3Submit').click(function(){
				that.needHelp3Submit(0,1);
			});
			/*4 上一步*/
			$('#needHelp3Back').click(function(){
				that.needHelp3Submit(0,0);
			});
			//发布
			$('#needHelp3Issue').click(function(){
				that.needHelp3Submit(1,2);
			});
			
			/*我为自己发布*/
			var help=$('#help').val();
			if(help==1){
				$('#appealName').keyup(function(){
					var v=$(this).val();
					$('#name').val(v);
				});
				$('#appealIdcard').keyup(function(){
					var v=$(this).val();
					$('#Idcard').val(v);
				});
			}
		},	
		appealFirestData:function(type,type2){
			var that=this,html=[],typeName=$('#needhelptype a').eq(type).attr("data"),
			name=$('#needhelptype a').eq(type).html();
			$.ajax({ 
					url:dataUrl.appealFirestData,
					type: 'POST',
					data:{type:typeName,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
							var data=result.obj;
							html.push('<dl>');
							html.push('<dt>为了您的求助能尽快发布，请在备齐以下材料之后进行发布('+name+')</dt>');
							for(var i=0;i<data.length;i++){
								html.push('<dd><i>'+(i+1)+'.'+data[i].configValue+'</i><b class="select">我已备齐</b></dd>');
							}
							if(type2==2){
								html.push('<div class="kefu">');
								html.push('<div class="kefu_hd">请留下您的联系方式，客服会在2小时内联系您</div>');
								html.push('<div class="kefu_bd">');
								html.push('<ul>');
								html.push('<li>姓名：<input name="" id="name" type="text"></li>');
								html.push('<li>电话：<input name="" id="phone" type="text"></li>');
								html.push('<li>QQ/微信：<input name="" id="qq" type="text"></li></ul>');
								html.push('<div class="liuyan"><span>说明：</span><textarea id="leaveWord" name="txt" warp="virtual"></textarea> </div>')
								html.push('</div></div>');
							}
							html.push('</dl>');
							$('#needhelpCon').html(html.join(''));
							type2==1?$('#needhelpOther').show():$('#needhelpOther').hide();
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.delproject);
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});
		},
		needhelpNext:function(){
			var that=this,IsLogin=$.cookie.get("sjj_username");			
			var type=$('#needhelptype a').index($('#needhelptype a.cur')),type2=$('#ReleaseType a').index($('#ReleaseType a.cur')),datalen=$('#needhelpCon dd').length,
				oklen=$('#needhelpCon .ok').length,name=$('#name').val(),qq=$('#qq').val(),phone=$('#phone').val(),leaveWord=$("#leaveWord").val();
			var typeName=$('#needhelptype a').eq(type).attr("data") , userId=$("#userId").val();
			if(userId==null || userId==''){
				$('.loginBtn').click();
				return false;
			}
			if(type==-1){
				return d.alert({content:'请选择求助类型！',type:'error'});
			}
			if(type2==-1){
				return d.alert({content:'请选择发布人类型！',type:'error'});
			}
			if(type2==0||type2==1){
				if(!IsLogin||IsLogin==null){
					en.show(that.needhelpNext);
					return false;
				}else{
					/*if(oklen<datalen){
						return d.alert({content:'您提交的资料还没备齐，请备齐了再提交！',type:'error'});
					}*/
				}
				//下一步页面链接
				window.location.href=base+"project/appealSecond.do?type="+typeName+"&help="+type2;
				
			}else if(type2==2){
				if(!name){d.alert({content:'姓名不能为空！',type:'error'});return false;}
				if(!phone){d.alert({content:'电话号码不能为空！',type:'error'});return false;}
				var isPhone=that.phone(phone);
				if(!isPhone){d.alert({content:'电话号码格式不正确',type:'error'});return false;}
				//if(!qq){d.alert({content:'qq不能为空！',type:'error'});return false}
				if(leaveWord.length>200){d.alert({content:'说明字数不能超过200字！',type:'error'});return false}
				$.ajax({
					url:dataUrl.appealByCustom,
					data:{name:name,qq:qq,phone:phone,leaveWord:leaveWord,t:new Date().getTime()},
					success: function(result){ 
						if(result.flag==1){
							 window.location.href=dataUrl.appealFive;//发布
							//d.alert({content:'提交成功，请等待客服联系！',type:'ok'});
						}else{
							d.alert({content:result.errorMsg,type:'error'});
							return false;
						}
					}
				}); 
			}
		},
		delPic:function(delV,obj){
			var that=this;
			var delI=jQuery.inArray(delV,that.photoData);
			$.ajax({
				url:dataUrl.helpphotoDel,
				data:{images:delV,t:new Date().getTime()},
				success: function(result){ 
					if(result.flag==1){
						that.photoData.splice(delI,1);
						var id = obj.parent().parent().attr("id");
						obj.parent().remove();
						$('#'+id+" .add").show();
						
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
					
					if(json.result==1){
						imgBox.children('.old').remove();
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var imgBox=obj.find('.add_list'),addObj=imgBox.children(".add");
						that.photoData.push(json.imageId);
						var num = obj.attr('id').substr(4);
						imgBox.children('.old').attr('id',json.imageId).children('img').attr('src',json.imageUrl).attr('class','io'+(num==1?'':num)).end();
						imgBox.children('.old').removeClass('old');
						imgBox.find(".item").not(".add").append('<i style="display:none">×</i>');
					}
				},
				error: function(data) {
					var imgBox=$('#imgList'),addObj=imgBox.children(".add");
					addObj.show();
					imgBox.children('.old').remove();
					d.alert({content:data.responseText,type:'error'});
				}
			}); 
		},
		needHelp2Submit:function(){
			var that=this,projectId=$('#projectId').val(),title=$('#title').val(),status=200,field=$('#field').val(),help=$('#help').val(),cryMoney=$('#cryMoney').val(),content=$("#content").val(),imgIds=that.photoData.join(','),conData=[],imgsDetail='';
			if(!title){d.alert({content:"求助标题不能为空",type:'error'});return false;};
			if(title.length>20){d.alert({content:"求助标题不能超过20个字",type:'error'});return false;};
			if(!cryMoney){d.alert({content:"求助金额不能为空",type:'error'});return false;};
			if(cryMoney<200){d.alert({content:"求助金额必须大于200元",type:'error'});return false;};
			if(that.photoData.length<1){d.alert({content:"请上传封面图片",type:'error'});return false;}
			if(!content){d.alert({content:"求助内容不能为空",type:'error'});return false;};
			if(projectId == ''){projectId=-1;}

			!content?content='1':'';
			conData.push(content);
			imgsDetail=conData.join(',');
			
			var isNeedVolunteer = $("input[name='choose']:checked").val();
			
			$.ajax({ 
					url:dataUrl.addproject,
					type: 'POST',
					data:{id:projectId,title:title,cryMoney:cryMoney,status:status,field:field,identity:help,content:content,imgIds:imgIds,imgsDetail:imgsDetail,isNeedVolunteer:isNeedVolunteer,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
							var obj=result.obj,help=obj.help,projectId=obj.projectId;
							window.location.href=base+"project/appealThree.do?help="+help+"&projectId="+projectId;
							 //成功后跳转下一步
							 
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.needHelp2Submit);
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
			});
			
			
		},
		needHelp3Submit:function(type,step){
			
			var that=this,projectId=$('#projectId').val(),proAddr=$('#seachprov1 :selected').text()+$('#seachcity1 :selected').text()+$('#seachdistrict1 :selected').text(),
			imgsId='' ,appealAddr=$('#seachprov :selected').text()+$('#seachcity :selected').text()+$('#seachdistrict :selected').text();
			//appealName=$('#appealName').val(),appealIdcard=$('#appealIdcard').val(),appealAddress=$('#appealAddress').val(),
			//appealAddr=$('#seachprov').val()+$('#seachcity').val()+$('#seachdistrict').val(),
			//appealGroupName=$('#appealGroupName').val(),proAddr=$('#seachprov1').val()+$('#seachcity1').val()+$('#seachdistrict1').val(),appealGroupAddr=$('#appealGroupAddr').val();
			//rCom=$('#rCom').val(),rzw=$('#rzw').val(),rPhone=$('#rPhone').val(),rname=$('#rName').val();
			//eName=$('#eName').val(),eAddr=$('#eAddr').val(),eContactor=$('#eContactor').val(),eOtherWay=$('#eOtherWay').val(),eTel=$('eTel').val();
			//获取是个人发起还是机构发起
			var name='',Idcard='',identity='',Phone='',gzdw='',zy='',Address='',weixin='',linkMan='',stype;//发起人
			var appealName='',appealSex='',appealAge='',appealIdcard='',appealAddress='',appealgzdw='',appealzy='',relation='',appealMobile='';
			var rname='',rPhone='',rAddress='',rzw='',rgzdw='';//证明人
			
			if($('.radio #dw').attr('checked')=='checked'){//机构
				stype=1;
				name=$('#eName').val(),Address=$('#eAddr').val(),weixin=$('#eOtherWay').val(),linkMan=$('#eContactor').val(),Phone=$('#eTel').val();
			}else{//个人
				stype=7;
				name=$('#fname').val(),Idcard=$('#fIdcard').val(),identity=$('#relaty :selected').text(),Phone=$('#fPhone').val();
			}
			//从而判断发起的项目类型
			if(stype==1){//机构
				if($('.radio2 #appealSingle').attr('checked')=='checked'){//受助人：个人
					stype=9;
					appealName=$('#appealName').val(),appealIdcard=$('#appealIdcard').val(),appealAddress=$('#seachprov :selected').text()+$('#seachcity :selected').text()+$('#seachdistrict :selected').text()+$('#appealAddress').val(),appealMobile=$('#appealMobile').val();
				}else{//受助人：群体
					stype=10;
					appealName=$('#appealGroupName').val(),appealAddress=$('#seachprov1 :selected').text()+$('#seachcity1 :selected').text()+$('#seachdistrict1 :selected').text()+$('#appealGroupAddr').val();
				}
			}else{//个人
				appealName=$('#appealName').val(),appealIdcard=$('#appealIdcard').val(),appealAddress=$('#seachprov :selected').text()+$('#seachcity :selected').text()+$('#seachdistrict :selected').text()+$('#appealAddress').val(),appealMobile=$('#appealMobile').val();
			}
			
			if(stype!=10){
				rgzdw=$('#rCom').val(),rname=$('#rName').val(),rzw=$('#rzw').val(),rPhone=$('#rPhone').val();
			}
			
			//if(type != 0 || step != 0){
				if($('.radio #gr').attr('checked')=='checked'){
					if(!name){d.alert({content:"发布人姓名不能为空",type:'error'});return false;};
					if(!Phone){d.alert({content:"发布人联系电话不能为空",type:'error'});return false;};
					var isPhone=that.phone(Phone);
					if(!isPhone){d.alert({content:"发布人联系电话格式不正确！",type:'error'});return false;};
					if(!Idcard){d.alert({content:"发布人身份证不能为空",type:'error'});return false;};
					var isIdcard=en.isVaildId($("#fIdcard"));
					if(!isIdcard){d.alert({content:"请输入正确的身份证件号码！",type:'error'});return false;}
					if(!identity || /请选择/.test(identity)){d.alert({content:"请选择发布人与受助人关系",type:'error'});return false;};
					var imgsid6 = $('.io6').parent().attr('id');
					var imgsid7 = $('.io7').parent().attr('id');
					var imgsid8 = $('.io8').parent().attr('id');
					//判断是否实名认证
					if($('#auditStaff_type').val()!=1&&(typeof(imgsid6)=="undefined"||typeof(imgsid7)=="undefined"||typeof(imgsid8)=="undefined")){
						d.alert({content:"请完整上传发起人身份证正面反面及手持身份证",type:'error'});return false;
					}
					
				}else if($('.radio #dw').attr('checked')=='checked'){
					if(!name){d.alert({content:"发布单位名称不能为空",type:'error'});return false;};
					if(!Address){d.alert({content:"发布单位详细地址不能为空",type:'error'});return false;};
					if(!eContactor){d.alert({content:"发布单位项目联系人不能为空",type:'error'});return false;};
					if(!Phone){d.alert({content:"发布人联系电话不能为空",type:'error'});return false;};
					var isPhone=that.phone(Phone);
					if(!isPhone){d.alert({content:"发布人联系电话格式不正确！",type:'error'});return false;};
					if(!weixin){d.alert({content:"发布人其他联系方式不能为空",type:'error'});return false;};	
					var imgsid3 = $('.io3').parent().attr('id');
					if($('#company_type').val()!=1&&typeof(imgsid3)=="undefined"){
						d.alert({content:"请上传营业执照",type:'error'});return false;
					}
				}
				if($('.radio2 #appealSingle').attr('checked')=='checked'){
					if(!appealName){d.alert({content:"受助人姓名不能为空",type:'error'});return false;};
					if(!appealIdcard){d.alert({content:"受助人身份证不能为空",type:'error'});return false;};
					var isAppealIdcard=en.isVaildId($('#appealIdcard')); 
					if(!isAppealIdcard){d.alert({content:"请输入正确的受助人身份证件号码！",type:'error'});return false;}
					//if(/请选择/.test(appealAddr)){d.alert({content:"受助人所在地请选择完整",type:'error'});return false;}
					if(!$('#appealAddress').val()){d.alert({content:"受助人详细地址不能为空",type:'error'});return false;};	
					//联系电话
					if(!$('#appealMobile').val()){d.alert({content:"受助人联系电话不能为空",type:'error'});return false;};	
					if(!that.phone($('#appealMobile').val())){d.alert({content:"请输入正确的受助人联系电话！",type:'error'});return false;}
					
					var imgsid4 = $('.io4').parent().attr('id');
					var imgsid5 = $('.io5').parent().attr('id');
					var imgsid9 = $('.io9').parent().attr('id');
					var imgsid10 = $('.io10').parent().attr('id');
					if(typeof(imgsid4)=="undefined"||typeof(imgsid5)=="undefined"){
						d.alert({content:"请完整上传受助人身份证正面反面",type:'error'});return false;
					}
					if(typeof(imgsid9)=="undefined"){
						d.alert({content:"请上传知情同意书",type:'error'});return false;
					}
					if(stype!=7 && typeof(imgsid10)=="undefined"){
						d.alert({content:"请上传项目发起申请书",type:'error'});return false;
					}
					
				}else if($('.radio2 #appealGroup').attr('checked')=='checked'){
					if(!appealName){d.alert({content:"受助群体名称不能为空",type:'error'});return false;};
					//if(/请选择/.test(proAddr)){d.alert({content:"项目地点请选择完整",type:'error'});return false;};
					if(!$('#appealGroupAddr').val()){d.alert({content:"项目详细地址不能为空",type:'error'});return false;};
					var imgsid10 = $('.io10').parent().attr('id');
					if(typeof(imgsid10)=="undefined"){
						d.alert({content:"请上传项目发起申请书",type:'error'});return false;
					}
				}
				//判断有无证明单位
				/*if(stype!=10){
					if(!rgzdw){d.alert({content:"证明人单位不能为空",type:'error'});return false;};
					if(!rname){d.alert({content:"证明人姓名不能为空",type:'error'});return false;};
					if(!rzw){d.alert({content:"证明人职务不能为空",type:'error'});return false;};
					if(!rPhone){d.alert({content:"证明人电话不能为空",type:'error'});return false;};
					var isRPhone=that.phone(rPhone);
					if(!isRPhone){d.alert({content:"证明单位电话格式不正确！",type:'error'});return false;};
				}*/
				
				
			/*}else{
				if(!appealAge){
					appealAge = 0;
				}
			}*/
			if(stype==7){
				if($('#auditStaff_type').val()!=1){
					imgsId=$('.io6').parent().attr('id')+','+$('.io7').parent().attr('id')+','+$('.io8').parent().attr('id')+',';
				}
				imgsId+=$('.io4').parent().attr('id')+','+$('.io5').parent().attr('id')+
				','+$('.io9').parent().attr('id');
			}else if(stype==9){
				if($('#company_type').val()!=1){
					imgsId=$('.io3').parent().attr('id')+','
				}
				imgsId+=$('.io4').parent().attr('id')+','+$('.io5').parent().attr('id')+
				','+$('.io9').parent().attr('id')+','+$('.io10').parent().attr('id');
			}else if(stype==10){
				if($('#company_type').val()!=1){
					imgsId=$('.io3').parent().attr('id')+','
				}
				imgsId+=$('.io10').parent().attr('id');
			}
			$.ajax({ 
					url:dataUrl.appealUserInfoData,
					type: 'POST',
					data:{id:projectId,imgIds:imgsId,helpType:stype,type:type,
						name:encodeURI(name),Idcard:encodeURI(Idcard),Phone:encodeURI(Phone),weixin:encodeURI(weixin),relation:encodeURI(identity),
						Address:encodeURI(Address),linkMan:encodeURI(linkMan),
						appealName:encodeURI(appealName),appealIdcard:encodeURI(appealIdcard),appealAddress:encodeURI(appealAddress),appealPhone:appealMobile,
						rname:encodeURI(rname),rPhone:encodeURI(rPhone),rAddress:encodeURI(rgzdw),rzw:encodeURI(rzw)},

					chase:false,
					success: function(result){
						if(result.flag==1){
							 if(type==0){
								 if(step == 0){
									 window.location.href=dataUrl.appealSecond+"?projectId="+result.obj;//上一步
								 }else{
									 window.location.href=dataUrl.projectview+"?projectId="+result.obj;//保存并预览
								 }
							 }else{
								 window.location.href="http://www.17xs.org/newReleaseProject/success.do?projectId="+projectId;//dataUrl.appealFour;//发布
							 }
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.needHelp3Submit);
						}
					},
					error: function(){ 
						return d.alert({content:"网络异常,请联系客服"});
					}
			});
			
		},phone:function(str){
			 var cellphone = /^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
			if(cellphone.test(str)){
				return true;
			}else{
				return false;
			}
		}
		
		
		
	};
	 
	needhelp.init();
});