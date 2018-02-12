var loginUrl = {
	wxLogin:'http://www.17xs.org/donateStep/wxLogin.do'
};
var wxlogin = {
	login:function(){
		/*微信授权登录*/
		var that = this;
		var userId = this.getcookie('userId');
		var access_code = this.GetQueryString('code');
		if(userId == "" || userId == 'null' || userId == undefined) {
			if(access_code == null || access_code == '' || access_code == 'null') {
				var fromurl = location.href;
				var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa09ee42dbe779694&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=STATE%23wechat_redirect&connect_redirect=1#wechat_redirect';
				location.href = url;
			} else {
				$.ajax({
					type: 'get',
					url: loginUrl.wxLogin,
					async: false,
					cache: false,
					data: {code: access_code},
					dataType: 'json',
					success: function(result) {
						if(result.code == 1){
							userId = result.result.userId;
							that.addcookie('userId', userId, 10);
						}else{
							alert(res.msg);
						}
					}
				});
			}
		}else{}
	},
	addcookie:function(name, value, expireMin){
		var cookieString = name + "=" + escape(value) + "; path=/";
		//判断是否设置过期时间  
		if(expireMin > 0) {
			var date = new Date();
			date.setTime(date.getTime + expireMin * 60 * 1000);
			cookieString = cookieString + "; expire=" + date.toGMTString();
		}
		document.cookie = cookieString;
	},
	//获取cookie
	getcookie:function(name){
		var strcookie = document.cookie;
		var arrcookie = strcookie.split("; ");
		for(var i = 0; i < arrcookie.length; i++) {
			var arr = arrcookie[i].split("=");
			if(arr[0] == name) return decodeURIComponent(arr[1]); //增加对特殊字符的解析  
		}
		return "";
	},
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};

wxlogin.login();

