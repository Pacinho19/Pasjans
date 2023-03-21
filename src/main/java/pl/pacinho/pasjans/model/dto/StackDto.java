package pl.pacinho.pasjans.model.dto;

import lombok.Setter;

import java.util.LinkedList;

public class StackDto {

    @Setter
    private LinkedList<CardDto> cards;
    private int currentIndex = 0;

    public int nextIndex() {
        if (currentIndex == cards.size() - 1)
            return -1;
        return ++currentIndex;
    }
}
