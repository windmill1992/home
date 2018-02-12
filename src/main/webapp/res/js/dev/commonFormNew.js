var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
	dataList:base+'resposibilityReport/entryFormNewList.do',	//报名数据
	add:base+'resposibilityReport/saveFormNew.do',				//保存报名
	delImg : base + 'newReleaseProject/delImg.do',				//删除图片
};
require.config({
	baseUrl:base+"/res",
	paths:{
		"jquery" : ["js/jquery-1.8.2.min"], 
		"extend" : "js/dev/common/extend",
		"dialog" : "js/dev/common/dialog",
		"util"   : "js/dev/common/util",
		"head"   : "js/dev/common/headNew",
		"entry"  : "js/dev/common/entryNew",
		"userCenter"  : "js/dev/common/userCenter" ,
		"ajaxform"  : "js/util/ajaxform" ,
		"pages"   :"js/dev/common/pages"
		
	},
	urlArgs:"v=20151010"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#activeList")});
	addListNew={
		ids:[],
		ua:'',
		init:function(){
			var that=this;
			that.dataList();
			
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){
				that.ua = 'wx';
				that.bindEventWX();
			}else{
				that.ua = 'h5';
				$('body').append('<div id="bigImg"><div class="hd"><ul></ul></div><div class="bd"></div></div>');
				that.bindEventH5();
			}
			
			$("body").on("click","#addr",function(){
				if(that.IsPC()){
					$("#city").show();
				}
			}).on("click","input:not(.file-input)",function(){
				$(this).parent().siblings(".title").removeClass("bg");
				$(this).parent().prev().addClass("bg");
				if($(this).hasClass("other")){
					$(this).prev().prev().prev().attr("checked","true");
				}
				e=arguments.callee.caller.arguments[0] || window.event; 
				e.stopPropagation();//阻止事件冒泡，防止在点击输入框或者选择框时
				that.getProgress();
				that.userBG();//兼容火狐,ie小圆点
			}).on("click",".content span",function(){
				$(this).parent().siblings(".title").removeClass("bg");
				$(this).parent().prev().addClass("bg");
				$(this).prev().prev(":checkbox").attr("checked",!$(this).prev().prev().attr("checked"));
				$(this).prev().prev(":radio").attr("checked","true");
				e=arguments.callee.caller.arguments[0] || window.event; 
				e.stopPropagation();
				that.getProgress();
				that.userBG();//兼容火狐,ie小圆点
			}).on("click","textarea",function(){
				$(this).parent().siblings(".title").removeClass("bg");
				$(this).parent().prev().addClass("bg");
				e=arguments.callee.caller.arguments[0] || window.event; 
				e.stopPropagation();
			}).on("click",".content",function(){
				if($(".content").hasClass("single")){
					$(this).siblings(".title").removeClass("bg");
					$(this).prev().addClass("bg");
				}
			}).on("click",".content .item .del",function(e){	
				e.stopPropagation();
				var _this = $(this);
				var id = Number(_this.closest('.item').attr('id'));
				$.ajax({
					type:"POST",
					url:dataUrl.delImg,
					data:{imgId:id},
					success:function(res){
						if(res.code == 1){
							that.showTips('删除成功');
							var $add = _this.closest('.addList');
							var len = $add.find('.item').not('.add').length;
							var img1 = (len<=3 && $add.attr('id')=='imgList');
							var delI=jQuery.inArray(''+id,that.ids);
							that.ids.splice(delI,1);
							if(img1){
								$add.find('.add').show();
							}else{}
							_this.closest(".item").remove();
						}else{
							alert(res.msg);return;
						}
					}
				});
			});
			
			$(document).click(function(){
				that.getProgress();
				that.userBG();//兼容火狐,ie小圆点
			}).on("click","#city .ok",function(){
				var prov = $("#city").find("#seachprov option:selected").text();
				var city = $("#city").find("#seachcity option:selected").text();
				var dist = $("#city").find("#seachdistrict option:selected").text();
				if(dist!="" && !/请选择/.test(dist)){
					$("#addr").val(prov+"-"+city+"-"+dist);
				}else{$("#addr").val(prov+"-"+city);}
				$("#city").hide();
			}).on("click","#city .no",function(){
				$("#city").hide();
			}).on("click",".submit",function(){
				if (that.ua == 'wx') {
					$('body').append('<script type="text/javascript" src="/res/js/h5/donateStep/wxLogin.js"></script>');
				}else{}
				var result2=true;
				var that2 = this;
				$(".content").each(function(index,ele){
					if($(this).hasClass("single") && ($(this).find(":text").not(".other").val()=="") ||($(this).find("input[type=number]").val()=="")){
						$("#"+$(this).prev().attr("id")).get(0).scrollIntoView();
						//$(that2).attr("href","#"+$(this).prev().attr("id"));
						$(this).siblings(".title").removeClass("bg");
						$(this).prev().addClass("bg");
						return false;
					} else if(($(this).hasClass("box")||$(this).hasClass("rad")) && $(this).find(":checked").size()==0){
						$("#"+$(this).prev().attr("id")).get(0).scrollIntoView();
						//$(that2).attr("href","#"+$(this).prev().attr("id"));
						$(this).siblings(".title").removeClass("bg");
						$(this).prev().addClass("bg");
						return false;
					} else if($(this).hasClass("area") && $(this).find("textarea").val()==""){
						$("#"+$(this).prev().attr("id")).get(0).scrollIntoView();
						//$(that2).attr("href","#"+$(this).prev().attr("id"));
						$(this).siblings(".title").removeClass("bg");
						$(this).prev().addClass("bg");
						return false;
					}else if($(this).hasClass("singlesel") && ($(this).find(":selected").text()=="请选择" || ($(this).find(":selected").text()==""))){
						$("#"+$(this).prev().attr("id")).get(0).scrollIntoView();
						$(this).siblings(".title").removeClass("bg");
						$(this).prev().addClass("bg");
						return false;
					}else if($(this).hasClass("pic") && $(this).find(".item").not(".add").length==0){
						$("#"+$(this).prev().attr("id")).get(0).scrollIntoView();
						$(this).siblings(".title").removeClass("bg");
						$(this).prev().addClass("bg");
						return false;
					}else{}
				});
				if(that.getProgress()==1){
					//验证手机号
					if($("#tel").length>0){
						var tel = $("#tel").val();
						if(!that.isTel(tel)){
							$(".title").removeClass("bg");
							$("#tel").parent().prev().addClass("bg");
							$("#tel").parent().prev().get(0).scrollIntoView();
							that.showTips('手机号填写有误！');
							result2=false;
						}
					}else{}
					if(result2){
						//验证身份证号
						if($("#IDcard").length>0){
							var idcard = $("#IDcard").val();
							if(that.isCardID(idcard)!==true){
								$(".title").removeClass("bg");
								$("#IDcard").parent().prev().addClass("bg");
								$("#IDcard").parent().prev().get(0).scrollIntoView();
								that.showTips('身份证号填写有误！');
								result2=false;
							}else{}
						}else{}
					}else{}
				}else{}
				if(that.getProgress()==1&&result2){
					var jsonText = that.getAns();
					if(jsonText==false){
						return false;
					}
					var imgIds=that.ids.join(',');
					var flag = true;
	                $.ajax({
	                	type: "POST",
	    				url:dataUrl.add,
	    				data:{imgIds:imgIds,information:jsonText,formId:$('#formId').val(),state:0},
	    				success: function(res){
	    					if(res.code=='-1'){
	    						that.showTips(res.msg);
	    						return false;
	    					}else if(res.code == 1){
	    						 that.showTips('恭喜您报名成功！');
	    			             if(res.result.gotoUrl!=null && res.result.gotoUrl!=''){
	    			            	setTimeout(function(){
	        			            	location.href=res.result.gotoUrl;
	        			            },2000);
	    			             }else{
	    			            	setTimeout(function(){
	        			            	location.href=base;
	        			            },2000);
	    			             }
	    			             return true;
	    					}else if(res.code == 2){
	    						alert('参数错误！');return;
	    					}else {
	    						alert('请勿重复提交！');return;
	    					}
	    				},
	    				error: function(r){
	    					that.showTips('报名失败！');
	    					return false;
	    				}
	    			});
				}else{}
            });
		},
		bindEventWX:function(){
			var that = this;
			var appId=$('#appId').val();
			var timestamp=$('#timestamp').val();
			var nonceStr=$('#nonceStr').val();
			var signature=$('#signature').val();
			wx.config({
				debug : false,
				appId : appId,
				timestamp : timestamp,
				nonceStr : nonceStr,
				signature : signature,
				jsApiList : ['chooseImage','previewImage','uploadImage' ]
			});
			//  拍照、本地选图
			var images = {localId: [],serverId: []};
			var inn = 0;
			$("body").on("click",".add-btn",function(){
				inn = 0;
				var html=[];
				var $addList = $(this).closest('.addList');
				var len = $addList.find('.item').not('.add').length;
			    wx.chooseImage({
				    count: 3-len, // 默认9
				    sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
				    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
				    success: function (res) {
			    		//销毁数据
			            images.localId = images.serverId  = [];
			          	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
			          	if(images.localId.length>=3){
			          		$addList.find(".add").hide();
			          	}
			          	syncUpload(images.localId[0],$addList);
				    }
			    });
			}).on('click','.addList .item:not(.add)',function(){
				var $img = $(this).find('img');
				var currentSrc = $img.attr('src');
				var urlsSrc = [];
				$('.addList .item .preview img').each(function(){
					urlsSrc.push($(this).attr('src'));
				});
				wx.previewImage({
					current:currentSrc,
					urls:urlsSrc
				});
			});
			//异步上传图片到微信服务器
			var syncUpload = function(localId,o){
				if(localId.indexOf('wxlocalresource') != -1){
	          		localId = localId.replace('wxlocalresource','wxLocalResource');
	          	}else{}
			    wx.uploadImage({
		            localId: localId,
		            isShowProgressTips: 1,
		            success: function (res) {
		                 images.serverId.push(res.serverId);// 返回图片的服务器端ID
		                 syncDownload(res.serverId,o);
		            },
		            error: function(){
		           		alert("上传图片出现问题，请联系客服");
		            }
			    });
			};
			  
			//异步下载图片到本地
			var syncDownload = function(serverids,o){
			    $.ajax({
					url:'http://www.17xs.org/file/wximage.do',
					data:{mId:serverids,typeName:'commonForm',t:new Date()},
					async:false,
					success: function(res){ 
						if(res.flag==1){
							var html = [];
							var imgUrls = res.obj,imgIds=res.obj1;
							imgUrls = imgUrls.substr(0,imgUrls.length-1);
							imgIds = imgIds.substr(0,imgIds.length-1);
							html.push('<div class="item" id="'+imgIds+'"><a href="javascript:;" class="preview"><img src="'+imgUrls+'"/></a>');
							html.push('<i class="del"><img src="/res/images/h5/images/close.png"/></i></div>')
		            		o.find('.add').before(html.join(''));
		            		inn++;
		            		if(inn<images.localId.length){
		            			syncUpload(images.localId[inn],o);
		            		}else{}
		            		
							var len = o.find('.item').not('.add').length;
							if(len>=3){
								o.find('.add').hide();
							}
							that.urls+=(imgUrls+"@");
							that.ids.push(imgIds);
							that.showTips('上传图片成功！');
						}else{
							that.showTips('上传图片失败！');
						}
					}
				});
			};
		},
		bindEventH5:function(){
			var that = this;
			$("body").on("change",".file-input",function(){
				var p=that.photoPic(this);
				if(p){
					var imgBox=$(this).closest(".addList"),addObj=imgBox.children(".add"),src='/res/images/common/bar1.gif';
					if(imgBox.find('.item:not(.add)').length>=2){
						addObj.hide();
					}else{
						addObj.show();
					}
					addObj.before('<div class="item old"><a href="javascript:;" class="preview"><img src="'+src+'"></a></div>');
					imgBox.parent().submit();
					var tempObj = $(this).parent();
					var id = $(this).attr('id');
					$(this).remove();
					tempObj.append('<input type="file" name="file" hidefocus="true" id="'+id+'" class="file-input" />');
				}
				return false;
			}).on('click','.addList .preview img',function(){
				var html = [];
				html.push('<ul>');
				html.push('<li><img src="'+this.src+'" /></li>');
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show();
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
			}).on('click','#bigImg',function(){
				$(this).hide().find('.bd ul').remove();
			});
		},
		dataList:function(){
			var that=this;
			var id=$('#formId').val();
			$.ajax({
				url:dataUrl.dataList,
				data:{id:id},
				success: function(result){
					if(result.flag=='0'){
						return false;
					}else {
						var html=[];
						var data=result.obj;
						var jsonText=jQuery.parseJSON(data.form);
						var count=0;	//记录总题数
						for(var i=0;i<jsonText.list.length;i++){
							if(jsonText.list[i].type=="explain"){
								count++;
							}
						}
						$('#countValue').val(jsonText.list.length-Number(count));
						for(var i=0;i<jsonText.list.length;i++){
							if(jsonText.list[i].type=="singleText"){//单行
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single"><input type="text" name="" id="" value="" /></div>');
							}else if(jsonText.list[i].type=="multilineText"){//多行
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content area"><textarea cols="50" rows="10" class="txt"></textarea></div>');
							}else if(jsonText.list[i].type=="singleRedio"){//单选
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content rad">');
								var singleValue=jsonText.list[i].value.replace("，",",").split(',');
								for(var j=0;j<singleValue.length;j++){
									html.push('<input type="radio" name="buy'+i+'" id="r_c'+j+i+'" value="" class="rad_1"/>');
									html.push('<label for="r_c'+j+i+'"></label><span>'+singleValue[j]+'</span><br />');
								}
								html.push('</div>');
							}else if(jsonText.list[i].type=="checkRedio"){//多选
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content box">');
								var checkValue=jsonText.list[i].value.replace("，",",").split(',');
								for(var j=0;j<checkValue.length;j++){
									html.push('<input type="checkbox" name="" id="ck_b'+j+i+'" class="chk_1" />');
									html.push('<label for="ck_b'+j+i+'" class="lab_1"></label><span>'+checkValue[j]+'</span><br />');
								}
								html.push('</div>');
							}else if(jsonText.list[i].type=="timeText"){//时间框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single">');
								html.push('<input type="text" name="" id="birthdate" value="" readonly="readonly"/>');
								html.push('<div id="dateContainer"></div>');
								html.push('</div>');
							}else if(jsonText.list[i].type=="areaText"){//区域框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single"><input type="text" name="" id="addr" value="" readonly="readonly"/>');
								html.push('<div id="addrContainer"></div>');
								html.push('<div id="city" style="display: none;"><select id="seachprov" name="seachprov"></select>&nbsp;&nbsp;<select id="seachcity" name="homecity" onChange=""></select>&nbsp;&nbsp;<span id="seachdistrict_div"><select id="seachdistrict" name="seachdistrict"></select></span><br /><button class="ok">确定</button><button class="no">取消</button></div></div>');
							}else if(jsonText.list[i].type=="telText"){//电话框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single">');
								html.push('<input type="number" name="" id="tel" value="" />');
								html.push('</div>');
							}else if(jsonText.list[i].type=="idCard"){//身份证框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single">');
								html.push('<input type="number" name="" id="IDcard" value="" />');
								html.push('</div>');
							}else if(jsonText.list[i].type=="dropDown"){//下拉框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content singlesel" style="position: relative;height: 30px;">');
								html.push('<input class="drop" type="text" name="" id="sel'+i+'" value="" readonly/><div id="selectContainer'+i+'"></div>');
								html.push('<select class="dropSelect" name="" id="select'+i+'">');
								if(that.IsPC()){
									var option=jsonText.list[i].value;
									var options=option.split(',');
									html.push('<option>请选择</option>');
									for(var j=0;j<options.length;j++){
										html.push('<option>'+options[j]+'</option>');
									}
								}
								html.push('</select></div>');
							}else if(jsonText.list[i].type=="explain"){//说明
								html.push('<p class="caption">'+jsonText.list[i].lable+'</p>');
							}else if(jsonText.list[i].type=="appointment"){//特殊预约框
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content single"><input type="text" name="" id="hospital" value="" readonly />');
								html.push('<div id="hospitalContainer"></div>');
								html.push('</div>');
							}else if(jsonText.list[i].type=="uploadPic"){
								html.push('<div class="title" id="t'+i+'">'+jsonText.list[i].lable+'</div>');
								html.push('<div class="content pic">');
								if(that.ua == 'wx'){
									html.push('<div class="addList" id="imgList"><div class="item add">');
									html.push('<a href="javascript:;" class="add-btn"><label></label></a></div></div></div>');
								}else{
									html.push('<form action="/file/upload3.do" id="form" method="post" enctype="multipart/form-data">');
									html.push('<div class="addList" id="imgList"><div class="item add">');
									html.push('<a href="javascript:;" class="add-btn"><label for="fileInput"></label>');
									html.push('<input type="file" name="file"  class="file-input" id="fileInput" hidefocus="true" />');
									html.push('<input type="hidden" name="type" id="type" value="4"></a></div></div></form></div>');
									$('body').append('<script src="/res/js/util/ajaxform.js"></script>');
								}
							}
						}
						html.push('<a href="javascript:;" class="submit">提交</a>');
						$('.survey').append(html.join(''));
						if($('#addr').length>0){
							$("#seachprov").attr("onchange","changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');")
							$("#seachcity").attr("onchange","changeCity(this.value,'seachdistrict','seachdistrict');")
						}
						if(that.IsPC()){
							$('.drop').hide();
							$('.select').show();
						}else{
							$('select').hide();
							$('.drop').show();
							$('.singlesel').addClass('single').removeClass('singlesel');
						}
						if($('#form').length > 0){
							that.ajaxForm($('#form'));
						}
					}
				},
				error: function(r){
					return false;
					//d.alert({content:'获取活动列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		},
		getProgress: function(){
			var countValue=$('#countValue').val();
			var count = new Array(countValue); //记录每道题是否回答
			var number=Number(countValue);
			var sum = 0; //答题数量和
			if($('#hospital').length>0){
				number += 1;
			}
			var txt = $(".content :text,.content input[type=number]").not(".other");
			var select=$(".content .dropSelect");
			var box = $(".box");
			var rad = $(".rad");
			var txtArea = $("textarea");
			var pic = $('.content.pic');
			txt.each(function(index,ele){
				if($(ele).val()!=""){
					count[index] = 1;
				}else{count[index] = 0}
			});
			box.each(function(index,ele){
//				if(index <= txt.size()){return true;}
				if($(ele).children(":checked").size()!=0){
					count[index+txt.size()] = 1;
				}else{count[index+txt.size()] = 0;}
			});
			rad.each(function(index,ele){
				if($(ele).children(":checked").size()!=0){
					count[index+txt.size()+box.size()] = 1;
				}else{count[index+txt.size()+box.size()] = 0;}
			});
			txtArea.each(function(index,ele){
				if($(ele).val()!=""){
					count[index+txt.size()+box.size()+rad.size()] = 1;
				}else{count[index+txt.size()+box.size()+rad.size()] = 0;}
			});
			select.each(function(index,ele){
				if($(ele).is(":hidden")){
					return false;
				}
				if(!/请选择/.test($(ele).children(":selected").text()) && $(ele).children(":selected").text()!=""){
					count[index+txt.size()+box.size()+rad.size()+txtArea.size()] = 1;
				}else{count[index+txt.size()+box.size()+rad.size()+txtArea.size()] = 0;}
			});
			pic.each(function(index,ele){
				if($(ele).find('.item').not('.add').length>0){
					count[index+txt.size()+box.size()+rad.size()+txtArea.size()+select.not(':hidden').size()] = 1;
				}else{
					count[index+txt.size()+box.size()+rad.size()+txtArea.size()+select.not(':hidden').size()] = 0;
				}
			});
			for(var i=0; i<count.length; i++){
				sum += count[i];
			}
			if(sum>=2){
				$(".pro").height(sum/number*180).children().text(Math.floor(sum/number*100)+"%");
			}else{$(".pro").height(sum/number*180);}
			if(sum == number){
				$(".pro").children().height(15);
			}
			return sum/number;
		},
		getAns:function(){
			var that = this;
			var str="";
			var s1="";
			var s2="";
			$(".content").each(function(){
				str += '{"'+$(this).prev().text().replace(/"|“|”/g,'')+'":';
				if($(this).hasClass("single")){
					if($(this).prev().text().indexOf("身份证")>=0){
						s1=$(this).children("input").val().substring(6,12);
					}
					if($(this).prev().text().indexOf("年月")>=0){
						s2=$(this).children("input").val();
						var s3=s2.split("-");
						s2=s3[0]+s3[1];
					}
					/*if($(this).children("select").size()>0 && IsPC()){
						str += '"'+$(this).find(":selected").text().replace(/"|“|”/g,'')+'"};';
					} else {*/
						str += '"'+$(this).children("input").val().replace(/"|“|”|{|}/g,'').replace(/;|；|:|：/g,',')+'"};';
					/*}*/
				}else if($(this).hasClass("box")){
					str += '"';
					$(this).children(":checked").each(function(){
						str += $(this).next().next().text().replace(/"|“|”/g,'')+',';
					});
					if($(this).children(".other").size()!=0 && $(this).children(".other").val()!=""){
						str += '"'+$(this).children(".other").val().replace(/"|“|”|{|}/g,'').replace(/;|；|:|：/g,',')+',';
					}
					str += '"};';
				}else if($(this).hasClass("rad")){
					str += '"'+$(this).children(":checked").next().next().text().replace(/"|“|”/g,'')+',';
					if($(this).children(".other").size()!=0 && $(this).children(".other").val()!=""){
						str += '"'+$(this).children(".other").val().replace(/"|“|”/g,'')+',';
					}
					str += '"};';
				}else if($(this).hasClass("area")){
					str += '"'+$(this).children().val().replace(/"|“|”|{|}/g,'').replace(/;|；|:|：|\n/g,',')+'"};';
				}else if($(this).hasClass("singlesel") && IsPC()){
					str += '"'+$(this).find(":selected").text().replace(/"|“|”/g,'')+'"};';
				}else if($(this).hasClass("pic")){
					str += '"'+that.urls+'"};';
				}
			})
			var lastIndex = str.lastIndexOf(',');
			if (lastIndex > -1) {
				str = str.substring(0, lastIndex) + str.substring(lastIndex + 1, str.length);
			}
			if(s1==s2 || s1=="" || s2==""){
				return str;
			}else{
				that.showTips('出生年月与身份证不符！');
				return false;
			}
		},
		photoPic:function(file){
		    var filepath = $(file).val();				
		    var extStart=filepath.lastIndexOf(".")+1;		
			var ext=filepath.substring(extStart,filepath.length).toUpperCase();		
			if(ext!='JPG' && ext!='PNG' && ext!='JPEG' && ext!='GIF'&& ext!='BMP'){	
				alert('上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！');
				return false;
			}
			var file_size = 0;
				file_size = file.files[0].size;
				var size = file_size / 1024;
				if (size > 2048) {
					alert("上传的图片大小不能超过2M！");
					return false;
				} 
			return true;
		},
		ajaxForm:function(obj){
			var that=this;
			$(obj).ajaxForm({
				beforeSend: function() {},
				uploadProgress: function(event, position, total, percentComplete) {},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')'); 
					if(json.result==1){
						var imgBox=obj.find('.addList');
						imgBox.children('.old').remove();
						alert(json.error);
						return false;
					}else{
						var imgBox=obj.find('.addList'),addObj=imgBox.children(".add");
						that.ids.push(json.imageId);
						imgBox.children('.old').attr('id',json.imageId).find('img').attr('src',json.imageUrl).attr('class','io').end();
						imgBox.children('.old').removeClass('old');
						imgBox.find(".item").not(".add").append('<i class="del"><img src="/res/images/h5/images/close.png"/></i>');
						that.showTips('上传成功！');
					}
				},
				error: function(data) {
					var imgBox=obj.find('.addList'),addObj=imgBox.children(".add");
					addObj.show();
					imgBox.children('.old').remove();
					alert('上传失败！');
				}
			}); 
		},
		//提示信息
		showTips:function(txt){
			var $tips = $('#tips');
			if(!$tips.is(':hidden')){
				$tips.hide().html('');
			}
			$tips.text(txt).fadeIn();
			setTimeout(function(){
				$tips.fadeOut(function(){
					$(this).text('');
				});
			},2000);
		},	
		userBG:function(){
			var userAgent = navigator.userAgent;
			if(userAgent.indexOf("Firefox")>-1){
				$(".rad_1").next().css({"background":"#fff"});
				$(".rad_1:checked").next().css({"background":"url('../../res/images/form/cir.png') no-repeat 0.15rem center"});
			} else if (userAgent.indexOf("MSIE")>-1 && userAgent.indexOf("Opera")==-1){
				alert(1)
				$(".rad_1").next().css({"background":"#fff"});
				$(".rad_1:checked").next().css({"background":"url('../../res/images/form/cir.png') no-repeat 0.28rem center"});
			}else if (userAgent.indexOf("Trident")>-1 && userAgent.indexOf("Opera")==-1){
				$(".rad_1").next().css({"background":"#fff"});
				$(".rad_1:checked").next().css({"background":"url(./img/cir.png) no-repeat 0.14rem center"});
			}
		},
		isCardID:function(sId){
			 var vCity={ 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古", 
			    21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏", 
			    33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南", 
			    42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆", 
			    51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃", 
			    63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"
			   }; 

			 var iSum=0 ;
			 var info="" ;
			 if(!/^\d{17}(\d|x)$/i.test(sId)) return "你输入的身份证长度或格式错误";
			 sId=sId.replace(/x$/i,"a");
			 if(vCity[parseInt(sId.substr(0,2))]==null) return "你的身份证地区非法";
			 sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
			 var d=new Date(sBirthday.replace(/-/g,"/")) ;
			 if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "身份证上的出生日期非法";
			 for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
			 if(iSum%11!=1) return "你输入的身份证号非法";
			 //aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
			 return true;
		},
		isTel:function(tel){
			return tel!="" && !(/^1[34578]\d{9}$/.test(tel))?false:true;
		},
		IsPC:function() {
		   	var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone",
		                "SymbianOS", "Windows Phone",
		                "iPad", "iPod"];
		    var flag = true;
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		            flag = false;
		            break;
		        }
		    }
		    return flag;
		}
	}
	addListNew.init();
});