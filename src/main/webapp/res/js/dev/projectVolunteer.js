var base=window.baseurl;
var dataUrl={
		volunteerList:base+"ucenter/getProjectVolunteer.do",// 志愿者列表
		volunteerDetail:base+"ucenter/volunteerDetail.do?id="// 志愿者详情
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
	//window.sitePage="p-"+pageName 
	window.mCENnavEtSite="m-qz";
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		volunteerList.getData(p.curPage);
	};
	uc.selectCallback=function(){
		volunteerList.getData(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var volunteerList={
		time:null, 
		init:function(){	
			var that=this;
			that.getData();
		},
		getData:function(page){ 
			if(!page){page=1;} 
			var projectId=$("#projectId").val();
			var flag=false;
			if(page<2){flag=true;}
			$.ajax({
				url:dataUrl.volunteerList,
				data:{page:page,pageNum:10,userId:null,projectId:projectId,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						html.push('<colgroup><col width="18%"></col><col width="16%"></col><col width="18%"></col><col width="20%"></col><col width="16%"></col><col width="10%"></col></colgroup>');
						html.push('<thead><tr><th>姓名（联系人）</th><th>类型</th><th>联系电话</th><th>参与项目</th><th>报名时间</th><th>操作</th></tr></thead>');
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							var itemUrl = dataUrl.volunteerDetail+idata.id;
							html.push('<tr>');
							if(idata.personType == 0){
								html.push('<td>'+idata.name+'</td>');
								html.push('<td>个人</td>');
							}else{
								html.push('<td>'+idata.linkMan+'</td>');
								html.push('<td>组织</td>');
							}
							html.push('<td>'+idata.mobile+'</td>');
							html.push('<td>'+idata.pTitle+'</td>');
							html.push('<td>'+(new Date(idata.createTime)).pattern("yyyy-MM-dd HH:mm:ss")+'</td>');
							html.push('<td><a style="color:#199a56" href="'+itemUrl+'">查看</a></td>');
							html.push('<tr>');
						}  
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						listObj.html('<div class="prompt"><div class="listno yh">没有求助者记录</div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(volunteerList.getData);
						}
					}
				}
			});
		}
	};
	
	volunteerList.init(); 
	 
});
