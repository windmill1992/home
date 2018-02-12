<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
<meta name="viewport"/>
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home_xin.css"/>
<script type='text/javascript'>window.BWEUM||(BWEUM={});BWEUM.info = {"stand":true,"agentType":"browser","agent":"bi-collector.oneapm.com/static/js/bw-send-411.4.5.js","beaconUrl":"bi-collector.oneapm.com/beacon","licenseKey":"I66F7~vv2ai6V3Nn","applicationID":2282681};</script>
<script type="text/javascript" src="//bi-collector.oneapm.com/static/js/bw-loader-411.4.5.js"></script>
<style type="text/css">
#wrapper {
	position: absolute; 
	z-index: 1;
	top: 0px;
	bottom: 0;
	left: 0;
	width: 100%;
	overflow: auto;
}

#scroller {
	position: relative;
	-webkit-tap-highlight-color: rgba(0, 0, 0, 0);
	float: left;
	width: 100%;
	padding: 0;
}

#pullDown, #pullUp {
	font-weight: bold;
	font-size: 12px;
	color: #666;
	text-align: center;
}
</style>
<style type="text/css">
	.load-more {
		text-align: center;
		width: 100%;
		height: 30px;
		line-height: 30px;
		font-size: 12px;
		color: #666;
		border-radius: 4px;
		background:#fff;
	}
	
	.aload {
		text-decoration: none;
		display: block;
	}
	@media only screen and (min-width: 320px) and (max-width: 640px) {
		.scroll_box{max-height: 100px;}
	}
	/*#total{width:500px; height:40px; line-height:40px; margin:80px auto 20px auto; font-size:20px}*/
	#total .num{ display:inline-block; line-height:13px; margin:-3px 4px 0 4px;vertical-align: middle;}
	#total .num i{
		width:14px;height:17px; display:inline-block;border: 1px solid #ccc;margin-left: 1px;
		background: url(<%=resource_url%>res/images/h5/images/number.png) no-repeat; background-position:0 0; text-indent:-999em;
		background-size: 14px;
	}
	.site_info .ucen{float: right;display: inline-block;line-height: 55px;margin-right: 5px;}
</style>
</head> 

<body>
<div id="pageContainer">
<!--	<div id="wrapper">-->
<!--		<div id="scroller">-->
<!--		    <div id="pullDown">-->
<!--		        <span class="pullDownIcon"></span><span class="pullDownLabel">下拉刷新</span>-->
<!--		    </div>-->
			<div class="home">
		  		<div class="site_info">
		  			<span class="logo">
		  				<img src="<%=resource_url%>res/images/h5/images/logo-lg.jpg" width="110%">
		  			</span>
		  			<!--<span class="txt">捐助<label id="lab1"><fmt:formatNumber type="number" pattern="####,###,###" value = "${totalMoney}"/></label>元</span>-->
		  			<a href="<%=resource_url%>ucenter/userCenter_h5.do" class="ucen"><img src="<%=resource_url%>res/images/h5/images/usercen.png" width="20" alt="用户中心"/></a>
		  			<span class="txt" id="total">捐助<span class="num"></span>元</span>
		  		</div>
		  		<div style="clear: both;"></div>
		    	<!--焦点图//-->
		     	<!--scroll-->
			  	<div class="scroll relative">
			    	<div class="scroll_box" id="scroll_img">
		        		<ul class="scroll_wrap">
			        	<c:forEach items="${bannerList}" var="img">
			       			<li><a href="${img.linkUrl}"><img src="${img.url}" width="100%" /></a></li>
			    		</c:forEach>
		        		</ul>
			    	</div>
		      		<%--<ul class="scroll_position" id='scroll_position'>--%>
				    	<%--<c:forEach items="${bannerList}" varStatus="status">--%>
			       			<%--<c:if test="${status.index == 0}"><li class="on"><a href="javascript:void(0);">${status.index+1}</a></li></c:if>--%>
			       			<%--<c:if test="${status.index != 0}"><li><a href="javascript:void(0);">${status.index+1}</a></li></c:if>--%>
				    	<%--</c:forEach>--%>
		      		<%--</ul>--%>
			  	</div>
			  	<!--scroll-->
		    	<!--分类//-->
	<div class="prsee_xin">
	<ul>
	<li class="pxin_l"><a href="http://www.17xs.org/project/batch_list.do?extensionPeople=${extensionPeople }" class="pxin_a"><span class="pxin_s pxin_s1"></span><p class="pxin_p1">批量捐</p></a></li>
	<li class="pxin_l"><a href="<%=resource_url%>user/getDonateTime.do" class="pxin_a"><span class="pxin_s pxin_s2"></span><p class="pxin_p2">月捐</p></a><li>

	<li class="pxin_l"> <a href="http://www.17xs.org/ucenter/myDonate.do" class="pxin_a"><span class="pxin_s pxin_s3"></span><p class="pxin_p3">捐助记录</p></a><li>
	<li class="pxin_l pxin"><a href="http://www.17xs.org/project/releaseH5Project.do" class="pxin_a"><span class="pxin_s pxin_s4"></span><p class="pxin_p4">发起求助</p></a><li>
	</ul>

	</div>
		    	<!--推荐主题//-->
		    	<div class="recommend">
		      		<div class="hot" id= "hot"> 
		        	</div>
		    	</div>
    
    			<!--主题列表//-->
		        <div class="list" id ="list">
					      	<div class="list-box"></div>
					      	<div class="load-more">
								<a class="aload" href = "http://www.17xs.org/h5/list/?extensionPeople=${extensionPeople }">点击加载更多</a>
							</div>
							
		        </div>
