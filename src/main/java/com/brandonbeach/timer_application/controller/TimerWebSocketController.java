package com.brandonbeach.timer_application.controller;

import com.brandonbeach.timer_application.dto.TimerRequestDTO;
import com.brandonbeach.timer_application.service.TimerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TimerWebSocketController {

    private final TimerService timerService;

    public TimerWebSocketController(TimerService timerService) {
        this.timerService = timerService;
    }

    @MessageMapping("/start")
    public void handleStart(TimerRequestDTO timerRequestDTO) {
        timerService.startTimer(timerRequestDTO.getTimerName(), timerRequestDTO.getTimerDuration());
    }
    @MessageMapping("/pause")
    public void handlePause() {
        timerService.pauseTimer();
    }

    @MessageMapping("/resume")
    public void handleResume() {
        timerService.resumeTimer();
    }

    @MessageMapping("/stop")
    public void handleStop() {
        timerService.stopTimer();
    }

}
