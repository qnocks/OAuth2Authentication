package ru.qnocks.oauth2authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.qnocks.oauth2authentication.domain.enums.MessageText;
import ru.qnocks.oauth2authentication.repository.MessagesRepository;

@Controller
@RequestMapping("/")
public class IndexController {

    private final MessagesRepository messagesRepository;

    @Autowired
    public IndexController(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("broCount", messagesRepository.countByText(MessageText.BRO.getName()));
        model.addAttribute("sisCount", messagesRepository.countByText(MessageText.SIS.getName()));
        return "index";
    }
}
