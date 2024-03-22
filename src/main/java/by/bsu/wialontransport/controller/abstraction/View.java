package by.bsu.wialontransport.controller.abstraction;

import java.util.Optional;

public interface View<ID> {
    Optional<ID> findId();
}
