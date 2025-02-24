package by.vladzuev.locationreceiver.crud.entity;

import by.vladzuev.locationreceiver.crud.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.type.SqlTypes.NAMED_ENUM;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
@ToString(callSuper = true)
public class UserEntity extends SecuredEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Enumerated(STRING)
    @Column(name = "role")
    @JdbcTypeCode(NAMED_ENUM)
    private UserRole role;

    @Builder
    public UserEntity(final Long id, final String email, final String password, final UserRole role) {
        super(password);
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
