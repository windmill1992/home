<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>送上祝福</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css"/>
</head> 
<body>
<div id="pageContainer">
  <div class="publish">
  	<!--标题//-->
    <div class="top">
    	<a href="#" onclick="history.back();" class="back">&lt;返回</a>
    	<div class="title">送上祝福</div>
     </div>

     <!----成功提示//-->
    	<div class="form_box">
        	<ul>
            	<li class="text"><textarea cols="" rows="" id="leaveWord">支持公益</textarea></li>
            	<li class="text"><span style="font-size:20px;color:red;text-align: center" id="checkInfo"></span></textarea></li>
              	<li class="btns"><a href="#" class="pub_btn" id="sendSubmit">发送</a></li>
            </ul>
        </div>
    <!--页脚
      <div class="footer">
      	<span>© 2015 杭州智善  版权所有</span>
        <img src="images/min-logo.jpg"></div>//-->
  </div>
</div>
	<input type="hidden" id="tranNo" value="${tradeNo}">
	<input type="hidden" id="projectId" value="${projectId}">
	<input type="hidden" id="headImage" value="${headImage}">
	<input type="hidden" id="nickName" value="${nickName}">
	<input type="hidden" id="amount" value="${amount}">
	<input type="hidden" id="title" value="${title}">
	<input type="hidden" id="fromWish" value="${fromWish}">
</body>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var base=window.baseurl;
	var dataUrl={
		sendSubmit:base+'/project/sendWish.do'
	};
	var send={
		init:function()
		{
			var that=this;
			$('#sendSubmit').click(function(){
				var tranNum = $('#tranNo').val(),leaveWord = $('#leaveWord').val();
				if(tranNum == "")
				{
					$('#checkInfo').html("对不起网络繁忙，请稍候再试。");
					return ;
				}
				if(leaveWord == "")
				{
					$('#checkInfo').html("亲，请填写祝福内容。");
					return ;	
				}
				var len = leaveWord.length;
				if(len>45)
				{
					$('#checkInfo').html("亲，最多可填写45个字。");
					return ;
				}
				that.sendWish();
			});
			$('#leaveWord').click(function(){
				$('#checkInfo').html("");
				$('#leaveWord').css("color","#000000");
			});
			
		},
		sendWish:function()
		{
			$.ajax({
				url:dataUrl.sendSubmit,
				data:{tranNum:$('#tranNo').val(),leaveWord:$('#leaveWord').val(),t:new Date().getTime()},
				success: function(result)
				{
						if(result.flag==1)
						{
							//$('#checkInfo').html("发送成功，感谢您的祝福。");
							//window.history.back();
							//window.history.go(-1);
							//window.location.reload();
							var url = "http://www.17xs.org/project/paysuccess_h5/"
							+ "?pName="+ $('#title').val() + "&amount="
							+ $('#amount').val()
							+ "&tradeNo="+$('#tranNo').val()
							+ "&nickName="+$('#nickName').val()+"&headImage="+$('#headImage').val()
							+ "&projectId="+$('#projectId').val()+ "&fromWish="+$('#fromWish').val();
							window.location.href = url;
						}
						else
						{
							$('#checkInfo').html("发送失败,请稍候再试。");
						}	
				}
			});
		}
	}
	send.init();
</script>
</html>