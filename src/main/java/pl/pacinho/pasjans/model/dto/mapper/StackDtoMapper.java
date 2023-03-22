package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.StackDto;
import pl.pacinho.pasjans.model.entity.Stack;

public class StackDtoMapper {
    public static StackDto parse(Stack stack) {
        return new StackDto(
                stack.getCurrentIndex() != stack.getCards().size() - 1,
                stack.getCurrentCard()
        );
    }
}
