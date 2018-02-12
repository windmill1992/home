<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园 - 日捐月捐计划</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link type="text/css" rel="stylesheet" href="<%=resource_url%>res/css/dev/monthly_donateStyle.css"/>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/dev/month_donate.js"></script>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<!-- 主体 -->
<div class="bodyNew gdWherelist">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> >  日捐月捐计划
    </div>
    <div class="w1000">
        <div class="listBorder donate">
            <!--捐赠方式-->
            <form class="donate_day">
                <li class="donate_fl1"><p>捐赠方式：</p></li>
                <li class="donate_fl1"><input type="radio" name="type" id="type" class="donate_inp1" value="0" checked="checked">按天捐</li>
                <li class="donate_fl1"><input type="radio" name="type" id="type" class="donate_inp2" value="1">按月捐</li>
            </form>
            <!--捐赠项目-->
            <form class="donate_day">
                <li class="donate_fl1"><p>捐赠项目：</p></li>
                <li class="donate_fl1"><input type="radio" name="category" id="category" class="donate_inp3" value="" checked="checked">随机捐十个项目</li>
                <li class="donate_fl1"><input type="radio" name="category" id="category" class="donate_inp4" value="field">按指定类型的项目</li>
            </form>
            <!--捐赠项目类型-->
            <div class="donate_none1" style="display:none">
            <div class="donate_san"><img src="http://www.17xs.org/res/images/sanj.png"></div>
            <div class="donate_day3">
                <div class="click_fl"><p>捐赠方式：</p></div>
                <div class="click_fr">
                 	<!-- <ul class="mon_list"> -->
    					<c:forEach var="record" items="${atc}" varStatus="status">
							<c:if test="${record.typeName_e != 'good'}">
								<li class="donate_fl3"><input type="checkbox" class="click_li1" id="categorys" name="categorys" value="${record.typeName_e}">${record.typeName}</li>
							</c:if>
						</c:forEach>
    				<!-- </ul> -->
                   <!--  <li class="donate_fl3"><input type="checkbox" class="click_li1" id="categorys" name="categorys"> <span class="click_span1">医疗救助</span></li>
                    <li class="donate_fl3"><input type="checkbox" class="click_li2" id="categorys" name="categorys"> <span class="click_span2">灾害救助</span></li>
                    <li class="donate_fl3"><input type="checkbox" class="click_li3" id="categorys" name="categorys"> <span class="click_span3">公益众筹</span></li>
                    <li class="donate_fl3"><input type="checkbox" class="click_li4" id="categorys" name="categorys"> <span class="click_span4">特殊群体</span></li>
                    <li class="donate_fl3"><input type="checkbox" class="click_li5" id="categorys" name="categorys"> <span class="click_span5">贫困救助</span></li>
                    <li class="donate_fl3"><input type="checkbox" class="click_li6" id="categorys" name="categorys"> <span class="click_span6">教育救助</span></li> -->
                </div>
            </div>
            </div>
            <!--捐赠通知方式-->
            <form class="donate_day">
                <li class="donate_fl1"><p>通知方式：</p></li>
                <li class="donate_fl1"><input type="radio" name="notice" id="notice" class="donate_inp5" value="-1" checked="checked">不通知</li>
                <li class="donate_fl1"><input type="radio" name="notice" id="notice" class="donate_inp6" value="0">短信通知</li>
                <li class="donate_fl1"><input type="radio" name="notice" id="notice" class="donate_inp7" value="1">微信通知</li>
            </form>
            <!--捐赠通知类型-->
            <div class="donate_none2" style="display: none">
            <div class="donate_san1"><img src="<%=resource_url%>res/images/sanj.png"></div>
            <div class="donate_day4">
                <div class="click_fl"><p>请输入手机号码：</p></div>
                <div class="click_fr">
                    <input type="text" placeholder="" maxlength="11" class="click_inpsj" id="MobileNum" name="MobileNum" value="${MobileNum }" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">
                </div>
            </div>
            </div>

                <div class="click_fl1" onclick="donate_p4()"><p>每次捐赠金额：</p></div>
                <div class="click_fr1">
                    <input type="text" placeholder="" class="click_inpsj" id="money" name="money" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">
                    <span>元<div style="display:none;left: left" id="yue">(余额不足，请<a href="http://www.17xs.org/ucenter/core/chargeonlineBank.do" style="color:red">充值</a>)</div></span>
                </div>
            <!--捐赠协议-->
            <div class="foot_5">
                <span class="foot_span"><input type="checkbox" checked="false" id="agree"/></span>
                <a href="#"><p class="foot_p51">已阅读并同意<span class="foot_p52">《善园月捐计划》</span></p></a>
            </div>
            <!--日捐按钮-->
            <div>
                <a href="#"><div class="donate_foot" >
                <!-- <input type="submit" id="donate_p4" name="donate_p4" class="donate_p4" value="开始日捐" onclick="donate_p4()"> -->
                <em class="donate_p4">开始日捐</em>
                </div>
                </a>
            </div>
        </div>
    </div>
