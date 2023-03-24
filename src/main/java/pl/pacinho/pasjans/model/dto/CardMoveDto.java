package pl.pacinho.pasjans.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardMoveDto {

    private CardDto firstCard;
    private CardDto secondCard;
    private Integer columnNumber;
}
