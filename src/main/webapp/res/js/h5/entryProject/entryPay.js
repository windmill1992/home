
var base = 'http://www.17xs.org/';
var dataUrl = {
	commonPay : base + 'visitorAlipay/tenpay/commonPay.do',					//捐款支付
	getProjectInfo : base + 'resposibilityReport/itemSignForm_detail.do',	//项目报名详情
	saveFormNew : base + 'resposibilityReport/saveFormNew.do',				//保存报名信息	
	isActivityVolunteer: base + 'activity/isOrNotActivityVolunteer.do',		//判断用户是否已经成为此活动的志愿者
	getActivityFeedBack: base + 'activity/getActivityFeedBack.do',			//活动反馈列表
	isActivityAdmin: base + 'activity/isOrNotActivityAdmin.do'				//活动反馈列表
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		projectLogo:'',
		projectId:'',
		projectTitle:'',
		projectSubTitle:'',
		entryNum:0,
		unitPrice:0,
		customItem:[],
		realName:'',
		mobile:'',
		content:'',
		signList:[],
		endDay:0,
		endTime:'',
		entrierUrl:'/webhtml/view/h5/entryProject/entryList.html?formId=',
		infoList:[],
		dh:0,
		flag:false,
		imgSrcs:[],
		aid:'',
		isSelf:false,
		fdList:[]
	}
});

