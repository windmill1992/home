
define(["extend"],function(ex){
	var banner={
		banner:null,
		init:function(o){/*banner,imgsWrap,type,isAuto,time*/  //type:circle,square
			var that=this; 
			var banner=$(o.wrapBox),
				bannerImgs=$(o.imgsWrap),
				slideTxt=$(o.slideTxt),
				prevBtn=$(o.prev),
				nextBtn=$(o.next);
			this.banner=banner; 
			this.bannerImgs=bannerImgs; 
			this.slideTxt=slideTxt;
			this.time=o.time;
			this.prevBtn=prevBtn;
			this.nextBtn=nextBtn;
			 
			if(o.isAuto){
				var imgLen=bannerImgs.children().length;
				var imgW=$(this.banner).width(); 
				this.imgLen=imgLen;
				this.imgW=imgW; 
				//bannerImgs.prepend(bannerImgs.html());
				bannerImgs.width(imgW*imgLen); 
				bannerImgs.children().width(imgW);  
				slideTxt.html("1/"+this.imgLen); 
				this.btnSlide(); 
				this.prevBtn.on("click",function(){
					clearTimeout(that.btnTimer); 
					that.dirMove(-1); 
					return; 
				});
				
				this.nextBtn.on("click",function(){  
					clearTimeout(that.btnTimer); 
					that.dirMove(1); 
					return; 
				}); 
				
				bannerImgs.children().on('mouseover',function(){ 
					clearTimeout(that.btnTimer); 
					return false;
				}).on('mouseout',function(){ 
					that.btnSlide();
					return false;
				}); 
			}  
		}, 
		time:6000,  
		page:0,
		btnTimer:null,
		dirMove:function(dir){
			clearTimeout(this.btnTimer); 
			var that=this; 
			var page=that.page+dir;  
			if(page>=that.imgLen){
				page=0;
			}else if(page<0){
				page=that.imgLen-1;
			} 
			left=-page*this.imgW; 
			
			this.bannerImgs.animate({"left":left+"px"});  
			this.page=page;  
			this.slideTxt.html((page+1)+"/"+this.imgLen); 
		}, 
		btnSlide:function(){
			var that=this; 
			this.btnTimer=setTimeout(function(){
				$(that.nextBtn).click();
				that.btnSlide();
			},that.time); 
		} 
	};
	return banner;
});
