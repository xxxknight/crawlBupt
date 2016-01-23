package com.bupt.util;

public enum Constants {
	PER_NOTE_REPLY(10.0), PER_BOARD_NOTE(30.0);

	private Double state;

	private Constants(Double state) {
		this.setState(state);
	}

	public Double getState() {
		return state;
	}

	public void setState(Double state) {
		this.state = state;
	}

}
