define(['jquery', 'util'], function($, u) {
	var d = {
		init: function() {
			$('body').on('click', '.closeDialog', function() {
				var i = $(this).attr('data');
				d.close(i);
				if (d._flag) {
					d._flag = false;
				}
			}).on('mousedown', '.dialogTitle', function(event) {
				var dragObj = $(this).parent(),
					pos = {
						top: dragObj.position().top,
						left: dragObj.position().left
					},
					oh = dragObj.outerHeight(),
					ow = dragObj.outerWidth();
				pos = {
					top: event.clientY - pos.top,
					left: event.clientX - pos.left
				};
				$(this).mousemove(function(event) {
					try {
						if (window.getSelection) {
							window.getSelection().removeAllRanges();
						} else {
							document.selection.empty();
						}
					} catch (e) {}
					var s = u.screen(),
						maxTop = s.sh,
						maxLeft = s.sw,
						top = Math.max(event.clientY - pos.top, 0),
						left = Math.max(event.clientX - pos.left, 0);
					dragObj.css({
						top: Math.min(top, maxTop - oh),
						left: Math.min(left, maxLeft - ow)
					});
				}).mouseup(function() {
					$(this).off('mousemove');
					$(this).off('mouseup');
				});
			});
		},
		_set: function(_obj, i) {
			var s=u.screen(),docST=s.top,viewH=s.h,viewW=s.w,_objH=_obj.height(),_objW=_obj.width();
			var dialogLeft=(viewW-_objW)/2,
			 	dialogTop=_objH >= viewH ? 20 :(viewH-_objH)/2+docST;
			  
			_obj.css({
				top: dialogTop,
				left: dialogLeft
			});
			$("#cp2yLock" + i).css({
				"width": s.sw,
				"height":s.sh
			}).show(); 
		},
		_flag: false,
		alert: function(o) {
			if (this._flag) { return false; }
			var i = $(".cp2yLock").size(); //中间有关闭 i不唯一 加一个 index 来记录
			window.dialogLock = true;
			this._lock(i);
			if (!o.type) {
				o.type = 'warn';
			}
			var k = {t: '提示信息',c: ''},
				c = '<div class="alertCon"><span class="' + o.type + '">' + o.content + '</span></div><div class="Btns">';
			if (o.link) {
				c += '<a href="../../../js/' + o.link + '" id="frameBtn' + i + '" target="_blank" class="closeDialog btn1" data=' + i + '>确定</a></div>';
			}else if(o.jp){
				c += '<a id="frameBtn' + i + '" class="closeDialog sureBtn" data=' + i + '>确定</a><a class="closeDialog" data="0">取消</a></div>';
			} else {
				c += '<a id="frameBtn' + i + '" class="closeDialog sureBtn" data=' + i + '>确定</a></div>';
			}
			k.c = c;
			if (o.okFn) { 
				k.okFn = o.okFn; 
			} 
			var cName="AlertDlg";
			if(!!o.cName){ cName+=" "+o.cName; }
			this._open(k, i, o.css,cName);
		},
		open: function(o, css, cName) {
			var i = $(".cp2yLock").size();
			window.dialogLock = true;
			this._lock(i);
			this._open(o, i, css, cName);
		},
		frame: function(o) {
			var i = $(".cp2yLock").size();
			window.dialogLock = true;
			this._lock(i);
			var css = {
				width: "584px",
				height: "391px"
			},x;
			if (o.css) {
				css.width = o.css.width;
				css.height = (o.css.height + 92);
			}
			x = {
				t: o.title,
				c: '<div style="height:' + (css.height - 92) + 'px"><iframe src="../../../js/' + o.url + '" class="iframe" frameborder="0" scrolling="no" marginwidth="0" marginheight="0"></iframe></div>'
			}
			if (!o.btns) {
				x.c += '<div class="Btns">';
				if (o.ok) {
					x.c += '<a id="frameBtn' + i + '" class="closeDialog btn1" data=' + i + '>' + o.ok + '</a>';
					x.okFn = o.okFn;
				}
				if (o.cancel) {
					x.c += '<a class="closeDialog btn1" data=' + i + '>' + o.cancel + '</a>';
				}
				x.c += '</div>';
			}
			this._open(x, i, css, false);
		},
		close: function(i) {
			if (isNaN(i)) {
				i = $("." + i).attr("data");
			}
			$("#cp2yLock" + i).remove();
			$("#cp2yDialogBox" + i).remove();
			if ($(".cp2yLock").size() == 0) {
				window.dialogLock = false;
			}
		},
		_lock: function(i) {
			$('body').append('<div class="cp2yLock" id="cp2yLock' + i + '"></div>');
		},
		_open: function(o, i, css, cName) {
			var that = this,d = [],gapH=80;//80:titH:40 padding:20px; 
			if (!!o.t) {
				d.push('<div class="dialogTitle"><i class="l"></i><span>' + o.t + '</span><a class="closeDialog" data="' + i + '"></a><i class="r"></i></div>');
			}else{ 
				gapH=40;
			}
			d.push('<div class="dialogContent">' + o.c + '</div>');
			$('body').append('<div class="cp2yDialogBox cp2yDialogBox1" data="' + i + '" id="cp2yDialogBox' + i + '"></div>');
			var ob = $("#cp2yDialogBox" + i);
			if (cName) {
				ob.addClass(cName);
			}
			if (css) {
				ob.css(css);
			}
			ob.html(d.join('')).show();
			var obH = ob.height(),
				conObj = ob.children(".dialogContent"); //11x5add
				//conObj.height(obH - gapH + "px");  
			if (o.okFn) {
				$('#frameBtn' + i).off().on('click', function() {
					o.okFn();
				});
			}
			u.throttle(this._set(ob, i), 50, 100);
			$(window).resize(function() {
				if (window.dialogLock) {
					u.throttle(that._set(ob, i), 50, 100);
				}
			});
		}
	};
	return d;
});