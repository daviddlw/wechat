package com.david.test;

import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * 微信公众号测试
 * @author dailiwei
 *
 */
public class TestWechat {

	String appid = "wx63ac9d2096253fc0";
	String appsecret = "5c1ab5f0bd3872b06a823eb7c532c311";
	
	String accessToken = "NlBWUwjRYrsKwA6WomiEjmb0v33KSbIQ4eec8yyiFWTIT2jQL7BpU3qHB-LwLQR22qesu2WdGshIuKSCWJW6TljoVuL5oVa-GPXJbU69R94";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String jsonUrl = "http://localhost:8080/pay-api/service/status?token=3969bdaaeb844ea28db8a8fa23280d61";
		Response response = ClientBuilder.newClient().target(jsonUrl).request(MediaType.TEXT_PLAIN).get();
		String result = response.readEntity(String.class);
		System.err.println(result);
	}
	
	@Test
	public void testShowTimestamp() {
		System.err.println(System.currentTimeMillis());
	}
	
	@Test
	public void testGetDavidToken(){
		
		String davidToken = DigestUtils.md5Hex("david_token");
		System.err.println(davidToken);
	}
	
	@Test
	public void wechatQuickStart() {
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(appid);
		config.setSecret(appsecret);
	}

}
