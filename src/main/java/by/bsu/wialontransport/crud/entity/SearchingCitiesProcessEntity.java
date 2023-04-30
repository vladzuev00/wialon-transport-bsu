package by.bsu.wialontransport.crud.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "searching_cities_processes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class SearchingCitiesProcessEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bounds")
    private Geometry bounds;

    @Column(name = "search_step")
    private double searchStep;

    @Column(name = "total_points")
    private long totalPoints;

    @Column(name = "handled_points")
    private long handledPoints;
}
