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
}
