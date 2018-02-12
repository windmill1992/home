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
<title>${project.title }</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/garden/projectDetail_temple.css" />
<link href="<%=resource_url%>res/css/h5/basic.css" rel="stylesheet" type="text/css" />
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
<!--头部-->
<div class="head_top">
	<a href="JavaScript:history.go(-1);"><li class="li1"><img src="<%=resource_url%>/res/images/h5/images/garden/tem_fl.png" class="tem_fl"></li></a>
	<li class="li2"><span></span><b>善缘</b></li>
	<li class="li3"><a href="<%=resource_url%>ucenter/userCenter_h5.do"><img src="<%=resource_url%>/res/images/h5/images/garden/head_top.png" class="tem_fr"></a></li>
</div>
<%--寺院头部--%>
<div class="temple_header">
    <img src="${zhengming.headImageUrl }" alt="" class="headerImg">
    <div class="headerFr">
        <p class="fr_name">${zhengming.realName }</p>
        <ul class="fr_project">
            <li class="name_li1"><span class="name_sp2"></span><p>${zhengming.workUnit }</p></li>
            <li class="name_li2"><span class="name_sp1"></span><p>${zhengming.familyAddress }</p></li>
            <li class="name_li3"><p>${project.oneLevel==100?'如法放生':(project.oneLevel==200?'供养佛宝':(project.oneLevel==300?'供养僧众':(project.oneLevel==400?'倡印法宝':'其他'))) }</p></li>
        </ul>
    </div>
</div>
<!--封面图-->
<div class="hot">
    <img src="${project.coverImageUrl }" class="img_big">
</div>
<h3>${project.title }</h3>
<!--进度条-->
<div class="progress">
    <div class="progress_bar"><span class="track"><i style="width:${process}%" class="fill"></i></span>
        <label>${processbfb }%</label>
    </div>
</div>
<!--信息卡-->
    <div class="data_list clearfix">
        <a>
            <li><i>目标金额</i><b><span class="num">${project.cryMoney }元</span></b></li>
        </a> <a>
        <li><i>已筹金额</i><b><span class="num">${extensionDonateAmount }元</span></b></li>
    </a> <a>
        <li class="last"><i>捐款人数</i><b><span class="num">${project.donationNum }</span></b></li>
    </a>
    </div>
</div>
<div style="clear: both;"></div>
<!--结缘详情-->
<div cl class="templeContent">
    <div class="mon_1">
        <div class="mon_t fl"></div>
        <p>结缘详情</p>
    </div>
</div>
<!--content-->
<div class="list" id="list_1">
    <div class="content">
    <div class="bdcon survey">${project.content}<br/>
    	<c:forEach items="${project.bfileList}" var="img">
			<p class="pic"><img src="${img.url}" width="590px"  alt=""><c:if test="${img.description!='1'}"><i>${img.description}</i></c:if></p>
		</c:forEach>
		</div>
       <!--  <p>
            视打撒啊是大是大V的事实打算打撒是谁的傲视打SDFASDFAASASDFASDFAASDAFSD双方都山东肥城
        </p>
        <br>
        <p class="pic">
            <img src="http://res.17xs.org/picture/projectContent/20160226/1456479236740_74.jpg" width="100%" alt="">
            <i></i>
        </p> -->
        <div class="clear"></div>
    </div>
</div>
<!--有缘人-->
<div cl class="templeContent">
    <div class="mon_1">
        <div class="mon_t fl"></div>
        <p>有缘人</p>
    </div>
</div>
<div class="list-box">
   <!--  <a href="javascript:void(0);">
        <div class="item record"><span class="thumb">
            <img osrc="http://res.17xs.org/picture/personhead/20160226/1456472231367_684.jpg" class="fl" src="http://res.17xs.org/picture/personhead/20160226/1456472231367_684.jpg"></span>
            <div class="name">
                <p class="name_p1">skn252的昵称a</p>
                <p class="name_p2">捐<label>￥0.01</label></p>
            </div>
            <div class="money">
                <p class="money_p1">支持公益</p>
                <div class="clear"></div>
                <div class="timeMessagej">
                    <span class="timeSpj">07月29日</span>
                    <div class="messagej">
                        <span class="messageSpj" onclick="donateLeaveWord(10337,0)"></span>
                    </div>
                </div>
                <div class="message_boardj  message_listj0" style="">
                    <p class="board_textj"><span onclick="donateReply(787,10301,1)">skn001：</span>hjhhhh</p>
                    <span class="danoteMore0 danote1" onclick="donateLoadMore(0,10337,0)" style="">查看更多</span>
                </div>
            </div>
            <div style="clear:both"></div>
        </div>
        <div class="clear"></div>
    </a> 
    -->