<!--		        <div id="pullUp">-->
<!--		          <span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多</span>-->
<!--		        </div>-->
		    </div>
<!--		</div>-->
<!--	</div>-->
</div>

	<div id="category" class="category">
	<div class="scroll_wrap">
	<c:if test="${size  > 6}">
		<c:forEach begin="0" end="${size}" varStatus="status" step="6">
			<ul class="categoryList clearfix">
			<c:forEach var="record" items="${atc}" begin="${status.index }" end="${status.index+5 }" varStatus="status">
				<c:if test="${record.typeName_e != 'good'}">
					<li>
					<a href="http://www.17xs.org/h5/list/?field=${status.index+1}&extensionPeople=${extensionPeople }">
					<img src="${record.coverImageUrl}">
					<span>${record.typeName}</span>
					</a>
					</li></c:if>
			</c:forEach>
			</ul>
		</c:forEach></c:if>
	<c:if test="${size  <= 6}">
		<ul class="categoryList clearfix">
		<c:forEach var="record" items="${atc}" varStatus="status">
			<c:if test="${record.typeName_e != 'good'}">
				<li>
				<a href="http://www.17xs.org/h5/list/?field=${status.index+1}&extensionPeople=${extensionPeople }">
				<img src="${record.coverImageUrl}">
				<span>${record.typeName}</span>
				</a>
				</li></c:if>
		</c:forEach>
		</ul></c:if>
	</div>
	</div>
<div class="footer_fk">
	<div class="footer_t">
	<a href="http://www.17xs.org?flag=pc">电脑版&nbsp;|&nbsp;</a>
	<a href="http://www.17xs.org/user/userAdvice.do">用户反馈</a>
	</div>
	<div class="footer_f"><p>Copyright ©宁波市善园公益基金会 版权所有 浙ICP备15018913号-1</p></div>
</div>
<input type="hidden" id="extensionPeople" value="${extensionPeople }">
<input type="hidden" name="cur_num" id="cur_num" value="${totalMoney}" />
<script src="<%=resource_url%>res/js/h5/iscroll.js"></script>
<script data-main="<%=resource_url%>res/js/h5/index.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>/res/js/jquery-1.8.2.min.js"></script>
<script src='<%=resource_url%>res/js/h5/hhSwipe.js'></script>
<script src="<%=resource_url%>res/js/h5/number.js"></script>
<script type="text/javascript">
var base=window.baseurl;
var i = 1;
var dataUrl={
		projectList:base+'project/list_h5.do',
		recommendList:base+'project/recommendList_h5.do',//推荐项目
		latestdonationlist:base+'project/latestdonationlist.do'
	};