</div>
    <%--弹框--%>
    <div class="tank" style="display:none" id="tank">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close0"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="ok">感谢您对公益事业的支持，日捐（月捐）已开通</span>
    </div>
    <div class="Btns">
    <a id="frameBtn0" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
    
    <div class="tank" style="display:none" id="tank1">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close1"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="error">你还没有选择捐赠项目！</span>
    </div>
    <div class="Btns">
    <a id="frameBtn1" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
    
    <div class="tank" style="display:none" id="tank2">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close2"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="error">请填写手机号码！</span>
    </div>
    <div class="Btns">
    <a id="frameBtn2" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
    
    <div class="tank" style="display:none" id="tank3">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close3"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="error">手机号不足11位，请填写正确的手机号！</span>
    </div>
    <div class="Btns">
    <a id="frameBtn3" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
    
    <div class="tank" style="display:none" id="tank4">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close4"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="error">请填写捐助金额！</span>
    </div>
    <div class="Btns">
    <a id="frameBtn4" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
    
    <div class="tank" style="display:none" id="tank5">
    <div class="cp2yLock" id="cp2yLock0" style=" width:100%; height:1250px; display: block;"></div>
    <div class="cp2yDialogBox cp2yDialogBox1 AlertDlg" data="0" id="cp2yDialogBox0" style="display: block; top: 369.5px; left: 349.5px;">
    <div class="dialogTitle">
    <i class="l"></i><span>提示信息</span>
    <a class="closeDialog" data="0" id="close5"></a><i class="r"></i>
    </div>
    <div class="dialogContent">
    <div class="alertCon">
    <span class="error">捐助金额不能小于0.1元！</span>
    </div>
    <div class="Btns">
    <a id="frameBtn5" class="closeDialog sureBtn" data="0">确定</a>
    </div>
    </div>
    </div>
    </div>
<!-- 主体结束 -->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->

<script data-main="<%=resource_url%>res/js/dev/launchDonate.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
<script type="text/javascript">
	/*  $('#frameBtn0').click(function(){
		$('.tank').hide();
	window.location.href='http://www.17xs.org/project/launchDonateView.do';
	})

	$('.donate_p4').click(function(){
		$("input").each(function(){
                    if($(this).val() == $(this).attr("data-placeholder")){
                        $(this).css("color","red");
                        return false;
                    }
                });
                //window.sitePage="p-monthDonate";
                if($('#type').val()!=$('#type').attr("data-placeholder")&&$('#category').val()!=$('#category').attr("data-placeholder")&&
                $('#notice').val()!=$('#notice').attr("data-placeholder")&&$('#MobileNum').val()!=$('#MobileNum').attr("data-placeholder")&&
                $('#money').val()!=$('#money').attr("data-placeholder")&&$('#agree').val()!=$('#agree').attr("data-placeholder")&&
                $('#categorys').val()!=$('#categorys').attr("data-placeholder")){
                	
                	var type=$("input[name='type']:checked").val(),category=$("input[name='category']:checked").val(),notice =$("input[name='notice']:checked").val(),
                	MobileNum = $('#MobileNum').val(),categorys="",
					money = $('#money').val();
					var obj=document.getElementsByName('categorys');
					for(var i=0; i<obj.length; i++){
						if(obj[i].checked) categorys+=obj[i].value+','; //如果选中，将value添加到变量s中
					} 
					if(categorys==''&&category=='field'){
						alert('你还没有选择任何内容！');
						return false; 
					}
					
	               	$.ajax({
					url: 'http://www.17xs.org/project/launchDonate.do',
					dataType: 'json',
					type: 'post',
					data: {
						type:type,
						category:categorys,
						notice:notice,
						categorys:categorys,
						MobileNum:MobileNum,
						money:money,
						state:200
					},
					success: function(result) {
    		   			if(result.flag==1){
    		   				$('.tank').show();
    					}
    		  			 if(result.flag==0){
    			   			$('#yue').show();
    		   			}
    				},
    				error: function(){ 
    					return false;
    					}	
					});
              }
	}) */
</script>