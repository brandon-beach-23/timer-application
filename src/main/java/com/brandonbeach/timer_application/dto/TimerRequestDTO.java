package com.brandonbeach.timer_application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimerRequestDTO {
    private String timerName;
    private long timerDuration;
}
