/**
 * 2017-7-19
 * 
 */
var base = window.baseurl;
//var base = 'http://www.17xs.org:8080/';
var dataUrl = {
	needHelpList:'/uCenterProject/oneAid_random.do'
};

var planSet = {
	init:function(){
		var that = this;
		this.needHelpList();
		this.bindEvent();
		$('.bigPic')[0].addEventListener('touchmove',function(event){event.preventDefault();},false);
	},
	needHelpList:function(){
		var projectId = $('#projectId').val();
		$.ajax({
			type:"get",
			url:dataUrl.needHelpList,
			data:{projectId:projectId},
			success:function(result){
				if(result.code==1){
					var html=[];
					var name = result.name;
					if(name.length>2){
						name = name.replace(name.charAt(name.length/2),'*');
					}else{
						name = name.charAt(0)+'*'+name.charAt(name.length-1);
					}
					
					html.push('<h2 class="f16">'+name);
					html.push('<span class="f12 cb2b">&nbsp;&nbsp;'+result.sex+'&nbsp;&nbsp;'+result.age+'岁</span></h2>');
					html.push('<p class="f12 cb2b mt5">'+result.addr+'</p>');
					html.push('<p class="info mt5">'+result.content+'</p>');
					html.push('<div class="pic"><ul>');
					for(var j=0;j<result.imgList.length;j++){
						html.push('<a href="javascript:;"><li><img src="'+result.imgList[j]+'" width="60" height="60"/></li></a>');
					}
					html.push('</ul></div>');
					$('#plan_box').html(html.join(''));
					$('#id').val(result.id);
					$('#plan_money').text(result.total_money);
				}else{
					console.log('项目：'+projectId+'帮扶列表为空');
				}
			}
		});
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#change_plan',function(){
			that.needHelpList();
		}).on('click','#plan_box .pic a',function(){
			$('#overLay').show();
			var h = $(window).height();
			var len = $('#plan_box .pic a').length;
			var html = [];
			html.push('<ul>');
			for(var i=0;i<len;i++){
				html.push('<li><img src="'+$('#plan_box .pic a').eq(i).find('img').attr('src')+'" width="100%"/></li>')
			}
			html.push('</ul>');
			$('.bigPic .bd').html(html.join(''));
			$('.bigPic').fadeIn();
			$('.bigPic .num').html($(this).index()+1+'/'+len);
			TouchSlide({
				slideCell:"#banner",
				titCell:".hd ul",
				mainCell:".bd ul",
				effect:"leftLoop",
				autoPlay:false,
				autoPage:true,
				defaultIndex:$(this).index(),
				endFun:function(i,c){
					$('.bigPic .num').html((i+1)+'/'+c);
				}
			});
			$('#banner .bd img').each(function(){
				var h2 = $(this).height();
				$(this).css({'margin-top':(h-h2)/2+'px'});
			});
		}).on('click','.bigPic',function(){
			$('#overLay').hide();
			$(this).fadeOut();
			$('.bigPic .bd').html('');
			TouchSlide(null);
		}).on('click','#submit_pay',function(){
			if($(this).hasClass('disable'))return;
			var money=$('#plan_money').text(),projectId=$("#projectId").val(),id=$('#id').val(),
			browser=$('#browser').val(),userId=$('#userId').val();
			if(browser!='wx' && userId==''){
				window.location.href="http://www.17xs.org/ucenter/user/Login_H5.do?flag=oneAid_"+projectId;
				return true;
			}
			var pName = $(".f15").text(); 
			var extensionPeople = $('#extensionPeople').val();
			window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
				+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&slogans=oneAid_"+id;
			
		}).on('change','#payProto',function(){
			if(this.checked){
				$('#submit_pay').removeClass('disable');
			}else{
				$('#submit_pay').addClass('disable');
			}
		});
	},
	showTips:function(text){
		if(!$('.tips').is(':hidden')){
			return false;
		}
		$('.tips').html(text).show().animate({'bottom':'100px'},'swing',function(){
			var _this = $(this);
			setTimeout(function(){
				_this.fadeOut(function(){
					_this.css({'bottom':'-50px'}).html('');
				})
			},2000);
		});
	}
};

planSet.init();
