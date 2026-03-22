package howdy.lab.birthcounterbot.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TgUser {
    private Long id;
    private Long chatId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean premium;
    private Boolean canJoinGroups;
    private String languageCode;
    private Boolean bot;
    private Integer status;
    private Long appUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
