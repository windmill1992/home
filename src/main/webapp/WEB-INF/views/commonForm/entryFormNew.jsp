<!DOCTYPE html>
<html lang="en">
<head>	
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
<title>${title }</title>
<link rel="stylesheet" type="text/css" href="/res/css/util/city.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/util/date.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/util/laydate.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/dev/entryFormNew.css?v=20171205"/>
</head>
<body>
	<div class="main">
		<h2>${title }</h2>
		<div class="introCon">${content }</div>
		<div class="dash"></div>
		<div class="survey">
			
		</div>
		<div class="process">
			<div class="pro">
				<span></span>
			</div>
		</div>
	</div>
	
	<div id="tips"></div>
	<input type="hidden" id="formId" value="${formId }">
	<input type="hidden" id="userId" value="${user.id }">
	<input type="hidden" id="nickName" value="${user.nickName }">
	<input type="hidden" id="titleName" value="${title }">
	<input type="hidden" id="special_fund_id" value="${special_fund_id }">
	<input type="hidden" id="countValue" value="1">
	<input type="hidden" id="appId" value="${appId }"/>
	<input type="hidden" id="timestamp" value="${timestamp }"/>
	<input type="hidden" id="nonceStr" value="${noncestr }"/>
	<input type="hidden" id="signature" value="${signature }"/>
</body>
<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/res/js/common/city5.js"></script>
<script type="text/javascript" src="/res/js/common/allcity.js"></script>
<script type="text/javascript" src="/res/js/common/date.js"></script>
<script type="text/javascript" src="/res/laydate-master/laydate.dev.js"></script>
<script type="text/javascript" src="/res/js/common/city.js"></script>
<script type="text/javascript" src="/res/js/common/city2.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script data-main="/res/js/dev/commonFormNew.js?v=20180130" src="/res/js/require.min.js"></script>
<script>
var jcount=0;
var a = "";
	$.ajax({
		url:"http://www.17xs.org/resposibilityReport/loadCountAppointmentForm.do",
		data:{formId:${formId }},
		success:function(result){
			var data=result;
			if(data.flag==1){
				a = data.obj;
			}
		}
	});
