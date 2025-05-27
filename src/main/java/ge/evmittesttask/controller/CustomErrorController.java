package ge.evmittesttask.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String errorPage(HttpServletRequest request, Model model) {
        Object errorAttr = request.getAttribute("error");
        if (errorAttr != null) {
            model.addAttribute("error", errorAttr);
        } else {
            model.addAttribute("error", "Неизвестная ошибка");
        }
        return "error";
    }
}
