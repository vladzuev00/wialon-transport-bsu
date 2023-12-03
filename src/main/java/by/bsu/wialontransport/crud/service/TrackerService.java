package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMapper;
import by.bsu.wialontransport.crud.repository.TrackerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TrackerService extends AbstractCRUDEncryptingPasswordService<
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
        return super.findUnique(repository -> repository.findByImei(imei));
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user) {
        final Long userId = user.getId();
        return super.findUnPaged((repository, pageable) -> repository.findByUserId(userId, pageable));
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user, final int pageNumber, final int pageSize) {
        final Long userId = user.getId();
        return super.findPaged(
                (repository, pageable) -> repository.findByUserId(userId, pageable), pageNumber, pageSize
        );
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user,
                                    final int pageNumber,
                                    final int pageSize,
                                    final Comparator<Tracker> comparator) {
        final List<Tracker> foundTrackers = this.findByUser(user, pageNumber, pageSize);
        foundTrackers.sort(comparator);
        return foundTrackers;
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByIdWithUser(final Long id) {
        return super.findUnique(repository -> repository.findByIdWithUser(id));
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByPhoneNumber(final String phoneNumber) {
        return super.findUnique(repository -> repository.findByPhoneNumber(phoneNumber));
    }
}
