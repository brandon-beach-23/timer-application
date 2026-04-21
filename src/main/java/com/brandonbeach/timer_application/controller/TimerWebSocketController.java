package com.brandonbeach.timer_application.controller;

import com.brandonbeach.timer_application.model.Greeting;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TimerWebSocketController {

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String handleTest(String message) throws Exception {
        return "Echo: " + message;
    }
}
