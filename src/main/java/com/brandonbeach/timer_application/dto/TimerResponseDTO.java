package com.brandonbeach.timer_application.dto;

import com.brandonbeach.timer_application.model.TimerState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimerResponseDTO {
    private long elapsedTime;
    private TimerState timerState;
    private boolean hasCompleted;

    public TimerResponseDTO(long elapsedTime, TimerState timerState, boolean hasCompleted) {
        this.elapsedTime = elapsedTime;
        this.timerState = timerState;
        this.hasCompleted = hasCompleted;
    }
}
