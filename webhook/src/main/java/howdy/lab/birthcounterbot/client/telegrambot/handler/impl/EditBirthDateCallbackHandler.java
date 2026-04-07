package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EditBirthDateCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && "EDIT_BIRTH_DATE".equals(update.callbackQuery().data());
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        // Regenerate year selection starting from page 0
        InlineKeyboardMarkup keyboard = AgreeTosCallbackHandler.generateYearKeyboard(0);

        telegramBot.execute(new EditMessageText(chatId, messageId, "To start, choose your birth year")
                .replyMarkup(keyboard));
    }
}
