package com.hupu.wechat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.hupu.dto.UserInfoDTO;
import com.hupu.service.UserInfoService;
import com.hupu.utils.CommonUtils;

@Controller
public class WechatController {

	private static Logger logger = Logger.getLogger(WechatController.class);
	private static final String ILLGAL_REQUEST = "非法请求";
	private static String david_openid = "ok33DvrkARxrATpIergV2cektbt0";
	private static WxMpService wxMpService;
	private static WxMpInMemoryConfigStorage wxMpConfigStorage;
	private static WxMpMessageRouter wxMpMessageRouter;

	@Autowired
	private UserInfoService userInfoService;

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

	@ResponseBody
	@RequestMapping(value = "/wechat/checkIn", method = RequestMethod.GET)
	public String checkIn(HttpServletRequest request, HttpServletResponse response) {
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String echostr = request.getParameter("echostr");

		logger.info("signature: " + signature);
		logger.info("nonce: " + nonce);
		logger.info("timestamp: " + timestamp);
		logger.info("echostr: " + echostr);

		boolean isValid = wxMpService.checkSignature(timestamp, nonce, signature);
		logger.info("isValid: " + isValid);
		return String.valueOf(isValid);
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
	@RequestMapping(value = "/wechat/saveUserInfo", method = RequestMethod.POST)
	public Map<String, Object> saveUserInfo(String name, Integer sex, String phone) {
		Map<String, Object> resultMap = new HashMap<>();
		logger.info("name: " + name);
		logger.info("sex: " + sex);
		logger.info("phone: " + name);

		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setName(name);
		userInfoDTO.setGender(sex);
		userInfoDTO.setCellphone(phone);

		try {
			int id = userInfoService.insertUserInfo(userInfoDTO);
			logger.info("newid: " + id);

			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("code", "500");
		}

		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/wechat/getUserInfo", method = RequestMethod.POST)
	public UserInfoDTO getUserInfoDTO(Integer id, String phone) {
		logger.info("id: " + id);
		logger.info("phone: " + phone);

		UserInfoDTO userInfoDTO = new UserInfoDTO();
		if (id != null && id > 0) {
			userInfoDTO = userInfoService.queryUserInfo(id);
		} else {
			userInfoDTO = userInfoService.queryUserInfo(phone);
		}

		return userInfoDTO;
	}

	@ResponseBody
	@RequestMapping(value = "/wechat/deleteUserInfo", method = RequestMethod.POST)
	public Map<String, Object> deleteUserInfo(Integer id) {

		Map<String, Object> resultMap = new HashMap<>();
		logger.info("id: " + id);
		try {
			int count = userInfoService.deleteUserInfo(id);
			logger.info("delete count: " + count);
			resultMap.put("code", "200");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("code", "500");
		}

		return resultMap;
	}

	/**
	 * 验证微信签名，转跳home主页
	 * 
	 * @param request
	 *            http请求
	 * @param response
	 *            http相应
	 * @return 返回对应页面
	 */
	@ResponseBody
	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public ModelAndView wechat(HttpServletRequest request, HttpServletResponse response) {

		try {
			logger.info("wechat.test: " + wxMpService.userInfo(david_openid, "zh_CN"));
			logger.info("wxMpConfigStorage: " + wxMpConfigStorage);
			logger.info("wxMpConfigStorage.appid: " + wxMpConfigStorage.getAppId());
			logger.info("wxMpConfigStorage.secret: " + wxMpConfigStorage.getSecret());
			logger.info("wxMpConfigStorage.token: " + wxMpConfigStorage.getToken());
			logger.info("wxMpConfigStorage.aeskey: " + wxMpConfigStorage.getAesKey());
			logger.info("wxMpService: " + wxMpService + ", is null: " + (wxMpService == null));

			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);

			String signature = request.getParameter("signature");
			String nonce = request.getParameter("nonce");
			String timestamp = request.getParameter("timestamp");
			String echostr = request.getParameter("echostr");

			if (StringUtils.isBlank(signature)) {
				logger.info("msgSignature: " + signature);
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
				logger.info("signature: " + signature + ", nonce: " + nonce + ", timestamp: " + timestamp + ", echostr: " + echostr);

				if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
					// 消息签名不正确，说明不是公众平台发过来的消息
					logger.info("msg_sign is not correct");
					
					response.getWriter().println(ILLGAL_REQUEST);
				} else {
					logger.info("echostr: " + echostr);
/*					WxMpCryptUtil cryptUtil = new WxMpCryptUtil(wxMpConfigStorage);
					String plainText = cryptUtil.decrypt(echostr);*/
//					logger.info("plainText: " + plainText);

					// 说明是一个仅仅用来验证的请求，回显echostr
					response.getWriter().println(echostr);
				}
			}

			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, signature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			if (outMessage != null) {
				response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
			}

			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("signature", signature);
			queryMap.put("nonce", nonce);
			queryMap.put("timestamp", timestamp);
			queryMap.put("echostr", echostr);

			return new ModelAndView("home", queryMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return new ModelAndView("home");
	}

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView("home");
		logger.info("execute home view...");
		return mav;
	}
}
