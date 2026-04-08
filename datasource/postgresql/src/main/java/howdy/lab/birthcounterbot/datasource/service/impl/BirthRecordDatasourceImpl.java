package howdy.lab.birthcounterbot.datasource.service.impl;

import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.datasource.entity.BirthRecordEntity;
import howdy.lab.birthcounterbot.datasource.repository.BirthRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirthRecordDatasourceImpl implements BirthRecordDatasource {

    private final BirthRecordRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BirthRecord> findAll() {
        return repository.findAll().stream()
                .map(BirthRecordEntity::map2Domain)
                .toList();
    }

    @Override
    public List<BirthRecord> findAllByTgUserId(final Long tgUserId) {
        return repository.findAllByTgUser_Id(tgUserId).stream()
                .map(BirthRecordEntity::map2Domain)
                .toList();
    }

    @Override
    public BirthRecord findActiveByTgUserId(Long tgUserId) {
        return repository.findByTgUserIdAndIsActiveTrue(tgUserId)
                .map(BirthRecordEntity::map2Domain)
                .orElse(null);
    }

    @Override
    public BirthRecord findByTgUserIdAndFullName(Long tgUserId, String fullName) {
        return repository.findByTgUserIdAndFullName(tgUserId, fullName)
                .map(BirthRecordEntity::map2Domain)
                .orElse(null);
    }

    @Override
    public BirthRecord get(final Long id) {
        return repository.findById(id)
                .orElseThrow()
                .map2Domain();
    }

    @Override
    @Transactional
    public BirthRecord getOrCreate(BirthRecord domain) {
        return Optional.ofNullable(domain.getId())
                .flatMap(repository::findById)
                .orElseGet(() -> BirthRecordEntity.map2Entity(create(domain), em))
                .map2Domain();
    }

    @Override
    @Transactional
    public BirthRecord create(BirthRecord domain) {
        final var entity = repository.save(BirthRecordEntity.map2Entity(domain, em));
        final var result = entity.map2Domain();
        log.info("New BirthRecord saved id: {}", result.getId());
        return result;
    }

    @Override
    @Transactional
    public BirthRecord update(final Long id, BirthRecord domain) {
        final var existing = repository.findById(id)
                .orElseThrow()
                .map2Domain();

        existing.setTgUserId(domain.getTgUserId());
        existing.setFullName(domain.getFullName());
        existing.setBirthDate(domain.getBirthDate());
        existing.setGender(domain.getGender());
        existing.setNextNotificationTimeUtc(domain.getNextNotificationTimeUtc());
        existing.setTimezoneId(domain.getTimezoneId());
        existing.setLatitude(domain.getLatitude());
        existing.setLongitude(domain.getLongitude());
        existing.setNotificationTime(domain.getNotificationTime());
        existing.setNotificationTimeUtc(domain.getNotificationTimeUtc());
        existing.setIsActive(domain.getIsActive());

        final var result = repository.save(BirthRecordEntity.map2Entity(existing, em)).map2Domain();
        log.info("Existing BirthRecord updated id: {}", id);
        return result;
    }

    @Override
    public List<BirthRecord> findAllByNotificationTimeUtc(LocalTime time) {
        return repository.findAllByNotificationTimeUtcAndIsActiveTrue(time).stream()
                .map(BirthRecordEntity::map2Domain)
                .toList();
    }
}
