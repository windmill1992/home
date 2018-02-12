
var base = 'http://www.17xs.org/';
var dataUrl = {
	getEntryInfos: base + 'activity/getEntryInfos.do',				//报名明细
	getCountEntry: base + 'activity/getCountEntry.do',				//报名明细统计
	activityUserDetail: base + 'activity/activityUserDetail.do',	//查询志愿者详情
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		hasRec:true,
		hasMore:3,
		totalSignCount:0,
		signedCount:0,
		noSignCount:0,
		entryList:[],
		otherData:[]
	}
});
var page = 1;
var entryDetail = {
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(aid == null){
			vm.hasRec = false;
		}else{
			vm.aid = aid;
			that.getCountEntry();
			that.bindEvent();
		}
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#qdTab a',function(){
			$('#qdTab a').removeClass('on');
			$(this).addClass('on');
			page = 1;
			var state = $(this).attr('data-state');
			vm.state = state;
			vm.hasMore = 3;
			vm.entryList = [];
			that.getEntryInfos(state,page,10);
		}).on('click','#loadMore',function(){
			that.getEntryInfos(vm.state,++page,10);
		}).on('click','#entryList .item .arrow',function(){
			$('#loadmask').show();
			vm.otherData = [];
			var id = $(this).attr('data-id');
			$.ajax({
				type:"get",
				url:dataUrl.activityUserDetail,
				data:{formId:id},
				success:function(res){
					$('#loadmask').hide();
					if(res.code == 1){
						$('#otherInfoDialog').addClass('show');
						var r = res.result;
						for(a in r){
							switch(a){
								case 'name':
									vm.otherData.push('姓名：'+r[a]);break;
								case 'sex':
									vm.otherData.push('性别：'+r[a]);break;
								case 'age':
									vm.otherData.push('年龄：'+r[a]);break;
								case 'address':
									vm.otherData.push('地址：'+r[a]);break;
								case 'mobile':
									vm.otherData.push('手机号：'+r[a]);break;
								case 'position':
									vm.otherData.push('职位：'+r[a]);break;
								case 'field':
									vm.otherData.push('志愿服务领域：'+r[a]);break;
								case 'groupName':
									vm.otherData.push('所属团队：'+r[a]);break;
								case 'historyService':
									vm.otherData.push('服务经历：'+r[a]);break;
								case 'information':
									var j = JSON.parse(r[a].split(';')[2]);
									for(b in j){
										vm.otherData.push(b+'：'+j[b]);break;
									}
								default:break;
							}
						}
					}else{
						alert(res.msg);
					}
				}
			});
		}).on('click','#otherInfoDialog .closeDialog',function(){
			$('#otherInfoDialog').addClass('hide');
			setTimeout(function(){
				$('#otherInfoDialog').removeClass('show hide');
			},400);
		});
	},
	getCountEntry:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getCountEntry,
			data:{activityId:vm.aid},
			success:function(res){
				if(res.code == 1){
					var r = res.result.data;
					vm.totalSignCount = r.totalSignCount;
					vm.signedCount = r.signedCount;
					vm.noSignCount = r.noSignCount;
					that.getEntryInfos(2,page,10);
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=entryDetail_'+vm.aid;
				}else if(res.code == 3){
					alert(res.msg);
				}
			}
		});
	},
	getEntryInfos:function(state,pageNum,pageSize){
		var that = this;
		$('#loadmask').show();
		$.ajax({
			type:"get",
			url:dataUrl.getEntryInfos,
			data:{activityId:vm.aid,state:state},
			success:function(res){
				$('#loadmask').hide();
				if(res.code == 1){
					var r= res.result;
					vm.count = r.count;
					if(r.count == 0){
						vm.hasMore = 0;
						return;
					}else if(r.count <= pageNum*pageSize){
						vm.hasMore = 1;
					}else{
						vm.hasMore = 2;
					}
					if(vm.entryList.length > 0){
						vm.entryList = vm.entryList.concat(r.data);
					}else{
						vm.entryList = r.data;
					}
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

entryDetail.init();