var extensionPeople = $("#extensionPeople").val();
var page=1;
var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			that.projectList(1,10);
			that.recommendList(1, 2);
		},
		projectList:function(pageNum,pageSize){
			$.ajax({
				url:dataUrl.projectList,
				data:{typeName:null,sortType:2,status:0,page:pageNum,len:pageSize,t:new Date().getTime()},
				success: function(result){
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length;
						var html=[];
						for(var i=0;i<len;i++){
						if(datas[i].field == 'garden'){
							html.push('<a href="http://www.17xs.org/project/gardenview_h5.do?projectId='+datas[i].itemId+'&extensionPeople='+extensionPeople+'">');
						}else{
							html.push('<a href="http://www.17xs.org/project/view_h5.do?projectId='+datas[i].itemId+'&extensionPeople='+extensionPeople+'">');
						}
						html.push('<div class="item bg2">');
						if(datas[i].imageurl != null ){
							html.push('<img class="fl" src="'+datas[i].imageurl+'">');
						}else{
							html.push('<img class="fl" src="'+base+'res/images/logo-def.jpg">');
						}
						if(datas[i].donaAmount >= datas[i].cryMoney){
							html.push('<i class="corner"><img src="'+base+'res/images/h5/images/corner_success.png"></i>');
						}else if(datas[i].state == 260 && (datas[i].special_fund_id==0 || datas[i].special_fund_id==null)){
							html.push('<i class="corner"><img src="'+base+'res/images/h5/images/corner_finish.png"></i>');
						}
						html.push('<div class="t"><div class="tn"></div>');
						html.push('<div class="tt">'+datas[i].title+'</div>');
						if(datas[i].special_fund_id==0 || datas[i].special_fund_id==null){
							html.push('<div class="tp"><label>目标<span class="r">'+datas[i].cryMoney+'</span>元</label>');
							html.push('<label>已完成<span class="r">'+datas[i].process+'%</span></label></div>');
						}
						html.push('</div><div class="clear"></div></div></a>');
						}
						$('.list-box').append(html.join(''));		
					}else {
						//$('.pullUpLabel').html('亲，没有更多内容了！');
						$(".load-more").html("没有更多数据了");
					}
				}
			});
		},
		recommendList:function(page,pageSize){
			$.ajax({
				url:dataUrl.recommendList,
				data:{page:page,len:pageSize,t:new Date().getTime()},
				success: function(result){
					if(result.result==0){
						var data=result.items,datas=data,len=data.length;
						var html=[];
						for(var i=0;i<len;i++){
						if(datas[i].field == 'garden'){
							html.push('<a href="http://www.17xs.org/project/gardenview_h5.do?projectId='+datas[i].itemId+'&extensionPeople='+extensionPeople+'">');
						}else{
							html.push('<a href="http://www.17xs.org/project/view_h5.do?projectId='+datas[i].itemId+'&extensionPeople='+extensionPeople+'">');
						}
						html.push('<div class="li"><div class="ctx">');
						if(datas[i].imageurl != null ){
							html.push('<img src="'+datas[i].imageurl+'" class="img">');
						}else{
							html.push('<img src="'+base+'res/images/logo-def.jpg" class="img">');
						}
						html.push('<p class="pp">'+datas[i].title+'</p>');
						if(datas[i].state == 240 && (datas[i].special_fund_id==0 || datas[i].special_fund_id==null)){
							html.push('<p class="ds">已募集<span>'+datas[i].donaAmount+'</span>元</p>');
						}else if(datas[i].state == 260 && (datas[i].special_fund_id==0 || datas[i].special_fund_id==null)){
							html.push('<p class="ds">已结束</p>');
						}
						html.push('<div style="clear:both;"></div>');
						html.push('</div></div></a>');
						}
						$('#hot').html(html.join(''));		
					}
				}
			});
			
		},
	};
	 
	h5detail.init();
	
	$(function(){
		setInterval(
			function(){
				$.ajax({
					url:base+'index/loadTotalMoney.do',
					data:{},
					type:'GET',
					success:function(res){
						$("#cur_num").val(res);
					},
					error:function(e){
					}
				});
			}
		,3000);
		var num = $("#cur_num").val();
		show_num(num);
		setInterval(function(){num = $("#cur_num").val();show_num(num);},3000);
	
		function show_num(n) {
			var it = $(".num i");
			var len = String(n).length;
			for(var i = 0; i < len; i++) {
				if(it.length <= i) {
					$(".num").append("<i></i>");
				}
				var num = String(n).charAt(i);
				var y = -parseInt(num) * 16.5 + 0;
				var obj = $(".num i").eq(i);
				obj.animate({
					backgroundPosition: '(0 ' + String(y) + 'px)'
				}, 1400, 'swing', function() {});
			}
			/*for(var i = 0; i < len; i++) {
				if(i % 3 == 0) {
					$(".num i").eq(len - i).before(",");
				}
			}*/
		}
	});
