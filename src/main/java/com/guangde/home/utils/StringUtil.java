package com.guangde.home.utils;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	private static String[] randomStr = new String[] { "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9" };

	// 验证是否是手机号码
	public static boolean isMobile(String str) {
		if (StringUtils.isBlank(str) || str.length() != 11) {
			return false;
		}
		String reg = "1(2|3|4|5|6|7|8)[0-9]{9}";
		return str.matches(reg);
	}

	// 生成随机数
	public static String randonNums(int length) {
		StringBuffer randomCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String rand = randomStr[random.nextInt(10)];
			randomCode.append(rand);
		}
		return randomCode.toString();
	}

	public static String uniqueCode() {
		Date date = new Date();
		return date.getTime() + randonNums(4);
	}

	// 将普通文本格式转成html格式
	public static String convertToHtml(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		String temp = str;
		temp = StringUtils.replace(temp, "<", "&lt;");
		temp = StringUtils.replace(temp, ">", "&gt;");
		temp = StringUtils.replace(temp, "\t", "    ");
		temp = StringUtils.replace(temp, "\n", "<br>");
		temp = StringUtils.replace(temp, " ", "&nbsp;");
		return temp;
	}
	
	public static String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    } 
	// 将普通文本格式消除符号
	public static String convertTodelete(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		String temp = str;
		temp = StringUtils.replace(temp, "<", "");
		temp = StringUtils.replace(temp, ">", "");
		temp = StringUtils.replace(temp, "\t", "");
		temp = StringUtils.replace(temp, "\n", "");
		temp = StringUtils.replace(temp, " ", "");
		temp = StringUtils.replace(temp, "\r", "");
		return temp;
	}
	/**
	 * 对double类型，小数点后面，截位
	 * @param d
	 * @param precision
	 * @return
	 */
	public static String formatDouble(double d, int precision) {
		try {
			String str = Double.toString(d);
			if (StringUtils.isBlank(str)) {
				return "";
			}
			if(precision<0){
				precision = 0;
			}
			int dot = str.indexOf(".");
			if(dot==-1){
				return str;
			}
			if(precision==0){
				return str.substring(0, dot);
			}
			int dotLen = str.length() - dot - 1;
			if (dotLen < precision) {
				StringBuilder b = new StringBuilder(str);
				for(int i=0;i<precision-dotLen;i++){
					b.append("0");
				}
				return b.toString();
			}else if(dotLen == precision){
				return str;
			}
			return str.substring(0, dot + precision + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 对纯数字的进行保护
	 * 
	 * @param identifyNum
	 * @return
	 */
	public static String getSafeNumber(String identifyNum) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < identifyNum.length(); i++) {
			if (i < 6) {
				sb.append(identifyNum.charAt(i));
			} else if (i >= 6 && i < identifyNum.length() - 4) {
				sb.append("*");
			} else {
				sb.append(identifyNum.charAt(i));
			}
		}
		return sb.toString();
	}
	/**
	 * 计算百分比，指定剩余位数(precision>0有效)
	 *  d1/d2
	 *  
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String doublePercentage(Double d1,Double d2,int  precision){
		if(d1==null||d2==null||d2<0.001){
			return  StringUtil.formatDouble(0, precision);
		}
		double temp = d1/d2;
		if(temp<0.001)
			return "0";
//		return temp > 1 ? "100" : StringUtil.formatDouble(temp * 100, precision);
		return StringUtil.formatDouble(temp * 100, precision);
	}
	
	private static boolean isNotEmojiCharacter(char codePoint)
	{
		return (codePoint == 0x0) ||
			(codePoint == 0x9) ||
			(codePoint == 0xA) ||
			(codePoint == 0xD) ||
			((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
			((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
			((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source)
	{
		int len = source.length();
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++)
		{
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint))
			{
				buf.append(codePoint);
			}
		}
		return buf.toString();
	}
	public static void main(String[]args){
		System.out.println(doublePercentage(1d,300000d,0));
	}
	
	/**
	 * 隐藏纯数字最后的六位
	 * 
	 * @param identifyNum
	 * @return
	 */
	public static String hideNumberLatestSix(String identifyNum) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < identifyNum.length(); i++) {
			 if ( i>=identifyNum.length() - 6) {
				sb.append("*");
			} else {
				sb.append(identifyNum.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 捐款语
	 */
	public static String LEAVEWORD="leaveWord";
}
