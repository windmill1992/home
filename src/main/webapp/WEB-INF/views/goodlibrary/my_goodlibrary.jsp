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
    <title>我的善库</title>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
    <script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodlibrary/my_goodlibrary.css" />
     <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head>
<body style="font-family: 微软雅黑">
<%@ include file="./../common/newhead.jsp" %>
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
            <div class="goodlibrary_one">
               <span class="value">
               		<form id="form" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                       <div class="add_list" id="imgList">
                           <c:choose>
                           	<c:when test="${logoUrl!=null }">
                           		<div class="item add">
                           		<img src="${logoUrl }" class="io" name="io" style="width:80px;height:80px;"/>
                           		<a href="javascript:void();" class="add_bjtx1">
                                    <p class="bjtx" style="display:none">编辑头像</p>
                                </a>
                                <input type="file" name="file" hidefocus="true" id="imgFile" class="imgFile">
                           		<input type="hidden" name="type" id="type" value="9">
                           		</div>
                           	</c:when>
                           	<c:otherwise>
                           		 <div class="item add">
                               		<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input">
                                   <input type="hidden" name="type" id="type" value="9">
                                   <span class="sctx">上传头像</span>
                               		</a>
                           		</div>
                           	</c:otherwise>
                           </c:choose>
                       </div>
                   </form>
                   
               </span>
                <div class="goodlibrary_two">
                    <p class="goodtwo_text1">${goodLibrary.nickName }</p>
                    <p class="goodtwo_text2">${goodLibrary.introduction }</p>
                </div>
                <div class="goodlibrary_three">
                    <a href="javascript:void();" class="goodthree_btn1">充值</a>
                    <a href="javascript:void();" class="goodthree_btn2">邀请好友加入</a>
                    <div id="qrcode" class="pr_ewm" style="display: none">
                       <img src="<%=resource_url%>res/images/goodlibrary/sanjiaoew.png" class="pr_img"/>
                    </div>
                    <input type="text" id="getval" style="display:none" />
                </div>
                <div style="clear: both"></div>
            </div>
            <div class="goodList">
                <li class="goodLi">
                    <p>善库成员</p>
                    <p>${goodLibrary.peopleNum }</p>
                </li>
                <li class="goodLi">
                    <p>已捐助</p>
                    <p>${goodLibrary.usedmoney }元</p>
                </li>
                <li class="goodLi">
                    <p>可用金额</p>
                    <p>${goodLibrary.balance }元</p>
                </li>
                <li class="goodLi">
                    <p>带动捐款</p>
                    <p>${daiDongMoney }元</p>
                </li>
                <li class="goodLi zui">
                    <p>获捐项目</p>
                    <p>${goodlibraryProNum }个</p>
                </li>
            </div>
            <!--记录-->
            <div class="goodRecord">
                <dl class="headTitle">
                    <dt >
                        <li>成员</li>
                        <li>电话号码</li>
                        <li>所在地</li>
                        <li>已捐助/可用</li>
                        <li>行善次数</li>
                        <li>善库充值</li>
                        <li>明细</li>
                    </dt>
                </dl>
            </div>
        </div>
    </div>
</div>
<!--弹窗-->
<div class="popUp" style="display: none">
    <div class="back"></div>
    <div class="pop_center">
        <div class="dialogTitle"><i class="l"></i><span>捐赠记录</span><a class="popUpcloseDialog" data="0"></a><i class="r" id="closeWin"></i></div>
        <div class="goodRecord1">
            <dl class="donateDetails">
                <dt>
	                <li class="dtperson">捐助人</li>
	                <li class="dtmoney">捐赠金额</li>
	                <li class="dtway">捐赠方式</li>
	                <li class="dtproject">获捐项目</li>
	                <li class="dttime">捐赠时间</li>
                </dt>
            </dl>
            <div id="lstPages" class="lstPages" style="">
	        	<span class="lstP-con">
	        	<a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a>
	        	<span class="pages"><i class="curPage" p="1">1</i><i p="2">2</i></span>
	        	<a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a>
	        	</span>
        	</div>
           <!--  <div id="page_div">
            <a href="javascript:void();" id="page_before"><div>上一页</div></a>
            <a href="javascript:void();" id="page_after"><div>下一页</div></a>
            </div> -->
        </div>
    </div>
