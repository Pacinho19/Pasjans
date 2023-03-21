package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.model.dto.GameDto;
import pl.pacinho.pasjans.model.dto.mapper.GameDtoMapper;
import pl.pacinho.pasjans.repository.GameRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameLogicService gameLogicService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<GameDto> getAvailableGames(String playerName) {
        return gameRepository.getAvailableGames(playerName);
    }

    public String newGame(String playerName) {
        List<GameDto> activeGames = getAvailableGames(playerName);
        if (activeGames.size() >= 10)
            throw new IllegalStateException("Cannot create new Game! Active game count : " + activeGames.size());
        return gameRepository.newGame(playerName);
    }

    public GameDto findDtoById(String gameId, String name) {
        return GameDtoMapper.parse(gameLogicService.findById(gameId));
    }

}