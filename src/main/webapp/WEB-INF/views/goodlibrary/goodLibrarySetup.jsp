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
<title>设置条件</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodlibrary/good_librarypc.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodlibrary/library_setup.css" />    
<!-- <script type="text/javascript" src="js/jquery-1.8.2.min.js"></script> -->
<script type="text/javascript">
	function tagClick(index){
		$('.spanClick'+index).toggleClass('inputdh');
		//alert($('.spanClick'+index).html());
	}
</script>
</head> 
<!--头部 end-->
<!--主体 start-->
<body>
<%@ include file="./../common/newhead.jsp" %><!-- <div class="bodyer userCenter mygood"></div>  -->
<div class="page">
    <div class="page-bd">
				<c:choose>
				    <c:when test="${userType=='enterpriseUsers'}">
				        <div class="uCEN-L">
                   		<%@ include file="./../common/eleft_menu.jsp"%>
                   		</div>
                    </c:when>
                    <c:otherwise>
	                    <div class="uCEN-L">
						<%@ include file="./../common/pleft_menu.jsp"%>
					   </div>
                    </c:otherwise>  
				</c:choose>
       <div class="uCEN-R">
            <p class="libraryone">设置加入条件</p>
            <div class="librarythree">
                <div class="rlitem">
                    <label>设置加入条件：</label>
                    <div class="rltext">
                        <div class="setup">
                            <div class="setup_fl" id="appointPerson_btn">
                                <div class="setup_input inputdh zdr"></div>
                                <span>指定的人才能加入</span>
                            </div>
                            <div class="setup_fr" id="appointArea_btn">
                                <div class="setup_input zddq"></div>
                                <span>指定的地区的人才能加入</span>
                            </div>
                        </div>
                    </div>
                </div>
                <!--指定的人才能加入显示-->
                <div class="rlitem rlitem1" id="appointPerson">
                    <label>成员手机号：</label>
                    <div class="rltext">
                        <textarea class="personFr1" placeholder="请输入成员手机号,以英文逗号隔开（如：132***,156***）" id="appointMobile"></textarea>
                        <span class="wrinfo"></span>
                    </div>
                </div>
                <!--指定地区的人才能加入显示-->
                <div class="rlitem rlitem1" style="display: none" id="appointArea">
                    <label>指定成员地区：</label>
                    <div class="rltext">
                        <textarea class="personFr1" placeholder="请输入指定成员地区,以英文逗号隔开（如：浙江省,宜昌市,北京市）" id="appointPersonArea"></textarea>
                        <span class="wrinfo"></span>
                    </div>
                </div>
                <div class="rlitem">
                    <label>输入成员可用金额：</label>
                        <div class="rltext">
                            <input type="number" class="inputtext inputcode" id="defaultMoney">元
                            <span class="wrinfo"></span>
                        </div>
                    </div>
                </div>
            <div class="rlitem_set">
                <label>指定捐赠项目：</label>
                <div class="setup">
                <c:forTokens items="${tag }" delims="," var="tags"  varStatus="status">
                	<li class="setup_fl tagss" onclick="tagClick(${status.count})">
                	<%-- <input type="hidden" value="${tags }"/> --%>
                        <div class="setup_input spanClick${status.count}"><input type="hidden" value="${tags }"/></div>
                        <span>${tags }</span>
                    </li>
                </c:forTokens>
                    <!-- <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>不限</span>
                    </li>
                    <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>儿童</span>
                    </li>
                    <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>贫困救济</span>
                    </li>
                    <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>贫困救济</span>
                    </li>
                    <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>贫困救济</span>
                    </li>
                    <li class="setup_fl">
                        <div class="setup_input"></div>
                        <span>贫困救济</span>
                    </li> -->
                </div>
                <div style="clear: both"></div>
            </div>
            <div class="rlitem rlitem1">
                <label>指定捐赠地区：</label>
                <div class="rltext">
                    <textarea class="personFr1" placeholder="请输入指定地区捐赠地区,以英文逗号隔开（如：浙江省,宜昌市,北京市）" id="appointDonateArea"></textarea>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <a href="javascript:void(0);" id="setGoodLibrary"><div class="rltext "><p class="data_button">开通善库</p></div></a>
           </div>
    </div>
    </div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="goodlibraryId" value="${libraryId }"/> 
<input type="hidden" id="type" value="1"/>
<script data-main="<%=resource_url%>res/js/dev/goodlibrary/goodLibrarySet.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>