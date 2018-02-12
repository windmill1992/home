package com.tenpay.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxNotice {
	Logger logger = LoggerFactory.getLogger(WxNotice.class);
	
	public WxNotice(){
		 Thread t = new Thread()
	     {
	         public void run()
	         {
	             logger.info("  send sm  >>  start ");
//	             ymSmsSenderService.send(announce);
	             logger.info("  send sm  >>  end ");
	         }
	     };
	}
}
