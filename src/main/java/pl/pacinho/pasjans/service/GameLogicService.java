package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.exception.GameNotFoundException;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.entity.CardGroup;
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

    public boolean addCardToGroup(String gameId, CardDto cardDto) {
        Game game = findById(gameId);
        return checkPosition(cardDto, game);
    }

    private boolean checkPosition(CardDto cardDto, Game game) {
        CardDto cardFromStack = getCardFromStack(game, cardDto);
        if (cardFromStack != null) {
            boolean addToGroup = addToGroup(game, cardDto);
            if (addToGroup)
                removeFromStack(game, cardFromStack);

            return addToGroup;
        }
        return false;
    }

    private boolean addToGroup(Game game, CardDto cardDto) {
        if (cardDto.getRank() == CardRank.ACE) {
            getGroup(game, cardDto.getSuit()).getCards().add(cardDto);
            return true;
        }

        return tryAddToGroup(game, cardDto);
    }

    private boolean tryAddToGroup(Game game, CardDto cardDto) {
        CardGroup cardGroup = findCardGroup(game, cardDto.getSuit());
        if (cardGroup == null) return false;

        CardDto topOfGroup = cardGroup.getTopOfGroup();
        if (topOfGroup == null) return false;

        if (topOfGroup.getRank().getValue() + 1 == cardDto.getRank().getValue()) {
            cardGroup.getCards().add(cardDto);
            return true;
        }
        return false;
    }

    private CardGroup getGroup(Game game, CardSuit suit) {
        CardGroup cardGroup = findCardGroup(game, suit);

        if (cardGroup != null)
            return cardGroup;

        cardGroup = new CardGroup(suit);
        game.addCardGroup(cardGroup);
        return cardGroup;
    }

    private CardGroup findCardGroup(Game game, CardSuit suit) {
        return game.getCardsGroup()
                .stream()
                .filter(cg -> cg.getSuit() == suit)
                .findFirst()
                .orElse(null);
    }

    private void removeFromStack(Game game, CardDto cardFromStack) {
        if (cardFromStack == null) return;

        game.getStack()
                .getCards()
                .remove(cardFromStack);

        if (game.getStack().getCurrentIndex() != 0)
            game.getStack().prevIndex();

    }

    private CardDto getCardFromStack(Game game, CardDto cardDto) {
        return game.getStack()
                .getCards()
                .stream()
                .filter(c -> c.equals(cardDto))
                .findFirst()
                .orElse(null);
    }
}