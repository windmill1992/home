<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>善园网-志愿者</title>
    <link rel="stylesheet" type="text/css" href="/res/css/h5/postulant.css?v=20171212"/>
</head>
<body>
<div class="postul">
    <div class="postul_top flex spb">
        <a href="/project/view_h5.do?projectId=${projectId}" class="back">返回</a>
        <p>志愿者</p>
        <a href="javascript:void(0);" class="btn-a" id="addVolun">报名</a>
    </div>
    <div class="postul_center">
        <ul id="vList"></ul>
        <div class="center_5">
        	<a href="javascript:void(0);" id="more"></a>
        </div>
    </div>
</div>
<input type="hidden" value="${projectId}" id="projectId">
<script src="/res/js/jquery-1.8.2.min.js"></script>
<script src="/res/js/h5/volunteerList.js?v=20171212"></script>
</body>
</html>