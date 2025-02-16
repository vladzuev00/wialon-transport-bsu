package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.type.SqlTypes.NAMED_ENUM;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class UserEntity extends EntityWithPassword<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(STRING)
    @JdbcTypeCode(NAMED_ENUM)
    private Role role;

    @Builder
    public UserEntity(final Long id, final String email, final String password, final Role role) {
        super(password);
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public enum Role implements GrantedAuthority {
        USER, ADMIN;

        //TODO: test
        @Override
        public String getAuthority() {
            return name();
        }
    }
}
