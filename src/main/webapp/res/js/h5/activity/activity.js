var base=window.baseurl;
var dataUrl={
	projectList:base+'activity/loadDonateRecord.do'
	
};
var projectTopic={
		init:function()
		{
			var that=this;
			that.page(1,1);
			$('.jiazai').click(function(){
				var currentPage= $('#currentPage').val();
				if(currentPage=='-1'){
					return false;
				}
				else{
					that.page(Number(currentPage)+1,1);
				}
			});
			 //高度
			   var det= $(".details").height();
			    //判断
			    if(det>200){
			        $(".details").addClass("detailHide");
			        $(".btnHide").show();
			        $(".white").show();
			    }else{
			        $(".details").removeClass("detailHide");
			        $(".btnHide").hide();
			        $(".white").hide();
			    }
			    //绑定
			    $(".btnHide>a").click(function(){
			        $(".details").removeClass("detailHide");
			        $(".btnHide").hide();
			        $(".white").hide();
			    });
		},
		
		page:function(pageNum,pageSize){
			var projectId = $('#projectId').val();
			$.ajax({
				url:dataUrl.projectList,
				data:{pageNum:pageNum,pageSize:pageSize,projectId:projectId,t:new Date().getTime()},
				success: function(result){
					if(result.result==2){
						alert('网络异常！');
						return false;
					}
					if(result.result==1){
						alert("无数据！");
						return false;
					}
					var html=[];
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length,totalPage=result.total;
						for(var i=0;i<len;i++)
						{
							html.push('<div class="record">');
							html.push('<span class="thumb">'); 
							html.push('<img src="'+data[i].coverImageUrl+'" class="fl">');
						    html.push('</span>');
						    html.push('<div class="name">');
						    html.push('<p class="name_p1">'+data[i].nickName+'</p>');
						    html.push('<p class="name_p2">支持<label>￥'+data[i].donatAmount+'</label></p>');
						    html.push('</div>');       
						    html.push('<div style="clear: both"></div>');
						    html.push('</div>');
						}
						$('#user_list').append(html.join(''));
						if(Number(pageNum)*Number(pageSize)>=totalPage){
							$('.jiazai').html('没有更多数据了');
							$('#currentPage').val(-1);
						}
						else{
							$('#currentPage').val(pageNum);
						}
					}
					if(html.length==0){
						$('.jiazai').html('没有数据~');
					}
				}
			});
			
		}
	};
	projectTopic.init();
