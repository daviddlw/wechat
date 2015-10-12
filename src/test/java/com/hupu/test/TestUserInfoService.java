package com.hupu.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hupu.dto.UserInfoDTO;
import com.hupu.service.UserInfoService;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserInfoService {

	private long timestamp = System.currentTimeMillis();
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInsertUserInfo() {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setName("测试名称"+timestamp);
		userInfoDTO.setGender(1);
		userInfoDTO.setCellphone("123111111111");
		
		int id = userInfoService.insertUserInfo(userInfoDTO);
		System.err.println(id);
	}
	
	@Test
	public void testQueryUserInfo() {
		UserInfoDTO userInfoDTO = userInfoService.queryUserInfo(1);
		System.err.println(userInfoDTO);
	}

}
