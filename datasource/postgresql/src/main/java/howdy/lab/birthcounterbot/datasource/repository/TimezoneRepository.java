package howdy.lab.birthcounterbot.datasource.repository;

import howdy.lab.birthcounterbot.datasource.entity.TimezoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<TimezoneEntity, Integer>, JpaSpecificationExecutor<TimezoneEntity> {
    Optional<TimezoneEntity> findByZoneName(String zoneName);
}
