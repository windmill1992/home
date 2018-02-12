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
<title>医院预约报名</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/util/city.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/timeRelaty.css"/>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/h5/appointmentForm/hospitals.js" ></script>
</head>
<body>
<input type="hidden" id="formId" value="${commonForm.id }">
<input type="hidden" id="userId" value="${user.id }">
<input type="hidden" id="nickName" value="${user.nickName }">
<input type="hidden" id="titleName" value="${commonForm.title }">
<input type="hidden" id="special_fund_id" value="${commonForm.special_fund_id }">
<div class="timeRelaty">
		<h3>${commonForm.title }</h3>
		<p class="txt">${commonForm.content }</p>
		
		<p class="title">请输入孩子姓名：</p>
		<input type="text" id="name"/>
		
		<p class="title">请选择医院：</p>
		<input type="text" name="" id="hospital" value="" readonly />
		<div id="hospitalContainer"></div>
		
		<div class="foot">
			<input type="button" value="提交" class="submit"/>
		</div>
	</div>
	<input type="hidden" name="sel" id="sel" value="" />
	<script type="text/javascript">
	var jcount=0;
	var a=${counts};
	</script>
	<script type="text/javascript" src="<%=resource_url%>res/js/common/city.js" ></script>
	<script type="text/javascript">
		$(document).on("click",".submit",function(){
			var txt = $(":text");
			var str = "";
			txt.each(function(){
				if($(this).val()==""){
					$(this).siblings(".title").removeClass("bg");
					$(this).prev().addClass("bg");
					return false;
				} else {
					str += '{"'+$(this).prev().text().substr(3)+'":"'+$(this).val()+'"};';
				}
			});
			if($('#name').val()==""){
				$('#xingming').show();
				setTimeout("$('#xingming').hide();",2000);
				return false;
			}
			if($('#hospital').val()==""){
				$('#yiyuan').show();
				setTimeout("$('#yiyuan').hide();",2000);
				return false;
			}
			if($('#time').val()==""){
				$('#shijian').show();
				setTimeout("$('#shijian').hide();",2000);
				return false;
			}
			//console.log(str);
			$.ajax({
				url:"http://www.17xs.org/resposibilityReport/saveAppointmentForm.do",
				data:{hospital:$('#hospital').val(),title:$('#titleName').val(),information:str,formId:$('#formId').val(),userId:$('#userId').val()==null?"":$('#userId').val(),user_nick_name:$('#nickName').val(),special_fund_id:$('#special_fund_id').val(),createTime:new Date()},
				success: function(result){
					if(result.flag=='0'){
						$('#fail').html(result.errorMsg);
   						$('#fail').show();
   						setTimeout("$('#fail').hide();",3000);
   						return false;
   					}else {
   						 $('#success').show();
   			             setTimeout("$('#success').hide();",3000);
   			             if(result.obj.gotoUrl!=null&&result.obj.gotoUrl!=''){
   			            	 setTimeout(function(){
       			            	 location.href=result.obj.gotoUrl;

       			            	 },1000);
   			             }
   			             else{
   			            	 setTimeout(function(){
       			            	 location.href=base;
       			            	 },1000);
   			             }
   			             return true;
   					}
				},
				error: function(r){
					$('#fail').html('参数错误！');
					$('#fail').show();
					setTimeout("$('#fail').hide();",2000);
		    		return false;
		    	}
			});
		}).on("click",function(){
			$(":text").each(function(){
				if($(this).val()!=""){
					$(this).prev().removeClass("bg");
				}
			});
		});
		//格式化json
		var jsonText=jQuery.parseJSON('${commonForm.form}');
		//获取医院数组
		for(var i=0;i<jsonText.list.length;i++){
			var hospitals=[];
			if(jsonText.list[i].type=="dropDown"){//下拉框
			var hospital=jsonText.list[i].value.split(',');
			for(var j=0;j<hospital.length;j++){
			 hospitals.push(JSON.parse('{"id":"00'+j+'","value":"'+hospital[j]+'"}'));
			}
		}}
		new MultiPicker({
	    	input:'hospital',
	    	container:'hospitalContainer',
	    	jsonData:hospitals,
	    	success:function(arr){
	    		$("#hospital").val(arr[0].value);
	    		if($("#hospitalContainer").next().hasClass("title")){
	    			$("#hospitalContainer").nextAll().not(".foot").remove();
	    		}
	    		var html = [];
	    		html.push('<p class="title">请选择预约时间：</p>');
	    		html.push('<input type="text" name="" id="time" value="" readonly />');
	    		html.push('<div id="timeContainer'+arr[0].id+'"></div>');
	    		$(".foot").before(html);
	    		jcount=parseInt(arr[0].id.substr(-1));
	    		html=[];
	    		html.push('<script type="text/javascript" src="http://www.17xs.org/res/js/h5/appointmentForm/time_quantum.js" ><\/script>');
	    		$(".foot").before(html);
			    new MultiPicker({
			    	input:'time',
			    	container:$("#time").next().attr("id"),
			    	jsonData:$time_quantum,
			    	success:function(arr){
			    		if(/已满/.test(arr[1].value)){
			    			$('#fail').html("预约人数已满,请重新选择！");
	    					$('#fail').show();
							setTimeout("$('#fail').hide();",2000);
			    		}else if(/周末不能预约/.test(arr[1].value)){
			    			$('#fail').html("周末不能预约！");
			    			$('#fail').show();
							setTimeout("$('#fail').hide();",2000);
			    		}else{
			    			$("#time").val(arr[0].value+arr[1].value.replace(/<.*?>/ig,",").split(",")[0]);
			    			$("#sel").val(arr[0].id);
			    		}
			    	}
			    });
	    	}
	    });
	    function userBG(){
			var userAgent = navigator.userAgent;
			if (userAgent.indexOf("Trident")>-1 && userAgent.indexOf("Opera")==-1){
				$(".rad_1:checked + label").css({"background-position-x":"0.13rem"});
			}
		}
	</script>
<div class="tips" id="success" style="display: none;">恭喜您预约成功！</div>
<div class="tips" id="fail" style="display: none;">预约失败失败！</div>
<div class="tips" id="xingming" style="display: none;">请填写姓名！</div>
<div class="tips" id="yiyuan" style="display: none;">请选择医院！</div>
<div class="tips" id="shijian" style="display: none;">请选择时间段！</div>
<div class="tips" id="xingqi" style="display: none;">请选择星期！</div>
<div class="tips" id="jutishijian" style="display: none;">请选择具体时间！</div>
</body>
</html>
