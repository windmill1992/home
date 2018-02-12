
define(["extend","dialog","entry"],function($,d,en){
	var page={ 
		pageLen:8,//总页数
		showLen:7,//最多展示页数
		gapLen:3,//当前页及前后显示长度  
		status:0,//当前分页状态 0:头部页显示 1：中间页显示  2：尾页显示
	    curPage:1,//当前页
		pageType:0,//分页显示类型  0:pageLen<=showLen 1:pageLen>showLen
		isShow:false,//是否显示分页
		pageChange:false,//翻页到某一页 传值
		init:function (o){ 
			var that=this;  
			for(var key in o){
				that[key]=o[key];
			}
			$("body").on("click","#lstPages .pages i",function(){
				if(!$(this).hasClass("curPage")&&!$(this).hasClass("p-elipsis")){ 
					var page=$(this).html();
					that.changePage(page);  
				}
			}).on("click","#lstPages a#lstP-prev",function(){ 
				var page=parseInt(that.curPage)-1;
				that.changePage(page); 
			}).on("click","#lstPages a#lstP-next",function(){ 
				var page=parseInt(that.curPage)+1;
				that.changePage(page); 
			});
			/*$("body").on("click","#lstPages a",function(){
				var dir=$(this).attr("id");
				alert(dir);
				var page=parseInt(that.curPage)+parseInt(dir);
				that.changePage(page); 
			});*/
			
			var pageHtml=[],isShow=this.isShow,afterObj=this.afterObj;  
			pageHtml.push('<div id="lstPages" class="lstPages" '+(isShow?'style="display:block;"':'style="display:none;"')+'>');
			pageHtml.push('<span class="lstP-con"><a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a>');
			pageHtml.push('<span class="pages"></span>');
			pageHtml.push('<a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a></span></div> ');
			if($('#lstPages').length>0){$('#lstPages').remove()}
			$(afterObj).after(pageHtml.join(''));
		},
		pageInit:function(o){  
			var that =this;
			for(var key in o){
				that[key]=o[key];
			}   
			var pageLen=this.pageLen,
				showLen=this.showLen,
				gapLen=this.gapLen, 
				status=this.status,
				pageType=this.pageType;
				isShow=this.isShow;
				pageChange=o.pageChange;				
			this.curPage=1;
			$("#lstPages a#lstP-prev").hide();
			$("#lstPages a#lstP-next").show();
			if(pageLen>1&&pageLen<=showLen){ 
				this.pageType=0;   
				var html=[]; 
				html.push('<i class="curPage" p="1">1</i>'); 
				for(var i=1;i<pageLen;i++){ 
					var page=i+1;
					html.push('<i p='+page+'>'+page+'</i>');
				}  
				$("#lstPages .pages").html(html.join(''));
			}else if(pageLen>showLen){
				this.pageType=1;
				var html=[]; 
				html.push('<i class="curPage" p="1">1</i>'); 
				var len = showLen-2;
				for(var i=1;i<len;i++){
					var page=i+1;
					html.push('<i p='+page+'>'+page+'</i>');
				}
				html.push('<i class="p-elipsis">…</i>'); 
				/*html.push('<i p='+pageLen+'>'+pageLen+'</i>'); */
				$("#lstPages .pages").html(html.join(''));  
			}
			$("#lstPages").show();
			isShow?$("#lstPages").show():$("#lstPages").hide();
			if(pageChange){
				that.changePage(pageChange);
			}
				 
		},
		//changeCallback:null,//当前页改边后的回调函数；
		changePage:function(page){
			var pageLen=this.pageLen,
				showLen=this.showLen,
				gapLen=this.gapLen, 
				status=this.status,
				curPage=this.curPage,
				pageType=this.pageType;
				
			if(page>0&&page<=pageLen){ 
				this.curPage=page;
				$("#lstPages .pages").children().removeClass("curPage");  
			}else{
				return;
			} 
			
			if(page==pageLen){
				$("#lstPages a").first().show(); 
				$("#lstPages a").last().hide();
			}else if(page==1){
				$("#lstPages a").first().hide(); 
				$("#lstPages a").last().show();
			}else{ 
				$("#lstPages a").first().show(); 
				$("#lstPages a").last().show();
			}
			
			var  startPos=showLen-gapLen,endPos=pageLen-gapLen,pageStatus;   
			if(pageType){  
				if(curPage<=startPos){//0：开始
					pageStatus=0; 
				}else if(curPage>=endPos){// 2：结尾
					pageStatus=2;
				}else{ //2：中间
					pageStatus=1;	
					this.pageShow(); 
				} 
				if(status!=pageStatus){
					this.status=pageStatus;
					this.pageShow(); 
				} 
			} 
			$('#lstPages .pages').children().filter('[p='+page+']').addClass("curPage");
			$("body,html").animate({
				scrollTop:0  //让body的scrollTop等于pos的top，就实现了滚动
			},200);
			if(!!this.changeCallback){
			   this.changeCallback();//当前页改边后的回调函数
			}
			$("#lstPages .pages").children().removeClass("curPage");
			$('#lstPages .pages i[p='+page+']').addClass("curPage"); 
		}, 
		pageShow:function(){ 
			var pageLen=this.pageLen,
				showLen=this.showLen, 
				status=this.status,
				curPage=this.curPage,
				html=[]; 
			
			switch(status){
				case 0: 
					for(var i=0;i<showLen-2;i++){
						var page=i+1;
						html.push('<i p='+page+'>'+page+'</i>'); 
					}
					html.push('<i class="p-elipsis">…</i>');
					/*html.push('<i p='+pageLen+'>'+pageLen+'</i>');*/ 
					$("#lstPages .pages").html(html.join('')); 
				 break;
				case 1:
					var curPageRe=Number(curPage)-1,curPageAd=Number(curPage)+1;
					html.push('<i p="1">1</i>');
					html.push('<i class="p-elipsis">…</i>');
					html.push('<i p='+curPageRe+'>'+curPageRe+'</i>');
					html.push('<i class="curPage" p='+curPage+'>'+curPage+'</i>');
					html.push('<i p='+curPageAd+'>'+curPageAd+'</i>');
					html.push('<i class="p-elipsis">…</i>');
					/*html.push('<i p='+pageLen+'>'+pageLen+'</i>'); */
					$("#lstPages .pages").html(html.join('')); 
				 break;
				case 2:
					html.push('<i p="1">1</i>'); 
					html.push('<i class="p-elipsis">…</i>');
					for(var i=pageLen-(showLen-2);i<pageLen;i++){ 
						var page=i+1;
						html.push('<i p='+page+'>'+page+'</i>'); 
					}
					$("#lstPages .pages").html(html.join('')); 
				 break;
			}  
		}
	};
	
	return page;
});