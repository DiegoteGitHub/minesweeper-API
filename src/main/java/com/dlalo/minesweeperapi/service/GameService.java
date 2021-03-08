package com.dlalo.minesweeperapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dlalo.minesweeperapi.model.CreateRequest;
import com.dlalo.minesweeperapi.model.EventRequest;
import com.dlalo.minesweeperapi.model.Game;
import com.dlalo.minesweeperapi.repository.GameRepository;

@Service
public class GameService {

	private final GameRepository gameRepository;

	public GameService(final GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game create(final CreateRequest aRequest) {
		if (BigDecimal.valueOf(aRequest.getRows()).multiply(BigDecimal.valueOf(aRequest.getColumns()))
				.compareTo(BigDecimal.valueOf(aRequest.getMines())) < 0 || aRequest.getMines() < 0) {
			throw new RuntimeException("Invalid number of mines");
		}
		final Game newGame = Game.builder().status("NEW").username(aRequest.getUsername()).rows(aRequest.getRows())
				.columns(aRequest.getColumns()).mines(aRequest.getMines()).build();
		newGame.start();
		return this.gameRepository.save(newGame);
	}

	public Game findGameById(final String id) {
		return this.gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
	}

	public List<Game> findGamesByUsername(final String username) {
		return this.gameRepository.findByUser(username);
	}

	public Game pause(final String id) {
		Game game = findGameById(id);
		if (game.isGameOver()) {
			throw new RuntimeException("The Game finished, can not update it.");
		}
		game.togglePause();
		return this.gameRepository.save(game);
	}

	public Game openCell(final EventRequest request) {
		Game game = findGameById(request.getGameId());
		if (game.isPaused()) {
			throw new RuntimeException("The Game is paused, can not update it.");
		}
		if (game.isGameOver()) {
			throw new RuntimeException("The Game finished, can not update it.");
		}
		game.openCell(request.getPosX(), request.getPosY());
		return this.gameRepository.save(game);
	}

	public Game flagCell(EventRequest request) {
		Game game = findGameById(request.getGameId());
		if (game.isPaused()) {
			throw new RuntimeException("The Game is paused, can not update it.");
		}
		if (game.isGameOver()) {
			throw new RuntimeException("The Game finished, can not update it.");
		}
		game.flagCell(request.getPosX(), request.getPosY());
		return this.gameRepository.save(game);
	}

	public Game markCell(EventRequest request) {
		Game game = findGameById(request.getGameId());
		if (game.isPaused()) {
			throw new RuntimeException("The Game is paused, can not update it.");
		}
		if (game.isGameOver()) {
			throw new RuntimeException("The Game finished, can not update it.");
		}
		game.markCell(request.getPosX(), request.getPosY());
		return this.gameRepository.save(game);
	}
}
