package ge.evmittesttask.controller;

import ge.evmittesttask.model.TelegramUser;
import ge.evmittesttask.service.SessionService;
import ge.evmittesttask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final SessionService sessionService;

    @GetMapping("/")
    public String auth() {
        log.info("Получен начальный запрос");
        return "index";
    }

    @GetMapping("main")
    public String home(Authentication authentication, Model model) {
            TelegramUser user = (TelegramUser) authentication.getPrincipal();
            log.info("Аутентифицированный запрос от пользователя {}", user.getUsername());
            sessionService.saveSession(user);
            model.addAttribute("user", user);
            return "main";
    }
}
