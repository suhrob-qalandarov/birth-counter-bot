package howdy.lab.birthcounterbot.client.telegrambot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
public class BotProperties {
    private String token;
    private String id;
    private String username;
    private String webhookPath;
}
