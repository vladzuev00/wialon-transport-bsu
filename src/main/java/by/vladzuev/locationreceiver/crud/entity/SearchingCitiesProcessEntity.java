package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;

import static jakarta.persistence.GenerationType.IDENTITY;

//TODO: remove
@jakarta.persistence.Entity
@Table(name = "searching_cities_processes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class SearchingCitiesProcessEntity extends by.vladzuev.locationreceiver.crud.entity.Entity<Long> {

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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private Status status;

    public enum Status {
        HANDLING, SUCCESS, ERROR
    }
}
