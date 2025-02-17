package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@jakarta.persistence.Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CityEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "addresses_id_seq")
    @SequenceGenerator(name = "addresses_id_seq", sequenceName = "addresses_id_seq")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private AddressEntity address;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "searching_cities_process_id")
    @ToString.Exclude
    private SearchingCitiesProcessEntity searchingCitiesProcess;
}
