package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.UserDomain;
import howdy.lab.birthcounterbot.api.enums.EGender;
import howdy.lab.birthcounterbot.api.enums.ERole;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "app_users", schema = "bot_core")
public class UserEntity extends FullAuditableEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", schema = "bot_core", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", schema = "bot_core",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ERole> roles;

    @Column(name = "referral_code", unique = true)
    private UUID referralCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(nullable = false)
    private BigDecimal targetScore;

    public UserDomain map() {
        return UserDomain.builder()
                .id(this.getId())
                .username(this.getUsername())
                .password(this.getPassword())
                .firstname(this.getFirstname())
                .lastname(this.getLastname())
                .fullName(this.getFullName())
                .roles(this.getRoles())
                .referralCode(this.getReferralCode())
                .gender(this.getGender())
                .build();
    }

    public UserDomain mapFull() {
        return UserDomain.builder()
                .id(this.getId())
                .username(this.getUsername())
                .password(this.getPassword())
                .firstname(this.getFirstname())
                .lastname(this.getLastname())
                .fullName(this.getFullName())
                .roles(this.getRoles())
                .referralCode(this.getReferralCode())
                .gender(this.getGender())
                .build();
    }

    public static UserEntity map(UserDomain domain) {
        final var entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setFirstname(domain.getFirstname());
        entity.setLastname(domain.getLastname());
        entity.setFullName(domain.getFullName());
        entity.setRoles(domain.getRoles());
        entity.setReferralCode(domain.getReferralCode());
        entity.setGender(domain.getGender());

        return entity;
    }
}
