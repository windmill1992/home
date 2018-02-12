/**
 * Created by Administrator on 2017/2/7.
 */
var base=window.baseurl;
var dataUrl={
	dataList:base+'uCenterProject/projectClaimList.do',//认领项目列表
	userInfo:base+'uCenterProject/faqiUserInfo.do',//发起人信息
	projectClaim:base+'uCenterProject/projectClaim.do',//认领项目
	zizhi:base+'enterprise/core/realName.do',//申请资质页面跳转
	qiuzhu:base+'ucenter/pindex.do'//我的	求助
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
	window.mCENnavEtSite="m-rl";	
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		var faqiUserType=$(".fqr1").children("p").children("input").val();
		var field=$(".fqr2").children("p").children("input").val();
		var projectProvince=$(".fqr3").children("p").children("input").val();
		projectClaim.dataList(p.curPage,10,faqiUserType,field,projectProvince);
	};
	projectClaim={
		init:function(){
			var that=this;
			that.dataList(1,10,'','','');
			$("body").on("mouseover",".fqr1",function(){
					$(".fqr1>ul").show();
				}).on("mouseout",".fqr1",function(){
					$(".fqr1>ul").hide();
				}).on("mouseover",".fqr2",function(){
					$(".fqr2>ul").show();
				}).on("mouseout",".fqr2",function(){
					$(".fqr2>ul").hide();
				}).on("mouseover",".fqr3",function(){
					$(".fqr3>ul").show();
				}).on("mouseout",".fqr3",function(){
					$(".fqr3>ul").hide();
				}).on("click",".faqi",function(){
					var faqiType=$(this).children("input").val();
					var faqiTypeShow=$(this).html();
					$(".fqr1>ul").hide();
					$(".fqr1").children("p").html(faqiTypeShow);
					 var field=$(".fqr2").children("p").children("input").val();
					 var projectProvince=$(".fqr3").children("p").children("input").val();
					 //查询列表
					 that.dataList(1,10,faqiType,field,projectProvince);
				}).on("click",".lx",function(){
					var field=$(this).children("input").val();
					var fieldShow=$(this).html();
					$(".fqr2>ul").hide();
					$(".fqr2").children("p").html(fieldShow);
					 var faqiUserType=$(".fqr1").children("p").children("input").val();
					 var projectProvince=$(".fqr3").children("p").children("input").val();
					//查询列表
					that.dataList(1,10,faqiUserType,field,projectProvince);
				}).on("click",".dd",function(){
					var projectProvince=$(this).children("input").val();
					var projectProvinceShow=$(this).html();
					$(".fqr3>ul").hide();
					$(".fqr3").children("p").html(projectProvinceShow);
					 var faqiUserType=$(".fqr1").children("p").children("input").val();
					 var field=$(".fqr2").children("p").children("input").val();
					//查询列表
					that.dataList(1,10,faqiUserType,field,projectProvince);
				}).on("click",".colNew",function(){
					var userId=$(this).attr("value");
					that.userInfo(userId);
				}).on("click",".closeUserInfo",function(){
					$("#userInfo").hide();
				}).on("click","#renlingBtn",function(){
					var title = "您确认认领项目：“"+$(this).attr("value").split('|')[0]+"”吗？";
					$("#confirm").val($(this).attr("value").split('|')[1]);
					$(".renlingAlert").children("span").html(title);
					$("#renling").show();
				}).on("click","#renlingClose",function(){
					$("#renling").hide();
					$("#zizhi").hide();
					$("#renlingSuccess").hide();
				}).on("click","#confirm",function(){
					//认领
					var projectId = $(this).attr("value");
					that.projectClaim(projectId);
					$("#renling").hide();
				}).on("click","#cancel",function(){
					$("#renling").hide();
				}).on("click","#zizhiBtn",function(){
					$("#zizhi").show();
				}).on("click","#zizhi_confirm",function(){
					$("#zizhi").hide();
					window.location.href=dataUrl.zizhi;
				}).on("click","#zizhi_cancel",function(){
					$("#zizhi").hide();
				}).on("click","#renlingSuccess_confirm",function(){
					var faqiUserType=$(".fqr1").children("p").children("input").val();
					var field=$(".fqr2").children("p").children("input").val();
					var projectProvince=$(".fqr3").children("p").children("input").val();
					that.dataList(1,10,faqiUserType,field,projectProvince);
					$("#renlingSuccess").hide();
				})
		},
		dataList:function(page,num,faqiUserType,field,projectProvince){
			var that=this,html=[],flag=false;
			if(page<2){flag=true};
			if(faqiUserType=='全部'||faqiUserType=='发起人'){
				faqiUserType='';
			}
			if(field=='全部'||field=='项目类型'){
				field='';
			}
			if(projectProvince=='全部'||projectProvince=='项目地点'){
				projectProvince='';
			}
			$.ajax({
				url:dataUrl.dataList,
				data:{pageSize:page,pageNum:num,field:field,type:faqiUserType,location:projectProvince,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.dataList);
					}else{if(result.obj!=null){
						var data=result.obj,total=data.pages,pageSize=data.pageSize,len=data.resultData.length;
						len=len>=pageSize?pageSize:len; 
						var showState=$('#showState').val();
						if(total>0){
							for(var i=0;i<len;i++){
								html.push('<dd>');
								html.push('<ul>');
								html.push('<li class="col1"><a href="http://www.17xs.org/project/view.do?projectId='+data.resultData[i].id+'" target="_blank">['+data.resultData[i].fieldChinese+']'+data.resultData[i].title+'</a><span class="time">发布时间：'+data.resultData[i].content+'</span></li>');
								html.push('<li class="col3">'+data.resultData[i].cryMoney+'元</li>');
								if(showState==0){
									html.push('<li class="col4" value="'+data.resultData[i].userId+'"><a href="javaScript:void(0);"><span id="zizhiBtn">'+data.resultData[i].uname+'</span></a></li>');
								}
								else{
									html.push('<li class="col4 colNew" value="'+data.resultData[i].userId+'"><a href="javaScript:void(0);">'+data.resultData[i].uname+'</a></li>');
								}
								html.push('<li class="col5">'+data.resultData[i].fieldChinese+'</li>');
								html.push('<li class="col6"><a href="http://www.17xs.org/project/view.do?projectId='+data.resultData[i].id+'" target="_blank" class="showPreview" data="">查看详情</a>');
								if(showState==0){
									html.push('<a href="javaScript:void(0);"><span id="zizhiBtn">认领</span></a>');//style="color:#bbb7b7"
								}
								else{
									html.push('<a href="javaScript:void(0);"><span id="renlingBtn" value="'+data.resultData[i].title+'|'+data.resultData[i].id+'">认领</span></a>');
								}
								html.push('</li>');
								html.push('</ul>');
								html.push('</dd>');
							}
						}else{
							html.push('<div class="noinfo">项目去火星了 ，请重新筛选条件 </div>');
						}
						$('.noinfo').remove();
						$('#listShow>dd').remove();
						$('#listShow').append(html.join(''));	
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}else if(total<2){
							p.pageInit({pageLen:total,isShow:false}); 
						} 
					}
					else{
						html.push('<div class="noinfo">项目去火星了 ，请重新筛选条件 </div>');
						$('.noinfo').remove();
						$('#listShow>dd').remove();
						$('#lstPages').hide();
						$('#listShow').append(html.join(''));
					}
					}
				},
				error   : function(r){ 
					d.alert({content:'获取求助列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		},
		userInfo:function(userId){
			var that=this;
			$.ajax({
				url:dataUrl.userInfo,
				data:{id:userId,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.userInfo);
					}else{if(result.obj!=null){
						var data=result.obj;
						$("#name").html("姓名："+data.realName);
						$("#identity").html("身份证："+data.idCard);
						$("#tel").html("电话："+data.mobileNum);
						$("#address").html("所在地："+data.familyAddress);
						$("#userInfo").show();
					}
					else{
					}
					}
				},
				error   : function(r){ 
					d.alert({content:'获取求助列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		},
		projectClaim:function(projectId){
			var that=this;
			$.ajax({
				url:dataUrl.projectClaim,
				data:{projectId:projectId,t:new Date()},
				success: function(result){
					if(result.flag=='0' || result.flag=='-1'){//失败;未登录
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else{//成功
						//d.alert({content:result.errorMsg,type:'ok'});
						$("#renlingSuccess").show();
						return true;
					}
				},
				error   : function(r){ 
					d.alert({content:'获取求助列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	projectClaim.init();
});