package com.hupu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hupu.dao.UserInfoDAO;
import com.hupu.dto.UserInfoDTO;

@Service
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Override
	public int insertUserInfo(UserInfoDTO userInfoDTO) {
		userInfoDAO.insertUserInfo(userInfoDTO);
		return userInfoDTO.getId();
	}

	@Override
	public UserInfoDTO queryUserInfo(int id) {
		return userInfoDAO.queryUserInfo(id);
	}

	@Override
	public UserInfoDTO queryUserInfo(String cellphone) {
		return userInfoDAO.queryUserInfoByPhone(cellphone);
	}

	@Override
	public int deleteUserInfo(int id) {
		return userInfoDAO.deleteUserInfo(id);
	}

}
