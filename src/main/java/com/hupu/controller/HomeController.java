package com.hupu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@ResponseBody
	@RequestMapping(value = "/checkItWorks", method = RequestMethod.GET)
	public String checkItWorks() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(new Date());
		return result;
	}

	@RequestMapping(value = "/index.jsp", method = RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("index");
	}
}
