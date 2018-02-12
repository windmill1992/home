var base=window.baseurl;
var dataUrl={
	dataList:base+'project/projectDonateFollowList.do',//善款跟综列表
	projectUrl:base+'project/view.do', //项目详情页
	gardenUrl:base+'project/newGardenView.do',  //善园详情页
	itemDetail:'http://sy.17xs.org/project/syview.do', //善缘项目详情页
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
	window.mCENnavEtSite="m-acccountTracking";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		tracking.dataList(p.curPage,10);
	};
	tracking={
		init:function(){
			var that=this;
			that.dataList(1,10);
		},
		dataList:function(page,num){
			var that=this,html=[],userId='',flag=false;
			if(page<2){flag=true};
			html.push('<tr>');
			html.push('<th class="racth_01">项目标题</th>');
			html.push('<th class="racth_02">筹得善款</th>');
			html.push('<th class="racth_03">提现金额</th>');
//			html.push('<th class="racth_04">提现次数</th>');
			html.push('<th class="racth_05">最近提现</th>');
			html.push('<th class="racth_06">收款账号</th>');
			html.push('</tr>');
			$.ajax({
				url:dataUrl.dataList,
				data:{userId:userId,page:page,pageNum:num,t:new Date()},
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
							    var titleurl=dataUrl.projectUrl+"?projectId="+datas[i].projectId;
							    if(datas[i].field=="garden"){
							    	titleurl=dataUrl.gardenUrl+"?projectId="+datas[i].projectId;
								}else if(datas[i].field=="good"){
									titleurl=dataUrl.itemDetail+"?projectId="+datas[i].projectId;
								}
								html.push('<tr>');
								html.push('<td class="ractd_01"><a href="'+titleurl+'" title="" target="_blank">'+$.ellipsis(datas[i].title,14,'...')+'</a></td>');
								html.push('<td class="ractd_02"><i>'+$.formatNum(datas[i].donateAmount)+'</i>元</td>');
								html.push('<td class="ractd_03"><i>'+$.formatNum(datas[i].withdrawAmount)+'</i>元</td>');
//								html.push('<td class="ractd_04">'+datas[i].withdrawNum+'次</td>');
								if(datas[i].time==null){
									html.push('<td class="ctd_05">无</td>');
								}else{
									html.push('<td class="ctd_05">'+(new Date(datas[i].time)).pattern("yyyy-MM-dd")+'<br>'+(new Date(datas[i].time)).pattern("HH:mm:ss")+'</td>');
								}
								html.push('<td class="ractd_06">开户名：'+datas[i].realName+'<br />开户行：'+datas[i].recipientName+'<br />卡号：'+datas[i].account+'</td>');
								html.push('</tr>');
							}
						}else{
							html.push('<tr><td colspan="6" class="noinfo">您还任何善款跟踪记录</td></tr>');
						}
						$('#listShow').html(html.join(''));			
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
					}
				},
				error   : function(r){ 
					d.alert({content:'获取善款跟踪记录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	tracking.init();
});