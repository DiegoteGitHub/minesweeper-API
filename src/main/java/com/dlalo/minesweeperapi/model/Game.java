package com.dlalo.minesweeperapi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Document
@AllArgsConstructor
@Builder
public class Game {

	@Id
	private String id;
	@CreatedDate
	private LocalDateTime created;
	@LastModifiedDate
	private LocalDateTime updated;
	private String username;
	private String status;
	private int moves;
	private int rows;
	private int columns;
	private int mines;
	private List<Cell> cells;

	public void start() {
		this.cells = new ArrayList<>();
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getColumns(); j++) {
				this.getCells().add(new Cell(i, j));
			}
		}

		// Shuffle to put mines in not adjacent positions
		Collections.shuffle(this.getCells());

		// Populate board with mines
		this.getCells().stream().limit(this.getMines()).forEach(cellMine -> cellMine.setMine(true));

		// Re-sort cells by row and then by column
		Comparator<Cell> comparator = Comparator.comparing(Cell::getPosX)
				.thenComparing(Comparator.comparing(Cell::getPosY));
		cells = cells.stream().sorted(comparator).collect(Collectors.toList());

		final Stream<Cell> aStreamOfCells = this.getCells().stream().filter((cell) -> !cell.isMine());
		aStreamOfCells.forEach((cell) -> cell.setNumOfAdjacentMines(this.getNumOfAdjacentMines(cell)));
	}

	public void openCell(final int posX, final int posY) {
		final Cell theCell = this.findMeTheCellIn(posX, posY);

		if (theCell.isFlagged()) {
			return;
		}

		this.moves++;

		if (theCell.isMine()) {
			this.setStatus("GAME OVER");
			return;
		}

		openAdjacentCells(cells, theCell);

		if (this.areAllNotMineCellsOpened()) {
			this.status = "YOU WON";
		}
	}

	public void flagCell(final int posX, final int posY) {
		final Cell selectedCell = this.findMeTheCellIn(posX, posY);
		selectedCell.setFlagged(true);
	}

	public void markCell(final int posX, final int posY) {
		final Cell selectedCell = this.findMeTheCellIn(posX, posY);
		selectedCell.setMarked(true);
	}

	public boolean isGameOver() {
		return "YOU WON".equals(this.status) || "GAME OVER".equals(this.status);
	}

	public boolean isPaused() {
		return "PAUSED".equals(this.status);
	}

	public void togglePause() {
		if (this.isPaused()) {
			this.setStatus("RESUME");
		} else {
			this.setStatus("PAUSED");
		}
	}

	public void openAdjacentCells(final List<Cell> cells, Cell theCell) {
		theCell.setOpened(true);
		List<Cell> adjacentCells = cells.stream().filter(cell -> theCell.isAdjacent(cell) && cell.canBeOpened())
				.collect(Collectors.toList());

		if (adjacentCells.size() > 0) {
			adjacentCells.forEach(cell -> openAdjacentCells(cells, cell));
		}
	}

	private long getNumOfAdjacentMines(final Cell aCell) {
		Stream<Cell> filtered = this.getCells().stream().filter(other -> aCell.isAdjacent(other) && other.isMine());
		return filtered.count();
	}

	private Cell findMeTheCellIn(final int posX, final int posY) {
		final Stream<Cell> streamCell = this.getCells().stream()
				.filter(cell -> cell.getPosX() == posX && cell.getPosY() == posY);
		return streamCell.findFirst()
				.orElseThrow(() -> new RuntimeException("Cell not found for the given coordinates"));
	}

	private boolean areAllNotMineCellsOpened() {
		return this.getCells().stream().filter(cell -> !cell.isMine()).allMatch(Cell::isOpened);
	}
}