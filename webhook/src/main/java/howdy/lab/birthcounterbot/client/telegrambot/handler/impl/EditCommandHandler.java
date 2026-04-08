package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EditCommandHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && update.message().text() != null
                && update.message().text().startsWith("/edit");
    }

    @Override
    public void handle(Update update) {
        var chatId = update.message().chat().id();

        // Set edit mode in session
        BotSession session = botSessionDatasource.getByChatId(chatId);
        session.setIsEditMode(true);
        botSessionDatasource.update(session.getId(), session);

        int page = 0;
        InlineKeyboardMarkup keyboard = AgreeTosCallbackHandler.generateYearKeyboard(page, true);
        
        SendMessage request = new SendMessage(chatId, "Great! Now, please choose your birth year")
                .replyMarkup(keyboard);
        telegramBot.execute(request);
    }
}