</div>
	<div class="load-more">没有更多数据了</div>
<!--底部按钮-->
<div class="templeBtn">
 	<c:choose>
 		<c:when test="${project.state==240 }">
 			<a href="javaScript:void(0)" class="btnFl fl" id="paybtn">我要结缘</a>
    		<a href="javaScript:void(0)" class="btnFr fr" id="dayDonate">日行一善</a>
 		</c:when>
 		<c:when test="${project.state==260 }">
 			<a href="javaScript:void(0)" class="btnFl fl">已结束</a>
 		</c:when>
 		<c:otherwise>
 			<a href="javaScript:void(0)" class="btnFl fl">未发布</a>
 		</c:otherwise>
 	</c:choose>
    
    
    
</div>

<!--输入金额弹框-->
<div id="payTips" style="display: none">
<c:choose>
<c:when test="${moneyConfigs[0]!=null }">
<div class="backBj"></div>
    <div class="moneyKuang">
		<div class="moneyTop">
			<a href="javascript:void(0);"><img src="<%=resource_url%>/res/images/h5/images/garden/cha.png" class="cha1" /></a>
		</div>
      <div class="moneyInput">
          <input type="button" value="-" class="conterFr" id="paytipsSub"/>
          <input type="text" value="${moneyConfigs[0].content }" class="conter" id="paytipsCon"  readonly="readonly" />
          <input type="button" value="+" class="conterFl" id="paytipsAdd"/>
      </div>
        <input type="text" value="${moneyConfigs[0].money }" class="moneyconter" id="money2"/>
        <textarea class="zfText" id="donateWord">我要祝福</textarea>
        <a href="javaScript:void(0);" class="moneyBtn" id="btn_submit">我要结缘</a>
    </div>
</c:when>
<c:otherwise>
<div id="payTips2" style="position: absolute; width: 100%; height: 18386px; top: 0px; left: 0px; z-index: 700; background-color: rgba(0, 0, 0, 0.701961);" class="">
    <div class="pay" style="">
        <!-- <div class="redPaperWrap" style="display:none;" id="redPackets">
            <div class="redPaperTop">
                <h1>点 击 红 包 捐 献 爱 心</h1>
                <span class="close" id="redclose"></span>
            </div>
            <ul class="redPaperList" id="redPaperList">
            </ul>
        </div> -->
        <div class="t">
            <div class="close" id="closeDetail">
                <img src="<%=resource_url%>/res/images/h5/images/garden/cha.png">
            </div>
            <span>请输入捐款金额</span>
        </div>
        <div class="box">
            <table class="tb" id="selectTb">
                <tbody>
                <tr>
                    <td>
                        <div t="50" class="sel l">
                            <span>50</span>元<b></b>
                        </div>
                    </td>
                    <td>
                        <div t="100" class="sel l">
                            <span>100</span>元<b></b>
                        </div>
                    </td>
                    <td>
                        <div t="300" class="sel l">
                            <span>300</span>元<b></b>
                        </div>
                    </td>
                    <td>
                        <div t="500" class="sel l">
                            <span>500</span>元<b></b>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><div style="height:10px;"></div></td>
                </tr>
                <tr>
                    <td colspan="4"><div t="0" class="sel l"><input type="number" id="money2" placeholder="请输入其它金额" class="money"></div></td>
                </tr>
                <tr>
                    <td colspan="4"><div style="height:10px;"></div></td>
                </tr>
                <tr>
                    <td colspan="4"><div style="" class="sel_fl"><input type="text" id="realName" placeholder="姓名" class="money" value="${user.realName }"></div><div style="" class="sel_fr"><input type="number" id="mobileNum" placeholder="手机号" class="money" value="${user.mobileNum }"></div></td>
                </tr>
                <tr>
                    <td colspan="4"><div style="height:10px;"></div></td>
                </tr>
                <tr>
                    <td colspan="4"><div t="0" class="sel l"><input type="text" id="donateWord" placeholder="我的祝福" class="money" style="text-align:left"></div></td>
                </tr>
                </tbody>

            </table>
            <a href="javascript:void(0)" class="btn_a" id="btn_submit"><div class="btn">立即捐款</div></a>
            <div class="prot">
                <input type="checkbox" id="check" name="check" checked="checked"><label for="check">同意并接受</label> <span onclick="toAbout1()" class="p"><a href="http://www.17xs.org/redPackets/getAgreement.do">《善园基金会用户协议》</a></span>
            </div>
        </div>
    </div>
</div>
</c:otherwise>
</c:choose>
</div>


