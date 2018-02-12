<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>我要行善</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/mui.min.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/list.css" />
</head>
<style type="text/css">
	#wrapper {
		position: absolute;z-index: 1;top: 40px;bottom: 0;left: 0;
		width: 100%;overflow: auto;
	}
	#scroller {
		-webkit-tap-highlight-color: rgba(0, 0, 0, 0);float: left;width: 100%;padding: 0;
	}
	#pullDown, #pullUp {
		font-weight: bold;font-size: 12px;text-align: center;
	}
	.tips {
		width: 40%;height: 30px;line-height: 30px;background: rgba(0, 0, 0, .8);margin: 0 30%;
		position: fixed;top: -30px;z-index: 99;text-align: center;color: #fff;
	}
</style>

<body>
	<div id="pageContainer">
		<div class="project_list">
			<!--标题//-->
			<!--主题筛选//-->
			<div class="filter">
				<div class="hd">
					<a href="javascript:void(0);" class="item1 oncur">全部分类</a>
					<a href="javascript:void(0);" class="item2">最新发布</a>
					<a href="javascript:void(0);" class="item3">全部状态</a>
				</div>
				<div class="bd" id="sub_1" style="display:none;">
					<ul>
						<li>
							<a href="javascript:void(0);" data="0" class="cur">全部</a>
						</li>
						<c:forEach var="record" items="${atc}" varStatus="status">
							<c:choose>
								<c:when test="${record.typeName_e != 'good'}">
									<li>
										<a href="javascript:void(0);" data="${record.typeName_e}">${record.typeName}</a>
									</li>
								</c:when>
								<c:otherwise>
									<li style="display:none">
										<a href="javascript:void(0);" data="${record.typeName_e}">${record.typeName}</a>
									</li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</ul>
				</div>
				<div class="bd" id="sub_2" style="display:none;">
					<ul>
						<li>
							<a href="javascript:void(0);" data="2" class="cur">最新发布</a>
						</li>
						<li>
							<a href="javascript:void(0);" data="1">关注最多</a>
						</li>
						<li>
							<a href="javascript:void(0);" data="4">捐助最多</a>
						</li>
						<li>
							<a href="javascript:void(0);" data="3">最新反馈</a>
						</li>
					</ul>
				</div>
				<div class="bd" id="sub_3" style="display:none;">
					<ul>
						<li>
							<a href="javascript:void(0);" data="0" class="cur">全部项目</a>
						</li>
						<li>
							<a href="javascript:void(0);" data="1">发布中</a>
						</li>
						<li>
							<a href="javascript:void(0);" data="2">已完成</a>
						</li>
					</ul>
				</div>
				<div class="ft" style="display:none;">
					<a href="" class="cancel">取消</a>
				</div>
				<div class="clear"></div>
			</div>

			<style>
				#scroller ul li {
					height: 100px;width: 100%;
				}
			</style>
			<!--主题列表//-->
			<div id="wrapper">
				<div id="scroller" class="mui-content mui-scroll-wrapper">
					<div class="list mui-scroll" id="project_list"></div>
					<div id="pullUp">
						<span class="pullUpIcon"></span><span class="pullUpLabel"></span>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div class="tips">没有更多内容了！</div>
	<input type="hidden" id="extensionPeople" value="" />
	<input type="hidden" id="field" value="" />
	<input type="hidden" id="tag" value="${tag }" />
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/res/js/h5/mui.min.js"></script>
	<script src="/res/js/h5/iscroll.js"></script>
	<script data-main="/res/js/h5/project_list.js?v=20171117" src="/res/js/require.min.js"></script>

	<script>
		mui.init({
			pullRefresh: {
				container: '#scroller',
				down: {
					callback: pulldownRefresh
				},
				up: {
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了',
					callback: pullupRefresh
				}
			}
		});
		mui('#project_list').on('tap', 'a', function() {
			window.location.href = this.href;
		});
		/**
		 * 下拉刷新具体业务实现
		 */
		var count = 1,
			flag = false,
			tip = 1;

		function pulldownRefresh() {
			setTimeout(function() {
				tip = 1;
				count = 1;
				$("#project_list").children("a").remove();
				flag = false;
				h5detail.projectList(1, 9);
				mui('#scroller').pullRefresh().endPulldownToRefresh(); //refresh completed
			}, 1500);
		}
		/**
		 * 上拉加载具体业务实现
		 */
		function pullupRefresh() {
			tip = 2;
			setTimeout(function() {
				h5detail.projectList(++count, 9);
			}, 1500);
		}
	</script>

	<script type="text/javascript">
		var height = $(".filter").outerHeight(true);
		var base = window.baseurl;
		var i = 1;
		var dataUrl = {
			projectList: base + '/project/list_h5.do',
			latestdonationlist: base + '/project/latestdonationlist.do'
		};
		var extensionPeople = $("#extensionPeople").val();
		var h5detail = {
			totle: 0,
			pageCurrent: 1,
			init: function() {
				var that = this;
				var field = $('#field').val();
				if(field) {
					$('#sub_1 a').removeClass('cur').eq(field).addClass('cur');
					$('.item1').html($('#sub_1 a').eq(field).html());
				}
				that.projectList(1, 9);
				$('.item1').click(function() {
					$(this).parent().children().removeClass("oncur");
					$(this).addClass("oncur");
					if($('#sub_1').is(':visible')) {
						$('#sub_1').hide();
					} else {
						$('#sub_1').slideDown();
						$('#sub_2').hide();
						$('#sub_3').hide();
					}
					height = $(".filter").outerHeight(true) + 2;
					$("#wrapper").css("top", height + 'px');
				});
				$('.item2').click(function() {
					$(this).siblings().removeClass("oncur");
					$(this).addClass("oncur");
					if($('#sub_2').is(':visible')) {
						$('#sub_2').hide();
					} else {
						$('#sub_2').slideDown();
						$('#sub_1').hide();
						$('#sub_3').hide();
					}
					height = $(".filter").outerHeight(true) + 2;
					$("#wrapper").css("top", height + 'px');
				});
				$('.item3').click(function() {
					$(this).siblings().removeClass("oncur");
					$(this).addClass("oncur");
					if($('#sub_3').is(':visible')) {
						$('#sub_3').hide();
					} else {
						$('#sub_3').slideDown();
						$('#sub_1').hide();
						$('#sub_2').hide();
					}
					height = $(".filter").outerHeight(true) + 2;
					$("#wrapper").css("top", height + 'px');
				});
				$('#sub_1 li').click(function() {
					flag = false;
					count = 1;
					mui('#scroller').pullRefresh().scrollTo(0, 0, 100);
					mui('#scroller').pullRefresh().refresh(true); //重置上拉加载
					var i = $('#sub_1 li').index(this);
					$('#sub_1 a').removeClass('cur').eq(i).addClass('cur');
					$('#sub_1').hide();
					$('.item1').html($('#sub_1 a').eq(i).html());
					$("#project_list").children("a").remove();
					that.projectList(1, 9);
					$("#wrapper").css("top", "39px");
				});
				$('#sub_2 li').click(function() {
					flag = false;
					count = 1;
					mui('#scroller').pullRefresh().scrollTo(0, 0, 100);
					mui('#scroller').pullRefresh().refresh(true);
					var i = $('#sub_2 li').index(this);
					$('#sub_2 a').removeClass('cur').eq(i).addClass('cur');
					$('#sub_2').hide();
					$('.item2').html($('#sub_2 a').eq(i).html());
					$("#project_list").children("a").remove();
					that.projectList(1, 9);
					$("#wrapper").css("top", "39px");
				});
				$('#sub_3 li').click(function() {
					flag = false;
					count = 1;
					mui('#scroller').pullRefresh().scrollTo(0, 0, 100);
					mui('#scroller').pullRefresh().refresh(true);
					var i = $('#sub_3 li').index(this);
					$('#sub_3 a').removeClass('cur').eq(i).addClass('cur');
					$('#sub_3').hide();
					$('.item3').html($('#sub_3 a').eq(i).html());
					$("#project_list").children("a").remove();
					that.projectList(1, 9);
					$("#wrapper").css("top", "39px");
				});
			},
			projectList: function(pageNum, pageSize) {
				var nt = new Date().getTime();
				var sub1 = $('#sub_1 a').index($('#sub_1 a.cur')),
					sub2 = $('#sub_2 a').index($('#sub_2 a.cur')),
					sub3 = $('#sub_3 a').index($('#sub_3 a.cur'));
				var field = $('#sub_1 a').eq(sub1).attr("data"),
					orderby = $('#sub_2 a').eq(sub2).attr("data"),
					state = $('#sub_3 a').eq(sub3).attr("data"),
					fieldName = $('#sub_1 a').eq(sub1).html();
				if(field == "0") {
					field = null;
				}
				$('#title').html(fieldName);
				$.ajax({
					url: dataUrl.projectList,
					data: {
						typeName: field,
						sortType: orderby,
						status: state,
						page: pageNum,
						len: pageSize,
						t: new Date().getTime()
					},
					success: function(res) {
						var html = [];
						if(pageNum == 1 && res.total <= pageSize) {
							mui('#scroller').pullRefresh().disablePullupToRefresh();
						} else {
							mui('#scroller').pullRefresh().enablePullupToRefresh();
						}
						if(res.result != 1 && res.result != 2) {
							var datas = res.items,
								len = datas.length;
							for(var i = 0; i < len; i++) {
								if(datas[i].field == 'garden') {
									html.push('<a href="http://www.17xs.org/project/gardenview_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + extensionPeople + '&ts='+nt+'">');
								} else {
									html.push('<a href="http://www.17xs.org/project/view_h5.do?projectId=' + datas[i].itemId + '&extensionPeople=' + extensionPeople + '&ts='+nt+'">');
								}
								html.push('<div class="item bg2">');
								if(datas[i].imageurl != null) {
									html.push('<img class="fl" src="' + datas[i].imageurl + '">');
								} else {
									html.push('<img class="fl" src="' + base + 'res/images/logo-def.jpg">');
								}
								if(datas[i].donaAmount >= datas[i].cryMoney) {
									html.push('<i class="corner"><img src="' + base + 'res/images/h5/images/corner_success.png"></i>');
								} else if(datas[i].state == 260) {
									html.push('<i class="corner"><img src="' + base + 'res/images/h5/images/corner_finish.png"></i>');
								}
								html.push('<div class="t"><div class="tn"></div>');
								if(datas[i].title.length > 20) {
									html.push('<div class="tt">' + datas[i].title.substr(0, 20) + '...</div>');
								} else {
									html.push('<div class="tt">' + datas[i].title + '</div>');
								}
								html.push('<div class="tp"><label>目标<span class="r">' + datas[i].cryMoney + '</span>元</label>');
								html.push('<label>已完成<span class="r">' + datas[i].process + '%</span></label></div>');
								html.push('</div><div class="clear"></div></div></a>');
							}
							$('#project_list').append(html.join('')).append($(".mui-pull-bottom-pocket"));
						} else if(tip == 1) {
							$(".tips").animate({"top": "40px"}, 500, function() {
								var _this = $(this);
								setTimeout(function() {
									_this.fadeOut(function() {
										_this.css({"top": "-30px"}).show();
									});
								}, 2000);
							});
						}
						if(pageNum * pageSize >= res.total) {
							flag = true;
						}
						mui('#scroller').pullRefresh().endPullupToRefresh(flag); //参数为true代表没有更多数据了。
					}
				});
			},
		};

		h5detail.init();
		setTimeout(function() {
			document.getElementById('wrapper').style.left = '0';
		}, 800);

		document.addEventListener('touchmove', function(e) {
			e.preventDefault();
		}, false);
	</script>
	<%@ include file="./../cs.jsp" %>
	<%CS cs = new CS(1257726653);cs.setHttpServlet(request,response);
	String imgurl = cs.trackPageView();%>
	<img src="<%= imgurl %>" width="0" height="0" />
	<!--<img src="http://c.cnzz.com/wapstat.php?siteid=1257726653&r=&rnd=675978807" width="0" height="0"  />-->
</body>

</html>