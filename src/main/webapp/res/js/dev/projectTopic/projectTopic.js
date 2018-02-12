/**
 * Created by Administrator on 2016/8/10.
 */
var base=window.baseurl;
var dataUrl={
	LoadTags:base+'projectTopic/loadTags.do',//加载项目标签
	loadProjects:base+'projectTopic/loadProjects.do',//按标签加载项目（最大100个）
	loadProjectTitles:base+'projectTopic/loadProjectTitles.do',//根据项目ids加载项目
	addProjectTopic:base+'projectTopic/addProjectTopic.do',//添加项目专题
	loadProjectTopicList:base+'projectTopic/loadProjectTopicList.do'//加载专题列表
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
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	window.mCENnavEtSite="m-topic"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	
	var reg={
		type:0,
		photoData:[],
		init:function(){
			var that=this;
			that.ajaxForm($('#form'));
			that.loadTags();
			that.loadProjectTopicList();
			/*打开弹框*/
			$('.center_tr2').click(function(){
				that.loadProjects('');
				$('.popUp').show();
			});
			/*关闭弹框*/
			$('.closeDialog').click(function(){
				$('.popUp').hide();
			});
			/*设置专题名称，简介*/
			$('.center_tr').click(function(){
				var topicName=$('#topicName').val();
				var topicInfo=$('#topicInfo').val();
				$('#topicName_text').html(topicName);
				$("#topicInfo_text").html(topicInfo);
			});
			/*带回项目id,查询项目标题及条数*/
			$('.foot_btn').click(function(){
				var projectIds=$('.footTar').val();
				that.loadProjectTitles(projectIds);
				$('.popUp').hide();
			});
			/*提交项目专题*/
			$('.center_btn').click(function(){
				that.addProjectTopic();
			});
			/*上传专题图片*/
			$('#imgFile').change(function(){
				var p=that.photoPic(this);
				if(p){
					/*var imgBox=$("#imgList1"),addObj=imgBox.children(".add"),src=base+'/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="'+src+'" width="80px" height="75px"><i>×</i></div>');*/
					$('#form').submit();
				}
				return false;
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
					html.push('<img class="banner_img" src="'+json.imageUrl+'" id="'+json.imageId+'" alt="">');
					$('#imgBanner').html(html.join(''));
				}
			},
			error: function(data) {
			}
		}); 
	},
	loadTags:function(){
		$.ajax({
			url:dataUrl.LoadTags,
			data:{},
			success: function(result){
				var data=result.obj.configValue;
				if(result.flag == 1){//成功
					var html=[],datas=data.split(',');
					html.push('<li><a href="javascript:void();" class="moren"  onclick="loadProject(\'\')">全部</a></li>');
					for(var i=0;i<datas.length;i++){
						html.push('<li><a href="javascript:void();" onclick="loadProject(\''+datas[i]+'\')" id="'+datas[i]+'">'+datas[i]+'</a></li>');
					}
					$('.goodRecord1>dl>dt').html(html.join(''));
				}else{//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
			}
		});
	},
	loadProjects:function(tag){
		$.ajax({
			url:dataUrl.loadProjects,
			data:{tag:tag},
			success: function(result){
				var data=result.obj;
				if(result.flag == 1){//成功
					var html=[];
					if(data!=null){
						for(var i=0;i<data.length;i++){
							html.push('<li><input type="checkbox" class="dtInput" id="'+data[i].id+'"><p class="dtperson">'+data[i].title+'</p></li>');
						}
					}
					$('.goodRecord1>dl>dd').html(html.join(''));
					$('.dtInput').click(function(){
						var projectId=$(this).attr('id');
						var state = $(this).attr('checked');
						var projectIds=$('.footTar').val();
						if(state=='checked'){
							if(projectIds.indexOf(projectId)>=0){//包含
							}
							else{//不包含
								if(projectIds==''){
									projectIds=projectId+',';
								}
								else{
									projectIds=projectIds+projectId+','
								}
								$('.footTar').val(projectIds);
							}
						}
						else{
							if(projectIds.indexOf(projectId)>=0){//包含
								projectIds=projectIds.replace(projectId+',','');
							}
							else{//不包含
							}
							$('.footTar').val(projectIds);
						}
					});
				}else{//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
			}
		});
	},
	loadProjectTitles:function(ids){
		$.ajax({
			url:dataUrl.loadProjectTitles,
			data:{ids:ids},
			success: function(result){
				var data=result.obj;
				if(result.flag == 1){//成功
					var html=[];
					if(data!=null&&data.resultData!=null){
						html.push('<p class="html_p1">已添加的项目（'+data.total+'）</p>');
						for(var i=0;i<data.resultData.length;i++){
							html.push('<p class="html_p">'+data.resultData[i].title+'</p>');
						}
					}
					$('.center_html').html(html.join(''));
				}else{//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
			}
		});
	},
	addProjectTopic:function(){
		var topicPicId=$('.banner_img').attr('id'),topicName=$('#topicName').val(),
		topicInfo=$('#topicInfo').val(),projectIds=$('.footTar').val();
		if(topicName==''){
			return d.alert({content:'请输入专题名称！',type:'error'});
		}
		if(topicInfo==''){
			return d.alert({content:'请输入专题简介！',type:'error'});
		}
		if(projectIds==''){
			return d.alert({content:'请选择添加项目！',type:'error'});
		}
		if(topicPicId==''||typeof(topicPicId)=='undefined'){
			return d.alert({content:'请上传专题图片！',type:'error'});
		}
		$.ajax({
			url:dataUrl.addProjectTopic,
			data:{projectIds:projectIds,topicName:topicName,topicPicId:topicPicId,topicInfo:topicInfo},
			success: function(result){
				if(result.flag == 1){//成功
					window.location='http://www.17xs.org/projectTopic/gotoProjectTopicSuccess.do?id='+result.errorMsg;
				}else if(result.flag == 0){//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
				else{//未登录
					window.location='http://www.17xs.org/user/sso/login.do?entrance=http://www.17xs.org/projectTopic/gotoProjectTopicAdd.do';
				}
			}
		});
	},
	loadProjectTopicList:function(){
		$.ajax({
			url:dataUrl.loadProjectTopicList,
			data:{},
			success: function(result){
				var data=result.obj;
				if(result.flag == 1){//成功
					var html=[];
					html.push('<dt><li class="zhuanti">专 题</li><li>项目数</li><li>总目标</li><li>创建时间</li><li>查看</li></dt>');
					if(data!=null&&data.resultData!=null){
						for(var i=0;i<data.resultData.length;i++){
							html.push('<dd>');
							html.push('<li class="zhuanti">'+data.resultData[i].topicName+'</li>');
							html.push('<li>'+data.resultData[i].projectCount+'个</li>');
							html.push('<li >'+data.resultData[i].sumProjectMoney+'元</li>');
							html.push('<li><p class="dm">'+data.resultData[i].timeString+'</p></li>');
							html.push('<li>');
							html.push('<a href="javascript:void();"><p class="dm" onclick="alertQrcode('+data.resultData[i].id+')">查看二维码</p></a>');
							html.push('<a href="javascript:void();"><p class="dm" onclick="deleteTopic('+data.resultData[i].id+')">删除</p></a>');
							html.push('</li>');
							html.push('</dd>');
						}
						$('.goodRecord>dl').html(html.join(''));
					}
					else{
						//无数据
						$('.goodRecord>dl').html(html.join(''));
						$('.seminarNone').show();
					}
					
				}else{//失败
					return d.alert({content:result.errorMsg,type:'error'});
				}
			}
		});
	}
	}
	reg.init()
})