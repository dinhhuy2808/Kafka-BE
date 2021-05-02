package com.elearning.model;

public class GameRequestResponse {
	private String hostName;
	private String roomKey;
	
	public GameRequestResponse(String hostName, String roomKey) {
		this.hostName = hostName;
		this.roomKey = roomKey;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getRoomKey() {
		return roomKey;
	}
	public void setRoomKey(String roomKey) {
		this.roomKey = roomKey;
	}
}
