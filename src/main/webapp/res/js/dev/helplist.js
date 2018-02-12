var base=window.baseurl;
var dataUrl={
	helpList:base+'/project/appeallist.do',
	getItem:base+'/project/spview.do',
	helpDel:'/project/delproject.do',
	helpSubmit:'/project/submit.do',
	helpReason:'/project/pcheck.do',
	detailPrefix:base+'project/view.do?projectId=',
	gardenPrefix:base+'project/gardenview.do?projectId='
};
require.config({
	baseUrl:base+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"  : "dev/common/util", 
		"dialog"  : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry" ,
		"userCenter"  : "dev/common/userCenter" ,
		"pages"   :"dev/common/pages"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages"],function($,d,h,en,uc,p){
	window.sitePage="p-seekHelp";
	window.mCENnavEtSite="m-qz";//如需导航显示当前页则加此设置
	d.init();
	h.init();
	en.init(); 
	uc.init(); 
	p.init({afterObj:$("#helplist")});
	p.changeCallback=function(){
		helpList.listshow(p.curPage,10);
	};
	
	var helpList={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			that.listshow(1,10);
			$('.hPreview').live('click',function(){
				var ul=$(this).parents('ul'),title=ul.attr('title'),id=ul.attr('id');
				that.viewItem(title,id);
			})
			$('.hDel').live('click',function(){
				var ul=$(this).parents('ul'),title=ul.attr('title'),id=ul.attr('id');
				that.helpDel(id,this);
			})
			$('.hSubmit').live('click',function(){
				var ul=$(this).parents('ul'),title=ul.attr('title'),id=ul.attr('id');
				that.helpSubmit(id);
			})
			$('.hReason').live('click',function(){
				var ul=$(this).parents('ul'),title=ul.attr('title'),id=ul.attr('id');
				that.hReason(id);
			})
			
			$("body").on("click","#updateBtn",function(){
				var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			})
			
		},
		formatDate:function(now){  
			var mouth,dtae,hour,minute,second;
              var   year=now.getFullYear();     
              now.getMonth()+1<10? month='0'+(now.getMonth()+1): month=now.getMonth()+1;     
              now.getDate()<10? date='0'+now.getDate(): date=now.getDate();     
              now.getHours()<10? hour='0'+now.getHours():  hour=now.getHours();     
              now.getMinutes()<10? minute='0'+now.getMinutes(): minute=now.getMinutes();     
              now.getSeconds()<10? second='0'+now.getSeconds(): second=now.getSeconds();     
              return   year+"-"+month+"-"+date+" &nbsp;  "+hour+":"+minute+":"+second;     
        }, 
		listshow:function(page,pageNum,t){
			var that=this,oldtotle=t,statData={'200':'草稿','210':'待初审','220':'待复审','230':'审核未通过','240':'募捐中','250':'执行中','260':'结束'},flag=false;
			if(page<2){flag=true};
			$.ajax({
					url:dataUrl.helpList,
					type: 'POST',
					data:{page:page,pageNum:pageNum,t:new Date().getTime()},
					success: function(result){ 
					//alert(result.obj.data.length);
					//console.log(result.obj.data.length);
						if(result.flag==1){
							var data=result.obj,listdata=data.data,html=[],bgcolor,url="";
							that.totle=data.total;that.pageCurrent=data.page;
							for(var i=0;i<listdata.length;i++){
								bgcolor=(i%2)?"":'bgcolor1';
								html.push('<ul class="'+bgcolor+'" title="'+listdata[i].title+'" id="'+listdata[i].id+'">');
								html.push('<li class="lst-col1">&nbsp;'+listdata[i].title+'</li>');
								html.push('<li class="lst-col3">'+statData[listdata[i].status]+'</li>');
								html.push('<li class="lst-col4">'+that.formatDate(new Date(listdata[i].uTime))+'</li>');
								listdata[i].field=="garden"?url=dataUrl.gardenPrefix+listdata[i].id:url=dataUrl.detailPrefix+listdata[i].id;
								if(listdata[i].status==200){
								html.push('<li class="lst-col5"><a title="" href="'+url+'">预览</a>|<a href="'+base+'ucenter/pedit.do?projectId='+listdata[i].id+'" title="">编辑</a>|<a class="hSubmit" title="">提交</a>|<a class="hDel" title="">删除</a></li>');
								}else if(listdata[i].status==202){
									html.push('<li class="lst-col5"><a title="" href="'+url+'">原因</a>|<a href="'+base+'ucenter/pedit.do?projectId='+listdata[i].id+'" title="">编辑</a>|<a class="hSubmit" title="">提交</a>|<a class="hDel" title="">删除</a></li>');
								}else if(listdata[i].status==230){
									html.push('<li class="lst-col5"><a title="" href="'+url+'">预览</a>|<a title="" class="hReason">原因</a>');
								}else{
									html.push('<li class="lst-col5"><a title="" href="'+url+'">预览</a></li>');
								}
								html.push('</ul>');
							}
							if(oldtotle){
								if(oldtotle>data.total){
									p.init({afterObj:$("#helplist")});
									p.curPage>data.total?p.curPage=data.total:'';
									p.pageInit({pageLen:data.total,isShow:true,pageChange:p.curPage}); 
								}
							}
							$('#helplist').html(html.join(''));
							if(data.total>1&&flag){
								p.pageInit({pageLen:data.total,isShow:true}); 
							}else if(data.total<2){
								p.pageInit({pageLen:data.total,isShow:false}); 
							} 
							
						}else if(result.flag==-1){
							en.show(helpList.listshow);
						}else if(result.flag==2){
							$('#helplist').html('<div class="listno yh">您还未发起任何求助项目 <a href="'+base+'ucenter/pcreate.do" title="">发起求助</a></div> ');
						}
						else{
							d.alert({content:result.errorMsg,type:'error'});
							return false;
						}
					},
					error:function(){
						
					}
				});
		},
		viewItem:function(title,id){
			var title=title?title:'预览';
			$.ajax({
				url:dataUrl.getItem,
				type: 'POST',
				data:{pid:id,t:new Date()},
				success: function(result){ 
					if(result.flag==1){
						var idata=result.obj,html=[];
						html.push('<div class="tit-group">');
						html.push('<label>标题:</label><input id="iTit" type="txt" value="'+idata.title+'"/>');
						html.push('</div>');
						html.push('<div class="con-group">');
						html.push('<label id="iCon" class="txtVT">内容:</label>');
						html.push('<textarea id="txtCon">'+idata.content+'</textarea>');
						html.push('</div>');
						html.push('<div class="img-group">');
						html.push('<label>图片:</label>');
						html.push('<ul id="imgList" class="imgList">');
						 
						var imgList=idata.imgs,imgLen=imgList.length;
						for(var i=0;i<imgLen;i++){ 
							html.push('<li><img src="'+imgList[i]+'" /></li>'); 
						} 
						html.push('</ul>');
						html.push('</div>');   
						d.open({t:title,c:html.join('')},{},"itemDlg");	
						
					}else if(result.flag==-1){
						en.show(helpList.viewItem);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
					if(!!result){
						//var idata= eval("("+result+")");
					}else{
						d.alert({content:'您的操作过于频繁，请……'});
					}
				}
			}); 
		},
		helpDel:function(id,obj){
			var that=this;
			$.ajax({
				url:dataUrl.helpDel,
				data:{pid:id},
				type: 'POST',
				success: function(result){ 
					if(result.flag==1){
						$(obj).parents('ul').remove();
						helpList.listshow(p.curPage,10,that.totle);
						//that.listshow(that.pageCurrent,10);
						return false;
					}else if(result.flag==-1){
						en.show(helpList.helpDel);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
		},
		helpSubmit:function(id){
			var that=this;
			$.ajax({
				url:dataUrl.helpSubmit,
				data:{pid:id},
				type: 'POST',
				success: function(result){ 
					if(result.flag==1){
						that.listshow(that.pageCurrent,10);
						return false;
					}else if(result.flag==-1){
						en.show(that.helpSubmit);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
		},
		hReason:function(id){
			var that=this;
			$.ajax({
				url:dataUrl.helpReason,
				data:{pid:id},
				type: 'POST',
				success: function(result){ 
					if(result.flag==1){
						var idata=result.obj,html=[];
						html.push('<div class="tit-group tit-Guide yh">');
						html.push('抱歉，您的求助项目由于以下原因未通过审核。');
						html.push('</div>');
						html.push('<div class="con-group">');
						html.push('<textarea id="txtCon">'+idata.comment+'</textarea>');
						html.push('</div>');  
						html.push('<div class="tit-group">');
						html.push('<span class="time">时间：'+that.formatDate(new Date(idata.time))+'</span>');
						html.push('</div>');
						html.push('<a  id="updateBtn" class="dlg-btn">关闭</a> ');  
						d.open({t:'未通过审核原因',c:html.join('')},{},"itemDlga");	
						return false;
					}else if(result.flag==-1){
						en.show(that.helpSubmit);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
		}
	
	}
	helpList.init();
});