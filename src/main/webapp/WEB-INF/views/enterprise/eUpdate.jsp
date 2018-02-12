<!DOCTYPE html>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.guangde.entry.ApiCompany,java.util.*"%>
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
	<% 
	   ApiCompany company = (ApiCompany)request.getAttribute("company");
	   int type = company.getType();
	   boolean type0 = type==0?true:false;
	   boolean type1 = type==1?true:false;
	   boolean type2 = type==2?true:false;
	   boolean type3 = type==3?true:false;
	   List<String> urls = company.getContentImageUrl();
	   int imgLen = urls==null?0:urls.size();
	   String imgFileIden0,imgFileIden1,imgFileIden2,imgFileIden3,imgFileorga,imgFile1,imgFile2;
	   String imgFileIdenId0,imgFileIdenId1,imgFileIdenId2,imgFileIdenId3,imgFileorgaId,imgFile1Id,imgFile2Id;
	   String[]imgIds = null;
	   String bussiness1,bussiness2,bussiness3,bussiness4;
	   if(StringUtils.isBlank(company.getServiceField())){
	     company.setServiceField("");
	   }
	   if(imgLen==4){
	   		imgIds = StringUtils.split(company.getContentImageId(),",");
	   		imgFileorga = urls.get(1);
	   	    imgFileorgaId = imgIds[1];
	   	    
	   	    imgFile1 = urls.get(2);
	   	    imgFile1Id = imgIds[2];
	   	    
	   	    imgFile2 = urls.get(3);
	   	    imgFile2Id = imgIds[3];
	   		
	   }else{
	   		imgFileorga="res/images/login/uploader.jpg";
	   		imgFileorgaId="";
	   		imgFile1="res/images/login/uploader.jpg";
	   		imgFile1Id="";
	   		imgFile2="res/images/login/uploader.jpg";
	   		imgFile2Id="";
	   }
	   
	   if(type0&&imgLen==4){
	   		imgFileIden0 = urls.get(0);
	   		imgFileIdenId0=imgIds[0];
	   		bussiness1=company.getServiceField();
	   }else{
	   		imgFileIden0 = "res/images/login/uploader.jpg";
	   		imgFileIdenId0="";
	   		bussiness1="";
	   }
	   
	   if(type1&&imgLen==4){
	   		imgFileIden1 = urls.get(0);
	   		imgFileIdenId1=imgIds[0];
	   		bussiness2=company.getServiceField();
	   }else{
	   		imgFileIden1 = "res/images/login/uploader.jpg";
	   		imgFileIdenId1="";
	   		bussiness2="";
	   }
	   
	   if(type2&&imgLen==4){
	   	    imgFileIden2 = urls.get(0);
	   		imgFileIdenId2=imgIds[0];
	   		bussiness3=company.getServiceField();
	   }else{
	   		imgFileIden2 = "res/images/login/uploader.jpg";
	   		imgFileIdenId2="";
	   		bussiness3="";
	   }
	   
	   if(type3&&imgLen==4){
	   		imgFileIden3 = urls.get(0);
	   		imgFileIdenId3=imgIds[0];
	   		bussiness4=company.getServiceField();
	   }else{
	   		imgFileIden3 = "res/images/login/uploader.jpg";
	   		imgFileIdenId3="";
	   		bussiness4="";
	   }                   
    %>
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../common/eleft_menu.jsp" %>
            </div>
            <div class="uCEN-R"> 
                <div class="entctf">
                	<h2>单位类型</h2>
                    <div class="rlitem rlitemtle">
                    	<label>选择单位类型：</label>
                        <div class="rltext">
                        	<select id="entTypeSelect">
                            	<option value="0" <% if(type0)out.print("selected = 'selected'");%>>企业</option>
                                <option value="1" <% if(type1)out.print("selected = 'selected'");%>>事业单位</option>
                                <option value="2" <% if(type2)out.print("selected = 'selected'");%>>社会团体</option>
                            	<option value="3" <% if(type3)out.print("selected = 'selected'");%>>党政及国家机关</option>
                            </select>
                            <div class="ent-picshow" id="entPicshow">
                            	<img src="<%=resource_url%>res/images/Enterprise/yqsmill.jpg" alt="" bigsrc="<%=resource_url%>res/images/Enterprise/yqbig.jpg">
                            </div>
                        </div>
                    </div>
                    <h2 id="listTle">企业信息<span>按照证书上的内容逐字填写</span></h2>
                    <div class="enttype" id="enttype">
                    	<div class="list" <% if(!type0)out.print("style='display:none'");%>>
                        	<div class="rlitem mt50">
                                <label>企业注册号：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text" value="<% if(type0)out.print(company.getRegisterNo());%>" id="identification0" placeholder="例：0000000000001234"/>
                                    </div>
                                    <span class="wrinfo error"></span>
                                </div>
                        </div>
                        <div class="rlgroup">
                                <label class="font-green">营业执照上传：</label>
                                <div class="controls controlsn conborderbom">
                                    <div class="uploader">
                                        <div class="pic" id="picIden0">
                                             <img src="<%=imgFileIden0%>" alt="" data="<%=imgFileIdenId0%>" id="imgFileIdenId0"/>
                                        </div>
                                        <a href="javascript:void(0)" class="uploadbtn">
                                        <span class="btn-text"> 上传并预览 </span>
                                        <div class="filewrapper" style="overflow: hidden;">
                                        <form  id="formIden0" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFileIden0" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
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
                        <div class="list" <% if(!type1)out.print("style='display:none'");%>>
                        	<div class="rlitem mt50">
                                <label>事证号：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text" value="<%if(type1)out.print(company.getRegisterNo());%>"  id="identification1" placeholder="例：市政第0000000000001234号"/>
                                    </div>
                                    <span class="wrinfo error"></span>
                                </div>
                        </div>
                        <div class="rlgroup">
                                <label class="font-green">市证执照上传：</label>
                                <div class="controls controlsn conborderbom">
                                    <div class="uploader">
                                        <div class="pic" id="picIden1">
                                            <img src="<%=imgFileIden1%>" alt="" data="<%=imgFileIdenId1%>" id="imgFileIdenId1"/>
                                        </div>
                                        <a href="javascript:void(0)" class="uploadbtn">
                                        <span class="btn-text"> 上传并预览 </span>
                                        <div class="filewrapper" style="overflow: hidden;">
                                          <form  id="formIden1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFileIden1" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
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
                                
                                <textarea class="area" id="business1"><%=bussiness2%></textarea>
                                <span class="wrinfo error"></span> 
                            </div>
                        </div>
                        </div>
                        <div class="list" <% if(!type2)out.print("style='display:none'");%>>
                        	<div class="rlitem mt50">
                                <label>社证号：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text" value="<%if(type2)out.print(company.getRegisterNo());%>" id="identification2" placeholder="例：(浙）社证字第1234号"/>
                                    </div>
                                    <span class="wrinfo error"></span>
                                </div>
                        </div>
                        <div class="rlgroup">
                                <label class="font-green">事证执照上传：</label>
                                <div class="controls controlsn conborderbom">
                                    <div class="uploader">
                                        <div class="pic" id="picIden2">
                                             <img src="<%=imgFileIden2%>" alt="" data="<%=imgFileIdenId2%>" id="imgFileIdenId2"/>
                                        </div>
                                        <a href="javascript:void(0)" class="uploadbtn">
                                        <span class="btn-text"> 上传并预览 </span>
                                        <div class="filewrapper" style="overflow: hidden;">
                                         <form  id="formIden2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFileIden2" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
                                        </form>
                                        </div>
                                        </a>
                                        <div class="wrinfo" id="msgphoto3"></div>
                                    </div>
                                    <div class="example">
                                        <span class="title">示例：</span>
                                        <div class="pic">
                                             <img src="<%=resource_url%>res/images/Enterprise/shpic_03.jpg" alt=""/>
                                            <a title="" class="idphoto" data="{'bgpic':'/Enterprise/shttrpic.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="rlgroup">
                                <label>业务范围：</label>
                                <div class="controls controlsn conborderbom">
                                    
                                    <textarea class="area" id="business2"><%=bussiness3%></textarea>
                                    <span class="wrinfo error"></span> 
                                </div>
                            </div>
                        </div>
                        <div class="list" <% if(!type3)out.print("style='display:none'");%>>
                        	<div class="rlitem mt50">
                                <label>执法证号：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text"  value="<%if(type3)out.print(company.getRegisterNo());%>" id="identification3" placeholder="例：(浙）社证字第1234号"/>
                                    </div>
                                    <span class="wrinfo error"></span>
                                </div>
                        </div>
                        <div class="rlgroup">
                                <label class="font-green">执法证上传：</label>
                                <div class="controls controlsn conborderbom">
                                    <div class="uploader">
                                        <div class="pic" id="picIden3">
                                             <img src="<%=imgFileIden3%>" alt="" data="<%=imgFileIdenId3%>" id="imgFileIdenId3"/>
                                        </div>
                                        <a href="javascript:void(0)" class="uploadbtn">
                                        <span class="btn-text"> 上传并预览 </span>
                                        <div class="filewrapper" style="overflow: hidden;">
                                         <form  id="formIden3" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFileIden3" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
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
                                    
                                    <textarea class="area" id="business3"><%=bussiness4%></textarea>
                                   <span class="wrinfo error"></span> 
                                </div>
                            </div>
                        </div>
                   </div>
                    <div class="rlitem mt15">
                            <label>组织机构代码：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="<%=company.getGroupCode()%>" id="organization" placeholder="例：00001234-1"/>
                                </div>
                                <span class="wrinfo error"></span>
                            </div>
                    </div>
                    <div class="rlgroup">
                            <label class="font-green">组织机构证件上传：</label>
                            <div class="controls controlsn">
                                <div class="uploader">
                                    <div class="pic" id="picorga">
                                         <img src="<%=imgFileorga%>" alt="" data="<%=imgFileorgaId%>" id="imgFileorgaId"/>
                                    </div>
                                    <a href="javascript:void(0)" class="uploadbtn">
                                    <span class="btn-text"> 上传并预览 </span>
                                    <div class="filewrapper" style="overflow: hidden;">
                                     <form  id="formorga" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFileorga" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
                                        </form>
                                    </div>
                                    </a>
                                    <div class="wrinfo" id="msgphotoorga"></div>
                                </div>
                                <div class="example">
                                    <span class="title">示例：</span>
                                    <div class="pic"  >
                                        <img src="<%=resource_url%>res/images/Enterprise/enye2_03.jpg" alt="" />
                                        <a title="" class="idphoto" data="{'bgpic':'/Enterprise/diapic_03.jpg','tle':'请准备国家颁发的企业法人营业执照(信息页)'}">查看详细要求</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    
                    <h2>法定代表人信息</h2>
                    <div class="rlitem mt50">
                            <label>真实姓名：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="<%=company.getLegalName()%>" id="legalPerson" placeholder="真实姓名"/>
                                </div>
                                <span class="wrinfo error"></span>
                            </div>
                        </div>
                    <div class="rlitem">
                            <label>身份证：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="<%=company.getIdentity()%>" id="legalIdentify" placeholder="身份证号码"/>
                                </div>
                                <span class="wrinfo"><em></em></span>
                            </div>
                        </div>
                    <div class="rlgroup mt0">
                            <label>身份证正面</label>
                            <div class="controls mb20">
                                <div class="uploader">
                                    <div class="pic" id="pic1">
                                        <img src="<%=imgFile1%>" alt="" data="<%=imgFile1Id%>" id="imgFile1Id"/>
                                    </div>
                                    <a href="javascript:void(0)" class="uploadbtn">
                                    <span class="btn-text"> 上传并预览 </span>
                                    <div class="filewrapper" style="overflow: hidden;">
                                     <form  id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
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
                            <label>身份证背面</label>
                            <div class="controls controlsn mb20">
                                <div class="uploader">
                                    <div class="pic" id="pic2">
                                         <img src="<%=imgFile2%>" alt="" data="<%=imgFile2Id%>" id="imgFile2Id"/>
                                    </div>
                                    <a href="javascript:void(0)" class="uploadbtn">
                                    <span class="btn-text"> 上传并预览 </span>
                                    <div class="filewrapper" style="overflow: hidden;">
                                    <form  id="form2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                        <input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input" />
                                        <input type="hidden" name="type" id="type" value="7"/>
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
                            <label>联系人姓名：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="<%=company.getHead()%>" id="linkman" placeholder="联系人姓名"/>
                                </div>
                                <span class="wrinfo error"></span>
                            </div>
                    </div>
                    <div id="ptcode">
                            <div class="rlitem">
                                <label>手机号：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text" class="" value="<%=company.getMobile()%>" id="linkPhone"/>
                                    </div>
                                    <span class="wrinfo"><em></em></span>
                                </div>
                            </div>
                            <div class="rlitem rlmcode">
                                <label>手机验证码：</label>
                                <div class="rltext">
                                    <div class="rlinput">
                                        <input type="text" class="" placeholder="手机验证码" value="" id="phoneCode"/>
                                    </div>
                                    <a class="wrsdcode" id="pSendT">获取手机验证码</a>
                                    <span class="wrsdpt"></span>
                                    <span class="wrinfo"><em></em></span>
                                </div>
                                
                            </div>
                        </div>
                    <div class="rlitem">
                        <label></label>
                        <div class="rltext">
                            <input type="button" class="rlbtnok yh" id="rlbokbtn"  value="提交认证"/>
                        </div>
                    </div>
                     <input type="hidden" id="vid"  value="<%=company.getId()%>"/>
                </div>
            </div>
        </div>
    </div> 
</div>
 <%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/entlegalizeEdit.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>