package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YearPaginationCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null 
                && update.callbackQuery().data() != null 
                && update.callbackQuery().data().startsWith("YEAR_PAGE_");
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();
        
        String data = callbackQuery.data();
        int page = Integer.parseInt(data.replace("YEAR_PAGE_", ""));

        BotSession session = botSessionDatasource.getByChatId(chatId);
        
        var keyboard = AgreeTosCallbackHandler.generateYearKeyboard(page, Boolean.TRUE.equals(session.getIsEditMode()));
        
        telegramBot.execute(new EditMessageReplyMarkup(chatId, messageId).replyMarkup(keyboard));
    }
}
