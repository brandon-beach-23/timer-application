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

    public TimerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startTimer(String name, long durationInSeconds) {
        // 1. Verify there is not a Timer already running.
        // 2. Create a new Timer object initializing name, duration, state, elapsed time, and lastStartedAt
        // 3. Validate state change using canChangeState()
        // 4. Start the ticking background thread
        // 5. Emit the state to all connected WebSocket clients
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
        // 1. Verify a timer is currently running
        // 2. Stop the ticking background thread
        // 3. Update timer state to STOPPED
        // 4. Emit the final state
        // 5. Persist the completed session to database
        // 6. Clear currentTimer
    }

    public void pauseTimer() {
        // 1. Verify timer is RUNNING (not already paused)
        // 2. Stop the ticking background thread
        // 3. Update timer state to PAUSED
        // 4. Emit the paused state
    }

    public void resumeTimer() {
        // 1. Verify timer is PAUSED
        // 2. Update lastStartedAt to now (to account for pause duration)
        // 3. Start the ticking background thread again
        // 4. Update state to RUNNING
        // 5. Emit the resumed state
    }

    private void startTicking() {
        // Schedule tick() to run every 1 second
        // Store the ScheduledFuture in tickingTask so we can cancel it later

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
        // Cancel the tickingTask if it exists
        // Set tickingTask to null
        if(tickingTask != null) {
            tickingTask.cancel(false);
            tickingTask = null;
        }
    }

    private void tick() {
        // 1. Increment elapsed time by 1 second
        // 2. Check if timer is now complete
        // 3. If complete, stop ticking and update state to COMPLETED
        // 4. Emit updated state to WebSocket clients
    }

    private void emitSnapshot() {
        // Send current timer state to all connected WebSocket clients
        // via messagingTemplate.convertAndSend("/topic/timer-updates", snapshot)
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