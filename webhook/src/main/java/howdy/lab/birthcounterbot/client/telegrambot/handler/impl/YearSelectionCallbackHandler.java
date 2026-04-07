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

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class YearSelectionCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && update.callbackQuery().data() != null
                && update.callbackQuery().data().startsWith("SELECT_YEAR_");
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        int selectedYear = Integer.parseInt(callbackQuery.data().replace("SELECT_YEAR_", ""));

        // Retrieve and update BotSession
        BotSession session = getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        session.setTempYear(selectedYear);
        updateBotSessionFunction.execute(session);

        // Generate the Months Keyboard
        InlineKeyboardMarkup markup = generateMonthKeyboard(selectedYear);

        telegramBot.execute(new EditMessageText(chatId, messageId, "Now choose your birth month")
                .replyMarkup(markup));
    }

    public static InlineKeyboardMarkup generateMonthKeyboard(int year) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        // Row 1: <- Year ->
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(new InlineKeyboardButton("<-").callbackData("SELECT_YEAR_" + (year - 1)));
        headerRow.add(new InlineKeyboardButton(String.valueOf(year)).callbackData("IGNORE"));
        headerRow.add(new InlineKeyboardButton("->").callbackData("SELECT_YEAR_" + (year + 1)));
        markup.addRow(headerRow.toArray(new InlineKeyboardButton[0]));

        // Rows 2-4: Months (4 per row)
        int monthNum = 1;
        for (int i = 0; i < 3; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                String monthName = Month.of(monthNum).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                row.add(new InlineKeyboardButton(monthName).callbackData("SELECT_MONTH_" + monthNum));
                monthNum++;
            }
            markup.addRow(row.toArray(new InlineKeyboardButton[0]));
        }

        return markup;
    }
}
