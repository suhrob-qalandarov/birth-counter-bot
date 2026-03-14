package howdy.lab.birthcounterbot.client.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    
    /**
     * Check if this handler supports the given update.
     */
    boolean supports(Update update);

    /**
     * Handle the given update.
     */
    void handle(Update update);
}
