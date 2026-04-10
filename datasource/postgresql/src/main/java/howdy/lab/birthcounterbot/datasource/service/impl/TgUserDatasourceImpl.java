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
import java.util.Objects;


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
    public TgUser update(Long id, TgUser domain) {
        if (!Objects.equals(id, domain.getId())) {
            throw new IllegalArgumentException("Ids do not match");
        }
        var entity = repository.findById(id).orElseThrow();
        entity.setChatId(domain.getChatId());
        entity.setUsername(domain.getUsername());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setPremium(domain.getPremium());
        entity.setCanJoinGroups(domain.getCanJoinGroups());
        entity.setLanguageCode(domain.getLanguageCode());
        entity.setBot(domain.getBot());
        entity.setStatus(domain.getStatus());
        entity.setGender(domain.getGender());
        entity.setIsAgreed(domain.getIsAgreed());
        return repository.save(entity).map();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
