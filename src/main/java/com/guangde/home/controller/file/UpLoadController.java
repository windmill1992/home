package com.guangde.home.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiGoodLibrary;
import com.guangde.home.utils.CompressPicUtil;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.ImageUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("file")
public class UpLoadController {

	Logger logger = LoggerFactory.getLogger(UpLoadController.class);
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
    private IUserFacade userFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ICommonFacade commonFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	@ResponseBody
	@RequestMapping("upload")
	public JSONObject upload(@RequestParam("file") CommonsMultipartFile[] files,@RequestParam("type") int type,
			HttpServletRequest request) {
		
		JSONObject jb = new JSONObject();
		ApiResult result = null;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				try {
//					//拿到输出流，同时重命名上传的文件  
					String name = "e:/" + new Date().getTime() + files[i].getOriginalFilename();
                    FileOutputStream os = new FileOutputStream(name);  
					FileInputStream in = (FileInputStream) files[i]
							.getInputStream();
					//以写字节的方式写文件  
                    int b = 0;  
                    while((b=in.read()) != -1){  
                        os.write(b);  
                    }  
                    os.flush();  
                    os.close();  
					if("error".equals(ImageUtil.checkExt(files[i],ImageUtil.typename(new Integer(type).toString())))){
						//文件的扩展名不符合要求
						jb.put("result", "1");
						jb.put("msg", "文件的扩展名不符合要求");
						return jb;
					}
//					result = fileFacade.upload(ImageUtil.typename(type), ImageUtil.checkExt(files[i]), in);
					jb.put("result", "0");
					jb.put("msg", "上传成功");
//					jb.put("url", result.getData());
					jb.put("url", "11;"+name);
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("上传出错");
				}
			}
		}
		return jb;
	}

	@RequestMapping("upload2")
	public String upload2(HttpServletRequest request,
			HttpServletResponse response) throws IllegalStateException,
			IOException {
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 记录上传过程起始时的时间，用来计算上传时间
				int pre = (int) System.currentTimeMillis();
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						System.out.println(myFileName);
						// 重命名上传后的文件名
						String fileName = "demoUpload"
								+ file.getOriginalFilename();
						// 定义上传路径
						String path = "e:/" + fileName;
						File localFile = new File(path);
						file.transferTo(localFile);
					}
				}
				// 记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				System.out.println(finaltime - pre);
			}

		}
		return "/success";
	}

	@RequestMapping("toUpload")
	public String toUpload() {

		return "common/upload";
	}
	
	@RequestMapping("wuliupload")
	public String wuliupload() {

		return "common/wuliupload";
	}
	@ResponseBody
	@RequestMapping(value="upload3" ,produces = {"text/html;charset=UTF-8"})
	public String upload3(@RequestParam("file") CommonsMultipartFile file,@RequestParam("type") String type,
			HttpServletRequest request,HttpServletResponse response) {
		
		JSONObject jObject = new JSONObject();
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = (InputStream) file.getInputStream();
			
				type = ImageUtil.typename(type);
                int rcode = ImageUtil.checkImg(file, type);
				if (1==rcode) {
					// 文件的大小
					jObject.put("result", "1");
					jObject.put("error", "上传图片太大，请保持2M内。");
					return jObject.toString();
				}
				if (2==rcode) {
					// 文件的扩展名不符合要求
					jObject.put("result", "1");
					jObject.put("error","上传图片格式出现问题，上传的图片格式jpg、png、gif、bmp.");
					return jObject.toString();
				}
				if("personhead".equals(type)){
					Integer userId = UserUtil.getUserId(request, response);
					ApiFrontUser user = userFacade.queryById(userId);
					if(user == null){
						jObject.put("result", "1");
						jObject.put("error", "你没有登录，不能更新头像");
						return jObject.toString();
					}
				
					//压缩图片
					InputStream in = CompressPicUtil.compressPic(inputStream, file, request,"local");
					ApiResult result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in);
					String imageId = result.getData().split(";")[0];
					String imageUrl = result.getData().split(";")[1];
					jObject.put("result", "0");
					jObject.put("imageId", imageId);
					jObject.put("imageUrl", imageUrl);
					user.setCoverImageId(Integer.valueOf(imageId));
					userFacade.updateUser(user);
					ApiCompany apiCompany = new ApiCompany();
					apiCompany.setUserId(userId);
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					if(apiCompany != null){
						apiCompany.setCoverImageId(user.getCoverImageId());
						companyFacade.updateCompany(apiCompany);
					}
					SSOUtil.writeUserHead(response,imageUrl);
				}
				else if("goodlibrary".equals(type)){
					Integer userId = UserUtil.getUserId(request, response);
					ApiFrontUser user = userFacade.queryById(userId);
					if(user == null){
						jObject.put("result", "1");
						jObject.put("error", "你没有登录，不能更新善库头像！");
						return jObject.toString();
					}
					ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
					goodLibrary.setUserId(userId);
					ApiPage<ApiGoodLibrary> page = goodLibraryFacade.queryByParam(goodLibrary, 1, 1);
					if(page.getTotal()>0 && page.getResultData().get(0).getLogoId()!=null){
						//压缩图片
						InputStream in = CompressPicUtil.compressPic(inputStream, file, request,"local");
						ApiResult result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in);
						String imageId = result.getData().split(";")[0];
						String imageUrl = result.getData().split(";")[1];
						jObject.put("result", "0");
						jObject.put("imageId", imageId);
						jObject.put("imageUrl", imageUrl);
						goodLibrary.setId(page.getResultData().get(0).getId());
						goodLibrary.setLogoId(Integer.valueOf(imageId));
						goodLibrary.setContentImageId(page.getResultData().get(0).getContentImageId()+","+imageId);
						goodLibraryFacade.update(goodLibrary);
					}
				}
				else if("projectTopic".equals(type)){
					Integer userId = UserUtil.getUserId(request, response);
					ApiFrontUser user = userFacade.queryById(userId);
					if(user == null){
						jObject.put("result", "1");
						jObject.put("error", "你没有登录，不能上传项目主题！");
						return jObject.toString();
					}
					// 等比压缩图片 长宽不变
					InputStream in = CompressPicUtil.compressPic(inputStream, file, request,"local");
					ApiResult result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in);
					String imageId = result.getData().split(";")[0];
					String imageUrl = result.getData().split(";")[1];
					logger.info("compressPic   imageId = "+imageId+" imageUrl = "+imageUrl);
					jObject.put("result", "0");
					jObject.put("imageId", imageId);
					jObject.put("imageUrl", imageUrl);
				}
				else {
				
					// 等比压缩图片 长宽不变
					InputStream in = CompressPicUtil.compressPic(inputStream, file, request,"local");
					
					ApiResult result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in);
					
					String imageId = result.getData().split(";")[0];
					String imageUrl = result.getData().split(";")[1];
					
					logger.info("compressPic   imageId = "+imageId+" imageUrl = "+imageUrl);
					/*
					// 等比压缩成宽640的图片
					InputStream in2 = CompressPicUtil.compressPic(inputStream, file, request,"middle");
					result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in2,Integer.parseInt(imageId),"middle");
					
					// 压缩成 100 * 100 
					InputStream in3 = CompressPicUtil.compressPic(inputStream, file, request,"litter");
					result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in3,Integer.parseInt(imageId),"litter");
					*/
					
					jObject.put("result", "0");
					jObject.put("imageId", imageId);
					jObject.put("imageUrl", imageUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("上传出错" + e.getMessage());
				jObject.put("result", "1");
				jObject.put("error", "上传图片出现问题。");
				return jObject.toString();
			}
		}
		
		return jObject.toString();
	}

	@ResponseBody
	@RequestMapping(value="uploadItem" ,produces = {"text/plain;charset=UTF-8"})
	public String uploadItem(@RequestParam("file") CommonsMultipartFile file,@RequestParam("type") String type,
			HttpServletRequest request,HttpServletResponse response) {
	
		JSONObject jObject = new JSONObject();
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = (InputStream) file.getInputStream();
			
				type = ImageUtil.typename(type);
                int rcode = ImageUtil.checkImg(file, type);
				if (1==rcode) {
					// 文件的大小
					jObject.put("result", "1");
					jObject.put("error", "上传图片太大，请保持2M内。");
					return jObject.toString();
				}
				if (2==rcode) {
					// 文件的扩展名不符合要求
					jObject.put("result", "1");
					jObject.put("error","上传图片格式出现问题，上传的图片格式jpg、png、gif、bmp.");
					return jObject.toString();
				}
//				ApiResult result = fileFacade.upload(
//						type,
//						ImageUtil.checkExt(file,type), in,null,"");
				
				// 等比压缩图片 长宽不变
				InputStream in = CompressPicUtil.compressPic(inputStream, file, request,"local");
				ApiResult result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in);
				
				String imageId = result.getData().split(";")[0];
				String imageUrl = result.getData().split(";")[1];
				/*
				// 等比压缩成宽640的图片
				InputStream in2 = CompressPicUtil.compressPic(inputStream, file, request,"middle");
				result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in2,Integer.parseInt(imageId),"middle");
				
				// 压缩成 100 * 100 
				InputStream in3 = CompressPicUtil.compressPic(inputStream, file, request,"litter");
				result = fileFacade.upload(type,ImageUtil.checkExt(file,type), in3,Integer.parseInt(imageId),"litter");
				*/
			
				jObject.put("result", "0");
				jObject.put("imageId", imageId);
				jObject.put("imageUrl", imageUrl);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("上传出错" + e.getMessage());
				jObject.put("result", "1");
				jObject.put("error", "上传图片出现问题。");
				return jObject.toString();
			}
		}
		
		return jObject.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/image/delete.do")
	public Map<String,Object> Imagedelete(int[] images) {
		if (images == null) {
			return webUtil.resMsg(0, "0006", "上传的参数有误", null);
		}
		ApiResult result =null;
		for (int j = 0; j < images.length; j++) {
			result = fileFacade.deleteBfile(images[j]);
			if(result == null || result.getCode() != 1){
				return webUtil.resMsg(0, "0005", "上传图片删除失败", null);
			}
		}
		return webUtil.resMsg(1, "0000", "成功", null);
	}
	
	/**
	 * 删除图片（包括数据库里的数据和文件）
	 * @param images
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/image/newDelete.do")
	public Map<String,Object> newImagedelete(int[] images) {
		if (images == null) {
			return webUtil.resMsg(0, "0006", "上传的参数有误", null);
		}
		ApiResult result =null;
		for (int j = 0; j < images.length; j++) {
			//--------------------------
			ApiConfig config = new ApiConfig();
            config.setConfigKey("fileBasicURL");
            List<ApiConfig> list = commonFacade.queryList(config);
            String fileBasicURL = list.get(0).getConfigValue();
            if (fileBasicURL=="")
            {
            	//return responseFaild(parameter, "删除失败！", null);
            }
            String realPath = fileBasicURL + "/upload/picture/";
            ApiBFile bFile = fileFacade.queryBFileById(images[j]);
            System.out.println(bFile.getUrl().substring(28));
            boolean result2 = ImageUtil.deletePicture(realPath + bFile.getUrl().substring(28));//删除图片
            if (result2)
            {
            	result = fileFacade.deleteBfile(images[j]);
            }
			//-----------------------------
			
			if(result == null || result.getCode() != 1){
				return webUtil.resMsg(0, "0005", "上传图片删除失败", null);
			}
		}
		return webUtil.resMsg(1, "0000", "成功", null);
	}
	
	@RequestMapping(value = "/image")
	public void getImage(@RequestParam(value = "filepath")String filepath ,HttpServletResponse response) {
	    FileInputStream fis = null;
	    response.setContentType("image/gif");
	    try {
	        OutputStream out = response.getOutputStream();
	        File file = new File(filepath);
	        fis = new FileInputStream(file);
	        byte[] b = new byte[fis.available()];
	        fis.read(b);
	        out.write(b);
	        out.flush();
	    } catch (Exception e) {
	         e.printStackTrace();
	    } finally {
	        if (fis != null) {
	            try {
	               fis.close();
	            } catch (IOException e) {
	            e.printStackTrace();
	        }   
	          }
	    }
	}
	
	
	public static String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?"; 
	/** 
	 * 获取媒体文件 微信上的已上传图片
	 *  
	 * @param accessToken 
	 *      接口访问凭证 
	 * @param media_id 
	 *      媒体文件id 
	 * */
	@ResponseBody
	@RequestMapping(value = "/wximage")
	public Map<String, Object> downloadMedia(@RequestParam(value = "mId",required = true) String mId,
			@RequestParam(value = "typeName",required = false) String typeName,
			HttpServletRequest request,HttpServletResponse response) { 
		//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
		String accessToken ="";//(String) redisService.queryObjectData("AccessToken");
		//if(accessToken == null){
			accessToken = TenpayUtil.queryAccessToken();
			//redisService.saveObjectData("Down_AccessToken" , accessToken, DateUtil.DURATION_TEN_S);
			redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
			String jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
			redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
		//}
		String image="";
		String imageL ="";
		String imageIds="";
		logger.info("图片上传："+mId);
		if(StringUtils.isNotEmpty(mId)){
			if("personhead".equals(typeName)){
				imageL = queryImage(accessToken,mId,"8");
				if(imageL != null){
					Integer userId = UserUtil.getUserId(request, response);
					ApiFrontUser user = userFacade.queryById(userId);
					if(user == null){
						return webUtil.loginFailedRes(null);
					}
					user.setCoverImageId(Integer.valueOf(imageL));
					userFacade.updateUser(user);
					ApiCompany apiCompany = new ApiCompany();
					apiCompany.setUserId(userId);
					apiCompany = companyFacade.queryCompanyByParam(apiCompany);
					if(apiCompany != null){
						apiCompany.setCoverImageId(user.getCoverImageId());
						companyFacade.updateCompany(apiCompany);
					}
				}else{
					return webUtil.failedRes("10111", "图片上传失败", null);
				}
			}
			else if("commonForm".equals(typeName)){//表单下载图片
				String[] mIdL = mId.split(",");
				for (int i = 0; i < mIdL.length; i++) {
					image +=(queryImageUrl(accessToken,mIdL[i],"4")+",");//http@11,http2@2,http3@3,
				}
				logger.info("image:"+image);
				String[] imgs = image.split("\\,");
				for (String img : imgs) {
					String im[] = img.split("\\@");
					imageL+=(im[0]+",");
					imageIds+=(im[1]+",");
				}
				
			}else {
				String[] mIdL = mId.split(",");
				for (int i = 0; i < mIdL.length; i++) {
					imageL +=(queryImage(accessToken,mIdL[i],"4")+",");
				}
			}
			logger.info("图片上传：imageL"+imageL);
			logger.info("图片上传：imageIds"+imageIds);
			return webUtil.successResDoubleObject(imageL,imageIds);
		}
		return webUtil.failedRes("10111", "图片上传失败", null);
	}
	
	private String queryImage(String accessToken ,String media_id,String type){
		String Url = requestUrl+"access_token="+ accessToken+"&media_id="+media_id; 
		HttpURLConnection conn = null; 
		InputStream in = null;
		try { 
			URL url = new URL(Url); 
			logger.info("Url:"+Url);
		    conn = (HttpURLConnection) url.openConnection(); 
		    conn.setDoInput(true); 
		    conn.setRequestMethod("GET"); 
		    conn.setConnectTimeout(30000); 
		    conn.setReadTimeout(30000); 
		    
		    in =  conn.getInputStream();
		    type = ImageUtil.typename(type);
		    // 等比压缩图片 长宽不变
			ApiResult result = fileFacade.upload(type,"jpg", in);
			String imageId = result.getData().split(";")[0];
			return imageId;
		  } catch (Exception e) { 
		    e.printStackTrace(); 
		  } finally { 
		    if(conn != null){ 
		      conn.disconnect(); 
		    } 
		  }
		return null;
	}
	
	/**
	 * 下载图片，返回地址
	 * @param accessToken
	 * @param media_id
	 * @param type
	 * @return
	 */
	private String queryImageUrl(String accessToken ,String media_id,String type){
		String Url = requestUrl+"access_token="+ accessToken+"&media_id="+media_id; 
		HttpURLConnection conn = null; 
		InputStream in = null;
		try { 
			URL url = new URL(Url); 
			logger.info("Url:"+Url);
		    conn = (HttpURLConnection) url.openConnection(); 
		    conn.setDoInput(true); 
		    conn.setRequestMethod("POST"); 
		    conn.setConnectTimeout(30000); 
		    conn.setReadTimeout(30000); 
		    
		    in =  conn.getInputStream();
		    type = ImageUtil.typename(type);
		    // 等比压缩图片 长宽不变
			ApiResult result = fileFacade.upload(type,"jpg", in);
			String imageUrl = result.getData().split(";")[1];
			String imageId = result.getData().split(";")[0];
			return imageUrl+"@"+imageId;
		  } catch (Exception e) { 
		    e.printStackTrace(); 
		  } finally { 
		    if(conn != null){ 
		      conn.disconnect(); 
		    } 
		  }
		return null;
	}
	
	/**
	 * 随机获取一张图片
	 * @return
	 */
	@RequestMapping(value="randomPic",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject randomPic(){
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		String fileType = "picture";
		String category = "drawPic";
		ApiBFile bFile = fileFacade.randomPic(fileType, category);
		if(bFile!=null){
			result.put("thumbUrl", bFile.getUrl());
			result.put("url", bFile.getMiddleUrl());
			result.put("description", bFile.getDescription());
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
		}else{
			item.put("code", 0);
			item.put("msg", "data is null");
		}
		return item;
	}
}
