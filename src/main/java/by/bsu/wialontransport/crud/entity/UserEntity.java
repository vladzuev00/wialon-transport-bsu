package by.bsu.wialontransport.crud.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
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

    @Enumerated(STRING)
    @Column(name = "role")
    @Type(type = "pgsql_enum")
    private Role role;

    @Builder
    public UserEntity(final Long id, final String email, final String password, final Role role) {
        super(password);
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public enum Role {
        USER, ADMIN
    }
}
