var dataUrl={
	garden:'/data/garden/list.json',
	joinGood:'http://www.17xs.org/user/steward/index.do',
	tole:baseurl+'user/realname.do',
	first:baseurl+'user/steward/first.do',
	second:baseurl+'user/steward/second.do',
	three:baseurl+'user/steward/three.do',
	login:baseurl+'user/sso/login.do'
};
require.config({
	baseUrl:window.baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20150511"
});
 
define(["extend","dialog","head","entry"],function($,d,h,en){
	window.sitePage="p-joinGood";
	d.init();
	h.init();
	en.init();  
  
	$(".applyBtn").on("click",function(){
//		JoinGood();
		 window.location.href = dataUrl.joinGood;
	});
	var state = $("#state").val();
	if(state == 203){
		d.alert({content:"您已经是善管家成员了"});
	}
	
	function JoinGood(){
		//var userId=$.cookie();
		$.ajax({
			url:dataUrl.joinGood,
			data:{userId:0,t:new Date().getTime()},
			success:function(data){ 
				if(!!data){
					rst=data.flag;
					var obj =data.obj;
					if(rst == -1){
						 window.location.href=dataUrl.login+data.obj;
					}else if(rst == 1){
						switch(obj){//0:实名审核中 1:实名认证  2：兴趣选项  3：审核中 4：通
						case 0:
						    window.location.href=dataUrl.first;
							break;
						case 1:
							window.location.href=dataUrl.tole;
							break;
						case 2:
							window.location.href=dataUrl.second;
							break;
						case 3:
							window.location.href=dataUrl.three;
							break;
						case 4:
						    d.alert({content:"您已经是善管家成员了"});
							break; 
					}
					}
				}
			},
			error:function(e){
				 
			}
		});
	} 
	
	
	/*Interest Field*/
	$(".applyStp-field i").not(".last").on("click",function(){
		$(this).toggleClass("ckb");
	}); 
	
	$("#toProcess").on("click",function(){
		var top=$("#applyProcess").offset().top; 
		$("html,body").animate({"scrollTop":top-180+"px"});
	});
	
	 
});






