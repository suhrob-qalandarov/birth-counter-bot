package howdy.lab.birthcounterbot.bot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.bot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final TelegramBot telegramBot;

    @Override
    public boolean supports(Update update) {
        return update.message() != null 
                && update.message().text() != null 
                && update.message().text().startsWith("/start");
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        SendMessage request = new SendMessage(chatId, "Welcome to Birth Counter Bot!");
        telegramBot.execute(request);
    }
}
