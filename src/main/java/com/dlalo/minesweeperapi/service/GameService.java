package com.dlalo.minesweeperapi.service;

import org.springframework.stereotype.Service;

import com.dlalo.minesweeperapi.repository.GameRepository;

@Service
public class GameService {
	
	private final GameRepository gameRepository;
	
    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    public Game create(final CreateRequest aRequest) {
        final Game newGame = Game.builder()
            .status("NEW")
            .username(aRequest.getUsername())
            .rows(aRequest.getRows())
            .columns(aRequest.getColumns())
            .mines(aRequest.getMines())
            .build();
        newGame.start();
        return this.gameRepository.save(newGame);
    }

}
