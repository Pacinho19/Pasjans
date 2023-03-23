package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.enums.CardSuit;

import java.util.LinkedList;

@Getter
public class CardGroup {

    private CardSuit suit;
    private LinkedList<CardDto> cards;

    public CardGroup(CardSuit suit) {
        this.suit = suit;
        this.cards = new LinkedList<>();
    }

    public CardDto getTopOfGroup() {
        if(cards.isEmpty()) return null;
        return cards.get(cards.size()-1);
    }
}
