package pl.pacinho.pasjans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.pasjans.exception.GameNotFoundException;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.CardMoveDto;
import pl.pacinho.pasjans.model.dto.ColumnCardDto;
import pl.pacinho.pasjans.model.entity.CardGroup;
import pl.pacinho.pasjans.model.entity.Game;
import pl.pacinho.pasjans.model.enums.CardRank;
import pl.pacinho.pasjans.model.enums.CardSuit;
import pl.pacinho.pasjans.repository.GameRepository;

import java.util.*;
import java.util.stream.Collectors;
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
    }

    private List<ColumnCardDto> getCardColumn(Stack<CardDto> cards, Integer i) {
        return IntStream.rangeClosed(1, i)
                .boxed()
                .map(j -> new ColumnCardDto(j.equals(i), cards.pop()))
                .collect(Collectors.toList());
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
            if (addToGroup) {
                game.incrementMoveCount();
                removeFromStack(game, cardFromStack);
            }

            return addToGroup;
        }

        CardDto cardFromColumn = getCardFromColumn(game, cardDto);
        if (cardFromColumn != null) {
            boolean addToGroup = addToGroup(game, cardDto);
            if (addToGroup) {
                game.incrementMoveCount();
                removeFromColumn(game, cardFromColumn);
            }

            return addToGroup;
        }
        return false;
    }

    private void removeFromColumn(Game game, CardDto cardDto) {
        Optional<List<ColumnCardDto>> cardColumnForCardOpt = getCardColumnForCard(game, cardDto, false);
        if (cardColumnForCardOpt.isEmpty())
            return;

        List<ColumnCardDto> cardColumnForCard = cardColumnForCardOpt.get();
        cardColumnForCard.remove(findColumnCardByCardDto(cardColumnForCard, cardDto));

        setVisibleCardTopOfColumn(cardColumnForCard);
    }

    private ColumnCardDto findColumnCardByCardDto(List<ColumnCardDto> cardColumnForCard, CardDto cardDto) {
        return cardColumnForCard.stream()
                .filter(c -> c.getCardDto().equals(cardDto))
                .findFirst()
                .orElse(null);
    }

    private void setVisibleCardTopOfColumn(List<ColumnCardDto> cardColumnForCard) {
        if (cardColumnForCard.isEmpty())
            return;

        cardColumnForCard.get(cardColumnForCard.size() - 1)
                .setVisible(true);

    }

    private Optional<List<ColumnCardDto>> getCardColumnForCard(Game game, CardDto cardDto, boolean checkOnTop) {
        return game.getCardsColumns()
                .stream()
                .filter(list -> checkOnTop ? checkColumnContainsCardOnTop(list, cardDto) : checkColumnContainsCard(list, cardDto))
                .findFirst();
    }

    private boolean checkColumnContainsCard(List<ColumnCardDto> list, CardDto cardDto) {
        return list.stream()
                .anyMatch(c -> c.getCardDto().equals(cardDto));
    }

    private CardDto getCardFromColumn(Game game, CardDto cardDto) {
        Optional<List<ColumnCardDto>> cardsColumOpt = getCardColumnForCard(game, cardDto, true);

        if (cardsColumOpt.isEmpty())
            return null;

        return getCardFromSpecificColumn(cardsColumOpt.get(), cardDto);
    }

    private CardDto getCardFromSpecificColumn(List<ColumnCardDto> columnCardDtos, CardDto cardDto) {
        return columnCardDtos.stream()
                .map(ColumnCardDto::getCardDto)
                .filter(c -> c != null && c.equals(cardDto))
                .findFirst()
                .orElse(null);
    }

    private boolean checkColumnContainsCardOnTop(List<ColumnCardDto> list, CardDto cardDto) {
        if (list.isEmpty()) return false;
        return list.get(list.size() - 1).getCardDto().equals(cardDto);
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

    public void moveCards(Game game, CardMoveDto cardMoveDto) {
        List<ColumnCardDto> targetColumn;

        boolean isKingMoveEmptyField = checkMoveKingInEmptyField(cardMoveDto);
        if (isKingMoveEmptyField) {
            targetColumn = getTargetColumnByNumber(game, cardMoveDto.getColumnNumber());

            if (targetColumn == null || !targetColumn.isEmpty())
                return;

        } else {

            if (cardMoveDto.getSecondCard() == null)
                return;

            boolean isDifferentColors = checkIsDifferentColors(cardMoveDto);
            if (!isDifferentColors)
                return;

            boolean isCardsRankCorrect = checkCardRank(cardMoveDto);
            if (!isCardsRankCorrect)
                return;

            Optional<List<ColumnCardDto>> targetColumnOpt = getCardColumnForCard(game, cardMoveDto.getSecondCard(), true);
            if (targetColumnOpt.isEmpty())
                return;

            targetColumn = targetColumnOpt.get();
        }

        List<CardDto> cardsToMove = getAllCardsAbove(game, cardMoveDto.getFirstCard());
        if (!cardsToMove.isEmpty())
            game.incrementMoveCount();

        List<ColumnCardDto> finalTargetColumn = targetColumn;
        cardsToMove.forEach(cardDto -> {
            CardDto cardFromStack = getCardFromStack(game, cardDto);
            if (cardFromStack != null) {
                removeFromStack(game, cardFromStack);
            } else {
                removeFromColumn(game, cardDto);
            }

            finalTargetColumn.add(new ColumnCardDto(true, cardDto));
        });

    }

    private List<ColumnCardDto> getTargetColumnByNumber(Game game, Integer columnNumber) {
        if (columnNumber < 1 || columnNumber > game.getCardsColumns().size())
            return null;

        return game.getCardsColumns().get(columnNumber - 1);
    }

    private boolean checkMoveKingInEmptyField(CardMoveDto cardMoveDto) {
        return cardMoveDto.getSecondCard() == null
               && cardMoveDto.getFirstCard().getRank() == CardRank.KING;
    }

    private List<CardDto> getAllCardsAbove(Game game, CardDto firstCard) {
        if (getCardFromStack(game, firstCard) != null)
            return List.of(firstCard);

        Optional<List<ColumnCardDto>> columnOpt = getCardColumnForCard(game, firstCard, false);
        if (columnOpt.isEmpty())
            return Collections.emptyList();

        List<CardDto> out = new ArrayList<>();
        boolean add = false;
        for (ColumnCardDto columnCard : columnOpt.get()) {
            if (columnCard.getCardDto().equals(firstCard)) {
                add = true;
            }

            if (add) out.add(columnCard.getCardDto());
        }
        return out;
    }

    private boolean checkCardRank(CardMoveDto cardMoveDto) {
        CardDto firstCard = cardMoveDto.getFirstCard();
        CardDto secondCard = cardMoveDto.getSecondCard();
        return firstCard != null
               && secondCard != null
               && secondCard.getRank().getValue() - firstCard.getRank().getValue() == 1;
    }

    private boolean checkIsDifferentColors(CardMoveDto cardMoveDto) {
        CardDto firstCard = cardMoveDto.getFirstCard();
        CardDto secondCard = cardMoveDto.getSecondCard();
        return firstCard != null
               && secondCard != null
               && firstCard.getSuit().getColor() != secondCard.getSuit().getColor();
    }

    public boolean isGameOver(Game game) {
        if (!game.getStack().getCards().isEmpty())
            return false;

        return game.getCardsColumns()
                .stream()
                .allMatch(List::isEmpty);
    }
}