
var base = 'http://www.17xs.org/';
var dataUrl = {
	addActivity: base + 'activity/addActivity.do',				//添加编辑活动信息
	getUserType: base + 'activity/getUserType.do',				//获取用户类型
	getActivityDetail: base + 'activity/getActivityDetil.do'	//获取活动信息
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		userType:0,
		tishi:'',
		unitPrice:0,
		atime:'',
		address:'',
		address2:''
	}
});

var activityPub = {
	opts:[],
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(aid == null || aid == ''){
			that.showTips('活动不存在！');
			setTimeout(function(){
				location.href = 'activityEdit.html';
			},2000);
		}else{
			vm.aid = aid;
			that.getUserType();
			
			new MultiPicker({
				input: 'actAddr',//点击触发插件的input框的id
		        container: 'actAddrContainer',//插件插入的容器id
		        jsonData: $city,
		        success:function(arr){
		        	if(arr.length==3){
		        		if(/请选择/.test(arr[2].value)){
		        			arr[2].value = '全部';
		        		}
						$("#actAddr").val(arr[0].value +arr[1].value+arr[2].value);
					}else{
						if(/请选择/.test(arr[1].value)){
		        			arr[1].value = '全部';
		        		}
						$("#actAddr").val(arr[0].value +arr[1].value);
					}
		        }
			});
			
			new MultiPicker({
			 	input: 'addOption',//点击触发插件的input框的id
		        container: 'addOptionContainer',//插件插入的容器id
		        jsonData: $actOptions,
		        success: function (arr) {
		        	for(var i=0;i<that.opts.length;i++){
		        		if(arr[0].id == that.opts[i]){
		        			that.showTips('已存在，请重新选择！');
		        			return;
		        		}
		        	}
					var html = [];
					html.push('<div class="ae-title add"><p class="tip">');
					html.push('<a href="javascript:;" class="del" data-id="'+arr[0].id+'">')
					html.push('<img src="/res/images/h5/images/volunteer/del.png"/></a>'+arr[0].value+'</p>');
					html.push('<div class="amount">');
					html.push('<input type="text" />');
					html.push('</div></div>');
					$('.add-other').before(html.join(''));
					that.opts.push(arr[0].id);
					if(that.opts.length >= $actOptions.length){
						$('.add-other').hide();
					}else{
						$('.add-other').show();
					}
		        }
		    });
			that.bindEvent();	
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
					if(vm.userType == 2){
						vm.tishi = '您不是管理员，无法发布活动，请联系管理员';
						$('#userDialog .sbtn:first-child').hide();
					}else if(vm.userType == 3){
						vm.tishi = '您不是管理员，无法发布活动，是否去创建团队后再发布活动？'
					}else{
						that.getActivityDetail();
					}
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=pubAct?activityId='+vm.aid;
				}else if(res.code == 3){
					that.showTips(res.msg);
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getActivityDetail:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getActivityDetail,
			data:{activityId:vm.aid},
			success:function(res){
				if(res.code == 1){
					var r = res.result.data;
					vm.unitPrice = r.price?r.price:0;
					var arr = r.customItem.split(';');
					vm.atime = arr[0].split(':')[1];
					vm.address = arr[1].split('-')[0].split(':')[1];
					$('#actAddr').val(vm.address);
					vm.address2 = arr[1].split('-')[1];
					if(arr[2]){
						var s2 = arr[2].split(':');
						that.appendHtml(s2[0],s2[1]);
					}
					if(arr[3]){
						var s3 = arr[3].split(':');
						that.appendHtml(s3[0],s3[1]);
					}
					if(arr[4]){
						var s4 = arr[4].split(':');
						that.appendHtml(s4[0],s4[1]);
					}
				}else{
					that.showTips(res.msg);
				}
			}
		});
	},
	appendHtml:function(title,value){
		var that = this;
		var id = '';
		for(var i=0;i<$actOptions.length;i++){
			if(value == $actOptions[i].value){
				id = $actOptions[i].id;
				break;
			}
		}
		var html = [];
		html.push('<div class="ae-title add"><p class="tip">');
		html.push('<a href="javascript:;" class="del" data-id="'+id+'">')
		html.push('<img src="/res/images/h5/images/volunteer/del.png"/></a>'+title+'</p>');
		html.push('<div class="amount">');
		html.push('<input type="text" value="'+value+'" />');
		html.push('</div></div>');
		$('.add-other').before(html.join(''));
		that.opts.push(id);
		if(that.opts.length >= $actOptions.length){
			$('.add-other').hide();
		}else{
			$('.add-other').show();
		}
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#userDialog .closeDialog, #userDialog .quxiao',function(){
			$('#userDialog').addClass('hide');
			setTimeout(function(){
				$('#userDialog').removeClass('show hide');
			},400);
		}).on('click','#userDialog .sure',function(){
			$('#userDialog').addClass('hide');
			setTimeout(function(){
				$('#userDialog').removeClass('show hide');
				if(vm.userType == 2){
					location.href = '../register/groupReg.html';
				}
			},400);
		}).on('click','#publish',function(){
			var money = $('#entryPay').val();
			var time = $('#actTime').val();
			var addr = $('#actAddr').val();
			var addrDetail = $('#actAddrDetail').val();
			var other = '',flag = false;
			var $add = $('.ae-title.add');
			if(money == ''){
				that.showTips('报名费不能为空！');
				$('#entryPay').focus();
			}else if(isNaN(Number(money))){
				that.showTips('请填写正确数字！');
				$('#entryPay').focus();
			}else if(time == ''){
				that.showTips('活动时间不能为空！');
				$('#actTime').focus();
			}else if(addr == ''){
				that.showTips('活动地点不能为空！');
				setTimeout(function(){
					$('#actAddr').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else if(addrDetail == ''){
				that.showTips('活动详细地点不能为空！');
				$('#actAddrDetail').focus();
			}else{
				if($add.length>0){
					$add.each(function(){
						var $inp = $(this).find('input');
						var v = $inp.val();
						var str = $(this).find('.tip').text();
						if(v){
							if((str.indexOf('电话')!=-1 || str.indexOf('手机')!=-1) && !that.telValidate(v)){
								that.showTips('手机号格式不正确！');
								$inp.focus();
								flag = true;
								return;
							}else{
								other += str+':'+v+';';
							}
						}
					});
				}
				if(!flag){
					money = Number(parseFloat(money).toFixed(2));
					var items = '';
					items += '活动时间:'+time+';活动地点:'+addr+'-'+addrDetail+';'+other;
					$.ajax({
						type:"post",
						url:dataUrl.addActivity,
						data:{unitPrice:money,customItem:items,id:vm.aid,address:addr},
						success:function(res){
							if(res.code == 1){
								that.showTips('发布成功!');
								setTimeout(function(){
									location.href = 'entryCondition.html?activityId='+vm.aid;
								},2000);
							}
						}
					});
				}else{
					return;
				}
			}
		}).on('click','.ae-title.add .del',function(){
			$(this).closest('.add').remove();
			for(var i=0;i<that.opts.length;i++){
				if(that.opts[i] == $(this).attr('data-id')){
					that.opts.splice(i,1);
					$('.add-other').show();
					break;
				}
			}
		});
	},
	//手机号格式验证
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(!reg.test(tel)){
	        return false;
	    }else{
	    	return true;
	    }
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
	activityPub.init();
});
