package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.pasjans.model.dto.CardDto;

import java.util.LinkedList;

@Getter
public class Stack {

    @Setter
    private LinkedList<CardDto> cards;
    private int currentIndex = -1;

    public int nextIndex() {
        if (cards.isEmpty()) {
            currentIndex = -1;
            return currentIndex;
        }

        if (currentIndex != cards.size() - 1)
            return ++currentIndex;

        currentIndex = -1;
        return currentIndex;
    }

    public CardDto getCurrentCard() {
        if (currentIndex == -1)
            return null;
        if (cards.isEmpty())
            return null;
        return cards.get(currentIndex);
    }

    public int prevIndex() {
        if (cards.isEmpty()) {
            currentIndex = -1;
            return currentIndex;
        }

        if (currentIndex > 0)
            return --currentIndex;

        currentIndex = cards.size() - 1;
        return currentIndex;
    }
}
