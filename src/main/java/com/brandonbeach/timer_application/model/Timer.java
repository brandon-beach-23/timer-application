package com.brandonbeach.timer_application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;


@Entity
@Table(name = "timers")
@Getter
@Setter
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long duration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimerState state;

    @Column(nullable = false)
    private long elapsedTime = 0;

    @Column(nullable = false)
    private Timestamp lastStartedAt;

    public long calculateElapsedTime() {
        return this.elapsedTime + 1;
    }

    public long calculateRemainingTime() {
        return this.duration - this.elapsedTime;
    }

    public boolean isComplete() {
        return this.elapsedTime >= this.duration;
    }

    public boolean canChangeState(TimerState state) {
        return this.state != state;
    }
}
