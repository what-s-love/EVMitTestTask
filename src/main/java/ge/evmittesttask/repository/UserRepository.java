package ge.evmittesttask.repository;

import ge.evmittesttask.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("update TelegramUser t set t.lastEntry = ?1 where t.userId = ?2")
    int updateLastEntryByUserId(LocalDateTime lastEntry, Long userId);

    TelegramUser findByUserId(Long userId);
}
