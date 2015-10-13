package com.hupu.test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserSummary;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
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
	public void testGetTicket() {
		Response resp = ClientBuilder.newClient().target("http://socialmedia.hupu.com/masterkong/dongYun").request(MediaType.APPLICATION_JSON).get();
		String result = resp.readEntity(String.class);
		System.err.println();
		Gson gson = new Gson();
		Map<String, Object> rsMap = gson.fromJson(result, Map.class);
		System.err.println(rsMap.get("ticket"));
	}
	
	@Test
	public void test() {
		String url = "a,b";
		System.err.println(StringUtils.substringBefore(url, ","));
		
		String url2="http://events.arenacloud.com/wechat-events/home/";
		System.err.println(StringUtils.replace(url2, "/wechat-events", ""));
	}

	@Test
	public void testCheckSign() {

		String timestamp = "14447193171";
		String nonce = "292651773";
		String signature = "6a594990fe62cdadd40f18d5d4ba152a28012943";
		boolean flag = wxMpService.checkSignature(timestamp, nonce, signature);
		System.err.println(flag);
		System.err.println(flag == true);

		String echostr = "7793887707463023370";

		/*
		 * WxMpInMemoryConfigStorage wxMpConfigStorage = new
		 * WxMpInMemoryConfigStorage();
		 * wxMpConfigStorage.setAppId(CommonUtils.appid);
		 * wxMpConfigStorage.setSecret(CommonUtils.appsecrect);
		 * wxMpConfigStorage.setToken(CommonUtils.token);
		 * wxMpConfigStorage.setAesKey(CommonUtils.aeskey);
		 */

	}

	@Test
	public void testWriteFile() throws IOException {
		String path = "F:" + File.separator + "accessToken.txt";
		File file = new File(path);
		FileUtils.writeStringToFile(file, "weixintoken=aaa");
		
		String result = FileUtils.readFileToString(file);
		System.err.println(result);
	}

	private byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	private String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
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

		Calendar sc = Calendar.getInstance();
		sc.set(2015, 9, 8, 0, 0, 0);

		Calendar ec = Calendar.getInstance();
		ec.set(2015, 9, 10, 23, 59, 59);

		Date st = sc.getTime();
		Date et = ec.getTime();

		List<WxMpUserSummary> ls = wxMpService.getUserSummary(st, et);
		System.err.println(ls.size());
		for (WxMpUserSummary item : ls) {
			System.err.println(item);
		}

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
