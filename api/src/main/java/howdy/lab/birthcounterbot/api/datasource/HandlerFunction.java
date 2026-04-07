package howdy.lab.birthcounterbot.api.datasource;

import howdy.lab.birthcounterbot.api.enums.EDatabaseFunction;

public interface HandlerFunction<R, P> {

    EDatabaseFunction getMethod();

    R execute(P param);
}
