package com.hupu.service;

import com.hupu.dto.UserInfoDTO;

public interface UserInfoService {
	
	int insertUserInfo(UserInfoDTO userInfoDTO);
	
	int deleteUserInfo(int id);
	
	UserInfoDTO queryUserInfo(int id);
	
	UserInfoDTO queryUserInfo(String cellphone);
}
