package by.bsu.wialontransport.crud.entity;

import lombok.*;

import org.hibernate.annotations.TypeDef;
import org.hibernate.spatial.JTSGeometryType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.InheritanceType.JOINED;

@Entity
@Table(name = "addresses")
@Inheritance(strategy = JOINED)
@TypeDef(
        name = "geometry-type",
        typeClass = JTSGeometryType.class
)
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

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "geometry")
    private Geometry geometry;
}
