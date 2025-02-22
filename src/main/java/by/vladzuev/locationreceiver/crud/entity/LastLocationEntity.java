package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@jakarta.persistence.Entity
@Table(name = "trackers_last_locations")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class LastLocationEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    @ToString.Exclude
    private TrackerEntity tracker;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    private LocationEntity location;
}