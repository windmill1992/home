<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head>
<body>
	<%@ include file="./../../common/newhead.jsp"%>
	<div class="bodyer">
	<div class="bodyer userCenter goodhelpList"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../../common/eleft_menu.jsp" %>
            </div>
            <div class="uCEN-R"> 
                <div class="charge-menu">
                    <ul>
                        <li class="active">
                            <a href="<%=resource_url%>ucenter/core/chargeonlineBank.do">网银支付</a>
                            <em>0手续费</em>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeAlipay.do">支付宝</a>
                            <em>0手续费</em>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeTransfer.do">银行汇款</a>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeRecord.do">充值记录</a>
                        </li>
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="onlineBank-form">
                        <dl>
                            <dt>支付金额：</dt>
                            <dd>
                                <input type="text" class="inp" placeholder="" id="amount" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)"/><span class="unit">元</span>
                            </dd>
                        </dl>
                        <dl>
                            <dt>选择银行：</dt>
                            <dd>
                                <div class="select-bank">
                                    <div class="select-bank-item">
                                        <div class="select-placeholder" data-id="">
                                            <span>- 请选择银行 -</span>
                                            <i class="icon-charge i_2"></i>
                                        </div>
                                        <span class="weak-remind">最高限额为100万元</span>
                                    </div>
                                    <div class="change-bank">
                                        <label for="bank-gs">
                                            <input type="radio" name="bank" id="bank-gs" value="ICBCB2C">
                                            <img src="<%=resource_url%>res/images/charge/bank-01.jpg"/>
                                        </label>
                                        <label for="bank-js">
                                            <input type="radio" name="bank" id="bank-js" value="CCB">
                                            <img src="<%=resource_url%>res/images/charge/bank-02.jpg"/>
                                        </label>
                                        <label for="bank-ny">
                                            <input type="radio" name="bank" id="bank-ny" value="ABC">
                                            <img src="<%=resource_url%>res/images/charge/bank-03.jpg"/>
                                        </label>
                                        <label for="bank-zs">
                                            <input type="radio" name="bank" id="bank-zs" value="CMB">
                                            <img src="<%=resource_url%>res/images/charge/bank-04.jpg"/>
                                        </label>
                                        <label for="bank-zg">
                                            <input type="radio" name="bank" id="bank-zg" value="BOCB2C">
                                            <img src="<%=resource_url%>res/images/charge/bank-05.jpg"/>
                                        </label>
                                        <label for="bank-yz">
                                            <input type="radio" name="bank" id="bank-yz" value="POSTGC">
                                            <img src="<%=resource_url%>res/images/charge/bank-06.jpg"/>
                                        </label>
                                        <label for="bank-jt">
                                            <input type="radio" name="bank" id="bank-jt" value="COMM-DEBIT">
                                            <img src="<%=resource_url%>res/images/charge/bank-07.jpg"/>
                                        </label>
                                        <label for="bank-gf">
                                            <input type="radio" name="bank" id="bank-gf" value="GDB">
                                            <img src="<%=resource_url%>res/images/charge/bank-08.jpg"/>
                                        </label>
                                        <label for="bank-gd">
                                            <input type="radio" name="bank" id="bank-gd" value="CEB-DEBIT">
                                            <img src="<%=resource_url%>res/images/charge/bank-09.jpg"/>
                                        </label>
                                        <label for="bank-xy">
                                            <input type="radio" name="bank" id="bank-xy" value="CIB" >
                                            <img src="<%=resource_url%>res/images/charge/bank-10.jpg"/>
                                        </label>
                                        <label for="bank-pa">
                                            <input type="radio" name="bank" id="bank-pa" value="SPABANK">
                                            <img src="<%=resource_url%>res/images/charge/bank-11.jpg"/>
                                        </label>
                                        <label for="bank-zx">
                                            <input type="radio" name="bank" id="bank-zx" value="CITIC-DEBIT">
                                            <img src="<%=resource_url%>res/images/charge/bank-12.jpg"/>
                                        </label>
                                        <label for="bank-pf">
                                            <input type="radio" name="bank" id="bank-pf" value="SPDB">
                                            <img src="<%=resource_url%>res/images/charge/bank-13.jpg"/>
                                        </label>
                                        <label for="bank-ms">
                                            <input type="radio" name="bank" id="bank-ms" value="CMBC">
                                            <img src="<%=resource_url%>res/images/charge/bank-14.jpg"/>
                                        </label>
                                        <label for="bank-bj">
                                            <input type="radio" name="bank" id="bank-bj" value="BJBANK">
                                            <img src="<%=resource_url%>res/images/charge/bank-15.jpg"/>
                                        </label>
                                        <label for="bank-sh">
                                            <input type="radio" name="bank" id="bank-sh" value="SHBANK">
                                            <img src="<%=resource_url%>res/images/charge/bank-16.jpg"/>
                                        </label>
                                        <label for="bank-hz">
                                            <input type="radio" name="bank" id="bank-hz" value="HZCBB2C">
                                            <img src="<%=resource_url%>res/images/charge/bank-17.jpg"/>
                                        </label>
                                        <label for="bank-nb">
                                            <input type="radio" name="bank" id="bank-nb" value="NBBANK">
                                            <img src="<%=resource_url%>res/images/charge/bank-18.jpg"/>
                                        </label>
                                        <label for="bank-fd">
                                            <input type="radio" name="bank" id="bank-fd" value="FDB">
                                            <img src="<%=resource_url%>res/images/charge/bank-19.jpg"/>
                                        </label>
                                        <label for="bank-bjny">
                                            <input type="radio" name="bank" id="bank-bjny" value="BJRCB">
                                            <img src="<%=resource_url%>res/images/charge/bank-20.jpg"/>
                                        </label>
                                        <label for="bank-gsqy">
                                            <input type="radio" name="bank" id="bank-gsqy" value="ICBCBTB">
                                            <img src="<%=resource_url%>res/images/charge/bank-21.jpg"/>
                                        </label>
                                        <label for="bank-jsqy">
                                            <input type="radio" name="bank" id="bank-jsqy" value="CCBBTB">
                                            <img src="<%=resource_url%>res/images/charge/bank-22.jpg"/>
                                        </label>
                                        <label for="bank-nyqy">
                                            <input type="radio" name="bank" id="bank-nyqy" value="ABCBTB">
                                            <img src="<%=resource_url%>res/images/charge/bank-23.jpg" />
                                        </label>
                                        <label for="bank-zsqy">
                                            <input type="radio" name="bank" id="bank-zsqy" value="CMBBTB">
                                            <img src="<%=resource_url%>res/images/charge/bank-24.jpg" />
                                        </label>
                                        <label for="bank-zgqy">
                                            <input type="radio" name="bank" id="bank-zgqy" value="BOCBTB">
                                            <img src="<%=resource_url%>res/images/charge/bank-25.jpg"/>
                                        </label>
                                    </div>
                                    <p class="link-limit"><a>查看银行额度</a></p>
                                    <!--/start工商银行-->
                                    <div class="bank-limit" data-id="bank-gs">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>工商银行客服热线：95588</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>500</td>
                                                <td>1000</td>
                                                <td>办理电子银行口令卡（无需开通短信验证）<br/><a>如何办理？</a></td>
                                                <td rowspan="6">
                                                    <p>1.如果您在银行设置的网上支付额度低于左标限额，以您的设置为准。</p>
                                                    <p>2.存量静态密码客户的总累计限额为300元。</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>2000</td>
                                                <td>5000</td>
                                                <td>办理电子银行口令卡，开通短信认证<br/><a>如何办理？</a></td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>办理U盾<br><a>如何办理？</a></td>
                                            </tr>


                                            <tr>
                                                <td rowspan="3">信用卡</td>
                                                <td>500</td>
                                                <td>1000</td>
                                                <td>办理电子银行口令卡(无需开通短信认证)<br/><a>如何办理？</a></td>
                                            </tr>
                                            <tr>
                                                <td>2000</td>
                                                <td>5000</td>
                                                <td>办理电子银行口令卡，开通短信认证<br/><a>如何办理？</a></td>
                                            </tr>
                                            <tr>
                                                <td>信用卡本身透支额度</td>
                                                <td>信用卡本身透支额度</td>
                                                <td>办理U盾<br><a>如何办理？</a></td>
                                            </tr>
                                            <tr>
                                                <td colspan="5">（注：工行企业版的支付限额以在银行设置的限额为准）</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end工商银行-->
                                    <!--/start建设银行-->
                                    <div class="bank-limit" data-id="bank-js">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>建行银行客服热线：95533</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>5000</td>
                                                <td>5000</td>
                                                <td>
                                                    <p>1、账号支付，无需办理网银，需要预留的手机号接收验证码</p>
                                                    <p>2、无网银盾的网银用户（如办理短信动态口令、刮刮卡）</p>
                                                </td>
                                                <td rowspan="6">
                                                    <p>详情请咨询建行客服：95533</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>5万</td>
                                                <td>10万</td>
                                                <td>一代网银盾用户</td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>50万</td>
                                                <td>二代网银盾用户</td>
                                            </tr>


                                            <tr>
                                                <td>信用卡</td>
                                                <td>5000</td>
                                                <td>5000</td>
                                                <td>账号直接支付，需要预留的手机号接收验证码</td>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">准贷记卡</td>
                                                <td>5万</td>
                                                <td>5万</td>
                                                <td>办理动态口令</td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>50万</td>
                                                <td>办理动态口令</td>
                                            </tr>
                                            <tr>
                                                <td colspan="5">（注：建行企业版的支付限额以在银行设置的限额为准）</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end建设银行-->
                                    <!--/start农行-->
                                    <div class="bank-limit" data-id="bank-ny">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>农行客服热线：95599</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>1000</td>
                                                <td>3000</td>
                                                <td>办理动态口令卡</td>
                                                <td rowspan="4">
                                                    <p>详情请咨询农行客服：95599</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>无限额</td>
                                                <td>无限额</td>
                                                <td>办理农行K宝</td>
                                            </tr>


                                            <tr>
                                                <td rowspan="2">信用卡</td>
                                                <td>1000</td>
                                                <td>3000</td>
                                                <td>办理动态口令卡</td>
                                            </tr>
                                            <tr>
                                                <td>自行设置</td>
                                                <td>自行设置</td>
                                                <td>办理农行K宝</td>
                                            </tr>
                                            <tr>
                                                <td colspan="5">（注：农行企业版的支付限额以在银行设置的限额为准）</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end农行-->
                                    <!--/start招行-->
                                    <div class="bank-limit" data-id="bank-zs">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>招行客服热线：95555</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>500</td>
                                                <td>500</td>
                                                <td>开通大众版网上支付功能</td>
                                                <td rowspan="3">
                                                    <p>详情请咨询招行客服：95555</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>无限额</td>
                                                <td>无限额</td>
                                                <td>开通专业版网上支付功能</td>
                                            </tr>


                                            <tr>
                                                <td>信用卡</td>
                                                <td>2000</td>
                                                <td>信用卡本身透支额度</td>
                                                <td>开通网上支付功能</td>
                                            </tr>
                                            <tr>
                                                <td colspan="5">（注：招行企业版网上银行支付无限额）</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end招行-->
                                    <!--/start中行-->
                                    <div class="bank-limit" data-id="bank-zg">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>中行客服热线：95566</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>50万</td>
                                                <td>350万</td>
                                                <td>
                                                    <p>方式一：开通U-KEY +短信</p>
                                                    <p>方式二：开通动态口令卡+短信</p>
                                                </td>
                                                <td rowspan="4">
                                                    <p>详情请咨询中行客服：95566</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>1000</td>
                                                <td>5000</td>
                                                <td>开通中银快付</td>
                                            </tr>


                                            <tr>
                                                <td rowspan="2">信用卡</td>
                                                <td>5000</td>
                                                <td>无限额</td>
                                                <td>
                                                    <p>方式一：开通U-KEY +短信</p>
                                                    <p>方式二：开通动态口令卡+短信</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>1000</td>
                                                <td>5000</td>
                                                <td>开通中银快付</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end中行-->
                                    <!--/start邮政-->
                                    <div class="bank-limit" data-id="bank-yz">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>邮储客服热线：95580</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>5万</td>
                                                <td>5万</td>
                                                <td>办理手机短信服务</td>
                                                <td rowspan="3">
                                                    <p>详情请咨询邮储客服：95580</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>20万</td>
                                                <td>20万</td>
                                                <td>办理电子令牌以及手机短信服务</td>
                                            </tr>
                                            <tr>
                                                <td>200万</td>
                                                <td>200万</td>
                                                <td>办理USB-KEY以及开通短信服务</td>
                                            </tr>

                                        </table>
                                    </div>
                                    <!--/end邮政-->
                                    <!--/start交行-->
                                    <div class="bank-limit" data-id="bank-jt">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>交行客服热线：95559</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>5万</td>
                                                <td>5万</td>
                                                <td>开通短信密码</td>
                                                <td rowspan="3">
                                                    <p>如果您在银行设置的网上支付额度低于左表限额，以您的设置为准。</p>
                                                    <p>详情请咨询交行客服：95559</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>开通U盾</td>
                                            </tr>
                                            <tr>
                                                <td>信用卡</td>
                                                <td>2000</td>
                                                <td>2000</td>
                                                <td></td>
                                            </tr>

                                        </table>
                                    </div>
                                    <!--/end交行-->
                                    <!--/start广发-->
                                    <div class="bank-limit" data-id="bank-gf">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>广发银行客服热线：400-830-8003</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>3000</td>
                                                <td>3000</td>
                                                <td>手机动态验证码</td>
                                                <td rowspan="4">
                                                    <p>客服热线：400-830-8003</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>30万</td>
                                                <td>30万</td>
                                                <td>key盾</td>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">信用卡</td>
                                                <td>2000</td>
                                                <td>2000</td>
                                                <td>手机动态验证码</td>
                                            </tr>
                                            <tr>
                                                <td>2000</td>
                                                <td>2000</td>
                                                <td>key盾</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end广发-->
                                    <!--/start光大-->
                                    <div class="bank-limit" data-id="bank-gd">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>光大银行客服热线：95595</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>2万</td>
                                                <td>2万</td>
                                                <td>手机动态验证码</td>
                                                <td rowspan="6">
                                                    <p>详情请咨询光大客服：95595</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>令牌动态密码</td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>50万</td>
                                                <td>阳光网盾</td>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">信用卡</td>
                                                <td>2万</td>
                                                <td>2万</td>
                                                <td>手机动态密码</td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>阳光网盾</td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>50万</td>
                                                <td>令牌动态密码</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end光大-->
                                    <!--/start兴业-->
                                    <div class="bank-limit" data-id="bank-xy">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>兴业银行客服热线：95561</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>5000</td>
                                                <td>5000</td>
                                                <td>仅支持开通短信口令的用户（且只针对在网上开通的）或者开通动态令牌的保护的银行卡</td>
                                                <td rowspan="3">
                                                    <p>详情请咨询光大客服：95595</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>开通证书保护，或通过柜面开通短信口令保护的用户</td>
                                            </tr>
                                            <tr>
                                                <td>信用卡</td>
                                                <td>2000</td>
                                                <td>2000</td>
                                                <td>开通网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end兴业-->
                                    <!--/start平安-->
                                    <div class="bank-limit" data-id="bank-pa">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>平安银行客服热线：95511转3</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td>储蓄卡</td>
                                                <td>5万</td>
                                                <td>5万</td>
                                                <td>开通网上支付功能</td>
                                                <td rowspan="2">
                                                    <p>详情请咨询平安客服：95511转3</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>信用卡</td>
                                                <td>2500</td>
                                                <td>2500</td>
                                                <td>开通网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end平安-->
                                    <!--/start中信-->
                                    <div class="bank-limit" data-id="bank-zx">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>中信银行客服热线：95558</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>1000</td>
                                                <td>1000</td>
                                                <td>办理文件证书</td>
                                                <td rowspan="2">
                                                    <p>详情请咨询中信客服：95558</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>办理移动证书</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end中信-->
                                    <!--/start浦发-->
                                    <div class="bank-limit" data-id="bank-pf">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>浦发银行客服热线：95528</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>20万</td>
                                                <td>20万</td>
                                                <td>开通动态密码版网上支付功能</td>
                                                <td rowspan="5">
                                                    <p>详情请咨询浦发客服：95528</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>自行设置</td>
                                                <td>自行设置</td>
                                                <td>开通数字证书版网上支付功能</td>
                                            </tr>
                                            <tr>
                                                <td>信用卡</td>
                                                <td>自行设置</td>
                                                <td>自行设置</td>
                                                <td>开通网上支付功能</td>
                                            </tr>

                                            <tr>
                                                <td rowspan="2">贷记卡</td>
                                                <td>自行设置</td>
                                                <td>自行设置</td>
                                                <td>开通数字证书版网上支付功能</td>
                                            </tr>
                                            <tr>
                                                <td>5万</td>
                                                <td>5万</td>
                                                <td>开通动态密码版网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end浦发-->
                                    <!--/start民生-->
                                    <div class="bank-limit" data-id="bank-ms">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>民生银行客服热线：95558</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>5000</td>
                                                <td>5000</td>
                                                <td>办理短信验证码或者浏览器证书</td>
                                                <td rowspan="2">
                                                    <p>详情请咨询民生客服：95568</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>50万</td>
                                                <td>办理otp或者u宝</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end民生-->
                                    <!--/start北京-->
                                    <div class="bank-limit" data-id="bank-bj">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>北京银行客服热线：95526</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>1000</td>
                                                <td>5000</td>
                                                <td>开通动态密码版网上支付功能</td>
                                                <td rowspan="2">
                                                    <p>北京银行网上支付不支持”普通支付“。</p><p> 详情请咨询北京银行客服：95526</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>100万</td>
                                                <td>100万</td>
                                                <td>开通证书版网上支付功能 </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end北京-->
                                    <!--/start上海-->
                                    <div class="bank-limit" data-id="bank-sh">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>上海银行客服热线：95594</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>2000</td>
                                                <td>--</td>
                                                <td>签约上银快付业务</td>
                                                <td rowspan="6">
                                                    <p>详情请咨询上海银行客服：95594</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>6000</td>
                                                <td>1万</td>
                                                <td>办理动态密码版个人网银（含文件证书）</td>
                                            </tr>
                                            <tr>
                                                <td>50万</td>
                                                <td>100万</td>
                                                <td>办理E盾证书专业版个人网银</td>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">信用卡</td>
                                                <td>2000</td>
                                                <td>--</td>
                                                <td>签约上银快付业务</td>
                                            </tr>
                                            <tr>
                                                <td>6000</td>
                                                <td>1万</td>
                                                <td>办理动态密码版个人网银（含文件证书）</td>
                                            </tr>
                                            <tr>
                                                <td>5万</td>
                                                <td>信用卡本身透支额度</td>
                                                <td>办理E盾证书专业版个人网银</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end上海-->
                                    <!--/start杭州-->
                                    <div class="bank-limit" data-id="bank-hz">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>杭州银行客服热线：95398</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>300</td>
                                                <td>300</td>
                                                <td>开通大众版网上支付功能</td>
                                                <td rowspan="3">
                                                    <p>详情请咨询杭州银行客服：95398</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>无限额</td>
                                                <td>无限额</td>
                                                <td>开通专业版网上支付功能</td>
                                            </tr>
                                            <tr>
                                                <td>信用卡</td>
                                                <td>500</td>
                                                <td>500</td>
                                                <td>开通网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end杭州-->
                                    <!--/start宁波-->
                                    <div class="bank-limit" data-id="bank-nb">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>宁波银行客服热线：95574</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">储蓄卡</td>
                                                <td>300</td>
                                                <td>300</td>
                                                <td>办理快速通道电子支付密码</td>
                                                <td rowspan="5">
                                                    <p>详情请咨询宁波银行客服：95574</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>20万</td>
                                                <td>20万</td>
                                                <td>办理动态密码牌/短信动态密码</td>
                                            </tr>
                                            <tr>
                                                <td>无限额</td>
                                                <td>无限额</td>
                                                <td>办理USBKEY移动证书</td>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">信用卡</td>
                                                <td>999</td>
                                                <td>3000</td>
                                                <td>开通信用卡版网上支付功能</td>
                                            </tr>
                                            <tr>
                                                <td>5000</td>
                                                <td>5000</td>
                                                <td>开通专业版网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end宁波-->
                                    <!--/start富滇-->
                                    <div class="bank-limit" data-id="bank-fd">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>富滇银行客服热线：400-889-6533</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>1000</td>
                                                <td>5000</td>
                                                <td>开通短信认证版网上支付功能</td>
                                                <td rowspan="2">
                                                    <p>详情请咨询富滇银行客服：400-889-6533</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>自行设置</td>
                                                <td>自行设置</td>
                                                <td>开通证书认证版网上支付功能</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end富滇-->
                                    <!--/start北京农商-->
                                    <div class="bank-limit" data-id="bank-bjny">
                                        <p class="til"><span>请关注您的充值金额是否受限</span><em>北京农商银行客服热线：96198</em></p>
                                        <table>
                                            <tr>
                                                <th width="80">银行卡种类</th>
                                                <th width="108">单笔限额（元）</th>
                                                <th width="108">每日限额（元）</th>
                                                <th>需要满足的条件</th>
                                                <th width="98">备注</th>
                                            </tr>
                                            <tr>
                                                <td rowspan="2">储蓄卡</td>
                                                <td>10万</td>
                                                <td>50万</td>
                                                <td>办理手机认证</td>
                                                <td rowspan="2">
                                                    <p>北京农村商业银行客服热线：96198</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>20万</td>
                                                <td>100万</td>
                                                <td>办理证书认证</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <!--/end北京农商-->
                                </div>
                            </dd>
                        </dl>
                        <dl>
                            <dt></dt>
                            <dd>
                                <a class="charge-btn charge-btn-green" id="chargeOnline">去网银页面支付</a>
                            </dd>
                        </dl>
                    </div>
                </div>

            </div>
        </div>
    </div> 
</div> 
	</div>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../../common/newfooter.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/charge/charge_onlineBank.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