</script>
<script type="text/javascript" src="/res/js/h5/appointmentForm/hospitals.js" ></script>
<script>
$(function(){
	var isPC=IsPC();
	setTimeout(function(){
	    if(isPC && $('#birthdate').length>0 ){
	        laydate({elem:"#birthdate"});
	    }
	    if(isPC && $('#addr').length>0){
	        initComplexArea('seachprov', 'seachcity', 'seachdistrict', area_array, sub_array, '0', '0', '0');
	    }
	    if(!isPC && $('#birthdate').length>0){
		     new DateSelector({
		        input : 'birthdate',//点击触发插件的input框的id
		        container : 'dateContainer',//插件插入的容器id
		        type : 0,
		        //0：不需要tab切换，自定义滑动内容，建议小于三个；
		        //1：需要tab切换，【年月日】【时分】完全展示，固定死，可设置开始年份和结束年份
		        param : [1,1,0,0,0],
		        //设置['year','month','day','hour','minute'],1为需要，0为不需要,需要连续的1
		        beginTime : [2005,1],//如空数组默认设置成1970年1月1日0时0分开始，如需要设置开始时间点，数组的值对应param参数的对应值。
		        endTime : [2012,12],//如空数组默认设置成次年12月31日23时59分结束，如需要设置结束时间点，数组的值对应param参数的对应值。
		        recentTime : [2005,1],//如不需要设置当前时间，被为空数组，如需要设置的开始的时间点，数组的值对应param参数的对应值。
		        success : function(arr){
		            console.log(typeof arr[1]);
		            var temp;
		            if(arr[1]<10 ){temp = "0"+arr[1];}
		            else{temp = arr[1];}
		            $("#birthdate").val(arr[0] +"-"+ temp);
		        }//回调
		    });
	    }
	    if(!isPC && $('#addr').length>0){
	    	new MultiPicker({
			 	input: 'addr',//点击触发插件的input框的id
		        container: 'addrContainer',//插件插入的容器id
		        jsonData: $city,
		        success: function (arr) {
					if(arr.length==3){
						$("#addr").val(arr[0].value +"-"+arr[1].value+"-"+arr[2].value);
					}else{$("#addr").val(arr[0].value +"-"+arr[1].value);}
					
		        }//回调
		    });
	    }
	    $.ajax({
			url:"http://www.17xs.org/resposibilityReport/entryFormNewList.do",
			data:{id:${formId}},
			success: function(result){
				if(result.flag=='0'){
					return false;
				}else {
					var data=result.obj;
					var jsonText=jQuery.parseJSON(data.form);
					for(var i=0;i<jsonText.list.length;i++){
						var ss=[];
						if(jsonText.list[i].type=="dropDown"){//下拉框
							var s=jsonText.list[i].value.split(',');
							for(var j=0;j<s.length;j++){
							 ss.push(JSON.parse('{"id":"00'+j+'","value":"'+s[j]+'"}'));
							}
					    	(function(h){
			    				new MultiPicker({
							    	input:'sel'+h,
							    	container:'selectContainer'+h,
							    	jsonData:ss,
							    	success:function(arr){
							    		$("#sel"+h).val(arr[0].value);
							    	}
							    });
		    				})(i);
						}else{}
					}
				}
			},
			error: function(r){
				return false;
			}
		});
		if($('#hospitalContainer').length>0){
			new MultiPicker({
		    	input:'hospital',
		    	container:'hospitalContainer',
		    	jsonData:$hospital,
		    	success:function(arr){
		    		$("#hospital").val(arr[0].value);
		    		if($("#hospitalContainer").parent().next().hasClass("title") && $("#time").length>0){
		    			$("#hospitalContainer").parent().next().remove();
						$("#hospitalContainer").parent().next().remove();
		    		}
		    		var html = [];
		    		html.push('<div class="title" id="tt">请选择预约时间：</div><div class="content single">');
		    		html.push('<input type="text" name="" id="time" value="" readonly />');
		    		html.push('<div id="timeContainer'+arr[0].id+'"></div></div>');
		    		$("#hospitalContainer").parent().after(html.join(''));
		    		jcount=parseInt(arr[0].id.substr(-1))-1;
		    		html=[];
		    		html.push('<script type="text/javascript" src="/res/js/h5/appointmentForm/time_quantum.js" ><\/script>');
		    		$("#hospitalContainer").parent().after(html);
		    		hos(jcount);
				    new MultiPicker({
				    	input:'time',
				    	container:$("#time").next().attr("id"),
				    	jsonData:$time_quantum,
				    	success:function(arr){
				    		if(/已满/.test(arr[1].value)){
				    			$('#tips').html("预约人数已满,请重新选择！");
		    					$('#tips').show();
								setTimeout("$('#tips').hide();",2000);
				    		}else if(/周末不能预约/.test(arr[1].value)){
				    			$('#tips').html("周末不能预约！");
				    			$('#tips').show();
								setTimeout("$('#tips').hide();",2000);
				    		}else{
				    			$("#time").val(arr[0].value+arr[1].value.replace(/<.*?>/ig,",").split(",")[0]);
				    			$("#sel").val(arr[0].id);
				    		}
				    	}
				    });
		    	}
		    });	
		}else{}
	},2000);
});
</script>
<script>
	function IsPC() {
	   	var userAgentInfo = navigator.userAgent;
	    var Agents = ["Android", "iPhone",
	                "SymbianOS", "Windows Phone",
	                "iPad", "iPod"];
	    var flag = true;
	    for (var v = 0; v < Agents.length; v++) {
	        if (userAgentInfo.indexOf(Agents[v]) > 0) {
	            flag = false;
	            break;
	        }
	    }
	    return flag;
	};
	
</script>
</html>
