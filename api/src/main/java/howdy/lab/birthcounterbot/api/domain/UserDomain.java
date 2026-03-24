package howdy.lab.birthcounterbot.api.domain;

import howdy.lab.birthcounterbot.api.domain.audit.FullAuditableResult;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.api.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

/**
 * This class is named using a specific suffix 'Domain' to avoid naming collisions with
 * 'org.springframework.security.core.userdetails.User'.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDomain extends FullAuditableResult {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String firstname;
    private String lastname;
    private Set<ERole> roles;
    private UUID referralCode;
    private EGender gender;
}
