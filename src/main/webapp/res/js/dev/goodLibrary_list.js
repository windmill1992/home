var base=window.baseurl;
var dataUrl={
	dataList:base+'goodlibrary/getLibrary.do',//列表
};
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
	//window.mCENnavEtSite="m-acccountTracking";
	window.sitePage="p-donateRecord";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	list={
		init:function(){
			  var that=this,$ul = null,width =null,height = null,count = null;
			  var $div = null,index = null,type=$("#type").val();
		      $(".notes_c").click(function(){
		          $(".notes_c").addClass("notes_gg");
		          $(".notes_q").removeClass("notes_gg");
		          $(".notes_m").removeClass("notes_gg");
		      });
		      $(".notes_q").click(function(){
		          $(".notes_q").addClass("notes_gg");
		          $(".notes_c").removeClass("notes_gg");
		          $(".notes_m").removeClass("notes_gg");
		          $("#company").show();
		          $("#goodPeople").hide();
		          $ul = $("#company>ul");//所有的ul
		          width = $ul.width()+73;//ul的宽度+margin-left的值
			      height = $ul.height();//ul的高度
			      count = $ul.length;//ul的个数
			      index = 1
			      $div = $("#company");//需要执行动画的那个div
			      resetDiv();
		      });
		      $(".notes_m").click(function(){
		          $(".notes_m").addClass("notes_gg");
		          $(".notes_c").removeClass("notes_gg");
		          $(".notes_q").removeClass("notes_gg");
		          $("#company").hide();
		          $("#goodPeople").show();	
			      $ul = $("#goodPeople>ul");//所有的ul
		          width = $ul.width()+73;//ul的宽度+margin-left的值
			      height = $ul.height();//ul的高度
			      count = $ul.length;//ul的个数
			      index = 1
		          $div = $("#goodPeople");//需要执行动画的那个div
		          resetDiv();
	      });
		      //轮播图
		      function resetDiv(){
		    	 // index = 1;
		    	  $div.offset({left:staticLeft});
		      }
		      if(type==3){
		    	  $(".notes_m").addClass("notes_gg");
		          $(".notes_c").removeClass("notes_gg");
		          $(".notes_q").removeClass("notes_gg");
		          $("#company").hide();
		          $("#goodPeople").show();
		    	  
		    	  $ul = $("#goodPeople>ul");//所有的ul
		          width = $ul.width()+73;//ul的宽度+margin-left的值
			      height = $ul.height();//ul的高度
			      count = $ul.length;//ul的个数
		
			      index = 1;//初始ul的位置，默认为第一个ul
			      $div = $("#goodPeople");//需要执行动画的那个div
			  }else{
				  $ul = $("#company>ul");//所有的ul
		          width = $ul.width()+73;//ul的宽度+margin-left的值
			      height = $ul.height();//ul的高度
			      count = $ul.length;//ul的个数
		
			      index = 1;//初始ul的位置，默认为第一个ul
			      $div = $("#company");//需要执行动画的那个div
			  }
		      
		      var staticLeft = $div.position().left;
		      $(".notes .prev_fl").click(function () {
		          $div.stop(false, true);
		          if(index == 1){
		              //如果是第一个ul了,什么都不做
		              return;
		          }
	
		          index--;//索引+1
		          //让div往左边移动一个ul的宽度，1000px
		          var l = $div.position().left;//先获取到div的left值
		          $div.animate({
		              "left":l+width-staticLeft
		          },500);
		      });
	
		      $(".notes .next_fr").click(function () {
		          $div.stop(false, true);
		          if(index == count){
		              //如果是最后一个ul了,什么都不做
		              return;
		          }
	
		          index++;//索引+1
		          //让div往左边移动一个ul的宽度，1000px
		          var l = $div.position().left;//先获取到div的left值
		          $div.animate({
		              "left":l-width-staticLeft
		          },500);
		      });
			      
		},
	}
	list.init();
});