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
<title>用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/center.css"/>
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
</head>

<body>
<div id="pageContainer">
	<div id="wrapper">
		<div id="scroller">
		    <div id="pullDown">
		        <span class="pullDownIcon"></span><span class="pullDownLabel">下拉刷新</span>
		    </div>
  <div class="user_center">
  	<!--个人信息卡//-->
    <div class="profile">
    	<div class="info">
        	<p class="avatar"><img src="${user.coverImageUrl}"></p>
            <p class="nickname">${user.userName}</p>
      </div>
        <ul class="data_tab">
        	<a href="#" class="cur"><li><i>我的求助</i><b id="needHelpNum"></b></li></a>
           <a href="#" id="myDonate"> <li><i>我的捐赠</i><b>￥${user.totalAmount}</b></li></a>
            <a href="#" id="careProject"><li class="last"><i>最新动态</i><b>${careCount}</b></li></a>
        </ul>
        <div class="clear"></div>
    </div>
    <!--我的求助-->
    <div class="list" id="list_1">    
   		
    </div>
      
      <!--最新动态//-->
      <div class="list status" id="list_3" style="display:none;margin-top:5px;">

    </div>
      
      <!----我的捐赠--//-->
      <div class="list donate_list" id="list_2" style="display:none;">

      </div>
          <div id="pullUp">
		          <span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多</span>
		        </div>
      <!--加载中//-->
<!--      <div class="loading">加载中…</div>-->
      <!--页脚//-->
      <div class="footer">
      	<span>© 2015 善园基金会  版权所有</span>
         <img src="<%=resource_url%>res/images/h5/images/min-logo.jpg"></div>
 			 </div>
  		</div>
	</div>
