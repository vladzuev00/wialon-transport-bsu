package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class EntityWithPassword<IdType> extends AbstractEntity<IdType> {

    @Column(name = "encrypted_password")
    private String password;

}
