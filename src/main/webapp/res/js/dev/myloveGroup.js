var base=window.baseurl;

var dataUrl={
	groupList:base+'ucenter/coredata/myloveGroupData.do', 
	viewItem:base+'project/view.do?projectId=',
	getItem:base+'ucenter/coredata/viewItem.do',
	updateItem:base+'ucenter/coredata/updatemyloveGroupData.do',
	stopItem:base+'ucenter/coredata/endItem.do',
	checkItem:base+'ucenter/coredata/checkReport.do',
	runReport:base+'/project/addpreport.do',
	delImgs:base+'file/image/delete.do' ,
	ColumnChange:base+"ucenter/coredata/myloveGroupNums.do",
	helpReason:'/project/pcheck.do'
};
require.config({
	baseUrl:base+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"util"  : "dev/common/util", 
		"dialog"  : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
	 "userCenter": "dev/common/userCenter",
	   "ajaxform": "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});
 
define(["extend","dialog","head","entry","userCenter","ajaxform"],function($,d,h,en,uc,af){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-group";//如需导航显示当前页则加此设置
	d.init();
	h.init();
	en.init();   
	uc.init();
	
	myGroup().init();
	 
	function myGroup(){
		var myGroup={ 
			dataCache:[],
			curType:1,//当前类型
			init:function(){
			    var that=this;
				//====== getGrouplist ======
				$(".tab-hd").children().eq(0).addClass("curTab"); 
				$(".tab-hd").children().on("click",function(){
					$(this).addClass("curTab").siblings().removeClass("curTab");
					var type=$(this).attr("type");
					if(type!=that.curType) {
						that.curType=type;
						that.GetGroupList();
					}
				}); 
				that.GetGroupList();
				$("body").on("mouseover",".imgList li",function(){
					$(this).children('i').show(); 
				}).on("mouseout",".imgList li",function(){
					$(this).children('i').hide(); 
				})
				
				//====== show Dialog ======
				$(".tab-bd").on("click",".toCheck",function(){
					var data=$(this).closest("ul").data("item");
					that.checkDialog(data);
				}).on("click",".toEditItem",function(){ 
					//that.updateInit();
					that.delImgList=[];
					that.updateData=[];
					var data=$(this).closest("ul").data("item"); 
					that.updateData=data;
					that.updateDialog(); 
					return false;
				}).on("click",".runReport",function(){
					that.delImgList=[];
					that.runData=[];
					var data=$(this).closest("ul").data("item");
					that.runData=data;
					that.runItemDialog(); 
				}).on("click",".stopItem",function(){
					var data=$(this).closest("ul").data("item");
					that.stopDialog(data);
				}).on("click","#tabPage i,#tabPage li",function(){
					if($(this).hasClass("triangle-right")||$(this).has('i.triangle-right').size()){
						selfPage(-1);
					}else if($(this).hasClass("triangle-left")||$(this).has('i.triangle-left').size()){
						selfPage(1);
					}
					return false;
				}).on("keyup","#tabPage #selfpage",function(){
					selfPage(0); 
				});
				function selfPage(addpage){
					var obj=$("#selfpage"),page=obj.formatInt(),
						parent=obj.closest("#tabPage"),
						total=parseInt(parent.attr("total")),
						curpage=parseInt(parent.attr("cur"));
					page+=addpage;
					page=page>0?(page>total?total:page):1;
					obj.val(page);
					parent.attr("cur",page); 
					
					that.GetGroupList(page);
				}
				
				//====== dialog operation ======
				$(".rdio[name='ck']").first().addClass("rdio-cked");
				$("body").on("click","#stopBtn",function(){
					that.stopItem();
				}).on("click",".rdio",function(){
					var name=$(this).attr("name");
					$(".rdio").filter("[name="+name+"]").removeClass("rdio-cked");
					$(this).addClass("rdio-cked");
				}).on("click","#checkBtn",function(){ 
					that.checkItem(); 
				}).on("click","#updateBtn",function(){
					that.updateItem();
				}).on("click","#runReportBtn",function(){
					that.runReport();
				}).on("click",".imgList img",function(){
					var parent=$(this).closest("li"); 
				 	parent.children("i").toggle();
				}).on("click",".imgList i",function(){
					var parent=$(this).closest("li");
					that.removeImg(parent); 
					if($('.imgList').children('li').not('.add').length<10){
						$('.imgList .add').show();
					}
				}).on("change","#updateImgFile",function(){
					var format=that.photoFormat(this);  
					if(format){
						var imgBox=$("#updateImgList"),addObj=imgBox.children("li.add"),src=base+'/res/images/common/bar1.gif';
						addObj.hide();
						addObj.before('<li class="old"><i>×</i><img src="'+src+'"></li>'); 
						$('#updateImgForm').submit(); 					}
					return false;
				}).on("change","#runImgFile",function(){
					var format=that.photoFormat(this);  
					if(format){
						var imgBox=$("#runImgList"),addObj=imgBox.children("li.add"),src=base+'/res/images/common/bar1.gif';
						addObj.hide();
						addObj.before('<li class="old"><i>×</i><img src="'+src+'"></li>'); 
						$('#runImgForm').submit();
					}
					return false;
				}).on("click","#closeBtn",function(){
					var di=$(this).parents(".cp2yDialogBox").attr("data");
					d.close(di);
				})
				
				$('.hReason').live('click',function(){
					var ul=$(this).parents('ul'),title=ul.attr('title'),id=ul.attr('id');
					that.hReason(id);
				})
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
			ColumnChange:function(){
				$.ajax({
						url:dataUrl.ColumnChange,
						type: 'POST',
						data:{t:new Date().getTime()}, 
						success: function(result){
							var result=eval(result).obj;
							if(result.flag==1){
								obj=result.obj;
								$('#audits').html(obj.audits);
								$('#back').html(obj.back);
								$('#collect').html(obj.collect);
								$('#end').html(obj.end);
							}else if(result.flag==-1){
								en.show(myGroup.ColumnChange);
							}
						}
				});
			},
			listChase:[],
			GetGroupList:function(page){//type:全部-0 待办事务-1 审核中-2 募捐中-3 执行中-4 未通过-5 已结束-6
				page=!!page?page:1; 
				var that=this,type=that.curType,chase=that.listChase[type+"_"+page],dataObj= $("#list");
				//if(!!chase){
				//	$("#list").html(chase); 
				//}else{
					$.ajax({
						url:dataUrl.groupList,
						type: 'POST',
						data:{type:type,page:page,t:new Date().getTime()}, 
						success: function(result){
							var result=eval(result);
							if(result.flag==1){
								var data=result.obj,listHd=[],listBd=[],typeNum=1,datas=data.data,len=datas.length;  
								switch(parseInt(type)){
									case 0:
									case 1:
									case 2:typeNum=1;break;
									case 5:typeNum=3;break;
									case 6:typeNum=4;break;
									case 4:
									case 3:typeNum=2;break;
									default:typeNum=1;break; 
								}
								 
								var listHtml=[],itemDtil=dataUrl.viewItem;//项目详情地址
								if(typeNum==4){
									listHtml.push('<div class="tab-main type'+3+'">'); 
								}else{
									listHtml.push('<div class="tab-main type'+typeNum+'">'); 
								}
								listHd.push('<ul class="tab-tit">');
								listHd.push('<li class="lst-col1">项目</li>');
								listHd.push('<li class="lst-col2">负责客服</li>');
								listHd.push('<li class="lst-col3">联系人</li>');
								
								listBd.push('<div id="listData" class="tab-data"></div>');
								var dataDom = document.createDocumentFragment();//dataTempDom
								if(typeNum==1){//待办事务-审核中-执行中-全部
									listHd.push('<li class="lst-col4">项目状态</li>');
									listHd.push('<li class="lst-col5">&nbsp;</li> '); 
									
									for(var i=0;i<len;i++){ 
										var idata=datas[i],listData=[],ul=document.createElement("ul");  
										ul.setAttribute("id",idata.itemId);
										$(ul).data("item",idata); 
										var state=idata.state;
										
										listData.push('<li class="lst-col1">');
									//	listData.push(idata.role==1?'<i class="chief-icon'+(state==210||state==240||state==250?' toEditItem':'')+'"></i>':'');
										listData.push(1==1?'<i class="chief-icon'+(state==210||state==240||state==250?' toEditItem':'')+'"></i>':'');
										listData.push('<a class="tab-link1" href="'+itemDtil+idata.itemId+'" target="_blank" title="'+idata.title+'">'+idata.title+'</a></li>');
										
										listData.push('<li class="lst-col2"><a class="tab-link2" href="javascript:void(0);">'+idata.customer+'</a></li>');
										listData.push('<li class="lst-col3">'+idata.contact+'</li>');
										listData.push('<li class="lst-col4">'+idata.status+'</li>');
										listData.push('<li class="lst-col5">'); 
										switch(state){
											case 210:
												listData.push('<input class="toCheck" type="button" value="提交考察报告"/>');
											 	break;
											case 240:
											case 250: 
												listData.push('<input class="runReport" type="button" value="填写报告"/>');
												listData.push('<input class="stopItem" type="button" value="结束项目"/>')
												break;
											default: 
												listData.push('<a class="tab-link1" href="javascript:void(0);">'+idata.oprate+'</a>');
												break; 
										}
										listData.push('</li>');  
										$(ul).append(listData.join(''));
										dataDom.appendChild(ul);  
									}   
								}else if(typeNum==2){//募捐中-执行中
									listHd.push('<li class="lst-col4">募捐进度</li>');
									listHd.push('<li class="lst-col5">&nbsp;</li>');
									//listHd.push('<li class="lst-col5">募捐进度</li>'); 
									//listHd.push('<li class="lst-col6">募捐截止时间</li>');
									var today=(new Date()).pattern('yyyy-MM-dd');
									 
									for(var i=0;i<len;i++){
										var idata=datas[i],listData=[],ul=document.createElement("ul");  
										ul.setAttribute("id",idata.itemId);
										$(ul).data("item",idata); 
										
										listData.push('<li class="lst-col1">');
										listData.push(1==1?'<i class="toEditItem chief-icon"></i>':'');
										listData.push('<a class="tab-link1" href="'+itemDtil+idata.itemId+'" target="_blank" title="'+idata.title+'">'+idata.title+'</a></li>');
										listData.push('<li class="lst-col2"><a class="tab-link2" href="javascript:void(0);">'+idata.customer+'</a></li>');
										listData.push('<li class="lst-col3">'+idata.contact+'</li>'); 
										listData.push('<li class="lst-col4">'+idata.schedule+'</li>'); 
										listData.push('<li class="lst-col5"><input class="runReport" type="button" value="填写报告"/>');
										listData.push('<input class="stopItem" type="button" value="结束项目"/></li>');
										/*listData.push('<li class="lst-col4">'+idata.status+'</li>');
										listData.push('<li class="lst-col5">'+idata.schedule+'</li>');
										listData.push('<li class="lst-col6"><input class="runReport" type="button" value="项目报告"/>');
										listData.push('<input class="stopItem" type="button" value="结束"/></li>');*/
										//var endTime=idata.endTime;
										//listData.push('<li class="lst-col6'+(today===endTime?' color3':'')+'">'+endTime+'</li>'); 
										listData.push('</ul>');
										
										$(ul).append(listData.join(''));
										dataDom.appendChild(ul);  
									}  
								}else if(typeNum==3){//未通过
									listHd.push('<li class="lst-col4">项目状态</li>');
									listHd.push('<li class="lst-col5">结束时间</li>');
									listHd.push('<li class="lst-col6">操作</li>'); 
									for(var i=0;i<len;i++){
										var idata=datas[i],listData=[],ul=document.createElement("ul");  
										ul.setAttribute("id",idata.itemId);
										ul.setAttribute("title",idata.title);
										$(ul).data("item",idata);  
										listData.push('<li class="lst-col1">'+(1==1?'<i class="chief-icon"></i>':''));
										listData.push('<a class="tab-link1" href="'+itemDtil+idata.itemId+'" target="_blank" title="'+idata.title+'">'+idata.title+'</a></li>');
										listData.push('<li class="lst-col2"><a class="tab-link2" href="javascript:void(0);">'+idata.customer+'</a></li>');
										listData.push('<li class="lst-col3">'+idata.contact+'</li>'); 
										listData.push('<li class="lst-col4">'+idata.status+'</li>');
										listData.push('<li class="lst-col5">'+idata.checkTime+'</li>');
										listData.push('<li class="lst-col6"><a class="hReason">详情</a></li>'); 
										listData.push('</ul>');
										
										$(ul).append(listData.join(''));
										dataDom.appendChild(ul);  
									}   
								}else if(typeNum==4){//已结束
									listHd.push('<li class="lst-col4">项目状态</li>');
									listHd.push('<li class="lst-col5">结束时间</li>');
									listHd.push('<li class="lst-col6">操作</li>'); 
									for(var i=0;i<len;i++){
										var idata=datas[i],listData=[],ul=document.createElement("ul");  
										ul.setAttribute("id",idata.itemId);
										ul.setAttribute("title",idata.title);
										$(ul).data("item",idata);  
										listData.push('<li class="lst-col1">'+(1==1?'<i class="chief-icon"></i>':''));
										listData.push('<a class="tab-link1" href="'+itemDtil+idata.itemId+'" target="_blank" title="'+idata.title+'">'+idata.title+'</a></li>');
										listData.push('<li class="lst-col2"><a class="tab-link2" href="javascript:void(0);">'+idata.customer+'</a></li>');
										listData.push('<li class="lst-col3">'+idata.contact+'</li>'); 
										listData.push('<li class="lst-col4">'+idata.status+'</li>');
										listData.push('<li class="lst-col5">'+idata.stopTime+'</li>');
										listData.push('<li class="lst-col6"><a href="'+"http://www.17xs.org/project/view.do?projectId="+idata.itemId+'">详情</a></li>'); 
										listData.push('</ul>');
										
										$(ul).append(listData.join(''));
										dataDom.appendChild(ul);  
									}   
								}
								listHd.push('</ul>');   
								listHtml.push(listHd.join(''));
								listHtml.push(listBd.join(''));
								listHtml.push('</div>');
								//===分页
								var totalPage=data.total,pageHtml=[];
								if(totalPage>1){
									pageHtml.push('<div id="tabPage" cur="'+page+'" total="'+totalPage+'" class="tab-page">');
									pageHtml.push('<ul class="pages">');
									pageHtml.push('<li><i class="triangle-right"></i></li>');
									pageHtml.push('<li><input id="selfpage" type="text" value="'+page+'"/></li>');
									pageHtml.push('<li><i class="triangle-left"></i></li>');
									pageHtml.push('</ul>');
									pageHtml.push('<p class="p-total">共'+totalPage+'页</p>');
									pageHtml.push('</div>'); 
								} 
								dataObj.html(listHtml.join(''));
								$("#listData").html(dataDom).after(pageHtml.join('')); 
								
								that.listChase[type+"_"+page]=dataObj.html();//缓存
								 
							}else if(result.flag==2){
								dataObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
							}else if(result.flag==0){
								dataObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" href="javascript:void(0);"></a></div></div>');
							}else if(result.flag==-1){
								en.show(myGroup.GetGroupList);
							}   
						} 
					});
			//	}
			}, 
			checkDialog:function(data){
				var html=[],contact=data.contact,itemId=data.itemId;
	      
				//html.push('<div class="check-group">');
				//html.push('<label>审核:</label>');
				//html.push('<span><i v=1 name="ck" class="rdio rdio-cked"></i>通过</span>');
				//html.push('<span><i v=0 name="ck" class="rdio"></i>未通过</span>');
				//html.push('</div>');
				html.push('<div class="txt-group">');
				html.push('<label class="txtVT">备注:</label>');
				html.push('<textarea id="txtCon"></textarea>');
				html.push('</div>');
				html.push('<input id="checkId" type="hidden" value="'+itemId+'"/>'); 
				html.push('<div id="checkBtn" class="dlg-btn">保存并提交</div> '); 
				
				d.open({t:'"'+contact+'"的考察报告',c:html.join('')},{},"checkItemDlg");
			},
			checkItem:function(reason){
				var checkRst= $(".rdio[name='ck']").filter(".rdio-cked").attr("v"),
					reason=$("#txtCon").val(),checkId=$("#checkId").val(); 
					if(reason.length>1000){
						return d.alert({content:"备注不能多于1000个字！"});
					}
				$.ajax({
					url:dataUrl.checkItem,
					type: 'POST',
					data:{checkRst:checkRst,reason:reason,itemId:checkId,t:new Date().getTime()},
					type: 'POST',
					success: function(result){
						if(!!result){
							var result=eval(result); 
							if(result.flag==1){
								d.alert({content:result.errorMsg,type:'ok'});
								myGroup.GetGroupList();
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
			updateData:[],
			/*updateInit:function(){
				var  that=myGroup; 
				that.delImgList=[];
				that.updateData=[];
			},*/
			updateDialog:function(){
				var that=myGroup,data=that.updateData,title=data.title,itemId=data.itemId; 
				$.ajax({
					url:dataUrl.getItem,
					type: 'POST',
					data:{itemId:itemId,t:new Date()},
					success: function(result){ 
						if(!!result){//&&resut.itemId==itemId
							var result=eval(result); 
							if(result.flag==1){
								var idata=result.obj,html=[]; 
								html.push('<div class="tit-group">');
								html.push('<label>标题:</label><input id="iTit" type="txt" value="'+idata.title+'"/>');
								html.push('</div>');
								html.push('<div class="con-group">');
								html.push('<label class="txtVT">内容:</label>');
								html.push('<textarea id="iCon">'+idata.information+'</textarea>');
								html.push('</div>');
								html.push('<div class="img-group">');
								html.push('<label>图片:</label>'); 
								html.push('<form id="updateImgForm" method="post" name="uploadItem" action="http://www.17xs.org/file/upload3.do"  enctype="multipart/form-data">');
								html.push('<ul id="updateImgList" class="imgList">');
								 
								var imgList=idata.images,imgLen=imgList.length;
								for(var i=0;i<imgLen;i++){
									var _img=imgList[i]; 
									html.push('<li id="'+_img.id+'"><i class="close-icon"></i><img src="'+_img.src+'" /></li>'); 
								} 
								if(imgLen<10){
									html.push('<li class="add"><span>+</span>');
								}else{
									html.push('<li class="add" style="display:none"><span>+</span>');
								}
								html.push('<input id="updateImgFile" type="file" name="file" />');
								html.push('<input type="hidden" name="type" id="type" value="4"/>');
								html.push('</li> ');
								html.push('</ul>'); 
								html.push('</form>'); 
								html.push('</div>'); 
								html.push('<p class="tips">注：图片大小每张请控制在2M以内，最多可上传10张图片</p>');
								html.push('<div id="updateBtn" class="dlg-btn">保存并提交</div> '); 
								 
								d.open({t:title,c:html.join('')},{},"imgTxtDlg itemDlg");	 
								
								that.ajaxForm($("#updateImgForm"),1,'#updateImgList');
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(myGroup.updateItem);
							} 
						}else{
							d.alert({content:'您的操作过于频繁，请……'});
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				}); 
			},
			updateItem:function(){
				var that=myGroup,
					title=$("#iTit").val(),
					con=$("#iCon").val(), 
					imgList=$("#updateImgList li"),
					img=[],
					len=imgList.length-1;//$("#imgList").html();  
			   	for(var i=0;i<len;i++){
					img.push($(imgList[i]).attr("id"));
				}
				if(!title){d.alert({content:"标题不能为空！",type:'error'});return false}
				if(title.length>18){d.alert({content:"标题不能超过18个字符！",type:'error'});return false}
				if(!con){d.alert({content:"内容不能为空！",type:'error'});return false}
				//console.log(img);	
				$.ajax({
					url:dataUrl.updateItem,
					type: 'POST',
					data:{itemId:that.updateData.itemId,title:title,information:con,imagesId:img.join(','),t:new Date().getTime()},
					success: function(result){
						if(!!result){
							var result=eval(result); 
							if(result.flag==1){
								d.alert({content:result.errorMsg,type:'ok'});
								myGroup.GetGroupList();
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(that.updateItem);
							}
							return d.close('itemDlg');
						}  
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});  
			},
			runData:[],
			runItemDialog:function(){ 
				var html=[];
				html.push('<div class="con-group">');
				html.push('<label class="txtVT">内容:</label>');
				html.push('<textarea id="reportCon"></textarea>');
				html.push('</div>');
				html.push('<div class="img-group">');
				html.push('<label>图片:</label>');
				html.push('<form id="runImgForm" method="post" name="runItem" action="http://www.17xs.org/file/upload3.do" enctype="multipart/form-data">');
				html.push('<ul id="runImgList" class="imgList">');
				html.push('<li class="add">');
				html.push('<span>+</span>');
				html.push('<input id="runImgFile" type="file"  name="file"/>');
				html.push('<input type="hidden" name="type" id="type" value="5"/>');
				html.push('</li>');
				html.push('</ul> ');
				html.push('</form>');
				html.push('</div>');
				html.push('<p class="tips">注：图片大小每张请控制在2M以内，最多可上传10张图片</p>');
				html.push('<div id="runReportBtn" class="dlg-btn">保存并提交</div> ');
				d.open({t:'"'+myGroup.runData.title+'"的执行报告',c:html.join('')},{},"imgTxtDlg runItemDlg");
				
				this.ajaxForm($("#runImgForm"),1,'#runImgList'); 
			},
			runReport:function(){ 
				var that=myGroup, 
					con=$("#reportCon").val(), 
					imgList=$("#runImgList").children().not(".add"),
					img=[],
					len=imgList.length;//$("#imgList").html();  
			   	for(var i=0;i<len;i++){
					img.push($(imgList[i]).attr("id"));
				} 
				if(con.length>1000){
					return d.alert({content:"内容不能超过1000个字！"});
				}
				$.ajax({ 
					url:dataUrl.runReport,
					type: 'POST',
					data:{pid:that.runData.itemId,content:con,imgIds:img.join(","),type:4,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(!!result){
							var result= eval(result); 
							if(result.flag==1){
								 d.alert({content:result.errorMsg,type:'ok'});
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(that.runReport);
							}
							that.ColumnChange();
							return d.close('runItemDlg');
						}  
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				}); 
			},
			stopDialog:function(data){
				var html=[],title=data.title,itemId=data.itemId;  
				html.push('<div class="txt-group">');
				html.push('<label class="txtVT">内容:</label>');
				html.push('<textarea id="txtCon"></textarea>');
				html.push('</div>');
				html.push('<div id="stopBtn"  class="dlg-btn">保存并提交</div>');
				html.push('<input id="stopId" type="hidden" value="'+itemId+'"/>'); 
				 
				d.open({t:'"'+title+'"的关闭报告',c:html.join('')},{},"stopItemDlg");	
			},
			stopItem:function(){
				var itemId=$("#stopId").val(),
					reason=$("#txtCon").val();
					if(reason.length>1000){
						return d.alert({content:"内容不能超过1000个字！"});
					}
				$.ajax({
					url:dataUrl.stopItem,
					type: 'POST',
					data:{itemId:itemId,reason:reason,t:new Date()},
					chase:false,
					success: function(result){
						if(!!result){ 
							var result=eval(result); 
							if(result.flag==1){
								d.alert({content:result.errorMsg,type:'ok'});
								myGroup.GetGroupList();
							}else if(result.flag==0){
								return d.alert({content:result.errorMsg,type:'error'});
							}else if(result.flag==-1){
								return en.show(myGroup.updateItem);
							}
							  
							return d.close('stopItemDlg');
						}  
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});
			},
			hReason:function(id){
			var that=this;
			$.ajax({
				url:dataUrl.helpReason,
				type: 'POST',
				data:{pid:id},
				success: function(result){ 
					if(result.flag==1){
						var idata=result.obj,html=[];
						html.push('<div class="tit-group tit-Guide yh">');
						html.push('抱歉，您负责跟进的项目由于以下原因未通过审核。');
						html.push('</div>');
						html.push('<div class="con-group">');
						html.push('<textarea id="txtCon">'+idata.comment+'</textarea>');
						html.push('</div>');  
						html.push('<div class="tit-group">');
						html.push('<span class="time">时间：'+(new Date(idata.time)).pattern("yyyy-MM-dd  HH:mm:ss")+'</span>');
						html.push('</div>');
						html.push('<a  id="closeBtn" class="dlg-btn">关闭</a> ');  
						d.open({t:'未通过审核原因',c:html.join('')},{},"itemDlga");	
						return false;
					}else if(result.flag==-1){
						en.show(that.hReason);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
		}  
		};
		return myGroup;
	}
	 
});