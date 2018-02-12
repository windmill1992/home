var base=window.baseurl;
var dataUrl={
	add:base+'resposibilityReport/saveForm.do'//保存报名
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
	window.sitePage="p-aboutGood";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#activeList")});
	p.changeCallback=function(){
		addListNew.dataList(p.curPage,10);
	};
	addListNew={
		init:function(){
			var that=this;
			$(".footer").click(function () {
                var flag = true;
                $(".center_input").each(function (index, element) {
                    if ($(element).val() == "" || $(element).val() == "请输入~") {
                        $(element).val("请输入~");
                        $(element).css("color", "red");
                        flag = false;
                    }
                });
                var formstring="";
                var forms = $('#forms').val().split(",");
                for (var i=0;i<forms.length;i++){
					if(i==0){
						if($('#'+forms[i]+'').val()=="请输入~"){
							return false;
						}
						else{
							formstring = "\""+forms[i]+"\":\""+$('#'+forms[i]+'').val()+"\"";
						}
					}
					else{
						if($('#'+forms[i]+'').val()=="请输入~"){
							return false;
						}
						else{
							formstring = formstring+",\""+forms[i]+"\":\""+$('#'+forms[i]+'').val()+"\"";
						}
					}
					
				}
                formstring="{"+formstring+"}";
                $.ajax({
    				url:dataUrl.add,
    				data:{information:formstring,formId:$('#formId').val(),userId:$('#userId').val()==null?"":$('#userId').val(),createTime:new Date()},
    				success: function(result){
    					if(result.flag=='0'){
    						$('#fail').show();
    						setTimeout("$('#fail').hide();",2000);
    						return false;
    					}else {
    						 $('#success').show();
    			             setTimeout("$('#success').hide();",2000);
    			             if(result.obj.gotoUrl!=null&&result.obj.gotoUrl!=''){
    			            	 setTimeout(function(){
        			            	 location.href=result.obj.gotoUrl;

        			            	 },1000);
    			             }
    			             else{
    			            	 setTimeout(function(){
        			            	 location.href=base;
        			            	 },1000);
    			             }
    			             return true;
    					}
    				},
    				error: function(r){
    					$('#fail').show();
						setTimeout("$('#fail').hide();",2000);
    					return false;
    					//d.alert({content:'获取活动列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
    				}
    			})
            });

            $(".center_input").focus(function () {
                if($(this).val() == "请输入~") {
                    $(this).val("");
                    $(this).css("color", "black");
                }
            }).blur(function () {
                if($(this).val() == "") {
                    $(this).val("请输入~");
                    $(this).css("color", "red");
                }
            });
			
		},
		dataList:function(){
			
		}
	}
	addListNew.init();
});