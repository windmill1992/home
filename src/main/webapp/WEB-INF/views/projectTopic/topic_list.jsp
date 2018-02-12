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
<title>我的专题</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/projectTopic/mySeminar.css" />
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	function deleteTopic(id){
		$('#topicId').val(id);
		$('.alertDelete').show();
		//确定
		$('#btn_sub').click(function(){
			var id=$('#topicId').val();
			$.ajax({
			url:"http://www.17xs.org/projectTopic/delectTopic.do",
			data:{id:id},
			success: function(result){
				if(result.flag == 1){//成功
					window.location='http://www.17xs.org/projectTopic/gotoMyTopic.do';
				}else if(result.flag == 0){//失败
				}
				else{//未登录
					window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/projectTopic/gotoMyTopic.do';
				}
			}
		});
		});
		//取消
		$('#btn_cancel').click(function(){
			$('.alertDelete').hide();
		});
	};
	function alertQrcode(id){
		var qrcode = new QRCode(document.getElementById("qrcode"), {
		width : 200,//设置宽高
		height : 200
		});
		var url = 'http://www.17xs.org/projectTopic/gotoProjectTopicDetail.do?id='
				+ id;
		qrcode.makeCode(url);
		$('.popUp').show();
		$('.back').click(function(){
			$('.popUp').hide();
		});
	};
</script>
<style type="text/css">
	
</style>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter">
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
                <div class="mySeminar">
                    <p class="mySe_fl">我的专题</p>
                    <a href="<%=resource_url%>projectTopic/gotoProjectTopicAdd.do" class="mySe_fr">创建专题页</a>
                    <div style="clear: both"></div>
                </div>
                <!--记录-->
                <div class="goodRecord">
                    <dl>
                        <!-- <dd>
                            <li class="zhuanti">张天明公益专题公益专题公益专题</li>
                            <li>181708个</li>
                            <li >34567890元</li>
                            <li><p class="dm">2016/09/18</p><p class="dm">12：25</p></li>
                            <li>
                                <a href="#"><p class="dm">查看二维码</p></a>
                                <a href="#"><p class="dm">删除</p></a>
                            </li>
                        </dd>-->
                    </dl>
                    <!--没有专题项目时显示-->
                    <p class="seminarNone" style="display: none">您还没有创建专题，<a href="<%=resource_url%>projectTopic/gotoProjectTopicAdd.do" class="semClick">点此创建></a></p>
                </div>
            </div>
        </div>
    </div>
</div>
<!--二维码弹框-->
<div class="popUp" style="display: none">
    <div class="back"></div>
    <div id="qrcode" class="pr_ewm" style=""></div>
    <input type="text" id="getval" style="display:none" />
</div>
<!-- 删除弹框 -->
<div class="alertDelete" style="display: none">
    <div class="back"></div>
    <div class="cp2yDialogBox1" data="0" id="cp2yDialogBox0">
        <div class="dialogTitle">
            <i class="l"></i><span>提示信息</span>
            <a class="closeDialog" data="0"></a>
            <i class="r"></i>
        </div>
        <div class="dialogContent">
            <div class="alertCon">
               你确定要删除这个专题吗？
            </div>
            <div class="Btns">
        <a id="btn_sub" class="closeDialog sureBtn1" href="javascript:void(0);">确定</a>
        <a id="btn_cancel" class="closeDialog sureBtn1" href="javascript:void(0);" style="background: #ccc;border: 1px solid #e0e0e0;border-radius: 4px;height: 38px;line-height: 38px;box-shadow: 1px -1px 0px 0px #ccc;width: 92px;">取消</a>
            </div>
        </div>
    </div>
    </div>

<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="topicId"/>
<script type="text/javascript" src="<%=resource_url%>res/js/qrcode.js"></script>
<script data-main="<%=resource_url%>res/js/dev/projectTopic/projectTopic.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
