package pl.pacinho.pasjans.model.dto;

import pl.pacinho.pasjans.utils.TimeUtils;

public record GameSummaryDto(int movesCount, long time) {

    public String getSummaryTimeText() {
        return TimeUtils.timeLeft(this.time);
    }

}
