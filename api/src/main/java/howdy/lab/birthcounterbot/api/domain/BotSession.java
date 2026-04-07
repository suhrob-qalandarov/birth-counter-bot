package howdy.lab.birthcounterbot.api.domain;

import howdy.lab.birthcounterbot.api.domain.audit.FullAuditableResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BotSession extends FullAuditableResult {
    private Long id;
    private Long chatId;
    private String step;
    private Integer tempYear;
    private Integer tempMonth;
    private Integer tempDay;
    private String tempFullName;
    private String tempGender;
    private String tempTimezone;
}
