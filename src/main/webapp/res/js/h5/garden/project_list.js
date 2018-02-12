var base=window.baseurl;
var dataUrl={
	projectList:base+'/h5GardenProject/getProject_list.do',//加载项目列表1
	projectList2:base+'/h5GardenProject/goodProjectList.do',//加载项目列表2
	openDonates:base+'/h5GardenProject/openDayDonates.do'//开通日捐月捐
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
	var page=1;
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			//初始化加载项目反馈
			that.projectList(1,20);
			//that.projectList2(1,30);
			/*.on("click",".dtInput",function(){
				var projectIds = $('#projectIds').val();
				var projectId = $(this).val();
				if($(this).attr('checked')=='checked'){
					if(projectIds.indexOf(projectId)>=0){//包含	
					}
					else{//不包含
						if(projectIds==''){
							projectIds=projectId+',';
						}
						else{
							projectIds=projectIds+projectId+','
						}
						$('#projectIds').val(projectIds);
					}
				}
				else{
					if(projectIds.indexOf(projectId)>=0){//包含
						projectIds=projectIds.replace(projectId+',','');
					}
					else{//不包含
					}
					$('#projectIds').val(projectIds);
				}
				//alert($('#projectIds').val());
			})*/
			$("body").on("click",".qqim",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qqim',1): that.bshare(event,'qqim',0);
			}).on("click",".load-more",function(){
				that.projectList(Number($('#page').val())+1,20);
			}).on("click",".btnFl",function(){
				$('#dayShan').show();
			}).on("click",".dayBj",function(){
				$('#dayShan').hide();
			}).on("click",".cue1_flr",function(){
				$('#payMoney_prompt').hide();
			}).on("click","#queding",function(){
				var spCodesTemp;
				$('input:checkbox[class=dtInput]:checked').each(function(i){
				    if(0==i){
				       spCodesTemp = $(this).val();
				    }else{
				      spCodesTemp += (","+$(this).val());
				     }
				  });
				$('#projectIds').val(spCodesTemp);
				$('#projects_alert').hide();
			}).on("click","#quxiao",function(){
				$("input[class='dtInput']").attr("checked",false);
				$("input[class='dtInput_quanxuan']").attr("checked",false);
				$('#projectIds').val('');
				$('#projects_alert').hide();
			}).on("click",".dtInput_quanxuan",function(){
				if($("input[class='dtInput_quanxuan']").attr("checked")=="checked"){
					$("input[class='dtInput']").attr("checked",true);
				}
				else{
					$("input[class='dtInput']").attr("checked",false);
				}
				
			}).on("click",".dt_quanxuan",function(){
				if($("input[class='dtInput']").attr("checked")=="checked"){
					$("input[class='dtInput']").attr("checked",false);
					$("input[class='dtInput_quanxuan']").attr("checked",false);
				}
				else{
					$("input[class='dtInput']").attr("checked",true);
					$("input[class='dtInput_quanxuan']").attr("checked",true);
				}
			}).on("click",".dtperson",function(){
				if($(this).siblings(".dtInput").attr("checked")=="checked"){
					$(this).siblings(".dtInput").attr("checked",false);
				}
				else{
					$(this).siblings(".dtInput").attr("checked",true);
				}
			});
			$(".dayNav>li").click(function(){
				$(this).addClass("navFr").siblings().removeClass("navFr");
				$('#projectIds').val('');
				if($('#confrim_nav').attr('class')=='navFr'){
					$('#projects_alert').show();
				}
			});
			/**日行一善，我要结缘*/
			$('#dayBtn').click(function(){
				var money=$('#dayMoney').val();
				var projectIds=$('#projectIds').val();
				if($('#confrim_nav').attr('class')=='navFr'&&(projectIds==null||projectIds=='')){
					$('#payMoney_content').html("没有选择指定项目！");
					$('#payMoney_prompt').show();
					return;
				}
				if(isNaN(Number(money))){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				if(Number(money)<0.01){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				that.openDayDonate(money,projectIds);
				return;
			});
			 /*充值*/
		    $("#confirCharge").click(function(){
		    	var chargeMomeny = Number($('#chargeMomeny').val()),userName = $('#userName').text(),mobile = $('#mobile').text(),
		    	browser=$('#browser').val(),dayMoney=$('#dayMoney').val(),projectId=$('#projectId').val(),message=$('#message').val();
				if(typeof(projectId)=="undefined"||projectId==''){
					projectId=0;
				}
		    	if(chargeMomeny <= 0){
		    		$('#payMoney_content').html("请输入充值金额");
					$('#payMoney_prompt').show();
		    		return false;
		    	}
		    	if(browser!='wx'){
		    		window.location.href='http://www.17xs.org/visitorAlipay/tenpay/rechargedeposit_sy.do?amount='+chargeMomeny+'&touristName='+userName+'&touristMobile='+mobile+'&projectId='+projectId+'&donateTimeId='+message;
		    	}
		    	else{
		    		window.location.href='http://www.17xs.org/rechargeWapAlipay/deposit.do?amount='+chargeMomeny+'&payType=5&touristName='+userName+'&touristMobile='+mobile+'&projectId='+projectId+'&donateTimeId='+message;
		    	}
		    });
		    $('#concelCharge').click(function(){
		    	$('.cue_f').hide();
		    	$('#dayShan').hide();
		    });
		    //var donateTimeId = $('#message').val();alert(donateTimeId);
		    var donateTimeId2 = $('#message2').val();
		    if(donateTimeId2!=''&&donateTimeId2!=0){
				$('#payMoney_content').html("日捐开通成功！");
				$('#payMoney_prompt').show();
			}
		},
		projectList:function(page,pageNum){
			var that=this,address=$('.dropdown_fr>dl>dd').html(),type=$('#type').val();
			$.ajax({ 
					url:dataUrl.projectList, 
					data:{type:type,address:address,page:page,pageNum:pageNum,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==1){
							var datalist=result.obj,html=[]
							if(datalist!=null){
							var total=result.obj.total;
							$('#page').val(page);
							for(var i=0;i<datalist.length;i++){
								html.push('<div class="dropdown_list">');
								html.push('<a href="http://www.17xs.org/h5GardenProject/projectDetail.do?projectId='+datalist[i].projectId+'">');
								html.push('<div class="center_top">');
								html.push('<p class="p_fl">'+datalist[i].title+'</p>');
								html.push('<p class="p_fr">'+datalist[i].issueTime+'</p>');
								html.push('</div>');
								html.push('<p class="content_p">'+datalist[i].content+'</p>');
								html.push('<div class="center_bottom">');
								html.push('<div class="flImg" style="background: url('+datalist[i].coverImageUrl+') center no-repeat;background-size: 169%;"></div>');
								html.push('<div class="listFr">');
								html.push('<div class="list_money">');
								html.push('<li class="money_li2">目标<span>'+datalist[i].cryMoney+'</span>元</li>');
								html.push('<li class="money_li1">已筹<span>'+datalist[i].donateAmount+'</span>元</li>');
								//html.push('<li class="money_li3">进度<span>'+datalist[i].donatePercent+'</span></li>');
								html.push('</div>');
								html.push('<div class="list_name">');
								html.push('<li class="name_li1"><span class="name_sp1"></span><p>'+datalist[i].familyAddress+'</p></li>');
								html.push('<li class="name_li2"><span class="name_sp2"></span><p>'+datalist[i].workUnit+'</p></li>');
								//html.push('<li class="name_li3"><a href="javascript:void(0)" class="name_btn">我要结缘</a></li>');
								html.push('</div></div></div></a></div>');
							}
							$('.dropdown_center').html($('.dropdown_center').html()+html.join(''));
							if(Number(datalist[0].total)>Number(page)*Number(pageNum)){
								$('.load-more').html('加载更多');
							}
							else{
								$('.load-more').html('没有更多数据了');
							}
						}
						else{
							//$('.dropdown_center').html(html.join(''));
							$('.load-more').html('没有更多数据了');
						}
						}
						else{
							//$('.dropdown_center').html(html.join(''));
							$('.load-more').html('没有更多数据了');
						}
						
					}
				});	
		},
		//开通日捐
		openDayDonate:function(money,projectIds){
				$.ajax({
					url:dataUrl.openDonates,
					data:{projectIds:projectIds,money:money,category:'good'},
					success: function(result){
						if(result.errorCode == "0000"){//成功
							$('#projectIds').val('');
							$('#payMoney_content').html("日捐开通成功！");
							$('#dayShan').hide();
							$('#payMoney_prompt').show();
						}
						else if(result.errorCode == "0001"){//未登录
							window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=gardenList';
						}
						else if(result.errorCode=="0002"){//微信授权登陆
							window.location.href=result.errorMsg;
						}
						else if(result.errorCode=="0003"){//余额不足，进行充值
							$('#chargeMomeny').val(Number(money));
							$('#message').val(result.errorMsg);
							$('.cue_f').show();
						}
						else{//失败
							$('#payMoney_content').html(result.errorMsg);
							$('#dayShan').hide();
							$('#payMoney_prompt').show();
						}
					}
				});
		},
		projectList2:function(page,pageNum){
			var that=this,address=$('.dropdown_fr>dl>dd').html();type=$('#type').val();
			$.ajax({ 
					url:dataUrl.projectList2, 
					data:{type:type,address:address,page:page,pageNum:pageNum,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==1){
							var datalist=result.obj,html=[]
							if(datalist!=null){
							var total=result.obj.total;
							for(var i=0;i<datalist.length;i++){
								html.push('<li><input type="checkbox" class="dtInput" value="'+datalist[i].id+'"><p class="dtperson">'+datalist[i].workUnit+','+datalist[i].title+'</p></li>');
							}
							$('.temple_li').html(html.join(''));
						}
						}
						/*else if(result.flag == -1){//未登录
							window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=gardenList';
						}*/
					}
				});	
		}
	};
	 
	h5detail.init();
});
