package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.BotSession;
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
@Table(name = "bot_sessions", schema = "bot_core")
@SQLRestriction(value = " deleted = false")
public class BotSessionEntity extends FullAuditableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(name = "step", nullable = false)
    private String step;

    @Column(name = "temp_year")
    private Integer tempYear;

    @Column(name = "temp_month")
    private Integer tempMonth;

    @Column(name = "temp_day")
    private Integer tempDay;

    @Column(name = "temp_full_name")
    private String tempFullName;

    @Column(name = "temp_gender")
    private String tempGender;

    @Column(name = "temp_timezone")
    private String tempTimezone;

    @Column(name = "temp_latitude")
    private Double tempLatitude;

    @Column(name = "temp_longitude")
    private Double tempLongitude;

    @Column(name = "is_edit_mode")
    private Boolean isEditMode;

    public BotSession map2Domain() {
        return BotSession.builder()
                .id(this.getId())
                .chatId(this.getChatId())
                .step(this.getStep())
                .tempYear(this.getTempYear())
                .tempMonth(this.getTempMonth())
                .tempDay(this.getTempDay())
                .tempFullName(this.getTempFullName())
                .tempGender(this.getTempGender())
                .tempTimezone(this.getTempTimezone())
                .tempLatitude(this.getTempLatitude())
                .tempLongitude(this.getTempLongitude())
                .isEditMode(this.getIsEditMode())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .deleted(this.isDeleted())
                .build();
    }

    public static BotSessionEntity map2Entity(BotSession domain) {
        final var entity = new BotSessionEntity();
        entity.setId(domain.getId());
        entity.setChatId(domain.getChatId());
        entity.setStep(domain.getStep());
        entity.setTempYear(domain.getTempYear());
        entity.setTempMonth(domain.getTempMonth());
        entity.setTempDay(domain.getTempDay());
        entity.setTempFullName(domain.getTempFullName());
        entity.setTempGender(domain.getTempGender());
        entity.setTempTimezone(domain.getTempTimezone());
        entity.setTempLatitude(domain.getTempLatitude());
        entity.setTempLongitude(domain.getTempLongitude());
        entity.setIsEditMode(domain.getIsEditMode());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());
        return entity;
    }
}
