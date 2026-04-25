package com.brandonbeach.timer_application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;



@Getter
@Setter
@NoArgsConstructor
public class Timer {

    private String name;
    private long duration;
    @Enumerated(EnumType.STRING)
    private TimerState state;
    private long elapsedTime;
    private Timestamp lastStartedAt;
    private boolean hasCompleted;

    public Timer(String name, long duration) {
        this.name = name;
        this.duration = duration;
        this.state = TimerState.INITIAL;
        this.hasCompleted = false;
    }

    public void incrementElapsedTime() {
        this.elapsedTime++;
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
            this.hasCompleted = true;
        }
    }

    public boolean getHasCompleted() {
        return this.hasCompleted;
    }
}
