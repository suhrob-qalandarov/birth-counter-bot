package howdy.lab.birthcounterbot.datasource.service.impl;

import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.datasource.entity.BotSessionEntity;
import howdy.lab.birthcounterbot.datasource.repository.BotSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotSessionDatasourceImpl implements BotSessionDatasource {

    private final BotSessionRepository repository;

    @Override
    public BotSession getByChatId(Long chatId) {
        return repository.findByChatId(chatId)
                .orElseThrow()
                .map2Domain();
    }

    @Override
    @Transactional
    public BotSession getOrCreate(BotSession domain) {
        return repository.findByChatId(domain.getChatId())
                .orElseGet(() -> {
                    final var saved = repository.save(BotSessionEntity.map2Entity(domain));
                    log.info("New BotSession created chatId: {}", domain.getChatId());
                    return saved;
                })
                .map2Domain();
    }

    @Override
    @Transactional
    public BotSession update(Long id, BotSession domain) {
        final var existing = repository.findById(id).orElseThrow();
        existing.setStep(domain.getStep());
        existing.setTempYear(domain.getTempYear());
        existing.setTempMonth(domain.getTempMonth());
        existing.setTempDay(domain.getTempDay());
        existing.setTempFullName(domain.getTempFullName());
        existing.setTempGender(domain.getTempGender());
        existing.setTempTimezone(domain.getTempTimezone());
        existing.setTempLatitude(domain.getTempLatitude());
        existing.setTempLongitude(domain.getTempLongitude());
        final var result = repository.save(existing).map2Domain();
        log.info("BotSession updated id: {}", id);
        return result;
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        repository.findByChatId(chatId).ifPresent(entity -> {
            entity.setDeleted(true);
            repository.save(entity);
            log.info("BotSession deleted chatId: {}", chatId);
        });
    }
}
