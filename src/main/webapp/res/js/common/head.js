
define(["extend"],function($){
	return { 
		init:function(){
			/*footer*/ 
			$(".codes-tab").children().on("mouseover",function(){
				if($(this).hasClass("c-selected")){
					return;
				}
				$(this).addClass("c-selected").siblings().removeClass("c-selected"); 
				$(".codes-tab").next().children().attr("class","img "+$(this).attr("data-code"));
			});
			
			/* site nav*/ 
			var headerH=$("#header").height(),srlTop=headerH-20,nameTop=headerH+$(".page-tit").height()+20;
			if($(window).scrollTop()>srlTop){
				$("#header").addClass("header-drift"); 
			}
			$(window).scroll(function(){
				var scrollTop=$(this).scrollTop();
				if(scrollTop<srlTop&&$("#header").hasClass("header-drift")){
					$("#header").removeClass("header-drift"); 
				}else if(scrollTop>srlTop&&!$("#header").hasClass("header-drift")){
					$("#header").addClass("header-drift");  
				}
			});
			
			$("#siteNav li a").removeClass("siteCur");
			
			if(!!window.sitePage){//非空时
				$("#siteNav li a").filter("#"+window.sitePage).addClass("siteCur");
			}
			
			 
		}
	}
});