package com.brandonbeach.timer_application.dto;

import com.brandonbeach.timer_application.model.TimerState;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimerSessionResponseDTO {

    private String timerName;
    private long duration;
    private long elapsedTime;
    @Enumerated(EnumType.STRING)
    private TimerState timerState;
    private boolean hasCompleted;
    private Timestamp createdAt;
    private Timestamp stoppedAt;


}
