package howdy.lab.birthcounterbot.datasource.function;

import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.datasource.HandlerFunction;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.enums.EDatabaseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrCreateBotSessionFunction implements HandlerFunction<BotSession, BotSession> {

    private final BotSessionDatasource botSessionDatasource;

    @Override
    public EDatabaseFunction getMethod() {
        return EDatabaseFunction.BOT_SESSION_GET_OR_CREATE;
    }

    @Override
    public BotSession execute(BotSession param) {
        return botSessionDatasource.getOrCreate(param);
    }
}
