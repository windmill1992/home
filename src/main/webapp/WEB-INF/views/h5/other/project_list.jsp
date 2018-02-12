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
<title>我要行善</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/list.css"/>
<style type="text/css">
	#wrapper {
		position: absolute; 
		z-index: 1;
		top: 87px;
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
</head> 

<body>
<div id="pageContainer">
	 <div class="project_list">
	  	<!--标题//-->
	    <div class="top">
	    	<a href="javascript:history.go(-1)" class="back"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAABBJREFUeNpi/P//PwNAgAEACQEC/2m8kPAAAAAASUVORK5CYII=" class="back_img back_icon_6"></a>
	    	<div class="title" id= "title">求医</div>
	     </div> 
	   	<!--主题筛选//-->
	    <div class="filter">
	    	<div class="hd">
	        	<a href="javascript:void(0);" class="item1">全部分类</a>
	            <a href="javascript:void(0);" class="item2">最新发布</a>
	            <a href="javascript:void(0);" class="item3">全部状态</a>
	        </div>        
	        <div class="bd" id="sub_1" style="display:none;">
	        	<ul>
	            	<li><a href="javascript:void(0);" data="0" class="cur">全部</a></li>
	            	<c:forEach var="record" items="${atc}" varStatus="status">
	            	<c:if test="${record.typeName_e != 'good'}">
	            	<li><a href="javascript:void(0);" data="${record.typeName_e}">${record.typeName}</a></li>
	                </c:if></c:forEach>
	            </ul>
	        </div>
	        <div class="bd" id="sub_2" style="display:none;">
	        	<ul>
	            	<li><a href="javascript:void(0);" data="0" class="cur">最新发布</a></li>
	                <li><a href="javascript:void(0);" data="1">关注最多</a></li>
	                <li><a href="javascript:void(0);" data="4">捐助最多</a></li>
	                <li><a href="javascript:void(0);" data="3">最新反馈</a></li>
	            </ul>
	        </div>        
	        <div class="bd" id="sub_3" style="display:none;">
	        	<ul>
	            	<li><a href="javascript:void(0);" data="0" class="cur">全部项目</a></li>
	                <li><a href="javascript:void(0);" data="1">发布中</a></li>
	                <li><a href="javascript:void(0);" data="2">已完成</a></li>
	            </ul>
	        </div>
	        <div class="ft" style="display:none;"><a href="#" class="cancel">取消</a></div>
	        <div class="clear"></div>
	    </div>
	    
	    <!--主题列表//-->
	    <div id="wrapper">
			<div id="scroller">
			    <div id="pullDown">
			        <span class="pullDownIcon"></span><span class="pullDownLabel">下拉刷新</span>
			    </div>
	    		<div class="list" id="project_list" >
	      			<!--加载中//-->
	      			<!--页脚//-->
	  			</div>
	  			<div id="pullUp">
		            <span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多</span>
		        </div>
	  		</div>
	  	</div>
	</div>
	<%-- <div class="footer">
		<span>© 2015 杭州智善  版权所有</span>
		<img src="<%=resource_url%>res/images/h5/images/min-logo.jpg">
	</div> --%>
</div>
<input type="hidden" id="field" value="${field}">
<script src="<%=resource_url%>res/js/h5/iscroll.js"></script>
<script data-main="<%=resource_url%>res/js/h5/project_list.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>/res/js/jquery-1.8.2.min.js"></script>

