package com.hupu.dto;

import java.util.Date;

/**
 * 用户信息
 * 
 * @author dailiwei
 * 
 */
public class UserInfoDTO {
	/**
	 * 自增ID
	 */
	private int id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别（1-男，2-女）
	 */
	private int gender;
	/**
	 * 手机号
	 */
	private String cellphone;
	/**
	 * 记录创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 状态
	 */
	private int status;

	public UserInfoDTO() {
		super();
		id = 0;
		name = "";
		gender = 0;
		cellphone = "";
		createTime = new Date();
		updateTime = new Date();
		status = 1;
	}

	public UserInfoDTO(int id, String name, int gender, String cellphone, Date createTime, Date updateTime, int status) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.cellphone = cellphone;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserInfoDTO [id=" + id + ", name=" + name + ", gender=" + gender + ", cellphone=" + cellphone + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", status=" + status + "]";
	}

}
