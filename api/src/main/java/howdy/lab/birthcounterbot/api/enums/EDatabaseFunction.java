package howdy.lab.birthcounterbot.api.enums;

public enum EDatabaseFunction {

    // TgUser
    TG_USER_GET_OR_CREATE,
    TG_USER_GET_BY_CHAT_ID,
    TG_USER_UPDATE,

    // BirthRecord
    BIRTH_RECORD_CREATE,
    BIRTH_RECORD_FIND_BY_USER,

    // BotSession
    BOT_SESSION_GET_OR_CREATE,
    BOT_SESSION_UPDATE,
    BOT_SESSION_DELETE
}
