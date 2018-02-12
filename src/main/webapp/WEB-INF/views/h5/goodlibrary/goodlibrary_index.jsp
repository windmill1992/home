<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=resource_url%>res/css/h5/basic.css" rel="stylesheet" type="text/css" />
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<title>善园网-善库-公益备忘录</title>
</head> 
<body onload="RefreshOnce()">
<div class="library">
    <div class="library_img1">
        <img src="${logoImg }" style="width: 136px;margin-left: -19px;height: 136px;margin-top: -12px;" id="img">
    </div>
    <p class="library_text1">${goodlibrary.nickName }</p>
    <p class="library_text2">${goodlibrary.familyAddress }</p>
</div>
    <div class="index_time tc">
    	<p><fmt:formatDate value="${goodlibrary.createtime }" type="date" dateStyle="long"/>-<fmt:formatDate value="${nowDate }" type="date" dateStyle="long"/></p>
    </div>
    <div class="clear"></div>
    <div class="index_big mar tc">
        <div class="index_money tc cut20 fl">
            <p>捐助项目</p>
            <p><span>${totalProjectNum }个</span></p>
        </div>
        <div class="index_money tc cut20 fl">
            <p>捐助金额</p>
            <p><span>${totalMoney }元</span></p>
        </div>
        <div class="index_money tc cut20 fl">
            <p>号召人数</p>
            <p><span>${totalPeople }人</span></p>
        </div>
        <div class="index_money tc cut20 fl">
            <p>行善次数</p>
            <p><span>${totalDonateNum }次</span></p>
        </div>
        <div class="clear"></div>
    </div>
    <div class="index_all tc">
    	<p>总捐助金额：<span>¥${totalMoney }元</span></p>
    </div>
    <div class="index_square  bodt bodr bodb bodl ">
        <div class="index_arrow bodbm">
            <img class="fl width25" src="<%=resource_url%>res/images/h5/images/goodlibrary/xiaz6.png"/>
            <p class="fl">加入成员：<span>${totalPeople }</span>人</p>
            <img class="mtop5 fr" src="<%=resource_url%>res/images/h5/images/goodlibrary/xiaz9.png"/>
            <div class="clear"></div>
        </div>
        <div class="index_pic">
        	<c:forEach var="userHead" items="${headimgs }" >
        		<c:choose>
        			<c:when test="${userHead.coverImageUrl == null || userHead.coverImageUrl == ''}">
        				<li><img src="http://www.17xs.org/res/images/user/4.jpg"/></li>
        			</c:when>
        			<c:otherwise>
        				<li><img src="${userHead.coverImageUrl }"/></li>
        			</c:otherwise>
        		</c:choose>
        	</c:forEach>
            
        </div>
    </div>
    <div class="index_square bodr bodb bodl">
        <div class="index_arrow bodbm">
            <img class="fl width25" src="<%=resource_url%>res/images/h5/images/goodlibrary/xiaz7.png"/>
            <p class="fl">公益项目：<span>${Num }</span>个</p>
            <img class="mtop5 fr" src="<%=resource_url%>res/images/h5/images/goodlibrary/xiaz9.png"/>
            <div class="clear"></div>
        </div>
        <c:if test="${tags != null && tags != '-1'}">
			<c:forTokens items="${tags }" delims="," var="tag">
				<div class="index_money1 tc fl"><p>${tag }</p></div> 
			</c:forTokens>
		</c:if>
        <div class="clear"></div>
    </div>
    <div class="index_max  bodr bodl bodb">
    	<div class="index_arrow bodbm">
            <div class="hx">
            <img class="fl width25" src="<%=resource_url%>res/images/h5/images/goodlibrary/xiaz8.png"/>
                <p class="fl">行善记录：<span>${totalDonateNum }</span>次</p>
            </div>
            <div class="clear"></div>
            <div style="display:none">
				<span id="page"></span>
				<span id="pageNum"></span>
			</div >
			<div id="add"></div>
			
        </div>
        </div>


    <a href="javascript:void(0);" id="loadMore">
        <div class="details_add ">
    	<p class="fl">点击查看更多</p>
        <img src="<%=resource_url%>res/images/h5/images/goodlibrary/pic_11.png"/>
    </div>
    </a>
	<div class="bor_btn"></div>
    <a href="javascript:void(0);" id="veLibrary">
        <div class="footer tc">
         <p class="btn">加入善库，一起行善</p>
    </div>
    </a>
	<!--弹框提示-->
	<div class="prompt_box" style="display:none">
	<div class="cue_back"></div>
	<div class="cue1">
	<div class="cue_center">
	<div class="cue_center1">
	<p class="cue_p1">恭喜您</p>
	<p class="cue_p3">您已经是该善库成员，无需提交申请，现在可以开始行善了</p>
	</div>
	<div class="cue_center2">
	<a href="javascript:void(0);" class="ui-link" id="submit"><div class="cue_fl"><p class="cue_pl">立即行善</p></div></a>
	<a href="javascript:void(0);" class="ui-link" id="cancel"><div class="cue_fr"><p class="cue_pr">取消</p></div></a>
	</div>
	</div>
	</div>
	</div>

	<input type="hidden" id="lirbraryId" value="${goodlibrary.id }">
    <input type="hidden" id="userId" value="${user.id }">
    <input type="hidden" id="libraryUserId" value="${libraryUserId }">
