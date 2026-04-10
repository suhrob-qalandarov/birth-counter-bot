package howdy.lab.birthcounterbot.api.datasource;

import howdy.lab.birthcounterbot.api.domain.BirthRecord;

import java.util.List;

public interface BirthRecordDatasource {
    List<BirthRecord> findAll();

    List<BirthRecord> findAllByTgUserId(final Long tgUserId);

    BirthRecord findActiveByTgUserId(final Long tgUserId);

    BirthRecord findByTgUserIdAndFullName(Long tgUserId, String fullName);

    BirthRecord get(final Long id);

    BirthRecord getOrCreate(BirthRecord domain);

    BirthRecord create(BirthRecord domain);

    BirthRecord update(final Long id, BirthRecord domain);

    java.util.List<BirthRecord> findAllByNotificationTimeUtc(java.time.LocalTime time);

    long count();
}
