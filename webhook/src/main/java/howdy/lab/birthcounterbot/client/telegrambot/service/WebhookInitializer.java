package howdy.lab.birthcounterbot.client.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import howdy.lab.birthcounterbot.client.telegrambot.config.BotProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookInitializer {

    private final TelegramBot telegramBot;
    private final BotProperties botProperties;

    @PostConstruct
    public void init() {
        String webhookUrl = botProperties.getWebhookPath() + "/callback/update";
        log.info("Setting webhook to URL: {}", webhookUrl);
        
        SetWebhook request = new SetWebhook().url(webhookUrl);
        BaseResponse response = telegramBot.execute(request);
        
        if (response != null && response.isOk()) {
            log.info("Telegram bot started successfully in WEBHOOK mode! Bot ID: {}, Username: @{}", 
                     botProperties.getId(), botProperties.getUsername());
        } else {
            log.error("Failed to set webhook. Telegram error code: {}, description: {}", 
                      response != null ? response.errorCode() : "null", 
                      response != null ? response.description() : "No response from Telegram API");
        }
    }
}
