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
<title>善缘列表</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/garden/projectDetail_temple.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/garden/temple_list.css" />
<link rel="stylesheet" href="<%=resource_url%>res/css/h5/garden/sjld.css" />
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<style type="text/css">
/*底部按钮*/
.templeBtn{
    width: 88%;
    height: 35px;
    line-height: 40px;
    background: #eeecdf;
    position: fixed;
    bottom: 0;
    padding: 2% 6%;
    border-top:1px solid #bcb996;
}
.templeBtn .btnFl{
    display: inline-block;
    width: 45%;
    background: #9a7940;
    height: 35px;
    line-height: 35px;
    text-align: center;
    color: #fff;
    border-radius: 5px;
}
.templeBtn .btnFr{
    display: inline-block;
    width: 45%;
    border: 2px solid #9a7940;
    height: 33px;
    line-height: 33px;
    text-align: center;
    color: #9a7940;
    border-radius: 5px;
}
</style>
    <script>
    $(function(){
        $(".dropdown_fl").click(function(){
            $(".bj").show();
            $(".sacrifices").slideDown();
        });
        $(".bj").click(function(){
            $(".sacrifices").slideUp();
            $(".bj").hide();
        });
	$(".backBj ").click(function(){
           
            $("#dayShan").hide();
        });
    });
    function loadList(oneLevel){
			var that=this,address=$('.dropdown_fr>dl>dd').html(),type=oneLevel,page=1,pageNum=20;
			$(".sacrifices").slideUp();
            $(".bj").hide();
            var type2=oneLevel==100?'如法放生':(oneLevel==200?'供养佛宝':(oneLevel==300?'供养僧众':(oneLevel==400?'倡印法宝':'全部')));
            $(".dropdown_fl").html(type2+'<span></span>');
			$.ajax({ 
					url:'http://www.17xs.org/h5GardenProject/getProject_list.do', 
					data:{type:type,address:address,page:page,pageNum:pageNum,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==1){
							var datalist=result.obj,html=[];
							if(datalist!=null){
							var total=result.obj.total;
							$('#page').val(page);
							for(var i=0;i<datalist.length;i++){
								html.push('<div class="dropdown_list">');
								html.push('<a href="http://www.17xs.org/h5GardenProject/projectDetail.do?projectId='+datalist[i].projectId+'">');
								html.push('<div class="center_top">');
								html.push('<p class="p_fl">'+datalist[i].title+'</p>');
								html.push('<p class="p_fr">'+datalist[i].issueTime+'</p>');
								html.push('</div>');
								html.push('<p class="content_p">'+datalist[i].content+'</p>');
								html.push('<div class="center_bottom">');
								html.push('<div class="flImg"><img src="'+datalist[i].coverImageUrl+'"></div>');
								html.push('<div class="listFr">');
								html.push('<div class="list_money">');
								html.push('<li class="money_li1">已筹<span>'+datalist[i].donateAmount+'</span>元</li>');
								html.push('<li class="money_li2">目标<span>'+datalist[i].cryMoney+'</span>元</li>');
								//html.push('<li class="money_li3">进度<span>'+datalist[i].donatePercent+'</span></li>');
								html.push('</div>');
								html.push('<div class="list_name">');
								html.push('<li class="name_li1"><span class="name_sp1"></span><p>'+datalist[i].familyAddress+'</p></li>');
								html.push('<li class="name_li2"><span class="name_sp2"></span><p>'+datalist[i].workUnit+'</p></li>');
								//html.push('<li class="name_li3"><a href="javascript:void(0)" class="name_btn">我要结缘</a></li>');
								html.push('</div></div></div></a></div>');
							}
							$('.dropdown_center').html(html.join(''));
							if(Number(datalist[0].total)>Number(page)*Number(pageNum)){
								$('.load-more').html('加载更多');
							}
							else{
								$('.load-more').html('没有更多数据了');
							}
						}
						else{
							//$('.dropdown_center').html(html.join(''));
							$('.load-more').html('没有更多数据了');
						}
						}
						else{
							//$('.dropdown_center').html(html.join(''));
							$('.load-more').html('没有更多数据了');
						}
						
					}
				});
		
    }
    </script>
</head>
<body>
<div class="head_top">
    <a href="javascript:history.go(-1)"><li class="li1"><img src="<%=resource_url%>res/images/h5/images/garden/tem_fl.png" class="tem_fl"></li></a>
    <li class="li2"><span></span><b>善缘</b></li>
    <li class="li3"><a href="<%=resource_url%>ucenter/userCenter_h5.do"><img src="<%=resource_url%>res/images/h5/images/garden/head_top.png" class="tem_fr"></a></li>
</div>
<!--下拉菜单-->
<div class="dropdown">
    <div class="dropdown_fl">全部<span></span></div>
    <a id="expressArea" href="javascript:void(0)">
        <div class="dropdown_fr">
            <dl style="float: left">
                <dd>点击选择地区</dd>
            </dl>
            <span></span>
        </div>
    </a>
    <div style="clear: both"></div>
