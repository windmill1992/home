var base=window.baseurl;
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform" ,
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20151010"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	window.sitePage="p-monthDonate";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#dtList")});
	p.changeCallback=function(){
		addListNew.dataList(p.curPage,10);
	};
	addListNew={
		init:function(){
			var that=this;
			$(".donate_p4").click(function(){
				that.dataList(1,10);
			});
			$('#frameBtn0').click(function(){
				$('#tank').hide();
				window.location.href='http://www.17xs.org/project/launchDonateView.do';
			}) 
			$('#close0').click(function() {
				$('#tank').hide();
				return false;
			})
			$('#frameBtn1').click(function(){
				$('#tank1').hide();
				return false;
			})
			
			$('#close1').click(function() {
				$('#tank1').hide();
				return false;
			})
			$('#frameBtn2').click(function() {
				$('#tank2').hide();
				return false;
			})
			$('#close2').click(function() {
				$('#tank2').hide();
				return false;
			})
			$('#frameBtn3').click(function() {
				$('#tank3').hide();
				return false;
			})
			$('#close3').click(function() {
				$('#tank3').hide();
				return false;
			})
			$('#frameBtn4').click(function() {
				$('#tank4').hide();
				return false;
			})
			$('#close4').click(function() {
				$('#tank4').hide();
				return false;
			})
			$('#frameBtn5').click(function() {
				$('#tank5').hide();
				return false;
			})
			$('#close5').click(function() {
				$('#tank5').hide();
				return false;
			})
		},
		dataList:function(page,num){
			$("input").each(function(){
                if($(this).val() == $(this).attr("data-placeholder")){
                    $(this).css("color","red");
                    return false;
                }
            });
            if($('#type').val()!=$('#type').attr("data-placeholder")&&$('#category').val()!=$('#category').attr("data-placeholder")&&
            $('#notice').val()!=$('#notice').attr("data-placeholder")&&$('#MobileNum').val()!=$('#MobileNum').attr("data-placeholder")&&
            $('#money').val()!=$('#money').attr("data-placeholder")&&$('#agree').val()!=$('#agree').attr("data-placeholder")&&
            $('#categorys').val()!=$('#categorys').attr("data-placeholder")){
            	
            	var type=$("input[name='type']:checked").val(),category=$("input[name='category']:checked").val(),notice =$("input[name='notice']:checked").val(),
            	MobileNum = $('#MobileNum').val(),categorys="",
				money = $('#money').val();
				var obj=document.getElementsByName('categorys');
				for(var i=0; i<obj.length; i++){
					if(obj[i].checked) categorys+=obj[i].value+','; //如果选中，将value添加到变量s中
				} 
				if(categorys==''&&category=='field'){
					$('#tank1').show();
					return false; 
				}
				if(MobileNum==''&&notice==0){
					$('#tank2').show();
					return false; 
				}
				if(MobileNum.length<11&&notice==0){
					$('#tank3').show();
					return false; 
				}
				if(money==''){
					$('#tank4').show();
					return false; 
				}
				if(money<0.1){
					$('#tank5').show();
					return false; 
				}
               	$.ajax({
				url: 'http://www.17xs.org/project/launchDonate.do',
				dataType: 'json',
				type: 'post',
				data: {
					type:type,
					category:categorys,
					notice:notice,
					categorys:categorys,
					MobileNum:MobileNum,
					money:money,
					state:200
				},
				success: function(result) {
		   			if(result.flag==1){
		   				$('#tank').show();
					}
		  			 if(result.flag==0){
			   			$('#yue').show();
		   			}
				},
				error: function(){ 
					return false;
					}	
				});
          }
		}
	}
	addListNew.init();
});