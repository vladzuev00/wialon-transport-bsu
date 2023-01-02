package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
