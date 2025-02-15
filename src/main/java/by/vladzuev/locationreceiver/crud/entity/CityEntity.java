package by.vladzuev.locationreceiver.crud.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@javax.persistence.Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CityEntity extends by.vladzuev.locationreceiver.crud.entity.Entity<Long> {

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
