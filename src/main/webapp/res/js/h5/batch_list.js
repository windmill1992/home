var base=window.baseurl;
var dataUrl={
	projectList:base+'project/list_batch_h5.do',
	pDetail:base+'project/view_h5.do?projectId=',
	payMoney:base+'visitorAlipay/tenpay/batchdeposit.do',
	isUserId:base+'project/isEmptyUserId.do',
};
var h5batchList={
		
		
		init:function()
		{
			var that=this;
			var userId;
			//判断用户是否登录
			//h5batchList.isEmptyUserID();
			//初始化
			h5batchList.projectList(1,20,0,userId,0);
			
		    $(".pay_i").click(function(){
		            $(".pay_box").hide();
		        });
		        
		    $(".prsee_footer .button1 input").click(function(){
		            if($(this).prop("checked")){
		                $(".prsee_center input").attr("checked","checked");
		                if($(".prsee_footer dl input").prop("checked")){
		                    $(".footer_j").removeClass("buttonDisabled");
		                }
		            }else{
		                $(".prsee_center input").removeAttr("checked");
		                $(".footer_j").addClass("buttonDisabled");
		            }
		        });
		    $(".prsee_center input").click(function(){
		            if(isCheck() && $(".prsee_footer dl input").prop("checked")){
		                    $(".footer_j").removeClass("buttonDisabled");
		            }else{
		                $(".footer_j").addClass("buttonDisabled");
		            }
		        });
		    $(".prsee_footer dl input").click(function(){
		            if($(this).prop("checked")&&isCheck()){
		                $(".footer_j").removeClass("buttonDisabled");
		            }else{
		                $(".footer_j").addClass("buttonDisabled");
		            }
		        });
			
			$('#changeP').click(function(){
				var currentPage = $('#currentPage').val();
				var totalPage = $('#totalPage').val();
				currentPage =parseInt(currentPage)+1 ;
				if(currentPage > totalPage)
				{
					currentPage = 1 ;
				}
				var focusState=$('#focusState').val();
				h5batchList.projectList(currentPage,20,focusState,userId,$('#key').val());
			});
			
			var plistId = "" ;
			$('#doSubmit').click(function(){
				$('#singleMoney').val('');
				$('#pmoney').text('');
				$('#checkInfo').hide();
				$('#checkPro').hide();
				var pcount = 0 ;
					$('input:checkbox[name=batchDonate]:checked').each(function(i){
						if(0 == i)
						{
							plistId = $(this).val();
						}
						else
						{
							plistId += (","+$(this).val());
						}
						pcount++ ;
					});
					$('#plistId').val(plistId);
					$('#pcount').text(pcount);
					
					if(pcount < 2)
					{
						$('#checkPro').show();
						setTimeout(function(){
							$('#checkPro').hide();
						},1000)
						return ;
					}
					else
					{
						 $(".pay_box").show();
					}
			});
			
			
			$('#singleMoney').bind('input propertychange', function() {
				//$('#checkInfo').hide();
				var pcount = $('#pcount').text();
				var singleMoney = Number($('#singleMoney').val());
		
				var pmoney = (Number(singleMoney*pcount)).toFixed(2) ;
				$('#pmoney').text(pmoney);
				
			});
			
			$('#singleMoney').focus(function(){
				$('#checkInfo').hide();
			});
			
			$('#donateSubmit').click(function(){
				var singleMoney = Number($('#singleMoney').val());
				if(singleMoney == 0)
				{
					$('#checkInfo').html('<p>请输入单个项目捐赠金额</p>');
					$('#checkInfo').show();
					setTimeout(function(){
						$('#checkInfo').hide();
					},1000);
					return ; 
					
				}
				var cxy= $('#xieyi').attr("checked");
				if(cxy != 'checked')
				{
					$('#checkInfo').html('<p>请阅读善园基金会协议</p>');
					$('#checkInfo').show();
					setTimeout(function(){
						$('#checkInfo').hide();
					},1000);
					return ; 
				}
				var totalAmount = Number($('#pmoney').text());
				var plistId = $('#plistId').val();
				var nickName = $('#nickName').val();
				var mobile = $('#mobile').val();
				
				var len = mobile.length;
				
				if(len > 0)
				{
	
					var reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
					var regValue = reg.test(mobile);
			        if(regValue==false&&len!=11){ 
						$('#checkInfo').html('<p>手机号码格式不正确</p>');
						$('#checkInfo').show();
						setTimeout(function(){
							$('#checkInfo').hide();
						},1000);
						return ;
			        }
				}
				
				var pcount = $('#pcount').text();
				var perMoney = $('#singleMoney').val();
				var extensionPeople = $('#extensionPeople').val();
				
				if(totalAmount > 0 && (plistId != '' && plistId != null &&　plistId != 'undefined'))
				{
					window.location.href=dataUrl.payMoney+"?plistId="
						+plistId+"&amount="+totalAmount+"&pcount="+pcount+"&perMoney="+perMoney+"&touristName="+nickName
						+"&touristMobile="+mobile+"&extensionPeople="+extensionPeople;
				}
				else
				{
					return ; 
				}
				
			});
			
			var click = 0 ;
			$('#project_list').click(function(){
				$('input:checkbox[name=batchDonate]:checked').each(function(i){
					click++ ;
				});
				if(click > 1)
				{
					 $(".footer_j").removeClass("buttonDisabled");
				}
			});
			
			$('#noDonate').click(function(){
				$('#doSubmit').addClass("buttonDisabled");
				var currentPage = 1;
				document.getElementById("focusState").value="0";
				document.getElementById("topicId").value="0";
				h5batchList.projectList(currentPage,20,0,userId,$('#key').val())
			})
			
			$('#Donate').click(function(){
				$('#doSubmit').addClass("buttonDisabled");
				var currentPage = 1;
				document.getElementById("focusState").value="1";
				document.getElementById("topicId").value="0";
				h5batchList.projectList(currentPage,20,1,userId,$('#key').val())
			})
			
			$('.cue_pl').click(function() {
				$('#loginCue').hide();
				window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=batchDonate';
			})
			$('.cue_fr').click(function() {
				$('#loginCue').hide();
				$('#key').val('1');
				document.getElementById('key').innerHTML=1;
				h5batchList.projectList(1,20,0,userId,1);
				return false;
			})
		
			
		},
		
		projectList:function(pageNum,pageSize,state,userId,key){
			var topicId=$('#topicId').val();
			if(Number(topicId)!=0){
				$('#noDonate').removeClass("gongy");
			}
			$.ajax({
				url:dataUrl.projectList,
				data:{page:pageNum,len:pageSize,status:1,sortType:2,t:new Date().getTime(),state:state,userID:userId,key:key,topicId:topicId},
				success: function(result){
					//alert(result.result);
					if(result.result==2){
						window.location.href=result.code;
					}
					if(result.result==-1&&result.key==0){
						$('#loginCue').show();
						return;
					}
					var html=[];
					$('#project_list').html(html.join(''))
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length,totalPage=result.total;
						for(var i=0;i<len;i++)
						{
							html.push('<li>');
							html.push('<a href = "'+dataUrl.pDetail+datas[i].itemId+'">');
							html.push('<div class="button1"><form action=""><input type="checkbox" name="batchDonate" value="'+datas[i].itemId+'"/></form></div>');
							if(datas[i].imageurl == null)
							{
								html.push('<img src="http://www.17xs.org/res/images/logo-def.jpg" alt="" />');
							}
							else
							{
								html.push('<img src="'+datas[i].imageurl+'"alt="" />');
							}
							html.push('<dl>');
							html.push('<dt><b>'+datas[i].title+'</b></dt>');
							html.push('<dd class="center_fl">目标<span>'+datas[i].cryMoney+'</span>元</dd>');
							html.push('<dd class="center_fr">已完成<span>'+datas[i].process+'%</span></dd>');
							html.push('</dl>');
							html.push('</a>');
							html.push('</li>');
						}
						$('#project_list').html(html.join(''));
						$('#currentPage').val(pageNum);
						$('#totalPage').val(totalPage);
						
						//alert(result.noDonateNum+','+result.donateNum);
					}
					if(html.length==0){
						html.push('<p class="noData">没有数据~</p>');
						$('#project_list').html(html.join(''));
						/*$ ("#Donate").addClass ('prsee_next gongy');
						$ ("#noDonate").removeClass ('gongy');
						h5batchList.projectList(1,20,1,userId,1);*/
					}
					document.getElementById('noDonateNum').innerHTML=result.noDonateNum;
					document.getElementById('donateNum').innerHTML=result.donateNum;
					if(Number(topicId)!=0){
						$("input[type='checkbox']").attr("checked","checked");
						$('#doSubmit').removeClass("buttonDisabled");
					}
				}
			});
			
		},
		/*isEmptyUserID:function(){	
			$.ajax({
				url:dataUrl.isUserId,
				data:{},
				success: function(data){
					if(data==0)
						
					}
				}
			});
			
		},*/
		isCheck:function(){
	        var flag = false;
	        $(".prsee_center input").each(function(){
	            if($(this).prop("checked")){
	                flag = true;
	            }
	        });
	        return flag;
	    },
	};
	h5batchList.init();
