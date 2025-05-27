package ge.evmittesttask.controller;

import ge.evmittesttask.model.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {
    @GetMapping("/")
    public String auth() {
        log.info("Получен начальный запрос, загрузка index.html");
        return "index";
    }

    @GetMapping("main")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            TelegramUser user = (TelegramUser) authentication.getPrincipal();
            log.info("Аутентифицированный запрос от пользователя {}", user.getUsername());
            model.addAttribute("user", user);
            return "main";
        }
        log.info("Неаутентифицированный запрос");
        return "error";
    }
}
