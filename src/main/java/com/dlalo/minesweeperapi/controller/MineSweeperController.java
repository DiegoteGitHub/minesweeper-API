package com.dlalo.minesweeperapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dlalo.minesweeperapi.model.CreateRequest;
import com.dlalo.minesweeperapi.model.EventRequest;
import com.dlalo.minesweeperapi.model.Game;
import com.dlalo.minesweeperapi.service.GameService;

@CrossOrigin
@RestController
@RequestMapping("/minesweeper/api")
public class MineSweeperController {

	private final GameService gameService;

	public MineSweeperController(final GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping(value = "/load/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Game loadGame(@PathVariable final String id) {
		try {
			return this.gameService.findGameById(id);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@GetMapping(value = "/load/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Game> getGamesByUser(@PathVariable final String username) {
		try {
			return this.gameService.findGamesByUsername(username);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Game create(@RequestBody final CreateRequest aRequest) {
		try {
			return this.gameService.create(aRequest);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@PutMapping(value = "/opencell", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Game recognize(@RequestBody final EventRequest request) {
		try {
			return this.gameService.openCell(request);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@PutMapping(value = "/flag", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Game flagCell(@RequestBody final EventRequest request) {
		try {
			return this.gameService.flagCell(request);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@PutMapping(value = "/pause/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Game pauseGame(@PathVariable final String id) {
		try {
			return this.gameService.pause(id);
		} catch (final Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
}
