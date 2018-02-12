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
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/cashManage.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
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
            	<!----资金管理-///---> 
                <div class="setting_account withdraw">
						<c:if test="${state == 1}">
							<div class="prompt_tip">
								<span>您已设置收款帐号，请输入提现金额</span>
							</div>
							<div class="setting_form">
								<ul class="form_box">
									<li><span class="label">开户名：</span><span class="value">
											<input type="text" value="${card.accountName}" size="20"
											id="bankName">
									</span></li>
									<li><span class="label">开户行：</span><span class="value">
											<input type="text" value="${card.bankName}" size="20"
											id="bankType">
									</span></li>
									<li><span class="label">提现卡号：</span><span class="value">
											<input type="text" value="${card.card}" size="30" id="cardNo">
									</span></li>
									<li><span class="label">提现金额：</span><span class="value">
											<input type="text" size="30" id="money"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">元
									</span></li>
									<li><span class="label">上传图片：</span> 
										<span class="value uploadValue">请上传打款申请、收据以及发票的照片，
											<a href="/resposibilityReport/resposibilityReport.do?id=406" class="pic-model" target="_blank">点此查看模板</a>
										</span>
										<div class="pic" id="pic1">
											<form action="/file/upload3.do" method="post" id="form1" enctype="multipart/form-data">
												<div class="add_list" id="imgList">
				       								<div class="item add">
														<a class="add_btn2">+
															<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input">
															<input type="hidden" name="type" id="type" value="4">
														</a>
													</div>
												</div>
											</form>
										</div>
									</li>
									<li class="btn_line"><a href="javascript:;" class="add_btn" id="add_btn">提交申请</a></li>
								</ul>
							</div>
						</c:if>
						<c:if test="${state == 0}">
						<div class="record_box">
							<table width="758" border="0" cellspacing="0" cellpadding="0" id="listShow"></table>
							<p class="pageNum" id='pageNum'></p>
                    </div>  
                    </c:if>
                </div>
            </div>
        </div>
    </div> 
</div>
<input type="hidden" id="projectId" value="${projectId}">
<input type="hidden" id="flag" value="${flag}">
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="/res/js/dev/cash_manage.js" src="/res/js/require.min.js"></script>
</body>
</html>
