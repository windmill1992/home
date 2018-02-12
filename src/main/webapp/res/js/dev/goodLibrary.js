var base=window.baseurl;
var dataUrl={
	dataList:base+'goodlibrary/goodLibraryList.do',//列表
	

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
	window.sitePage="p-donateRecord";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#libraryList")});
	p.changeCallback=function(){
		var date = $("#currentDate").val();
		list.dataList(p.curPage,10,date);
	};
	list={
		init:function(){
			var that=this;
			that.dataList(1,10,"2016");
			$("#2016").click(function(){
				$("#2016").removeClass("year_a2");
		        $("#2016").addClass("year_a1");
		        $("#2015").removeClass("year_a1");
		        $("#2015").addClass("year_a2");
		        $("#currentDate").val("2016")
				that.dataList(1,10,"2016");
			});
			$("#2015").click(function(){
				$("#2015").removeClass("year_a2");
		        $("#2015").addClass("year_a1");
		        $("#2016").removeClass("year_a1");
		        $("#2016").addClass("year_a2");
		        $("#currentDate").val("2015")
				that.dataList(1,10,"2015");
			});
			$(function () {
				var $ul = $(".mainNew .library_list>ul");
				var $li = $ul.children("li");
				//获取li的宽度(10是margin-left的值)
				var width = $li.width() + 15;
				//设置当前的图片下标
				var index = 0;

				//向前滚动
				$(".mainNew .prev_fl").click(function () {
					if (index <= 0) {
						return;
					}
					index = index - 4;

					//停止动画，防止抽风式的点击按钮
					$ul.stop(false, true);
					//设置ul的left值
					var left = $ul.position().left + width * 4;
					$ul.animate({left: left}, 500);

				});

				//向后滚动
				$(".mainNew .next_fr").click(function () {
					//alert($li.length);
					if (index + 4 >= $li.length) {
						//如果到了最后一张，就不再执行了
						return;
					}
					index = index + 4;
					//停止动画，防止抽风式的点击按钮
					$ul.stop(false, true);
					//设置$ul的left值
					var left = $ul.position().left - width*4;
					$ul.animate({left: left}, 500);
				});
			});
		},
		dataList:function(page,num,date){
			var that=this,html=[],userId=$("#userId").val(),flag=false;
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.dataList,
				data:{page:page,pageNum:num,userId:userId,donateTime:date},
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
							    if(datas[i].field == 'garden'){
							    	var projectUrl = 'http://www.17xs.org/project/newGardenView/?projectId='+datas[i].projectId;
							    }else{
							    	var projectUrl = 'http://www.17xs.org/project/view/?projectId='+datas[i].projectId;
							    }
								html.push('<div class="time_one"><i class="library_icon1 "></i>');
								html.push('<p class="time_fl fl">'+(new Date(datas[i].donatTime)).pattern("MM月dd日")+'</p>');
								html.push('<div class="time_fr fl"><span class="time_icon1"></span>');
								html.push('<div class="time_two"><div class="time_top"><h3>捐助项目：<a href='+projectUrl+'>'+datas[i].projectTitle+'</a></h3>');
								html.push('<span class="library_money">'+datas[i].donatAmount+'元</span></div>');
								if(datas[i].familyAddress == null){
									html.push('<div class="time_top"><p class="library_ren">受助人：<span>'+datas[i].realName+'</span></p>');
								}else{
									html.push('<div class="time_top"><p class="library_ren">受助人：<span>'+datas[i].realName+'&nbsp;&nbsp;'+datas[i].familyAddress+'</span></p>');
								}
								if(userId != datas[i].userId){
									html.push('<span class="library_money1">'+'“'+datas[i].nickName+'”代捐'+'</span>');
								}
								if(datas[i].province == null){
									html.push('<div class="library_bq">');
									for(var j=0;j<datas[i].tagList.length;j++){
										html.push('<li>'+datas[i].tagList[j]+'</li>');
									}
									html.push('</div></div>');
								}else{
									html.push('<div class="library_bq"><li>'+datas[i].province+'</li>');
									for(var j=0;j<datas[i].tagList.length;j++){
										html.push('<li>'+datas[i].tagList[j]+'</li>');
									}
									html.push('</div></div>');
								}
								html.push('</div></div></div>');
							}
						}else{
							html.push('<div class="listno yh">抱歉， 该列表暂时没有行善列表</div>');
						}
						$('#libraryList').html(html.join(''));			
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
					}
				},
				error   : function(r){ 
					d.alert({content:'获取行善列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	list.init();
});