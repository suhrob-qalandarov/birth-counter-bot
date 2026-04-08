package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AgreeTosCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final TgUserDatasource tgUserDatasource;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && "AGREE_TOS".equals(update.callbackQuery().data());
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        // 0. Save agreement status
        TgUser tgUser = tgUserDatasource.getByChatId(chatId);
        tgUser.setIsAgreed(true);
        tgUserDatasource.update(tgUser.getId(), tgUser);

        // 1. Edit welcome message to ask for gender
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Male").callbackData("SELECT_GENDER_MALE"),
                new InlineKeyboardButton("Female").callbackData("SELECT_GENDER_FEMALE")
        );
        
        telegramBot.execute(new EditMessageText(chatId, messageId, "Please select your gender")
                .replyMarkup(keyboard));
    }

    public static InlineKeyboardMarkup generateYearKeyboard(int page) {
        int currentYear = Year.now().getValue();
        int maxYear = currentYear - (page * 20);
        int minYear = maxYear - 19;

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        // Row 1: arrows
        // <- goes to older years (page + 1). -> goes to newer years (page - 1)
        List<InlineKeyboardButton> arrowRow = new ArrayList<>();
        arrowRow.add(new InlineKeyboardButton("<-").callbackData("YEAR_PAGE_" + (page + 1)));
        if (page > 0) {
            arrowRow.add(new InlineKeyboardButton("->").callbackData("YEAR_PAGE_" + (page - 1)));
        } else {
            // Placeholder or disable the right arrow if on the newest page
            arrowRow.add(new InlineKeyboardButton(" ").callbackData("IGNORE"));
        }
        markup.addRow(arrowRow.toArray(new InlineKeyboardButton[0]));

        // Rows 2-5: years (5 years per row)
        int yearToProcess = minYear;
        for (int i = 0; i < 4; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(new InlineKeyboardButton(String.valueOf(yearToProcess))
                        .callbackData("SELECT_YEAR_" + yearToProcess));
                yearToProcess++;
            }
            markup.addRow(row.toArray(new InlineKeyboardButton[0]));
        }

        return markup;
    }
}
