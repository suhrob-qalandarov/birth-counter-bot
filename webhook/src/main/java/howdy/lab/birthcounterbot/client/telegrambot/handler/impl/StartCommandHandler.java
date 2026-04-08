package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateBotSessionFunction;
import howdy.lab.birthcounterbot.datasource.function.GetOrCreateTgUserFunction;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final GetOrCreateTgUserFunction getOrCreateTgUserFunction;
    private final GetOrCreateBotSessionFunction getOrCreateBotSessionFunction;

    @Value("${telegram.bot.user-agreement-link:https://example.com/terms}")
    private String userAgreementLink;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && update.message().text() != null
                && update.message().text().startsWith("/start");
    }

    @Override
    public void handle(Update update) {
        final var from = update.message().from();
        final var chatId = update.message().chat().id();

        final var tgUser = getOrCreateTgUserFunction.execute(TgUser.builder()
                .id(from.id())
                .chatId(chatId)
                .username(from.username())
                .firstName(from.firstName())
                .lastName(from.lastName())
                .languageCode(from.languageCode())
                .premium(from.isPremium())
                .bot(from.isBot())
                .build());

        getOrCreateBotSessionFunction.execute(BotSession.builder()
                .chatId(chatId)
                .step(EBotStep.START.name())
                .build());

        String messageText = "Welcome, " + tgUser.getFirstName() + "!\nBy using the bot, you agree to our [user agreement](" + userAgreementLink + ").";
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("I agree").callbackData("AGREE_TOS")
        );
        telegramBot.execute(new SendMessage(chatId, messageText)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(keyboard));
    }
}
