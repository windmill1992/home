<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网-认领项目</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/projectClaim.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> <input type="hidden" id="showState" value="${showState }" />
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            <c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<%@ include file="./../common/eleft_menu.jsp"%>
         	</c:when>
         	<c:otherwise>
         		<%@ include file="./../common/pleft_menu.jsp"%>
         	</c:otherwise>
         	</c:choose>
            </div>
            <div class="uCEN-R">
           		<!----认领项目-///--->
				<div class="help_list spage_list">
					<div class="list_hd">
						<span class="title">认领项目</span>
						<div class="claimFr" style="height: 40px;float: right">
							<li class="fqr fqr1"><p class="zt"><input type="hidden" value="发起人"/><span>发起人</span></p><span class="ztIcon"></span>
								<ul class="fqr_ul" style="display: none">
									<a href="javaScript:void(0);"><li class="col2_li faqi"><input type="hidden" value="全部"/><span>全部</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li faqi"><input type="hidden" value="individualUsers"/><span>个人</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li faqi"><input type="hidden" value="enterpriseUsers"/><span>企业</span></li></a>
								</ul>
							</li>
							<li class="fqr fqr2"><p class="zt"><input type="hidden" value="项目类型"><span>项目类型</span></p><span class="ztIcon"></span>
								<ul class="fqr_ul" style="display: none">
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="全部"/><span>全部</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="elderly"/><span>特殊群体</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="education"/><span>教育救助</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="disease"/><span>医疗救助</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="povertyAlleviation"/><span>贫困救助</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="disasterRelief"/><span>灾害救助</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="Publicinterest"/><span>公益众筹</span></li></a>
									<a href="javaScript:void(0);"><li class="col2_li lx"><input type="hidden" value="crowdfunding"/><span>商品众筹</span></li></a>
								</ul>
							</li>
							<li class="fqr fqr3"><p class="zt"><input type="hidden" value="项目地点"/><span>项目地点</span></p><span class="ztIcon"></span>
								<ul class="fqr_ul" style="display: none">
									<a href="javaScript:void(0);"><li class="col2_li dd"><input type="hidden" value="全部"/><span>全部</span></li></a>
									<c:forEach var="list" items="${list }">
										<c:if test="${list.province != null && list.province != '' }">
											<a href="javaScript:void(0);"><li class="col2_li dd"><input type="hidden" value="${list.province }"/><span>${list.province }</span></li></a>
										</c:if>
									</c:forEach>
								</ul>
							</li>
						</div>
					</div>
					<dl class="list_bd" id="listShow">
						<dt>
							<ul>
								<li class="col1">项目标题</li>
								<li class="col3">筹款金额</li>
								<li class="col4">发起方</li>
								<li class="col5">项目类型</li>
								<li class="col6">操作</li>
							</ul>
						</dt>
					</dl>
				</div>
			</div>
		</div>
    </div> 
</div>
	<!--认领项目弹框-->
	<div class="claimBox" style="display: none" id="renling">
		<div class="cp2yLock" id="cp2yLock0" style="width: 100%; height: 1833px; display: block;"></div>
		<div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 359px; left: 395px;">
			<div class="dialogTitle">
				<i class="l"></i><span>提示信息</span><a id="renlingClose"></a><i class="r"></i>
			</div>
			<div class="dialogContent">
				<div class="alertCon renlingAlert"><span>您确认认领项目：“回复多发点覅地方”吗？</span></div>
				<div class="Btns">
					<a id="confirm">确定</a>
					<a id="cancel">取消</a>
				</div>
			</div>
		</div>
	</div>
	<!--发布人信息弹框-->
	<div class="claimBox" style="display: none" id="userInfo">
		<div class="cp2yLock" id="cp2yLock0" style="width: 100%; height: 1833px; display: block;"></div>
			<div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 359px; left: 395px;margin: -75px 0 -476px -179px;">
			<div class="dialogTitle" style="width:610px;">
				<i class="l"></i><span>发布人信息</span><a class="closeUserInfo" data="0"></a><i class="r"></i>
			</div>
			<div class="dialogContent" style="width:569px;">
				<p id="name"></p>
				<p id="identity"></p>
				<p id="tel"></p>
				<p id="address"></p>
			</div>
		</div>
	</div>
	<!--申请资质弹框-->
	<div class="claimBox" style="display: none" id="zizhi">
		<div class="cp2yLock" id="cp2yLock0" style="width: 100%; height: 1833px; display: block;"></div>
		<div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 359px; left: 395px;">
			<div class="dialogTitle">
				<i class="l"></i><span>提示信息</span><a id="renlingClose"></a><i class="r"></i>
			</div>
			<div class="dialogContent">
				<div class="alertCon"><span>您还没有认证公募基金会资质，无法查看个人信息/无法认领该项目。</span></div>
				<div class="Btns">
					<a id="zizhi_confirm">申请资质</a>
					<a id="zizhi_cancel">取消</a>
				</div>
			</div>
		</div>
	</div>
	<!--认领成功弹框-->
	<div class="claimBox" style="display: none" id="renlingSuccess">
		<div class="cp2yLock" id="cp2yLock0" style="width: 100%; height: 1833px; display: block;"></div>
		<div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 359px; left: 395px;">
			<div class="dialogTitle">
				<i class="l"></i><span>提示信息</span><a id="renlingClose"></a><i class="r"></i>
			</div>
			<div class="dialogContent">
				<div class="alertCon"><span>认领成功！</span></div>
				<div class="Btns">
					<a id="renlingSuccess_confirm">确定</a>
				</div>
			</div>
		</div>
	</div>
<%@ include file="./../common/newfooter.jsp" %>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script data-main="<%=resource_url%>res/js/dev/projectClaim.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>