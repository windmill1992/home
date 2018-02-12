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
<title>善园网 - 我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/needhelp.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
</head> 

<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer">
  <div class="page clear">
    <!-- <div class="page-hd">
      <h2 class="w1000">我要求助</h2>
    </div> -->
    <div class="page-bd">
    	<div class="publish">
        	<div class="step-progress step3">            	
            </div>
            <div class="step-detail">
           	  <div class="person_info" ng-app="personInfoApp" ng-controller="personInfoCon">
                  <dl class="col">
                   	  <!--<dt>发布人信息</dt>-->
                   	  <dd><label><i class="important">*</i>发起人性质：</label>
                   	  	<span class="radio">
                   	  		<label><input type="radio" ng-model="com" ng-value="true" name="com1" id="dw"/>
                   	  			<span class="radio-left">公益组织/政府单位/事业单位/公司</span>
                   	  		</label><label>
                   	  		<input type="radio" ng-model="com" ng-value="false" name="com1" id="gr"/><span class="radio-left">个人</span></label>
                   	  	</span></dd>
                      <c:choose>
                      	<c:when test="${help=='' }">
                      	  <dd ng-hide="com"><label><i class="important">*</i>姓名：</label><span class="value"><input name="" type="text" id="fname" value="${fbr.realName}"></span></dd>
	                      <dd ng-hide="com"><label><i class="important">*</i>电话：</label><span class="value"><input name="" type="text" id="fPhone" value="${fbr.linkMobile}"></span></dd>
	                      <dd ng-hide="com">
	                        <label><i class="important">*</i>身份证号码：</label><span class="value"><input name="" type="text" id="fIdcard" value="${fbr.indetity}"></span>
	                      </dd>
	                      <dd ng-hide="com">
	                        <label><i class="important">*</i>受助人关系：</label>
	                        <span class="value">
	                        	<select class="relaty" id="relaty">
	                        		<option <c:if test="${fbr.relation == '请选择'}">selected="selected"</c:if>>请选择</option>
	                        		<option <c:if test="${fbr.relation == '本人'}">selected="selected"</c:if>>本人</option>
	                        		<option <c:if test="${fbr.relation == '直系亲属'}">selected="selected"</c:if>>直系亲属</option>
	                        		<option <c:if test="${fbr.relation == '亲戚朋友同学'}">selected="selected"</c:if>>亲戚朋友同学</option>
	                        		<option <c:if test="${fbr.relation == '志愿者'}">selected="selected"</c:if>>志愿者</option>
	                        		<option <c:if test="${fbr.relation == '病友'}">selected="selected"</c:if>>病友</option>
	                        		<option <c:if test="${fbr.relation == '其他'}">selected="selected"</c:if>>其他</option>
	                        	</select>
	                        </span>
	                      </dd>
                      	</c:when>
                      	<c:otherwise>
                      	  <dd ng-hide="com"><label><i class="important">*</i>姓名：</label><span class="value"><input name="" type="text" id="fname" value="${user.realName}"></span></dd>
	                      <dd ng-hide="com"><label><i class="important">*</i>电话：</label><span class="value"><input name="" type="text" id="fPhone" value="${user.mobileNum}"></span></dd>
	                      <dd ng-hide="com">
	                        <label><i class="important">*</i>身份证号码：</label><span class="value"><input name="" type="text" id="fIdcard" value="${user.idCard}"></span>
	                      </dd>
	                      <dd ng-hide="com">
	                        <label><i class="important">*</i>受助人关系：</label>
	                        <span class="value"><!--<input name="" type="text" id="identity" value="${fbr.relation}">-->
	                        	<select class="relaty" id="relaty">
	                        		<option>请选择</option>
	                        		<option>本人</option>
	                        		<option>直系亲属</option>
	                        		<option>亲戚朋友同学</option>
	                        		<option>志愿者</option>
	                        		<option>病友</option>
	                        		<option>其他</option>
	                        	</select>
	                        </span>
	                      </dd>
                      	</c:otherwise>
                      </c:choose>
                      <!--<dd><label>职业：</label><span class="value"><input name="" type="text" id="zy" value="${fbr.vocation}"></span></dd>-->
                      <dd ng-hide="com"><label>身份证照片：</label>
                      	<c:choose>
                      		<c:when test="${apiAuditStaff==null }">
                      			<span class="value">
		                      		请上传身份证正反面及手持照片，上传照片字体清晰可见
		                      		<span class="realNameIdentity">，</span><a href="javascript:;" class="Identity realNameIdentity">点此实名认证</a>
		                      		<br />
		                      		<form id="form6" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
			                      		<div class="add_list" id="imgList6">
			                      		<c:choose>
			                      			<c:when test="${szr.helpType==7 && imgId6!=null }">
			                      				<div class="item" id="${imgId6 }">
													<img src="${imgUrl6}" width="80px" height="80px" class="io6"><i>×</i>
												</div>
												<div class="item add" style="display: none">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile6" class="file-input">
						                      			<input type="hidden" name="type" value="4">
						                      		</a>
					                      		</div>
			                      			</c:when>
			                      			<c:otherwise>
			                      				<div class="item add">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile6" class="file-input">
						                      			<input type="hidden" name="type" value="4">
						                      		</a>
					                      		</div>
			                      			</c:otherwise>
			                      		</c:choose>
			                      		
			                      		</div>
		                      		</form>
		                      		<form id="form7" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
		                      			<div class="add_list" id="imgList7">
		                      			<c:choose>
		                      				<c:when test="${szr.helpType==7 && imgId7!=null }">
		                      					<div class="item" id="${imgId7 }">
													<img src="${imgUrl7}" width="80px" height="80px" class="io7"><i>×</i>
												</div>
												<div class="item add" style="display: none">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile7" class="file-input">
						                      			<input type="hidden" name="type" value="4">
						                      		</a>
					                      		</div>
		                      				</c:when>
		                      				<c:otherwise>
		                      					<div class="item add">
				                      				<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile7" class="file-input">
						                      			<input type="hidden" name="type" id="type1" value="4">
						                      		</a>
						                      	</div>
		                      				</c:otherwise>
		                      			</c:choose>
		                      			
				                      	</div>
				                     </form>
				                     <form id="form8" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
				                     	<div class="add_list" id="imgList8">
				                     	<c:choose>
		                      				<c:when test="${szr.helpType==7 && imgId8!=null }">
		                      					<div class="item" id="${imgId8 }">
													<img src="${imgUrl8}" width="80px" height="80px" class="io8"><i>×</i>
												</div>
												<div class="item add" style="display: none">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile8" class="file-input">
						                      			<input type="hidden" name="type" value="4">
						                      		</a>
					                      		</div>
		                      				</c:when>
		                      				<c:otherwise>
		                      					<div class="item add">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile8" class="file-input">
						                      			<input type="hidden" name="type" id="type2" value="4">
						                      		</a>
						                      	</div>
		                      				</c:otherwise>
		                      			</c:choose>
				                      	</div>
				                      </form>	
		                      	</span>
                      		</c:when>
                      		<c:otherwise>
                      			<span class="value">已认证，无需上传</span>
		                      	<br />
                      		</c:otherwise>
                      	</c:choose>
                      </dd>
                      <div style="clear: both;"></div>
                      <c:choose>
                      	<c:when test="${help=='' }">
                      	  <dd ng-show="com">
	                      	<label><i class="important">*</i>单位名称：</label><span class="value"><input name="" type="text" id="eName" value="${fbr.workUnit }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>详细地址：</label><span class="value"><input name="" type="text" id="eAddr" value="${fbr.familyAddress }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>项目联系人：</label><span class="value"><input name="" type="text" id="eContactor" value="${fbr.realName }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>电话：</label><span class="value"><input name="" type="text" id="eTel" value="${fbr.linkMobile }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>QQ/微信/邮箱：</label><span class="value"><input name="" type="text" id="eOtherWay" value="${fbr.qqOrWx }"></span>
	                      </dd>
                      	</c:when>
                      	<c:otherwise>
                      	  <dd ng-show="com">
	                      	<label><i class="important">*</i>单位名称：</label><span class="value"><input name="" type="text" id="eName" value="${apiCompany.name }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>详细地址：</label><span class="value"><input name="" type="text" id="eAddr" value="${apiCompany.address }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>项目联系人：</label><span class="value"><input name="" type="text" id="eContactor" value="${apiCompany.head }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>电话：</label><span class="value"><input name="" type="text" id="eTel" value="${apiCompany.mobile }"></span>
	                      </dd>
	                      <dd ng-show="com">
	                      	<label><i class="important">*</i>QQ/微信/邮箱：</label><span class="value"><input name="" type="text" id="eOtherWay" value=""></span>
	                      </dd>
                      	</c:otherwise>
                      </c:choose>
                      <dd ng-show="com"><label>营业执照照片：</label>
                      	<c:choose>
                      		<c:when test="${apiCompany==null }">
                      			<span class="value">
	                      		<span class="identity1">认证之后无需上传，<a href="javascript:;" class="Identity">点此认证</a></span>
	                      		<br />
	                      		<form id="form3" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
		                      		<div class="add_list" id="imgList3">
		                      		<c:choose>
	                      				<c:when test="${(szr.helpType==9 || szr.helpType==10) && imgId3!=null }">
	                      					<div class="item" id="${imgId3 }">
												<img src="${imgUrl3}" width="80px" height="80px" class="io3"><i>×</i>
											</div>
											<div class="item add" style="display: none">
						                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile3" class="file-input">
						                      			<input type="hidden" name="type" value="4">
						                      		</a>
					                      		</div>
		                      			</c:when>
	                      				<c:otherwise>
	                      					<div class="add">
					                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile3" class="file-input">
					                      			<input type="hidden" name="type" value="4">
					                      		</a>
				                      		</div>
	                      				</c:otherwise>
	                      				</c:choose>
		                      		</div>
	                      		</form>
                      			</span>
                      		</c:when>
                      		<c:otherwise>
                      			<span class="value">
	                      		<span class="identity1">已认证，无需上传</span>
	                      		<br />
                      		</c:otherwise>
                      	</c:choose>
                      </dd>
                  </dl>
                  <div style="clear: both;"></div>
                  <hr />
                	<dl class="col">
                   	  <!--<dt>受助人信息</dt>-->
                   	  <dd><label><i class="important">*</i>受助人性质：</label>
                   	  	<span class="radio2">
                   	  		<label class="single"><input type="radio" ng-model="group" checked="checked" ng-value="false" id="appealSingle"/><span>个人/单个家庭</span></label>
                   	  		<label ng-show="com"><input type="radio" ng-model="group" ng-value="true" id="appealGroup"/><span>群体/多个人</span></label>
                   	  	</span>
                   	  </dd>
                   	  <c:if test="${help!='' }">
                   	  	  <dd ng-hide="group"><label><i class="important">*</i>受助人姓名：</label><span class="value"><input name="" type="text" id="appealName" value=""></span></dd>
	                   	  <dd ng-hide="group"><label><i class="important">*</i>身份证号：</label> <span class="value"><input name="" type="text" id="appealIdcard" value=""></span></dd>                         
	                   	  <dd ng-hide="group"><label><i class="important">*</i>所在地：</label> 
	                   	  	<span class="value" id="city">
	                   	  		<select id="seachprov" name="seachprov" onchange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>&nbsp;&nbsp;
	                   	  		<select id="seachcity" name="homecity" onchange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
	                   	  		<span id="seachdistrict_div"><select id="seachdistrict" name="seachdistrict"></select></span>
	                   	  	</span>
	                   	  </dd>   
	                     <dd ng-hide="group"> <label><i class="important">*</i>详细地址：</label><span class="value"><input type="text" size="20" id="appealAddress" value=""></span></dd>
	                     <dd ng-hide="group"> <label><i class="important">*</i>联系电话：</label><span class="value"><input type="text" size="20" id="appealMobile" value=""></span></dd>
                   	  </c:if>
                   	  <c:if test="${help=='' }">
                   	  	  <dd ng-hide="group"><label><i class="important">*</i>受助人姓名：</label><span class="value"><input name="" type="text" id="appealName" value="${szr.realName}"></span></dd>
	                   	  <dd ng-hide="group"><label><i class="important">*</i>身份证号：</label> <span class="value"><input name="" type="text" id="appealIdcard" value="${szr.indetity}"></span></dd>                         
	                   	  <dd ng-hide="group"><label><i class="important">*</i>所在地：</label> 
	                   	  	<span class="value" id="city">
	                   	  		<select id="seachprov" name="seachprov" onchange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>&nbsp;&nbsp;
	                   	  		<select id="seachcity" name="homecity" onchange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
	                   	  		<span id="seachdistrict_div"><select id="seachdistrict" name="seachdistrict"></select></span>
	                   	  	</span>
	                   	  </dd>
	                     <dd ng-hide="group"> <label><i class="important">*</i>详细地址：</label><span class="value"><input type="text" size="20" id="appealAddress" value=""></span></dd>
	                     <dd ng-hide="group"> <label><i class="important">*</i>联系电话：</label><span class="value"><input type="text" size="20" id="appealMobile" value="${szr.linkMobile}"></span></dd>
                   	  </c:if>
                      <dd ng-hide="group"><label>受助身份证照片：</label>
                      	<span class="value">
                      		身份证正反面照片，请勿打码
                      		<br />
                      		<form id="form4" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
	                      		<div class="add_list" id="imgList4">
	                      		<c:choose>
                      				<c:when test="${(szr.helpType==7 || szr.helpType==9) && imgId4!=null }">
                      					<div class="item" id="${imgId4 }">
											<img src="${imgUrl4}" width="80px" height="80px" class="io4"><i>×</i>
										</div>
										<div class="add" style="display: none">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile4" class="file-input">
				                      			<input type="hidden" name="type"value="4">
				                      		</a>
				                      	</div>
	                      			</c:when>
                      				<c:otherwise>
			                      		<div class="add">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile4" class="file-input">
				                      			<input type="hidden" name="type"value="4">
				                      		</a>
				                      	</div>
			                      </c:otherwise>
			                    </c:choose>
			                    </div>
		                     </form>
		                     <form id="form5" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
		                      	<div class="add_list" id="imgList5">
		                      	<c:choose>
                      				<c:when test="${(szr.helpType==7 || szr.helpType==9) && imgId5!=null }">
                      					<div class="item" id="${imgId5 }">
											<img src="${imgUrl5}" width="80px" height="80px" class="io5"><i>×</i>
										</div>
										<div class="item add" style="display: none">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile5" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:when>
                      				<c:otherwise>
				                      	<div class="add">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile5" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:otherwise>
	                      		</c:choose>
	                      		</div>
                      		</form>
                      	</span>
                      </dd>
                      <div style="clear: both;"></div>
                      <dd ng-hide="group"><label>知情同意书：</label>
                      	<span class="value">
                      		必须是受助人或直系亲属签字确认，<a href="http://res.17xs.org/pdf/zq_agree.pdf" target="_blank" class="Identity agree-model">点此查看模板</a>
                      		<br />
                      		<form id="form9" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
	                      		<div class="add_list" id="imgList9">
	                      		<c:choose>
                      				<c:when test="${(szr.helpType==7 || szr.helpType==9) && imgId9!=null }">
                      					<div class="item" id="${imgId9 }">
											<img src="${imgUrl9}" width="80px" height="80px" class="io9"><i>×</i>
										</div>
										<div class="item add" style="display: none">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile9" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:when>
                      				<c:otherwise>
			                      		<div class="add">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile9" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:otherwise>
	                      		</c:choose>
	                      		</div>
                      		</form>
                      	</span>
                      </dd>
                      
                      <c:choose>
                      	<c:when test="${help=='' }">
                      	  <dd ng-show="group">
	                      	<label><i class="important">*</i>受助群体名称：</label>
	                      	<span class="value"><input type="text" id="appealGroupName" value="${szr.realName }"><span class="tishi">孤寡老人、贫困学生、抗战老兵等</span></span>
	                      </dd>
	                      <dd ng-show="group">
	                      	<label><i class="important">*</i>项目地点：</label>
	                      	<span class="value" id="projectAddr">
	                   	  		<select id="seachprov1" name="seachprov1" onchange="changeComplexProvince(this.value, sub_array, 'seachcity1', 'seachdistrict1');"></select>&nbsp;&nbsp;
	                   	  		<select id="seachcity1" name="homecity1" onchange="changeCity(this.value,'seachdistrict1','seachdistrict1');"></select>&nbsp;&nbsp;
	                   	  		<span id="seachdistrict_div1"><select id="seachdistrict1" name="seachdistrict1"></select></span>
	                   	  	</span>
	                      </dd>
	                      <dd ng-show="group">
	                      	<label><i class="important">*</i>详细地址：</label>
	                      	<span class="value"><input type="text" id="appealGroupAddr" value=""><span class="tishi">如果是全国项目请填写发起方所在地</span></span>
	                      </dd>
                      	</c:when>
                      	<c:otherwise>
                      	  <dd ng-show="group">
	                      	<label><i class="important">*</i>受助群体名称：</label>
	                      	<span class="value"><input type="text" id="appealGroupName" value=""><span class="tishi">孤寡老人、贫困学生、抗战老兵等</span></span>
	                      </dd>
	                      <dd ng-show="group">
	                      	<label><i class="important">*</i>项目地点：</label>
	                      	<span class="value" id="projectAddr">
	                   	  		<select id="seachprov1" name="seachprov1" onchange="changeComplexProvince(this.value, sub_array, 'seachcity1', 'seachdistrict1');"></select>&nbsp;&nbsp;
	                   	  		<select id="seachcity1" name="homecity1" onchange="changeCity(this.value,'seachdistrict1','seachdistrict1');"></select>&nbsp;&nbsp;
	                   	  		<span id="seachdistrict_div1"><select id="seachdistrict1" name="seachdistrict1"></select></span>
	                   	  	</span>
	                      </dd>
	                      <dd ng-show="group">
	                      	<label><i class="important">*</i>详细地址：</label>
	                      	<span class="value"><input type="text" id="appealGroupAddr" value=""><span class="tishi">如果是全国项目请填写发起方所在地</span></span>
	                      </dd>
                      	</c:otherwise>
                      </c:choose>
                      
                      <dd ng-show="com"><label>项目发起申请书：</label>
                      	<span class="value">
                      		项目发起申请书需单位盖章生效，<a href="http://res.17xs.org/pdf/fq_model.pdf" target="_blank" class="Identity project-model">点此查看模板</a>
                      		<br />
                      		<form id="form10" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
	                      		<div class="add_list" id="imgList10">
	                      		<c:choose>
                      				<c:when test="${(szr.helpType==9 ||szr.helpType==10) && imgId10!=null }">
                      					<div class="item" id="${imgId10 }">
											<img src="${imgUrl10}" width="80px" height="80px" class="io10"><i>×</i>
										</div>
										<div class="item add" style="display: none">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile10" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:when>
                      				<c:otherwise>
			                      		<div class="add">
				                      		<a class="add_btn" href="javascript:;">+<input type="file" name="file" hidefocus="true" id="imgFile10" class="file-input">
				                      			<input type="hidden" name="type" value="4">
				                      		</a>
			                      		</div>
	                      			</c:otherwise>
	                      		</c:choose>
	                      		</div>
                      		</form>
                      	</span>
                      </dd>
                	</dl>
                	<div style="clear: both;"></div>
                	<hr ng-hide="group" />
                <dl class="col single" ng-hide="group">
               	  <!--<dt>证明人信息</dt>-->
                  <dd><label>证明单位：</label><span class="value"><input name="" type="text" id="rCom" value="${zmr.workUnit }"></span><span class="tishi">大病救助必须填写医院和主治医生，其他的可以是学校或政府单位</span></dd>
                  <dd><label>证明人姓名：</label><span class="value"><input type="text" id="rName" value="${zmr.realName }"></span></dd>
                  <dd>
                    <label>职务：</label><span class="value"><input name="" type="text" id="rzw" value="${zmr.persition}"></span>
                  </dd>
                  <dd><label>单位电话：</label><span class="value"><input type="text" id="rPhone" value="${zmr.linkMobile}"></span></dd>
                </dl>
                <div style="clear:both;"></div>
                </div>
              <div class="nextbtn"><a id="needHelp3Back" class="next">上一步</a><a  id="needHelp3Submit" class="next">保存并预览</a><a id="needHelp3Issue" class="next">发布</a></div>
          </div>
        </div>
    </div>
  </div>