<!--日行一善弹框-->
<div style="display: none" id="dayShan">
    <div class="backBj"></div>
    <div class="daykuang">
        <div class="dayTop">日行一善</div>
        <div class="daykuang1">
            <p class="dayText">日行一善，大慈大悲，请选择与您结缘的寺院</p>
            <div class="dayNav">
                <li class="navFl">随喜结缘</li>
                <li class="navFr">指定寺院</li>
            </div>
            <p class="dayText">请输入每日行善金额</p>
            <input type="text" value="" class="moneyconter" id="dayMoney"/>
            <!-- <textarea class="zfText">我要祝福</textarea> -->
            <a href="javaScript:void(0);" class="moneyBtn" id="dayBtn">我要结缘</a>
        </div>
    </div>
</div>
<!--充值弹框-->
<div class="cue_f" style="display:none;">
    <div class="cue_background"></div>
    <div class="cue">
        <div class="cue_center">
            <div class="cue_center1">
                <p class="cue_p1">善款充值</p>
                <p class="cue_p2">请输入充值金额：<input type="text" placeholder="" id="chargeMomeny" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" style="color: rgb(182, 182, 182);">元</p>
                <p class="cue_p3">充值的善款只能用于捐赠，不可以提现或者购物</p>
            </div>
            <div class="cue_center2">
                <a href="javascript:void(0);"><div class="cue_fl" id="confirCharge"><p class="cue_pl">确认充值</p></div></a>
                <a href="javascript:void(0);"><div class="cue_fr" id="concelCharge"><p class="cue_pr">取消</p></div></a>
            </div>
        </div>
    </div>
</div>
<!--回复留言-->
	<div class="moved_up" style="display: none">
		<div class="back"></div>
		<div class="end_center">
			<div class="end_hfk">
				<span class="end_r">发表评论：</span>
				<textarea class="end_text" cols="45" rows="7" id="content"
					placeholder=""></textarea>
			</div>
			<div class="end_foot">
				<a href="javascript:void();">
					<div class="end_fl1 fl" id="leaveWordSubmit">确定</div>
				</a> <a href="javascript:void();">
					<div class="end_fl1 fr">取消</div>
				</a>
			</div>
		</div>
	</div>
<!--捐款确认弹框-->
<div class="prompt_box" style="display: none" id="loginCue">
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
<!--余额不足时弹框提示-->
<div class="prompt_box" style="display: none" id="payMoney_prompt">
    <div class="cue_back"></div>
    <div class="cue1">
        <div class="cue_center" style="height: 158px;">
            <div class="cue_center1">
                <p class="cue_p1">提示 </p>
                <p class="cue_p3" id="payMoney_content">捐款金额不低于0.01元</p>
            </div>
            <div class="cue_center2">
                <a href="javascript:void(0);" class="ui-link" id="payMoney_promptConfirm"><div class="cue1_flr"><p class="cue_pl1">确认</p></div></a>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="pprim" data="">
<input type="hidden" id="projectId" value="${projectId}"/>
<input type="hidden" id="userId" value="${userId }"/>
<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-extensionDonateAmount}"/>">
<input type="hidden" id="browser" value="${browser }"/>
<input type="hidden" id="extensionPeople" value="${extensionPeople }"/>
<input type="hidden" id="sort" value="${moneyConfigs[0].priority }" />
<input type="hidden" id="donateType" value="0" />
<input type="hidden" id="userName" value="${user.userName }" />
<input type="hidden" id="mobile" value="${user.mobileNum }" />
<script language="javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script data-main="<%=resource_url%>res/js/h5/garden/projectdetail.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
<script type="text/javascript">
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline' ]
	});

	wx.ready(function(){
		wx.onMenuShareAppMessage({
					title : '${project.title}', // 分享标题
					desc : '${desc}', // 分享描述
					link : 'http://www.17xs.org/project/gardenview_h5.do?projectId=${project.id}', // 分享链接
					imgUrl : '${project.coverImageUrl}', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
// 						window.location.href = 'http://www.17xs.org/';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '${project.title}', // 分享标题
			link : 'http://www.17xs.org/project/gardenview_h5.do?projectId=${project.id}', // 分享链接
			imgUrl : '${project.coverImageUrl}', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	});
		});
