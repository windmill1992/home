var base=window.baseurl;
var dataUrl={
	itemDetail:base+"user/serviceDetail.do", //详情页
	addItem:base+"user/addServiceLeaveWord.do" //添加评论
};

require.config({
	baseUrl:baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"util"   : "dev/common/util",
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter" : "dev/common/userCenter",		
		"area"	: "util/area",
		"pages"   :"dev/common/pages",
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20151116"
});

define(["extend","dialog","head","entry","userCenter","pages","area","ajaxform"],function($,d,h,en,uc,p,a,f){
	//window.sitePage="p-doGood";
	h.init();
	d.init();
	en.init();
	uc.init();
	p.init({afterObj:$("#serviceDetail")});
	p.changeCallback=function(){ items.getItem(p.curPage);} 
	p.changeCallback=function(){
		items.getItem(p.curPage);
	};
	uc.selectCallback=function(){
		items.getItem(p.curPage);
	};
 
	var itemsObj=$("#serviceDetail");
    var items={ 
		init:function(){
			var that=this;
			that.getItem();
			
			$('#tijiao_button').click(function(){
				if($.trim($('#user_txt').val()) == null || $.trim($('#user_txt').val()) == ''){
					$('#wrong_cuo2').show();
					return false;
				}
				if($.trim($('#phone_txt').val()) == null || $.trim($('#phone_txt').val()) == ''){
					$('#wrong_cuo3').show();
					return false;
				}
					that.addServiceLeaveWord();
			})
		},
		getItem:function(page){ 
			var that=this,serviceId=$('#serviceId').val(),orderBy=$('#orderBy').val(),orderDirection=$('#orderDirection').val();
			var flag=false;
			if(this.serviceId!=serviceId){
				flag=true;
				this.serviceId=serviceId; 
			}
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.itemDetail,
				data:{pageNum:10,page:page,orderBy:orderBy,orderDirection:orderDirection,serviceId:serviceId,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.result==0){
							var total=data.total,pageNum=data.pageNum,items=data.items,len=items.length,html=[];
							len=len>pageNum?pageNum:len;
							for(var i=0;i<len;i++){
								var _item=items[i];
								if(_item.customerId != null){
									html.push('<div class="kefuhuifubox">');
									html.push('<div class="kefu_tx"><img  style="width:80px;height:80px;"  src="http://www.17xs.org/res/images/kefuzhongxin/kefutouxiang.png">');
									html.push('<span style="color:#ee7a00;font-size:14px;display:inline-block;text-align:center;margin-left:60px;margin-top:10px;width:88px;">客服小善</span>');
									html.push('</div>');
									html.push('<div class="kefu_answer">');
									html.push('<div class="zj_yonghu">');
									html.push('<p>尊敬的用户：</p> <p>您好！欢迎来到善园客服中心，小善很荣幸为您服务！</br>');
									html.push('</div>');
									html.push('<div class="kefu_answer_nr">');
									html.push('<p>'+_item.reply+'</p>');
									html.push('</div>');
									html.push('<div class="kefu_answer_address">');
									html.push('<div class="kfad_zhichi"><p>感谢您对善园的关注与支持！</br></p> <p style="color:#ee7a00;">www.17xs.org</p></div>');
									html.push('<div class="kfad_time">回复于<span style="margin-left:10px;">'+(new Date(_item.createTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
									html.push('</div> </div> </div>');
								}else{
									html.push('<div class="wangyou">');
									if(_item.headUrl != null){
										html.push('<div class="wangyou_tx"><img  style="width:80px;height:80px;" src="'+_item.headUrl+'">');
									}else{
										html.push('<div class="wangyou_tx"><img  style="width:80px;height:80px;"  src="http://www.17xs.org/res/images/kefuzhongxin/touxiang5.png">');
									}
									if(_item.userNickName != null){
										html.push('<span>'+_item.userNickName+'</span></div>');
									}else{
										html.push('<span>爱心人士</span></div>');
									}
									html.push('<div class="wangyou_nr">');
									html.push('<div class="wangyou_nr1"><span>'+_item.reply+'</span></div>');
									html.push(' <div class="wangyou_nr2">回复于<span style="margin-left:10px;">'+(new Date(_item.createTime)).pattern("yyyy-MM-dd HH:mm")+'</span></div>');
									html.push('</div> </div>');
								}
							}  
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
						}else if(data.result==1){
							if(status==0){
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有回答！！！</div></li>');
							}else if(status==1){
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有回答！！！</div></li>');
							}else{
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有回答！！！</div></li>');
							}
						}else if(data.result==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');
							
						}
					}
				}
			});
		},
		addServiceLeaveWord:function(){
			var that = this,s={},serviceId=$('#serviceId').val(),userId=$('#userId').val(),reply=$('#reply').val(),name=$('#user_txt').val(),mobile=$('#phone_txt').val();
			
			s.serviceId=serviceId;s.userId=userId;s.reply=reply;s.name=name;s.mobile=mobile;
			$.ajax({
				url:dataUrl.addItem,
				data:s,
				cache:false,
				success:function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else{
						window.location.href="http://www.17xs.org/user/toServiceDetail.do?id="+$('#serviceId').val();
					}
				},
				error:function(r){
					d.alert({content:'提交失败！，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		}
	};
	
	items.init();
	
});