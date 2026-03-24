package howdy.lab.birthcounterbot.datasource.service.impl;

import howdy.lab.birthcounterbot.api.datasource.TimezoneDatasource;
import howdy.lab.birthcounterbot.api.domain.Timezone;
import howdy.lab.birthcounterbot.datasource.entity.TimezoneEntity;
import howdy.lab.birthcounterbot.datasource.repository.TimezoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimezoneDatasourceImpl implements TimezoneDatasource {

    private final TimezoneRepository repository;

    @Override
    @Transactional
    public Timezone getOrCreateTimezone(Timezone timezone) {
        return repository.findByZoneName(timezone.getZoneName())
                .map(TimezoneEntity::map2Domain)
                .orElseGet(() -> repository.save(TimezoneEntity.map2Domain(timezone)).map2Domain());
    }
}
