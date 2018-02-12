
define(["extend"],function($){
	return { 
		init:function(){
			var that=this;
			/*nav*/
			$("#uCENNav").children().on("click",function(){
				if(!$(this).hasClass("uCENCur")){
					$(this).addClass("uCENCur").siblings().removeClass("uCENCur");
				}
			});
			
			/*select */
			$(".sel-checked").on("click",function(){
				var triangle=$(this).children(".triangle").children();
				if(!triangle.hasClass("triangle-up")){
					triangle.attr("class","triangle-up");
					$(this).next('.sel-options').hide();
				}else{
					triangle.attr("class","triangle-down");
					$(this).next('.sel-options').show();
				}
				return false;
			});
			
			$(".sel-options").children().on("click",function(){
				var parent=$(this).closest(".select"),val=$(this).html(),v=$(this).parent().children().index($(this));
				parent.children(".sel-checked").click().children(".sel-val").val(val).attr("v",v);  
			    if(!!that.selectCallback){
					that.selectCallback();
				} 
				return false;
			});
			
			$(document).on("click",function(){
				var obj = $(this).closest(".select");
				if(obj.size()<=1){
					$(".select .sel-options").hide();
					$(".select .triangle").children().attr("class","triangle-up");
					if(obj.size()==1){
						$(".select .sel-options").show();
						$(".select .triangle").children().attr("class","triangle-down");
					}
				}
			});
			
			if(!!window.uCENnavEtSite){//非空时 企业中心的焦点设置
				$(".uCEN-nav li ").filter("#"+window.uCENnavEtSite).addClass("uCENCur");
			}
			
			if(!!window.mCENnavEtSite){//非空时 个人中心的焦点设置
				$(".uCEN-nav li ").filter("#"+window.mCENnavEtSite).addClass("uCENCur");
			}
			
		}
	};
});