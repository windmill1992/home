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
<div id="header" class="header">
	<div class="header-bd ">
        <div class="logo">
            <h1><a href="http://www.17xs.org/" alt="善园基金会"></a></h1>
            <h2><a href="http://www.17xs.org/" alt="善园基金会"></a></h2>
        </div>
        <ul id="siteNav" class="siteNav">
        	<li><a id="p-index" href="<%=resource_url%>" target="_self">首页</a></li>
            <li><a id="p-doGood" href="<%=resource_url%>project/index.do" target="_self">我要行善</a></li>
            <li><a id="p-seekHelp" href="<%=resource_url%>project/appealFirest.do" target="_self">我要求助</a></li>
            <li><a id="p-joinGood" href="<%=resource_url%>project/joinGood.do" target="_self">加入善管家</a></li> 
<!--            <li><a id="p-goodGarden" href="<%=resource_url%>project/gardenindex.do" target="_self">善园众筹</a></li>-->
        </ul>
        <div id="enter" class="enter">
            <i class="line"></i><a id="hd-login" class="link-btn toIn">登录</a><a id="hd-register" class="toIn">注册</a>
        </div>
    </div>
</div>

<div class="suspen">
	<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes">
    	<p>在线客服</p>
        online service
    </a>
    <span class="saoCode"></span>
</div> 

<!--  <script src="http://kefu.qycn.com/vclient/state.php?webid=113140" language="javascript" type="text/javascript"></script>-->
