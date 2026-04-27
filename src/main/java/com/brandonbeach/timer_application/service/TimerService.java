package com.brandonbeach.timer_application.service;

import com.brandonbeach.timer_application.dto.TimerResponseDTO;
import com.brandonbeach.timer_application.dto.TimerSessionResponseDTO;
import com.brandonbeach.timer_application.model.Timer;
import com.brandonbeach.timer_application.model.TimerSession;
import com.brandonbeach.timer_application.model.TimerState;
import com.brandonbeach.timer_application.repository.TimerSessionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TimerService {

    private Timer currentTimer;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TimerSessionRepository timerSessionRepository;
    private ScheduledFuture<?> tickingTask;
    private final SimpMessagingTemplate messagingTemplate;
    private TimerResponseDTO timerDTO;
    private Timestamp startTime;
    private Timestamp endTime;


    public TimerService(SimpMessagingTemplate messagingTemplate,TimerSessionRepository timerSessionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.timerSessionRepository = timerSessionRepository;
    }

    public Timer getCurrentTimer() {
        return currentTimer;
    }

    public void startTimer(String name, long durationInSeconds) {
        if (currentTimer != null && currentTimer.getState() == TimerState.RUNNING) {
            return;
        }

        Timestamp now = Timestamp.from(Instant.now());
        currentTimer = new Timer(name, durationInSeconds);
        currentTimer.start(now);
        this.startTime = now;
        startTicking();
        emitSnapshot();
    }

    public void stopTimer() {

        if (currentTimer == null || (currentTimer.getState() != TimerState.RUNNING && currentTimer.getState() != TimerState.PAUSED)) {
            return;
        }

        Timestamp now = Timestamp.from(Instant.now());
        this.endTime = now;
        stopTicking();
        currentTimer.stop();
        saveTimerSession();
        emitSnapshot();
        broadcastTimerHistory();
        currentTimer = null;
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

    private void saveTimerSession() {
        TimerSession timerSession = new TimerSession(
                currentTimer.getName(),
                currentTimer.getDuration(),
                currentTimer.getElapsedTime(),
                currentTimer.getState(),
                currentTimer.getHasCompleted(),
                this.startTime,
                this.endTime
        );

        timerSessionRepository.save(timerSession);
        System.out.println("Timer Session was saved!");
    }

    public void broadcastTimerHistory() {
        List<TimerSessionResponseDTO> timerSessionsResponseDTOs = timerSessionRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        System.out.println("Broadcasting: " + timerSessionsResponseDTOs.size() + timerSessionsResponseDTOs);
        messagingTemplate.convertAndSend("/topic/timer-history", timerSessionsResponseDTOs);
    }

    private TimerSessionResponseDTO convertToDTO (TimerSession timerSession) {
        TimerSessionResponseDTO timerSessionResponseDTO = new TimerSessionResponseDTO(
                timerSession.getTimerName(),
                timerSession.getDuration(),
                timerSession.getElapsedTime(),
                timerSession.getTimerState(),
                timerSession.isHasCompleted(),
                timerSession.getCreatedAt(),
                timerSession.getStoppedAt()
        );
        return timerSessionResponseDTO;
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