</div>
</body>
<script data-main="<%=resource_url%>res/js/h5/needhelplist_new.js?v=201510131042" src="<%=resource_url%>res/js/require.min.js"></script>
<script src="<%=resource_url%>res/js/h5/iscroll.js"></script>
<script type="text/javascript" src="<%=resource_url%>/res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	var base=window.baseurl;
	var i = 1;
	var item = 1 ; // 1 : 我的求救  2 ： 我的捐赠  3： 最新动态
	var dataUrl={
		dataList:base+'project/appeallist_h5.do',//我的求助列表
		projectUrl:base+'project/view_h5.do',
		projectFeedbackUrl:base+'project/view_feedback_h5.do',
		careList:base+"project/careProjectList.do",//关注项目
		myDonateList:base+"project/donationlist_h5.do",
		publish:base+"ucenter/coreHelp/publish_h5.do",
	
	};
	var	needhelplist={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			that.dataList(1,4);
			$('#careProject').click(function(){
				item = 3 ;
				i = 1;
				$('#list_3').empty();
				that.feedbackList(1);
			});
			$('#myDonate').click(function(){
				item = 2 ;
				i = 1;
				$('#list_2').empty();
				that.myDonateList(1);
			});
			
			
		},

		dataList:function(page,num){
			var that=this,html=[],flag=false;
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.dataList,
				data:{page:page,pageNum:num,t:new Date()},
				success: function(result){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length;
						len=len>pageNum?pageNum:len; 
						$('#needHelpNum').html(len);
						
					if(page<=data.total){
							if(data.total>0){
									for(var i=0;i<len;i++)
									{
										html.push('<div class="item bg2">');
									    html.push('<a href="'+dataUrl.projectUrl+'?projectId='+datas[i].id+'">');
												if(datas[i].status == 260)
												{
													html.push('<div class="item_bd finish">');
												}else if(datas[i].donatePercent==100)
												{
													html.push('<div class="item_bd success">');
												}
												else
												{
													html.push('<div class="item_bd">');
												}
										html.push('<span class="thumb">');
									 
									    if(datas[i].imageurl != null ){
									    	html.push('<img class="fl" src="'+datas[i].imageurl+'">');
									    }else{
									    	html.push('<img class="fl" src="'+base+'res/images/logo-def.jpg">');
									    }
		
									    html.push('</span>');
									    
									    html.push('<div class="t">');
									    html.push('<div class="tn"></div>');
									    html.push('<div class="tt">'+datas[i].title+'</div>');
									    html.push('<div class="tp"><label>目标<span class="r">'+datas[i].cryMoney+'</span>元</label><label>已完成<span class="r">'+datas[i].donatePercent+'%</span></label></div>');
									    html.push('</div>');
									    html.push('<div class="clear"></div>');
									    html.push('</div></a>');
									    html.push('<div class="item_ft">');
									    html.push('<a href="javascript:void(0);" class="withdraw">我要提现</a><a href="'+dataUrl.projectFeedbackUrl+'?itemId='+datas[i].id+'" class="feedback">我要反馈</a>');
									    html.push('</div>');
									    html.push('</div>');	
									}
							
							}
							$('#list_1').append(html.join(''));	
						}
						else
						{
							$('.pullUpLabel').html('亲，没有更多内容了！');
						}	
						/*
						html.push('<div class="norecord">');
						html.push('<span class="tip">您还没有发布求助信息</span>');
						html.push('<a href="'+dataUrl.publish+'" class="publish">点此发布求助信息</a>');
						html.push('</div>');	
						$('#list_1').append(html.join(''));	
						*/
				},
				error   : function(r){ 
					d.alert({content:'获取求助列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		},
		feedbackList:function(page){ 
			var time=$(".sel-val").attr('v'),userId="";
			var projectId = $("#projectId").val();
			var flag=false;
			if(!page){page=1;} 
			if(this.time!=time){
				flag=true;
				this.time=time; 
				page=1;
			} 
			
			$.ajax({
				url:dataUrl.careList,
				data:{userId:userId,type:time,page:page,pageNum:10,t:new Date(),projectId:projectId},
				success: function(result){
						result = eval(result); 
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
					 	if(page<=total){
							var itemDtil=dataUrl.projectUrl+'?projectId=';
							for(var i=0;i<len;i++){
							
								var idata=datas[i]; 
								
								html.push('<div class="item bg2">');
								if(idata.uName==null){
									html.push('<div class="avatar"><img src="'+base+'/res/images/item/items/txicon_04.jpg" width="81" height="57"/></div>')
								}else{
									html.push('<div class="avatar"><img osrc="'+idata.userImageUrl+'" class="fl" src="'+idata.userImageUrl+'"></div>')
								}
							    html.push('<div class="detail">');
							    if(idata.uName==null){
							    	html.push('<div class="title"><span class="name">客服</span><span class="time">'+(new Date(idata.cTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
							    }else{
							    	html.push('<div class="title"><span class="name">'+idata.uName+'</span><span class="time">'+(new Date(idata.cTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
							    }
								html.push('<div class="info">');
								html.push('<p>'+idata.content+'</p>');
								
								if(idata.imgs){							
									html.push('<div class="po_img">');
									for(var j=0;j<idata.imgs.length;j++){
										html.push('<a href="'+idata.imgs[j]+'" target="_blank" title=""><img src="'+idata.imgs[j]+'" width="77" height="77" /></a>');
									}
									html.push('</div>');
								}
							
								html.push('<div class="clear"></div>');
								html.push('</div>');
								html.push('</div>');
								html.push('<div class="clear"></div>');
								html.push('</div>');
							}  
							//$('#list_3').html(html.join(''));
							$('#list_3').append(html.join(''));	
					
						}else {
							$('.pullUpLabel').html('亲，没有更多内容了！');
						}	
				}
			});
		},
		myDonateList:function(page){ 
			var time=$(".sel-val").attr('v'),userId="";
			var projectId = $("#projectId").val();
			var flag=false;
			if(!page){page=1;} 
			if(this.time!=time){
				flag=true;
				this.time=time; 
				page=1;
			} 
			
			$.ajax({
				url:dataUrl.myDonateList,
				data:{userId:userId,type:time,page:page,pageNum:20,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.projectUrl+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
		
							html.push('<a href="'+itemDtil+idata.pid+'">');
							if(idata.imagesurl==null){
								html.push('<div class="item bg2"><span class="thumb"><img osrc="'+base+'res/images/logo-def.jpg" class="fl" src="'+base+'res/images/logo-def.jpg"><label>'+idata.name+'</label></span>');
							}else{
								html.push('<div class="item bg2"><span class="thumb"><img osrc="'+idata.imagesurl+'" class="fl" src="'+idata.imagesurl+'"><label>'+idata.name+'</label></span>');
							}
							html.push('<div class="t">');
							html.push('<div class="tn"></div>');
							html.push('<div class="tt">'+idata.donationNum+'人捐助，金额'+idata.donatAmountpt+'元</div>');
					
							if(idata.status==260&&idata.cryMoney>idata.donatAmountpt)
							{
								html.push('<div class="ts_success">已结束</div>');
							}else if(idata.cryMoney<=idata.donatAmountpt)
							{
								html.push('<div class="ts_success">已筹满</div>');
							}
							else
							{
								html.push('<div class="ts">已募集<span>'+idata.donatAmountpt+'</span>元</div>');
							}
							html.push('<div class="tp">');
							if(idata.dType == 1){
								html.push('<label>您的助善已召集<span class="r">'+idata.donateNum+'</span>次，共<span class="r">捐款'+idata.dMoney+'</span>元</label></div>');
							}else{
								html.push('<label>您已捐助<span class="r">'+idata.donateNum+'</span>次，共<span class="r">'+idata.dMoney+'</span>元</label></div>');
							}
							html.push('</div>');
							html.push('<div class="clear"></div>');
							html.push('</div>');
							html.push('</a>');
						}  
						$('#list_2').html(html.join(''));
						
					
					}else if(result.flag==2){
						$('#list_2').html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有相关关注列表</div></div>');
					}else if(result.flag==0){
						$('#list_2').html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						$('#list_2').html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						
					}
					$('#list_2').html(html.join(''));		
				}
			});
		}
	}
	needhelplist.init();
	
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
		  		if(item == 1)
		  		{
		  			needhelplist.dataList(++i,4);
		  		}
		  		else if(item == 3)
		  		{
			    	needhelplist.feedbackList(++i);
		  		}
		  		
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
</html>