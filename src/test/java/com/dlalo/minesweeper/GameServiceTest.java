package com.dlalo.minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlalo.minesweeperapi.model.CreateRequest;
import com.dlalo.minesweeperapi.model.EventRequest;
import com.dlalo.minesweeperapi.model.Game;
import com.dlalo.minesweeperapi.repository.GameRepository;
import com.dlalo.minesweeperapi.service.GameService;

/**
 * GameServiceTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @MockBean
    private GameRepository gameRepository;

    private GameService gameService;

    private Game gameCreated;
    private Game gameFound;

    @Before
    public void setup() {
        this.gameCreated = Game.builder()
            .id("clalo-888")
            .created(LocalDateTime.now())
            .status("NEW")
            .username("Diego")
            .rows(3)
            .columns(4)
            .mines(10)
            .build();

        this.gameFound = Game.builder()
            .id("dlalo-game1")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .status("NEW")
            .username("Diego")
            .rows(8)
            .columns(5)
            .mines(10)
            .build();    
    }

    @Test
    public void createGame_ok() {
        this.gameService = new GameService(gameRepository);

        CreateRequest request = new CreateRequest();
        request.setUsername("Diego");
        request.setColumns(4);
        request.setRows(3);
        request.setMines(10);

        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(this.gameCreated);
        Game newGame = this.gameService.create(request);

        assertNotNull(newGame);
        assertEquals(request.getRows(), newGame.getRows());
        assertEquals(request.getUsername(), newGame.getUsername());
    }

    @Test
    public void findGameById_should_find_id_ok() {
        this.gameService = new GameService(gameRepository);

        final String theID = "dlalo-game1";
        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(gameRepository.findById(theID)).thenReturn(optionalGame);

        Game aGame = this.gameService.findGameById(theID);
        assertNotNull(aGame);
        assertEquals(aGame.getId(), this.gameFound.getId());
    }

    @Test
    public void findGameById_should_fail_not_valid_id() {
        this.gameService = new GameService(gameRepository);

        final String theID = "dlalo-game1";
        Exception ex = new RuntimeException("Game not found");
        Mockito.when(gameRepository.findById(theID)).thenThrow(ex);

        Game aGame = null;
        try {
            aGame = this.gameService.findGameById(theID);
        } catch (Exception e) {
            assertNull(aGame);
            assertEquals(e.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void findGamesByUsername_should_find_a_username_ok() {
        this.gameService = new GameService(gameRepository);

        final String me = "Diego";
        List<Game> games = new ArrayList<>();
        games.add(this.gameFound);
    
        Mockito.when(gameRepository.findByUser(me)).thenReturn(games);

        List<Game> gamesFound = this.gameService.findGamesByUsername(me);
        assertNotNull(gamesFound);
        assertEquals(gamesFound.get(0).getUsername(), this.gameFound.getUsername());
    }

    @Test
    public void pause_ok() {
        this.gameService = new GameService(gameRepository);

        final String theID = "dlalo-game1";
        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(this.gameRepository.findById(theID)).thenReturn(optionalGame);

        Game gameDB = Game.builder()
            .id("dlalo-game1")
            .updated(LocalDateTime.now())
            .status("PAUSED")
            .username("Diego")
            .rows(3)
            .columns(4)
            .mines(10)
            .build();
        Mockito.when(this.gameRepository.save(any(Game.class))).thenReturn(gameDB);
        
        Game gameSaved = this.gameService.pause(theID);
        assertNotNull(gameSaved);
        assertEquals(gameDB.getStatus(), gameSaved.getStatus());
    }

    @Test
    public void discover_ok() {
        final String theID = "dlalo-game1";
        this.gameService = new GameService(gameRepository);

        this.gameFound.start();

        Optional<Game> optionalGame = Optional.of(this.gameFound);
        Mockito.when(gameRepository.findById(theID)).thenReturn(optionalGame);

        Game gameDB = Game.builder()
            .id("dlalo-game1")
            .updated(LocalDateTime.now())
            .status("RESUME")
            .username("Diego")
            .moves(10)
            .rows(3)
            .columns(4)
            .mines(10)
            .build();

        Mockito.when(this.gameRepository.save(any(Game.class))).thenReturn(gameDB);

        final EventRequest request = new EventRequest();
        request.setGameId(theID);
        request.setPosX(2);
        request.setPosY (3);
        
        Game theGame = this.gameService.openCell(request);
        assertNotNull(theGame);
        assertEquals(gameDB.getId(), theGame.getId());
    }
}