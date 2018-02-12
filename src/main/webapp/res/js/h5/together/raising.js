/*
 * 2017-6-15
 *
 */
var base = window.baseurl;
var dataUrl = {
	addTogetherConfig: base + "together/addTogetherConfig.do",
	delImg: base + "file/image/delete.do",
	donationlist: base + 'project/gardendonationlist_new.do',
	selectMoneyConfig: base + 'together/selectMoneyConfig.do',
	User_replyLeaveWord: base + "h5ProjectDetails/addNewLeaveWord.do" //提交评论
};
require.config({
	baseUrl: base + "/res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"ajaxform": "util/ajaxform"
	},
	urlArgs: "v=20170616"
});

define(["extend", "ajaxform"], function($, a) {
	var page = 1;
	var winH=$(window).height();
	var together = {
		init: function() {
			var that = this;
			that.ajaxForm($('#form'));
			that.loadRecordList(1, 10);
			$('body').on('change', '#file-input', function() {
				var p = that.photoPic(this);
				if(p) {
					var imgBox = $(this).parents(".add-list"),
						addObj = imgBox.children(".add"),
						src = base + '/res/images/common/bar1.gif';
					addObj.hide();
					addObj.before('<div class="item old"><img src="' + src + '" width="60px" height="60px"></div>');
					imgBox.parent().submit();
					var tempObj = $(this).parent();
					var id = $(this).attr('id');
					$(this).remove();
					tempObj.append('<input type="file" name="file" hidefocus="true" id="' + id + '" class="file-input" />');
				}
				return false;
			});

			$('.next').click(function() {
				//获取type
				//必填： 发起说明     团体（团体名称）
				var launchName = $('#groupName').val(),
					content = $('#content').val(),
					projectId = $('#projectId').val(),
					type,totalMoney = -1,
					coverImageId = $('.item').attr('id');
					
				if($('.raise-way .btn.on').html() == '个人') {
					type = 'personal';
				} else { //团体
					type = 'group';
					if(launchName == '') {
						layer.msg('请填写团体名称！');
						$('#groupName').focus();
						return false;
					}
				}
				if(content == '') {
					content=$('#content').attr('placeholder');
				}
				if(projectId !=3429){
					totalMoney = $('#dMoney').val();
					if($('.user-defined').hasClass('on')) {
						if(!Number(totalMoney)) {
							layer.msg('请输入正确的金额！');
							$('.raise-dest #dMoney').val('').focus();
							return false;
						} else if(Number(totalMoney) <= 0) {
							layer.msg('请输入正确的金额！');
							$('.raise-dest #dMoney').val('').focus();
							return false;
						}
						totalMoney = parseFloat(totalMoney).toFixed(2);
					} else {
						totalMoney = -1;
					}	
				}else{
					if($('.total span').length>0){
						totalMoney = Number($('.total span').text());
					}else{
						totalMoney = -1;
					}
				}
				$.ajax({
					url: dataUrl.addTogetherConfig,
					data:{
						projectId: projectId,
						totalMoney: totalMoney,
						launchName: launchName,
						content: content,
						type: type,
						coverImageId: coverImageId,
						t: new Date()
					},
					type: 'GET',
					success: function(res) {
						if(res.code == "1") {
							layer.msg(res.msg);
							location.href = "/together/raiseDetail_view.do?show=1&projectId=" + projectId;
							return true;
						} else {
							layer.msg(res.msg);
							return false;
						}
					}
				});
			});

			$('body').on('click', '.item i', function() {
				that.delPic($(this).parent().attr('id'), $(this));
			});
			
			$('input.dMoney').on('focus',function(){
				if($(window).height()!=winH){
					$(this).parents('.rd-donate').css({'bottom':'-60px'});
				}
			});
			
			//回复按钮
			$('#leaveWordSubmit').click(function() {
				var content = $('#content').val();
				if(content == '') {
					layer.msg('请输入评论！');
					return false;
				}
				that.verify();
			});

			$('.end_fl1').click(function() {
				$(".moved_up").hide();
				$("#overLay").hide();
			});

			$('.tishi2 .close').click(function() {
				$(".tishi2").hide();
				$("#overLay").hide();
			});

			$('.payBtn').click(function() {
				if($(this).hasClass('disable'))return;
				var money = $(".dd-ul li.on span").text(),
					money2 = $('#dMoney').val(),
					projectId = $("#projectId").val(),
					userId = $("#userId").val(),
					realName = $("#realName").val(),
					mobileNum = $("#mobileNum").val(),
					donateWord = $("#donateWord").val(),
					shareUserId = $('#shareUserId').val(),
					needmoney = $("#needmoney").val();
				if(donateWord == '') {
					donateWord = $('#donateWord').attr('placeholder');
				}
				if(shareUserId == '') {
					shareUserId = 0;
				}
				if(typeof(money) != undefined && money > 0) {

				} else {
					money = money2;
				}

				var paymoney = Number(money),
					pName = $("#pprim").val();
				if(isNaN(paymoney)) {
					layer.msg("捐款金额不低于0.01元");
					$('#dMoney').val('').focus();
					return;
				}
				if(paymoney <= 0) {
					money = $("#money2").val();
					if(money < 0.01) {
						layer.msg("捐款金额不低于0.01元");
						$('#dMoney').val('').focus();
						return;
					}
					money = Number(money);
					if(!isNaN(money)) {
						if(needmoney < money) {
							layer.msg("最多捐款金额" + needmoney + "元");
							$('#dMoney').val('').focus();
							return;
						}
						if((userId == null || userId == '')) {
							window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?" +
								"slogans=together_" + shareUserId + "&projectId=" + projectId + "&amount=" + money + "&realName=" +
								realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord + "&extensionPeople=" +
								$("#shareUserId").val();
						} else {
							window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?" +
								"slogans=together_" + shareUserId + "&projectId=" + projectId + "&amount=" + money + "&realName=" +
								realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord + "&extensionPeople=" +
								$("#shareUserId").val();
						}

					} else {
						layer.msg("请输入数字");
						$('#dMoney').val('').focus();
						return;
					}
				} else {
					needmoney = Number(needmoney);
					if(needmoney < money) {
						layer.msg("最多捐款金额" + needmoney + "元");
						$('#dMoney').val('').focus();
						return;
					}
					if((userId == null || userId == '')) {
						window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?" +
							"slogans=together_" + shareUserId + "&projectId=" + projectId + "&amount=" + money + "&realName=" +
							realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord + "&extensionPeople=" +
							$("#shareUserId").val();
					} else {
						window.location.href = "http://www.17xs.org/visitorAlipay/tenpay/deposit.do?" +
							"slogans=together_" + shareUserId + "&projectId=" + projectId + "&amount=" + money + "&realName=" +
							realName + "&mobileNum=" + mobileNum + "&donateWord=" + donateWord + "&extensionPeople=" +
							$("#shareUserId").val();
					}
				}
			});

			$('.raise-way .btn').on('click', function() {
				$(this).siblings().removeClass('on');
				$(this).addClass('on');
				if($(this).hasClass('group')) {
					$('.raise-team').show();
				} else {
					$('.raise-team').hide();
				}
			});

			$('.introduction-text textarea').on('keyup', function() {
				$(this).css({'max-height':'100px'});
				$(this).height(function() {
					if($(this).height()<=100)
					return $(this).height() + $(this).scrollTop();
					else return 100;
				});
				$('.letter-rest').text(140 - $(this).val().length);
				if(Number($(this).val().length) >= 140) {
					$('.letter-rest').text(0);
					$(this).val($(this).val().substr(0, 140));
					return;
				}
			});

			$('.meiyuanshi-num .min').on('click', function() {
				$('#number').text(Number($('#number').text()) - 100);
				$('#total-money').text(Number($('#number').text()) * 1.68);
				if(Number($('#number').length > 0 && $('#number').text()) <= 0) {
					$('.num').html('目标不限');
					$('.total').html('目标不限');
					return;
				}
			});

			$('.meiyuanshi-num .add').on('click', function() {
				if($('#number').length == 0) {
					$('.num').html('<span id="number">0</span>块青砖');
					$('.total').html('合计<span id="total-money">0</span>元');
				}
				$('#number').text(Number($('#number').text()) + 100);
				$('#total-money').text(Number($('#number').text()) * 1.68);
			});

			$('.raise-progress .fill').css({
				width: $('.raise-progress .progress-bar label').text()
			});
			
			var $p1 = $(".raise-project .project-content p");
			if($p1.text().length > 45) {
				$p1.text($p1.text().substr(0, 44) + '...');
			}
			
			var $p2 = $('.rd-project .project-content p');
			if($p2.find('img').length > 0){
				$p2.css({'text-indent':0});
			}

			$(".rd-progress .fill").css({
				width: $('#raised-per').val()
			});

			$('#dest-money').on('keyup', function() {
				var num = parseFloat($(this).val()).toFixed(2);
				if(!isNaN(num)) {
					$('#money-num').val(num);
				} else {
					$('#money-num').val(0);
				}
			});

			$('.raise-dest #dMoney').on('keyup', function() {
				var num = parseFloat($(this).val()).toFixed(2);
				if(!isNaN(num)) {
					$('.raise-dest .total span').text(num);
				} else {
					$('.raise-dest .total span').text(0);
				}
			});

			$('.rt-next .next').on('click', function() {
				var num = parseFloat($('#money-num').val());
				if(num <= 0 && $('.dest').hasClass('on')) {
					that.tips('请输入有效金额！');
				}
			});

			$('.rd-btn .donate').on('click', function() {
				$('#overLay').show();
				$('.rd-donate').fadeIn();
				var num = $('.rd-donate .dd-ul li.on span').text();
				if(num){
					$.ajax({
						url: dataUrl.selectMoneyConfig,
						data: {
							projectId: $('#projectId').val(),
							money: num
						},
						type: 'GET',
						success: function(res) {
							$('.rd-donate #dMoney').val(res.content);
						}
					});
				}
			});

			$('.rd-donate .dd-ul li').on('click', function() {
				$(this).siblings().removeClass('on');
				$(this).addClass('on');
				$('.rd-donate #dMoney').attr('readonly', 'readonly');
				//获取选中的金额，查询文本
				var money = $(this).find('span').text();
				if($(this).hasClass('user-defined')) {
					$('.rd-donate #dMoney').removeAttr('readonly');
					$('.rd-donate #dMoney').val('').focus();
				} else {
					$.ajax({
						url: dataUrl.selectMoneyConfig,
						async:false,
						data: {
							projectId: $('#projectId').val(),
							money: money
						},
						type: 'GET',
						success: function(res) {
							$('.rd-donate #dMoney').val(res.content);
						}
					});
				}
			});

			$('.raise-dest .dd-ul li').on('click', function() {
				$(this).siblings().removeClass('on');
				$(this).addClass('on');
				if($(this).hasClass('user-defined')) {
					$('.raise-dest .diyMoney').show();
					$('.raise-dest .dd-money').height(90);
					$('.raise-dest #dMoney').val('').focus();
					$('.raise-dest .total').html('合计: <span style="color:#ff9001;">0</span>元');
				} else {
					$('.raise-dest .diyMoney').hide();
					$('.raise-dest .dd-money').height(50);
					$('.raise-dest .total').html('目标不限');
				}
			});

			$('.dd-donate-money .close').on('click', function() {
				if($('.tishi').is(':hidden')){
					$('#overLay').hide();
				}
				$('.rd-donate').hide();
			});

			$('.tishi2 .close').on('click', function() {
				$('#overLay').hide();
				$('.tishi2').hide();
			});

			if($('.lookmore').length > 0) {
				that.lookmore();
			}
			
			if($('.rd-head .head-txt p').height() <= 32) {
				$('.rd-head .head-txt p').css({
					'text-align': 'center'
				});
			} else {
				$('.rd-head .head-txt p').css({
					'text-align': 'left'
				});
			}
			
			$('.dd-prop :checkbox').on('change',function(){
				if(this.checked){
					$('.payBtn').removeClass('disable');
				}else{
					$('.payBtn').addClass('disable');
				}
			});
			
			$(".aload").on("click", function() {
				that.loadRecordList(++page, 10);
			});
		},
		delPic: function(delV, obj) {
			var that = this;
			$.ajax({
				url: dataUrl.delImg,
				data: {
					images: delV,
					t: new Date().getTime()
				},
				success: function(result) {
					if(result.flag == 1) {
						obj.parent().remove();
						$('#imgList .add').show();
					} else if(result.flag == -1) {
						en.show(that.delPic);
					} else {
						layer.msg(result.errorMsg);
						return false;
					}
				}
			});
			return true;
		},
		photoPic: function(file) {
			var that = this,
				filepath = $(file).val();
			var extStart = filepath.lastIndexOf(".") + 1;
			var ext = filepath.substring(extStart, filepath.length).toUpperCase();
			if(ext != 'JPG' && ext != 'PNG' && ext != 'JPEG' && ext != 'GIF' && ext != 'BMP') {
				d.alert({
					content: "上传图片仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！",
					type: 'error'
				});
				return false;
			}
			var file_size = 0;
			if(!$.browser.msie) {
				file_size = file.files[0].size;
				var size = file_size / 1024;
				if(size > 2048) {
					d.alert({
						content: "上传的图片大小不能超过2M！",
						type: 'error'
					});
					return false;
				}
			}
			return true;
		},
		ajaxForm: function(obj) {
			var that = this;
			$(obj).ajaxForm({
				beforeSend: function() {
					status.empty();
				},
				uploadProgress: function(event, position, total, percentComplete) {},
				success: function(data) {
					data = data.replace("<PRE>", "").replace("</PRE>", "");
					var json = eval('(' + data + ')');
					var imgBox = $('#imgList'),
						addObj = imgBox.children(".add");
					addObj.hide();
					if(json.result == 1) {
						imgBox.children('.old').remove();
						return false;
					} else {
						var imgBox = $('#imgList'),
							addObj = imgBox.children(".add");
						//that.photoData.push(json.imageId);
						imgBox.children('.old').attr('id', json.imageId).children('img').attr('src', json.imageUrl).end();
						imgBox.children('.old').find('.imgCon').attr('id', 'imgCon' + json.imageId);
						imgBox.children('.old').removeClass('old');
						imgBox.find(".item").not(".add").append('<i>&times;</i>');
					}
				},
				error: function(data) {
					var imgBox = $('#imgList'),
						addObj = imgBox.children(".add");
					addObj.show();
					imgBox.children('.old').remove();
				}
			});
		},
		page: function(pageSize, pageNum, type, flag) {
			$.ajax({
				url: dataUrl.newsList,
				data: {
					pageNum: pageNum,
					pageSize: pageSize,
					type: type,
					t: new Date().getTime()
				},
				success: function(result) {}
			});
		},
		tips: function(text) {
			var that = this;
			if(!$('.tips').is(':hidden')) {
				if($('.tips').css('bottom') < 100) {
					return;
				} else {
					clearTimeout(that.timer);
					that.timer = setTimeout(that.hide, 2000);
					return;
				}
			}
			$('.tips').text(text).show().animate({
				bottom: '100px'
			}, 300, function() {
				clearTimeout(that.timer);
				that.timer = setTimeout(that.hide, 2000);
			});
		},
		hide: function() {
			$('.tips').fadeOut(function() {
				$('.tips').css({bottom: '-40px'});
			});
		},
		lookmore: function() {
			var $p = $('.rd-project .project-content');
			if($p) {
				var content = $p.html();
				var smallCon = $p.html().trim();
				if(smallCon.length<220){
					smallCon = smallCon;
				}else{
					smallCon = smallCon.substr(0,221)+'...';
				}
				if($p.parent().height() >= 150) {
					$p.html(smallCon);
				}else{}
				$('.lookmore').on('click', function() {
					if($(this).hasClass('lookless')) {
						$p.html(smallCon);
						$('.rd-project .xqImg').removeClass('on');
						$(this).removeClass('lookless').text('查看全文');
						$(this).parent().addClass('detail-mask-bg');
						$('.rd-project')[0].scrollIntoView();
					} else {
						$p.html(content);
						$('.rd-project .xqImg').addClass('on');
						$(this).addClass('lookless').text('收起');
						$(this).parent().removeClass('detail-mask-bg');
					}
				});
			}
		},
		loadRecordList: function(page, pageNum) {
			var that = this,
				projectId = $('#projectId').val(),
				flag = false;
			if($(".aload").text()!="没有更多数据了"){
				if(page == 1) {
					flag = true;
				}
				$.ajax({
					url: dataUrl.donationlist,
					data: {
						userId: $('#shareUserId').val(),
						id: projectId,
						page: page,
						pageNum: 10,
						t: new Date().getTime()
					},
					success: function(result) {
						var leavewords = result.obj1;
						if(result.flag == 1) {
							if(result.obj.data.length == 0){
								$(".aload").html('暂无数据~').css({color:'#999'});
							}else if(result.obj.data.length < 10) {
								$(".aload").html("没有更多数据了").css({color:'#999'});
							}
							if(result.obj.data.length > 0){
								var data = result.obj,
									total = data.total,
									pageNum = data.pageNum,
									datas = data.data,
									len = datas.length,
									html = [];
								len = len > pageNum ? pageNum : len;
								var html = [];
								for(var i = 0; i < len; i++) {
									html.push('<a href="javascript:void(0);"><div class="item record">');
									html.push('<span class="thumb">');
									if(datas[i].imagesurl != null) {
										html.push('<img class="fl" src="' + datas[i].imagesurl + '">');
									} else {
										html.push('<img class="fl" src="' + base + 'res/images/detail/people_avatar.jpg">');
									}
									html.push('</span><div class="name">');
									html.push('<p class="name_p1">' + $.ellipsis(datas[i].name, 7, "..") + '</p><p class = "name_p2"> 捐 <label> ￥' + $.formatNum(datas[i].dMoney) + ' </label></p> ');
									html.push('</div><div class="money">');
									html.push('<p class="money_p1">' + (datas[i].leaveWord == null ? "" : datas[i].leaveWord) + '</p>');
									html.push('<div class="clear"></div>');
									//增加留言信息
									var itemss = leavewords[i];
									html.push('<div class="timeMessagej">');
									if(itemss.length > 0) {
										html.push('<span class="timeSpj">' + datas[i].showTime + '</span>');
										html.push('<div class="messagej"><span class="messageSpj" onclick="donateLeaveWord(' + datas[i].id + ',' + i + ')"></span>');
										html.push('</div></div>');
									} else {
										html.push('<span class="timeSpj">' + datas[i].showTime + '</span>');
										html.push('<div class="messagej"><span class="messageSpj" onclick="donateLeaveWord(' + datas[i].id + ',' + i + ')"></span>');
										html.push('</div></div>');
									}
									if(itemss.length > 0) {
										html.push('<div class="message_boardj  message_listj' + i + '">')
									} else {
										html.push('<div class="message_boardj  message_listj' + i + '" style="background:none">')
									}
									for(var j = itemss.length - 1; j >= 0; j--) {
										if(itemss[j].replyUserId == null) {
											html.push('<p class="board_textj" ><span onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + i + ')" >' + itemss[j].leavewordName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
										} else {
											html.push('<p class="board_textj" ><span  onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + i + ')" >' + itemss[j].leavewordName + '<b class="hf">回复</b>' + itemss[j].replyName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
										}
									}
									if(itemss.length > 0) {
										html.push('<span  class="danoteMore' + i + ' danote1" onclick="donateLoadMore(' + i + ',' + datas[i].id + ',' + itemss[0].total + ')">查看更多</span>');
									} else {
										html.push('<span  class="danoteMore' + i + ' danote1" onclick="donateLoadMore(' + i + ',' + datas[i].id + ',' + 0 + ')">查看更多</span>');
									}
									html.push('</div>');
									html.push('</div><div style="clear:both"></div>');
									html.push('</div><div class="clear"></div></div>');
									html.push('</a>');
								}
								$('.rd-record .record-list-box').append(html.join(''));
								//是否显示更多
								for(var k = 0; k < datas.length; k++) {
									itemsss = leavewords[k];
									if(itemsss.length > 0) {
										if(itemsss[0].total <= (itemsss[0].currentPage - 1) * 20 + itemsss.length) {
											$('.danoteMore' + k).hide();
										} else {
											$('.danoteMore' + k).html("查看更多");
										}
									} else {
										$('.danoteMore' + k).hide();
									}
								}
							}
						}
					}
				});
			}
		},
		//提交评论
		verify: function() {
			var that = this,
				projectId = $('#projectId').val(),
				projectDonateId = $('#projectDonateId').val(),
				projectFeedbackId = $('#projectFeedbackId').val(),
				leavewordUserId = $('#leavewordUserId').val(),
				replyUserId = $('#replyUserId').val(),
				leavewordName = $('#leavewordName').val(),
				replyName = $('#replyName').val(),
				content = $('#content').val();
			var index = $("#index").val();
			if(content == '') {
				layer.msg('请输入评论！');
				return false;
			}
			if(projectDonateId == '' || projectDonateId == undefined) {
				projectDonateId = null;
			}
			if(projectFeedbackId == '' || projectFeedbackId == undefined) {
				projectFeedbackId = null;
			}
			if(leavewordUserId == '' || leavewordUserId == undefined) {
				leavewordUserId = null;
			}
			if(replyUserId == '' || replyUserId == undefined) {
				replyUserId = null;
			}
			if(leavewordName == '' || leavewordName == undefined) {
				leavewordName = null;
			}
			if(replyName == '' || replyName == undefined) {
				replyName = null;
			}
			$.ajax({
				url: dataUrl.User_replyLeaveWord,
				data: {
					projectId: projectId,
					projectDonateId: projectDonateId,
					projectFeedbackId: projectFeedbackId,
					leavewordUserId: leavewordUserId,
					replyUserId: replyUserId,
					leavewordName: leavewordName,
					replyName: replyName,
					content: content
				},
				success: function(result) {
					if(result.errorCode == "0000") { //成功
						$(".moved_up").hide();
						$("#overLay").hide();
						//回复或者发表评论成功之后需要清空页面隐藏的值
						$('#replyUserId').val("");
						$('#leavewordUserId').val("");
						$('#leavewordName').val("");
						$('#replyName').val("");
						$('#content').val("");
						$('#projectDonateId').val("");
						$('#projectFeedbackId').val("");
						//判断回复之后刷新
						if(projectDonateId == null && projectFeedbackId != null && projectFeedbackId != "") {
							$.ajax({
								url: base + "h5ProjectDetails/refleshLeaveWord.do",
								data: {
									type: 0,
									projectId: projectId,
									projectFeedbackId: projectFeedbackId,
									currentPage: 1
								},
								success: function(result) {
									if(result.flag == "1") {
										//成功
										var html = [], itemss = result.obj;
										for(var j = itemss.length - 1; j >= 0; j--) {
											if(itemss[j].replyUserId == null) {
												html.push('<p class="board_text" onclick="reply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectFeedbackId + ',' + index + ')"><span>' 

+ itemss[j].leavewordName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
											} else {
												html.push('<p class="board_text" onclick="reply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectFeedbackId + ',' + index + ')"><span>' 

+ itemss[j].leavewordName + '<b class="hf">回复</b>' + itemss[j].replyName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
											}
										}
										//判断是否显示加载更多
										if(itemss.length > 0) {
											// $('.messageTs'+index).html(itemss[0].total);
											//回复之后回复的总的条数+1
											if(itemss[0].total <= (itemss[0].currentPage - 1) * 20 + itemss.length) {
												$('.more' + index).hide();
											} else {
												// $('.more'+index).html("查看更多");
												html.push('<span class="more' + index + '  danote1" onclick="loadMore(' + index + ',' + projectFeedbackId + ',' + 0 + ')">查看更多</span>');
											}
										}
										//追加数据
										$('.message_list' + index).html(html.join(''));
										$('.message_list' + index).css("background", "#f3f3f3");

									} else { //失败
										layer.msg(result.errorMsg);
										return;
									}
								}
							});
						} else if(projectDonateId != null && projectFeedbackId == null && projectDonateId != "") {
							$.ajax({
								url: base + "h5ProjectDetails/refleshLeaveWord.do",
								data: {
									type: 0,
									projectId: projectId,
									projectDonateId: projectDonateId,
									currentPage: 1
								},
								success: function(result) {
									if(result.flag == "1") {
										//成功
										var html = [], itemss = result.obj;
										for(var j = itemss.length - 1; j >= 0; j--) {
											if(itemss[j].replyUserId == null) {
												html.push('<p class="board_textj" ><span onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + index + 

')" >' + itemss[j].leavewordName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');

											} else {
												html.push('<p class="board_textj" ><span  onclick="donateReply(' + itemss[j].leavewordUserId + ',' + itemss[j].projectDonateId + ',' + index 

+ ')" >' + itemss[j].leavewordName + '<b class="hf">回复</b>' + itemss[j].replyName + '：</span>' + itemss[j].content + '</p><div class="clear"></div>');
											}
										}

										//判断是否显示加载更多
										if(itemss.length > 0) {
											// $('.messageTsj'+index).html(itemss[0].total);
											if(itemss[0].total <= (itemss[0].currentPage - 1) * 20 + itemss.length) {
												$('.danoteMore' + index).hide();
											} else {
												//$('.danoteMore'+index).html("查看更多");
												html.push('<span  class="danoteMore' + index + ' danote1" onclick="donateLoadMore(' + index + ',' + projectDonateId + ',' + 0 + ')">查看更多</span>');
											}
										}
										//追加数据
										$('.message_listj' + index).html(html.join(''));
										$('.message_listj' + index).css("background", "#f3f3f3");

									} else { //失败
										layer.msg(result.errorMsg);
										return;
									}
								}
							});
						}
					} else if(result.errorCode == "0001") { //未登录，账号密码登录
						layer.msg('您还未登录！');
						setTimeout(function() {
							location.href = '/ucenter/user/Login_H5.do?raiseDetail_'+projectId;
						}, 2000);

					} else { //失败
						layer.msg(result.errorMsg);
						return;
					}
				}
			});
		}
	};
	together.init();
});