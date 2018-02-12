
var base = 'http://www.17xs.org/';
var dataUrl = {
	getCompanySign: base + 'activity/getCompanySign.do',		//团队查看签到记录
	getPersonalSign: base + 'activity/getPersonalSign.do',		//个人查看签到记录
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		isCharity:true,
		hasRec:true,
		hasMore:3,
		qrLogo:'/res/images/h5/images/donateStep/gh_a4d76db3a208_258.jpg',
		gsignRecords:[],
		psignRecords:[]
	}
});
var page = 1;
var qdRecord = {
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(aid == null){
			vm.hasRec = false;
		}else{
			vm.aid = aid;
			if($('#time').length > 0){
				var dd = new Date();
				var y = dd.getFullYear(), m = dd.getMonth()+1, d = dd.getDate();
				new DateSelector({
			        input : 'time',//点击触发插件的input框的id
			        container : 'timeContainer',//插件插入的容器id
			        type : 0,
			        //0：不需要tab切换，自定义滑动内容，建议小于三个；
			        //1：需要tab切换，【年月日】【时分】完全展示，固定死，可设置开始年份和结束年份
			        param : [1,1,1,0,0],
			        //设置['year','month','day','hour','minute'],1为需要，0为不需要,需要连续的1
			        beginTime : [2017,1,1],//如空数组默认设置成1970年1月1日0时0分开始，如需要设置开始时间点，数组的值对应param参数的对应值。
			        endTime : [2017,12,31],//如空数组默认设置成次年12月31日23时59分结束，如需要设置结束时间点，数组的值对应param参数的对应值。
			        recentTime : [2017,m,d],//如不需要设置当前时间，被为空数组，如需要设置的开始的时间点，数组的值对应param参数的对应值。
			        success : function(arr){
			            arr[1] = arr[1]<10?('0'+arr[1]):arr[1];
			            arr[2] = arr[2]<10?('0'+arr[2]):arr[2];
			            $("#time").val(arr[0] +"年"+ arr[1] +'月'+ arr[2] +'日');
			            page = 1;
			            vm.tt = arr[0]+'-'+arr[1]+'-'+arr[2];
			            vm.gsignRecords = [];
			            that.getCompanySign(vm.tt,page,10);
			        }
			    });	
			    m = m<10?('0'+m):m;
			    d = d<10?('0'+d):d;
			    $('#time').val(y+'年'+m+'月'+d+'日');
			    vm.tt = y+'-'+m+'-'+d;
			    that.getCompanySign(vm.tt,page,10);
			}else{
				that.getPersonalSign(page,10);
			}
			that.bindEvent();	
		}
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#loadMore',function(){
			that.getCompanySign(vm.tt,++page,10);
		}).on('click','#loadMore2',function(){
			that.getPersonalSign(++page,10);
		}).on('click','#qrCode img',function(){
			var src = this.src;
			wx.previewImage({
				current:src,
				urls:[src]
			});
		});
	},
	getCompanySign:function(time,pageNum,pageSize){
		$.ajax({
			type:"get",
			url:dataUrl.getCompanySign,
			data:{activityId:vm.aid,time:time,pageNum:pageNum,pageSize:pageSize},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					vm.qrLogo = r.logoUrl;
					if(r.count == 0){
						vm.hasMore = 0;
						return;
					}else if(r.count <= pageNum*pageSize){
						vm.hasMore = 1;
					}else{
						vm.hasMore = 2;
					}
					vm.gsignRecords = vm.gsignRecords.concat(r.data);
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=signIn_'+vm.aid;
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getPersonalSign:function(pageNum,pageSize){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getPersonalSign,
			data:{activityId:vm.aid,pageNum:pageNum,pageSize:pageSize},
			success:function(res){
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
					vm.psignRecords = vm.psignRecords.concat(r.data);
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=signIn_'+vm.aid;
				}else if(res.code == 3){
					alert('您今天还未签到！');
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
	qdRecord.init();
});
