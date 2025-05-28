package ge.evmittesttask.service;

import ge.evmittesttask.model.Session;
import ge.evmittesttask.model.TelegramUser;
import ge.evmittesttask.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;

    public void saveSession(TelegramUser user) {
        TelegramUser currentUser = userService.saveUserEntry(user);
        Session newSession = Session.builder()
                .userId(currentUser.getId())
                .createdAt(LocalDateTime.now())
                .build();
        sessionRepository.save(newSession);
    }
}
