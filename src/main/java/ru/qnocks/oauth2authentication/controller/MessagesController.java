package ru.qnocks.oauth2authentication.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.qnocks.oauth2authentication.domain.Message;
import ru.qnocks.oauth2authentication.repository.MessagesRepository;
import ru.qnocks.oauth2authentication.service.PrincipalExtractor;


import java.time.LocalTime;

@Controller
@RequestMapping("/messages")
public class MessagesController {

    private final MessagesRepository messagesRepository;

    private final PrincipalExtractor principalExtractor;

    @Autowired
    public MessagesController(MessagesRepository messagesRepository,
                              PrincipalExtractor principalExtractor) {
        this.messagesRepository = messagesRepository;
        this.principalExtractor = principalExtractor;
    }

    @PostMapping("{text}")
    public String save(@PathVariable String text, OAuth2AuthenticationToken authentication) {
        messagesRepository.save(new Message(text, getUsername(authentication), LocalTime.now()));
        return "redirect:/success";
    }

    private String getUsername(OAuth2AuthenticationToken authentication) {
        return (String) principalExtractor.extractUser(authentication).get("name");
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message getMessage(Message message) {
        messagesRepository.save(message);
        return new Message(message.getText(), message.getSendBy(), message.getTime());
    }
}
