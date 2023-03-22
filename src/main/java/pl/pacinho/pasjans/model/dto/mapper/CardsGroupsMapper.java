package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.entity.CardGroup;

import java.util.LinkedList;
import java.util.Objects;

public class CardsGroupsMapper {
    public static LinkedList<CardDto> parse(LinkedList<CardGroup> cardsGroup) {
        return new LinkedList<>(
                cardsGroup.stream()
                        .map(CardsGroupsMapper::getTopCardOfGroup)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private static CardDto getTopCardOfGroup(CardGroup cg) {
        if (cg.getCards().isEmpty()) return null;
        return cg.getCards().get(cg.getCards().size() - 1);
    }
}
