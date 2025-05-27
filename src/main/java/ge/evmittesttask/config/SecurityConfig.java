package ge.evmittesttask.config;

import ge.evmittesttask.security.TelegramAuthFilter;
import ge.evmittesttask.security.TelegramAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TelegramAuthValidator validator;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new TelegramAuthFilter(validator), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            request.setAttribute("error", "Доступ запрещён");
                            request.getRequestDispatcher("/error").forward(request, response);
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            request.setAttribute("error", "Неаутентифицированный запрос");
                            request.getRequestDispatcher("/error").forward(request, response);
                        }))
                .build();
    }
}
