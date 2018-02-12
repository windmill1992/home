
var base = 'http://www.17xs.org/';
var dataUrl = {
	dataList:base+'resposibilityReport/entryFormNewList.do',						//获取问答题目
	getCommonFormUserInfo:base + 'resposibilityReport/getCommonFormUserInfo.do'		//查询表单提交信息与答案
};

var vm = new Vue({
	el:'#pageContainer',
	data:{formList:[],manfen:false,grades:0,curNum:0,errNum:0,canLook:false}
});

var survey = {
	init:function(){
		var that = this;
		var formId = that.GetQueryString('formId');
		if(formId == null){
			alert('暂时没有报名信息');return;
		}else{}
		$.ajax({
			type:"get",
			url:dataUrl.dataList,
			data:{id:formId},
			success:function(result){
				if(result.flag == 1){
					var data = result.obj;
					var $body = $('body');  
					document.title = data.title;
					var $iframe = $('<iframe src="http://www.17xs.org/res/images/favicon.ico"></iframe>').on('load', function() {
						setTimeout(function() {
							$iframe.off('load').remove();
						}, 0);
					}).appendTo($body);
					var form = JSON.parse(data.form).list;
					$.ajax({
						type:"get",
						url:dataUrl.getCommonFormUserInfo,
						data:{formId:formId},
						success:function(res){
							if(res.code == 1){
								var answer = res.result.answer;
								for(var i=0;i<form.length;i++){
									var letter = answer[i][0] , ansTxt = '',userAns = '', values = form[i].value.split(',');
									switch(letter){
										case 'A':
											ansTxt = values[0];break;
										case 'B':
											ansTxt = values[1];break;
										case 'C':
											ansTxt = values[2];break;
										case 'D':
											ansTxt = values[3];break;
										default:break;
									}
									for(var j=0;j<values.length;j++){
										var temp = answer[i][1].split(',')[0];
										if(temp == values[j]){
											userAns = temp;
										}else{continue;}
									}
									vm.formList.push({
										title:form[i].lable,
										options:values,
										ansShow:false,
										state:0,
										answer:ansTxt,
										userAnswer:userAns
									});
								}
							}else if(res.code == 0){
								var ua = window.navigator.userAgent.toLowerCase();
								if (ua.match(/MicroMessenger/i) == 'micromessenger') {
									$('body').append('<script type="text/javascript" src="../../../../res/js/h5/donateStep/wxLogin.js"></script>');
								}else{
									location.href = res.result.url;
								}
							}else if(res.code == 2){
								location.href = base + 'ucenter/user/Login_H5.do?flag=survey_'+formId;
							}else{
								alert('报名信息不存在');return;
							}
						}
					});
				}else{
					alert('报名信息不存在');return;
				}
			}
		});
		this.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		var count1 = 0;
		$('body').on('click','#lookGrades',function(){
			var count = 0 ;
			var len = vm.formList.length;
			for(var i=0;i<len;i++){
				if(vm.formList[i].state != 0){
					count1++;
				}else{}
				if(vm.formList[i].state == 2){
					count++;
				}else{continue;}
			}
			vm.grades = count * parseInt(100 / len);
			vm.curNum = count;
			vm.errNum = len - count;
			if(vm.errNum == 0){
				vm.manfen = true;
			}else{
				vm.manfen = false;
			}
			$('#gradesDialog').addClass('show');
			
		}).on('click','#gradesDialog .close',function(){
			$('#gradesDialog').addClass('hide');
			setTimeout(function(){
				$('#gradesDialog').removeClass('show hide');
			},400);
		}).on('click','.sv-bd .lookAns',function(){
			count1++;
			if(count1>=vm.formList.length){
				vm.canLook = true;
			}else{}
			$(this).css({opacity:0});
			var idx = $(this).attr('idx');
			vm.formList[idx].ansShow = true;
			if(vm.formList[idx].answer != vm.formList[idx].userAnswer){
				vm.formList[idx].state = 1;
			}else{
				vm.formList[idx].state = 2;
			}
		});
		
		$('.dialog')[0].addEventListener('touchmove',function(e){
			e.stopPropagation();
			e.preventDefault();
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
	survey.init();
});
