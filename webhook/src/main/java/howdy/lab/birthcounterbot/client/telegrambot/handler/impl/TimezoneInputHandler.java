package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.UpdateBotSessionFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TimezoneInputHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        if (update.message() == null) {
            return false;
        }
        if (update.message().text() != null && update.message().text().startsWith("/")) {
            return false;
        }
        
        // Let's exclude "Confirm "* or "Edit "* if needed, but they trigger text messages too!
        if (update.message().text() != null && (
                update.message().text().equals("Confirm timezone") ||
                update.message().text().equals("Edit timezone"))) {
            return false;
        }

        try {
            Long chatId = update.message().chat().id();
            BotSession session = botSessionDatasource.getByChatId(chatId);
            return session != null && EBotStep.SETTINGS_TIMEZONE.name().equals(session.getStep());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void handle(Update update) {
        var message = update.message();
        var chatId = message.chat().id();

        // Extract location if present
        Double latitude = null;
        Double longitude = null;
        if (message.location() != null) {
            latitude = message.location().latitude().doubleValue();
            longitude = message.location().longitude().doubleValue();
        }

        // Mock resolving timezone string
        String resolvedTimezoneId = "Asia/Tashkent";
        
        // Save to BotSession
        BotSession session = botSessionDatasource.getByChatId(chatId);
        session.setTempTimezone(resolvedTimezoneId);
        session.setTempLatitude(latitude);
        session.setTempLongitude(longitude);
        updateBotSessionFunction.execute(session);

        // Current time in that timezone
        ZoneId zoneId = ZoneId.of(resolvedTimezoneId);
        String currentTime = ZonedDateTime.now(zoneId).format(DateTimeFormatter.ofPattern("HH:mm"));

        String responseText = "🌍 Your timezone is: " + resolvedTimezoneId + "\n" +
                "🕔 Your current time is: " + currentTime;

        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(
                new KeyboardButton("Confirm timezone"),
                new KeyboardButton("Edit timezone")
        ).resizeKeyboard(true).oneTimeKeyboard(true);

        telegramBot.execute(new SendMessage(chatId, responseText)
                .replyMarkup(replyKeyboard));
    }
}
