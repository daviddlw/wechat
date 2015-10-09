package com.david.wechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechat")
public class WechatController {

	private static String Token = "davidtoken_123456";
	private static Logger logger = Logger.getLogger(WechatController.class);

	@ResponseBody
	@RequestMapping(value = "/handlerRedirect", method = RequestMethod.GET)
	public void handlerRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpServletRequest request = req;
		HttpServletResponse response = res;
		// 微信服务器将发送GET请求到填写的URL上,这里需要判定是否为GET请求
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		System.out.println("Get Wechat request:" + request.getMethod() + " method");
		if (isGet) {
			// 验证URL真实性
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			String echostr = request
					.getParameter("echostr");// 随机字符串

			logger.info("signature: " + signature);
			logger.info("timestamp: " + timestamp);
			logger.info("nonce: " + nonce);
			logger.info("echostr: " + echostr);

			if (signature != null && timestamp != null && nonce != null && echostr != null) {
				List<String> params = new ArrayList<String>();
				params.add(Token);
				params.add(timestamp);
				params.add(nonce);
				// 1. 将token、timestamp、nonce三个参数进行字典序排序
				Collections.sort(params, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
				String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
				if (temp.equals(signature)) {
					logger.info("temp: " + temp);
					response.getWriter().write(echostr);
				}
			}
		} else {
			// 处理接收消息
		}
	}
}
