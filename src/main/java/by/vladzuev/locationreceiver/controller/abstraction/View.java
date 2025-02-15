package by.vladzuev.locationreceiver.controller.abstraction;

import java.util.Optional;

public interface View<ID> {
    Optional<ID> findId();
}
