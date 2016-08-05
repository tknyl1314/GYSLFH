package com.otitan.entity;

public class Record {
	private Integer rId;
	private String fromUser;
	private String toUser;
	private String content;
	private String rTime;
	private Integer isReaded;

	public Integer getrId() {
		return rId;
	}

	public void setrId(Integer rId) {
		this.rId = rId;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getrTime() {
		return rTime;
	}

	public void setrTime(String rTime) {
		this.rTime = rTime;
	}

	public Integer getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(Integer isReaded) {
		this.isReaded = isReaded;
	}
}
