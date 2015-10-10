package com.david.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechat")
public class WechatController {

	private static Logger logger = Logger.getLogger(WechatController.class);
	private static String david_openid = "ok33DvrkARxrATpIergV2cektbt0";

	@Autowired
	private WxMpService wxMpService;

	@ResponseBody
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public WxMpUser wechatIn(HttpServletRequest request, HttpServletResponse response) {
		WxMpUser wxMpUser = new WxMpUser();
		try {
			wxMpUser = wxMpService.userInfo(david_openid, "zh_CN");
			logger.info("wxMpUser: " + wxMpUser);
		} catch (WxErrorException e) {
			logger.error(e.getMessage(), e);
		}

		return wxMpUser;
	}
}
