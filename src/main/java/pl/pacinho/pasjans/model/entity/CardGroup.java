package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.enums.CardColor;

import java.util.LinkedList;

@Getter
public class CardGroup {

    private CardColor color;
    private LinkedList<CardDto> cards;

    public CardGroup(CardColor color) {
        this.color = color;
        this.cards = new LinkedList<>();
    }

    public void addCard(CardDto card) {
        this.cards.add(card);
    }
}
