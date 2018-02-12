var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
	boundbankcard:base+"ucenter/coredata/boundbankcard.do",//填提款账户
	WithdrawDeposit:base+"ucenter/coredata/WithdrawDeposit.do",//提款申请
	DrawingList:base+"ucenter/core/WithdrawDepositRecordData.do",//提款记录
	Manage:base+"ucenter/core/personalcapital.do",
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
		"ajaxform"  : "util/ajaxform" ,
		"area":"util/area",
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","area","pages"],function($,d,h,en,uc,f,a,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	if($("#flag").val()=="withdraw"){
		window.mCENnavEtSite="m-widthDraw";
	}else{
		window.mCENnavEtSite="m-capital";//如需导航显示当前页则加此设置
	}
	
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		cash.DrawingList(p.curPage,10);
	};
	if($('#province').length>0){
		a.init({selectsId:["province","city","county"],def:[0,0,0]});
	}
	var cash={
		photo1:'',
		photoData:[],
		init:function(){
			var that=this;
			if($('#cashNo').length<1){//提款申请
					that.DrawingList(1,10);
			}
			$('.switch_tab li').click(function(){
				var i=$('.switch_tab li').index(this);
				$('.switch_tab li').removeClass('cur').eq(i).addClass('cur');
				i==0?$('#bankLabel').html('开户名：'):$('#bankLabel').html('单位全名：');
				i==0?$('#prompt').show():$('#prompt').hide();
				$('#name').val('');
			});
			
			$('.add_btn').click(function(){
				if($('#cashNo').length>0){//填提款账户
					that.boundbankcard();
				}else{//提款申请
					that.WithdrawDeposit();
				}
			});
			
			$('#imgFile1').change(function(){
					var p=that.photoPic(this);
					if(p){
						$('#form1').submit();
					}
					return false;
			});
			
			$("body").on("change",".file-input",function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$(this).parents(".add_list"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					if(addObj.siblings('.item').length>=2){
						addObj.hide();
					}
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="80px"></div>');
					imgBox.parent().submit();
					var tempObj = $(this).parent();
					var id = $(this).attr('id');
					$(this).remove();
					tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
//					$(this).val('');
				}
				return false;
			}).on("mouseenter",".add_list .item",function(){ 
				$(this).children('i').show();
			}).on("mouseleave",".add_list .item",function(){ 
				$(this).children('i').hide();
			}).on("click",".add_list .item i",function(){ 
				var delV=$(this).parent().attr('id');
				var isDel=that.delPic(delV,$(this));
			});
			that.ajaxForm2($('#form1'));
			
			
			$('#money').blur(function(){
				var money=$(this).val(),userMoney=$('#userMoney').val();//;页面传的可用金额值
				if(money>userMoney){
					d.alert({content:"提款金额不能超过可用金额！",type:'error'});
					money=userMoney;
				}
				$(this).val(money);
			});
		},
		boundbankcard:function(){
			var that=this,name=$('#name').val(),projectId=$('#projectId').val(),bankName=$('#bankname').val(),bankType=$('.switch_tab').find(".cur").attr('tel'),cardNo=$('#cardNo').val(),location=a.getArea();
			if(!name){return d.alert({content:"对不起，开户名不能为空！",type:'error'});}
			if(!bankName){return d.alert({content:"对不起，开户行不能为空！",type:'error'});}
			if(!cardNo){return d.alert({content:"对不起，卡号不能为空！",type:'error'});}
			if(!location){return d.alert({content:"对不起，开户行所在地尚未选择！",type:'error'});}
			var province=location.split("#")[0];
			$.ajax({ 
					url:dataUrl.boundbankcard,
					type: 'POST',
					data:{pid:projectId,name:name,bankName:bankName,bankType:8,cardNo:cardNo,province:province,accountType:bankType,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
							 //成功
							window.location.reload();
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.boundbankcard);
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});
		},
		WithdrawDeposit:function(){
			var that=this,bankName=$('#bankName').val(),projectId=$('#projectId').val(),
				bankType=$('#bankType').val(),cardNo=$('#cardNo').val(),money=$('#money').val(),imgId=that.photoData.join(',');
			if(!money){return d.alert({content:"对不起，请输入提现金额！",type:'error'});}
			if(!projectId){return d.alert({content:"对不起，没有选择项目",type:'error'});}
			if(!imgId){return d.alert({content:"对不起，没有上传票据",type:'error'});}
			$.ajax({ 
				url:dataUrl.WithdrawDeposit,
				type: 'POST',
				data:{bankName:bankName,bankType:bankType,pid:projectId,cardNo:cardNo,money:money,imageId:imgId,t:new Date().getTime()},
				chase:false,
				success: function(result){
					if(result.flag==1){
						 //成功
						 window.location.href=dataUrl.Manage;//发布
					}else if(result.flag==0){
						return d.alert({content:result.errorMsg,type:'error'});
					}else if(result.flag==-1){
						return en.show(that.WithdrawDeposit);
					}
				},
				error: function(){
					return d.alert({content:"网络异常,请联系客服"});
				}
			});
			
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
		ajaxForm:function(obj,showobj,type){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'),srcOld=base+"res/images/login/uploader.jpg"; 
					if(json.result==1){
						$(showobj).attr('src',srcOld);
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						$(showobj).attr('src',json.imageUrl);
						that.photo1=json.imageId;
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		ajaxForm2:function(obj){
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
						imgBox.find(".item").not(".add").append('<i style="display:none">&times;</i>');
						
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
		DrawingList:function(page,num){
			var that=this,html=[],flag=false;
			if(page<2){flag=true;};
			html.push('<tr class="th">');
			html.push('<td>申请单号</td>');
			html.push('<td>申请时间</td>');
			html.push('<td>项目标题</td>');
			html.push('<td>申请金额</td>');
			html.push('<td>状态</td>');
			html.push('<td>打款时间</td>');
			html.push('</tr>');
			$.ajax({
				url:dataUrl.DrawingList,
				data:{page:page,pageNum:num,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.dataList);
					}else{ 
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length;
						len=len>pageNum?pageNum:len; 
						
						if(data.total>0){
							for(var i=0;i<len;i++){
								var idata=datas[i]; 
								html.push('<tr '+((i+1)%2==0?'class="rowbg"':'')+'>');
								html.push('<td>'+idata.trannum+'</td>');
								html.push('<td>'+(new Date(idata.createtime)).pattern("yyyy/MM/dd HH:mm")+'</td>');
								html.push('<td>'+idata.title+'</td>');
								html.push('<td>￥'+$.formatNum(idata.money)+'</td>');
								if(idata.state == 300){
									html.push('<td>待打款</td>');
								}else if(idata.state == 301){
									html.push('<td>取消</td>');
								}else{
									html.push('<td>成功</td>');
								}
								if(idata.state == 300){
									html.push('<td></td>');
								}else{
									html.push('<td>'+(new Date(idata.paydate)).pattern("yyyy/MM/dd HH:mm")+'</td>');
								}
								
								html.push('</tr>');
							}
						}else{
							html.push('<td colspan="6">还没有提款记录</td>');
						}
						$('#listShow').html(html.join(''));		
						data.pages>1&&flag?p.pageInit({pageLen:data.pages,isShow:true}):'';
					}
				},
				error   : function(r){ 
					d.alert({content:'获取提现记录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
			
		}
	};
	 
	cash.init();
});