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
<title>善园 - 我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/applyStep.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/helpinfo.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/util/jquery-ui.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div id="bodyer" class="bodyer applySteps">
	<div class="page">
    	<div class="page-hd">
        	<div class="page-tit">&nbsp;我要求助</div>
        </div>
        <div class="page-bd"> 
            <div class="applyStp-main">
            	<div class="applyStp-hd">
                	<div class="applyStp-tit">我要求助申请流程</div>
                    <ul class="applyStp-tab">
                    	<li><i>1</i><span>实名认证</span></li>
                        <li class="curTab"><i>2</i><span>完善资料</span></li>
                        <li><i>3</i><span>等待审核</span></li>
                    </ul>
                    
                </div>
                <c:choose>   
				  <c:when test="${userState==203}">   
				         <div class="helpprim">实名认证信息已审核通过，请填写您的求助内容</div> 
				  </c:when> 
				  <c:otherwise>   
				       <div class="helpprim">实名认证信息已提交审核，请填写您的求助内容</div>   
				  </c:otherwise>   
				</c:choose> 
                <div class="applyStp-bd helpStp-bd">
                	<div class="helpEdit helpInfo">
                    <dl class="info-box info-tit fdef">
                        <dt><label for="tit"><i>*</i>标题：</label></dt>
                        <dd><input id="title" type="text" placeholder="标题不能超过18个字" value=""></dd>
                    </dl>
                    <dl class="info-box info-con fdef">
                        <dt><label for="con"><i>*</i>内容：</label></dt>
                        <dd><textarea id="content"></textarea></dd>
                    </dl>
                    <dl class="info-box info-imgs fdef">
                        <dt><label>图片：</label></dt>
                        <dd>
                       <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">							
                        <ul class="imgList">
                            <li class="last">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input"><input type="hidden" name="type" id="type" value="4"></li>
                        </ul>
                        </form>
                        <p class="prim clear">
                            注：图片大小每张请控制在2M以内，最多可上传10张图片
                        </p>
                        </dd>
                    </dl>
                    <dl class="info-box info-area">
                        <dt><label><i>*</i>所在地区：</label></dt>
                        <dd>
                        	<select name="province_id" id="province"></select>
                            <select class="city" id="city"></select>
                            <select class="area" id="county"></select> 
                        </dd>
                    </dl>
                    <dl class="info-box info-street">
                        <dt><label for="street"><i>*</i>详细地址：</label></dt>
                         <dd><textarea id="detailAddress" placeholder="建议您如实填写详细地址，例如街道名称，门牌号码，楼层和房间号等信息" class="address"></textarea></dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="helpMoney"><i>*</i>求助金额：</label></dt>
                         <dd><input id="cryMoney" type="text" class="itemtext textmoney">元</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label><i>*</i>您的身份：</label></dt>
                        <dd>
                        <select class="itemselect" id="identity">
                            <option tel="callerI">求助者本人</option>
                            <option tel="callerFather">求助人父亲</option>
                            <option tel="callerMother">求助人母亲</option>
                            <option tel="volunteers">爱心志愿者</option>
                            <option tel="otherCaller">其它</option>
                        </select> 
						<input type="text" id="identityInfo" class="itemtext textmoney">
						</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label><i>*</i>项目截止时间：</label></dt>
                        <dd>
                           <input id="deadline" value=""  readonly="readonly" type="text" class="itemtext textmoney">（请根据您求助的紧急程度填写，上限为180天，不填写默认为最大值）
                    </dl>
                    <dl class="info-box info-field">
                    	<input type="hidden" id="field" value="disease">
                        <dt><label><i>*</i>求助领域：</label></dt>
                        <dd><span class="sel" tel="disease">疾病</span><span tel="education">助学</span></dd>
                    </dl>
                    <p class="promot-bank">请填写您所在机构的银行账户，以便我们拨款</p>
                    <dl class="info-box nopadT">
                        <dt><label class="bankUser">开户单位：</label></dt>
                        <dd> <input id="accountName" type="text">例：宁波善园基金会</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankName">银行名：</label></dt>
                        <dd><input id="accountBank" type="text">例：招商银行总行营业部</dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankAccount">银行账号：</label></dt>
                        <dd><input id="collectNum" type="text">例：755917671010888</dd>
                    </dl>
                    <div class="btn-box">
                    	<div class="info-warn" id="warn"> </div>
                        <div class="btn btn-gray" id="hiDraft">
                            保存草稿 
                        </div>
                         <p class="btn-point"><i></i>如果您对一些内容还需要斟酌，可先保存为草稿（草稿文件可再次编辑后提交）</p>
                        <div class="btn btn-green" id="hiSubmit">提交资料</div>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>

</div>
<%@ include file="./../common/footer.jsp" %>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js?v=20150511"></script>
<script src="<%=resource_url%>res/js/util/datepicker/jquery-ui.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-ar.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-fr.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-he.js"></script>
<script src="<%=resource_url%>res/js/util/datepicker/datepicker-zh-TW.js"></script>
<script>
$(function(){
$( "#deadline" ).datepicker( "option", "dateFormat", 'yy-mm-dd');
$( "#deadline" ).datepicker( "option",$.datepicker.regional[ 'zh-TW'] );
$( "#deadline" ).datepicker({ minDate: "7D", maxDate: "+6M +1D" });
})
</script>
<script data-main="<%=resource_url%>res/js/dev/helpinfo.js" src="<%=resource_url%>res/js/require.min.js"></script>

</body>
</html>
