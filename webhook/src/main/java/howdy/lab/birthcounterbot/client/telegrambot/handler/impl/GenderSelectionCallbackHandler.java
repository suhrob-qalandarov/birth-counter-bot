package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.UpdateBotSessionFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenderSelectionCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;
    private final TgUserDatasource tgUserDatasource;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && update.callbackQuery().data().startsWith("SELECT_GENDER_");
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var data = callbackQuery.data();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        String genderStr = data.replace("SELECT_GENDER_", "");
        
        // Save gender to BotSession
        BotSession session = botSessionDatasource.getByChatId(chatId);
        session.setTempGender(genderStr);
        updateBotSessionFunction.execute(session);

        // Save gender to TgUser
        TgUser tgUser = tgUserDatasource.getByChatId(chatId);
        tgUser.setGender(EGender.valueOf(genderStr));
        tgUserDatasource.update(tgUser.getId(), tgUser);

        // Remove buttons from the gender selection message
        telegramBot.execute(new EditMessageReplyMarkup(chatId, messageId)
                .replyMarkup(new InlineKeyboardMarkup()));

        // Move to year selection
        int page = 0;
        InlineKeyboardMarkup keyboard = AgreeTosCallbackHandler.generateYearKeyboard(page);
        
        SendMessage request = new SendMessage(chatId, "Great! Now, please choose your birth year")
                .replyMarkup(keyboard);
        telegramBot.execute(request);
    }
}
