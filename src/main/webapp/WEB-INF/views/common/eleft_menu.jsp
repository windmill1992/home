<%@page import="com.guangde.home.utils.UserUtil"%>
<%@page import="com.guangde.entry.ApiCompany"%>
<%@page import="com.guangde.api.user.ICompanyFacade"%>
<%@page import="com.guangde.home.utils.SpringContextUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!-- ===============================新版企业用户与个人用户导航统一起来================================ -->

<div class="uCEN-L funlist_left">
	<div class="fun_profile">
        <div class="fnro_title"><i></i><a href="<%=resource_url%>ucenter/core/enterpriseCenter.do">用户中心首页</a></div> 
        <ul class="uCEN-nav">
        	<%-- <li id="m-Mydetail" href="<%=resource_url%>ucenter/getUserDetail.do">我的资料<i></i></a></li> --%>
        	 <li id="m-Mydetail"><a href="<%=resource_url%>ucenter/getUserDetail.do">我的资料<i></i></a></li>
	        <li id="m-name"><a href="<%=resource_url%>user/realname.do">实名认证<i></i></a></li>
	        <li id="m-pwd"><a href="<%=resource_url%>ucenter/core/uCenterPassword.do">修改密码<i></i></a></li>
	        <li id="m-msg"><a href="<%=resource_url%>ucenter/core/msg.do">系统信息<i></i></a></li>
        </ul>
        <div class="fnro_title"><i></i>我是行善者</div>
        <ul class="uCEN-nav">
	        <li id="m-jk"><a href="<%=resource_url%>ucenter/core/mygood.do">捐赠记录<i></i></a></li>
	        <li id="m-acccountTracking"><a href="<%=resource_url%>ucenter/core/personTracking.do">善款跟踪<i></i></a></li>
	        <li id="m-focus"><a href="<%=resource_url%>ucenter/core/focusProject.do">关注的项目<i></i></a></li>
	        <li id="m-getInvoice"><a href="<%=resource_url%>ucenter/getInvoicePage.do">索取发票<i></i></a></li>
            <li id="m-invoiceList"><a href="<%=resource_url%>ucenter/getInvoicList.do">开票记录<i></i></a></li>
	        <li><a href="<%=resource_url%>project/index.do" target="_blank">我要行善<i></i></a></li>
        </ul>
        <div class="fnro_title"><i></i>项目管理</div>
        <ul class="uCEN-nav">
        	<li id="m-rl"><a href="<%=resource_url%>uCenterProject/projectClaim_view.do">认领项目<i></i></a></li>
	        <li id="m-qz"><a href="<%=resource_url%>ucenter/pindex.do">我的项目<i></i></a></li>
	        <li id="m-hz"><a href="<%=resource_url%>ucenter/core/donatelistByuser.do">获捐记录<i></i></a></li>
	        <li id="m-capital"><a href="<%=resource_url%>ucenter/core/personalcapital.do">提现记录<i></i></a></li>
	        <li id="m-widthDraw"><a href="<%=resource_url%>ucenter/pindex.do?flag=withdraw">我要提现<i></i></a></li>
	        <li id="m-topic"><a href="<%=resource_url%>projectTopic/gotoMyTopic.do">我的专题<i></i></a></li>
        </ul>
        <div class="fnro_title"><i></i>企业机构</div>
        <ul class="uCEN-nav">
	        <li id="p-einformation"><a href="<%=resource_url%>enterprise/core/realName.do">机构认证<i></i>
	        <em
				class="rzMark"> 
				<%
				    Integer userId = UserUtil.getUserId(request, response);
				    ICompanyFacade companyFacade = SpringContextUtil.getBean("companyFacade",ICompanyFacade.class);
				    ApiCompany tempc = new ApiCompany();
				    tempc.setUserId(userId);
				    tempc = companyFacade.queryCompanyByParam(tempc);
				    if(tempc==null||tempc.getState()!=203){
				       out.print("(未认证)");
				    }
				%>
				<c:if test="${company.state!=203}"></c:if>
			</em>
	        </a></li>
	        <li id="m-group"><a href="<%=resource_url%>ucenter/core/myloveGroup.do">善管家<i></i></a></li>
	        <li id="p-staffmanagement"><a href="<%=resource_url%>enterprise/core/employeList.do">善员工<i></i></a></li>
	        <li id="realtionProject"><a href="<%=resource_url%>ucenter/pindex.do?flag=realtionProject">相关项目<i></i></a></li>
        </ul>
        <div class="fnro_title"><i></i>爱心助善</div>
        <ul class="uCEN-nav">
	        <li id="p-ehelpgoodarea"><a href="<%=resource_url%>enproject/core/zhuShanList.do">助善项目<i></i></a></li>
	        <li id="p-donationdetail"><a href="<%=resource_url%>enterprise/core/enterpriseCharityDetail.do">助善明细<i></i></a></li>
        </ul>
        <div class="fnro_title"><i></i>资金管理</div>
        <ul class="uCEN-nav">
	        <li id="p-recharge"><a href="<%=resource_url%>ucenter/core/chargeonlineBank.do">充值<i></i></a></li>
	        <li id="p-capitaldetail"><a href="<%=resource_url%>enterprise/core/entDetailsFunds.do">资金明细<i></i></a></li>
        </ul>
	</div>
</div>