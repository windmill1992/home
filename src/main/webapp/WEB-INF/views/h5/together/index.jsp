<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
		<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
		<title>善园</title>
	</head>
	<style type="text/css">
		*{padding: 0;margin: 0;
			-webkit-margin-after: 0;
			-webkit-margin-before: 0;
			-webkit-margin-start: 0;
			-webkit-margin-end: 0;
			-webkit-padding-start: 0;
			-webkit-padding-end: 0;
			-webkit-padding-before: 0;
			-webkit-padding-after: 0;
		}
		a{text-decoration: none;}
		.welcome{width: 100%;height: 100%;position: absolute;top: 0;}
		.time{
			position: fixed;top: 20px;right: 20px;background: rgba(0,0,0,.4);color: #fff;font-size: 13px;text-align: center;height: 20px;line-height: 20px;
			width: 100px;display: inline-block;
		}
	</style>
	<body>
		<div class="welcome">
			<img src="../../../../res/images/h5/images/together/sy.png" width="100%" height="100%"/>
			<a href="javascript:;" class="time">跳过(3s)</a>
		</div>
		
		<script src="../../../../res/js/jquery-1.8.2.min.js"></script>
		<script>
			document.body.style.minHeight = screen.height+'px';
			$(function(){
				var time = 3;
				var timer = null;
				timer = setInterval(function(){
					if(time<=0){
						toUrl();
						return;
					}
					time--;
					$('.time').text('跳过('+time+'s)');
				},1000);
				
				$('.time').on('click',function(){
					toUrl();
				});
				
				function toUrl(){
					window.location.href='http://www.17xs.org/together/detail_view.do?projectId=${projectId}';
					clearInterval(timer);
				}
			});
		</script>
	</body>
</html>