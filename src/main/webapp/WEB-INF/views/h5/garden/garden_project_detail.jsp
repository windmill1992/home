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
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/h5New.css"/>
</head>
<body class="h5bg">
<div class="h5Detail"  id="h5Detail">
	<div class="pic">
    	<img src="${project.coverImageUrl}" alt="">
    </div>
    <div class="detcon">
    	<h2>${project.title}</h2>
        <p>${project.subTitle}</p>
        <div class="detData">
        	<span>可认捐<br><em id="leaveCopies">${project.leaveCopies}</em>份</span>
            <span>每份<br><em id="perMoney">${project.perMoney}</em>元</span>
            <span>已认捐<br><em>${project.totalCopies-project.leaveCopies}</em>份</span>
        </div>
    </div>
    <div class="detComm">
<!--        <img src="res/images/newIndex/h5/pci_03.jpg" alt="">-->
        ${project.content}
        <br/>
        <c:forEach items="${project.bfileList}" var="img">
			<p class="pic"><img src="${img.url}"  alt=""><i>${img.description}</i></p>
		</c:forEach>
    </div>
    <div class="h5tle">善园认捐记录</div>
  
    <div id = "donatList" >
     <!-- 
	    <div class="h5list">
	    	<h3>宁波某某公司<span>认捐<em>1000</em>元</span></h3>
	        <div class="listCon">
	        	<h4>“古牌坊”认捐<em>1</em>份</h4>
	            <p>祝宁波善园项目越办越好，祝宁波善园项目越办越好，祝宁波善园项目越办越好，祝宁波善园项目越办越好。</p>
	        </div>
	    </div>
	    <div class="h5list">
	    	<h3>宁波某某公司<span>认捐<em>1000</em>元</span></h3>
	        <div class="listCon">
	        	<h4>“古牌坊”认捐<em>1</em>份</h4>
	            <p>祝宁波善园项目越办越好，祝宁波善园项目越办越好，祝宁波善园项目越办越好，祝宁波善园项目越办越好。</p>
	        </div>
	    </div>
	    -->
    </div>
    

</div>
<div class="h5Bottom">
	<c:if test="${project.leaveCopies<1}">
		<a  title="" ><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束，感谢您的支持</a>
	</c:if>
	<c:if test="${project.leaveCopies>0}">
		<a title="" id="paySubmit">我要认捐</a>
	</c:if>
</div>

<div class="Purch" id="Purch" style="display:none">
	<div class="tle">请选择认捐份额<span class="close" id="closeBuy"></span></div>
    <div class="con">
    	<div class="selectTab">
        	<a href="javascript:void(0);" class="on" title=""><em>1</em>份</a>
            <a href="javascript:void(0);" title=""><em>2</em>份</a>
            <a href="javascript:void(0);" title=""><em>3</em>份</a>
            <a href="javascript:void(0);" title=""><em>4</em>份</a>
        </div>
        <div class="selectClick">
        	<a href="javascript:void(0);" class="reduce" id="reduce">-</a>
            <a href="javascript:void(0);" class="add" id="add">+</a>
            <input type="text" title="" placeholder="请输入其它份额" disabled id="buyNum">
        </div>
        <a href="javascript:void(0);" class="buyBtn" id="submitBuy">我要认捐</a>
        <div class="clause">
        	<em id="clauseOk" class="on"></em>同意并接受<a href="http://www.17xs.org/redPackets/getAgreement.do" title="">《善园基金会用户协议》</a>
        </div>
    </div>
</div>
<div class="Purch-shadow" ></div>
<input type="hidden" id="pprim" data="">
<input type="hidden" id="projectId" value="${projectId}">
<script language="javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
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

