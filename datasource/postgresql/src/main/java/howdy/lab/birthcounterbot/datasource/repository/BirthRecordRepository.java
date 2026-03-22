package howdy.lab.birthcounterbot.datasource.repository;

import howdy.lab.birthcounterbot.datasource.entity.BirthRecordEntity;
import howdy.lab.birthcounterbot.datasource.entity.TgUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BirthRecordRepository extends JpaRepository<BirthRecordEntity, Long> {
    List<BirthRecordEntity> findAllByTgUser(TgUserEntity tgUser);
}
