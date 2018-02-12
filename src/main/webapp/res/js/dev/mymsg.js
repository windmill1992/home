window.baseurl='http://www.17xs.org/';
var dataUrl={ 
	baseurl:'http://www.17xs.org/',
	msgList:baseurl+"message/list.do",
	msgDel:baseurl+"message/dmsg.do",
	msgDtil:baseurl+'message/msgdetail.do'
}
require.config({
	baseUrl:window.baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages"],function($,d,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-msg";//如需导航显示当前页则加此设置
	window.uCENnavEtSite="p-msg";//如需导航显示当前页则加此设置
	d.init();
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#list-bd")});
	p.changeCallback=function(){
		msg.getData(p.curPage);
	};
	uc.selectCallback=function(){
		msg.getData(p.curPage);
	}; 
	var listObj=$("#list-bd"); 
	var msg={
		time:null,
		isGetData:-1,
		init:function(o){
			var that=this;
			for(var key in o){
				that[key]=o[key]; 
			}   
			
			msg.getData();
 			
			$("body").on("click","#itemRefrash",function(){
				msg.getData();
			}).on("click",".i-del",function(){
				var parent = $(this).closest("ul"); 
				d.alert({content:"是否确定删除",cName:'delMsg'});  
				$("#delMsgId").val(parent.attr("id"));  
			}).on("click",".delMsg .sureBtn",function(){ 
				if(en.isIn){
					that.delMsg(); 
				}else{
					en.show(msg.delMsg);
				} 
			});
		},
		getData:function(page){ 
			var time=$(".sel-val").attr('v');
			var flag=false,that=this;
			if(this.time!=time){
				flag=true;
				this.time=time; 
				page=1;
			} 
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.msgList,
				data:{type:time,page:page,t:new Date().getTime()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						
						var msgDtil=dataUrl.msgDtil+"?id=";
						for(var i=0;i<len;i++){
							var idata=datas[i];
							var idate=idata.cTimeFormat.split(' '); 
							html.push('<ul id="'+idata.id+'" data="'+idata+'" '+((i+1)%2==0?'class="bgcolor1"':'')+'>'); 
							html.push('<li class="lst-col1">'+(i+1)+'</li>');
							html.push('<li class="lst-col2">'+idata.sender+'</li>');
							html.push('<li class="lst-col3 lst-br">'+idate[0]+'<br/>'+idate[1]+'</li>');
							html.push('<li class="lst-col4"><a href="javascript:void(0);">'+idata.title+'</a></li>');
							html.push('<li class="lst-col5"><span class="i-del">删除</span>&nbsp;<a href="'+msgDtil+idata.id+'">查看</a></li>');
					        html.push('</ul>');
						}  
						listObj.html(html.join('')); 
						//分页
						data.total>1&&flag?p.pageInit({pageLen:total,isShow:true}):'';
						data.total==1?p.pageInit({pageLen:total,isShow:false}):'';
					}else if(result.flag==2){
						listObj.html('<div class="listno yh">抱歉，该列表暂时没有消息与通知 <a  title="">请查看其它类别~</a></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="listno yh">抱歉，该列表暂时没有消息与通知 <a  title="">请查看其它类别~</a></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(msg.getData);
						}
					}
				},
				error:function(){
				   d.alert({content:"网络异常请联系客服"});
				}
			});
		},
		delMsg:function(){
			var msgId=$("#delMsgId").val();
			$.ajax({
				url:dataUrl.msgDel,
				data:{id:msgId}, 
				success: function(result){ 
					var rst=eval(result);  
					if(rst.flag==1){ 
						window.location.reload(true);
					}else{
						d.alert({content:rst.errorMsg});
					}  
				},
				error:function(){
				   d.alert({content:"网络异常请联系客服"});
				}
			});
		}
		 
	};
	
	msg.init(); 
	
/*	var delBtnId=0;
	$("body").on("click",".i-del",function(){
		var parent = $(this).closest("ul"); 
		d.alert({content:"是否确定删除",cName:'delMsg'}); 
		delBtnId=$(".delMsg").attr("data");
		$("#delMsgId").val(parent.attr("id")); 
		 
	});
	
	$("body").on("click",".delMsg #frameBtn"+delBtnId,function(){ 
		if(en.isIn){
			delMsg($("#delMsgId").val()); 
		}else{
			en.show();
		} 
	});
	 
	function delMsg(msgId){
		$.ajax({
			url:dataUrl.msgDel,
			data:{id:msgId}, 
			success: function(result){ 
				var rst=eval("("+result+")");  
				if(rst.flag==1){ 
					window.location.reload(true);
				}else{
					d.alert({content:rst.errorMsg});
				}  
			},
			error:function(){
			   
			}
		});
	}*/
});