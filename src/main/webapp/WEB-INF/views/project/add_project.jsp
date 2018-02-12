<%@page import="com.guangde.home.utils.UserUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>广德王</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/dev/userCenter.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer userCenter mygood">
	<div class="mygood">
    	<div class="uCEN-hd"><h2 class="w1000">&nbsp;王小二，您好！欢迎回来！这里是您的求助人页面 : )  </h2></div>
        <div class="uCEN-bd">
        	<div class="uCEN-bdL">
            	<div class="uCEN-user wuCEN-user">
                	<div class="uCEN-user-show"><i class="user-icon-gt">
                	   <% 
		                   String url = UserUtil.getUserHead(request, response);
		                   if(url!=null){
		                     out.print("<img src='"+url+"' width='120px' height='120px'>");
		                   }
		                %>
                	</i></div>
                </div>
                <ul id="uCENNav" class="uCEN-nav"> 
                	<li class="uCENCur">我的求助申请</li>
                    <li>消息与通知</li>
                </ul> 
            </div>
            <div class="uCEN-bdR wuCEN-bdR">
            	<div class="uCEN-list list">
                	<div class="list-hd">
                    	<span class="list-tit">发布新的求助申请</span>
                    	<dl class="select">
                        	<dt class="sel-checked sel-btn">
                            	<a href="#" title="">返回</a>
                            </dt>
                        </dl>
                    	
                    </div>
                    <div class="list-bd">
                        <div class="wadd-list">
                        	<div class="add-item add-itemtop mb20">
                            	<label>求助标题：</label>
                                <div class="item">
                                	<input type="text" class="itemtext">
                                </div>
                            </div>
                            <div class="add-item add-itemtop mb20">
                            	<label>求助内容：</label>
                                <div class="item">
                                	<textarea style="width:620px; height:315px; border:1px solid #ccc" ></textarea>
                                </div>
                            </div>
                            <div class="add-item add-itemtop">
                            	<label>图片：</label>
                                <div class="item">
                                	<a title="" class="addpic"></a>
                                    <span class="prim">注：图片大小每张请控制在500KB以内，最多可上传10张图片</span>
                                </div>
                            </div>
                            <div class="add-item add-itemcity">
                            	<label><i>*</i>所在地区：</label>
                                <div class="item">
                                	<div class="itemcity">
                                    	<div class="citytitle">广东 <span style="color:#cfcfcf">/</span> 湛江 <span style="color:#cfcfcf">/</span> 雷州 <span style="color:#cfcfcf">/</span> <span style="color: red">稍后再说</span></div>
    									<div class="icondown"></div>
                                        <div class="citycon">
                                        	<div class="citytab">
                                            	<a class="current" attr-cont="city-province">省份</a>
                                                <a class="" attr-cont="city-city">城市</a>
                                                <a class="" attr-cont="city-district">县区</a>
                                                <a class="last" attr-cont="city-street">街道</a>
                                            </div>
                                            <div class="citycontent">
                                            	<div class="city-select city-province">
                                                	<dl class="fn-clear">
                                                    <dt>A-G</dt>
                                                    <dd>
                                                        <a title="安徽" attr-id="340000" href="javascript:;">安徽</a>
                                                        <a title="北京" attr-id="110000" href="javascript:;">北京</a>
                                                        <a title="重庆" attr-id="500000" href="javascript:;">重庆</a>
                                                        <a title="福建" attr-id="350000" href="javascript:;">福建</a>
                                                        <a title="甘肃" attr-id="620000" href="javascript:;">甘肃</a>
                                                        <a title="广东" attr-id="440000" href="javascript:;">广东</a>
                                                        <a title="广西" attr-id="450000" href="javascript:;">广西</a>
                                                        <a title="贵州" attr-id="520000" href="javascript:;">贵州</a>
                                                    </dd>
                                                  </dl>
                                                  <dl class="fn-clear">
                                                    <dt>A-G</dt>
                                                    <dd>
                                                        <a title="安徽" attr-id="340000" href="javascript:;">安徽</a>
                                                        <a title="北京" attr-id="110000" href="javascript:;">北京</a>
                                                        <a title="重庆" attr-id="500000" href="javascript:;">重庆</a>
                                                        <a title="福建" attr-id="350000" href="javascript:;">福建</a>
                                                        <a title="甘肃" attr-id="620000" href="javascript:;">甘肃</a>
                                                        <a title="广东" attr-id="440000" href="javascript:;">广东</a>
                                                        <a title="广西" attr-id="450000" href="javascript:;">广西</a>
                                                        <a title="贵州" attr-id="520000" href="javascript:;">贵州</a>
                                                    </dd>
                                                  </dl>
                                                  <dl class="fn-clear">
                                                    <dt>A-G</dt>
                                                    <dd>
                                                        <a title="安徽" attr-id="340000" href="javascript:;">安徽</a>
                                                        <a title="北京" attr-id="110000" href="javascript:;">北京</a>
                                                        <a title="重庆" attr-id="500000" href="javascript:;">重庆</a>
                                                        <a title="福建" attr-id="350000" href="javascript:;">福建</a>
                                                        <a title="甘肃" attr-id="620000" href="javascript:;">甘肃</a>
                                                        <a title="广东" attr-id="440000" href="javascript:;">广东</a>
                                                        <a title="广西" attr-id="450000" href="javascript:;">广西</a>
                                                        <a title="贵州" attr-id="520000" href="javascript:;">贵州</a>
                                                    </dd>
                                                  </dl>
                                                  <dl class="fn-clear">
                                                    <dt>A-G</dt>
                                                    <dd>
                                                        <a title="安徽" attr-id="340000" href="javascript:;">安徽</a>
                                                        <a title="北京" attr-id="110000" href="javascript:;">北京</a>
                                                        <a title="重庆" attr-id="500000" href="javascript:;">重庆</a>
                                                        <a title="福建" attr-id="350000" href="javascript:;">福建</a>
                                                        <a title="甘肃" attr-id="620000" href="javascript:;">甘肃</a>
                                                        <a title="广东" attr-id="440000" href="javascript:;">广东</a>
                                                        <a title="广西" attr-id="450000" href="javascript:;">广西</a>
                                                        <a title="贵州" attr-id="520000" href="javascript:;">贵州</a>
                                                    </dd>
                                                  </dl>
                                                </div>
                                            	<div class="city-select city-city" style="display:none">
                                                	<dl class="fn-clear city-select-city">
                                                    <dd>
                                                        <a title="南宁" attr-id="450100" href="javascript:;">南宁</a>
                                                        <a title="柳州" attr-id="450200" href="javascript:;" data-spm-anchor-id="0.0.0.0" class="current">柳州</a><a title="桂林" attr-id="450300" href="javascript:;">桂林</a>
                                                        <a title="梧州" attr-id="450400" href="javascript:;">梧州</a>
                                                        <a title="北海" attr-id="450500" href="javascript:;">北海</a>
                                                        <a title="防城港" attr-id="450600" href="javascript:;">防城港</a>
                                                        <a title="钦州" attr-id="450700" href="javascript:;">钦州</a>
                                                        <a title="贵港" attr-id="450800" href="javascript:;">贵港</a>
                                                        <a title="玉林" attr-id="450900" href="javascript:;">玉林</a>
                                                        <a title="百色" attr-id="451000" href="javascript:;">百色</a>
                                                        <a title="贺州" attr-id="451100" href="javascript:;">贺州</a>
                                                        <a title="河池" attr-id="451200" href="javascript:;">河池</a>
                                                        <a title="来宾" attr-id="451300" href="javascript:;">来宾</a>
                                                        <a title="崇左" attr-id="451400" href="javascript:;">崇左</a>
                                                    </dd>
                                                   </dl>
                                                </div>
                                                <div class="city-select city-city" style="display:none">
                                                	<dl class="fn-clear city-select-city">
                                                    <dd>
                                                        <a title="南宁" attr-id="450100" href="javascript:;">南宁</a>
                                                        <a title="柳州" attr-id="450200" href="javascript:;" data-spm-anchor-id="0.0.0.0" class="current">柳州</a><a title="桂林" attr-id="450300" href="javascript:;">桂林</a>
                                                        <a title="梧州" attr-id="450400" href="javascript:;">梧州</a>
                                                        <a title="北海" attr-id="450500" href="javascript:;">北海</a>
                                                        <a title="防城港" attr-id="450600" href="javascript:;">防城港</a>
                                                        <a title="钦州" attr-id="450700" href="javascript:;">钦州</a>
                                                        <a title="贵港" attr-id="450800" href="javascript:;">贵港</a>
                                                        <a title="玉林" attr-id="450900" href="javascript:;">玉林</a>
                                                        <a title="百色" attr-id="451000" href="javascript:;">百色</a>
                                                        <a title="贺州" attr-id="451100" href="javascript:;">贺州</a>
                                                        <a title="河池" attr-id="451200" href="javascript:;">河池</a>
                                                        <a title="来宾" attr-id="451300" href="javascript:;">来宾</a>
                                                        <a title="崇左" attr-id="451400" href="javascript:;">崇左</a>
                                                    </dd>
                                                   </dl>
                                                </div>
                                                <div class="city-select city-city" style="display:none">
                                                	<dl class="fn-clear city-select-city">
                                                    <dd>
                                                        <a title="南宁" attr-id="450100" href="javascript:;">南宁</a>
                                                        <a title="柳州" attr-id="450200" href="javascript:;" data-spm-anchor-id="0.0.0.0" class="current">柳州</a><a title="桂林" attr-id="450300" href="javascript:;">桂林</a>
                                                        <a title="梧州" attr-id="450400" href="javascript:;">梧州</a>
                                                        <a title="北海" attr-id="450500" href="javascript:;">北海</a>
                                                        <a title="防城港" attr-id="450600" href="javascript:;">防城港</a>
                                                        <a title="钦州" attr-id="450700" href="javascript:;">钦州</a>
                                                        <a title="贵港" attr-id="450800" href="javascript:;">贵港</a>
                                                        <a title="玉林" attr-id="450900" href="javascript:;">玉林</a>
                                                        <a title="百色" attr-id="451000" href="javascript:;">百色</a>
                                                        <a title="贺州" attr-id="451100" href="javascript:;">贺州</a>
                                                        <a title="河池" attr-id="451200" href="javascript:;">河池</a>
                                                        <a title="来宾" attr-id="451300" href="javascript:;">来宾</a>
                                                        <a title="崇左" attr-id="451400" href="javascript:;">崇左</a>
                                                    </dd>
                                                   </dl>
                                                </div>
                                            </div>
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="add-item">
                            	<label><i>*</i>详情地址：</label>
                                <div class="item">
                                	<textarea placeholder="建议您如实填写详细收货地址，例如街道名称，门牌号码，楼层和房间号等信息" class="address"></textarea>
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>求助金额：</label>
                                <div class="item">
                                	<input type="text" class="itemtext textmoney">
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>您的身份：</label>
                                <div class="item">
                                	<select class="itemselect">
                                    	<option>求助人本人</option>
                                        <option>求助人本人</option>
                                        <option>求助人本人</option>
                                        <option>求助人本人</option>
                                    </select>
                                	<input type="text" class="itemtext">
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>选择收款方式：：</label>
                                <div class="item">
                                	<label for="blank">
                                    	<input type="radio" id="blank">
                                        <span class="blankpic">
                                        <img src="/res/images/user/userblank.jpg" alt=""></span>
                                    </label>
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>开户人姓名：</label>
                                <div class="item">
                                	<input type="text" class="itemtext textmoney">
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>开户人姓名：</label>
                                <div class="item">
                                	<input type="text" class="itemtext">
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>开户银行名称：</label>
                                <div class="item">
                                	<select class="itemselect">
                                    	<option>工商银行（推荐）</option>
                                        <option>招商银行</option>
                                        <option>中国银行</option>
                                        <option>农业银行</option>
                                    </select>
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>开户银行所在省份：</label>
                                <div class="item">
                                	<select class="itemselect">
                                    	<option>北京市</option>
                                        <option>湖北省</option>
                                    </select>
                                    城市：
                                    <select class="itemselect">
                                    	<option>北京</option>
                                        <option>武汉</option>
                                    </select>
                                </div>
                            </div>
                            <div class="add-item">
                            	<label>支行：</label>
                                <div class="item">
                                	<select class="itemselect itbank">
                                    	<option>北京市支行银行</option>
                                    </select>
                                </div>
                            </div>
                             <div class="add-item">
                            	<label>银行卡号：</label>
                                <div class="item">
                                	<input type="text" class="itemtext">
                                </div>
                            </div>
                            <div class="add-item">
                            	<label></label>
                                <div class="item itemfoot">
                                	<input type="button" class="itembtn yh" value="保存草稿">
                                    <input type="button" class="itembtn yh" value="提交">
                                </div>
                            </div>
                        </div>  
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mygood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
