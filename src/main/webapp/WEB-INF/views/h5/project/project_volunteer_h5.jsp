<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园网 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css?v=20171212">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/enlist.css?v=20171212">
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body>
	<div class="topNav">
		<a onclick="history.go(-1);" class="backLink" href="javascript:;"></a>
		<h1>基本信息</h1>
	</div>
	<div>
		<div class="form-item">
			<span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</span>
			<input id="name" name="name" value="" type="text" data-placeholder="请输入您的姓名" />
		</div>
		<div class="form-item">
			<span>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</span>
			<input id="sex" name="sex" value="" type="text" data-placeholder="请输入您的性别" />
		</div>
		<div class="form-item">
			<span>服务时段</span>
			<input id="serviceTime" name="serviceTime" value="" type="text" data-placeholder="请输入您可用的服务时间" />
		</div>
		<div class="form-item">
			<span>身份证号</span>
			<input id="indentity" name="indentity" value="" type="text" data-placeholder="请输入您的身份证号码" onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" />
		</div>
		<div class="form-item">
			<span>联系方式</span>
			<input id="mobile" name="mobile" value="" type="text" data-placeholder="请输入您的电话号码" onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" />
		</div>
		<div class="form-item">
			<span>联系地址</span>
			<input id="address" name="address" value="" type="text" data-placeholder="请输入您的联系地址" />
		</div>
		<div class="form-item">
			<span>单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位</span>
			<input id="position" name="position" value="" type="text" data-placeholder="请输入您的工作单位" />
		</div>
		<div class="form-item">
			<span>你的邮箱</span>
			<input id="email" name="email" value="" type="text" data-placeholder="请输入你的邮箱方便我们与您联系" />
		</div>
	</div>
	<div style="text-align: center">
		<a href="javascript:;" class="btnStyle" id="crySubmit">确认报名</a>
	</div>
	<span id="projectId" style="display:none">${projectId}</span>
	<%--报名成功提示--%>
	<div class="project_cgts" id="tcdiv" style="display:none;">
		<p>报名成功</p>
	</div>
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("input").css("color", "#B6B6B6");
			$("input").each(function() {
				var text = $(this).attr("data-placeholder");
				$(this).val(text);
			});
			$("input").focus(function() {
				if($(this).val() == $(this).attr("data-placeholder")) {
					$(this).val("");
				}
				$(this).css("color", "#444");
			});
			$("input").blur(function() {
				if($(this).val() == "") {
					$(this).css("color", "#B6B6B6");
					var text = $(this).attr("data-placeholder");
					$(this).val(text);
				}
			});
			$(".btnStyle").click(function() {
				$("input").each(function() {
					if($(this).val() == $(this).attr("data-placeholder")) {
						$(this).css("color", "red");
						return false;
					}
				});
				if($('#name').val() != $('#name').attr("data-placeholder") && $('#mobile').val() != $('#mobile').attr("data-placeholder") &&
					$('#indentity').val() != $('#indentity').attr("data-placeholder") && $('#serviceTime').val() != $('#serviceTime').attr("data-placeholder") &&
					$('#email').val() != $('#email').attr("data-placeholder") && $('#sex').val() != $('#sex').attr("data-placeholder") &&
					$('#address').val() != $('#address').attr("data-placeholder") && $('#position').val() != $('#position').attr("data-placeholder")) {

					var name = $('#name').val(),
						mobile = $('#mobile').val(),
						indentity = $('#indentity').val(),
						address = $('#address').val(),
						serviceTime = $('#serviceTime').val(),
						email = $('#email').val(),
						sex = $('#sex').val(),
						position = $('#position').val();
					var projectId = $("#projectId").text();
					$.ajax({
						url: 'http://www.17xs.org/project/addVolunteer.do',
						dataType: 'json',
						type: 'post',
						data: {
							name: name,
							mobile: mobile,
							indentity: indentity,
							serviceTime: serviceTime,
							email: email,
							sex: sex,
							address: address,
							position: position,
							projectId: projectId,
							personType: 0
						},
						success: function(result) {
							if(result.flag == 1) {
								$('#tcdiv').show();
								setTimeout(function() {
									location.href = 'http://www.17xs.org/project/view_h5.do?projectId=' + projectId;
								}, 2000);
							}
						}
					});
				}
			});
			$(".bottomTab button").click(function() {
				$("#popup").show();
			});
			$("#download").click(function() {
				$(".motalTop>.p1").eq(0).hide();
				$(".motalTop>.p1").eq(1).show();
				$(this).hide();
				$("#confirm").show();
				$(".motalBottom").css("background-color", "#fff");
			});
			$("#confirm").click(function() {
				$("#popup").hide();
				$(".motalTop>.p1").eq(1).hide();
				$(".motalTop>.p1").eq(0).show();
				$(this).hide();
				$("#download").show();
				$(".motalBottom").css("background-color", "#ff6811");
			});
		});
	</script>
</body>
</html>