package pl.pacinho.pasjans.model.entity;

import lombok.Getter;
@Getter
public class Player {
    private final String name;

    public Player(String name, int index) {
        this.name = name;
    }


}