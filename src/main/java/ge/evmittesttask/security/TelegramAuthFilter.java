package ge.evmittesttask.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.evmittesttask.model.TelegramUser;
import ge.evmittesttask.security.TelegramAuthValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class TelegramAuthFilter extends OncePerRequestFilter {
    private final TelegramAuthValidator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelegramAuthFilter(TelegramAuthValidator validator) {
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        log.info("Request URI: {}", request.getRequestURI());
        String initData = request.getHeader("X-Telegram-InitData");
        if (initData != null) {
            log.info("Получен заголовок X-Telegram-InitData");
        } else {
            log.warn("Отсутствует заголовок X-Telegram-InitData");
        }

        if (initData != null && validator.validate(initData)) {
            Map<String, String> params = validator.parseInitData(initData);

            if (params.containsKey("user")) {
                try {
                    TelegramUser user = objectMapper.readValue(
                            java.net.URLDecoder.decode(params.get("user"), "UTF-8"),
                            TelegramUser.class
                    );

                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            )
                    );
                } catch (Exception e) {
                    //ToDo Логирование ошибки + выброс до обработчика
                }
            }
        }

        chain.doFilter(request, response);
    }
}