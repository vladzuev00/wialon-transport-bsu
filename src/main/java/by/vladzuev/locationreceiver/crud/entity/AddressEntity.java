package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class AddressEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "addresses_id_seq")
    @SequenceGenerator(name = "addresses_id_seq", sequenceName = "addresses_id_seq")
    private Long id;

    @Column(name = "bounding_box")
    private Geometry boundingBox;

    @Column(name = "center")
    private Point center;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "geometry")
    private Geometry geometry;
}
