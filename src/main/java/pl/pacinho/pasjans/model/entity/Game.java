package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.ColumnCardDto;
import pl.pacinho.pasjans.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    @Setter
    private Stack stack;
    private LinkedList<CardGroup> cardsGroup;
    private LinkedList<List<ColumnCardDto>> cardsColumns;
    private Player player;
    private LocalDateTime startTime;

    public Game(String player1) {
        this.player = new Player(player1, 1);
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
        this.cardsColumns = new LinkedList<>();
        this.cardsGroup = new LinkedList<>();
        this.stack = new Stack();
    }

    public void addCardColumn(List<ColumnCardDto> cards){
        this.cardsColumns.add(cards);
    }

    public void addCardGroup(CardGroup cardGroup) {
        this.cardsGroup.add(cardGroup);
    }
}