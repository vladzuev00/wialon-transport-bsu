package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trackers")
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class TrackerEntity extends EntityWithPassword<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "imei")
    private String imei;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "mileage_id")
    @ToString.Exclude
    private TrackerMileageEntity mileage;

    @Builder
    public TrackerEntity(final Long id,
                         final String imei,
                         final String password,
                         final String phoneNumber,
                         final UserEntity user,
                         final TrackerMileageEntity mileage) {
        super(password);
        this.id = id;
        this.imei = imei;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.mileage = mileage;
    }
}
