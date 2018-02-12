<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
	<!-- 公共底部 -->
	<div class="buttonBox">
		<!-- 按钮灰色去掉active，橙色加active -->
		<ul class="buttonList clearfix">
			<li class="buttonItem"><a href="javascript:void(0);" class="xsBtn"></a></li>
			<li class="buttonItem"><a href="javascript:void(0);" class="qzBtn"></a></li>
			<li class="buttonItem"><a href="javascript:void(0);" class="xxBtn"></a></li>
			<li class="buttonItem active"><a href="javascript:void(0);" class="wdBtn"></a></li>
		</ul>
		<script type="text/javascript">
			$(document).on('click','.xsBtn',function(){
				location.href = "<%=resource_url%>index/index_h5.do";
			});
			$(document).on('click','.qzBtn',function(){
				<%-- location.href = '<%=resource_url%>ucenter/core/personalCenter_h5.do'; --%>
				location.href = '<%=resource_url%>uCenterProject/uCenterProjectList.do?state=210&currentPage=1';
			});
			$(document).on('click','.xxBtn',function(){
				alert('暂未开放！');
			});
			$(document).on('click','.wdBtn',function(){
				location.href = "<%=resource_url%>ucenter/userCenter_h5.do";
			});
		</script>
	</div>
