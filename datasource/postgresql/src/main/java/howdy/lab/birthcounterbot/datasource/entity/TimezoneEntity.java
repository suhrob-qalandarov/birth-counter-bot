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

    public Timezone map2Domain() {
        return Timezone.builder()
                .id(this.getId())
                .zoneName(this.getZoneName())
                .countryCode(this.getCountryCode())
                .utcOffsetSeconds(this.getUtcOffsetSeconds())
                .isActive(this.getIsActive())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .deleted(this.isDeleted())
                .build();
    }

    public static TimezoneEntity map2Domain(Timezone domain) {
        return TimezoneEntity.builder()
                .id(domain.getId())
                .zoneName(domain.getZoneName())
                .countryCode(domain.getCountryCode())
                .utcOffsetSeconds(domain.getUtcOffsetSeconds())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deleted(domain.isDeleted())
                .build();
    }
}