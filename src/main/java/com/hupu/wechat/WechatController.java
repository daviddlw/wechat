package com.hupu.wechat;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import antlr.collections.List;

import com.google.gson.Gson;
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
	private static int count = 0;
	private static Gson gson = new Gson();

	private String signature = "";
	private String nonce = "";
	private String timestamp = "";
	private String echostr = "";

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

	private void executeCheckAccess(HttpServletRequest request, HttpServletResponse response) {

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

			signature = request.getParameter("signature");
			nonce = request.getParameter("nonce");
			timestamp = request.getParameter("timestamp");
			echostr = request.getParameter("echostr");

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
					logger.info("output echostr: " + echostr.trim());

					// 说明是一个仅仅用来验证的请求，回显echostr
					response.getWriter().println(echostr.trim());
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
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
	public void wechat(HttpServletRequest request, HttpServletResponse response) {
		logger.info("execute wechat view...");
		executeCheckAccess(request, response);
	}

	@ResponseBody
	@RequestMapping(value = "/wechat/share", method = RequestMethod.GET)
	public Map<String, String> share(String dynamicUrl, HttpServletRequest request) {
		try {
			String newUrl = URLDecoder.decode(dynamicUrl, "UTF-8");
			logger.info("execute share action..." + newUrl);

			// String path =
			// "/data/pf/arenacloud/inst-8080/webapps/wechat-events/" +
			// "ticket.txt";
			String path = request.getSession().getServletContext().getRealPath("/") + "ticket.txt";
			logger.info("path: " + path);
			try {
				String jsapiTicket = wxMpService.getJsapiTicket();
				logger.info("jspapiTicket: " + jsapiTicket);
				FileUtils.write(new File(path), jsapiTicket);
			} catch (IOException | WxErrorException e) {
				logger.error(e.getMessage(), e);
			}

			String url = newUrl;
			logger.info("orginal url: " + url);

			String dealUrl = StringUtils.substringBefore(url, "#");
			logger.info("deal url: " + dealUrl);
			String finalUrl = StringUtils.substringBeforeLast(dealUrl, "&");
			finalUrl = StringUtils.replace(finalUrl, "/wechat-events", "");

			logger.info("finalUrl: " + finalUrl);

			Map<String, String> result = getWechatConfig(request, finalUrl);

			return result;
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return new HashMap<String, String>();
		}

	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {

		if (count == 0) {
			startScheduleTask(request);
			count++;
		}
		// return new ModelAndView("home", result);
		return new ModelAndView("home");
	}

	/**
	 * 启动定时任务定时扫描业务数据缓存是否有变化
	 */
	private void updateToken(HttpServletRequest request) {
		logger.info("execute updateToken start...");

		String path = request.getSession().getServletContext().getRealPath("/") + "ticket.txt";
		File file = new File(path);
		logger.info("path: " + path);
		try {
			FileUtils.write(file, wxMpService.getJsapiTicket(true));
		} catch (IOException | WxErrorException e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("execute updateToken end...");

	}

	private Map<String, String> getWechatConfig(HttpServletRequest request, String url) {
		Map<String, String> result = new HashMap<>();
		String path = request.getSession().getServletContext().getRealPath("/") + "ticket.txt";
		logger.info("path: " + path);
		try {
			String jsTicket = FileUtils.readFileToString(new File(path));
			result = getJsSinMap(jsTicket, url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 获取签名Map
	 * 
	 * @param jsTicket
	 * @param url
	 * @return
	 * @throws WxErrorException
	 */
	private Map<String, String> getJsSinMap(String jsTicket, String url) throws WxErrorException {
		logger.info("url: " + url);
		Map<String, String> map = new HashMap<String, String>();
		map = sign(jsTicket, url);
		return map;
	}

	/**
	 * 
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = createNoncestr();
		String timestamp = createTimestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		logger.info("string1 ===> " + string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e);
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonce", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		logger.info("signature ===> " + signature);
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String createNoncestr() {
		return "Wm3WZYTPz0wzccnW";
	}

	/**
	 * 
	 * @return
	 */
	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	/**
	 * 调用远程ticket
	 * 
	 * @return
	 */
	private String getRemoteTicket() {
		Response resp = ClientBuilder.newClient().target("http://socialmedia.hupu.com/masterkong/dongYun").request(MediaType.APPLICATION_JSON).get();
		String result = resp.readEntity(String.class);
		Gson gson = new Gson();
		Map<String, Object> rsMap = gson.fromJson(result, Map.class);
		String ticket = rsMap.containsKey("ticket") ? String.valueOf(rsMap.get("ticket")) : "";
		logger.info("ticket: " + ticket);
		return ticket;
	}

	private void startScheduleTask(final HttpServletRequest request) {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		Runnable scheduledTask = new Runnable() {

			@Override
			public void run() {
				updateToken(request);
			}
		};

		service.scheduleAtFixedRate(scheduledTask, 0, 7100, TimeUnit.SECONDS);
	}
}
