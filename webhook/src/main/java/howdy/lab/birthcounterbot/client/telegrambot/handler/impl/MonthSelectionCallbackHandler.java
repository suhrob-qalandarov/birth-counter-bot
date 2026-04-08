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
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MonthSelectionCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && update.callbackQuery().data() != null
                && update.callbackQuery().data().startsWith("SELECT_MONTH_");
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        int selectedMonth = Integer.parseInt(callbackQuery.data().replace("SELECT_MONTH_", ""));

        BotSession session = getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        session.setTempMonth(selectedMonth);
        updateBotSessionFunction.execute(session);

        Integer tempYear = session.getTempYear();
        if (tempYear == null) {
            tempYear = LocalDate.now().getYear(); // fallback
        }

        InlineKeyboardMarkup markup = generateDayKeyboard(tempYear, selectedMonth, Boolean.TRUE.equals(session.getIsEditMode()));

        telegramBot.execute(new EditMessageText(chatId, messageId, "Choose your birth day")
                .replyMarkup(markup));
    }

    public static InlineKeyboardMarkup generateDayKeyboard(int year, int month, boolean showCancel) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String monthStr = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String headerText = year + ", " + monthStr;

        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(new InlineKeyboardButton("<-").callbackData("CALENDAR_PREV_MONTH"));
        headerRow.add(new InlineKeyboardButton(headerText).callbackData("IGNORE"));
        headerRow.add(new InlineKeyboardButton("->").callbackData("CALENDAR_NEXT_MONTH"));
        markup.addRow(headerRow.toArray(new InlineKeyboardButton[0]));

        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();

        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            currentRow.add(new InlineKeyboardButton(String.valueOf(day))
                    .callbackData("SELECT_DAY_" + day));
            
            if (currentRow.size() == 8) {
                markup.addRow(currentRow.toArray(new InlineKeyboardButton[0]));
                currentRow = new ArrayList<>();
            }
        }
        
        if (!currentRow.isEmpty()) {
            markup.addRow(currentRow.toArray(new InlineKeyboardButton[0]));
        }

        if (showCancel) {
            markup.addRow(new InlineKeyboardButton("Cancel").callbackData("CANCEL_EDIT"));
        }

        return markup;
    }
}
