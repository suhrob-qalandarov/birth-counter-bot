package howdy.lab.birthcounterbot.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import howdy.lab.birthcounterbot.api.enums.EGender;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BirthRecord {
    private Long id;
    private TgUser tgUser;
    private String fullName;
    private LocalDate birthDate;
    private EGender gender;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
