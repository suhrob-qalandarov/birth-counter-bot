package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelEditCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && "CANCEL_EDIT".equals(update.callbackQuery().data());
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        // 1. Edit message to indicate cancellation
        telegramBot.execute(new EditMessageText(chatId, messageId, "Editing cancelled. Your record remains unchanged.")
                .replyMarkup(new InlineKeyboardMarkup()));

        // 2. Reset session step to MAIN_MENU
        BotSession session = botSessionDatasource.getByChatId(chatId);
        if (session != null) {
            session.setStep(EBotStep.MAIN_MENU.name());
            session.setIsEditMode(false);
            botSessionDatasource.update(session.getId(), session);
        }
    }
}
