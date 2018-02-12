
var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
var dataUrl={
		donationlist:base+'message/loadMessageWallList.do',
		addLeaveWord:base+'h5ProjectDetails/addNewLeaveWord.do'
};
var pages = 1;
var messageWall = {
	init:function(){
		var that = this;
		var myChart = echarts.init($('#wall')[0]);
		myChart.showLoading();
		var datas = [];
		for(var i=1;i<=pages;i++){
			if(i == 1){
				datas = that.leaveWordList(i,50);
			}else{
				datas = datas.concat(that.leaveWordList(i,10));
			}
		}
		pages++;
		var option = {
			title:{
				text:"",
				left:"center"
			},
			tooltip:{
				trigger:'item',
				formatter:function(params){
					var html = '<span class="circle" style="background-color:'+params.color+';"></span>';
					if(params.data.value[0]>1000){
						return '<p><a href="'+params.data.link+'">'+html+''+params.data.name+'：为善园添块砖</a></p><p style="word-wrap:break-word!important;max-width:300px;white-space:normal">'+html+''+'留言：'+params.data.value[1]+'</p><p>'+html+''+'日期：'+params.data.value[2];
					}else{
						return '<p><a href="'+params.data.link+'">'+html+''+params.data.name+'：到此一游</a></p><p style="word-wrap:break-word!important;max-width:300px;white-space:normal">'+html+''+'留言：'+params.data.value[1]+'</p><p>'+html+''+'日期：'+params.data.value[2];
					}
				},
//				position:['10%','40%'],
				textStyle:{
					fontSize:13
				},
				confine:true,
				enterable:true
			},
			calculable : false,
		    series : [
		        {
		            name:'恢复原状',
		            breadcrumb:{
		            	show:true,
		            	left:0,
		            	top:"bottom"
		            },
		            type:'treemap',
		            width:'100%',
		            height:'100%',
		            visibleMin:2,
		            itemStyle: {
		                normal: {
		                    borderWidth: 1
		                },
		                emphasis: {
		                    label: {
		                        show: true
		                    }
		                }
		            },
		            data:datas
		        }
		    ]
		};
		myChart.setOption(option,true);
		myChart.hideLoading();
	},
	leaveWordList:function(page,pageNum){
		var projectId = $('#projectId').val();
		var datas = [];
		var userId = $('#userId').val();
		$.ajax({
			url:dataUrl.donationlist,
			data:{projectId: projectId,page: page,pageNum: pageNum,t: new Date().getTime()},
			type:'GET',
			async:false,
			success:function(result){
				var w = Math.floor($(window).width()*0.0124);
				var total = result.total;
				if(result.result != 2) {
					if(result.items.length>0){
						for(var i=0;i<result.items.length;i++){
							var b = (result.items[i].id == userId);
							var a = (result.items[i].donated == 0);
							datas.push({
								name:result.items[i].name,
								value:[Number(result.items[i].donated)+1000,result.items[i].text,result.items[i].time],
								link:'javascript:;',
								id:result.items[i].id,
								itemStyle:{
									normal:{
//										borderColor:b?'#f00':a?'#ff9002':'#fff',
										color:a?'#eaeaea':null
									}
								},
								label:{
									normal:{
										textStyle:{
											color:a?'#333':'#fff'
										}
									}
								}
							});
						}
					}
				}
				if(result.items.length<pageNum || total<=pages*pageNum){
					$('.loadmore span').html("没有数据了");
				}
			}
		});
		return datas;
	},
	bindEvent:function(){
		var that = this;
		/*if($('#show').val()==1){
			$('#overLay').show();
			$('.leaveWordTips').fadeIn();
		}else{
			$('.leaveWordTips').fadeOut();
			$('#overLay').hide();
		}*/
		$('body').on('click','.loadmore',function(){
			if($(this).children('span').html() == "没有数据了"){
				return false;;
			}
			that.init();
		}).on('click','.leaveWordTips .close',function(){
			$('#overLay').hide();
			$('.leaveWordTips').fadeOut();
		}).on('click','.leaveWordEdit .close2',function(){
			$('#overLay').hide();
			$('.leaveWordEdit').fadeOut();
		}).on('click','.selectTo .close4',function(){
				$('#overLay').hide();
				$('.selectTo').fadeOut();
		}).on('click','.fd-foot .btn',function(){
			$('#overLay').show();
			$('.leaveWordEdit').fadeIn();
		}).on('click','.fd-foot .share',function(){
			var projectId = $('#projectId').val();
			var userId = $('#userId').val();
			var content = "到此一游";
			that.addLeaveWord(userId,projectId,content);
			return true;
		}).on('click','.selectTo .toLeaveWord',function(){
			$('.selectTo').fadeOut(function(){
				$('.leaveWordEdit').fadeIn();
			});
		}).on('click','#overLay',function(){
			$('#overLay').hide();
			$('.leaveWordEdit').fadeOut();
			$('.leaveWordTips').fadeOut();
			$('.selectTo').fadeOut();
		}).on('click','#pub',function(){
			var projectId = $('#projectId').val();
			var userId = $('#userId').val();
			var content = $('#word').val();
			if(content==''){
				layer.msg('留言内容不能为空');
				return false;
			}
			that.addLeaveWord(userId,projectId,content);
			return true;
		});
		$('#overLay')[0].addEventListener('touchmove',function(event){
			event.preventDefault();
		},false);
	},
	addLeaveWord:function(userId,projectId,content){
		$.ajax({
			url:dataUrl.addLeaveWord,
			data:{leavewordUserId: userId,projectId: projectId,content: content,t: new Date().getTime()},
			type:'POST',
			success:function(res){
				if(res.flag==1){
					layer.msg(res.errorMsg);
					setTimeout(window.location=base+'message/messageWall_view.do?show=0&projectId='+projectId,1000)
					
				}else{
					layer.msg(res.errorMsg);
				}
			},
			error:function(e){
				layer.msg(e);
			}
		});
	}
};
messageWall.init();
messageWall.bindEvent();

