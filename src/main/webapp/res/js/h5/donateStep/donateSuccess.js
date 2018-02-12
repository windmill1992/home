
var base = 'http://www.17xs.org/';
var dataUrl = {
	getRandomCompanyInfo : base + 'donateStep/getRandomCompanyInfo.do'	//获取捐步信息
};
var otherUrl = {
	companyImgUrl:'/res/images/h5/images/donateStep/wen.png',
	projUrl:'/project/view_h5.do?projectId='
};

var vm = new Vue({
	el:'#successPage',
	data:{
		userImgUrl:otherUrl.companyImgUrl,
		projectTitle:'项目标题',
		donateSteps:0,
		companyImgUrl:otherUrl.companyImgUrl,
		companyName:'企业名称',
		randomMoney:0,
		projectUrl:otherUrl.projUrl
	}
});

var donateSuccess = {
	init:function(){
		this.getDonateInfo();
	},
	getDonateInfo:function(){
		var userId = wxlogin.getcookie('userId');
		$.ajax({
			type:"get",
			url:dataUrl.getRandomCompanyInfo,
			data:{userId:userId},
			success:function(res){
				if(res.code == 2){
					var result = res.result;
					vm.userImgUrl = result.coverImageurl;
					vm.projectTitle = result.project.projectTitle;
					vm.donateSteps = result.stepCount;
					vm.companyImgUrl = result.companyImgUrl;
					vm.companyName = result.name;
					vm.randomMoney = result.userDonatAmount;
					vm.projectUrl += result.project.projectId;
				}else{
					alert(res.msg);return;
				}
			}
		});
	}
};

$(function(){
	donateSuccess.init();
	setTimeout(function(){
		$('#mask').hide();
		$('#successPage').show();
	},1000);
});

