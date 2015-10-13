package com.hupu.dao;

import com.hupu.dto.UserInfoDTO;

public interface UserInfoDAO {

	int insertUserInfo(UserInfoDTO userInfoDTO);
	
	int deleteUserInfo(int id);
	
	UserInfoDTO queryUserInfo(int id);
	
	UserInfoDTO queryUserInfoByPhone(String cellphone);
	
}
