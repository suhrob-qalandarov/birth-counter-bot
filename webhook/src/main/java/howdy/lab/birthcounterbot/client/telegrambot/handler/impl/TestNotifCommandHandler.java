package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestNotifCommandHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final TgUserDatasource tgUserDatasource;
    private final BirthRecordDatasource birthRecordDatasource;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && update.message().text() != null
                && update.message().text().startsWith("/test_notif");
    }

    @Override
    public void handle(Update update) {
        var chatId = update.message().chat().id();
        TgUser user = tgUserDatasource.getByChatId(chatId);

        List<BirthRecord> userRecords = birthRecordDatasource.findAllByTgUserId(user.getId());
        if (userRecords.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "No birth records found for testing."));
            return;
        }

        StringBuilder notificationMessage = new StringBuilder();
        notificationMessage.append("🔔 *[TEST] Daily Birthday Updates*\n\n");

        LocalDate currentUtcDate = LocalDate.now(ZoneOffset.UTC);
        boolean hasUpdates = false;

        for (BirthRecord record : userRecords) {
            LocalDate nextBirthday = record.getBirthDate().withYear(currentUtcDate.getYear());
            if (nextBirthday.isBefore(currentUtcDate)) {
                nextBirthday = nextBirthday.plusYears(1);
            }
            
            long daysRemaining = ChronoUnit.DAYS.between(currentUtcDate, nextBirthday);
            
            if (daysRemaining == 0) {
                notificationMessage.append("🎉 Today is your birthday! Happy Birthday, *").append(record.getFullName()).append("*! 🎂\n");
            } else {
                notificationMessage.append("⏳ Your birthday is in ").append(daysRemaining).append(" days.\n");
            }
            hasUpdates = true;
        }

        if (hasUpdates) {
            telegramBot.execute(
                new SendMessage(chatId, notificationMessage.toString())
                    .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
            );
        }
    }
}
