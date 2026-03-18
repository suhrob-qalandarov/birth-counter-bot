package howdy.lab.birthcounterbot.datasource.entity;

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
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "zone_name", unique = true, nullable = false)
    private String zoneName;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Column(name = "utc_offset_seconds")
    private Integer utcOffsetSeconds;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}