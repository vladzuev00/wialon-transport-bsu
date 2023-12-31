package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMapper;
import by.bsu.wialontransport.crud.repository.TrackerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.data.domain.Pageable.unpaged;

@Service
public class TrackerService extends CRUDEncryptingPasswordService<
        Long,
        TrackerEntity,
        Tracker,
        TrackerMapper,
        TrackerRepository
        > {

    public TrackerService(final TrackerMapper mapper,
                          final TrackerRepository repository,
                          final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository, passwordEncoder);
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByImei(final String imei) {
        return findUnique(repository -> repository.findByImei(imei));
    }

    @Transactional(readOnly = true)
    public Page<Tracker> findByUserOrderedByImei(final User user) {
        return findByUserOrderedByImei(user, unpaged());
    }

    @Transactional(readOnly = true)
    public Page<Tracker> findByUserOrderedByImei(final User user, final Pageable pageable) {
        return findDtoPage(repository -> repository.findByUserIdOrderedByImei(user.getId(), pageable));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByIdFetchingUser(final Long id) {
        return findUnique(repository -> repository.findByIdFetchingUser(id));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByPhoneNumber(final String phoneNumber) {
        return findUnique(repository -> repository.findByPhoneNumber(phoneNumber));
    }
}
