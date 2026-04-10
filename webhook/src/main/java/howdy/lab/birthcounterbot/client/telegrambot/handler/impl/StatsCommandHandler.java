package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatsCommandHandler implements UpdateHandler {

    private static final long ADMIN_CHAT_ID = 6513286717L;

    private final TelegramBot telegramBot;
    private final TgUserDatasource tgUserDatasource;
    private final BirthRecordDatasource birthRecordDatasource;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && update.message().text() != null
                && update.message().text().startsWith("/stats")
                && update.message().chat().id().equals(ADMIN_CHAT_ID);
    }

    @Override
    public void handle(Update update) {
        var chatId = update.message().chat().id();
        var usersCount = tgUserDatasource.count();
        var birthRecordsCount = birthRecordDatasource.count();

        var text = """
                📊 Bot Statistics

                👥 Users: %d
                🎂 Birth records: %d""".formatted(usersCount, birthRecordsCount);

        telegramBot.execute(new SendMessage(chatId, text));
    }
}