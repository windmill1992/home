package com.guangde.home.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

//加密解密类
public class EncryptionUtil {
	
	public static final String TYPE_DES = "DES";
	 /**
     * Description 根据键值进行加密
     * @param data 
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key,String type) throws Exception {
        if(StringUtils.isBlank(data)||StringUtils.isBlank(key)||StringUtils.isBlank(type)){
        	throw new Exception("参数不能为空");
        }
        if(TYPE_DES.equals(type)){
        	byte[]chars = desEncrypt(data.getBytes(), key.getBytes());
        	return new BASE64Encoder().encode(chars);
        }
    	return null;
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key,String type) throws Exception,
            Exception {
    	 if(StringUtils.isBlank(data)||StringUtils.isBlank(key)||StringUtils.isBlank(type)){
         	throw new Exception("参数不能为空");
         }
    	 if(TYPE_DES.equals(type)){
         	return new String(desDecrypt(new BASE64Decoder().decodeBuffer(data), key.getBytes()));
         }
       return null;
    }
    
    private static Key toKey(byte[] key,String type) throws Exception {  
        if(TYPE_DES.equals(type)){
        	DESKeySpec dks = new DESKeySpec(key);  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(type);  
            SecretKey secretKey = keyFactory.generateSecret(dks);  
            return secretKey;
        }else{
        	return null;
        }  
    }
    
    private static byte[] desEncrypt(byte []data, byte[] key) throws Exception {
    	 // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
        // 一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(EncryptionUtil.TYPE_DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(EncryptionUtil.TYPE_DES);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
        // 现在，获取数据并加密  
        // 正式执行加密操作  
        return cipher.doFinal(data);  
    }
    
    public static byte[] desDecrypt(byte[] data, byte[] key) throws Exception{
    	 // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
        // 一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(EncryptionUtil.TYPE_DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(EncryptionUtil.TYPE_DES);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
        // 现在，获取数据并加密  
        // 正式执行加密操作  
        return cipher.doFinal(data);  
      }
    public static void main(String[]args){
    	String key = "kfrbekrgsd";
    	try {
			String str = EncryptionUtil.encrypt("uuid", key,EncryptionUtil.TYPE_DES);
			System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
