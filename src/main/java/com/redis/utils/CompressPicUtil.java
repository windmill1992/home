package com.redis.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CompressPicUtil {
	 private static File file = null; // 文件对象   
     private String inputDir; // 输入图路径  
     private String outputDir; // 输出图路径  
     private String inputFileName; // 输入图文件名  
     private String outputFileName; // 输出图文件名  
     private int outputWidth = 1024; // 默认输出图片宽  
     private int outputHeight = 1024; // 默认输出图片高  
     private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)  
     public CompressPicUtil() { // 初始化变量  
         inputDir = "";   
         outputDir = "";   
         inputFileName = "";   
         outputFileName = "";   
         outputWidth = 1024;   
         outputHeight = 1024;   
     }   
     public void setInputDir(String inputDir) {   
         this.inputDir = inputDir;   
     }   
     public void setOutputDir(String outputDir) {   
         this.outputDir = outputDir;   
     }   
     public void setInputFileName(String inputFileName) {   
         this.inputFileName = inputFileName;  
     }   
     public void setOutputFileName(String outputFileName) {   
         this.outputFileName = outputFileName;   
     }   
     public void setOutputWidth(int outputWidth) {  
         this.outputWidth = outputWidth;   
     }   
     public void setOutputHeight(int outputHeight) {   
         this.outputHeight = outputHeight;   
     }   
     public void setWidthAndHeight(int width, int height) {   
         this.outputWidth = width;  
         this.outputHeight = height;   
     }   
       
     /*  
      * 获得图片大小  
      * 传入参数 String path ：图片路径  
      */   
     public long getPicSize(String path) {   
         file = new File(path);   
         return file.length();   
     }  
       
     public static void main(String[] args) {
		String inurl = "F:\\3.jpg"; 
		String outurl = "F:\\out.jpg"; 
		//大于100kb不进行缩放
		scale(inurl,outurl,1,false);
	}
     /** 
      * 缩放图像（按比例缩放） 
      * @param srcImageFile 源图像文件地址 
      * @param result 缩放后的图像地址 
      * @param scale 缩放比例 
      * @param flag 缩放选择:true 放大; false 缩小; 
      */  
     public final static void scale(String srcImageFile, String result,  
             int scale, boolean flag) {  
         try {  
        	 File aFile = new File(srcImageFile);
             BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件  
             int width = src.getWidth(); // 得到源图宽  
             int height = src.getHeight(); // 得到源图长  
             if (flag) {// 放大  
                 width = width * scale;  
                 height = height * scale;  
             } else {// 缩小  
                 width = width / scale;  
                 height = height / scale;  
             }  
             Image image = src.getScaledInstance(width, height,  
                     Image.SCALE_DEFAULT);  
             BufferedImage tag = new BufferedImage(width, height,  
                     BufferedImage.TYPE_INT_RGB);  
             Graphics g = tag.getGraphics();  
             g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
             g.dispose();  
             ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流  
         } catch (IOException e) {  
             e.printStackTrace();  
         }  
     } 
}
