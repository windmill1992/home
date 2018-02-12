
define(["extend"],function(ex){
	var slide={ 
		init:function(o){/*slide,imgsWrap,type,isAuto,time*/  //type:circle,square
			var that=this,slide=$(o.wrapBox),slideList=$(o.imgsWrap); 
			
			this.slide=slide; 
			this.slideList=slideList;  
			this.time=o.time;
			
			var pageLen=slideList.children().length;
			var imgW=slide.width(); 
			this.pageLen=pageLen;
			this.imgW=imgW;
			slideList.width(imgW*pageLen);
			slideList.children().width(imgW); 
			
			var circleHtml=[];//<i class="selected"></i><i></i><i></i> 
			for(var i=0;i<pageLen;i++){
				circleHtml.push('<i></i>');
			} 
			this.slide.append('<div class="slide-bar">'+circleHtml.join('')+'</div>');
			
			var slideBar=this.slide.children(".slide-bar");
			slideBar.children().first().addClass("selected");
			
			
			this.slideBar=slideBar;  
			this.autoMove();
			
			slideBar.on("mouseover","i",function(){ 
				var index=slideBar.children().index($(this)); 
				that.circleMove(index); 
				return; 
			}).on("mouseout","i",function(){
				that.autoMove();
				return false;
			});
			 
			slideList.children().on('mouseover',function(){ 
				clearTimeout(that.timer); 
				return false;
			}).on('mouseout',function(){ 
				that.autoMove();
				return false;
			});
			
			  
		}, 
		time:3000,  
		page:1,
		timer:null,
		circleMove:function(page){
			clearTimeout(this.timer);
			var left=0,toPage=page+1;
			if(toPage!=this.page){ 
				left=-this.imgW*page;
				this.page=toPage;
				this.slideList.animate({"left":left+"px"});
				(this.slideBar.children().eq(page)).addClass("selected").siblings().removeClass("selected");
			}
			return;
		},
		direction:1,
		toMove:function(){
			clearTimeout(this.timer);
			var that=this,page=that.page;
			if(page==that.pageLen){//向右滑动→到1
			   that.direction=-1;
			} 
			if(page==1){//向左滑动←到pageLen
				that.direction=1;
			} 
			var left=0;
			if(page<this.pageLen&&that.direction==1){ 
				left=-page*this.imgW; 
				this.page++;	
			}else{
				left=this.slideList.position().left+this.imgW;
				this.page--;  
			}
			
			this.slideList.animate({"left":left+"px"});
			(this.slideBar.children().eq(this.page-1)).addClass("selected").siblings().removeClass("selected");
			this.autoMove();  
			
		},
		autoMove:function(){
			clearTimeout(this.timer);
			var that=this;
			this.timer=setTimeout(function(){
			 	that.toMove(); 
			},that.time);
		}
		
	};
	return slide;
});
