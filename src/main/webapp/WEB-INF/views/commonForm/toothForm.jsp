<!DOCTYPE html>
<html lang="en">
<head>	
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
<title>牙齿检查</title>
<script type="text/javascript" src="../../../../res/js/jquery-1.8.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/toothForm.css"/>
</head>
<body style="background: #f1f1f1;">
<input type="hidden" id="formId" value="${formId }">
<input type="hidden" id="userId" value="${user.id }">
<input type="hidden" id="nickName" value="${user.nickName }">
<input type="hidden" id="titleName" value="${title }">
<input type="hidden" id="special_fund_id" value="${special_fund_id }">
	<div class="teeth_check">
		<h3>儿童口腔健康调查口腔检查记录表</h3>
		<ul style="padding: 0;">
				<li><span class="txt">姓名：</span> <input type="text" class="underline" value="${apiHospitalAppoint.name }"/><span class="error"></span></li>
				<li><span class="txt">受检者编号：</span><input type="text" class="underline" value="${apiHospitalAppoint.id_card }" /><span class="error"></span></li>
				<li><span class="txt">户口类型：</span><input type="radio" name="residence" class="rad_1" id="r_1" />
					<label for="r_1"></label><span>城市</span>&nbsp;
					<input type="radio" name="residence" class="rad_1" id="r_2" />
					<label for="r_2"></label><span>农村</span>
					<span class="error"></span>
				</li>
				<li><span class="txt">性别：</span><c:if test=""></c:if>
					<input type="radio" name="sex" class="rad_1" id="r_3" <c:if test="${apiHospitalAppoint.sex=='男' }">checked</c:if> />
					<label for="r_3"></label><span>男</span>&nbsp;
					<input type="radio" name="sex" class="rad_1" id="r_4" <c:if test="${apiHospitalAppoint.sex=='女' }">checked</c:if> />
					<label for="r_4"></label><span>女</span>
					<span class="error"></span>
				</li>
				<li><span class="txt">民族：</span><input type="text" class="underline" size="6" style="padding-left: 10px;"/><span class="error"></span></li>
				<li><span class="txt">出生日期：</span> <input type="text" id="birth" class="underline" value="${apiHospitalAppoint.birth }"/><span class="error"></span></li>
				<li><span class="txt">检查者编号：</span> <input type="text" class="underline" size="18" value=""/><span class="error"></span></li>
				<li><span class="txt">检查日期：</span> <input type="text" id="checkdate" class="underline" value="${checkTime }" /><span class="error"></span></li>
			</ul>
		
		<div class="line"></div>
		<div class="line"></div>
		
		<div class="teethtop">
			<p class="fl">龋齿1</p>
			
			<div class="num lt">
				
				<p class="teeth left">
					<span class="hidden">0</span>
					<span>55</span>
					<span>54</span>
					<span>53</span>
					<span>52</span>
					<span>51</span>
				</p>
				
				<p class="teeth left">
					<span>16</span>
					<span>15</span>
					<span>14</span>
					<span>13</span>
					<span>12</span>
					<span>11</span>
				</p>
				
				<div class="fr">
					<input type="text" size="1"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text" style="margin-right: 10px;"/>
				</div>
				
				<div class="clear"></div>
			</div>
			
			<div class="num rt">
				
				<p class="teeth right">
					<span>61</span>
					<span>62</span>
					<span>63</span>
					<span>64</span>
					<span>65</span>
					<span class="hidden"></span>
				</p>
				
				<p class="teeth right">
					<span>21</span>
					<span>22</span>
					<span>23</span>
					<span>24</span>
					<span>25</span>
					<span>26</span>
				</p>
				
				<div class="fl">
					<input type="text" style="margin-left: 10px;"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
				</div>
				
				<div class="clear"></div>
			</div>
			
			<p class="fl">龋齿2</p>
			<div class="clear"></div>
		</div>
		
		<div class="teethbot" >
			<p class="fl">龋齿3</p>
			
			<div class="num lb">
				<div class="fr">
					<input type="text" size="1"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text" style="margin-right: 10px;"/>
				</div>
				
				<p class="teeth left fr">
					<span>46</span>
					<span>45</span>
					<span>44</span>
					<span>43</span>
					<span>42</span>
					<span>41</span>
				</p>
				
				<p class="teeth left fr">
					<span class="hidden"></span>
					<span>85</span>
					<span>84</span>
					<span>83</span>
					<span>82</span>
					<span>81</span>
				</p>
				
				<div class="clear"></div>
			</div>
			
			<div class="num rb">
				<div class="fl">
					<input type="text" style="margin-left: 10px;"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
					<input type="text"/>
				</div>
				
				<p class="teeth right fl">
					<span>31</span>
					<span>32</span>
					<span>33</span>
					<span>34</span>
					<span>35</span>
					<span>36</span>
				</p>
				
				<p class="teeth right fl">
					<span>71</span>
					<span>72</span>
					<span>73</span>
					<span>74</span>
					<span>75</span>
					<span class="hidden"></span>
				</p>
				
				<div class="clear"></div>
			</div>
			<p class="fl">龋齿4</p>
			<div class="clear"></div>
		</div>
		
		<div class="line"></div>
		
		<div class="check_target fl">
			<table>
				<caption style="text-align: left;">龋齿的检查指标</caption>
				<tr><th>乳牙</th><th>恒牙</th><th></th></tr>
				<tr><td>A</td><td>0</td><td>无龋</td></tr>
				<tr><td>B</td><td>1</td><td>有龋</td></tr>
				<tr><td>C</td><td>2</td><td>已填充有龋</td></tr>
				<tr><td>D</td><td>3</td><td>已填充无龋</td></tr>
				<tr><td>E</td><td>4</td><td>因龋缺失</td></tr>
				<tr><td>-</td><td>5</td><td>因其他原因失牙</td></tr>
				<tr><td>F</td><td>6</td><td>窝沟封闭</td></tr>
				<tr><td>G</td><td>7</td><td>非龋全冠</td></tr>
				<tr><td>-</td><td>8</td><td>未萌牙</td></tr>
				<tr><td>W</td><td>T</td><td>牙外伤</td></tr>
				<tr><td>N</td><td>9</td><td>不作记录</td></tr>
			</table>
		</div>
		
		<div class="periodontal_condition fl">
			<p>牙周状况</p>
			<div class="periodontal">
				<p>
					<span>16</span>
					<span>11</span>
					<span>26</span>
				</p>
				<div class="num2">
					<table border="1" cellspacing="0" cellpadding="">
						<tr>
							<td><input type="text" style="margin-left: 0;"/></td>
							<td><input type="text" /></td>
							<td><input type="text" /></td>
						</tr>
						<tr>
							<td><input type="text" /></td>
							<td><input type="text" /></td>
							<td><input type="text" /></td>
						</tr>
					</table>
				</div>
				<p>
					<span>46</span>
					<span>31</span>
					<span>36</span>
				</p>
			</div>
			
			<div class="check_target2">
				<table>
					<caption style="text-align: left;">牙龈出血的检查指标</caption>
					<tr><td>0</td><td>无</td></tr>
					<tr><td>1</td><td>有</td></tr>
					<tr><td>9</td><td>不作记录</td></tr>
					<tr><td>X</td><td>缺失牙</td></tr>
				</table>
			</div>
		</div>
		<div class="clear"></div>
		<button class="submit">提交</button>
	</div>
	<script type="text/javascript" src="../../../../res/laydate-master/laydate.dev.js"></script>
	<script>laydate({elem: '#birth',format: 'YYYY-MM' });laydate({elem: '#checkdate' ,format: 'YYYY-MM-DD'});</script>
	<script>
		$(function(){
			var baseUrl="http://www.17xs.org/";
			$(".num span").click(function(){
				if($(this).hasClass("select")){
					$(this).removeClass("select");
				}else{
					$(this).addClass("select");
					$(this).parent().siblings().children("span").eq($(this).index()).removeClass("select");
				}
			});
			function get(){
				var s = "" , t = "",content="";
				var arr = new Array(4);
				arr[0] = new Array(6);
				arr[1] = new Array(6);
				arr[2] = new Array(6);
				arr[3] = new Array(6);
				$(".teeth_check ul li").each(function(index,ele){
					if($(this).children("input").hasClass("rad_1")){
						content+='{"'+$(this).children(".txt").text() +'":"'+ $(this).children(":checked").next().next().text()+'"};';
					} else {
						content+='{"'+$(this).children(".txt").text() +'":"'+ $(this).children("input").val()+'"};';
					}
					errortips(this,index);
				});
				$(".num").each(function(index,ele){
					$(this).find("input").each(function(index2,ele2){
						arr[index][index2] = $(this).val();
					});
				});
				
				for (var i=0;i<arr.length;i++) {
					for (var j=0;j<arr[i].length;j++) {
							s = $(".num").eq(i).find("span.select:nth-child("+(j+1)+")").text();
							if(s==""&&i<2){s = $(".num").eq(i).find("span:nth-child("+(j+1)+")").text().substr(-2,2);}
							else if(s==""&&i>1){s = $(".num").eq(i).find("span:nth-child("+(j+1)+")").text().substr(0,2);}
							if(s.indexOf("1")==0||s.indexOf("5")==0){//左上龋齿1-1~1-6
								content+='{"左上龋齿1-'+s.substring(1,2) +'：":"'+ arr[i][j]+'"};';
							}
							if(s.indexOf("4")==0||s.indexOf("8")==0){//左下龋齿3-1~3-6
								content+='{"左下龋齿3-'+s.substring(1,2) +'：":"'+ arr[i][j]+'"};';
							}
							if(s.indexOf("6")==0||s.indexOf("2")==0){//右上龋齿2-1~2-6
								content+='{"右上龋齿2-'+s.substring(1,2) +'：":"'+ arr[i][j]+'"};';
							}
							if(s.indexOf("3")==0||s.indexOf("7")==0){//右下龋齿4-1~4-6
								content+='{"右下龋齿4-'+s.substring(1,2) +'：":"'+ arr[i][j]+'"};';
							}
					}
				}
				var arr2 = new Array(6);
				$(".periodontal span").each(function(index,ele){
					arr2[index] = $(this).parent().siblings(".num2").find("input").eq(index).val();
				});
			
				for (var i=0;i<arr2.length;i++) {
					t = $(".periodontal span").eq(i).text();
					content+='{"牙周状况-'+ t +'：": "'+ arr2[i]+'"};';
				}
				return content;
			}
			$("button").click(function(){
				var jsonText = get();
				console.log(jsonText);
				if($(".error").text()!=""){return false;}
				 $.ajax({
		                	type: "get",
		    				url:baseUrl+"resposibilityReport/saveForm.do",
		    				data:{hospitalAppointId:${hospitalAppointId},title:$('#titleName').val(),information:jsonText,formId:$('#formId').val(),userId:$('#userId').val()==null?"":$('#userId').val(),user_nick_name:$('#nickName').val(),special_fund_id:$('#special_fund_id').val(),createTime:new Date()},
		    				success: function(result){
		    					if(result.flag=='0'){
		    						return false;
		    					}else {
		    			             if(result.obj.gotoUrl!=null&&result.obj.gotoUrl!=''){
		    			            	 setTimeout(function(){
		        			            	 location.href=result.obj.gotoUrl;

		        			            	 },1000);
		    			             }
		    			             else{
		    			            	 setTimeout(function(){
		        			            	 location.href="http://www.17xs.org";
		        			            	 },1000);
		    			             }
		    			             return true;
		    					}
		    				},
		    				error: function(r){
		    					return false;
		    				}
		    			});
			});
			$(document).on("click",function(){
		$(".teeth_check ul li").each(function(index,ele){
			if(errortips(this,index)){return false;}
		});
	}).on("click","li input",function(e){
		$(".teeth_check ul li").each(function(index,ele){
			if(errortips(this,index)){$(".error").not(":eq("+index+")").text("");return false;}
		});
		$(".error").eq($(this).parent().index()).text("");
		e.stopPropagation();
		
	}).on("click",".rad_1",function(){
		userBG();
	});
	function errortips(obj,index){
		var f = 0;
		switch (index){
			case 0:if($(obj).children("input").val()==""){$(".error").eq(0).text("请填写姓名！");f=1;}else{$(".error").eq(0).text("");}
				break;
			case 1:if($(obj).children("input").val()==""){$(".error").eq(1).text("请填写受检者编号！");f=1;}else{$(".error").eq(1).text("");}
				break;
			case 2:if($(obj).children(":checked").size()<1){$(".error").eq(2).text("请选择户口！");f=1;}else{$(".error").eq(2).text("");}
				break;
			case 3:if($(obj).children(":checked").size()<1){$(".error").eq(3).text("请选择性别！");f=1;}else{$(".error").eq(3).text("");}
				break;
			case 4:if($(obj).children("input").val()==""){$(".error").eq(4).text("请填写民族！");f=1;}else{$(".error").eq(4).text("");}
				break;
			case 5:if($(obj).children("input").val()==""){$(".error").eq(5).text("请填写出生日期！");f=1;}else{$(".error").eq(5).text("");}
				break;
			case 6:if($(obj).children("input").val()==""){$(".error").eq(6).text("请填写检查者编号！");f=1;}else{$(".error").eq(6).text("");}
				break;
			case 7:if($(obj).children("input").val()==""){$(".error").eq(7).text("请填写检查日期！");f=1;}else{$(".error").eq(7).text("");}
				break;
			default:
				break;
		}
		return f;
	}
	function userBG(){
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Trident")>-1 && userAgent.indexOf("Opera")==-1){
			$("span").css({"padding":"0 0.14rem"});
			$(".periodontal_condition .periodontal p span").css({"padding":"0 0.24rem"});
			$(".rad_1#r_1:checked + label").css({"background-position-x":"0.13rem"});
			$(".rad_1#r_2:checked + label").css({"background-position-x":"0.10rem"});
			$(".rad_1#r_3:checked + label").css({"background-position-x":"0.13rem"});
			$(".rad_1#r_4:checked + label").css({"background-position-x":"0.10rem"});
		}
	}
	userBG();
});
	</script>
</body>
</html>
