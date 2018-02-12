
var base = 'http://www.17xs.org/';
var dataUrl = {
	loadCount:base + 'resposibilityReport/loadCountAppointmentForm.do',			//获取各医院各时间段预约人数
	getHospitalAppoint:base + 'newReleaseProject/getHospitalAppoint.do',		//涂氟记录查询
	updateHospitalAppoint:base + 'newReleaseProject/updateHospitalAppoint.do'	//涂氟预约
};
var vm = new Vue({
	el:'#pageContainer',
	data:{
		hasJoin:false,		//参加涂氟状态
		tufuState:0,		//预约涂氟状态
		childName:'',		//孩子姓名
		IDCard:'',			//身份证号
		parentName:'',		//家长姓名
		tufuHospital:'',	//涂氟医院
		tufuNum:'',			//涂氟次数
		lastTufuTime:'',	//最近涂氟时间
		nextTufuTime:'',	//下次涂氟时间
		needTimeAfter:''	//不能预约，需在该时间后预约
	}
});
var jcount,a;
var tufu = {
	flag:true,
	flag2:false,
	init:function(){
	    this.bindEvent();
	},
	getOrderNum:function(tel){
		var that = this;
		$.ajax({
			url:dataUrl.loadCount,
			data:{formId:17},
			success:function(result){
				that.flag = false;
				var data=result;
				if(data.flag==1){
					vm.entryNum = data.obj;
					that.getHospitalAppoint(tel);
				}else{
					that.showTips('获取预约人数失败！');
				}
			},
			error:function(){
				that.showTips('请求错误，请稍后再试！');
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#chaxun',function(){
			if(that.flag2){return;}
			var tel = $('#tel').val();
			var _this = $(this);
			if(tel==''){
				that.showTips('手机号不能为空<br/>请重新输入');
				$('#tel').focus();
				that.clearData();
			}else if(!that.telValidate(tel)){
				that.showTips('手机号格式不正确<br/>请重新输入');
				$('#tel').focus();
				that.clearData();
			}else{
				$('#loadmask').show();
				that.flag2 = true;
				if(that.flag){
					that.getOrderNum(tel);
				}else{
					that.getHospitalAppoint(tel);
				}
				
			}
	    }).on('click','#submitOrder',function(){
	    	if($(this).hasClass('disabled')){
	    		return;
	    	}else{
		    	var time = $('#order').val();
		    	if(/未预约/.test(time)){
		    		that.showTips('请选择预约时间');
		    		return;
		    	}else{
			    	$.ajax({
			    		type:"POST",
			    		url:dataUrl.updateHospitalAppoint,
			    		data:{id:vm.id,time:time},
			    		success:function(res){
			    			if(res.code == 1){
			    				that.showTips('恭喜您，预约成功！');
			    				vm.tufuState = 2;
			    				vm.nextTufuTime = time;
			    			}else{
			    				that.showTips('预约失败');return;
			    			}
			    		}
			    	});	
		    	}	
	    	}
	    }).on('focus','#tel',function(e){
	    	isShow($(this));
	    }).on('click','#tel',function(e){
	    	e.stopPropagation();
	    }).on('input propertychange','#tel',function(){
	    	isShow($(this));
	    }).on('click','#resetTel',function(e){
	    	e.stopPropagation();
	    	$('#tel').val('').focus();
	    }).on('click',function(){
	    	$('#resetTel').hide();
	    });
	    function isShow($o){
	    	var v = $o.val();
	    	if(v==''){
	    		$('#resetTel').hide();
	    	}else{
	    		$('#resetTel').show();
	    	}
	    }
	},
	getHospitalAppoint:function(tel){
		var that = this;
		$.ajax({
			type:"GET",
			url:dataUrl.getHospitalAppoint,
			data:{mobile:tel},
			success:function(res){
				$('#loadmask').hide();
				that.flag2 = false;
				if(res.code == 1){
					var curY = new Date().getFullYear();
					var result = res.result;
					vm.hasJoin = true;
					vm.childName = result.childName;
					var cd = result.iDCard;
					vm.IDCard = cd.substr(0,10)+'*******'+cd.substr(-1,1);
					vm.parentName = result.parentName;
					vm.tufuHospital = result.tufuHospital;
					vm.tufuNum = result.tufuNum;
					vm.lastTufuTime = result.lastTufuTime==null?'未涂氟':result.lastTufuTime;
					vm.id = result.id;
					vm.tufuState = result.tufuState;
					if(vm.tufuState == 0){
						var dd = vm.lastTufuTime;
						dd = dd.split(' ')[0].split('-');
						var day = new Date(dd[0],dd[1]-1,dd[2]);
						day.setDate(day.getDate() + 177);
						var mm = (day.getMonth()+1).toString();
						var mon = mm[1]?mm:('0'+mm);
						var newDay = day.getDate()<10?('0'+day.getDate()):day.getDate();
						vm.needTimeAfter = day.getFullYear()+'年'+mon+'月'+newDay+'日';
					}else if(vm.tufuState == 1){
						var html = [];
						var host = ['宁波牙科医院','鄞州口腔医院','鄞州儿童口腔医院','北仑口腔医院'];
						for(var i=0;i<host.length;i++){
							if(vm.tufuHospital.indexOf(host[i]) != -1){
								jcount = i;break;
							}else{}
						}
						a = vm.entryNum;
						html.push('<script src="/res/js/common/city.js"></script>');
						html.push('<script src="/res/js/common/city2.min.js"></script>');
						html.push('<script src="/res/js/h5/appointmentForm/time_quantum.js"></script>');
						$('body').append(html.join(''));
						setTimeout(function(){
							hos(jcount);
							new MultiPicker({
							 	input: 'order',//点击触发插件的input框的id
						        container: 'orderContainer',//插件插入的容器id
						        jsonData: $time_quantum,
						        success: function (arr) {
						        	if(/已满/.test(arr[1].value)){
						    			that.showTips('预约人数已满');
						    		}else if(/周末不能预约/.test(arr[1].value)){
						    			that.showTips('周末不能预约');
						    		}else{
							        	$("#order").addClass('selected').val(arr[0].value+' '+arr[1].value.replace(/<.*?>/ig,",").split(",")[0]);
										$('.submit').removeClass('disabled');
						    		}
						        }
						    });	
						},600);
					}else if(vm.tufuState == 2){
						if(/年/.test(result.appointmentTime)){
							vm.nextTufuTime = result.appointmentTime;
						}else{
							vm.nextTufuTime = curY + '年' + result.appointmentTime.replace(/\(.*\)/ig,' ');
						}
					}else{}
				}else if(res.code == 2){
					that.clearData();
					that.showTips('对不起，该号码的涂氟记录没有找到，或许您还没有带孩子去医院涂氟！<br/>请输入其他号码试试。');
				}else{
					alert('服务器错误，请稍后重试！');return;
				}
			}
		});
	},
	clearData:function(){
		vm.hasJoin = false;
		vm.childName = '';
		vm.IDCard = '';
		vm.parentName = '';
		vm.tufuHospital = '';
		vm.tufuNum = '';
		vm.lastTufuTime = '';
		vm.nextTufuTime = '';
		vm.needTimeAfter = '';
	},
	showTips:function(txt){
		var $t = $('#tips');
		var winh = $(window).height();
		var bot = winh / 2 - 30;
    	if(!$t.hasClass('show')){
	    	$t.html(txt).show().addClass('show').css({bottom:bot,opacity:1});
	    	setTimeout(function(){
    			$t.css({opacity:0});
	    	},3000);
	    	setTimeout(function(){
	    		$t.css({bottom:'-100px'}).removeClass('show').html('');
	    	},3500);
    	}else{
    		return;
    	}
	},
	//手机号格式验证
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(!reg.test(tel)){
	        return false;
	    }else{
	    	return true;
	    }
	}
};
$(function(){
	tufu.init();
});
