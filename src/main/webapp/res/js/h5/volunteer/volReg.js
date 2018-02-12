
var base = 'http://www.17xs.org/';
var dataUrl = {
	addProjectVolunteer: base + 'activity/addProjectVolunteer.do',	//添加志愿者信息
	getUserType: base + 'activity/getUserType.do',					//获取用户类型
	groupNameList: base + 'activity/groupNameList.do'				//所属团队名称列表
};

var volReg = {
	cname:'',
	userType:3,
	init:function(){
		var that = this;
		that.getUserType();
		var cname = that.GetQueryString('cname');
		if(cname!='' && cname!=null){
			$('#belongGroup').val(cname);
			that.cname = cname;
		}
		var $sex = [{"value":"男","id":"001"},{"value":"女","id":"002"}];
		new MultiPicker({
		 	input: 'volSex',
	        container: 'volSexContainer',
	        jsonData: $sex,
	        success: function (arr) {
				$('#volSex').val(arr[0].value);
	        }
	    });
		new MultiPicker({
		 	input: 'volAddr',
	        container: 'volAddrContainer',
	        jsonData: $city,
	        success: function (arr) {
				if(arr.length==3){
					if(/请选择/.test(arr[2].value)){
		        			arr[2].value = '全部';
		        		}
					$("#volAddr").val(arr[0].value +"-"+arr[1].value+"-"+arr[2].value);
				}else{
					if(/请选择/.test(arr[1].value)){
	        			arr[1].value = '全部';
	        		}
					$("#volAddr").val(arr[0].value +"-"+arr[1].value);
				}
	        }
	    });
	    that.groupNameList();
		that.bindEvent();
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
					that.userType = res.result.type;
					if(that.userType == 2){
						alert('您已经是志愿者了！');
					}else{}
				}else if(res.code == 0){
					$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else if(res.code == 2){
					location.href = '/ucenter/user/Login_H5.do?flag=volReg';
				}else if(res.code == 3){
					that.showTips(res.msg);
				}else{
					alert(res.msg);
				}
			}
		});
	},
	groupNameList:function(){
		var that = this;
		$.ajax({
	    	type:"get",
	    	url:dataUrl.groupNameList,
	    	data:{},
	    	success:function(res){
	    		if(res.code == 1){
	    			var r = res.result.groupName;
	    			var $charity = [];
	    			for(var i=0;i<r.length;i++){
	    				$charity.push({
	    					"value":r[i],
	    					"id":i
	    				});
	    			}
				    new MultiPicker({
					 	input: 'belongGroup',
				        container: 'belongGroupContainer',
				        jsonData: $charity,
				        success: function (arr) {
							$('#belongGroup').val(arr[0].value);
				        }
				    });			
	    		}else{
	    			that.showTips('获取团队信息失败！');
	    		}
	    	}
	    });
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#submit',function(){
			var name = $('#volName').val();
			var sex = $('#volSex').val();
			var tel = $('#volTel').val();
			var idCard = $('#volIDCard').val();
			var addr = $('#volAddr').val();
			var prof = $('#volProfession').val();
			var scope = $('#volServiceScope').val();
			var group = $('#belongGroup').val();
			var exp = $('#volServiceExp').val();
			if(name==''){
				that.showTips('姓名不能为空！');
				$('#volName').focus();
			}else if(sex==''){
				that.showTips('性别不能为空！');
				setTimeout(function(){
					$('#volSex').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else if(tel==''){
				that.showTips('电话不能为空！');
				$('#volTel').focus();
			}else if(!that.telValidate(tel)){
				that.showTips('电话格式不正确！');
				$('#volTel').focus();
			}else if(idCard==''){
				that.showTips('身份证号不能为空！');
				$('#volIDCard').focus();
			}else if(!checkCard(idCard)){
				that.showTips('身份证号格式不正确！');
				$('#volIDCard').focus();
			}else if(addr==''){
				that.showTips('所在地不能为空！');
				setTimeout(function(){
					$('#volAddr').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else if(prof==''){
				that.showTips('职业不能为空！');
				$('#volProfession').focus();
			}else if(scope==''){
				that.showTips('服务领域不能为空！');
				$('#volServiceScope').focus();
			}else if(group==''){
				that.showTips('所属团队不能为空！');
				setTimeout(function(){
					$('#belongGroup').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else{
				$.ajax({
					type:"post",
					url:dataUrl.addProjectVolunteer,
					data:{
						name:name,
						sex:sex,
						mobile:tel,
						indentity:idCard,
						address:addr,
						position:prof,
						field:scope,
						groupName:group,
						historyService:exp
					},
					success:function(res){
						if(res.code == 1){
							that.showTips('添加成功！');
							var entry = that.GetQueryString('entry');
							setTimeout(function(){
								if(that.cname != '' && entry == null){
									var aid = that.GetQueryString('joinGroup');
									location.href = '../uCenter/charity.html?activityUserId='+aid;
								}else if(entry != '' && entry != null){
									location.href = '/visitorAlipay/tenpay/entryPay.html?activityId='+entry;
								}else{
									location.href = '../uCenter/management.html';
								}
							},2000);
						}else if(res.code == 0){
							$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
						}else if(res.code == 2){
							location.href = '/ucenter/user/Login_H5.do?flag=volReg';
						}else{
							alert(res.msg);
						}
					}
				});
			}
		}).on('input propertychange','#volServiceExp',function(){
			var h = $(this).height();
			var t = this.scrollTop;
			$(this).height(t+h);
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
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
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
	}
};

$(function(){
	volReg.init();
});
