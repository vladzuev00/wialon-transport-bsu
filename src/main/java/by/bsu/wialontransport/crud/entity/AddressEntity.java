package by.bsu.wialontransport.crud.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class AddressEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "addresses_id_seq")
    @SequenceGenerator(name = "addresses_id_seq", sequenceName = "addresses_id_seq")
    private Long id;

    @Column(name = "boundaries")
    private Geometry boundaries;

    @Column(name = "centerLatitude")
    private double centerLatitude;

    @Column(name = "centerLongitude")
    private double centerLongitude;

    @Column(name = "cityName")
    private String cityName;

    @Column(name = "countryName")
    private String countryName;
}
