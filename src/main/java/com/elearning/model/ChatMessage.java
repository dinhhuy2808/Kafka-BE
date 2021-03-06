package com.elearning.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ChatMessage extends KafkaMessage {
	
	private String text;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm dd.MM.yyyy")
	private Date time;
	
	private String user;
	
	private String room;
	
	private String userKey;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	@Override
	public String toString() {
		return "ChatMessage [text=" + text + ", time=" + time + ", user=" + user + ", room=" + room + "]";
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	
}
