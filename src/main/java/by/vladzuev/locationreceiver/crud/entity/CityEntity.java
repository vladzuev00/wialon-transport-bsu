package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class CityEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "cities_id_seq")
    @SequenceGenerator(name = "cities_id_seq", sequenceName = "cities_id_seq")
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
