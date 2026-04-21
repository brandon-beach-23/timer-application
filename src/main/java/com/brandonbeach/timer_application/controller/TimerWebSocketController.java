package com.brandonbeach.timer_application.controller;

import com.brandonbeach.timer_application.model.Greeting;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TimerWebSocketController {

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Greeting greeting (String message) throws Exception {
        Thread.sleep(1000);
        return new Greeting ("Hello, " + message);
    }
}
