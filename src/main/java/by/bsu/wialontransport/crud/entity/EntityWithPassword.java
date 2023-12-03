package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class EntityWithPassword<IdType> extends Entity<IdType> {

    @Column(name = "encrypted_password")
    private String password;

}
