var base=window.baseurl;
var dataUrl={
	addList:base+'project/latestdonationlist.do',//捐款人列表
	addListSearch:base+'project/latestdonationlistSearch.do',//捐款人列表（搜索）
	addUrl:base+'news/center/'//捐款人详情连接
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
		"pages"   :"dev/common/newpages" 
	},
	urlArgs:"v=20151010"
});
var states = null;
define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	//window.mCENnavEtSite="m-acccountTracking";
	window.sitePage = 'p-donateRecord';
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#dtList")});
	p.changeCallback=function(){
		addListNew.dataListSearch(states,p.curPage,100);
	};
	addListNew={
		init:function(){
			var that=this;
			that.dataListSearch(null,1,100);
			$('#search').click(function(){
				that.dataListSearch(states,1,100);
			});
			$('.sel-option').on('click',function(){
				var v = $(this).attr('v');
				if(v == ''){
					v = null;
				}else{
					v = Number(v);
				}
				states = v;
				that.dataListSearch(v,1,100);
			});
			$('.sel-val').val('一个月内');
		},
		dataList:function(state,page,num){
			var that=this,html=[],userId='',flag=false,projectTitle=$("#projectTitle").val();
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.addList,
				data:{projectTitle:projectTitle,state:state,type:1,page:page,pageSize:num,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.dataList);
					}else{ 
						if(result.obj == null){
							html.push('<div class="listno yh">抱歉， 该列表暂时没有相关捐款人列表</div>');
							p.pageInit({pageLen:0,isShow:false}); 
						}else{
							var data=result.obj,data2=result.obj1,total=data2.total,pageNum=data2.pageSize,datas=data,len=datas.length;
							len=len>pageNum?pageNum:len; 
							if(total>0){
								for(var i=0;i<len;i++){
								    var titleurl=dataUrl.addUrl+"?id="+datas[i].projectId;
									html.push('<ul>');
									html.push('<li class="lst-col1">'+(new Date(datas[i].donatTime)).pattern("yyyy-MM-dd")+'</li>');
									if(datas[i].nickName.indexOf("游客") !=-1){
										html.push('<li class="lst-col2">爱心人士</li>');
									}else{
										html.push('<li class="lst-col2" title="'+datas[i].nickName+'">'+$.ellipsis(datas[i].nickName,5,'..')+'</li>');
									}
									if(datas[i].field =='garden'){
										html.push('<li class="lst-col3"><a href="http://www.17xs.org/project/newGardenView/?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}else if(datas[i].field =='good'){
										html.push('<li class="lst-col3"><a href="http://sy.17xs.org/project/syview.do?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}else{
										html.push('<li class="lst-col3"><a href="http://www.17xs.org/project/view/?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}
									if(datas[i].recipientName == null){
										html.push('<li class="lst-col4">善园基金会</li>');
									}else{
										html.push('<li class="lst-col4">'+datas[i].recipientName+'</li>');
									}
									html.push('<li class="lst-col5 orangeText">'+$.formatNum(datas[i].donatAmount)+'</li>');
									html.push('</ul>');
								}
							}else{
								html.push('<div class="listno yh">抱歉， 该列表暂时没有相关捐款人列表</div>');
							}	
							if(data2.pages>1&&flag){
								p.pageInit({pageLen:data2.pages,isShow:true}); 
							}else if(data2.pages<2){
								p.pageInit({pageLen:data2.pages,isShow:false}); 
							} 
						}
						$('#dtList').html(html.join(''));			
					}
				},
				error: function(r){ 
					d.alert({content:'获取捐款人列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		},
		dataListSearch:function(state,page,num){
			var that=this,html=[],userId='',flag=false,projectTitle=$("#projectTitle").val();
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.addListSearch,
				data:{projectTitle:projectTitle,state:state,type:1,page:page,pageSize:num,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.dataList);
					}else{ 
						if(result.obj == null){
							html.push('<div class="listno yh">抱歉， 该列表暂时没有相关捐款人列表</div>');
							p.pageInit({pageLen:0,isShow:false}); 
						}else{
							var data=result.obj,total=data.total,pageNum=data.pageSize,datas=data.resultData,len=datas.length;
							len=len>pageNum?pageNum:len; 
							/*if(data.total>0){*/
							if(len>0){
								for(var i=0;i<len;i++){
								    var titleurl=dataUrl.addUrl+"?id="+datas[i].projectId;
									html.push('<ul>');
									html.push('<li class="lst-col1">'+(new Date(datas[i].donatTime)).pattern("yyyy-MM-dd")+'</li>');
									if(datas[i].nickName.indexOf("游客") !=-1){
										html.push('<li class="lst-col2">爱心人士</li>');
									}else{
										html.push('<li class="lst-col2" title="'+datas[i].nickName+'">'+$.ellipsis(datas[i].nickName,5,'..')+'</li>');
									}
									if(datas[i].field =='garden'){
										html.push('<li class="lst-col3"><a href="http://www.17xs.org/project/newGardenView/?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}else if(datas[i].field =='good'){
										html.push('<li class="lst-col3"><a href="http://sy.17xs.org/project/syview.do?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}else{
										html.push('<li class="lst-col3"><a href="http://www.17xs.org/project/view/?projectId='+datas[i].projectId+'">'+datas[i].projectTitle+'</a></li>');
									}
									if(datas[i].recipientName == null){
										html.push('<li class="lst-col4">善园基金会</li>');
									}else{
										html.push('<li class="lst-col4">'+datas[i].recipientName+'</li>');
									}
									html.push('<li class="lst-col5 orangeText">'+$.formatNum(datas[i].donatAmount)+'</li>');
									html.push('</ul>');
								}
							}else{
								html.push('<div class="listno yh">抱歉， 该列表暂时没有相关捐款人列表</div>');
							}
							if(data.pages>1 && flag){
								p.pageInit({pageLen:data.pages,isShow:true}); 
							}else if(data.pages<2 && flag){
								p.pageInit({pageLen:1,isShow:false}); 
							} 
							$('#dtList').html(html.join(''));	
						}
					}
				},
				error: function(r){ 
					d.alert({content:'获取捐款人列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	addListNew.init();
});