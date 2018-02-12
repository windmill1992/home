<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171208">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="topBg">
		<h1 class="topTitle flex spb fline">
			<a href="/ucenter/accountInfo.do" class="cancleBtn">取消</a>
			<span>修改昵称</span>
			<a href="javascript:;" class="savaBtn" id="saveSubmit">保存</a>
		</h1>
	</div>
	<div class="nickNameBox">
		<input type="text" value="${userName}" id="uName">
		<a href="javascript:;" class="clearBtn" id="clearUname"></a>
	</div>
	<!-- 修改成功 successTip -->
	<div class="popup" style="display: none;" id="successTip">
		<div class="bindTip">昵称修改成功</div>
	</div>
	<!-- 错误信息提示 -->
	<div class="errorBox" style="display: none; top: 45%;" id="checkDiv"><i></i><span id="checkInfo"></span></div>
</body>
<script src="/res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var base = window.baseurl;
	var dataUrl = {
		SSO_ExistName: base + "user/existuser.do", 	//检查用户名
		SSO_EditName: base + "ucenter/editUser.do", //更新用户名
		Account: base + "ucenter/accountInfo.do" 	//账户信息
	};
	var edit = {
		init: function() {
			var that = this;
			$('#uName').click(function() {
				$('#checkDiv').hide();
			});

			$('#saveSubmit').click(function() {
				that.editName();
			});

			$('#clearUname').click(function() {
				$('#uName').val('');
			});
		},
		/*
		isValidName: function(nameObj) {
	    	var that = this, nameObj = $(nameObj), nameVal = nameObj.val();
	         
	        var len = nameVal.length; 
			if (len==0){
	        	$('#checkInfo').html('请输入用户名');
	        	$('#checkDiv').show();
	            return false;
	        } 
	        if(len<4 || len>32){ 
	        	$('#checkInfo').html('用户名长度应为4-32位字符');
	        	$('#checkDiv').show();
	            return false;
	        }
			var nameReg = new RegExp("^([a-z|A-Z]+|[ \u4e00-\u9fa5]+|[0-9]+|[_|_]+)+$");
	        if (!nameReg.test(nameVal)){
	        	$('#checkInfo').html('账号可用中文、数字、英文（区分大小写）组成');
	        	$('#checkDiv').show();
	            return false;
	        }
			var reg = /^[1-9]\d*$|^0$/, c = that.checkMobileStr(nameVal);  
	        if(reg.test(nameVal)==true&&len!=11 || !c&&len==11 && reg.test(nameVal)==true){ 
	        	$('#checkInfo').val('用户名不能为除手机号码以外的纯数字');
	        	$('#checkDiv').show();
			    return false; 
	        }
	    	var data = {name:nameVal,t:new Date().getTime()};
			$.ajax({
				url:dataUrl.SSO_ExistName,
				data:data,
				success:function(result){
				   if(!result.flag){
				 	  	$('#checkInfo').html('用户名已存在');
	        		  	$('#checkDiv').show();
				   }
				} 
			});
			return true;
	    },
	    */
		editName: function() {
			var nameVal = $('#uName').val();
			if(nameVal == '') {
				$('#checkInfo').html('昵称不能为空');
				$('#checkDiv').show();
				return;
			}
			var data = {
				nickName: nameVal,
				t: new Date().getTime()
			};
			$.ajax({
				url: dataUrl.SSO_EditName,
				data: data,
				success: function(result) {
					if(!result.flag) {
						$('#checkInfo').html('' + result.errorMsg + '');
						$('#checkDiv').show();
					} else {
						$('#successTip').show();
						setTimeout(function(){
							location.href = dataUrl.Account;
						},1500);
					}
				}
			});
		}
	};
	edit.init();
</script>

</html>