package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateBotSessionFunction;
import howdy.lab.birthcounterbot.datasource.function.UpdateBotSessionFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DaySelectionCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && update.callbackQuery().data() != null
                && update.callbackQuery().data().startsWith("SELECT_DAY_");
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        int selectedDay = Integer.parseInt(callbackQuery.data().replace("SELECT_DAY_", ""));

        BotSession session = getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        session.setTempDay(selectedDay);
        updateBotSessionFunction.execute(session);

        Integer year = session.getTempYear();
        Integer month = session.getTempMonth();

        if (year == null || month == null) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }

        String formattedDate = String.format("%02d.%02d.%04d", selectedDay, month, year);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Confirm").callbackData("CONFIRM_BIRTH_DATE"),
                new InlineKeyboardButton("Edit").callbackData("EDIT_BIRTH_DATE")
        );

        telegramBot.execute(new EditMessageText(chatId, messageId, "Chosen date: " + formattedDate)
                .replyMarkup(markup));
    }
}