</div>
<!--输入金额弹框-->
<div class="popMoney" style="display:none">
    <div class="back"></div>
    <div class="pop_center1">
        <div class="dialogTitle" style="width:300px;"><i class="l"></i><span>善库充值</span><a class="closeDialog" data="0"></a><i class="r"></i></div>
        <div class="goodtanc">
            <div class="kemoney">可用余额：<span id="leftMoney">0</span>元</div>
            <div class="czmoney">充值金额：<input type="text" id="chargeMoney"></div>
            <a href="javascript:void();" class="tancBtn">确定</a>
        </div>
        </div>
    </div>
</div>
<script data-main="<%=resource_url%>res/js/dev/goodlibrary/myGoodLibraryDetails.js" src="<%=resource_url%>res/js/require.min.js"></script>
<input id="libraryId" type="hidden" value="${libraryId }">
<input id="userBalance" type="hidden" value="${frontUser.balance }">
<input id="shanKuUserId" type="hidden" value="${goodLibrary.userId }">
<input id="donateUserId" type="hidden" />
<input id="defaultValue" type="hidden" value="0"/>
<%@ include file="./../common/newfooter.jsp" %>
</body>
<script type="text/javascript" src="<%=resource_url%>res/js/qrcode.js"></script>
<script type="text/javascript">
    $(function(){
        $(".imgFile").mouseover(function(){
            $(".add_bjtx1").addClass("add_bjtx");
            $(".bjtx").show();
        });
    $(".imgFile").mouseout(function(){
    $(".add_bjtx1").removeClass("add_bjtx");
    $(".bjtx").hide();
    });
    });
	window.onload = function() {
		var goodlibraryId = $('#libraryId').val();
		var qrcode = new QRCode(document.getElementById("qrcode"), {
		width : 120,//设置宽高
		height : 120
		});
		var url = 'http://www.17xs.org/goodlibrary/index.do?lirbraryId='
				+ goodlibraryId;
		qrcode.makeCode(url);
	}
   function danateDetails(userId){
   		$(".donateDetails>dd").remove();
   		var donateDetails=$(".donateDetails").html();
       $.ajax({
				url:window.baseurl+"myGoodLibrary/goodLibraryUserDonationlist.do",
				data:{
					id:userId,
					pageNum:1,
					pageSize:10,
					t:new Date().getTime()
				},
				success: function(result){ 
					if(result.flag==1){
						var html=[];
						datas=result.obj;
						data=datas.items;
						total=datas.total;alert(total);
						//html.push(donateDetails);
						for(var i=0;i<data.length;i++){
							html.push('<dd>');
							html.push('<li class="dtperson">'+data[i].name+'</li>');
	                        html.push('<li class="dtmoney">'+data[i].donatAmount+'元</li>');
	                        html.push('<li class="dtway">'+data[i].donateType+'</li>');
	                        html.push('<li class="dtproject">'+data[i].title+'</li>');
	                        html.push(' <li class="dttime">'+data[i].showTime+'</li>');
	                        html.push('</dd>');
						}
						//alert(donateDetails+html.join(""));
						$(".donateDetails").html(donateDetails+html.join(""));
						if(total>1){
							p.pageInit({pageLen:total,isShow:true}); 
						}else if(total<2){
							p.pageInit({pageLen:total,isShow:false}); 
						}
						$(".popUp").show();
					}else if(result.flag==0){
						//暂无成员数据
						//$(".donateDetails").html(donateDetails);
						$(".popUp").hide();
						d.alert({content:"该成员暂无捐款记录",type:'error'});
						return false;
					}
				}
			}); 
     
   }
</script>
</html>