package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
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

    @Column(name = "zone_id", length = 100)
    private String zoneId = "Asia/Tashkent";

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
        return new TgUser()
                .setId(this.getId())
                .setChatId(this.getChatId())
                .setUsername(this.getUsername())
                .setFirstName(this.getFirstName())
                .setLastName(this.getLastName())
                .setPhoneNumber(this.getPhoneNumber())
                .setPremium(this.getPremium())
                .setCanJoinGroups(this.getCanJoinGroups())
                .setLanguageCode(this.getLanguageCode())
                .setBot(this.getBot())
                .setStatus(this.getStatus())
                .setAppUserId(this.getAppUserId())
                .setZoneId(this.getZoneId())
                .setLatitude(this.getLatitude())
                .setLongitude(this.getLongitude())
                .setNotificationTime(this.getNotificationTime())
                .setNotificationTimeUtc(this.getNotificationTimeUtc());
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
        entity.setAppUserId(domain.getAppUserId());
        entity.setZoneId(domain.getZoneId());
        entity.setLatitude(domain.getLatitude());
        entity.setLongitude(domain.getLongitude());
        entity.setNotificationTime(domain.getNotificationTime());
        entity.setNotificationTimeUtc(domain.getNotificationTimeUtc());

        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeleted(domain.isDeleted());

        return entity;
    }
}
