package ge.evmittesttask.service;

import ge.evmittesttask.model.TelegramUser;
import ge.evmittesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public TelegramUser saveUserEntry(TelegramUser user) {
        if (userRepository.existsByUserId(user.getUserId())) {
            userRepository.updateLastEntryByUserId(LocalDateTime.now(), user.getUserId());
            return userRepository.findByUserId(user.getUserId());
        } else {
            log.info("Новый пользователь! Добро пожаловать {}", user.getUsername());
            return userRepository.save(TelegramUser.builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .languageCode(user.getLanguageCode())
                    .isPremium(user.getIsPremium())
                    .createdAt(LocalDateTime.now())
                    .lastEntry(LocalDateTime.now())
                    .build());
        }
    }
}
