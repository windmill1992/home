var base=window.baseurl;
var dataUrl={
	hostList:base+'/project/H5careProjectList.do',//反馈
	donationlist:base+'project/gardendonationlist.do'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" 	 : ["jquery-1.8.2.min"], 
		"extend" 	 : "dev/common/extend",
		"dialog" 	 : "dev/common/dialog",
		"util"   	 : "dev/common/util",
		"head"   	 : "dev/common/head",
		"entry"  	 : "dev/common/entry",
		"userCenter" : "dev/common/userCenter" ,
		"ajaxform" 	 : "util/ajaxform",
		"pages" 	 : "dev/common/pages",
		"pageCommon" :"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
//	window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		h5detail.delRecordList(p.curPage,10);
	};
	uc.selectCallback=function(){
		h5detail.delRecordList(1,10);
	};
	var page=1;
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			var ndmoney=$("#needmoney").val();
			$('.pic').each(function(){
				if($(this).find('iframe').length > 0){
					$(this).parent().prepend($(this)[0]);
				}
			});
			//===========item定位start=============//
			var itemType = Number($('#itemType').val());
			if(itemType == 1){
				$(".content_tabs li").removeClass("cur");
				$(".content_tabs li").eq(1).addClass("cur");
				$("#list_1").hide();
				$("#list_2").show();
			}
			else if(itemType == 2){
				$(".content_tabs li").removeClass("cur");
				$(".content_tabs li").eq(2).addClass("cur");
				$("#list_1").hide();
				$("#list_3").show();
				that.delRecordList(1,10);
			}
			else if(itemType == 3){
				$(".content_tabs li").removeClass("cur");
				$(".content_tabs li").eq(3).addClass("cur");
				$("#list_1").hide();
				$("#list_4").show();
				that.hosList(1);
			}
			//===========item定位end=============//
			var w = $('.fill').attr('data-width');
			$('.fill').width(w+'%');
			if(Number(ndmoney) <= 0){
				$("#paybtn").hide();
				$("#finishedbtn").show();
			}
			that.bindEvent();
		},
		bindEvent:function(){
			var that = this;
			$("body").on("click",".content_tabs li",function(){ 	//切换内容
				var index = $(this).index();
				$(".content_tabs li").removeClass("cur");
				$(this).addClass("cur");
				$(".detail_info .list").hide();		
				$(".detail_info #list_"+Math.floor(index+1)).show();
				if(index == 2){
					$("#list_3").html("<div class='list-box'></div><div class='load-more'><a href='javascript:;' class='aload'>点击加载更多</a></div>");
					that.delRecordList(1,10);
				}else if(index == 3){
					that.hosList(1);
				}
			}).on("click","#list_4 .info img",function(){ 		//图片预览
				that.previewImg($('#list_4 .info img'),$(this));
				
			}).on("click","#paybtn",function(){ 
				var userId = $("#userId").val(),browser = $("#browser").val();	//判断是否已登录
				if((userId == null || userId == '') && browser!='wx'){
					$('#loginCue').show();
				}else{
					location.href='http://www.17xs.org/project/toCrowdfundingPrize.do?projectId='
						+$("#projectId").val()+'&extensionPeople='+$("#extensionPeople").val();
				}
			}).on("click","#cue_fr",function(){ 	//取消登录
				$('#loginCue').hide();
				
			}).on("click","#cue_fl",function(){ 	//登录
				location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=projectId_'+$("#projectId").val();
				
			}).on("click","#bigImg",function(){ 	//关闭预览
				$(this).hide();
			}).on("click",".aload",function(){ 	//关闭预览
				that.delRecordList(++page,10);
			});
		},
		previewImg:function(objs,obj){
			var ua = $('#browser').val();
			if(ua == 'wx'){
				var imgArray = [];
		        var curImageSrc = obj.attr('src');
		        var oParent = obj.parent();
		        if (curImageSrc && !oParent.attr('href')) {
		            objs.each(function(index, el) {
		                var itemSrc = $(el).attr('src');
		                imgArray.push(itemSrc);
		            });
		             wx.previewImage({
		                current: curImageSrc,
		                urls: imgArray
		            });
		        }
			}else{
				var html = [];
				html.push('<ul>');
				html.push('<li><img src="'+obj[0].src+'" /></li>');
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show();
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
			}
		},
		delRecordList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false;
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.donationlist,
				data:{id:projectId,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){
						if(result.obj.data.length==0){
							$(".load-more").html("暂无人捐助~").css({'color':'#999'});
						}else if(result.obj.data.length<10){
							$(".load-more").html("没有更多数据了...").css({'color':'#999'});
						}
						if(result.obj.data.length>0){
							var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
							len=len>pageNum?pageNum:len; 
							var html=[];
							for(var i=0;i<len;i++){
							html.push('<a href="javascript:void(0);">');
							html.push('<div class="item bg2 record"><span class="thumb">');
							if(datas[i].imagesurl != null ){
								html.push('<img osrc="'+datas[i].imagesurl+'" class="fl" src="'+datas[i].imagesurl+'"></span>');
							}else{
								html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg" class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
							}
							html.push('<div class="name"><p class="name_p1">'+$.ellipsis(datas[i].name,7,'..')+'</p>');
							html.push('<p class="name_p2">捐');
							html.push('<label>￥'+$.formatNum(datas[i].dMoney)+'</label></p>');
							html.push('</div></div><div class="clear"></div></div>');
							html.push('</a>');
							}
							$('.list-box').append(html.join(''));	
							data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
							data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
						}
					}
				}
			});
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
				url:dataUrl.hostList, 
				data:{projectId:id,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result) { 
					if(result.flag==0){
						d.alert({content:result.errorMsg,type:'error'});
					}else if(result.flag==-1){
						en.show(that.hosList);
					}else if(result.flag==2){
						$('#pagetotle').html("共0页");
						$('.bdback-page .page').hide();
						$('#list_4').html('<p style="color:#999;text-align:center;line-height:40px;font-size:14px;">暂无反馈~</p>');
						return false;						
					}else{
						var datalist=result.obj.data,html=[];
						that.total=page=result.obj.total,that.currpage=result.obj.page,that.hosData=datalist;
						$('#pagenum').val(that.currpage);
						$('#feedNum').html(result.obj.nums);
						for(var i=0;i<datalist.length;i++){
							var imgs = datalist[i].imgs;
							var temp="";
							html.push('<div class="item bg2"><div class="avatar">');
							if(datalist[i].userImageUrl != null){
								html.push('<img class="fl" src="'+datalist[i].userImageUrl+'"></div>');
							}else{
								if(datalist[i].uName != null){
									html.push('<img class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></div>');
								}else{
									html.push('<img class="fl" src="'+base+'res/images/h5/images/kefu_hulu.jpg"></div>');
								}
							}
							html.push('<div class="detail">');
							if(datalist[i].source == 'home'){
								html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
							}
							else if(datalist[i].source == 'admin'){
								html.push('<div class="title"><span class="name">客服</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
							}
							else{
								if(datalist[i].uName == null){
									html.push('<div class="title"><span class="name">客服</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
								}
								else{
									html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
								}
							}
							html.push('<div class="info">');
							if(imgs&&imgs.length>0){
								html.push(' <p>'+datalist[i].content+'</p>');
								temp+="";
								for(var j=0;j<imgs.length;j++){
									temp+=('<p><img src="'+imgs[j]+'" width="268" height="357"/></p>');
								}	
								html.push(temp);
							}else{
								html.push('<p>'+datalist[i].content+'</p>');
							}
							html.push('<div class="clear"></div> </div></div>');
							html.push('<div class="clear"></div></div>');
						}
						if(datalist.length<1&&that.currpage==1){
							$('.bdback-page .page').hide();
						}else{
							$('#list_4').html(html.join(''));
							if(that.currpage==1){
								$('#pageprev').attr("disabled","disabled").addClass('pagedefalut');
							}else{
								$('#pageprev').removeAttr("disabled").removeClass('pagedefalut');
							}
							if(that.currpage==that.totalpage){
								$('#pagenext').attr("disabled","disabled").addClass('pagedefalut');
							}else{
								$('#pagenext').removeAttr("disabled").removeClass('pagedefalut');
							}
						}
						$('#pagetotle').html("共"+that.totalpage+"页");
					}
				}
			});	
		}
	};
	h5detail.init();
});
