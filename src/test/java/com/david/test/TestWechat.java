package com.david.test;

import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

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
	String openid1 = "ok33DvrkARxrATpIergV2cektbt0";
	String openid2 = "ok33DvhN86O43q0z88Vxa3wZ3_Y0";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void wechatQuickStart() {
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(appid);
		config.setSecret(appsecret);
		config.setToken(appToken);
		config.setAesKey(aesKey);

		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);

		WxMpCustomMessage message1 = WxMpCustomMessage.TEXT().toUser(openid1).content("Hello World, " + System.currentTimeMillis()).build();
//		WxMpCustomMessage message2 = WxMpCustomMessage.TEXT().toUser(openid2).content("Hello World, " + System.currentTimeMillis()).build();

		try {
			wxMpService.customMessageSend(message1);
//			wxMpService.customMessageSend(message2);
			System.err.println("执行微信成功");
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
