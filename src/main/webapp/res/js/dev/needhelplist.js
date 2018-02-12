var base=window.baseurl;
var dataUrl={
	dataList:base+'project/appeallist.do',//我的求助列表
	stopSubmit:base+'ucenter/coredata/endItem.do',//停止募捐
	addfeedback:base+'project/addfeedback.do',//项目反馈
	delproject:base+'project/delproject.do',//删除项目
	delImgs:base+'file/image/delete.do' ,//删除图片
	projectUrl:base+'project/view.do',
	pfresult:base+'project/pcheck.do',//原因
	tixianUrl:base+'ucenter/core/personalcapital.do',
	addProject:base+'project/appealSecond.do',
	Volunteer:base+'ucenter/ProjectVolunteerView.do', //志愿者
	CryPeople:base+'ucenter/ProjectCryPeopleView.do' //求助者
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
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	if($('#flag').val()=='withdraw'){
		window.mCENnavEtSite="m-widthDraw";
	}else{
		if($("#flag").val()=="realtionProject"){
			window.mCENnavEtSite="realtionProject";
		}else{
			window.mCENnavEtSite="m-qz";
		}
		
	}
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		needhelplist.dataList(p.curPage,10);
	};
	needhelplist={
		init:function(){
			var that=this;
			that.dataList(1,10);
			$("body").on("click",".showStop",function(){
					var data=$(this).parents('ul').attr('data').split('|');
					data?that.showStopDialog(data[0],data[1]):'';
				}).on("click","#showStopSubmit",function(){ 
					var id=$(this).attr('data'),con=$('#stopCon').val(),di=$(this).parents(".cp2yDialogBox").attr("data");
					if(!con){ d.alert({content:'内容不能为空！',type:'error'});return  false;};
					if(con.length>1000){ d.alert({content:'内容不能超过1000个字！',type:'error'});return  false;};
					con&&id ?that.showStopSubmit(id,con,di):'';
				}).on("click",".showSituation",function(){//项目反馈
					var data=$(this).parents('ul').attr('data').split('|');
					data?that.showSituation(data[0],data[1]):'';	
				}).on("click","#situSubmit",function(){
					var id=$(this).attr('data')
					that.situSubmit(id);
				}).on("mouseover",".imgList li",function(){
					$(this).children('i').show(); 
				}).on("mouseout",".imgList li",function(){
					$(this).children('i').hide(); 
				}).on("click",".showReason",function(){
					var data=$(this).parents('ul').attr('data').split('|');
					if(data){
						that.hReason(data[0],data[1]);
					}
				}).on("click",".imgList img",function(){
					var parent=$(this).closest("li"); 
				 	parent.children("i").toggle();
				}).on("click",".imgList i",function(){
					var parent=$(this).closest("li");
					that.removeImg(parent); 
					if($('.imgList').children('li').not('.add').length<10){
						$('.imgList .add').show();
					}
				}).on("change","#situFile",function(){
					var format=that.photoFormat(this);  
					if(format){
						var imgBox=$("#situList"),addObj=imgBox.children("li.add"),src=base+'/res/images/common/bar1.gif';
						addObj.hide();
						addObj.before('<li class="old"><i>×</i><img src="'+src+'"></li>'); 
						$('#situForm').submit();
					}
					return false;
				}).on("click","#closeBtn",function(){
					var di=$(this).parents(".cp2yDialogBox").attr("data");
					d.close(di);
				}).on("click",".showDel",function(){
					var data=$(this).parents('ul').attr('data').split('|');
					data?that.delproject(data[0]):'';
				});
				//搜索
				$('#search').click(function(){
					that.dataList(1,10);
				});
				//筛选 状态，200：保存，210：审核中，220：待发布，230：审核未通过，240：募捐中，260：结束
				$(".state1").click(function(){
					$('#state').val(-1);
					that.dataList(1,10);
				});
				$(".state2").click(function(){
					$('#state').val(200);
					that.dataList(1,10);
				});
				$(".state3").click(function(){
					$('#state').val(210);
					that.dataList(1,10);
				});
				$(".state4").click(function(){
					$('#state').val(220);
					that.dataList(1,10);
				});
				$(".state5").click(function(){
					$('#state').val(230);
					that.dataList(1,10);
				});
				$(".state6").click(function(){
					$('#state').val(240);
					that.dataList(1,10);
				});
				$(".state7").click(function(){
					$('#state').val(260);
					that.dataList(1,10);
				});
		},
		photoFormat:function(file){ 
				var filepath =$(file).val(); 
				var pos = filepath.lastIndexOf("."),imgType = filepath.substring(pos+1,filepath.length).toUpperCase();; 
				var isImg=imgType.toLowerCase().search(/[('jpg'|'png'|'jpeg'|'gif'|'bmp')]/);
				if(isImg==-1){
					d.alert({content:"上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！",type:'error'});
					return false;
				} 
				
				var file_size = 0;
				  if (!$.browser.msie) {
						file_size = file.files[0].size;
						var size = file_size / 1024;
						if (size > 2048) {
							d.alert({content:"上传的图片大小不能超过2M！",type:'error'});
							return false;
						} 
					}
				return true;
			},
			ajaxForm:function(obj,type,imgBox){
				var that=this; 
				//return that.imgShow(imgBox,''http://www.17xs.org/res/images/item/show1.jpg','imgId1');
				$(obj).ajaxForm({
					beforeSend: function() {status.empty();},
					uploadProgress: function(event, position, total, percentComplete) {
					},
					success: function(data) {
						data = data.replace("<PRE>", "").replace("</PRE>", "");
						var json = eval('(' + data + ')'); 
						var imgBox=$('.imgList'),addObj=imgBox.children("li.add");
						addObj.show();
						if(json.result==1){
							imgBox.children('.old').remove();
							d.alert({content:json.error,type:'error'});
							return false;
						}else{
							that.imgShow(imgBox,json.imageUrl,json.imageId);   
						}
					},
					error: function(data) {
						var imgBox=$('.imgList'),addObj=imgBox.children("li.add");
						addObj.show();
						imgBox.children('.old').remove();
						d.alert({content:data.responseText,type:'error'}); 
					}
				}); 
		}, 
		imgShow:function(imgBox,src,id){ 
				var imgBox=$(imgBox),addObj=imgBox.children("li.add");
				if(imgBox.children().not('.add').size()>=10){
					addObj.hide();
				} 
				imgBox.children('.old').attr('id',id).children('img').attr('src',src).end();
				imgBox.children('.old').removeClass('old');
				//addObj.before('<li id="'+id+'"><i class="close-icon"></i><img src="'+src+'"></li>');
				
		},
		delImgList:[],
			removeImg:function(li){
				var imgId=$(li).attr("id");
				this.delImgList.push(imgId);
				li.remove();
				var list=$("#imgList").children();
				 
				if(!list.filter(".add").is(":visible")){//list.length<11
					list.filter(".add").show(); 
				}
			},
			delectImgs:function(){
				var that=myGroup,itemId=that.updateData.itemId,images=that.delImgList;
				if(images.length<=0){return;}
				$.ajax({
					url:dataUrl.delImgs,
					type: 'POST',
					data:{itemId:itemId,images:images.join(',')}, 
					success: function(result){
						if(!!result){
							var result=eval(result);
							if(result.flag==1){
								d.alert({content:result.errorMsg,type:'ok'});
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(myGroup.checkItem);
							}
							return d.close('checkItemDlg');
						}  
						
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					} 
				});
			},
		hReason:function(id,tle){
			var that=this,html=[];
			$.ajax({
				url     : dataUrl.pfresult,
				data:{pid:id,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.hReason);
					}else{
						var obj=result.obj;
						html.push('<div class="tit-group tit-Guide yh">');
						html.push('抱歉，您发起的'+$.ellipsis(tle,15,'...')+'由于以下原因未审核通过。');
						html.push('</div>');
						html.push('<div class="con-group">');
						html.push('<textarea id="txtCon">'+(obj.comment?obj.comment:'')+'</textarea>');
						html.push('</div>');  
						html.push('<div class="tit-group">');
						html.push('</div>');
						html.push('<a  id="closeBtn" class="dlg-btn">关闭</a> ');  
						d.open({t:tle+'原因',c:html.join('')},{},"itemDlga");
					}
				},
				error   : function(r){ 
					d.alert({content:'获取审核失败原因，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});

			

		},
		showStopDialog:function(id,tle){
			var that=this,html=[],txt='';
			html.push('<div class="con-group">');
			html.push('<textarea id="stopCon"></textarea>');
			html.push('</div>');  
			html.push('<div class="tit-group">');
			html.push('</div>');
			html.push('<a  id="showStopSubmit" data="'+id+'" class="dlg-btn">保存并提交</a> ');  
			d.open({t:tle+'的关闭报告',c:html.join('')},{},"itemDlga");
		},
		showStopSubmit:function(id,con,di){
			var that=this;
			$.ajax({
				url     : dataUrl.stopSubmit,
				data:{itemId:id,reason:con,t:new Date().getTime()},
				type: 'POST',
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						 that.dataList(that.pageCurrent, 10);
						 d.close(di);
					}
				},
				error   : function(r){ 
					d.alert({content:'停止募捐失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		showSituation:function(id,tel){
			var html=[];
				html.push('<div class="con-group">');
				html.push('<label class="txtVT">内容:</label>');
				html.push('<textarea id="situCon"></textarea>');
				html.push('</div>');
				html.push('<div class="img-group">');
				html.push('<label>图片:</label>');
				html.push('<form id="situForm" method="post" name="runItem" action="http://www.17xs.org/file/upload3.do" enctype="multipart/form-data">');
				html.push('<ul id="situList" class="imgList">');
				html.push('<li class="add">');
				html.push('<span>+</span>');
				html.push('<input id="situFile" type="file"  name="file"/>');
				html.push('<input type="hidden" name="type" id="type" value="5"/>');
				html.push('</li>');
				html.push('</ul> ');
				html.push('</form>');
				html.push('</div>');
				html.push('<p class="tips">注：图片大小每张请控制在2M以内，最多可上传10张图片</p>');
				html.push('<div id="situSubmit" data="'+id+'" class="dlg-btn">保存并提交</div> ');
				d.open({t:'"'+tel+'"的执行报告',c:html.join('')},{},"imgTxtDlg runItemDlg");
				this.ajaxForm($("#situForm"),1,'#situList'); 
		},
		situSubmit:function(id){
			var that=this, 
					con=$("#situCon").val(), 
					imgList=$("#situList").children().not(".add"),
					img=[],
					len=imgList.length;//$("#imgList").html();  
			   	for(var i=0;i<len;i++){
					img.push($(imgList[i]).attr("id"));
				} 
				if(con.length>1000){
					return d.alert({content:"内容不能超过1000个字！"});
				}
				$.ajax({ 
					url:dataUrl.addfeedback,
					type: 'POST',
					data:{pid:id,content:con,imgIds:img.join(","),t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(!!result){
							var result= eval(result); 
							if(result.flag==1){
								 d.alert({content:result.errorMsg,type:'ok'});
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(that.situSubmit);
							}
							return d.close('runItemDlg');
						}  
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				}); 	
		},
		delproject:function(id){
			var that=this;
			$.ajax({ 
					url:dataUrl.delproject,
					type: 'POST',
					data:{pid:id,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
							 d.alert({content:result.errorMsg,type:'ok'});
							 that.dataList(1,10);
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.delproject);
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});
		},
		removeImg:function(li){
				var imgId=$(li).attr("id");
				this.delImgList.push(imgId);
				li.remove();
				var list=$("#imgList").children();
				 
				if(!list.filter(".add").is(":visible")){//list.length<11
					list.filter(".add").show(); 
				}
		},
		delectImgs:function(){
				var that=myGroup,itemId=that.updateData.itemId,images=that.delImgList;
				if(images.length<=0){return;}
				$.ajax({
					url:dataUrl.delImgs,
					type: 'POST',
					data:{itemId:itemId,images:images.join(',')}, 
					success: function(result){
						if(!!result){
							var result=eval(result);
							if(result.flag==1){
								d.alert({content:result.errorMsg,type:'ok'});
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(myGroup.checkItem);
							}
							return d.close('checkItemDlg');
						}  
						
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					} 
				});
		},
		dataList:function(page,num){
			var that=this,html=[],statData={'200':'草稿','210':'审核中','220':'审核通过待发布','230':'审核未通过','240':'募捐中','260':'已结束'},flag=false,projectTitle=$('#projectTitle').val(),state=$('#state').val();
			if(page<2){flag=true};
			/*html.push('<dt><ul>');
			html.push('<li class="col1">项目标题</li>');
			html.push('<li class="col2"><p class="zt">状态</p><span class="ztIcon"></span>');
			html.push('<ul class="col2_ul" style="display:none">'+
					'<a href="javaScript:vide(0);"><li class="col2_li state1">全部</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(200)"><li class="col2_li">草稿</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(210)"><li class="col2_li">审核中</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(220)"><li class="col2_li">待发布</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(230)"><li class="col2_li">审核未通过</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(240)"><li class="col2_li">募捐中</li></a>'+
					'<a href="javaScript:vide(0);" onclick="stateClick(260)"><li class="col2_li">结束</li></a>'+
				'</ul>');
			html.push('</li>');
			html.push('<li class="col3">筹款金额</li>');
			html.push('<li class="col4">已筹集</li>');
			html.push('<li class="col5">可提现</li>');
			html.push('<li class="col6">操作</li>');
			html.push('</ul></dt>');*/
			if(state==-1){
				state=null;
			}
			$.ajax({
				url:dataUrl.dataList,
				data:{page:page,pageNum:num,title:projectTitle,state:state,t:new Date()},
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
							html.push('<dd '+((i+1)%2==0?'class="color_bg"':'')+'><ul data="'+datas[i].id+'|'+datas[i].title+'">');	
							html.push('<li class="col1"><a href="'+dataUrl.projectUrl+'?projectId='+datas[i].id+'" target="_blank">'+$.ellipsis("["+datas[i].fieldChinese+"]"+datas[i].title,12,'...')+'</a><span class="time">发布时间：'+(new Date(datas[i].cTime)).pattern("yyyy-MM-dd HH:mm")+'</span></li>');
							html.push('<li class="col2">'+statData[datas[i].status]);
							if(datas[i].status==230){
							html.push('<a class="showReason">原因</a>');
							}
							html.push('</li><li class="col3">'+$.formatNum(datas[i].cryMoney)+' 元</li>');
							html.push('<li class="col4">'+$.formatNum(datas[i].donatAmount)+'元</li>');
							html.push('<li class="col5">'+$.Subtr(datas[i].donatAmount,datas[i].panyAmount)+'元</li>');
							html.push('<li class="col6">');
							if(datas[i].status==200 || datas[i].status==210 || datas[i].status==220 || datas[i].status==240){
								html.push('<a href="'+dataUrl.addProject+'?projectId='+datas[i].id+'">编辑/发布</a>');
							}
							
							if(datas[i].status==200){
								html.push('<a href="'+dataUrl.projectUrl+'?projectId='+datas[i].id+'" target="_blank" class="showPreview" data="">预览信息</a>');
								html.push('<a class="showDel">删除</a>');
							}
							else if(datas[i].status==210){
								html.push('<a href="'+dataUrl.projectUrl+'?projectId='+datas[i].id+'" target="_blank" class="showPreview" data="">预览信息</a>');
							}
							else if(datas[i].status==220){
								html.push('<a href="'+dataUrl.tixianUrl+'?projectId='+datas[i].id+'&flag=withdraw">申请提现</a><a class="showSituation">项目反馈</a><a href="'+dataUrl.tixianUrl+'">提现记录</a>');
							}else if(datas[i].status==230){
								html.push('<a href="'+dataUrl.projectUrl+'?projectId='+datas[i].id+'" class="showPreview" data="">预览信息</a><a href="'+dataUrl.addProject+'?projectId='+datas[i].id+'">编辑/发布</a><a class="showDel">删除</a>');
							}else if(datas[i].status==240){
								html.push('<a href="'+dataUrl.tixianUrl+'?projectId='+datas[i].id+'&flag=withdraw">申请提现</a><a class="showSituation">项目反馈</a><a class="showStop" >停止募捐</a><a href="'+dataUrl.tixianUrl+'">提现记录</a>');
								if(datas[i].isNeedVolunteer==1){
									html.push('<a href="'+dataUrl.Volunteer+'?projectId='+datas[i].id+'">志愿者名单</a><a href="'+dataUrl.CryPeople+'?projectId='+datas[i].id+'">求助者名单</a>');
								}
							}else{
								html.push('<a href="'+dataUrl.tixianUrl+'?projectId='+datas[i].id+'&flag=withdraw">申请提现</a><a class="showSituation">项目反馈</a><a href="'+dataUrl.tixianUrl+'">提现记录</a>');
								if(datas[i].isNeedVolunteer==1){
									html.push('<a href="'+dataUrl.Volunteer+'?projectId='+datas[i].id+'">志愿者名单</a><a href="'+dataUrl.CryPeople+'?projectId='+datas[i].id+'">求助者名单</a>');
								}
							}
							html.push('</li>');
							html.push('</ul></dd>');
							}
						}else{
							html.push('<div class="noinfo">您还未发起任何求助项目 <a href="'+base+'project/appealFirest.do'+'">发起求助</a></div>');
						}
						$('.noinfo').remove();
						$('#listShow>dd').remove();
						$('#listShow').append(html.join(''));	
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
					}
					$("dt .col2").mouseover(function(){
						$(".col2_ul").show();
					});
					$("dt .col2").mouseout(function(){
						$(".col2_ul").hide();
					});
				},
				error   : function(r){ 
					d.alert({content:'获取求助列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	needhelplist.init();
});