
define(["extend"],function($){
	return { 
		init:function(){
			//ie6提示
	    	  var isIE=window.ActiveXObject,isIE6=isIE&&!window.XMLHttpRequest;
	          if(isIE6){ $("#browser").show(); }
	          $('#brsClose').click(function(){
	        	  $('#browser').hide();
	          })
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
			
			$(".navList .nav-a").removeClass("on");
			
			if(!!window.sitePage){//非空时
				$(".navList .sub-a").filter("#"+window.sitePage).parent().addClass("on");
			}
		}
	}
});