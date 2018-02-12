
var base = 'http://www.17xs.org/';
var dataUrl = {
	getPubInfo: base + 'project/oneProjectFaqiUserinformation.do',		//发起人信息
	getProjList: base + 'project/oneProjectFaqiUserList.do'				//发起人项目发布列表
};
var page = 1;
var vm = new Vue({
	el:'#pageContainer',
	data:{
		pubImgUrl:'',
		pubName:'善园',
		summary:'......',
		totalAmount:0,
		peopleNum:0,
		projects:[],
		hasMore:-1
	}
});
var page = 1;
var publisher = {
	uid:'',
	init:function(){
		var that = this;
		var userId = that.GetQueryString('userId');
		if(userId == null){
			$('#pageContainer').hide();
			alert('暂无发起人信息！');
		}else{
			that.uid = userId;
			that.getPubInfo();
		}
	},
	getPubInfo:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getPubInfo,
			data:{userId:that.uid},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					if(r.coverImageUrl){
						vm.pubImgUrl = r.coverImageUrl;
					}else{
						vm.pubImgUrl = '/res/images/detail/people_avatar.jpg';
					}
					vm.pubName = r.name;
					vm.totalAmount = r.donatAmount;
					vm.peopleNum = r.donationNum;
					if(r.introduction!=null && r.introduction!='' && r.introduction.length > 60){
						var con = r.introduction.substr(0,61);
						var con2 = r.introduction.substr(61);
						var html = '简介：' + con + '<span>...<b class="viewall" id="viewAll">全部</b></span>';
						html += '<span style="display: none;">' + con2 + '</span>';
						vm.summary = html;
					}else{
						if(r.introduction==null || r.introduction=='' || r.introduction=='null'){
							r.introduction = '无';
						}
						vm.summary = '简介：' + r.introduction;
					}
					that.getProjList(page,10);
					that.bindEvent();
				}else{
					$('#pageContainer').hide();
					alert(res.msg);
				}
			}
		});
	},
	getProjList:function(pn,ps){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getProjList,
			data:{userId:that.uid,pageNum:pn,pageSize:ps},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					if(r.count == 0 || !r.count){
						vm.hasMore = 0;
						return;
					}else if(r.count <= pn * ps){
						vm.hasMore = 1;
					}else{
						vm.hasMore = 2;
					}
					vm.projects = vm.projects.concat(r.data);
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
		}).on('click','#loadmore',function(){
			that.getProjList(++page,10);
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
	publisher.init();
});

