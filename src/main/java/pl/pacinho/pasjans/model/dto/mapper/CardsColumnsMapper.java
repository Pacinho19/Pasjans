package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.CardColumnDto;
import pl.pacinho.pasjans.model.dto.CardDto;

import java.util.LinkedList;
import java.util.List;

public class CardsColumnsMapper {
    public static LinkedList<CardColumnDto> parse(LinkedList<List<CardDto>> cardsColumns) {
        return new LinkedList<>(
                cardsColumns.stream()
                        .map(cards -> new CardColumnDto(cards.size(), getTopOfStack(cards)))
                        .toList()
        );
    }

    private static CardDto getTopOfStack(List<CardDto> cards) {
        return cards.isEmpty()
                ? null
                : cards.get(cards.size() - 1);
    }
}
