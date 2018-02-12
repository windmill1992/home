
var base = 'http://www.17xs.org/';
var stepUrl = '/webhtml/view/h5/donateStep/';
var dataUrl = {
	getDonateStepRank : base + 'donateStep/getDonateStepRank.do',			//企业排行分页，按照捐赠金额排序
	getUserUpdateRunData : base + 'donateStep/getUserUpdateWeRundata.do', 	//判断用户10分钟内是否更新运动数据
	getRandomCompanyInfo : base + 'donateStep/getRandomCompanyInfo.do',		//随机选取企业，从企业选取一个项目
	getStatisticsData : base + 'donateStep/getStatisticsData.do',			//统计(总捐步人数，参与企业数，总金额)
	donateStep : base + 'donateStep/donateStep.do',							//捐步
	getRandomProjectInfo : base + 'donateStep/getRandomProjectInfo.do'		//换项目
};
var otherUrl = {
	companyUrl:stepUrl+'investors.html?companyId=',
	defaultImgUrl:'/res/images/h5/images/donateStep/wen.png',
	projUrl:'/project/view_h5.do?projectId='
};
var page = 1;
var dd = new Date();
var vm = new Vue({
	el:'#pageContainer',
	data:{
		endStep:1000,
		endStepCn:'千',
		year:dd.getFullYear(),
		month:dd.getMonth()+1,
		day:dd.getDate(),
		donateAmount:0,								//配捐金额
		projectId:-1,								//项目id
		projectTitle:'项目标题',						//项目标题
		companyName:'企业名称',						//企业名称
		companyUrl:otherUrl.companyUrl,				//企业链接
		companyId:-1,								//企业id
		slogan:'企业标语',								//企业标语
		companyImgUrl:otherUrl.defaultImgUrl,		//企业logo
		effectLists:[],								//企业排行
		steps:0,									//今日步数
		amount:0,									//捐步随机金额
		donateState:false,							//今日是否捐步
		last_donated:{								//今日已捐步
			userName:'用户昵称',						//用户昵称
			cLogo:otherUrl.defaultImgUrl,			//配捐企业logo
			cUrl:otherUrl.companyUrl,				//配捐企业链接
			cName:'企业',								//配捐企业名称
			projTitle:'项目标题',						//捐赠项目标题
			projUrl:otherUrl.projUrl,				//捐赠项目链接
			steps:0,								//捐赠步数
			money:0,								//捐赠金额
		}
	},
	methods:{
		getDataRunNum:function(num){
			return num;
		}
	}
});

//千位以上数字格式化,(1,000.00)
Vue.filter('numFmt',function(value){
	var newValue = '';
	var count = 0;
	var v = value.toString().split('.');	//取小数部分
	var r = '';
	if(v[1]){
		r = '.'+v[1].substr(0,2);
	}else{}
	
	if(value>999){
		value = v[0];
		for(var i=value.length-1;i>=0;i--){
			count++;
			if(count%3==0 && i>0){
				newValue += value[i]+',';
			}else{
				newValue += value[i];
			}
		}
		newValue = newValue.split('').reverse().join('')+r;
	}else{
		newValue = v[0]+r;
	}
	return newValue;
});