<script language="javascript">
$(function(){
	$('#buyNum').val($('.selectTab a.on').children('em').html());
	var buyNum=$('#buyNum').val();
	
	$('.selectTab a').click(function(){
		var v=Number($(this).children('em').html()),i=$('.selectTab a').index(this),html=$('#buyNum');
		$('.selectTab a').removeClass('on').eq(i).addClass('on');
		
		var leaveCopies = Number($("#leaveCopies").html());
		if(leaveCopies<v)
		{
			html.val(leaveCopies);	
		}else
		{
			html.val(v);
		}
	})
	
	$('#reduce').click(function(){
		var v=parseInt($('#buyNum').val()),vNew=v-1;
		if(v<=1){
			$('#reduce').addClass('cursor');
			$('#buyNum').val(1);
			$('#submitBuy').addClass('cursor');
		}else{
			$('#buyNum').val(vNew);
			$('#reduce').removeClass('cursor');
			$('#submitBuy').removeClass('cursor');
		}
	})
	$('#add').click(function(){
		var v=parseInt($('#buyNum').val());
		var buyV = v+1 ;
		var leaveCopies = Number($("#leaveCopies").html());
		if(leaveCopies<buyV)
		{
			$('#buyNum').val(leaveCopies);
		}
		else
		{
			$('#buyNum').val(buyV);
		}
		
		$('#reduce').removeClass('cursor');
		$('#submitBuy').removeClass('cursor');
	})
	$('#clauseOk').click(function(){
		var isOn=$('#clauseOk').hasClass('on');
		isOn?$('#clauseOk').removeClass('on'):$('#clauseOk').addClass('on')
	})
	$('#closeBuy').click(function(){
		$('#Purch').hide();
		$('.Purch-shadow').hide();
	})
	$("#paySubmit").click(function(){
		$("#Purch").show();
		$('.Purch-shadow').show();
	});
	
	$("body").on("mousedown",function(e){
		var target=$(e.target);
		if(!(target.closest("#Purch").length) && !(target.closest(self).length) && $("#Purch").is(":visible")){
			//$(".dowebok dl dd").css({"display":"none"});
			$('#Purch').hide();
			$('.Purch-shadow').hide();
			//$("body").removeClass('modal-open');
		}
	})

})
var base=window.baseurl;
var	WebAppUrl={
		HOSLIST:base+"project/gardendonationlist.do" //历史记录
	};
	var detail={
		hosData:'',
		totalpage:1,
		currpage:1,
		init:function(){
			detail.hosList(1);
			$('#submitBuy').click(function(){
				detail.buyproject();
			})	
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
					url:WebAppUrl.HOSLIST, 
					data:{id:id,page:page,pageNum:10,t:new Date().getTime(),id:$('#projectId').val()},
					success: function(result) { 
						if(result.flag){
							var datalist=result.obj.data,html=[];
							that.totalpage=result.obj.total,that.currpage=result.obj.page,that.hosData=datalist;
							for(var i=0;i<datalist.length;i++){
								html.push('<div class="h5list"><h3>'+datalist[i].name+'<span>认捐<em>'+datalist[i].dMoney+'</em>元</span></h3><div class="listCon"><h4>“'+datalist[i].title+'”认捐<em>'+datalist[i].copies+'</em>份</h4><p>'+datalist[i].leaveWord+'</p>');
								//html.push('<h3>宁波某某公司</h3>');
							   // html.push('<h3>'+datalist[i].name+'<span>认捐<em>'+datalist[i].dMoney+'</em>元</span></h3>');
							   // html.push('');
							    //html.push('<h4>“'+datalist[i].title+'”认捐<em>'+datalist[i].copies+'</em>份</h4>');
							    //html.push('<p>'+datalist[i].leaveWord+'</p>');
							    html.push('</div>');
							    html.push('</div>');
							}
							$("#donatList").html(html);
							
						}
						
					}
				});	
		}
		,buyproject:function(){
				var clauseOk,buyNum=parseInt($('#buyNum').val());
				clauseOk=$('#clauseOk').hasClass('on');
				if(!clauseOk){return false;}
				if(buyNum<1){return false;}
				var leaveCopies = Number($("#leaveCopies").html());
				var v=parseInt($('#buyNum').val());
				var projectId = $("#projectId").val();
				if(v>leaveCopies){
					alert("超过库存");
				}else{
					var perMoney = Number($("#perMoney").html());
					var money = v*perMoney;
					if(!isNaN(money)){
						
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&copies="+v;
					}else{
						alert("请输入数字");
						return;
					}	
				}
			
		}
	}
	detail.init();


</script>
</body>
</html>
