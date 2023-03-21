package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.StackDto;
import pl.pacinho.pasjans.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.UUID;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    private StackDto stack;
    private Player player;
    private LocalDateTime startTime;

    public Game(String player1) {
        this.player = new Player(player1, 1);
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
    }

}