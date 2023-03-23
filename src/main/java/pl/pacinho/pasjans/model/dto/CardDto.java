package pl.pacinho.pasjans.model.dto;

import lombok.*;
import pl.pacinho.pasjans.model.enums.CardRank;
import pl.pacinho.pasjans.model.enums.CardSuit;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CardDto {

    private CardSuit suit;
    private CardRank rank;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardDto cardDto = (CardDto) o;

        if (!suit.name().equals(cardDto.getSuit().name())) return false;
        return rank.name().equals(cardDto.rank.name());
    }

    @Override
    public int hashCode() {
        int result = suit.name().hashCode();
        result = 31 * result + rank.name().hashCode();
        return result;
    }
}