</script>
<script type="text/javascript">
	//捐助留言
	function donateLeaveWord(donateId,index) {
		var projectId = $("#projectId").val();
		$("#projectDonateId").val(donateId);
		$("#projectFeedbackId").val("");
		$("#index").val(index);
		$.ajax({
					url : "http://www.17xs.org/h5GardenProject/gotoAlterReply.do",
					data : {
						type : 1,
						projectId : projectId,
						projectDonateId : donateId
					},
					success : function(result) {
					  if (result.flag == "201") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#leavewordUserId").val(result.obj.leavewordUserId);
							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.flag == "202") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#replyUserId").val(result.obj.replyUserId);
							$("#replyName").val(result.obj.replyName);
							$("#leavewordUserId").val(result.obj.leavewordUserId);

							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.errorCode == "0001") {//未登录
							$("#msg").html('<p>您还未登录！</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							window.location = 'http://www.17xs.org/ucenter/user/Login_H5.do';
						} 
						else if(result.flag==-2){
							window.location =result.errorMsg;
						}
						else {//失败
							$("#msg").html('<p>' + result.errorMsg + '</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							return;
						}
					}
				});
	}
	//
	function donateReply(leavewordUserId, donateId,index) {
		var projectId = $("#projectId").val();
		$("#index").val(index);
		$("#projectDonateId").val(donateId);
		$("#projectFeedbackId").val("");
		$.ajax({
					url : "http://www.17xs.org/h5ProjectDetails/gotoAlterReply.do",
					data : {
						type : 1,
						projectId : projectId,
						projectDonateId : donateId,
						leavewordUserId : leavewordUserId
					},
					success : function(result) {
						if (result.flag == "101") {//成功
							$("#leavewordName").val(result.obj.leavewordName);
							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.flag == "102") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#replyUserId").val(result.obj.replyUserId);
							$("#replyName").val(result.obj.replyName);
							$("#leavewordUserId").val(result.obj.leavewordUserId);
							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.flag == "201") {
							$("#leavewordName").val(result.obj.leavewordName);
							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.flag == "202") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#replyUserId").val(result.obj.replyUserId);
							$("#replyName").val(result.obj.replyName);
							$("#leavewordUserId").val(
									result.obj.leavewordUserId);

							$(".moved_up").show();
							$(".end_text").focus();
						} else if (result.errorCode == "0001") {//未登录
							$("#msg").html('<p>您还未登录！</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							window.location = 'http://www.17xs.org/ucenter/user/Login_H5.do';
						}
						else if(result.flag==-2){
							window.location =result.errorMsg;
						}
						 else {//失败
							$("#msg").html('<p>' + result.errorMsg + '</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							return;
						}
					}
				});
	}
	//查看更多留言
	function donateLoadMore(i, projectDonateId,surplusTotal) {
		var currentPage = $("#currentPageForFeedBack").val();
		
		var projectId = $("#projectId").val();
		var leaveWords = $('.donateLoadMore' + i).html();
		var html = [];
		$.ajax({
				url : "http://www.17xs.org/h5ProjectDetails/loadMoreLeaveWord.do",
				data : {
					type : 0,
					projectId : projectId,
					projectDonateId : projectDonateId,
					currentPage : currentPage,
					surplusTotal:surplusTotal
				},
				success : function(result) {
					if (result.flag == "1") {
						//成功
						itemss = result.obj;
						for (var j = itemss.length - 1; j >= 0; j--) {
							if (itemss[j].replyUserId == null) {
								html.push('<p class="board_textj" ><span onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
								} else {
									html.push('<p class="board_textj" ><span  onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+"<b class='hf'>回复</b>"+itemss[j].replyName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
								}
							}
							//追加数据
							$('.message_listj'+i).html(html.join(''));
							//判断是否显示加载更多
							//$('.danoteMore' + i).html("");
							$('.danoteMore' + i).hide();
						} else {//失败
							$("#msg").html('<p>' + result.errorMsg + '</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							return;
						}
					}
				});
	}
</script>
<script>
    $(function(){
        $('.sel').click(function(){
             $(this).closest('td').siblings('td').find('b').removeClass('ed');
		     $(this).children("b").addClass("ed");

        });
        $("#money2").click(function(){
            $(".sel>b").removeClass("ed");
        });
    });
</script>
<input id="projectDonateId" type="hidden">
<input id="projectFeedbackId" type="hidden">
<input id="leavewordUserId" type="hidden">
<input id="replyUserId" type="hidden">
<input id="leavewordName" type="hidden">
<input id="index" type="hidden">
<input id="replyName" type="hidden">
<input id="donateWord" type="hidden">
<!-- 存储返回信息的当前页 -->
<input id="currentPageForFeedBack" type="hidden" value="1">
<input id="judge" type="hidden">
<input id="auditProjectCount" type="hidden" value="${auditProjectCount }">
<input id="message" type="hidden" value="${message }" />
<input id="message2" type="hidden" value="${message }" />
<%--提示信息--%>
<div class="cue2" style="display: none"  id="msg"></div>
</body>
</html>
