var base=window.baseurl;
var dataUrl={
	setGoodLibrary:base+"goodlibrary/setGoodLibrary.do"//提交
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
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon",
		"area"	: "util/area"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon","area"],function($,d,h,en,uc,f,p,p1,a){
	//window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			 $(".zddq").click(function(){
				 	$('#type').val('2');
			        $(".zdr").removeClass("inputdh");
			        $(".zddq").addClass("inputdh");
			        $('#appointPerson').hide();
			    	$('#appointArea').show();
			    });
			    $(".zdr").click(function(){
			    	$('#type').val('1');
			        $(".zddq").removeClass("inputdh");
			        $(".zdr").addClass("inputdh");
			        $('#appointPerson').show();
			    	$('#appointArea').hide();
			    });
//			    捐赠项目复选按钮
			   /* $('.setup_fl').on('click', '.setup_input', function(){
			      var $this = $(this);
			      $this.toggleClass('inputdh');
			    })*/
			   /* $('#appointPerson_btn').click(function(){
			    	
			    	$('#appointPerson_btn').toggleClass('inputdh');
			    	
			    })
			     $('#appointArea_btn').click(function(){
			    	
			    	$('#appointArea_btn').toggleClass('inputdh');
			    	
			    })*/
			//提交
			$('#setGoodLibrary').click(function(){
				var tags=[];
				$("li>div").each(function(){
					if($(this).attr("class").indexOf("inputdh")!=-1){
					//if($(this).attr("class").contains("inputdh")){
						var tag=$(this).children().val();
						tags.push(tag);
					}
				});
				that.submitInfo(tags)
			})
		},
	//提交
	submitInfo: function(tags){
		var type=$('#type').val(),appointMobile=$('#appointMobile').val(),appointPersonArea=$('#appointPersonArea').val(),
		defaultMoney=$('#defaultMoney').val(),appointDonateArea=$('#appointDonateArea').val(),libraryId=$('#goodlibraryId').val();
		if(libraryId==''){
			return d.alert({content:"善库id为空！",type:'error'});
		}
		if(type==1){
			appointPersonArea=null
		}
		else if(type==2){
			appointMobile=null
		}
		if(defaultMoney==''||defaultMoney==null){
			return d.alert({content:"请输入每个成员可用金额！",type:'error'});
		}
		//添加数据
		$.ajax({
			url:dataUrl.setGoodLibrary,
			cache: false,
			type: "POST",
			data:{id:libraryId,appointMobile:appointMobile,appointPersonArea:appointPersonArea,defaultMoney:defaultMoney,
				appointArea:appointDonateArea,tag:tags.join(',')},
			success: function(result){
				if(result.flag==1){//成功
					//return d.alert({content:"设置成功！",type:'success'});
					data=result.obj;
					window.location='http://www.17xs.org/myGoodLibrary/gotoMyGoodLibraryDetails.do?id='+data;
				}
				else if(result.flag==-1){//未登录
					data=result.obj;
					window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/goodlibrary/setGoodLibrary.do?libraryId='+data;
				}
				else if(result.flag==0){//异常
					return d.alert({content:result.errorMsg,type:'error'});
				}
			},
			error : function(result) { 
				return d.alert({content:"请求异常",type:'error'});
			}

		});
	}
	}
	
	reg.init()
})
