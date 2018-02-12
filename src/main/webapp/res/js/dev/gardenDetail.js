var base=window.baseurl;
require.config({
	baseUrl:window.baseurl+"/res/js/",
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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		PLEDGING:base+"user/realname.do",//立即认捐
		HOSLIST:base+"project/gardendonationlist.do", //历史记录
		//HOSLIST:"/DATA/hoslist.json"
	}
	
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
				if(tmptxt==''){
					tmptxt=1;
					$(this).val(1);
				}
				
				that.priceCount(1,1);
			})
			
			$('#gDtile-btn').click(function(){
				var sum=parseInt($('#total').html());
				$.ajax({ 
					url:WebAppUrl.PLEDGING, 
					data:{sum:sum,t:new Date().getTime()},
					success: function(result) { 
						//结果处理
					}
				});	
			})
			
			
			$('#pagenum').blur(function(){
				var tmptxt=$(this).val();     
       			$(this).val(tmptxt.replace(/\D|^0/g,'')); 
				tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				if(tmptxt==''||teptxt=="NaN"){
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
			var sum,price=parseInt($('#unitPrice').html());num=parseInt($('#num').val()),numbtn=$('#num');sumbtn=$('#total'),leaveNumber=parseInt($('#leaveNumber').html());
			if(sn){
				num>leaveNumber?num=leaveNumber:num=num;
			}else{
				if(type){
					num>leaveNumber?num=leaveNumber:num=num+1;
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
			sumbtn.html(num*price);
		},
		formatDate:function(now){     
              var   year=now.getFullYear();     
              var   month=now.getMonth()+1;     
              var   date=now.getDate();     
              var   hour=now.getHours();     
              var   minute=now.getMinutes();     
              var   second=now.getSeconds();     
              return   year+"-"+month+"-"+date+"   "+hour+":"+minute;     
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
								html.push('<li class="wlst-col3">'+datalist[i].dMoney+'</li>');
								html.push('<li class="wlst-col4">'+that.formatDate(new Date(datalist[i].dTime))+'</li>');					html.push('</ul>');
							}
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
							
							$('#pagetotle').html("共"+that.totalpage+"页");
						}
						
					}
				});	
		}
	}
	detail.init();	
});