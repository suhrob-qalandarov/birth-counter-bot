package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tg_users", schema = "bot_core", indexes = {
        @Index(name = "idx_tguser_notif_utc", columnList = "notification_time_utc")
})
@SQLRestriction(value = " deleted = false")
public class TgUserEntity extends FullAuditableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "chat_id", unique = true, nullable = false, updatable = false)
    private Long chatId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_premium")
    private Boolean premium;

    @Column(name = "can_join_groups")
    private Boolean canJoinGroups;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "is_bot")
    private Boolean bot;

    @Column(name = "status")
    private Integer status;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "timezone_id")
    private Long timezoneId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "app_user_id")
    private Long appUserId;

    @Column(name = "notification_time")
    private LocalTime notificationTime;

    @Column(name = "notification_time_utc")
    private LocalTime notificationTimeUtc;

    public TgUser map() {
        return TgUser.builder()
                .id(this.getId())
                .chatId(this.getChatId())
                .username(this.getUsername())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .phoneNumber(this.getPhoneNumber())
                .premium(this.getPremium())
                .canJoinGroups(this.getCanJoinGroups())
                .languageCode(this.getLanguageCode())
                .bot(this.getBot())
                .status(this.getStatus())
                .appUserId(this.getAppUserId())
                .birthDate(this.getBirthDate())
                .timezoneId(this.getTimezoneId())
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .notificationTime(this.getNotificationTime())
                .notificationTimeUtc(this.getNotificationTimeUtc())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .deleted(this.isDeleted())
                .build();
    }

    public static TgUserEntity map(TgUser domain) {
        final var entity = new TgUserEntity();

        entity.setId(domain.getId());
        entity.setChatId(domain.getChatId());
        entity.setUsername(domain.getUsername());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setPremium(domain.getPremium());
        entity.setCanJoinGroups(domain.getCanJoinGroups());
        entity.setLanguageCode(domain.getLanguageCode());
        entity.setBot(domain.getBot());
        entity.setStatus(domain.getStatus());
        entity.setBirthDate(domain.getBirthDate());
        entity.setAppUserId(domain.getAppUserId());
        entity.setTimezoneId(domain.getTimezoneId());
        entity.setLatitude(domain.getLatitude());
        entity.setLongitude(domain.getLongitude());
        entity.setNotificationTime(domain.getNotificationTime());
        entity.setNotificationTimeUtc(domain.getNotificationTimeUtc());

        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());

        return entity;
    }
}
