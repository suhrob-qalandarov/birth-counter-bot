package howdy.lab.birthcounterbot.datasource.service.impl;

import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.datasource.entity.TgUserEntity;
import howdy.lab.birthcounterbot.datasource.repository.TgUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TgUserDatasourceImpl implements TgUserDatasource {

    private final TgUserRepository repository;

    @Override
    public List<TgUser> findAll() {
        return repository.findAll().stream()
                .map(TgUserEntity::map)
                .toList();
    }

    @Override
    public TgUser get(Long id) {
        return repository.findById(id)
                .orElseThrow()
                .map();
    }

    @Override
    public TgUser getByChatId(Long chatId) {
        return repository.findByChatId(chatId)
                .orElseThrow()
                .map();
    }

    @Override
    @Transactional
    public TgUser getOrCreateTgUser(TgUser domain) {
        return java.util.Optional.ofNullable(domain.getId())
                .flatMap(repository::findById)
                .orElseGet(() -> TgUserEntity.map(create(domain)))
                .map();
    }

    @Override
    public TgUser getOrCreateTgUserWithChatId(TgUser domain) {
        if (domain.getChatId() == null) {
            return create(domain);
        }

        return repository.findByChatId(domain.getChatId())
                .orElseGet(() -> TgUserEntity.map(create(domain)))
                .map();
    }

    @Override
    @Transactional
    public TgUser create(TgUser domain) {
        final var entity = repository.save(TgUserEntity.map(domain));
        final var result = entity.map();
        log.info("New TgUser saved id: {}, chatId: {}", result.getId(), result.getChatId());
        return result;
    }

    @Override
    @Transactional
    public TgUser update(final Long id, TgUser domain) {
        final var existing = repository.findById(id)
                .orElseThrow().map();

        existing.setChatId(domain.getChatId());
        existing.setBot(domain.getBot());
        existing.setBirthDate(domain.getBirthDate());
        existing.setLanguageCode(domain.getLanguageCode());
        existing.setFirstName(domain.getFirstName());
        existing.setLastName(domain.getLastName());
        existing.setUsername(domain.getUsername());
        existing.setLatitude(domain.getLatitude());
        existing.setLongitude(domain.getLongitude());
        existing.setNotificationTime(domain.getNotificationTime());
        existing.setZoneId(domain.getZoneId());
        existing.setPhoneNumber(domain.getPhoneNumber());
        existing.setPremium(domain.getPremium());
        existing.setNotificationTimeUtc(domain.getNotificationTimeUtc());
        existing.setCanJoinGroups(domain.getCanJoinGroups());
        existing.setStatus(domain.getStatus());
        existing.setAppUserId(domain.getAppUserId());

        final var result = repository.save(TgUserEntity.map(existing)).map();
        log.info("Exist TgUser updated id: {}", id);
        return result;
    }
}
