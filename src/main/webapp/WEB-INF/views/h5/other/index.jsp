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
<title>善园网—一起行善，互联网公益平台</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home.css"/>
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
	
</style>
</head> 

<body>
<div id="pageContainer">
	<div class="home">
  		<div class="site_info clearfix">
  			<span class="logo">
  				<img src="<%=resource_url%>res/images/h5/images/logonew.png">
  			</span>
  			<a href="" class="user"></a>
  		</div>
    	<!--焦点图11111//-->
     	<!--scroll-->
    	<div class="scroll_box" id="scroll_img">
       		<ul class="scroll_wrap">
        	<c:forEach items="${bannerList}" var="img">
       			<li><a href="${img.linkUrl}"><img src="${img.url}" width="100%" /></a></li>
    		</c:forEach>
       		</ul>
    	</div>
	  	<!--scroll-->
    	<!--分类//--> 
    	<!-- 
	    	<div id="category" class="category">
	        	<div class="scroll_wrap">
	        	<c:if test="${size  > 6}">
	        		<c:forEach begin="0" end="${size}" varStatus="status" step="6">
	        		<ul class="categoryList clearfix">
		        		<c:forEach var="record" items="${atc}" begin="${status.index }" end="${status.index+5 }" varStatus="status">
		        		<c:if test="${record.typeName_e != 'good'}">
		        		<li>
		        			<a href="http://www.17xs.org/h5/list/?field=${status.index+1}">
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
		        			<a href="http://www.17xs.org/h5/list/?field=${status.index+1}">
		        				<img src="${record.coverImageUrl}">
		        				<span>${record.typeName}</span>
		        			</a>
		        		</li></c:if>
		        		</c:forEach>
		        	</ul></c:if>
		        </div>
	        	<ul class="scroll_position" id="scroll_position">
		    		<li class="on"></li>
		    		<li></li>
		    	</ul>
	    	</div>
	    	 -->
    	<!--推荐主题//-->
    	<div class="recommend">
      		<div class="hot" id= "hot"> 
        	</div>
    	</div>
  
  			<!--主题列表//-->
        <div class="list"  id ="list">
	      	<div class="list-box"></div>
	      	<div class="load-more">
				<a class="aload" href = "http://www.17xs.org/h5/list/">查看更多 >></a>
			</div>	
        </div>
    </div>
</div>

<script data-main="<%=resource_url%>res/js/h5/index.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>/res/js/jquery-1.8.2.min.js"></script>
<script src='<%=resource_url%>res/js/h5/hhSwipe.js'></script>
<script type="text/javascript">
var base=window.baseurl;
var i = 1;
var dataUrl={
		projectList:base+'project/otherlist_h5.do',
		recommendList:base+'project/recommendList_h5.do',//推荐项目
		latestdonationlist:base+'project/latestdonationlist.do'
	};
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
				data:{typeName:null,sortType:0,status:0,page:pageNum,len:pageSize,t:new Date().getTime()},
				success: function(result){
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length;
						var html=[];
						for(var i=0;i<len;i++){
						if(datas[i].field == 'garden'){
							html.push('<a href="http://nb.17xs.org/project/gardenview_h5.do?projectId='+datas[i].itemId+'">');
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
							html.push('<a href="http://nb.17xs.org/project/gardenview_h5.do?projectId='+datas[i].itemId+'">');
						}else{
							html.push('<a href="http://nb.17xs.org/project/otherview_h5.do?projectId='+datas[i].itemId+'">');
						}
						html.push('<div class="li"><div class="ctx">');
						if(datas[i].imageurl != null ){
							html.push('<img src="'+datas[i].imageurl+'" class="img">');
						}else{
							html.push('<img src="'+base+'res/images/logo-def.jpg" class="img">');
						}
						html.push('<p class="pp">'+datas[i].title+'</p>');
						if(datas[i].state == 240){
							html.push('<p class="ds">已募集<span>'+ datas[i].donaAmount +'</span>元</p>');
						}else{
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

</body>
</html>