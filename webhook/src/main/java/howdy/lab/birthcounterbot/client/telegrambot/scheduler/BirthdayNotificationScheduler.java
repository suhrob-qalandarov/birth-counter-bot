package howdy.lab.birthcounterbot.client.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import howdy.lab.birthcounterbot.api.datasource.BirthRecordDatasource;
import howdy.lab.birthcounterbot.api.datasource.TgUserDatasource;
import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.api.domain.TgUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayNotificationScheduler {

    private final TelegramBot telegramBot;
    private final TgUserDatasource tgUserDatasource;
    private final BirthRecordDatasource birthRecordDatasource;

    @Scheduled(cron = "0 * * * * *") // Runs at second 0 of every minute
    public void sendDailyBirthdayNotifications() {
        LocalTime currentUtcTime = LocalTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
        log.info("Running BirthdayNotificationScheduler at UTC time: {}", currentUtcTime);

        List<TgUser> eligibleUsers = tgUserDatasource.findAll().stream()
                .filter(u -> u.getNotificationTimeUtc() != null)
                .filter(u -> u.getNotificationTimeUtc().truncatedTo(ChronoUnit.MINUTES).equals(currentUtcTime))
                .toList();

        log.info("Found {} eligible users for notification at {}", eligibleUsers.size(), currentUtcTime);
        
        if (eligibleUsers.isEmpty()) {
            return;
        }

        LocalDate currentUtcDate = LocalDate.now(ZoneOffset.UTC); // Ideally we use user's local date, but difference is small for calculating days diff

        for (TgUser user : eligibleUsers) {
            List<BirthRecord> userRecords = birthRecordDatasource.findAllByTgUserId(user.getId());
            if (userRecords.isEmpty()) {
                continue;
            }

            StringBuilder notificationMessage = new StringBuilder();

            boolean hasUpdates = false;

            for (BirthRecord record : userRecords) {
                // Calculate days remaining. 
                // Using UTC date for simplicity. In a strict setup, we'd use their timezone date.
                LocalDate nextBirthday = record.getBirthDate().withYear(currentUtcDate.getYear());
                if (nextBirthday.isBefore(currentUtcDate)) {
                    nextBirthday = nextBirthday.plusYears(1);
                }
                
                long daysRemaining = ChronoUnit.DAYS.between(currentUtcDate, nextBirthday);
                
                if (daysRemaining == 0) {
                    notificationMessage.append("🎉 Today is your birthday! Happy Birthday, *").append(record.getFullName()).append("*! 🎂\n");
                    hasUpdates = true;
                } else { 
                    notificationMessage.append("⏳ Your birthday is in ").append(daysRemaining).append(" days.\n");
                    hasUpdates = true;
                }
            }

            if (hasUpdates) {
                try {
                    telegramBot.execute(
                        new SendMessage(user.getChatId(), notificationMessage.toString())
                            .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                    );
                    log.info("Sent birthday notification to user chat ID: {}", user.getChatId());
                } catch (Exception e) {
                    log.error("Failed to send notification to chat ID: {}", user.getChatId(), e);
                }
            }
        }
    }
}
