/**
 * Created by Administrator on 2016/4/1.
 */
$(function(){
	var type = 1,category = '',notice = 0;
   	
   	$("input").css("color","#B6B6B6");
   	$("input").each(function(){
        var text = $(this).attr("placeholder");
    	$(this).val(text);
    });
   	$("input").focus(function(){
        if($(this).val() == $(this).attr("placeholder")){
            $(this).val("");
        }
        $(this).css("color","#444");
    });
   	$("input").blur(function(){
        if( $(this).val() == ""){
            $(this).css("color","#B6B6B6");
            var text = $(this).attr("placeholder");
            $(this).val(text);
        }
    });

    $("#mon_mon").attr("class,mon_zsys");
    $("#mon_mon").click(function(){
        $("#mon_mon").addClass("mon_zsys");
        $("#mon_day").removeClass("mon_zsys");
        $("#mon_day").addClass("mon_zsys1");
        type = 1;
    });
    $("#mon_day").click(function(){
        $("#mon_day").addClass("mon_zsys");
        $("#mon_mon").removeClass("mon_zsys");
        $("#mon_mon").addClass("mon_zsys1");
        type = 0;
    });
    $("#mon_mon1").attr("class,mon_zsys");
    $("#mon_mon1").click(function(){
        $("#mon_mon1").addClass("mon_zsys");
        $("#mon_day1").removeClass("mon_zsys");
        $("#mon_day1").addClass("mon_zsys1");
        $(".mon_conment").hide();
    });
    $("#mon_day1").click(function(){
        $("#mon_day1").addClass("mon_zsys");
        $("#mon_mon1").removeClass("mon_zsys");
        $("#mon_mon1").addClass("mon_zsys1");
        $(".mon_conment").show();
    });
    $("#mon_day2").attr("class,mon_zsys");
    $("#mon_mon2").click(function(){
        $("#mon_mon2").addClass("mon_zsys");
        $("#mon_day2").removeClass("mon_zsys");
        $("#mon_day2").addClass("mon_zsys1");
        $(".foot_sh1").hide();
        notice = 1;
    });
    $("#mon_day2").click(function(){
        $("#mon_day2").addClass("mon_zsys");
        $("#mon_mon2").removeClass("mon_zsys");
        $("#mon_mon2").addClass("mon_zsys1");
        $(".foot_sh1").show();
        notice = 0;
    });
    $.each($(".list_zsys"), function(){
    	$($(this)).click(function(){
            if($(this).hasClass("list_a")){
            	$(this).removeClass("list_a");
                category = category.replace($(this).attr("v")+",","");
            }else{
            	$(this).addClass("list_a");
                category = category + $(this).attr("v")+",";
            }
        });
    });
    
    $('#chargeMomeny').blur(function(){
		var money = Number($('#money').val()),chargeMomeny=Number($('#chargeMomeny').val());
		if(money>chargeMomeny){
			$('#chargeMomeny').val(money);
		}else{
			$("#chargeMomeny").val(chargeMomeny);
		}
	});
    
    $(".foot_4").click(function(){
        if(notice == 0 && $("#mobileNum").val() == "请输入电话号码"){
            $("#mobileNum").css("color","red");
            return false;
        }
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if (!reg.test($("#mobileNum").val())) {
			$("#prompt_info").text("请输入正确的手机号！");
        	$("#agreeRule1").show();
            setTimeout(function () {
                $("#agreeRule1").hide();
            }, 2000);
			return false;
		}
        if($("#money").val() == ""){
        	$("#prompt_info").text("请您输入金额");
        	$("#agreeRule1").show();
            setTimeout(function () {
                $("#agreeRule1").hide();
            }, 2000);
            return false;
        }
        if(Number($("#money").val()) <= 0.1){
        	$("#prompt_info").text("您输入的金额低于0.1");
        	$("#agreeRule1").show();
            setTimeout(function () {
                $("#agreeRule1").hide();
            }, 2000);
            return false;
        }
        
        var isAgree = $('#checkbox').attr("checked");
    	if(!isAgree){
    		$(".cue_f1").hide();
    		$("#agreeRule").show();
    		setTimeout(function () {
    			$("#agreeRule").hide();
			  }, 2000);
    		return false;
    	}
        $(".cue_f1").show();
    });
    
    $("#concelDonate").click(function(){
        $(".cue_f1").hide();
    });
    $("#concelCharge").click(function(){
        $(".cue_f").hide();
    });
    
    $("#confirmDonate").click(function(){
    	
    	var mobileNum = $('#mobileNum').val(),money = $('#money').val();
    	if(notice == 1){
    		mobileNum = null;
    	}
    	$.ajax({
    		url: 'http://www.17xs.org/user/addDonateTime.do',
    		dataType: 'json',
    		type: 'post',
    		data: {
    			type:type,
    			category:category,
    			notice:notice,
    			mobileNum:mobileNum,
    			money:money
    		},
    		success: function(result) {
    		   if(result.flag==1){
    				location.href='/user/getMyDonateTime.do';
    			}
    		   if(result.flag==0){
    			   $(".cue_f1").hide();
    			   $(".cue_f").show();
    			   $('#chargeMomeny').val(money);
    		   }
    		}
    	});
    });
    /*充值*/
    $("#confirCharge").click(function(){
    	var chargeMomeny = $('#chargeMomeny').val(),userName = $('#userName').text(),mobile = $('#mobileNum').val();
    	if(notice == 1){
    		mobile = $('#mobile').text();
    	}
    	location.href='/visitorAlipay/tenpay/rechargedeposit.do?amount='+chargeMomeny+'&touristName='+userName+'&touristMobile='+mobile;
    });

});
