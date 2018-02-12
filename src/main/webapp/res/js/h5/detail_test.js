var base=window.baseurl;
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	helpphotoDel:base+'file/image/delete.do',
	hostList:base+'/project/pfeedback.do',
	toCommon:base+'/project/addfeedback.do',
	FeedbackList:base+'/project/leaveWordList.do',
	addleaveWord:base+'/project/addleaveWord.do',
	donationlist:base+'project/gardendonationlist.do'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
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
	
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			var ndmoney=$("#needmoney").val();
			$(".content_tabs li").each(function(index,domEle){ 
				$(domEle).click(function(){	
					$(".content_tabs li").removeClass("cur");
					$(this).addClass("cur");
					$(".detail_info .list").hide();		
					$(".detail_info #list_"+Math.floor(index+1)).show();
					if(index == 2){
						that.delRecordList(1,10);
					}else if(index == 3){
						that.hosList(1);
					}
				});
			});
			if(Number(ndmoney) <= 0){
				$("#paybtn").hide();
				$("#finishedbtn").show();
			}
			
			$('#share').click(function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
			});
			
			$('#list_1 img').click(function(){
		        var imgArray = [];
		        var curImageSrc = $(this).attr('src');
		        var oParent = $(this).parent();
		        if (curImageSrc && !oParent.attr('href')) {
		            $('#list_1 img').each(function(index, el) {
		                var itemSrc = $(this).attr('src');
		                imgArray.push(itemSrc);
		            });
		             wx.previewImage({
		                current: curImageSrc,
		                urls: imgArray
		            });
		        }
		    });
			
			$('#paybtn').click(function(){
				$('#payTips').show();
			});
			$('#closeDetail').click(function(){
				$('#payTips').hide();
			});
			
			$('#selectTb .sel').click(function(){
				$('#selectTb .sel').removeClass('ed');
				$(this).addClass('ed');
				
			});
			$('#btn_submit').click(function(){
				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val();
				var paymoney = Number(money); 
				if(paymoney <= 0){
					money = $("#money2").val();
					if(money < 0.01){
						d.alert({content:"捐款金额不低于0.01元",type:'error'});
						return;
					}
					money = Number(money);
					if(!isNaN(money)){
						if(needmoney < money){
							d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
						}
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/testdeposit2.do?projectId="
							+projectId+"&amount="+money;
					}else{
						d.alert({content:"请输入数字",type:'error'});
					}
				}else{
					if(needmoney < money){
						d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
					}
					window.location.href="http://www.17xs.org/visitorAlipay/tenpay/testdeposit2.do?projectId="
						+projectId+"&amount="+paymoney;
				}
			});
			$("body").on("click",".qqim",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qqim',1): that.bshare(event,'qqim',0);
				 //that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qzone',1): that.bshare(event,'qzone',0);
				 //that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'sinaminiblog',1): that.bshare(event,'sinaminiblog',0);
				// that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'weixin',1): that.bshare(event,'weixin',0);
				 //that.bshare(event,'weixin',0);
			});
		},
		delRecordList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false;
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.donationlist,
				data:{id:projectId,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var html=[];
						for(var i=0;i<len;i++){
						html.push('<a href="#">');
						html.push('<div class="item bg2 record"><span class="thumb">');
						html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg" class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
						html.push('<div class="name">'+datas[i].name+'</div>');
						html.push(' <div class="money">捐助');
						html.push('<label>￥'+$.formatNum(datas[i].dMoney)+'</label>');
						html.push('</div><div class="clear"></div></div>');
						html.push('</a>');
						}
						$('#list_3').html(html.join(''));		
						data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
						data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
					}
				}
			})
			
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
					url:dataUrl.hostList, 
					data:{pid:id,page:page,pageNum:10,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==0){
							d.alert({content:result.errorMsg,type:'error'});
							
						}else if(result.flag==-1){
							en.show(that.hosList);
						}else if(result.flag==2){
							$('#pagetotle').html("共0页");
							$('.bdback-page .page').hide();
							$('#list_4').html('');
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
								html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg"  class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></div>');
								html.push('<div class="detail">');
								html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');						
								html.push('<div class="info">');
								if(imgs&&imgs.length>0){
									html.push(' <p>我提取了善款，以下是我的收据，谢谢大家</p>');
									temp+="";
									for(var j=0;j<imgs.length;j++){
										temp+=('<p><img src="'+imgs[j]+'" width="268" height="357"/></p>');
									}	
									html.push(temp);
								}else{
									html.push('<p>现在好多了，谢谢大家关心，希望大家工作顺利，阖家幸福。</p>');
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
							
							//===图片查看 ===//
							$('#list_4 .info img').click(function(){
						        var imgArray = [];
						        var curImageSrc = $(this).attr('src');
						        var oParent = $(this).parent();
						        if (curImageSrc && !oParent.attr('href')) {
						            $('#list_4 img').each(function(index, el) {
						                var itemSrc = $(this).attr('src');
						                imgArray.push(itemSrc);
						            });
						             wx.previewImage({
						                current: curImageSrc,
						                urls: imgArray
						            });
						        }
						    });
						}
						
					},
					share:function(){
						var html=[],v=$('#pprim').attr('data').split('|');
						html.push('<h2>邀请内容</h2>');
						html.push('<textarea class="area">'+v[2]+'，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐'+v[0]+'元！(注册邀请码：'+v[1]+')</textarea>');
						html.push('<h2>传播至：</h2>');
						html.push('<div class="shareWeblist">');
						html.push('<a class="qqim" tel="1" >QQ好友</a>');
						html.push('<a class="qzone" tel="1">QQ空间</a>');
						html.push('<a class="sinaminiblog" tel="1" >新浪微博</a>');
						html.push('<a class="weixin" tel="1">微信</a>');
						html.push('</div>');
						var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
						d.open(o,css,"Delshare"); 	
					},
				});	
		},
		share1:function(ptle){
			var html=[];
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+ptle+'</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" >QQ好友</a>');
			html.push('<a class="qzone">QQ空间</a>');
			html.push('<a class="sinaminiblog" >新浪微博</a>');
			html.push('<a class="weixin">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
	};
	 
	h5detail.init();
});