package pl.pacinho.pasjans.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pacinho.pasjans.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Getter
@Builder
@AllArgsConstructor
public class GameDto {

    private String id;
    private GameStatus status;
    private String player;
    private LocalDateTime startTime;
    private StackDto stack;
    private LinkedList<CardDto> cardsGroups;
    private LinkedList<CardColumnDto> cardsColumns;

}