package by.bsu.wialontransport.crud.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import static java.util.Arrays.stream;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@javax.persistence.Entity
@Table(name = "parameters")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ParameterEntity extends Entity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "parameters_id_seq")
    @SequenceGenerator(name = "parameters_id_seq", sequenceName = "parameters_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "type")
    @org.hibernate.annotations.Type(type = "pgsql_enum")
    private Type type;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "data_id")
    @ToString.Exclude
    private DataEntity data;

    @RequiredArgsConstructor
    public enum Type {
        INTEGER((byte) 1), DOUBLE((byte) 2), STRING((byte) 3);

        @Getter
        private final byte value;

        //TODO: test
        public static Type findByValue(final byte value) {
            return stream(values())
                    .filter(type -> type.value == value)
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException(
                                    "There is no type with value '%d'".formatted(value)
                            )
                    );
        }
    }
}
