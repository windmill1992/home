package com.guangde.home.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.tenpay.demo.H5Demo;

public class AppletUtil {

	public static String getminiqrQr(Integer activityId, String path, String imgName) {
		String accessToken = H5Demo.getAppletAccessTokenRequest();
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token="+accessToken;
           /* JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject.put("page", "pages/index/index");
            jsonObject.put("width", 430);
            jsonObject.put("auto_color", false);
            jsonObject2.put("r", "255");
            jsonObject2.put("g", "104");
            jsonObject2.put("b", "17");
            jsonObject.put("line_color", jsonObject2);*/
            
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity("{\"path\":\"/pages/signIn/signIn?activityId="+activityId+"\",\"auto_color\":false,\"line_color\":{\"r\":\"255\",\"g\":\"104\",\"b\":\"17\"}}", headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            File file = new File(path + imgName);
            if (!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;    
    }

}
