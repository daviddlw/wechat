package com.hupu.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);
	public static boolean wechatFlag = true;
	public static String appid = "wx63ac9d2096253fc0";
	public static String appsecrect = "5c1ab5f0bd3872b06a823eb7c532c311";
	public static String token = "arenacloud_token_1";
	public static String aeskey = "QG7OitGVpU344C8HmYOI8AbPfFbDW4p0cu0eg5oxYIA";

	static {
		try {
			PropertiesConfiguration conf = new PropertiesConfiguration("config.properties");
			appid = conf.getString("wechat.appid", appid);
			appsecrect = conf.getString("wechat.appsecrect", appsecrect);
			token = conf.getString("wechat.apptoken", token);
			aeskey = conf.getString("wechat.aeskey", aeskey);
		} catch (ConfigurationException e) {
			logger.error(e.getMessage(), e);
		}

	}

}
