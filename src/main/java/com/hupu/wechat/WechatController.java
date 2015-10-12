package com.hupu.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hupu.utils.CommonUtils;

@Controller
public class WechatController {

	private static Logger logger = Logger.getLogger(WechatController.class);
	private static final String ILLGAL_REQUEST = "非法请求";
	private static String david_openid = "ok33DvrkARxrATpIergV2cektbt0";
	private static WxMpService wxMpService;
	private static WxMpInMemoryConfigStorage wxMpConfigStorage;
	private static WxMpMessageRouter wxMpMessageRouter;

	static {
		init();
	}

	private static void init() {

		wxMpService = new WxMpServiceImpl();

		WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
		wxMpConfigStorage.setAppId(CommonUtils.appid);
		wxMpConfigStorage.setSecret(CommonUtils.appsecrect);
		wxMpConfigStorage.setToken(CommonUtils.token);
		wxMpConfigStorage.setAesKey(CommonUtils.aeskey);

		logger.info("wxMpConfigStorage.appid: " + wxMpConfigStorage.getAppId());
		logger.info("wxMpConfigStorage.secret: " + wxMpConfigStorage.getSecret());
		logger.info("wxMpConfigStorage.token: " + wxMpConfigStorage.getToken());
		logger.info("wxMpConfigStorage.aeskey: " + wxMpConfigStorage.getAesKey());

		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

		WxMpMessageHandler handler = new WxMpMessageHandler() {

			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager)
					throws WxErrorException {
				WxMpXmlOutMessage m = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
						.build();
				return m;
			}

		};

		logger.info("handler: " + handler);

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule().async(false).content("哈哈") // 拦截内容为“哈哈”的消息
				.handler(handler).end();

		logger.info("wxMpMessageRouter: " + wxMpMessageRouter);
	}

	@ResponseBody
	@RequestMapping(value = "/wechat/demo", method = RequestMethod.GET)
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

	@ResponseBody
	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public void wechat(HttpServletRequest request, HttpServletResponse response) {

		try {
			logger.info("wechat.test: " + wxMpService.userInfo(david_openid, "zh_CN"));

			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);

			String msgSignature = request.getParameter("signature");
			String nonce = request.getParameter("nonce");
			String timestamp = request.getParameter("timestamp");
			String echostr = request.getParameter("echostr");

			if (StringUtils.isBlank(msgSignature)) {
				logger.info("msgSignature: " + msgSignature);
				response.getWriter().println("msgSignature is null");
			}

			if (StringUtils.isBlank(nonce)) {
				logger.info("nonce: " + nonce);
				response.getWriter().println("nonce is null");
			}

			if (StringUtils.isBlank(timestamp)) {
				logger.info("timestamp: " + timestamp);
				response.getWriter().println("timestamp is null");
			}

			if (StringUtils.isNotBlank(echostr)) {
				if (!wxMpService.checkSignature(msgSignature, timestamp, nonce)) {
					// 消息签名不正确，说明不是公众平台发过来的消息

					logger.info("");
					response.getWriter().println(ILLGAL_REQUEST);
				}
				WxMpCryptUtil cryptUtil = new WxMpCryptUtil(wxMpConfigStorage);
				String plainText = cryptUtil.decrypt(echostr);

				logger.info("plainText: " + plainText);

				// 说明是一个仅仅用来验证的请求，回显echostr
				response.getWriter().println(plainText);
			}

			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			if (outMessage != null) {
				response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
