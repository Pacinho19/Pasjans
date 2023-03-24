package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.CardColumnDto;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.ColumnCardDto;

import java.util.LinkedList;
import java.util.List;

public class CardsColumnsMapper {
    public static LinkedList<List<ColumnCardDto>> parse(LinkedList<List<ColumnCardDto>> cardsColumns) {
        return new LinkedList<>(
                cardsColumns.stream()
                        .map(CardsColumnsMapper::mapToDtos)
                        .toList()
        );
    }

    private static List<ColumnCardDto> mapToDtos(List<ColumnCardDto> cards) {
        return cards.stream()
                .map(c -> new ColumnCardDto(c.isVisible(), c.isVisible() ? c.getCardDto() : null))
                .toList();
    }

    private static CardDto getTopOfStack(List<CardDto> cards) {
        return cards.isEmpty()
                ? null
                : cards.get(cards.size() - 1);
    }
}
