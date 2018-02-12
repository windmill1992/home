package com.guangde.home.controller.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("help")
public class HelpController {

	Logger logger = LoggerFactory.getLogger(HelpController.class);

	@RequestMapping("about")
	public ModelAndView about() {
		ModelAndView view = new ModelAndView("help/about");
		view.addObject("flag", "about");
		return view;
	}
	
	@RequestMapping("groundProject")
	public ModelAndView groundProject() {
		ModelAndView view = new ModelAndView("shanhePlan/groundProject");
		view.addObject("flag", "groundProject");
		return view;
	}
	
	@RequestMapping("shanhe")
	public ModelAndView shanhe() {
		ModelAndView view = new ModelAndView("shanhePlan/introduction");
		view.addObject("flag", "shanhe");
		return view;
	}
	
	@RequestMapping("aboutTrait")
	public ModelAndView aboutTrait() {
		ModelAndView view = new ModelAndView("help/aboutTrait");
		view.addObject("flag", "aboutTrait");
		return view;
	}
	
	@RequestMapping("aboutPromise")
	public ModelAndView aboutPromise() {
		ModelAndView view = new ModelAndView("help/aboutPromise");
		view.addObject("flag", "aboutPromise");
		return view;
	}
	
	@RequestMapping("aboutMembers")
	public ModelAndView aboutMembers() {
		ModelAndView view = new ModelAndView("help/aboutMembers");
		view.addObject("flag", "aboutMembers");
		return view;
	}
	
	@RequestMapping("userService")
	public ModelAndView service() {
		ModelAndView view = new ModelAndView("help/service");
		view.addObject("flag", "userService");
		return view;
	}
	
	@RequestMapping("supplicantService")
	public ModelAndView helperService(){
		ModelAndView view = new ModelAndView("help/supplicantService");
		view.addObject("flag", "supplicantService");
		return view;
	}
	
	@RequestMapping("donorsService")
	public ModelAndView donorsService(){
		ModelAndView view = new ModelAndView("help/donorsService");
		view.addObject("flag","donorsService");
		return view;
	}
	
	@RequestMapping("gooderService")
	public ModelAndView gooderService(){
		ModelAndView view = new  ModelAndView("help/gooderService");
		view.addObject("flag", "gooderService");
		return view;
	}
	
	@RequestMapping("aboutDonation")
	public ModelAndView aboutDonation() {
		ModelAndView view = new ModelAndView("help/aboutDonation");
		view.addObject("flag", "aboutDonation");
		return view;
	}
	
	@RequestMapping("news")
	public ModelAndView news(int no) {
		ModelAndView view = new ModelAndView("news/new1");
		if(no==1){
			view = new ModelAndView("news/new1");
		}else if (no==2) {
			view = new ModelAndView("news/new2");
		}else if (no==3) {
			view = new ModelAndView("news/new3");
		}else {
			view = new ModelAndView("news/new4");
		}
		return view;
	}
	
	@RequestMapping("questions")
	public ModelAndView questions(@RequestParam(value = "no",required =false, defaultValue ="0") Integer no) {
		ModelAndView view = new ModelAndView("help/questions/ask-acticle0");
		if(no==0){
			view.addObject("flag0", "0");
		}
		if(no==1){
			view = new ModelAndView("help/questions/ask-acticle1");
			view.addObject("flag1", "1");
		}else if (no==2) {
			view = new ModelAndView("help/questions/ask-acticle2");
			view.addObject("flag2", "2");
		}else if (no==3) {
			view = new ModelAndView("help/questions/ask-acticle3");
			view.addObject("flag3", "3");
		}else if (no==4){
			view = new ModelAndView("help/questions/ask-acticle4");
			view.addObject("flag4", "4");
		}else if (no==5){
			view = new ModelAndView("help/questions/ask-acticle5");
			view.addObject("flag5", "5");
		}else if (no==6){
			view = new ModelAndView("help/questions/ask-acticle6");
			view.addObject("flag6", "6");
		}
		return view;
	}
	
	@RequestMapping("info")
	public ModelAndView info() {
		ModelAndView view = new ModelAndView("help/info");
		view.addObject("flag", "info");
		return view;
	}
}
