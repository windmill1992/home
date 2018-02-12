define(["jquery"],function($){
	$.extend({
		chartLen:function(str){
			str=str.toString();
			var count=0,len=str.length; 
			for(var i=0;i<len;i++){ 
				if(str[i].match(/[^\x00-\xff]/)){ 
					count = count+2; 
				}else{ 
					count = count+1; 
				} 
			} 
			return count;  
		},
		formatNum:function(num){//,splt 
		    var Decimal='',Integer='',str=String(num);
			if(str.indexOf(".")!=-1){
				Decimal=str.substring(str.indexOf("."));
				if(Decimal.length==2){
					Decimal=Decimal+0;
				}else{
					Decimal=Decimal.substr(0,3);
				}
				Integer=str.substring(0,str.indexOf("."));
			}else{
				Integer=num;
				Decimal='.00';
			}
		    num=Number(Integer);
			if(num!=0&&!isNaN(num)){
				num=""+num;
				var result=[],len=num.length,subI=0; 
				for(var i=0;i<len;i++){
					if(i%3==0){
						subI=i/3;
						var temp=num.substring(len-subI*3,len-subI*3-3); 
						result.push(temp);
					} 
				} 
				return result.reverse().join(',')+Decimal;
			}
			return 0+Decimal;
		},
		formatNum1:function(num){//,splt 
		    var Decimal='',Integer='',str=String(num);
			if(str.indexOf(".")!=-1){
				Decimal=str.substring(str.indexOf("."));
				if(Decimal.length==2){
					Decimal=Decimal+0;
				}else{
					Decimal=Decimal.substr(0,3);
				}
				Integer=str.substring(0,str.indexOf("."));
			}else{
				Integer=num;
				Decimal='.00';
			}
		    num=Number(Integer);
			if(num!=0&&!isNaN(num)){
				num=""+num;
				var result=[],len=num.length,subI=0; 
				
				return num+Decimal;
			}
			return 0+Decimal;
		},
		toNumber:function(v){
			var s = function(v){
				v = Number(v);
				return v<10 ? '0'+v : v;
			}
			if(!v||v.length==0)return '';
			if($.isArray(v)){
				var a = [];
				$(v).each(function(i,val){a.push(s(val));});
				return a;
			}
			return s(v);
		},
		count:function(o){//对象属性个数/长度 
			var t = typeof o;
			if(t == 'string'){
					return o.length;
			}else if(t == 'object'){
				var n = 0;
				for(var i in o){
						n++;
				}
				return n;
			}
			return false;
		},
		Subtr:function(arg1,arg2){//相减保留两位
		 var r1,r2,m,n;
		 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		 m=Math.pow(10,Math.max(r1,r2));
		 //last modify by deeka
		 //动态控制精度长度
		 n=(r1>=r2)?r1:r2;
		 return ((arg1*m-arg2*m)/m).toFixed(n);
		},
		contain:function(erArr,edArr){//erArr:container,edArr:contained  //数组是否为包含关系
			var erLen=erArr.length,edLen=edArr.length,i=0,sameLen=0;
			
			for(;i<edLen;i++){ 
				for(var j=0;j<erLen;j++){
					if(edArr[i]==erArr[j]){
						sameLen+=1;
						break;
					}else{
						continue;
					}
				} 
			} 
			return sameLen==edLen;
		}, 
		ellipsis:function(obj,num,replaceObj){
		replaceObj==undefined ?replaceObj='...':'';
		var data,obj=$.trim(obj),len=obj.length;
		$.cut_str(obj,num)?data=$.cut_str(obj,num)+replaceObj:data=obj;
		//len>num?data=obj.substr(0,num)+replaceObj:data=obj;		
		return data;	
	    },
		cut_str:function(str, len){
			var char_length = 0;
			for (var i = 0; i < str.length; i++){
				var son_str = str.charAt(i);
				encodeURI(son_str).length > 2 ? char_length += 1 : char_length += 0.5;
				if (char_length >= len){
					var sub_len = char_length == len ? i+1 : i;
					return str.substr(0, sub_len);
					break;
				}
			}
		},
		isZhiShu:function(n){
			if(n<1){ return false;}
			for(var i=2;i<n;i++){
				if(n%i==0){ return false;}
			}
			return true;
		}
	});
	
	//DOM 
	$.extend($.fn,{
		formatInt:function(){
			var v= $(this).val();
			if(!!v){
				return v.replace(/([^\d]|^0)/g,'').toInt(1);
			} 
			return 1;   
		}
	});
	
	/**
	* 对Date的扩展，将 Date 转化为指定格式的String
	* 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符
	* 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	* eg:
	* (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	* (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
	* (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
	* (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
	* (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
	*/
	$.extend(Date.prototype,{
		pattern : function(fmt){
			var o = {
			"M+" : this.getMonth()+1, //月份
			"d+" : this.getDate(), //日
			"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
			"H+" : this.getHours(), //小时
			"m+" : this.getMinutes(), //分
			"s+" : this.getSeconds(), //秒
			"q+" : Math.floor((this.getMonth()+3)/3), //季度
			"S" : this.getMilliseconds() //毫秒
			};
			var week = {
			"0" : "\u65e5",
			"1" : "\u4e00",
			"2" : "\u4e8c",
			"3" : "\u4e09",
			"4" : "\u56db",
			"5" : "\u4e94",
			"6" : "\u516d"
			};
			if(/(y+)/.test(fmt)){
				fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
			}
			if(/(E+)/.test(fmt)){
				fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);
			}
			for(var k in o){
				if(new RegExp("("+ k +")").test(fmt)){
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
				}
			}
			return fmt;
		}
	});
	//解决闭包
	Function.prototype.bind = function () {
		var __method = this;
		var args = Array.prototype.slice.call(arguments);
		var object = args.shift();
		return function () {
			return __method.apply(object, args.concat(Array.prototype.slice.call(arguments)));
		}
	}
	//==== Cookie Extent ====
	$.cookie={ 
		get: function(name) {
			if (document.cookie == null) {
				alert("没有document.cookie无法操作Cookie");
				return;
			}
			var tmpDate = document.cookie;
			var tmpStart = tmpDate.indexOf(name + "=");
			if (tmpStart == -1) {
				return null;
			}
			tmpStart += name.length + 1;
			var tmpEnd = tmpDate.indexOf(";", tmpStart);
			if (tmpEnd == -1) {
				return decodeURI(tmpDate.substring(tmpStart))
			};
			return decodeURI(tmpDate.substring(tmpStart, tmpEnd));
		},
		set: function(name, value, expires, path, domain, secure) {
			if (document.cookie == null) {
				alert("没有document.cookie无法操作Cookie");
				return;
			}
			var tmpCookie = name + "=" + encodeURI(value);
			if (expires != null) {
				tmpCookie += ";expires=" + expires.toGMTString();
			}
			if (path != null) {
				tmpCookie += ";path=" + path;
			}
			if (domain != null) {
				tmpCookie += ";domain=" + domain;
			}
			if (secure != null) {
				tmpCookie += ";secure=" + secure;
			}
			document.cookie = tmpCookie; 
		},
		remove: function(name, path, domain) {
			if (document.cookie == null) {
				alert("没有document.cookie无法操作Cookie");
				return;
			}
			var tmpCookie = name + "=null;expires=" + new Date(new Date().getTime() - 1000000000000).toGMTString();
			if (path != null) {
				tmpCookie += ";path=" + path;
			}
			if (domain != null) {
				tmpCookie += ";domain=" + domain;
			}
			document.cookie = tmpCookie;
		},
		clear: function(path, domain) {
			if (document.cookie == null) {
				alert("没有document.cookie无法操作Cookie");
				return;
			}
			var tmpCookie = document.cookie.split(";");
			var tmpName;
			for (var i = 0; i < tmpCookie.length; i++) {
				tmpName = tmpCookie[i].split("=")[0].strip();
				Cookie.remove(tmpName, path, domain);
			}
		} 
	 };
	
    //==== String Extend ====
	$.extend(String.prototype,{
		empty: function () { return this == null || this == '' || this.length == 0; },
		encode: function () { return encodeURIComponent(this); },
		decode: function () { return decodeURIComponent(this); },
		stripTags: function () { return this.replace(/<\/?[^>]+>/gi, ''); },
		extractScripts: function () { return (this.match(new RegExp('<script[^>]*>([\\S\\s]*?)<\/script>', 'img')) || []); },
		evalScripts: function () { $(this.extractScripts()).each(function (i, v) { eval(v.substring(v.indexOf('>') + 1, v.lastIndexOf('<'))); }); },
		strip: function () { return this.replace(/^\s+/, '').replace(/\s+$/, ''); },
		stripScripts: function () { return this.replace(new RegExp('<script[^>]*>([\\S\\s]*?)<\/script>', 'img'), ''); },
		trim: function () { return $.trim(this); },
		replaceAll: function (rgExp, replaceText) { var tmpStr = this; while (tmpStr.indexOf(rgExp) != -1) tmpStr = tmpStr.replace(rgExp, replaceText); return tmpStr; },
		toArray: function (s) { if (s) return this.split(s); var arr = []; for (var i = 0; i < this.length; i++) arr.push(this.substring(i, i + 1)); return arr; },
		toInt: function (val) { if (arguments.length == 0) val = 0; if (!this.isNumber()) { return val; } return Number(this); },
		toFloat: function (val) { var a = this; if (arguments.length == 0) val = 0; if (!a.replace('.', '').isNumber()) { return val; } return parseFloat(this); },
		isNumber: function () { return /^[0-9]+$/.test(this); },
		hasChinese: function () { return escape(this).indexOf("%u") != -1; },
		repChinese: function () { var a = this; $(['０', '１', '２', '３', '４', '５', '６', '７', '８', '９']).each(function (i, v) { a = a.replaceAll(v, i); }); return a; },
		isIDCard: function () { return /^(\d{15}|\d{18}|\d{17}(X|x))$/.test(this); },
		isEmail: function () { if (this == null || this.length == 0) return false; var pattern = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/; return pattern.test(this); },
		len: function () { return this.replace(/[^\x00-\xff]/g, "**").length; },
		cnLength: function () {
			var escStr = escape(this);
			var numI = 0;
			var escStrlen = escStr.length;
			for (i = 0; i < escStrlen; i++) {
				if (escStr.charAt(i) == '%') {
					if (escStr.charAt(++i) == 'u') {
						numI++;
					}
				}
			}
			return this.length + numI;
		} 
	});
	
	//==== Date Extend ====
	/**
	* 对Date的扩展，将 Date 转化为指定格式的String
	* 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符
	* 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	* eg:
	* (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	* (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
	* (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
	* (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
	* (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
	*/
  $.extend(Date.prototype, {
		toDate: function (s) { var t = this; if (!s) s = new Text(); s._(t.getFullYear()); s._('-'); if (t.getMonth() + 1 < 10) s._('0'); s._(t.getMonth() + 1); s._('-'); if (t.getDate() < 10) s._('0'); s._(t.getDate()); return s.ts(); },
		toTime: function (s) { var t = this; if (!s) s = new Text(); if (t.getHours() < 10) s._('0'); s._(t.getHours()); s._(':'); if (t.getMinutes() < 10) s._('0'); s._(t.getMinutes()); s._(':'); if (t.getSeconds() < 10) s._('0'); s._(t.getSeconds()); return s.ts(); },
		toDatetime: function () { var t = this, s = new Text(); t.toDate(s); s._(' '); t.toTime(s); return s.ts(); },
   		pattern: function(fmt){
			var o = {
				"M+" : this.getMonth()+1, //月份
				"d+" : this.getDate(), //日
				"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
				"H+" : this.getHours(), //小时
				"m+" : this.getMinutes(), //分
				"s+" : this.getSeconds(), //秒
				"q+" : Math.floor((this.getMonth()+3)/3), //季度
				"S" : this.getMilliseconds() //毫秒
			};
			var week = {"0" : "\u65e5","1" : "\u4e00","2" : "\u4e8c","3" : "\u4e09","4" : "\u56db","5" : "\u4e94","6" : "\u516d"};
			if(/(y+)/.test(fmt)){
				fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
			}
			if(/(E+)/.test(fmt)){
				fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);
			}
			for(var k in o){
				if(new RegExp("("+ k +")").test(fmt)){
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
				}
			}
			return fmt;
		}
	});
   //==== Array Extend ====
	$.extend(Array.prototype, {
		indexOf   : function(val){var pos=-1;$(this).each(function(i,v){if(v==val){pos=i;return;}});return pos;},
		del       : function(value){var pos = this.indexOf(value);if(pos==-1)return;this.splice(pos,1);return this;},
		max       : function(){var max;$(this).each(function(i,v){v = Number(v);max = i==0?v:(v>max?v:max);});	return max;},
		min       : function(){var min;$(this).each(function(i,v){v = Number(v);min = i==0?v:(v<min?v:min);});	return min;},
		hasRepeat : function(){var b = {};var a = this;for(var i=0,l=a.length; i<l&&!b[a[i]];b[a[i++]]=1);return i<l;	},
		clone     : function(){var a=[];for(var i=0;i<this.length;i++)a[i] = this[i];return a;},
		del       : function(value,isPos){var pos=!isPos?this.indexOf(value):value;if(pos==-1)return;this.splice(pos,1);return this;},
		inArray : function(a,arr){
			for(var i=0;i<arr.length;i++)
				if(a==arr[i])
					return true;
			return false;
		}, 
		random : function(o){
			o = $.extend({
				len    : 1,//号码长度
				repeat : false,//是否可以重复
				sort   : false,//是否需要排序
				zero   : false//是否需要补0
			},o);
			var s=this,a=[],no;
			var r = function(){return s[Math.round(Math.random()*(s.length-1))];}
			while(a.length!=o.len){
				no = r();
				if(!o.repeat && a.indexOf(no)!=-1)continue;
				a.push(no);
			}
			if(o.sort)
				a = a.sort(function(a,b){return a-b;});
			if(o.zero)
				a = $.toNumber(a);
			return a;
		},
		aSort :function(method){//数组随机排序
			function Sort(a,b){
				if(method == 0 || method == 1){
					if(a > b){if(method == 0){return 1}else{ return -1}}
					if(a < b){if(method == 0){return -1}else{ return 1}}
					else{return 0}
				}
				else if(method == 2){return Math.random() > .5 ? -1 : 1;}//用Math.random()函数生成0~1之间的随机数与0.5比较，返回-1或1
			}
			
			this.sort(Sort); 
		}
	});
	
	 //==== Math Extend ====
	 
	 $.fn.myScroll = function(options){
	//默认配置
	var defaults = {
		speed:40,  //滚动速度,值越大速度越慢
		rowHeight:24 //每行的高度
	};
	
	var opts = $.extend({}, defaults, options),intId = [];
	
	function marquee(obj, step){
	
		obj.find("ul").animate({
			marginTop: '-=1'
		},0,function(){
				var s = Math.abs(parseInt($(this).css("margin-top")));
				if(s >= step){
					$(this).find("li").slice(0, 1).appendTo($(this));
					$(this).css("margin-top", 0);
				}
			});
		}
		
		this.each(function(i){
			var sh = opts["rowHeight"],speed = opts["speed"],_this = $(this);
			intId[i] = setInterval(function(){
				if(_this.find("ul").height()<=_this.height()){
					clearInterval(intId[i]);
				}else{
					marquee(_this, sh);
				}
			}, speed);

			_this.hover(function(){
				clearInterval(intId[i]);
			},function(){
				intId[i] = setInterval(function(){
					if(_this.find("ul").height()<=_this.height()){
						clearInterval(intId[i]);
					}else{
						marquee(_this, sh);
					}
				}, speed);
			});
		
		});

	}
	 
  return $;
});