<script type="text/javascript">
	$(document).ready(function(){
			var pageNum = 10;
			var page = 1;
			var libraryUserId= $('#libraryUserId').val();
			$.ajax({
				url: 'http://www.17xs.org/goodlibrary/goodLibraryList.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNum,
					page: page,
					userId: libraryUserId
				},
				success: function(data) {
					if(data.flag == 1){
					   var nums = data.obj.nums ;
					   var page = data.obj.page;
					   $('#page').text(page);
					   var pageNum = data.obj.pageNum;
					   $('#pageNum').text(pageNum);
						var donateDate = '';
						var donateProject = '';
						var donateName = '';
						var donatAmount='';
						var htmlStr = '';
						var ret = data.obj.data;
						for (var i in ret) {
							var r = ret[i];
							donateDate = r.donatTimeStr;
							donateProject=r.projectTitle;
							donateName=r.nickName;
							donatAmount=r.donatAmount;
							htmlStr = htmlStr +'<div class="index_date mtop"><div class="index_two cut20 fl tc"><p>'+donateDate+'</p></div><div class="index_number cut80 fl"><span class="tu_1"></span><div class="index_project"><p>捐助项目：'+donateProject+'</p>';
							if(libraryUserId != r.userId){
								htmlStr = htmlStr +'<p>代捐人：'+donateName+'</p>';
							}
							htmlStr = htmlStr +'<p><span class="p1">¥'+donatAmount+'元</span></p></div></div></div>';
						}
						$('#add').append(htmlStr);
						if(nums <= pageNum){
							   $(".details_add").css('display','none'); 
						}
					 
					}else{				
					}	
				}					
			});
				$('#loadMore').click(function(){
				var page = $("#page").text() ;
				var pageNum = $("#pageNum").text();
			    page = Number(page) + 1;
				   $.ajax({
					url: 'http://www.17xs.org/goodlibrary/goodLibraryList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page,
						userId: libraryUserId
					},
					success: function(data) {
						if(data.flag == 1){
                       		var nums = data.obj.nums ;
					   		var page = data.obj.page;
					   		$('#page').text(page);
					   		var pageNum = data.obj.pageNum;
					   		$('#pageNum').text(pageNum);
					   		var sum = page * pageNum;
							var donateDate = '';
							var donateProject = '';
							var donateName = '';
							var donatAmount='';
							var htmlStr = '';
							var ret = data.obj.data;
							for (var i in ret) {
								var r = ret[i];
								donateDate = r.donatTimeStr;
								donateProject=r.projectTitle;
								donateName=r.nickName;
								donatAmount=r.donatAmount;
								htmlStr = htmlStr +'<div class="index_date mtop"><div class="index_two cut20 fl tc"><p>'+donateDate+'</p></div><div class="index_number cut80 fl"><span class="tu_1"></span><div class="index_project"><p>捐助项目：'+donateProject+'</p>';
								if(libraryUserId != r.userId){
									htmlStr = htmlStr +'<p>代捐人：'+donateName+'</p>';
								}
								htmlStr = htmlStr +'<p><span class="p1">¥'+donatAmount+'元</span></p></div></div></div>';
							}
							$('#add').append(htmlStr);
							if(sum >= nums){
							   $(".details_add").css('display','none'); 
							}
					    	}else{
							
							}	
				    	}					
			    	});
				})
				
				$('#veLibrary').click(function(){
					var userId=$('#userId').val();
					var lirbraryId=$('#lirbraryId').val();
					$.ajax({
						url: 'http://www.17xs.org/goodlibrary/isOrgoodLibrary.do',
						type:"POST",
						data:{
						userId:userId,
						libraryId:lirbraryId
						},
					success: function(result){
					if(result.flag == 1){
						location.href='http://www.17xs.org/goodlibrary/gotoBind.do?id='+userId+'&lirbraryId='+lirbraryId;
					}else{
					$('.prompt_box').show();
						/* $("#msg").html('<p>'+result.errorMsg+'</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000); */
						return ;
					}
				}
			});
		})
	})
	$('#submit').click(function(){
		$('.prompt_box').hide();
		location.href='http://www.17xs.org/index/index_h5.do';
	})
	$('#cancel').click(function(){
		$('.prompt_box').hide();
		return false;
	})
	function RefreshOnce(){
           if (location.href.indexOf("&num=") < 0) {
               location.href = location.href + "&num=" + Math.random();
           }
        }
</script>
</body>
</html>
