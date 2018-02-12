
var base = 'http://www.17xs.org/';
var dataUrl = {
	wxLogin : base + 'donateStep/wxLogin.do', 	//微信授权登录
	getUserDonateStepRecord : base + 'donateStep/getUserDonateStepRecord.do',	//捐步记录分页
	getUserStatistics : base + 'donateStep/getUserStatistics.do',	//统计（累计捐赠步数，累计捐赠金额）
};
var otherUrl = {
	defaultProjImgUrl:'/res/images/h5/images/donateStep/activity_default.png',
	projUrl:'/project/view_h5.do?projectId='
}

var page = 1;
var vm = new Vue({
	el:'#recordPage',
	data:{
		recordList:[],
		totalStep:0,
		totalMoney:0
	}
});

//时间戳格式化
Vue.filter('dateFmt',function(timestamp){
	var dd = new Date(timestamp);
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;
	var d = dd.getDate();
	var h = dd.getHours();
	var mi = dd.getMinutes();
	var s = dd.getSeconds();
	m = m<10?('0'+m):m;
	d = d<10?('0'+d):d;
	h = h<10?('0'+h):h;
	mi = mi<10?('0'+mi):mi;
	s = s<10?('0'+s):s;
	return y+'-'+m+'-'+d+' '+h+':'+mi+':'+s;
});

var xsrecord = {
	init:function(){
		var that = this;
		this.getUserDonateStepRecord(page,10);
		this.getUserStatistics();
		$('body').on('click','.loadmore',function(){
			if($(this).html() == '没有更多数据了'){
				return;
			}else{
				that.getUserDonateStepRecord(++page,10);
			}
		});
	},
	getUserDonateStepRecord:function(pageNum,pageSize){
		var userId = wxlogin.getcookie('userId');
		$.ajax({
			url:dataUrl.getUserDonateStepRecord,
			type:'GET',
			data:{userId:userId,pageNum:pageNum,pageSize:pageSize},
			success:function(res){
				var result = res.result;
				if(result.length>0){
					for(var i=0;i<result.length;i++){
						if(!result[i].projectCoverImageUrl){
							result[i].projectCoverImageUrl = otherUrl.defaultProjImgUrl;
						}else{}
						vm.recordList.push({
							donateTime:result[i].donateTime,									//捐赠时间
							donateStep:result[i].donateStep,									//捐赠步数
							donateAmount:result[i].donateAmount,								//捐赠金额
							projectTitle:result[i].projectTitle,								//捐赠项目标题
							projectUrl:otherUrl.projUrl+result[i].projectId,					//捐赠项目链接
							projectCoverImageUrl:result[i].projectCoverImageUrl,				//捐赠项目图片
						});
					}
				}else{
					var p = '<p style="text-align: center;padding: 20px;">您还未有捐步信息</p>';
					$('#main-list-wrap').append(p);
				}
				if(res.state){
					var a = '<a href="javascript:;" class="loadmore">点击加载更多</a>';
					$('#recordList').append(a);
				}else if(page>1){
					var a = '<a href="javascript:;" class="loadmore">没有更多数据了</a>';
					$('#recordList').append(a);
				}else{}
			}
		});
	},
	getUserStatistics:function(){
		var userId = wxlogin.getcookie('userId');
		$.ajax({
			type:"get",
			url:dataUrl.getUserStatistics,
			data:{userId:userId},
			success:function(res){
				if(res.code == 1){
					var result = res.result;
					vm.totalStep = result.totalStep;
					vm.totalMoney = result.totalMoney;
				}else{
//					alert(res.msg);
					return;
				}
			}
		});
	}
};

$(function(){
	xsrecord.init();
	setTimeout(function(){
		$('#mask').hide();
		$('#recordPage').show();
	},1000)
});

