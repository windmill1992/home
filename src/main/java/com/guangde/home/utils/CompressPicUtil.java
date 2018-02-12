package com.guangde.home.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**  
 * 图片压缩的工具类
 * 
 */
public class CompressPicUtil {
	
     private static int outputWidth = 800; // 默认输出图片宽  
     private static int outputHeight = 400; // 默认输出图片高  
     private static boolean proportion = false; // 是否等比缩放标记(默认为等比缩放)  
    
     /**  
      * 获得图片大小  
      *   @param file
      *   @return
      */   
     public long getPicSize(File file) {
         return file.length();   
     }  
       
     /**  
      * 图片处理
      * @param inputStream
      * @param file
      * @param request
      */
     public static InputStream compressPic(InputStream inputStream,MultipartFile file,HttpServletRequest request,String type) {  
    	 
    	 String path = request.getSession().getServletContext().getRealPath("/");
    	 File f = new File(path+"res/images/compressPic/temp"); 
    	 System.out.println("compressPic = "+path);
    	 if(!f.exists()){
    		 f.mkdirs();
    	 }
    	 String destFile = f+"/"+System.currentTimeMillis()+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
    	 InputStream ins =null;
         try {  
             Image img = ImageIO.read(inputStream);   
             // 判断图片格式是否正确   
             if (img.getWidth(null) == -1) {  
                 System.out.println(" can't read,retry!" + "<BR>");   
                 return null;   
             } else {   
                 Integer newWidth; Integer newHeight;   
                 double with =  (double) img.getWidth(null); 
                 double height = (double) img.getHeight(null);
                 // 判断是否是等比缩放   
                 if (proportion == true) {   
                     // 为等比缩放计算输出的图片宽度及高度   
                     double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1;   
                     double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1;   
                     // 根据缩放比率大的进行缩放控制   
                     double rate = rate1 > rate2 ? rate1 : rate2;   
                	 System.out.println(img.getWidth(null));
                	 System.out.println(img.getHeight(null));
                     newWidth = (int) (((double) img.getWidth(null))/rate);   
                     newHeight = (int) (((double) img.getHeight(null))/rate);   
                 } else {   
                     newWidth = (int) ((double) img.getWidth(null)); // 输出的图片宽度   
                     newHeight = (int) ((double) img.getHeight(null)); // 输出的图片高度   
                 }  
                
                 
                 if("local".equals(type))
                 {
                	  newWidth = (int) ((double) img.getWidth(null)); // 输出的图片宽度   
                      newHeight = (int) ((double) img.getHeight(null)); // 输出的图片高度   
                 }
                 else if("middle".equals(type))
                 {
                	 newWidth = 640;
                	 newWidth = 640;
                	 double rate = with/newWidth+0.1;
                	 newHeight = (int) (height/rate);   
                 }
                 else if("litter".equals(type))
                 {	
                	 newWidth = 100;
                	 newHeight = 100 ;
                
                 }
                 
                BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);   
                  
                /* 
                 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 
                 * 优先级比速度高 生成的图片质量比较好 但速度慢 
                 */   
                tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);  
                FileOutputStream out = new FileOutputStream(destFile);  
                // JPEGImageEncoder可适用于其他图片类型的转换   
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
                encoder.encode(tag);   
                out.close();   
                ins = new FileInputStream(destFile);
                File fi = new File(destFile);
                if(fi.exists()){
                	fi.delete();
                }
             }   
         } catch (IOException ex) {   
             ex.printStackTrace();   
         }   
         return ins;   
    }
  
}
