package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.crud.mapper.TrackerMapper;
import by.vladzuev.locationreceiver.crud.repository.TrackerRepository;
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
        return findUniqueDto(repository -> repository.findByImei(imei));
    }

    @Transactional(readOnly = true)
    public Page<Tracker> findByUserOrderedByImei(final User user) {
        return findByUserOrderedByImei(user, unpaged());
    }

    @Transactional(readOnly = true)
    public Page<Tracker> findByUserOrderedByImei(final User user, final Pageable pageable) {
        return findDtoPage(repository -> repository.findByUserIdOrderingByImei(user.getId(), pageable));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByIdFetchingUser(final Long id) {
        return findUniqueDto(repository -> repository.findByIdFetchingUser(id));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByPhoneNumber(final String phoneNumber) {
        return findUniqueDto(repository -> repository.findByPhoneNumber(phoneNumber));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByIdFetchingMileage(final Long id) {
        return findUniqueDto(repository -> repository.findByIdFetchingMileage(id));
    }

    //TODO: test
    @Transactional(readOnly = true)
    public boolean isExistByImei(final String imei) {
        return findBoolean(repository -> repository.existsByImei(imei));
    }

    //TODO: test
    @Transactional(readOnly = true)
    public boolean isExistByPhoneNumber(final String phoneNumber) {
        return findBoolean(repository -> repository.existsByPhoneNumber(phoneNumber));
    }
}
