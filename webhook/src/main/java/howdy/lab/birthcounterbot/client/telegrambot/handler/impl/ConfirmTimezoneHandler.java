package howdy.lab.birthcounterbot.client.telegrambot.handler.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.datasource.BotSessionDatasource;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.datasource.TimezoneDatasource;
import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.api.domain.BotSession;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.domain.Timezone;
import howdy.lab.birthcounterbot.api.enums.EBotStep;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.client.telegrambot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ConfirmTimezoneHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final BotSessionDatasource botSessionDatasource;
    private final TgUserDatasource tgUserDatasource;
    private final TimezoneDatasource timezoneDatasource;
    private final BirthRecordDatasource birthRecordDatasource;

    @Override
    public boolean supports(Update update) {
        return update.message() != null
                && "Confirm timezone".equals(update.message().text());
    }

    @Override
    public void handle(Update update) {
        var message = update.message();
        var chatId = message.chat().id();

        BotSession session = botSessionDatasource.getByChatId(chatId);
        if (session == null || !EBotStep.SETTINGS_TIMEZONE.name().equals(session.getStep())) {
            return;
        }

        // 1. Get TgUser
        TgUser tgUser = tgUserDatasource.getByChatId(chatId);

        // 2. Resolve or Create Timezone
        String tzName = session.getTempTimezone() != null ? session.getTempTimezone() : "Asia/Tashkent";
        Timezone savedTimezone = timezoneDatasource.getOrCreateTimezone(
                Timezone.builder().zoneName(tzName).isActive(true).build()
        );

        // 3. Resolve BirthDate
        int year = session.getTempYear() != null ? session.getTempYear() : LocalDate.now().getYear();
        int month = session.getTempMonth() != null ? session.getTempMonth() : 1;
        int day = session.getTempDay() != null ? session.getTempDay() : 1;
        LocalDate birthDate = LocalDate.of(year, month, day);

        // 4. Time calculations
        ZoneId zoneId = ZoneId.of(tzName);
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime localZdt = nowUtc.withZoneSameInstant(zoneId);

        LocalTime utcNotifTime = nowUtc.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime localNotifTime = localZdt.toLocalTime().truncatedTo(ChronoUnit.MINUTES);

        // 5. Versioning: Deactivate old record and create a NEW one
        String fullName = tgUser.getFirstName() + (tgUser.getLastName() != null ? " " + tgUser.getLastName() : "");
        
        // Find currently active record for this user
        BirthRecord activeRecord = birthRecordDatasource.findActiveByTgUserId(tgUser.getId());
        if (activeRecord != null) {
            activeRecord.setIsActive(false);
            birthRecordDatasource.update(activeRecord.getId(), activeRecord);
        }

        // Create the new record as active
        BirthRecord newRecord = BirthRecord.builder()
                .tgUserId(tgUser.getId())
                .fullName(fullName)
                .birthDate(birthDate)
                .gender(session.getTempGender() != null ? EGender.valueOf(session.getTempGender()) : null)
                .timezoneId(savedTimezone.getId())
                .latitude(session.getTempLatitude())
                .longitude(session.getTempLongitude())
                .notificationTime(localNotifTime)
                .notificationTimeUtc(utcNotifTime)
                .isActive(true)
                .build();
        birthRecordDatasource.create(newRecord);

        // Calculate days to next birthday
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalDate currentDate = now.toLocalDate();
        
        LocalDate nextBirthday = birthDate.withYear(currentDate.getYear());
        if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, nextBirthday);

        // Send confirmation
        String text = "Your birthday is in " + daysRemaining + " days";

        telegramBot.execute(new SendMessage(chatId, text)
                .replyMarkup(new ReplyKeyboardRemove()));
                
        // Reset session step and edit mode
        session.setStep(EBotStep.MAIN_MENU.name());
        session.setIsEditMode(false);
        botSessionDatasource.update(session.getId(), session);
    }
}
