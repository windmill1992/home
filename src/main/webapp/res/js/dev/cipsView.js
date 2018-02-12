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
	baseUrl:window.baseurl+"res/js/",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","ajaxform","pages","pageCommon"],function($,d,h,en,f,p,p1){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.sitePage="p-goodGarden";
	h.init();
	d.init();
	en.init(); 
	p.init({afterObj:$("#grecordList")});
	p.changeCallback=function(){
		detail.delRecordList(p.curPage,10);
	};
	var detail={
		hosData:'',
		totalpage:1,
		currpage:1,
		photoData:[],
		init:function(){
			var that=this;
			$('#dtList').myScroll({
				speed: 40, //数值越大，速度越慢
				rowHeight: 63 //dl的高度
			});
			
			$('.cipsnum .reduce').click(function(){
				var input=$('#cipnum'),v=parseInt(input.val());
				if(v<=1){
					d.alert({content:'数量不能低于1！',type:'error'});
					input.val(v);
					return false;	
				}else{
					input.val(v-1);
				}
				v=parseInt(input.val());
			
				var perMoney = $("#perMoney").html();
				perMoney=perMoney.replace(",","");
				var amount = v*perMoney;
				$("#donateAmount").html($.formatNum(amount));
			})
			$('.cipsnum .add').click(function(){
				var input=$('#cipnum'),v=parseInt(input.val()),maxNum=Number($('#maxNum').html());
				if(v>=maxNum){
					d.alert({content:'数量不能大于剩余份数！',type:'error'});
					input.val(maxNum);
					return false;	
				}else{
					input.val(v+1);
				}
				v=parseInt(input.val());
				var perMoney = $("#perMoney").html();
				perMoney=perMoney.replace(",","");
				var amount = v*perMoney;
				$("#donateAmount").html($.formatNum(amount));
			})
			
			$('#cipnum').blur(function(){
				var v=Number($(this).val()),maxNum=Number($('#maxNum').html());
				if(v>maxNum){
					$(this).val(maxNum);
				}else if(v<1){
					v = 1 ;
					$(this).val(v);
				}else{
					v = Math.floor(v);
					$(this).val(v);
				}
			
				var perMoney = $("#perMoney").html();
				perMoney=perMoney.replace(",","");
				v=$(this).val();
				var amount = v*perMoney;
				$("#donateAmount").html($.formatNum(amount));
			});
			
			$('#tab-hd span').click(function(){
				var i=$('#tab-hd span').index(this);
				$('#tab-hd span').removeClass('on').eq(i).addClass('on');
				$('#cipsCon .conOne').hide().eq(i).show();
				if(i==2){that.delRecordList(1,10)}
			})
			
			$("#payBt1").click(function(){
				that.buyproject();
			});
			
			$("#payBt2").click(function(){
				that.buyproject();
			});
			
		},		
		delRecordList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false;
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.donationlist,
				data:{id:projectId,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					//result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var html=[];
						for(var i=0;i<datas.length;i++){
							html.push('<dl>');
							if(datas[i].imagesurl != null )
							{
								html.push('<dt><img src="'+datas[i].imagesurl+'" alt=""></dt>');
							}else
							{
								html.push('<dt><img src="'+base+'res/images/detail/people_avatar.jpg" alt=""></dt>');
							}
							html.push('<dd>');
							html.push('<p><strong>'+$.ellipsis(datas[i].name,8,"...")+'</strong><span>认捐了'+datas[i].copies+'份 <em class="redText">'+$.formatNum(datas[i].dMoney)+'</em>元</span><span class="gray">'+that.formatDate(new Date(datas[i].dTime))+'</span></p>');
							html.push('<p>'+datas[i].leaveWord+'</p>');
							html.push('<dd>');
							html.push('</dl>');
							
						}
						/*
						for(var i=0;i<len;i++){
						html.push('<dt><img src="res/images/newIndex/view/txpic_11.jpg" alt=""></dt>');
						html.push('<dd>');
						html.push('<p><strong>傅旭东</strong><span class="gray">10/20 08:32</span><span>认投了10份 <em class="redText">150</em>元</span></p>');
						html.push('<p>希望孩子们永远开心快乐</p>');
						html.push('<dd>');
						}
						*/
						$('#grecordList').html(html.join(''));		
						data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
						data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
					}
				}
			})
			
		}
		,
		formatDate:function(now)
		{  
				var mouth,dtae,hour,minute,second;
	              var   year=now.getFullYear();     
	              now.getMonth()+1<10? month='0'+(now.getMonth()+1): month=now.getMonth()+1;     
	              now.getDate()<10? date='0'+now.getDate(): date=now.getDate();     
	              now.getHours()<10? hour='0'+now.getHours():  hour=now.getHours();     
	              now.getMinutes()<10? minute='0'+now.getMinutes(): minute=now.getMinutes();     
	              now.getSeconds()<10? second='0'+now.getSeconds(): second=now.getSeconds();     
	              return   year+"/"+month+"/"+date+" &nbsp;  "+hour+"："+minute;     
	     }
		,
		buyproject:function(){
				var sum=parseFloat($("#donateAmount").html());
				var input=$('#cipnum'),num=parseInt(input.val());
				var leaveNumber = Number($('#maxNum').html());
				if(num>leaveNumber)
				{
					alert("超过库存");
				}
				else
				{
					var that=this;
					//已登录
					if(en.isIn){ 
						window.location.href=base+"project/buyproject.do?projectId="+$('#projectId').val()+"&donateCopies="+num+"&donatAmount="+sum;	
					}
					//未登录
					else
					{
						window.location.href=base+"project/buyproject.do?projectId="+$('#projectId').val()+"&donateCopies="+num+"&donatAmount="+sum;	
						//window.location.href=base+"visitorAlipay/visitorpay.do?projectId="+$('#projectId').val()+"&amount="+sum+"&pName="+$('#pName').val();
					} 
				}
		}
		
	}
	
	detail.init();	
});