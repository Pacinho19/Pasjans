package pl.pacinho.pasjans.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pacinho.pasjans.model.enums.CardRank;
import pl.pacinho.pasjans.model.enums.CardSuit;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CardDto {

    private CardSuit suit;
    private CardRank rank;


}