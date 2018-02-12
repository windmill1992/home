<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="101221899" data-redirecturi="http://17xs.org" charset="utf-8"></script>
<script type="text/javascript">
	//从页面收集OpenAPI必要的参数。get_user_info不需要输入参数，因此paras中没有参数
	var paras = {};

	//用JS SDK调用OpenAPI
	QC.api("get_user_info", paras)
	//指定接口访问成功的接收函数，s为成功返回Response对象
	.success(function(s){
		//成功回调，通过s.data获取OpenAPI的返回数据
		//alert("获取用户信息成功！当前用户昵称为："+s.data.nickname);
	})
	//指定接口访问失败的接收函数，f为失败返回Response对象
	.error(function(f){
		//失败回调
		//alert("获取用户信息失败！");
	})
	//指定接口完成请求后的接收函数，c为完成请求返回Response对象
	.complete(function(c){
		//完成请求回调
		//alert("获取用户信息完成！");
	});
	
	if(QC.Login.check()){//如果已登录
	QC.Login.getMe(function(openId, accessToken){
		alert(["当前登录用户的", "openId为："+openId, "accessToken为："+accessToken].join("\n"));
	});
	//这里可以调用自己的保存接口
	//...
	}
</script>
 -->
<div class="navTOP">
	<div class="w1000">
		<div class="lNew">
			慈善咨询热线：<span class="orangeText">0571-87165191</span>
		</div>
		<div class="rNew">
			<a href="http://www.17xs.org/ucenter/core/mygood.do" title="" target="_blank">我的捐赠记录</a>
			<a href="http://www.17xs.org/ucenter/pindex.do" title="" target="_blank">我的项目 </a>
			<a href="http://www.17xs.org/help/questions/" title="" target="_blank">帮助中心</a>
			<a href="http://www.17xs.org/index/indexService.do" title="" target="_blank">客服中心</a>
		</div>
	</div>
</div>
<div class="newyear-bg"></div>
<div class="headerNew w1000">
	<div class="lNew">
		<a href="http://www.17xs.org/" class="logo" title="善园网—一起行善，互联网公益平台，大病筹款，公益众筹">
			<img src="<%=resource_url%>res/images/index/logo_03.png" alt="善园网—一起行善，互联网公益平台，大病筹款，公益众筹" title="善园网—一起行善，互联网公益平台，大病筹款，公益众筹" style="width:178px;">
		</a>

		<div class="lunbotu">
			<img src="http://www.17xs.org/res/images/newIndex/index/logoS_1.jpg">
			<!--<img src="http://www.17xs.org/res/images/newIndex/index/logoS_2.jpg">
			<img src="http://www.17xs.org/res/images/newIndex/index/logoS_3.jpg">
			<img src="http://www.17xs.org/res/images/newIndex/index/logoS_4.jpg">-->
		</div>
	</div>
	<%--搜索框--%>

	<div class="rNew" id="enter">
		<span class="noLogin" style="display:none">
            <a href="#" title="注册" class="regBtn"><em class="iconNew iconPen"></em>注册</a>
            <a href="#" title="登录" class="loginBtn"><em class="iconNew iconPerson"></em>登录</a>
        </span>
		<!--登录后-->
		<span class="onLogin"></span>
	</div>
	<div class="headerNew_s">
		<div class="form1">
			<input type="text" class="inputText" placeholder="&nbsp;请输入关键字" value="" id="keyWords" />
			<a href="javascript:;" onclick="select();">搜索</a>

		</div>
	</div>
</div>
<div class="navNew">
	<div class="w1000 navList">
		<div class="nav-a on">
			<a href="http://www.17xs.org/" class="sub-a" id="p-index">首页</a>
		</div>
		<div class="nav-a">
			<a href="http://www.17xs.org/project/index/" class="sub-a" id="p-doGood">我要行善</a>
		</div>
		<div class="nav-a">
			<a href="http://www.17xs.org/project/appealFirest/" class="sub-a" id="p-seekHelp">我要筹款</a>
		</div>
		<div class="nav-a">
			<a href="http://www.17xs.org/project/joinGood/" class="sub-a" id="p-joinGood">加入善管家</a>
		</div>
		<div class="nav-a">
			<a href="http://www.17xs.org/index/donationWhere/" class="sub-a" id="p-gardenWhere">善款去向</a>
		</div>
		<div class="nav-a nav-sub">
			<a href="javascript:;" class="sub-a" id="p-shanhe">公益项目</a>
			<div class="submenu">
				<a href="http://www.17xs.org/help/shanhe.do" class="sub-a sub-aa">善荷计划</a>
			</div>
		</div>
		<div class="nav-a nav-sub">
			<a href="javascript:;" class="sub-a" id="p-aboutGood">关于善园</a>
			<div class="submenu">
				<a href="http://www.17xs.org/help/about/" class="sub-a sub-aa">善园基金会</a>
				<a href="http://www.17xs.org/news/infoByItem/?type=7&item=章程" class="sub-a sub-aa">信息披露</a>
			</div>
		</div>
		<div class="nav-a nav-sub">
			<a href="javascript:;" class="sub-a" id="p-donateRecord">行善记录</a>
			<div class="submenu">
				<a href="http://www.17xs.org/data/dataStatistics_index.do" class="sub-a sub-aa">爱心贡献榜</a>
				<a href="http://www.17xs.org/index/donationsListNew/" class="sub-a sub-aa">捐赠记录</a>
			</div>
		</div>
		<!-- <a href="http://www.17xs.org/project/gardenindex.do" class="nav-a" id="p-goodGarden">公益众筹</a> -->

		<!-- <a href="http://www.17xs.org/goodlibrary/getGoodLibraryList.do" title="" id="p-donateRecord">行善记录</a> -->

		<!-- <a href="http://www.17xs.org/project/launchDonateView.do" title="" id="p-monthDonate">日捐月捐计划</a> -->
	</div>
</div>

<script>
	var imgNum = 1;

	function changeImg() {
		$(".lunbotu img").attr("src", "http://www.17xs.org/res/images/newIndex/index/logoS_" + imgNum + ".jpg");
		if(imgNum < 4) {
			imgNum++;
		} else {
			imgNum = 1;
		}
	}
	setInterval(changeImg, 3000);

	function select() {
		var keyWords = $('#keyWords').val();
		//window.location.href = "http://www.17xs.org/project/index/?keyWords="+keyWords;
		window.open("http://www.17xs.org/project/index/?keyWords=" + keyWords);
	}
</script>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>