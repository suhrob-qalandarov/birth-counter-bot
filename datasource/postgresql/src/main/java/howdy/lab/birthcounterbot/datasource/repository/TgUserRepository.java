package howdy.lab.birthcounterbot.datasource.repository;

import howdy.lab.birthcounterbot.datasource.entity.TgUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TgUserRepository extends JpaRepository<TgUserEntity, Long> {
    Optional<TgUserEntity> findByChatId(Long chatId);
    
    List<TgUserEntity> findAllByNotificationTimeUtc(LocalTime time);
}
