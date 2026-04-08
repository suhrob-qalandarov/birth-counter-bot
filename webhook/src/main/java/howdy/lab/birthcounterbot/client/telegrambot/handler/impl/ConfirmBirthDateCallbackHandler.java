package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateBotSessionFunction;
import howdy.lab.birthcounterbot.datasource.function.UpdateBotSessionFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmBirthDateCallbackHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;
    private final UpdateBotSessionFunction updateBotSessionFunction;

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null
                && "CONFIRM_BIRTH_DATE".equals(update.callbackQuery().data());
    }

    @Override
    public void handle(Update update) {
        var callbackQuery = update.callbackQuery();
        var chatId = callbackQuery.message().chat().id();
        var messageId = callbackQuery.message().messageId();

        // Remove inline keyboard from the "Chosen date" message
        telegramBot.execute(new EditMessageReplyMarkup(chatId, messageId)
                .replyMarkup(new InlineKeyboardMarkup()));

        // Update BotSession step
        BotSession session = getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        session.setStep(EBotStep.SETTINGS_TIMEZONE.name());
        updateBotSessionFunction.execute(session);

        // Send timezone prompt with reply keyboard
        String text = "🌍 To work correctly, the bot needs to know your timezone. Please send your current location or the name of the nearest big city";
        
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(
                new KeyboardButton("📩 Share timezone").requestLocation(true)
        ).resizeKeyboard(true).oneTimeKeyboard(true);

        telegramBot.execute(new SendMessage(chatId, text)
                .replyMarkup(replyKeyboard));
    }
}
