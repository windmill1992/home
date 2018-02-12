var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt",
	showDetailList:base+'ucenter/coredata/callList.do',
	userInfo:base+'ucenter/coredata/personalUserInfo.do'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"util"   : "dev/common/util",
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter" : "dev/common/userCenter",		
		"area"	: "util/area",
		"pages"   :"dev/common/pages",
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages","area","ajaxform"],function($,d,h,en,uc,p,a,f){
	window.uCENnavEtSite="p-centralaccount";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var addressNum = $('#addressNum').val();
	if(addressNum){
		var adr =addressNum.split(" ");
		a.init({selectsId:["province","city","county"],def:[adr[0],adr[1],adr[2]]});
	}else{
		if($('#province').length>0){
			a.init({selectsId:["province","city","county"],def:[0,0,0]});
		}
	}
	var reg={
		type:0,
		id:'',
		init:function(){
			var that=this;
			 $("body").on("click",".gdend",function(){ 
		    	var data = $(this).parents('li').attr("data"); 
		    	that.gdend(data);
			}).on("click",".gdModify",function(){ 
				var data = $(this).parents('li').attr("data"); 
				that.gdModify(data);
			}).on('click','#endSubmit',function(){
				 var di=$(this).parents(".cp2yDialogBox").attr("data");
				 var id = $(this).attr("data");
				that.stopZhuShan(di, id);
			}).on('click','#ModifySubmit',function(){
				 var di=$(this).parents(".cp2yDialogBox").attr("data");
				 var id = $(this).attr("data");
				 that.updateZhuShan(di,id);
			}).on("click",".showDetail",function(){ 
				var data=$(this).attr('data').split('|');
				that.showDialog(data[0],data[1]);
				p.init({afterObj:$("#itemListd")});
				p.changeCallback=function(){
					reg.showDetail(p.curPage,that.id);
				};
				uc.selectCallback=function(){
					reg.showDetail(p.curPage,that.id);
				};
			});
			 $('#submitData').click(function(){
				 that.userInfo();
			 });
			 $('#imgFileLogo').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#pic1 img').attr('src',base+"/res/images/common/bar2.gif");
						$('#formLogo').submit();
					}
					return false;
				});
				
			$('.sona_img').hover(function(){
				$(this).children('.edit').show();
			},function(){
				$(this).children('.edit').hide();
			})
				that.ajaxForm($('#formLogo'),$('.sona_img img'),1);

			
		},
		userInfo:function(){
			var realname = $('#realname').val(),persition = $('#persition').val(),mobileNum = $('#mobileNum').val(),
			weixin = $('#weixin').val(),workUnit = $('#workUnit').val(),address=a.getArea();
			$.ajax({
				url : dataUrl.userInfo,
				data:{realname:realname,phone:mobileNum,vocation:persition,
					workUnit:workUnit,weixin:weixin,address:address,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						d.alert({content:"保存成功",type:'ok'});
					}
				},
				error   : function(r){ 
					d.alert({content:'修改助善信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
			
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
						window.location.reload();
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		showDialog:function(title,id){
			var that=this,html=[];	
			html.push('<div class="diabox-details_funds ">');
			html.push('<div class="details_funds-list" id="itemListd">');
			html.push('</div>')	;
			var o ={t:title+" 助善捐款名单",c:html.join("")},css={width:'600px',height:'500px'};  
			d.open(o,css,"dialogContentNone");
			that.showDetail(1,id);
		},	
		showDetail:function(page,id){
			var that=this,html=[],flag=false;
			page==1?flag=true:'';
			id?that.id=id:'';
			html.push('<ul class="ul1">');
			html.push('<li class="">助善时间</li>');
			html.push('<li class="">用户名</li>');
			html.push('<li class="li1">助善金</li>');
			html.push('</ul>');
			$.ajax({
				url:dataUrl.showDetailList,
				data:{page:page,projectId:id,pageNum:9,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=result.total,pageNum=data.pageSize,datas=data,len=datas.resultData.length;
						//len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas.resultData[i]; 
							html.push('<ul '+((i+1)%2==0?'class="bgcolor1 listone"':'listone')+'>');
							
							html.push('<li class="">'+(new Date(idata.donatTime)).pattern("yyyy/MM/dd HH:mm")+'</li>');
							html.push('<li class=" ">'+idata.userName+'</li>');
							html.push('<li class="li1 ">'+idata.donatAmount+'</li>');
							html.push('</ul>');
						}  
						$('#itemListd').html(html.join(''));
						data.pages>1&&flag?p.pageInit({pageLen:data.pages,isShow:true}):'';
						
					}else if(result.flag==2){
						html.push('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
						$('#itemListd').html(html.join(''));
					}else if(result.flag==0){
						html.push('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						$('#itemListd').html(html.join(''));
					}else if(result.flag==-1){
						html.push('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						$('#itemListd').html(html.join(''));
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(detail.showDetail);
						}
					}
					
					
				}
			});
			
		},
		gdend:function(data){
			var temp = data.split(",");
			var html=[];
				html.push('<div class="gddialog yh">');
				html.push('<div class="rlgroup"><label>项目名称</label><div class="controls controlsn"><div class="rlinputText">'+temp[0]+'</div></div></div>');
				html.push('<div class="rlgroup"><label>终止原因</label><div class="controls controlsn"><textarea class="tarea" id="stopReason" placeholder="请输入50字以内的提前终止原因。"></textarea></div>');
				html.push('<div class="rlgroup"><label>当前已捐赠</label><div class="controls controlsn"><div class="rlinputText">'+temp[2]+'元</div></div></div>');
				html.push('<div class="gdinfo">因为您的助善，已有'+temp[4]+'人加入我们，一起行善！</div>');
				html.push('<div class="rlitem"><label></label><div class="rltext"><input type="button" class="rlbtnok yh" id="endSubmit"  data="'+temp[5]+'" value="确认终止"/></div></div>');
				html.push('</div>');
				
				var o ={t:"确认终止助善",c:html.join("")},css={width:'600px'};  
				d.open(o,css,"gdendDialog");
		},
		gdModify:function(data){
			var temp = data.split(",");
			var html=[];
				html.push('<div class="gddialog yh">');
				html.push('<div class="rlgroup"><label>项目名称</label><div class="controls controlsn"><div class="rlinputText">'+temp[0]+'</div></div></div>');
				html.push('<div class="rlgroup"><label>奖励金额/人</label><div class="controls controlsn"><input type="text" class="gdmoney" id="gdmoney" placeholder="'+temp[1]+'" >元</div>');
				html.push('<div class="rlgroup"><label>奖励总额/预算总额</label><div class="controls controlsn"><div class="rlinputText">'+temp[2]+'元/'+temp[3]+'元</div></div></div>');
				html.push('<div class="gdinfo">因为您的助善，已有'+temp[4]+'人加入我们，一起行善！</div>');
				html.push('<div class="rlitem"><label></label><div class="rltext"><input type="button" class="rlbtnok yh" id="ModifySubmit" data="'+temp[5]+'" value="确定"/></div></div>');
				html.push('</div>');
				
				var o ={t:"企业助善修改",c:html.join("")},css={width:'600px'};  
				d.open(o,css,"gdendDialog gdModifyD");
		},
		updateZhuShan : function(di,id){
			var perMoney = $('#gdmoney').val();
			var that = this;
			if(!perMoney){
				d.alert({content:'捐助信息不能为空。',type:'error'});
				return ;
			}
			$.ajax({
				url     : WebAppUrl.UPDATEGOODHELP,
				data:{aid:id,perMoney:perMoney,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						 that.listShow(that.pageCurrent, 10);
						 d.close(di);
					}
				},
				error   : function(r){ 
					d.alert({content:'修改助善信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
        stopZhuShan : function(di,id){
        	var stopReason = $('#stopReason').val();
        	var that = this;
        	if(!stopReason){
				d.alert({content:'终止原因不能为空。',type:'error'});
				return ;
			}
        	$.ajax({
				url     : WebAppUrl.STOPGOODHELP,
				data:{aid:id,reason:stopReason,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						 that.listShow(that.pageCurrent, 10);
						 d.close(di);
					}
				},
				error   : function(r){ 
					d.alert({content:'停止助善项目失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		}
		
	};
	
	reg.init();
});