/*
	$(".aload").live("click",function(){
		page++;
		h5detail.projectList(page,10);
	});
	*/
	/*
	  var myScroll,
	      pullDownEl, pullDownOffset,
	      pullUpEl, pullUpOffset,
	      generatedCount = 0;
	  function pullDownAction () { 
	    setTimeout(function () { 
	      myScroll.refresh();     //数据加载完成后，调用界面更新方法 
	    }, 1000);  
	    i=1;
	  }
	  function pullUpAction () { //上拉加载更多
		  setTimeout(function(){
		    	h5detail.projectList(++i,10);
		        myScroll.refresh();
		  },200);
	    }
	    function loaded() {
	      pullDownEl = document.getElementById('pullDown');
	      pullDownOffset = pullDownEl.offsetHeight;
	      pullUpEl = document.getElementById('pullUp'); 
	      pullUpOffset = pullUpEl.offsetHeight;
	        
	      myScroll = new iScroll('wrapper', {
	        useTransition: true,
	        topOffset: pullDownOffset,
	        onRefresh: function () {
	        if (pullDownEl.className.match('loading')) {
	          pullDownEl.className = '';
	          pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新';
	        } else if (pullUpEl.className.match('loading')) {
	          pullUpEl.className = '';
	          pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';
	        }
	      },
	      onScrollMove: function () {
	        if (this.y > 5 && !pullDownEl.className.match('flip')) {
	          pullDownEl.className = 'flip';
	          pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放立即刷新';
	          this.minScrollY = 0;
	        } else if (this.y < 5 && pullDownEl.className.match('flip')) {
	          pullDownEl.className = '';
	          pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新';
	          this.minScrollY = -pullDownOffset;
	        } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
	          pullUpEl.className = 'flip';
	          pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放立即加载';
	          this.maxScrollY = this.maxScrollY;
	        } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
	          pullUpEl.className = '';
	          pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';
	          this.maxScrollY = pullUpOffset;
	        }
	      },
	      onScrollEnd: function () {
	        if (pullDownEl.className.match('flip')) {
	          pullDownEl.className = 'loading';
	          pullDownEl.querySelector('.pullDownLabel').innerHTML = '刷新中...';        
	          pullDownAction();
	        } else if (pullUpEl.className.match('flip')) {
	          pullUpEl.className = 'loading';
	          pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';        
	          pullUpAction();
	        }
	      }
	    });
	    setTimeout(function () { 
	      document.getElementById('wrapper').style.left = '0'; }, 800);
	    }
	    
	    document.addEventListener('touchmove', function(e) {
	      e.preventDefault();
	    }, false);
	    document.addEventListener('DOMContentLoaded', function() {
	      setTimeout(loaded, 200);
	    }, false);
	    */
</script>
<%@ include file="cs.jsp" %>
<%CS cs = new CS(1257726653);cs.setHttpServlet(request,response);
String imgurl = cs.trackPageView();%> 
</body>
</html>