<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta name="baidu-site-verification" content="SxG3ais7R3" />
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./common/file_url.jsp" %>
<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
<meta name="viewport"/>
<meta name="baidu-site-verification" content="LXxErKoY7i" />
<title>善园网—一起行善，互联网公益平台，大病筹款，公益众筹</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css?v=20180206"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNewlunbo.css"/>
<script type='text/javascript'>window.BWEUM||(BWEUM={});BWEUM.info = {"stand":true,"agentType":"browser","agent":"bi-collector.oneapm.com/static/js/bw-send-411.4.5.js","beaconUrl":"bi-collector.oneapm.com/beacon","licenseKey":"I66F7~vv2ai6V3Nn","applicationID":2282681};</script>
<script type="text/javascript" src="//bi-collector.oneapm.com/static/js/bw-loader-411.4.5.js"></script>
</head> 
<body>
<!--头部 start-->
<%@ include file="./common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="bodyNew">
  	<div class="indexBnner" id="slide_div">
    	<div class="w1000">
    	<a class="sbtn prev" style="display: inline;"></a>
        <a class="sbtn next" style="display: inline;"></a>
        </div>
        <div class="bnnerpic" id="scroll">
        	<c:forEach items="${bannerList}" var="img">
	       		<a href="${img.linkUrl}" title="" style="background:url('${img.url}') no-repeat center top / 100% 100%"></a>
	    	</c:forEach>
        </div>
    	<div class="bnnernum" id="scrollNum">
    		<c:forEach items="${bannerList}" varStatus="status">
       			<c:if test="${status.index == 0}"><a class="on" href="javascript:;"></a></c:if>
       			<c:if test="${status.index != 0}"><a href="javascript:;"></a></c:if>
	    	</c:forEach>
        </div>
    </div>
    <div class="indexSum">
    	<div class="w1000">
        	<span class="sumStart"><i class="sumIcon1"></i>
                  <div class="xiangmuleiji">累计发布<em><fmt:formatNumber type="number" pattern="#,##0" value = "${family}"/></em>个项目<br>
                  <div class="huodebangzhu">已有<em class="huodebangzhu_num">${helpPeople }</em>人获得帮助</div></div>
        	</span>
            <span class="sumMiddle"><i class="sumIcon2"></i>累计捐款额<em><fmt:formatNumber type="number" pattern="#,##0" value = "${totalMoney}"/></em>元</span>
            <span class="sumEnd"><i class="sumIcon3"></i>累计捐款<em><fmt:formatNumber type="number" pattern="#,##0" value = "${people}"/></em>人次</span>
        </div>
    </div>
    <div class="mainNew w1000">
    	<div class="lNew">
    		<c:if test="${isShowSF != 0}">
        	<div class="boederbox">
            	<h2>
                	<span class="lText">热门项目</span>
                    <span class="rText" id="projectTab">
                    	<c:forEach items="${tvbTitleList}" varStatus="status" var="tab">
			      			<c:if test="${status.index == 0}"><a class="on">${tab}</a></c:if>
			      			<c:if test="${status.index != 0}"><a>${tab}</a></c:if>
				    	</c:forEach>
                    </span>
                </h2>
                <div class="project" id="projectCon">
                	<c:forEach items="${specialFundList}" varStatus="status" var="project">
                		<c:if test="${status.index == 0}"><dl></c:if>
				      	<c:if test="${status.index != 0}"><dl style="display:none"></c:if>
				      		<a href = "http://www.17xs.org/project/view/?projectId=${project.id}">
		                    	<dt>
		                    	<c:if test="${project.coverImageUrl != null}"><img  src="${project.coverImageUrl}" alt=""></c:if>
		       			 		<c:if test="${project.coverImageUrl == null}"><img  src="<%=resource_url%>res/images/logo-def.jpg" alt=""></c:if>
		                    	</dt>
	                    	</a>
	                    	<c:if test="${project.state == 260}"><dd class="finish"></c:if>
       			 			<c:if test="${project.state == 240}"><dd></c:if>
	                        	<h3><a href = "http://www.17xs.org/project/view/?projectId=${project.id}&extensionPeople=${extensionPeople }">${project.title}</a></h3>
	                            <p class="description">${project.subTitle}</p>
	                            <p>累计募捐：<span class="orangeText"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/></span>元 </p>
	                            <p>累计捐款：<em class="fontArial">${project.donationNum}</em>人次</p>
	                            <p>设立时间：<em class="fontArial"><fmt:formatDate  value="${project.registrTime}" pattern="yyyy-MM-dd"/></em></p>
	                            <c:if test="${project.state == 260 && (project.special_fund_id == 0 || project.special_fund_id == null)}"><b class="grayBtn">募捐完成</b></c:if>
       			 				<c:if test="${project.state == 240}">
       			 				<c:choose>
									<c:when test="${userId == null}">
			                           	<a href="http://www.17xs.org/visitorAlipay/visitorpay.do?projectId=${project.id}&amount=${project.cryMoney - project.donatAmount}&pName=${project.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a>
									</c:when>
									<c:otherwise>
										<a href="http://www.17xs.org/alipay/commondpay.do?projectId=${project.id}&amount=${project.cryMoney - project.donatAmount}&pName=${project.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a>
									</c:otherwise>
								</c:choose>
       			 				</c:if>
	                        </dd>
                    	</dl>
				    </c:forEach>
                </div>
            </div>
            </c:if>
            <div class="boederbox mt15">
            	<h2>
                	<span class="lText">本周推荐</span>
                </h2>
                <ul class="pojectList">
                <c:forEach items="${rdList}" var="record" varStatus="status">
       			 	<li <c:if test="${status.index == 0}"> class = "first"</c:if> >
       			 		<c:choose>
       			 			<c:when test="${record.field =='garden'}">
       			 				<a href = "http://www.17xs.org/project/newGardenView/?projectId=${record.id}">
       			 			</c:when>
       			 			<c:otherwise>
       			 				<a href = "http://www.17xs.org/project/view/?projectId=${record.id}&extensionPeople=${extensionPeople }">
       			 			</c:otherwise>
   			 			</c:choose>
	       			 		<c:if test="${record.coverImageUrl != null}"><img src="${record.coverImageUrl}" alt=""></c:if>
	       			 		<c:if test="${record.coverImageUrl == null}"><img src="<%=resource_url%>res/images/logo-def.jpg" alt=""></c:if>
	                        <h3 title="${record.title}">${record.title}</h3>
                        </a>
                        <c:choose>
					        <c:when test="${record.state == 260  && (record.special_fund_id == 0 || record.special_fund_id == null)}">
								<p>已募集：<span class="orangeText fontArial"><fmt:formatNumber type="number" pattern="#,##0" value = "${record.donatAmount}"/></span>元 </p>
								<p>参与人次：<em class="fontArial">${record.donationNum}</em>人次</p>
                        		<b class="grayBtn">募捐完成</b>
							</c:when>
							<c:otherwise >
							   <c:if test="${record.special_fund_id == 0 ||record.special_fund_id == null }">
							   	<p>已募集：<span class="orangeText fontArial"><fmt:formatNumber type="number" pattern="#,##0" value = "${record.donatAmount}"/></span>元 </p>
							   </c:if>
							   <p>参与人次：<em class="fontArial">${record.donationNum}</em>人次</p>
							   <c:choose>
									<c:when test="${userId == null}">
									<c:if test="${record.field=='garden'}"><a href="http://www.17xs.org/project/buyproject.do?projectId=${record.id}&donateCopies=1&donatAmount=${record.cryMoney/record.totalCopies}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
									<c:if test="${record.field!='garden'}"><a href="http://www.17xs.org/visitorAlipay/visitorpay.do?projectId=${record.id}&amount=${record.cryMoney - record.donatAmount}&pName=${record.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${record.field=='garden'}"><a href="http://www.17xs.org/project/buyproject.do?projectId=${record.id}&donateCopies=1&donatAmount=${record.cryMoney/record.totalCopies}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
										<c:if test="${record.field!='garden'}"><a href="http://www.17xs.org/alipay/commondpay.do?projectId=${record.id}&amount=${record.cryMoney - record.donatAmount}&pName=${record.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
                   </li>
    			</c:forEach>
                </ul>
            </div>
				
			<div class="boederbox mt15">
            	<h2>
                	<span class="lText">募捐项目</span>
                    <span class="rText">
					<!-- <a title="" class="on" href = "http://www.17xs.org/project/index.do">热门</a>-->
                   	<c:forEach var="record" items="${atc}" varStatus="status">
                   	<c:if test="${record.typeName_e != 'good'}">
                  	<a v="${record.typeName_e}" href="http://www.17xs.org/project/index/?typeName=${record.typeName}&extensionPeople=${extensionPeople }">${record.typeName}</a>
                  	</c:if>
                  	</c:forEach>
                  	<a href="http://www.17xs.org/project/index/?extensionPeople=${extensionPeople }" title="" class="more">更多</a>
                    </span>
                </h2>
                <div class="serchCity">
                	<a href="http://www.17xs.org/project/index/?extensionPeople=${extensionPeople }" title="" class="on">全部</a>
                	<c:forEach var="province" items="${provinces}" varStatus="status">
                  	<a v="${province}" href="http://www.17xs.org/project/index/?province=${province}&extensionPeople=${extensionPeople }">${province}</a>
                  	</c:forEach>
                    <a href="http://www.17xs.org/project/index/?extensionPeople=${extensionPeople }" title="" class="more">更多</a>
                </div>
                <div class="cityProject">
	                <c:forEach items="${projectList}" var="p">
						<dl>
	                    	<dt>
	                    		<c:choose>
	                    			<c:when test="${p.field=='garden' }">
	                    				<a href = "http://www.17xs.org/project/newGardenView/?projectId= ${p.id }&extensionPeople=${extensionPeople }">
	                    			</c:when>
	                    			<c:otherwise>
	                    				<a href = "http://www.17xs.org/project/view/?projectId= ${p.id }&extensionPeople=${extensionPeople }">
	                    			</c:otherwise>
	                    		</c:choose>
		                    		<c:if test="${p.coverImageUrl != null}"><img  src="${p.coverImageUrl}" alt=""></c:if>
	       			 				<c:if test="${p.coverImageUrl == null}"><img  src="<%=resource_url%>res/images/logo-def.jpg" alt=""></c:if>
	                    		</a>
	                        </dt>
	                        <c:if test="${p.state == 260}"><dd class="finish"></c:if>
       			 			<c:if test="${p.state == 240}"><dd></c:if>
	   			 				<c:choose>
	                    			<c:when test="${p.field=='garden' }">
	                    				<a href = "http://www.17xs.org/project/newGardenView/?projectId= ${p.id }&extensionPeople=${extensionPeople }">
	                    			</c:when>
	                    			<c:otherwise>
	                    				<a href = "http://www.17xs.org/project/view/?projectId= ${p.id }&extensionPeople=${extensionPeople }">
	                    			</c:otherwise>
	                    		</c:choose>
		                        	<h3>
		                        		<c:if test="${fn:length(p.title)<=20}">${p.title}</c:if>
		                        		<c:if test="${fn:length(p.title)>20}">${fn:substring(p.title, 0, 20)}...</c:if>
		                        	</h3>
	            				</a>
	                            <p>${p.information}</p>
	                            <div class="progress">
	                            	<span class="pgGray">
	                                	<span class="pgRed" style="width:${p.donatePercent }%"></span>
	                                </span>
	                                <span class="pgText">${p.donatePercent }%</span>
	                            </div>
	                        	<c:choose>
			                        <c:when test="${p.state == 260 && (project.special_fund_id == 0 || project.special_fund_id == null)}">
									  	<p>已募集：<span class="orangeText fontArial"> <fmt:formatNumber type="number" pattern="#,##0.00#" value = "${p.donatAmount}"/></span>元 </p>
			                            <p>参与人次：<em class="fontArial">${p.donationNum }</em>人次</p>
			                            <b href="javascript:void(0);" title="" class="grayBtn">募捐完成</b>
									</c:when>
									<c:otherwise >
										    <p>目标金额：<span class="orangeText fontArial">
			                          			  	<fmt:formatNumber type="number" pattern="#,##0.00#" value = "${p.cryMoney}"/>
			                            		</span>元
										    </p>
			                            <p>参与人次 ：<em class="fontArial">${p.donationNum }</em>人次</p>
										<c:choose>
											<c:when test="${userId == null}">
												<c:if test="${p.field=='garden'}"><a href="http://www.17xs.org/project/buyproject.do?projectId=${p.id}&donateCopies=1&donatAmount=${p.cryMoney/p.totalCopies}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
					                           	<c:if test="${p.field!='garden'}"><a href="http://www.17xs.org/visitorAlipay/visitorpay.do?projectId=${p.id}&amount=${p.leaveCryMoney}&pName=${p.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${p.field=='garden'}"><a href="http://www.17xs.org/project/buyproject.do?projectId=${p.id}&donateCopies=1&donatAmount=${p.cryMoney/p.totalCopies}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
												<c:if test="${p.field!='garden'}"><a href="http://www.17xs.org/alipay/commondpay.do?projectId=${p.id}&amount=${p.leaveCryMoney}&pName=${p.title}&extensionPeople=${extensionPeople }" title="" class="projectBtn">我要捐款</a></c:if>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
	                        </dd>
	                    </dl>
	    			</c:forEach>
                    <p class="cpMore">
                    	<a href="http://www.17xs.org/project/index/?extensionPeople=${extensionPeople }" title=""> 
                    		<i class="iconNew iconSearch"></i>更多项目
                    	</a>
                    </p>
                </div>
             </div>
             
			  <%-- 	
             <div class="boederbox mt15"  id="pjProject">
             	<h2>
                	<span class="lText">众筹项目</span>
                    <span class="rText" id="pjProjectTab">
                    	<a title="" class="on">公益众筹</a>
                    </span>
                </h2>
                
                <div class="cityProject">
                	 <c:forEach items="${gardenList}" varStatus="status" var="project">
                		<dl>
	                    	<dt>
	                    	<a href="http://www.17xs.org/project/newGardenView/?projectId=${project.id}">
	                    	<c:if test="${project.coverImageUrl != null}"><img  src="${project.coverImageUrl}" alt="${project.title}"></c:if>
	       			 		<c:if test="${project.coverImageUrl == null}"><img  src="<%=resource_url%>res/images/logo-def.jpg" alt="${project.title}"></c:if>
	       			 		</a>
	       			 		</dt>
	                        <dd>
	                        	<a href="http://www.17xs.org/project/newGardenView/?projectId=${project.id}"><h3>${project.title}</h3></a>
	                            <p class="pjDetail">${project.subTitle}</p>
	                            <p>单价：<span class="orangeText fontArial"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.cryMoney / project.totalCopies}"/></span>元 </p>
	                            <p>剩余： <em class="fontArial">${project.leaveCopies}</em>份</p>
	                            <a href="http://www.17xs.org/project/gardenview/?projectId=${project.id}" title="" class="projectBtn">我要捐款</a>
	                        </dd>
                    	</dl>
				    </c:forEach>
                     <p class="cpMore">
                    	<a href="http://www.17xs.org/project/gardenindex/" title=""> <i class="iconNew iconSearch"></i>更多项目</a>
                     </p>
                </div>
             </div>
            --%>
        </div>
        <div class="rNew">
        	<div class="indexIdenty">
            	<a href="http://www.17xs.org/project/appealFirest/" title="">我要求助</a>
                <a href="http://www.17xs.org/project/index/?extensionPeople=${extensionPeople }" title="" class="idBtnEnd">我要捐款</a>
            </div>
            <div class="boederbox mt15">
            	<h2>
                	<span class="lText">新闻公告</span>
                    <span class="rText">
                        <a href="<%=web_url%>news/center/?type=1" class="more" title="">更多>></a>
                    </span>
                </h2>
                <div class="adList mt10">
                	<c:forEach items="${latestNews}" var="news">
                    	 <p data="${news.id}">
                    	    <c:if test="${fn:length(news.title)>14}">
                    	    	<a href="<%=web_url%>news/center/?id=${news.id}&type=1" title=""><span class="listIcon"></span>${fn:substring(news.title, 0, 14)}...</a>
						    </c:if>
						    <c:if test="${fn:length(news.title)<=14}">
						       <a href="<%=web_url%>news/center/?id=${news.id}&type=1" title=""><span class="listIcon"></span>${news.title}</a>
						    </c:if>
                    </c:forEach>
                </div>
                <h2>
                	<span class="lText">慈善资讯</span>
                    <span class="rText">
                        <a href="<%=web_url%>news/center/?type=3" class="more" title="">更多>></a>
                    </span>
                </h2>
                <div class="activeList">
                	<c:forEach items="${loveNews}" var="news">
                		<p data="${news.id}">
                	    <c:if test="${fn:length(news.title)>15}">
                	     	<dl>
		                    	<dt>
	                    			<a href="<%=web_url%>news/center/?id=${news.id}&type=3" ><img src="${news.coverImageUrl }" alt=""></a>
		                        </dt>
		                        <dd>
		                        	<p class="time fontArial"><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM-dd"/></p>
		                            <p><a href="<%=web_url%>news/center/?id=${news.id}&type=3" > ${fn:substring(news.title, 0, 15)}...</a></p>
		                        </dd>
		                    </dl>
					    </c:if>
					    <c:if test="${fn:length(news.title)<=15}">
					       <dl>
		                    	<dt>
		                        	<a href="<%=web_url%>news/center/?id=${news.id}&type=3" ><img src="${news.coverImageUrl }" alt=""></a>
		                        </dt>
		                        <dd>
		                        	<p class="time fontArial"><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM-dd"/></p>
		                            <p><a href="<%=web_url%>news/center/?id=${news.id}&type=3" >${news.title}</a></p>
		                        </dd>
		                    </dl>
					    </c:if>
                	</c:forEach>
                </div>
                <h2 class="mt10">
                	<span class="lText">最新捐款</span>
                    <span class="rText">
                        <a href="http://www.17xs.org/index/donationsListNew/" title="">更多>></a>
                    </span>
                </h2>
                <div class="dtList" id="dtList">
                	<ul>
                	<c:forEach items="${donateRecord}" var="donateRecord">
                		<li><dl>
	                    	<dt>
	                    	<c:choose>
                    			<c:when test="${donateRecord.coverImageurl != null}"><img src="${donateRecord.coverImageurl }" alt=""></c:when>
                    			<c:otherwise><img src="http://www.17xs.org/res/images/user/user-icon-gt.png" alt=""></c:otherwise>
                    		</c:choose>
	                    	</dt>
	                        <dd>
	                        	<p class="title">
	                        	<c:choose>
								  <c:when test="${fn:startsWith(donateRecord.nickName,'游客') }">
								          爱心人士
								  </c:when>
								  <c:when test="${fn:length(donateRecord.nickName)<=6}">${donateRecord.nickName}</c:when>
								  <c:otherwise>${fn:substring(donateRecord.nickName, 0, 6)}...</c:otherwise>
								</c:choose>
                        	 	<span class="time fontArial">${donateRecord.timeStamp }</span></p>
	                            <p>捐助<c:choose>
	                            	<c:when test="${donateRecord.field =='garden'}"><a href="http://www.17xs.org/project/newGardenView/?projectId=${donateRecord.projectId }"></c:when>
	                            	<c:when test="${donateRecord.field =='good'}"><a href="http://sy.17xs.org/project/syview.do?projectId=${donateRecord.projectId }"></c:when>
	                            	<c:otherwise><a href="http://www.17xs.org/project/view/?projectId=${donateRecord.projectId }"></c:otherwise>
	                            </c:choose>
	                            <c:choose>
								  <c:when test="${fn:length(donateRecord.recipientName)>=7}">${fn:substring(donateRecord.recipientName, 0, 7)}...</c:when>
								  <c:when test="${donateRecord.field =='garden'}">善园基金会</c:when>
								  <c:otherwise>${donateRecord.recipientName }</c:otherwise>
								</c:choose></a>
	                            <span class="num fontArial">${donateRecord.donatAmount }</span>元</p>
	                        </dd>
	                    </dl></li>
                	</c:forEach>
                	</ul>
                </div>
            </div>
            <div class="boederbox mt15" id="house">
            	<h2>
                	<span class="lText">善管家</span>
                    <span class="rText" id="houseTab">
                        <a title="" class="on" href="javascript:;">机构</a>
                        <a title="" href="javascript:;">个人</a>
                    </span>
                </h2>
                <div class="houseList">
                	<c:forEach items="${eloveGroup}" var="eloveGroup">
	                	<dl>
	                    	<dt>
	                    	<c:choose>
                    			<c:when test="${eloveGroup.coverImageUrl != null}"><img src="${eloveGroup.coverImageUrl }" alt=""></c:when>
                    			<c:otherwise><img src="http://www.17xs.org/res/images/user/user-icon-gt.png" alt=""></c:otherwise>
                    		</c:choose>
	                        </dt>
	                        <dd>
	                        	<h3 title="${eloveGroup.userName }">
	                        	<c:if test="${fn:length(eloveGroup.userName)>10 }">${fn:substring(eloveGroup.userName,0,10)}...</c:if>
	                        	<c:if test="${fn:length(eloveGroup.userName)<=10 }">${eloveGroup.userName}</c:if>
	                        	</h3>
	                            <p title="${eloveGroup.province }${eloveGroup.city }">${eloveGroup.area }</p>
	                            <p class="fontArial"><fmt:formatDate  value="${eloveGroup.registrTime}" pattern="yyyy-MM-dd"/></p>
	                        </dd>
	                    </dl>
                    </c:forEach>
                    <p class="cpMore">
                    	<a href="http://www.17xs.org/goodlibrary/getGoodLibraryList.do?type=3" title=""> <i class="iconNew iconSearch"></i>更多成员</a>
                     </p>
                </div>
                <div class="houseList" style="display:none">
                	<c:forEach items="${ploveGroup}" var="ploveGroup">
	                	<dl>
	                    	<dt>
	                    	<c:choose>
                    			<c:when test="${ploveGroup.coverImageUrl != null}"><img src="${ploveGroup.coverImageUrl }" alt=""></c:when>
                    			<c:otherwise><img src="http://www.17xs.org/res/images/user/user-icon-gt.png" alt=""></c:otherwise>
                    		</c:choose>
	                        </dt>
	                        <dd>
	                        	<h3 title="${ploveGroup.userName }">
	                        	<c:if test="${fn:length(ploveGroup.userName)>10 }">${fn:substring(ploveGroup.userName,0,10)}...</c:if>
	                        	<c:if test="${fn:length(ploveGroup.userName)<=10 }">${ploveGroup.userName}</c:if>
	                        	</h3>
	                            <p title="${ploveGroup.province }${ploveGroup.city }">${ploveGroup.area }</p>
	                            <p class="fontArial"> <fmt:formatDate  value="${ploveGroup.registrTime}" pattern="yyyy-MM-dd"/></p>
	                        </dd>
	                    </dl>
                    </c:forEach>
                    <p class="cpMore">
                    	<a href="javascript:;" title=""> <i class="iconNew iconSearch"></i>更多成员</a>
                     </p>
                </div>
            </div>
            <div class="boederbox mt15" id="house">
            	<h2>
                	<span class="lText">善款去向</span>
                    <span class="rText">
                        <a title="" href="http://www.17xs.org/index/donationWhere/">更多>></a>
                    </span>
                </h2>
                <ul class="DirtList">
                     <c:forEach var="pay" items="${payList}" varStatus="status">
                     	 <li>
	                    	<p class="time fontArial">${pay.payDate}</p>
	                        <p title ="${pay.projectTitle}" class="ztname" >${pay.projectTitle}</p>
	                        <p><span class="nameobj">${pay.realName }</span><span>提取善款  <em class="fontArial">${pay.panMoney}</em>元</span></p>
                    	</li>
                  	 </c:forEach>
                </ul>
            </div>
        </div>
        <div class="boederbox mt15 loveProject">
        	<h2>
                <span class="lText">爱心企业</span>
                <span class="rText">
                    <a title="" href="javascript:;">更多>></a>
                </span>
            </h2>
            <div class="loveList">
                <c:forEach var="lc" items="${loveCompany}">
                	<a href="${lc.linkUrl }" title="" target="_blank"><img src="${lc.url}"  alt=""/></a>
                </c:forEach>
            </div>
        </div>
        
        <div class="friendlylink">
        	<h2>
                <span class="lText">友情链接</span>
        	</h2>
          <ul>
          	<c:forEach items="${linkImg}" var="img">
     			 <li><a href="${img.linkUrl}" title="" target="_blank">${img.description}</a></li>
	    	</c:forEach>
          </ul>
        </div>
    </div>
