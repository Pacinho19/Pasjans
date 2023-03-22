package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.exception.GameNotFoundException;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.entity.Game;
import pl.pacinho.pasjans.model.enums.CardRank;
import pl.pacinho.pasjans.model.enums.CardSuit;
import pl.pacinho.pasjans.repository.GameRepository;

import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final List<CardDto> CARDS_DECK = initCards();
    private final Integer CARD_COLUMNS_COUNT = 7;

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

    public void drawCards(Game game) {
        Stack<CardDto> cards = new Stack<>();
        cards.addAll(mixedCards(CARDS_DECK));

        IntStream.rangeClosed(1, CARD_COLUMNS_COUNT)
                .boxed()
                .map(i -> getCardColumn(cards, i))
                .forEach(game::addCardColumn);

        game.getStack()
                .setCards(new LinkedList<>(cards));

        int x = 0;
    }

    private List<CardDto> getCardColumn(Stack<CardDto> cards, Integer i) {
        return IntStream.rangeClosed(1, i)
                .boxed()
                .map(j -> cards.pop())
                .toList();
    }

    private List<CardDto> mixedCards(List<CardDto> cards_deck) {
        ArrayList<CardDto> cards = new ArrayList<>(cards_deck);
        Collections.shuffle(cards);
        return cards;
    }
}