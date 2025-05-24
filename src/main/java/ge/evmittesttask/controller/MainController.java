package ge.evmittesttask.controller;

import ge.evmittesttask.model.TelegramUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            TelegramUser user = (TelegramUser) authentication.getPrincipal();
            model.addAttribute("user", user);
            return "index";
        }
        return "error";
    }
}
