var base = 'http://www.17xs.org/';
var dataUrl = {
	getDonateList: base +'user/donationlist_invoice.do',		//获取捐赠列表
	addInvoice: base +'user/addInvoice.do',						//提交发票
	getInvoiceList: base +'user/invoiceList.do'					//待发货列表
};
var flag1 = flag2 = flag3 = false;
var invoice = {
	timer:null,
	cb:null,
	cancel_cb:null,
	page:1,
	len:0,
	onOff:false,
	init:function(){
		var that = this;
		
		if(global_addrs.length == 0){
			that.showDialog({
				title:'缺少收货地址',
				ask:'去设置收货地址吗？',
				lText:'稍后再去',
				rText:'现在就去',
				callback:function(){
					location.href = "http://www.17xs.org/user/toAddress.do?type=4";
				}
			});
		}else{
			var html = [];
			html.push('<ul id="d-addr">');
			for(var i in global_addrs){
				html.push('<li data-id="'+ global_addrs[i].id +'" class="flex fcen spb '+ (i==0?'on':'') +'">');
				html.push('<a href="javascript:;" class="addr-a">'+ global_addrs[i].addr +'</a>');
				html.push('<span class="gou"></span></li>');
			}
			html.push('</ul>');
			$('#d-ask').after(html.join(''));
		}
		
		var mark = that.GetQueryString("mark");
		if(mark == 2) {
			$("#mark2").addClass("active").siblings().removeClass("active");
			$("#tab2").show();
			$('#loading').show();
			setTimeout(function(){
				that.getInvoiceList(that.page,10,300);
			},500);
		}else{
			$('#loading').show();
			setTimeout(function(){
				that.getDonateList(that.page,20);
			},500);
		}
		that.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		
		/*开票start*/
		$('body').on('click','#cancel',function(){	//关闭对话框，执行函数
			that.hideDialog(that.cancel_cb);
			
		}).on('click','#sure',function(){	//关闭对话框，执行函数
			that.hideDialog(that.cb);
			
		}).on('click','#closeIcon',function(){	//关闭对话框
			that.hideDialog();
			
		}).on('click','#loadMoreALeft',function(){	//开票数据点击加载更多
			if($(this).hasClass('no')) return false;
			if(that.onOff){
				$('#loading').show();
				setTimeout(function(){
					that.getDonateList(++that.page,20);
				},500);
			}
		}).on('click','.tabBox li',function(){	//选择显示内容
			that.page = 1;
			$(this).addClass("active").siblings().removeClass("active");
			$(".tabInfo").hide();
			var idx = $(this).index() + 1;
			$("#tab" + idx).show();
			if(idx == 1 && !flag1){
				$('#loading').show();
				setTimeout(function(){
					that.getDonateList(that.page,20);
				},500);
			}else if(idx == 2 && !flag2){
				$('#loading').show();
				setTimeout(function(){
					that.getInvoiceList(that.page,10,300);
				},500);
			}else if(idx == 3 && !flag3){
				$('#loading').show();
				setTimeout(function(){
					that.getInvoiceList(that.page,10,302);
				},500);
			}
		}).on('change','.mainerLeft .check',function(){		//单选
			var len1 = $('.mainerLeft .check').length,
				len2 = $('.mainerLeft .check:checked').length;
			if(len1 !== len2){
				$('#chkAll')[0].checked = false;
			}else{
				$('#chkAll')[0].checked = true;
			}
			if(len2 == 0){
				$('.submitBtn').addClass('disabled');
			}else{
				$('.submitBtn').removeClass('disabled');
			}
			that.getSumMoney();
			
		}).on('change','#chkAll',function(){	//全选
			var check = this.checked;
			$('.mainerLeft .check').each(function(){
				this.checked = check;
			});
			if(check){
				$('.submitBtn').removeClass('disabled');
			}else{
				$('.submitBtn').addClass('disabled');
			}
			that.getSumMoney();
			
		}).on('click','#d-addr .addr-a',function(){		//选择地址
			var $li = $(this).parent();
			if($li.hasClass('on')) return false;
			$li.addClass('on').siblings().removeClass('on');
			
		}).on('click','.submitBtn',function(){		//提交
			if($(this).hasClass('disabled')) return false;
			
			var arr = [], obj = {},addrId = '',
				sumMoney = Number($("#money_sum").text()), 
				$check = $('.mainerLeft .check:checked'),
				head = $("#invoiceHead").val(), 
				reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
				
			if(head == '' || !reg.test(head)){
				that.showTips('请输入正确的发票抬头！');
				$("#invoiceHead").focus();
				return;
			}
			if(global_addrs.length == 0){
				that.showDialog({
					title:'缺少收货地址',
					ask:'去设置收货地址吗？',
					lText:'稍后再去',
					rText:'现在就去',
					callback:function(){
						location.href = "http://www.17xs.org/user/toAddress.do?type=4";
					}
				});
			}else{
				that.showDialog({
					title:'选择收货地址',
					ask:'确定选择下面的收货地址吗？',
					addr:true,
					lText:'新增地址',
					closeShow:true,
					cancel_cb:function(){
						location.href = "http://www.17xs.org/user/toAddress.do?type=4";
					},
					callback:function(){
						addrId = $('#d-addr .on').data('id');
						$check.each(function(){
							arr.push($(this).data('id'));
						});
						obj.head = head;
						obj.addrId = addrId;
						obj.info = arr.join('_');
						obj.money = sumMoney;
						if(sumMoney < 100) {
							that.showDialog({
								title:'开票确认',
								ask:'您的开票金额少于100元，邮寄费用由您承担，是否继续开票？',
								cancel_cb:null,
								callback:function(){
									that.submitInvoice(obj);
								}
							});
							return;
						}else{
							that.submitInvoice(obj);
						}
					}
				});
			}
		});
		$('.mainerLeft').on('scroll',function(){	//上拉加载
			if(that.onOff){
				var st = this.scrollTop,
					h = $(this).height(),
					sh = this.scrollHeight - 5;
				if(st + h >= sh){
					$('#loading').show();
					setTimeout(function(){
						that.getDonateList(++that.page,20);
					},500);
				}
			}
		});
		/*开票end*/
		
		/*待/已发货start*/
		$('body').on('click','#loadMoreACenter',function(){		//待发货加载更多
			if($(this).hasClass('no')) return false;
			$('#loading').show();
			setTimeout(function(){
				that.getInvoiceList(++that.page,10,300);
			},500);
			
		}).on('click','.fpListItem a',function(){		//待发货点击跳转
			var id = $(this).data('id');
			location.href = "http://www.17xs.org/user/detailInvoice.do?id="+ id;
			
		}).on('click','#loadMoreARight',function(){		//已发货加载更多
			if($(this).hasClass('no')) return false;
			$('#loading').show();
			setTimeout(function(){
				that.getInvoiceList(++that.page,10,302);
			},500);
		});
		/*待/已发货end*/
		
	},
	getDonateList:function(pageNum,pageSize){	//捐赠列表
		var that = this;
		that.onOff = false;
		$.ajax({
			url: dataUrl.getDonateList,
			type: 'get',
			data: {
				pageNum: pageSize,
				page: pageNum
			},
			success: function(data) {
				$('#loading').hide();
				if(data.flag == 1) {
					var nums = data.obj.nums;
					var htmlStr = '';
					var ret = data.obj.data;
					for(var i in ret) {
						i = Number(i);
						var r = ret[i];
						if(r.title.length > 15){
							r.title = r.title.substr(0,15)+'...';
						}
						htmlStr = htmlStr + '<li class="fpdonateItem flex spb fcen">'
								+ '<label for="chk'+ (that.len + i) + '">为"' + r.title + '"捐助了' + r.dMoney + '元</label>'
								+ '<input type="checkbox" id="chk'+ (that.len + i) +'" value="'+ r.dMoney +'" data-id="'+ r.id +'" class="check" />'
								+ '</li>';
					}
					$('.mainerLeft').append(htmlStr);
					that.onOff = true;
					if(nums == 0){
						$('.submitBtn').hide();
						$('#tab1').html('<p style="padding:15px;color:#999;font-size:.25rem;text-align:center;">没有捐赠数据，不能开票！</p>');
					}else if(nums <= pageNum * pageSize) {
						that.onOff = false;
						$("#loadMoreALeft").html('没有更多数据了').addClass('no');
					}else{
						$("#loadMoreALeft").html('上拉或点击查看更多捐款记录');
					}
					$('#tab1').show();
					that.len += ret.length;
					$('#chkAll')[0].checked = false;
				} else {
					$('.submitBtn').hide();
					$('#tab1').html('<p style="padding:15px;color:#999;font-size:.25rem;text-align:center;">没有捐赠数据，不能开票！</p>').show();
				}
				flag1 = true;
			},error:function(){
				$('#loading').hide();
				alert('请求出错，请稍后再试！');
			}
		});
	},
	getInvoiceList:function(pageNum,pageSize,state){	// 待/已发货列表 300/302
		var that = this;
		$.ajax({
			url: dataUrl.getInvoiceList,
			type: 'get',
			data: {
				pageNum: pageSize,
				page: pageNum,
				state: state
			},
			success: function(data) {
				$('#loading').hide();
				var stateText = (state == 300 ? '待发货' : '已发货');
				var temp = (state == 300 ? 'Center' : 'Right');
				var $a = $('#loadMoreA'+ temp);
				if(data.flag == 1) {
					var nums = data.obj.nums;
					var htmlStr = "";
					var ret = data.obj.data;
					for(var i in ret) {
						var r = ret[i];
						htmlStr = htmlStr + '<li class="fpListItem"><a href="javascript:;" data-id="'+ r.id +'">'
								+ '</i><div class="fpListTitle clearfix"><p>' + r.invoiceHead + '</p><span>¥' + r.invoiceAmount 
								+ '</span></div><div class="fpListState clearfix"><span class="fl" id="time">' + r.createTime 
								+ '</span><span class="fr">'+ stateText +'</span></div><span class="fpListPiont"></span>';
					}
					$('.mainerCenter').append(htmlStr);
					if(nums == 0){
						$a.html('还没有'+ stateText +'的记录！').addClass('no');
					}else if(nums <= pageNum * pageSize) {
						$a.html('没有更多数据了').addClass('no');
					}else{
						$a.html('点击查看更多记录');
					}
				} else {
					$a.html('还没有'+ stateText +'的记录！').addClass('no');
				}
				if(state == 300){
					flag2 = true;
				}else{
					flag3 = true;
				}
			},error:function(){
				$('#loading').hide();
				alert('请求出错，请稍后再试！');
			}
		});
	},
	getSumMoney:function(){		//计算总开票金额
		var that = this,
			sum =  0,
			$check = $('.mainerLeft .check:checked');
			
		$check.each(function(){
			sum += Number(this.value) * 100;
		});
		sum = parseFloat(sum / 100).toFixed(2);
		$('#money_sum').text(sum);
	},
	submitInvoice:function(data){		//提交开票数据
		var that = this;
		$.ajax({
			url: dataUrl.addInvoice,
			type: 'post',
			data: {
				invoiceHead: data.head,
				addressId: data.addrId,
				info: data.info,
				content: '',
				isFree: '',
				mailAmount: '',
				mailCompany: '',
				mailCode: '',
				invoiceAmount: data.money
			},
			success: function(data) {
				if(data.flag == 1) {
					that.showTips('开票成功！');
					setTimeout(function() {
						location.href = "http://www.17xs.org/ucenter/myInvoice.do?mark=2";
					}, 2000);
				}
			}
		});
	},
	showTips:function(txt){		//提示信息
		var $msg = $('#msg');
    	if(!$msg.is(':hidden')){
    		$msg.hide();
    		this.timer = null;
    	}
    	$msg.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$msg.fadeOut();
    	},2000);
	},
	showDialog:function(options){		//显示对话框
		var that = this;
		var t = options.title || '标题',
			a = options.ask || '提示语',
			l = options.lText || '取消',
			r = options.rText || '确定',
			c = options.closeShow || false,	//是否显示关闭图标
			addr = options.addr || false;	//是否显示地址列表
		that.cb = options.callback || null;	//确定的回调函数
		that.cancel_cb = options.cancel_cb || null;	//取消的回调函数
		$('#d-title').text(t);
		$('#d-ask').text(a);
		$('#cancel').text(l);
		$('#sure').text(r);
		if(addr){
			$('#d-addr').show();
		}else{
			$('#d-addr').hide();
		}
		if(c){
			$('#closeIcon').show();
		}else{
			$('#closeIcon').hide();
		}
		$('#addrDialog').addClass('show');
	},
	hideDialog:function(cb){		//隐藏对话框
		$('#addrDialog').addClass('hide');
		setTimeout(function(){
			$('#addrDialog').removeClass('show hide');
			if(cb && (typeof cb === 'function')){
				cb();
			}
		},400);
	},
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};

$(function(){
	invoice.init();
});
