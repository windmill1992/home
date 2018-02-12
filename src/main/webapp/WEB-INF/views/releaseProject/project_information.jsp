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
    <title>项目发布</title>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseProjectpc.css">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
    <script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
<body style="background: #f7f7f7">
<%@ include file="./../common/newhead.jsp" %>
<div class="relProject">

    <div class="relImg relImg2"></div>
    <p class="relnav">项目信息</p>
    <!--项目信息-->
    <div class="personRelease">
        <div class="personNav">
            <div class="personFl">项目标题：</div>
            <div class="personFr">
                <input type="text" placeholder="项目标题" class="personInp"  id="title" value="${project.title}">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">目标金额：</div>
            <div class="personFr">
                <div class="personYz">
                    <input type="text" placeholder="目标金额" class="personInp2"  id="cryMoney" value="<fmt:formatNumber value="${project.cryMoney}" pattern="0.00"/>" onKeyUp="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">
                    <span href="">元</span>
                </div>
            </div>
        </div>
        <div class="personNav">
        <div class="personFl">募捐时间：</div>
        <div class="personFr">
            <div class="personYz">
                <input type="number" placeholder="募捐时间默认为30天"  class="personInp2"  id="donateTime" value=""   onkeyup="this.value=this.value.replace(/D/g,'')" onafterpaste="this.value=this.value.replace(/D/g,'')">
                <span href="">天</span>
            </div>
        </div>
    </div>
    <!-- 配置文件 -->
    <script type="text/javascript" src="<%=resource_url%>res/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="<%=resource_url%>res/ueditor/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('content', {
    toolbars: [
        [
        'undo', //撤销
        'bold', //加粗
        'italic', //斜体
        'indent', //首行缩进
        'snapscreen', //截图
        'underline', //下划线
        'time', //时间
        'date', //日期
        'unlink', //取消链接
        'inserttitle', //插入标题
        'fontfamily', //字体
        'fontsize', //字号
        'paragraph', //段落格式
        'simpleupload', //单图上传
        'insertimage', //多图上传
        'link', //超链接
        'emotion', //表情
        'map', //Baidu地图
        'gmap', //Google地图
        'insertvideo', //视频
        'justifyleft', //居左对齐
        'justifyright', //居右对齐
        'justifycenter', //居中对齐
        'justifyjustify', //两端对齐
        'forecolor', //字体颜色
        'backcolor', //背景色
        'fullscreen', //全屏
        'imagecenter', //居中
        'background', //背景
        'template', //模板
        'inserttable', //插入表格
        'drafts', // 从草稿箱加载
        'charts' // 图表
        ]
    ],
    autoHeightEnabled: true,
    autoFloatEnabled: true,
    initialFrameHeight:300,
    initialFrameWidth:600
});
    </script>
    <!--上传照片-->
        <div class="personNav1">
             <div class="personFl">上传封面图片：</div>
                <div class="personFr">
                    <div class="form_box">
                        <p class="form_text uploadImageDirection"  >封面图片建议尺寸640×420像素</p>
                        <ul class="box_ul">
                            <li class="line4"><label></label><span class="value">
                                        <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                            <div class="add_list" id="imgList1" style="width:390px">
                                            <div class="add_list" id="imgList">
                                             <c:forEach var="record" items="${bflist}" varStatus="status">
                                                <div class="item" id="${record.id}">
                                                    <img
                                                        src="${record.url}"
                                                        width="80px" height="70px"><i style="display: none;">×</i>
                                                </div>
                                                </c:forEach>
                                                <div class="item add"><a  class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input">
                                                       <input type="hidden" name="type" id="type" value="4"></a>
                                                </div>
                                                </div>
                                            </div>
                                       </form>
                                       </span>
                            </li>
                        </ul>
                    </div>
                 </div>
             </div>
    <div style="clear: both"></div>
    <div class="personNav">
    <div class="personFl">项目内容：</div>
    <div class="personFr">为了便于您的编辑，图片宽度请控制在600像素以内，图片大小不超过2M</div>
    </div>
    <div class="personNav2">
            <div class="personFl"></div>
             <div class="personFr"><script id="content" name="content" type="text/plain"></script></div>
        </div>
        <div style="clear: both"></div>
        <%-- <div class="personNav2">
            <div class="personFl">项目内容：</div>
               <textarea name="textArea1"  class="personFr1" cols="35" rows="8"  id="content"  placeholder="项目内容：${atc.model }"></textarea>  
        </div> --%>
        <!-- <div style="clear: both"></div> -->
          </div>
        </div>

    <div style="clear: both"></div>
    <div class="foot"><a href="javaScript:void (0);" class="footBtn" id="submitProject">确认发布</a></div>
</div>
<input type="hidden"  id="typeName_e" value="${typeName_e }"/>
<input type="hidden"  id="institutionType" value="${type }" />
<input type="hidden"  id="projectId" value="${projectId }"/>
<input type="hidden"  id="zlId"  value="${zlId }"/>
<input type="hidden"  id="helpType"  value="${helpType }"/>
 <div class="cue2" style="display: none"  id="msg"></div>
<script data-main="<%=resource_url%>res/js/dev/newReleaseProject/submitProject.js" src="<%=resource_url%>res/js/require.min.js"></script>
<%@ include file="./../common/newfooter.jsp" %>
</body>
</html>