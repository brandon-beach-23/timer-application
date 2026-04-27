package com.brandonbeach.timer_application.service;

import com.brandonbeach.timer_application.model.Timer;
import com.brandonbeach.timer_application.model.TimerState;
import com.brandonbeach.timer_application.repository.TimerSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimerServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private TimerSessionRepository timerSessionRepository;

    @InjectMocks
    private TimerService timerService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testStartTimer_CreatesTimerWithCorrectName() {
        // Arrange
        String timerName = "Coding Practice";
        long duration = 3600;  // 1 hour in seconds

        // Act
        timerService.startTimer(timerName, duration);

        // Assert
        Timer timer = timerService.getCurrentTimer();
        assertNotNull(timer);
        assertEquals(timerName, timer.getName());
        assertEquals(duration, timer.getDuration());
        assertEquals(TimerState.RUNNING, timer.getState());

    }

    @Test
    public void testStartTimer_ShouldNotStartIfTimerAlreadyRunning() {
        // Arrange
        String timerName = "Coding Practice";
        long duration = 3600;

        // Start first timer
        timerService.startTimer(timerName, duration);

        // Act & Assert
        String timerName2 = "Stretching";
        long duration2 = 3600;
        timerService.startTimer(timerName2, duration2);
        Timer timer = timerService.getCurrentTimer();
        assertEquals(timerName, timer.getName());

    }

    @Test
    public void testTick_IncrementsElapsedTime() throws InterruptedException {
        // Arrange
        timerService.startTimer("Test", 3600);
        Timer timer = timerService.getCurrentTimer();
        long initialElapsedTime = timer.getElapsedTime();

        // Act
        Thread.sleep(2100);

        // Assert
        assertTrue(timer.getElapsedTime() >= initialElapsedTime + 2);
    }

    @Test
    public void testPauseTimer_ChangesStateToPaused() {
        // Arrange
        timerService.startTimer("Test", 3600);

        // Act
        timerService.pauseTimer();

        // Assert
        Timer timer = timerService.getCurrentTimer();
        assertEquals(TimerState.PAUSED, timer.getState());
    }

    @Test
    public void testStopTimer_SavesSessionToDatabase() {
        // Arrange
        timerService.startTimer("Test", 3600);

        // Act
        timerService.stopTimer();

        // Assert
        verify(timerSessionRepository, times(1)).save(any());
    }

}

