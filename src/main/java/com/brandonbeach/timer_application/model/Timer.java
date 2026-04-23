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
    private BigInteger duration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimerState state;

    @Column(nullable = false)
    private BigInteger elaspedTime = new BigInteger("0");

    @Column(nullable = false)
    private Timestamp lastStartedAt;

    public BigInteger calculateElapsedTime() {
        return this.elaspedTime + 1;
    }

    public BigInteger calculateRemainingTime() {
        return this.duration - this.elaspedTime;
    }

    public boolean isComplete() {
        if(this.duration.compareTo(this.elaspedTime.ZERO)){
            return true;
        }
    }

    public boolean canChangeState(TimerState state) {
        return this.state != state;
    }
}
