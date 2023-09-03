package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tracker_mileages")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class TrackerMileageEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "urban")
    private double urban;

    @Column(name = "country")
    private double country;
}
