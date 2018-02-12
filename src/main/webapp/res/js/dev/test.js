var base=window.baseurl;
var dataUrl={
	addList:base+'project/projectDonateGo.do',//善款去向列表
	addUrl:base+'project/view/',//善款去向详情连接
	syaddUrl:'http://sy.17xs.org/project/syview.do?projectId='

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
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20151010"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	//window.mCENnavEtSite="m-acccountTracking";
	window.sitePage="p-monthDonate";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#dtList")});
	p.changeCallback=function(){
		addListNew.dataList(p.curPage,10);
	};
	addListNew={
		init:function(){
			var that=this;
			that.dataList(1,10);
			$("#select").click(function(){
				that.dataList(1,10);
			});
		},
		dataList:function(page,num){
			var that=this,html=[],title=$("#smsg").val(),flag=false;
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.addList,
				data:{page:page,pageNum:num,title:title,t:new Date()},
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
							    var titleurl=dataUrl.addUrl+"?projectId="+datas[i].projectId;
							    if(datas[i].field == 'good'){
							    	titleurl=dataUrl.syaddUrl+datas[i].projectId;
							    }
								html.push('<ul>');
								html.push('<li class="lst-col1">'+(new Date(datas[i].payMoneyTime)).pattern("yyyy-MM-dd")+'</li>');
								html.push('<li class="lst-col2"><a href="'+titleurl+'" target="_blank">'+datas[i].projectTitle+'</a></li>');
								html.push('<li class="lst-col3">'+datas[i].realName+'</li>');
								html.push('<li class="lst-col4">'+datas[i].account+'</li>');
								html.push('<li class="lst-col5 orangeText">'+datas[i].panMoney+'</li>');
								html.push(' <li class="lst-col6"><a href="'+titleurl+'&itemType=1" title="" class="greenText">查看凭证</a></li>');
								html.push('</ul>');
							}
						}else{
							html.push('<div class="listno yh">抱歉， 该列表暂时没有相关善款去向列表</div>');
						}
						$('#dtList').html(html.join(''));			
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
					}
				},
				error   : function(r){ 
					d.alert({content:'获取善款去向列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	addListNew.init();
});