    <%@page import="com.guangde.home.utils.UserUtil" %>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ include file="./../common/file_url.jsp" %>
        <!DOCTYPE>
        <html lang="zh">
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
        <meta name="keywords" content=""/>
        <meta name="description" content=""/>
        <meta name="viewport"/>
        <title>善园网—一起行善，互联网公益平台</title>
        <link rel="stylesheet" type="text/css" href="/res/css/dev/projectSpread.css"/>
        <script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" ></script>
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
        <script>
        	wx.config({
				debug : false,
				appId : '${appId}',
				timestamp : '${timestamp}',
				nonceStr : '${noncestr}',
				signature : '${signature}',
				jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','previewImage' ]
			});

			wx.ready(function(){
				wx.onMenuShareAppMessage({
							title : '${oneNew.title}', // 分享标题
							desc : '${oneNew.title}', // 分享描述
							link : 'www.17xs.org/resposibilityReport/resposibilityReport.do?id=${oneNew.id}', // 分享链接
							imgUrl : '', // 分享图标
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
					title : '${oneNew.title}', // 分享标题
					link : 'www.17xs.org/resposibilityReport/resposibilityReport.do?id=${oneNew.id}', // 分享链接
					imgUrl : '', // 分享图标
					success : function() {
						// 用户确认分享后执行的回调函数
					},
					cancel : function() {
						// 用户取消分享后执行的回调函数
					}
				});
			});
            $(function () {
            $('.surImg img').click(function () {
            var imgArray = [];
            var curImageSrc = $(this).attr('src');
            var oParent = $(this).parent();
            if (curImageSrc && !oParent.attr('href')) {
            $('.surImg img').each(function (index, el) {
            var itemSrc = $(this).attr('src');
            imgArray.push(itemSrc);
            });
            wx.previewImage({
            current: curImageSrc,
            urls: imgArray
            });
            }
            });

            });
            </script>
        </head>
        <body>
        <div class="bdcon survey">
        <h1>${oneNew.title }</h1>
        <div class="top_p"><em><fmt:formatDate value="${oneNew.createtime }" pattern="yyyy-MM-dd"/></em><span
        class="ren_s">${oneNew.keywords }</span></div>
        <%--<h4>项目介绍：</h4>--%>
		<div class="surImg">
        ${oneNew.content }
        </div>
            </div>
        <div class="footer">
        <a href="http://www.17xs.org/"><img src="<%=resource_url%>res/images/h5/images/min-logo.jpg"
        class="footer_img"></a>
        <span class="foot_s">©宁波市善园公益基金会 版权所有</span>
        </div>
        </body>
        </html>