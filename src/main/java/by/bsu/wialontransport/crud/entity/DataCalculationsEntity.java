package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tracker_last_data_calculations")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class DataCalculationsEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gps_odometer")
    private double gpsOdometer;

    @Column(name = "ignition_on")
    private boolean ignitionOn;

    @Column(name = "engine_on_duration_seconds")
    private long engineOnDurationSeconds;

    @Column(name = "acceleration")
    private double acceleration;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "data_id")
    @ToString.Exclude
    private DataEntity data;
}
