package howdy.lab.birthcounterbot.api.datasource;

import howdy.lab.birthcounterbot.api.domain.TgUser;

import java.util.List;

public interface TgUserDatasource {
    List<TgUser> findAll();

    TgUser get(Long id);

    TgUser getByChatId(Long chatId);

    TgUser getOrCreateTgUser(TgUser domain);

    TgUser getOrCreateTgUserWithChatId(TgUser domain);

    TgUser create(TgUser domain);

    TgUser update(final Long id, TgUser domain);

    java.util.List<TgUser> findAllByNotificationTimeUtc(java.time.LocalTime time);
}
