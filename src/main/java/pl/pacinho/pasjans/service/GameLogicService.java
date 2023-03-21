package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.exception.GameNotFoundException;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.entity.Game;
import pl.pacinho.pasjans.model.enums.CardRank;
import pl.pacinho.pasjans.model.enums.CardSuit;
import pl.pacinho.pasjans.repository.GameRepository;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final List<CardDto> CARDS_DECK = initCards();

    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                ;
    }

    private List<CardDto> initCards() {
        return Arrays.stream(CardSuit.values())
                .flatMap(cs -> Arrays.stream(CardRank.values())
                        .map(cr -> new CardDto(cs, cr)))
                .toList();
    }
}