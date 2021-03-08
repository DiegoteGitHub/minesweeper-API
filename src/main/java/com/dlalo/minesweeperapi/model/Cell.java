package com.dlalo.minesweeperapi.model;

import lombok.Data;

@Data
public class Cell {
	private int posX;
	private int posY;
	private boolean opened;
	private boolean mine;
	private boolean flagged;
	private boolean marked;
	private long numOfAdjacentMines;

	public Cell(final int posX, final int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public boolean isAdjacent(final Cell other) {
		boolean x = Math.abs(this.getPosX() - other.getPosX()) <= 1;
		boolean y = Math.abs(this.getPosY() - other.getPosY()) <= 1;
		return this != other && x && y;
	}

	public boolean canBeOpened() {
		return !this.isOpened() && !this.isMine() && !this.isFlagged() && !this.isMarked()
				&& this.getNumOfAdjacentMines() == 0;
	}
}