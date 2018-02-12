<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>善园 - 我要行善支付</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entPay.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entAlipay.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/css.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/amountpay.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>

</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer pay">
	<div class="bodyer pay notPay">
	<div class="page">
    	<form name="pay" method="post" action="">
<!--     	<div class="page-hd">
           <div class="page-tit"><h2 class="w1000">支付中心</h2></div>
        </div> -->
        
       <div class="batchdonation">
        <div class="batchdonation_title">您本次捐助项目：</div>
        <ul class="batchdonation_detail">
            
            <c:forEach var="project" items="${projects}" varStatus="status">
            	<li class="amountlanmu">
	            	<div class="batchdonation_pic"><img src="${project.coverImageUrl}"></div>
	            	<p class="title">${project.title}</p><p>已募集：<i><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/></i>元</p>
	                <span class="amountguanbi" v="${project.id}" >取消捐款</span>
            	</li>
            </c:forEach>
        </ul>
     </div>
  		
  		<div class="sjjys_2">
   			 <div class="p_1">请选择每个项目的捐款金额：</div>
    			<div class="select">
	      			<ul id="ulMoneyList">
	            	    <li class="" style="margin-left:0;" v="20"><span class="moneyRadio"></span><label for="rd20"><strong>￥</strong><b>20</b></label></li>
	        			<li class="" v="50"><span class="moneyRadio"></span><label for="rd50"><strong>￥</strong><b>50</b></label></li>
	                    <li class="" v="100"><span class="moneyRadio"></span><label for="rd100"><strong>￥</strong><b>100</b></label></li>
	                    <li class="" v="500"><span class="moneyRadio"></span> <label for="rd500"><strong>￥</strong><b>500</b></label></li>
	                    <li class="custom"  v="0"><span class="moneyRadio"></span>  <label for="rdOther">其它金额</label><input id="txtOtherMoney" type="text" class="enter" maxlength="10" onpaste="return false;" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)"></li>
	                </ul>
            	</div>
            <div class="thesum">
            <ul>
                <li>每个项目捐款：<span class="amount">20</span>元</li>
                <li>共计：<span class="amount" id ="totalamount">200</span>元</li>
            </ul>
            </div>
		</div>

        <div class="page-bd page-entBd"> 
            	<div class="pay-hdm">
                <h2>支付确认</h2>
                <ul class="pay-tit">
                    <li>认捐主题：${pName}</li>
                    <li><label for="isUsable"><input type="checkbox" id="isUsable" checked> 可用余额：<fmt:formatNumber value="${balance}" pattern="0.00"/>元</label><span class="more">剩余支付：<strong><span id="stillMoney">0.00</span>元</strong></span></li>
                </ul>
            	</div>
    	</div>
        <div class="pageCon yh" id="directPay">
        	<div class="payMoney-btn" >
                <a class="charge-btn charge-btn-green" id="directBtn">立即支付</a>
                <a class="charge-btn charge-btn-green" id="otherBtn">其它支付</a>
            </div>
            <div class="xieyi">
            <input type="checkbox" class="xie_in1" id="kuaijie" checked="true">
            <span class="xie_sp">同意并接受<a href="http://www.17xs.org/help/userService/">《善园基金会用户协议》</a></span>
            </div>
        </div>
        <div class="pageCon yh mt15" id="chargePay" style="display:none">
        	<div class="charge-menu">
                    <ul>
						<li  class="active">
                            <a>支付宝</a>
                            <em>0手续费</em>
                        </li>
                        <li>
                            <a>银行汇款</a>
                        </li>
                    </ul>
                </div>
       	    <div class="charge-wrap">
                    
                    <div class="charge-alipay chargeList">
                       <div class="pay-options">
                            <a class="pay-alipay paySelect" href="javascript:void(0);">
                            	<img src="<%=resource_url%>res/images/Enterprise/pay/apliy.jpg">
                                <i></i>
                            </a> 
                            <a class="pay-weixin" href="javascript:void(0);">
                            	<img src="<%=resource_url%>res/images/Enterprise/pay/weixin.jpg">
                                <i></i>
                            </a>                          
                        </div>
                        <div class="payMoney-btn payMoney-btnl">
                            <a class="charge-btn charge-btn-green" id="submitPayList">立即支付</a>
                        </div>
                        <div class="xieyi1">
                        <input type="checkbox" class="xie_in1" id="yinghang" checked="true">
                        <span class="xie_sp">同意并接受<a href="http://www.17xs.org/help/userService/">《善园基金会用户协议》</a></span>
                        </div>
                    </div>
                    <div class="charge-transfer chargeList" style="display:none">
                        <div class="transfer-top">
                            <h6>大额捐赠，可通过网上银行或银行柜台向善园基金会转账</h6>
                            <p>转账时，请务必把您的手机号填写在转账备注或用途里，以便工作人员查实。</p>
                        </div>
                        <div class="transfer-list">
                        <dl>
                            <dt>
                                <img src="<%=resource_url%>res/images/charge/bank-logo.jpg" />
                            </dt>
                            <dd>
                                <p>
                                    <span>开户名：宁波市善园公益基金会</span>
                                    <span>开户账号：8101 1101 3022 89012</span>
                                    <span>开户行：宁波鄞州农村合作银行（鄞州银行）营业部</span>
                                </p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>
                                <img src="<%=resource_url%>res/images/charge/gsyh.jpg" />
                            </dt>
                            <dd>
                                <p>
                                    <span>开户名：宁波市善园公益基金会</span>
                                    <span>开户账号：39011 50019 00036 8944</span>
                                    <span>开户行：工商银行鄞州支行营业部</span>
                                </p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>
                                <img src="<%=resource_url%>res/images/charge/zgyh.jpg" />
                            </dt>
                            <dd>
                                <p>
                                    <span>开户名：宁波市善园公益基金会</span>
                                    <span>开户账号：3740 6997 6779</span>
                                    <span>开户行：中国银行宁波鄞州城南支行</span>
                                </p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>
                                <img src="<%=resource_url%>res/images/charge/jsyh.jpg" />
                            </dt>
                            <dd>
                                <p>
                                    <span>开户名：宁波市善园公益基金会</span>
                                    <span>开户账号：3315 0199 5036 0000 0076</span>
                                    <span>开户行：中国建设银行股份有限公司宁波鄞州支行</span>
                                </p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>
                                <img src="<%=resource_url%>res/images/charge/zsyh.jpg" />
                            </dt>
                            <dd>
                                <p>
                                    <span>开户名：宁波市善园公益基金会</span>
                                    <span>开户账号：5749 0581 3210 501</span>
                                    <span>开户行：招商银行宁波分行鄞州支行</span>
                                </p>
                            </dd>
                        </dl>
                           <%--  <dl>
                                <dt>
                                    <img src="<%=resource_url%>res/images/charge/alipay-logo.jpg">
                                </dt>
                                <dd>
                                    <p>
                                        <span>开户名：宁波市善园公益基金会</span>
                                        <span>开户账号：zfb@17xs.org</span>
                                    </p>
                                </dd>
                            </dl> --%>
                        </div>
                        <div class="warm-tips">
                            <h6><font>*</font>温馨提示</h6>
                            <p>转账完成后，请务必联系在线客服（9:00-17:00 节假日除外）</p>
                            <p>告知您的帐号名，及汇款凭证截图，以便我们及时充值到您的账号内。</p>
                        </div>
                    </div>
            </div>
        </div>
        </form>
    </div>
</div>
</div>
<input type="hidden" value="${balance}" id="usableMoney"/>
<input type="hidden" value="10000000" id="NeedMoney"/>
<input type="hidden" value="${projectId}" id="projectId"/>
<input type="hidden" value="${userType}" id="userType"/>
<input type="hidden" value="${pList}" id="pList">
<input type="hidden" value="${extensionPeople }" id="extensionPeople"/>
<%@ include file="./../common/newfooter.jsp" %>

<script data-main="<%=resource_url%>res/js/dev/batchPay.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>

</body>
</html>
