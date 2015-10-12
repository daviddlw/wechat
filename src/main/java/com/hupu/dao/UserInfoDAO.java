package com.hupu.dao;

import com.hupu.dto.UserInfoDTO;

public interface UserInfoDAO {

	int insertUserInfo(UserInfoDTO userInfoDTO);
	
	UserInfoDTO queryUserInfo(int id);
	
}
