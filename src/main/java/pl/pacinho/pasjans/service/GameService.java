package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.CardMoveDto;
import pl.pacinho.pasjans.model.dto.GameDto;
import pl.pacinho.pasjans.model.dto.GameSummaryDto;
import pl.pacinho.pasjans.model.dto.mapper.GameDtoMapper;
import pl.pacinho.pasjans.model.entity.Game;
import pl.pacinho.pasjans.model.enums.GameStatus;
import pl.pacinho.pasjans.repository.GameRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        Game game = gameRepository.newGame(playerName);
        gameLogicService.drawCards(game);
        return game.getId();
    }

    public GameDto findDtoById(String gameId) {
        return GameDtoMapper.parse(gameLogicService.findById(gameId));
    }

    public void nextStackCard(String gameId) {
        Game game = gameLogicService.findById(gameId);
        game.getStack().nextIndex();

        simpMessagingTemplate.convertAndSend("/reload-board/" + game.getId(), true);
    }

    public void addCardToGroup(String gameId, CardDto cardDto) {
        boolean success = gameLogicService.addCardToGroup(gameId, cardDto);
        if (success) {
            Game game = gameLogicService.findById(gameId);
            if (gameLogicService.isGameOver(game))
                game.setStatus(GameStatus.FINISHED);

            simpMessagingTemplate.convertAndSend("/reload-board/" + gameId, true);
        }
    }

    public void moveCards(String gameId, CardMoveDto cardMoveDto) {
        Game game = gameLogicService.findById(gameId);
        gameLogicService.moveCards(game, cardMoveDto);

        if (gameLogicService.isGameOver(game))
            game.setStatus(GameStatus.FINISHED);

        simpMessagingTemplate.convertAndSend("/reload-board/" + gameId, true);
    }

    public GameSummaryDto getGameSummary(String gameId) {
        Game game = gameLogicService.findById(gameId);

        if (game.getStatus() != GameStatus.FINISHED)
            return null;

        return new GameSummaryDto(game.getMoveCount(), ChronoUnit.MILLIS.between(game.getStartTime(), LocalDateTime.now()));
    }
}