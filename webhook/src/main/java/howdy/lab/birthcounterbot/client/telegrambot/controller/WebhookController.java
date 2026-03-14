package howdy.lab.birthcounterbot.client.telegrambot.controller;

import com.pengrad.telegrambot.utility.BotUtils;
import com.pengrad.telegrambot.model.Update;
import howdy.lab.birthcounterbot.client.telegrambot.service.UpdateDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final UpdateDispatcher updateDispatcher;

    @PostMapping("/callback/update")
    public ResponseEntity<String> onUpdateReceived(@RequestBody String requestBody) {
        try {
            Update update = BotUtils.parseUpdate(requestBody);
            updateDispatcher.dispatch(update);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error evaluating update from webhook: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
