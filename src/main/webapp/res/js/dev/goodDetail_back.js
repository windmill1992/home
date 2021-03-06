var base=window.baseurl;
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	helpphotoDel:base+'file/image/delete.do',
	hostList:base+'/project/pfeedback.do',
	toCommon:base+'/project/addfeedback.do',
	FeedbackList:base+'/project/leaveWordList.do',
	addleaveWord:base+'/project/addleaveWord.do',
	donationlist:base+'project/gardendonationlist.do'
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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.sitePage="p-doGood";
	en.inMustFun=function(){
		if(en.isIn){
			if($('#blockAdd').length>0){
				$('#blockAdd').show();
			}
		}
	}
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#grecordList")});
	p.changeCallback=function(){
		detail.delRecordList(p.curPage,10);
	};
	var detail={
		hosData:'',
		totalpage:1,
		currpage:1,
		photoData:[],
		init:function(){
			var that=this;
			$('.g-scroll').myScroll({
				speed: 40, //数值越大，速度越慢
				rowHeight: 32 //li的高度
			});
			
			$('.forward_btn').click(function(){
				var data=$(this).attr('data');
				$('#pprim').attr('data',data);
				that.share();
			})
			$('#share').click(function(){
				var data=$(this).attr('data'),pTle=$(this).attr('ptitle');
				if(data){
					$('#pprim').attr('data',data);
					that.share();
				}else{
					$('#pprim').attr('data',pTle);
					that.share1(pTle);
				}
			})
			$("body").on("click",".qqim",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qqim',1): that.bshare(event,'qqim',0);
				 //that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qzone',1): that.bshare(event,'qzone',0);
				 //that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'sinaminiblog',1): that.bshare(event,'sinaminiblog',0);
				// that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'weixin',1): that.bshare(event,'weixin',0);
				 //that.bshare(event,'weixin',0);
			})/*.on("mouseover",".delLogo",function(){ 
				 $(this).children('em').show();
			}).on("mouseout",".delLogo",function(){ 
				 $(this).children('em').hide();
			})*/.on("click",".list_more .showBtn",function(){ 
				var listMore=$(this).parents('.list_more'),comList=listMore.parent('.comList'),len=$(this).parent().children('.totlemore').html(),isShow=comList.attr('isShow');
				comList.children('.comListOne').children().show();
				if(len>5){
					comList.children('.lstPages').show();comList.attr('isPage',true);
				}
				listMore.children('.l').hide();
				comList.attr('isShow',true);
			}).on("click",".bdback-group .close",function(){ 
				var p=$(this).parent('p');
				p.next('.comList').hide();
				$(this).removeClass('close').addClass('open').html('查看祝福');
			}).on("click",".bdback-group .open",function(){ 
				var p0=$(this).parent('p'),id=$(this).parents('p').attr('id').split('|'),comList=$(this).parents('p').next('.comList'),isShowLen=parseInt(comList.attr('isShowLen')),
				li=p0.parent('.bdback-group').siblings(".bdback-group"),liComlist=li.children('.comList'),limoreBtn=li.children('p').children('.more');
				if(isShowLen<1){
					p1.init({afterObj:$("#comListOne"+id),listId:id[0]});
					p1.changeCallback=function(){
						detail.FeedbackList(id[0],id[1],p1.curPage);
					};
					detail.FeedbackList(id[0],id[1],1,true);
					comList.attr('isShowLen',isShowLen+1)
				}
				p0.next('.comList').show();
				$(this).removeClass('open').addClass('close').html('收起祝福');	
				
				limoreBtn.removeClass('close').addClass('open').html('查看祝福');	
				liComlist.hide();
				
			}).on("click",".list_more .sendBtn",function(){ 
				var comList=$(this).parents('.comList');
				comList.find('.answer_box').is(":hidden")?comList.find('.answer_box').show():comList.find('.answer_box').hide();
			}).on("click",".answer_box .answerBtn",function(){ 
				var id=$(this).attr('id').split('|'),con=$('#area'+id[0]).val();
				if(!con){
					d.alert({content:'请输入祝福的内容！',type:'error'});
					return false;
				}
				that.addleaveWord(id[0],id[1],con);
			})
		    that.hosList(1);
			$('#tab-hd span').click(function(){
				var i=$('#tab-hd span').index(this);
				$('#tab-hd span').removeClass('on').eq(i).addClass('on');
				$('#tab-bd .bdcon').hide().eq(i).show();
				if(i==2){
					that.hosList(1);
				}else if(i==3){
					that.delRecordList(1,10);
				}
			})
			
			$('#msgpic').click(function(){
				$('#msg-pic').show();
			})
			
			$("body").on("mouseover","#msg-pic li",function(){ 
				$(this).children('i').show();
			}).on("mouseout","#msg-pic li",function(){ 
				$(this).children('i').hide();
			}).on("click","#msg-pic li i",function(){ 
				var delV=$(this).parent().attr('data');
				var isDel=that.delPic(delV);
				if(isDel){	
					$(this).parent().remove();
					$('.imgList .last').show();
				}
			})
			
			$('#gDtile-btn').click(function(){
				that.buyproject();
			})
			
			that.ajaxForm($('#form1'));
			$('#picaddImg').change(function(){
				var p=that.photoPic(this);
				if(p){
					$('#form1').submit();
				}
				return false;
			})
			$('#pagenum').blur(function(){
				var tmptxt=$(this).val();     
       			$(this).val(tmptxt.replace(/\D|^0/g,'')); 
				tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				if(tmptxt==''||isNaN(tmptxt)){
					tmptxt=1;
				}
				else{
					if(tmptxt>that.totalpage){
						tmptxt=that.totalpage;
					}
				}
				that.hosList(tmptxt);
				$(this).val(tmptxt);
			})
			$('#pageprev').click(function(){
				var page;
				if(that.currpage==1){
					page=1;
					return false;
				}else{
					page=that.currpage-1;
					that.hosList(page);
				}
				
			})
			$('#pagenext').click(function(){
				var page;
				if(that.currpage==that.totalpage){
					page=that.totalpage;
					return false;
				}else{
					page=that.currpage+1;
					that.hosList(page);
				}
			})		
			
			$('#msgsubmit').click(function(){
				that.toCommon();
			})		
		},
		addleaveWord:function(id,pid,con){
			var that=this;
			$.ajax({
				url:dataUrl.addleaveWord,//http://127.0.0.1:8080/data/goodlist.json
				data:{pid:pid,content:con,id:id,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
							that.FeedbackList(id,pid,1,true);
							$('#area'+id).val('');
					}else if(result.flag==-1){
						en.show(that.addleaveWord);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			})
		},
		delRecordList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false;
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.donationlist,//http://127.0.0.1:8080/data/goodlist.json
				data:{id:projectId,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					//result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var html=[];
						html.push('<ul>');
						for(var i=0;i<len;i++){
						html.push('<li '+((i+1)%2==0?'class="bgGreen"':'')+'>');
						html.push('<div class="lst-col1"><span>'+datas[i].name+'</span><span class="delLogo" style="display:none"><em><i></i>浙江宾果信息科技有限公司企业员工</em></span></div>');
						html.push('<div class="lst-col2">￥'+$.formatNum(datas[i].dMoney)+'</div>');
						html.push('<div class="lst-col3">'+(new Date(datas[i].dTime)).pattern("yyyy/MM/dd HH:mm")+'</div>');
						html.push('</li>');
						}
						html.push('</ul>');
						$('#grecordList').html(html.join(''));		
						data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
						data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
					}
				}
			})
			
		},
		share1:function(ptle){
			var html=[];
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+ptle+'</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" >QQ好友</a>');
			html.push('<a class="qzone">QQ空间</a>');
			html.push('<a class="sinaminiblog" >新浪微博</a>');
			html.push('<a class="weixin">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
		share:function(){
			var html=[],v=$('#pprim').attr('data').split('|');
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+v[2]+'，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐'+v[0]+'元！(注册邀请码：'+v[1]+')</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" tel="1" >QQ好友</a>');
			html.push('<a class="qzone" tel="1">QQ空间</a>');
			html.push('<a class="sinaminiblog" tel="1" >新浪微博</a>');
			html.push('<a class="weixin" tel="1">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
		bshare:function(event,o,type){
			if(type==1){
				var v=$('#pprim').attr('data').split('|'),projectId=$('#projectId').val();
				 bShare.addEntry({
				  title: "【善园基金会】",
				  url: base+"/user/sso.do?projectId="+projectId+"&companyCode="+v[1],
				  summary:v[2]+"，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐"+v[0]+"元！(注册邀请码："+v[1]+")" ,
				  pic: ""
				});
			}else{
				var tle=$('#pprim').attr('data'),projectId=$('#projectId').val();
				 bShare.addEntry({
				  title: "【善园基金会】",
				  url: base+"/user/sso.do?projectId="+projectId,
				  summary:tle ,
				  pic: ""
				});
			}
			bShare.share(event,o,0);
		},
		buyproject:function(){
			var that=this;
			//已登录
			if(en.isIn){ 
				window.location.href=base+"alipay/commondpay.do?projectId="
				+$('#projectId').val()+"&amount="+$('#amount').val()+"&pName="+$('#pName').val();
			}
			//未登录
			else{
				  // en.show(that.buyproject);
				window.location.href=base+"visitorAlipay/visitorpay.do?projectId="
				+$('#projectId').val()+"&amount="+$('#amount').val()+"&pName="+$('#pName').val();
			} 
		},
		delPic:function(delV){
			var that=this;
			var delI=jQuery.inArray(delV,that.photoData);
			
			$.ajax({
				url:dataUrl.helpphotoDel,
				data:{images:delI},
				success: function(result){ 
					if(result.flag==1){
						that.photoData.splice(delI,1);
					}else if(result.flag==-1){
						en.show(that.delPic);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			}); 
			
			return true;
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
							d.alert({content:"上传的图片大小不能超过2M！",type:'error'});
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
						d.alert({content:json.error,type:'error'});
						return false;
					}else{
						var html='<li  data="'+json.imageId+'"><i>×</i><img src="'+json.imageUrl+'" alt=""></li>';
						that.photoData.push(json.imageId);
						$('#picaddImg').parent().before(html);
						if(that.photoData.length==10){
							$('.imgList .last').hide();
						}
						//json.imageUrl;
						
					}
				},
				error: function(data) {
					d.alert({content:data.responseText,type:'error'});
					//d.alert({content:"上传失败，请重新上传！",type:'error'});
				}
			}); 
		},
		FeedbackList:function(id,pid,page,isFirst){
			var that=this,flag=false,isShow=$('#comList'+id).attr('isShow'),isShowLen=$('#comList'+id).attr('isShowLen'),isPage=$('#comList'+id).attr('isPage');
			page==1&&isFirst?flag=true:'';
			$.ajax({
				url:dataUrl.FeedbackList,
				data:{id:id,page:page,t:new Date()},
				success: function(result){ 
					if(result.flag==1){
						var result=result.obj,total=result.total,nums=result.nums,data=result.data,page=result.page,pageNum=result.pageNum,html=[],cmp=[],cls,clsck;
						isShow=='true'?clsck="none":data.length>2?clsck="block":clsck="none";
						total>1&&!isFirst?$('#comList'+id).attr('isPage',true):'';
						isPage=$('#comList'+id).attr('isPage');
						for(var i=0;i<data.length;i++){
							isShow=='true'?cls="block":i>1&&page==1?cls="none":cls="block";
							html.push('<div class="list_ul" style="display:'+cls+'">');
							//html.push('<div class="list-fase">');
							//html.push('<img src="http://www.17xs.org/res/images/user/user-icon.png">');
							//html.push('</div>');
							html.push('<div class="list_con">');
							html.push('<div class="WB_fun">');
							html.push(''+data[i].uName+' <span class="time">'+(new Date(data[i].cDate)).pattern("yyyy-MM-dd HH:mm")+'</span>');
							html.push(' </div>');
							html.push(' <div class="WB_text"> '+data[i].content+'</div>');        	
							html.push('</div></div>');            	            
						}
						cmp.push('<div class="list_more">');
						cmp.push('<span class="l" style="display:'+clsck+'">共有<em class="totlemore">'+nums+'</em>条留言，<a class="color4 showBtn">点击查看</a></span>');
						cmp.push('<span class="r"><a class="sendBtn color4">送祝福</a></span>');
						cmp.push('</div>');
						cmp.push('<div class="answer_box"  style="display:none">');
						cmp.push('<textarea class="area" id="area'+id+'"></textarea>');
						cmp.push('<p><a class="answerBtn" id="'+id+'|'+pid+'">马上祝福</a></p>');
						cmp.push('</div>');      
						$('#comListOne'+id).html(html.join(''));
						$('#comList'+id).find('.list_more').next('.answer_box').remove();
						$('#comList'+id).find('.list_more').remove();
						$('#lstPages'+id).after(cmp.join(''))
						
						flag?p1.pageInit({pageLen:total,isShow:false,listId:id}):'';
						if(total>1&&flag){ 
							isPage=="true"?p1.pageInit({pageLen:total,isShow:true,listId:id}):p1.pageInit({pageLen:total,isShow:false,listId:id}); 
						}
						
					}else if(result.flag==-1){
						en.show(that.FeedbackList);
					}else{
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}
				}
			});
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
					url:dataUrl.hostList, 
					data:{pid:id,page:page,pageNum:10,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==0){
							d.alert({content:result.errorMsg,type:'error'});
							
						}else if(result.flag==-1){
							en.show(that.hosList);
						}else if(result.flag==2){
							$('#pagetotle').html("共0页");
							$('.bdback-page .page').hide();
							$('#hoslist').html('');
							return false;						
						}else{
							var datalist=result.obj.data,html=[];
							that.totalpage=page=result.obj.total,that.currpage=result.obj.page,that.hosData=datalist;
							$('#pagenum').val(that.currpage);
							$('#feedNum').html(result.obj.nums);
							for(var i=0;i<datalist.length;i++){
								html.push('<div class="bdback-group">');
								html.push('<div class="picimg"><img src="'+datalist[i].userImageUrl+'"/></div>');
								html.push('<div class="name">'+datalist[i].uName+'<span>'+that.formatDate(new Date(datalist[i].cTime))+'</span></div>');						
								var imgs = datalist[i].imgs;
								var temp="";
								if(imgs&&imgs.length>0){
									temp+="<br/>";
									for(var j=0;j<imgs.length;j++){
										temp+=("<img src='"+imgs[j]+"'/><br/>");
									}	
								}
								//html.push(' <p>'+datalist[i].content+temp+'</p>');
								html.push(' <p id="'+datalist[i].id+'|'+datalist[i].pid+'">'+datalist[i].content+temp+'<!--<a class="more close">收起祝福</a>--> <a class="more open">查看祝福</a></p>');
								html.push('<div class="comList" id="comList'+datalist[i].id+'" isshowLen="0" isshow=false ispage=false>');
								html.push('<div class="comListOne" id="comListOne'+datalist[i].id+'">');
								html.push('</div></div>');
								html.push('</div>');
							}
							if(datalist.length<1&&that.currpage==1){
								$('.bdback-page .page').hide();
							}else{
							$('#hoslist').html(html.join(''));
							if(that.currpage==1){
								$('#pageprev').attr("disabled","disabled").addClass('pagedefalut');
							}else{
								$('#pageprev').removeAttr("disabled").removeClass('pagedefalut');
							}
							if(that.currpage==that.totalpage){
								$('#pagenext').attr("disabled","disabled").addClass('pagedefalut');
								
							}else{
								$('#pagenext').removeAttr("disabled").removeClass('pagedefalut');
							}
							}
							$('#pagetotle').html("共"+that.totalpage+"页");
						}
						
					}
				});	
		},
		formatDate:function(now){  
			var mouth,dtae,hour,minute,second;
              var   year=now.getFullYear();     
              now.getMonth()+1<10? month='0'+(now.getMonth()+1): month=now.getMonth()+1;     
              now.getDate()<10? date='0'+now.getDate(): date=now.getDate();     
              now.getHours()<10? hour='0'+now.getHours():  hour=now.getHours();     
              now.getMinutes()<10? minute='0'+now.getMinutes(): minute=now.getMinutes();     
              now.getSeconds()<10? second='0'+now.getSeconds(): second=now.getSeconds();     
              return   year+"/"+month+"/"+date+" &nbsp;  "+hour+":"+minute;     
        }, 
		toCommon:function(){
			var that=this,content=$('#msg-text').val(),imgIds=that.photoData.join(','),pid=$('#projectId').val();
			if(!content&&!imgIds){
				return d.alert({content:"您还没有提交任何留言内容！",type:'error'});
			}
			$.ajax({
				url:dataUrl.helpsubmit,
				data:{pid:pid,content:content,imgIds:imgIds,t:new Date()},
				success: function(result){ 
					if(result.flag==1){
					    that.hosList(1);//刷新评论列表	
					    $('#msg-text').val('');
						that.photoData=[];
						$('#msg-pic li').not('.last').remove();
						d.alert({content:"评论成功",type:'warn'});
						that.hosList(1);
						
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