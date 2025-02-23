package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "last_locations")
public class LastLocationEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;
}