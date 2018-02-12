var base = window.baseurl;
var dataUrl = {
	dataList: base +'project/appeallist_h5.do',		//我的求助列表
	projectFeedbackUrl: base +'project/view_feedback_h5.do',
	careList: base +"project/careProjectList.do",		//关注项目
	myDonateList: base +"project/donationlist_h5.do",
	projectUrl: base +'project/view_h5.do',
	publish: base +"ucenter/coreHelp/publish_h5.do"
};
Date.prototype.pattern = function(fmt){
	var o = {
	"M+" : this.getMonth()+1, //月份
	"d+" : this.getDate(), //日
	"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
	"H+" : this.getHours(), //小时
	"m+" : this.getMinutes(), //分
	"s+" : this.getSeconds(), //秒
	"q+" : Math.floor((this.getMonth()+3)/3), //季度
	"S" : this.getMilliseconds() //毫秒
	};
	var week = {
	"0" : "\u65e5",
	"1" : "\u4e00",
	"2" : "\u4e8c",
	"3" : "\u4e09",
	"4" : "\u56db",
	"5" : "\u4e94",
	"6" : "\u516d"
	};
	if(/(y+)/.test(fmt)){
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	if(/(E+)/.test(fmt)){
		fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);
	}
	for(var k in o){
		if(new RegExp("("+ k +")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		}
	}
	return fmt;
};
var page = 1;
var needhelplist = {
	init:function(){
		var that = this, itemType = $('#itemType').val();
		that.dataList(1,10);
		if(itemType == 1){
			that.myDonateList(1);
			$('#myDonate').addClass("cur");
			$('#list_2').show();
		}else if(itemType == 2){
			that.feedbackList(1);
			$('#careProject').addClass("cur");
			$('#list_3').show();
		}else{
			$('#myCry').addClass("cur");
			$('#list_1').show();
		}
		$('body').on('click','#loadmore',function(){
			if($(this).hasClass('no'))return;
			var idx = $('.data_tab .cur').index();
			if(idx == 0){
				that.dataList(++page,10);
			}else if(idx == 1){
				that.myDonateList(++page);
			}else{
				that.feedbackList(++page);
			}
		}).on('click','.data_tab a',function(){
			var index = $(this).index()+1;
			$('.list').html('');
			page = 1;
			$(this).addClass("cur").siblings().removeClass("cur");
			$(".user_center .list").hide();		
			$("#list_"+index).show();
			
		}).on('click','#myCry',function(){
			that.dataList(1,10);
			
		}).on('click','#myDonate',function(){
			that.myDonateList(1);
			
		}).on('click','#careProject',function(){
			that.feedbackList(1);
			
		}).on('click','#list_3 .info img',function(){
			that.previewImg($(this));
			
		}).on('click','#bigImg',function(){
			$(this).hide().find('.bd').html('');
		});
	},
	dataList:function(page,num){
		var that = this, html = [];
		$.ajax({
			url:dataUrl.dataList,
			data:{page:page,pageNum:num,t:new Date()},
			success: function(result){
				if(result.flag=='1'){
					var data = result.obj,
						total = data.nums,
						pageNum = data.pageNum,
						datas = data.data,
						len = datas.length;
					len = len>pageNum?pageNum:len; 
					$('#needHelpNum').html(total);
					if($('.more').length > 0){
						$('.more').remove();
					}
					if($('.norecord').length > 0){
						$('.norecord').remove();
					}
					if(total>0){
						for(var i=0;i<len;i++){
							html.push('<div class="item bg2">');
						    html.push('<a href="'+ dataUrl.projectUrl +'?projectId='+ datas[i].id +'">');
							if(datas[i].status == 260){
								html.push('<div class="item_bd finish">');
							}else if(datas[i].donatePercent==100){
								html.push('<div class="item_bd success">');
							}else{
								html.push('<div class="item_bd">');
							}
							html.push('<span class="thumb">');
						    if(datas[i].imageurl != null ){
						    	html.push('<img class="fl" src="'+ datas[i].imageurl +'">');
						    }else{
						    	html.push('<img class="fl" src="/res/images/logo-def.jpg">');
						    }
						    html.push('</span><div class="t">');
						    html.push('<div class="tn"></div>');
						    html.push('<div class="tt">'+ datas[i].title +'</div>');
						    html.push('<div class="tp"><label>目标<span class="r">'+ that.formatNum(datas[i].cryMoney) +'</span>元</label>');
						   	html.push('<label>已完成<span class="r">'+ datas[i].donatePercent +'%</span></label></div>');
						   	html.push('</div><div class="clear"></div>');
						    html.push('</div></a><div class="item_ft">');
						    html.push('<a href="/uCenterProject/showAccountNumber.do?projectId='+ datas[i].id +'" class="withdraw">我要提现</a>');
						    html.push('<a href="'+ dataUrl.projectFeedbackUrl +'?itemId='+ datas[i].id +'" class="feedback">我要反馈</a>');
						    html.push('</div></div>');
						}
						if(total > page * num){
							html.push('<div class="more"><a href="javascript:;" id="loadmore">查看更多</a></div>');
						}else{
							html.push('<div class="more"><a href="javascript:;" id="loadmore" class="no">没有更多数据了</a></div>');
						}
						html.push('<div class="norecord">');
						html.push('<a href="'+ dataUrl.publish +'" class="publish">点此发布求助信息</a>');
						html.push('</div>');
					}else{
						html.push('<div class="norecord">');
						html.push('<span class="tip">您还没有发布求助信息</span>');
						html.push('<a href="'+ dataUrl.publish +'" class="publish">点此发布求助信息</a>');
						html.push('</div>');
					}
					$('#list_1').append(html.join(''));
				}else{
					$('#needHelpNum').html(0);
				}
			},
			error: function(r){ 
				alert('获取求助列表失败！');
			}
		});
	},
	feedbackList:function(page){ 
		var that = this,
			userId = "",
			projectId = $("#projectId").val();
		$.ajax({
			url:dataUrl.careList,
			data:{userId:userId,type:'',page:page,pageNum:10,t:new Date(),projectId:projectId},
			success: function(result){
				result = eval(result);  
				if($('.more').length > 0){
					$('.more').remove();
				}
				if(result.flag==1){
					var data = result.obj,
						total = data.nums,
						pageNum = data.pageNum,
						datas = data.data,
						len = datas.length,
						html=[];
					len = len>pageNum?pageNum:len; 
					var itemDtil = dataUrl.projectUrl+'?projectId=';
					for(var i=0;i<len;i++){
						var idata = datas[i]; 
						html.push('<div class="item bg2">');
						if(idata.userImageUrl==null){
							html.push('<div class="avatar"><img src="/res/images/item/items/txicon_04.jpg" /></div>')
						}else{
							html.push('<div class="avatar"><img osrc="'+idata.userImageUrl+'" class="fl" src="'+idata.userImageUrl+'"></div>')
						}
					    html.push('<div class="detail">');
					    if(idata.uName==null){
					    	html.push('<div class="title"><span class="name">客服</span><span class="time">'+(new Date(idata.cTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
					    }else{
					    	html.push('<div class="title"><span class="name">'+idata.uName+'</span><span class="time">'+(new Date(idata.cTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
					    }
						html.push('<div class="info">');
						html.push('<p>'+idata.content+'</p>');
						if(idata.imgs){							
							html.push('<div class="po_img">');
							for(var j=0;j<idata.imgs.length;j++){
								html.push('<a href="javascript:;"><img src="'+idata.imgs[j]+'" width="77" height="77" /></a>');
							}
							html.push('</div>');
						}
						html.push('<div class="clear"></div>');
						html.push('</div></div>');
						html.push('<div class="clear"></div></div>');
					}  
					if(total > page * 10){
						html.push('<div class="more"><a href="javascript:;" id="loadmore">查看更多</a></div>');
					}else{
						html.push('<div class="more"><a href="javascript:;" id="loadmore" class="no">没有更多数据了</a></div>');
					}
					$('#list_3').append(html.join(''));
					
				}else if(result.flag==2){
					$('#list_3').html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有相关关注列表</div></div>');
				}else if(result.flag==0){
					$('#list_3').html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
				}else if(result.flag==-1){
					$('#list_3').html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
				}
			}
		});
	},
	myDonateList:function(page){ 
		var that = this,
			userId = "",
			projectId = $("#projectId").val(),
			pstate = $('#pstate').val();
		$.ajax({
			url:dataUrl.myDonateList,
			data:{userId:userId,type:'',page:page,pageNum:10,pstate:pstate,t:new Date()},
			success: function(result){
				result = eval(result); 
				if($('.more').length > 0){
					$('.more').remove();
				}
				if(result.flag==1){
					var data = result.obj,
						total = data.nums,
						pageNum = data.pageNum,
						datas = data.data, 
						len = datas.length,
						html = [];
					len=len>pageNum?pageNum:len; 
					var itemDtil=dataUrl.projectUrl+'?projectId=';
					for(var i=0;i<len;i++){
						var idata = datas[i]; 
						html.push('<a href="'+itemDtil+idata.pid+'">');
						if(idata.imagesurl==null){
							html.push('<div class="item bg2"><span class="thumb"><img osrc="/res/images/logo-def.jpg" class="fl" src="/res/images/logo-def.jpg"><label>'+idata.name+'</label></span>');
						}else{
							html.push('<div class="item bg2"><span class="thumb"><img osrc="'+idata.imagesurl+'" class="fl" src="'+idata.imagesurl+'"><label>'+idata.name+'</label></span>');
						}
						html.push('<div class="t">');
						html.push('<div class="tn"></div>');
						html.push('<div class="tt">'+idata.donationNum+'人捐助，金额'+idata.donatAmountpt+'元</div>');
				
						if(idata.status==260 && idata.cryMoney>idata.donatAmountpt){
							html.push('<div class="ts_success">已结束</div>');
						}else if(idata.cryMoney<=idata.donatAmountpt){
							html.push('<div class="ts_success">已筹满</div>');
						}else{
							html.push('<div class="ts">已募集<span>'+ idata.donatAmountpt +'</span>元</div>');
						}
						html.push('<div class="tp">');
						if(idata.dType == 1){
							html.push('<label>您的助善已召集<span class="r">'+idata.donateNum+'</span>次，共<span class="r">捐款'+idata.dMoney+'</span>元</label></div>');
						}else{
							html.push('<label>您已捐助<span class="r">'+idata.donateNum+'</span>次，共<span class="r">'+idata.dMoney+'</span>元</label></div>');
						}
						html.push('</div>');
						html.push('<div class="clear"></div>');
						html.push('</div>');
						html.push('</a>');
					}  
					if(total > page * 10){
						html.push('<div class="more"><a href="javascript:;" id="loadmore">查看更多</a></div>');
					}else{
						html.push('<div class="more"><a href="javascript:;" id="loadmore" class="no">没有更多数据了</a></div>');
					}
					$('#list_2').append(html.join(''));
					
				}else if(result.flag==2){
					$('#list_2').html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有相关关注列表</div></div>');
				}else if(result.flag==0){
					$('#list_2').html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
				}else if(result.flag==-1){
					$('#list_2').html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
				}
			}
		});
	},
	previewImg:function(obj){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			var imgArray = [];
			var curImgSrc = obj.attr('src');
			imgArray.push(curImgSrc);
			 wx.previewImage({
				current: curImgSrc,
				urls: imgArray
			});
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
	formatNum:function(num){//,splt 
	    var Decimal='',Integer='',str=String(num);
		if(str.indexOf(".")!=-1){
			Decimal=str.substring(str.indexOf("."));
			if(Decimal.length==2){
				Decimal=Decimal+0;
			}else{
				Decimal=Decimal.substr(0,3);
			}
			Integer=str.substring(0,str.indexOf("."));
		}else{
			Integer=num;
			Decimal='.00';
		}
	    num=Number(Integer);
		if(num!=0&&!isNaN(num)){
			num=""+num;
			var result=[],len=num.length,subI=0; 
			for(var i=0;i<len;i++){
				if(i%3==0){
					subI=i/3;
					var temp=num.substring(len-subI*3,len-subI*3-3); 
					result.push(temp);
				} 
			} 
			return result.reverse().join(',')+Decimal;
		}
		return 0+Decimal;
	},
	Subtr:function(arg1,arg2){//相减保留两位
	 var r1,r2,m,n;
	 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
	 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
	 m=Math.pow(10,Math.max(r1,r2));
	 //last modify by deeka
	 //动态控制精度长度
	 n=(r1>=r2)?r1:r2;
	 return ((arg1*m-arg2*m)/m).toFixed(n);
	}
};

$(function(){
	needhelplist.init();
});
