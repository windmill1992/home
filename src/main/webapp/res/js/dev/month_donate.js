/**
 * Created by Administrator on 2016/4/11.
 */
$(function(){
    $(".donate_inp4").click(function(){
        $(".donate_none1").show();
    });
    $(".donate_inp3").click(function(){
        $(".donate_none1").hide();
    });
    $(".donate_inp6").click(function(){
        $(".donate_none2").show();
    });
    $(".donate_inp5").click(function(){
        $(".donate_none2").hide();
    });
    $(".donate_inp7").click(function(){
        $(".donate_none2").hide();
    });
    $("#mobileNum").click(function() {
        if ($("#mobile").val() == "") {
            $("#mobile").val("请输入手机号码").css("color", "red");;
            return false;
        }
    });

    $("#mobileNum").click(function() {
        if($("#money").val() == "") {
            $("#money").val("请输入金额").css("color","red");
            return false;
        }

    });

    $("#money").click(function() {
        $("#money").val("");
        $("#money").css("color","#000");
        return false;
    });
    $("#mobile").click(function() {
        $("#mobile").val("");
        $("#mobile").css("color","#000");
        return false;
    });

});

















