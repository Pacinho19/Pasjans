package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.entity.CardGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.IntStream;

public class CardsGroupsMapper {
    public static Map<Integer, CardDto> parse(LinkedList<CardGroup> cardsGroup) {
        return IntStream.rangeClosed(0, 3)
                .boxed()
                .collect(HashMap::new, (m, v)->m.put(v,  getValueByIndex(v, cardsGroup)), HashMap::putAll);
    }

    private static CardDto getValueByIndex(Integer index, LinkedList<CardGroup> cardsGroup) {
        if (index > cardsGroup.size() - 1) return null;
        return getTopCardOfGroup(cardsGroup.get(index));
    }

    private static CardDto getTopCardOfGroup(CardGroup cg) {
        if (cg.getCards().isEmpty()) return null;
        return cg.getCards().get(cg.getCards().size() - 1);
    }
}
