<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<div class="footNew">
   
   <div class="footLink w1000">
   		<div class="linkImg">
   			<!-- 
        	<a href="#" title="" class="link1"></a>
            <a href="#" title="" class="link2"></a>
            <a href="#" title="" class="link3"></a>
            <a href="#" title="" class="link4"></a>
            <a href="#" title="" class="link5"></a>
            <a href="#" title="" class="link6"></a>
            -->
            <c:forEach items="${linkImg}" var="img">
	       			 <a href="${img.linkUrl}" title="" target="_blank"><img src="${img.url}"  alt=""/></a>
	    	</c:forEach>
        </div>
        <div class="linkText">
       	  <!-- 
        	<a href="#" title="" target="_blank">浙江慈善总会 </a>
            <a href="#" title="" target="_blank">中国公益在线 </a>
            <a href="#" title="" target="_blank">周宁县慈善总会 </a>
            <a href="#" title="" target="_blank">中国报恩网 </a>
            <a href="#" title="" target="_blank">伏羲生态网  </a>
            <a href="#" title="" target="_blank">海西公益联盟 </a>
            <a href="#" title="" target="_blank">善爱公益 </a>
            <a href="#" title="" target="_blank">谷谷互联 </a>
            <a href="#" title="" target="_blank">佛子家园网 </a>
            <a href="#" title="" target="_blank">新华网评 </a>
            <a href="#" title="" target="_blank">人民网   </a>
            <a href="#" title="" target="_blank">中国广播网 </a>
            <a href="#" title="" target="_blank">国际在线   </a>
            <a href="#" title="" target="_blank">中国网新闻  </a>
            <a href="#" title="" target="_blank">中国台湾网   </a>
            <a href="#" title="" target="_blank">中国经济网 </a>
            <a href="#" title="" target="_blank">中国青年网  </a>
            <a href="#" title="" target="_blank">环球网 </a>
            <a href="#" title="" target="_blank">华商网 </a>
            -->
             <c:forEach items="${textImg}" var="img">
	       			 <a href="${img.linkUrl}" title="" target="_blank">${img.description}</a>
	    	</c:forEach>
        </div>
   </div>
   <div class="w1000 footCom">
   		<div class="footComText">
        	<a href="http://www.17xs.org/help/about/" title="">善园基金会 </a>|
        	<!-- <a href="#" title="">联系我们 </a>|
            <a href="#" title="">合作伙伴 </a>|
            <a href="#" title=""> 网站地图 </a>| -->
            <a href="http://www.17xs.org/help/userService/" title=""> 服务协议  </a>|
            <a href="#" title="">技术支持   </a>|
            <a href="http://www.17xs.org/index/toAddService.do?type=1" title="">意见反馈   </a>
        </div>
        <div class="footComDetail">
        	<div class="detailOne oneFirst">
            	<h3>善园基金会捐款渠道</h3>
                <p>户&nbsp;&nbsp;&nbsp;名：宁波市善园公益基金会</p>
                <p>帐&nbsp;&nbsp;&nbsp;号：<em class="fontArial">3901150019000368944</em></p>
                <p>开户行：中国工商银行鄞州支行营业部</p>
                <p>地&nbsp;&nbsp;&nbsp;址：宁波市鄞州区泰康西路399号（宁波·善园）</p>
            </div>
            <div class="detailOne oneMiddle">
            	<h3>联系我们</h3>
                <div class="footTel"><i></i><em class="fontArial">0574-87412436<span style="margin-left:5px;font-size:18px;vertical-align:2px;">(日常咨询)</span></em></div>
                <div class="footTel" style="border:none;"><i></i><em class="fontArial">18106606173<span style="margin-left:23px;font-size:18px;vertical-align:2px;">(应急咨询)</span></em></div>
                
            </div>
            <div class="detailOne oneEnd">
            	<h3>关注我们</h3>
                <p>
                	<a href="#" title="" class="xin"><i></i>新浪微博</a>
                    <a href="#" title="" class="teng"><i></i>微信公众号</a>
                </p>
            </div>
        </div>
        <p class="footText"><span>Copyright  ©</span>    <span>宁波市善园公益基金会</span>  <span>版权所有</span>    <span>   浙ICP备<em class="fontArial">15018913</em>号-1 </span>    <span>  杭州智善网络科技有限公司 </span>    <span> 提供技术支持</span>
        <a key ="5652be0aefbfb04945150337"  logo_size="83x30"  logo_type="realname"  href="http://www.anquan.org" ></a>
        </p>
   </div>
</div>

<div class="suspen">
	<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">
    	<p>在线客服</p>
        online service
    </a>
	<span class="saoCode"></span>
</div>

<div id="browser" class="browser">
<div class="browserbd">
            亲，您的浏览器版本过低，为了您更好的体验，您可以：点击升级<a target="_blank" href="http://rj.baidu.com/soft/detail/23253.html" class="brie">IE浏览器</a>，或点击下载<a target="_blank" href="http://rj.baidu.com/soft/detail/14744.html?ald" class="brgoogle">谷歌浏览器</a>
    <div id="brsClose" class="browserClose"></div>
</div>
</div>
<script src="http://static.anquan.org/static/outer/js/aq_auth.js"></script>
<!--<script src="http://kefu.qycn.com/vclient/state.php?webid=113140" language="javascript" type="text/javascript"></script>-->