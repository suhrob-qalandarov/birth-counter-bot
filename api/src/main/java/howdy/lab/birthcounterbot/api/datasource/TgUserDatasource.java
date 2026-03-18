package howdy.lab.birthcounterbot.api.datasource;

import howdy.lab.birthcounterbot.api.domain.Timezone;

public interface TimezoneDatasource {
    /**
     * Gets timezone from database or creates a new one if not exists.
     * 
     * @param timezone IANA zone name (e.g. Asia/Tashkent)
     * @return zoneName
     */
    Timezone getOrCreateTimezone(Timezone timezone);
}
