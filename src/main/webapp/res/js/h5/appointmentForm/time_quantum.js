function GetDateStr(AddDayCount) {   
	var temp;
	var dd = new Date();
	var week = ["周日","周一","周二","周三","周四","周五","周六"];
	var tm = dd.getMonth()+1, td = dd.getDate();
	if(tm==4 && td==28){
		temp = 4;
	}else{
		temp = 3;
	}
	var nodate = td + AddDayCount;
	if(tm == 2 && nodate <=17 && nodate >= 12){
		temp = 21 - td;
	}
	var oldy = dd.getFullYear();
	dd.setDate(dd.getDate()+AddDayCount+temp);//获取AddDayCount天后的日期  
	var newy = dd.getFullYear();   
	var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0  
	var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate();//获取当前几号，不足10补0  
	var w = dd.getDay();
	
	if(oldy!==newy){
		return newy+'年'+m+"月"+d+"日"+"("+week[w]+")";
	}else{
		return m+"月"+d+"日"+"("+week[w]+")";   
	}	
}
var j=jcount;
var $time_quantum = [];
for (var i=0;i<7;i++) {
	$time_quantum.push(func(i));
}
var arr1 = [],arr2 = [],arr3 = [],arr4 = [];
var temparr = [];
function tempfun(x){
	switch(j){
		case 0:
			temparr = [];
			temparr.push({"value":"08:30-10:00"+"<span class='full'>"+(parseInt(a[j][x][0])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"001"},
						{"value":"10:00-11:30"+"<span class='full'>"+(parseInt(a[j][x][1])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"002"},
						{"value":"14:00-15:30"+"<span class='full'>"+(parseInt(a[j][x][2])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"003"},
						{"value":"15:30-17:00"+"<span class='full'>"+(parseInt(a[j][x][3])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"004"}
			);
			break;
		case 1:
		case 2:
			temparr = [];
			temparr.push({"value":"15:30-16:30"+"<span class='full'>"+(parseInt(a[j][x][0])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"001"},
						{"value":"16:30-17:30"+"<span class='full'>"+(parseInt(a[j][x][1])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"002"}
			);
			break;
		case 3:
			temparr = [];
			temparr.push({"value":"17:00-18:00"+"<span class='full'>"+(parseInt(a[j][x][0])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"001"},
						{"value":"18:00-19:00"+"<span class='full'>"+(parseInt(a[j][x][1])>=15?"已满":'')+"</span>",
						"id":"0"+((x+1)<10?"0"+(x+1):(x+1))+"002"}
			);
			break;
	}
	return temparr;
};

function func(x){
	return {
				"value":GetDateStr(x),
				"id":"0"+((x+1)<10?"0"+(x+1):(x+1)),
				"child":tempfun(x)
		};
}
function hos(h){
	switch(h){
		case 0:
			for (var i=0;i<$time_quantum.length;i++) {
				//if(/周一|周三/.test($time_quantum[i].value)){
					arr1.push(func(i));
				//}
			}
		$time_quantum = arr1;
		break;
		
		case 1:
			for (var i=0;i<$time_quantum.length;i++) {
				if(!/周六|周日/.test($time_quantum[i].value)){
					arr2.push(func(i));
				}
			}
			$time_quantum = arr2;
		break;
		case 2:
			for (var i=0;i<$time_quantum.length;i++) {
				if(!/周六|周日/.test($time_quantum[i].value)){
					arr3.push(func(i));
				}
			}
			$time_quantum = arr3;
		break;
		case 3:
			for (var i=0;i<$time_quantum.length;i++) {
				if(/周一|周五/.test($time_quantum[i].value)){
					arr4.push(func(i));
				}
			}
			$time_quantum = arr4;
		break;
		default:return;
	}
}


