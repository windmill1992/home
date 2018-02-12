function showMsg(txt){
	var $msg = $('#msg');
	if($msg.length > 0){
		if(!$msg.is(':hidden')){
			$msg.hide().html('');
		}
		$msg.text(txt).fadeIn();
		setTimeout(function(){
			$msg.fadeOut(function(){
				$(this).html('');
			});
		},2000);	
	}else{
		alert(txt);
	}
}
var _global_ua = navigator.userAgent.toLowerCase();
if(_global_ua.match(/MicroMessenger/i) == 'micromessenger'){
	_global_ua = 'WX';
}else{
	_global_ua = 'H5';
}
//反馈回复
function reply(leavewordUserId, feedbackId, index) {
	var projectId = $("#projectId").val();
	$("#type").val(0);
	$("#index").val(index);
	$("#projectFeedbackId").val(feedbackId);
	$("#projectDonateId").val("");
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/gotoAlterReply.do",
		data: {
			type: 0,
			projectId: projectId,
			projectFeedbackId: feedbackId,
			leavewordUserId: leavewordUserId
		},
		success: function(result) {
			if(result.flag == "101") { //成功
				$("#leavewordName").val(result.obj.leavewordName);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "102") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#replyUserId").val(result.obj.replyUserId);
				$("#replyName").val(result.obj.replyName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "201") {} else if(result.flag == "202") {} else if(result.errorCode == "0001") { //未登录
				showMsg('您还未登录！');
				setTimeout(function(){
					if(_global_ua == 'WX'){
						location.href = result.errorMsg;
					}else{
						location.href = loginUrl;
					}
				},2000);
			} else if(result.flag == -2) {
				if(_global_ua == 'WX'){
					location.href = result.errorMsg;
				}else{
					location.href = loginUrl;
				}
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}

function donateReply(leavewordUserId, donateId, index) {
	var projectId = $("#projectId").val();
	$("#index").val(index);
	$("#projectDonateId").val(donateId);
	$("#projectFeedbackId").val("");
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/gotoAlterReply.do",
		data: {
			type: 1,
			projectId: projectId,
			projectDonateId: donateId,
			leavewordUserId: leavewordUserId
		},
		success: function(result) {
			if(result.flag == "101") { //成功
				$("#leavewordName").val(result.obj.leavewordName);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "102") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#replyUserId").val(result.obj.replyUserId);
				$("#replyName").val(result.obj.replyName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "201") {
				$("#leavewordName").val(result.obj.leavewordName);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "202") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#replyUserId").val(result.obj.replyUserId);
				$("#replyName").val(result.obj.replyName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.errorCode == "0001") { //未登录
				showMsg('您还未登录！');
				setTimeout(function(){
					if(_global_ua == 'WX'){
						location.href = result.errorMsg;
					}else{
						location.href = loginUrl;
					}
				},2000);
			} else if(result.flag == -2) {
				if(_global_ua == 'WX'){
					location.href = result.errorMsg;
				}else{
					location.href = loginUrl;
				}
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}
//反馈留言
function feedBackleaveword(feedBackId, index) {
	var projectId = $("#projectId").val();
	$("#projectFeedbackId").val(feedBackId);
	$("#projectDonateId").val("");
	$("#index").val(index);
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/gotoAlterReply.do",
		data: {
			type: 0,
			projectId: projectId,
			projectFeedbackId: feedBackId
		},
		success: function(result) {
			if(result.flag == "101") { //成功
				$("#leavewordName").val(result.obj.leavewordName);
				$("#leavewordUserId").val(
					result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "102") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#replyUserId").val(result.obj.replyUserId);
				$("#replyName").val(result.obj.replyName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "201") {} else if(result.flag == "202") {} else if(result.errorCode == "0001") { //未登录
				showMsg('您还未登录！');
				setTimeout(function(){
					if(_global_ua == 'WX'){
						location.href = result.errorMsg;
					}else{
						location.href = loginUrl;
					}
				},2000);
			} else if(result.flag == -2) {
				if(_global_ua == 'WX'){
					location.href = result.errorMsg;
				}else{
					location.href = loginUrl;
				}
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}
//捐助留言
function donateLeaveWord(donateId, index) {
	var projectId = $("#projectId").val();
	$("#projectDonateId").val(donateId);
	$("#projectFeedbackId").val("");
	$("#index").val(index);
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/gotoAlterReply.do",
		data: {
			type: 1,
			projectId: projectId,
			projectDonateId: donateId
		},
		success: function(result) {
			if(result.flag == "201") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.flag == "202") {
				$("#leavewordName").val(result.obj.leavewordName);
				$("#replyUserId").val(result.obj.replyUserId);
				$("#replyName").val(result.obj.replyName);
				$("#leavewordUserId").val(result.obj.leavewordUserId);
				$(".moved_up").show();
				$(".end_text").focus();
			} else if(result.errorCode == "0001") { //未登录
				showMsg('您还未登录！');
				setTimeout(function(){
					if(_global_ua == 'WX'){
						location.href = result.errorMsg;
					}else{
						location.href = loginUrl;
					}
				},2000);
			} else if(result.flag == -2) {
				if(_global_ua == 'WX'){
					location.href = result.errorMsg;
				}else{
					location.href = loginUrl;
				}
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}

//点击加载更多查看信息
function loadMore(i, feedBackId, surplusTotal) {
	var currentPage = $("#currentPageForFeedBack").val();
	var projectId = $("#projectId").val();
	var leaveWords = $('.loadMore' + i).html();
	var html = [];
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/loadMoreLeaveWord.do",
		data: {
			type: 0,
			projectId: projectId,
			projectFeedbackId: feedBackId,
			currentPage: currentPage,
			surplusTotal: surplusTotal
		},
		success: function(result) {
			if(result.flag == "1") {
				//成功
				itemss = result.obj;
				for(var j = itemss.length - 1; j >= 0; j--) {
					if(itemss[j].replyUserId == null) {
						html.push('<p class="board_text" onclick="reply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectFeedbackId + ',' + i + ')"><span>' + itemss[j].leavewordName + '：</span>' +
							itemss[j].content +
							'</p>');
						html.push('<div style="clear:both"></div>');
					} else {
						html.push('<p class="board_text" onclick="reply(' +
							itemss[j].leavewordUserId +
							',' +
							itemss[j].projectFeedbackId +
							',' + i + ')"><span>' +
							itemss[j].leavewordName +
							"<b class='hf'>回复</b>" +
							itemss[j].replyName +
							'：</span>' +
							itemss[j].content +
							'</p>');
						html.push('<div style="clear:both"></div>');
					}
				}
				//追加数据
				$('.message_list' + i).html('<i></i>' + html.join(''));
				//判断是否显示加载更多
				$('.more' + i).hide();
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}

function donateLoadMore(i, projectDonateId, surplusTotal) {
	var currentPage = $("#currentPageForFeedBack").val();
	var projectId = $("#projectId").val();
	var leaveWords = $('.donateLoadMore' + i).html();
	var html = [];
	$.ajax({
		url: "http://www.17xs.org/h5ProjectDetails/loadMoreLeaveWord.do",
		data: {
			type: 0,
			projectId: projectId,
			projectDonateId: projectDonateId,
			currentPage: currentPage,
			surplusTotal: surplusTotal
		},
		success: function(result) {
			if(result.flag == "1") {
				//成功
				itemss = result.obj;
				for(var j = itemss.length - 1; j >= 0; j--) {
					if(itemss[j].replyUserId == null) {
						html.push('<p class="board_textj" ><span onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + i + ')" >' + itemss[j].leavewordName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
					} else {
						html.push('<p class="board_textj" ><span  onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + i + ')" >' + itemss[j].leavewordName + "<b class='hf'>回复</b>" + itemss[j].replyName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
					}
				}
				//追加数据
				$('.message_listj' + i).html(html.join(''));
				//判断是否显示加载更多
				$('.danoteMore' + i).hide();
			} else { //失败
				showMsg(result.errorMsg);
				return;
			}
		}
	});
}