var donateSteps = {
	flag:false,
	init:function(){
		
		this.getRandomCompany();
		this.getStatisticsData();
		this.effectList(page,10);
		this.bindEvent();
		
		//捐步弹框进度初始化
		$('#notEnoughDialog .run_pic_progress,#yqj-stepNotEnough .run_pic_progress').css({
			'-webkit-transform':'rotate(180deg)',
			'transform':'rotate(180deg)'
		});
		$('#notEnoughDialog .run_pic_people,#yqj-stepNotEnough .run_pic_people').css({
			'-webkit-transform':'rotate(0deg)',
			'transform':'rotate(0deg)'
		});	
		
		//阻止弹框遮罩层滑动引起页面滚动
		$('.mask,.dialog').each(function(){
			this.addEventListener('touchmove',function(e){
				e.stopPropagation();
				e.preventDefault();
			},false);
		});
	},
	//随机企业和项目,已捐获取捐赠时的企业和项目
	getRandomCompany:function(){
		var userId = wxlogin.getcookie('userId');
		$.ajax({
			type:"get",
			url:dataUrl.getRandomCompanyInfo,
			data:{userId:userId},
			success:function(res){
				if(res.code == 1){	//未捐
					var result = res.result;
					vm.projectId = result.project.projectId;
					vm.projectTitle = result.project.projectTitle;
					var bgUrl = 'url('+result.project.projectImgUrl+')';
					vm.donateAmount = parseFloat(result.donateAmount/10000).toFixed(2);
					vm.donateState = false;
					vm.companyName = result.name;
					vm.slogan = result.slogan;
					vm.companyId = result.companyId;
					vm.companyUrl += result.companyId;
					if(result.companyImgUrl){
						vm.companyImgUrl = result.companyImgUrl;
					}else{}
					$('.hd-pic').css({'background-image':bgUrl});
				}else if(res.code == 2){	//已捐
					var result = res.result;
					var bgUrl = 'url('+result.project.projectImgUrl+')';
					vm.projectId = result.project.projectId;
					vm.projectTitle = result.project.projectTitle;
					vm.donateState = true;
					vm.companyId = result.companyId;
					if(result.companyImgUrl){
						vm.last_donated.cLogo = result.companyImgUrl;
					}else{}
					vm.last_donated.userName = result.nickName;
					vm.last_donated.cUrl += result.companyId;
					vm.last_donated.projTitle = result.project.projectTitle;
					vm.last_donated.projUrl += result.project.projectId;
					vm.last_donated.steps = result.stepCount;
					vm.last_donated.cName = result.name;
					vm.last_donated.money = result.userDonatAmount;
					$('.hd-pic').css({'background-image':bgUrl});
				}else{
					alert(res.msg);return;
				}
			}
		});
	},
	//随机项目
	getRandomProjectInfo:function(){
		$.ajax({
			type:"get",
			url:dataUrl.getRandomProjectInfo,
			data:{companyId:vm.companyId},
			success:function(res){
				console.log(res);
				if(res.code == 1){
					var result = res.result.project;
					var bgUrl = 'url('+result.projectImgUrl+')';
					vm.projectTitle = result.projectTitle;
					vm.projectId = result.projectId;
					$('.hd-pic').css({'background-image':bgUrl});
				}else{
					alert(res.msg);return;
				}
			}
		});
	},
	//获取成果榜单数据
	getStatisticsData:function(){
		$.ajax({
			type:"get",
			url:dataUrl.getStatisticsData,
			success:function(res){
				if(res.code == 1){
					var result = res.result;
					var scrollNumArr = [result.peopleNum,result.companyNum,parseInt(result.donatedTotalMony)];
					var $num = $('.scroll-num .num');
					var patt1 = /万/;
					$num.each(function(idx,t){
						var snr = scrollNumArr[idx].toString();
						t = $(t);
						var s = t.next();
						if(snr.length<6){
							s.text(s.text().replace(patt1,''));
						}else{
							snr = snr.substr(0,snr.length-4);
							if(!patt1.test(s.text())){
								s.text('万'+s.text());
							}
						}
						for(var i=0;i<snr.length;i++){
							t.append('<span id="scrollNumSpan'+(idx+1)+''+i+'" data-oldno="0" data-newno="'+snr.charAt(i)+'">0</span>');
						}
					});
				}else{
					alert(res.msg);return;
				}
			}
		});
	},
	
	bindEvent:function(){
		var that = this;
		var delayTime = 400;
		$('body').on('click','#donateBtn',function(){
			var userId = wxlogin.getcookie('userId');
			var onStep = [vm.endStep/10,vm.endStep*2/10,vm.endStep*7/10,vm.endStep*9/10,vm.endStep*999/1000];
			var word = ['帅的人已日均万步，丑的人还原地踏步，你选哪个？','一整天都没有运动，别等沙发上的土豆都发了芽','今天运动量还没达标哦~宝宝坚持再走几步吧','还差一点就达标啦，宝宝加油~','就差一点点了哦，再走几步吧~'];
			$.ajax({
				url:dataUrl.getUserUpdateRunData,
				data:{userId:userId},
				success:function(res){
					if(res.code == 2 || res.code==0){
						$('#getRunSteps').addClass('show');
					}else{
						var result = res.result;
						var steps = result.runData;
						steps = steps?steps:0;
						vm.steps = steps;
						vm.amount = result.amount;
						if(steps<vm.endStep){
							$('#notEnoughDialog').addClass('show');
							that.runProgress($('#notEnoughDialog'),false);
							if(steps<onStep[0]){
								$('.run_text_area p').html(word[0]);
							}else if(steps<onStep[1]){
								$('.run_text_area p').html(word[1]);
							}else if(steps<onStep[2]){
								$('.run_text_area p').html(word[2]);
							}else if(steps<onStep[3]){
								$('.run_text_area p').html(word[3]);
							}else if(steps<onStep[4]){
								$('.run_text_area p').html(word[4]);
							}
						}else{
							$('#enoughDialog').addClass('show');
						}
					}
				}
			});
		}).on('click','#yqj-create',function(){
			that.runProgress($('#yqj-stepNotEnough'),true);
		}).on('click','#donateSteps',function(){
			var userId = wxlogin.getcookie('userId');
			$.ajax({
				type:"post",
				url:dataUrl.donateStep,
				data:{extensionPeople:vm.companyId,userId:userId,projectId:vm.projectId,amount:vm.amount},
				success:function(res){
					location.href = base + stepUrl + 'success.html';
				}
			});
		}).on('click','.ds-hd .hd-page-prompt',function(){
			$('#introDialog').addClass('show');
		}).on('click','#introduce',function(){
			$('#yqj-introduce').addClass('show');
		}).on('click','#notEnoughDialog .stepIntro',function(){
			$('#stepIntroDialog').addClass('show');
		}).on('click','#introDialog .closeDialog',function(){
			$('#introDialog').addClass('hide');
			setTimeout(function(){
				$('#introDialog').removeClass('show hide');
			},delayTime);
		}).on('click','#notEnoughDialog .closeDialog',function(){
			$('#notEnoughDialog').addClass('hide');
			setTimeout(function(){
				$('#notEnoughDialog').removeClass('show hide');
			},delayTime);
		}).on('click','#enoughDialog .closeDialog',function(){
			$('#enoughDialog').addClass('hide');
			setTimeout(function(){
				$('#enoughDialog').removeClass('show hide');
			},delayTime);
		}).on('click','#yqj-stepNotEnough .closeDialog',function(){
			$('#yqj-stepNotEnough').addClass('hide');
			setTimeout(function(){
				$('#yqj-stepNotEnough').removeClass('show hide');
			},delayTime);
		}).on('click','#yqj-introduce .closeDialog',function(){
			$('#yqj-introduce').addClass('hide');
			setTimeout(function(){
				$('#yqj-introduce').removeClass('show hide');
			},delayTime);
		}).on('click','#stepIntroDialog .closeDialog',function(){
			$('#stepIntroDialog').addClass('hide');
			setTimeout(function(){
				$('#stepIntroDialog').removeClass('show hide');
			},delayTime);
		}).on('click','#getRunSteps .closeDialog',function(){
			if($(this).hasClass('knowBtn')){
				$('#getRunSteps').removeClass('show');
				that.refresh = true;
				$('#donateBtn').click();
//				location.href = location.href;
			}else{
				$('#getRunSteps').addClass('hide');
				setTimeout(function(){
					$('#getRunSteps').removeClass('show hide');
				},delayTime);
			}
		}).on('click','#targetListMenu .closeBtn',function(){
			$('#targetListMenu').addClass('hide');
			setTimeout(function(){
				$('#targetListMenu').removeClass('show hide');
			},delayTime);
		}).on('click','.hd-pic',function(){
			location.href = base + 'project/view_h5.do?projectId='+vm.projectId;
		}).on('click','#changeProj',function(){
			that.getRandomProjectInfo();
		}).on('click','.loadmore',function(){
			var txt = $(this).html();
			if(txt == '没有更多数据了' || txt == '暂无数据'){
				return;
			}else{
				that.effectList(++page,10);
			}
		});
		
		$(document).on('scroll',function(){
			if($(this).scrollTop()>=100 && !that.flag){
				that.showBillboard();
			}else{}
		});
	},
	//显示榜单过渡
	showBillboard:function(){
		this.flag = true;
		var $span = $('.scroll-num .num span');
		$span.each(function(idx,t){
			t = $(t);
			var newno = Number(t.attr('data-newno'));
			t.data('oldno',t.attr('data-oldno'));
			var timer = setInterval(function(){
				var oldno = t.data('oldno');
				oldno++;
				if(oldno>=newno){
					oldno = newno;
					t.removeData('oldno');
					t.text(oldno);
					clearInterval(timer);
				}else{
					t.text(oldno);
					t.data('oldno',oldno);
				}
			},150);
		});	
	},
	//运动步数进度动画
	runProgress:function(o,isyqj){
		var a = o.find('.run_pic_progress');
		var n = o.find('.run_pic_people');
		var s = o.find('.run_num_inner');
		a.css({'transform':'rotate(180deg)'});
		n.css({'transform':'rotate(0deg)'});	
		var steps = vm.getDataRunNum(vm.steps);
		var endStep = vm.endStep;
		if(!isyqj){
			if(steps<endStep){
				o.addClass('show');
			}else{
				$('#enoughDialog').addClass('show');
				return;
			}	
		}else{
			if(steps<endStep){
				o.addClass('show');
			}else{
				$('#targetListMenu').addClass('show');
				return;
			}
		}
		
		var deg = Number(parseFloat(steps/endStep*180).toFixed(1));
		if(deg>=180){deg = 180;}else{}
		setTimeout(function(){
			a.css({
				'transform':'rotate('+(180+deg)+'deg)',
				'transition':'-webkit-transform 1s cubic-bezier(0.5,0,0,1) 0s'
			});
			n.css({
				'transform':'rotate('+deg+'deg)',
				'transition':'transform 1s cubic-bezier(0.5,0,0,1) 0s'
			});	
		},500);
		
		setTimeout(function(){
			a.removeAttr('style').css({
				'transform':'rotate('+(180+deg)+'deg)'
			});	
			n.css({
				'transform':'rotate('+deg+'deg)'
			});	
		},1500);
		s.text(0);
		var std = vm.endStep / 40;
		if(steps>std){
			var speed = Math.floor(steps/std);
		}else{
			var speed = 1;
		}
		if(speed>20){speed = 20;}else{}
		var patt2 = /,/;
		var timer = setInterval(function(){
			var num = Number(s.text().replace(patt2,''));
			var step = '';
			var count = 0;
			if(num > 999){
				var l = parseInt(num/1000);
				var r = num%1000;
				var newNum = '';
				var rn = r + speed;
				var isGreater10 = rn >= 10;
				var isLess100 = rn < 100;
				var isLess10 = rn < 10;
				if(isLess10){newNum = '00'+rn;}
				else if(isGreater10 && isLess100){newNum = '0'+rn;}
				else{newNum = rn;}
				s.text(l+','+newNum);
			}else{
				s.text(num+speed);
			}
			if(steps > num && (steps-num)<(vm.endStep/100) && speed>1){
				speed--;
			}else{}
			if(num >= steps){
				clearInterval(timer);
				steps = steps.toString();
				if(steps.length>3){
					for(var i=steps.length-1;i>=0;i--){
						step += steps.charAt(i);
						count += 1;
						if(count%3 == 0){
							step += ',';
						}else{}
					}
					step = step.split('').reverse().join('');
				}else{
					step = steps;
				}
				s.text(step);
			}else{}
		},5);
	},
	//企业排行分页
	effectList:function(pageNum,pageSize){
		$.ajax({
			url:dataUrl.getDonateStepRank,
			type:'GET',
			data:{pageNum:pageNum,pageSize:pageSize},
			success:function(res){
				var result = res.result;
				if(result.length>0){
					for(var i=0;i<result.length;i++){
						vm.effectLists.push({
							donateAmount:result[i].donateAmount,				//企业捐赠金额
							companyImgUrl:result[i].companyImgUrl,				//企业logo
							donateNum:result[i].donateNum,						//慈善家数量
							companyName:result[i].companyName,					//企业名称
							companyUrl:vm.companyUrl + result[i].companyId		//企业链接
						});	
					}
				}else{
					$('#effectList').html('');
					$('.loadmore').html('暂无数据');
				}
				if(!res.state){
					$('.loadmore').html('没有更多数据了');
				}else{}
			}
		});
	}
};

$(function(){
	donateSteps.init();
	setTimeout(function(){
		$('#mask').hide();
		$('#pageContainer').show();
	},1000);
});
