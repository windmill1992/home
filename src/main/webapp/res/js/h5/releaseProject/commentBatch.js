var base = window.baseurl;
var dataUrl = {
	newLeaveWordList: base +'commentBatch/newLeaveWordList.do',			//根据条件查询留言列表
	addBatchNewLeaveWord: base +'commentBatch/addBatchNewLeaveWord.do'
};
var reg = {
	state: 0,
	page: 1,
	size: 0,
	timer: null,
	init: function(){
		var that = this;
		that.getLeaveWordList(that.page,that.state);
       
		$('body').on('click','.tor_a',function(){
			that.state = parseInt($(this).attr('data-state'));
			$("#batchList").html("");
        	$(this).addClass("ligg").siblings().removeClass('ligg');
        	that.page = 1;
        	that.size = 0;
        	that.getLeaveWordList(that.page,that.state);
        	if(that.state == 1){
        		$('#batchList').addClass('ed');
        	}else{
        		$('#batchList').removeClass('ed');
        	}
		}).on('click','.jiazai',function(){
			if($(this).hasClass('no'))return;
		    that.getLeaveWordList(++that.page,that.state);
		    
		}).on('click','.batch',function(){
			var len = $('.duihao1:checked').length;
			if(len == 0){
				that.showTips('请选择评论！');
			}else{
				$(".moved_up").show();
			}
		}).on('click','#confirm',function(){
			that.confirmCommentBatch();
			
		}).on('click','#cancel',function(){
			$(".moved_up").hide();
			
		}).on('change','#selAll',function(){
			if(this.checked){
				$('.duihao1').each(function(){
					this.checked = true;
				});
			}else{
				$('.duihao1').each(function(){
					this.checked = false;
				});
			}
		}).on('change','.duihao1',function(){
			var len1 = $('.duihao1').length,
				len2 = $('.duihao1:checked').length;
			if(len1 !== len2){
				$('#selAll')[0].checked = false;
			}else{
				$('#selAll')[0].checked = true;
			}
		}).on('click','.batchli_center',function(){
			var rname = '', words = '';
			$('.moved_up').show();
			$('.add_text').remove();
			$('#batchContent').focus();
			rname = $(this).prev().find('[name="leavewordName"]').val();
			words = $(this).find('[name="content"]').val();
			$('.end_hfk').before('<p class="add_text"><span class="add_sp">'+ rname +':</span>'+ words +'</p>');
			$(this).prev().find('.duihao1')[0].checked = true;
		});
	},
	getLeaveWordList:function(pageNum,state){
		var that = this, pid = $("#projectId").val(),
		username = that.getcookie('sjj_username');
		$.ajax({
			url:dataUrl.newLeaveWordList,
			data:{
				projectId:pid,
				pageNum:pageNum,
				index:state,        //index为0查询全部
				t:new Date().getTime()
			},
			success: function(result){
				if(result.flag==1){
					var datas = result.obj,
						data = datas.data,
						total = datas.total,
						html = [],
						isSelef = false;
					if(total == 0){
						$(".jiazai").html("暂无数据！");
						$('.batch_btn').hide();
						$('.batch_fr').hide();
						return;
					}else{
						$('.batch_btn').show();
						$('.batch_fr').show();
					}
					for(var i=0;i<data.length;i++){
						isSelf = (username == data[i].leavewordName);
						var j = i+1;
						html.push('<li class="batch_list">');
						if(!isSelf){
							html.push('<div class="batchli_fl">');
							html.push('<input type="checkbox" id="chk'+ (that.size+j) +'" class="duihao1" />');
							html.push('<input type="hidden" name="leavewordUserId" value="'+ data[i].leavewordUserId +'"/>');
							html.push('<input type="hidden" name="leavewordName" value="'+ data[i].leavewordName +'"/>');
							html.push('<input type="hidden" name="id" value="'+ data[i].id +'"/>');
							if(data[i].projectDonateId!=null && data[i].projectFeedbackId==null){
								html.push('<input type="hidden" name="type" value="1"/>');
								html.push('<input type="hidden" name="recordId" value="'+ data[i].projectDonateId +'"/>');
							}
							if(data[i].projectDonateId==null && data[i].projectFeedbackId!=null){
								html.push('<input type="hidden" name="type" value="0"/>');
								html.push('<input type="hidden" name="recordId" value="'+ data[i].projectFeedbackId +'"/>')
							}
							html.push('</div>');
							html.push('<a href="javascript:;" class="batchli_center">');
							html.push('<input type="hidden" name="content" value="'+ data[i].content +'"/>');
						}else{
							html.push('<a href="javascript:;" class="batchli_center self">');
						}
						//放入留言数据
						if(data[i].replyUserId=='' || data[i].replyUserId==null){
					        html.push(data[i].leavewordName +":"+ data[i].content);
						}else{
					        html.push(data[i].leavewordName +"回复"+data[i].replyName +":"+ data[i].content);
						}
						html.push('</a>');
						html.push('<label class="biaoji" for="chk'+ (that.size+j) +'">');
						//留言内容结束
						html.push('<p class="batchli_fr">'+ (that.size+j) +'/'+ total +'</p>');
						html.push('</label>');
						html.push('</li>');
					}
					that.size += data.length;
					//进行数据的追加
					$("#batchList").append(html.join(''));
					if(total > that.size){
				    	$(".jiazai").html("点击加载更多").removeClass('no');
					}else{
						$(".jiazai").html("没有更多数据了").addClass('no');
					}
				}else if(result.flag==-1){
					$("#batchList").html("");
					$('.batch_btn').hide();
					$('.batch_fr').hide();
					$(".jiazai").html("暂无数据！").addClass('no');
				}
			},
			error : function(result) { 
				that.showTips('网络异常！');
			}
		});
	},
	confirmCommentBatch:function(){
		var that = this,
			content = $('#batchContent').val(),
			projectId = $("#projectId").val(),
			types = [],
			recordIds = [],
			leaveWordIds = [],
			replyNames = [],
			ids = [];
		$(".duihao1").each(function(){
			if(this.checked){
				var leaveWordId = $(this).nextAll("[name='leavewordUserId']").val(),
					type = $(this).nextAll("[name='type']").val(),
					recordId = $(this).nextAll("[name='recordId']").val(),
					replyName = $(this).nextAll("[name='leavewordName']").val(),
					id = $(this).nextAll("[name='id']").val();
				types.push(type);
				recordIds.push(recordId);
				leaveWordIds.push(leaveWordId);
				replyNames.push(replyName);
				ids.push(id);
			}
		});
		//发送异步请求提交回复 
		$.ajax({
			url:dataUrl.addBatchNewLeaveWord,
			data:{
				projectId:projectId,
				typess:types.join(','),
				recordIdss:recordIds.join(','),
				leaveWordIdss:leaveWordIds.join(','),  
				content:content,
				replyNamess:replyNames.join(','),
				idss:ids.join(','),
				t:new Date().getTime()
			},
			success: function(result){ 
				if(result.flag==1){
					that.showTips(result.errorMsg);
					$(".moved_up").hide();
					//回复成功之后刷新页面数据
					that.page = 1;
					$('#batchList').html('');
					that.getLeaveWordList(1,that.state);
				}else if(result.flag==0){
					that.showTips(result.errorMsg);
				}else{
					that.showTips('您还未登录！');
					setTimeout(function(){
						location.href = '/ucenter/user/Login_H5.do';
					},2000);
				}
			},
			error : function(result) { 
				that.showTips('网络异常！');
			}
		});
	},
	//获取cookie
	getcookie:function(name){
		var strcookie = document.cookie;
		var arrcookie = strcookie.split("; ");
		for(var i = 0; i < arrcookie.length; i++) {
			var arr = arrcookie[i].split("=");
			if(arr[0] == name) return decodeURIComponent(arr[1]); //增加对特殊字符的解析  
		}
		return "";
	},
	showTips:function(txt){
    	var $msg = $('#msg');
    	if(!$msg.is(':hidden')){
    		$msg.hide();
    		this.timer = null;
    	}
    	$msg.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$msg.fadeOut();
    	},2000);
    }
};

$(function(){
	reg.init();
});
