var base=window.baseurl;
var dataUrl={
		donationList:base+"user/donationlist_invoice.do",//捐款列表
		addInvoiceUrl:base+"user/addInvoice.do", //索取发票入库
		editAddressUrl:base+"user/editAdress.do", //用户地址更新
		addAddressUrl:base+"user/addAdress.do" //用户地址入库
};
require.config({
	baseUrl:base+"/res/js",
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
	urlArgs:"v=20151010"
});

define(["extend","head","dialog","entry","userCenter","pages"],function($,h,d,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-getInvoice";
	h.init();
	d.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		getInvoice.getData(p.curPage);
	};
	uc.selectCallback=function(){
		getInvoice.getData(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var invoiceAmount1=$("#invoiceAmount1").text(),invoiceAmount2=$("#invoiceAmount2").text(),totalMoney=0,donateInfo="",info=$("#info").val();
	var getInvoice={
		time:null, 
		init:function(){	
			var that=this;
			that.getData();
			
		   $('#addAddress').click(function(){
			   $('#editAddr').show();
			   $('#addr').hide();
			   $('#flag').val(0);
		   })
		   $('#editAddress').click(function(){
			   $('#editAddr').show();
			   $('#addr').hide();
			   $('#flag').val(1);
		   })
		   
		   $('#confirm').click(function(){
			   var flag=$('#flag').val();
			   if(flag == 1){
				   that.editAddress();
			   }
			   if(flag == 0){
				   that.addAddress();
			   }
			   if(flag == -1){
				   that.addInvoice();
			   }
		   })
		   
		},
		
		addAddress:function(){
			var that = this,p={},name = $("#name").val(),mobile=$('#mobile').val(),
			detailAddress=$('#detailAddress').val();
			
			p.name=name;p.mobile=mobile;p.detailAddress=detailAddress;
			$.ajax({
				url: dataUrl.addAddressUrl,
				data:p,
				cache   : false,
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else{
						that.addInvoice();
					}
				},
				error   : function(r){ 
					d.alert({content:'提交失败！，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		
		editAddress:function(){
			var that = this,p={},id=$('#addressId').val(),name = $("#name").val(),mobile=$('#mobile').val(),
			detailAddress=$('#detailAddress').val();
			
			p.id=id;p.name=name;p.mobile=mobile;p.detailAddress=detailAddress,p.province="",p.city="",p.area="";
			$.ajax({
				url: dataUrl.editAddressUrl,
				data:p,
				cache   : false,
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else{
						that.addInvoice();
					}
				},
				error   : function(r){ 
					d.alert({content:'提交失败！，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		
		addInvoice:function(){
			var that = this,p={},addressId=$('#addressId').val(),info = $("#info").val(),flag=$('#flag').val();
			if(flag != -1){
				invoiceHead=$('#invoiceHead1').val();
				invoiceAmount = $('#invoiceAmount1').text();
			}else{
				invoiceHead=$('#invoiceHead2').val();
				invoiceAmount = $('#invoiceAmount2').text();
			}
			invoiceAmount = invoiceAmount.replace(",","");
			p.addressId=addressId;p.invoiceAmount=invoiceAmount;p.invoiceHead=invoiceHead;p.info=info;
			$.ajax({
				url: dataUrl.addInvoiceUrl,
				data:p,
				cache   : false,
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else{
						window.location.href="http://www.17xs.org/ucenter/getInvoicList.do";
					}
				},
				error: function(r){ 
					d.alert({content:'提交失败！，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		
		getData:function(page){ 
			if(!page){page=1;} 
			var flag=false;
			if(page<2){flag=true;}
			$.ajax({
				url:dataUrl.donationList,
				data:{page:page,pageNum:10,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len,totalMoney=0,donateInfo=""; 
						$("#checkAll").attr("checked",false);
						$("#invoiceAmount1").html('0.00');
						$("#invoiceAmount2").html('0.00');
						$("#info").val("");
						html.push('<colgroup><col width="15%"></col><col width="25%"></col><col width="16%"></col><col width="16%"></col><col width="28%"></col></colgroup>');
						html.push('<thead><tr><th><input type="checkbox" onclick="checkAll();" id="checkAll"name="" style="margin:0px 20px;">全选</th><th>捐助项目</th><th>捐助金额</th><th>捐助时间</th><th>订单号</th></tr></thead>');
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							totalMoney=totalMoney+idata.dMoney,donateInfo=donateInfo+idata.id+"_";
							html.push('<tr>');
							html.push('<td class="text-left pl"><input type="checkbox" class="checkbox" check="" id="checkbox'+i+'" value="'+idata.dMoney+'" name="'+idata.id+'" onclick="check(this);"></td>');
							html.push('<td>'+idata.title+'</td>');
							html.push('<td><i class="sum">'+$.formatNum(idata.dMoney)+'</i>元</td>');
							html.push('<td>'+(new Date(idata.dTime)).pattern("yyyy-MM-dd")+'</td>');
							html.push('<td>'+idata.tranNum+'</td>');
							html.push('<tr>');
						}
						$("#totalMoney").val(totalMoney.toFixed(2));
						$("#donateInfo").val(donateInfo);
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						listObj.html('<div class="prompt"><div class="listno yh">您还没有捐赠记录，无法开票</div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(getInvoice.getData);
						}
					}
				}
			});
		},
		
		
		
	};
	
	getInvoice.init(); 
	
});

function priceCount(){ 
	var info="";
	if($(".checkbox").attr("checked")=='checked'){
		invoiceAmount1 = $.formatNum($("#totalMoney").val());
		invoiceAmount2 = $.formatNum($("#totalMoney").val());
		info = $("#donateInfo").val();
	}
	$("#invoiceAmount1").html(invoiceAmount1);
	$("#invoiceAmount2").html(invoiceAmount2); 
	$("#info").val(info);
}

function checkAll(){
	if($("#checkAll").attr("checked")=='checked'){
		$(".checkbox").attr("checked",true);
		priceCount();
	}else{
		$(".checkbox").attr("checked",false);
		$("#invoiceAmount1").html('0.00');
		$("#invoiceAmount2").html('0.00');
		$("#info").val("");
	}
}

function check(e){
	var invoiceAmount1=$("#invoiceAmount1").text(),invoiceAmount2=$("#invoiceAmount2").text();
	var money = $(e).val(),info=$("#info").val();
	invoiceAmount1 = invoiceAmount1.replace(",",""); invoiceAmount2 =invoiceAmount2.replace(",","");
	if($(e).is(':checked')){
		invoiceAmount1 = Number(invoiceAmount1) + Number(money);
		invoiceAmount2 = Number(invoiceAmount2) + Number(money);
		info = info + $(e).attr("name")+"_";
	}else{
		$("#checkAll").attr("checked",false);
		invoiceAmount1 = Number(invoiceAmount1) - Number(money);
		invoiceAmount2 = Number(invoiceAmount2) - Number(money);
		info = info.replace($(e).attr("name")+"_", "");
	}
	$("#invoiceAmount1").html(invoiceAmount1.toFixed(2));
	$("#invoiceAmount2").html(invoiceAmount2.toFixed(2));
	$("#info").val(info);
}


