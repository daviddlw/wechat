package com.david.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class TestWechatOpenID {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetOpenID() {
//		String redirectUrl = "http://115.159.62.132:8080/wechat-opensdk/redirectPage";
		 String redirectUrl = "http://www.qq.com";
		redirectUrl = urlEncodeUTF8(redirectUrl);
		System.err.println(redirectUrl);
		String url = String
				.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=daviddai#wechat_redirect",
						"wx63ac9d2096253fc0", redirectUrl);

		System.err.println(url);
	}

	private String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;

	}
	
	public void wechatQuick() {
		
	}

}
