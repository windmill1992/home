var base=window.baseurl;
//拍照、本地选图
var images = {
    localId: [],
    serverId: []
};
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	projectUrl:base+'project/view_h5.do',
};
require.config({
	baseUrl:window.baseurl+"res/js/",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	
	h.init();
	d.init();
	en.init(); 
	uc.init();

	var detail={
		hosData:'',
		totalpage:1,
		currpage:1,
		photoData:[],
		init:function(){
			var that=this;
			$('#msg-submit').click(function(){
				syncDownload(images.serverId);//去微信服务器上下载图片
				//that.toCommon();
			});
			
			$('#msg-text').click(function(){
				var msg = $('#msg-text').html();
				if(msg == '请输入求助人近期状况'){
					$('#msg-text').html('');
				}
			});	
			
			$('.pic').click(function(){
				var msg = $('#msg-text').html();
				if(msg == '请输入求助人近期状况'){
					$('#msg-text').html('');
				}
			});	
			/*//异步上传图片到微信服务器
			  var syncUpload = function(localId){
			      wx.uploadImage({
			            localId: localId,
			            isShowProgressTips: 1,
			            success: function (res) {
			                 images.serverId.push(res.serverId);// 返回图片的服务器端ID
			                 //异步上传图片id到服务器
			                 var  serverids = images.serverId.join(',');
			                 $('#mediaId').html(serverids);
			            },
			            error: function(){
			           		alert("上传图片出现问题，请联系客服");
			            }
			      });
			  };*/
			$('#chooseImage').click(function(){
				var html=[];
			    wx.chooseImage({
			    count: 1, // 默认9
			    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
			    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
			    success: function (res) {
			    		//销毁数据
			            //images.localId = images.serverId  = [];
			          	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
			          	for(var i = 0;i<images.localId.length;i++){
			          		html.push('<span class="pic"><div class="del_dt">');
							html.push('<img src="'+images.localId[i]+'"/>');
							html.push('</div></span>');
							that.syncUpload(images.localId[i]);
			          	}
			          	$('#upload_list').append(html.join(''));
			          	//===图片查看 ===//
						$('.pic img').click(function(){
					        var imgArray = [];
					        var curImageSrc = $(this).attr('src');
					        var oParent = $(this).parent();
					        if (curImageSrc && !oParent.attr('href')) {
					            $('.pic img').each(function(index, el) {
					                var itemSrc = $(this).attr('src');
					                imgArray.push(itemSrc);
					            });
					             wx.previewImage({
					                current: curImageSrc,
					                urls: imgArray
					            });
					        }
					    });
					    
			    }
			    });
			});
			
			  //异步下载图片到本地
			  var syncDownload = function(serverids){
			   serverids = $('#mediaId').html();
			     $.ajax({
					url:'http://www.17xs.org/file/wximage.do',
					data:{mId:serverids,t:new Date()},
					success: function(res){ 
						if(res.flag==1){
							//alert(res.obj);
							that.toCommon(1,res.obj);//图片id
						}else{
							that.toCommon(2,1);//不上传图片
						}
					}
				}); 
			  };
		},
		//异步上传图片到微信服务器
		syncUpload:function(localId){
		      wx.uploadImage({
		            localId: localId,
		            isShowProgressTips: 1,
		            success: function (res) {
		                 images.serverId.push(res.serverId);// 返回图片的服务器端ID
		                 //异步上传图片id到服务器
		                 var  serverids = images.serverId.join(',');
		                 $('#mediaId').html(serverids);
		            },
		            error: function(){
		           		alert("上传图片出现问题，请联系客服");
		            }
		      });
		  },
		toCommon:function(type,obj){
			var that=this,content=$('#msg-text').val(),imgIds=that.photoData.join(','),pid=$('#projectId').val();
			if((!content&&!imgIds)||content=='请输入求助人近期状况'){
				return d.alert({content:"您还没有提交任何留言内容！",type:'error'});
			}
			//图片id，这个方法只能在微信中使用，其他的请重新编写
			if(type == 1){
				imgIds = obj;
			}
			
			if(content.length>500){
				return d.alert({content:"最长不超500字！",type:'error'});
			}
			
			$.ajax({
				url:dataUrl.helpsubmit,
				data:{pid:pid,content:content,imgIds:imgIds,t:new Date()},
				success: function(result){ 
					if(result.flag==1){
					  
						//d.alert({content:"评论成功",type:'warn'});
						window.location.href=dataUrl.projectUrl+'?projectId='+pid;
						
					}else if(result.flag==-1){
						en.show(that.toCommon);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
		}
		
	}
	detail.init();	
});