<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>个人用户中心</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v="/>
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/project_initiator.css?v=2017">
<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
</head>
<script type="text/javascript">
   //项目管理
	function projectManage(projectId,recommendedPerson,userId,type){
        //将点击获取的项目id存储起来
        $("#projectId").val(projectId);
        $("#recommendedPerson").val(recommendedPerson);
        $("#userId").val(userId);
        $("#type").val(type);
        $(".layer").slideDown();
    }
    //项目反馈
	function projectCommentBatch(projectId){
       $("#projectId").val(projectId);
       var state=$("#state").val();
       location.href = "/uCenterProject/projectFeedBack.do?projectId="+projectId+"&state="+state;
       
    }
    function reprotForm(){
    	$('.layer').hide();
    	$("#msg").html('<p>报名清单正在火速开发中，请耐心等待！</p>');
		$("#msg").show();
		setTimeout(function () {
			  $("#msg").hide();
		}, 2000);
		return ;
    }
    function withDrawDeposit(){//项目数据
        //将点击获取的项目id存储起来
    	var projectId=$("#projectId").val();
        location.href = "/uCenterProject/gotoInitatorDetal.do?projectId="+projectId;
    }
    function edit(){
    	var projectId = $("#projectId").val(),
    		recommendedPerson = $("#recommendedPerson").val(),
       		userId = $("#userId").val(),
       		type = $("#type").val(),
       		urls = "/webhtml/view/h5/releaseProject/raiseProjEdit.html?projectId="+projectId;
       if(recommendedPerson){
       		urls += "&recommendedPerson="+recommendedPerson;
       }else if(type=="enterpriseProject"){
       		urls += "&charityId="+userId;
       }
       location.href=urls;
    }
    function projectList(state ,i){
        var urls = "/uCenterProject/uCenterProjectList.do?state="+state;
        location.href = urls + "&currentPage="+i;
    }
    //回到项目发布页面
    function releaseProject(){
    	location.href = "/test/gotoProtocolRelease.do";
    }
    $(function () { 
    	var state=$('#state').val();
        //获取选中状态
    	if(state==210){
    		$('#li1').addClass("ligg").siblings().removeClass("ligg");
    	}
    	if(state==240){
    		$('#li2').addClass("ligg").siblings().removeClass("ligg");
    	}
    	if(state==260){
    		$('#li3').addClass("ligg").siblings().removeClass("ligg");
    	}
    	if(state==230){
    		$('#li4').addClass("ligg").siblings().removeClass("ligg");
    	}
    	$('#end').click(function(){//提前结束项目弹框
    		$('.moved_up').show();
    	});
    	$('.end_fl').click(function(){//提前结束项目：确定
    		$('.moved_up').hide();
    		$('.layer').hide();
    		var projectId=$("#projectId").val(),deadExplain=$('#deadExplain').val();
    		$.ajax({
				url:window.baseurl+"uCenterProject/beforeEndProject.do",
				data:{id:projectId,deadExplain:deadExplain},
				success: function(result){
					if(result.flag==1){//成功
						$("#msg").html('<p>提前结束项目成功！</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
				        location.reload();
						return ;
					}else if(result.flag==-1){//为登录
						location.href = '/ucenter/user/Login_H5.do';
					}else{//失败
						$("#msg").html('<p>'+result.errorMsg+'</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
						return ;
					}
				}
			});
    	});
    	$('.end_fr').click(function(){//提前结束项目：取消
    	 	$('.moved_up').hide();
    		return;
    	});
    	$('.layer_qx').click(function(){//layer：取消
    		$('.layer').hide();
    		return;
    	});
    	$('.mask').on('click',function(){
    		$(this).parent().hide();
    	})
    	//当页面的数据显示完了是隐藏“加载更多”
    	var tips = $("#tips").val(),
    		total = $("#total").val(),   
    		counts = $('input[name="count"]');
    	if(total>counts.length){
        	$("#more").show();
    	}else{
        	$("#more").hide();
    	}
    	if(tips==1) {
        	$('#projectlist').append('<p class="zwsj">暂无数据<p>');
    	} 
      
    	//当点击加载更多事件需要ajax请求
    	var currentPage=1;  
    	$('#more').click(function(){	
		 	var state=$("#state").val();
		 	currentPage++;
		 	var uRl = window.baseurl+"uCenterProject/ajaxProjectList.do";
		 	$.ajax({
				url:uRl,
				type: 'post',      //POST方式发送数据
	            async: false,      //ajax同步
				data:{state:state,currentPage:currentPage},
				success: function(result){
					var html=[];
					if(result.result==0){
						var data=result.items,datas=data,len=data.length;
						for(var i=0;i<len;i++){
							html.push('<div class="initator_conter">');
							html.push('<input type="hidden" name="count" />');
							html.push('<div class="tor_top" onclick="clickTitle('+datas[i].id+')">');
							html.push('<div class="torfl">');
							html.push('<img src="'+datas[i].coverImageUrl+'" />');
							html.push('</div><div class="torfr">');
							html.push('<h5 class="frone" >'+datas[i].title+'</h5>');
							html.push('<div class="frtwo">');
							html.push('<p class="frtwo_fl">目标&nbsp;<span class="frtwo_sp">'+datas[i].cryMoney +'</span></p>');
							html.push('<p class="frtwo_fr">进度&nbsp;<span class="frtwo_sp">'+datas[i].donatePercent +'</span></p>');
							html.push('</div><div class="frthree">'+datas[i].lastUpdateTime+'</div>')
							html.push('</div></div><div class="tor_btn">');
						    html.push('<ul><a href="/uCenterProject/showAccountNumber.do?projectId='+datas[i].id+'">');
							html.push('<li class="btn_li">善款提现</li>');
							html.push('</a><a href="javascript:;"  onclick="projectCommentBatch('+datas[i].id+')">');
							html.push('	<li class="btn_li">项目反馈</li>');
							html.push('</a><a href="javascript:;" onclick="projectManage('+datas[i].id+','+(datas[i].recommendedPerson==null?'null':datas[i].recommendedPerson)+','+datas[i].userId+',"'+datas[i].type+'")">');
							html.push('<li class="btn_li guanli">项目管理</li>');
							html.push('</a><a href="http://www.17xs.org/webhtml/view/h5/releaseProject/perfectData.html?projectId='+datas[i].id+'&recommendedPerson='+datas[i].recommendedPerson+'">');
							html.push('<li class="btn_li guanli">提交验证</li>');
							html.push('</a></ul></div></div>');
						}
						$('#add').append(html.join(''));
						var counts=$('input[name="count"]');
					    	if(total>counts.length){
					        	$("#more").html('<a href="javascript:;" class="jiazai">点击加载更多</a>');
					    	}else{
					        	$("#more").html('<a href="javascript:;" class="jiazai">没有更多数据了</a>');
					    	}
					}else {
						alert("没有更多数据了");
					}
				},
				error:function(){
				   alert("请求发生错误");
				}
			});
		});
    });
    //单击标题进入项目页面
    function clickTitle(projectId){
        var state=$("#state").val();
        var urla = window.baseurl+"uCenterProject/projectDetails.do";
        $.ajax({ 
			url:urla,
			type: 'POST',
			data:{projectId:projectId,state:state},
			cache:false,
			success: function(result){
				if(result.flag==1){
				    var pid = result.obj.id;
					location.href = "/newReleaseProject/project_detail.do?projectId="+pid;
					return true;
				}else{
					$("#msg").html('<p>'+result.errorMsg+'</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
			},
			error: function(){
				$("#msg").html('<p>网络异常,请联系客服</p>');
				$("#msg").show();
				setTimeout(function () {
		            $("#msg").hide();
		        }, 2000);
				return false;
			}
		});
    }
</script>
<body>
	<!--头部-->
	<div class="initator_head flex spb">
		<a href="/ucenter/userCenter_h5.do" class="left">用户中心</a>
		<div>我发起的项目</div>
		<a class="right" href="javascript:;" onclick="releaseProject()">
			<span>发布项目</span>
			<img src="/res/images/h5/images/userCenter/plus.png"/>
		</a>
	</div>
	<!--导航-->
	<div class="initator_nav flex">
		<a href="javascript:;" class="tor_a ${state==210?'ligg':''}" onclick="projectList(210,1)" id="li1">审核中</a>
		<a href="javascript:;" class="tor_a ${state==240?'ligg':''}" onclick="projectList(240,1)" id="li2">进行中</a>
		<a href="javascript:;" class="tor_a ${state==260?'ligg':''}" onclick="projectList(260,1)" id="li3">已结束</a>
		<a href="javascript:;" class="tor_a tor_lig ${state==230?'ligg':''}" onclick="projectList(230,1)" id="li4">审核未通过</a>
	</div
	<!--列表-->
	<div id="projectlist">
	<!--加载-->
		<c:if test="${list==null}">
			<p style="line-height: 3;color: #999;text-align: center;">暂无数据</p>
		</c:if>
		<c:forEach items="${list}" var="project" varStatus="status">
			<div class="initator_conter">
				<input type="hidden" name="count" />
				<div class="tor_top" onclick="clickTitle(${project.id })">
					<div class="torfl">
						<c:if test="${project.coverImageUrl!=null }">
							<img src="${project.coverImageUrl }" />
						</c:if>
					</div>
					<div class="torfr">
						<h5 class="frone">${project.title }</h5>
						<div class="frtwo">
							<p class="frtwo_fl">
								目标&nbsp;<span class="frtwo_sp">${project.cryMoney }</span>
							</p>
							<p class="frtwo_fr">
								进度&nbsp;<span class="frtwo_sp">${project.donatePercent }</span>
							</p>
						</div>
						<div class="frthree">
							<fmt:formatDate value="${project.lastUpdateTime}" type="both"
								pattern="yyyy-MM-dd" />
						</div>
					</div>
				</div>
				<div class="tor_btn">
					<ul>
						<a href="/uCenterProject/showAccountNumber.do?projectId=${project.id }">
							<li class="btn_li">善款提现</li>
						</a>
						<a href="javascript:;" onclick="projectCommentBatch(${project.id })">
							<li class="btn_li">项目反馈</li>
						</a>
						<a href="javascript:;" onclick="projectManage(${project.id },${empty project.recommendedPerson?'null':project.recommendedPerson },${project.userId },'${project.type }')">
							<li class="btn_li guanli">项目管理</li>
						</a>
						<a href="/webhtml/view/h5/releaseProject/perfectData.html?projectId=${project.id}&recommendedPerson=${project.recommendedPerson}">
							<li class="btn_li guanli">提交验证</li>
						</a>
					</ul>
				</div>
			</div>
		</c:forEach>
		<div id="add"></div>
	</div>
	<div id="more">
		<a href="javascript:;" class="jiazai">点击加载更多</a>
	</div>
	<!--弹层-->
	<div class="layer" style="display: none">
		<div class="mask"></div>
		<div class="layer_center">
			<li class="layer_li" style="color:#666">项目管理</li>
			<a href="javascript:;" onclick="edit()">
				<li class="layer_li">编辑项目</li>
			</a> <a href="javascript:;">
				<li class="layer_li" id="end" >提前结束</li>
			</a> <a href="javascript:;" onclick="withDrawDeposit()">
				<li class="layer_li">项目数据</li>
			</a> <a href="javascript:;" onclick="reprotForm()">
				<li class="layer_li">报名清单</li>
			</a> <a href="javascript:;">
				<li class="layer_li layer_qx">取消</li>
			</a>
		</div>
	</div>
	<!--确认提前结束-->
	<div class="moved_up" style="display: none">
		<div class="mask"></div>
		<div class="end_center">
			<div class="end_hfk">
				<span class="end_r">请输入提前结束的原因：</span>
				<textarea class="end_text" cols="45" rows="7" id="deadExplain" name="deadExplain"></textarea>
			</div>
			<div class="end_foot">
				<a href="javascript:;">
					<div class="end_fl fl">确认提前结束</div>
				</a> 
				<a href="javascript:;">
					<div class="end_fr fr">取消</div>
				</a>
			</div>
		</div>
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" name="projectId" id="projectId" value="${project.id }" />
	<input type="hidden" name="recommendedPerson" id="recommendedPerson" value="${project.recommendedPerson }" />
	<input type="hidden" name="userId" id="userId" value="${project.userId }" />
	<input type="hidden" name="type" id="type" value="${project.type }" />
	<input type="hidden" name="total" id="total" value="${total}" />
	<input type="hidden" name="tips" id="tips" value="${tips}" />
	<input type="hidden" name="state" id="state" value="${state}" />
</body>
</html>