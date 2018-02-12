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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/detail.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/weixin.css"/>
</head> 

<body>
<div id="pageContainer">
<div class="project">
<!--焦点图//-->
<div id="mySwipe" style="margin:0px auto; overflow: hidden; visibility: visible;" class="swipe">
  <div class="swipe-wrap">
    <div> <a href="#">
      <%-- <div class="hot"> <img src="${project.coverImageUrl}" class="img_big"> </div> --%>
      <div class="hot"><div id="login_container" ></div> </div>
      </a> </div>
  </div>
</div>
<!--进度信息//-->
<div class="progress">
 		<h3 id="menu-image">图像接口</h3>
      <span class="desc">拍照或从手机相册中选图接口</span>
      <button class="btn btn_primary" id="chooseImage">chooseImage</button>
      <br>
      <span class="desc">上传图片接口</span>
      <button class="btn btn_primary" id="uploadImage">uploadImage</button>
      <br>
      <span class="desc">下载图片接口</span>
      <button class="btn btn_primary" id="downloadImage">downloadImage</button>
      <br>
  <div class="progress_bar"><span  class="track"><i style="width:${process}%" class="fill"></i></span>
    <label>${processbfb}%</label>
  </div>
</div>
<!--------支付窗-----//-->
			<div id="payTips" 
				style="display:none;background-color: rgba(0, 0, 0, 0.701961); position: absolute; width: 100%; height: 18386px; top: 0px; left: 0px; z-index: 700; background-position: initial initial; background-repeat: initial initial;"
				class="">
				<div class="pay" >
					<div class="t">
						<div class="close" id="closeDetail">
							<img src="<%=resource_url%>res/images/h5/images/icon_close.png">
						</div>
						<span>请输入捐款金额</span>
					</div>
					<div class="box">
						<table class="tb" id="selectTb">
							<tbody>
								<tr>
									<td><div t="20" class="sel l ed">
											<img src="<%=resource_url%>res/images/h5/images/yqj_selected.png"><span>20</span>元
										</div></td>
									<td><div t="50" class="sel l">
											<img src="<%=resource_url%>res/images/h5/images/yqj_selected.png"><span>50</span>元
										</div></td>
									<td><div t="100" class="sel l">
											<img src="<%=resource_url%>res/images/h5/images/yqj_selected.png"><span>100</span>元
										</div></td>
									<td><div t="500" class="sel l">
											<img src="<%=resource_url%>res/images/h5/images/yqj_selected.png"><span>500</span>元
										</div></td>
								</tr>
								<tr>
									<td colspan="4"><div style="height:5px;"></div></td>
								</tr>
								<tr>
									<td colspan="4"><div t="0" class="sel l">
											<img src="<%=resource_url%>res/images/h5/images/yqj_selected.png">
											<input class="fl" id="money2"  type="number" style="margin-left:8px" placeholder="其他">
											<div class="fr" style="margin-right:8px">元</div>
											<div style="display:inline-block;">
												<input type="number" id="money" style="display: none;">
											</div>
										</div></td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:void(0)" class="btn_a" id="btn_submit"><div
								class="btn">立即捐款</div></a>
						<div class="prot">
							<img
								src="http://mat1.gtimg.com/gongyi/m/wx/detail_icon_8_checked.png"
								class="check"><input type="checkbox" id="check"
								name="check" checked="checked" class="none"><label
								for="check">同意并接受</label> <span onclick="toAbout1()" class="p"><a href="http://www.17xs.org/redPackets/getAgreement.do">《善园基金会用户协议》</a></span>
						</div>
					</div>
				</div>
			</div>
			<!------按钮//---->
    <div class="btns">
      <div style="margin:5px;">
        <table style="width:100%">
          <tbody>
            <tr>
              <td width="49%"><a href="javascript:void(0)" id="btn_submit1">
                <div class="btn" id="paybtn"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要捐款</div>
                <div class="btn" id="finishedbtn" style="display: none;"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束，感谢您的支持</div>
                </a></td>
