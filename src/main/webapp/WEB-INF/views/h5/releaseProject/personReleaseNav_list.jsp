<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
<title>请选择帮扶对象</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/releaseNav.css?v=20171128">
</head>
<body>
	<div class="header">
    	<a href="javascript:history.go(-1)" class="back">返回</a>
	    <div>请选择帮扶对象</div>
	</div>
	<div class="release_next">
	    <a href="/test/typeConfigList.do?helpType=1&type=${type}&zlId=${zlId}">
	        <div class="lis_two flex spb fcen">
	            <div class="lis_fl">
	                <p class="lis_text">为本人发起求助</p>
	                <p class="lis_text1">我自己需要帮助,我为自己筹款</p>
	            </div>
	            <span class="lis_sp"></span>
	        </div>
	    </a>
	    <a href="/test/typeConfigList.do?helpType=2&type=${type}&zlId=${zlId}">
	        <div class="lis_two flex spb fcen">
	            <div class="lis_fl">
	                <p class="lis_text">为家人发起求助</p>
	                <p class="lis_text1">受助人是您的直系亲属，需要提供户口本或其他证明</p>
	            </div>
	            <span class="lis_sp"></span>
	        </div>
	    </a>
	    <a href="/test/typeConfigList.do?helpType=3&type=${type}&zlId=${zlId}">
	        <div class="lis_two flex spb fcen">
	            <div class="lis_fl">
	                <p class="lis_text">为其他亲友发起求助</p>
	                <p class="lis_text1">受助人和您不是直系亲属，需要提供委托书</p>
	            </div>
	            <span class="lis_sp"></span>
	        </div>
	    </a>
	</div>
	<div class="release_next1">
	    <a href="/test/typeConfigList.do?helpType=4&type=${type}&zlId=${zlId}">
	        <div class="lis_two flex spb fcen">
	            <div class="lis_fl">
	                <p class="lis_text">为个人发起筹款</p>
	                <p class="lis_text1">受助人是一个人或者一个家庭</p>
	            </div>
	            <span class="lis_sp"></span>
	        </div>
	    </a>
	    <a href="/test/typeConfigList.do?helpType=5&type=${type}&zlId=${zlId}">
	        <div class="lis_two flex spb fcen">
	            <div class="lis_fl">
	                <p class="lis_text">为公益项目筹款</p>
	                <p class="lis_text1">受助人是一个群体或一类人</p>
	            </div>
	            <span class="lis_sp"></span>
	        </div>
	    </a>
	</div>
	<input type="hidden" id="type" name="type" value="${type }">
	<input type="hidden" id="zlId" name="zlId" value="${zlId }">
	
    <script src="/res/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript">
	    $(function(){
	         var type=$("#type").val();
	         if(type=="0"){
	           $(".release_next").show();
	           $(".release_next1").hide();
	         }else if(type=="1"){
	            $(".release_next").hide();
	            $(".release_next1").show();
	         }
	    });
	</script>
</body>
</html>