</div>
<div id="areaMask" class="mask"></div>
<section id="areaLayer" class="express-area-box">
    <header>
        <h3>选择地区</h3>
        <a id="backUp" class="back" href="javascript:void(0)" title="返回"></a>
        <a id="closeArea" class="close" href="javascript:void(0)" title="关闭"></a>
    </header>
    <article id="areaBox">
        <ul id="areaList" class="area-list"></ul>
    </article>
</section>
<div class="heTiao"></div>
<!--列表内容-->
<div class="dropdown_center">
    <!-- <div class="dropdown_list">
        <div class="center_top">
            <p class="p_fl">九峰禅寺佛前诵经供灯</p>
            <p class="p_fr">一个月前</p>
        </div>
        <p class="content_p">于供养位居士参于供养位居士参于供养位居士参于供养位居士参于供养位居士参于供养位居士参于供养位居士。</p>
        <div class="center_bottom">
            <div class="flImg"><img src="images/zpt1.png"></div>
            <div class="listFr">
                <div class="list_money">
                    <li class="money_li1">已筹<span>23456</span>元</li>
                    <li class="money_li2">目标<span>23456333</span>元</li>
                    <li class="money_li3">进度<span>23%</span></li>
                </div>
                <div class="list_name">
                    <li class="name_li1"><span class="name_sp1"></span><p>九峰禅寺</p></li>
                    <li class="name_li2"><span class="name_sp2"></span><p>九峰禅寺</p></li>
                    <li class="name_li3"><a href="javascript:void(0)" class="name_btn">我要支持</a></li>
                </div>
            </div>
        </div>
    </div> -->
</div>
<!--底部按钮-->
<div class="templeBtn">
    <!-- <a href="javaScript:void(0)" class="btnFl fl">我要结缘</a> -->
    <a href="javaScript:void(0)" class="btnFl" style="width:100%">日行一善</a>
</div>
<a href="javascript:void(0)"><div class="load-more"></div></a>
<!--日行一善弹框-->
<div style="display: none" id="dayShan">
    <div class="backBj dayBj"></div>
    <div class="daykuang">
        <div class="dayTop">日行一善</div>
        <div class="daykuang1">
            <p class="dayText">日行一善，大慈大悲，请选择与您结缘的寺院</p>
            <div class="dayNav">
                <li class="navFl navFr" id="random">随喜结缘</li>
                <li class="" id="confrim_nav">指定寺院</li>
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
<!--结缘选择弹窗-->
<div style="display: none" id="projects_alert">
    <div class="backBj"></div>
    <div class="temple_input">
        <div class="quanxuan">
            <span class="quan_fl">请选择结缘寺院</span>
            <span class="quan_fr"><input type="checkbox" class="dtInput_quanxuan"><p class="dtperson dt_quanxuan" style="width:50px">全选</p></span>
        </div>
        <div class="temple_li">
        	<c:forEach  items="${projects}" var="lists">
        	<li><input type="checkbox" class="dtInput" value="${lists.id }"><p class="dtperson">${lists.workUnit }，${lists.title }</p></li>
        	</c:forEach>
            <!-- <li><input type="checkbox" class="dtInput"><p class="dtperson">帮助孤儿完遽反道山完成他的学业，圆他上学梦</p></li> -->
        </div>
        <div class="temBtn">
            <a herf="javascript:void();" class="temTj" id="queding">添加</a>
            <a herf="javascript:void();" class="temQx" id="quxiao">取消</a>
        </div>
    </div>
</div>
    <!--供养佛像下拉列表-->
<div class="sacBlock">
    <div class="bj" style="display: none"></div>
    <div class="sacrifices" style="display: none">
    	<a href="javascript:void();" onclick="loadList('')"><li>全部</li></a>
    	<c:forEach  items="${list}" var="lists">
    		<a href="javascript:void();" onclick="loadList(${lists.oneLevel})"><li>${lists.oneLevel==100?'如法放生':lists.oneLevel==200?'供养佛宝':lists.oneLevel==300?'供养僧众':lists.oneLevel==400?'倡印法宝':'其他' }</li></a>
    	</c:forEach>
        
    </div>
</div>
<input type="hidden" id="type" value="" />
<input type="hidden" id="page" value="1" />
<input type="hidden" id="projectIds" />
<input type="hidden" id="browser" />
<input type="hidden" id="projectId" />
<input type="hidden" id="userName" value="${user.userName }"/>
<input type="hidden" id="mobile" value="${user.mobileNum }"/>
<input id="message" type="hidden" value="${message }" />
<input id="message2" type="hidden" value="${message }" />
<script src="<%=resource_url%>res/js/h5//jquery.area.js"></script>
<script data-main="<%=resource_url%>res/js/h5/garden/project_list.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
