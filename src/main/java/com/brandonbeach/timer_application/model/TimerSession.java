package com.brandonbeach.timer_application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="timer_session")
@Getter
@Setter
@NoArgsConstructor
public class TimerSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String timerName;

    @Column(nullable = false)
    private long duration;

    @Column(nullable = false)
    private long elapsedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimerState timerState;

    @Column(nullable = false)
    private boolean hasCompleted;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp stoppedAt;
}

