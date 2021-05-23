package com.elearning.model;

import java.util.List;

public class ChangeRuleGame {
	private String gameKey;
	private GameType gameType;
	private List<Integer> hsk;
	private List<Integer> lessons;
	private Level level;
	public String getGameKey() {
		return gameKey;
	}
	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}
	public GameType getGameType() {
		return gameType;
	}
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	public List<Integer> getHsk() {
		return hsk;
	}
	public void setHsk(List<Integer> hsk) {
		this.hsk = hsk;
	}
	public List<Integer> getLessons() {
		return lessons;
	}
	public void setLessons(List<Integer> lessons) {
		this.lessons = lessons;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
}
