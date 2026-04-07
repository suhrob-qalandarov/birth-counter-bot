package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
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
public class CalendarNavigationCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && update.callbackQuery().data() != null
                && (update.callbackQuery().data().equals("CALENDAR_PREV_MONTH")
                || update.callbackQuery().data().equals("CALENDAR_NEXT_MONTH"));
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        BotSession session = getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        Integer year = session.getTempYear();
        Integer month = session.getTempMonth();

        if (year == null || month == null) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }

        if (callbackQuery.data().equals("CALENDAR_PREV_MONTH")) {
            month--;
            if (month < 1) {
                month = 12;
                year--;
            }
        } else if (callbackQuery.data().equals("CALENDAR_NEXT_MONTH")) {
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        session.setTempYear(year);
        session.setTempMonth(month);
        updateBotSessionFunction.execute(session);

        var markup = MonthSelectionCallbackHandler.generateDayKeyboard(year, month);

        telegramBot.execute(new EditMessageReplyMarkup(chatId, messageId)
                .replyMarkup(markup));
    }
}
