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
    private long elapsedTime;

    @Column(nullable = false)
    private Timestamp lastStartedAt;

    public Timer() {

    }

    public Timer(String name, long duration) {
        this.name = name;
        this.duration = duration;
        this.state = TimerState.INITIAL;
    }

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

    public void start(Timestamp now) {
        if (this.state == TimerState.INITIAL) {
            this.state = TimerState.RUNNING;
            this.elapsedTime = 0;
            this.lastStartedAt = now;
        }
    }

    public void pause() {
        if (this.state == TimerState.RUNNING) {
            this.state = TimerState.PAUSED;
        }
    }

    public void resume(Timestamp now){
        if (this.state == TimerState.PAUSED) {
            this.state = TimerState.RUNNING;
            this.lastStartedAt = now;
        }
    }

    public void stop() {
        if (this.state == TimerState.RUNNING || this.state == TimerState.PAUSED) {
            this.state = TimerState.STOPPED;
        }
    }

    public void complete() {
        if ((this.state == TimerState.RUNNING || this.state == TimerState.PAUSED) && this.isComplete()) {
            this.state = TimerState.COMPLETED;
        }
    }
}
