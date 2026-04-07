package howdy.lab.birthcounterbot.datasource.entity;

import howdy.lab.birthcounterbot.api.domain.UserDomain;
import howdy.lab.birthcounterbot.api.enums.ERole;
import howdy.lab.birthcounterbot.datasource.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", schema = "bot_core",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ERole> roles;

    public UserDomain map() {
        return UserDomain.builder()
                .id(this.getId())
                .username(this.getUsername())
                .password(this.getPassword())
                .fullName(this.getFullName())
                .roles(this.getRoles())
                .build();
    }

    public UserDomain mapFull() {
        return UserDomain.builder()
                .id(this.getId())
                .username(this.getUsername())
                .password(this.getPassword())
                .fullName(this.getFullName())
                .roles(this.getRoles())
                .build();
    }

    public static UserEntity map(UserDomain domain) {
        final var entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setFullName(domain.getFullName());
        entity.setRoles(domain.getRoles());

        return entity;
    }
}
