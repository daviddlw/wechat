package com.hupu.test;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.junit.Before;
import org.junit.Test;

import com.hupu.utils.CommonUtils;

/**
 * 微信公众号测试
 * 
 * @author dailiwei
 * 
 */
public class TestWechat {

	String appid = "wx63ac9d2096253fc0";
	String appsecret = "5c1ab5f0bd3872b06a823eb7c532c311";
	String accessToken = "NlBWUwjRYrsKwA6WomiEjmb0v33KSbIQ4eec8yyiFWTIT2jQL7BpU3qHB-LwLQR22qesu2WdGshIuKSCWJW6TljoVuL5oVa-GPXJbU69R94";
	String appToken = "arenacloud_token_1";
	String aesKey = "QG7OitGVpU344C8HmYOI8AbPfFbDW4p0cu0eg5oxYIA";
	String david_openid = "ok33DvrkARxrATpIergV2cektbt0";
	String openid2 = "ok33DvhN86O43q0z88Vxa3wZ3_Y0";

	private WxMpService wxMpService;

	@Before
	public void setUp() throws Exception {

		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(appid);
		config.setSecret(appsecret);
		config.setToken(appToken);
		config.setAesKey(aesKey);

		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);
	}

	@Test
	public void wechatQuickStart() {

		WxMpCustomMessage message1 = WxMpCustomMessage.TEXT().toUser(david_openid).content("Hello World, " + System.currentTimeMillis()).build();
		try {
			wxMpService.customMessageSend(message1);
			System.err.println("执行微信成功");
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void wechatGetAccessToken() throws WxErrorException {
		System.err.println(wxMpService.getAccessToken());
	}

	@Test
	public void wechatMediaMessage() throws WxErrorException {
		String mediaId = "1eQnWyTLg44OX1cBEHJ7NntN9bVoKaJaq1ALz9merZZyck88dCM810Gh71hFoPzX";

		WxMpCustomMessage mediaMessage = WxMpCustomMessage.IMAGE().toUser(david_openid).mediaId(mediaId).build();
		WxMpUser wxMpUser = wxMpService.userInfo(david_openid, "zh_CN");
		System.err.println(wxMpUser);

		try {
			wxMpService.customMessageSend(mediaMessage);
			System.err.println("执行微信成功");
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void wechatUserInfo() throws WxErrorException {
		
		long id = wxMpService.userGetGroup(david_openid);
		System.err.println(id);
		
		wxMpService.userUpdateRemark(david_openid, "戴维");
		wxMpService.userUpdateGroup(david_openid, 2);
		
		WxMpUser wxMpUser = wxMpService.userInfo(david_openid, "zh_CN");
		System.err.println(wxMpUser);
		
	}
	
	@Test
	public void testWechatTest() {
		System.err.println(CommonUtils.wechatFlag);
		System.err.println(CommonUtils.appid);
		System.err.println(CommonUtils.appsecrect);
		System.err.println(CommonUtils.token);
		System.err.println(CommonUtils.aeskey);
	}
	
}
