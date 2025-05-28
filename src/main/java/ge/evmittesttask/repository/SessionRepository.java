package ge.evmittesttask.repository;

import ge.evmittesttask.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