</div>
<!--主体 end-->
<div class="footPrse">
	<div class="w1000">
    	<h2>我们的使命</h2>
        <p>让求助更方便快捷&nbsp;&nbsp;&nbsp;&nbsp;让行善更精准轻松</p>
        <div class="prseList">
        	<span>
        		<a href="http://www.17xs.org/help/aboutPromise/">
                	<i class="icon1"></i>
                    <em>善款透明</em>
                </a>
            </span>
            <span>
            	<a href="http://www.17xs.org/help/aboutPromise/#b">
                	<i class="icon2"></i>
                    <em>大众参与</em>
                </a>
            </span>
            <span>
            	<a href="http://www.17xs.org/help/aboutPromise/#c">
                	<i class="icon3"></i>
                    <em>全程跟进</em>
                </a>
            </span>
            <span>
            	<a href="http://www.17xs.org/help/about/">
                	<i class="icon4"></i>
                    <em>关于善园基金会</em>
                </a>
            </span>
        </div>
    </div>
</div>
   <%--pc扫二维码登录成功弹框--%>
<c:if test="${fn:startsWith(user.userName,'weixin')}">
	<div class="succeed_login" id="prompt_weixin">
		<div class="login_next">
			<div class="login_next1">
				<span></span>
				<p>欢迎您登录善园网，从此开始行善之旅</p>
		 	</div>
			<div class="login_000"></div>
			<div class="login_next2">
				<li><p>账号:</p><span>${user.userName }</span><!-- <a href="">修改账号</a> --></li>
				<li><p>密码:</p><span>************</span></li>
				<li><p>昵称:</p><span>${user.nickName }</span></li>
			</div>
			<div class="login_next3">
				<a href="<%=resource_url%>ucenter/getUserDetail.do"><div class="login_fl"><p>完善账号信息</p></div></a>
				<a href="javascript:void(0);" id="ignore"><div class="login_fr"><p>以后再说</p></div></a>
			</div>
		</div>
	</div>
