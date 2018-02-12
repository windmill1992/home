
var base = 'http://www.17xs.org/';
var dataUrl = {
	getCompanyDetail : base + 'donateStep/getCompanyDetail.do'	//企业详情
};
var otherUrl = {
	defaultImgUrl:'/res/images/h5/images/donateStep/wen.png',
	projUrl:'/project/view_h5.do?projectId='
};
var page = 1;
var vm = new Vue({
	el:'#investorPage',
	data:{
		companyImgUrl:otherUrl.defaultImgUrl,
		companyName:'企业名称',
		summary:'企业介绍',
		projectNum:0,
		donateMoney:0,
		projects:[]
	}
});

var investors = {
	init:function(){
		var companyId = wxlogin.GetQueryString('companyId');
		$.ajax({
			type:"get",
			url:dataUrl.getCompanyDetail,
			data:{companyId:companyId},
			success:function(res){
				if(res.code == 1){
					var result = res.result;
					vm.companyImgUrl = result.companyImageUrl;
					vm.companyName = result.name;
					vm.summary = result.introduction;
					vm.projectNum = result.projectCount;
					vm.donateMoney = result.donateAmount;
					var projs = result.project;
					for(var i=0;i<projs.length;i++){
						vm.projects.push({
							projectUrl:otherUrl.projUrl+projs[i].projectId,
							projectImgUrl:projs[i].projectImgUrl,
							projectTitle:projs[i].projectTitle,
							totalUser:projs[i].totalUser,
							totalStep:projs[i].totalStep,
							totalAmount:projs[i].totalAmount
						});
					}
					if($('.summary p').text().length>60){
						var txt =  $('.summary p').text();
						var con = txt.substr(0,61);
						var con2 = txt.substr(61);
						var html = '<span>...<b class="viewall" id="viewAll">全部</b></span>';
						$('.summary p').html(con+html+'<span style="display: none;">'+con2+'</span>');
					}else{}
				}else{
					alert(res.msg);return;
				}
			}
		});
		
		$('body').on('click','#viewAll',function(){
			$('#viewAll').hide().parent().next().show();
			$('#viewAll').parent().remove();
		});
	}
	
};

$(function(){
	investors.init();
	setTimeout(function(){
		$('#mask').hide();
		$('#investorPage').show();
	},1000);
});

