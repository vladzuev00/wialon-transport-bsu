package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.ExtendedDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtendedDataRepository extends JpaRepository<ExtendedDataEntity, Long> {
    
}
