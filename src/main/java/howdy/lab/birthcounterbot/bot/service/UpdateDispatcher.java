package howdy.lab.birthcounterbot.bot.service;

import com.pengrad.telegrambot.model.Update;
import howdy.lab.birthcounterbot.bot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;

    public void dispatch(Update update) {
        if (update == null) {
            return;
        }

        try {
            for (UpdateHandler handler : handlers) {
                if (handler.supports(update)) {
                    handler.handle(update);
                    return; // Stop at first matched handler
                }
            }
            log.warn("No handler found for update: {}", update.updateId());
        } catch (Exception e) {
            log.error("Error occurred while dispatching update {}: {}", update.updateId(), e.getMessage(), e);
        }
    }
}
