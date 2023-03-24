package pl.pacinho.pasjans.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ColumnCardDto {

    @Setter
    private boolean visible;
    private CardDto cardDto;

    public ColumnCardDto(boolean visible, CardDto cardDto) {
        this.visible = visible;
        this.cardDto = cardDto;
    }
}
