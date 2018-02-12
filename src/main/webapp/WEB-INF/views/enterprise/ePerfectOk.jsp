<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entLegalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../common/eleft_menu.jsp"%>
            </div>
            <div class="uCEN-R"> 
                <div class="entInfo">
                    <h2>完善公司信息<span>（完善公司信息）</span><a  id="entExplain" class="more">什么是企业助善</a></h2>
                     <div class="rlgroup">
                        <label>公司名称：</label>
                        <div class="controls controlsn">
                            <div class="rlinput font14">
                              ${company.name}
                            </div>
                        </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司头像：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                               <span class="entLogo"><img src="${company.coverImageUrl}" alt=""> 
								 
							   </span>
                            </div>
                            
                        </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司LOGO：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                            	<span class="entfileBtn">
                                	公司LOGO上传<br>(企业助善区)
                                     <div class="entfile">
                               			 <img src="${company.conpanyImageUrl}" alt="">
                               		 </div>
                                </span>
                              	
                            </div>
                            
                        </div>
                    </div>
                    <div class="rlitem rlitemUrl mt30">
                            <label>网址：</label>
                            <div class="rltext">
                                <div class="rlinputText">
                                 ${company.url}
                                </div>
                               
                            </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司简介：</label>
                        <div class="controls controlsn conborderbom">
                            <div class="rlinputText">
                               ${company.infomation}
                            </div>
                           
                        </div>
                    </div>
                    <div class="rlitem mt80 entlitem">
                        <label></label>
                        <div class="rltext">
                        	<a href="<%=resource_url%>enproject/company.do" title="" class="rlbtnok yh"> 别人眼中的你</a>
                            <a type="button" class="rlbtnok yh" href="<%=resource_url%>/enterprise/core/perfect.do" >修改</a>
                            <span class="rgray">|</span>
                            <a href="<%=resource_url%>enterprise/core/employeList.do" title="" class="entlink">善员工管理</a>
                            <a href="<%=resource_url%>project/index.do" title="" class="entlink">行善大厅</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/entInfo.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>