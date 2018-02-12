/**
 * Created by zhenghl on 2016/12/16.
 */
var base=window.baseurl;
var dataUrl={
		loadWeekDonate:base+'data/loadWeekDonate.do',
		loadYearDonate:base+'data/loadYearDonate.do',
		loadCompany:base+'data/loadCompany.do'
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
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	window.sitePage="p-donateRecord";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			this.loadWeekDonate();
			this.loadYearDonate();
			this.loadCompany();
			
		},
		//加载上周、上月排行
		loadWeekDonate:function(){
			$.ajax({
				url:dataUrl.loadWeekDonate,
				data:{},
				success: function(result){ 
					if(result.flag==1){
						var htmlWeek=[],htmlMonth=[];
						dataWeek=result.obj;
						dataMonth=result.obj1;
						for(var i=0;i<dataWeek.length;i++){
							htmlWeek.push('<dd>');
							htmlWeek.push('<div class="ddFl">');
							if(i==0){
								htmlWeek.push('<b class="ddB1"></b>');
							}
							else if(i==1){
								htmlWeek.push('<b class="ddB2"></b>');
							}
							else if(i==2){
								htmlWeek.push('<b class="ddB3"></b>');
							}
							else{
								htmlWeek.push('<b>'+(Number(i)+1)+'</b>');
							}
							if(dataWeek[i].coverImageurl==''||dataWeek[i].coverImageurl==null){
								htmlWeek.push('<img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" />');
							}
							else{
								htmlWeek.push('<img src="'+dataWeek[i].coverImageurl+'" />');
							}
							htmlWeek.push('</div>');
							htmlWeek.push('<div class="ddFr">');
							htmlWeek.push('<p>'+dataWeek[i].nickName+'</p>');
							htmlWeek.push('<p class="ddp1">捐<span>'+dataWeek[i].donatAmount+'元</span></p>');
							htmlWeek.push('</div>');
							htmlWeek.push('</dd>')
						}
						for(var i=0;i<dataMonth.length;i++){
							htmlMonth.push('<dd>');
							htmlMonth.push('<div class="ddFl">');
							if(i==0){
								htmlMonth.push('<b class="ddB1"></b>');
							}
							else if(i==1){
								htmlMonth.push('<b class="ddB2"></b>');
							}
							else if(i==2){
								htmlMonth.push('<b class="ddB3"></b>');
							}
							else{
								htmlMonth.push('<b>'+(Number(i)+1)+'</b>');
							}
							if(dataMonth[i].coverImageurl==''||dataMonth[i].coverImageurl==null){
								htmlMonth.push('<img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" />');
							}
							else{
								htmlMonth.push('<img src="'+dataMonth[i].coverImageurl+'" />');
							}
							htmlMonth.push('</div>');
							htmlMonth.push('<div class="ddFr">');
							htmlMonth.push('<p>'+dataMonth[i].nickName+'</p>');
							htmlMonth.push('<p class="ddp1">捐<span>'+dataMonth[i].donatAmount+'元</span></p>');
							htmlMonth.push('</div>');
							htmlMonth.push('</dd>')
						}
						$("#weeked").append(htmlWeek.join(""));
						$("#monthed").append(htmlMonth.join(""));
					}
				}
			}); 
		},
		//加载本年、全部排行
		loadYearDonate:function(){
			$.ajax({
				url:dataUrl.loadYearDonate,
				data:{},
				success: function(result){ 
					if(result.flag==1){
						var htmlyear=[],htmltotal=[];
						dataYear=result.obj;
						dataTotal=result.obj1;
						for(var i=0;i<dataYear.length;i++){
							htmlyear.push('<dd>');
							htmlyear.push('<div class="ddFl">');
							if(i==0){
								htmlyear.push('<b class="ddB1"></b>');
							}
							else if(i==1){
								htmlyear.push('<b class="ddB2"></b>');
							}
							else if(i==2){
								htmlyear.push('<b class="ddB3"></b>');
							}
							else{
								htmlyear.push('<b>'+(Number(i)+1)+'</b>');
							}
							if(dataYear[i].coverImageurl==''||dataYear[i].coverImageurl==null){
								htmlyear.push('<img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" />');
							}
							else{
								htmlyear.push('<img src="'+dataYear[i].coverImageurl+'" />');
							}
							htmlyear.push('</div>');
							htmlyear.push('<div class="ddFr">');
							htmlyear.push('<p>'+dataYear[i].nickName+'</p>');
							htmlyear.push('<p class="ddp1">捐<span>'+dataYear[i].donatAmount+'元</span></p>');
							htmlyear.push('</div>');
							htmlyear.push('</dd>')
						}
						for(var i=0;i<dataTotal.length;i++){
							htmltotal.push('<dd>');
							htmltotal.push('<div class="ddFl">');
							if(i==0){
								htmltotal.push('<b class="ddB1"></b>');
							}
							else if(i==1){
								htmltotal.push('<b class="ddB2"></b>');
							}
							else if(i==2){
								htmltotal.push('<b class="ddB3"></b>');
							}
							else{
								htmltotal.push('<b>'+(Number(i)+1)+'</b>');
							}
							if(dataTotal[i].coverImageurl==''||dataTotal[i].coverImageurl==null){
								htmltotal.push('<img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" />');
							}
							else{
								htmltotal.push('<img src="'+dataTotal[i].coverImageurl+'" />');
							}
							htmltotal.push('</div>');
							htmltotal.push('<div class="ddFr">');
							htmltotal.push('<p>'+dataTotal[i].nickName+'</p>');
							htmltotal.push('<p class="ddp1">捐<span>'+dataTotal[i].donatAmount+'元</span></p>');
							htmltotal.push('</div>');
							htmltotal.push('</dd>')
						}
						$("#yeared").append(htmlyear.join(""));
						$("#total").append(htmltotal.join(""));
					}
				}
			}); 
		},
		//加载爱心企业、善管家
		loadCompany:function(){
			$.ajax({
				url:dataUrl.loadCompany,
				data:{},
				success: function(result){ 
					if(result.flag==1){
						var htmlCompany=[],htmlgoodpeople=[];
						dataCompany=result.obj;
						dataGoogpeople=result.obj1;
						for(var i=0;i<dataCompany.length;i++){
							htmlCompany.push('<a href="http://www.17xs.org/goodlibrary/getGoodLibraryView.do?lirbraryId='+dataCompany[i].id+'"><li><img src="'+dataCompany[i].logoUrl+'"></li></a>');
							
						}
						for(var i=0;i<dataGoogpeople.length;i++){
							htmlgoodpeople.push('<a href="http://www.17xs.org/project/index/?keyWords='+dataGoogpeople[i].userName+'"><li><img src="'+dataGoogpeople[i].coverImageUrl+'"></li></a>');
							
						}
						htmlCompany.push('<div style="clear:both;"></div>');
						htmlgoodpeople.push('<div style="clear:both;"></div>');
						$("#goodlibrary").html(htmlCompany.join(""));
						$("#goodPeople").html(htmlgoodpeople.join(""));
					}
				}
			}); 
		}
	}
	reg.init();
})