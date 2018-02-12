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
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseProjectpc.css">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/recipients_info.css">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
    <script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
<body style="font-family: '微软雅黑'; background: #eee">
<%@ include file="./../common/newhead.jsp" %>
<div class="relProject">
    <div class="relImg relImg1"></div>
    <!--类目-->
    <div class="selectorNew">
        <dl>
            <dt>请选择帮扶对象：</dt>
            <dd id="tagList">
                <a v="妇女" href="javascript:void(0);" class="curTab" id="institutionToOne">为个人发起筹款 <i class="curTabi"></i></a>
                <a v="老人" href="javascript:void(0);" id="institutionToPublic">为公益项目发起筹款 <i></i></a>
            </dd>
        </dl>

        <dl>
            <dt>请选择捐助项目领域:</dt>
            <dd id="fieldList">
                <a v="dabing" href="javascript:void(0);" class="curTab">大病<i class="curTabi"></i></a>

                <a v="deport" href="javascript:void(0);">自然灾害<i ></i></a>

                <a v="disease" href="javascript:void(0);">医疗救助 <i></i></a>

                <a v="education" href="javascript:void(0);">教育救助<i></i></a>

                <a v="elderly" href="javascript:void(0);">特殊群体<i></i></a>

                <a v="disasterRelief" href="javascript:void(0);">赈灾 <i></i></a>

                <a v="Publicinterest" href="javascript:void(0);">公益众筹11<i></i></a>

            </dd>
        </dl>
    </div>
    <!--受助人信息-->
    <div class="personRelease wgr">
        <div class="personNav">
            <div class="personFl">受助人姓名：</div>
            <div class="personFr">
                <input type="text" class="personInp" name="appealName" id="appealName">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">受助人身份证：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="appealIdcard" name="appealIdcard">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">联系电话：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="appealPhone" name="appealPhone">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">所在地：</div>
            <div class="personFr">
                <div class="rlitem data_o main">
                	<select id="province"  class="prov_city"><option value="所在省">所在省</option></select>
    				<select id="city"  class="prov_city"><option value="地区">地区</option></select>
    				<select id="county"   class="prov_city"><option value="县/市/区">县/市/区</option></select>
<!--                     <select id="province" class="prov_city"><option value="省份">省份</option><option value="北京市">北京市</option><option value="天津市">天津市</option><option value="上海市">上海市</option><option value="重庆市">重庆市</option><option value="河北省">河北省</option><option value="山西省">山西省</option><option value="内蒙古">内蒙古</option><option value="辽宁省">辽宁省</option><option value="吉林省">吉林省</option><option value="黑龙江省">黑龙江省</option><option value="江苏省">江苏省</option><option value="浙江省">浙江省</option><option value="安徽省">安徽省</option><option value="福建省">福建省</option><option value="江西省">江西省</option><option value="山东省">山东省</option><option value="河南省">河南省</option><option value="湖北省">湖北省</option><option value="湖南省">湖南省</option><option value="广东省">广东省</option><option value="广西">广西</option><option value="海南省">海南省</option><option value="四川省">四川省</option><option value="贵州省">贵州省</option><option value="云南省">云南省</option><option value="西藏">西藏</option><option value="陕西省">陕西省</option><option value="甘肃省">甘肃省</option><option value="青海省">青海省</option><option value="宁夏">宁夏</option><option value="新疆">新疆</option><option value="香港">香港</option><option value="澳门">澳门</option><option value="台湾省">台湾省</option></select> -->
<!--                     <select id="city" class="prov_city"><option value="地级市">地级市</option></select> -->
<!--                     <select id="county" class="prov_city"><option value="区/县级市">区/县级市</option></select> -->
                </div>
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">详细地址：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="detailAddress" name="detailAddress">
            </div>
        </div>
    <div style="clear: both"></div>
        <!--上传照片-->
        <div class="personNav1">
            <div class="personFl">上传图片：</div>
            <p class="at">请上传受助人身份证（正面，反面以及手持身份证三张），机构与个人关系以及委托书</p>
            <div class="personFr">
                <div style="width:490px">
                <span class="value">
                  <form id="form2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">	
		       						<div class="add_list" id="imgList2">
		       						 <c:forEach var="record" items="${bflist}" varStatus="status">
										<div class="item" id="${record.id}">
											<img
												src="${record.url}"
												width="80px" height="70px"><i style="display: none;">×</i>
										</div>
										</c:forEach>
										<div class="item add"><a  class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input">
										       <input type="hidden" name="type" id="type2" value="4"></a>
		   					            </div>
		
		    						</div>
		              </form> 
                </span>
                </div>
            </div>
        </div>

    </div>
    <div style="clear: both"></div>
     <!--为群体发布项目-->
    <div class="personRelease gyxm">
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
		       						 <c:forEach var="record" items="${bflist}" varStatus="status">
										<div class="item" id="${record.id}">
											<img
												src="${record.url}"
												width="80px" height="70px"><i style="display: none;">×</i>
										</div>
										</c:forEach>
										<div class="item add"><a  class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input">
										       <input type="hidden" name="type" id="type1" value="4"></a>
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
        <%-- <div class="personNav2">
            <div class="personFl">项目内容：</div>
        	<textarea name="textArea1"  class="personFr1" cols="35" rows="8"  id="content"  placeholder="项目内容：${atc.model }"></textarea>
        </div>--%> 
        <div style="clear: both"></div>
        
    </div>
</div>
    <div style="clear: both"></div>

<div class="foot"><a class="footBtn confirm">确认，下一步</a></div>
<input type="hidden"  id="typeName_e" />
<input type="hidden"  id="institutionType" value="1" />
<input type="hidden"  id="projectId" />
<input type="hidden"  id="zlId"  value="${zlId }"/>
<input type="hidden"  id="helpType"  value="${helpType }"/>
 <div class="cue2" style="display: none"  id="msg"></div>
<script data-main="<%=resource_url%>res/js/dev/newReleaseProject/recipients_infoJG.js" src="<%=resource_url%>res/js/require.min.js"></script>
<%@ include file="./../common/newfooter.jsp" %>
</body>
<script type="text/javascript">

   function onclickFields(typeName_e,index){
         //追加该标签的样式，同事去掉别的样式
         $(".curTab"+index).addClass("curTab").siblings().removeClass("curTab");
         $("#typeName_e").val(typeName_e);
         //一步请求
         $.ajax({ 
						url:window.baseurl+"newReleaseProject/queryTypeConfigByTypeName_e.do",
						type: 'POST',
						data:{
						   typeName_e:typeName_e
						},
						chase:false,
						success: function(result){
							if(result.flag==1){
								//给出提示
								datas=result.obj;
						        data=datas.items;
								$("#content").attr("placeholder","项目内容："+data[0].model);
								$(".uploadImageDirection").html("上传图片说明："+data[0].uploadImageDirection);
							}else if(result.flag==0){
								$("#msg").html('<p>'+result.errorMsg+'</p>');
								$("#msg").show();
								setTimeout(function () {
									$("#msg").hide();
								}, 2000);
								return false;
							}
						} ,
						error: function(){
							return alert("网络异常");
						}
				});
				
   }
</script>
</html>