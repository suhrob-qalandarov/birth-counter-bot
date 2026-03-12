package howdy.lab.birthcounterbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import howdy.lab.birthcounterbot.bot.config.BotProperties;
import howdy.lab.birthcounterbot.bot.service.UpdateDispatcher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final UpdateDispatcher updateDispatcher;
    private final BotProperties botProperties;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                log.error("Telegram error: ", e);
            }
        });
        log.info("Telegram bot started successfully in polling mode! Bot ID: {}, Username: @{}", botProperties.getId(), botProperties.getUsername());
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            updateDispatcher.dispatch(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
