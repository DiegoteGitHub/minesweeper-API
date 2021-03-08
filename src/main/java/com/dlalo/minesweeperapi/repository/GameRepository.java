package com.dlalo.minesweeperapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlalo.minesweeperapi.model.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {
	@Query("{'username': ?0}")
	List<Game> findByUser(final String username);
}
