       //项目管理
       function projectManage(projectId){
            //将点击获取的项目id存储起来
            $("#projectId").val(projectId)
            $(".layer").slideDown();
		
        }
        //项目反馈
       function projectCommentBatch(projectId){
           $("#projectId").val(projectId);
           var state=$("#state").val();
           window.location.href=window.baseurl+"uCenterProject/projectFeedBack.do?projectId="+projectId+"&state="+state;
           
        }
        function reprotForm(){
        	$('.layer').hide();
        	$("#msg").html('<p>报名清单正在火速开发中，请耐心等待！</p>');
			$("#msg").show();
			setTimeout(function () {
				  $("#msg").hide();
			}, 2000);
			return ;
        }
        $(function(){
            var state=$('#state').val();
            //获取选中状态
           if(state==210){
        		$('#li1').addClass("ligg").siblings().removeClass("ligg");
        	}
        	if(state==250){
        		$('#li2').addClass("ligg").siblings().removeClass("ligg");
        	}
        	if(state==260){
        		$('#li3').addClass("ligg").siblings().removeClass("ligg");
        	}
        	if(state==230){
        		$('#li4').addClass("ligg").siblings().removeClass("ligg");
        	}
        
        	$('#end').click(function(){//提前结束项目弹框
        		$('.moved_up').show();
        	});
        	$('.end_fl').click(function(){//提前结束项目：确定
        		$('.moved_up').hide();
        		$('.layer').hide();
        		var projectId=$("#projectId").val(),deadExplain=$('#deadExplain').val();
        		$.ajax({
				url:window.baseurl+"uCenterProject/beforeEndProject.do",
				data:{id:projectId,deadExplain:deadExplain},
				success: function(result){
					if(result.flag==1){//成功
						$("#msg").html('<p>提前结束项目成功！</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
				        location.reload();
						return ;
					}
					else if(result.flag==-1){//为登录
						window.location='http://www.17xs.org/ucenter/user/Login_H5.do';
					}
					else{//失败
						$("#msg").html('<p>'+result.errorMsg+'</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
						return ;
					}
				}
			})
        	});
        	$('.end_fr').click(function(){//提前结束项目：取消
        	 	$('.moved_up').hide();
        		return;
        	});
        	$('.layer_qx').click(function(){//layer：取消
        		$('.layer').hide();
        		return;
        	})
        	
        })
        function withDrawDeposit(){//善款提现
            //将点击获取的项目id存储起来
           var projectId=$("#projectId").val();
            //alert(projectId);
            window.location.href=window.baseurl+"uCenterProject/showAccountNumber.do?projectId="+projectId;
		
        }
        
        function edit(){
           var projectId=$("#projectId").val();
           var state=$("#state").val();
            window.location.href=window.baseurl+"uCenterProject/editPublicRelease.do?projectId="+projectId+"&state="+state;
		
        }
        function projectList(state ,i){
        
            var url="";
	        if(state=="210"){
	         	url=window.baseurl+"uCenterProject/uCenterProjectList.do?state=210";
	        }
           
            if(state=="250"){
                url=window.baseurl+"uCenterProject/uCenterProjectList.do?state=250";
            }
            
            if(state=="260"){
            	url=window.baseurl+"uCenterProject/uCenterProjectList.do?state=260";
            }
            if(state=="230"){
            	url=window.baseurl+"uCenterProject/uCenterProjectList.do?state=230";
            }
            window.location.href=url+"&currentPage="+i;
      
          
        }
       
        
        //回到项目发布页面
        function releaseProject(){
        	window.location.href=window.baseurl+"project/releaseH5Project.do";
        }
       
        $(function () { 
	       //当页面的数据显示完了是隐藏“加载更多”
	      var tips=$("#tips").val(); 
	       var total=$("#total").val();     
	       var counts=$('input[name="count"]');
	       if(total>counts.length){
	          $("#more").show();
	       
	       }else{
	          $("#more").hide();
	       }
	       if(tips==1) {
	           $('#projectlist').append('<p class="zwsj">暂无数据<p>');
	       } 
	      
	       //当点击加载更多事件需要ajax请求
	        var currentPage=1;  
	       $('#more').click(function(){	
			 	var state=$("#state").val();
			 	currentPage=currentPage+1;
			 	var uRl=window.baseurl+"uCenterProject/ajaxProjectList.do";
			 	$.ajax({
				url:uRl,
				type: 'post',      //POST方式发送数据
                async: false,      //ajax同步
				data:{state:state,currentPage:currentPage},
				success: function(result){
					var html=[];
					if(result.result==0){
						var data=result.items,datas=data,len=data.length;
						for(var i=0;i<len;i++){
						
						html.push('<div class="initator_conter">');
						html.push('<input type="hidden" name="count" />');
						html.push('<div class="tor_top">');
						html.push('<div class="torfl">');
						html.push('<img src="'+datas[i].coverImageUrl+'" />');
						html.push('</div>');
						html.push('<div class="torfr">');
						html.push('<h5 class="frone">'+datas[i].title+'</h5>');
						html.push('<div class="frtwo">');
						html.push('<p class="frtwo_fl">目标&nbsp;<span class="frtwo_sp">'+datas[i].cryMoney +'</span></p>');
						html.push('<p class="frtwo_fr">进度&nbsp;<span class="frtwo_sp">'+datas[i].donatePercent +'</span></p>');
						html.push('</div>');
						html.push('<div class="frthree">'+datas[i].lastUpdateTime+'</div>')
						html.push('</div></div>');
						html.push('<div class="tor_btn">');
					    html.push('<ul>');
					    html.push('<a href="initator_detal.html">');
						html.push('	<li class="btn_li">项目数据</li>');
						html.push('</a>');
						html.push('<a href="javascript:void();">');
						html.push('	<li class="btn_li">项目反馈</li>');
						html.push('</a>');
						html.push('<a href="javascript:void();" onclick="projectManage('+datas[i].id+')">');
						html.push('	<li class="btn_li guanli">项目管理</li>');
						html.push('</a>');
						html.push('</ul>');
						html.push('</div>');
						html.push('</div>');	
				
						}
						//$('#projectlist').append(html.join(''));
						$('#add').append(html.join(''));
						var counts=$('input[name="count"]');
					       if(total>counts.length){
					          $("#more").show();
					       
					       }else{
					          $("#more").hide();
					       }
					}else {
					  alert("没有更多数据了")
					}
				},
				error:function(){
				   alert("请求发生错误")
				}
			});
			 	
			});
	        
        });
      
      