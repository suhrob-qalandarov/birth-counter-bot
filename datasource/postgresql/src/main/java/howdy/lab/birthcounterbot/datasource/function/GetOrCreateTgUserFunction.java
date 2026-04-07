package howdy.lab.birthcounterbot.datasource.function;

import howdy.lab.birthcounterbot.api.datasource.HandlerFunction;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.enums.EDatabaseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrCreateTgUserFunction implements HandlerFunction<TgUser, TgUser> {

    private final TgUserDatasource tgUserDatasource;

    @Override
    public EDatabaseFunction getMethod() {
        return EDatabaseFunction.TG_USER_GET_OR_CREATE;
    }

    @Override
    public TgUser execute(TgUser param) {
        return tgUserDatasource.getOrCreateTgUserWithChatId(param);
    }
}
