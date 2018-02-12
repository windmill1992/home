       function uploadImage(file,id)
        {
          var MAXWIDTH  = 30; 
          var MAXHEIGHT = 30;
		  var filepath =$(file).val();
		  var ext =filepath.substr(filepath.length-3,3);
		  if(ext!='jpg' && ext!='png' && ext!='jpeg' && ext!='gif'&& ext!='bmp')
		  {	
			alert("LOGO仅支持PNG、JPG、JPEG、GIF、BMP格式文件，请重新选择！")
			return false;
		  }
		  var file_size = 0;
		  if ($.browser.msie) {
                    var img = new Image();
                    img.src = filepath;
                    while (true) {
                        if (img.fileSize > 0) {
                            if (img.fileSize > 2 * 1024 * 1024) {
                                alert("图片不大于2MB。");
								return false;
                            } 
                            break;
                        }
                    }
                } else {
                    file_size = file.files[0].size;
                    var size = file_size / 1024;
                    if (size > 2048) {
                        alert("上传的图片大小不能超过2M！");
						return false;
                    } 
            }
		  var div = document.getElementById('pic_upload');
          if (file.files && file.files[0])
          {
              var reader = new FileReader();
					reader.onload = function(evt){
				  $(id+" img").attr('src',evt.target.result);
			  }
              reader.readAsDataURL(file.files[0]);
			  return true;
          }
          else //兼容IE
          {
            //var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
			//file.select();
			//file.blur(); 
            //var src = document.selection.createRange().text;
			//var img = document.getElementById('imgpic1'); 
			//img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src; 
			
			//$(id+" img").attr('src',src);
			//return false;
            //img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
            //var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            //status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
            //div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
          }
        }
        function clacImgZoomParam( maxWidth, maxHeight, width, height ){
            var param = {top:0, left:0, width:width, height:height};
            if( width>maxWidth || height>maxHeight )
            {
                rateWidth = width / maxWidth;
                rateHeight = height / maxHeight;
                
                if( rateWidth > rateHeight )
                {
                    param.width =  maxWidth;
                    param.height = Math.round(height / rateWidth);
                }else
                {
                    param.width = Math.round(width / rateHeight);
                    param.height = maxHeight;
                }
            }
            
            param.left = Math.round((maxWidth - param.width) / 2);
            param.top = Math.round((maxHeight - param.height) / 2);
            return param;
        }
				