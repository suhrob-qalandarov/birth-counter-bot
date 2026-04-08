package howdy.lab.birthcounterbot.datasource.repository;

import howdy.lab.birthcounterbot.datasource.entity.BirthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BirthRecordRepository extends JpaRepository<BirthRecordEntity, Long> {
    List<BirthRecordEntity> findAllByTgUser_Id(Long tgUserId);
    
    java.util.Optional<BirthRecordEntity> findByTgUserIdAndFullName(Long tgUserId, String fullName);
    
    List<BirthRecordEntity> findAllByNotificationTimeUtc(java.time.LocalTime time);
}
