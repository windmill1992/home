
var base = 'http://www.17xs.org/';
var dataUrl = {
	getLaunchActivityInfo: base + 'activity/getLaunchActivityInfo.do',		//发起团队详情
	getEntryActivityInfo: base + 'activity/getEntryActivityInfo.do',		//报名团队详情
	getMyActivityInfo: base + 'activity/getMyActivityInfo.do',				//发起活动列表
	getMyEntryActivityInfo: base + 'activity/getMyEntryActivityInfo.do',	//报名活动列表
	batchSignOutTime: base + 'activity/batchSignOutTime.do',				//批量签出
	singleSignOutTime: base + 'activity/singleSignOutTime.do',				//单人签出
	addTeamActivity: base + 'activity/addTeamActivity.do'					//修改头像
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		isCharity:false,
		userAvatar:'/res/images/detail/people_avatar.jpg',
		userName:'',
		state:0,
		hasMore:-1,
		charity:{
			actNum:0,
			groupMemberNum:0,
			signNum:0
		},
		personal:{
			entriedActNum:0,
			signNum:0,
			serviceTime:0
		},
		activities:[]
	}
});
var page = 1;
var management = {
	photoData:[],
	init:function(){
		this.ajaxForm($('#form5'));
		if($('#pubPage').length > 0){
			this.getLaunchActivityInfo();
			vm.isCharity = true;
		}else{
			this.getEntryActivityInfo();
			vm.isCharity = false;
		}
		this.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#actSwitch .subtab a',function(){
			if(vm.isCharity){
				$(this).siblings().removeClass('on');
				$(this).addClass('on');
				page = 1;
				vm.activities = [];
				if($(this).index() == 1){
					vm.state = 0;
					that.getMyActivityInfo(0,page,10);
				}else{
					vm.state = 1;
					that.getMyActivityInfo(1,page,10);
				}
			}
		}).on('click','.activities .item .batch-signout',function(){
			var aid = $(this).attr('data-id');
			$.ajax({
				type:"post",
				url:dataUrl.batchSignOutTime,
				data:{activityId:aid},
				success:function(res){
					if(res.code == 1){
						that.showTips('批量签出成功！');
					}else{
						that.showTips(res.msg);
					}
				}
			});
		}).on('click','.activities .item .signout',function(){
			var aid = $(this).attr('data-id');
			$.ajax({
				type:"post",
				url:dataUrl.singleSignOutTime,
				data:{activityId:aid},
				success:function(res){
					if(res.code == 1){
						that.showTips('签出成功！');
					}else{
						that.showTips('您已签出或未签到！');
					}
				}
			});
		}).on('click','#loadMore',function(){
			if(vm.isCharity){
				that.getMyActivityInfo(vm.state,++page,10);
			}else{
				that.getMyEntryActivityInfo(vm.state,++page,10);
			}
			
		}).on("change",".file-input",function(){
			var p = that.photoPic(this);
			if(p){
				vm.userAvatar = '/res/images/common/bar1.gif';
				$('#form5').submit();
				var tempObj = $(this).parent();
				var id = $(this).attr('id');
				$(this).remove();
				tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
			}
			return false;
		});
	},
	getLaunchActivityInfo:function(){
		var that = this;
		var code = that.GetQueryString('code');
		$.ajax({
			type:"get",
			url:dataUrl.getLaunchActivityInfo,
			data:{code:code},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					vm.userName = r.title?r.title:'？？？';
					vm.userAvatar = r.headImg?r.headImg:'/res/images/detail/people_avatar.jpg';
					vm.charity.actNum = r.totalActivityNum;
					vm.charity.groupMemberNum = r.totalVolunteerNum;
					vm.charity.signNum = r.signNum;
					vm.id = r.id;
					that.getMyActivityInfo(1,page,10);
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=managementpub';
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getEntryActivityInfo:function(){
		var that = this;
		var code = that.GetQueryString('code');
		$.ajax({
			type:"get",
			url:dataUrl.getEntryActivityInfo,
			data:{code:code},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					vm.userName = r.title?r.title:'？？？';
					vm.userAvatar = r.headImg?r.headImg:'/res/images/detail/people_avatar.jpg';
					vm.state = -1;
					vm.personal.entriedActNum = r.totalActivityNum;
					vm.personal.signNum = r.signNum;
					vm.personal.serviceTime = r.serviceTimeLength==null?0:r.serviceTimeLength;
					that.getMyEntryActivityInfo(-1,page,10);
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=managemententry';
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getMyActivityInfo:function(state,pageNum,pageSize){
		var that = this;
		$('#loadmask').show();
		$.ajax({
			type:"get",
			url:dataUrl.getMyActivityInfo,
			data:{state:state,pageNum:pageNum,pageSize:pageSize},
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
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=managementpub';
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getMyEntryActivityInfo:function(state,pageNum,pageSize){
		var that = this;
		$('#loadmask').show();
		$.ajax({
			type:"get",
			url:dataUrl.getMyEntryActivityInfo,
			data:{state:state,pageNum:pageNum,pageSize:pageSize},
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
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=managemententry';
				}else{
					alert(res.msg);
				}
			}
		});
	},
	photoPic:function(file){
	    var filepath = $(file).val();				
	    var extStart = filepath.lastIndexOf(".") + 1;		
		var ext = filepath.substring(extStart,filepath.length).toUpperCase();		
		if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP'){	
			alert('上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！');
			return false;
		}
		var file_size = 0;
		file_size = file.files[0].size;
		var size = file_size / 1024;
		if (size > 2048) {
			alert("上传的图片大小不能超过2M！");
			return false;
		} 
		return true;
	},
	ajaxForm:function(obj){
		var that=this;
		$(obj).ajaxForm({
			beforeSend: function() {},
			uploadProgress: function(event, position, total, percentComplete) {},
			success: function(data) {
				data = data.replace("<PRE>", "").replace("</PRE>", "");
				var jsons = eval('(' + data + ')'); 
				if(jsons.result==1){
					alert(jsons.error);
					return false;
				}else{
					var imgBox=obj.find('.addList');
					that.photoData.push(jsons.imageId);
					var num = obj.attr('id').substr(4);
					vm.userAvatar = jsons.imageUrl;
					imgBox.children('.item').attr('id',jsons.imageId);
					$.ajax({
						type:"post",
						url:dataUrl.addTeamActivity,
						data:{id:vm.id,coverImageId:jsons.imageId},
						success:function(res){
							if(res.code == 4){
								that.showTips('修改头像成功！');
								that.getCompanyInfo();
							}else{
								that.showTips('修改头像失败！');
							}
						}
					});
				}
			},
			error: function(data) {
				alert('上传失败！');
			}
		}); 
	},
	//提示信息
	showTips:function(txt){
		var $tips = $('#tips');
		if($tips.is(':hidden')){
			$tips.text(txt).fadeIn();
			setTimeout(function(){
				$tips.css({'transform':'scale(0.5)'}).fadeOut(function(){
					$(this).text('').css({'transform':'scale(1)'});
				});
			},2000);	
		}else{
			return;
		}
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
	management.init();
});
