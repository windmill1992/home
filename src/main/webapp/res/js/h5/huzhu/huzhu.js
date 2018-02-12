$(function() {
	$(".item_plan_mask").hDialog({
		box: "#sick_thirty",
		width: 320,
		boxBg: "#fff",
		height: 440,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#sick_thirty .dialog_btn").on("click", function() {
		$(".item_plan_mask").hDialog("close", {
			box: "#sick_thirty"
		});
	})
	$(".insurance_item_1 .content1").hDialog({
		box: "#adult_help",
		width: 320,
		boxBg: "#fff",
		height: 420,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#adult_help .dialog_btn").on("click", function() {
		$(".insurance_item_1 .content1").hDialog("close",{box:"#adult_help"});
	})
	$(".insurance_item_3 .content1").hDialog({
		box: "#love_rule",
		width: 320,
		boxBg: "#fff",
		height: 310,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#love_rule .dialog_btn").on("click", function() {
		$(".insurance_item_3 .content1").hDialog("close",{box:"#love_rule"});
	})
	$(".item_operate_mask").hDialog({
		box: "#weiai_foundation",
		width: 320,
		boxBg: "#fff",
		height: 290,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#weiai_foundation .dialog_btn").on("click", function() {
		$(".item_operate_mask").hDialog("close",{box:"#weiai_foundation"});
	})
	
	$("#has_sick").hDialog({
		box: "#convention_love",
		width: 320,
		boxBg: "#fff",
		height: 440,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#convention_love .dialog_btn").on("click", function() {
		$("#has_sick").hDialog("close",{box:"#convention_love"});
	})
	
	$("#faq_love").find(".items").on("click",function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{$(this).addClass("active");}
	})
	
	
	/*健康告知*/
	$(".some_as").hDialog({
		box: "#sick_convention",
		width: 320,
		boxBg: "#fff",
		height: 150,
		position: "middle",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#sick_convention .btn").on("click", function() {
		$(".some_as").hDialog("close",{box:"#sick_convention"});
	})
	
	/*退出登录*/
	$(".login_off").hDialog({
		box: "#dialog_login_off",
		width: 320,
		boxBg: "#fff",
		height: 120,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#dialog_login_off .btn").on("click", function() {
		$(".login_off").hDialog("close",{box:"#dialog_login_off"});
	})
	
	/*我的会员卡删除信息*/
	$(".del_member").hDialog({
		box: "#dialog_del",
		width: 320,
		boxBg: "#fff",
		height: 120,
		position: "center",
		effect: "hide",
		modalHide: true,
		escHide: true,
		closeHide: false,
		modalBg: "rgba(0,0,0,0.4)"
	});
	$("#dialog_del .btn").on("click", function() {
		$(".del_member").hDialog("close",{box:"#dialog_del"});
	})
	
})