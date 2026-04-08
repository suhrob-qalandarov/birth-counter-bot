package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.datasource.TimezoneDatasource;
import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.domain.Timezone;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateBotSessionFunction;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateTgUserFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateTgUserFunction getOrCreateTgUserFunction;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final BirthRecordDatasource birthRecordDatasource;
    private final TimezoneDatasource timezoneDatasource;

    @Value("${telegram.bot.user-agreement-link:https://example.com/terms}")
    private String userAgreementLink;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && update.message().text() != null
                && update.message().text().startsWith("/start");
    }

    @Override
    public void handle(Update update) {
        final var from = update.message().from();
        final var chatId = update.message().chat().id();

        final var tgUser = getOrCreateTgUserFunction.execute(TgUser.builder()
                .id(from.id())
                .chatId(chatId)
                .username(from.username())
                .firstName(from.firstName())
                .lastName(from.lastName())
                .languageCode(from.languageCode())
                .premium(from.isPremium())
                .bot(from.isBot())
                .build());

        getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .isEditMode(false)
                .build());

        // Check if user has already agreed and has a birth record
        if (Boolean.TRUE.equals(tgUser.getIsAgreed())) {
            var record = birthRecordDatasource.findActiveByTgUserId(tgUser.getId());
            if (record != null) {
                final var tz = timezoneDatasource.get(record.getTimezoneId());
                final var zoneId = ZoneId.of(tz.getZoneName());
                
                var now = ZonedDateTime.now(zoneId);
                var currentDate = now.toLocalDate();
                var birthDate = record.getBirthDate();
                
                var nextBirthday = birthDate.withYear(currentDate.getYear());
                if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
                    nextBirthday = nextBirthday.plusYears(1);
                }
                
                var daysRemaining = ChronoUnit.DAYS.between(currentDate, nextBirthday);
                String text = "Your birthday is in " + daysRemaining + " days";
                
                telegramBot.execute(new SendMessage(chatId, text));
                return;
            } else {
                // Agreed but no record, start year selection
                int page = 0;
                InlineKeyboardMarkup keyboard = AgreeTosCallbackHandler.generateYearKeyboard(page, false);
                
                SendMessage request = new SendMessage(chatId, "Great! Now, please choose your birth year")
                        .replyMarkup(keyboard);
                telegramBot.execute(request);
                return;
            }
        }

        var messageText = "Welcome, " + tgUser.getFirstName() + "!\nBy using the bot, you agree to our [user agreement](" + userAgreementLink + ").";
        var keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("I agree").callbackData("AGREE_TOS")
        );
        telegramBot.execute(new SendMessage(chatId, messageText)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(keyboard));
    }
}
