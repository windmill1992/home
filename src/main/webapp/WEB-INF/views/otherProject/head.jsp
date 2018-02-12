<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- <div class="headerNew w1000"> -->
	
<div class="theheader"><img src="http://nb.17xs.org/res/images/banner/thebanner.png"></div>
<!-- </div> -->
<div class="navNew">
	<div class="w1000 navList">
		<a href="http://nb.17xs.org/" title="" id="p-service">服务项目</a>
        <a href="http://www.17xs.org/project/index/" title="" id="p-doGood">我要行善</a>
    </div>
</div> 

<script>
var imgNum = 1
function changeImg(){
	$(".lunbotu img").attr("src","res/images/newIndex/index/logoS_"+imgNum+".jpg");
	if(imgNum<4){
		imgNum++;	
	}else{
		imgNum=1;	
	}
}
setInterval(changeImg,3000);
</script>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>