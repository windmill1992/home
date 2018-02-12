<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="cache-control" content="no-store" />
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>善园网 - 我要求助</title>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/needhelp.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>" />
</head>
<style type="text/css">
	#editor{float: left;}
	#edui1{width: 650px!important;margin-top: 10px;}
	#edui4,#edui5,#edui14,#edui15,#edui16,#edui17,#edui18,#edui21,#edui22,#edui30,#edui43,#edui50,#edui51,#edui77,#edui83,#edui92,
	#edui117,#edui118,#edui135,#edui136,#edui141,#edui142,#edui143,#edui116,#edui119,#edui76,
	#edui160,#edui165,#edui170,#edui175,#edui180,#edui185,#edui190,#edui219,#edui220,#edui225,#edui230,#edui231,#edui232,#edui233,
	#edui234,#edui237,#edui238,#edui247,#edui248,#edui249,#edui252,#edui253,#edui254,#edui255,#edui256,#edui257,#edui258,#edui259,
	#edui260,#edui261,#edui262,#edui263,#edui268,#edui269,#edui270,#edui273,#edui276,#edui277,#edui280,#edui126,#edui127,#edui12,
	#edui13,#edui128,
	#edui144,#edui145,#edui146,#edui147,#edui191,#edui1_bottombar{display: none!important;}
	.help_txt{line-height: 1.2;color: #666;font-size: 13px;margin: 5px 0 0 5px;}
</style>
<body>
	<%@ include file="./../common/newhead.jsp" %>
	<div class="bodyer">
		<div class="page clear">
			<!-- <div class="page-hd">
  <h2 class="w1000">我要求助</h2>
</div> -->
			<div class="page-bd">
				<div class="publish">
					<div class="step-progress step2"></div>
					<div class="step-detail">
						<div class="help_content">
							<ul>
								<li class="line1"><label><i class="important">*</i>求助标题：</label><span class="value"><input type="text" id="title" value="${project.title}" placeholder="标题不得超过20个字" value=""></span></li>
								<li class="line2"><label><i class="important">*</i>求助金额：</label><span class="value">
								<c:choose>
									<c:when test="${project.state==240 }">
										<input name="" id="cryMoney" value="<fmt:formatNumber value="${project.cryMoney}" pattern="0.00"/>" onKeyUp="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" type="text" readonly="readonly">
									</c:when>
									<c:otherwise>
										<input name="" id="cryMoney" value="<fmt:formatNumber value="${project.cryMoney}" pattern="0.00"/>" onKeyUp="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" type="text">
									</c:otherwise>
								</c:choose>
								元</span></li>
								<li><label><i class="important">*</i>是否需要志愿者报名：</label><span><input type="radio"  name="choose" value="1" style="margin:0px 10px;"/>是<input type="radio" name="choose" value="0" checked="checked" style="margin:0px 15px;"/>否</span></li>
								<li class="line4"><label>上传封面图片：封面图片建议尺寸640×420像素</label><span class="value">
                 					<form id="form1" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">	
			       						<div class="add_list" id="imgList">
			       						<c:if test="${project.coverImageUrl!=null && project.coverImageUrl!= ''}">
		       								<%-- <c:forEach var="record" items="${bflist}" varStatus="status"> --%>
											<div class="item" id="${project.coverImageId}">
												<img src="${project.coverImageUrl}" width="80px" height="80px"><i style="display: none;">×</i><!--<span>点击添加</span>-->
												<!--<textarea name="textarea" id="imgCon${record.id}" class="imgCon" cols="45" rows="5" maxlength="200">${record.description}</textarea>-->
											</div>
											<%-- </c:forEach> --%>
		       							</c:if>
			       						<c:choose>
			       							<c:when test="${project.coverImageUrl!=null && project.coverImageUrl!= ''}">
			       								<div class="item add">
													<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><!--<span>点击添加</span>-->
													<!--<textarea name="textarea" id="content" class="imgCon" cols="45" rows="5" maxlength="200" placeholder="图片描述">${record.description}</textarea>-->
												</div>
			       							</c:when>
			       							<c:otherwise>
			       								<div class="item add">
													<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></a><!--<span>点击添加</span>-->
													<!--<textarea name="textarea" id="content" class="imgCon" cols="45" rows="5" maxlength="200" placeholder="图片描述">${record.description}</textarea>-->
												</div>
			       							</c:otherwise>
			       						</c:choose>
													
										</div>
									</form>
									<div class="add_tip"></div>
								</span></li>
								<li class="line5"><label><i class="important">*</i>求助内容：为了便于您的编辑，图片宽度请控制在600像素以内，图片大小不超过2M</label></li>
								<li class="line3"><label style="visibility: hidden;"><i class="important"></i>求助内容：</label>
                     
                     				<span class="value"><!--<i>示例</i>-->
                    				<!--<textarea name="textarea" id="content" cols="45" rows="5" >${model}${project.content}</textarea>-->
                    				<script type="text/plain" id="editor" style="width:650px;height:300px;">${project.content}</script>
                    				</span>
								</li>
							</ul>
							<div style="clear:both;"></div>
						</div>
						<div class="nextbtn">
							<a id="needHelp2Submit" class="next" href="javascript:;">保存/下一步</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="./../common/newfooter.jsp" %>
	<input type="hidden" id="field" value="${field}">
	<input type="hidden" id="help" value="${help}">
	<input type="hidden" id="projectId" value="${project.id}">
	<input type="hidden" name="content" id="content" value='' />
	
	<script src="../../../res/ueditor/ueditor.config.js"></script>
	<script src="../../../res/ueditor/ueditor.all.min.js"></script>
	<script data-main="<%=resource_url%>res/js/dev/needhelp.js" src="<%=resource_url%>res/js/require.min.js"></script>
	<script>
		$(function(){
			var ue = UE.getEditor("editor");
			$(document).on("mousemove",function(){
				$("#content").val(ue.getContent());
			});
		});
	</script>
</body>
</html>