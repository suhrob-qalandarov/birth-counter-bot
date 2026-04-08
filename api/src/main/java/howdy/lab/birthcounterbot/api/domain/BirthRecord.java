package howdy.lab.birthcounterbot.api.domain;

import howdy.lab.birthcounterbot.api.domain.audit.FullAuditableResult;
import howdy.lab.birthcounterbot.api.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BirthRecord extends FullAuditableResult {
    private Long id;
    private Long tgUserId;
    private String fullName;
    private LocalDate birthDate;
    private EGender gender;
    private LocalDateTime nextNotificationTimeUtc;
    private Long timezoneId;
    private Double latitude;
    private Double longitude;
    private LocalTime notificationTime;
    private LocalTime notificationTimeUtc;
    private Boolean isActive;
}