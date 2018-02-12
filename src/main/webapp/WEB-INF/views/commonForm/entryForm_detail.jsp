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
<title>${commonForm.title }</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/timeRelaty.css"/>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
<body>
<div class="timeRelaty">
		<h3>${commonForm.title }</h3>
		<c:if test="${commonForm.content!=null && commonForm.content!='' }">
			<p class="txt">${commonForm.content }</p>
		</c:if>
		<!-- <p class="title">请输入孩子姓名：</p>
		<input type="text"/> -->
		
		<a href="javascript:history.go(-1)">
		<div class="foot">
			<input type="button" value="返回" class="submit" />
		</div>
		</a>
	</div>
<script type="text/javascript">
	var infoText='${information}';
	var html=[];
	var infoTexts=infoText.split(';');
	for(var i=0;i<infoTexts.length-1;i++){
		var text=infoTexts[i].split(':');
		html.push('<p class="title">'+text[0].replace(/"|“|”/g,'').replace("{","").replace("}","")+'</p>');
		html.push('<input type="text" value="'+text[1].replace(/"|“|”/g,'').replace("{","").replace("}","").replace(",","").replace("，","")+'" readonly="readonly"/>');
	}
	$("a").before(html);
</script>
</body>
</html>
