
var base = 'http://www.17xs.org/';
var dataUrl = {
	indexHead : base +'newReleaseProject/indexHead.do'		//获取捐赠金额 和受助人数
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		totalMoney:0,
		helpPeople:0,
		raiseProjUrl:'raiseProjEdit.html'
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

var pubIndex = {
	init:function(){
		var recommendedPerson = wxlogin.getcookie('recommendedPerson');
		if(recommendedPerson=='' || recommendedPerson=='null' || recommendedPerson==null){
			recommendedPerson = wxlogin.GetQueryString('recommendedPerson');
		}else{}
		var recommendedId = wxlogin.GetQueryString('recommendedId');
		var charityId = wxlogin.GetQueryString('charityId');
		if(recommendedPerson != null && charityId == null){
			vm.raiseProjUrl += '?recommendedPerson='+recommendedPerson;
		}else if(recommendedPerson == null && charityId != null){
			vm.raiseProjUrl += '?charityId='+charityId;
		}else if(recommendedId != null){
			vm.raiseProjUrl += '?recommendedId='+recommendedId;
		}
		$.ajax({
			type:"GET",
			url:dataUrl.indexHead,
			success:function(res){
				if(res.code == 1){
					vm.totalMoney = res.result.totalMoney;
					vm.helpPeople = res.result.helpPeople;
				}else{
					alert(res.msg);return;
				}
			}
		});
	}
};

$(function(){
	pubIndex.init();
});

