<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" /> 
<title>筹款项目</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/together/raising.css?v=20180118"/>
</head>
<body>
	<div id="pageScroll">
		<div class="raise">
			<div class="raise-head">
				<p>筹款项目</p>
				<div class="raise-project clearFix">
					<div class="project-pic">
						<img src="${project.coverImageUrl }" width="100%" height="70px"/>
					</div>
					<div class="project-content clearFix">
						<h4>${project.title }</h4>
						<p>${project.subTitle }</p>
					</div>
				</div>
				<div class="raise-progress">
					<div class="progress-bar">
						<span class="track">
							<i class="fill"></i>
						</span>
						<label>${process }%</label>
					</div>
					<div class="progress-info">
						目标金额${project.cryMoney }元，已募集${project.donatAmount }元
					</div>
				</div>
				
			</div>
			<div class="kong"></div>
			
			<div class="raise-bd">
				<p>发起形式</p>
				<div class="raise-way clearFix">
					<a href="javascript:;" class="btn fl person on">个人</a>
					<a href="javascript:;" class="btn fr group">团体</a>
				</div>
				
				<div class="raise-team">
					<p>团体名称</p>
					<div class="team-name">
						<input type="text" name="" id="groupName" placeholder="如:宁波市善园公益基金会" />
					</div>	
					<p>团体标识</p>
					<div class="team-pic">
							<form action="/file/upload3.do" method="post" id="form" enctype="multipart/form-data">
							<div class="add-list" id="imgList">
								<div class="item add">
									<a class="add-btn" href="javascript:;">
										<label for="file-input"></label>
										<input type="file" name="file" id="file-input" value="" />
										<input type="hidden" name="type" id="type" value="4" />
									</a>
								</div>	
							</div>
						</form>
					</div>
				</div>
				
				<div class="raise-introduction">
					<p>发起说明  ( 140字以内  )</p>
					<div class="introduction-text">
						<textarea id="content" placeholder="${leaveWord }"></textarea>
						<p class="letter-total">剩余<span class="letter-rest">140</span>字</p>
					</div>
				</div>
				<div class="raise-dest">
					<p>筹款目标</p>
					<c:choose>
						<c:when test="${project.id==3429}">
							<div class="dd-money">
								<div class="meiyuanshi-num">
									<a href="javascript:;" class="btn min fl">-</a>
									<a href="javascript:;" class="num fl">目标不限</a>
									<a href="javascript:;" class="btn add fl">+</a>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="dd-money">
								<ul class="dd-ul clearFix">
									<li class="on"><a href="javascript:;"><span>目标不限</span></a></li>
									<li class="user-defined"><a href="javascript:;">自定义</a></li>
								</ul>
								<div class="diyMoney">
									<input type="text" name="dMoney" id="dMoney" value="" />
								</div>
							</div>	
						</c:otherwise>
						
					</c:choose>
					<div class="total">
						目标不限
					</div>
				</div>
				
				<div class="dashed"></div>
				
				<div class="raise-next">
					<a href="javascript:;" class="next">下一步</a>
				</div>
			</div>
		</div>	
	</div>
	
	<input type="hidden" id="projectId" value="${project.id }"/>
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js" ></script>
	<script type="text/javascript" src="/res/layer/layer.js" ></script>
	<script data-main="/res/js/h5/together/raising.js?v=20180118" src="/res/js/require.min.js"></script>
</body>
</html>