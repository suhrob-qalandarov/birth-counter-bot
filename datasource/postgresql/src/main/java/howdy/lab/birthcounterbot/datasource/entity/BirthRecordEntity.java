package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.BirthRecord;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "birth_records", schema = "bot_core", indexes = {
        @Index(name = "idx_br_next_notif", columnList = "next_notification_time_utc")
})
@SQLRestriction(value = " deleted = false")
public class BirthRecordEntity extends FullAuditableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tg_user_id", nullable = false)
    private TgUserEntity tgUser;

    @Column(name = "tg_user_id", insertable = false, updatable = false)
    private Long tgUserId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private EGender gender;

    @Column(name = "next_notification_time_utc")
    private LocalDateTime nextNotificationTimeUtc;

    public BirthRecord map2Domain() {
        return BirthRecord.builder()
                .id(this.getId())
                .tgUserId(this.getTgUserId())
                .fullName(this.getFullName())
                .birthDate(this.getBirthDate())
                .gender(this.getGender())
                .nextNotificationTimeUtc(this.getNextNotificationTimeUtc())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .deleted(this.isDeleted())
                .build();
    }

    public static BirthRecordEntity map2Entity(BirthRecord domain, final EntityManager em) {
        final var entity = new BirthRecordEntity();

        entity.setId(domain.getId());

        entity.setFullName(domain.getFullName());
        entity.setBirthDate(domain.getBirthDate());
        entity.setGender(domain.getGender());
        entity.setNextNotificationTimeUtc(domain.getNextNotificationTimeUtc());

        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());

        Long tgUserIdParam = domain.getTgUserId();
        if (nonNull(tgUserIdParam)) {
            final var tgUserReference = em.getReference(TgUserEntity.class, tgUserIdParam);
            entity.setTgUser(tgUserReference);
            entity.setTgUserId(tgUserIdParam);
        }

        return entity;
    }
}