<script type="text/javascript">
	var height=$(".filter").outerHeight(true);
	var base=window.baseurl;
	var i = 1;
	var dataUrl={
		projectList:base+'/project/otherlist_h5.do',
		latestdonationlist:base+'/project/latestdonationlist.do'
	};
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			var field = $('#field').val();
			if(field){
				$('#sub_1 a').removeClass('cur').eq(field).addClass('cur');
				$('.item1').html($('#sub_1 a').eq(field).html());
			}
			that.projectList(1,9);
			$('.item1').click(function(){
				$('#sub_1').show();
				$('#sub_2').hide();
				$('#sub_3').hide();
				height=$(".filter").outerHeight(true)+45;
				$("#wrapper").css("top",height);
			});
			$('.item2').click(function(){
				$('#sub_2').show();
				$('#sub_1').hide();
				$('#sub_3').hide();
				height=$(".filter").outerHeight(true)+45;
				$("#wrapper").css("top",height);
			});
			$('.item3').click(function(){
				$('#sub_3').show();
				$('#sub_1').hide();
				$('#sub_2').hide();
				height=$(".filter").outerHeight(true)+45;
				$("#wrapper").css("top",height);
			});
			$('#sub_1 a').click(function(){
				var i=$('#sub_1 a').index(this);
				$('#sub_1 a').removeClass('cur').eq(i).addClass('cur');
				$('#sub_1').hide();
				$('.item1').html($('#sub_1 a').eq(i).html());
				$("#project_list").html("");
				that.projectList(1,9);
				$("#wrapper").css("top","87px");
			});
			$('#sub_2 a').click(function(){
				var i=$('#sub_2 a').index(this);
				$('#sub_2 a').removeClass('cur').eq(i).addClass('cur');
				$('#sub_2').hide();
				$('.item2').html($('#sub_2 a').eq(i).html());
				$("#project_list").html("");
				that.projectList(1,9);
				$("#wrapper").css("top","87px");
			});
			$('#sub_3 a').click(function(){
				var i=$('#sub_3 a').index(this);
				$('#sub_3 a').removeClass('cur').eq(i).addClass('cur');
				$('#sub_3').hide();
				$('.item3').html($('#sub_3 a').eq(i).html());
				$("#project_list").html("");
				that.projectList(1,9);
				$("#wrapper").css("top","87px");
			});
		},
		projectList:function(pageNum,pageSize){
			var sub1 = $('#sub_1 a').index($('#sub_1 a.cur')),
				sub2 = $('#sub_2 a').index($('#sub_2 a.cur')),
				sub3 = $('#sub_3 a').index($('#sub_3 a.cur'));
			var field = $('#sub_1 a').eq(sub1).attr("data"),
				orderby = $('#sub_2 a').eq(sub2).attr("data"),
				state = $('#sub_3 a').eq(sub3).attr("data"),
				fieldName = $('#sub_1 a').eq(sub1).html();
			if(field == "0"){
				field=null;
			}
			$('#title').html(fieldName);
			$.ajax({
				url:dataUrl.projectList,
				data:{typeName:field,sortType:orderby,status:state,page:pageNum,len:pageSize,t:new Date().getTime()},
				success: function(result){
					var html=[];
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length;
						for(var i=0;i<len;i++){
						if(datas[i].field == 'garden'){
							html.push('<a href="http://nb.17xs.org/project/othergardenview_h5.do?projectId='+datas[i].itemId+'">');
						}else{
							html.push('<a href="http://nb.17xs.org/project/otherview_h5.do?projectId='+datas[i].itemId+'">');
						}
						html.push('<div class="item bg2">');
						if(datas[i].imageurl != null ){
							html.push('<img class="fl" src="'+datas[i].imageurl+'">');
						}else{
							html.push('<img class="fl" src="'+base+'res/images/logo-def.jpg">');
						}
						if(datas[i].donaAmount >= datas[i].cryMoney){
							html.push('<i class="corner"><img src="'+base+'res/images/h5/images/corner_success.png"></i>');
						}else if(datas[i].state == 260){
							html.push('<i class="corner"><img src="'+base+'res/images/h5/images/corner_finish.png"></i>');
						} 
						html.push('<div class="t"><div class="tn"></div>');
						html.push('<div class="tt">'+datas[i].title+'</div>');
						html.push('<div class="tp"><label>目标<span class="r">'+datas[i].cryMoney+'</span>元</label>');
						html.push('<label>已完成<span class="r">'+datas[i].process+'%</span></label></div>');
						html.push('</div><div class="clear"></div></div></a>');
						}
						$('#project_list').append(html.join(''));		
					}else {
						$('.pullUpLabel').html('亲，没有更多内容了！');
					}
				}
			});
			
		},
	};
	 
	h5detail.init();
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
		 h5detail.projectList(++i,9);
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
	
</script>

</body>
</html>