<!--                 <td width="49%"><a href="javascript:void(0)" onclick="fx()"> -->
<!--                 <div class="btn" ><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon2.png">邀朋友一起捐</div> -->
<!--                 </a></td> -->
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!--详情信息//-->
    <div class="detail_info"> 
      <!--个人信息卡//-->
      <div class="profile">
        <ul class="data_list">
          <a>
          <li><i>目标金额</i><b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.#" value = "${project.cryMoney}"/></span>元</b></li>
          </a> <a>
          <li><i>已筹金额</i><b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/>元</span></b></li>
          </a> <a>
          <li class="last"><i>捐款人数</i><b><span class="num">${peopleNum}</span>人</b></li>
          </a>
        </ul>
         <c:choose>
	        <c:when test="${project.state == 260}">
			   <div class="finish"><i>已结束</i><b>${project.title}</b></div>
			</c:when>
			<c:otherwise >
			   <div class="info"><i>筹款中</i><b>${project.title}</b></div>
			</c:otherwise>
		</c:choose>
        <div class="clear"></div>
      </div>
      <!--关注项目//-->
      <ul class="content_tabs">
        <li class="cur">详情</li>
        <li>发起人</li>
        <li>捐助</li>
        <li>反馈</li>
      </ul>
      <div class="clear"></div>
      <!--详情//-->
      <div class="list" id="list_1">
        <div class="content">
        <p>
          ${project.content}
          </p>
          <br/>
           <c:forEach items="${project.bfileList}" var="img">
       			 <p class="pic">
	       			 <c:if test="${img.fileType !='video'}">
		       			 <img src="${img.url}" width="100%"  alt="">
		       			 <c:if test="${img.description!='1'}">
		       			 <i>${img.description}</i>
		       			 </c:if>
       			  	</c:if>
       			  	<c:if test="${img.fileType =='video'}">
		       			 <iframe  src="${img.description}"
							allowfullscreen="" frameborder="0" width="100%" height="100%">
						 </iframe>
       			  	</c:if>
       			 </p>
    		</c:forEach>
            <!--优酷视频的连接  embed/XNTcyMzIwMTE2-->
			<!-- <div style="position:relative;z-index:1;">
				<iframe  src="http://player.youku.com/embed/XNTcyMzIwMTE2"
					allowfullscreen="" frameborder="0" width="100%" height="100%">
				</iframe>
			</div>
			腾讯视频的连接  vid=m0019dui6y5
			<div style="position:relative;z-index:1;">
				<iframe src="http://v.qq.com/iframe/player.html?vid=c0186z036de&tiny=0&auto=0"
					allowfullscreen="" frameborder="0" width="100%" height="100%">
				</iframe>
			</div> -->
			<div class="clear"></div>
        </div>
      </div>
      
      <!--发起人//-->
      <div class="list" id="list_2" style="display:none;">
        <div class="publisher">
          <div class="item">
            <div class="thumb"><img src="<%=resource_url%>res/images/detail/people_avatar.jpg"></div>
            <ul class="info">
              <li class="title">发起人<i>与求助者关系是${fabu.relation}</i></li>
              <li><span>姓名：${fabu.realName}</span><span>职业：<c:if test="${fabu.vocation==null||fabu.vocation==''}">无职业</c:if><c:if test="${fabu.vocation!=null&&fabu.vocation!=''}">${fabu.vocation}</c:if></span></li>
              <li>电话：${fabu.linkMobile}</li>
              <li>住址：${fabu.familyAddress}</li>
            </ul>
          </div>
          <div class="item">
            <div class="thumb"><img src="<%=resource_url%>res/images/detail/people_avatar.jpg"></div>
            <ul class="info">
              <li class="title">求救人</li>
              <li><span>姓名：${shouzhu.realName}</span><span>职业：<c:if test="${shouzhu.vocation==null||shouzhu.vocation==''}">无职业</c:if><c:if test="${shouzhu.vocation!=null&&shouzhu.vocation!=''}">${shouzhu.vocation}</c:if></span></li>
              <li>电话：${shouzhu.linkMobile}</li>
              <li>住址：${shouzhu.familyAddress}</li>
            </ul>
          </div>
          <div class="item">
            <div class="thumb"><img src="<%=resource_url%>res/images/detail/people_avatar.jpg"></div>
            <ul class="info">
              <li class="title">证明人</li>
             <li><span>姓名：${zhengming.realName}</span></li>
              <li>职务：${zhengming.persition}</li>
              <li>单位：${zhengming.workUnit}</li>
              <li>电话：${zhengming.linkMobile}</li>
            </ul>
          </div>
          <div class="clear"></div>
        </div>
      </div>
      <!----捐助人--//-->
      <div class="list" id="list_3" style="display:none;">
        </div>
      
      <!--反馈//-->
      <div class="list status" id="list_4" style="display:none;">
        
      </div>
      <!--加载中//-->
