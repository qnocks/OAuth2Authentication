package ru.qnocks.oauth2authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.qnocks.oauth2authentication.repository.MessagesRepository;
import ru.qnocks.oauth2authentication.service.PrincipalExtractor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final MessagesRepository messagesRepository;

    private final PrincipalExtractor principalExtractor;

    @Autowired
    public AuthenticationController(MessagesRepository messagesRepository,
                                    PrincipalExtractor principalExtractor) {
        this.principalExtractor = principalExtractor;
        this.messagesRepository = messagesRepository;
    }

    @GetMapping("/success")
    public String login(OAuth2AuthenticationToken authentication, Model model) {
        Map<String, Object> map = principalExtractor.extractUser(authentication);

        if (map != null) {
            model.addAttribute("name", map.get("name"));
        }

        messagesRepository.findFirstByOrderByIdDesc().ifPresent(message -> {
            model.addAttribute("message", message);
        });

        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        SecurityContextHolder.clearContext();

        request.logout();
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/username")
    public @ResponseBody String getUsername(OAuth2AuthenticationToken authentication) {
        Map<String, Object> map = principalExtractor.extractUser(authentication);
        return (String) map.get("name");
    }
}
