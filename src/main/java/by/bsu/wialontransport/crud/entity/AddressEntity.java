package by.bsu.wialontransport.crud.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

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

    @Column(name = "bounding_box")
    private Geometry boundingBox;

    @Column(name = "center")
    private Point center;

    @Column(name = "cityName")
    private String cityName;

    @Column(name = "countryName")
    private String countryName;
}
