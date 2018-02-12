/*
 * 微信登录
 * 
 */

var base = 'http://www.17xs.org/';
var loginUrl = {
	wxLogin : base + 'donateStep/wxLoginHtml.do' 	//微信授权登录
};

var wxlogin = {
	/*微信授权登录*/
	login:function(){
		var that = this;
		var recommendedPerson = this.GetQueryString('recommendedPerson');
		var charityId = this.GetQueryString('charityId');
		if(recommendedPerson!=null && charityId == null){
			that.addcookie('recommendedPerson',recommendedPerson,120);
		}else if(recommendedPerson==null && charityId != null){
			that.addcookie('recommendedPerson','',-1);
		}else if(recommendedPerson==null && charityId == null){
			that.addcookie('recommendedPerson','',-1);
		}else{}
		
		var userId = that.getcookie('userId');
		var appId = that.getcookie('appId');
		var timestamp = that.getcookie('timestamp');
		var nonceStr = that.getcookie('nonceStr');
		var signature = that.getcookie('signature');
		var access_code = that.GetQueryString('code');
		if(access_code == null){
			access_code = that.getcookie('code');
		}else{}
		if(access_code == "") {
			var fromurl = location.href;
			var url1 = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa09ee42dbe779694&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=STATE%23wechat_redirect&connect_redirect=1#wechat_redirect';
			location.href = url1;
		} else {
			that.addcookie('code',access_code,10);
			$.ajax({
				type: 'GET',
				url: loginUrl.wxLogin,
				async: false,
				cache: false,
				data: {code: access_code,url:location.href},
				dataType: 'json',
				success: function(res) {
					userId = res.result.userId;
					appId = res.result.appId;
					timestamp = res.result.timeStamp;
					nonceStr = res.result.nonceStr;
					signature = res.result.signature;
					that.addcookie('userId', userId, 2);
					that.addcookie('appId', appId, 2);
					that.addcookie('timestamp', timestamp, 2);
					that.addcookie('nonceStr', nonceStr, 2);
					that.addcookie('signature', signature, 2);
				}
			});
		}	
		
		wx.config({
			debug : false,
			appId : appId,
			timestamp : timestamp,
			nonceStr : nonceStr,
			signature : signature,
			jsApiList : ['chooseImage','previewImage','uploadImage']
		});
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
	getcookie:function(name){
		var strcookie = document.cookie;
		var arrcookie = strcookie.split("; ");
		for(var i = 0; i < arrcookie.length; i++) {
			var arr = arrcookie[i].split("=");
			if(arr[0] == name) return decodeURIComponent(arr[1]); //增加对特殊字符的解析  
		}
		return "";
	},
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};
