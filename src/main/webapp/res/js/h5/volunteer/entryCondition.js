
var base = 'http://www.17xs.org/';
var dataUrl = {
	addForm: base + 'activity/addForm.do',			//添加表单信息
	getFormInfo: base + 'activity/getFormInfo.do'	//根据活动id查询表单详情
};

var entryCondition = {
	aid:'',
	time:'',
	formId:'',
	init:function(){
		var that = this;
		var aid = that.GetQueryString('activityId');
		if(aid == null){
			$('.no-page').show();
			$('.ec-bd').hide();
		}else{
			that.aid = aid;
			var dd = new Date();
			var m = dd.getMonth()+1,d = dd.getDate();
			new DateSelector({
		        input : 'endTime',//点击触发插件的input框的id
		        container : 'endTimeContainer',//插件插入的容器id
		        type : 0,
		        //0：不需要tab切换，自定义滑动内容，建议小于三个；
		        //1：需要tab切换，【年月日】【时分】完全展示，固定死，可设置开始年份和结束年份
		        param : [1,1,1,0,0],
		        //设置['year','month','day','hour','minute'],1为需要，0为不需要,需要连续的1
		        beginTime : [2017,m,d],//如空数组默认设置成1970年1月1日0时0分开始，如需要设置开始时间点，数组的值对应param参数的对应值。
		        endTime : [2019,12,31],//如空数组默认设置成次年12月31日23时59分结束，如需要设置结束时间点，数组的值对应param参数的对应值。
		        recentTime : [2017,m,d],//如不需要设置当前时间，被为空数组，如需要设置的开始的时间点，数组的值对应param参数的对应值。
		        success : function(arr){
		            arr[1] = arr[1]<10?('0'+arr[1]):arr[1];
		            arr[2] = arr[2]<10?('0'+arr[2]):arr[2];
		            $("#endTime").val(arr[0] +"年"+ arr[1] +'月'+ arr[2] +'日');
		            that.time = new Date(arr[0]+'-'+arr[1]+'-'+arr[2]+' 23:59:59');
		        }
		    });
		    that.getFormInfo();
		    that.bindEvent();
		}
	},
	getFormInfo:function(){
		var that = this;
		$.ajax({
			type:"get",
			url:dataUrl.getFormInfo,
			data:{activityId:that.aid},
			success:function(res){
				if(res.code == 1){
					var r = res.result;
					$('#entryNumLimit').val(r.limit);
					$('#endTime').val(r.endTime);
					var y = r.endTime.substr(0,4);
					var m = r.endTime.substr(5,2);
					var d = r.endTime.substr(8,2);
					that.time = new Date(y+'-'+m+'-'+d+' 23:59:59');
					that.formId = r.id;
				}else if(res.code != 4){
					alert(res.msg);
				}
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#addWrite',function(){
			$('#needOptionsDialog').addClass('show');
		}).on('change','#selAll',function(){
			if(this.checked == true){
				$('#needOptionsDialog .item input').each(function(){
					this.checked = true;
				});
			}else{
				$('#needOptionsDialog .item input').each(function(){
					this.checked = false;
				});
			}
		}).on('change','#needOptionsDialog .item input',function(){
			var len1 = $('#needOptionsDialog .item input').length;
			var len2 = $('#needOptionsDialog .item input:checked').length;
			if(len2<len1){
				$('#selAll')[0].checked = false;
			}else{
				$('#selAll')[0].checked = true;
			}
		}).on('click','#needOptionsDialog .sure',function(){
			$('#needOptionsDialog').addClass('hide');
			setTimeout(function(){
				$('#needOptionsDialog').removeClass('show hide');
			},400);
			var sel = [],html = [];
			var len = $('#needOptionsDialog .item.show input').length;
			var checks = $('#needOptionsDialog .item.show input:checked');
			if(checks.length > 0){
				var flag = false;
				checks.each(function(){
					html.push('<div class="need"><div class="item">');
					html.push('<a href="javascript:;" class="del" data-id="'+this.id+'">');
					html.push('<img src="/res/images/h5/images/volunteer/del.png"/></a>');
					html.push($(this).next().text()+'</div></div>');
					$(this).parent().removeClass('show');
				});	
				$('#addWrite').parent().before(html.join(''));
				if(checks.length == len){
					$('#addWrite').parent().hide();
					$('.ec-bd .pp').css({'margin-top':'.1rem'});
				}else{
					$('#addWrite').parent().fadeIn();
					$('.ec-bd .pp').css({'margin-top':'0'});
				}
			}
		}).on('click','.ec-bd .need .del',function(){
			var id = $(this).attr('data-id');
			$(this).closest('.need').remove();
			$('#needOptionsDialog').find('#'+id).attr('checked',false).parent().addClass('show');
			$('#selAll')[0].checked = false;
			$('#addWrite').parent().show();
			$('.ec-bd .pp').css({'margin-top':'0'});
		}).on('click','#needOptionsDialog .closeDialog',function(){
			$('#needOptionsDialog').addClass('hide');
			setTimeout(function(){
				$('#needOptionsDialog').removeClass('show hide');
			},400);
		}).on('click','#saveCondition',function(){
			var num = $('#entryNumLimit').val();
			var times = $('#endTime').val();
			var $need = $('.ec-bd .need');
			if(num == ''){
				that.showTips('报名人数上限不能为空！');
				$('#entryNumLimit').focus();
			}else if(times == ''){
				that.showTips('截止时间不能为空！');
				setTimeout(function(){
					$('#endTime').closest('.ae-title')[0].scrollIntoView();
				},2000);
			}else{
				var obj = {};
				if($need.length>0){
					obj['list'] = [];
					$need.each(function(){
						obj['list'].push({"lable":$(this).text().replace(/"|“|”/,''),"type":"singleText","value":""});
					});
				}
				obj = JSON.stringify(obj);
				var $data = {activityId:that.aid,limit:num,endTime:that.time,form:obj};
				if(that.formId){
					$data['id'] = that.formId;
				}
				$.ajax({
					type:"post",
					url:dataUrl.addForm,
					data:$data,
					success:function(res){
						if(res.code == 1){
							that.showTips('保存成功！');
							setTimeout(function(){
								location.href = 'pubSuccess.html?activityId='+that.aid;
							},2000);
						}else if(res.code == 0){
							that.showTips('保存失败！');
						}else if(res.code == 0){
							$('body').append('<script src="/res/js/h5/donateStep/wxLogin.js"></script>');
						}else if(res.code == 2){
							location.href = '/ucenter/user/Login_H5.do?flag=condition_'+vm.aid;
						}
					}
				});
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
	entryCondition.init();
});
