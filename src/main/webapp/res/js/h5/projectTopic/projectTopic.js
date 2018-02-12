var base=window.baseurl;
var dataUrl={
	projectList:base+'projectTopic/ajaxProjectTopic.do'
	
};
var projectTopic={
		init:function()
		{
			var that=this;
			that.page(1,20);
			$('.jzgd').click(function(){
				var currentPage= $('#currentPage').val();
				if(currentPage=='-1'){
					return false;
				}
				else{
					that.page(Number(currentPage)+1,20);
				}
			})
		
			
		},
		
		page:function(pageNum,pageSize){
			var id = $('#topicId').val();
			$.ajax({
				url:dataUrl.projectList,
				data:{pageNum:pageNum,pageSize:pageSize,id:id,t:new Date().getTime()},
				success: function(result){
					//alert(result.result);
					if(result.result==2){
						alert('网络异常！');
						return false;
					}
					if(result.result==1){
						alert("无数据！");
						return false;
					}
					var html=[];
					//$('.seminar_content').html(html.join(''))
					if(pageNum<=result.total){
						var data=result.items,datas=data,len=data.length,totalPage=result.total;
						for(var i=0;i<len;i++)
						{
							html.push('<a href="http://www.17xs.org/project/view/?projectId='+datas[i].id+'">');
							html.push('<div class="seCont">'); 
							html.push('<h4>'+datas[i].title+'</h4>');
						    html.push('<div class="seTenter">');
						    html.push('<div class="seImgfl"><img src="'+datas[i].coverImageUrl+'"></div>');
						    html.push('<div class="seFr">');
						    html.push('<p class="seText1">目标金额：'+datas[i].cryMoney+'元</p>');
						    html.push('<p class="seText1">已筹金额：'+datas[i].donatAmount+'元</p>');
						    html.push('<div class="setPr">');
						    html.push('<div class="sePerson"><i class="seIcon"></i><p class="seIcon_fr">共&nbsp;<span>'+datas[i].peopleNum+'</span>&nbsp;人捐助</p></div>');
						    html.push('<a href="#" class="sePersonfr">立即捐助</a>');
						    html.push('</div></div></div>');
						    html.push('<div style="clear: both"></div>');
						    html.push('</div></a>');
						}
						$('.seminar_content').append(html.join(''));
						if(Number(pageNum)*Number(pageSize)>=totalPage){
							$('.jzgd').html('没有更多数据了');
							$('#currentPage').val(-1);
						}
						else{
							$('#currentPage').val(pageNum);
						}
					}
					if(html.length==0){
						$('.jzgd').html('没有数据~');
						//html.push('<p class="noData">没有数据~</p>');
						//$('#project_list').html(html.join(''));
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
	projectTopic.init();
