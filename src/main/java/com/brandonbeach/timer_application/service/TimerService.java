package com.brandonbeach.timer_application.service;

import com.brandonbeach.timer_application.dto.TimerResponseDTO;
import com.brandonbeach.timer_application.model.Timer;
import com.brandonbeach.timer_application.model.TimerState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.*;

@Service
public class TimerService {

    private Timer currentTimer;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> tickingTask;
    private final SimpMessagingTemplate messagingTemplate;
    private TimerResponseDTO timerDTO;

    public TimerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startTimer(String name, long durationInSeconds) {
        if (currentTimer != null && currentTimer.getState() == TimerState.RUNNING) {
            return;
        }

        Timestamp now = Timestamp.from(Instant.now());
        currentTimer = new Timer(name, durationInSeconds);
        currentTimer.start(now);
        startTicking();
        emitSnapshot();
    }

    public void stopTimer() {

        // TODO: 5. Persist the completed session to database
        // TODO: 6. Clear currentTimer (May handle through button on the UI)
        if (currentTimer == null || (currentTimer.getState() != TimerState.RUNNING && currentTimer.getState() != TimerState.PAUSED)) {
            return;
        }
        stopTicking();
        if (!currentTimer.isComplete()){
            currentTimer.stop();
        } else { currentTimer.complete(); }
        emitSnapshot();
    }

    public void pauseTimer() {
        if (currentTimer == null || currentTimer.getState() == TimerState.PAUSED) {
            return;
        }

        stopTicking();
        currentTimer.pause();
        emitSnapshot();
    }

    public void resumeTimer() {
       if (currentTimer == null || currentTimer.getState() != TimerState.PAUSED) {
            return;
        }

        Timestamp now = Timestamp.from(Instant.now());
        startTicking();
        currentTimer.resume(now);
        emitSnapshot();
    }

    private void startTicking() {
       if (tickingTask != null &&  !tickingTask.isCancelled()) {
            return;
        }

        tickingTask = scheduler.scheduleAtFixedRate(
                this::tick,
                1,
                1,
                TimeUnit.SECONDS
        );
    }

    private void stopTicking() {
        if(tickingTask != null) {
            tickingTask.cancel(false);
            tickingTask = null;
        }
    }

    private void tick() {
        currentTimer.incrementElapsedTime();
        if(currentTimer.isComplete() && !currentTimer.getHasCompleted()){
            currentTimer.complete();
            // TODO: Play a sound
        }
        emitSnapshot();
    }

    private void emitSnapshot() {
       if (currentTimer == null) {
            return;
        }
        timerDTO = new TimerResponseDTO(currentTimer.getElapsedTime(), currentTimer.getState(), currentTimer.getHasCompleted());
        messagingTemplate.convertAndSend("/topic/timer-updates",  timerDTO);
        System.out.println("Emitting snapshot: " + timerDTO.getElapsedTime() + ", " + timerDTO.getTimerState());

    }

    @PostConstruct
    public void init() {
        // Initialize any resources needed
    }

    @PreDestroy
    public void cleanup() {
        // Shutdown the scheduler gracefully
        // This runs when Spring shuts down the application
    }
}