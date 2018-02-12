var base=window.baseurl;
var dataUrl={
	myGoodLibraryDetails:base+"myGoodLibrary/myGoodLibraryDetails.do",//提交
	leftMoneyToCharge:base+"myGoodLibrary/shankuRecharge.do" ,  //用户余额进行善库充值
	goodLibraryUserDonationlist:base+"myGoodLibrary/goodLibraryUserDonationlist.do"//捐款记录分页
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
	window.mCENnavEtSite="m-goodlibrary"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$(".goodRecord1")});//捐助明细分页
	//p.init({afterObj:$(".goodRecord")});//善库成员分页
	p.changeCallback=function(){ reg.pages(p.curPage);}
	//p.changeCallback=function(){ reg.pages2(p.curPage);}
	var reg={
			photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form'));
			//列表标题
			
			that.goodLibraryDetails();
			
			//点击捐赠明细弹框
			//善库充值
			$('.goodthree_btn1').click(function(){
				var userBalance=$("#userBalance").val();
				if(userBalance>0){
					//进行充值
					$("#leftMoney").html(userBalance);
					$(".popMoney").show();
					
				}else{
					d.alert({content:"用户余额不足,请先去个人账户充值！",type:'error'});
				}
			});
			//弹出或关闭二维码
			$('.goodthree_btn2').click(function(){
				if($('#qrcode').css("display")=="block"){
					$('#qrcode').hide();
				}
				else{
					$('#qrcode').show();
				}
			});
			//关闭弹框
			$('.closeDialog').click(function(){
					$(".popMoney").hide();
					$(".chargeMoney").val("");
					
			});
			$('.popUpcloseDialog').click(function(){
				//$("#defaultValue").val(0);
				$(".popUp").hide();	
			});
			
			$('.tancBtn').click(function(){	
				var chargeMoney=$("#chargeMoney").val();
				var userBalance=$("#userBalance").val();
				//alert(chargeMoney+":"+userBalance);
				if(chargeMoney>userBalance){
					d.alert({content:"充值金额不足！",type:'error'});
					return false;
				}else{
					that.leftMoneyCharge(chargeMoney);
				}
			});
			$('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"></div>');
					$('#form').submit();
				}
				return false;
			});
		},

		//根据个人余额充值的方法
		leftMoneyCharge:function(chargeMoney){
			var shanKuUserId=$("#shanKuUserId").val();
			var libraryId=$("#libraryId").val();
			$.ajax({
				url:dataUrl.leftMoneyToCharge,
				data:{
					shanKuUserId:shanKuUserId,
					libraryId:libraryId,
					chargeMoney:chargeMoney,
					t:new Date().getTime()
				},
				success: function(result){ 
					if(result.flag==1){
						$(".popMoney").hide();
						d.alert({content:"充值成功！",type:'success'});
						window.href.location="http://www.17xs.org/myGoodLibrary/gotoMyGoodLibraryDetails.do?id="+libraryId;
					}else if(result.flag==0){
						$(".popMoney").hide();
						d.alert({content:"充值失败！",type:'error'});
						return false;
					}
				}
			}); 
			
		},
		photoPic:function(file){
		    var that=this, filepath =$(file).val();				
		    var extStart=filepath.lastIndexOf(".")+1;		
			var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
			if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP')
			{	
				d.alert({content:"上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！",type:'error'});
				return false;
			}
			var file_size = 0;
			  if (!$.browser.msie) {
					file_size = file.files[0].size;
					var size = file_size / 1024;
					if (size > 2048) {
						$("#msg").html('<p>上传的图片大小不能超过2M！</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
						return false;
					} 
				}
		return true;
	},
		ajaxForm:function(obj){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {status.empty();},
				uploadProgress: function(event, position, total, percentComplete) {
				},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					if(json.result==1){
						return false;
					}else{
						var html=[];
						html.push('<div class="item add"><img src="'+json.imageUrl+'" id="'+json.imageId+'" class="io" name="io" style="width:80px;height:80px;"/>');
						html.push('<a href="javascript:void();" class="add_bjtx1"><p class="bjtx" style="display:none">编辑头像</p></a>');
						html.push('<input type="file" name="file" hidefocus="true" id="imgFile" class="imgFile">');
                        html.push('<input type="hidden" name="type" id="type" value="9"></div>');
						$('#imgList').html(html.join(''));
						$('#imgFile').change(function(){
							var p=that.photoPic(this);
							if(p){
								var imgBox=$("#imgList"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
								addObj.hide();
								addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="80px"></div>');
								$('#form').submit();
							}
							return false;
						});
						 $(".imgFile").mouseover(function(){
					            $(".add_bjtx1").addClass("add_bjtx");
					            $(".bjtx").show();
					        });
					    $(".imgFile").mouseout(function(){
					    $(".add_bjtx1").removeClass("add_bjtx");
					    $(".bjtx").hide();
					    });
					}
				},
				error: function(data) {
				}
			}); 
		},
		//查询某个善库成员的数量
		goodLibraryDetails:function(){
			var headTitle=$(".headTitle").html();
			var libraryId=$("#libraryId").val();
			$.ajax({
				url:dataUrl.myGoodLibraryDetails,
				data:{
					id:libraryId,
					pageNum:1,
					pageSize:10,
					t:new Date().getTime()
				},
				success: function(result){ 
					if(result.flag==1){
						var html=[];
						datas=result.obj;
						data=datas.items;
						//total=datas.total;
						for(var i=0;i<data.length;i++){
							html.push('<dd>');
							html.push('<li>'+data[i].userName+'</li>');
	                        html.push('<li>'+data[i].mobileNum+'</li>')
	                        var address=data[i].familyAddress==null?'暂无':data[i].familyAddress;
	                        html.push('<li ><p class="dm">'+address+'</p></li>');
	                        var balance =data[i].balance==-1?"无限":data[i].balance;
	                        html.push('<li>'+data[i].donateAmountSum+'/'+balance+'</li>')
	                        html.push('<li>'+data[i].donateNum+'次</li>');
	                        html.push('<li>'+data[i].chargeNum+'次</li>');
	                        //html.push('<li>'+data[i].createTime+'</li>');
	                        if(data[i].donateNum==0){
	                        	html.push('<li><div class="mx11'+i+' mx" style="background:#aaa">捐赠明细</div></li>'); 
	                        }
	                        else{
	                        	html.push('<li><a href="javascript:void();" class="mx11'+i+' mx mm"><input type="hidden" value="'+data[i].userId+'"/>捐赠明细</a></li><div style="clear:both"></div>');
	                        }
	                        
	                        html.push('</dd>')
						}
						//
						/*if(total>1){
							p.pageInit({pageLen:total,isShow:true}); 
						}else if(total<2){
							p.pageInit({pageLen:total,isShow:false});
							html.push('<dd><div>没有更多数据了</div></dd>');
						}*/
						//
						$(".headTitle").html(headTitle+html.join(""));
					}else if(result.flag==0){
						//暂无成员数据
						$(".goodRecord").html("<div class='noData'>暂无成员数据</div>");
						//d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
					$('.mm').click(function(){
						//$('#defaultValue').val(1);
						var page=p.curPage;
						var userId=$(this).children("input").val();
						$('#donateUserId').val(userId);
						$(".donateDetails>dd").remove();
				   		var donateDetails=$(".donateDetails").html();
				   		$.ajax({
							url:dataUrl.goodLibraryUserDonationlist,
							data:{
								id:userId,
								pageNum:page,
								pageSize:10,
								t:new Date().getTime()
							},
							success: function(result){ 
								if(result.flag==1){
									var html=[];
									datas=result.obj;
									data=datas.items;
									total=datas.total;
									//html.push(donateDetails);
									for(var i=0;i<data.length;i++){
										html.push('<dd>');
										html.push('<li class="dtperson">'+data[i].name+'</li>');
				                        html.push('<li class="dtmoney">'+data[i].donatAmount+'元</li>');
				                        html.push('<li class="dtway">'+data[i].donateType+'</li>');
				                        html.push('<li class="dtproject">'+data[i].title+'</li>');
				                        html.push(' <li class="dttime">'+data[i].showTime+'</li>');
				                        html.push('</dd>');
									}
									//alert(donateDetails+html.join(""));
									
									if(total>1){
										p.pageInit({pageLen:total,isShow:true}); 
									}else if(total<2){
										p.pageInit({pageLen:total,isShow:false});
										html.push('<dd><div>没有更多数据了</div></dd>');
									}
									$(".donateDetails").html(donateDetails+html.join(""));
									$(".popUp").show();
								}else if(result.flag==0){
									//暂无成员数据
									//$(".donateDetails").html(donateDetails);
									$(".popUp").hide();
									d.alert({content:"该成员暂无捐款记录",type:'error'});
									return false;
								}
							}
						}); 
					});
				}
			}); 
		},
		pages:function(page){
			var userId=$('#donateUserId').val();
			//var defaultValue=$('#defaultValue').val();
			if(page<=0){
				page=1;
			}
			//-------------------------------------
			/*if(defaultValue==0){
				var libraryId=$("#libraryId").val();
				$.ajax({
					url:dataUrl.myGoodLibraryDetails,
					data:{
						id:libraryId,
						pageNum:page,
						pageSize:1,
						t:new Date().getTime()
					},
					success: function(result){ 
						if(result.flag==1){
							var html=[];
							datas=result.obj;
							data=datas.items;
							total=datas.total;
							for(var i=0;i<data.length;i++){
								html.push('<dd>');
								html.push('<li>'+data[i].userName+'</li>');
		                        html.push('<li>'+data[i].mobileNum+'</li>')
		                        var address=data[i].familyAddress==null?'暂无':data[i].familyAddress;
		                        html.push('<li ><p class="dm">'+address+'</p></li>');
		                        var balance =data[i].balance==-1?"无限":data[i].balance;
		                        html.push('<li>'+data[i].donateAmountSum+'/'+balance+'</li>')
		                        html.push('<li>'+data[i].donateNum+'次</li>');
		                        html.push('<li>'+data[i].chargeNum+'次</li>');
		                        //html.push('<li>'+data[i].createTime+'</li>');
		                        if(data[i].donateNum==0){
		                        	html.push('<li><div class="mx11'+i+' mx" style="background:#aaa">捐赠明细</div></li>'); 
		                        }
		                        else{
		                        	html.push('<li><a href="javascript:void();" class="mx11'+i+' mx mm"><input type="hidden" value="'+data[i].userId+'"/>捐赠明细</a></li><div style="clear:both"></div>');
		                        }
		                        
		                        html.push('</dd>')
							}
							//
							if(total>1){
								p.pageInit({pageLen:total,isShow:true}); 
							}else if(total<2){
								p.pageInit({pageLen:total,isShow:false});
								html.push('<dd><div>没有更多数据了</div></dd>');
							}
							//
							$(".headTitle>dd").remove();
							$(".headTitle").html(html.join(""));
						}else if(result.flag==0){
							//暂无成员数据
							$(".goodRecord").html("<div class='noData'>暂无成员数据</div>");
							//d.alert({content:result.errorMsg,type:'error'});
							return false;
						}
						$('.mm').click(function(){
							$('#defaultValue').val(1);
							var page=p.curPage;
							var userId=$(this).children("input").val();
							$('#donateUserId').val(userId);
							$(".donateDetails>dd").remove();
					   		var donateDetails=$(".donateDetails").html();
					   		$.ajax({
								url:dataUrl.goodLibraryUserDonationlist,
								data:{
									id:userId,
									pageNum:page,
									pageSize:10,
									t:new Date().getTime()
								},
								success: function(result){ 
									if(result.flag==1){
										var html=[];
										datas=result.obj;
										data=datas.items;
										total=datas.total;
										//html.push(donateDetails);
										for(var i=0;i<data.length;i++){
											html.push('<dd>');
											html.push('<li class="dtperson">'+data[i].name+'</li>');
					                        html.push('<li class="dtmoney">'+data[i].donatAmount+'元</li>');
					                        html.push('<li class="dtway">'+data[i].donateType+'</li>');
					                        html.push('<li class="dtproject">'+data[i].title+'</li>');
					                        html.push(' <li class="dttime">'+data[i].showTime+'</li>');
					                        html.push('</dd>');
										}
										
										if(total>1){
											p.pageInit({pageLen:total,isShow:true}); 
										}else if(total<2){
											p.pageInit({pageLen:total,isShow:false});
											html.push('<dd><div>没有更多数据了</div></dd>');
										}
										$(".donateDetails").html(donateDetails+html.join(""));
										$(".popUp").show();
									}else if(result.flag==0){
										//暂无成员数据
										$(".popUp").hide();
										d.alert({content:"该成员暂无捐款记录",type:'error'});
										return false;
									}
								}
							}); 
						});
					}
				});
			}*/
			//----------------------------------------
			//else{
			$.ajax({
				url:dataUrl.goodLibraryUserDonationlist,
				data:{
					id:userId,
					page:page,
					pageSize:10,
					t:new Date().getTime()
				},
				success: function(result){ 
					if(result.flag==1){
						var html=[];
						datas=result.obj;
						data=datas.items;
						total=datas.total;
						//html.push(donateDetails);
						for(var i=0;i<data.length;i++){
							html.push('<dd>');
							html.push('<li class="dtperson">'+data[i].name+'</li>');
	                        html.push('<li class="dtmoney">'+data[i].donatAmount+'元</li>');
	                        html.push('<li class="dtway">'+data[i].donateType+'</li>');
	                        html.push('<li class="dtproject">'+data[i].title+'</li>');
	                        html.push(' <li class="dttime">'+data[i].showTime+'</li>');
	                        html.push('</dd>');
						}
						$(".donateDetails>dd").remove();
				   		var donateDetails=$(".donateDetails").html();
						$(".donateDetails").html(donateDetails+html.join(""));
						/*if(total>1){
							p.pageInit({pageLen:total,isShow:true}); 
						}else if(total<2){
							p.pageInit({pageLen:total,isShow:false}); 
						}*/
						$(".popUp").show();
					}else if(result.flag==0){
						//暂无成员数据
						$(".popUp").hide();
						d.alert({content:"该成员暂无捐款记录",type:'error'});
						return false;
					}
				}
			}); 
			//}
		}
	}
	reg.init()
})