</c:if>
<input type="hidden" id="extensionPeople" name="extensionPeople" value="${extensionPeople }">
<!--底部 start-->
<%@ include file="./common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/indexNew.js?v=20180206" src="<%=resource_url%>res/js/require.min.js"></script>
<!-- 百度统计 -->
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1257726653'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1257726653' type='text/javascript'%3E%3C/script%3E"));</script>
<div class="renz" style="height:70px;">
	<!--<div style="float: left;margin-left: 40%;">
		<a href="http://webscan.360.cn/index/checkwebsite/url/www.17xs.org">
			<img border="0" style="width: 80px;height: 30px;" src="http://img.webscan.360.cn/status/pai/hash/35fa1b524e77bfbbf4daab2ad8d4a128"/>
		</a>
	</div>-->
	<div style="text-align: center;">
		<a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=33021202000300" style="display: inline-block;text-decoration: none;height: 20px;line-height: 20px;margin-top: 4px;margin-left: 10px;">
			<img src="http://www.beian.gov.cn//img/ghs.png" style="float:left;"/>
			<p style="float:left;height:20px;line-height:20px;margin: 0px 0px 0px 5px; color:#939393;">浙公网安备 33021202000300号</p>
		</a>
	</div>
</div>
	
<script>
	(function(){
	    var bp = document.createElement('script');
	    var curProtocol = window.location.protocol.split(':')[0];
	    if (curProtocol === 'https') {
	        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
	    } else {
	        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
	    }
	    var s = document.getElementsByTagName("script")[0];
	    s.parentNode.insertBefore(bp, s);
	})();
</script>

</body>
</html>
