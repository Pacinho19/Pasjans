package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.PlayerDto;
import pl.pacinho.pasjans.model.entity.Player;

public class PlayerDtoMapper {
    public static PlayerDto parse(Player player) {
        return new PlayerDto(
                player.getName()
        );
    }
}