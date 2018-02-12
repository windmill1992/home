var dataUrl = {
	items:"http://www.17xs.org/ucenter/getProjectVolunteer.do",
	addVolunteer:"http://www.17xs.org/project/addVolunteerView.do"
};
var page = 1;
var items={ 
	typeFlag:-1,
	statusFlag:-1,
	init:function(){
		var that = this;
		that.getItem(1,10);
		
		$('body').on('click','#more',function(){
			if($(this).hasClass('no')) return;
			that.getItem(++page,10);
			
		}).on('click','#addVolun',function(){
			var projectId = $("#projectId").val();
			location.href = dataUrl.addVolunteer +'?itemId='+ projectId;
		});
	},
	getItem:function(page,pageNum){ 
		var projectId = $("#projectId").val();
		$.ajax({
			url:dataUrl.items,
			data:{page:page,pageNum:pageNum,projectId:projectId,t:new Date().getTime()},
			cache:false,
			success: function(data){
				if(!!data){
					data = eval(data); 
					if(data.flag==1){
						var total = data.obj.nums,
							items = data.obj.data,
							len = items.length,
							html = [];
						for(var i=0;i<len;i++){
							var _item = items[i];
							html.push('<li>');
							html.push('<a href="javascript:;"><img src="http://www.17xs.org/res/images/h5/images/tx1.png" alt=""></a>');
							html.push('<dl>');
							if(_item.personType == 0){
								html.push('<dt><span>'+_item.name+'</span></dt>');
							}else if(_item.personType == 1){
								html.push('<dt><span>'+_item.groupName+'</span></dt>');
							}
							html.push('<dd>服务时间：'+_item.serviceTime+'</dd>');
							html.push('</dl></li>');
						}  
						$('#vList').append(html.join(''));
						if(total <= page * pageNum){
							$('#more').text('没有更多数据了~').addClass('no');
						}else{
							$('#more').text('点击加载更多~').removeClass('no');
						}
					}else{
						$('#more').text('还没有志愿者~').addClass('no');
					}
				}
			}
		});
	}
};

$(function(){
	items.init();
});

