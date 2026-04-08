package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.TgUser;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;


@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tg_users", schema = "bot_core")
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

    @Column(name = "app_user_id")
    private Long appUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private EGender gender;

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
                .gender(this.getGender())
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
        entity.setAppUserId(domain.getAppUserId());
        entity.setGender(domain.getGender());

        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());

        return entity;
    }
}
