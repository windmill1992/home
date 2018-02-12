<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/userAdvice.css">
	<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>

<body>
<div class="feed">
    <div class="header">
        <div class="head_fl fl"><a onclick="history.go(-1)" >&lt;返回</a></div>
        <div class="head_fr fr"><p>意见反馈</p></div>
    </div>
    <section>
   <div class="content">
       <div class="cont_y"></div>
       <p class="cont_t">意见填写</p>
   </div>
        <div class="content2">
            <form class="cont_ip">
                <textarea rows="30" cols="4" id="content" placeholder="请输入您的意见"></textarea>
            </form>
        </div>
        <div class="content3"><p>您的称呼</p></div>
        <div class="content4"><input type="text" placeholder="请输入您的姓名" id="name"/></div>
        <div class="content3"><p>您的电话</p></div>
        <div class="content4"><input type="text" placeholder="请输入您的电话" id="mobile"/></div>
        <div class="content3"><p>咨询电话：0571-87165191</p></div>
        <div class="content3 zx"><p>咨询QQ：2777819027</p></div>
    </section>
    <div class="footer">
        <a href="javascript:void(0);" id="foot_q"><p>确认提交</p></a>
    </div>
    <div class="cwts"><p>提交成功~</p></div>
</div>
<span id="flag" style="display:none">h5</span>
<span id="userId" style="display:none">${userId }</span>
<script type="application/javascript" >
    $(document).ready(function(){
    	
    	$("input").css("color","#B6B6B6");
            $("input").each(function(){
                var text = $(this).attr("placeholder");
               $(this).val(text);
            });
            $("input").focus(function(){
                if($(this).val() == $(this).attr("placeholder")){
                    $(this).val("");
                }
                $(this).css("color","#444");
            });
            $("input").blur(function(){
                if( $(this).val() == ""){
                    $(this).css("color","#B6B6B6");
                    var text = $(this).attr("placeholder");
                    $(this).val(text);
                }
            });
            $("#content").focus(function(){
                if($("#content").val()==""||$("#content").val()=="请输入您的意见"){
                    $("#content").val("");
                }
                $("#content").css("color","#444");
            });

        $("#foot_q").click(function (){
        	if($("#content").val()==""||$("#content").val()=="请输入您的意见"){
        		$("#content").css("color","red");
        		$("#content").val("请输入您的意见");
                return false;
        	}
        	$("input").each(function(){
                    if($(this).val() == $(this).attr("placeholder")){
                        $(this).css("color","red");
                        return false;
                    }
                });
        	if($('#name').val()!=$('#name').attr("placeholder")&&$('#mobile').val()!=$('#mobile').attr("placeholder")&&
        		$("#content").val()!=""&&$("#content").val()!="请输入您的意见"){
        		var name=$('#name').val(),mobile=$('#mobile').val(),content = $('#content').val(),flag = $('#flag').text(),userId = $('#userId').text();
              	$.ajax({
					url: 'http://www.17xs.org/user/addCustomerService.do',
					dataType: 'json',
					type: 'post',
					data: {
						name:name,
						mobile:mobile,
						content:content,
						userId:userId,
						type:1,
						flag:flag
					},
					success: function(result) {
					   if(result.flag==1){
					   		$(".cwts").show();
				            setTimeout(function () {
								window.location.href='http://www.17xs.org/index/index_h5.do';
							  }, 2000);
						}
					   
					},
					error: function(){ 
							return false;
						}					
				});
        	}
      });
    })
</script>
</body>
</html>
