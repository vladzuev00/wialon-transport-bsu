package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "trackers")
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

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "mileage_id")
    private MileageEntity mileage;

    @Builder
    public TrackerEntity(final Long id,
                         final String imei,
                         final String password,
                         final String phoneNumber,
                         final UserEntity user,
                         final MileageEntity mileage) {
        super(password);
        this.id = id;
        this.imei = imei;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.mileage = mileage;
    }
}