var entry = {
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(isNaN(aid)){
			$('#pageContainer').hide();
			alert('暂无活动信息！');
		}else{
			vm.aid = aid;
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){
				$('body').append('<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"><\/script>');
				vm.ua = 'wx';
			}else{
				$('body').append('<script src="/res/js/common/TouchSlide.1.1.js"><\/script>');
				vm.ua = 'h5';
			}
			that.isActivityAdmin();
			that.getProjectInfo();
			that.getFeedbackList();
			that.bindEvent();
		}
	},
	wxShare:function(){
		wx.config({
			debug : false,
			appId : vm.appId,
			timestamp : vm.timestamp,
			nonceStr : vm.noncestr,
			signature : vm.signature,
			jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','previewImage' ]
		});

		wx.ready(function(){
			wx.onMenuShareAppMessage({
				title : vm.projectTitle, // 分享标题
				desc : '', // 分享描述
				link : 'http://www.17xs.org/visitorAlipay/tenpay/entryPay.html?activityId='+vm.aid, // 分享链接
				imgUrl : vm.projectLogo, // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					//用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/visitorAlipay/tenpay/entryPay.html?activityId='+vm.aid;
				},
				cancel : function() {
					//用户取消分享后执行的回调函数
				}
			});
					
			wx.onMenuShareTimeline({
				title : vm.projectTitle, // 分享标题
				link : 'http://www.17xs.org/visitorAlipay/tenpay/entryPay.html?activityId='+vm.aid, // 分享链接
				imgUrl : vm.projectLogo, // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/visitorAlipay/tenpay/entryPay.html?activityId='+vm.aid;
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
	},
	bindEvent:function(){
		var that = this;
		var $pay = $('#payDialog');
		$('body').on('click','#lookmore',function(){
			var txt = $(this).text();
			var $con = $('#det_act .content');
			var oldcon = vm.content;
			if(txt=='查看全文'){
				$con.html(oldcon);
				$('.detail-mask').removeClass('detail-mask-bg');
				$(this).text('收起');
			}else{
				$con.html(vm.smallcon);
				$('.detail-mask').addClass('detail-mask-bg');
				$(this).text('查看全文');
				$('#det_act')[0].scrollIntoView();
			}
		}).on('click','#toEntry',function(){
			$.ajax({
				type:"get",
				url:dataUrl.isActivityVolunteer,
				data:{activityId:vm.aid},
				success:function(res){
					if(res.code == 2){
						location.href ='/ucenter/user/Login_H5.do?flag=entryPay_'+vm.actId;
					}else if(res.code == 0){
						$('body').append('<script type="text/javascript" src="/res/js/h5/donateStep/wxLogin.js"></script>');
					}else if(res.code == 6){
						that.showTips('不能报名自己发起的活动！');
					}else if(res.code == 1){
						if(!vm.flag){
							vm.dh = $pay.height();
							vm.flag = true;	
						}else{}
						$pay.height(0);
						$('.mask').show();
						$pay.show().animate({height:vm.dh+'px'},'linear');			
					}else if(res.code == 4){
						if(vm.type == 1){
							that.showTips('您还不是志愿者，请先注册！');
							setTimeout(function(){
								location.href = '/webhtml/view/h5/volunteer/register/volReg.html?entry='+vm.aid+'&cname='+vm.cname;
							},2000);
						}else{
							if(!vm.flag){
								vm.dh = $pay.height();
								vm.flag = true;	
							}else{}
							$pay.height(0);
							$('.mask').show();
							$pay.show().animate({height:vm.dh+'px'},'linear');	
						}
					}else{
						alert(res.msg);
					}
				}
			});	
		}).on('click','#payDialog .close,.mask',function(){
			$pay.animate({height:0},'100',function(){
				$pay.hide().height(vm.dh);
			});
			$('.mask').fadeOut();
		}).on('click','#wxpay',function(){
			var inputs = [];
			for(var i=0;i<vm.infoList.length;i++){
				var t = vm.infoList[i].lable;
				var $inp = $('#payDialog .item').eq(i).find('input');
				var temp = $inp.val();
				if(!temp){
					that.showTips(t.split('/')[0]+'不能为空！');
					$inp.focus();
					return;
				}else if((/手机|电话/.test(t) && !that.telValidate(temp)) || (/身份证/.test(t) && !checkCard(temp))){
					that.showTips(t.split('/')[0]+'格式不正确！');
					return;
				}else{
					inputs.push($inp.val());
				}
			}
			var str = '';
			for(var j=0;j<inputs.length;j++){
				str += '{\"'+vm.infoList[j].lable+'\":\"'+inputs[j]+'\"};';
			}
			var ep = that.GetQueryString('extensionPeople');
			$('#loading').fadeIn().find('#load p').text('正在保存信息,请稍等...');
			$.ajax({
				type:"POST",
				url:dataUrl.saveFormNew,
				data:{formId:vm.formId,information:str,state:vm.unitPrice == 0?0:1},
				success:function(res){
					if(res.code == 1){
						if(vm.unitPrice == 0){
							$('#loading').hide();
							that.showTips('报名成功');
							var u = res.result.gotoUrl;
							$pay.animate({height:0},'100',function(){
								$pay.hide();
							});
							$('.mask').fadeOut();
							if(u != null && u != ''){
								setTimeout(function(){
									location.href = u;
								},2100);	
							}else{
								setTimeout(function(){
									location.href = location.href;
								},2000);
							}
						}else{
							$('#load p').html('正在调起微信支付<br/>请稍等...');
							vm.userFormId = res.result.userFormId;
							var uu = decodeURI(location.href);
							$.ajax({
								type:"POST",
								url:dataUrl.commonPay,
								data:{
									projectId:vm.projectId,
									amount:vm.unitPrice,
									extensionPeople:ep,
									slogans:'commonForm_'+vm.userFormId+'_'+vm.formId,
									weixinUrl:uu
								},
								success:function(res1){
									$('#loading').hide().find('#load p').text('');
									if(res1.code == 1){
										var r = res1.result;
										wx.config({
											debug:false,
											appId:r.appId,
											timestamp:r.config_timestamp,
											nonceStr:r.config_noncestr,
											signature:r.signature,
											jsApiList:['chooseWXPay']
										});
										setTimeout(function(){
											wx.chooseWXPay({
												timestamp:r.timestamp,
												nonceStr:r.noncestr,
												package:r.packageValue,
												signType:r.paysignType,
												paySign:r.paySign,
												success:function(res){
													$pay.animate({height:0},'100',function(){
														$pay.hide();
													});
													$('.mask').fadeOut();
													location.href = location.href;
												}
											});	
										},200);
									}else if(res1.code == 0){
										that.showTips('提交失败，请去微信端报名');return;
									}else {
										alert(res1.msg);return;
									}
								},
								error:function(){
									$('#loading').hide().find('#load p').text('');
									that.showTips('提交失败');return;
								}
							});	
						}
					}else if(res.code == 0){
						$('#loading').hide().find('#load p').text('');
						if (vm.ua == 'wx') {
							$('body').append('<script type="text/javascript" src="/res/js/h5/donateStep/wxLogin.js"></script>');
						}else{
							location.href = res.result.url;
						}
					}else if(res.code == 2){
						$('#loading').hide().find('#load p').text('');
						location.href = base + 'ucenter/user/Login_H5.do?flag=entryPay_'+vm.actId;
					}else{
						$('#loading').hide().find('#load p').text('');
						alert(res.msg);
					}
				}
			});
		}).on('click','.feedback .preview img',function(){
			var $li = $(this).closest('li');
			var idx = $li.index();
			var $imgs = $li.parent().find('img');
			
			if(vm.ua == 'h5'){
				var html = [];
				html.push('<ul>');
				$imgs.each(function(){
					html.push('<li><img src="'+this.src+'" /></li>');
				});
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show()
				TouchSlide({ slideCell:"#bigImg",mainCell:".bd ul",effect:"left",autoPlay:false,defaultIndex:idx});
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
			}else{
				var srcs = [];
				var src = this.src;
				$imgs.each(function(){
					srcs.push(this.src);
				});
				wx.previewImage({
					current:src,
					urls:srcs
				});
			}
		}).on('click','#bigImg',function(){
			$(this).hide().find('.bd ul').remove();
		}).on('click','.wx-btn',function(){
			$('#inviteDialog').show();
			$('.mask').show();
		}).on('click','.mask',function(){
			$('#inviteDialog').hide();
			$(this).hide();
		});
		$('.dialog,.mask').each(function(){
			this.addEventListener('touchmove',function(e){
				e.stopPropagation();
			});
		});
	},
	isActivityAdmin:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.isActivityAdmin,
			data:{activityId:vm.aid},
			success:function(res){
				if(res.code == 1){
					vm.isSelf = true;
				}else if(res.code == 4 || res.code == 2){
					vm.isSelf = false;
				}else{
					that.showTips(res.msg);
				}
			}
		});
	},
	getProjectInfo:function(){
		var that = this;
		var code = that.GetQueryString('code');
		var froms = location.href.split('?')[1];
		$.ajax({
			type:"get",
			url:dataUrl.getProjectInfo,
			data:{activityId:vm.aid,code:code,share:froms},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					for(var i=0;i<r.signList.length;i++){
						if(r.signList[i].headImage == null){
							r.signList[i].headImage = '/res/images/detail/people_avatar.jpg';
						}else{continue;}
					}
					var $con = $('#det_act .content');
					$con.html(r.content);
					var oldcon = r.content;
					var h = $con.height();
					var cha = $con.html().length-$con.text().length;
					if(h > 150 && cha >= 250){
						vm.smallcon = oldcon.substr(0,600);
						$con.html(vm.smallcon);
					}else if(h > 150 && cha < 150){
						vm.smallcon = oldcon.substr(0,300);
						$con.html(vm.smallcon);
					}else if(h > 150 && cha >= 150 && cha <= 250){
						vm.smallcon = oldcon.substr(0,450);
						$con.html(vm.smallcon);
					}else{
						$('.detail-mask').removeClass('detail-mask-bg');
						$('#lookmore').hide();
					}
					vm.projectId = r.projectId;
					vm.projectTitle = r.projectTitle;
					vm.projectSubTitle = r.projectSubTitle;
					vm.projectLogo = r.projectLogo;
					vm.cname = r.companyName;
					vm.entryNum = r.number;
					vm.unitPrice = r.unitPrice;
					vm.customItem = r.customItem;
					vm.content = r.content;
					vm.signList = r.signList;
					vm.endDay = r.endDay;
					vm.type = r.type;
					if(r.limit <= r.number){
						vm.endDay = -1;
					}
					if(r.endDay == 0){
						vm.endTime = r.endTime;
					}
					vm.formId = r.formId;
					vm.actId = r.activityId;
					vm.entrierUrl += r.formId + '&unitprice='+vm.unitPrice;
					vm.realName = r.realName?r.realName:'';
					vm.mobile = r.mobile?r.mobile:'';
					if(r.defaultOption == 1){
						vm.infoList = vm.infoList.concat([{"lable":"姓名"},{"lable":"电话"}]);
					}
					if(r.information != '' && r.information !='{}'){
						vm.infoList = vm.infoList.concat(JSON.parse(r.information).list);
					}
					if(r.appId){
						vm.appId = r.appId;
						vm.timestamp = r.timestamp;
						vm.noncestr = r.noncestr;
						vm.signature = r.signature;
						that.wxShare();
					}
				}else{
					that.showTips(res.msg);return;
				}
			}
		});
	},
	getFeedbackList:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getActivityFeedBack,
			data:{activityId:vm.aid},
			success:function(res){
				if(res.code == 1){
					vm.fdList = res.result.data;
				}else{
					that.showTips(res.msg);
				}
			}
		});
	},
	showTips:function(txt){
		var $t = $('#tips');
		if($t.hasClass('show')) return;
		var winh = $(window).height();
		var bot = winh / 2 - 80;
		$t.html(txt).show().animate({'bottom':bot+'px'},'ease').addClass('show');
		setTimeout(function(){
			$t.fadeOut(function(){
				$(this).html('').css({'bottom':'-100px'}).removeClass('show');
			});
		},2000);
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
	//获取cookie
	getcookie:function(name){
		var strcookie = document.cookie;
		var arrcookie = strcookie.split("; ");
		for(var i = 0; i < arrcookie.length; i++) {
			var arr = arrcookie[i].split("=");
			if(arr[0] == name) return decodeURIComponent(arr[1]); //增加对特殊字符的解析  
		}
		return "";
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
	entry.init();
});

