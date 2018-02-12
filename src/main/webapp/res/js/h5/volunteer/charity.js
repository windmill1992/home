
var base = 'http://www.17xs.org/';
var dataUrl = {
	getTeamDetail: base + 'activity/getTeamDetail.do',					//团队详情
	getTeamActivityList: base + 'activity/getTeamActivityList.do',		//活动列表
	getUserType: base + 'activity/getUserType.do'						//获取用户类型
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		aid:'',
		charityLogo:'/res/images/detail/people_avatar.jpg',
		charityName:'',
		charityIntro:'',
		actNum:0,
		volNum:0,
		hasMore:3,
		serviceTime:0,
		hasCharity:false,
		activities:[]
	}
});
var page = 1;
var charity = {
	init:function(){
		var that = this;
		that.getUserType();
		var aid = that.GetQueryString('activityUserId');
		if(aid == null || aid == ''){
			$('#pageContainer').hide();
			$('.no-page').show();
		}else{
			vm.aid = aid;
			this.getTeamDetail();
			this.bindEvent();
		}
	},
	getUserType:function(){
		var that = this;
		var code = that.GetQueryString('code');
		$.ajax({
			type:"get",
			url:dataUrl.getUserType,
			data:{code:code},
			success:function(res){
				if(res.code == 1){
					vm.userType = res.result.type;
					if(vm.userType == 1 || vm.userType == 2){
						vm.hasCharity = true;
					}else if(vm.userType == 3){
						vm.hasCharity = false;
					}else{}
				}else if(res.code == 0){
					vm.client = 0;
				}else if(res.code == 2){
					vm.client = 2;
				}else{
					alert(res.msg);
				}
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#viewAll',function(){
			$('#viewAll').hide().parent().next().show();
			$('#viewAll').parent().remove();
		}).on('click','#joinGroup',function(){
			if(vm.client == 0){
				$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
			}else if(vm.client == 2){
				location.href = '/ucenter/user/Login_H5.do?flag=charity_'+vm.aid;
			}else if(vm.hasCharity == false){
				location.href = '../register/volReg.html?cname='+vm.charityName+'&joinGroup='+vm.aid;
			}
		}).on('click','#loadMore',function(){
			that.getTeamActivityList(++page,10);
		}).on('click','.activities .item .preview',function(){
			var $img = $(this).find('img');
			var currentSrc = $img.attr('src');
			var urlsSrc = [];
			$(this).closest('.item').find('img').each(function(){
				urlsSrc.push($(this).attr('src'));
			});
			wx.previewImage({
				current:currentSrc,
				urls:urlsSrc
			});
		});
	},
	getTeamDetail:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getTeamDetail,
			data:{activityUserId:vm.aid},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					vm.charityName = r.title;
					if(r.headImg != null && r.headImg != '' && r.headImg != 'null'){
						vm.charityLogo = r.headImg;
					}
					vm.charityIntro = r.introduction;
					vm.actNum = r.totalActivityNum;
					vm.volNum = r.totalVolunteerNum;
					vm.serviceTime = r.serviceTimeLength;
					that.getTeamActivityList(page,10);
					setTimeout(function(){
						if($('.summary p').text().length>80){
							var txt =  $('.summary p').text();
							var con = txt.substr(0,81);
							var con2 = txt.substr(81);
							var html = '<span>...<b class="viewall" id="viewAll">全部</b></span>';
							$('.summary p').html(con+html+'<span style="display: none;">'+con2+'</span>');
						}else{}
					},0);
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getTeamActivityList:function(pageNum,pageSize){
		var that = this;
		$('#loadmask').show();
		$.ajax({
			type:"get",
			url:dataUrl.getTeamActivityList,
			data:{pageNum:pageNum,pageSize:pageSize,activityUserId:vm.aid},
			success:function(res){
				$('#loadmask').hide();
				if(res.code == 1){
					var r = res.result;
					if(r.count == 0){
						vm.hasMore = 0;
						return;
					}else if(r.count <= pageNum*pageSize){
						vm.hasMore = 1;
					}else{
						vm.hasMore = 2;
					}
					vm.activities = vm.activities.concat(r.data);
				}else{
					alert(res.msg);
				}
			}
		});
	},
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};

$(function(){
	charity.init();
});
