package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.Timezone;
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
@Table(name = "timezones", schema = "bot_core", indexes = {
        @Index(name = "idx_tz_country", columnList = "country_code")
})
@SQLRestriction(value = " deleted = false")
public class TimezoneEntity extends FullAuditableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String zoneName;

    @Column(length = 3)
    private String countryCode;

    private Integer utcOffsetSeconds;

    @Column(nullable = false)
    private Boolean isActive;

    public static Timezone map2Domain(TimezoneEntity entity) {
        return Timezone.builder()
                .id(entity.getId())
                .zoneName(entity.getZoneName())
                .countryCode(entity.getCountryCode())
                .utcOffsetSeconds(entity.getUtcOffsetSeconds())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deleted(entity.isDeleted())
                .build();
    }

    public static TimezoneEntity map2Entity(Timezone timeZone) {
        return TimezoneEntity
                .builder()
                .zoneName(timeZone.getZoneName())
                .countryCode(timeZone.getCountryCode())
                .utcOffsetSeconds(timeZone.getUtcOffsetSeconds())
                .isActive(timeZone.getIsActive())
                .build();
    }
}