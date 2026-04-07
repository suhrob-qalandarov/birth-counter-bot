package howdy.lab.birthcounterbot.api.datasource;

import howdy.lab.birthcounterbot.api.domain.BotSession;

public interface BotSessionDatasource {

    BotSession getByChatId(Long chatId);

    BotSession getOrCreate(BotSession domain);

    BotSession update(Long id, BotSession domain);

    void delete(Long chatId);
}
