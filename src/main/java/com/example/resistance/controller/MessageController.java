package com.example.resistance.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.example.resistance.entity.Message;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/receive/message")
    public Message send(Message message) throws Exception {
        Thread.sleep(300);
        return new Message(HtmlUtils.htmlEscape(message.getName()), HtmlUtils.htmlEscape(message.getStatement()));
    }
}

