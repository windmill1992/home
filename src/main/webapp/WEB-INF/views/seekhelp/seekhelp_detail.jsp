<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@page import="com.guangde.home.utils.UserUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<!--<link rel="stylesheet" type="text/css" href="res/css/dev/helpList.css"/>-->
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer userCenter helpEdit">
	<div class="page"> 
        <div class="page-hd"><h2 class="w1000">&nbsp;王小二，您好！欢迎回来！这里是您的个人中心:)</h2></div>
        <div class="page-bd">
        	<div class="uCEN-L">
                <div class="uCEN-user">
                    <div class="uCEN-user-show"><i class="user-icon-gt">
                      <% 
		                   String url = UserUtil.getUserHead(request, response);
		                   if(url!=null){
		                     out.print("<img src='"+url+"' width='120px' height='120px'>");
		                   }
		                %>
                    </i></div>
                    <div class="uCEN-user-info">
                        Hi王小二，
                        <br/>在过去的198天里
                        <br/>您一共参与了<i class="em-color">4个项目</i>
                        <br/>您总共捐赠了<i class="em-color">1800元</i>
                        <br/>高于<i class="em-color">90%</i>的善园基金会友
                        <br/><i class="biger em-color">爱心爆棚！</i> 
                    </div>
                </div>
                <ul id="uCENNav" class="uCEN-nav"> 
                    <li><a href="<%=resource_url%>ucenter/core/mygood.do">捐款明细</a></li>
                    <li><a href="<%=resource_url%>ucenter/core/msg.do">消息与通知</a></li>
                    <li><a href="<%=resource_url%>ucenter/core/mygood.do">善管家</a></li>
                    <li><a href="<%=resource_url%>user/realname.do">实名认证</a></li>
                    <li class="uCENCur"><a href="<%=resource_url%>ucenter/pindex.do">我的求助</a></li>
                </ul> 
            </div>
            <div class="uCEN-R"> 
                <div class="uCEN-hd">
                    <span class="uCEN-tit">求助申请</span>  
                    <a class="back" href="javascript:window.history.back()" title="">返回</a> 
                </div>
				<div class="uCEN-bd">
                    <dl class="info-box info-tit fdef">
                        <dt><label for="tit"><i>*</i>标题：</label></dt>
                        <dd><input id="tit" type="text" value="" /></dd>
                    </dl>
                    <dl class="info-box info-con fdef">
                        <dt><label for="con"><i>*</i>内容：</label></dt>
                        <dd><textarea id="con"></textarea></dd>
                    </dl>
                    <dl class="info-box info-imgs fdef">
                        <dt><label>图片：</label></dt>
                        <dd>
                        <ul class="imgList">
                            <li><i>×</i></li>
                            <li><i>×</i></li>
                            <li class="last">+</li>
                        </ul>
                        <p class="prim clear">
                            注：图片大小每张请控制在500KB以内，最多可上传10张图片
                        </p>
                        </dd>
                    </dl>
                    <dl class="info-box info-area">
                        <dt><label><i>*</i>所在地区：</label></dt>
                        <dd>
                    		<select id="province">
                           		<option>黑龙江省</option>
                                <option>吉林省</option>
                                <option>辽宁省</option>
                                <option>河北省</option>
                                <option>山东省</option>
                                <option>河南省</option>
                                <option>安徽省</option>
                                <option>江苏省</option>
                                <option selected="selected">浙江省</option>
                                <option>福建省</option>
                                <option>广东省</option>
                                <option>广西省</option>
                                <option>云南省</option>
                                <option>内蒙古自治区</option>
                                <option>广西壮族自治区</option>
                      		</select> 
                            <select class="city"> 
                                <option>杭州市</option>
                                <option selected="selected">宁波市</option>
                                <option>嘉兴市</option>
                                <option>绍兴市</option>
                                <option>温州市</option>
                                <option>湖州市</option>
                                <option>金华市</option>
                                <option>衢州市</option>
                                <option>舟山市</option>
                                <option>台州市</option>
                                <option>丽水市</option> 
                      		</select>
                            <select class="area">
                           		<option selected="selected">海曙区</option>
                                <option>江东区</option>
                                <option>江北区</option>
                                <option>北仑区</option>
                                <option>镇海区</option>
                                <option>鄞州区</option>
                                <option>余姚市</option>
                                <option>慈溪市</option>
                                <option>奉化市</option>
                                <option>象山县</option>
                                <option>宁海县</option> 
                      		</select>
                        </dd>
                    </dl>
                    <dl class="info-box info-street">
                        <dt><label for="street"><i>*</i>详情地址：</label></dt>
                        <textarea id="street" placeholder="建议您如实填写详细收货地址，例如街道名称，门牌号码，楼层和房间号等信息" class="address"></textarea>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="helpMoney"><i>*</i>求助金额：</label></dt>
                        <input id="helpMoney" type="text" />
                    </dl>
                    <dl class="info-box">
                        <dt><label><i>*</i>您的身份：</label></dt>
                        <dd>
                        <select class="itemselect">
                            <option>求助人本人</option>
                            <option>求助人本人</option>
                            <option>求助人本人</option>
                            <option>求助人本人</option>
                        </select> 
                    </dd></dl>
                    <dl class="info-box">
                        <dt><label for="helpEndDate">项目截至时间：</label></dt>
                        <dd>
                        	<input id="helpEndDate" type="text"  />
                            <span class="info-weak">（请根据您求助的紧急程度填写，上限为180天，不填写默认为最大值）</span>
                    	</dd>
                    </dl>
                    <dl class="info-box info-field">
                        <dt><label><i>*</i>求助领域：</label></dt>
                        <dd><span class="sel">疾病</span><span>教育</span></dd>
                    </dl>
                    <p class="promot-bank">请填写您所在机构的银行账户，以便我们拨款</p>
                    <dl class="info-box nopadT">
                        <dt><label class="bankUser">开户单位：</label></dt>
                        <dd> <input id="bankUser" type="text" /></dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankName">银行名：</label></dt>
                        <dd><input id="bankName" type="text" /></dd>
                    </dl>
                    <dl class="info-box">
                        <dt><label for="bankAccount">银行账号：</label></dt>
                        <dd><input id="bankAccount" type="text" /></dd>
                    </dl>
                    <div class="btn-box">
                        <div class="btn btn-gray">
                            保存草稿 
                        </div>
                         <p class="btn-point"><i></i>如果您对一些内容还需要斟酌，可先保存为草稿（草稿文件可再次编辑后提交）</p>
                        <div class="btn btn-green">提交</div>
                    </div>
                </div> 
            </div>
        </div>
    </div>
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/helpEdit.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