<!--       <div class="loading">加载中…</div> -->
      <!--页脚//--></div>
    <!--页脚//-->
    <div class="footer"> <span>© 2015 杭州智善  版权所有</span> <img src="<%=resource_url%>res/images/h5/images/min-logo.jpg"></div>
  </div>
</div>
<input type="hidden" id="pprim" value="${project.title}">
<input id="projectId" type="hidden" value="${project.id}">
<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" /></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	wx.config({
		debug : true,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','chooseImage','previewImage','uploadImage' ]
	});
	wx.ready(function(){
		wx.onMenuShareAppMessage({
					title : '${project.title}', // 分享标题
					desc : '${desc}', // 分享描述
					link : 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}', // 分享链接
					imgUrl : '${project.coverImageUrl}', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
						window.location.href = 'http://www.17xs.org/';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '${project.title}', // 分享标题
			link : 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}', // 分享链接
			imgUrl : '${project.coverImageUrl}', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});
	  
		});
	// 5 图片接口
  // 5.1 拍照、本地选图
  var images = {
    localId: [],
    serverId: []
  };
  document.querySelector('#chooseImage').onclick = function () {
    wx.chooseImage({
    count: 9, // 默认9
    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
    success: function (res) {
          images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
         alert('已选择 ' + res.localIds.length + ' 张图片');
    }
    });
  };	
		
/* 	document.querySelector('#uploadImage').onclick = function() {
		if (images.localId.length == 0) {
			alert('请先使用 chooseImage 接口选择图片');
			return;
		}
		var i = 0, length = images.localId.length;
		images.serverId = [];
		function upload() {
			wx.uploadImage({
				localId : images.localId[i],
				success : function(res) {
					i++;
					alert('已上传：' + i + '/' + length);
					images.serverId.push(res.serverId);
					if (i < length) {
						upload();
					}
				},
				fail : function(res) {
					alert(JSON.stringify(res));
				}
			});
		}
		upload();
	};
	var serverids ="tgYZTKhcvvMKL9xSyZR4xPoQZJ8koj9n_8CtcBfECP6oPiZvFrNJvFgyThCsenka";
	$('#downloadImage').click(function(){
				syncDownload(serverids);
	});	
	//异步下载图片到本地
	  var syncDownload = function(serverids){
	     $.ajax({
			url:'http://www.17xs.org/file/wximage.do',
			data:{mId:serverids,t:new Date()},
			success: function(res){ 
				if(res.flag==1){
					alert(res.obj);
				}
			}
		}); 
	  }; */
</script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<script>
	var obj = new WxLogin({
		id : "login_container",
		appid : "wxe4a86dee2b974b99",
		scope : "snsapi_login",
		redirect_uri : "http%3a%2f%2fwww.17xs.org%2fsso%2fwxLoad.do",
		state : "STATE#wechat_redirect",
		style : "",
		href : "http://www.17xs.org/res/css/dev/weixin.css"
	});
	
	//$('#login_container').val(obj);
</script>
<script data-main="<%=resource_url%>res/js/h5/detail_test.js?v=201510131029" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>