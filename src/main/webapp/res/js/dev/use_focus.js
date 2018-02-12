var base=window.baseurl;
var dataUrl={
	dataLsit:base+"project/careProjectList.do",//关注项目
	itemDetail:base+'project/view.do',     //项目详情页
	itemDetail2:base+'project/newGardenView.do',  //善园详情页
	itemDetail3:'http://sy.17xs.org/project/syview.do'     //善缘项目详情页
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
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages"
		
	},
	urlArgs:"v=20151010"
});

define(["extend","head","entry","userCenter","pages"],function($,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-focus";
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		use_focus.getData(p.curPage);
	};
	uc.selectCallback=function(){
		use_focus.getData(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var use_focus={
		time:null, 
		init:function(){	
			var that=this;
			that.getData();
		},
		getData:function(page){ 
			var time=$(".sel-val").attr('v'),userId=""//用户ID;
			var projectId = $("#projectId").val();
			var flag=false;
			if(!page){page=1;} 
			if(this.time!=time){
				flag=true;
				this.time=time; 
				page=1;
			} 
			
			$.ajax({
				url:dataUrl.dataLsit,
				data:{userId:userId,type:time,page:page,pageNum:10,t:new Date(),projectId:projectId},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							var itemDtil=dataUrl.itemDetail+'?projectId=';
							if(idata.field=="garden"){
								itemDtil=dataUrl.itemDetail2+'?projectId=';
							}else if(idata.field=="good"){
								itemDtil=dataUrl.itemDetail3+'?projectId=';
							}
							html.push('<li>');
							if(idata.uName==null){
								html.push('<div class="fndpo_left"><img class="img_shadow" src="'+base+'/res/images/item/items/txicon_04.jpg" width="81" height="57"/><span>客服</span></div>');
							}else{
								if(idata.userImageUrl != null){
									html.push('<div class="fndpo_left"><img class="img_shadow" src="'+idata.userImageUrl+'" width="81" height="57"/><span>'+idata.uName+'</span></div>');
								}else{
									html.push('<div class="fndpo_left"><img class="img_shadow" src="http://www.17xs.org/res/images/user/user-icon.png" width="81" height="57"/><span>'+idata.uName+'</span></div>');
								}
							}
							html.push('<div class="fndpo_right">');
							//html.push('<div class="fdpo_title"><a class="del" href="javascript:void(0)">删除</a><a class="tit" href="javascript:void(0)">'+idata.title+'</a></div>');
							html.push('<div class="fdpo_title"><a class="tit" href="'+itemDtil+idata.pid+'" target="_blank">'+idata.title+'</a></div>');
							html.push('<div class="fdpo_conn"><i></i>');
							html.push('<p>'+idata.content+'</p>');
							if(idata.imgs){							
								html.push('<div class="po_img">');
								for(var j=0;j<idata.imgs.length;j++){
									html.push('<img src="'+idata.imgs[j]+'" width="77" height="77" />');
								}
								html.push('</div>');
							}
							html.push('<div class="po_bottom">'+(new Date(idata.cTime)).pattern("yyyy-MM-dd HH:mm")+'</div>');
							html.push('</div></div>');
							html.push('</li>');
						}  
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						listObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有相关关注列表</div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(use_focus.getData);
						}
					}
					layer.photos({
						photos : '#listShow',
						shift : 5,
						closeBtn : 1
					});
				}
			});
		}
	};
	
	use_focus.init(); 
	 
});



