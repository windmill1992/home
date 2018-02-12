<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="cache-control" content="no-store" />
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>善园网 - 用户中心</title>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css?v=20180116" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entLegalize.css" />
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
					<div class="entctf">
						<h2>单位类型</h2>
						<div class="rlitem rlitemtle">
							<label><strong>*</strong>选择单位类型：</label>
							<div class="rltext">
								<select id="entTypeSelect">
									<option value="0">企业</option>
									<option value="1">事业单位</option>
									<option value="2">社会团体</option>
									<option value="3">党政及国家机关</option>
									<option value="4">公益基金会</option>
								</select>
								<div class="ent-picshow" id="entPicshow">
									<img src="<%=resource_url%>res/images/Enterprise/yqsmill.jpg" alt="" bigsrc="<%=resource_url%>res/images/Enterprise/yqbig.jpg">
								</div>
							</div>
						</div>
						<h2 id="listTle">企业信息<span>按照证书上的内容逐字填写</span></h2>
						<div class="enttype" id="enttype">
							<div class="list">
								<div class="rlitem mt50">
									<label><strong>*</strong>企业注册号：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="" id="identification0" placeholder="例：0000000000001234" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlgroup">
									<label class="font-green"><strong>*</strong>营业执照上传：</label>
									<div class="controls controlsn conborderbom">
										<div class="uploader">
											<div class="pic" id="picIden0">
												<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
											</div>
											<a href="javascript:void(0);" class="uploadbtn">
												<span class="btn-text"> 上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form id="formIden0" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
														<input type="file" name="file" hidefocus="true" id="imgFileIden0" class="file-input" />
														<input type="hidden" name="type" id="type" value="7" />
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphotoIden0"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/Enterprise/qiye_03.jpg" alt="" />
												<a title="" class="idphoto" data="{'bgpic':'/Enterprise/yqrpic.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="list" style="display:none">
								<div class="rlitem mt50">
									<label><strong>*</strong>事证号：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="" id="identification1" placeholder="例：市政第0000000000001234号" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlgroup">
									<label class="font-green"><strong>*</strong>市证执照上传：</label>
									<div class="controls controlsn conborderbom">
										<div class="uploader">
											<div class="pic" id="picIden1">
												<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
											</div>
											<a href="javascript:void(0)" class="uploadbtn">
												<span class="btn-text"> 上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form id="formIden1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
														<input type="file" name="file" hidefocus="true" id="imgFileIden1" class="file-input" />
														<input type="hidden" name="type" id="type" value="7" />
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphoto3"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/Enterprise/syprim_03.jpg" alt="" />
												<a title="" class="idphoto" data="{'bgpic':'/Enterprise/syrpic.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlgroup">
									<label>宗旨和业务范围：</label>
									<div class="controls controlsn conborderbom">

										<textarea class="area" id="business1"></textarea>
										<span class="wrinfo error"></span>
									</div>
								</div>
							</div>
							<div class="list" style="display:none">
								<div class="rlitem mt50">
									<label><strong>*</strong>社证号：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="" id="identification2" placeholder="例：(浙）社证字第1234号" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlgroup">
									<label class="font-green"><strong>*</strong>事证执照上传：</label>
									<div class="controls controlsn conborderbom">
										<div class="uploader">
											<div class="pic" id="picIden2">
												<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
											</div>
											<a href="javascript:void(0)" class="uploadbtn">
												<span class="btn-text"> 上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form id="formIden2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
														<input type="file" name="file" hidefocus="true" id="imgFileIden2" class="file-input" />
														<input type="hidden" name="type" id="type" value="7" />
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphoto3"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/Enterprise/shpic_03.jpg" alt="" />
												<a title="" class="idphoto" data="{'bgpic':'/Enterprise/shttrpic.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlgroup">
									<label>业务范围：</label>
									<div class="controls controlsn conborderbom">

										<textarea class="area" id="business2"></textarea>
										<span class="wrinfo error"></span>
									</div>
								</div>
							</div>
							<div class="list" style="display:none">
								<div class="rlitem mt50">
									<label><strong>*</strong>执法证号：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="" id="identification3" placeholder="例：(浙）社证字第1234号" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlgroup">
									<label class="font-green"><strong>*</strong>执法证上传：</label>
									<div class="controls controlsn conborderbom">
										<div class="uploader">
											<div class="pic" id="picIden3">
												<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
											</div>
											<a href="javascript:void(0)" class="uploadbtn">
												<span class="btn-text"> 上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form id="formIden3" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
														<input type="file" name="file" hidefocus="true" id="imgFileIden3" class="file-input" />
														<input type="hidden" name="type" id="type" value="7" />
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphot"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/Enterprise/dzpic_03.jpg" alt="" />
												<a title="" class="idphoto" data="{'bgpic':'/Enterprise/dzrpic.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlgroup">
									<label>业务范围：</label>
									<div class="controls controlsn conborderbom">

										<textarea class="area" id="business3"></textarea>
										<span class="wrinfo error"></span>
									</div>
								</div>
							</div>
							<div class="list" style="display:none">
								<div class="rlitem mt50">
									<label><strong>*</strong>统一社会信用代码：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="" id="identification4" placeholder="例：0000000000001234" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlgroup">
									<label class="font-green"><strong>*</strong>基金会法人登记证书：</label>
									<div class="controls controlsn conborderbom">
										<div class="uploader">
											<div class="pic" id="picIden4">
												<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
											</div>
											<a href="javascript:void(0)" class="uploadbtn">
												<span class="btn-text"> 上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form id="formIden4" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
														<input type="file" name="file" hidefocus="true" id="imgFileIden4" class="file-input" />
														<input type="hidden" name="type" id="type" value="7" />
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphotoIden0"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/Enterprise/frdzSmall.jpg" alt="" />
												<a title="" class="idphoto" data="{'bgpic':'/Enterprise/frdz_03.jpg','tle':'请准备国家颁发的基金会法人登记证书(信息页)'}">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="rlitem mt15">
							<label><strong>*</strong>组织机构代码：</label>
							<div class="rltext">
								<div class="rlinput">
									<input type="text" value="" id="organization" placeholder="例：00001234-1" />
								</div>
								<span class="wrinfo error"></span>
							</div>
						</div>
						<div class="rlgroup other">
							<label class="font-green"><strong>*</strong>组织机构证件上传：</label>
							<div class="controls controlsn">
								<div class="uploader">
									<div class="pic" id="picorga">
										<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
									</div>
									<a href="javascript:void(0)" class="uploadbtn">
										<span class="btn-text"> 上传并预览 </span>
										<div class="filewrapper" style="overflow: hidden;">
											<form id="formorga" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
												<input type="file" name="file" hidefocus="true" id="imgFileorga" class="file-input" />
												<input type="hidden" name="type" id="type" value="7" />
											</form>
										</div>
									</a>
									<div class="wrinfo" id="msgphotoorga"></div>
								</div>
								<div class="example">
									<span class="title">示例：</span>
									<div class="pic">
										<img src="<%=resource_url%>res/images/Enterprise/enye2_03.jpg" alt="" />
										<a title="" class="idphoto" data="{'bgpic':'/Enterprise/diapic_03.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
									</div>
								</div>
							</div>
						</div>
						<div class="rlgroup gy" style="display: none">
							<label class="font-green"><strong>*</strong>慈善组织公开募捐资格证书：</label>
							<div class="controls controlsn">
								<div class="uploader">
									<div class="pic" id="picorga2">
										<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
									</div>
									<a href="javascript:void(0)" class="uploadbtn">
										<span class="btn-text"> 上传并预览 </span>
										<div class="filewrapper" style="overflow: hidden;">
											<form id="formorga2" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
												<input type="file" name="file" hidefocus="true" id="imgFileorga2" class="file-input" />
												<input type="hidden" name="type" id="type" value="7" />
											</form>
										</div>
									</a>
									<div class="wrinfo" id="msgphotoorga"></div>
								</div>
								<div class="example">
									<span class="title">示例：</span>
									<div class="pic">
										<img src="<%=resource_url%>res/images/Enterprise/gkmjSmall.jpg" alt="" />
										<a title="" class="idphoto" data="{'bgpic':'/Enterprise/gkmj_03.jpg','tle':'请准备国家颁发的慈善组织公开募捐资格证书(信息页)'}">查看详细要求</a>
									</div>
								</div>
							</div>
						</div>

						<h2>法定代表人信息</h2>
						<div class="rlitem mt50">
							<label><strong>*</strong>真实姓名：</label>
							<div class="rltext">
								<div class="rlinput">
									<input type="text" value="" id="legalPerson" placeholder="真实姓名" />
								</div>
								<span class="wrinfo error"></span>
							</div>
						</div>
						<div class="rlitem">
							<label><strong>*</strong>身份证：</label>
							<div class="rltext">
								<div class="rlinput">
									<input type="text" value="" id="legalIdentify" placeholder="身份证号码" />
								</div>
								<span class="wrinfo"><em></em></span>
							</div>
						</div>
						<div class="rlgroup mt0">
							<label><strong>*</strong>身份证正面</label>
							<div class="controls mb20">
								<div class="uploader">
									<div class="pic" id="pic1">
										<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
									</div>
									<a href="javascript:void(0)" class="uploadbtn">
										<span class="btn-text"> 上传并预览 </span>
										<div class="filewrapper" style="overflow: hidden;">
											<form id="form1" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
												<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input" />
												<input type="hidden" name="type" id="type" value="7" />
											</form>
										</div>
									</a>
									<div class="wrinfo" id="msgphoto1"></div>
								</div>
								<div class="example">
									<span class="title">示例：</span>
									<div class="pic">
										<img src="<%=resource_url%>res/images/login/id-front-s.jpg" alt="" />
										<a title="" class="idphoto">查看详细要求</a>
									</div>
								</div>
							</div>
						</div>
						<div class="rlgroup ">
							<label><strong>*</strong>身份证背面</label>
							<div class="controls controlsn mb20">
								<div class="uploader">
									<div class="pic" id="pic2">
										<img src="<%=resource_url%>res/images/login/uploader.jpg" alt="" />
									</div>
									<a href="javascript:void(0)" class="uploadbtn">
										<span class="btn-text"> 上传并预览 </span>
										<div class="filewrapper" style="overflow: hidden;">
											<form id="form2" action="<%=resource_url%>file/upload3.do" method="post" enctype="multipart/form-data">
												<input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input" />
												<input type="hidden" name="type" id="type" value="7" />
											</form>
										</div>
									</a>
									<div class="wrinfo" id="msgphoto2"></div>
								</div>
								<div class="example">
									<span class="title">示例：</span>
									<div class="pic">
										<img src="<%=resource_url%>res/images/login/id-back-s.jpg" alt="" />
										<a title="" class="idphoto">查看详细要求</a>
									</div>
								</div>
							</div>
						</div>
						<h2>联系人信息</h2>
						<div class="rlitem mt50">
							<label><strong>*</strong>联系人姓名：</label>
							<div class="rltext">
								<div class="rlinput">
									<input type="text" value="" id="linkman" placeholder="联系人姓名" />
								</div>
								<span class="wrinfo error"></span>
							</div>
						</div>
						<div id="ptcode">
							<div class="rlitem">
								<label><strong>*</strong>手机号：</label>
								<div class="rltext">
									<div class="rlinput">
										<input type="text" class="" value="" id="linkPhone" />
									</div>
									<span class="wrinfo"><em></em></span>
								</div>
							</div>
							<div class="rlitem rlmcode">
								<label><strong>*</strong>手机验证码：</label>
								<div class="rltext">
									<div class="rlinput">
										<input type="text" class="" placeholder="手机验证码" value="" id="phoneCode" />
									</div>
									<a href="javascript:;" class="wrsdcode" id="pSendT">获取手机验证码</a>
								</div>

							</div>
						</div>
						<div class="rlitem">
							<label></label>
							<div class="rltext">
								<input type="button" class="rlbtnok yh" id="rlbokbtn" value="提交认证" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="dialog" id="yzmDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>请输入验证码</h3>
			<div class="yzm-pic">
				<img src="" id="codepic"/>
				<a href="javascript:void(0);" class="refreshCode"></a>
				<input type="number" name="yzmCode" id="yzmCode" maxlength="4" value="" />
			</div>
			<div class="close">
				<a href="javascript:void(0);" class="close-a left" id="cancel">取消</a>
				<a href="javascript:void(0);" class="close-a right" id="sure">确定</a>
			</div>
		</div>
	</div>
	<div class="cue2" id="msg"></div>
	<%@ include file="./../common/newfooter.jsp" %>
	<script data-main="<%=resource_url%>res/js/dev/entlegalize.js?v=20180117" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>