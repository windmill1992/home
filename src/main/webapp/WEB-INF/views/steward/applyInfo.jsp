<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园 - 加入善管家</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/applyStep.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div id="bodyer" class="bodyer applySteps">
	<div class="page">
    	<!-- <div class="page-hd">
        	<div class="page-tit"><h2 class="w1000">加入善管家</h2></div>
        </div> -->
        <div class="page-bd"> 
            <div class="applyStp-main">
            	<div class="applyStp-hd">
                	<div class="applyStp-tit">善管家申请流程</div>
                    <ul class="applyStp-tab">
                    	<li><i>1</i><span>实名认证</span></li>
                        <li class="curTab"><i>2</i><span>完善资料</span></li>
                        <li><i>3</i><span>等待审核</span></li>
                    </ul>
                    
                </div>
                <c:choose>   
				  <c:when test="${userState==203}">   
				         <div class="helpprim">实名认证信息已审核通过，请填写您的申请信息</div> 
				  </c:when> 
				  <c:otherwise>   
				       <div class="helpprim">实名认证信息已提交审核，请填写您的申请信息</div>   
				  </c:otherwise>   
				</c:choose>
                <div class="applyStp-bd">
                	<dl class="applyStp-address">
                    	<dt>你目前所在地：</dt>
                        <dd>
                        	<select id="province"></select>
                            <select id="city"> </select>
                            <select id="county"></select>
                        </dd> 
                    </dl>
                	<dl class="applyStp-field">
                    	<dt>您感兴趣的领域：</dt>
                        <dd id="applyField">
                        	<p class="first"><i class="ckb">疾病</i><i>助学</i><i>救灾</i><i>环保</i><i>动物保护</i><i>农业农村</i></p>
                            <p><i>劳工</i><i>老人</i><i>宗教信仰</i><i>文化艺术</i><i>残障</i><i class="last">（可多选）</i></p> 
                        </dd> 
                    </dl>
                    <dl class="applyStp-reason">
                    	<dt>您申请的理由：</dt>
                        <dd>
                        	<textarea id="applyReason" placeholder="申请理由最多为500个字"></textarea>
                        </dd>
                    </dl>
                    <dl class="applyStp-proof">
                    	<dt>资质证明:</dt>
                        <dd>
                        	<form id="proofImgForm" name="proofImgForm" method="post"  action="http://www.17xs.org/file/uploadItem.do" enctype="multipart/form-data">
                        	 <ul id="proofImgList" class="imgList">
                                <li class="add">
                                	<span>+</span>
                                	<input id="proofImgFile" type="file" name="file" class="file-input"/>
                                	<input type="hidden" name="type" id="type" value="6"/>
                                </li>
                            </ul>
                            </form>
                            <p class="prim clear">
                                注：图片大小每张请控制在2M以内，最多可上传5张图片
                            </p>
                        </dd>
                    </dl>
                    <div id="dataMsg" class="dataMsg"><i class="error-icon"></i><span></span></div>
                    <div class="btnBox"><a id="submitData" class="btn">提交资料</a></div>
                </div>
            </div>
        </div>
    </div>

</div>
<%@ include file="./../common/newfooter.jsp"%>
<script data-main="<%=resource_url%>res/js/dev/donateInfo.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
