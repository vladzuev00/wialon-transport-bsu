package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class SecuredEntity<IdType> extends AbstractEntity<IdType> {

    @Column(name = "encrypted_password")
    private String password;
}
