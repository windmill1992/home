var base=window.baseurl;
var dataUrl={
	showDetailList:base+'ucenter/coredata/callList.do' //企业助善明细
};
require.config({
	baseUrl:base+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"pages"   :"dev/common/pages",
		"pageCommon"   :"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","head","entry","userCenter","pages"],function($,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="p-donationdetail";
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		enterprise_charity.showDetail(p.curPage);
	};
	uc.selectCallback=function(){
		enterprise_charity.showDetail(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var enterprise_charity={
		time:null, 
		init:function(){	
			var that=this;
			that.showDetail(1);
		},
		showDetail:function(page){
			var that=this,html=[],time=$(".sel-val").attr('v');
			var flag=false;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			}
			if(!page){page=1;} 
			html.push('<dl>');
			html.push('<dt><span class="naidt_01">项目标题</span>');
			html.push('<span class="naidt_02">用户名</span>');
			html.push('<span class="naidt_03">助善金</span>');
			html.push('<span class="naidt_04">助善时间</span>');
			html.push('</dt>');
			$.ajax({
				url:dataUrl.showDetailList,
				data:{type:time,page:page,pageNum:9,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=result.total,pageNum=data.pageSize,datas=data,len=datas.resultData.length;
						//len=len>pageNum?pageNum:len; 
						for(var i=0;i<len;i++){
							var idata=datas.resultData[i]; 
							html.push('<dd>');
							html.push('<div class="naidd_hkp">');
							html.push('<span class="naidd_01">'+idata.projectTitle+'</span>');
							html.push(' <span class="naidd_02">'+idata.userName+'</span>');
							html.push('<span class="naidd_03">'+$.formatNum(idata.donatAmount)+'元</span>');
							html.push('<span class="naidd_04">'+(new Date(idata.donatTime)).pattern("yyyy/MM/dd HH:mm")+'</span>');
							html.push('</div>');
							html.push('</dd>');
						}  
						html.push('</dl>')
						listObj.html(html.join(''));
					
					}else if(result.flag==2){
						html.push('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
						listObj.html(html.join(''));
					}else if(result.flag==0){
						html.push('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						listObj.html(html.join(''));
					}else if(result.flag==-1){
						html.push('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						listObj.html(html.join(''));
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(detail.showDetail);
						}
					}
					
					
				}
			});
			
		}
	};	
	enterprise_charity.init(); 
	 
});