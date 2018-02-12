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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home.css?v=201603292">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/mon_donation.css">
<title>善园</title>
    <script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
    <script>
        $(function(){
            $("#Donate").click(function(){
                $("#Donate").addClass("gongy").siblings().removeClass("gongy");
        });
    $("#noDonate").click(function(){
    $("#noDonate").addClass("gongy").siblings().removeClass("gongy");
    });
    });
    </script>
</head>

<body>
<input type="hidden" id="topicId" value="${topicId }" />
<div class="box">
  <div class="prsee_top">
    <div class="top_left"><span class="span_icn"></span>
      <p class="prsee_p">募捐项目</p>
    </div>


    <div class="top_right">
    <a href="#" class="prsee_next" id="changeP">换一批</a><span class="span_icn1"><!--<img src="" alt="" />--></span>
    <a href="#" class="prsee_next gongy" id="noDonate">未捐<span><lable id="noDonateNum"></lable></span></a>
    <a href="#" class="prsee_next" id="Donate">已捐<span><lable type="text" id="donateNum"></lable></span></a>
    </div>
  </div>
  <div class="prsee_center">
    <ul id="project_list">
      
    </ul>
  </div>
  <div class="text">
  <div class="prsee_footer" style="position: fixed;bottom: 0;">
    <div class="button1"> <form action=""><input type="checkbox" />全选</form></div>
    <dl>
      <dd><form action=""><input type="checkbox" checked="checked" value=""/>已阅读并同意</form></dd>
      <dd><a href="<%=resource_url%>redPackets/getAgreement.do">《善园基金会捐赠协议》</a></dd>
    </dl>
   <a href="javascript:void(0);" id="doSubmit" class="footer_j buttonDisabled"><p>我要捐助</p></a>
  </div>
  </div>
</div>
<div class="pay_box">
 <div class="pay"></div>
  <div class="pay_je">

  </div>
    <div class="chare"></div>
    <div class="pay_xq">
        <div class="pay_top">
            <div class="pay_icon"></div>
            <span class="pay_te">捐赠人</span>
            <a href="javascript:void(0);" class="pay_i" ><img src="<%=resource_url%>res/images/h5/images/cha.png" alt="" class="pay_img"></a>
        </div>
        <div class="chare"></div>
        <div class="pay_ren">
            <div class="pay_fl">
                <span>昵称</span>
                <input type="text" paaceholder="zxcfdf1223" id="nickName" value="${user.nickName}">
            </div>
            <div class="pay_fr">
                <span>电话</span>
                <input type="text" paaceholder="15385609095" id = "mobile" value="${user.mobileNum}">
            </div>
        </div>
        <div class="chare"></div>
        <div class="pay_top">
            <div class="pay_icon"></div>
            <span class="pay_te">捐赠信息</span>
        </div>
        <div class="chare"></div>
        <div class="pay_jin">
            <p>单个项目捐赠:<span><input type="text" data-placeholder="请输入捐助金额" id = "singleMoney"/></span>元</p>
        </div>
        <div class="chare"></div>
        <div class="pay_top">
           <div class="top_fl"><p>认捐项目：<span id="pcount"></span>个</p></div>
            <div class="top_fr"><p>合计：<span id="pmoney"></span>元</p></div>
        </div>
        <div class="chare"></div>
        <div class="pay_foot" >
            <a href="javascript:void(0);" class="foot_ju" id="donateSubmit"><P>我要捐助</P></a>
        </div>
        <div class="chare"></div>
        <div class="foot_xie">
            <input type="checkbox" checked="checked" id="xieyi"/>
            <span>已阅读并同意</span>
            <a href="<%=resource_url%>redPackets/getAgreement.do">《善园基金会捐赠协议》</a>
        </div>
        <!--错误提示-->
        <div class="chare"></div>
        <div class="pay_cuo1" id="checkInfo"></div>
    </div>


</div>

    <%--捐助登录提示--%>
    <div class="prompt_box" style="display:none" id="loginCue">
    <div class="cue_back"></div>
    <div class="cue1">
    <div class="cue_center">
    <div class="cue_center1">
    <p class="cue_p1">捐款确认</p>
    <p class="cue_p3">您目前还没有登录善园网，是否继续匿名捐助</p>
    </div>
    <div class="cue_center2">
    <a href="javascript:void(0)"><div class="cue_fl" id="cue_fl"><p class="cue_pl">登录</p></div></a>
    <a href="javascript:void(0)"><div class="cue_fr" id="cue_fr"><p class="cue_pr">匿名捐助</p></div></a>
    </div>
    </div>
    </div>
    </div>
<input type="hidden" id="extensionPeople" value="${extensionPeople }" >
<div class="pay_cuo1" id="checkPro" style="display: none"><p>请至少选择两个捐赠项目</p></div>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script src="<%=resource_url%>res/js/h5/batch_list.js?v=201603305"></script>
<input type="hidden" id = "currentPage" >
<input type="hidden" id = "totalPage" >
<input type="hidden" id = "plistId" >
<input type="hidden" id="userId" value="${user.id }" />
<input type="hidden" id="focusState" value="0" />
<input type="hidden" id="key" value="0"/>
</body>
</html>
