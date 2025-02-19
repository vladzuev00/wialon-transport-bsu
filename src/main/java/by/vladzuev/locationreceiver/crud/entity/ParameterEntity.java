package by.vladzuev.locationreceiver.crud.entity;

import by.vladzuev.locationreceiver.model.ParameterType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;
import static org.hibernate.type.SqlTypes.NAMED_ENUM;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parameters")
public class ParameterEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "parameters_id_seq")
    @SequenceGenerator(name = "parameters_id_seq", sequenceName = "parameters_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "type")
    @JdbcTypeCode(NAMED_ENUM)
    private ParameterType type;

    @Column(name = "value")
    private String value;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "data_id")
    private LocationEntity location;
}
