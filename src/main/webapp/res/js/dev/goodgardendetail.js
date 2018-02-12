var base=window.baseurl;
require.config({
	baseUrl:window.baseurl+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter"],function($,d,h,en,uc){
	window.sitePage="p-goodGarden"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		PLEDGING:base+"user/realname.do",//立即认捐
		HOSLIST:base+"project/gardendonationlist.do" //历史记录
		//HOSLIST:"/DATA/hoslist.json"
	};
	var detail={
		hosData:'',
		totalpage:1,
		currpage:1,
		init:function(){
			var that=this;
			$('#tab-hd span').click(function(){
				var i=$('#tab-hd span').index(this);
				$('#tab-hd span').removeClass('on').eq(i).addClass('on');
				$('#tab-bd .bdcon').hide().eq(i).show();
				if(i==1){
					that.hosList(1);
				}
			})
			
			$('#btn-reduce').click(function(){
				that.priceCount(0);
			})
			$('#btn-add').click(function(){
				that.priceCount(1);
			})
			
			$('#num').blur(function(){
				var tmptxt=$(this).val();     
       			$(this).val(tmptxt.replace(/\D|^0/g,'')); 
				tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				
				if(tmptxt==''||isNaN(tmptxt)){
					tmptxt=1;
					$(this).val(1);
				}
				
				that.priceCount(1,1);
			})
			
			$('#gDtile-btn').click(function(){
				that.buyproject();
			})
			
			
			$('#pagenum').blur(function(){
				var tmptxt=$(this).val();     
       			$(this).val(tmptxt.replace(/\D|^0/g,'')); 
				tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				if(tmptxt==''||isNaN(tmptxt)){
					tmptxt=1;
				}
				else{
					if(tmptxt>that.totalpage){
						tmptxt=that.totalpage;
					}
				}
				that.hosList(tmptxt);
				$(this).val(tmptxt);
			})
			$('#pageprev').click(function(){
				var page;
				if(that.currpage==1){
					page=1;
					return false;
				}else{
					page=that.currpage-1;
					that.hosList(page);
				}
				
			})
			$('#pagenext').click(function(){
				var page;
				if(that.currpage==that.totalpage){
					page=that.totalpage;
					return false;
				}else{
					page=that.currpage+1;
					that.hosList(page);
				}
			})
			
			
		},
		priceCount:function(type,sn){
			var sum,price=parseFloat($('#unitPrice').attr("p")),num=parseInt($('#num').val()),numbtn=$('#num');sumbtn=$('#total');
			var leaveNumber=parseFloat($('#leaveNumber').html());
			if(leaveNumber==0){return false;}
			if(sn){
				num>leaveNumber?num=leaveNumber:num=num;
			}else{
				if(type){
					if(num>(leaveNumber-1)){
						num=leaveNumber;
						d.alert({content:'不能超过总份数',type:'error'});
					}else{
						num=num+1;
					}
				}else{
					if(num==1){
						d.alert({content:'不能低于1份',type:'error'});
						return false;
					}else{
						num=num-1;					
					}
				}
			}
			numbtn.val(num);
			var str=num*price,strs;
			str<1?strs=str:strs=$.formatNum(str);
			sumbtn.html(strs).attr('p',str);
		},
		formatDate:function(now){  
			var mouth,dtae,hour,minute,second;
              var   year=now.getFullYear();     
              now.getMonth()+1<10? month='0'+(now.getMonth()+1): month=now.getMonth()+1;     
              now.getDate()<10? date='0'+now.getDate(): date=now.getDate();     
              now.getHours()<10? hour='0'+now.getHours():  hour=now.getHours();     
              now.getMinutes()<10? minute='0'+now.getMinutes(): minute=now.getMinutes();     
              now.getSeconds()<10? second='0'+now.getSeconds(): second=now.getSeconds();     
              return   year+"/"+month+"/"+date+" &nbsp;  "+hour+"："+minute;     
        }, 
		buyproject:function(){
			var that=this;
			if(en.isIn){ 
				var sum=parseFloat($('#total').attr('p'));
				var num = $('#num').val();
				if(num>leaveNumber){
					alert("超过库存");
				}else{
					window.location.href=base+"project/buyproject.do?projectId="+$('#projectId').val()+"&donateCopies="+num+"&donatAmount="+sum;	
				}
			}else{
				   en.show(that.buyproject);
			} 
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
					url:WebAppUrl.HOSLIST, 
					data:{id:id,page:page,pageNum:10,t:new Date().getTime(),id:$('#projectId').val()},
					success: function(result) { 
						if(!result.flag){
							d.alert({content:result.errorMsg,type:'error'});
						}else{
							var datalist=result.obj.data,html=[];
							that.totalpage=result.obj.total,that.currpage=result.obj.page,that.hosData=datalist;
							$('#pagenum').val(that.currpage);
							for(var i=0;i<datalist.length;i++){
								html.push('<ul>');
								html.push('<li class="wlst-col1">'+datalist[i].name+'</li>');
								html.push('<li class="wlst-col2">'+datalist[i].copies+'</li>');
								html.push('<li class="wlst-col3">'+$.formatNum(datalist[i].dMoney)+'</li>');
								html.push('<li class="wlst-col4">'+that.formatDate(new Date(datalist[i].dTime))+'</li>');
								html.push('</ul>');
							}
							if(datalist.length<1&&that.currpage==1){
								$('.bdback-page .page').hide();
							}else{
							$('#hoslist').html(html.join(''));
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
	}
	detail.init();
	detail.priceCount(1,1);
});