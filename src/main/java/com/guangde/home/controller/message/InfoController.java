package com.guangde.home.controller.message;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("info")
public class InfoController {
	
	@RequestMapping("rules")
	public ModelAndView rules(){
		ModelAndView view = new ModelAndView("info/rules");
		view.addObject("rules","rules");
		return view;
	}
	
	@RequestMapping("baseInfo")
	public ModelAndView baseInfo(){
		ModelAndView view = new ModelAndView("info/baseInfo");
		view.addObject("baseInfo","baseInfo");
		return view;
	}
	
	@RequestMapping("workUnit")
	public ModelAndView workUnit(){
		ModelAndView view = new ModelAndView("info/workUnit");
		view.addObject("workUnit","workUnit");
		return view;
	}
	@RequestMapping("xmglzd")
	public ModelAndView xmglzd(){
		ModelAndView view = new ModelAndView("info/projectRule");
		view.addObject("xmglzd","xmglzd");
		return view;
	}
	@RequestMapping("cwgl")
	public ModelAndView cwgl(){
		ModelAndView view = new ModelAndView("info/amountManage");
		view.addObject("caiwu","caiwu");
		return view;
	}
	@RequestMapping("rsgl")
	public ModelAndView rsgl(){
		ModelAndView view = new ModelAndView("info/rsManage");
		view.addObject("renshi","renshi");
		return view;
	}
}
