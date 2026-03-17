package howdy.lab.birthcounterbot.datasource.repository;

import howdy.lab.birthcounterbot.datasource.entity.BotSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotSessionRepository extends JpaRepository<BotSessionEntity, Long>, JpaSpecificationExecutor<BotSessionEntity> {
    Optional<BotSessionEntity> findByChatId(Long chatId);
}
