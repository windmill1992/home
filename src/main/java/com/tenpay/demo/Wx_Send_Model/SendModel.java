package com.tenpay.demo.Wx_Send_Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.guangde.entry.ApiAuctionProject;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiMatchDonate;
import com.guangde.home.utils.DateUtil;
import com.tenpay.demo.TemplateData;
import com.tenpay.demo.WxTemplate;
import com.tenpay.utils.TenpayUtil;

public class SendModel {

	private final static String url ="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	/**
	 * 对于善款捐赠通知的模版封装
	 */
	public JSONObject Donation_Success_Notice(String accessToken,String openId,String returnUrl,ApiDonateRecord record){
		//没有额外的操作
		return Donation_Success_Notice(accessToken,openId,returnUrl,record,0,null);
	}
	/**
	 * 对于善款捐赠通知的模版封装
	 */
	public JSONObject Donation_Success_Notice(String accessToken,String openId,String returnUrl,ApiDonateRecord record,int type,Object object){
		String sendurl = url + accessToken;
		WxTemplate wxTemplate = new WxTemplate();
		wxTemplate.setTemplate_id("N1bknb5srKF7kBoKH4SQ4z89K4xAidl00FSxc7HpHJE");
		wxTemplate.setTouser(record.getOpenId());
		wxTemplate.setUrl(returnUrl);
		wxTemplate.setTopcolor("#FF0000");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
		TemplateData first = new TemplateData();
		if(type == 1){
			ApiMatchDonate mDonate = (ApiMatchDonate)object;
			first.setValue("亲爱的"+record.getNickName()+"，您已成功捐助"+record.getDonatAmount()+"元。“"+mDonate.getCompanyname()+"”因为您的善举为该项目配捐了"+mDonate.getMoney()+"元，感谢有您！");
		}else {
			first.setValue("亲爱的"+record.getNickName()+"，赠人玫瑰，手有余香，您已成功捐赠"+record.getDonatAmount()+"元。感谢有你！");
		}
		first.setColor("#173177");
		m.put("first", first);
		TemplateData DonateNum = new TemplateData();
		DonateNum.setValue(record.getProjectTitle()+"\n");
		DonateNum.setColor("#F1451C");
		m.put("DonateNum", DonateNum);
		TemplateData DonateSum = new TemplateData();
		DonateSum.setValue(record.getDonatAmount().toString()+"元");
		DonateSum.setColor("#F1451C");
		m.put("DonateSum", DonateSum);
		TemplateData remark = new TemplateData();
		if(type == 1){
			ApiMatchDonate mDonate = (ApiMatchDonate)object;
			remark.setValue("\n赞助企业："+mDonate.getCompanyname()+"");
		}else {
			remark.setValue("\n我们会实时公布善款执行明细，敬请关注。");
		}
		remark.setColor("#173177");
		m.put("remark", remark);
		wxTemplate.setData(m);
		return TenpayUtil.httpRequest(sendurl,"GET",JSONObject.toJSONString(wxTemplate));
	}
	
	/**
	 * 对于善款使用说明的模版封装
	 */
	public JSONObject Use_Donation_Explain(String accessToken,String openId,String returnUrl,ApiDonateRecord record){
		String sendurl = url + accessToken;
		WxTemplate wxTemplate = new WxTemplate();
		wxTemplate.setTemplate_id("UPjGFTCzZqDtZV8jgP7NgIPCvwY1GHnySjE3FV8cum0");
		wxTemplate.setTouser(openId);
		wxTemplate.setUrl(returnUrl);
		wxTemplate.setTopcolor("#FF0000");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
		TemplateData first = new TemplateData();
		first.setValue("亲爱的"+record.getNickName()+"，您捐助的公益项目有新反馈了，包括善款公开、项目执行等详细内容。请点击本消息查看。");
		first.setColor("#173177");
		m.put("first", first);
		TemplateData ProjName = new TemplateData();
		ProjName.setValue(record.getProjectTitle());
		ProjName.setColor("#F1451C");
		m.put("ProjName", ProjName);
		TemplateData Report = new TemplateData();
		Report.setValue(record.getDonatAmount());
		Report.setColor("#F1451C");
		m.put("Report", Report);
		TemplateData remark = new TemplateData();
		remark.setValue("公开、透明，我们一直在努力！");
		remark.setColor("#173177");
		m.put("remark", remark);
		wxTemplate.setData(m);
		return TenpayUtil.httpRequest(sendurl,"GET",JSONObject.toJSONString(wxTemplate));
	}
	
	/**
	 * 对于竞拍价格被超越提醒的模版封装
	 */
	public JSONObject Use_Price_Auction_Over(String accessToken,String openId,String returnUrl,ApiAuctionProject project){
		String sendurl = url + accessToken;
		WxTemplate wxTemplate = new WxTemplate();
		wxTemplate.setTemplate_id("fdd42QkcT71hY_cZN5jEg8QFbktEknT87J0NDYVMeU8");
		wxTemplate.setTouser(openId);
		wxTemplate.setUrl(returnUrl);
		wxTemplate.setTopcolor("#FF0000");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
		TemplateData first = new TemplateData();
		first.setValue(project.getNickName()+"，您好，您参与竞拍的作品报价已被超越"+"\n");
		first.setColor("#173177");
		m.put("first", first);
		TemplateData keyword1 = new TemplateData();
		keyword1.setValue(project.getTitle()+"\n");
		keyword1.setColor("#F1451C");
		m.put("keyword1", keyword1);
		TemplateData keyword2 = new TemplateData();
		keyword2.setValue(project.getUserprice()+"元");
		keyword2.setColor("#F1451C");
		m.put("keyword2", keyword2);
		TemplateData keyword3 = new TemplateData();
		keyword3.setValue(project.getCurrentPrice()+"元");
		keyword3.setColor("#F1451C");
		m.put("keyword3", keyword3);
		TemplateData keyword4 = new TemplateData();
		keyword4.setValue(DateUtil.parseToFormatDateString(new Date(), "yyyy年MM月dd日 HH:mm")+"\n");
		keyword4.setColor("#F1451C");
		m.put("keyword4", keyword4);
		TemplateData remark = new TemplateData();
		remark.setValue("快去看看吧，感谢您的关注");
		remark.setColor("#173177");
		m.put("remark", remark);
		wxTemplate.setData(m);
		return TenpayUtil.httpRequest(sendurl,"GET",JSONObject.toJSONString(wxTemplate));
	
	}
	
}