</div>

<input type="hidden" id="help" value="${help}">
<input type="hidden" id="projectId" value="${projectId}">
<input type="hidden" id="auditStaff_type" value="${auditStaff_type }" />
<input type="hidden" id="company_type" value="${company_type }" />
<%@ include file="./../common/newfooter.jsp" %>
<script type="text/javascript" src="<%=resource_url%>res/js/common/city5.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/common/allcity.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/angular.min.js"></script>
<script data-main="<%=resource_url%>res/js/dev/needhelp.js" src="<%=resource_url%>res/js/require.min.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script>
	
	var code=[],p,c,s;
	var code1=[],p1,c1,s1;
	//var address='${szr.familyAddress}';
	if('${szr.familyAddress}'==''){
		initComplexArea('seachprov', 'seachcity', 'seachdistrict', area_array, sub_array, '0', '0', '0');
		initComplexArea('seachprov1', 'seachcity1', 'seachdistrict1', area_array, sub_array, '0', '0', '0');
	}
	else{
	/* var address = '${szr.familyAddress}';
	if(address.charAt('区')!=-1){
		address=address.substring(address.indexOf('区')+1,address.length);
		$('#appealAddress').val(address);
	}else{
		address=address.substring(address.indexOf('市')+1,address.length);
		$('#appealAddress').val(address);
	} */
		$.ajax({
			url:"http://restapi.amap.com/v3/geocode/geo",
			method:"get",
			data:{key:"e2a5cfcf7032e817a07c11615a1ba50c",address:'${szr.familyAddress}'+'|'+'${szr.familyAddress}',batch:true},
			success:function(result){
				code = result.geocodes[0].adcode.split('');
				p = code[0]+''+code[1];
				c = code[2]+''+code[3];
				s = code[4]+''+code[5];
				initComplexArea('seachprov', 'seachcity', 'seachdistrict', area_array, sub_array, sub_arr, p,c,s);
				
				code1 = result.geocodes[1].adcode.split('');
				p1 = code1[0]+''+code1[1];
				c1 = code1[2]+''+code1[3];
				s1 = code1[4]+''+code1[5];
				initComplexArea('seachprov1', 'seachcity1', 'seachdistrict1', area_array, sub_array, sub_arr, p1,c1,s1);
			}
		});}
	angular.module("personInfoApp",[]).controller("personInfoCon",['$scope',function($scope){
		$scope.com = false;
		$scope.group = false;
	}]);
	$(function(){
		if(${fbr.helpType==7}){
			$('.radio #gr').attr('checked','checked');
			$('.radio2 #appealSingle').attr('checked','checked');
			$('#gr').click();
			$('#appealSingle').click();
		}else if(${fbr.helpType==9}){
			$('.radio #dw').attr('checked','checked');
			$('.radio2 #appealSingle').attr('checked','checked');
			$('#dw').click();
			$('#appealSingle').click();
		}else if(${fbr.helpType==10}){
			$('.radio #dw').attr('checked','checked');
			$('.radio2 #appealGroup').attr('checked','checked');
			$('#dw').click();
			$('#appealGroup').click();
		}
		$(".radio input").on("click",function(){
			$(".radio2 .single input").click();
		});
		if('${help}'!=''){
			if($("#fname").val()!=""){
				$("#fname").attr("disabled","disabled");
			}
			if($("#fIdcard").val()!=""){
				$("#fIdcard").attr("disabled","disabled");
			}
			if($("#eName").val()!=""){
				$("#eName").attr("disabled","disabled");
			}
		}
		$(".identity1 a").on("click",function(){
			window.open('<%=resource_url%>enterprise/core/realName.do','','width=1000,height=600,top=150,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		});
		$(".value .realNameIdentity").on("click",function(){
			window.open('<%=resource_url%>user/realname.do','实名认证','width=1000,height=600,top=150,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		});
	});
	
</script>
</body>
</html>
