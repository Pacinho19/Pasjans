package pl.pacinho.pasjans.repository;

import org.springframework.stereotype.Repository;
import pl.pacinho.pasjans.model.dto.GameDto;
import pl.pacinho.pasjans.model.dto.mapper.GameDtoMapper;
import pl.pacinho.pasjans.model.entity.Game;
import pl.pacinho.pasjans.model.enums.GameStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepository {
    private Map<String, Game> gameMap;

    public GameRepository() {
        gameMap = new HashMap<>();
    }

    public Game newGame(String playerName) {
        Game game = new Game(playerName);
        gameMap.put(game.getId(), game);
        return game;
    }

    public List<GameDto> getAvailableGames(String name) {
        return gameMap.values()
                .stream()
                .filter(game -> game.getStatus() != GameStatus.FINISHED && game.getPlayer().getName().equals(name))
                .map(GameDtoMapper::parse)
                .toList();
    }

    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(gameMap.get(gameId));
    }

}