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
<title>专题设置</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<%-- <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/> --%>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/projectTopic/seminarSetup.css" />
<script type="text/javascript">
	function loadProject(tag){
		$('.goodRecord1>dl>dt>li>a').removeClass("moren");
		$('#'+tag).attr("class","moren");
		//$('#typeName' + i).attr("class", "curTab");
		
		$.ajax({
			url:"http://www.17xs.org/projectTopic/loadProjects.do",
			data:{tag:tag},
			success: function(result){
				if(result.flag == 1){//成功
					var html=[],data=result.obj;
					if(data!=null){
						for(var i=0;i<data.length;i++){
						html.push('<li><input type="checkbox" class="dtInput" id="'+data[i].id+'"><p class="dtperson">'+data[i].title+'</p></li>');
						}
					}
					$('.goodRecord1>dl>dd').html(html.join(''));
					$('.dtInput').click(function(){
						var projectId=$(this).attr('id');
						var state = $(this).attr('checked');
						var projectIds=$('.footTar').val();
						if(state=='checked'){
							if(projectIds.indexOf(projectId)>=0){//包含
							}
							else{//不包含
								if(projectIds==''){
									projectIds=projectId+',';
								}
								else{
									projectIds=projectIds+projectId+','
								}
								$('.footTar').val(projectIds);
							}
						}
						else{
							if(projectIds.indexOf(projectId)>=0){//包含
								projectIds=projectIds.replace(projectId+',','');
							}
							else{//不包含
							}
							$('.footTar').val(projectIds);
						}
					});
				}else{//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
			}
		});
	}
</script>
</head> 
<body style='font-family: "微软雅黑", "宋体", "楷体", "仿宋", Tahoma, Geneva, sans-serif'>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
	<div class="page"> 
        <div class="page-bd">
			<div class="uCEN-L">
			<c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<%@ include file="./../common/eleft_menu.jsp"%>
         	</c:when>
         	<c:otherwise>
         		<%@ include file="./../common/pleft_menu.jsp"%>
         	</c:otherwise>
         	</c:choose>
			</div>
			<div class="uCEN-R">
            <!--专题设置块-->
            <div class="semSteup">
                <div class="setup_top">专题设置</div>
                <div class="setup_center">
                    <div class="upcenter_one">
                        <span class="center_fl">封面图</span>
                        <div class="center_fr">
                            <form id="form" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                <div class="add_list" id="imgList">
                                    <div class="item add">
                                        <a class="add_btn">上传图片<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input">
                                            <input type="hidden" name="type" id="type" value="10">
                                        </a>
                                     </div>
                                </div>
                            </form>
                        </div>
                        <a href="javascript:void();" class="center_tr">设置完成</a>
                    </div>
                    <div class="upcenter_one">
                        <span class="center_fl">专题名称</span>
                        <input type="text" class="center_input" id="topicName">
                    </div>
                    <div class="upcenter_two">
                        <span class="center_fl">专题简介</span>
                        <textarea class="center_text" id="topicInfo"></textarea>
                    </div>
                    <div class="upcenter_one">
                        <a href="javascript:void();" class="center_tr2">添加项目</a>
                    </div>
                        <div class="center_html" style="">
                            <!-- <p class="html_p1">已添加的项目（10）</p>
                            <p class="html_p">帮助他完成学业，圆她上学梦。帮助他完成学业，圆她上学梦</p> -->
                        </div>
                    <a href="javascript:void();" class="center_btn">生成专题页</a>


                </div>
            </div>
            <!--专题预览块-->
            <div class="semPreview">
                <div class="setup_top" style="background:#efefef">专题预览</div>
                <div class="preview">
                    <div class="seminar_banner">
                        <!--banner背景图-->
                        <div id="imgBanner"><img class="banner_img" id="8033" src="<%=resource_url%>res/images/projectTopic/list_banner.png" alt=""></div>
                        <div class="sembackground">
                            <div class="sem_p1"><span></span><p id="topicName_text">善园邀请您唯爱加倍</p></div>
                            <p class="sem_p2" id="topicInfo_text">好地方多岁的担惊受恐家都快接发的说法几点睡了看法开卷考试大街上可浪费开发讲的是看风景的酸辣粉快捷方式的咖啡机</p>
                            <img class="semImg" src="<%=resource_url%>res/images/projectTopic/sem_back.png" alt="">
                        </div>
                    </div>
                    <div class="preiewImg">
                        <img src="<%=resource_url%>res/images/projectTopic/yul.jpg">
                    </div>
                </div>
            </div>
            <div style="clear: both"></div>
        </div>			
        </div>
    </div> 
</div>
<!--添加专题弹框-->
<div class="popUp" style="display:none">
    <div class="back"></div>
    <div class="pop_center">
        <div class="dialogTitle"><i class="l"></i><span>选择项目添加至专题页中</span><a class="closeDialog" data="0"></a><i class="r"></i></div>
        <div class="goodRecord1">
            <dl>
                <dt></dt>
                    <div style="clear:both"></div>
                <dd></dd>
                <div style="clear: both"></div>
            </dl>
            <p class="footText1">已选中项目编号（以上没有您要的结果可自行添加项目编号，编号之间用英文逗号隔开；项目链接后面的数字即项目编号）</p>
            <div class="foot_tar">
                <textarea class="footTar"></textarea>
                <a href="javascript:void();" class="foot_btn">添加完毕</a>
            </div>
        </div>
    </div>
</div> 
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/projectTopic/projectTopic.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
