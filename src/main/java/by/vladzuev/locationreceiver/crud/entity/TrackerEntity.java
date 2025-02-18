package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trackers")
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class TrackerEntity extends SecuredEntity<Long> {

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
