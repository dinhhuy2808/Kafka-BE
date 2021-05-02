package com.elearning.model;

import java.util.List;

public class GameRequest extends KafkaMessage{
	private List<Gamer> gamers;
	private String hostName;
	private String gameRoomKey;
	
	public List<Gamer> getGamers() {
		return gamers;
	}
	public void setGamers(List<Gamer> gamers) {
		this.gamers = gamers;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getGameRoomKey() {
		return gameRoomKey;
	}
	public void setGameRoomKey(String gameRoomKey) {
		this.gameRoomKey = gameRoomKey;
	}
	
}
