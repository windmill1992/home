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
<title>受助人信息</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>  
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseProjectpc.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/recipients_info.css">
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	function selectTypeList(field, fieldChinese, i) {
		$('#field').val(field);
		$('#fieldChinese').val(fieldChinese);
		$("#fieldList>a").removeClass("curTab");
		$('#typeName' + i).attr("class", "curTab");
	}
</script>
</head> 
<body style="font-family: '微软雅黑'; background: #f7f7f7">
<%@ include file="./../common/newhead.jsp" %>
<div class="relProject">
    <div class="relImg relImg1"></div>
    <!--类目-->
    <div class="selectorNew">
		<dl>
            <dt>请选择帮扶对象：</dt>
            <dd id="tagList">
                <a v="1" href="javascript:void(0);" class="curTab" id="me1">为本人发起求助</a>
                <a v="2" href="javascript:void(0);" id="family2">为家人发起求助</a>
                <a v="3" href="javascript:void(0);" id="kin3">为其他亲友发起求助</a>

            </dd>
        </dl>
		
        <dl>
            <dt>请选择捐助项目领域： </dt>
            <dd id="fieldList">
            </dd>
        </dl>
    </div>
    <!--受助人信息-->
    <div class="personRelease">
        <div class="br">
        <div class="personNav">
            <div class="personFl">请输入受助人姓名：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="name" value="${oneself.realName }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入受助人身份证：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="idCard" value="${oneself.idCard }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入联系电话：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="mobile" value="${oneself.mobileNum }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">所在地：</div>
            <div class="personFr">
                <div class="rlitem data_o main">
                    <select id="province" class="prov_city"><option value="省份">省份</option></select>
                    <select id="city" class="prov_city"><option value="地级市">地级市</option></select>
                    <select id="county" class="prov_city"><option value="县/区/市">县/区/市</option></select>
                </div>
            </div>
        </div>
    
        <div class="personNav">
            <div class="personFl">请输入详细地址：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="detailAddress">
            </div>
        </div>
        </div>
        <!--上传照片-->
        <!--为家人发起-->
        <div class="jr" style="display: none">
        <div class="personNav1 " >
            <div class="personNav">
                <div class="personFl">请输入您与受助人的关系：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="appealRelation">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入受助人姓名：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="appealName">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入受助人身份证：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="appealIdcard">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入联系电话：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="appealMobile">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">所在地：</div>
                <div class="personFr">
                    <div class="rlitem data_o main">
                        <select id="appealProvince" class="prov_city"><option value="省份">省份</option></select>
                        <select id="appealCity" class="prov_city"><option value="地级市">地级市</option></select>
                        <select id="appealCounty" class="prov_city"><option value="区/县级市">区/县级市</option></select>
                    </div>
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入详细地址：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="appealDetailAddress">
                </div>
            </div>
            <div class="personFl">上传图片：</div>
            <p class="at">请上传受助人户口本或结婚证</p>
            <div class="personFr">
                 <div class="" style="width:393px">
                <span class="value">
                 <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                     <div class="add_list" id="imgList1">
                         <div class="item add">
                             <a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input">
                                 <input type="hidden" name="type" id="type" value="4"></a>
                         </div>
                     </div>
                 </form>
                </span>
            </div>
    </div>
        </div>
        </div>
        <!--为其他亲友发起-->
        <div class="personNav1 qy" style="display: none">
            <div class="personNav">
                <div class="personFl">请输入您与受助人的关系：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="otherRelation">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入受助人姓名：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="otherName">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入受助人身份证：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="otherIdCard">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入联系电话：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="otherMobile">
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">所在地：</div>
                <div class="personFr">
                    <div class="rlitem data_o main">
                        <select id="otherProvince" class="prov_city"><option value="省份">省份</option></select>
                        <select id="otherCity" class="prov_city"><option value="地级市">地级市</option></select>
                        <select id="otherCounty" class="prov_city"><option value="区/县级市">区/县级市</option></select>
                    </div>
                </div>
            </div>
            <div class="personNav">
                <div class="personFl">请输入详细地址：</div>
                <div class="personFr">
                    <input type="text" class="personInp" id="otherDetailAddress">
                </div>
            </div>
            <div class="personFl">上传图片：</div>
            <p class="at">请上传受助人委托书</p>
            <div class="personFr">
                <div class="" style="width:397px">
                    <span class="value">
                     <form id="form2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                         <div class="add_list" id="imgList2">
                             <div class="item add">
                                 <a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input">
                                     <input type="hidden" name="type" id="type" value="4"></a>
                             </div>
                         </div>
                     </form>
                    </span>
                    <div class="add_list" id="imgList3">
                        <div class="item add">
                             <div class="imgCon2"><img src="../../../res/images/newPcrelease/shili.jpg">点击查看示例</div>
                        </div>
                    </div>

                </div>
             </div>
        </div>
    </div>
    <div style="clear: both"></div>
    <a href="javascript:void();" class="footBtn">确认，下一步</a>
</div>
<input type="hidden" id="typeState" value="1" />
<input type="hidden" id="fieldChinese" value="" />
<input type="hidden" id="field" value="" />
<input type="hidden" id="presonReleaseId" value="${presonReleaseId }" />
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/newReleaseProject/recipients_infogr.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
