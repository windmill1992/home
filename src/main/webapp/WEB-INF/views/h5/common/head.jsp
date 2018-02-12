<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<!-- 公共头部 began -->
<!-- 未登录 -->
<div class="userTop nologin" style="display: none;">
	<img src="/res/images/h5/images/temp/userLogo.png">
	<p>未登录</p>
</div>
<!-- 已登录 -->
<div class="userTop">
	<a href="http://www.17xs.org/ucenter/accountInfo.do?v=1" id="linkToUcenter">
		<div class="userLogo">
			<!-- 点击头像跳转至账户信息页面 -->
			<img src="/res/images/detail/people_avatar.jpg" id="headImageUrl">
		</div>
		<div class="userInfo">
			<h2 class="name" id="nickName"><i class="medal"></i></h2>
			<p class="phone" id="mobile"></p>
			<p class="address" id="address"></p>
			<p id="addressId" style="display:none"></p>
		</div>
	</a>
	<script type="text/javascript">
		var global_addrs = [];
		function cutstr(str, len) {
			var str_length = 0;
			var str_len = 0;
			str_cut = new String();
			str_len = str.length;
			for(var i = 0; i < str_len; i++) {
				a = str.charAt(i);
				str_length++;
				if(escape(a).length > 4) {
					//中文字符的长度经编码之后大于4
					str_length++;
				}
				str_cut = str_cut.concat(a);
				if(str_length >= len) {
					str_cut = str_cut.concat("...");
					return str_cut;
				}
			}
			//如果给定字符串小于指定长度，则返回源字符串；
			if(str_length < len) {
				return str;
			}
		}
		$(function() {
			$.ajax({
				url: 'http://www.17xs.org/user/userInfo.do',
				dataType: 'json',
				type: 'post',
				data: {},
				success: function(data) {
					if(data.result == 0) {
						var item = data.item;
						var headImageUrl = item.headImageUrl;
						if(headImageUrl != '' && headImageUrl != null) {
							$('#headImageUrl').attr("src", headImageUrl);
						}
						var nickName = item.nickName;
						$('#nickName').text(nickName);
					}
				}
			});

			var pageNum1 = 10;
			var page1 = 1;
			$.ajax({
				url: 'http://www.17xs.org/user/userAddressList.do',
				dataType: 'json',
				type: 'get',
				data: {
					pageNum: pageNum1,
					page: page1
				},
				async:false,
				timeout:5000,
				success: function(data) {
					if(data.flag == 1) {
						var ret = data.obj.data, address = '';
						for(var i in ret) {
							var r = ret[i];
							address = r.province + r.city + r.area + r.detailAddress;
							address = cutstr(address, 85);
							global_addrs.push({'addr':address,'id':r.id});
							if(i == 0){
								$('#mobile').text(r.mobile);
								$('#address').text(address);
								$('#addressId').text(r.id);
							}
						}
					}
				},error:function(){
					alert('请求超时！');
				}
			});
		});
	</script>
</div>
<!-- 公共头部 end -->