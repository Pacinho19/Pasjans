package pl.pacinho.pasjans.model.dto.mapper;

import pl.pacinho.pasjans.model.dto.GameDto;
import pl.pacinho.pasjans.model.entity.Game;

public class GameDtoMapper {

    public static GameDto parse(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .startTime(game.getStartTime())
                .player(game.getPlayer().getName())
                .status(game.getStatus())
                .build();
    }
}