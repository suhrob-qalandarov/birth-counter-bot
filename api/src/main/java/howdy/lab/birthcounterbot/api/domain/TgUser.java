package howdy.lab.birthcounterbot.api.domain;

import howdy.lab.birthcounterbot.api.domain.audit.FullAuditableResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TgUser extends FullAuditableResult {
    private Long id;
    private Long chatId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean premium;
    private Boolean canJoinGroups;
    private String languageCode;
    private Boolean bot;
    private Integer status;
    private Long appUserId;

    private LocalDate birthDate;
    private Long timezoneId;
    private Double latitude;
    private Double longitude;
    private LocalTime notificationTime;
    private LocalTime notificationTimeUtc;
}
