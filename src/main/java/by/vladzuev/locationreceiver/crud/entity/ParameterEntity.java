package by.vladzuev.locationreceiver.crud.entity;

import by.vladzuev.locationreceiver.model.ParameterType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@jakarta.persistence.Entity
@Table(name = "parameters")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ParameterEntity extends by.vladzuev.locationreceiver.crud.entity.Entity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "parameters_id_seq")
    @SequenceGenerator(name = "parameters_id_seq", sequenceName = "parameters_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type")
    private ParameterType type;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "data_id")
    @ToString.Exclude
    private DataEntity data;
}
