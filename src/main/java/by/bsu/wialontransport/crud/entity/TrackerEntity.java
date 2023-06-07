package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trackers")
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class TrackerEntity extends AbstractEntityWithPassword<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imei")
    private String imei;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @Builder
    public TrackerEntity(final Long id,
                         final String imei,
                         final String password,
                         final String phoneNumber,
                         final UserEntity user) {
        super(password);
        this.id = id;
        this.imei = imei;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }
}
