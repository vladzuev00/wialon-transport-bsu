package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;

import static java.lang.Byte.MIN_VALUE;
import static java.util.Arrays.stream;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "parameters")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ParameterEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "extended_data_id")
    private ExtendedDataEntity extendedData;

    public enum Type {
        NOT_DEFINED(MIN_VALUE), INTEGER((byte) 1), DOUBLE((byte) 2), STRING((byte) 3);

        private final byte value;

        Type(final byte value) {
            this.value = value;
        }

        public final byte getValue() {
            return this.value;
        }

        public static Type findByValue(byte value) {
            return stream(Type.values())
                    .filter(type -> type.value == value)
                    .findAny()
                    .orElse(NOT_DEFINED);
        }
    }
}
