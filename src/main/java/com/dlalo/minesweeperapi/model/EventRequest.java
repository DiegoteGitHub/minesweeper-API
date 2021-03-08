package com.dlalo.minesweeperapi.model;

import lombok.Data;

@Data
public class EventRequest {
	private String gameId;
	private int posX;
	private int posY;
}
