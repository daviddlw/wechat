package com.hupu.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import com.hupu.utils.CommonUtils;

public class WechatUitls {

	private static WxMpService wxMpService;
	private static WxMpInMemoryConfigStorage wxMpConfigStorage;
	private static WxMpMessageRouter wxMpMessageRouter;
	private static Logger logger = Logger.getLogger(WechatController.class);

	static {
		init();
	}

	private static void init() {

		wxMpService = new WxMpServiceImpl();

		wxMpConfigStorage = new WxMpInMemoryConfigStorage();
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

	public static String checkToken(HttpServletRequest request, String token) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		return checkToken(signature, timestamp, nonce, echostr, token);
	}

	/**
	 * 微信token校验
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param token
	 * @return
	 */
	public static String checkToken(String signature, String timestamp, String nonce, String echostr, String token) {
		boolean flag = wxMpService.checkSignature(timestamp, nonce, signature);
		if (flag) {
			return echostr;
		} else {
			return "";
		}
	}

	/**
	 * 获取access_token
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		try {
			return wxMpService.getAccessToken();
		} catch (WxErrorException e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	/**
	 * 获取jsticket
	 * 
	 * @return
	 * @throws WxErrorException 
	 */
	public static String getJsTicket(String token) throws WxErrorException {
		return wxMpService.getJsapiTicket();
	}

}
