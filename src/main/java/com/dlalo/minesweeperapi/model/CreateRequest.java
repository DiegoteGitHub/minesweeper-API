package com.dlalo.minesweeperapi.model;

import lombok.Data;

@Data
public class CreateRequest {
	private String username;
	private int rows;
	private int columns;
	private int mines;
}
